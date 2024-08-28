#!/usr/bin/perl

use strict;

####logging details#################
my $log_file="/tmp/datagen_start_stop.log";

####mediation zone details##########
my $mz_home="/eniq/home/dcuser/mz";
my $mzsh_user="mzadmin";
my $mzsh_pass="central1";

sub LOG{
	my @log=@_;
	
	open(FILE,">>$log_file");
	foreach my $log_line (@log){
		my $time = localtime(time);
		chomp($log_line);
		print FILE "$time: $log_line\n";
	}
	close(FILE);
}

sub runCommandAndLog{
	my $command=shift;
	my @result;
	open(CMD,"$command |");
	
	while(<CMD>){
		push(@result,$_);
	}
	
	close(CMD);
	LOG(@result);
	return @result;
}

# main
{
	my $username = getpwuid($<);
	if($username ne "root"){
		print "ERROR:This script must be run as root\n";
		exit(1);
	}
	printf("Initializing Datagen Server, Please wait...\n");
	
	LOG("############Starting to Initialize Datagen Server#############");
	runCommandAndLog("su dcuser -c 'mkdir -p /tmp/OMS_LOGS/ebs/ready'");
	runCommandAndLog("su dcuser -c 'mkdir -p /tmp/OMS_LOGS/ebs/ready1'");
	runCommandAndLog("su dcuser -c 'mkdir -p /tmp/DatagenStore/DVTP/'");
	runCommandAndLog("su dcuser -c 'mkdir -p /tmp/DatagenStore/DVTP/java'");
	runCommandAndLog("su dcuser -c 'cp /eniq/home/dcuser/dvtp_automation_datagen.jar /tmp/DatagenStore/DVTP/'");
	runCommandAndLog("su dcuser -c 'cp /eniq/home/dcuser/java/jdk-7u51-fcs-bin-b13-solaris-i586-18_dec_2013.tar /tmp/DatagenStore/DVTP/java/'");
	runCommandAndLog("su dcuser -c 'cd /tmp/DatagenStore/DVTP/java; tar -xvf jdk-7u51-fcs-bin-b13-solaris-i586-18_dec_2013.tar'");
	runCommandAndLog("su dcuser -c 'cd $mz_home/bin; ./launchmzsh.sh $mzsh_user/$mzsh_pass startup platform EC1 2>&1'");
	runCommandAndLog("/etc/init.d/datagen start");
	`su dcuser -c '/eniq/home/dcuser/centralDatagen/CentralDatagen.pl -force >/dev/null 2>&1 &'`;
	LOG("############Datagen Server Initialization Done#############");
	
	printf("Datagen Server Initialization done. Check log at %s\n", $log_file);
	print "Sleeping for 3 minute to check status\n";
	sleep(3*60);
	my $server_status=`su dcuser -c 'perl /eniq/home/dcuser/centralDatagen/DatagenMonitor/monitor.pl'`;
	$server_status=~s/INFO: //;
	printf("Server Status: %s\n", $server_status);
}