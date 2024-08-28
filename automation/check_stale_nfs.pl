#!/usr/bin/perl

##
# files mounted on /net of CEP blade
# occasionally loose connection and cause Stale NFS file handle
# error. This script remounts those mount points.
#
use strict;

sub executeThisQuiet
{
	my $command = shift;
	open(CMD,"$command |");
	my @cmd=<CMD>;
	close(CMD);
	return @cmd;
}

#
#MAIN
#

{
	my @running_instances=executeThisQuiet("ps -ef | grep check_stale_nfs.pl | grep -v grep");
	if(@running_instances > 2){
		exit(0);
	}
	
	my @mount_points = ("/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen","/net/atclvm559.athtem.eei.ericsson.se/tmp/DatagenStore");
	my @nfs_shares = ("atclvm559.athtem.eei.ericsson.se:/tmp/CentralDatagen","atclvm559.athtem.eei.ericsson.se:/tmp/DatagenStore");
	#open(FILE, ">>/tmp/check_stale_nfs.log");
	for (my $i = 0; $i < @mount_points; $i++) {
		my @output = executeThisQuiet("ssh dcuser\@cep_med_1 'ls -la ".$mount_points[$i]."'");
		if(grep(/Stale NFS file handle/,@output) || @output < 2){
			executeThisQuiet("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py umount -f ".$mount_points[$i]." 2>/dev/null'");
			executeThisQuiet("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py mount -t nfs -o remount ".$nfs_shares[$i]." ".$mount_points[$i]." 2>/dev/null'");
		}
		
		#print FILE "INFO: @output";
	}
	#close(FILE);
}