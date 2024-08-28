#!/usr/bin/perl -w

use strict;


#	ejmnqrz
#	This script is used for checking relevant EC processes' status every 5 mins
#	Script will be invoked/stopped when MONITOREC/STOPMONITOREC is applied in Config files 
#	Please contact central regression team if you have any queries

sub info{
	return qq{
	
Invalide arguments used!
Usage: ecChecker.pl [-start] | [-stop]

};
}

sub writeToLog{
	#Not applied yet
	my @cmds = @_;
	my $logFile = "/eniq/home/dcuser/automation/ecSingleMonitor.txt";
	open (FILE, ">>$logFile") or die "Cannot open >>$logFile: $!";
	print FILE @cmds;
	close FILE;
}


sub checkEcProcess{
	my @ecToCheck = @_;
	my $ecToMonitor = join('|', @ecToCheck);
	print ("INFO: Monitor $ecToMonitor with PID $$");
	while (1){
		my @result = `ec status | egrep '$ecToMonitor'`;
		if (grep /not running/i, @result){
			system ("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup @ecToCheck'");
		}
		#Below value concerns with how often the scripts checks the EC processes
		sleep 299;	
	}
}

sub checkRunning{
	my @processAlreadyCreat = `ps -efa |grep 'ecChecker.pl -start'| grep -v grep | grep -v $$ | awk '{print \$2}'`;
	my $numOfProcess = @processAlreadyCreat;
	if (!$numOfProcess ==0)
	{	
		print "INFO: Kill previous child processes @processAlreadyCreat\n";
		kill 9, @processAlreadyCreat;
	}
}


###########################MAIN###########################
{	
	if (@ARGV ==0)
	{
		print info();
		exit(0);
	}
	foreach my $arg (@ARGV){
		
		if ($arg =~m/stop/i)
		{
			my @checker = `ps -efa |grep 'ecChecker.pl -start'| grep -v grep | awk '{print \$2}'`;
			#print "HERE: @checker\n";
			if (@checker){
				foreach my $ecCheckerProcess (@checker){
					print ("INFO: Stop monitoring ECs' status on single blade, kill $ecCheckerProcess\n");
					kill 9, $ecCheckerProcess;
				}
				exit(0);
			}else {
				print ("INFO: No ecChecker process found, script exits\n");
				exit(0);
			}
			
		}elsif ($arg =~m/start/i){
			print "INFO: Sart monitoring ECs' status on single blade\n";
			my @ecs = ();
			my @command=`ls -lrt /eniq/home/dcuser/automation/audit |tail -1 |awk '{print \$9}'`;
			if (grep (/lte[c|h]fa/i, @command) )
			{
				@ecs = ("EC1", "EC_LTEEFA_1", "EC_LTEEFA_2", "EC_LTEEFA_3");
				print "INFO: Monitering @ecs for LTEFA\n";
			}elsif(grep (/uiimprovement/i, @command))
			{
				@ecs = ("EC1", "EC_SGEH_1");
			}elsif (grep (/wcdma/i, @command) )
			{
				@ecs = ("EC1");
				print "INFO: Monitering @ecs for WCDMA\n";
			}elsif (grep (/mss/i, @command) )
			{
				@ecs = ("EC1");
				print "INFO: Monitering @ecs for MSS\n";
			}elsif (grep (/ltees/i, @command) )
			{	
				@ecs = ("EC1", "EC_LTEES_1", "EC_LTEES_2", "EC_LTEES_3", "EC_LTEES_4");
				print "INFO: Monitering @ecs for LTEES\n";
			}else{
				checkRunning();
				print "INFO: The feature seems not using relevant ECs processes\nScript exits\n";
				exit(0);
			}
			#If there is previous child process created, kill them before recreating a new child process

			checkRunning();
			my $child = fork();
			#Child process is created to monitor relevant ECs status
			if ($child == 0){
				checkEcProcess(@ecs);
			}else
				{
				my $running = `ps -p $child`;
				print "\nINFO: Parent $$\nINFO: Monitoring EC processes with below child process\n $running\n";
				}
			}else{
			print info();
		}
	}
}