#!/usr/bin/perl

require 5.6.1;
use strict;
use warnings;
use Getopt::Std;
use File::Basename;
use POSIX;

my @eventNames = (
	"ATTACH",
	"ACTIVATE",
	"RAU",
	"ISRAU",
	"DEACTIVATE",
	"L_ATTACH",
	"L_DETACH",
	"L_HANDOVER",
	"L_TAU",
	"L_DEDICATED_BEARER_ACTIVATE",
	"L_DEDICATED_BEARER_DEACTIVATE",
	"L_PDN_CONNECT",
	"L_PDN_DISCONNECT",
	"L_SERVICE_REQUEST",
	"DETACH",
	"SERVICE_REQUEST"
);

#----------------------------------------------------------
# Set path for Sybase iSQL
#----------------------------------------------------------

my $ISQL;

my $version_file="/eniq/sybase_iq/version/iq_version";

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


sub fileTime{
	my $file = shift;
	my $time;
	$time = (stat($file))[9] or $time = 0;
	chomp($time);
	return $time;
}

sub ls{
	my ($args, $pathToList, $pos);
	$pathToList = shift;
	$args = shift;
	$pos = shift;
	opendir DIR, $pathToList;
	my @list = readdir(DIR);

	chomp(@list);
	closedir(DIR);
	@list = grep(!/^\.|\.\.$/, @list);#Remove current (.) and previous (..) dirs

	@list = sort { lc($a) cmp lc($b) } @list;#alphabetical

	if($args =~ m/d/){#directories only
		my @dirs = ();
		foreach my $dir (@list){
			if( -d "$pathToList/$dir" ){
				push( @dirs, $dir);
			}
		}
		@list = @dirs;
	}
	if($args =~ m/f/){#files only
		my @files = ();
		foreach my $file (@list){
			if( ! -d "$pathToList/$file" ){
				push( @files, $file);
			}
		}
		@list = @files;
	}
	if($args =~ m/t/){#time (newest first)
		my %hash ;
		%hash = map { $_ => fileTime($pathToList."/$_") } @list;
		@list = ();
		foreach my $value (sort {$hash{$b} cmp $hash{$a} } keys %hash){
			push( @list, $value);
		}
	}
	if($args =~ m/r/){#reverse alphabetical or time
		@list = reverse(@list)
	}
	if($args =~ m/l/){#full path of each file
		@list = map( $pathToList."/$_", @list);
	}
	if( defined($pos) && $pos =~ m/-?\d+/){
		return $list[$pos];
	}
	return @list;
}

sub getEventId{
	my $eventName=shift;
	for(my $i=0;$i<@eventNames;$i++){
		if($eventName eq $eventNames[$i]){
			return $i;
		}
	}
	return -1;
}
sub getMinsPastHour{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time);
	return $min;
}

sub executeThisQuiet
{
	my $command = shift;
	open(CMD,"$command |");
	my @cmd=<CMD>;
	close(CMD);
	return @cmd;
}

sub grepFile{
	my $pattern = shift;
	my $file = shift;
	my $arg = shift;

	open( FILE, $file );
	my @contents = <FILE>;
	close( FILE );

	if($arg =~ /i/){
		return grep(/$pattern/i, @contents);
	}
	return grep(/$pattern/, @contents);
}
sub getUtcTimeFromString{
	my $toSplit=shift;
	$toSplit=~s/(\d{4})(\d{2})(\d{2}).(\d{2})(\d{2})([\+|\-])(\d{2})(\d{2})/$1,$2,$3,$4,$5,$6,$7,$8/;
	my @params=split(/,/,$toSplit);
	##mktime(sec,      min,        hour,      mday,      mon,        year)
	my $utcTime=mktime(0,$params[4], $params[3],$params[2],$params[1]-1, $params[0]-1900);
	my $tzOffsetSeconds=($params[6]*3600)+($params[7]*60);
	if($params[5] eq "+"){
		$utcTime-=$tzOffsetSeconds;
	}elsif($params[5] eq "-"){
		$utcTime+=$tzOffsetSeconds;
	}
	return $utcTime;
}
sub getDbTimeString{
	my $utcTime=shift;
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=gmtime($utcTime);
	return sprintf "%4d-%02d-%02d %02d:%02d", $year+1900,$mon+1,$mday,$hour,$min;
}
sub sqlSelect{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -b<< EOF $sql |");
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
	foreach my $line (@output)
	{
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
######MAIN
my $dirname = dirname(__FILE__);
my $sgsnName="";
getopts('f:d:n:');
our($opt_f);
our($opt_d);
our($opt_n);
my @inputfiles = ();
if(defined($opt_f)){
	push(@inputfiles,$opt_f);
}
if(defined($opt_n) && $opt_n ne ""){
	$sgsnName=$opt_n;
}

if(defined($opt_d)){
	if(!-d  $opt_d){
		print "ERROR:Directory $opt_d does not exist\n";
		exit(1);
	}else{
		my @files=ls("$opt_d","t");
		foreach(@files){
			push(@inputfiles,$opt_d."/".$_);
		}
	}
}
if(@inputfiles==0){
	print "USAGE: $0 -f <input file>|-d <input directory> [-n <SGSN/MME NAME]\n";
}else{
	my $exitCode=0;
	foreach my $inputfile(@inputfiles){
		if(!-e $inputfile){
			print "ERROR:$inputfile does not exist\n";
		}elsif($inputfile!~m/.*A\d{8}.\d{4}[\+|\-]\d{4}-\d{8}.\d{4}[\+|\-]\d{4}.*/){
			print "ERROR:The file name '$inputfile' is not in the right format\n";
		}else{
			(my $toSplit=$inputfile)=~s/.*\///;
			$toSplit=~s/(.*)A(\d{8}.\d{4}[\+|\-]\d{4})-(\d{8}.\d{4}[\+|\-]\d{4}).*/$1,$2,$3/;
			my @params=split(/,/,$toSplit);
			if($sgsnName eq ""){
				($sgsnName=$params[0])=~s/_//;
			}
			my $startTimeUtc = getUtcTimeFromString($params[1]);
			my $endTimeUtc = getUtcTimeFromString($params[2]);
			my $startTime=getDbTimeString($startTimeUtc);
			my $endTime=getDbTimeString($endTimeUtc);
			
			my $diffBetweenFileAndStartOfHour=time-((getMinsPastHour()+1)*60)-$startTimeUtc;
			my $diffBetweenFileAndCurrentTime=time-$startTimeUtc;
			my $successesHaveBeenLoaded=1;
			if($diffBetweenFileAndStartOfHour<0){
				$successesHaveBeenLoaded=0;
			}
			if($diffBetweenFileAndCurrentTime<=600){
				print "INFO:The file $inputfile is too recent to accurately count events. The File should be at least 5 minutes old\n";
			}elsif($diffBetweenFileAndStartOfHour<=3660 && getMinsPastHour()<10){
				print "INFO:Events cannot be accurately calculated for the file $inputfile because successes are currently loading into the database. Wait until 10 minutes past the hour for successes to load\n";
			}else{
				my @result=();
				if($successesHaveBeenLoaded){
					@result=executeThisQuiet("/usr/bin/perl $dirname/parse_ebm_log_generic_mod.pl -f $inputfile -s");
				}else{
					print "INFO:Successful events have not been loaded for the file $inputfile yet. The results will only count failed events\n";
					@result=executeThisQuiet("/usr/bin/perl $dirname/parse_ebm_log_generic_mod.pl -f $inputfile -s -u");
				}
				print "------------------------------------------\n";
				foreach(@result){
					print $_;
				}
				print "------------------------------------------\n";
				my @totalLine=grep(/Total/, @result);
				my @ffvLine=grep(/FFV=/, @result);
				(my $ffv=$ffvLine[0])=~s/FFV=//;
				chomp($ffv);
				my @fivLine=grep(/FIV=/, @result);
				(my $fiv=$fivLine[0])=~s/FIV=//;
				chomp($fiv);
				(my $total=$totalLine[0])=~s/Total number of events=//;
				chomp($total);
				my @event_types=grep(!/unknown/,grep(/Number of /, @result));
				
				my $exitCode=0;
				my $all2g3g=1;
				my $all4g=1;
				foreach my $event_line(@event_types){
					if($event_line=~m/L_/){
						$all2g3g=0;
					}else{
						$all4g=0;
					}
				}
				my $table="";
				if($all2g3g==1){
					$table="event_e_sgeh_raw";
				}else{
					$table="event_e_lte_raw";
				}
				if($table ne ""){
					my $sql="select count(*) from $table where event_time>='$startTime' and event_time<'$endTime' and ne_version='$ffv,$fiv'";
					my $fullSql=$sql;
					if($sgsnName ne ""){
						$fullSql.=" and event_source_name='$sgsnName'";
					}
					print "INFO:Running query:$fullSql\n";
					my @cols=sqlSelect($fullSql);
					if($cols[0]==$total){
						print "PASS:Expected $total events in total, found $cols[0]\n";
					}else{
						print "FAIL:Expected $total events in total, found $cols[0]\n";
					}
				}
				if(!$exitCode){
					print "INFO:Because the total event check failed, individual event types will now be checked\n";
					foreach my $event_line(@event_types){
						$event_line=~s/Number of\s+//;
						chomp($event_line);
						my @event=split(/=/,$event_line);
						my $table="event_e_sgeh_raw";
						if($event[0]=~m/L_/){
							$table="event_e_lte_raw";
						}
						my $sql="select count(*) from $table where event_time>='$startTime' and event_time<'$endTime' and ne_version='$ffv,$fiv' and event_id=".getEventId($event[0]);
						my $fullSql=$sql;
						if($sgsnName ne ""){
							$fullSql.=" and event_source_name='$sgsnName'";
						}
						print "INFO:Running query:$fullSql\n";
						
						my @cols=sqlSelect($fullSql);
						if($cols[0]==$event[1]){
							print "PASS:Expected $event[1] events, found $cols[0]\n";
						}elsif($cols[0]>$event[1]){
							print "FAIL:Expected $event[1] events, found $cols[0]\n";
							$exitCode=1;
						}elsif($cols[0]<$event[1]){
							print "FAIL:Expected $event[1] events, found $cols[0] here\n";
							print "Running:$sql\n";
							$exitCode=1;
							@cols=sqlSelect($sql);
							if($cols[0]>=$event[1]){
								print "INFO:Found $cols[0] events when event_source_name was excluded from query\n";
							}
						}
					}
				}
			}
		}
	}
	exit($exitCode);
}
exit(3);
