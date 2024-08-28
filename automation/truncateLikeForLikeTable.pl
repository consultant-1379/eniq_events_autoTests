#!/usr/bin/perl -C0

#-----------------------------------------------------------
# COPYRIGHT Ericsson Radio Systems  AB 2011
#
# The copyright to the computer program(s) herein is the
# property of ERICSSON RADIO SYSTEMS AB, Sweden. The
# programs may be usedand/or copied only with the written
# permission from ERICSSON RADIO SYSTEMS AB or in accordance
# with the terms and conditions stipulated in the agreement
# contract under which the program(s)have been supplied.
#-----------------------------------------------------------
#-----------------------------------------------------------

use strict;
use Net::Ping;
use Net::FTP;
use Sys::Hostname;
use File::Copy;
use File::Compare;
use File::Path;
use File::Find;
use File::Basename;
use Cwd 'abs_path';
no strict 'refs';

my $packageRunStartTime = "";
my $temp_pwd = `grep -w "DCPassword" /eniq/sw/conf/niq.ini | head -1 | cut -d "=" -f2`;
chomp($temp_pwd);
my $dBPassword = "P"."$temp_pwd";
chomp($dBPassword);
my $password_encrypation = `grep "DCPassword_Encrypted" /eniq/sw/conf/niq.ini | cut -d "=" -f2`;
chomp($password_encrypation);
if ($password_encrypation eq 'Y') {
$temp_pwd= `echo $dBPassword |/usr/sfw/bin/openssl enc -base64 -d`;
chomp($temp_pwd); 
$dBPassword = "P"."$temp_pwd";
chomp($dBPassword);
}

my $reservedDataLocation="/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv";
my $pageMiddle = "";
my $fullPage = "";
my $complete = "";
my $result = "";
my $doWrapperUpload=0;

my $trafficLightColour = "green";
my $failure_threshold_red = 0;
my $failure_threshold_orange = 0;
my $traffic_light_display = "false";
my @numberOfTests;

my $listOfTestsToRun = "";
############################################################
# THIS ENV VARIABLE IS NEEDED FOR CRONTAB
$ENV{'SYBASE'}='/eniq/sybase_iq';
$ENV{'IQDIR15'}='/eniq/sybase_iq/IQ-15_2';
$ENV{'CONF_DIR'}='/eniq/sw/conf';
$ENV{'PATH'} = '/eniq/sybase_iq///IQ-15_2/bin64:/usr/bin::/eniq/sw/runtime/nokalva/asn1pjav/solaris.tgt/3.0:/usr/local/bin:/eniq/sw/bin:/eniq/sybase_iq///IQ-15_2/lib64::/eniq/sw/runtime/nokalva/asn1pjav/solaris.tgt/3.0/lib:/eniq/sql_anywhere/lib64
';

# Setup Directory structure

my $BASE_DIR='/eniq/home/dcuser/automation';
my $LOGS_DIR="$BASE_DIR".'/RegressionLogs';
my $AUDIT_DIR="$BASE_DIR".'/audit';
my $HTML_DIR="$BASE_DIR".'/html';
my $TOPOLOGY_DIR="$BASE_DIR".'/topology';
my $DGTOPOLOGY_DIR="$BASE_DIR".'/DataGenTopology';
my $DGWORKFLOWS_DIR="$BASE_DIR".'/DataGenWorkFlows';
#Directory Paths for Glassfish
my $autoDeployPath = "/eniq/glassfish/glassfish/glassfish/domains/domain1/autodeploy/";
my $glassfishConfigPath = "/eniq/glassfish/glassfish/glassfish/domains/domain1/config/";
my $applicationsFolder = "/eniq/glassfish/glassfish3/glassfish/domains/domain1/applications/";

# Log Settings

my $audit_log = "$AUDIT_DIR";
my $audit_path = "";
my $html_results;
my $html_path;
my @seleniumResultsFiles=();
my $verifylogs_results = "";
my $ltees_results = "";
my $kpi_results = "";
my $ltees_counterlog = "";
my $aacHTML = "";
my $aacLog = "";

my $event_err_raw_csv = "";
my $event_suc_raw_csv = "";

my $AdminUIUsername = "admin";
my $AdminUIPassword = "admin";


#----------------------------------------------------------
# Set path for Sybase iSQL
#----------------------------------------------------------

my $ISQL;

my $version_file="/eniq/sybase_iq/version/iq_version";

if ( ! -f "$version_file" )
{
  print "Could not access file '$version_file'.
It is required to set 'isql' path. Could not able to set path for 'isql'.
Check for issue. Script will exit at this line";
 exit;
}


open(INPUT,$version_file);
my @file_syb=<INPUT>;
close(INPUT);

my @line_syb=grep(/^VERSION/,@file_syb);
my @syb_ver=split(':',$line_syb[0]);

if ( $syb_ver[2] >= 16 )
{
   $ISQL="/eniq/sybase_iq/OCS-15_0/bin/isql";
}
else
{
   $ISQL="/eniq/sybase_iq/IQ-15_2/bin64/iqisql";
}

sub sqlSelect{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 80 -b << EOF $sql |");
	my @output=<SEL>;
	chomp(@output);
	close(SEL);
	my @result=();
	my $colsItem=lc($statement);
	$colsItem=~s/select //;
	$colsItem=~s/ from.*//;
	my $colsCount = @{[$colsItem =~ /(,)/g]} +1;
	my $lineCount=0;
	my @buffer=();
	foreach my $line (@output){
		$_=$line;
		next if(/affected/);
		$line=~s/\n//g;
		$line =~ s/^\s+//; #remove leading spaces
		$line =~ s/\s+$//; #remove trailing spaces
		if($line ne ""){
			if($line !~ m/ / && $colsCount>1 && $lineCount<$colsCount-1){
				push(@buffer,$line);
			}else{
				for(my $i=0;$i<$lineCount;$i++){
					if($buffer[$i] ne ""){
						$line="$buffer[$i] $line";
					}
				}
				while ($line =~ m/  /){
					$line=~s/  / /g;
				}
				@buffer=();
				push @result, $line;
			}
			$lineCount++;
			if($lineCount==$colsCount){
				$lineCount=0;
			}
		}
	}
	return @result;
}

my @all_truncate_queries;


my $file_list="/eniq/home/dcuser/automation/atom_db/config_files/like4liketables.txt";
open(INPUT,"$file_list");
my @input=<INPUT>;
chomp(@input);
close(INPUT);
my @all_select_queries;

foreach my $input (@input) {
	my $tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like '$input%'";
	my $select_query = "select count(*) from $input";
	#print "$tables_query";	

	my @truncate_queries = sqlSelect($tables_query);
	push(@all_truncate_queries,@truncate_queries);
	push(@all_select_queries,$select_query);
	}


foreach my $input (@all_truncate_queries) {
	print $input;
        my $sql="\n$input\ngo\nEOF";
        open(SEL,"$ISQL -Udc -$dBPassword-h0 -Sdwhdb -w 50 -b << EOF $sql |");
        my @output=<SEL>;
	print ("@output");
        close(SEL);
 }

foreach my $input (@all_select_queries){
	print "$input ";
	my $sql="\n$input\ngo\nEOF";
        open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
        my @output=<SEL>;
        print ("@output");
        close(SEL);
 }


