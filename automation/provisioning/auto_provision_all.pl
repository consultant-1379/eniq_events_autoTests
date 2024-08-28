#!/usr/bin/perl

use strict;
use warnings;


sub main { 
	open (logFile, '>',"/eniq/sw/log/autoProvision.txt") or die "ERROR: Could not create logfile";
	
	#autoPreProvision();
	autoProvisionSGEH();
	autoProvisionMSS();
	autoProvisionLTEEFA();
	autoProvisionLTEES();
	autoUpdateProperties();
	autoProvisionCTR();
	autoProvisionCTUM();
	autoProvisionDVTP();
	close logFile;
	checkLogsForFailures();
	}

sub autoPreProvision{

	my $eniqStatus = "/eniq/admin/version/eniq_status";
	open(versionFile, $eniqStatus) or die "ERROR: Could not open Eniq Status";
	while(<versionFile>){
		if($_ =~ m/Events_Shipment_/){
			my @values = split('ENIQ_STATUS ENIQ_Events_Shipment_', $_);
			#$values[1] now contains full shipment number
			if($values[1] =~ /^3.0/){
				print logFile "\nVersion No : 13A, Not pre provisioning! ";
				close(versionFile);
				
			} else {
				print logFile "\nPre provisioning workflows.";
				print "\nPre Provisioning";
				system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh 
				/eniq/mediation_inter/bin/provision_mg_workflows.bsh -a pre -l /eniq/sw/log/autoPreProvision2.txt");
				close(versionFile);
			}
		}
	}	
}	
	
sub autoProvisionSGEH{
	print logFile "\n----Start of SGEH Provisioning----\n";
	print "\nProvisioning SGEH";
	my $result = `/eniq/mediation_inter/M_E_SGEH/bin/provision_workflows.sh; echo SGEH exit code is : $?`;
	print logFile "$result";
	print logFile "\n\n----End of SGEH Provisioning----\n";
}

sub autoProvisionMSS{
	print logFile "\n----Start of MSS Provisioning----\n";
	print "\nProvisioning MSS";
	my $dirOwner = `/eniq/home/dcuser/automation/RunCommandAsRoot.sh chown -R dcuser:dc5000 /eniq/data/pushData/`;
	print logFile "$dirOwner";
	$ENV{"LOGNAME"} = "root";
	my $pushDataPermission = `/eniq/home/dcuser/automation/RunCommandAsRoot.sh /eniq/mediation_inter/M_E_MSS/bin/mss_upgrade_pushdata_permission_correction.sh`;
	print logFile "$pushDataPermission";
	$ENV{"LOGNAME"} = "dcuser";
	my $result = `/eniq/mediation_inter/M_E_MSS/bin/mss_populate.sh`;
	print logFile "$result";
	print logFile "\n\n----End of MSS Provisioning----\n";
}

sub autoProvisionLTEEFA{
	print logFile "\n----Start of LTEEFA Provisioning----\n";
	print "\nProvisioning LTE EFA";
	my $result = `/eniq/mediation_inter/M_E_LTEEFA/bin/provision_workflows.sh -o 1; echo LTE EFA exit code is : $?`;
	print logFile "$result";
	print logFile "\n\n----End of LTEEFA Provisioning----\n";
}

sub autoProvisionLTEES{
	print logFile "\n----Start of LTEES Provisioning----\n";
	print "\nProvisioning LTE ES";
	my $result = `/eniq/home/dcuser/automation/provisioning/provisionLTEES.sh; echo LTEES exit code is : $?`;
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
		if($_ =~ m/ipfdn.mapping.source.db=false/){
			print CTRSout "ipfdn.mapping.source.db=false\n";
		} else {
			print CTRSout $_;
		}
	}

	close (CTRSIN); 
	close (CTRSout); 
	system("rm -rf /eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop.bkup");
	
	my $ecStart =`ssh dcuser\@ec_2 /eniq/mediation_sw/mediation_gw/bin/mzsh startup EC2`;
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

sub autoProvisionCTUM{
	print logFile "\n----Start of CTUM Provisioning----\n";
	print "\nProvisioning CTUM";
	
	my $createResult = `/eniq/home/dcuser/automation/provisioning/provisionCTUM.sh; echo CTUM exit code is : $?`;
	print logFile "$createResult";
	my $startListen = `/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctum  -e startlisten`;
	print logFile "$startListen";
	my $startOutput = `/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctum  -e startoutput`;
	print logFile "$startOutput";
	my $status = `/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctum  -e status`;
	print logFile "$status";
		
	print logFile "\n\n----End of CTUM Provisioning----\n";
}


sub autoProvisionDVTP{
	print logFile "\n----Start of DVTP Provisioning----\n";
	print "\nProvisioning DVTP";
	system("cp /eniq/sw/conf/static.properties /eniq/sw/conf/static.properties.bkup");
	open (CTRSIN, '<', "/eniq/sw/conf/static.properties.bkup") or die "Can't open";
	open (CTRSout, '>', "/eniq/sw/conf/static.properties") or die "Can't open";
	
	print logFile "\nUpdating /eniq/sw/conf/static.properties";
	while( <CTRSIN> ) {
		if($_ =~ m/DIM_E_SGEH.IMSItoIMEIProvider/){
			print CTRSout "\nDIM_E_SGEH.IMSItoIMEIProvider=true\n";
		} else {
			print CTRSout $_;
		}
	}

	close (CTRSIN); 
	close (CTRSout); 
	system("rm -rf /eniq/sw/conf/static.properties.bkup");
	
	my $engineRestart = `engine restart`;
	print logFile "$engineRestart";
	
	if($engineRestart =~ m/enabling svc/){
			my $createResult = `/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh –e create ; echo Create exit code is : $?`;
			print logFile "$createResult";
			my $startListen = `/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh –e startlisten; echo startlisten exit code is : $?`;
			print logFile "$startListen";
			my $startOutput = `/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh –e startoutput; echo start output exit code is : $?`;
			print logFile "$startOutput";
			my $startProcessing = `/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh –e startprocessing; echo start process exit code is : $?`;
			print logFile "$startProcessing";
	
	
			my $status = `/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh –e status`;
			print logFile "$status";
		}else{
			print logFile "\nTrouble starting engine";
			exit 1; 
		}
	print logFile "\n----End of DVTP Provisioning----\n";
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
