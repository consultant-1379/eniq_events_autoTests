#!/usr/bin/perl

use strict;
use warnings;


sub main { 
	open (logFile, '>',"/eniq/sw/log/autoProvision.txt") or die "ERROR: Could not create logfile";
	autoProvisionLTEEFA();
	autoProvisionLTEES();
	autoUpdateProperties();
	autoProvisionCTR();
	autoProvisionCTUM();
	close logFile;
	checkLogsForFailures();
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
	my $result = `/eniq/home/dcuser/automation/provisioning/provisionLTEES_Stream.sh; echo LTEES exit code is : $?`;
	print logFile "$result";
	print logFile "\n\n----End of LTEES Provisioning----\n";
}

sub autoUpdateProperties{


	my $addedInFile = 0;

        open my $file1,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";

	while( <$file1> )   # print the lines before the change
		{
		
		if( $_ =~ m/ipfdn.mapping.source.db=false/ ) {
		         $addedInFile = 1;
	         }

		last if $addedInFile == 1;
		}
		if ($addedInFile == 0) {

			 my $lineToAdd = "";
		   	 open my $in,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";
			 open my $out, '>', "/tmp/ctrs.prop2"      or die "Can't read file: $!";
			 while( <$in> )   # print the lines before the change
			 {
			     if ( $_ =~ m/ipfdn.mapping.source.db/ ) {
			     $lineToAdd = $_;
			      }
			    last if( $_ =~ m/ipfdn.mapping.source.db/ );
		            print $out $_;
		         }
			 $lineToAdd =~ s/true/false/g;
			 print $out $lineToAdd;

			 while( <$in> )   # print the rest of the lines
				 {
					 print $out $_;
				 }

				 close $in;
				 close $out;
				 system " cp /tmp/ctrs.prop2 /eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"
			}

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
