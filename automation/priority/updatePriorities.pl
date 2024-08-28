#!/usr/bin/perl
use strict;
use warnings;
use File::Copy;

my @passingFeatures;
my $currentFeature;
my @temp;		#used to hold str.splits()
my @allfiles;	#All files in the priority folder
my $filepath = "/eniq/home/dcuser/automation/priority/runResults/"; 		#the Pass or fail.txt file
my $automationConfigPath = "/eniq/home/dcuser/automation/Config_"; 
my $priorityConfigPath = "/eniq/home/dcuser/automation/priority/Config_"; 
my @knownFeatures;

my %testGroup = (
	   "wcdmaCFA", "WcdmaCfaTestGroupNewUI",
	   "wcdmaHFA","WcdmaHfaTestGroupNewUI",
       "mss", "MssTestGroupUILaunch",
       "aac", "AacTestGroup",
       "ltecfa", "LteCfaTestGroup",
       "ltehfa", "LteHfaTestGroup",
       "uiimprovements", "UIimprovementsTestGroup",
       "KPI", "KPINotificationTestGroup",
       "adminui", "AdminUITestGroup",	
);

sub readInFeatures(){
 opendir (DIR, $filepath) or die;
	while (my $file = readdir(DIR)) {
        if ($file =~ m/.txt/){
			@temp = split(/\./, $file);
			push (@allfiles, $temp[0]);
		}
	}

	my %temp_hash = map { $_, 0 } @allfiles;	#Delete duplicate files
	my @knownFeatures = keys %temp_hash;	
		
	my $arraySize = @knownFeatures;
	for(my $i = 0; $i < $arraySize; $i++){
		if($knownFeatures[$i] =~ m/alarmkpi/i){
			$knownFeatures[$i] = "KPI";
		}
		elsif($knownFeatures[$i] =~ m/twogthreeg/i){
			$knownFeatures[$i] = "2G3G";
		}
	}
	@knownFeatures = grep( !/dummy/,  @knownFeatures); #get rid of dummy file
	
	%temp_hash = map { $_, 0 } @knownFeatures;	#get rid of array duplicates
	@knownFeatures = keys %temp_hash;	
	
	return @knownFeatures;
}

sub getConfigCommands(){
my $arraySize = @knownFeatures;

	for(my $i = 0; $i < $arraySize; $i++){
	my $input = $automationConfigPath.uc($knownFeatures[$i]);
	my $output = $priorityConfigPath.uc($knownFeatures[$i]);

		 #If making a new one, delete old one
		if (-e $input."Data.txt") {
			if (-e $output."_PASS.txt"){
			unlink($output."_PASS.txt");
			}
			if(-e $output."_FAIL.txt"){
			unlink($output."_FAIL.txt");
			}
			
		copy($input."Data.txt",
			$output."_PASS.txt") 
			or die "Copy failed: $!";

		copy($input."Data.txt",
			$output."_FAIL.txt") 
			or die "Copy failed: $!";	
		}
		
		else{
			if (-e $input.".txt"){
					unless(open INPUT, $input.".txt"){print "\nUnable to open '$input'\n";}
					unless(open OUTPUT, '>'."$output"."_PASS.txt"){print "\nUnable to open $output"."_PASS.txt $!\n";}
					unless(open OUTPUT2, '>'."$output"."_FAIL.txt"){print "\nUnable to open $output"."_FAIL.txt\n";}
					while(my $line = <INPUT>) {	
						if ($line =~ m/INIT_SELENIUM|RUN_SELENIUM/i){last;}
						if ($line =~ m/FAILURE_THRESHOLDS/i){$line = "FAILURE_THRESHOLDS 0,0";}
						print OUTPUT $line;
						print OUTPUT2 $line;
					}
				}else{print "Not Found \n";}
			}	
	}
}

sub addToPassing(){
my $arraySize = @knownFeatures;
	opendir (DIR, $filepath) or die("Could not find directory: $filepath");
	for(my $i = 0; $i < $arraySize; $i++){
		my $output = $priorityConfigPath.uc($knownFeatures[$i])."_PASS.txt";
		unless(open OUTPUT, '>>'."$output"){print "Unable to open $output $!\n";}
		print OUTPUT "INIT_SELENIUM localhost";
		print OUTPUT "\n\nTEST_LIST -DTEST_SUITE=$knownFeatures[$i] -DTESTS=";
			
		my $passFile = "$filepath"."$knownFeatures[$i].PASS.txt";
		if(-e $passFile){
			unless (open INPUT, $passFile){print "\n No $knownFeatures[$i] PASSES Recorded\n";}
			while(my $line = <INPUT>) {	   
					chomp($line);
					print OUTPUT "$line".",";	
			}
		}
		print OUTPUT "\n\nRUN_SELENIUM ".$testGroup{"$knownFeatures[$i]"}.",selenium_events_tests.jar,localhost\n"
	}
}

sub addToFailing(){
	my $arraySize = @knownFeatures;
	opendir (DIR, $filepath) or die("Could not find directory: $filepath");
	for(my $i = 0; $i < $arraySize; $i++){
		my $output = $priorityConfigPath.uc($knownFeatures[$i])."_FAIL.txt";
		unless(open OUTPUT, '>>'."$output"){print "Unable to open $output $!\n";}
		print OUTPUT "INIT_SELENIUM localhost";
		print OUTPUT "\n\nTEST_LIST -DTEST_SUITE=$knownFeatures[$i] -DTESTS=";
			
		my $failFile = "$filepath"."$knownFeatures[$i].FAIL.txt";
		if(-e $failFile){
			unless (open INPUT, $failFile){print "\n No $knownFeatures[$i] Fails Recorded\n";}
			while(my $line = <INPUT>) {	   
					chomp($line);
					print OUTPUT "$line".",";	
			}
		}
		print OUTPUT "\n\nRUN_SELENIUM ".$testGroup{"$knownFeatures[$i]"}.",selenium_events_tests.jar,localhost\n"
	}
}

@knownFeatures = readInFeatures(); 
print "\nGenerating Priority Config files for the following features: \n@knownFeatures \n";
getConfigCommands();
addToPassing();
addToFailing();