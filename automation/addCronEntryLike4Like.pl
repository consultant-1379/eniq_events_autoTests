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
                 print FILE $Cron_entry unless ( $Cron_entry =~ m/LikeForLike/ );
        }
        #print FILE "$min $hr $day $month * /ede/LikeForLike/ede-16.A3-Release/bin POSTPROCESSING START\n";
	$min=$min+1;
	$mon=$mon+1;
	print FILE "$min $hour $mday $mon * /ede/LikeForLike/ede-16.A3-Release/bin/startEdeTool.sh POSTPROCESSING START >/dev/null 2>&1\n";    
	close(FILE);
        executeThisQuiet("crontab /tmp/tmp_cron.$$");
        unlink("/tmp/tmp_cron.$$");
        print "Made the entry " ; 
}
	

	print "EDE process is dead, adding Cron Entry to re-start EDE ";
	updateCron();

