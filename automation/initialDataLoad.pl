#!/usr/bin/perl

use strict;
use warnings;


sub startDataLoad(){
open (logFile, '>> /eniq/home/dcuser/autoData.txt') or die "ERROR: Could not create logfile";
	setupRegressionPackage();
	my $shipmentNo = getEniqShip();
	my $filesExist = checkFilesExists();
	cleanAudit();
	if($filesExist == '1'){
		startData("$shipmentNo");
		my $initDataLoaded = waitForInitDataLoading("$shipmentNo");
		if($initDataLoaded == '1'){
			my $didInitDataLoadPass = checkInitLogs("$shipmentNo");
			if ($didInitDataLoadPass == '1') {
				my $lteesDataLoaded = waitForLteesDataLoading("$shipmentNo");
				if($lteesDataLoaded == '1'){ 
						my $didLteesDataLoadPass = checkLteesLogs("$shipmentNo");
						if ($didLteesDataLoadPass == '1') {
							print logFile "\nSUCCESS: Data Loaded successfully";
						} else {
							print logFile "\nStopping upgrade, LTEES Data failed to Load";
							die "\nERROR: LTEES Data loading failed.";
						}
				} else {
					print logFile "\nStopping upgrade, LTEES Data Load taking too long";
					die "\nERROR: LTEES Data Load taking too long.";
				}
			} else {
				print logFile "\nStopping upgrade, Data failed to Load";
				die "\nERROR: Data loading failed.";
			}		
		} else {
			print logFile "\nERROR: Data loading seems to be taking to long.";
			die "\nERROR: Data loading taking too long. Something may be wrong";
		}
	} else {
		print logFile "\nERROR: Necessary data loading files not present.";
		die "\nERROR: Data loading files not present.";
	}
	close (logFile);
}

sub setupRegressionPackage(){
	print logFile "\nRemoving old Regression Files.";
	system('rm -rf /eniq/home/dcuser/ENIQ_EVENTS_AUTO_TESTS*');
	system('rm -rf /eniq/home/dcuser/automation/*');
	system('rm -rf /eniq/home/dcuser/selenium/*');
	print logFile "\nGetting latest regression package and unzipping.";
	system('id | grep dcuser >/dev/null && cp `ls -t /net/atclvm559.athtem.eei.ericsson.se/ossrc/package/ENIQ* | head -1` /eniq/home/dcuser || echo "You must be dcuser"');
	system('unzip -o /eniq/home/dcuser/ENIQ_EVENTS_AUTO_TESTS_R*.zip -d /eniq/home/dcuser/');
}

#reads eniq_status file to determine eniq shipment
sub getEniqShip(){
	print logFile "\nGetting Eniq shipment number.";
	my $eniqStatus = "/eniq/admin/version/eniq_status";
	open(versionFile, $eniqStatus) or die "ERROR: Could not open Eniq Status";
	while(<versionFile>){
		if($_ =~ m/Events_Shipment_/){
			my @values = split('ENIQ_STATUS ENIQ_Events_Shipment_', $_);
			#$values[1] now contains full shipment number
			if($values[1] =~ /^3.0/){
				close(versionFile);
				return "13A";
			} else {
				close(versionFile);
				return "14A";
			}
		}
	}
}

sub checkFilesExists(){
	print logFile "\nMaking sure required files exits!";
	my $RegressionScript = "/eniq/home/dcuser/automation/EniqEventsRegress.sh";
	my $AdminUIConf = "/eniq/home/dcuser/automation/Config_ADMINUI.txt";
	my $InitConf = "/eniq/home/dcuser/automation/Config_INIT.txt";
	my $Init13AConf = "/eniq/home/dcuser/automation/Config_13ADATA.txt";
	my $testWrapper = "/eniq/home/dcuser/automation/nmi_auto_test_wrapper.pl";
	
	if(-e $RegressionScript && -e $AdminUIConf && -e $InitConf && -e $Init13AConf && -e $testWrapper){
		return '1';
	} else {
		print logFile "\nRequired files DO NOT exist!";
		return '0';
	}
}

sub cleanAudit(){
	print logFile "\nCleaning out audit folder of old data load logs.";

	my $initFiles = '/eniq/home/dcuser/automation/audit/*INIT*';
	my $adminFiles = '/eniq/home/dcuser/automation/audit/*ADMIN*';
	my $dataFiles13A = '/eniq/home/dcuser/automation/audit/*DATA*';
	
	unlink glob $initFiles;
	unlink glob $adminFiles;
	unlink glob $dataFiles13A;
}

sub startData(){
	print logFile "\nStarting data load\n.";
	print "\nStarting data load.";
	print "\nData Loading about 70mins. \nTo monitor how dataloading is doing check :\n/eniq/home/dcuser/autoData.txt\n";
	my $version = join("", @_);
	
	if ($version =~ m/13A/){
		print logFile "\n".localtime(time)."  Sleeping 40 minutes for ADMINUI to complete and 13A Data Loading to initiate\n";
		system('/eniq/home/dcuser/automation/nmi_auto_test_wrapper.pl -13adata');
	} else {
		print logFile "\n".localtime(time)." Sleeping 40 minutes for ADMINUI to complete and 14A Data Loading to initiate\n";
		system('/eniq/home/dcuser/automation/nmi_auto_test_wrapper.pl -data');
	}
	
}

sub waitForInitDataLoading(){
	my $version = join("", @_);	
	sleep(2400);
	for (my $count = 10; $count >= 0; $count--) {
		print logFile "\n".localtime(time)." Data not yet finished loading. Will check again in 10 minutes. $count tries left.";
		if ($version =~ m/13A/){ 
			open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*DATA*`) or die "ERROR: Cannot open datloading logfile";
		} else {
			open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*INIT*`) or die "ERROR: Cannot open datloading logfile";
		}
		
		while (<MYFILE>) {
			chomp;
			if($_ =~ m/Complete Html File/){
				$count = -1;
				print logFile "\n".localtime(time)." Data Loading Complete";
				close (MYFILE); 
				return '1';
			}
		}
	close (MYFILE); 
	sleep(600);
	}
	return '0';
}


sub waitForLteesDataLoading(){
	my $version = join("", @_);	
	for (my $count = 9; $count >= 0; $count--) {
		print logFile "\n".localtime(time)." LTEES Data not yet finished loading. Will check again in 10 minutes. $count tries left.";
		if ($version =~ m/13A/){ 
			open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*LTEES*`) or die "ERROR: Cannot open datloading logfile";
		} else {
			open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*LTEES*`) or die "ERROR: Cannot open datloading logfile";
		}
		
		while (<MYFILE>) {
			chomp;
			if($_ =~ m/Complete Html File/){
				$count = -1;
				print logFile "\n".localtime(time)." Data Loading Complete";
				close (MYFILE); 
				return '1';
			}
		}
	close (MYFILE); 
	sleep(600);
	}
	return '0';
}

sub checkInitLogs(){
	my $version = join("", @_);
	if ($version =~ m/13A/){ 
		open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*DATA*`) or die "ERROR: Cannot open datloading logfile";
	} else {
		open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*INIT*`) or die "ERROR: Cannot open datloading logfile";
	}
	
	while (<MYFILE>) {
			chomp;
			if($_ =~ m/ERROR:Raw Table/){
				print "\n".localtime(time)." Some tables failed to populate";
				print logFile "\n".localtime(time)." Some tables failed to populate\n";
				close (MYFILE); 
				return '0';
			}
		}
	close (MYFILE); 
	return '1';
}


sub checkLteesLogs(){
	my $version = join("", @_);
	if ($version =~ m/13A/){ 
		open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*LTEES*`) or die "ERROR: Cannot open datloading logfile";
	} else {
		open (MYFILE, `ls /eniq/home/dcuser/automation/audit/*LTEES*`) or die "ERROR: Cannot open datloading logfile";
	}
	
	while (<MYFILE>) {
			chomp;
			if($_ =~ m/LTEES files NOT detected in directory - FAIL/){
				print "\n".localtime(time)." LTEES failed to produce files";
				print logFile "\n".localtime(time)." LTEES failed to produce files\n";
				close (MYFILE); 
				return '0';
			}
		}
	close (MYFILE); 
	return '1';
}

startDataLoad();
##
