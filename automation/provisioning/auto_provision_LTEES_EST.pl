#!/usr/bin/perl

use strict;
use warnings;


sub main { 
	open (logFile, '>',"/eniq/sw/log/autoProvision.txt") or die "ERROR: Could not create logfile";
	
	#autoPreProvision();
	autoProvisionLTEES();
	autoUpdateProperties();
	autoProvisionCTR();
	close logFile;
	checkLogsForFailures();
	}


sub autoProvisionLTEES{
	print logFile "\n----Start of LTEES Provisioning----\n";
	print "\nProvisioning LTE ES";
	my $result = `/eniq/home/dcuser/automation/provisioning/provisionLTEES_Stream.sh; echo LTEES exit code is : $?`;
	print logFile "$result";
	print logFile "\n\n----End of LTEES Provisioning----\n";
}

sub autoUpdateProperties{
	print logFile "\n----Start of CTRS Pre Steps----\n";
	print "\nRunning CTRS Pre steps";
	system("cp /eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop /eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop.bkup");
	open (CTRSIN, '<', "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop.bkup") or die "Can't open";
	open (CTRSout, '>', "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop") or die "Can't open";
	
	print logFile "\nUpdating /eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop";
	while( <CTRSIN> ) {
		if($_ =~ m/ipfdn.mapping.source.db=true/){
			print CTRSout "ipfdn.mapping.source.db=false\n";
		} else {
			print CTRSout $_;
		}
	}

	close (CTRSIN); 
	close (CTRSout); 
	system("rm -rf /eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop.bkup");
	
	my $ecStart =`ssh dcuser\@ec_1 /eniq/mediation_sw/mediation_gw/bin/mzsh startup EC1`;
	print logFile "\n$ecStart";
	
	print logFile "\n----End of of CTRS Pre Steps----\n";
}

sub autoProvisionCTR{
	print logFile "\n----Start of CTRS Provisioning----\n";
	print "\nProvisioning CTRS";
	
	my $createResult = `/eniq/home/dcuser/automation/provisioning/provisionCTR.sh; echo CTRS exit code is : $?`;
	print logFile "$createResult";
	my $startListen = `/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctr -e startlisten`;
	print logFile "$startListen";
	my $startOutput = `/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctr -e startoutput`;
	print logFile "$startOutput";
	my $status = `/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctr -e status`;
	print logFile "$status";
		
	print logFile "\n\n----End of CTRS Provisioning----\n";
}

sub checkLogsForFailures{
	printf "\n\nVerifying.";
	open (MYFILE, '/eniq/sw/log/autoProvision.txt') or die "ERROR: Cannot open datloading logfile";
	while (<MYFILE>) {
			chomp;
			if($_ =~ m/exit code is :/ && $_ !=~ m/0/){
			#	print logFile "\nProvisoning feature failed.";
				print "\nProvisoning feature failed.";
			#	print logFile "\n $_.";
				print "\n $_.";
				printf "\n\n Provisoning failed. Check logs";
				exit 1;
				die;
			}
			printf "\n\nAuto Provisoning successfull.";
			exit 0;
	}
}


main();
