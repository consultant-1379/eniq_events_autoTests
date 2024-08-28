#!/usr/bin/perl

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

use POSIX qw(strftime);
use Time::Local;
my($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime(time);


sub executeThisQuiet{
        my $command = shift;
        open(CMD,"$command |");
        my @cmd=<CMD>;
        close(CMD);
        return @cmd;
}

sub updateCron{
	#Run EDE from crontab
	my @cron_table = executeThisQuiet("crontab -l");
        open(FILE, ">/tmp/tmp_cron.$$");
        foreach my $Cron_entry (@cron_table){
                 print FILE $Cron_entry unless ( $Cron_entry =~ m/edeRegression/ );
        }
        #print FILE "$min $hr $day $month * /ede/edeRegression/ede-16.A3-Release/bin/startEdeTool.sh POSTPROCESSING START\n";
	$min=$min+1;
	$mon=$mon+1;
	print FILE "$min $hour $mday $mon * /ede/edeRegression/ede-16.A3-Release/bin/startEdeTool.sh POSTPROCESSING START >/dev/null 2>&1\n";    
	close(FILE);
        executeThisQuiet("crontab /tmp/tmp_cron.$$");
        unlink("/tmp/tmp_cron.$$");
}

#Check if EDE process is running
my @procs=`ps -ef | grep edeRegression | grep -v grep | awk '{print \$2}'`;

my $ede_starting = `ls /eniq/home/dcuser/centralDatagen | grep ede_starting_up`;

if ( @procs ){
	#EDE process is running. Make sure it is still processing files by checking that last processed ROP time.
	#If the time since the last processed ROP is too great, then kill the EDE and re-start via the cron

	my $ok=0;
	my @processed_rop = `ls /ede/data/inter/sgeh/4G/temp | grep processed | awk '{print $9}'`;	
	if(@processed_rop)
	{
		if ( $ede_starting ) {
               		 executeThisQuiet("rm /eniq/home/dcuser/ede_starting_up");
         	}	

		chomp @processed_rop;
		$ok=0;
		foreach my $rops (@processed_rop){
		#$rops = substr($rops, 0, -10);
		my $rop_year=substr($rops, 0,4);
		my $rop_month=substr($rops,4,2);
		my $rop_day=substr($rops,6,2);
		my $rop_hour=substr($rops,8,2);
		my $rop_minute=substr($rops,10,2);
		my $rop_date = timelocal(00,$rop_minute,$rop_hour,$rop_day,$rop_month,$rop_year);
		my $now = time(); 	
		if ( $now - $rop_date < 300 ) {
			$ok=1;
		}
		}
	}else{
		#EDE process is running, but no processed ROPS
		#EDE may have just started
		#Do an ls, if all returned values are in future then do nothing
		$ok=1;
		my @all_rops = `ls /ede/data/inter/sgeh/4G/temp | awk '{print $9}'`;
		if(@all_rops){
			if ( $ede_starting ) {
        		        executeThisQuiet("rm /eniq/home/dcuser/ede_starting_up");
        		}
			chomp @all_rops;
			foreach my $rops (@all_rops){
				my $rop_year=substr($rops,0,4);
				my $rop_month=substr($rops,4,2);
				my $rop_day=substr($rops,6,2);
                		my $rop_hour=substr($rops,8,2);
                		my $rop_minute=substr($rops,10,2);
                		my $rop_date = timelocal(00,$rop_minute,$rop_hour,$rop_day,$rop_month,$rop_year);
				my $now = time();
				if ( $now > $rop_date ){
					#ROPS are NOT being processed, need to kill EDE and start EDE again
					$ok=0;	
				}
			}	
		}else{
			my $ede_starting = `ls /eniq/home/dcuser/centralDatagen | grep ede_starting_up`;
			if ( $ede_starting ){
				#Twice this script was run and there were no ROP folders present, need to kill EDE and start again
				print "second time no rop files present\n";
				executeThisQuiet("rm /eniq/home/dcuser/centralDatagen/ede_starting_up");
				$ok=0;
			}else{
				#No ROP folders present, give EDE another chance to create them
				executeThisQuiet("touch /eniq/home/dcuser/centralDatagen/ede_starting_up");
				print "first time no ROP files present\n";
			}	
		}
	}
	
	#Kill Current EDE process
	if ( !$ok ){
		 foreach my $ede_procs (@procs){
       		         executeThisQuiet("kill -9 $ede_procs");
       		 }
		updateCron();
	}	


}else{
	print "EDE process is dead, restart from crontab";
	updateCron();
}