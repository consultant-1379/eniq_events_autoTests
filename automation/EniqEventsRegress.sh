#!/usr/bin/perl -C0

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
use POSIX;

my $WCDMAFiledate;
my $dateIs;
my @report;
my @lines1;
my $reportFile;
my $packageRunStartTime = "";
my $reservedDataLocation="/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv";
my $pageMiddle = "";
my $fullPage = "";
my $complete = "";
my $result = "";
my $initDate = getHTMLTime();
my $doWrapperUpload=0;
my $referenceServer = " " ;
my $referenceServerHostname = " " ;
my $trafficLightColour = "green";
my $endTime = "";
my $failure_threshold_red = 0;
my $failure_threshold_orange = 0;
my $traffic_light_display = "false";
my @numberOfTests;
my $like4likeIsRunning = 1;
my $listOfTestsToRun = "";
my $dataloading_status = 1 ;
my $topology_present="/tmp/topology_halt";
my $concurrent_present="/tmp/concurrent_halt";
my $wcdmaData_complete="/tmp/wcdmaData_halt";
my $gritToolFlag = '1';
my $sel_port;
my @estParam = "";
my $ecstParam = "";
my $edeESTserver ;
my $edeESTInter ;
my $edeESTTopology ;
my $edeESTOutput ;
my $edeESTLocation ;
my $wcdmaGritFlag = '0';
my $temp_pwd = `grep -w "DCPassword" /eniq/sw/conf/niq.ini | head -1 | cut -d "=" -f2`;
chomp($temp_pwd);
my $dBPassword = "P"."$temp_pwd";
chomp($dBPassword);
my $password_encrypation = `grep "DCPassword_Encrypted" /eniq/sw/conf/niq.ini | cut -d "=" -f2`;
chomp($password_encrypation);
if ($password_encrypation eq 'Y') {
$temp_pwd= `echo $dBPassword |/usr/sfw/bin/openssl enc -base64 -d`;
chomp($temp_pwd);
$dBPassword = "P"."$temp_pwd";
chomp($dBPassword);
}
############################################################
# THIS ENV VARIABLE IS NEEDED FOR CRONTAB
$ENV{'SYBASE'}='/eniq/sybase_iq';
$ENV{'IQDIR15'}='/eniq/sybase_iq/IQ-15_2';
$ENV{'CONF_DIR'}='/eniq/sw/conf';
$ENV{'PATH'} = '/eniq/sybase_iq///IQ-15_2/bin64:/usr/bin::/eniq/sw/runtime/nokalva/asn1pjav/solaris.tgt/3.0:/usr/local/bin:/eniq/sw/bin:/eniq/sybase_iq///IQ-15_2/lib64::/eniq/sw/runtime/nokalva/asn1pjav/solaris.tgt/3.0/lib:/eniq/sql_anywhere/lib64
';

# Setup Directory structure

my $BASE_DIR='/eniq/home/dcuser/automation';
my $LOGS_DIR="$BASE_DIR".'/RegressionLogs';
my $AUDIT_DIR="$BASE_DIR".'/audit';
my $HTML_DIR="$BASE_DIR".'/html';
my $TOPOLOGY_DIR="$BASE_DIR".'/topology';
my $DGTOPOLOGY_DIR="$BASE_DIR".'/DataGenTopology';
my $DGWORKFLOWS_DIR="$BASE_DIR".'/DataGenWorkFlows';
#Directory Paths for Glassfish
my $autoDeployPath = "/eniq/glassfish/glassfish/glassfish/domains/domain1/autodeploy/";
my $glassfishConfigPath = "/eniq/glassfish/glassfish/glassfish/domains/domain1/config/";
my $applicationsFolder = "/eniq/glassfish/glassfish3/glassfish/domains/domain1/applications/";

# Log Settings

my $run_date = &getRunDate();
my $audit_log = "$AUDIT_DIR";
my $audit_path = "";
my $error_log = "$AUDIT_DIR".'/ErrorLog.'."$run_date".'.txt';
my $html_results;
my $html_path;
my @seleniumResultsFiles=();
my $verifylogs_results = "";
my $ltees_results = "";
my $kpi_results = "";
my $ltees_counterlog = "";
my $aacHTML = "";
my $aacLog = "";
my $gritcsvlogs = "";
my $event_err_raw_csv = "";
my $event_suc_raw_csv = "";

my $AdminUIUsername = "admin";
my $AdminUIPassword = "admin";


#----------------------------------------------------------
# Set path for Sybase iSQL
#----------------------------------------------------------

my $ISQL;

my $version_file="/eniq/sybase_iq/version/iq_version";

if ( ! -f "$version_file" )
{
  print "Could not access file '$version_file'.
It is required to set 'isql' path. Could not able to set path for 'isql'.
Check for issue. Script will exit at this line";
 exit;
}


open(INPUT,$version_file);
my @file_syb=<INPUT>;
close(INPUT);

my @line_syb=grep(/^VERSION/,@file_syb);
my @syb_ver=split(':',$line_syb[0]);

if ( $syb_ver[2] >= 16 )
{
   $ISQL="/eniq/sybase_iq/OCS-15_0/bin/isql";
}
else
{
   $ISQL="/eniq/sybase_iq/IQ-15_2/bin64/iqisql";
}



#-------------------------------------------------------------------------
# INITIALISE Config Parameters
#
# These parameters are used to set or unset a test
# if the test is set to true then the test is executed, else is not
# parameters without initial value are variables that get their input from 
# the configuration file
#--------------------------------------------------------------------------

# Where the html results get posted
my	@resParamList                   = undef;;
my	$resultsServer;
my	$resultsPath;
my	$resultsUser;
my	$resultsPass;
my $ftpLogin = 0;
my $resultsPathNew;

#Separate results when run regression parallel
my $separateResultsFlag=0;
my $numberOfFeatureInSuit;
my @pageMiddleArray=();

my $tempSummary = $LOGS_DIR."/summary.html";

# LTE ES
my	@lteesParamList		= undef;

#	ADMIN UI - System Monitoring (19)
my	$OverallStatus			="false";

#  like4likeEDE
my @like4likeParam = undef;

# Topology Loading and Updating (2)
my	@topologylist			= undef;

# Data Loading
my	$refreshTopology		="false";

# Start Selenium
my	@seleniumParamList		= undef;
my 	$epTp              		="";
my 	$epProcess         		="";

# Non-Configuration Parameters
my $baselinePath			="";
my $pathLogs				="";	
my $level				="DAY";
my $seleniumTestGroup			="VoidTestGroup";
my @tpini=();
my @aggini=();

if(!isLteesOnlyServer() and !isSonVisOnlyServer()){
	@tpini				= getAllTechPacks();
	@aggini				= getAllTechPacks();
}

my $YEARTIMEWARP			="";
my $MONTHTIMEWARP			="";
my $DAYTIMEWARP				="";
my $DATETIMEWARP			="";
my $timeWarp          			= -8;
my $DGtimeWarp          		= 10;
my $numRops				= 0;
my $LOCALHOST				="http://localhost:8080";
my $LOCALHOSTSEL			="http://localhost";
my $domain = getDomainName();

my $startTime = getTime();
my $totalPass = 0;
my $totalFail = 0;

# Used to extract the <feature> from the Config file.
my $feature = "";

my $gDir;
my @gFileMatch;
my @gDirMatch;
my @gDirExclude;

my @ecHosts=();
my $dgServer="atclvm559.athtem.eei.ericsson.se";
my $dgServerHexIpAddress="0x0a2dcfd3000000000000000000000000";
my $dgReservedServer="atclvm560";
my $dgReservedServerHexIpAddress="0x0a3b8276000000000000000000000000";
my $dgPath="/tmp/CentralDatagen";
my $dgNfsPath="/net/$dgServer$dgPath";
my $remoteDG=0;
my $skipEcXmlSetup=0;
my $resultServer="atdl785esxvm8.athtem.eei.ericsson.se";
my $counterConfigPath="/eniq/mediation_inter/M_E_LTEES/config/counter_conf";
my $counterPropertiesPath="/eniq/home/dcuser/automation/ltees/properties";

#----------------------------------------------------------
#	END Initialisation.
#----------------------------------------------------------


#----------------------------------------------------------
#
# SUBROUTINE parseParam()
#
# This routine reads all the configuration parameters.
# The input is a configuration text file
# The file contains a set of tests in sequential order i.e:
#HELP
#VERIFY_EXECUTABLES
#SCHEDULERCLI
#ENGINECLI
#WEBSERVERCLI
#READLOG
#
# The hash is used to comment out the line
# The algorithm is:
# read the file and check it for test labels
# if the label matches then the execution flag is turn to true
# the file is read from start to end and sets as many flags as needed.
# The sub processParam will follow.
#-------------------------------------------------------------
sub processResults{
	my $testResults=shift;
	my $testName=shift;
	my $reportFileName=shift;
	my $report =getHtmlHeader();
	$report.= "<h2>STARTTIME:";
	$report.= $startTime."</h2>";
	my $times = getTime();
	$result.= "<br>$times $testName<br>";
	&FT_LOG("$testName\n\n");
	$result.=$testResults;
	$report.= $testResults;
	$report.= "<h2>ENDTIME:";
	$report.= getTime()."</h2>";
	$report.= getHtmlTail();
	my $tempPage = getHTMLStart();
	$tempPage.= getSummary($result);
	$tempPage.=$result;
	$tempPage.=getHtmlTail();
	writeHtml($complete,$tempPage);
}
sub waitForTopologyLoad{
        sleep 5;
        while (1) {
                if (-e "$topology_present"){
                        &FT_LOG("INFO: Topology Loading is on-going. Will wait till it finishes.");
                        sleep 10;
                }
                else{
                        &FT_LOG("INFO: Topology Loading completed. will proceed with execution.");
                        last;
                }
        }
}

sub waitForWCDMADataLoad{
        sleep 5;
        while (1) {
                if (-e "$wcdmaData_complete"){
                        &FT_LOG("INFO: Data Loading is on-going. Will wait till it finishes.");
                        sleep 10;
                }
                else{
                        &FT_LOG("INFO: Data Loading completed. will proceed with execution.");
                        last;
                }
        }
}

sub createConcurrent{
	if(! -e "$wcdmaData_complete"){
		system "touch $wcdmaData_complete";
	}
}

sub removeConcurrent{
	if(! -e "$concurrent_present"){
                system "rm -f $concurrent_present";
        }
}

sub parseParam{
	$feature=$ARGV[0];
	$feature =~ s/\.txt.*$/\.txt/;
	$feature =~ s/^.*Config/Config/;
	$feature =~ s/_FAIL|_PASS//i;
	print "FEATURE = $feature\n";
	my $auditFeature = $feature;
	$auditFeature =~ s/.txt/_/;
	$auditFeature =~ s/Config_/_/;
	
	#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
	my $host_tmp = hostname;
	chomp($host_tmp);
	if ( $host_tmp eq "eniqe" ){
		$audit_log.='/Audit.eniqe.'.getHostName().$auditFeature."$run_date".'.txt';
		$audit_path.='/Audit.eniqe.'.getHostName().$auditFeature."$run_date".'.txt';
	}
	else{
		$audit_log.='/Audit.'.getHostName().$auditFeature."$run_date".'.txt';
		$audit_path.='/Audit.'.getHostName().$auditFeature."$run_date".'.txt';
	}
	
	
	$feature =~ s/Config_//;
	$feature =~ s/.txt//;
	
	#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
	my $host_tmp= hostname;
	chomp($host_tmp);
	if ( $host_tmp eq "eniqe" ){
		$complete = 'eniqe_'.getHostName().'_'.$feature;
	}
	else{
		$complete = getHostName().'_'.$feature;
	}

	
	
	executeThisQuiet( "dos2unix " . $ARGV[0] . " " . $ARGV[0] );#Sort out Windows line breaks
	open(INPUT,"< $ARGV[0]");
	my @input=<INPUT>;
	chomp(@input);
	close(INPUT);

	#ENC-2314: Updating platform.xml so that EC and controlzone processes will run smoothly in vapp
        my $host=hostname;
        my $ecXMLPath="/eniq/mediation_sw/mediation_gw/etc";
        my $ecXMLFile="$ecXMLPath/platform.xml";

	foreach my $input (@input) {
		$_=$input;
		# SKIP IF THE LINE IS COMMENTED OUT
		# SKIP IF THE LINE IS AN INFO COMMENT (!)
		next if(/^#/);
		next if(/^!/);
		if(/^TOPOLOGYLOADWAIT/){
			waitForTopologyLoad();
        }
		if(/^CONCURRENTSTART/){
			createConcurrent();
        }
		if(/^CONCURRENTSTOP/){
			removeConcurrent();
        }
		if(/^WCDMADATALOADWAIT/){
			waitForWCDMADataLoad();
		}

		######################################
		# Where the results (Html) get posted
		#
		if(/^RESULTSPATH /){
			$ftpLogin = 1;
			$input=~s/RESULTSPATH //;
			$input=~s/ //g;
			@resParamList = split(/,/, $input);
			$resultsServer = $resParamList[0];
			$resultsPath = $resParamList[1];
			
			#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
			my $host_tmp= hostname;
			chomp($host_tmp);
			if ( $host_tmp eq "eniqe" ){
				$resultsPathNew =  $resultsPath.'/eniqe_'.getHostName().$packageRunStartTime;
			}
			else{
				$resultsPathNew =  $resultsPath.'/'.getHostName().$packageRunStartTime;
			}
			
			
			$resultsUser = $resParamList[2];
			$resultsPass = $resParamList[3];
			
			my $ping = Net::Ping->new();
			if($ping->ping($resultsServer)){
				print "Ftp Server is alive.\n";
				$ftpLogin = 1;
			}else{
				print "Ftp Server is not alive";
				$ftpLogin = 0;
			}
			$ping->close();
			
			print $ftpLogin;
			#CHECK FTP LOGIN HERE??
			if($ftpLogin == 1){
				my $ftp=Net::FTP->new($resultsServer,Timeout=>240) or $ftpLogin = 0;
				$ftp->login($resultsUser, $resultsPass) or $ftpLogin = 0;
				$ftp->quit;
			}
			
			if( $ftpLogin ){
				&FT_LOG("INFO:FTP login is correct!\n");
			}else{
				&FT_LOG("INFO:FTP login failed. Results will not be uploaded!\n");
			}
		}
		
		if(/^FAILURE_THRESHOLDS /){
			$input=~s/FAILURE_THRESHOLDS //;
			$input=~s/ //g;
			my @maxfails = split(/,/, $input);
			$failure_threshold_orange = $maxfails[0];
			$failure_threshold_red = $maxfails[1];
			$traffic_light_display = "true";
		}
		
		if(/^CREATE_DASHBOARD_USER/){	
			$input=~s/CREATE_DASHBOARD_USER//;
			$input=~s/ //g;
			createDashboardUser(1);
		}
		
		if(/^HTTPSTEST/){
			$input=~s/HTTPSTEST//;
			$input=~s/ //g;
			my $result1=checkHTTPS($input);
			processResults($result1,"HTTPS enable/disable test:","HTTPSTEST");
		}
		
		if(/^CHECK_TIMES/){
			$input=~s/CHECK_TIMES//;
			$input=~s/ //g;
			my $result1=verifyDataGenTimes($input);
			processResults($result1,"latest data in datagen tables: ","CHECK_TIMES");
		}
		
		if(/^LOADMAPS/){
			$input=~s/LOADMAPS//;
			$input=~s/ //g;
			my $result1=ismapsInstalled($input);
			processResults($result1,"loading Maps","MapsLoad");
		}
		
		if(/^STARTDATAGEN_KPI/){
			$input=~s/STARTDATAGEN_KPI//;
			$input=~s/ //g;
			my $result1=dataGenStart_kpi($input);
			processResults($result1,"DATAGEN START KPI (first table) AND MSS(second table):","STARTDATAGEN_KPI");
		}
		
		if(/^UPDATE_CRON_FOR_PROFILER/){
			$input=~s/UPDATE_CRON_FOR_PROFILER//;
			$input=~s/ //;
			$input=~s/ //g;
			@lteesParamList = split(/,/, $input);
			my $result1=upadteProfilerCron();
		}
		
		if(/^STARTDATAGEN_WCDMA/){
			$input=~s/STARTDATAGEN_WCDMA//;
			$input=~s/ //g;
			my $result1=dataGenStart_WCDMA_OnCEP($input);
			processResults($result1,"DATAGEN START WCDMA:","STARTDATAGEN_WCDMA");
		}
		
		if(/^STARTDATAGEN_13AWCDMA/){
			$input=~s/STARTDATAGEN_WCDMA//;
			$input=~s/ //g;
			my $result1=dataGenStart_WCDMA($input);
			processResults($result1,"DATAGEN START WCDMA:","STARTDATAGEN_WCDMA");
		}
		
		if(/^KPI_PHASE1_CREATE_GROUPS/){
			kpiPhase1CreateGroups();
		}
		
		if(/^2G3G_CREATE_GROUPS/){
			twoGThreeGCreateGroups();
		}
		
		if(/^LTE_CREATE_GROUPS/){
			lteCreateGroups();
		}
		
		if(/^KPI_PHASE1/){
			$input=~s/KPI_PHASE1//;
			my $result1=runKpiPhase1();
			processResults($result1,"KPI_PHASE1","KPI_PHASE1");
		}
		
		if(/^SB_FIND_IMSI/){
			my $result1 = sbFindIMSIs();
			processResults($result1,"SB_FIND_IMSI","SB_FIND_IMSI");
		}
		
		if(/^RUN_LTEES/){
			$input=~s/RUN_LTEES//;
			$input=~s/ //;
			$input=~s/ //g;
			@lteesParamList = split(/,/, $input);
			system("perl -i -pe 's/=OFF/=ON/ig' $counterConfigPath/counterConfig.prop");
			my $result1=runLtees("RUN_LTEES");
			system("perl -i -pe 's/=N/=Y/ig' $counterPropertiesPath/counter.properties");
			processResults($result1,"RUN_LTEES","RUN_LTEES");
		}

		if(/^RUN_CM_LTEES/){
			$input=~s/RUN_CM_LTEES//;
			$input=~s/ //;
			$input=~s/ //g;
			@lteesParamList = split(/,/, $input);
			system("perl -i -pe 's/=ON/=OFF/ig' $counterConfigPath/counterConfig.prop");
			setSelectiveCounterStatus();
			my $result1=runLtees("RUN_CM_LTEES");
			system("perl -i -pe 's/=N/=Y/ig' $counterPropertiesPath/counter.properties");
			processResults($result1,"RUN_CM_LTEES","RUN_CM_LTEES");
		}
		
		if(/^RUN_GRIT_VTOC/){
			$input=~s/RUN_GRIT_VTOC//;
			$input=~s/ //;
			chomp($input);
			print "Parameter is $input";
			system("perl -i -pe 's/=OFF/=ON/ig' $counterConfigPath/counterConfig.prop");
			runLtees_vtoc("$input");
		}

		if(/^RUN_ATOMDB/){
			$input=~s/RUN_ATOMDB//;
			$input=~s/ //;
			$input=~s/ //g;
			@lteesParamList = split(/,/, $input);
			my $result1=runATOMDB();
			processResults($result1,"RUN_ATOMDB","RUN_ATOMDB");
		}

		#Hi. This will disable all work flows on a single blade as to free up space. Each feature testing will start what it needs.
		if(/^SINGLE_BLADE_ROLLING_DISABLE/){
			if(isMultiBladeServer()){
				#no need to do anything
				&FT_LOG("Is multiblade server, skipping 'SINGLE_BLADE_ROLLING_DISABLE'\n");
			}else{
				#Single Blade Server. Disables all workflows to reduce load
				my @stopped = disableAllWorkflows();
			}
		}
		
		######################################
		# Check all services status
		#
		if(/^CHECKINGSERVICESTATUS/){
			if(!isLteesOnlyServer()){
				$input=~s/CHECKINGSERVICESTATUS//;
				$input=~s/ //;
				my $result1=checkingServicesStatus($input);
				if($result1 ne ""){#If empty then no errors and not in verbose mode
					processResults($result1,"EXECUTE BASIC_STATUS COMMAND LINE TESTS:","CHECKINGSERVICESTATUS");
				}
			}
		}
	
		if(/^SWITCHOFFSECURITY/){
			swichOffSecurity();
		}
		
		if(/^SWITCHONSECURITY/){
			swichOnSecurity();
		}
				
		######################################
		# Compare Baseline Grouping (1)
		#
		if(/^COMPARE_BASELINE/){
			$input=~s/COMPARE_BASELINE //;
			$baselinePath = $input;
			my $result1.=compareBaselineModules($baselinePath);
			$result1.=compareBaselineTechpacks($baselinePath);
			processResults($result1,"COMPARE BASELINE<br>$baselinePath","COMPAREBASELINE");
		}

		######################################
		#	ADMIN UI - System Monitoring (19)
		#
		if(/^OVERALLSTATUS/){
			$OverallStatus= "true";
			my $result1=OverallStatus();
			processResults($result1,"OVERALLSTATUS","OverallStatus");
			
			# If we find a 'fail' in OverallStatus we want to exit
			if($result1 =~ m/FAIL/)
			{
				print "INFO: FAIL found in Overall Status, so exiting now.\n";
				$trafficLightColour = "red";
				last;
			}

			$OverallStatus="false";
		}
		
		if(/^SYSTEMSTATUS/){
			my $result1=SystemStatus();
			processResults($result1,"SYSTEMSTATUS","SystemStatus");
		}
		
		if(/^DWHDBSTATUS/){	
			my $result1=DwhStatus();
			processResults($result1,"DWHDBSTATUS","DwhStatus");
		}
		
		if(/^REPDBSTATUS/){
			my $result1=RepStatus();
			processResults($result1,"REPDBSTATUS:","RepStatus");
		}
		
		if(/^ENGINESTATUS/){
			my $result1=EngineStatus();
			processResults($result1,"ENGINESTATUS:","EngineStatus");
		}
		
		if(/^SCHEDULERSTATUS/){
			my $result1=SchedulerStatus();
			processResults($result1,"SCHEDULERSTATUS:","SchedulerStatus");
		}
		
		if(/^LICSERVSTATUS/){
			my $result1=LicservStatus();
			processResults($result1,"LICSERVSTATUS:","LicservStatus");
		}
		
		if(/^LICMGRSTATUS/){
			my $result1=LicmgrStatus();
			processResults($result1,"LICMGRSTATUS:","LicmgrStatus");
		}
		
		if(/^DISKSPACE/){
			my $result1=DiskSpace();
			processResults($result1,"DISKSPACE:","DISKSPACE");
		}	
		
		if(/^ENIQVERSION/){
			my $result1=eniqVersion();
			processResults($result1,"ENIQVERSION:","ENIQVERSION");
		}	
		
		if(/^INSTALLED_MODULES/){
			my $result1=InstalledModules();
			processResults($result1,"INSTALLED_MODULES ADMINUI:","INSTALLED_MODULES");
		}
		
		if(/^INSTALLED_TPS/){
			my $result1=InstalledTps();
			processResults($result1,"INSTALLED_TPS IN ADMINUI:","INSTALLED_TPS");
		}
		
		if(/^ACTIVE_PROCS/){
			my $result1=ActiveProcs();
			processResults($result1,"ACTIVE_PROCS:","ACTIVE_PROCS");
			#backhere
		}
		
		if(/^ADMIN_UI_TEST/){
			my $result1=AdminUiTest();
			processResults($result1,"AdminUI","ReportAdminUi");
		}

		######################################
		# Command Line Execution Grouping (11)
		#
		if(/^VERIFY_EXECUTABLES/){
			my $result1=verifyExecutables();
			processResults($result1,"FIND WRONG EXEs(DOS style):","verifyExecutables");
		}
		
		if(/^MEDIATIONCLI/){
			my $result1=runMediationGW();
			processResults($result1,"EXECUTE MEDIATION GW CMD LINE TESTS:","MEDIATIONCLI");
		}
		
		if(/^GLASSFISHCLI/){
			my $result1=runGlassfish();
			processResults($result1,"EXECUTE GLASSFISH COMMAND LINE TESTS:","GLASSFISHCLI");
		}
		
		if(/^ECCLI/){
			my $result1=runEC();
			processResults($result1,"EXECUTE EC COMMAND LINE TESTS:","ECCLI");
		}
		
		if(/^SCHEDULERCLI/){
			my $result1=runScheduler();
			processResults($result1,"EXECUTE SCHEDULER CMD LINE TESTS:","SCHEDULERCLI");
		}
		
		if(/^ENGINECLI/){
			my $result1=runEngine();
			processResults($result1,"EXECUTE ENGINE CMD LINE TESTS:","ENGINECLI");
		}
		
		if(/^WEBSERVERCLI/){
			my $result1=runWebserver();
			processResults($result1,"EXECUTE WEBSERVER CMD LINE TESTS:","WEBSERVERCLI");
		}
		
		if(/^LICMGRCLI/){
			my $result1=runLicenseManager();
			processResults($result1,"EXECUTE LICENSE MANAGER CMD LINE TESTS:","LICENSECLI");
		}
		
		if(/^LICSERVCLI/){	
			my $result1=runLicserv();
			processResults($result1,"EXECUTE LICENSE SERVER CMD LINE TESTS:","LICENSESERVERCLI");
		}
		
		if(/^DWHDBCLI/){
			my $result1=runDwhdb();
			processResults($result1,"EXECUTE DWHDB CMD LINE TESTS:","DWHDBCLI");
		}
		
		if(/^REPDBCLI/){
			my $result1=runRepdb();
			processResults($result1,"EXECUTE REPDB CMD LINE TESTS:","REPDBCLI");
		}
		
		if(/^RUNCMDLINE/){
			my $result1=runCMDLineNP();
			$result1.=runCMDLineComplete();
			processResults($result1,"EXECUTE CMD LINE TESTS:","CMDLINE");
		}

		#######################################
		#	PRE Verification Tests Grouping (2)
		#
		
		if(/^PRE_LOAD/){
			my $result1=preLoad();
			processResults($result1,"PRE_LOAD RUNS SETS TO LOAD DATA","PRE_LOAD");
		}
		
		######################################
		#	Generic Tests (3)
		#	
		if(/^VERIFY_DIRECTORIES/){
			my $result1=VerifyDirectories();
			processResults($result1,"VERIFY_DIRECTORIES:","VERIFY_DIRECTORIES");
		}
		
		if(/^VERIFY_ADMIN_SCRIPTS/){
			my $result1=VerifyAdminScripts();
			processResults($result1,"VERIFY_ADMIN_SCRIPTS:","VERIFY_ADMIN_SCRIPTS");
		}
		
		if(/^MAX_USERS_ADMINUI/){
			my $result1=MaxUsersAdminui();
			processResults($result1,"MAX_USERS_ADMINUI:","MAX_USERS_ADMINUI");
		}
		
		if(/^ADMINUI_WRONG_USER/){
			my $result1=wrongUser();
			processResults($result1,"ADMINUI_WRONG_USER:","ADMINUI_WRONG_USER");
		}	
		
		#######################################
		# Topology Loading and Updating (2)
		#
		if(/^LOADTOPOLOGY /){
			$input=~s/LOADTOPOLOGY //;
			$input=~s/ //g;
			@topologylist = split(/,/, $input);
			my $result1=loadTopology();
			#$result1.=verifyTopology();
			processResults($result1,"LOAD TOPOLOGY:","LOADTOPOLOGY");			
		}

		#######################################
		#	ADMIN UI - User Administeration
		#
		if(/^VERIFYLDAP/){
			my $result1=VerifyLDAP();
			processResults($result1,"VERIFYLDAP:","VERIFYLDAP");
		}
		
		if(/^HELP/){
			my $result1=help();
			processResults($result1,"VERIFY HELP LINKS","HELP");
		}	
		
		if(/^DGSOURCE/){
			$input=~s/DGSOURCE//;
			$input=~s/ //g;
			my $host=$input;
			if($host =~ m/remote/){
                        ## re-initilising the varable value in case we switch the datagen from ede to remote 
                            $dgServer="atclvm559.athtem.eei.ericsson.se";
                            $dgPath="/tmp/CentralDatagen";
                            $dgNfsPath="/net/$dgServer$dgPath";
				&FT_LOG("INFO:Enabling remote datagen from host '$dgServer'");
				$remoteDG=1;
			}elsif(length($host)>1 && $host !~ m/local/){
				$remoteDG=1;
				if($host !~ m/ede/){
					my @ping=executeThisQuiet("/usr/sbin/ping $host 2>/dev/null");

					if (grep(/is alive/,@ping)) {
						&FT_LOG("INFO:Enabling remote datagen from host '$host'");
						$dgNfsPath=~s/$dgServer/$host/;
						$dgServer=$host;
					}else{
						&FT_LOG("ERROR:Not setting remote datagen server to host '$host' because it could not be pinged. Leaving as '$dgServer'");
						$remoteDG=1;
					}
				}else{
					&FT_LOG("INFO:Enabling remote datagen from EDE on atclvm560");
					$dgNfsPath = "/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen";
					$dgServer = "atclvm560.athtem.eei.ericsson.se";
				}
			}
		}

		############################
		# Starting Monitoring EC processes for single blade
		# 
		if (/^MONITOREC/){
			if (!isMultiBladeServer()){
				&FT_LOG("START MONITORING EC PROCESSES\n\n");
				system ("perl /eniq/home/dcuser/automation/ecChecker.pl -start");
			}
		}
		
		if (/^STOPMONITOREC/){
			if (!isMultiBladeServer()){
				&FT_LOG("STOP MONITORING EC PROCESSES\n\n");
				system ("perl /eniq/home/dcuser/automation/ecChecker.pl -stop");
			}
		}
		
		#############################
		# Starting DataGen Features
		#
		
		if(/^STARTDATAGEN_LIKE4LIKE /){
                        $input=~s/STARTDATAGEN_LIKE4LIKE //;
                        $input=~s/ //g;
                        &FT_LOG("START DATAGEN 2G3G,4G LIKE FOR LIKE\n\n");
                        my $result1=dataGenStart_2G3G4G_Like4Like($input);
                        processResults($result1,"DATAGEN START LIKE4LIKE 2G3G and/or 4G:","DATAGENSTART_2G3G4G");
                }
		if(/^STARTDATAGEN /){
			$input=~s/STARTDATAGEN //;
			$input=~s/ //g;
			&FT_LOG("START DATAGEN 2G3G,4G\n\n");
			my $result1=dataGenStart_2G3G4G($input);
			processResults($result1,"DATAGEN START 2G3G and/or 4G:","DATAGENSTART_2G3G4G");
		}
		
		if(/^STARTDATA /){
			my $allSkipped=1;
			$input=~s/STARTDATA //;
			$input=~s/ //g;
			$input=~s/waitifstarting//g;
			if($input=~ m/[234]G/i){
				my $result1=dataGenStart_2G3G4G($input);
				processResults($result1,"DATAGEN START 2G3G and/or 4G:","DATAGENSTART_2G3G4G");
				&FT_LOG("START DATAGEN 2G3G,4G\n\n");
				if($result1 =~ m/Skipping dataGenStart_/){
					$allSkipped=0;
				}
			}
			if($input=~ m/LTE/i){
				my $result1=dataGenStart_LTE($input);
				processResults($result1,"DATA GEN START LTE:","STARTDATAGEN_LTE");
				if($result1 =~ m/Skipping dataGenStart_/){
					$allSkipped=0;
				}
			}
			if($input=~ m/MSS/i){
				my $result1=dataGenStart_MSS($input);
				processResults($result1,"DATA GEN START MSS:","STARTDATAGEN_MSS");
				if($result1 =~ m/Skipping dataGenStart_/){
					$allSkipped=0;
				}
			}
			if($input=~ m/WCDMA/i){
				my $result1=dataGenStart_WCDMA($input);
				processResults($result1,"DATAGEN START WCDMA:","STARTDATAGEN_WCDMA");
				if($result1 =~ m/Skipping dataGenStart_/){
					$allSkipped=0;
				}
			}
			if($input=~ m/KPI/i){
				my $result1=dataGenStart_kpi($input);
				processResults($result1,"DATAGEN START KPI (first table) AND MSS(second table):","STARTDATAGEN_KPI");
				if($result1 =~ m/Skipping dataGenStart_/){
					$allSkipped=0;
				}
			}
			if(!$allSkipped && $input =~m/dowaitif/){
				my $time = ($remoteDG?10:150);#10 mins for remote datagen, 150 mins for local
				&FT_LOG("INFO:Waiting $time minutes for data to load");
				sleep($time * 60);
			}
		}
		
		if(/^STARTDATAGEN_SGEH_DVDT/){
			my $result1=dataGenStart_SGEH_DVDT();
			processResults($result1,"DATA GEN START SGEH_DVDT:","STARTDATAGEN_SGEH_DVDT");
		}
		
		if(/^STARTDATAGEN_MSS/){
			$input=~s/STARTDATAGEN_MSS //;
			$input=~s/ //g;
			my $result1=dataGenStart_MSS($input);
			processResults($result1,"DATA GEN START MSS:","STARTDATAGEN_MSS");
		}
		
		if(/^STARTDATAGEN_LTE/){
			$input=~s/STARTDATAGEN_LTE //;
			$input=~s/ //g;
			my $result1=dataGenStart_LTE($input);
			processResults($result1,"DATA GEN START LTE:","STARTDATAGEN_LTE");
		}
		
		if(/STARTDATAGEN_3GSESSIONBROWSER/){
			$input=~s/STARTDATAGEN_3GSESSIONBROWSER //;
			$input=~s/ //g;
			my $result1=dataGenStart_3GSessionBrowser($input);
			processResults($result1,"DATA GEN START 3G SESSION BROWSER:", "STARTDATAGEN_3GSESSIONBROWSER");
		}
		
		if(/^STARTDATAGEN_DVTP/){
			my $result1=dataGenStart_DVTP();
			processResults($result1,"DATA GEN START DVTP:", "STARTDATAGEN_DVTP");
		}
		
		if(/^STOPDATAGEN_DVTP/){
			dataGenStop_DVTP();
		}
		
		if(/^DATAGENSTART_ARREST_IT/){
			my $result1 = datagenStartForArrest_IT();
			processResults($result1,"DATA GEN START ARREST_IT:", "DATAGENSTART_ARREST_IT");
		}
		
		if(/^RUN_GRIT_TOOL/){
			$input=~s/RUN_GRIT_TOOL//;
			$input=~s/ //g;
			my $result1 = runGritTool($input);
			processResults($result1,"GRIT TESTS:$input", "RUN_GRIT_TOOL");
		}
		if(/^CHANGE_IMSI_SETTING_DEFAULT/){
			changingIMSI_Setting_To_deafult();
		}
		if(/^RUN_LIKE4LIKE/){
			$input=~s/RUN_LIKE4LIKE //;
               		$input=~s/ //g;
			my $result1 = runLike4Like($input);
			processResults($result1,"LIKE4LIKE TSTS:", "RUN_LIKE4LIKE");
		}
		
		if(/^RUNARREST_GEN/){
			$input=~s/RUNARREST_GEN//;
			$input=~s/ //g;
			&FT_LOG("Value of input File: $input \n");
			my $result1 = runArrest_IT_general($input);
			processResults($result1,"ARREST_IT TESTS for $input:", "RUNARREST_IT_GEN");
		}
		
		if(/^REFERENCE_SERVER_IP/){
			$input=~s/REFERENCE_SERVER_IP //;
			$input=~s/ //g;
			$edeESTserver=$input;
		        $edeESTserver=~s/ //g;
			chomp($edeESTserver);
		}
		if(/^TOPOLOGYLOCATION/){
			$input=~s/TOPOLOGYLOCATION //;
			$input=~s/ //g;
			$edeESTTopology=$input;
		        $edeESTTopology=~s/ //g;
			chomp($edeESTTopology);
		}

		if(/^INTERMEDIATELOCATION/){
			$input=~s/INTERMEDIATELOCATION //;
			$input=~s/ //g;
			$edeESTInter=$input;
		        $edeESTInter=~s/ //g;
			chomp($edeESTInter);
			
		}
		if(/^OUTPUTLOCATION/){
			$input=~s/OUTPUTLOCATION //;
			$input=~s/ //g;
			$edeESTOutput=$input;
		        $edeESTOutput=~s/ //g;
			chomp($edeESTOutput);
			
		}
		if(/^EDELOCATIONFORSGEHEST/){
			$input=~s/EDELOCATIONFORSGEHEST //;
			$input=~s/ //g;
			$edeESTLocation=$input;
		        $edeESTLocation=~s/ //g;
			chomp($edeESTLocation);
			
		}

		if(/^RUN_EST_SGEH/){
			$input=~s/RUN_EST_SGEH //;
			$input=~s/ //g;
			@estParam= split(/,/, $input);
			my $result1=runESTSgeh(@estParam);			
		}
		
		if(/^UPDATETOPOLOGY/){
			updateTopology_Lteefa();
		}

                if(/^DELETEUSERPREFRENCE/){
			deletepreferences_Wcdma();
                }
		
		if(/^CHECK_EC_ST_STATUS/){
			$input=~s/CHECK_EC_ST_STATUS //;
            $input=~s/ //g;
            $ecstParam= $input;
			$ecstParam=~s/ //g;
            my $result1=checkECSTStatus($ecstParam);
            processResults($result1,"CHECKING EC_ST_'$ecstParam' STATUS:", "CHECK_EC_ST_'$ecstParam'_STATUS");
		}
		
		if(/^RUN_EDE_LTE_STREAMING/){
			my $result1=runEdeLteStreaming();
			processResults($result1,"LTE STREAMING:", "RUN_EDE_LTE_STREAMING");
		}
		
		if(/^RUN_EDE_KGBLTE_STREAMING/){
            my $result1=runEdeLteStreamingOnVapp();
            processResults($result1,"LTE STREAMING:", "RUN_KGBEDE_LTE_STREAMING");
        }
						
		if(/^EXEC_LTEES_LIKE4LIKE/){
			$input = $referenceServerHostname;
			my $result1 = run_ltees_Like4Like($input);
			processResults($result1,"Like4Like LTE-ES:", "EXEC_LTEES_LIKE4LIKE");
		} 
    
		if(/^RUN_EDE_LIKE4LIKE/){
			$input=~s/RUN_EDE_LIKE4LIKE //;
			$input=~s/ //g;
			@like4likeParam = split(/,/, $input);
			my $result1=runEDELIke4Like();			
		}



		 if(/^EC_MOVEMENT/){
                        my $found=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep 'Ecmovement:YES'`;
                        if($found =~ /Ecmovement/){
                        ec_Movement();
                        print " EC movement needed\n";
                        return;
                        }
                        print " EC movement not required\n";
                }


                if(/^CREATE_DIRECTORY/){
                        create_DirectoryLTEES();
                }

                 if(/^RUN_EST_DATAGEN/){
                        my $res1=runESTonDatagen();
                }


                if(/^RUN_EST/){
                        my $res=runEST();

                }


                if(/^PROVISION_WORKFLOWS_LTEES/){
                        my $res=provisionWorkflowsEST();

                }

                if(/^run_EDE/){
                        addCrontabEST();
                }


		
		## This part will install the EDE on datagen server .
		if(/^INSTALLEDE/){
            executeThisWithLogging("./installEDE");
         }

		
		if(/^REFERENCE_SERVER_IP/){
			$input=~s/REFERENCE_SERVER_IP //;
			$input=~s/ //g;
			$referenceServer =$input;
			
			$referenceServerHostname=`/eniq/home/dcuser/automation/RunCommandAsRoot.sh  nslookup $referenceServer | grep athtem | cut -d "=" -f2 | cut -d "." -f1`;
		        $referenceServerHostname =~s/ //g;
			chomp($referenceServerHostname);
			
		}
		

		#############################
		# Stopping DataGen Features
		#
		# NO RETURN STRING TO PASS TO PROCESS RESULTS SUB
		#
		if(/^STOPDATAGEN_2G3G4G/){
			&FT_LOG("STOP DATAGEN 2G3G4G\n\n");
			my $result1=dataGenStop_2G3G4G();
		}
		
		if(/^STOPDATAGEN_WCDMA/){
			&FT_LOG("STOP DATAGEN WCDMA\n\n");
			my $result1=dataGenStop_WCDMA();
		}

                if(/^RESTORETACS_WCDMA/){
                        &FT_LOG("RESTORE EXCLUSIVE TACS AFTER WCDMA\n\n");
                        my $result1=restoreTACs_WCDMA();
                }

		if(/^STOPDATAGEN_SGEH_DVDT/)		{
			&FT_LOG("STOP DATAGEN SGEH_DVDT\n\n");
			my $result1=dataGenStop_SGEH_DVDT();
		}
		
		if(/^STOPDATAGEN_MSS/){
			&FT_LOG("STOP DATAGEN MSS\n\n");
			my $result1=dataGenStop_MSS();
		}
		
		if(/^STOPDATAGEN_LTE/){
			&FT_LOG("STOP DATAGEN LTE\n\n");
			my $result1=dataGenStop_LTE();
		}
		
		if(/^STOPDATAGEN_KPI/){
			&FT_LOG("STOP DATAGEN KPI\n\n");
			my $result1=dataGenStop_KPI();
		}
		
		if(/^STOPDATAGEN_3GSESSIONBROWSER/){
			&FT_LOG("STOP DATAGEN 3GSESSIONBROWSER\n\n");
			my $result1=dataGenStop_3GSESSIONBROWSER();
		}

		#############################
		# Verify Functions
		#
		if(/^VERIFYLOADING/){
			my $result1=verifyLoadings();
			processResults($result1,"VERIFY DATA LOADING:","VERIFYDATALOADING");
		}
		
		if(/^VERIFYACCURACY/){
			my $result1=verifyAccuracy();
			processResults($result1,"VERIFY DATA LOADING:","VERIFYDATALOADINGACCURACY");
		}
		
		#######################################
		#	WAIT  
		#
		if(/^WAIT /){
			$input=~s/WAIT //;
			my $sleep       =0;
			# TRANSFORM TO MINUTES
			$sleep       =$input*60;
			&FT_LOG("Sleep for $input mins\n");
			sleep($sleep);
		}
		
		if(/^WAIT_UNTIL_HR /){
			$input=~s/WAIT_UNTIL_HR //;
			&FT_LOG("Sleep until $input:00 hrs\n");
			while(1){
				sleep(60);
				my @time = localtime(time);
				last if(($time[2] == $input) && ($time[1] == 0));
			}
			&FT_LOG("CONTINUE\n");
		}
		
		if(/WAIT_UNTIL_PROCESSES_DONE/){
			waitUntilProcessesDone();
		}

		#######################################
		#	 REFRESH TOPOLOGY 
		#
		if(/^REFRESH_TOPOLOGY/){
			$refreshTopology="true";
		}
	
		#######################################
		# Show Loading and Aggregation (8)
		#
		if(/^LISTUPDATE TPINI DISABLE ALL/){
			@tpini    = undef;
		}
		
		if(/^LISTUPDATE TPINI ENABLE ALL/){
			# CHECK TP 
			@tpini    = getAllTechPacks();
		}
		
		if(/^SHOW_LOADINGS/){
			my $result1=verifyLoadings();
			processResults($result1,"VERIFY DATA LOADING:","VERIFYDATALOADING");
		}
		
		if(/^LISTUPDATE AGGINI ENABLE ALL/){
			@aggini= getAllTechPacks();
		}	
		
		if(/^SHOW_AGGREGATION/){
			my $result1=verifyAggregations();
			processResults($result1,"VERIFY DATA AGGREGATIONS:","VERIFYDATAAGGREGATIONS");
		}	
		
		if(/^LISTUPDATE TPINI ENABLE /){
			$input=~s/LISTUPDATE TPINI ENABLE //;
			$input=~s/ //g;
			@tpini = split(/,/, $input);
		}
		
		if(/^LISTUPDATE AGGINI DISABLE ALL/){
			@aggini= undef;
		}
		
		if(/^LISTUPDATE AGGINI ENABLE /){
			$input=~s/LISTUPDATE AGGINI ENABLE //;
			$input=~s/ //g;
			@aggini = split(/,/, $input);
		}	

		###############################
		# Run Selenium UI Tests (Jar file)
		#
		if(/^INIT_SELENIUM/){
			$input=~s/INIT_SELENIUM//;
			$input=~s/ //g;
			$input="DummyTestGroup,selenium_events_tests.jar,$input";
			my @result1=runSelenium($input);
			if($input !~ m/noreport/){
				foreach my $result1 (@result1){
					processResults($result1,"INIT_SELENIUM:","INIT_SELENIUM");
				}
			}
		}
		
		if(/^DO_FIREFOX_VERSION/){
			$input=~s/DO_FIREFOX_VERSION//;
			$input=~s/ //g;
			doFirefoxVersion(split(',',$input));
		}
		
		if(/^STOP_SELENIUM/){
			my @status=executeThisWithLogging("cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient quit;/eniq/sw/runtime/java/bin/java stopHub");
			my @status2=executeThisWithLogging("cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient quitUnix;/eniq/sw/runtime/java/bin/java stopHub");
			sleep(1*20);
		}
		
		if(/^RUN_SELENIUM/){
			$input=~s/RUN_SELENIUM//;
			$input=~s/ //;
			$input=~s/ //g;
			if($input=~ m/CreateDashboardUser/){
				createDashboardUser(0);
			}
			my @result1=runSelenium($input);
			foreach my $result1 (@result1){
				#processResultsForSuits(\@result1,"RUN_SELENIUM - (UI Screenshots stored @ /tmp/ENIQ_Events_UI_Selenium/):","RUN_SELENIUM");
				processResults($result1,"RUN_SELENIUM - (UI Screenshots stored @ /tmp/ENIQ_Events_UI_Selenium/):","RUN_SELENIUM");
			}			
		}
		
		if(/^STARTUPNODESERVICES/){
			$input=~s/STARTUPNODESERVICES //;
			$input=~s/ //g;
			#Returns 1 if war is deployed successfully and 0 if not.
			my $result1=startUpNodeServices($input);
			
			my $resultToPrint = didNodeServicesFail($result1);
			processResults($resultToPrint,"STUB DEPLOYMENT", "START STUB DEPLOY");
			if($result1 == 0){
				tearDownNodeServices();
				exit 0;
			}
			#If 0 is returned the script should stop and not continue on to run selenium tests.
			processResults($result1,"STUB DEPLOYMENT", "START STUB DEPLOY");
		}
		
		if(/^TEARDOWNNODESERVICES/){
			$input=~s/TEARDOWNNODESERVICES //;
			$input=~s/ //g;
			my $result1=tearDownNodeServices();
			processResults($result1,"STUB DEPLOYMENT", "STOP STUB DEPLOY");
		}
		
		if(/^TEST_LIST/)
		{
			$input=~s/TEST_LIST//;
			$input=~s/ //;
			$listOfTestsToRun = $input;
		}
		if (/^SAPARATE_RESULTS/)
		{
			$separateResultsFlag=1;
		}
		
		if(/^SELENIUMUI/){
			my $result1=seleniumUI();
			processResults($result1,"SELENIUM_UI - (UI Screenshots stored @ /tmp/ENIQ_Events_UI_Selenium/)","SELENIUMUI");
		}
		
		if(/^USE_2G3GRESERVEDDATA/){
		    use2g3gReservedData();
		}
		
		if(/^USE_ORIGINALRESERVEDDATA/){
			useOriginalReservedData();
		}
	
		###############################
		# Read Logs (1)
		#
		if(/^READLOG/){
			my $result1=verifyLogs();
			processResults($result1,"LOG VERIFICATION","verifyLogs");
		}
	
		###############################
		# ADMINUI - Configuration (10)
		#
		if(/^MONITORINGRULES/){
			my $result1=MonitoringRules();
			processResults($result1,"MONITORINGRULES","MONITORINGRULES");
		}
		
		if(/^TYPECONFIG/){
			my $result1=TypeConfig();
			processResults($result1,"TYPECONFIG","TYPECONFIG");
		}
		
		if(/^DWHCONFIG/){
			my $result1=DWHConfig();
			processResults($result1,"DWHCONFIG","DWHCONFIG");
		}
		
		if(/^LOGGING_INFO/){
			my $result1=LoggingInfo();
			processResults($result1,"LOGGING_INFO","LOGGING_INFO");
		}
		
		if(/^LOGGING_SEVERE/){
			my $result1=LoggingSevere();
			processResults($result1,"LOGGING_SEVERE","LOGGING_SEVERE");
		}
		
		if(/^LOGGING_WARNING/){
			my $result1=LoggingWarning();
			processResults($result1,"LOGGING_WARNING","LOGGING_WARNING");
		}
		
		if(/^LOGGING_CONFIG/){
			my $result1=LoggingConfig();
			processResults($result1,"LOGGING_CONFIG","LOGGING_CONFIG");
		}
		
		if(/^LOGGING_FINE/){
			my $result1=LoggingFine();
			processResults($result1,"LOGGING_FINE","LOGGING_FINE");
		}
		
		if(/^LOGGING_FINER/){
			my $result1=LoggingFiner();
			processResults($result1,"LOGGING_FINER","LOGGING_FINER");
		}
		
		if(/^LOGGING_FINEST/){
			my $result1=LoggingFinest();
			processResults($result1,"LOGGING_FINEST","LOGGING_FINEST");
		}

		####################################
		# ADMINUI - Data Flow Monitoring (4)
		#	
		if(/^SHOW_LOADING_FUTURE_DATES/){
			my $result1=ShowLoadingFutureDates();
			processResults($result1,"SHOW_LOADING_FUTURE_DATES","SHOW_LOADING_FUTURE_DATES");
		}
		
		if(/^SHOW_PROBLEMATIC/){
			my $result1=ShowProblematic();
			processResults($result1,"SHOW_PROBLEMATIC","SHOW_PROBLEMATIC");
		}
		
		if(/^SHOW_AGG_FUTURE_DATES/){
			my $result1=ShowAggFutureDates();
			processResults($result1,"SHOW_AGG_FUTURE_DATES","SHOW_AGG_FUTURE_DATES");
		}
		
		if(/^REAGGREGATION/){
			$input=~s/REAGGREGATION //;
			$level   =$input;
			my $result1=Reaggregation($level);
			processResults($result1,"REAGGREGATION","Reaggregation");
		}	
		
		if(/^SESSIONLOGS/){
			my $result1=SessionLogs();
			processResults($result1,"SESSIONLOGS","SESSIONLOGS");
		}
		
		if(/^DATASOURCELOGS/){
			my $result1=DataSourceLogs();
			processResults($result1,"DATASOURCELOGS","DATASOURCELOGS");
		}		
		
		####################################
		# ADMINUI - Data Verification (4)
		#		
		if(/^DATAROWINFO/){
			my $result1=DataRowInfo();
			processResults($result1,"DATAROWINFO","DATAROWINFO");
		}
		
		if(/^SHOWREFTABLES/){
			my $result1=ShowRefTables();
			processResults($result1,"SHOWREFTABLES","SHOWREFTABLES");
		}
		
		if(/^RANKBH/){
			my $result1=RankBh();
			processResults($result1,"RANKBH","RANKBH");
		}	
		
		if(/^BUSYHOUR/){
			my $result1=busyhour();
			processResults($result1,"BUSYHOUR","BUSYHOUR");
		}	
		
		if(/^ENGINE_PROCESS/){
			$input         =~ s/^ENGINE_PROCESS //;
			my @in         =split(/\s/,$input);
			$epTp          =$in[0];
			$epProcess     =$in[1];
			&FT_LOG("$epTp $epProcess\n");
			my $result1=engineProcess($epTp,$epProcess);
			processResults($result1,"ENGINE_PROCESS $epTp $epProcess:","engineProcess");
		}

		#############  NOW DO SOMETHING  ##########
	} #/End of forloop for readinmg the Config File

	return $result;
}

#----------------------------------------------------------
#	END Subroutine parseParam()
#----------------------------------------------------------

sub use2g3gReservedData{
	&FT_LOG("Change reservedData.csv to reservedData_original.csv\n");
	executeThisWithLogging("mv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData_original.csv");
	&FT_LOG("Change reservedData2g3g.csv to reservedData.csv\n");
	executeThisWithLogging("mv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData2g3g.csv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv");
}

sub useOriginalReservedData{
	&FT_LOG("Change reservedData.csv to reservedData2g3g.csv\n");
	executeThisWithLogging("mv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData2g3g.csv");
	&FT_LOG("Change reservedData_original.csv to reservedData.csv\n");
	executeThisWithLogging("mv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData_original.csv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv");
}

sub createDashboardUser{
	my $runTests=shift;
	
	&FT_LOG("Creating Dashboard User...\n");
	executeThisWithLogging("cp /eniq/home/dcuser/automation/licences/test.ldif /eniq/home/dcuser/");
	&FT_LOG("Adding LDAP...\n");
	executeThisWithLogging('ldapadd -c -h ldapserver -p 9001 -D "cn=Administrator,dc=ericsson,dc=se" -w ldap -f /eniq/home/dcuser/test.ldif');
	&FT_LOG("Copying Licence to /var/tmp/...\n");
	if( -e "/net/159.107.177.74/eniq/eniq_build/license/ENIQ_FULL_License_Events" ){#use up to date network one if available
		executeThisWithLogging("cp /net/159.107.177.74/eniq/eniq_build/license/ENIQ_FULL_License_Events /var/tmp/");
	}else{#else use one in package
		executeThisWithLogging("cp /eniq/home/dcuser/automation/licences/ENIQ_FULL_License_Events /var/tmp/");
	}
	
	&FT_LOG("Installing Licence...\n");
	executeThisWithLogging("cd /var/tmp/; licmgr -install ENIQ_FULL_License_Events");
	if($runTests==1){
		my $result=runSelenium("CreateDashboardUser");
		return $result;
	}
}

sub sqlSelect{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 80 -b << EOF $sql |");
	my @output=<SEL>;
	chomp(@output);
	close(SEL);
	my @result=();
	my $colsItem=lc($statement);
	$colsItem=~s/select //;
	$colsItem=~s/ from.*//;
	my $colsCount = @{[$colsItem =~ /(,)/g]} +1;
	my $lineCount=0;
	my @buffer=();
	foreach my $line (@output){
		$_=$line;
		next if(/affected/);
		$line=~s/\n//g;
		$line =~ s/^\s+//; #remove leading spaces
		$line =~ s/\s+$//; #remove trailing spaces
		if($line ne ""){
			if($line !~ m/ / && $colsCount>1 && $lineCount<$colsCount-1){
				push(@buffer,$line);
			}else{
				for(my $i=0;$i<$lineCount;$i++){
					if($buffer[$i] ne ""){
						$line="$buffer[$i] $line";
					}
				}
				while ($line =~ m/  /){
					$line=~s/  / /g;
				}
				@buffer=();
				push @result, $line;
			}
			$lineCount++;
			if($lineCount==$colsCount){
				$lineCount=0;
			}
		}
	}
	return @result;
}

sub sqlSelectArray{
	my $statement=shift;
	my $seperator = ":";
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -s$seperator -b << EOF $sql |");
	my @output=<SEL>;
	chomp(@output);
	close(SEL);
	my @result=();
	my @buffer=();
	foreach my $line (grep(/${seperator}/, @output)){
		chomp($line);
		my @values = split(/${seperator}/, $line);
		foreach my $value (@values){
			$value =~ s/^\s+//; #remove leading spaces
			$value =~ s/\s+$//; #remove trailing spaces
			if ($value eq "" ){
				next;
			}
			push(@buffer, $value);
		}
		if ($line=~m/${seperator}$/){#end of current row
			push(@result, [@buffer]);
			@buffer = ();#Reset buffer
		}
	}
	return @result;
}

sub sqlRepdbSelect{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udba -Psql -h0 -Srepdb -w 50 -b << EOF $sql |");
	my @output=<SEL>;
	chomp(@output);
	close(SEL);
	my @result=();
	my $colsItem=lc($statement);
	$colsItem=~s/select //;
	$colsItem=~s/ from.*//;
	my $colsCount = @{[$colsItem =~ /(,)/g]} +1;
	my $lineCount=0;
	my @buffer=();
	foreach my $line (@output){
		$_=$line;
		next if(/affected/);
		$line=~s/\n//g;
		$line =~ s/^\s+//; #remove leading spaces
		$line =~ s/\s+$//; #remove trailing spaces
		if($line ne ""){
			if($line !~ m/ / && $colsCount>1 && $lineCount<$colsCount-1){
				push(@buffer,$line);
			}else{
				for(my $i=0;$i<$lineCount;$i++){
					if($buffer[$i] ne ""){
						$line="$buffer[$i] $line";
					}
				}
				@buffer=();
				push @result, $line;
			}
			$lineCount++;
			if($lineCount==$colsCount){
				$lineCount=0;
			}
		}
	}
	return @result;
}

sub sqlInsert{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @output=<SEL>;
	&FT_LOG("@output \n");
	close(SEL);
}

sub sqlUpdate{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @output=<SEL>;
	close(SEL);
}

sub sqlDelete{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @output=<SEL>;
	close(SEL);
}

sub sqlRepDbDelete{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udba -Psql -h0 -Srepdb -w 50 -b << EOF $sql |");
	my @output=<SEL>;
	close(SEL);
}

sub sqlCreate{
	my $statement=shift;
	my $sql="\n$statement\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @output=<SEL>;
	close(SEL);
}

sub sqlMSSWorkaround{
	&FT_LOG("\n\n\nINFO:DOING MSS WORKAROUND. GET RID OF IN 14A\n\n\n");
	my $statement1 ="
	INSERT
	INTO
	DIM_E_SGEH_TAC
	(
	TAC,
	BAND,
	MARKETING_NAME,
	MANUFACTURER,
	ACCESS_CAPABILITY,
	MODEL,
	VENDOR_NAME,
	UE_TYPE,
	OS,
	INPUT_MODE,
	STATE,
	CREATED_TIME,
	MODIFIED_TIME,
	MODIFIER_NAME
	)
	VALUES
	(
	44123400,
	'GSM 1800, GSM 1900, GSM 900, WCDMA FDD',
	'Cheddar',
	'BABT Manufacturing Co',
	'GSM 1800, GSM 1900, GSM 900, WCDMA FDD',
	'Cheddar',
	'BABT Manufacturing Co',
	'',
	'',
	'',
	'ACTIVE',
	'2012-08-14 10:41:39',
	'2012-08-14 10:41:39',
	'ENIQ_EVENTS'
	)";

	my $statement2 ="INSERT
	INTO
	DIM_E_SGEH_TAC
	(
	TAC,
	BAND,
	MARKETING_NAME,
	MANUFACTURER,
	ACCESS_CAPABILITY,
	MODEL,
	VENDOR_NAME,
	UE_TYPE,
	OS,
	INPUT_MODE,
	STATE,
	CREATED_TIME,
	MODIFIED_TIME,
	MODIFIER_NAME
	)
	VALUES
	(
	44000100,
	'GSM 1800, GSM 900',
	'Thumper',
	'BABT Manufacturing Co',
	'GSM 1800, GSM 900',
	'Thumper',
	'BABT Manufacturing Co',
	'',
	'',
	'',
	'ACTIVE',
	'2012-08-14 10:41:39',
	'2012-08-14 10:41:39',
	'ENIQ_EVENTS'
	)";

	my $statement3 ="update DIM_E_SGEH_HIER321 set VENDOR ='Ericsson' where HIERARCHY_3='BSC1' AND RAT=0";
	my $statement=shift;
	my $sql="\n$statement1\ngo\nEOF";
	my $sql2="\n$statement2\ngo\nEOF";
	my $sql3="\n$statement3\ngo\nEOF";
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql2 |");
	open(SEL,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql3 |");
	my @output=<SEL>;
	close(SEL);
}

#for dataTypes array, give s for string and n for num
sub kpiNotificationGroupsDB{
	my ($tables,$columns,$dataTypes,$destTable, $groupName,$doGroupNum)=@_;
	my $groupNum=1;
	my @uniqueRows=();
	my $commaSeparatedColNames="";
	foreach my $table(@$tables){
		$commaSeparatedColNames="";
		foreach my $col(@$columns){
			if($commaSeparatedColNames ne ""){
				$commaSeparatedColNames.=",";
			}
			$commaSeparatedColNames.=$col;
		}
		my @cols=sqlSelect("select distinct $commaSeparatedColNames from $table");
		my $uniqueLen=@uniqueRows;
		if($uniqueLen>0){
			push(@cols,@uniqueRows);
		}
		my %hash   = map { $_, 1 } @cols;
		@uniqueRows = keys %hash;
	}
	$groupNum=1;
	foreach my $row(@uniqueRows){
		my @rowItems=split(/ /,$row);
		my $whereString="";
		my $valuesString="";
		my $i=0;
		foreach my $column(@rowItems){
			if($whereString ne ""){
				$whereString.=" and ";
			}
			if($valuesString ne ""){
				$valuesString.=",";
			}
			if(lc(@$dataTypes[$i]) =~ m/n/){
				$whereString=$whereString."@$columns[$i]=$column";
				$valuesString=$valuesString."$column";
			}else{
				$whereString=$whereString."@$columns[$i]='$column'";
				$valuesString=$valuesString."'$column'";
			}
			$i++;
		}
		my $select = "SELECT $commaSeparatedColNames FROM $destTable WHERE $whereString";
		my @currentEntries=sqlSelect($select);
		my $currentEntries=@currentEntries;
		my $insert = "INSERT INTO $destTable ($commaSeparatedColNames, group_name) values ($valuesString, '$groupName";
		
		if($doGroupNum==1){
			$insert.="_"."$groupNum')";
		}else{
			$insert.="')";
		}
		if($currentEntries==0){
			&FT_LOG("INFO:Inserting entry: $insert\n");
			sqlInsert($insert);
			$groupNum++;
			if($groupNum==11){
				$groupNum=0;
			}
		}else{
			&FT_LOG("WARN:Entry already exists, not inserting: $insert\n");
		}
	}
}

sub gpmgtXmlStuff{
	my ($tables,$columns,$groupName,$groupType)=@_;
	my $groupNum=1;
	my @uniqueRows=();
	my $commaSeparatedColNames="";
	foreach my $table(@$tables){
		$commaSeparatedColNames="";
		foreach my $col(@$columns){
			if($commaSeparatedColNames ne ""){
				$commaSeparatedColNames.=",";
			}
			$commaSeparatedColNames.=$col;
		}
		my @cols=sqlSelect("select distinct $commaSeparatedColNames from $table");
		my $uniqueLen=@uniqueRows;
		if($uniqueLen>0){
			push(@cols,@uniqueRows);
		}
		my %hash   = map { $_, 1 } @cols;
		@uniqueRows = keys %hash;
	}
	$groupNum=1;
	unlink("/tmp/gpmgtfile.tmp");
	open(TEXT, ">/tmp/gpmgtfile.tmp");
	print TEXT "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <groupmgt>\n";
	close (TEXT);
	foreach my $row(@uniqueRows){
		my @rowItems=split(/ /,$row);
		my $i=0;
		open(TEXT, ">>/tmp/gpmgtfile.tmp");
		print TEXT "  <group name=\"$groupName"."_"."$groupNum\" type=\"".uc($groupType)."\">\n   <group-element>\n";
		foreach my $column(@rowItems){
			print TEXT "     <key name=\"@$columns[$i]\" value=\"$column\"/>\n";
			$i++;
		}
		$groupNum++;
		if($groupNum==11){
			$groupNum=0;
		}
		print TEXT "   </group-element>\n  </group>\n";
		close(TEXT);
	}
	open(TEXT, ">>/tmp/gpmgtfile.tmp");
	print TEXT " </groupmgt>\n";
	close (TEXT);
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add -f /tmp/gpmgtfile.tmp");
	unlink("/tmp/gpmgtfile.tmp");
}

sub twoGThreeGCreateGroups{
	my $timeWarp=getDateWithArg(30);
	#UPDATE RESERVED DATA
	print "INFO: Updating ReservedData.csv with new IMSI info.\n";
	my $reservedDataLine = "IMSI=<IMSI>;TAC=<TAC>;TERMINAL_MAKE=<MAKE>;TERMINAL_MODEL=<MODEL>;APN=<APN>";
	my @cols=sqlSelectArray(qq(SELECT TOP 1 IMSI, EVENT_E_SGEH_SUC_RAW.TAC, MANUFACTURER, MODEL, APN
		FROM EVENT_E_SGEH_SUC_RAW, DIM_E_SGEH_TAC
		WHERE EVENT_E_SGEH_SUC_RAW.TAC = DIM_E_SGEH_TAC.TAC
		AND EVENT_E_SGEH_SUC_RAW.datetime_id > '$timeWarp'
		AND APN != null));	
	foreach my $row (@cols){
		my @row = @$row;
		$reservedDataLine =~ s/<IMSI>/$row[0]/;
		$reservedDataLine =~ s/<TAC>/$row[1]/;
		$reservedDataLine =~ s/<MAKE>/$row[2]/;
		$reservedDataLine =~ s/<MODEL>/$row[3]/;
		$reservedDataLine =~ s/<APN>/$row[4]/;
	}
	
	print "INFO: New values: $reservedDataLine.\n";
	if(-e "$reservedDataLocation"){
		open(CSV,"<$reservedDataLocation");
		my @contents=<CSV>;
		close(CSV);
		my $replace = 0;#false
		foreach my $line (@contents){
			if($line=~m/#All data related to specific IMSI number/){
				$replace = 1;
			}elsif($replace){
				$line=~s/IMSI=.*;TAC=.*;TERMINAL_MAKE=.*;TERMINAL_MODEL=.*;APN=.*/$reservedDataLine/;
				$replace = 0;
				last;
			}
		}
		open(CSV, ">${reservedDataLocation}");
		print CSV @contents;
		close (CSV);
		print "INFO: Succesfully updated Reserved Data file.\n";
	}else{
		&FT_LOG("ERROR: Cannot find ReservedData.csv.\n");
	}
	print "INFO: Creating and importing groups.\n";
	#IMSI GROUPS
	my @imsis = sqlSelectArray("SELECT TOP 3 IMSI FROM EVENT_E_SGEH_SUC_RAW, DIM_E_SGEH_TAC WHERE EVENT_E_SGEH_SUC_RAW.TAC = DIM_E_SGEH_TAC.TAC AND APN != null AND EVENT_E_SGEH_SUC_RAW.datetime_id > '$timeWarp'");	
	my @elements;
	foreach my $imsi (@imsis){
		my @arr = @$imsi;
		my %hash = ("IMSI", $arr[0]);
		push (@elements, {%hash});
	}
	my $IMSIGroup = createGroupXML(0, "DG_ReserveIMSI_group", "IMSI", @elements);
	print "IMSIs XML\n";
	print "$IMSIGroup\n";
	sqlDelete("delete from GROUP_TYPE_E_IMSI where group_name='DG_ReserveIMSI_group'");
	importGroup($IMSIGroup);

	#TAC GROUPS/TERMINAL GROUP
	##my @tacs = sqlSelectArray("SELECT TOP 3 TAC FROM DIM_E_SGEH_TAC ORDER BY CREATED_TIME DESC");
	##  ENC-2262 Modification of EniqEventsRegress.sh script to include Creation of Terminal Group for 2g3g SGEH
	my @tacs = sqlSelectArray("SELECT TOP 3 A.TAC FROM EVENT_E_SGEH_ERR_RAW A,DIM_E_SGEH_TAC WHERE A.TAC = DIM_E_SGEH_TAC.TAC AND APN != null AND A.datetime_id > '$timeWarp'");
	my @elements;
	foreach my $tac (@tacs){
		my @arr = @$tac;
		my %hash = ("tac", $arr[0]);
		push (@elements, {%hash});
	}
	my $TACGroup = createGroupXML(1, "AutomationTerminalGroupTest", "TAC", @elements);
	print "TACs XML\n";
	print "$TACGroup\n";
	sqlDelete("delete from GROUP_TYPE_E_TAC where group_name='AutomationTerminalGroupTest'");
	importGroup($TACGroup);
	
	#APN GROUPS
	my @apns = sqlSelectArray("SELECT TOP 3 APN FROM DIM_E_SGEH_APN ORDER BY LAST_SEEN DESC");
	my @elements;
	foreach my $apn (@apns){
		my @arr = @$apn;
		my %hash = ("apn", $arr[0]);
		push (@elements, {%hash});
	}
	my $APNGroup = createGroupXML(1, "DG_ReserveAPN_group", "APN", @elements);
	print "APNs XML\n";
	print "$APNGroup\n";
	sqlDelete("delete from GROUP_TYPE_E_APN where group_name='DG_ReserveAPN_group'");
	importGroup($APNGroup);
	
	#CONTROLLER
	my @controllers = sqlSelectArray("SELECT TOP 3 RAT, VENDOR, HIERARCHY_3 from DIM_E_SGEH_HIER321");
	my @elements;
	foreach my $controller (@controllers){
		my @arr = @$controller;
		my %hash = ("RAT", $arr[0],
					"VENDOR", $arr[1],
					"HIERARCHY_3", $arr[2]);
		push (@elements, {%hash});
	}
	my $CONTROLLERGroup = createGroupXML(1, "DG_ReserveController_group", "RAT_VEND_HIER3", @elements);
	print "CONTROLLERs XML\n";
	print "$CONTROLLERGroup\n";
	sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER3 where group_name='DG_ReserveController_group'");
	importGroup($CONTROLLERGroup);
	
	#ACCESS AREA
	my @accessAreas = sqlSelectArray("SELECT TOP 3 RAT, VENDOR, HIERARCHY_3, HIERARCHY_1, CELL_ID FROM DIM_E_SGEH_HIER321_CELL");
	my @elements;
	foreach my $accessArea (@accessAreas){
		my @arr = @$accessArea;
		my %hash = ("RAT", $arr[0],
					"VENDOR", $arr[1],
					"HIERARCHY_3", $arr[2],
					"HIERARCHY_1", $arr[3],
					"CELL_ID", $arr[4]);
		push (@elements, {%hash});
	}
	my $ACCESSAREAGroup = createGroupXML(1, "DG_ReserveAccessArea_group", "RAT_VEND_HIER321_CELL", @elements);
	print "ACCESS AREAs XML\n";
	print "$ACCESSAREAGroup\n";
	sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER321_CELL where group_name='DG_ReserveAccessArea_group'");
	importGroup($ACCESSAREAGroup);
	
	#SGSN
	my @SGSNs = sqlSelectArray("SELECT TOP 3 SGSN_NAME FROM DIM_E_SGEH_SGSN ORDER BY CREATED DESC");
	my @elements;
	foreach my $SGSN (@SGSNs){
		my @arr = @$SGSN;
		my %hash = ("EVENT_SOURCE_NAME", $arr[0]);
		push (@elements, {%hash});
	}
	my $SGSNGroup = createGroupXML(1, "DG_ReserveSGSN_group", "EVNTSRC", @elements);
	print "SGSNs XML\n";
	print "$SGSNGroup\n";
	sqlDelete("delete from GROUP_TYPE_E_EVNTSRC where group_name='DG_ReserveSGSN_group'");
	importGroup($SGSNGroup);
		
	print "INFO: Done Importing Groups.\n";
}

#Elements in an array of hash maps.
# Hashmap key gets set to group element name and value gets put to group element value
sub createGroupXML{
	my $standalone = shift;
	my $name = shift;
	my $type = shift;
	my @elements = @_;
	my $xml;
	if($standalone){
		$xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<groupmgt>\n";
	}else{
		$xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<groupmgt>\n";
	}
	$xml .= "\t<group name=\"$name\" type=\"$type\">\n";
	foreach my $element (@elements){
		$xml .="\n\t\t<group-element>\n";
		foreach my $key (keys %$element){
			$xml .= "\t\t\t<key name=\"$key\" value=\"" . $element->{$key} . "\"/>\n";
		}
		$xml .="\t\t</group-element>\n";
	}
	$xml .="\n\t</group>\n";
	$xml .="</groupmgt>\n";
	return $xml;
}

sub importGroup{
	my $xml = shift;
	open( TMP, ">/tmp/importGroup.xml" );
	print TMP $xml;
	close( TMP );
	print "IMPORTING...\n";
	executeThisQuiet("/eniq/sw/bin/gpmgt -i -add -f /tmp/importGroup.xml");
	unlink("/tmp/importGroup.xml");
}

sub ismapsInstalled{
	my $input = shift;
	my $result="";
	my $verbose = "";
	
	$verbose.=qq{
		<h3>Is Maps Installed?</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
				<th>CMD</th>
				<th>DESCRIPTION</th>
				<th>STATUS</th>
			</tr>
	};
	
	my $isInstalled=runCommand("ssh dcuser\@glassfish 'psql -l -U postgres postgres | grep rnc'",0);

	if ($isInstalled > 0){
		print "maps installed";
	}else{
		loadMaps();
	}
	$isInstalled=runCommand("ssh dcuser\@glassfish 'psql -l -U postgres postgres | grep rnc'",0);
	$verbose .= "<tr><td>Check postgres tables</td><td>Search for RNC cell tables</td><td>".($isInstalled?"<font color=darkblue><b>PASS</b></font>":"<font color=red><b>FAIL</b></font>")."</td></tr>";
	
	print $input ."\n";
	$verbose.=qq{</TABLE>};
	$result .= $verbose;
	return $result;
}

sub loadMaps{my $mapsFile = "natural_earth_raster_map_R2A02.tar.gz";
                my $maps = "/eniq/opengeo_data/natural_earth_raster_map".$mapsFile;

                #&FT_LOG("INFO: Get Maps...");
                runCommand("ssh dcuser\@glassfish '/eniq/home/dcuser/automation/RunCommandAsRoot.sh mkdir -p /eniq/opengeo_data/natural_earth_raster_map'",1);
                runCommand("ssh dcuser\@glassfish '/eniq/home/dcuser/automation/RunCommandAsRoot.sh chmod 777 /eniq/opengeo_data/natural_earth_raster_map'",1);
                runCommand("ssh dcuser\@glassfish 'cp /net/atclvm559.athtem.eei.ericsson.se/package/maps/natural_earth_raster_map_R2A02.tar.gz /eniq/opengeo_data/natural_earth_raster_map'",1);
                runCommand("ssh dcuser\@glassfish '/eniq/home/dcuser/automation/RunCommandAsRoot.sh chown dcuser:dc5000 /eniq/opengeo_data/natural_earth_raster_map/natural_earth_raster_map_R2A02.tar'",1);
                sleep 180;
				###Leave this sleep to allow file to transfer
                chmod 0755, $maps;

                #Unzipping Maps Files... ;
                executeThisWithLogging("ssh dcuser\@glassfish 'gunzip -f /eniq/opengeo_data/natural_earth_raster_map/$mapsFile -d /eniq/opengeo_data/natural_earth_raster_map'");
                &FT_LOG("INFO: Untaring maps files...");
                runCommand("ssh dcuser\@glassfish 'cd /eniq/opengeo_data/natural_earth_raster_map;tar -xvf /eniq/opengeo_data/natural_earth_raster_map/natural_earth_raster_map_R2A02.tar; cd /eniq/home/dcuser/automation;'",0);
                sleep 180;
				###Leave this sleep to allow file to transfer

                #Disabling HTTPS as maps is wanted ;
                #&enableDisableHTTPS("disable");

                &FT_LOG("INFO: Initial Maps setup...");
                runCommand("ssh dcuser\@glassfish 'cd /eniq/opengeo_data/natural_earth_raster_map/; ./setup.sh geoserver.properties; cd /eniq/home/dcuser/automation;'",0);

                # Setting up RNC cells...;
                runCommand("ssh dcuser\@glassfish 'cd /eniq/opengeo_data/natural_earth_raster_map/; ./setup_RNC_Cell.sh geoserver.properties; cd /eniq/home/dcuser/automation;'",0);

                # INFO: Getting geo CSV files... ;
                runCommand("ssh dcuser\@glassfish 'cp /net/atclvm559.athtem.eei.ericsson.se/package/maps/AmericaCells.csv /eniq/opengeo_data/natural_earth_raster_map/'",1);
                runCommand("ssh dcuser\@glassfish 'cp /net/atclvm559.athtem.eei.ericsson.se/package/maps/AmericaRncs13_9.csv /eniq/opengeo_data/natural_earth_raster_map/'",1);

                &FT_LOG("INFO: Uploading CSV with geolocations...");
                runCommand("ssh dcuser\@glassfish 'cd /eniq/opengeo_data/natural_earth_raster_map/; ./uploadData.sh -c AmericaCells.csv -r AmericaRncs13_9.csv -w y; cd /eniq/home/dcuser/automation;'",0);

                #print "\nRe-enabling HTTPS";
                #&enableDisableHTTPS("enable");
}


sub lteCreateGroups{	
	my $imsiGroup='<group name="LTE_Group" type="IMSI">';
	my $rat3Group='<group name="LTE_Group" type="RAT_VEND_HIER3">';
	my $rat321Group='<group name="LTE_Group" type="RAT_VEND_HIER321">';
	my $tracGroup='<group name="LTETrackingAreaGroup" type="LTE_TRAC">';
	my $resDataImsiLine="IMSI_LTE=var1;IMSI_GROUP_LTE=LTE_Group;IMSI_GROUP_DATA_LTE=var2;";
	my $resDataController="CONTROLLER_LTE=var1;CONTROLLER_GROUP_LTE=LTE_Group;CONTROLLER_GROUP_DATA_LTE=var2;";
	my $resDataAccessArea="ACCESS_AREA_LTE=var1;ACCESS_AREA_GROUP_LTE=LTE_Group;ACCESS_AREA_GROUP_DATA_LTE=var2;";
	my $resDataTrackingArea="TRACKING_AREA_LTE=var1;TRACKING_AREA_GROUP_LTE=LTETrackingAreaGroup;TRACKING_AREA_GROUP_DATA_LTE=var2;";
	my $firstImsi="";
	my $imsiData="";
	my $firstController="";
	my $controllerData="";
	my $firstAccessArea="";
	my $accessAreaData="";
	my $firstTrackingArea="";
	my $trackingAreaData="";	
    my $terminalGroup='<group name="LTE_Group" type="TAC">';
    my $firstTerminal="";
    my $terminalData="";
	
	my $firstRun=1;
	my $timeWarp=getDateWithArg(30);
	my @versions=("13B","16A","17A");
	foreach (@versions){
		my $statement="select top 1 imsi,hier321_id from dc.EVENT_E_LTE_CFA_ERR_RAW where ne_version='$_' and ne_version!=null and imsi!=null and hier321_id!=null and datetime_id>'$timeWarp'";
		&FT_LOG("INFO:$statement");
		my @cols=sqlSelect($statement);
		if(@cols){
			&FT_LOG("INFO:SQL result:");
			foreach(@cols){
				&FT_LOG("$_");
			}
			my @imsiAndCellId=split(/ /,$cols[0]);
			$statement="select top 1 HIERARCHY_3,HIERARCHY_1 from DIM_E_LTE_HIER321 where hier321_id=$imsiAndCellId[1] and HIERARCHY_3!=null and HIERARCHY_1!=null";
			&FT_LOG("INFO:$statement");
			@cols=sqlSelect($statement);
			if(@cols){
				&FT_LOG("INFO:SQL result:");
				foreach(@cols){
					&FT_LOG("$_");
				}
				my @controllerAndCellName=split(/ /,$cols[0]);
				if($imsiGroup !~ m/$imsiAndCellId[0]/){
					$imsiGroup.=qq{
						<group-element>
							<key name="IMSI" value="$imsiAndCellId[0]"/>
						</group-element>
					};
					if($firstRun){
						$firstImsi=$imsiAndCellId[0];
						$imsiData=$imsiAndCellId[0];
					}else{
						$imsiData.=",$imsiAndCellId[0]";
					}
				}
				if($rat3Group !~ m/$controllerAndCellName[0]/){
					$rat3Group.=qq{
						<group-element>
							<key value="$controllerAndCellName[0]" name="HIERARCHY_3"/>
							<key value="2" name="RAT"/>
							<key value="Ericsson" name="VENDOR"/>
						</group-element>
					};
					if($firstRun){
						$firstController=$controllerAndCellName[0];
						$controllerData=$controllerAndCellName[0];
					}else{
						$controllerData.=",$controllerAndCellName[0]";
					}
				}
				if($rat321Group !~ m/$controllerAndCellName[1]/){
					$rat321Group.=qq{
						<group-element>
							<key value="$controllerAndCellName[0]" name="HIERARCHY_3"/>
							<key value="$controllerAndCellName[1]" name="HIERARCHY_1"/>
							<key value="2" name="RAT"/>
							<key value="Ericsson" name="VENDOR"/>
						</group-element>
					};					
					if($firstRun){
						$firstAccessArea="$controllerAndCellName[1],,$controllerAndCellName[0]";
						$accessAreaData=$controllerAndCellName[1];
					}else{
						$accessAreaData.=",$controllerAndCellName[1]";
					}
				}
				$firstRun=0;
			}
		}
	}
	
	my $statement="select distinct trac from EVENT_E_LTE_CFA_ERR_RAW where trac!=null and datetime_id>'$timeWarp'";
	&FT_LOG("INFO:$statement");
	my @cols=sqlSelect($statement);
	if(@cols){
		&FT_LOG("INFO:SQL result:");
	}
	
	foreach my $col(@cols){
		&FT_LOG("$col");
		if ($col =~ /^[0-9]+$/) {
			if($firstTrackingArea==""){
				$firstTrackingArea=$col;
			}
			$trackingAreaData.="$col,";
			$tracGroup.=qq{
				<group-element>
					<key value ="$col" name="trac"/>
				</group-element>
			};
		}
	}
	
	# ENC-2221 :Modification of EniqEventsRegress.sh script to include Creation of Terminal Group for LTE
    my $statement="select distinct top 10 TAC from EVENT_E_LTE_HFA_ERR_RAW where tac!=null and datetime_id>'$timeWarp'";
	&FT_LOG("INFO:$statement");
	my @cols=sqlSelect($statement);
       
	if(@cols){
		&FT_LOG("INFO:SQL result:");
	}
    foreach my $col(@cols){
		&FT_LOG("$col");
		if ($col =~ /^[0-9]+$/) {
			if($firstTerminal==""){
				$firstTerminal=$col;
			}
			$terminalData.="$col,";
			$terminalGroup.=qq{
				<group-element>
					<key value ="$col" name="tac"/>
				</group-element>
			};
		}
		}	
	
	$trackingAreaData=~s/,$//;
	$imsiGroup.="\n	</group>";
	$rat3Group.="\n	</group>";
	$rat321Group.="\n	</group>";
	$tracGroup.="\n	</group>";
    $terminalGroup.="\n </group>";
	
	$resDataImsiLine=~s/var1/$firstImsi/;
	$resDataImsiLine=~s/var2/$imsiData/;
	
	$resDataController=~s/var1/$firstController/;
	$resDataController=~s/var2/$controllerData/;
	
	$resDataAccessArea=~s/var1/$firstAccessArea/;
	$resDataAccessArea=~s/var2/$accessAreaData/;
	
	$resDataTrackingArea=~s/var1/$firstTrackingArea/;
	$resDataTrackingArea=~s/var2/$trackingAreaData/;
	
	my $foundValues=1;
	if(length($imsiGroup)<62){
		&FT_LOG("ERROR:No IMSIs found for LTE IMSI group");
		$foundValues=0;
	}
	if(length($rat3Group)<62){
		&FT_LOG("ERROR:No controller found for LTE controller group");
		$foundValues=0;
	}
	if(length($rat321Group)<62){
		&FT_LOG("ERROR:No controller or cell found for LTE controller group");
		$foundValues=0;
	}
	if(length($tracGroup)<62){
		&FT_LOG("ERROR:No trac values found for LTE tracking area group");
		$foundValues=0;
	}
	if($foundValues){
		if(-e "$reservedDataLocation"){
			open(CSV,"<$reservedDataLocation");
			my @contents=<CSV>;
			close(CSV);
			foreach(@contents){
				s/IMSI_LTE.*$/$resDataImsiLine/;
				s/CONTROLLER_LTE.*$/$resDataController/;
				s/ACCESS_AREA_LTE.*$/$resDataAccessArea/;
				s/TRACKING_AREA_LTE.*$/$resDataTrackingArea/;
				s/VERSION_12A_SUPPORTED.*$/VERSION_12A_SUPPORTED=no;VERSION_11B_SUPPORTED=no;VERSION_12B_SUPPORTED=no;VERSION_13A_SUPPORTED=no;VERSION_13B_SUPPORTED=yes;/;
			}
			open(CSV, ">$reservedDataLocation");
			print CSV @contents;
			close (CSV);
		}else{
			&FT_LOG("ERROR:Could not find reserved data file at location: $reservedDataLocation");
		}
		sqlDelete("delete from GROUP_TYPE_E_LTE_TRAC where group_name='LTETrackingAreaGroup'");
		sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER321 where group_name='LTE_Group'");
		sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER3 where group_name='LTE_Group'");
		sqlDelete("delete from GROUP_TYPE_E_IMSI where group_name='LTE_Group'");
        sqlDelete("delete from GROUP_TYPE_E_TAC where group_name='LTE_Group'");
		my $groups=qq{<?xml version="1.0" encoding="UTF-8"?>
			<groupmgt>
				$imsiGroup
				$rat3Group
				$rat321Group
				$tracGroup
                $terminalGroup
			</groupmgt>
		};
		open(TEXT, ">/tmp/gpmgtfile.tmp");
		print TEXT $groups;
		close (TEXT);
		&FT_LOG("INFO:Creating groups using XML:");
		&FT_LOG($groups);
		executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add -f /tmp/gpmgtfile.tmp");
		unlink("/tmp/gpmgtfile.tmp");
	}
}

sub kpiPhase1CreateGroups{
	#Add distinct MCCs and MNCs from dc.event_e_sgeh_err_raw and dc.event_e_lte_err_raw to dc.group_type_e_mcc_mnc if they haven't already been added
	my @tables=("dc.event_e_sgeh_err_raw","dc.event_e_lte_err_raw");
	my @columns=("mcc","mnc");
	my @dataTypes=("s","s");
	kpiNotificationGroupsDB(\@tables,\@columns,\@dataTypes,"dc.group_type_e_mcc_mnc","DG_GroupNameMCCMNC",1);
	
	#Add distinct event_source_name from dc.event_e_sgeh_err_raw and dc.event_e_lte_err_raw to dc.group_type_e_evntsrc if they haven't already been added
	@tables=("dc.event_e_sgeh_err_raw","dc.event_e_lte_err_raw");
	@columns=("event_source_name");
	@dataTypes=("s");
	kpiNotificationGroupsDB(\@tables,\@columns,\@dataTypes,"dc.group_type_e_evntsrc","DG_GroupNameEVENTSRC",1);

	#Add distinct event_source_name and evntsrc_id from tables in @tables array to dc.DIM_E_MSS_EVNTSRC if they haven't already been added
	@tables=("dc.DIM_E_MSS_EVNTSRC");
	@columns=("event_source_name","evntsrc_id");
	@dataTypes=("s","n");
	kpiNotificationGroupsDB(\@tables,\@columns,\@dataTypes,"dc.GROUP_TYPE_E_EVNTSRC_CS","DG_GroupNameMSS",1);

	#Add distinct imsi from tables in @tables array to dc.group_type_e_imsi if they haven't already been added
	@tables=("dc.event_e_sgeh_err_raw","dc.event_e_lte_err_raw","dc.EVENT_E_MSS_LOC_SERVICE_CDR_suc_RAW","EVENT_E_MSS_SMS_CDR_ERR_RAW","dc.EVENT_E_MSS_VOICE_CDR_ERR_RAW");
	@columns=("imsi");
	gpmgtXmlStuff(\@tables,\@columns,"DG_GroupNameIMSI","IMSI");
}

sub runKpiPhase1{
	my $autoPath="/eniq/home/dcuser/automation";
	my $host = hostname;
    my $hostURL = "$host.$domain";
	&FT_LOG("Running KPI Phase 1\n");
	my $kpiPath = "$autoPath/test_automation_kpi_notification";		
	my @status1=executeThisWithLogging("/eniq/sw/runtime/java/bin/java -DHOST=$hostURL -jar $kpiPath/kpiNotificationAutomation.jar");
	
	#delay for 5 seconds to make sure file is present
	&FT_LOG("Delaying for 5 seconds\n");
	sleep 5;
	
	my @statusNEWEST=grep(/A.*\.htm$/, ls("$kpiPath/target/kpi_automation/", "lt"));
	#my @statusNEWEST=executeThisWithLogging("ls -t  $kpiPath/target/kpi_automation/A*.htm | head -1"); #UPDATED
	my $fileName = $statusNEWEST[0];
	chomp($fileName);
	
	#log file
	$kpi_results = "kpiLog_$run_date.log";
	
	copy("$kpiPath/target/kpi_automation/kpi_automation.log.0", "/eniq/home/dcuser/automation/RegressionLogs/$kpi_results");
	
	my $result = "";
	my @exe=();
	if(!-e $fileName){
		&FT_LOG("ERROR:$fileName does not exist");
	}else{
		open( FILE, $fileName);
		@exe=<FILE>;
		close FILE;
		chomp(@exe);
	}
	
	$result = join("",@exe);
	#<TABLE.*?>(.*?)</TABLE>
	
	$result =~ s/(.*)<h3/<h3/;
	$result =~ s/<\/TABLE(.*)/<\/TABLE>/;
	
	#more regex for appearance (coppied from LTEES)
	$result =~ s/ BGCOLOR.*?>/>/g;
	$result =~ s/<FONT.*?>//g;
	$result =~ s/<\/FONT>//g;
	$result =~ s/ ALIGN.*?>/>/g;
	$result =~ s/<A HREF.*?>//g;
	$result =~ s/<B>//g;
	$result =~ s/<\/B>//g;
	
	$result =~ s%<font color="red"><b>FAIL</b></font>%<font color=#ff0000><b>FAIL<\/b><\/font>%g;
	$result =~ s%<font color='darkblue'><b>PASS</b></font>%<font color=darkblue><b>PASS<\/b><\/font>%g;
	my @tableFill = split(/(<tr>)/,$result);
	foreach my $line (@tableFill){
		$line =~ s%<font color='red'><b>FAIL</b></font></td><td>(.*?)</td><td>NoEvents</td>%<font color=orange><b>WARNING</b></font></b></font></td><td>$1</td><td>NoEvents</td>%g;
		$line =~ s%<font color='red'><b>FAIL</b></font></td><td>(.*?)</td><td>MissingEvents</td>%<font color=orange><b>WARNING</b></font></b></font></td><td>$1</td><td>MissingEvents</td>%g;
	}
	$result = join("",@tableFill);
	if($result eq ""){
		$result="<h3>KPI Tests<\/h3><br><TABLE BORDER = 1 CELLSPACING = 2 CELLPADDING = 2><tr><td><font color=#ff0000><b>SEVERE FAIL<\/b><\/font></td></tr></TABLE>";
	}
	return $result;
}

sub getRunDate{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
	return sprintf "%4d%02d%02d%02d%02d", $year+1900,$mon+1,$mday,$hour,$min;
}

#
# RUN_LTEES
#
sub runLtees{
	my $cm_ltees = shift;
	my @stopped=();
	my $runDate = getRunDate();
	my $host=hostname;
	my $ecXMLPath="/eniq/mediation_sw/mediation_gw/etc";
	my $ecMemPath="/eniq/mediation_inter/etc";
	my $ecXMLModifyPath="/eniq/home/dcuser/automation/ltees";
	my $backupFile = "executioncontext.xml-BACKUP-${runDate}";
	my $ecSingleFile="/eniq/home/dcuser/automation/ltees/executioncontextSingle.xml";
	&FT_LOG("INFO:Params: $lteesParamList[0] $lteesParamList[1] $lteesParamList[2]");
	
	setCounterStatus();
	my @ecltees=executeThisWithLogging("cat /eniq/sw/conf/service_names | grep ec_ltees_* | awk -F':' '{print \$5}'");
	print "The number of ec_ltees is ".@ecltees."\n";
	
	my $numOfParams=0;
	foreach my $val (@lteesParamList) {
		$numOfParams++;
	}
	
	my $verifyCounters="true";
		
	if($numOfParams==3){
	       	if($lteesParamList[2] eq "NoVerification"){
	               	$verifyCounters="false";
		}
	}
	        
        print "verifyCounters flag is set to ".$verifyCounters;
	
	if(!isLteesOnlyServer()){
		&FT_LOG("INFO:Running LTE ES\nDisable non-relevant workflows");
		#Disable non-EBSL || non-Sim workflows
		my @wf = ("EBSL","Sim");
		@stopped = disableAllWorkflows(@wf);
		
		if(!isMultiBladeServer()){		
			if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
			
		       if ($verifyCounters eq "true"){
					&FT_LOG("INFO: Now shutdown non-relevant ECs, and restart EC_LTEES ECs");
					foreach my $i(@ecltees){
						chomp($i);
						executeThisWithLogging("ssh dcuser\@${i} 'source ~/.profile; ec stop'");
					}

					executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEEFA_1 '");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEEFA_2 '");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEEFA_3'");
					executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_SGEH_1 '");
				}
			}
	
			foreach my $i(@ecltees){
			chomp($i);
			executeThisWithLogging("ssh dcuser\@${i} 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr restart ${i} '");
			}                     
		}
		&FT_LOG("EC process setup:");
		&FT_LOG("INFO: Check ECs' status");
		foreach my $i(@ecltees){
			chomp($i);
			executeThisWithLogging("ssh dcuser\@${i} 'source ~/.profile; ec status' ");
		}
		
		&FT_LOG("INFO: Wait 2 mins to refresh the ECs' status again");
		sleep 120;	###Leave this sleep to allow workflows to disable
		my @contents;
		
		foreach my $i(@ecltees){
			chomp($i);
			push @contents,executeThisWithLogging("ssh dcuser\@${i} 'source ~/.profile; ec status'");
		}
		
		if(isMultiBladeServer() && grep(/EC_LTEES_\d is not running/,@contents)){
			&FT_LOG("Some or all of the LTEES EC processes are not running. Running ec restart");
				
				if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
			
				foreach my $i(@ecltees){
					chomp($i);
					executeThisWithLogging("ssh dcuser\@${i} 'source ~/.profile; ec restart'");
					executeThisWithLogging("ssh dcuser\@${i} 'source ~/.profile; ec status'");
				}
			}			
		}
		foreach my $i(@ecltees){
			chomp($i);
			executeThisWithLogging("ssh dcuser\@${i} 'ps -ef | grep Xmx'");
		}	
	}
	&FT_LOG("INFO:Initial cleanup...");
	my $ran1 = 0;
	my $ran2 = 0;
	my $lteesPath = "/eniq/northbound/test_automation_ltees";
	executeThisWithLogging("rm -r /eniq/northbound/test_automation_ltees /eniq/northbound/lte_event_stat_file");
	if ($verifyCounters eq "true"){
		# STOP LTEES MZ DATAGEN, Not needed for Counter Verification
		executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable \"Sim1*\"");
		executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop -immediate \"Sim1*\"");
		executeThisWithLogging("rm -rf /eniq/upgrade/automation");
	}
	
	executeThisQuiet("ssh dcuser\@ec_ltees_1 'rm -r /eniq/data/eventdata/events_oss_*/lteRbsCellTrace/* /eniq/data/eventdata/events_oss_*/lteTopologyData/*'");
	executeThisQuiet("ssh dcuser\@ec_ltees_2 'rm -r /eniq/data/eventdata/events_oss_*/lteRbsCellTrace/* /eniq/data/eventdata/events_oss_*/lteTopologyData/*'");
	executeThisQuiet("ssh dcuser\@ec_ltees_3 'rm -r /eniq/data/eventdata/events_oss_*/lteRbsCellTrace/* /eniq/data/eventdata/events_oss_*/lteTopologyData/*'");
	executeThisQuiet("ssh dcuser\@ec_ltees_4 'rm -r /eniq/data/eventdata/events_oss_*/lteRbsCellTrace/* /eniq/data/eventdata/events_oss_*/lteTopologyData/*'");
	executeThisQuiet("ssh dcuser\@ec_ltees_8 'rm -r /eniq/data/eventdata/events_oss_*/lteRbsCellTrace/* /eniq/data/eventdata/events_oss_*/lteTopologyData/*'");

	&FT_LOG("INFO:More cleanup");
	
	#update the files needed and add directory below because ltees tests neeed this folder
	my @preparationFirst=executeThisWithLogging("mkdir -p /eniq/northbound/lte_event_stat_file");
	my $propertyFile="/eniq/home/dcuser/automation/ltees/app.properties";#latest property file used 
	my $hostInfo = hostname;
	if(-e $propertyFile){
		print "Copy latest app.properties file to fetch files from remote server\n";
		#update configration for VAPP environment
		if ($hostInfo =~ /eniqe/)
		{
			&FT_LOG("INFO: Test environment is VApp, updates will be applied on app.properties");
			open (FILE, "< $propertyFile") or die "Could not open $propertyFile";
			my @content = <FILE>;
			close FILE;
			open (FILE, "> $propertyFile") or die "Could not open $propertyFile";
			foreach (@content){
				if ($_ =~ /Unix_Host/){
					$_ =~ s/Unix_Host.*$/Unix_Host=$resultServer/;
				}elsif ($_ =~ /MZ_Server_Host/){
					$_ =~ s/MZ_Server_Host.*$/MZ_Server_Host=.vts.com/;
				}
				print FILE $_;
			}
			close (FILE);
		}
		copy("$propertyFile", "/eniq/home/dcuser/automation/test_automation_ltees");
	}
	my @preparation1=executeThisWithLogging("mkdir -p /eniq/northbound/test_automation_ltees");
	my @preparation2=executeThisWithLogging("cp -r /eniq/home/dcuser/automation/test_automation_ltees/* $lteesPath");	
	if ($verifyCounters eq "true"){
		my @preparation3=executeThisWithLogging("mkdir -p /eniq/upgrade/automation");
		foreach my $i(@ecltees){
			chomp($i);
			executeThisWithLogging("ssh dcuser\@${i} 'ln -s /eniq/upgrade/automation /eniq/data/eventdata'");
		}
	}
        my $result="";
	
	#Check the EBSL workflows were enabled or not
	my @EBSLwfNum=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist EBSL* | wc -l");
	$EBSLwfNum[0] =~ s/ //g;
	my @enableWfResult=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable EBSL* |grep Already| wc -l");
	$enableWfResult[0] =~ s/ //g;
	&FT_LOG("Total Num of EBSL worflows: $EBSLwfNum[0]\nTotal Num of EBSL worflows already enabled: $enableWfResult[0]");
	if ($EBSLwfNum[0] > $enableWfResult[0]) {
		&FT_LOG("WARN: The EBSL workflows were not totaly enabled! Enabled them just now");
	}
	
	print "/eniq/sw/runtime/java/bin/java -jar $lteesPath/lteesAutomation.jar $lteesParamList[0] $lteesParamList[1] $lteesParamList[2]";
	opendir ( JAVADIR, "/eniq/sw/runtime/" ) || print "/eniq/sw/runtime/ not found\n";
	while(my $JavaJDK = readdir(JAVADIR)){
		if($JavaJDK =~ /jdk1.6/){
			my @status1=executeThisWithLogging("/eniq/sw/runtime/$JavaJDK/bin/java -jar $lteesPath/lteesAutomation.jar $lteesParamList[0] $lteesParamList[1] $lteesParamList[2]");
			last;
		}elsif($JavaJDK =~ /jdk/){
			my @status1=executeThisWithLogging("/eniq/sw/runtime/$JavaJDK/bin/java -jar $lteesPath/lteesAutomation.jar $lteesParamList[0] $lteesParamList[1] $lteesParamList[2]");
			last;
		}
	}
	closedir JAVADIR;
	my @statusNEWEST= ls("$lteesPath/result_archive", "t");
	my $newestString = $statusNEWEST[0];
	chomp($newestString);
	my $stringWithDashesForCMD = $newestString;
	$stringWithDashesForCMD =~ s/ /\\ /g;
	$stringWithDashesForCMD =~ s/:/\\:/g;
	my $lteesResultPath = "$lteesPath/result_archive/$newestString";
	my $lteesResultPathForCMD = "$lteesPath/result_archive/$stringWithDashesForCMD";
	$ltees_results = "lteesLog_$run_date.log";
	$ltees_counterlog = "lteesCounterLog_$run_date.log";
	my @testTypes=ls($lteesResultPath);
	my $XMLFileValues;
	my @XMLValues;
	my $comparisonValues;
	my @calculatedValues;
	my $failedcountergroup;
	foreach my $testTypesString(@testTypes){
		chomp($testTypesString);
		print("\n$lteesResultPathForCMD/$testTypesString\n");
		#my @statusLS=executeThisWithLogging("cd $lteesResultPathForCMD/$testTypesString; ls *Results.html");
		my @statusLS=grep(/Results.html$/, ls("$lteesResultPath/$testTypesString"));
		#create empty files
		open(TEXT, ">>/eniq/home/dcuser/automation/RegressionLogs/$ltees_results");
		print TEXT "\n###################################\n\n$testTypesString\n\n###################################\n";
		close (TEXT);
		open(TEXT, ">>/eniq/home/dcuser/automation/RegressionLogs/$ltees_counterlog");
		print TEXT "\n###################################\n\n$testTypesString\n\n###################################\n";
		close (TEXT);
		my @statusCP=executeThisWithLogging("cp $lteesResultPathForCMD/$testTypesString/Topology*Logfile.log /eniq/home/dcuser/automation/RegressionLogs/NEW$ltees_results");
		my @statusCounterCP=executeThisWithLogging("cp $lteesResultPathForCMD/$testTypesString/Counter*Logfile.log /eniq/home/dcuser/automation/RegressionLogs/NEW$ltees_counterlog");
		
		my @concatLog1=executeThisWithLogging("mv /eniq/home/dcuser/automation/RegressionLogs/$ltees_results /eniq/home/dcuser/automation/RegressionLogs/OLD$ltees_results");
		my @concatLog2=executeThisWithLogging("cat /eniq/home/dcuser/automation/RegressionLogs/OLD$ltees_results /eniq/home/dcuser/automation/RegressionLogs/NEW$ltees_results > /eniq/home/dcuser/automation/RegressionLogs/$ltees_results");
		my @concatLog3=executeThisWithLogging("rm /eniq/home/dcuser/automation/RegressionLogs/OLD$ltees_results");
		my @concatLog4=executeThisWithLogging("rm /eniq/home/dcuser/automation/RegressionLogs/NEW$ltees_results");	

		my @concatCounterLog1=executeThisWithLogging("mv /eniq/home/dcuser/automation/RegressionLogs/$ltees_counterlog /eniq/home/dcuser/automation/RegressionLogs/OLD$ltees_counterlog");
		my @concatCounterLog2=executeThisWithLogging("cat /eniq/home/dcuser/automation/RegressionLogs/OLD$ltees_counterlog /eniq/home/dcuser/automation/RegressionLogs/NEW$ltees_counterlog > /eniq/home/dcuser/automation/RegressionLogs/$ltees_counterlog");
		my @concatCounterLog3=executeThisWithLogging("rm /eniq/home/dcuser/automation/RegressionLogs/OLD$ltees_counterlog");
		my @concatCounterLog4=executeThisWithLogging("rm /eniq/home/dcuser/automation/RegressionLogs/NEW$ltees_counterlog");
				
		##Below changes are made in the result display of Ltees feature. Jira EQEV-34460
		my $lteesCounterFilePath = "$lteesResultPathForCMD/$testTypesString";
		chomp($lteesCounterFilePath);
		#########For TOPOLOGY ############
		$result.="<h4>$testTypesString Topology</h4>";
		$result.=qq{
			<h3>$testTypesString Topology</h3>
			<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
			<th>Topology TestCase</th>
			<th>Event_OSS_1</th>
			<th>Event_OSS_2</th>
			<th>Event_OSS_3</th>
			<th>Event_OSS_4</th>
			</tr>
		};
		for(my $i=1;$i<=4;$i++){
			system("sed -n -e '/OSS-RC: EVENTS_OSS_$i/,/Overall Results/p' $lteesCounterFilePath/Topology\\ Logfile.log > /tmp/topology_events_oss_$i");
			system("cat /tmp/topology_events_oss_$i | grep '^Result' | cut -d '(' -f2 |cut -d ')' -f1 | perl -pi -e 's/>//g' | perl -pi -e 's/<//g'> /tmp/topology_test_cases");
		}
		open(TOPOLOGYVERIFY,"/tmp/topology_test_cases");
		my @verifytopology=<TOPOLOGYVERIFY>;
		close(TOPOLOGYVERIFY);
		foreach my $topologyverify (@verifytopology){
			chomp($topologyverify);
			$result.="<tr>";
			$result.="<td align=center>$topologyverify</td>";
			for(my $i=1;$i<=4;$i++){
				my $topologyresult = `cat /tmp/topology_events_oss_$i | grep '$topologyverify' | cut -d ':' -f2`;
				chomp($topologyresult);
				if ($topologyresult =~ /FAIL/){
					$result.="<td align=center><b><font color=ff0000>FAIL</b></font></td>";
				}	
				else{
					$result.="<td align=center><b><font color=darkblue>PASS</b></font></td>";			
				}
			}
			$result.="</tr>";
		}
		$result.="</TABLE>";
		$result.="</br>";
		
		#########For Counters#########
		system("rm -rf /tmp/failedCounters_*");
		print "LTEES counter file path is $lteesCounterFilePath \n";
		system("sed -n -e '/List Of Counters/,/Overall Counter/p' $lteesCounterFilePath/Counter\\ Logfile.log > /tmp/counter_tested");
		for (my $i=1;$i<=4;$i++){
			if($cm_ltees eq "RUN_CM_LTEES"){
				system("sed -n -e '/EVENTS_OSS_$i/,/List Of/p' $lteesCounterFilePath/Counter\\ Logfile.log > /tmp/cm_events_oss_$i");
				system("cat /tmp/cm_events_oss_$i | egrep -v 'does NOT exist' | egrep '^pm' | cut -d ',' -f1 | sort | uniq > /tmp/counterGenerated_$i");
			}
			else{
				system("rm -rf /tmp/failedCounters_$i");
				system("sed -n -e '/EVENTS_OSS_$i/,/List Of/p' $lteesCounterFilePath/Counter\\ Logfile.log > /tmp/ltees_events_oss_$i");
				system("grep 'Verify' /tmp/ltees_events_oss_$i > /tmp/verifycounter_$i");
				open(VERIFYCOUNTER,"/tmp/verifycounter_$i");
				my @verifycounter=<VERIFYCOUNTER>;
				close(VERIFYCOUNTER);
				foreach my $verifycount (@verifycounter) {
					chomp($verifycount);
					system("sed -n -e '/$verifycount/,/Cell4/p' /tmp/ltees_events_oss_$i > /tmp/verify_fail_$i");
					$XMLFileValues = `cat /tmp/verify_fail_$i | grep XML  | cut -d '-' -f2`;
					chomp($XMLFileValues);
					$comparisonValues = `cat /tmp/verify_fail_$i | grep 'Calculated Values' |cut -d '-' -f2`;
					chomp($comparisonValues);
					@XMLValues = split(/,/,$XMLFileValues);
					@calculatedValues = split(/,/,$comparisonValues);
					chomp(@XMLValues);
					chomp(@calculatedValues);
					for(my $j=1;$j<=@XMLValues;$j++){
						if($XMLValues[$j] != $calculatedValues[$j])
						{
							$failedcountergroup = `cat /tmp/verify_fail_$i | head -1 |cut -d ' ' -f2`;
							chomp($failedcountergroup);
							my $k = $j + 1;
							system("cat /tmp/ltees_events_oss_$i | grep $failedcountergroup | sed -n '$k p' >> /tmp/failedCounters_$i");
						}
					}
				}
			}
		}
		
		system("cat /tmp/counter_tested | sort | uniq > /tmp/counterTest");
		system("cat /eniq/mediation_inter/M_E_LTEES/config/counter_conf/counterConfig.prop | egrep '^pm' > /tmp/totalCounters");
		if($cm_ltees eq "RUN_CM_LTEES"){
			$result.="<h4>$testTypesString Counter Management</h4>";
			$result.="<p><b>Only the counters which are ON in file '/eniq/mediation_inter/M_E_LTEES/config/counter_conf/counterConfig.prop' are generated, and all other counters will not be generated</b></p>";
			$result.=qq{
				<h3>$testTypesString Counter Management</h3>
				<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
				<tr>
				<th>Counter_Name</th>
				<th>Event_OSS_1</th>
				<th>Event_OSS_2</th>
				<th>Event_OSS_3</th>
				<th>Event_OSS_4</th>
				</tr>
			};
			open(TOTALCOUNTERS,"/tmp/totalCounters");
			my @totalCounters=<TOTALCOUNTERS>;
			close(TOTALCOUNTERS);
			foreach my $cmName (@totalCounters){
				chomp($cmName);
				my ($countername,$counterstatus) = split(/=/,$cmName);
				$result.="<tr>";
				$result.="<td align=center>$countername</td>";
				for (my $i=1;$i<=4;$i++){
					
					if($counterstatus =~ /ON/){
						if(grepFileLtees("$countername","/tmp/counterGenerated_$i")){
							$result.="<td align=center><b><font color=darkblue>PASS</b></font></td>";
						}
						else{
							$result.="<td align=center><b><font color=ff0000>FAIL</b></font></td>";
						}
					}
					else{
						if (grepFileLtees("$countername","/tmp/counterGenerated_$i")){
							$result.="<td align=center><b><font color=ff0000>FAIL</b></font></td>";
						}
						else{
							$result.="<td align=center><b><font color=orange>Counter Not Generated</b></font></td>";
						}
					}
				}
			}
		}
		else{
			$result.="<h4>$testTypesString Counters</h4>";
			$result.=qq{
				<h3>$testTypesString Counters</h3>
				<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
				<tr>
				<th>Counter_Name</th>
				<th>Event_OSS_1</th>
				<th>Event_OSS_2</th>
				<th>Event_OSS_3</th>
				<th>Event_OSS_4</th>
				</tr>
			};
			open(COUNTERFILE,"/tmp/counterTest");
			my @countertested =<COUNTERFILE>;
			close(COUNTERFILE);
			foreach my $counterName (@countertested)
			{
				next if ($counterName =~ m/List Of|Overall Counter|^$/);
				chomp($counterName);
				$result.="<tr>";
				$result.="<td align=center>$counterName</td>";
				for(my $i=1;$i<=4;$i++){
					system("ls /tmp/failedCounters_$i > /dev/null 2>&1");
					if($? == 0){
						if (grepFileLtees("$counterName","/tmp/failedCounters_$i")){
							$result.="<td align=center><b><font color=ff0000>FAIL</b></font></td>";
						}
						else{
							$result.="<td align=center><b><font color=darkblue>PASS</b></font></td>";				
						}
					}
					else{
						$result.="<td align=center><b><font color=darkblue>PASS</b></font></td>";				
					}
				}
				$result.="</tr>";
			}
		}
		$result.="</TABLE>";
		$result.="</br>";
	}
	if(@stopped){
		executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");
	}
	system("perl -i -pe 's/=ON/=OFF/ig' $counterConfigPath/counterConfig.prop");
 	return $result;
}

sub grepFileLtees{
	my $pattern = shift;
	my $file = shift;
	my $returnvalue = `grep -w $pattern $file`;
	return $returnvalue;
}

sub logAndGenerateTableEntryLTEES{
	my ($message,$status)=@_;
	if($status==0){
		&FT_LOG("ERROR:$message - ERROR\n");
		return "<tr><td><font color=ff0000><b>$message</b></font></td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
	}else{
		&FT_LOG("INFO:$message - SUCCESS\n");
		return "<tr><td>$message</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}
}

sub setCounterStatus{
	my %keyValue;
	my @array = ();
	my $element;
	my $lineno;
	open my $in, "$counterConfigPath/counterConfig.prop" or die $!;
	while(<$in>) {
		$keyValue{$1}=$2 while m/(\S+)=(\S+)/g;
		if($2 eq 'ON' ){
			push(@array, $1);
		}
	}
	close $in;

	system("perl -i -pe 's/=Y/=N/ig' $counterPropertiesPath/counter.properties");

	open my $in1, "$counterPropertiesPath/counter.properties" or die $!;
	while(<$in1>) {
		$keyValue{$1}=$2 while m/(\S+)=(\S+)/g;
		if($2 eq 'N' )
		{
			$element=$1;
			if (grep {$_ eq $element} @array) {
				$lineno=$.;
				system("perl -i -pe 's/=N/=Y/ig if $lineno .. $lineno'  $counterPropertiesPath/counter.properties");
			}
		}
	}
	close $in1;
}

sub setSelectiveCounterStatus{
	my %keyValue9;
	my @array9 = ();
	my $element9;
	my $lineno9;
	my $releaseVersion=$lteesParamList[0];

	open my $in, "$counterPropertiesPath/counterManagement.prop" or die $!;
	while(<$in>){
		$keyValue9{$1}=$2 while m/(\S+)=(\S+)/g;
		if($releaseVersion eq 'ALLPMS' ){
			push(@array9, $2);
		}
		elsif($1 eq $releaseVersion ){
			push(@array9, $2);
		}
	}
	close $in;
	open my $in1, "$counterConfigPath/counterConfig.prop" or die $!;
	while(<$in1>) {
		$keyValue9{$1}=$2 while m/(\S+)=(\S+)/g;
		if($2 eq 'OFF' ){
			$element9=$1;
			if (grep {$_ eq $element9} @array9) {
				$lineno9=$.;
				system("perl -i -pe 's/=OFF/=ON/ig if $lineno9 .. $lineno9' $counterConfigPath/counterConfig.prop");
			}
		}
	}
	close $in1;
}

#GRIT_LTEES_VTOC

sub runLtees_vtoc{
	my $vtoc_release = shift;
	chomp ($vtoc_release);
	&FT_LOG("\n\nvtoc_release = $vtoc_release\n\n");
	#EQEV-33768:As a EE Engineer I want to include test suite for LTEES counters testing.
	#Initializing all variables.
	my $remote_path_ltees_vtoc = "/net/atclvm559.athtem.eei.ericsson.se/package/";
	my $vtocPath = "/eniq/home/dcuser/automation/LTEES_VTOC/VTOC/";
	my $hostname = getHostName();
	my $config_path = "/eniq/home/dcuser/VTOC";
	my $CTRS_vtoc = "$vtocPath/$vtoc_release/CTRS";
	my $TOPOLOGY_vtoc = "$vtocPath/$vtoc_release/TOPOLOGY";
	my $COUNTER_vtoc = "$vtocPath/$vtoc_release/COUNTER";
	
	&FT_LOG("\n\nInitial Cleanup\n\n");
	#executeThisWithLogging("rm -rf /eniq/home/dcuser/automation/LTEES_VTOC/VTOC");
	
	my @ecltees=executeThisWithLogging("cat /etc/hosts | grep ec_ltees | awk '{print \$2}' | uniq");
	&FT_LOG("\nThe number of EC's on which ltees is deployed are '.@ecltees.'\n");
	foreach my $ec(@ecltees){
		chomp($ec);
		executeThisWithLogging("ssh dcuser\@${ec} 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS /eniq/data/eventdata/events_oss_1/lteTopologyData/dir1'");
		executeThisWithLogging("ssh dcuser\@${ec} 'rm -rf /eniq/northbound/lte_event_stat_file");
	}
	&FT_LOG("\n\nMaking required directories\n");
	
	foreach my $ec(@ecltees){
		chomp($ec);
		executeThisWithLogging("ssh dcuser\@${ec} 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir1 /eniq/data/eventdata/events_oss_1/lteTopologyData/dir1'");
		executeThisWithLogging("ssh dcuser\@${ec} 'mkdir -p /eniq/northbound/lte_event_stat_file/events_oss_1/dir1/");
	}

	###Copy VTOC.zip from atclvm559 server and unzip
	if ( !-d "$vtocPath" ) {
		&FT_LOG("\nInside IF\n");
		executeThisWithLogging("mkdir -p /eniq/home/dcuser/automation/LTEES_VTOC/");
		executeThisWithLogging("chmod -R 777 /eniq/home/dcuser/automation/LTEES_VTOC/ ");
		
		&FT_LOG("INFO: LTEES_VTOC directory created \n");
		###Copy the VTOC zip from 559 server and unzip it
		executeThisWithLogging("cp $remote_path_ltees_vtoc/VTOC*.zip /eniq/home/dcuser/automation/LTEES_VTOC/");
		executeThisWithLogging("cd /eniq/home/dcuser/automation/LTEES_VTOC/; unzip -o /eniq/home/dcuser/automation/LTEES_VTOC/VTOC*.zip");
		executeThisWithLogging("rm -rf /eniq/home/dcuser/automation/LTEES_VTOC/*.zip");
		
		#Updating value in VTOC.sh
		executeThisWithLogging("perl -pi -e 's/read options/options=1/g' $vtocPath/bin/VTOC.sh");
		executeThisWithLogging("perl -pi -e 's/CheckRunningProcess/#CheckRunningProcess/g' $vtocPath/bin/VTOC.sh");
		executeThisWithLogging("perl -pi -e 's/function #CheckRunningProcess/function CheckRunningProcess/g' $vtocPath/bin/VTOC.sh");
		&FT_LOG("INFO: VTOC.sh updated \n");
	}

		##Make VTOC files directory for 17A and 17A_1 releases
		executeThisWithLogging("mkdir -p $vtocPath/$vtoc_release");
		executeThisWithLogging("cp -r $remote_path_ltees_vtoc/VTOC_Files/$vtoc_release/* $vtocPath/$vtoc_release/");
		executeThisWithLogging("rm -rf $vtocPath/$vtoc_release/COUNTER/* ");

		##### UPDATING CONFIG FILE #######
		executeThisWithLogging("chmod 777 /eniq/home/dcuser/automation/Config_VTOC.txt");
		executeThisWithLogging("cp /eniq/home/dcuser/automation/Config_VTOC.txt $vtocPath/config/Config.txt");
		my @replacehostname = `grep -i 'hostname=' $vtocPath/config/Config.txt`;
		foreach my $finalhost(@replacehostname){
		chomp($finalhost);
		if($hostname eq "eniqe"){
			executeThisWithLogging("perl -pi -e 's/$finalhost/hostName=$hostname.vts.com/g' $vtocPath/config/Config.txt");
			print "Updated the config file for vApp";
		}else{
			executeThisWithLogging("perl -pi -e 's/$finalhost/hostName=$hostname.athtem.eei.ericsson.se/g' $vtocPath/config/Config.txt");
		}
		&FT_LOG("\nINFO: Hostname in config file = $finalhost\n");
		}

	######Setting cronjob entry to copy the northbound files to COUNTER directory
		executeThisWithLogging("crontab -l > /tmp/tmp_cron.txt");
		executeThisWithLogging("echo '00,05,10,15,20,25,30,35,40,45,50,55 * * * * cp /eniq/northbound/lte_event_stat_file/events_oss_1/dir1/A2016*.gz $COUNTER_vtoc/' >> /tmp/tmp_cron.txt");
		executeThisWithLogging("crontab /tmp/tmp_cron.txt");
		&FT_LOG("\n\nUpdated the cron job\n\n");
	
	####Provision LTEES workflows for stream based input####
	executeThisWithLogging("/eniq/home/dcuser/automation/LTEESprovision.sh");
	FT_LOG("\nWaiting for 10 minutes\n");
	sleep(600);
	changeTime("$vtoc_release");
	#####Copying input files from VTOC directory to ltees input directory##
	foreach my $ec(@ecltees){
		chomp($ec);
		executeThisWithLogging("ssh dcuser\@${ec} 'cp $TOPOLOGY_vtoc/*.xml /eniq/data/eventdata/events_oss_1/lteTopologyData/dir1/'");
		##wait for topology files to get processed.
		&FT_LOG("\nWaiting for 10 minutes to process topology files\n");
		sleep(600);
		executeThisWithLogging("ssh dcuser\@${ec} 'cp $CTRS_vtoc/*.gz /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir1/'");
		&FT_LOG("\nWaiting for 10 minutes to process LTEES input files\n");
		sleep(600);
	}
	
	##### Remove cron entry
		executeThisWithLogging("crontab -l | grep -v COUNTER > /tmp/tmp_cron2.txt");
		executeThisWithLogging("crontab /tmp/tmp_cron2.txt");
		&FT_LOG("\n\nremoved the cron entry\n\n");
	
	##### Execute VTOC.sh script ####
	executeThisWithLogging("cd /eniq/home/dcuser/automation/LTEES_VTOC/VTOC/bin; ./VTOC.sh -a startvtoc -r $vtoc_release -v Y");

	### printing the html Page
	$gritcsvlogs = "Full_CSV_$feature.txt";
        executeThisWithLogging("rm -rf /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
        executeThisWithLogging("touch /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
        my @sampleFile = `cat /eniq/home/dcuser/automation/gritcsv_template`;
        system("ls /eniq/home/dcuser/automation/LTEES_VTOC/VTOC/results/$vtoc_release > /tmp/ltees_dir");
        open(CSVDIR,"/tmp/ltees_dir");
        my @Dirs=<CSVDIR>;
        close(CSVDIR);
        foreach my $dircsv (@Dirs) {
                chomp ($dircsv);
                my $csvdir = "/eniq/home/dcuser/automation/LTEES_VTOC/VTOC/results/$vtoc_release/$dircsv/CSV";
                chomp($csvdir);
                system("ls $csvdir |grep -v CSV | egrep -v -e '$^' > /tmp/csvdir");
                open(CSVFILE, "/tmp/csvdir") || die "Can not open file ";
                my @Files=<CSVFILE>;
                close(CSVFILE);
                foreach my $file (@Files) {
                        chomp($file);
                        my $csvfile = "$csvdir/$file";
                        executeThisWithLogging("cat $csvdir/$file >> /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
						executeThisWithLogging("echo '' >> /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
                open(GRITCSV, $csvfile) || die "Can not open file $csvfile";
                my @Lines=<GRITCSV>;
            close(GRITCSV);
                        $file =~ s/.csv//;
            $result.=qq{
                <h3>Event_$file</h3>
                <TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
                <tr>
                <th>Event_Id/In Table</th>
                <th>Counter/Field_Tested</th>
                <th>Result</th>
                </tr>
            };
                foreach my $line (@Lines) {
                    #next if ($line =~ m/Event_Id|Updating Alias/);
					next if ($line =~ m/Expected|Actual|Field_Tested|Updating Alias/);
                    my ($InTable,$OutTable,$Field_Tested,$Rule,$Result,$Left_Status,$Right_Status,$Left_Verbose,$Right_Verbose) = split(/,/,$line);
                $result.="<tr>";

                if (grep(/InTable/,@sampleFile)){
                                        $result.="<td align=center>$InTable</td>";
                }
                if (grep(/OutTable/,@sampleFile)){
                                        $result.="<td align=center>$OutTable</td>";
                }
                if (grep(/Field_Tested/,@sampleFile)){
                                        $result.="<td align=center>$Field_Tested</td>";
                                }
                if (grep(/Rule/,@sampleFile)){
                                        $result.="<td align=center>$Rule</td>";
                }
                if (grep(/Result/,@sampleFile)){
                                        if ($Result =~ /FAIL/m){
                                                $result.="<td align=center><b><font color=ff0000>$Result</b></font></td>";
                                        }
										elsif($Result =~ /PASS/m){
                                                $result.="<td align=center><b><font color=darkblue>$Result</b></font></td>";
                                        }
                                else{
                                        $result.="<td align=center><b>$Result<b></td>";
                                        }
                                }
                if (grep(/Left_Verbose/,@sampleFile)){
                                        $result.="<td align=center>$Left_Verbose</td>";
                }
                if (grep(/Right_Verbose/,@sampleFile)){
                                        $result.="<td align=center>$Right_Verbose</td>";
                }
                $result.="</tr>";
            }
                        $result.="</TABLE>";
            $result.="</br>";
                }
        }
        system("rm -rf /tmp/ltees_dir /tmp/csvdir");
        return $result;
}

sub changeTime{
	my $release = shift;
	#Update date of input files
	my $currHour = `date +%H`;
	print "\n1. $currHour\n";
	my $PrevHour = $currHour - 1;
	if($PrevHour < 10){
		$PrevHour = "0$PrevHour";
	}
	print "\n2. $PrevHour\n";
	opendir(ctr,"/eniq/home/dcuser/automation/LTEES_VTOC/VTOC/$release/CTRS/");
	while(my $file = readdir(ctr)){
		if($file =~ /A2016/){
			chomp($file);
			my $hour1 = substr("$file", 10, 2);
			system("mv /eniq/home/dcuser/automation/LTEES_VTOC/VTOC/17A_1/CTRS/$file `echo /eniq/home/dcuser/automation/LTEES_VTOC/VTOC/17A_1/CTRS/$file | sed 's/-$hour1/-$PrevHour/g' | sed 's/A20160531.$hour1/A20160531.$PrevHour/g' | sed 's/A20160918.$hour1/A20160918.$PrevHour/g'`");
		}
	}
	close(ctr);
	opendir(topology,"/eniq/home/dcuser/automation/LTEES_VTOC/VTOC/$release/TOPOLOGY/");
	while(my $file = readdir(topology)){
		if($file =~ /2015/){
			chomp($file);
			my $hour1 = substr("$file", 9, 2);
			system("mv /eniq/home/dcuser/automation/LTEES_VTOC/VTOC/17A_1/TOPOLOGY/$file `echo /eniq/home/dcuser/automation/LTEES_VTOC/VTOC/17A_1/TOPOLOGY/$file | sed 's/.$hour1/.$PrevHour/g'`");
		}
	}
	close(topology);
}

sub runATOMDB{

	my $dgServer="atclvm560.athtem.eei.ericsson.se";
	my $dgPath="/tmp/CentralDatagen";
	my $dgNfsPath="/net/$dgServer$dgPath";
	my $host= getHostName();
	my $hostname = hostname;
	my $sgeh_dir="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	my $remote_sgeh_dir="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	my @table_names=("ERR_RAW","SUC_RAW","ERR_EXTENDED_RAW");
	&FT_LOG("INFO: Database cleanup before running ATOMDB");
	my $err_tables_query = "select 'delete from '||Table_name|| ' where EVENT_SOURCE_NAME = \"MME09\" ;' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_LTE_ERR_RAW%';";
	my $err_extended_tables_query = "select 'delete from '||Table_name|| ' where EVENT_SOURCE_NAME = \"MME09\" ;' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_LTE_ERR_EXTENDED_RAW%';";
	my $suc_tables_query = "select 'delete from '||Table_name|| ' where EVENT_SOURCE_NAME = \"MME09\" ;' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_LTE_SUC_RAW%';";
	
	my @err_delete_queries = sqlSelect($err_tables_query);
	my @err_extended_delete_queries = sqlSelect($err_extended_tables_query);
	my @suc_delete_queries = sqlSelect($suc_tables_query);
	
	my @delete_queries = ( @err_delete_queries,@err_extended_delete_queries,@suc_delete_queries);
	for my $delete_query (@delete_queries) {
		print "Delete Query: $delete_query\n";
		sqlDelete($delete_query);
	}
	sqlDelete("delete from event_e_lte_err_dubcheck");
	sqlDelete("delete from event_e_lte_err_extended_dubcheck");
	sqlDelete("delete from event_e_lte_suc_dubcheck");

	&FT_LOG("INFO: Update Topology Table MCC and MNC information");	
	sqlUpdate("update DIM_E_LTE_HIER321 set MCC = '348' where MCC = '440'");	
	sqlUpdate("update DIM_E_LTE_HIER321 set MNC = '770' where MNC = '20'");

	#Remove any previously stored logs or reports
	FT_LOG("INFO: Deleting any existing atomDB installation\n");
	executeThisWithLogging("cd atomdb; rm -rf logs reports bin setup;");


	&FT_LOG("INFO: Unzip and Untar AtomDB package\n");
	executeThisWithLogging("cd atomdb; gunzip AtomDB.tar.gz -c | tar xf - ; chmod 777 *");
	
	&FT_LOG("INFO: Update Database Information in AtomDB properties file");
	my $file = "/eniq/home/dcuser/automation/atomdb/setup/database/dbconnection-remoteserver.prop";
	my $host=hostname;
	my $newHost;
	if($host eq "eniqe"){		
		$newHost = $host.".vts.com";	
	}	
	else{		
		$newHost = $host.".athtem.eei.ericsson.se";	
	}

	my $setupHost=`cat $file | grep "dburl" | cut -f4 -d":"`;	
	my $dbFilePassword=`cat $file | grep "password="`;
	chomp($setupHost);
	chomp($dbFilePassword);
	my $newpswd = "password="."$temp_pwd";
	chomp($newpswd);
	executeThisWithLogging("perl -pi -e 's/$setupHost/$newHost/g' $file");
	executeThisWithLogging("perl -pi -e 's/$dbFilePassword/$newpswd/g' $file");	
	
	$file = "/eniq/home/dcuser/automation/atomdb/setup/database/dbconnection-local.prop";
	my $dbLocalPasswd=`cat $file | grep "password="`;
	chomp($dbLocalPasswd);
	print "dbLocalPasswd ::: $dbLocalPasswd \n";
	executeThisWithLogging("perl -pi -e 's/$dbLocalPasswd/$newpswd/g' $file");
	
	&FT_LOG("INFO: Stop EC and GlassFish Services Glassfish Services...(This may take a few mins\n");
	runCommand("ssh dcuser\@ec_sgeh_1 ' source ~/.profile; ec stop '",1);
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish stop '",1);
	
	&FT_LOG("INFO: Update MZ and Glassfish Files for verifying EVENT_E_LTE_SUC_RAW table\n");
	$file = "/eniq/mediation_inter/M_E_SGEH/etc/configuration.prop";  
	open (IN, $file) || die "Cannot open file ".$file." for read";
	my @lines=<IN>;
	close IN;
	my $line='';
	open (OUT, ">", $file) || die "Cannot open file ".$file." for write";
	foreach $line (@lines){
		$line =~ s/SUCCESS_EVENT_DROP=ON/SUCCESS_EVENT_DROP=OFF/ig;
		$line =~ s/SUCCESS_DATA_HANDLING=AGGREGATES/SUCCESS_DATA_HANDLING=ROP_RAW/ig;
		print OUT $line;
	}    
	close OUT;
        
	$file = "/eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml";
	open (IN, $file) || die "Cannot open file ".$file." for read";
	my @input=<IN>;
	close IN;
	my $input='';
	open (OUT, ">", $file) || die "Cannot open file ".$file." for write";
	foreach $input (@input)
	{
	   $input =~ s/\"ENIQ_EVENTS_SUC_RAW\" value=\"false\"/\"ENIQ_EVENTS_SUC_RAW\" value=\"true\"/ig;
	   print OUT $input;
	}
        close OUT;
    
	&FT_LOG("INFO: Re-starting glassfish and EC.... (This may take a few mins)\n");
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish start '",1);
	runCommand("ssh dcuser\@ec_sgeh_1 ' source ~/.profile; ec start '",1);

	&FT_LOG("INFO: Remove existing link to SGEH Datagen\n");
	my $linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh | grep ^l'",0);
	
	if($linkExists){
		&FT_LOG("INFO:/eniq/data/eventdata/events_oss_1/sgeh is linked to central datagen. Remove this softlink");
		executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh 2>/dev/null'");
	}
	
	executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir{1..5}'",1);
	&FT_LOG("INFO: Creating folders/eniq/data/eventdata/events_oss_1/sgeh/dir1...dir5");	
	&FT_LOG("INFO: DISABLING SGEH WOKFLOWS/GROUPS AND ENABLING SGEH WORKFLOWS/GROUPS FOR ATOMDB TESTING, TAKES 2 MINS");

	my @wfGroupsToDisable=(
		"SGEH.WFG*",
		"SGEH.WG*");
		
	executeThisQuiet("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable @wfGroupsToDisable'");
	executeThisQuiet("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop @wfGroupsToDisable'");

	sleep(60);

	my @wfsToEnable=(
		"SGEH.WF00_ParsingLog_Inter.logging",
		"SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir*",
		"SGEH.WF00_ParsingLog_Inter.logging");

	executeThisQuiet("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @wfsToEnable'");
	
	my @wfGroupsToEnable=(
		"SGEH.WFG_Cell_Lookup_Refresh_DB",
		"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_1*",
		"SGEH.WG00_LogParsing_Inter");
	
	executeThisQuiet("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable @wfGroupsToEnable'");
	
	sleep(60);
	&FT_LOG("INFO: STARTING ATOMDB TESTS");
	executeThisWithLogging("mkdir -p /tmp/Atomdb");
	executeThisWithLogging("cp /net/atclvm559.athtem.eei.ericsson.se/ossrc/package/Atomdb/* /tmp/Atomdb");	
	my $file_name = `ls /tmp/Atomdb/*`;
	chomp $file_name;
	executeThisWithLogging("bash /eniq/home/dcuser/automation/atomdb/bin/run_atomdb.bsh -j $file_name -a TRACE -d /net/atclvm560.athtem.eei.ericsson.se/edefiles/AtomDB/inter/temp");
	my $check_status=$?;
	my $reports = '/eniq/home/dcuser/automation/atomdb/reports/ebs';
	my $overallResult=qq{
	<h3>RUN LTEES</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		   <tr>
			<th>TEST STAGES</th>
			 <th>STATUS</th>
		   </tr>
	};
	if($check_status == 0){
		opendir(DIR,$reports);
		my @files = grep ! m/^\./ , readdir(DIR);
		closedir(DIR);
		my $count = 0;
		my @failed_table=();
		my @passed_table=();
		my $tablename = '';
		
		foreach(@table_names){
			my $event="EVENT_E_LTE_";
			my $table_name=$_;
			my @match=grep(/$table_name/,@files);
			$table_name="$event$table_name";
			if(@match){
				push(@failed_table,$table_name);
				$count ++;
			}
			else{
				push(@passed_table,$table_name);
			}
		}
	
		foreach(@failed_table){
			my $filename =  $_;
			my @testnames = split(/\./,$filename);
			$tablename = @testnames[0];
			my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
			$overallResult.="<tr><td><a href='$url/logs/$filename'>$tablename</a></td><td align=center><font color=darkblue><b>FAIL</b></font></td></tr>\n";
			&FT_LOG("ERROR: $tablename - FAIL\n");
		}
	
		foreach(@passed_table){
			my $passed=$_;
			my @result=();
			my $res = verifyDataGenLoadingAtomdb("$passed","EVENT_SOURCE_NAME","MME09");
			my @result=$res;
			my @result = split(/ /,$res);
			if($result[0] eq "Error:"){
				$overallResult.="<tr><td>$res</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			}
			else{
			$overallResult.="<tr><td>$res</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			&FT_LOG("INFO: EVENT_E_LTE_$passed - PASS\n");
			}
		}		
			
		if($count != 0){
			executeThisWithLogging("mv /eniq/home/dcuser/automation/atomdb/reports/ebs/* /eniq/home/dcuser/automation/RegressionLogs/");
		}
	}
	else{
		&FT_LOG("_processed Directory not present");
	}
	
	&FT_LOG("INFO: CLEANUP - Putting server back to the way it was prior to AtomDB test case was run");
	
	if($linkExists){
		&FT_LOG("INFO:Deleting /eniq/data/eventdata/events_oss_1/sgeh directory and recreating softlink to Central Datagen");
		executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh 2>/dev/null'");
		executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir3'",1);
		&FT_LOG("INFO:re-linking /eniq/data/eventdata/events_oss_1/sgeh to central datagen. Creating softlink");
		my $success=runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
		if($success==1){
			&FT_LOG("INFO:Symbolic link created to $remote_sgeh_dir at $sgeh_dir on ec_sgeh_1");
		}else{
			&FT_LOG("ERROR:Failed to create symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh at /eniq/data/eventdata/events_oss_1/sgeh on ec_sgeh_1");
		}
	}
	
	&FT_LOG("INFO: Stop EC and GlassFish Services Glassfish Services...(This may take a few mins\n");
	runCommand("ssh dcuser\@ec_sgeh_1 ' source ~/.profile; ec stop '",1);
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish stop '",1);
	
	&FT_LOG("INFO: Update MZ and Glassfish Files \n");
	$file = "/eniq/mediation_inter/M_E_SGEH/etc/configuration.prop";
	open (IN, $file) || die "Cannot open file ".$file." for read";
	my @lines=<IN>;
	close IN;
	my $line='';
	open (OUT, ">", $file) || die "Cannot open file ".$file." for write";
	foreach $line (@lines){
		$line =~ s/SUCCESS_EVENT_DROP=OFF/SUCCESS_EVENT_DROP=ON/ig;
		$line =~ s/SUCCESS_DATA_HANDLING=ROP_RAW/SUCCESS_DATA_HANDLING=AGGREGATES/ig;
		print OUT $line;
	}
	close OUT;
        
	$file = "/eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml";
	open (IN, $file) || die "Cannot open file ".$file." for read";
	my @input=<IN>;
	close IN;
	my $input='';
	open (OUT, ">", $file) || die "Cannot open file ".$file." for write";
	foreach $input (@input)
	{
	   $input =~ s/\"ENIQ_EVENTS_SUC_RAW\" value=\"true\"/\"ENIQ_EVENTS_SUC_RAW\" value=\"false\"/ig;
	   print OUT $input;
	}
        close OUT;
        
    &FT_LOG("INFO: Re-starting glassfish and EC.... (This may take a few mins)\n");
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish start '",1);
	runCommand("ssh dcuser\@ec_sgeh_1 ' source ~/.profile; ec start '",1);;

	$overallResult.="</TABLE>";
	return $overallResult;
}
# 11. UTILITY TO EXECUTE ANY LTEES COMMAND AND PRINT RESULTS. In general use executeThisWithLogging() instead.
#

#------------------------------------------------------------
#	SUBROUTINES FOR COMPARE BASELINE GROUPING:
#
#	1.	compareBaselineModules()
#	2.	compareBaselineTechpacks()
#	3.	compareBase()
#	4.	getTPversion()
#	5.	getBLmodules()
#	6.	getBLTPs()
#	7.	getInstalledTechpacks()
#	8.	getInstalledModules()
#	9.	getAllTechPacks()
#	10.	getAllTechPacksAgg()
#	11.	executeThisWithLogging()
#
#-------------------------------------------------------------

#Returns in mili-seconds how long ago the system was installed.
sub installTimeDifference{
	open(INSTALL,"/eniq/admin/version/eniq_status");
	my @file=<INSTALL>;
	close(INSTALL);
	my @installDate=grep(/INST_DATE/, @file);
	my $dateTime = $installDate[0];
	$dateTime =~ s/INST_DATE //;
	
	my @dateOfPack = split( /[^A-Za-z0-9]/ , $dateTime);
	my %month = (
        Jan => '0',
        Feb => '1',
        Mar => '2',
		Apr => '3',
        May => '4',
        Jun => '5',
		Jul => '6',
        Aug => '7',
        Sep => '8',
		Oct => '9',
        Nov => '10',
        Dec => '11',
    );
	
	my $installMonth=$month{$dateOfPack[1]};
	my $unixYear=$dateOfPack[0]-1900;#THIS IS WHERE THE PROBLEM WAS DECLAN... SO CLOSE!!!
	my $installTime = mktime($dateOfPack[5], $dateOfPack[4], $dateOfPack[3], $dateOfPack[2], $installMonth, $unixYear);
	my $currentTime = time;
	my $timeDifference = $currentTime - $installTime;
	return $timeDifference;
}

###############################
# 1. COMPARE BASELINE MODULES 
# This subroutine is in charge of comparing the installation
# path modules and the modules installed in the server
# if equal then PASS, else FAIL
#
sub compareBaselineModules{
	my $timeDifference = installTimeDifference();
	my $allowedDifference = 36000;#10 hours difference
	#FAIL
	my $word ="FAIL";
	my $colour = "#ff0000";
	if( $timeDifference > $allowedDifference){
		#WARNING
		$word ="WARNING";
		$colour = "#ff9933";
	}

	my $path = shift;
	my @version = split('/', $path);
	pop @version;
	
	my @bl=getBLmodules($path);
	my @modules=getInstalledModules();
	my %modres=compareBase(\@bl,\@modules);
	&FT_LOG("\\compareBaselineModules\n");
	my $result.=qq{
	<h3>Compare Baseline: $version[-2]</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
			<th>MODULE</th>
			<th>DESCRIPTION</th>
			<th>STATUS</th>
		</tr>
	};

	foreach my $module (sort keys %modres){
		$_=$module;
		next if(/^$/);
		if($modres{$module}== 3){
			my $string=sprintf("%-35s FOUND IN BASELINE, NOT INSTALLED: $word\n",$module);
			&FT_LOG("$string");
			$result.= "<tr><td>$module</td><td>FOUND IN BASELINE, NOT INSTALLED</td><td align=center><font color=$colour><b>$word</b></font></td></tr>\n";
		} 
		if($modres{$module}== 7){
			my $string=sprintf("%-35s FOUND INSTALLED, NOT IN BASELINE: $word\n",$module);
			&FT_LOG("$string");
			$result.= "<tr><td>$module</td><td>FOUND INSTALLED, NOT IN BASELINE</td><td align=center><font color=$colour><b>$word</b></font></td> </tr>\n";
		}
		if($modres{$module}== 10){
			my $string=sprintf("%-35s FOUND IN BASELINE, INSTALLED: PASS\n",$module);
			&FT_LOG("$string");
			$result.= "<tr><td>$module</td><td>FOUND IN BASELINE, INSTALLED</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}
	}
	
	#small table rather than TABLE. This will be ignored by the getSummary method. This is to ensure that TECHPACKS are counted with MODULES in the summary
	#DONT use comparebaseline as a template
	$result.="</table> <br>\n";
	return $result;
}

###############################
# 2. COMPARE BASELINE TECHPACKS
# This subroutine is in charge or comparing 
# the installation path techpacks and the techpacks displayed in adminUI
# if equal then PASS, else FAIL
#
sub compareBaselineTechpacks{	
	my $timeDifference = installTimeDifference();
	my $allowedDifference = 36000;#10 hours difference
	#FAIL
	my $word ="FAIL";
	my $colour = "#ff0000";
	if( $timeDifference > $allowedDifference){
		#WARNING
		$word ="WARNING";
		$colour = "#ff9933";
	}
	
	my $path = shift;
	my @bt=getBLTPs($path);
	my @tps=getInstalledTechpacks();
	#my @tps=getTPversion();
	my %modres=compareBase(\@bt,\@tps);
	&FT_LOG("\ncompareBaselineTechpacks\n");
	#small table rather than TABLE. This will be ignored by the getSummary method. This is to ensure that TECHPACKS are counted with MODULES in the summary
	#DONT use comparebaseline as a template
	my $result.=qq{
		<table  BORDER='1' CELLSPACING='0' CELLPADDING='0'  >
                <tr>
                        <th>TECHPACK</th>
                        <th>DESCRIPTION</th>
                        <th>STATUS</th>
                </tr>
	};

	foreach my $module (sort keys %modres){
		$_=$module;
		next if(/^$/);
		if($modres{$module}== 3){
			my $string=sprintf("%-35s FOUND IN BASELINE, NOT INSTALLED: $word\n",$module);
			&FT_LOG("$string");
			$result.= "<tr><td>$module</td><td> FOUND IN BASELINE, NOT INSTALLED</td><td align=center><font color=$colour ><b>$word</b></font></td></tr>\n";
		}
		if($modres{$module}== 7){
			my $string=sprintf("%-35s FOUND INSTALLED, NOT IN BASELINE: $word\n",$module);
			&FT_LOG("$string");
			$result.= "<tr><td>$module</td><td>FOUND INSTALLED, NOT IN BASELINE</td><td align=center><font color=$colour ><b>$word</b></font></td></tr>\n";
		}
		if($modres{$module}== 10){
			my $string=sprintf("%-35s FOUND IN BASELINE, INSTALLED: PASS\n",$module);
			&FT_LOG("$string");
			$result.= "<tr><td>$module</td><td>FOUND IN BASELINE, INSTALLED</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}
	}
	
	$result.="</TABLE> <br>\n";
	return $result;
}

##############################################################
# 3. COMPARE BASE AND INSTALLED MODULES OR TECHPACKS
# This subroutine is a utility
# is in charge of comparing a couple of arrays
# if the arrays contain equal values, then they are inserted in a
# hash, if equal the value is updated to 10
# if different then the value remains 3
#
sub compareBase{
	my ( $ref_1,$ref_2)=@_;
	my @baseline =@{$ref_1};
	my @installed=@{$ref_2};
	my %result=undef;
	foreach my $baseline (@baseline){
		$result{$baseline}=3;
	}
	foreach my $installed (@installed){
		$result{$installed}+=7;
	}
	return %result;
}

###################################################
# 4. GET TP VERSION FROM DB
# This subroutine queries the DB to get 
# the techpack versions,
# it is not a test case, is a utility
#
sub getTPversion{
	my $sql=qq{
		select 
		t.techpack_name||'_'||
		v.techpack_version
		from 
		dwhrep.versioning v, 
		dwhrep.tpactivation t, 
		dwhrep.dwhtechpacks d 
		where 
		v.versionid = t.versionid 
		and v.versionid = d.versionid 
		and t.status = 'ACTIVE';
		go
		EOF
	};
	my @result=undef;
	open(VERSION,"$ISQL -Udba -Psql -h0 -Srepdb -w 50 -b << EOF $sql |");
	my @version=<VERSION>;
	chomp(@version);
	close(VERSION);
	@result=undef;
	foreach my $version (@version){
 	}
	return @result;
}

#########################################################
# 5. GET BASE LINE MODULES
# This subroutine is a utility
# is in charge of getting a list of modules 
# from the input path
#
sub getBLmodules{
	my $path=shift;
	$_=$path;
	$path=~s/\s//;
	#open(BSLN,"cd $path/eniq_sw;ls *.zip  |");
	my @bsln=grep(/.*\.zip/, ls("$path/eniq_sw"));
	#my @bsln=<BSLN>;
	chomp(@bsln);
	#close(BSLN);
	my @result=undef;
	foreach my $bsln (@bsln){
		$_=$bsln;
		$bsln=~s/.zip//;
		$bsln=~s/_/-/g;
		push @result, $bsln;
	}
	return @result;
}

#############################################
# 6. GET BASE LINE TECH PACKS
# This subroutine is a utility
# is in charge of getting a list of techpacks 
# from the installation path
#
sub getBLTPs{
	my $path=shift;
	$_=$path;
	$path=~s/\s//;
	#open(BLTP,"cd $path/eniq_techpacks; ls *.tpi | awk -F. '{print \$0}' | grep -v INTF | sed 's/.tpi//' |");
	my @bltp=grep( /\.tpi/, grep(!/INTF/, ls("$path/eniq_techpacks","f")));#UPDATED
	foreach (@bltp){
		s/.tpi*//;
	}
	#my @bltp=<BLTP>;
	chomp(@bltp);
	return @bltp;
}

###############################
# 7. GET INSTALLED TECHPACKS
# This subroutine is a utility
# is in charge of getting the installed 
# techpacks from AdminUI (Monitoring Commands)
#
sub getInstalledTechpacks{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Installed+tech+packs&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");

	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/tps.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/tps.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Installed+tech+packs&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");

	my @status=executeThisQuiet("egrep '(Not active techpacks|					<tr>|						<td class=.basic.>)'  /eniq/home/dcuser/automation/html/tps.html");
	chomp(@status);
	my @result=();
	my $finalresult=0;
	my $line="";

	foreach my $status (@status)
	{
		$_=$status;
		$status =~ s/                                                <td class=.basic.>//;
		$status =~ s/<.td>//;
		$status =~ s/\s//;
		$status =~ s/.*">//;

		#" just added for editor compatability!

		if(/<tr>|Not active techpacks/){ 
			push @result, $line;
			$line="";
		}
		last if(/Not active techpacks/);

		if(/ENIQ_EVENT/i){
			$_=~ s/ENIQ_EVENT//;
		}

		if(/.._._.*|\w_\w|R.*_\w/i){
			if ($line eq ""){ 
				$line=$status;
			}else{ 
				$line.="_$status";
			}
		}
	}

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");

	return @result;
}

###############################
# 8. GET INSTALLED MODULES
# This subroutine is a utility
# is in charge of getting the installed modules using
# grep module /eniq/sw/installer/versiondb.properties | sed 's/module.//'  | sed 's/=/-/'
#
sub getInstalledModules{
	open(MODULES,"grep module /eniq/sw/installer/versiondb.properties | sed 's/module.//'  | sed 's/=/-/g' |sed 's/_/-/g' | sed 's/EniqEventsServices/eventsservices/g' | sed 's/EniqEventsUI/eventsui/g' |");
	my @modules=<MODULES>;
	close(MODULES);
	chomp(@modules);
	return @modules;
}	
	
############################################################
# 9. GETS ALL THE INSTALLED TECHPACKS FROM REPDB
# This is a utility
# runs a query to get all the techpacks installed from REPDB
sub getAllTechPacks{
	my $sql=qq{
		select
		distinct SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID)-1)
		from  
		dwhrep.MeasurementCounter
		where 
		SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID)-1) <>'DWH_MONITOR'
		and SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID)-1) <>'DC_Z_ALARM';
		go
		EOF
	};

	my @result=undef;
	open(ALLTP,"$ISQL -Udba -Psql -h0 -Srepdb -w 50 -b << EOF $sql |");

	my @allTechPacks=<ALLTP>;
	@result=undef;
	chomp(@allTechPacks);
	foreach my $allTechPacks (@allTechPacks){
		$_=$allTechPacks;
		$allTechPacks=~s/ //g;
		next if(/affected/);
		next if(/^$/);
		push @result,$allTechPacks;
	}
	close(ALLTP);
	return @result;
}

######################################################
# 9. GETS ALL THE INSTALLED TECHPACKS FROM REPDB
# This is a utility
# This is used only in the re-aggregation process for DAY
#
sub getAllTechPacksAgg{
	my $sql=qq{
		select
		distinct SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID))||
		SUBSTR( SUBSTR(TYPEID, CHARINDEX(':',TYPEID)+1,20),  0,CHARINDEX(':',SUBSTR(TYPEID, CHARINDEX(':',TYPEID)+1,20)))||
		'&'||SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID)-1)
		from
		dwhrep.MeasurementCounter
		where
		SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID)-1) <>'DWH_MONITOR'
		and SUBSTR(TYPEID,0,CHARINDEX(':',TYPEID)-1) <>'DC_Z_ALARM';
		go
		EOF
	};
	my @result=undef;
	open(ALLTP,"$ISQL -Udba -Psql -h0 -Srepdb -w 50 -b << EOF $sql |");
	my @allTechPacks=<ALLTP>;
	@result=undef;
	chomp(@allTechPacks);
	foreach my $allTechPacks (@allTechPacks){
		$_=$allTechPacks;
		$allTechPacks=~s/ //g;
		next if(/affected/);
		next if(/^$/);
		push @result,$allTechPacks;
	}
	close(ALLTP);
	return @result;
}

#
# 11. UTILITY TO EXECUTE ANY COMMAND AND GET RESULT IN ARRAY
##########################################################
sub executeThisWithLogging{
	my @cmd;

	my $command = shift;	
	open(CMD,"$command |");
	while(<CMD>){
		&FT_LOG($_);
		push(@cmd, $_);
	}
	close(CMD);
	return @cmd;
}

#####################################################
sub executeThisAndGiveReturnCode{
	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>){
		&FT_LOG($_);
	}
	close(CMD);
	return $?;
}

sub executePerlQuietAndGiveReturnCode{
	my $command = shift;
	open(CMD,"$command|");
	while(<CMD>){
	}
	close(CMD);
	my $returnCode=$?>>8;
	return $returnCode;
}

#############no logging only output to cmd line###########
sub executeThisWithOutput{
	my @cmd;

	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>){
		&FT_LOG($_);
		push(@cmd, $_);
	}
	close(CMD);
	return @cmd;
}

###########no logging or output################
sub executeThisQuiet{
	my $command = shift;
	open(CMD,"$command |");
	my @cmd=<CMD>;
	close(CMD);
	return @cmd;
}

#----------------------------------------------------------
#	END Compare Baseline Grouping
#----------------------------------------------------------

#------------------------------------------------------------
#	SUBROUTINES FOR COMMAND LINE GROUPING (11):
#
#	1.	verifyExecutables()
#	2.	runMediationGW()
#	3.	runGlassfish()
#	4.	runScheduler()
#	5.	runEngine()
#	6.	runWebserver()
#	7.	runLicenseManager()
#	8.	runLicserv()
#	9.	runDwhdb()
#	10.	runRepdb()
#	11.	runCMDLineNP()
#	12.	runEC()
#
#-------------------------------------------------------------

# 
# VERIFIES ALL EXECUTABLES ARE OK
# This is test to check that all scripts in the bin directories are executable
# This test catches scripts ending in hat-M
sub verifyExecutables{
	my $result="<h3>VERIFIES ALL EXECUTABLES ARE OK</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
			<th>STATUS</th>
		</tr>
	";
	my $count=0;
	open(LIST,"file /eniq/sw/bin/* /eniq/admin/bin/* |");
	my @list=<LIST>;
	close(LIST);

	chomp(@list);
	foreach my $list (grep(/executable/, @list)){
		$_=$list;
		$list=~s/:.*executable.*//;
		open(EXE,"egrep -l \$'\r\n' $list |");
		my @exe=<EXE>;
		foreach my $exe (@exe){
			#$result.= "<font color=#ff0000><b>ERROR: EXE file with DOS format: $exe</b></font><br>\n";
			$result.= "<tr><td align=center><font color=#ff0000><b>ERROR: EXE file with DOS format: $exe</b></font></td></tr>\n";
			$count++;
		}
		close(EXE);
	}
	if($count>0){
		print("FAIL\n");
		#$result.= "<font color=#ff0000><b>FAIL</b></font><br>\n";
		$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}else{
		print("PASS\n");
		#$result.= "<font color=#darkblue><b>PASS</b></font><br>\n";
		$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}
	$result.="</TABLE> <br>\n";
	return $result;
}

# 
# MEDIATION GW COMMAND LINE TESTS 
# runs all mediation gw cli test below
#
sub runMediationGW{
	my $result="";
	#WHEN WORKING UPDATE TO NEW FORMAT TO USE commandsToTable(@cmds); AND USE IT. REMOVE FOREACH.
	my @cmds=(
	#	"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC1 ",
	#	"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown Platform ",
	#	"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup Platform  ",
	#	"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1  ",
	#	"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr restart EC1 ",
	#	"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr restart Platform ",
		"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist ",
		"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist ",
		"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr status "
	);
	$result=qq{
	<h3>RUN MEDIATION GW CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	 <tr>
	  <th>CMD</th>
	  <th>DESCRIPTION</th>
	  <th>STATUS</th>
	 </tr>
	};	
	#$result .= commandsToTable(@cmd);

	#REMOVE ME
	foreach my $cmd (@cmds){
		&FT_LOG("$cmd	");
		my $success = 0;
		my $failure = 0;
		my @res=executeThisWithLogging($cmd);
		my @result=map {$_."<br>"} @res;
		$result.= "<tr><td>$cmd:</td><td>@result</td>\n";

		foreach my $res (@result){
			$_=$res;
			if(/Exception|Execute failed|cannot execute/i){
				$failure = 1;
				&FT_LOG("FAIL\n");
			}
			elsif(/OK|You must be root|shall only be used by SMF|Usage:| is online.|All files within .eniq.data.etldata.adapter_tmp removed|Listing active servers on local subnet|SMF .*abling/i){
				$success = 1;
				&FT_LOG("PASS\n");
			}
		}
		
		if((@result)==0){
			$failure = 1;
			&FT_LOG("FAIL\n");
		}
		if($success == 1 && $failure == 0){
			$result.= "<td><font color=darkblue><b>PASS</b></font></td></tr>";
			&FT_LOG("PASS\n");
		}else{
			$result.= "<td><font color=ff0000><b>FAIL</b></font></td></tr>";
			$trafficLightColour = "red";
			&FT_LOG("FAIL\n");
		}
	}
	
	##
	$result.=qq{</TABLE>};
	return $result;
}

sub executeSeleniumAndWait{
	my $command = shift;
	my $timeout = shift;
	
	if($timeout==""){
		$timeout=18000;
	}
	
	&FT_LOG("INFO:Executing: $command with timeout:$timeout");
	my $childPid = fork(); #fork a process
	
	if($childPid == 0){
		#Execute tests in the child process
		my $startTime=time();
		executeThisWithLogging($command);
		my $endTime=time();
		&FT_LOG("INFO:Finished executing $command");
		exit(0);
	}else{
		&FT_LOG("INFO:Executing tests in process: $childPid");
		#Wait in the parent process for the child process to finish
		for(my $i=0;$i<=$timeout;$i++){
			my $running = `ps -p $childPid`;
			if( $running !~ /^{$childPid}\s/ || $running =~ /defunct/){
				&FT_LOG("INFO:Child PID $childPid not found. Ending timeout loop");
				last;
			}
			if($i==$timeout){
				&FT_LOG("INFO:Timeout of $timeout seconds reached. Killing child PID $childPid and Selenium processes");
				my $dat=getRunDate();
				open(TEXT, ">/eniq/home/dcuser/automation/$dat-timed-out.log");
				print TEXT "$command";
				close (TEXT);
				killProcessAndChildren($childPid,$$);
			}else{
				if( $i % 3000 == 0){
					&FT_LOG("INFO:$i seconds have passed. Waiting up to $timeout seconds for command to finish:$command");
				}
				sleep(1);
			}
		}
	}
}

#Takes in the command and a timeout
sub executeAndWaitForTimeout{
	my $command = shift;
	my $timeout = shift;
	
	if($timeout==""){
		$timeout=36000;
	}
	&FT_LOG("Executing: $command with timeout:$timeout");
	my $childPid = fork(); #fork a process
	
	if($childPid == 0){
		#Execute tests in the child process
		my @output = executeThisQuiet($command);
		exit(0);
	}else{
		#Wait in the parent process for the child process to finish
		my $running;
		for(my $i=0;$i<=$timeout;$i++){
			$running = `ps -p $childPid`;
			if( $running !~ /^{$childPid}\s/ || $running =~ /defunct/){
				&FT_LOG("Child PID $childPid not found. Finished!");
				return 1;
				last;
			}
			if($i==$timeout){
				&FT_LOG("Timeout of $timeout seconds reached. Killing child PID $childPid and Selenium processes");
				killProcessAndChildren($childPid,$$);
				return 0;
			}else{
				sleep(1);
			}
		}
	}
}

sub refreshTopologyAndWait{
	my $command = shift;
	my $timeout = shift;
	if ($timeout == ""){	
		#Set timout period to 1200s
		$timeout = 1200;
	}
	&FT_LOG("INFO:Executing: $command with timeout: $timeout seconds\n");
	my $childPid = fork(); 	#fork a process
	if($childPid == 0)		#child process
	{
		executeThisWithLogging($command);
		&FT_LOG("INFO:Finished executing $command\n");
		exit(0);	
	}else{
		&FT_LOG("INFO:Refreshing topology in process: $childPid");
		#Wait in the parent process for the child process to finish
		for ( my $refreshTopologyTime = 0; $refreshTopologyTime <= $timeout; $refreshTopologyTime++){
			my $running = `ps -p $childPid`;
			if( $running !~ /^{$childPid}\s/ || $running =~ /defunct/){
				&FT_LOG("INFO:Child PID $childPid not found. Ending timeout loop");
				return 1;
			}
			if ($refreshTopologyTime > 0 && $refreshTopologyTime % 200 == 0){
				&FT_LOG("INFO:$refreshTopologyTime seconds have passed. Waiting up to $timeout seconds for command to finish:$command\n")
			}
			if ($refreshTopologyTime == $timeout){
				&FT_LOG("INFO:Topology refreshing times out after $timeout seconds... Kill process\n");
				killProcessAndChildren($childPid,$$);
				return 0;
			}else{
				sleep (1);
			}
		}					
		return 0;
	}	
}		

sub checkRefreshTopologyResult{	
	my $checkResult = shift;
	my $command = shift;
	my $overallResult=qq{
	<h3>REFRESH TOPOLOGY</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	if(!$checkResult){#refresh topology hung 1st time
		&FT_LOG("ERROR:mg_topology_refresh.sh timed out. Trying again");
		$overallResult.=logAndGenerateTableEntry("Excuting $command 1st Time", 0);
		&FT_LOG("INFO: Executing Kill and Restart EC1");
		executeThisWithLogging("ssh ec_1 \"ps -ef | egrep \\\"16000M|1026M\\\" | egrep -v egrep| awk '{print \\\$2}' | xargs kill -9 2>/dev/null\"");
		executeThisWithLogging("ssh ec_1 \"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr restart EC1\"");
		$checkResult = refreshTopologyAndWait($command,1200);
		if(!$checkResult){#refresh topology hung twice
			&FT_LOG("ERROR:mg_topology_refresh.sh timed out again. Skipping...");
			$overallResult.=logAndGenerateTableEntry("Excuting $command 2nd Time", 0);
		}else{
			&FT_LOG("INFO:mg_topology_refresh.sh finished");
			$overallResult.=logAndGenerateTableEntry("Excuting $command 2nd Time", 1);
		}
	}else{
		&FT_LOG("INFO:mg_topology_refresh.sh finished");
		$overallResult.=logAndGenerateTableEntry("Excuting $command", 1);
	}
	$overallResult.="</TABLE>";
	$result.=$overallResult;
}

sub killProcessAndChildren{
	my $pid=shift;
	my $ppid=shift;
	my @pids;
	while(1){
		push(@pids,$pid);
		my @procs=`/usr/bin/ps -ef`;
		my @var=grep(/ $pid /,@procs);
		if($ppid =~ /^[0-9]+$/){
			@var=grep(!/ $ppid /,@var);
		}
		if($var[0] eq ""){
			last;
		}else{
			$ppid=$pid;
			my @sp = split(/ /, $var[0]);
			foreach my $item(@sp){
				if($item =~ /^[0-9]+$/){
					$pid=$item;
					last;
				}
			}
		}
	}
	kill 9,@pids;
}

sub splitAndGetValue{
	my $toSplit = shift;
	my $splitOn = shift;
	my $toReturn = shift;
	$toReturn -= 1;
	my @values = split( $splitOn, $toSplit );
	chomp(@values);
	if(!$toReturn or $toReturn >= @values ){
		return @values;
	}else{
		return $values[$toReturn];
	}
}

sub grepFile{
	my $pattern = shift;
	my $file = shift;
	my $arg = shift;
	
	open( FILE, $file );
	my @contents = <FILE>;
	close( FILE );
	
	if($arg =~ /i/){
		return grep(/$pattern/i, @contents);
	}
	return grep(/$pattern/, @contents);
}

#Returns 1 if multiblade server
sub isMultiBladeServer{
	my $co_server = (grepFile("engine", "/etc/hosts"))[0];
	my $engine = splitAndGetValue($co_server, " ", 3);
	my $ec_server = (grepFile("ec_1", "/etc/hosts"))[0];
	my $ec = splitAndGetValue($ec_server, " ", 3);
	
	if ($ec eq $engine) {
		#This is a Standalone server
		return 0;
	}else{
		#This is a Multi-blade server
		return 1;
	}
}

sub getSystemMemory{
	my @memory=executeThisQuiet("/usr/sbin/prtconf | grep Memory");
	my $var=$memory[0];
	$var =~ s/\D//g;
	return $var;
}

sub isSevenBlade{
	open(HOSTS,"</etc/hosts");
	my @contents=<HOSTS>;
	close(HOSTS);
	if(grep(/ec_2/,@contents)){
		return 1;
	}else{
		return 0;
	}
}

#Returns 1 if multiblade server
sub isMultipleEC{	
	#counts the number of times 'config name' occurs inexecutioncontext.xml
	my @output=executeThisQuiet("grep -c 'config name' /eniq/mediation_sw/mediation_gw/etc/executioncontext.xml");
	chomp(@output);
	
	my $numberOfEC = $output[0];
	chomp($numberOfEC);
	
	if ( $numberOfEC > 2 ){
		#This has multiple ECs
		return 1;
	}else{
		#This does NOT have multiple ECs
		return 0;
	}
}

sub hasCepBlade{
	my $cep_entry = (grepFile("cep_med_1", "/etc/hosts"));
	if ($cep_entry){
		return 1;
	}else{
		return 0;
	}
}

sub fileTime{
	my $file = shift;
	my $time;
	$time = (stat($file))[9] or $time = 0;
	chomp($time);
	return $time;
}

sub ls{
	my ($args, $pathToList, $pos);
	$pathToList = shift;
	$args = shift;
	$pos = shift;
	opendir DIR, $pathToList;
	my @list = readdir(DIR);

	chomp(@list);
	closedir(DIR);
	@list = grep(!/^\.|\.\.$/, @list);#Remove current (.) and previous (..) dirs
	@list = sort { lc($a) cmp lc($b) } @list;#alphabetical
	
	if($args =~ m/d/){#directories only
		my @dirs = ();
		foreach my $dir (@list){
			if( -d "$pathToList/$dir" ){
				push( @dirs, $dir);
			}
		}
		@list = @dirs;
	}
	
	if($args =~ m/f/){#files only
		my @files = ();
		foreach my $file (@list){
			if( ! -d "$pathToList/$file" ){
				push( @files, $file);
			}
		}
		@list = @files;
	}
	
	if($args =~ m/t/){#time (newest first)
		my %hash = {};
		%hash = map { $_ => fileTime($pathToList."/$_") } @list;
		@list = ();
		foreach my $value (sort {$hash{$b} cmp $hash{$a} } keys %hash){
			push( @list, $value);
		}
	}
	
	if($args =~ m/r/){#reverse alphabetical or time
		@list = reverse(@list)
	}
	
	if($args =~ m/l/){#full path of each file
		@list = map( $pathToList."/$_", @list);
	}
	
	if( $pos =~ m/-?\d+/){
		return $list[$pos];
	}
	return @list;
}

sub NASTimeCheck{
	my $nasDir = "/eniq/home";
	my $time = time;
	open( FILE, ">${nasDir}/FileTimeCheck.txt" );
	print FILE $time;
	close (FILE);
	my $fileTime = fileTime("${nasDir}/FileTimeCheck.txt");
	unlink("FileTimeCheck.txt");
	if(abs($fileTime - $time) <= 10){
		return 1;#ok
	}else{
		return 0;#bad
	}
}

sub enableDisableHTTPS{
	my $input = shift;
    my $result="";
	
	my $command = "/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh status";
	my @test = executeThisWithLogging(sshCommand("glassfish",$command));
	
	if($test[1] !~ /$input/i){
		$command = "/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh $input";
		executeThisWithLogging(sshCommand("glassfish",$command));
	}
	my $command = "/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh status";
	my @test = executeThisWithLogging(sshCommand("glassfish",$command));
	
	if($test[1] !~ /$input/i){
		&FT_LOG("ERROR: Failed to change HTTPS status to $input\n\n\n");
		return 0;
	}
	&FT_LOG("\n\n\nSTATUS: $test[1]\n\n\n");
	return 1;
}

sub checkHTTPS{
	my $input = shift;
    my $result="";
    my ($HTTPSenabled, $HTTPSdisabled, $command);
	my @res;
	my @val;
	
	$HTTPSenabled = enableDisableHTTPS("enable");
	if($HTTPSenabled){
		$command ="/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/check_https_cookies.txt \"http://localhost:18080/EniqEventsUI/\"";
		executeThisQuiet(sshCommand("glassfish",$command));
		$command = "cat /eniq/home/dcuser/check_https_cookies.txt";
		@res = executeThisQuiet(sshCommand("glassfish",$command));
		@val = grep(/8181/, @res);
		if (@val){
			&FT_LOG("Check HTTPS enabled - HTTP request to EniqEventsUI redirects to HTTPS\n\n\n");
			$HTTPSenabled = 1;
		} else {
			&FT_LOG("ERROR: Check HTTPS enabled - HTTP request to EniqEventsUI did NOT redirect to HTTPS\n\n\n");
			$HTTPSenabled = 0;
		}
	}
	
	$HTTPSdisabled = enableDisableHTTPS("disable");
	if($HTTPSdisabled){
		$command = "/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/check_http_cookies.txt \"https://localhost:8181/EniqEventsUI/\"";
		executeThisQuiet(sshCommand("glassfish",$command));
	
		$command = "cat /eniq/home/dcuser/check_http_cookies.txt";
		@res = executeThisQuiet(sshCommand("glassfish",$command));
		@val = grep(/18080/, @res);
		if (@val){
			&FT_LOG("Check HTTPS disabled - HTTPS request to EniqEventsUI redirects to HTTP\n\n\n");
			$HTTPSdisabled = 1;
		}else{
			&FT_LOG("ERROR: Check HTTPS disabled - HTTPS request to EniqEventsUI did NOT redirect to HTTP\n\n\n");
			$HTTPSdisabled = 0;
		}
	}
	# Re-enable https
	&enableDisableHTTPS("enable");
	
	#Cleaning up 
	$command = "rm -rf rm -rf /eniq/home/dcuser/check_http*";
	executeThisQuiet(sshCommand("glassfish",$command));

	my $verbose = "";

	$verbose.=qq{
		<h3>HTTPS STATUS CHECKS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
				<th>CMD</th>
				<th>DESCRIPTION</th>
				<th>STATUS</th>
			</tr>
	};
	
	$verbose .= "<tr><td>Check HTTPS enabled</td><td>HTTP request redirects to HTTPS</td><td>".($HTTPSenabled?"<font color=darkblue><b>PASS</b></font>":"<font color=red><b>FAIL</b></font>")."</td></tr>";
	$verbose .= "<tr><td>Check HTTPS disabled</td><td>HTTPS request redirects to HTTP</td><td>".($HTTPSdisabled?"<font color=darkblue><b>PASS</b></font>":"<font color=red><b>FAIL</b></font>")."</td></tr>";
	
	#$verbose =~ s%<font color=darkblue><b>PASS</b></font>%<font color=darkblue><b>SUCCESS</b></font>%g;
	#$verbose =~ s%<font color=ff0000><b>FAIL</b></font>%<font color=red><b>UNSUCCESSFUL</b></font>%g;
	
	print $input ."\n";
	$verbose.=qq{</TABLE>};
	$result .= $verbose;
	return $result;
}
	
sub checkingServicesStatus{
	my $input = shift;
    my $result="";

	my @cmdsAndResponses=(
		[sshCommand("webserver", "svcs svc:/eniq/webserver:default | tail -1"),"online"],
		[sshCommand("glassfish","svcs svc:/eniq/glassfish:default | tail -1"),"online"],
		[sshCommand("engine","svcs svc:/eniq/engine:default | tail -1"),"online"],
		["engine status | grep Status","active"],
		['engine status | grep \'Current Profile\'',"Normal"],
		["svcs svc:/eniq/scheduler:default | tail -1","online"],
		[sshCommand("licenceservice","svcs svc:/eniq/licmgr:default | tail -1"),"online"],
		[sshCommand("repdb","svcs svc:/eniq/repdb:default | tail -1"),"online"],
		#[sshCommand("controlzone","svcs svc:/eniq/controlzone:default | tail -1"),"online"],
		#For Single Blade we do not bring the ec service online because we turn it off before hand
	);
	
	if(!isSonVisOnlyServer()){
		push(@cmdsAndResponses,[sshCommand("controlzone","svcs svc:/eniq/controlzone:default | tail -1"),"online"]);
		if( isMultiBladeServer() ){
			push(@cmdsAndResponses,[sshCommand("ec_1","svcs svc:/eniq/ec:default | tail -1"),"online"]);
			push(@cmdsAndResponses,[sshCommand("dwh_reader_1","svcs svc:/eniq/dwh_reader:default"),"online"]);
		}
		if(!isMultiBladeServer()){#SingleBlade
			#Workaround checks
			push(@cmdsAndResponses,['ls /swapfile','/swapfile']);
			push(@cmdsAndResponses,['grep /swapfile /etc/vfstab','/swapfile - - swap - no -']);
			push(@cmdsAndResponses,['grep eniq_sp_1 /etc/vfstab','/dev/zvol/dsk/eniq_sp_1/swap - - swap - no -']);
		}
		if(isSevenBlade()){
			push(@cmdsAndResponses,[sshCommand("ec_2","svcs svc:/eniq/ec:default | tail -1"),"online"]);
		}
	}
	
	if (isVirtualApp()) {
		executeThisWithLogging("ssh dcuser\@controlzone '/eniq/home/dcuser/automation/runCommandAsRoot.py svcadm disable /application/graphical-login/cde-login'");
	}
	
	my $len=@cmdsAndResponses;
	for( my $i = 0; $i < $len; $i++){
		my $command = $cmdsAndResponses[$i][0];
		my @res=executeThisQuiet($command);
		my @temp = split( / /,$command);
		my $host = $temp[0]eq"ssh"?$temp[1]:"localhost";#Get host name if it was a ssh command or else set it to localhost
		my $serviceName = $temp[5] =~ m/svc/?$temp[5]:$temp[1];
		$serviceName =~  s/;\'//;
		my $server = $serviceName;
		$server =~ s/:default//;
		$server =~ s/svc:\/eniq\///;

		if( grep(/online\*/, @res) ==1){
			sleep(60);
			&FT_LOG("ATTEMPTING TO START $server\n\n".sshCommand($host,"$server start")."\n\n\n");
			executeThisQuiet(sshCommand($host,"$server start"));
		}elsif(grep(/offline\*/, @res) ==1){
		    sleep(60);
			my @res=executeThisQuiet($command);
			sleep(60);
		}elsif( grep(/offline/, @res) ==1){
		    if($server eq "scheduler"){
				&FT_LOG("ATTEMPTING TO START $server\n");
				executeThisQuiet("engine restart");
				sleep(60);			   
			}else{		
				&FT_LOG("ATTEMPTING TO START $server\n\n".sshCommand($host,"$server start")."\n\n\n");
				executeThisQuiet(sshCommand($host,"$server start"));
				sleep (60);
			}
		}elsif( grep(/disabled/, @res) ==1){
			&FT_LOG("ATTEMPTING TO START $server\n\n".sshCommand($host,"$server start")."\n\n\n");
			executeThisQuiet(sshCommand($host,"$server start"));
			sleep (60);
		}elsif( grep(/maintenance/, @res) ==1){
			if(isMultiBladeServer()){
				&FT_LOG("$host\n ATTEMPTING TO CLEAR MAINTENACE STATUS\n");
				executeThisQuiet(sshCommand("$host","./automation/RunCommandAsRoot.sh svcadm clear $serviceName"));
			    sleep (60);
			}else{
				&FT_LOG("$host\n ATTEMPTING TO CLEAR MAINTENACE STATUS\n");
				executeThisQuiet("./RunCommandAsRoot.sh svcadm clear $serviceName");
				sleep (60);
			}
		}elsif(grep(/Status/, @res) ==1){#for engine profile
			if((grep(/active/, @res) == 0)){
				executeThisQuiet( sshCommand("engine","/eniq/sw/bin/engine -e changeProfile 'Normal'| tail -1") );
			}
		}elsif(grep(/Current Profile/, @res) ==1){#for engine profile
			if((grep(/Normal/, @res) == 0)){
				executeThisQuiet( sshCommand("engine","/eniq/sw/bin/engine -e changeProfile 'Normal'| tail -1") );
			}
		}
	}
	
	my $ecStatus = executeAndWaitForTimeout("ec status", 60);
	my $NASTimeOk = NASTimeCheck();	
	my $verbose = "";
	$verbose.=qq{
		<h3>BASIC STATUS CHECKS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
				<th>CMD</th>
				<th>DESCRIPTION</th>
				<th>STATUS</th>
			</tr>
	};

	$verbose.=commandsToTable(@cmdsAndResponses);
	if(!isSonVisOnlyServer()){
		$verbose .= "<tr><td>ec status</td><td>Did 'ec status' execute in 1 minute?</td><td>".($ecStatus?"<font color=darkblue><b>PASS</b></font>":"<font color=red><b>FAIL</b></font>")."</td></tr>";
	}
	$verbose .= "<tr><td>NAS time</td><td>Is time on NAS same as server?</td><td>".($NASTimeOk?"<font color=darkblue><b>PASS</b></font>":"<font color=red><b>FAIL</b></font>")."</td></tr>";
	
	#$verbose =~ s%<font color=darkblue><b>PASS</b></font>%<font color=darkblue><b>SUCCESS</b></font>%g;
	#$verbose =~ s%<font color=ff0000><b>FAIL</b></font>%<font color=red><b>UNSUCCESSFUL</b></font>%g;
	
	print $input ."\n";
	$verbose.=qq{</TABLE>};
	if($input !~ /quiet/i){
		$result .= $verbose;
	}
	if($verbose =~ m/FAIL/g ){#Still not working. Report and continue
		$trafficLightColour = "red";
		
		my @matches = $verbose =~ m/<tr><td>(.*)<\/td><td>.*<br><\/td>\n<td><font color=red><b>FAIL<\/b><\/font><\/td>/g;
		my $oneMatch = $verbose =~ m/<tr><td>(.*)<\/td><td>.*<br><\/td>\n<td><font color=red><b>FAIL<\/b><\/font><\/td>/g;
		if($oneMatch == 1){
			push( @matches, $1);
		}
		if($ecStatus == 0){
			push( @matches, "ec status");
		}
		if($NASTimeOk == 0){
			push( @matches, "NAS time");
		}
		
		&FT_LOG(qq{
				********************************************************************************
				********************************************************************************
				**************************                           ***************************
				**************************   NOT WORKING CORRECTLY   ***************************
				**************************                           ***************************
				********************************************************************************
				********************************************************************************
				****One or more of the services was is not working. See 'svcs -a | grep eniq'***
				****Not working: @matches
				********************************************************************************
				********************************************************************************
		});
		if($input !~ /quiet/i){
			return $verbose;
		}
		$result.=qq{
				<h3>SERVICES NOT WORKING</h3>
				<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
					<tr>
						<th>SERVICE NAME</th>
						<th>STATUS</th>
					</tr>
		};

		foreach my $service (@matches){
			$result .= "<tr><td>$service</td><td><font color=red><b>FAIL</b></font></td></tr>";
		}
		$result.=qq{</TABLE>};
	}
	return $result;
}

sub swichOffSecurity{
    my $execFile="/eniq/sw/runtime/tomcat/webapps/adminui/WEB-INF/web.xml";
    open(FILE,"$execFile") or die "can not open file /eniq/sw/runtime/tomcat/webapps/adminui/WEB-INF/web.xml";
    my @contents=<FILE>;
    close(FILE);
	foreach my $line(@contents){
		if(grep(/transport-guarantee/,$line)){
			print "$line\n";
			$line=~ s/CONFIDENTIAL/NONE/g;
			print "$line\n";
			print ("CONFIDENTIAL has been changed to NONE\n");	
		}
	}
	open(FILE1, ">$execFile") or die "can not open file /eniq/sw/runtime/tomcat/webapps/adminui/WEB-INF/web.xml";
	foreach(@contents){
		print FILE1;
	}
	close(FILE1);
}

sub swichOnSecurity{
    my $execFile="/eniq/sw/runtime/tomcat/webapps/adminui/WEB-INF/web.xml";
    open(FILE,"$execFile") or die "can not open file /eniq/sw/runtime/tomcat/webapps/adminui/WEB-INF/web.xml";
    my @contents=<FILE>;
    close(FILE);
	foreach my $line(@contents){
		if(grep(/transport-guarantee/,$line)){
			print "$line\n";
			$line=~ s/NONE/CONFIDENTIAL/g;
			print "$line\n";
			print ("NONE has been changed back to CONFIDENTIAL\n");	
		}
	}
	open(FILE1, ">$execFile") or die "can not open file /eniq/sw/runtime/tomcat/webapps/adminui/WEB-INF/web.xml";
	foreach(@contents){
		print FILE1;
	}
	close(FILE1);
}

# 
# GLASSFISH COMMAND LINE TESTS 
# runs all Glassfish cli test below
#
sub runGlassfish{
	my $serverName = "glassfish";
	my $result="";
	my @cmdsAndResponses=(
		[sshCommand( $serverName, "glassfish"),"Usage: glassfish start|stop|restart|status"],
		[sshCommand( $serverName, "glassfish stop | tail -1"),"SMF disabling svc:/eniq/glassfish"],
		[sshCommand( $serverName, "svcs svc:/eniq/glassfish:default | tail -1"),"disabled"],
		[sshCommand( $serverName, "glassfish start | tail -1"),"SMF enabling svc:/eniq/glassfish"],
		[sshCommand( $serverName, "svcs svc:/eniq/glassfish:default | tail -1"),"online"],
		[sshCommand( $serverName, "glassfish restart | tail -1"),"SMF enabling svc:/eniq/glassfish"],
		[sshCommand( $serverName, "svcs svc:/eniq/glassfish:default | tail -1"),"online"],
		[sshCommand( $serverName, "glassfish status | head -1"),"Status:"],
	);
	$result=qq{<br /><br /><br />
		<h3>RUN GLASSFISH CLI CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
				<th>CMD</th>
				<th>DESCRIPTION</th>
				<th>STATUS</th>
			</tr>
	};
	$result.=commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

# 
# SCHEDULER COMMAND LINE TESTS 
#
sub runScheduler{
	my $result="";
	my @cmdsAndResponses=(
	["scheduler","Usage: scheduler start|stop|restart|status|activate|hold"],
	["scheduler stop | tail -1    ","SMF disabling svc:/eniq/scheduler"],
	["svcs svc:/eniq/scheduler:default | tail -1 ","offline|disabled"],
	["scheduler start | tail -1","SMF enabling svc:/eniq/scheduler"],
	["svcs svc:/eniq/scheduler:default | tail -1 ","online"],
	["scheduler restart | tail -1","SMF enabling svc:/eniq/scheduler"],
	["svcs svc:/eniq/scheduler:default | tail -1 ","online"],
	["scheduler status | tail -1","scheduler is running OK"],
	["scheduler hold | tail -1","scheduler is running OK"],
	["scheduler activate | tail -1","scheduler is running OK"],
	);
	$result=qq{
	<h3>RUN SCHEDULER CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	 <tr>
	  <th>CMD</th>
	  <th>DESCRIPTION</th>
	  <th>STATUS</th>
	 </tr>
	};
	$result.=commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

# 
# ENGINE CMD LINE TESTS
#
sub runEngine{
	my $result="";
	#An array containing
	#command	and								#expected resopnse
	my @cmdsAndResponses=(
		["/eniq/sw/bin/engine -e reloadConfig | tail -1", "Reload properties requested successfully"],
		["/eniq/sw/bin/engine -e reloadAggregationCache| tail -1","Reload aggregation cache requested successfully"],
		["/eniq/sw/bin/engine -e reloadProfiles | tail -1","Reload profiles requested successfully"],
		["/eniq/sw/bin/engine -e loggingStatus| tail -1","Logging status printed succesfully"],
		["/eniq/sw/bin/engine -e changeProfile 'Normal'| tail -1","Change profile requested successfully"],
		["/eniq/sw/bin/engine -e holdPriorityQueue| tail -1","Priority hold successfully"],
		["/eniq/sw/bin/engine -e restartPriorityQueue| tail -1","Restart priority queue requested successfully"],
		["/eniq/sw/bin/engine -e showSetsInQueue| tail -1","Finished successfully"],#EMPTY. PROBLEM??
		["/eniq/sw/bin/engine -e showSetsInExecutionSlots| tail -1","Finished successfully"],
		["/eniq/sw/bin/engine -e removeSetFromPriorityQueue 1 | tail -1","not removed from priority queue"],#invalid id??
		["/eniq/sw/bin/engine -e changeSetPriorityInPriorityQueue 1 0","priority not changed to 0"],#invalid id??
		["/eniq/sw/bin/engine -e activateSetInPriorityQueue 1 | tail -1","Set \((.*?)\) is activated"],
		["/eniq/sw/bin/engine -e holdSetInPriorityQueue 1| tail -1","Set \((.*?)\) is set on hold"],
		["/eniq/sw/bin/engine stop| tail -1","SMF disabling svc:/eniq/engine"],
		["svcs svc:/eniq/engine:default | tail -1 ","disabled"],
		["/eniq/sw/bin/engine start| tail -1","SMF enabling svc:/eniq/engine"],
		["svcs svc:/eniq/engine:default | tail -1 ","online"],
		["/eniq/sw/bin/engine restart| tail -1","SMF enabling svc:/eniq/engine"],
		["svcs svc:/eniq/engine:default | tail -1","online"],
		["/eniq/sw/bin/engine status| tail -1","engine is running OK"],
		["/eniq/sw/bin/engine -e | head -1","Usage: engine -e command"]
	);

	#We need to see it these return codes are correct
	my @cmdsNeedCode=(
	"/eniq/sw/bin/engine -e removeSetFromPriorityQueue 1",#NOTHING IN QUEUE #!not removed
	"/eniq/sw/bin/engine -e changeSetPriorityInPriorityQueue 1 0",#NOTHING IN QUEUE #!not changed
	"/eniq/sw/bin/engine stop",
	"/eniq/sw/bin/engine start",
	"eniq/sw/bin/engine restart",
	);
	
	$result=qq{
	<h3>RUN ENGINE CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>CMD</th>
		 <th>DESCRIPTION</th>
		 <th>STATUS</th>
	   </tr>
	};

	$result.=commandsToTable(@cmdsAndResponses);

	#Tests that need fail code checks
	foreach my $command (@cmdsNeedCode){
		$result.= "<tr><td>$command:</td><td>Fail code check</td>\n";
		my $returncode=executeThisAndGiveReturnCode($command);
		if ($returncode==0){
			$result.= "<td><font color=darkblue><b>PASS</b></font></td></tr>";
			&FT_LOG("PASS\n");
		}else{
			$result.= "<td><font color=ff0000><b>FAIL</b></font></td></tr>";
			&FT_LOG("FAIL\n");
		}
	}

	$result.=qq{</TABLE>};
	return $result;
}

#	| Command | Description | Status |
#	|---------|-------------|--------|
#	|		  |				|		 |
#	|		  |				|		 |
sub commandsToTable{
	my @cmdsAndResponses = @_;
	my $result = "";
	my $num=@cmdsAndResponses;
 	for(my $i=0; $i < $num; $i++){
		my $command = $cmdsAndResponses[$i][0];
		my $expectedResult = $cmdsAndResponses[$i][1];
		&FT_LOG("$command	");
		my $success = 0;
		my @res=executeThisWithLogging($command);
		chomp(@res);
		#Removes the tail and head which was only used to make the description look better
		$command=~ s/[ ]?\|.?(tail|head).?-1//;
		$command=~ s/ssh(.*)'source .profile; //;
		$command=~ s/;'//;
		my @result=map {$_."<br>"} @res;#adds a <br> to the end of every line
		if($expectedResult ne ""){
			$result.= "<tr><td>$command:</td><td>".(@result?"@result":"<em>No Information Available</em>")."</td>\n";
		}else{									  
			$result.= "<tr><td>$command:</td><td>".(@result?"@result":"Nothing returned. Correct behaviour")."</td>\n";
		}

		if(!@res && $expectedResult eq ""){
			$success = 0;
		}elsif((@res)==0){
			$success = 1;
		}elsif(grep(/$expectedResult/,@res)==0){
			$success = 1;
		}
		if($success == 0){
			$result.= "<td><font color=darkblue><b>PASS</b></font></td></tr>";
			&FT_LOG("PASS\n");
		}else{
			$result.= "<td><font color=ff0000><b>FAIL</b></font></td></tr>";
			$trafficLightColour = "red";
			&FT_LOG("FAIL\n");
		}
	}
	
	return $result;
}

#Converts a command to a ssh command
sub sshCommand{
	my $host = shift;
	my $command = shift;
	return "ssh $host 'source .profile; $command;'";
}

############################################################
# WEBSERVER COMMAND LINE TESTS 
# runs all webserver cli test below
sub runWebserver{
	my $result="";
	my @cmdsAndResponses=(
		["webserver","Usage: webserver start|stop|restart|status"],
		["webserver stop | tail -1","SMF disabling svc:/eniq/webserver"],
		["svcs svc:/eniq/webserver:default | tail -1","disabled"],
		["webserver start | tail -1","SMF enabling svc:/eniq/webserver"],
		["svcs svc:/eniq/webserver:default | tail -1","online"],
		["webserver restart | tail -1","SMF enabling svc:/eniq/webserver"],
		["svcs svc:/eniq/webserver:default | tail -1","online"],
		["webserver status","webserver is running OK"]
	);
	$result=qq{
	<h3>RUN WEBSERVER CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>CMD</th>
	 <th>DESCRIPTION</th>
	 <th>STATUS</th>
	</tr>
	};
	$result.=commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

# 
# LICENSE SERVER COMMAND LINE TESTS 
#
sub runLicserv{
	my $result="";
	my @cmdsAndResponses=(
	#COMMAND						#Exprcted Result
	["/eniq/sw/bin/licserv"			,"Usage: licserv start|stop|restart"],
	["/eniq/sw/bin/licserv stop | tail-1"	,"SMF disabling svc:/licensing/sentinel"],
	["/eniq/sw/bin/licserv start | tail-1"	,"SMF enabling svc:/licensing/sentinel"],
	#CHECK IF ONLINE 
	#["/eniq/sw/bin/licserv restart"	,"SMF enabling svc:/licensing/sentinel"]
	#CHECK IF ONLINE
	);
	$result=qq{
	<h3>RUN LICENSE SERVER CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>CMD</th>
	 <th>DESCRIPTION</th>
	 <th>STATUS</th>
	</tr>
	};
	$result.=commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

# 
# LICENSE MANAGER CMD LINE TESTS
#
sub runLicenseManager{
  my $result="";
  
  my $licenceLocation = "/eniq/home/dcuser/automation/*_license_*";
  my $testCMC = "CXC4011318";
  my $expectedFAJ = "FAJ 121 2183";
  my $expectedDesc = "LTE Cell Trace Streaming Termination Tech Pack";
  
  my @cmdsAndResponses=(
	["licmgr | head -1","Usage: /eniq/sw/bin/licmgr"],
	["licmgr -status | tail -1","License manager is running OK"],
	["licmgr -decode | head -1","Usage: /eniq/sw/bin/licmgr"],
	["licmgr -decode $licenceLocation | head -1","Reading license codes from file: \"/eniq/home/dcuser/automation/.*\""],
	["licmgr -install $licenceLocation | tail -1","Updating license manager"],
	["licmgr -getlicinfo | tail -1","Finished successfully"],
	["licmgr -getlockcode","Locking Code 1"],
	["licmgr -isvalid $testCMC","License for feature $testCMC is valid"],#"^(?:(?!not valid).)*\$"],
	["licmgr -uninstall $testCMC | head -1","Removed feature CXC4011318 from"],
	["licmgr -isvalid $testCMC","License for feature $testCMC is not valid"],
	["licmgr -listserv | head -1","Listing active servers on local subnet"],
	
	["licmgr -map faj $testCMC | head -1","$expectedFAJ"],
	["licmgr -map description $testCMC | head -1","$expectedDesc"],
	#["licmgr -map interface $testCMC | head -1",""],
	
	["licmgr -serverstatus | tail -1","Server:"],
	["licmgr -stop | tail -1","SMF disabling svc:/eniq/licmgr"],
	["svcs svc:/eniq/licmgr:default|tail -1","disabled"],
	["licmgr -start | tail -1","SMF enabling svc:/eniq/licmgr"],
	["svcs svc:/eniq/licmgr:default|tail -1","online"],
	["licmgr -restart | tail -1","SMF enabling svc:/eniq/licmgr"],
	["svcs svc:/eniq/licmgr:default| tail -1","online"],
	["licmgr -status | tail -1","License manager is running OK"],
	["licmgr -update","Updating license manager"],
	["licmgr -uninstall test | tail -1","Updating license manager"],
	#Reinstall Licsences
	["licmgr -install $licenceLocation | tail -1","Updating license manager"],
	
  );
	$result=qq{
	<h3>RUN LICMGR CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>CMD</th>
		 <th>DESCRIPTION</th>
		 <th>STATUS</th>
	   </tr>
	};
	$result.=commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

# 
# DWHDB COMMAND LINE TESTS 
#
sub runDwhdb{
	my $result="";
	my @cmdsAndResponses=(
		["dwhdb","Usage: dwhdb start|stop|restart|status"],
		["dwhdb stop | tail -1","SMF disabling svc:/eniq/dwhdb"],
		["svcs svc:/eniq/dwhdb:default | tail -1","disabled"],
		["sleep 60; dwhdb start | tail -1","SMF enabling svc:/eniq/dwhdb"],
		["svcs svc:/eniq/dwhdb:default | tail -1","online"],
		["sleep 60; dwhdb restart | tail -1","SMF enabling svc:/eniq/dwhdb"],
		["svcs svc:/eniq/dwhdb:default | tail -1","online"],
	);
	$result=qq{
		<h3>RUN DWHDB CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
			 <th>CMD</th>
			 <th>DESCRIPTION</th>
			 <th>STATUS</th>
		</tr>
	};
	$result.=commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

############################################################
# REPDB COMMAND LINE TESTS 
sub runRepdb{
	my $result="";
	my @cmdsAndResponses=(
		["repdb","Usage: repdb start|stop|restart|status"],
		["repdb stop | tail -1","SMF disabling svc:/eniq/repdb"],
		["svcs svc:/eniq/repdb:default | tail -1","disabled"],
		["sleep 60; repdb start | tail -1","SMF enabling svc:/eniq/repdb"],
		["svcs svc:/eniq/repdb:default | tail -1","online"],
		["sleep 60; repdb restart | tail -1","SMF enabling svc:/eniq/repdb"],
		["svcs svc:/eniq/repdb:default | tail -1","online"],
	);

	$result=qq{
		<h3>RUN REPDB CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>CMD</th>
		 <th>DESCRIPTION</th>
		 <th>STATUS</th>
		</tr>
	};
	$result .= commandsToTable(@cmdsAndResponses);
	$result.=qq{</TABLE>};
	return $result;
}

# 
# EC COMMAND LINE TESTS 
# runs all Glassfish cli test below
#
sub runEC{
	my $result="";
	$result=qq{
		<h3>RUN EC CLI CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
				<th>CMD</th>
				<th>DESCRIPTION</th>
				<th>STATUS</th>
			</tr>
	};

	foreach my $ec(@ecHosts){
		my $serviceName=$ec;
		$serviceName=~s/_//g;
		$serviceName=uc($serviceName);
		my @cmdsAndResponses=(
			[ sshCommand( $ec, "ec"),"Usage: ec start|stop|restart|status"],
			[ sshCommand( $ec, "ec stop | tail -1"),"SMF disabling svc:/eniq/ec"],
			[ sshCommand( $ec, "svcs svc:/eniq/ec:default | tail -1"),"disabled"],
			[ sshCommand( $ec, "ec start | tail -1"),"SMF enabling svc:/eniq/ec"],
			[ sshCommand( $ec, "svcs svc:/eniq/ec:default | tail -1"),"online"],
			[ sshCommand( $ec, "ec restart | tail -1"),"SMF enabling svc:/eniq/ec"],
			[ sshCommand( $ec, "svcs svc:/eniq/ec:default | tail -1"),"online"],
			[ sshCommand( $ec, "ec status | head -1"),"$serviceName is running"],
		);
		$result.=commandsToTable(@cmdsAndResponses);
	}
	$result.=qq{</TABLE>};
	return $result;
}

#----------------------------------------------------------
#	END Command Line Commands
#----------------------------------------------------------

#------------------------------------------------------------
#	SUBROUTINES FOR PRE VERIFICATION (2):
#
#	1.	pre()
#	2.	pre_load()
#
#-------------------------------------------------------------

# 
# PRE TEST, CHECKS THE inDIR: THE DIRECTORIES FOR ALL ETLS 
# This is a very simple test, just runs the query below and lists 
# the results in a table
# The table represents each entry for the inDir directory for all techpacks and 
# interfaces
# This means each techpack and interface should have an entry directory
# I believe in the past there were faults related with missing paths,
# if a path is missing the row is failed.
#
sub pre{
	my $result="";
	my $sql=qq{
		SELECT c.collection_set_name||"|"|| 
		SUBSTRING(action_contents_01,
		CHARINDEX('inDir=', action_contents_01),
		CHARINDEX('interfaceName=', 
		SUBSTRING(action_contents_01, CHARINDEX('inDir=', action_contents_01)))-2
		) 
		FROM 
		etlrep.meta_transfer_actions a 
		JOIN etlrep.meta_collections b 
		ON (   a.version_number = b.version_number 
		AND a.collection_id = b.collection_id 
		AND a.collection_set_id = b.collection_set_id) 
		JOIN etlrep.meta_collection_sets c 
		ON (   b.version_number = c.version_number 
		AND b.collection_set_id = c.collection_set_id) 
		WHERE 
		action_type = 'Parse' AND c.enabled_flag = 'Y'
		order BY 1;
		go
		EOF
	};
	my @result=undef;
	open(TABLES,"$ISQL -Uetlrep -Petlrep -h0 -Srepdb -w 50 -b << EOF $sql |");
	my @tables=<TABLES>;
	chomp(@tables);
	close(TABLES);
	$result.=qq{<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>INTERFACE</th>
	 <th>DIRECTORY</th>
	 <th>RESULT</th>
	</tr>
	};

	foreach my $tables (@tables){
		$_=$tables;
		my $status="";
		next if(/parser.header/);
		next if(/affected/);
		next if(/^$/);
		$tables=~ s/\t//g;
		$tables=~ s/\s//g;

		if(/AlarmInterfaces/ && /inDir=\$\{PMDATA_DIR\}\/AlarmInterface_/){
			$status="<td align=center><font color=darkblue><b>PASS</b></font></td>";
			&FT_LOG("   PASS\n");
		}
		elsif(/AlarmInterfaces/ && /inDir=\${PMDATA_DIR}\/AlarmInterface_/){
			$status="<td align=center><font color=#ff0000><b>FAIL</b></font></td>";
			&FT_LOG("   FAIL\n");
		}
		elsif(/INTF_/ && 
		/-eniq_events_topology/ && 
		/inDir=\$\{PMDATA_DIR\}\/eniq_events_topology\//){
			$status="<td align=center><font color=darkblue><b>PASS</b></font></td>";
			&FT_LOG("   PASS\n");
		}
		elsif(/INTF_/ && 
		!/-eniq_events_topology/ && 
		/inDir=\$\{PMDATA_DIR\}\/\$\{OSS\}\//){
			$status="<td align=center><font color=darkblue><b>PASS</b></font></td>";
			&FT_LOG("   PASS\n");
		}else{
			$status="<td align=center><font color=#ff0000><b>FAIL</b></font></td>";
			&FT_LOG("	FAIL\n");
		}

		$tables=~ s/\t//g;
		$tables=~ s/\s//g;
		$tables=~ s/^/<tr><td>/g;
		$tables=~ s/\|/<\/td><td>/g;
		$tables=~ s/$/<\/td>$status<\/tr>/g;
		$result.= "$tables\n";
	}
	
	$result.= "</TABLE>";
	return $result;
}

# 
# PRE_LOAD
# This process executes 4 tasks, is used to kick off loading, update monitoring types, first loadings and aggregation:
# engine -e startSet DWH_MONITOR SessionLoader_Starter Start
# engine -e startSet DWH_MONITOR UpdateMonitoredTypes Start
# engine -e startSet DWH_MONITOR UpdateFirstLoadings Start 
# engine -e startSet DWH_MONITOR AggregationRuleCopy Start
# NOTE: Currently there is no way to load data and then to re run again PRE_LOAD, because it 
# creates a huge queue of aggregation processes
#
sub preLoad{
	my $result="<h3>PRE_LOAD</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>STATUS</th>
	</tr>
	";
	#$result.="/eniq/sw/bin/engine -e startSet DWH_MONITOR SessionLoader_Starter Start<br>";
	#my @out1=executeThisWithLogging("/eniq/sw/bin/engine -e startSet DWH_MONITOR SessionLoader_Starter Start ");
	#sleep(10);
	#$result.="/eniq/sw/bin/engine -e startSet DWH_MONITOR UpdateMonitoredTypes Start<br>";
	#my @out2=executeThisWithLogging("/eniq/sw/bin/engine -e startSet DWH_MONITOR UpdateMonitoredTypes Start ");
	#sleep(10);
	#$result.="/eniq/sw/bin/engine -e startSet DWH_MONITOR UpdateFirstLoadings Start<br>";
	$result.= "<tr><td align=center><font color=darkblue><b>/eniq/sw/bin/engine -e startSet DWH_MONITOR UpdateFirstLoadings Start</b></font></td></tr>\n";
	my @out3=executeThisWithLogging("/eniq/sw/bin/engine -e startSet DWH_MONITOR UpdateFirstLoadings Start ");
	sleep(60);
	#$result.="/eniq/sw/bin/engine -e startSet DWH_MONITOR AggregationRuleCopy Start<br>";
	$result.= "<tr><td align=center><font color=darkblue><b>/eniq/sw/bin/engine -e startSet DWH_MONITOR AggregationRuleCopy Start</b></font></td></tr>\n";
	my @out4=executeThisWithLogging( "/eniq/sw/bin/engine -e startSet DWH_MONITOR AggregationRuleCopy Start ");
	sleep(60);
	my @out=(@out3,@out4);
	foreach my $out (@out){
		#$result.=$out."<br>";
		$result.= "<tr><td align=center><font color=darkblue><b>$out</b></font></td></tr>\n";
	}
	
	$result.= "</TABLE>";
	return $result;
}

#----------------------------------------------------------
#	END PRE VERIFICATION
#----------------------------------------------------------

#------------------------------------------------------------	
#	SUBROUTINES - ADMIN UI SYSTEM MONITOR COMMANDS (19)
#
#	0.	OverallStatus() (Tests 1-7)
#	1.	SystemStatus()
#	2.	DwhStatus()
#	3.	RepStatus()
#	4.	EngineStatus()
#	5.	SchedulerStatus()
#	6.	LicservStatus()
#	7.	LicmgrStatus()
#	8.	MediationStatus() GONE
#	9.	GlassfishStatus() GONE
#	13.	DiskSpace()
#	14.	eniqVersion()
#	15.	InstalledModules()
#	16.	InstalledTps()
#	17.	help()
#	18.	MaxUsersAdminui()
#	19.	wrongUser()
#
#------------------------------------------------------------

#
# OVERALLSTATUS
# This subroutine simply runs the next 9 tests in one.
sub OverallStatus{
	my  $result=qq{
		<h3>OVERALL SYSTEM STATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
	};
	$result.=SystemStatus();
	$result.=DwhStatus();
	$result.=EngineStatus();
	$result.=SchedulerStatus();
	$result.=LicservStatus();
	$result.=LicmgrStatus();
	$result.="</TABLE>";
	return $result;
}

#
# SYSTEMSTATUS
# This subroutine goes to admin UI and checks the System status, greps if there are RED bulbs or status NOLOADS
# If that's the case it fails the test case.
sub SystemStatus{
	my  $result="";

	if($OverallStatus eq "false"){
		$result=qq{
		<h3>SYSTEMSTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
		};
	}
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/LoaderStatusServlet");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/status.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# Check for General Pass/Fail
	my @status=executeThisQuiet("egrep '(red_bulp|NoLoads)' /eniq/home/dcuser/automation/html/status.html | egrep -c -v 'ec_'");
	if($status[0] == 0){
		&FT_LOG("Overall System Status - PASS\n");
		$result.="<tr><td>Overall System Status</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("Overall System Status - FAIL\n");
		$result.="<tr><td>Overall System status</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	} 

	# Mediation GW
	my @status1=executeThisQuiet("egrep '(Platform Monitor)' /eniq/home/dcuser/automation/html/status.html");
	my @status2=executeThisQuiet("egrep '(EC1)' /eniq/home/dcuser/automation/html/status.html");
	my @status3=executeThisQuiet("egrep '(Control Zone)' /eniq/home/dcuser/automation/html/status.html");

	# Glassfish
	my @status4=executeThisQuiet("egrep '(Application Server Host)' /eniq/home/dcuser/automation/html/status.html");
	my @status5=executeThisQuiet("egrep -i '(domain1)' /eniq/home/dcuser/automation/html/status.html");

	$status1[0] =~ s/.*Status: //;
	$status2[0] =~ s/.*EC1 is //;
	$status3[0] =~ s/.*Control Zone is //;
	$status4[0] =~ s/.*Status: //;
	$status5[0] =~ s/.*domain1 //;

	(my $med_platform, my $rest1) = split /</, $status1[0];
	(my $med_ec1, my $rest2) = split /</, $status2[0];
	(my $med_control, my $rest3) = split /</, $status3[0];
	(my $glassfish_server, my $rest4) = split /</, $status4[0];
	(my $glassfish_domain1, my $rest5) = split /</, $status5[0];
	chomp($glassfish_domain1);

	if($med_platform eq 'Running'){
		&FT_LOG("Mediation Plaform is Running - PASS\n");
		$result.="<tr><td>Mediation Platform is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("Mediation Platform is not running - FAIL\n");
		$trafficLightColour = "red";
		$result.="<tr><td>Mediation Platform is not running</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}
	if($med_ec1 eq 'running'){
		&FT_LOG("Mediation Execution Context1 is running - PASS\n");
		$result.="<tr><td>Mediation Execution Context1 is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		if(isMultiBladeServer()){
			&FT_LOG("Mediation Execution Context1 is not running - FAIL\n");
			$trafficLightColour = "red";
			$result.="<tr><td>Mediation Execution Context1 is not running</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
		}else{
			&FT_LOG("Mediation Execution Context1 is NOT running (singleblade) - PASS\n");
			$result.="<tr><td>Mediation Execution Context1 is NOT running (singleblade)</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}
	}
	if($med_control eq 'running'){
		&FT_LOG("Mediation Control Zone is running - PASS\n");
		$result.="<tr><td>Mediation Control Zone is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("Mediation Control Zone is not running - FAIL\n");
		$trafficLightColour = "red";
		$result.="<tr><td>Mediation Control Zone is not running</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}
	if($glassfish_server eq 'Running'){
		&FT_LOG("Glassfish Server is running - PASS\n");
		$result.="<tr><td>Glassfish Server is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("Glassfish Server is not running - FAIL\n");
		$trafficLightColour = "red";
		$result.="<tr><td>Glassfish Server is not running</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}
	if($glassfish_domain1=~/running/i){
		&FT_LOG("Glassfish Domain1 is running - PASS\n");
		$result.="<tr><td>Glassfish Domain1 is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("Glassfish Domain1 is not running - FAIL\n");
		$trafficLightColour = "red";
		$result.="<tr><td>Glassfish Domain1 is not running</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}

	# LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

#
# DWHSTATUS
# this subroutine goes to AdminUI and verifies the DWHSatus, 
# greps DWH_DBSPACES_MAIN, if present it will pass the test
sub DwhStatus{
	my  $result="";
	if($OverallStatus eq "false"){
		$result=qq{
		<h3>DWHSTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
		};
	}
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/dwhstatus.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/StatusDetails?ds=rockDwhDba\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/dwhstatus.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/StatusDetails?ds=rockDwhDba\"");
	#  my @status=executeThisWithLogging("grep -c DWH_DBSPACES_MAIN /eniq/home/dcuser/automation/html/dwhstatus.html");
	my @statusMain=executeThisQuiet("egrep  '(Main Store Out Of Space)' /eniq/home/dcuser/automation/html/dwhstatus.html");
	my @statusTemp=executeThisQuiet("egrep  '(Temp Store Out Of Space)' /eniq/home/dcuser/automation/html/dwhstatus.html");

	$statusMain[0] =~ s/^.*Main Store Out Of Space:\s\S*\s\S*\s\S*>//;
	$statusMain[0] = substr($statusMain[0],0,1);

	$statusTemp[0] =~ s/^.*Temp Store Out Of Space:\s\S*\s\S*\s\S*>//;
	$statusTemp[0] = substr($statusTemp[0],0,1);

	if($statusMain[0] eq 'N'){
		&FT_LOG("DWH: Main Store - PASS\n");
		#$result.="DWH: Main Store - PASS<br>\n";
		#$result.= "<tr><td align=center><font color=darkblue><b>DWH: Main Store - PASS</b></font></td></tr>\n";
		$result.="<tr><td>DWH: Main Store</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("DWH: Main Store - FAIL\n");
		$trafficLightColour = "red";
		#$result.="DWH: Main Store - FAIL<br>\n";
		#$result.= "<tr><td align=center><font color=#ff0000><b>DWH: Main Store - FAIL</b></font></td></tr>\n";
		$result.="<tr><td>DWH: Main Store</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	} 

	if($statusTemp[0] eq 'N'){
		&FT_LOG("DWH: Temp Store - PASS\n");
		#$result.="DWH: Temp Store - PASS<br>\n";
		#$result.= "<tr><td align=center><font color=darkblue><b>DWH: Temp Store - PASS</b></font></td></tr>\n";
		$result.="<tr><td>DWH: Temp Store</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("DWH: Temp Store - FAIL\n");
		$trafficLightColour = "red";
		#$result.="DWH: Temp Store - FAIL<br>\n";
		#$result.= "<tr><td align=center><font color=#ff0000><b>DWH: Temp Store - FAIL</b></font></td></tr>\n";
		$result.="<tr><td>DWH: Temp Store</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	} 

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

#
# REPSTATUS
# This subroutine checks AdminUI and Repstatus 
# greps the webpage for IQ.Server, if present it passes the test
sub RepStatus{
	my  $result="";
	if($OverallStatus eq "false"){
		$result=qq{
		<h3>REPSTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
		};
	}
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/repstatus.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/StatusDetails?ds=rockEtlRepDba\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/repstatus.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/StatusDetails?ds=rockEtlRepDba\"");
	my @status=executeThisWithLogging("egrep '(Main IQ)' /eniq/home/dcuser/automation/html/repstatus.html");

	$status[0] =~ s/^.*Main IQ Blocks Used:\s\S*\s\S*\s\S*\s\S*\s\S*\s//;
	(my $blocks, my $rest) = split /%/, $status[0];

	if($blocks < 90){
		&FT_LOG("REP: Main IQ Blocks Used($blocks%) -  PASS\n");
		$result.="<tr><td>REP: Main IQ Blocks Used($blocks%)</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("REP: Main IQ Blocks Used($blocks%) - FAIL\n");
		$trafficLightColour = "red";
		$result.="<tr><td>REP: Main IQ Blocks Used($blocks%)</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	} 

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

#
# ENGINESTATUS
# This subroutine goes to AdminUI and checks that engine status is Normal
sub EngineStatus{
	my  $result="";
	if($OverallStatus eq "false"){
		$result=qq{
		<h3>ENGINESTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
		};
	}
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/enginestatus.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/EngineStatusDetails\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/enginestatus.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/EngineStatusDetails\"");
	my @status=executeThisWithLogging("grep -c Normal /eniq/home/dcuser/automation/html/enginestatus.html");
	if($status[0] > 0){
		&FT_LOG("EngineStatus PASS\n");
		#$result.="PASS<br>\n";
		#$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		$result.="<tr><td>ENGINESTATUS</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("EngineStatus FAIL\n");
		$trafficLightColour = "red";
		#$result.="FAIL<br>\n";
		#$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
		$result.="<tr><td>ENGINESTATUS</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	} 
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

# 
# SCHEDULESTATUS
# This subroutine goes to AdminUI and checks that scheduler status is active
sub SchedulerStatus{
	my  $result="";
	if($OverallStatus eq "false"){
		$result=qq{
		<h3>SCHEDULESTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
		};
	}
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/schedulerstatus.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/SchedulerStatusDetails\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/schedulerstatus.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/SchedulerStatusDetails\"");
	my @status=executeThisWithLogging("grep -c active /eniq/home/dcuser/automation/html/schedulerstatus.html");
	if($status[0] > 0){
		&FT_LOG("SchedulerStatus PASS\n");
		#$result.="PASS<br>\n";
		#$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		$result.="<tr><td>SCHEDULESTATUS</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("SchedulerStatus FAIL\n");
		$trafficLightColour = "red";
		#$result.="FAIL<br>\n";	 
		#$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
		$result.="<tr><td>SCHEDULESTATUS</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}
	
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

#
# LICSERVSTATUS
# This subroutine goes to AdminUI and checks that the licenserver lists the FAJ or CXC
# is number is higher than 40 the test is passed.
sub LicservStatus{
	my  $result="";
	if($OverallStatus eq "false"){
		$result=qq{
		<h3>LICSERVSTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>SYSTEM ATTRIBUTE</th>
		 <th>STATUS</th>
		</tr>
		};
	}
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/licserv.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"command='Disk space information'&submit=Start\" \"$LOCALHOST/adminui/servlet/ShowInstalledLicenses\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/licserv.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/ShowInstalledLicenses\"");
	my @status=executeThisWithLogging("egrep -c '(FAJ|CXC)' /eniq/home/dcuser/automation/html/licserv.html");
	if($status[0] > 2){
		&FT_LOG("LicservStatus PASS\n");
		#$result.="PASS<br>\n";
		#$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		$result.="<tr><td>LICSERVSTATUS</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("LicservStatus FAIL\n");
		$trafficLightColour = "red";
		#$result.="FAIL<br>\n";	 
		#$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
		$result.="<tr><td>LICSERVSTATUS</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	} 
	# LOGOUT
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

#
# LICMGRSTATUS 
# This subroutine goes to AdminUI and checks licmgr and verifies that is 'running OK'
# is so the test is passed.
sub LicmgrStatus{
	my  $result="";
	if($OverallStatus eq "false"){
		$result=qq{
		<h3>LICMGRSTATUS</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		   <tr>
			 <th>SYSTEM ATTRIBUTE</th>
			 <th>STATUS</th>
		   </tr>
		};
	}
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=localtime(time);

	$year=$year+1900; 
	my $month= sprintf("%02d",$mon+1);
	my $day  = sprintf("%02d",$mday);

	#system("/usr/sfw/bin/wget --no-check-certificate -O  /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	#my @status=executeThisWithLogging("egrep -c '(is running OK)' /eniq/home/dcuser/automation/html/liclog.html") ;

	# SEND USR AND PASSWORD
	unlink("/eniq/home/dcuser/automation/html/cookies.txt");
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/LoaderStatusServlet");
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/LoaderStatusServlet");
	
	open(PAGE, "</eniq/home/dcuser/automation/html/liclog.html");
	my @page=<PAGE>;
	close(@page);
	if(grep(/License manager is running OK/,@page)){
		&FT_LOG("LicmgrStatus PASS\n");
		#$result.="PASS<br>\n";
		#$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		$result.="<tr><td>LICMGRSTATUS</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("LicmgrStatus FAIL\n");
		$trafficLightColour = "red";
		#$result.="FAIL<br>\n";	 
		#$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
		$result.="<tr><td>LICMGRSTATUS</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	if($OverallStatus eq "false"){
		$result.= "</TABLE>";
	}
	return $result;
}

# 
# DISKSPACE
# This subroutine goes to AdminUI and checks that the DiskSpace information
# displayed in Monitoring Commands has the right header, and displays info for eniq_sp 
# A check is made to ensure all filesystems are below 80% capacity (PASS), o/w WARNING.
# Each filesystem and % capacity is displayed in a html table.
#
sub DiskSpace{
	my  $result=qq{
	<h3>DISK SPACE</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>FILESYSTEM CAPACIY</th>
	 <th>STATUS</th>
	</tr>
	};

	&FT_LOG("Getting cookies from $LOCALHOST/adminui/servlet/CommandLine");
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Disk+space+information&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	# SEND USR AND PASSWORD
	&FT_LOG("Log in to $LOCALHOST/adminui/j_security_check");
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	&FT_LOG("Download $LOCALHOST/adminui/servlet/CommandLine to /eniq/home/dcuser/automation/html/dsk.html");
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/dsk.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Disk+space+information&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	my @status=executeThisQuiet("egrep  '(Filesystem\s*size   used  avail capacity  Mounted on|eniq_sp)' | grep % /eniq/home/dcuser/automation/html/dsk.html");
	my $diskResult = 0;
	foreach my $status (@status){
		$_=$status;
		while ($_ =~ m/\d+%\s+\S+</g){
			(my $val, my $rest1) = split /</, $&;
			if ($val > 90 && $status !~ m/\d+%\s+\/net\S+</g){
				$diskResult =1;
				print ("Filesystem:$val - FAIL\n");
				$trafficLightColour = "orange";
				$result.="<tr><td>$val</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
			}
		}
	}
	if($diskResult == 0){
		print ("Disk Space - PASS\n");
		$result.="<tr><td>Disk Space Test</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		#		$result .= "Filesystem:$val - PASS<br>\n";
	}else{
		executeThisQuiet("egrep  '(Filesystem\s*size   used  avail capacity  Mounted on|eniq_sp)' | grep % /eniq/home/dcuser/automation/html/dsk.html");
	}

	#LOGOUT 
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout");
	$result.="</TABLE>";
	return $result;
}

# 
# ENIQVERSION
# This subroutine goes to AdminUI and checks the version, if the 
# string starts with ENIQ_STATUS ENIQ the test is passed.
# else is failed
# NOTE: when the version file is not available another string is displayed like 'version not available'
#
sub eniqVersion{
	my  $result=qq{
		<h3>ENIQ VERSION</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>VERSION</th>
		 <th>STATUS</th>
		</tr>
	};

	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=ENIQ+software+version&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/version.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=ENIQ+software+version&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");

	my @status=executeThisQuiet("egrep  '(ENIQ_STATUS)' /eniq/home/dcuser/automation/html/version.html") ;

	if($status[0] =~"ENIQ_STATUS ENIQ"){
		my $version = $status[0];
		$version =~ s/.*Shipment_//;
		$version =~ s/AOM.*//;
		print "ver:$version\n";

		$result.="<tr><td>$version</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Eniq Version:$version\n");
	}else{
		&FT_LOG("Eniq Version FAIL\n");
		$trafficLightColour = "orange";
		$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}

	#LOGOUT 
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout");

	$result.="</TABLE>";
	return $result;
}

# 
# INSTALLED_MODULES
# This subroutine goes to AdminUI and verifies that the installed modules exist
# NOTE: it has to be updated when new modules are added
#
sub InstalledModules{
	my $result="<h3>INSTALLED_MODULES</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>INSTALLED MODULES</th>
		 <th>STATUS</th>
		</tr>
	";
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"command=Installed+modules&submit=Start&action=/servlet/CommandLine\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/modules.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"command=Installed+modules&submit=Start&action=/servlet/CommandLine\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	my @status=executeThisQuiet("cat /eniq/home/dcuser/automation/html/modules.html");

	foreach my $status (@status){
		$_=$status;
		next if(!/>module/);
		$status =~ s/<td><font size..-1. face..Courier.>//g;
		$status =~ s/<br .>/\n/g;
		$status =~ s/<.font><.td>//g;

		if(/3GPP32435|AdminUI|MDC|alarm|alarmcfg|ascii|asn1|common|csexport|ct|dbbaseline|diskmanager|dwhmanager|ebs|ebsmanager|engine|export|installer|libs|licensing|mediation|monitoring|nascii|nossdb|omes2|omes|parser|raml|redback|repository|runtime|sasn|scheduler|stfiop|uncompress|xml/){
			&FT_LOG("$status PASS\n");
			my $list = $_;
			$list =~ s/<td>//g;
			$list =~ s/<\/td>//g;
			$result.= "<tr><td>Install Modules Test</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}else{
			&FT_LOG("FAIL\n");
			my $list = $_;
			$list =~ s/<td>//g;
			$list =~ s/<\/td>//g;
			$result.= "<tr><td>$list</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
			$trafficLightColour = "orange";
			my @status=executeThisWithLogging("cat /eniq/home/dcuser/automation/html/modules.html");
		}
	}
	
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	$result.="</TABLE> <br>\n";
	return $result;
}

# 
# INSTALLED_TPS
# This subroutine goes to AdminUI and verifies that the techpacks display the columns
# Note: this module does not check if the right version is installed, that is handled in
# the BASELINE checker
#
sub InstalledTps{
	my $result="<h3>INSTALLED_TPS</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	";
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Installed+tech+packs&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/tps.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Installed+tech+packs&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	my @status=executeThisQuiet("egrep '.*'  /eniq/home/dcuser/automation/html/tps.html");
	chomp(@status);
	my $tpsString = join(" ",@status);
	$tpsString =~ s/.*<tr><td colspan="6" class="basic">Active Tech Packs<\/td><\/tr>//;
	$tpsString =~ s/<tr> 			<td colspan="6" class="basic">&nbsp;<\/td> 		<\/tr>.*//;
	$tpsString =~ s/<tr><td colspan="6" class="basic">Not active Tech Packs<\/td><\/tr>.*//;
	#td -> th

	my $tpsTitle = $tpsString;
	$tpsString =~ s/<tr>.*?<\/tr>//;
	$tpsTitle =~ s/<\/tr>.*/<\/tr>/;
	$tpsTitle =~ s/<td/<th/g;
	$tpsTitle =~ s/\/td>/\/th>/g;
	$tpsTitle =~ s/<\/tr>/<th>RESULT<\/th><\/tr>/;

	@status=executeThisQuiet("egrep '(					<tr>|						<td class=.basic.>)'  /eniq/home/dcuser/automation/html/tps.html");
	chomp(@status);
	my $tpname="";
	my $tpcoa="";
	my $tprev="";
	my $tp="";
	my $tpactive="";
	my $tpdate="";
	my $finalresult=0;

	print "----------------------------------------\n$tpsString\n----------------------------------------";
	foreach my $status (@status){
		$_=$status;
		$status =~ s/                                                <td class=.basic.>//;
		$status =~ s/<.td>//;
		$status =~ s/.*">//;
		#" - comment line added for editor compatibility.
		if(/<tr>|<.tr>/){ 
			&FT_LOG("\n");
		}
		if(/Active|COA 252|PM|\w_\w|Topology|System|20..-\d\d-\d\d|n.a|BASE/i){
			&FT_LOG("$status	");
			$tpsString =~ s/\t<\/tr>/<td align=center><font color=darkblue><b>PASS<\/b><\/font><\/td><\/tr>/;
		}
		elsif(/ERROR|Exception|Fail|Not found/i){
			$finalresult++;
			$tpsString =~ s/\t<\/tr>/<td align=center><font color=#ff0000><b>FAIL<\/b><\/font><\/td><\/tr>/;
			$trafficLightColour = "orange";
		}
	}

	if($finalresult>0){
		&FT_LOG("FAIL\n");
	}else{
		&FT_LOG("\nPASS\n");
	}

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	$tpsString = "$tpsTitle$tpsString";
	$result.= $tpsString;
	$result.="</TABLE> <br>\n";
	return $result;
}

############################################################
# GET DOMAIN
# This is a utility, just checks and returns the domain
sub getDomain{
	open(DOMAIN,"grep domain /etc/resolv.conf |  cut -d ' ' -f 2 | ");
	my @domain=<DOMAIN>;
	chomp(@domain);
	close(DOMAIN);
	return $domain[0];
}

# 
# HELP
# This subroutine logs in to adminUI and 
# cuts the help links and puts them in a table
# the tester must read the help links to ensure they are OK
# This test should be considered passed when the links are tested
# if no links are displayed the test must be failed
#
sub help{
	my $result="";
	my $host=getHostName();
	my $domain=getDomain();
	# SAVE COOKIES
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/LoaderStatusServlet");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/help.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	open(HELP, "< html/help.html");
	my @help=<HELP>;
	print @help;
	chomp(@help);
	my $title="manual";
	$result=qq{
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>HELP TITLE</th>
	 <th>LINK</th>
	 <th>RESULT</th>
	</tr>
	};
	
	foreach my $help (@help){
		$_=$help;
		$help =~ s/<tr><td><a class="menulink" href=.servlet.\w>//;
		$help =~ s/<tr><td><a class="menulink" href=.adminui.servlet.\w>//;
		$help =~ s/.*href=.//;
		$help =~ s/. onClick.*//;
		$help =~ s/<.a>//;
		$help =~ s/.*\">//;
		$help =~ s/.*>//;
		next if(/Logout/);
		if(/User Manual/){
			$result.= "<tr><td>User Manual</td>";
			$help= "http://$host.$domain:8080".$help;
			$result.= "<td align=center><a href=$help>$help<a/></td><td align=center><font color=darkblue><b>PASS</b></td></tr>\n";
		}elsif(/adminui.manual/){
			$help =~ s/adminui.manual/manual/;
			#system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/$title.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $help");
			$help= "http://$host.$domain:8080".$help;
			$result.= "<td align=center><a href=$help>$help<a/></td><td align=center><font color=darkblue><b>PASS</b></td></tr>\n";
		}elsif(/menulink/){
			$title=$help;
			#$title=~s/ /_/g;
			$result.= "<tr><td>$title</td>";
		}
	}
	close(HELP);
	$result.=qq{
	</TABLE>
	};
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

#
# MAX_USERS_ADMINUI
# NOTE: this subroutine exceedes the MAX number of users and produces a total fault in the system
# After executing this process all services are restarted because the servers is not usable.
#
sub MaxUsersAdminui{
	for (my $i=0;$i<30;$i++){
		&FT_LOG("TRIAL: $i\n");
		system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/max.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LoaderStatusServlet\"");
		# SEND USR AND PASSWORD
		system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
		# GET Information
		system("/usr/sfw/bin/wget  --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/max.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LoaderStatusServlet\"");
	}
	my @status=executeThisWithLogging("grep -c 'Max connection reached' /eniq/home/dcuser/automation/html/max.html");
	 
	if($status[0] >= 1 ){
		&FT_LOG("PASS\n");
	}else{
		&FT_LOG("FAIL\n");
	} 
	system("/eniq/admin/bin/eniq_service_start_stop.bsh -s engine -a clear");
	system("dwhdb restart");
	system("repdb restart");
	system("webserver restart");
	system("engine restart");
	&FT_LOG("Sleep 2 min to allow processes to recover...\n");
	sleep(2*60);
}

#
# ADMINUI WRONG USER
# Checks in AdminUI if a user tries to enter with a wrong user or password
# then the user is redirected again to the login screen
#
sub wrongUser{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/wrong.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LoaderStatusServlet\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/cookies.txt --post-data 'action=j_security_check&j_username=wrong&j_password=wrong' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/wrong.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LoaderStatusServlet\"");
	my $result="<h3>ADMINUI WRONG USER</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
			<th>STATUS</th>
		</tr>
	";

	open(WO,"< /eniq/home/dcuser/automation/html/wrong.html ");
	my @wo=<WO>;
	print @wo;
	close(WO);
	my $found=0;
	foreach my $wo (@wo){
		$_=$wo;
		if(m%<form id=.login. action=./adminui/login.jsp. method=.POST.>%){
			$found++;
		}
	}
	if($found == 1){
		&FT_LOG("PASS\n");
		#$result.= "<font color=darkblue><b>PASS</b></font><br>\n";
		$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.= "<font color=#ff0000><b>FAIL</b></font><br>\n";
		$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	$result.="</TABLE> <br>\n";
	return $result;
}

#----------------------------------------------------------
#	END ADMIN UI System Monitor Commands (19)
#----------------------------------------------------------

#------------------------------------------------------------	
#	SUBROUTINES - GENERIC TESTS (3)
#
#	1.	VerifyDirectories()
#	2.	VerifyAdminScripts()
#	3.	ActiveProcs()
#
#------------------------------------------------------------

#
# VERIFY_DIRECTORIES
# This test only checks that the directories do not exceed 90% 
# os space, is every thing is below that the test is passed.
#
sub VerifyDirectories{
	my  $result=qq{
	<h3>VERIFY DIRECTORY SPACE</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>CMD</th>
	 <th>STATUS</th>
	</tr>
	};
	my $testResult = 0;
	my @dir=executeThisWithLogging("df -lk | grep eniq ");
	chomp(@dir);
	foreach my $dir (@dir){
		my @line = split(/\s+/,$dir);
		&FT_LOG("$line[4] $line[5]");
		$line[4]=~s/%//;
		if($line[4]> 90){
			$result.="<tr><td>$line[4] $line[5]</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
			&FT_LOG("   FAIL\n");
			$testResult = 1;
		}
	}
	if($testResult==0){
		$result.="<tr><td>Verify directories test</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("  PASS\n");
	}
	$result.="</TABLE>";
	return $result;
}

#
# VERIFY ADMIN SCRIPTS
# This test executes each of the scripts below and expects a result 
# from the console, if the output includes words like 
# /Exception|Execute failed|cannot execute/i
# if so the test is failed
# else is passed.
#
sub VerifyAdminScripts{
	my @cmds=(
	"cleanup_after_restore.bsh",
	"delete_dwh_emmadb.bsh",
	"eniq_service_start_stop.bsh",
	"eniq_smf_start_stop.bsh",
	"licmgr_rollback.bsh",
	"manage_eniq_features.bsh",
	"manage_eniq_oss.bsh",
	"manage_eniq_services.bsh",
	"manage_eniq_status.bsh",
	"manage_eniq_techpacks.bsh",
	"manage_eniq_tp_interf.bsh",
	"manage_snapshot.bsh",
	"snapshot_fs.bsh",
	"update_cell_node_count.bsh",
	"zfs_snapshot.bsh"
	);
	my  $result=qq{
	<h3>RUN CMD LINE CHECK EXCEPTIONS OR ERRORS ON EXECUTION</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	  <th>CMD</th>
	  <th>DESCRIPTION</th>
	  <th>STATUS</th>
	</tr>
	};
	foreach my $cmd ( @cmds ){
		my $script=qq{ 
		(
		sleep 2 ;
		echo "root\n" ; sleep 2
		echo "shroot\n"; sleep 2 ;
		echo "cd /eniq/admin/bin/";
		echo "bash $cmd" ; sleep 2 ;
		echo "exit\n";
		) | telnet localhost
		};
		my @res=executeThisWithLogging($script);
		my @result=map {$_."<br>"} @res;
		$result.= "<tr><td>$cmd:</td><td>@result</td>\n";
		my $check = 0;
		foreach my $res (@result){
			$_=$res;
			if(/Exception|Execute failed|cannot execute/i){
				$result.= "<td><font color=ff0000><b>FAIL</b></font></td></tr>";
				&FT_LOG("FAIL\n");
			}
			elsif(/OK|You must be root|shall only be used by SMF|Usage:| is online.|All files within .eniq.data.etldata.adapter_tmp removed|Listing active servers on local subnet|ERROR :|The existing default value set is 1000/){
				$result.= "<td><font color=darkblue><b>PASS</b></font></td></tr>";
				&FT_LOG("PASS\n");
				$check = 5;
			}
		}
		if((@result)==0){
			$result.= "<td><font color=ff0000><b>FAIL</b></font></td></tr>";
			&FT_LOG("FAIL\n");
		}elsif($check eq 0){
			$result.= "<td><b>N/A</b></font></td></tr>";
		}
	} 
	$result.=qq{</TABLE>};
	return $result;
}

#
# ACTIVE PROCS
# This subroutine goes to AdminUI and verifies that the Monitoring Commands displays active processes
#
sub ActiveProcs{
	my $result="<h3>ACTIVE PROCS</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
					<th>ACTIVE PROCS</th>
					<th>STATUS</th>
			</tr>
	";

	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Most+active+processes&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");

	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/active.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Most+active+processes&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");

	my @status=executeThisWithLogging("cat /eniq/home/dcuser/automation/html/modules.html");

	foreach my $status (@status){
		$_=$status;
		next if(!/>module/);
		$status =~ s/<td><font size..-1. face..Courier.>//g;
		$status =~ s/<br .>/\n/g;
		$status =~ s/<.font><.td>//g;

		if(/3GPP32435|AdminUI|MDC|alarm|alarmcfg|ascii|asn1|common|csexport|ct|dbbaseline|diskmanager|dwhmanager|ebs|ebsmanager|eng
		ine|export|installer|libs|licensing|mediation|monitoring|nascii|nossdb|omes2|omes|parser|raml|redback|repository|runtime|sasn
		|scheduler|stfiop|uncompress|xml/)
		{
			&FT_LOG("$status PASS\n");
			my $list = $_;
			$list =~ s/<td>//g;
			$list =~ s/<\/td>//g;
			$result.= "<tr><td>$list</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}else{
			&FT_LOG("FAIL\n");
			my $list = $_;
			$list =~ s/<td>//g;
			$list =~ s/<\/td>//g;
			$result.= "<tr><td>$list</td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";
		}
	}


	#my @status=executeThisWithLogging("cat  /eniq/home/dcuser/automation/html/active.html");
	#foreach my $status (@status)
	#{
	#	$_=$status;
	#   next if(!/^<td><font size=.-1. face=.Courier.>   /);
	#   $status =~ s/<td>|<.td>/\n/g;
	#   $status =~ s/<br .>/\n/g;
	#
	#   if(/dcuser|root|Total|USERNAME/)
	#   {
	#		&FT_LOG("PASS\n");
	#		$result.= "<tr><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	#   }
	#   else
	#   {
	#		&FT_LOG("FAIL\n");
	#		$result.= "<tr><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>\n";	
	#   }
	#} 

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");

	$result.="</TABLE> <br>\n";
	return $result;
}

#Admin UI testing by selenium
sub AdminUiTest{
    my  $result=qq{
    <h3>ADMIN UI TEST</h3>
    <TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
    <tr>
    <th>TEST CASE</th>
    <th>STATUS</th>
    </tr>
    };
    my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
    $year = $year + 1900;
    $mon = $mon + 1;
    if($mday < 10){
        $mday = "0$mday";
    }
    @report = glob("/eniq/home/dcuser/automation/Admin_UI/Admin_UI_Report_$year-$mon-$mday*");
    $reportFile = $report[0];
    print "reportFile = $reportFile\n";
    open(TEXT, "$reportFile");
    my @lines1 = <TEXT>;
    foreach my $status (@lines1){
        my @line = split(/:/,$status);
        if($line[1] =~ " Pass"){
            $result.="<tr><td>$line[0]</td><td align=center><font color=green><b>PASS</b></font></td></tr>\n";
            &FT_LOG("   Pass\n");
        }elsif($line[1] =~ " Fail"){
            $result.="<tr><td>$line[0]</td><td align=center><font color=red><b>FAIL</b></font></td></tr>\n";
            &FT_LOG("   Fail\n");
        }
    }
    close(TEXT);
    $result.="</TABLE>";
    return $result;
}

sub deletepreferences_Wcdma{
        #truncate table user preference

        sqlRepDbDelete("delete from dwhrep.UserPreferences");
        &FT_LOG("deleting from user preferences table for WCDMA");
        }

#------------------------------------------------------------	
#	SUBROUTINES - TOPOLOGY (2)
#
#	1.	loadTopology()
#	2.	loadTopologyGeneric()
#	3.	verifyTopology()
#	4.	getAllTables4TP
#	5.	getTopologyLoading()
#	6.	ensureInterfaceIsActivated()
#------------------------------------------------------------

# 
# LOAD TOPOLOGY
# This test is in charge of unzipping the topology file and 
# wait 20 minutes 
# then it queries the database for the topology tables and counts the rows
# if the number of rows is 0, it fails the test case
#
sub loadTopology{
	my $server   = "159.107.220.49";
	my $user     = "gubasadm";
	my $password = "gubas";
	my $force=0;
	my $status=0;
	my %params;

	if((! -e "$topology_present") && (-e "$concurrent_present")){
		system "touch $topology_present";
	}
	my $result=qq{
	<h3>LOAD TOPOLOGY</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	open(STAT,"</eniq/admin/version/eniq_status");
	my $topologyFolder="topologyleadingzeroes";
	my @contents=<STAT>;
	close(STAT);
	if(grep(/3.0.11/,@contents)){
		$topologyFolder="topology";
	}
	
    foreach my $topology (@topologylist){
		chomp($topology);
		
		if($topology eq "force"){
			$params{'force_flag'}=1;
			print "force done\n";
		}
		
		#
		# Installing 2G Topology
		#
		if($topology eq "2G"){
			print "2G loading\n";
			$params{'topology_name'}="2G";
			$params{'table_name'}=["DIM_E_SGEH_GGSN","DIM_E_SGEH_SGSN"];
			$params{'column_name'}=["GGSN_NAME","SGSN_NAME"];
			$params{'column_value'}=["'GGSN5'","'SGSN1'"];
			$params{'cmp_operator'}=["=","="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/2G_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_SGEH";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_ascii";
			
			$result.=loadTopologyGeneric(\%params);
		}
		
		  # Installing 2G_LIKE4LIKE Topology
                #
                if($topology eq "2G_Like4Like"){
                        print "2G_Like4Like loading\n";
                        $params{'topology_name'}="2G_Like4Like";
                        $params{'table_name'}=["DIM_E_SGEH_GGSN","DIM_E_SGEH_SGSN"];
                        $params{'column_name'}=["GGSN_NAME","SGSN_NAME"];
                        $params{'column_value'}=["'GGSN5'","'SGSN1'"];
                        $params{'cmp_operator'}=["=","="];
                        $params{'threshold'}=1;
                        $params{'topology_file'}="/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/topology_for_SGEH/2G_Topology.zip";
                        $params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH";
                        $params{'topology_action'}="unzip";
                        $params{'interface_name'}="INTF_DIM_E_SGEH";
                        $params{'oss_name'}="eniq_events_topology";
                        $params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_ascii";

                        $result.=loadTopologyGeneric(\%params);
                }
		
		#
		# Installing 3G Topology
		#
		if($topology eq "3G"){
			$params{'topology_name'}="3G DIM_E";
			$params{'table_name'}=["DIM_E_SGEH_HIER321"];
			$params{'column_name'}=["HIERARCHY_3"];
			$params{'column_value'}=["\'%RNC02%\'"];
			$params{'cmp_operator'}=["like"];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/3G_Dim_E_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH_3G_Ericsson";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_SGEH_3G";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_3G_csexport_Ericsson";
			
			$result.=loadTopologyGeneric(\%params);
			
			$params{'topology_name'}="3G DIM_Z";
			$params{'table_name'}=["DIM_Z_SGEH_HIER321"];
			$params{'column_name'}=["HIERARCHY_3"];
			$params{'column_value'}=["\'%RNC03%\'"];
			$params{'cmp_operator'}=["like"];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/3G_Dim_Z_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH_3G_NonEricsson";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_SGEH_3G";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_3G_csexport_NonEricsson";
			
			$result.=loadTopologyGeneric(\%params);
		}

		
		
		#
                # Installing 3G_Like4Like Topology
                #
                if($topology eq "3G_Like4Like"){
                        $params{'topology_name'}="3G DIM_E";
                        $params{'table_name'}=["DIM_E_SGEH_HIER321"];
                        $params{'column_name'}=["HIERARCHY_3"];
                        $params{'column_value'}=["\'%RNC17%\'"];
                        $params{'cmp_operator'}=["like"];
                        $params{'threshold'}=1;
                        $params{'topology_file'}="/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/topology_for_SGEH/3G_Dim_E_Topology.zip";
                        $params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH_3G_Ericsson";
                        $params{'topology_action'}="unzip";
                        $params{'interface_name'}="INTF_DIM_E_SGEH_3G";
                        $params{'oss_name'}="eniq_events_topology";
                        $params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_3G_csexport_Ericsson";

                        $result.=loadTopologyGeneric(\%params);

                        $params{'topology_name'}="3G DIM_Z";
                        $params{'table_name'}=["DIM_Z_SGEH_HIER321"];
                        $params{'column_name'}=["HIERARCHY_3"];
                        $params{'column_value'}=["\'%RNC03%\'"];
                        $params{'cmp_operator'}=["like"];
                        $params{'threshold'}=1;
                        $params{'topology_file'}="/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/topology_for_SGEH/3G_Dim_Z_Topology.zip";
                        $params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH_3G_NonEricsson";
                        $params{'topology_action'}="unzip";
                        $params{'interface_name'}="INTF_DIM_E_SGEH_3G";
                        $params{'oss_name'}="eniq_events_topology";
                        $params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_3G_csexport_NonEricsson";

                        $result.=loadTopologyGeneric(\%params);
                }
		#
		# Installing 4G Topology
		#
		if($topology eq "4G"){
			$params{'topology_name'}="4G";
			$params{'table_name'}=["DIM_E_LTE_HIER321"];
			$params{'column_name'}=["hierarchy_1"];
			$params{'column_value'}=["\'LTE24ERBS00002-4\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/4G_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_LTE_ERBS";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_LTE_ERBS_csexport";
			
			$result.=loadTopologyGeneric(\%params);
		}
		
		  # Installing 4G_Like4Like Topology
                #
                if($topology eq "4G_Like4Like"){
                        $params{'topology_name'}="4G_Like4Like";
                        $params{'table_name'}=["DIM_E_LTE_HIER321"];
                        $params{'column_name'}=["hierarchy_1"];
                        $params{'column_value'}=["\'95522B\'"];
                        $params{'cmp_operator'}=["="];
                        $params{'threshold'}=1;
                        $params{'topology_file'}="/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/topology_for_SGEH/4G_Topology.zip";
                        $params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS";
                        $params{'topology_action'}="unzip";
                        $params{'interface_name'}="INTF_DIM_E_LTE_ERBS";
                        $params{'oss_name'}="eniq_events_topology";
                        $params{'adapter_name'}="Adapter_INTF_DIM_E_LTE_ERBS_csexport";

                        $result.=loadTopologyGeneric(\%params);
                }

		
		#
		# Installing LTE Topology
		#
		if($topology eq "LTE"){
			$params{'topology_name'}="LTE";
			$params{'table_name'}=["DIM_E_LTE_HIER321"];
			$params{'column_name'}=["hierarchy_1"];
			$params{'column_value'}=["\'LTE01ERBS00003-2\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/LTE_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_LTE_ERBS";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_LTE_ERBS_csexport";
			
			$result.=loadTopologyGeneric(\%params);
		}
		
		#
        # Installing LTE Topology for AtomDB
        #
        if($topology eq "ATOMDB"){
			my $server = getHostName();
			$params{'topology_name'}="ATOMDB";
		    $params{'table_name'}=["DIM_E_LTE_HIER321"];
		    $params{'column_name'}=["hierarchy_1"];
		    $params{'column_value'}=["\'LTE01ERBS00003-2\'"];
		    $params{'cmp_operator'}=["="];
		    $params{'threshold'}=42000;
		    $params{'topology_file'}="/net/atclvm560.athtem.eei.ericsson.se/edefiles/LTE_AtomDB_topology/LTE_AtomDB_topology.zip";
		    $params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/LTE_AtomDB_topology";
		    $params{'topology_action'}="unzip";
		    $params{'interface_name'}="INTF_DIM_E_LTE_ERBS";
		    $params{'oss_name'}="eniq_events_topology";
		    $params{'adapter_name'}="Adapter_INTF_DIM_E_LTE_ERBS_csexport";
		
		    $result.=loadTopologyGeneric(\%params);
		}
		
		# Installing LTE Topology for like4like
        #

		if($topology eq "LTE_CLIENT"){
			$params{'topology_name'}="LTE_CLIENT";
			$params{'table_name'}=["DIM_E_LTE_HIER321"];
			$params{'column_name'}=["hierarchy_1"];
			$params{'column_value'}=["\'13798B\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/topology_for_Like_For_like/LTE_Client_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_LTE_ERBS";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_LTE_ERBS_csexport";


			$result.=loadTopologyGeneric(\%params);
		}
		
		#
		# Installing WCDMA Topology
		#
		if($topology eq "WCDMA"){
			$params{'topology_name'}="WCDMA IMSI";
			$params{'table_name'}=["DIM_E_IMSI_IMEI"];
			$params{'column_name'}=["imsi"];
			$params{'column_value'}=["240010000000031"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/DataGenWorkFlows/DIM_E_IMSI_IMEI.csv";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/DIM_E_IMSI_IMEI";
			$params{'topology_action'}="copy";
			$params{'interface_name'}="INTF_DIM_E_IMSI_IMEI";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_IMSI_IMEI_ascii";
			
			$result.=loadTopologyGeneric(\%params);
			
			$params{'topology_name'}="WCDMA";
			$params{'table_name'}=["DIM_E_RAN_RNC"];
			$params{'column_name'}=["rnc_name"];
			$params{'column_value'}=["\'RNC06\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/WCDMA_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/events_oss_1/utran/topologyData/RNC";
			$params{'topology_action'}="unzip";	
			$params{'interface_name'}="INTF_DIM_E_SGEH_RNC";
			$params{'oss_name'}="events_oss_1";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_RNC_csexport";
			
			$result.=loadTopologyGeneric(\%params);
		}
		
		#
		# Installing MSS Topology
		#
		if($topology eq "MSS"){
			#sqlMSSWorkaround();
			$params{'topology_name'}="MSS";
			$params{'table_name'}=["DIM_E_MSS_EVNTSRC"];
			$params{'column_name'}=["EVENT_SOURCE_NAME"];
			$params{'column_value'}=["\'MSS_3\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/MSS_Topology.zip";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/core/topologyData/AXE";
			$params{'topology_action'}="unzip";
			$params{'interface_name'}="INTF_DIM_E_MSS_AXE";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_MSS_AXE_ct";
			
			$result.=loadTopologyGeneric(\%params);
		}
		
		#
		# Installing 3g Session Browser Topology
		#
		if($topology eq "3GSessionBrowser"){
			#1. Load RNC09 Topology
			$params{'topology_name'}="3GSessionBrowser RNC09";
			$params{'table_name'}=["DIM_E_RAN_RNC"];
			$params{'column_name'}=["rnc_name"];
			$params{'column_value'}=["\'RNC09\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/topology/3GSessionBrowserTopology/SubNetwork_RNC09_MeContext_RNC09.xml";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/events_oss_1/utran/topologyData/RNC";
			$params{'topology_action'}="copy";
			$params{'interface_name'}="INTF_DIM_E_SGEH_RNC";
			$params{'oss_name'}="events_oss_1";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_RNC_csexport";
			$result.=loadTopologyGeneric(\%params);
			
			#2. Load SGSN09 Topology
			$params{'topology_name'}="3GSessionBrowser SGSN09";
			$params{'table_name'}=["DIM_E_SGEH_SGSN"];
			$params{'column_name'}=["SGSN_NAME"];
			$params{'column_value'}=["'SGSN_SmarTone'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/topology/3GSessionBrowserTopology/DIM_E_SGEH_SGSN.Topology.23022012";
			$params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH";
			$params{'topology_action'}="copy";
			$params{'interface_name'}="INTF_DIM_E_SGEH";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_ascii";
			$result.=loadTopologyGeneric(\%params);
			
			#3. Load Home Network
			$result.=load3gSessionBrowserHomeNetwork();
		}
		
		
		#
		# Installing new WCDMA topology (CEP Processing)
		#
		if($topology eq "WCDMA_NEW"){
			#1. Load RNC09 Topology
			$params{'topology_name'}="3GSessionBrowser RNC09";
			$params{'table_name'}=["DIM_E_RAN_RNC"];
			$params{'column_name'}=["rnc_name"];
			$params{'column_value'}=["\'RNC09\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/topology/3GSessionBrowserTopology/SubNetwork_RNC09_MeContext_RNC09.xml";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/events_oss_1/utran/topologyData/RNC";
			$params{'topology_action'}="copy";	
			$params{'interface_name'}="INTF_DIM_E_SGEH_RNC";
			$params{'oss_name'}="events_oss_1";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_RNC_csexport";
			$result.=loadTopologyGeneric(\%params);
			
			#2. Load SGSN09 Topology
			$params{'topology_name'}="3GSessionBrowser SGSN09";
			$params{'table_name'}=["DIM_E_SGEH_SGSN"];
			$params{'column_name'}=["SGSN_NAME"];
			$params{'column_value'}=["'SGSN_SmarTone'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/topology/3GSessionBrowserTopology/DIM_E_SGEH_SGSN.Topology.23022012";
			$params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH";
			$params{'topology_action'}="copy";
			$params{'interface_name'}="INTF_DIM_E_SGEH";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_ascii";
			$result.=loadTopologyGeneric(\%params);
			
			# Load DIM_E_SGEH_HIER321
			
			$params{'topology_name'}="2G";
			$params{'table_name'}=["DIM_E_SGEH_HIER321"];
			$params{'column_name'}=["HIERARCHY_3"];
			$params{'column_value'}=["\'%2G_SUNTRPRMAI_0%\'"];
			$params{'cmp_operator'}=["like"];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/$topologyFolder/DIM_E_SGEH_HIER321.Topology.13022013";
			$params{'topology_dest_folder'}="/eniq/data/pmdata/eniq_events_topology/DIM_E_SGEH";
			$params{'topology_action'}="copy";
			$params{'interface_name'}="INTF_DIM_E_SGEH";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_ascii";
			$result.=loadTopologyGeneric(\%params);
			
			# Load IMEI IMSI mapping
			$params{'topology_name'}="WCDMA IMSI";
			$params{'table_name'}=["DIM_E_IMSI_IMEI"];
			$params{'column_name'}=["imsi"];
			$params{'column_value'}=["454063306043718"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/eniq/home/dcuser/automation/DataGenWorkFlows/DIM_E_IMSI_IMEI.csv";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/eniq_events_topology/DIM_E_IMSI_IMEI";
			$params{'topology_action'}="copy";
			$params{'interface_name'}="INTF_DIM_E_IMSI_IMEI";
			$params{'oss_name'}="eniq_events_topology";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_IMSI_IMEI_ascii";
			$result.=loadTopologyGeneric(\%params);
			
			#3. Load Home Network
			$result.=load3gSessionBrowserHomeNetwork();
		}
		if($topology eq "WCDMA_GRIT"){
			#1. Load RNC09 Topology
			$params{'topology_name'}="3GSessionBrowser RNC09";
			$params{'table_name'}=["DIM_E_RAN_RNC"];
			$params{'column_name'}=["rnc_name"];
			$params{'column_value'}=["\'RNC09\'"];
			$params{'cmp_operator'}=["="];
			$params{'threshold'}=1;
			$params{'topology_file'}="/net/atclvm559.athtem.eei.ericsson.se/package/GRIT/wcdma_cfahfa/topology/RNC09.xml";
			$params{'topology_dest_folder'}="/eniq/data/eventdata/events_oss_1/utran/topologyData/RNC";
			$params{'topology_action'}="copy";	
			$params{'interface_name'}="INTF_DIM_E_SGEH_RNC";
			$params{'oss_name'}="events_oss_1";
			$params{'adapter_name'}="Adapter_INTF_DIM_E_SGEH_RNC_csexport";
			$result.=loadTopologyGeneric(\%params);			
		}
	}

    $result.="</TABLE>";
	unlink "$topology_present";
    return $result;
}

sub updateTopology_Lteefa{
	#truncate table first
	#Implement loading script (invoke bash )
	#delete the rows which oss_id=null
	#*give a final check
	
	
	&FT_LOG("LTE EFA UPDATE TOPOLOGY!");
	my $DIM_E_LTE_HIER321_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'DIM_E_LTE_HIER321%'";
	my @lte_truncate_queries = sqlSelect($DIM_E_LTE_HIER321_tables_query);
	
	#run truncate queries
	foreach my $query (@lte_truncate_queries) {
		&FT_LOG("Truncate Query: $query\n");
		sqlDelete($query);
	}
	executeThisWithLogging("chmod 777 /eniq/home/dcuser/automation/lteefa/load_test.bsh");
	executeThisWithLogging("bash /eniq/home/dcuser/automation/lteefa/load_test.bsh");
	
	my $DIM_E_LTE_HIER321_oss_id_tables_query = "select 'delete from '||Table_name||' where oss_id = null;' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'DIM_E_LTE_HIER321%'";
	my @lte_truncate_queries = sqlSelect($DIM_E_LTE_HIER321_oss_id_tables_query );
	
	#run truncate queries
	foreach my $query (@lte_truncate_queries) {
		&FT_LOG("Truncate Query: $query\n");
		sqlDelete($query);
	}
	

}
sub loadTopologyGeneric{
	my $params=shift;
	my $topology_name=$params->{'topology_name'};
	my $table_name=$params->{'table_name'};
	my $column_name=$params->{'column_name'};
	my $column_value=$params->{'column_value'};
	my $cmp_operator=$params->{'cmp_operator'};
	my $threshold=$params->{'threshold'};
	my $force=$params->{'force_flag'};
	my $topology_file=$params->{'topology_file'};
	my $topology_dest_folder=$params->{'topology_dest_folder'};
	my $topology_action=$params->{'topology_action'};
	my $interface_name=$params->{'interface_name'};
	my $oss_name=$params->{'oss_name'};
	my $adapter_name=$params->{'adapter_name'};
	
	my @topology_tables=();
	my $load_topology = $force;
	my $allOk=0;
	my $status=0;
	my $result="";
	
	#ENC-2308
	executeThisWithLogging("./RunCommandAsRoot.sh mkdir -p  $topology_dest_folder");
    executeThisWithLogging("./RunCommandAsRoot.sh chmod -R 777 /eniq/data/eventdata/events_oss_1/utran");
    executeThisWithLogging("./RunCommandAsRoot.sh chmod -R 777 /eniq/data/eventdata/eniq_events_topology");
    executeThisWithLogging("./RunCommandAsRoot.sh chmod -R 777 /eniq/data/pmdata/eniq_events_topology");
	
	if(!$force){
		for(my $i = 0; $i < (@$table_name); $i++){
			my @res=sqlSelect("select count(*) from $table_name->[$i] where $column_name->[$i] $cmp_operator->[$i] ".$column_value->[$i]."");
			push (@topology_tables, $res[0]);
		}
		
		foreach my $count (@topology_tables){
			if($count < $threshold){
				$load_topology = 1;
				last;
			}
		}
	}
	
	if($load_topology){
		my $interfaceActivated = ensureInterfaceIsActivated($oss_name, $interface_name);
		if($interfaceActivated==2){
			$result.=logAndGenerateTableEntry("Interface $interface_name-$oss_name $adapter_name wasn't active but this script just activated it",1);
		}elsif($interfaceActivated==1){
			$result.=logAndGenerateTableEntry("Interface $interface_name-$oss_name $adapter_name was already active.",1);
		}else{
			$result.=logAndGenerateTableEntry("Interface $interface_name-$oss_name $adapter_name could not be activated.",0);
		}
		
		if($interfaceActivated){

			if($topology_name eq 'ATOMDB'){

				if(! -d $topology_dest_folder){
					&FT_LOG("INFO:Creating a Folder to unzip topology to: $topology_dest_folder ");
					executeThisWithLogging("mkdir -p $topology_dest_folder; chmod 755 $topology_dest_folder");
				}	

				&FT_LOG("INFO:Unzipping topology from $topology_file to $topology_dest_folder");
				$status=runCommand("unzip -o $topology_file -d $topology_dest_folder");
                $result.=logAndGenerateTableEntry("Unzip $topology_file",$status);

				if(-e "/eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS")
				{
					executeThisWithLogging("rm -rf /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS 2>/dev/null");
                	&FT_LOG("INFO:Creating softlink from /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS to $topology_dest_folder");
					my $success=runCommand("ln -s $topology_dest_folder /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS");
                    if($success==1){
                        &FT_LOG("INFO:Symbolic link created to atclvm560");
                       }else{
                        &FT_LOG("ERROR:Failed to create symbolic link created to atclvm560");
                    }
				}
			}
			else{
			
				if($topology_action eq "unzip"){
					$status=runCommand("unzip -o $topology_file -d $topology_dest_folder");
					$result.=logAndGenerateTableEntry("Unzip $topology_file",$status);
				}elsif($topology_action eq "copy"){
					$status=copy($topology_file, $topology_dest_folder);
					$result.=logAndGenerateTableEntry("Copied $topology_file",$status);
				}
			}
			
			$status=runCommand("/eniq/sw/bin/engine -e startSet $interface_name-$oss_name $adapter_name");
			$result.=logAndGenerateTableEntry("engine -e startSet $interface_name-$oss_name $adapter_name",$status);
			
			if($topology_name eq 'ATOMDB'){
				my $count=0;	
				while(1){		
					$count++;
					sleep(10);
					&FT_LOG("INFO: Waiting For Topology To Load");
                              		my @res=sqlSelect("select count(*) from DIM_E_LTE_HIER321");
                              		&FT_LOG("Number of rows in DIM_E_LTE_HIER_321 = $res[0], wait until greater than $threshold");
					last if (($res[0] > $threshold)||($count == 100));	

				}	
				if($count==100){
					$allOk=0;
					&FT_LOG("Number of rows in DIM_E_LTE_HIER_321 is less than expected, please manually check DB, meanwhile the tests will continue as the topology that is loaded may be enough for the tests to run");
				}else{
					$allOk=1;		
				}
			}else{
				for(my $i=1;$i<50;$i++){
					$allOk=1;
					@topology_tables=();
					for(my $j = 0; $j < (@$table_name); $j++){
						my @res=sqlSelect("select count(*) from $table_name->[$j] where $column_name->[$j] $cmp_operator->[$j] ".$column_value->[$j]."");
						push(@topology_tables,$res[0]);
						if($res[0] < $threshold){
							$allOk=0;
						}
					}
					if($allOk){
						last;
					}else{
						sleep(20);
					}
				}
			}
		}

		if($allOk){
			$result.=logAndGenerateTableEntry("Load $topology_name Topology Files Complete",1);

			if($topology_name eq 'ATOMDB'){
				&FT_LOG("INFO: Topology is finished loading to the DIM_E_LTE_HIER321 table\n");
				&FT_LOG("INFO: Remove the softlink to atclvm560 and re-create /eniq/data/eventdata/eniq_events_topology/lte/topologyData");
				executeThisWithLogging("cd /eniq/data/eventdata/eniq_events_topology/lte/topologyData; rm ERBS; mkdir ERBS; chmod 777 ERBS");
			}

		}else{
			my $tables="";
			for(my $i = 0; $i < @topology_tables; $i++){
				if($topology_tables[$i] < $threshold){
					$tables.="$table_name->[$i], ";
				}
			}
			$result.=logAndGenerateTableEntry("Load $topology_name Topology Files failed. $tables not populated",0);
		}
	}else{
		$result.=logAndGenerateTableEntry("Load $topology_name Topology Files",1);
		&FT_LOG("INFO:Skipping $topology_name topology load because it's been done. To force it to load put LOADTOPOLOGY force,$topology_name in a config file");
	}
	
	return $result;
}

#
#	checks if interface is activated before loading topology.
#	If not activated, it will attempt to activate the interface.
#
sub ensureInterfaceIsActivated{
	my $oss = shift; #should normally be events_oss_1 or eniq_events_topology
	my $interface = shift;

	my @output= executeThisWithLogging("cd /eniq/sw/installer; ./activate_interface -o $oss -i $interface");
	if(grep(/is already activated/,@output)){
		return 1;
	}else{
		@output= executeThisWithLogging("cd /eniq/sw/installer; ./activate_interface -o $oss -i $interface");
		if(grep(/is already activated/,@output)){
			&FT_LOG("INFO:Interface wasn't active. Activating again to ensure that it was just activated successfully");
			return 2;
		}
		return 0;
	}
}

sub load3gSessionBrowserHomeNetwork{
	# gpmgt -i -add -f home_network.xml
	my @output = executeThisWithLogging("gpmgt -i -add -f /eniq/home/dcuser/automation/topology/3GSessionBrowserTopology/home_network.xml");
	my @res=sqlSelect("select * from dc.GROUP_TYPE_E_MCC_MNC where mcc = '454' and mnc = '06'");
	my $result = "";
	if(@res){
		$result.=logAndGenerateTableEntry("Load 3gSessionBrowserHomeNetwork Topology Complete.",1);
	}else{
		$result.=logAndGenerateTableEntry("Load 3gSessionBrowserHomeNetwork Topology Files failed. GROUP_TYPE_E_MCC_MNC not populated",0);
	}
	return $result;
}

#
# Verify Topology 
# queries the database for the topology tables and counts the rows
# if the number of rows is 0, it fails the test case
#
sub verifyTopology{
	my $result="";
	my @alltopologytables=getAllTables4TP("DIM_E_");
	$result.=qq{
	<br><TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>TOPOLOGY TABLE</th>
	 <th>COUNT</th>
	 <th>RESULT</th>
	</tr>
	};
	
	foreach my $tables (@alltopologytables){
		my @data=getTopologyLoading($tables);
		foreach my $data (@data){
			$_=$data;
			next if(/affected/);
			next if(/Msg 102, Level 15, State 0:/);
			next if(/^$/);
			$data=~ s/\|0/|<b>0<\/b>/;
			$data=~ s/^/<tr><td>/g;
			$data=~ s/ //g;
			$data=~ s/\|/<\/td><td align=center>/g;
			$_=$data;
			if(/<b>0<.b>/){
				$data=~ s/$/<\/td><td align=center><font color=660000><b>FAIL<\/b><\/font><\/td><\/tr>/;
			}else{
				$data=~ s/$/<\/td><td align=center><font color=darkblue><b>PASS<\/b><\/font><\/td><\/tr>/;
			}
			$result.="$data\n";
		}
	}
	$result.="</TABLE>\n";
	return $result;
}

#
# Verify Groups
#
sub verifyGroups{
	my $result="";
	$result.=qq{
	<br><TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>GROUP TABLE</th>
	 <th>COUNT</th>
	 <th>RESULT</th>
	</tr>
	};
	
	my @grouptables = ("dc.GROUP_TYPE_E_APN", "dc.GROUP_TYPE_E_EVNTSRC", "dc.GROUP_TYPE_E_IMSI", "dc.GROUP_TYPE_E_MCC_MNC", "dc.GROUP_TYPE_E_RAT_VEND_HIER3", "dc.GROUP_TYPE_E_RAT_VEND_HIER321", "dc.GROUP_TYPE_E_TAC");
	
	foreach my $tables (@grouptables){
		my @data=getTopologyLoading($tables);
		foreach my $data (@data){
			$_=$data;
			next if(/affected/);
			next if(/Msg 102, Level 15, State 0:/);
			next if(/^$/);
			$data=~ s/\|0/|<b>0<\/b>/;
			$data=~ s/^/<tr><td>/g;
			$data=~ s/ //g;
			$data=~ s/\|/<\/td><td align=center>/g;
			$_=$data;
			my $val = $data;
			$val =~ s/.*center>//g;
			if($val =~ /750/){
				$data=~ s/$/<\/td><td align=center><font color=darkblue><b>PASS<\/b><\/font><\/td><\/tr>/;
			}else{
				$data=~ s/$/<\/td><td align=center><font color=660000><b>FAIL<\/b><\/font><\/td><\/tr>/;
			}
			$result.="$data\n";
		}
	}
	$result.="</TABLE>\n";
	return $result;
}

#
# GETS ALL THE TABLES FOR CERTAIN TECHPACK FROM DWHDB
# This is a utility
# needs one parameter, the techpacks name
# it gets all the tables for that techpack
#
sub getAllTables4TP{
	my $tp = shift;
	$tp=~ s/ //g;

	my $sql=qq{select A.Table_name from SYSTABLE  A where A.table_type like 'VIEW' and A.Table_Name LIKE ('$tp%')
			and A.creator=103;
			go
			EOF
		};

	my @result=undef;
	open(ALLTP,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @allTechPacksTables=<ALLTP>;
	chomp(@allTechPacksTables);
	close(ALLTP);
	foreach my $t (@allTechPacksTables){
		$_=$t;
		next if(/affected/);
		next if(/^$/);
		$t=~ s/\t//g;
		$t=~ s/\s//g;
		$t=~ s/ //g;
		push @result,$t;
	}
	
	return @result;
}

#
# GETS ALL THE TABLES FOR CERTAIN TECHPACK FROM DWHDB
# This is a utility subroutine 
# queries the db for a certain table and counts the rows
# the input param is the table name
#
sub getTopologyLoading{
	my $table = shift;
	my $date  = $DATETIMEWARP;###getDateTimewarp();
	my $sql=qq{select '$table'||'|'|| COUNT(*) as COUNT from $table;
			go
			EOF
		};
	my @result=undef;
	open(DATA,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @data=<DATA>;
	chomp(@data);
	push @result,$data[0];
	return @result;
}

#------------------------------------------------------------	
#	SUBROUTINES - Data Generation via Simulator
#
#
#	6.	dataGenStart_SGEH_DVDT()
#	7.	dataGenStart_MSS()
#
#	8.      dataGenStop_2G3G4G()
#	9.      dataGenStop_SGEH_DVDT()
#	10.      dataGenStop_MSS()
#
#------------------------------------------------------------

sub runCommand{
	my ($command,$printOutput)=@_;
	my $success=1;
	my $suffix=" 2>&1 |";	#send stderr to stdout stream
	if($printOutput==1){
		&FT_LOG("INFO:Running command: $command $suffix\n");
	}
	open(COM,"$command $suffix");
	my @com=<COM>;
	close(COM);
	my $returnCode=$?;
	if ($returnCode ne 0){
		$success=0;
		if($printOutput==1){
			&FT_LOG("ERROR:Return code: $returnCode\n");
		}
	}
	
	foreach (@com){
		if(/Exception|Fail|Error/i){
			if($success==1 && $printOutput==1){
				&FT_LOG("ERROR:Found exception, fail or error in the command output\n");
			}
			$success=0;
		}
	}
	
	if($success==0 && $printOutput==1){
		&FT_LOG("INFO:Command output:\n");
		foreach (@com){
			&FT_LOG("$_");
		}
	}	
	return $success;
}

sub trim($){
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}

sub logAndGenerateTableEntry{
	my ($message,$status)=@_;
	if($status==0){
		&FT_LOG("ERROR:$message - ERROR\n");
		return "<tr><td>$message</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
	}else{
		&FT_LOG("INFO:$message - SUCCESS\n");
		return "<tr><td>$message</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}
}

sub copyBatchFiles{
	my $force=shift;
	my $result="";
	my @ecsWithoutFiles=();
	
	foreach my $ec(@ecHosts){
		my $inputFilesExist=runCommand("ssh dcuser\@$ec 'ls /tmp/bin/input/batch5000'",0);
		if($inputFilesExist!=1 || $force==1){
			push(@ecsWithoutFiles,"$ec");
		}
	}
	
	foreach my $ec(@ecsWithoutFiles){
		my $status=runCommand("ssh dcuser\@$ec 'mkdir -p /tmp/bin/input '",1);
		$result.=logAndGenerateTableEntry("mkdir /tmp/bin/input on $ec",$status);
		
		if(! -d "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G"){
			$status=runCommand("unzip /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G.zip -d /eniq/home/dcuser/automation/DataGenTopology",1);
			$result.=logAndGenerateTableEntry("Unzip DatagenTopology4G.zip",$status);
		}
				
		$status=runCommand("scp /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/input.zip dcuser\@$ec:/tmp/bin",1);
		$result.=logAndGenerateTableEntry("Copy input.zip to /tmp/bin on $ec",$status);
		
		$status=runCommand("ssh dcuser\@$ec 'unzip -o /tmp/bin/input.zip -d /tmp/bin/input'",1);
		$result.=logAndGenerateTableEntry("Unzip input.zip on $ec(trigger batch files)",$status);
		$status=runCommand("ssh dcuser\@$ec 'mkdir -p /tmp/OMS_LOGS/ebs/ready'",1);
		$result.=logAndGenerateTableEntry("mkdir /tmp/OMS_LOGS/ebs/ready on $ec",$status);
	}
	
	my $arrSize=@ecsWithoutFiles;
	if($arrSize==0 || $force==0){
		&FT_LOG("INFO:Skipping copying input batch files because it's been done.\n");
	}
	
	return $result;
}

sub installLCHS{
	my $force=shift;
	my $result="";
	opendir (DIR, "/eniq/home/dcuser/automation/DataGenWorkFlows");
	my @installerFile = grep /installer_.*.zip/, readdir(DIR);
	closedir DIR;
	
	if (!@installerFile) {
		$result.=logAndGenerateTableEntry("Look for /eniq/home/dcuser/automation/DataGenWorkFlows/installer_.*.zip",0);
	}else{
		$result.=logAndGenerateTableEntry("Look for /eniq/home/dcuser/automation/DataGenWorkFlows/installer_.*.zip",1);
		mkdir("/eniq/sw/installer/temp");
		open(PROP,"</eniq/sw/installer/versiondb.properties");
		my @installedVersion=grep /installer/,<PROP>;
		close(PROP);
		$installedVersion[0]=~s/module.installer=//g;
		$installedVersion[0]=~s/\n//g;
		$installedVersion[0]=~s/_.*//g;
		
		my $instFile=$installerFile[0];
		$instFile=~s/installer_//g;
		$instFile=~s/\.zip//g;
		if( $installedVersion[0] eq $instFile){
			$result.=logAndGenerateTableEntry("Skipping installing $installerFile[0] because it's already installed",1);
		}elsif($installedVersion[0] gt $instFile){
			$result.=logAndGenerateTableEntry("Skipping installing $installerFile[0] because installed version $installedVersion[0] is newer",1);
		}else{
			my $status=runCommand("unzip -o /eniq/home/dcuser/automation/DataGenWorkFlows/$installerFile[0] -d /eniq/sw/installer/temp",1);
			$result.=logAndGenerateTableEntry("Unzip $installerFile[0]",$status);
			chmod 0777,"/eniq/sw/installer/temp/install_installer.sh";
			$status=runCommand("cd /eniq/sw/installer/temp;./install_installer.sh",1);
			$result.=logAndGenerateTableEntry("Run /eniq/sw/installer/temp/install_installer.sh",$status);
		}
	}
	
	opendir (DIR, "/eniq/home/dcuser/automation/DataGenWorkFlows");
	@installerFile = grep /M_E_LCHS_DATAGEN.*.tpi/, readdir(DIR);
	closedir DIR;
	if(!@installerFile){
		$result.=logAndGenerateTableEntry("Look for /eniq/home/dcuser/automation/DataGenWorkFlows/M_E_LCHS_DATAGEN.*.tpi",0);
	}elsif(! -e "/eniq/mediation_inter/M_E_LCHS_DATAGEN/etc/conf_lte_cfa_hfa.prop" || $force==1){
		$result.=logAndGenerateTableEntry("Look for /eniq/home/dcuser/automation/DataGenWorkFlows/M_E_LCHS_DATAGEN.*.tpi",1);
		opendir (DIR, "/eniq/sw/installer");
		my @existingFiles = grep /M_E_LCHS_DATAGEN/, readdir(DIR);
		closedir DIR;
		foreach(@existingFiles){
			unlink("/eniq/sw/installer/$_");
		}
		copy("/eniq/home/dcuser/automation/DataGenWorkFlows/$installerFile[0]","/eniq/sw/installer");
		sqlRepDbDelete("delete from dwhrep.mztechpacks where techpack_name ='M_E_LCHS_DATAGEN'");
		#Can't use runCommand because tp_installer doesn't return 0 for success as it should
		my @res=executeThisWithLogging("cd /eniq/sw/installer;./tp_installer -p /eniq/sw/installer/ -t M_E_LCHS_DATAGEN -n");
		if(grep(/Exception|Fail|Error/,@res)){
			$result.=logAndGenerateTableEntry("Install M_E_LCHS_DATAGEN",0);
		}else{
			$result.=logAndGenerateTableEntry("Install M_E_LCHS_DATAGEN",1);
		}
		my $status=runCommand("/eniq/mediation_inter/M_E_LCHS_DATAGEN/bin/test_dirs.sh",1);
		$result.=logAndGenerateTableEntry("test_dirs.sh",$status);
		open(PROP,"</eniq/mediation_inter/M_E_LCHS_DATAGEN/etc/conf_lte_cfa_hfa.prop");
		my @properties=<PROP>;
		close(PROP);
		foreach (@properties){
			s/RSRV_ENB=0/RSRV_ENB=1/;
			s/NO_OF_EVENTS=.*$/NO_OF_EVENTS=130/;
			s/No_Of_Nodes=.*$/No_Of_Nodes=3/;
			s/No_Of_Resv_Subscribers=.*$/No_Of_Resv_Subscribers=3/;
		}
		open(PROP,">/eniq/mediation_inter/M_E_LCHS_DATAGEN/etc/conf_lte_cfa_hfa.prop");
		foreach(@properties){
			print PROP;
		}
		close(PROP);
	}else{
		$result.=logAndGenerateTableEntry("Skipping installing M_E_LCHS_DATAGEN because it's already installed ",1);
	}
	return $result;
}

sub create4GAndMSSGroups{
	my $force=shift;
	my $result="";
	if(!-e "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/.loadingdone" || $force==1){
		my $status=runCommand("unzip -o /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G.zip -d /eniq/home/dcuser/automation/DataGenTopology",1);
		$result.=logAndGenerateTableEntry("Unzip DataGenTopology4G.zip",$status);
		
		$status=runCommand("unzip -o /eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS.zip -d /eniq/home/dcuser/automation/DataGenTopology",1);
		$result.=logAndGenerateTableEntry("Unzip DataGenTopologyMSS.zip",$status);
		$status=runCommand("$ISQL -Udc -$dBPassword -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/predropDG.sql",0);
		$status=runCommand("$ISQL -Udc -$dBPassword -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/createDG2.sql",1);
		$result.=logAndGenerateTableEntry("Create 2G 3G SGEH & CDR Data Generation Tables",$status);
		$status=runCommand("$ISQL -Udc -$dBPassword -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDG.sql",1);
		$result.=logAndGenerateTableEntry("Load 2g3g4g Data Generation Topology",$status);
		$status=runCommand("$ISQL -Udc -$dBPassword -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/updateDG.sql",1);
		$result.=logAndGenerateTableEntry("Update 2g3g4g DataGen Tables",$status);
		$status=runCommand("$ISQL -Udc -$dBPassword -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/deleteDGGroup.sql",1);
		$result.=logAndGenerateTableEntry("Delete 2g3g4g Data Generation Groups",$status);
		$status=runCommand("$ISQL -Udc -$dBPassword -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/deleteDGGroup.sql",1);
		$result.=logAndGenerateTableEntry("Delete MSS Data Generation Groups",$status);
		
		chmod 0755,"/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDGGroups.sh";
		$status=runCommand("/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDGGroups.sh",1);
		$result.=logAndGenerateTableEntry("Load 2g3g4g DataGen Groups",$status);
		chmod 0755,"/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/loadDGGroups.sh";
		$status=runCommand("/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/loadDGGroups.sh",1);
		$result.=logAndGenerateTableEntry("Load MSS DataGen Groups",$status);
		open(PROP,">/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/.loadingdone");
		print PROP "done";
		close(PROP);
	}elsif($force!=1){
		&FT_LOG("INFO:Skipping creating 4G and MSS groups because it's been done.\n");
	}
	return $result;
}

#
# Start DataGen 2G3G4G Workflows
#

#################################
# Start DataGen 2G3G4G Workflows#
#################################

sub findAndDeleteFiles{
	my ($dir,$fiMatch,$diMatch,$diExclude)=@_;
	@gFileMatch=@$fiMatch;
	@gDirMatch=@$diMatch;
	@gDirExclude=@$diExclude;
	foreach my $d(@$dir){
		find(\&deleteMatchingFiles, $d);
	}
}

sub deleteMatchingFiles{
	my $file="$_";
	my $directory=$File::Find::dir;
	my $isDirMatch=0;
	my $isFileMatch=0;
	
	foreach my $dMatch(@gDirMatch){
		if($directory=~m/$dMatch/){
			$isDirMatch=1;
		}
	}
	
	if(!@gDirMatch){
		$isDirMatch=1;
	}
	
	if($isDirMatch){
		foreach my $dExclude(@gDirExclude){
			if($directory=~m/$dExclude/){
				$isDirMatch=0;
			}
		}
	}
	
	if($isDirMatch){
		foreach my $fMatch(@gFileMatch){
			if($file=~m/$fMatch/){
				$isFileMatch=1;
			}
		}
	}
	
	if($isDirMatch && $isFileMatch){
		&FT_LOG("INFO:Deleting $File::Find::name");
		unlink($File::Find::name);
	}
}

sub changeConfSetting{
	my $confFile="/eniq/home/dcuser/automation/DataGenWorkFlows/conf_4G.prop";
	my $varName=shift;
	my $settingTF=shift;
	my $madeChange=0;

	if($settingTF ne "true" && $settingTF ne "false"){
		if($settingTF==1){
			$settingTF="true";
		}elsif($settingTF==0){
			$settingTF="false";
		}
	}
	
	open(PROP,"<$confFile");
	my @contents=<PROP>;
	close(PROP);
	my @arr=grep /$varName=/,@contents;
	
	if(!grep(/$varName=$settingTF/,@arr)){
		$madeChange=1;
		if(@arr){
			foreach(@contents){
				s/$varName=.*$/$varName=$settingTF/;
			}
		}else{
			push(@contents,"\n$varName=$settingTF\n");
		}
	}
	
	if($madeChange){
		open(PROP,">$confFile.2");
		print PROP @contents;
		close(PROP);
		return 1;
	}else{
		return 0;
	}
}

sub copy2g3g4gWcdmaConfFiles{
	foreach my $ec(@ecHosts){
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /tmp/OMS_LOGS/ebs/ready'");
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /tmp/bin/input' ");
		executeThisWithLogging("mkdir -p /tmp/bin/input");
		executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_WCDMA.prop /tmp/bin/input");
		executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_4G.prop /tmp/bin/input/");
		executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn.prop /tmp/bin/input/");
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /tmp/bin/input' ");
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir1' ");
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir2' ");
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir3' ");
		executeThisWithLogging("ssh dcuser\@$ec 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir4' ");
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/input.zip dcuser\@$ec:/tmp/bin");
		executeThisWithLogging("ssh dcuser\@$ec 'ls /tmp/bin/input/batch5000 >/dev/null 2>&1|| unzip -o /tmp/bin/input.zip -d /tmp/bin/input' ");
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_WCDMA.prop dcuser\@$ec:/tmp/bin/input");
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_4G.prop dcuser\@$ec:/tmp/bin/input/");
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn.prop dcuser\@$ec:/tmp/bin/input/");
		&FT_LOG("INFO:On $ec - Created /tmp/bin/input");
	}
}

sub preDelete2g3g4gFiles{
	######
	#Delete pre-existing temp files to prevent PreProcessing workflow aborting
	my @dir;
	my @fileMatch;
	my @subDirMatch;
	my @subDirExclude;
	push(@dir,"/eniq/data/pmdata/eventdata");
	push(@dir,"/eniq/data/etldata_");
	push(@dir,"/tmp/OMS_LOGS/ebs/ready");
	push(@dir,"/eniq/data/eventdata");
	push(@fileMatch,"^MZ");
	push(@fileMatch,"^A");
	push(@subDirMatch,"SGEH");
	push(@subDirMatch,"ready");
	push(@subDirMatch,"events_oss_1/sgeh");
	push(@subDirMatch,"event_e_sgeh");
	push(@subDirExclude,"archive");
	&findAndDeleteFiles(\@dir,\@fileMatch,\@subDirMatch,\@subDirExclude);
}

sub files_match{

    my ( $fileA, $fileB ) = @_;
    open my $file1, '<', $fileA;
    open my $file2, '<', $fileB;

    while (my $lineA = <$file1>) {

        next if $lineA eq <$file2>;
        return 0 and last;
    }

    return 1;
}

#Stops and disables all workflows (we use anyways). 
sub disableAllWorkflows{
	my @exclude = @_;#will not disable these
	my @stopped;
	my $ex="";
	foreach my $wf(@exclude){
		$ex.="$wf,";
	}
	if($ex ne ""){
		&FT_LOG(qq{********************************************************************************
**************************                           ***************************
**************************      ROLLING DISABLE      ***************************
**************************                           ***************************
********************************************************************************
INFO: Disabling workflows not related to $ex in order to reduce load
});
	}
	&FT_LOG("INFO: Disabling all workflows.\n");
	my @allWorkflows = executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist");
	if( @exclude ){
		my $toExclude = join("|", @exclude);
		@allWorkflows = grep(!/$toExclude/i, @allWorkflows);
	}
	my @workflowsToStop = grep(/EBSL|GPEH|LTEEFA|MSS|SGEH|Sim|eankmuk_4G/, @allWorkflows);
	my @workFlowsToStopTrimmed = map { (split(/ /, $_))[0] } @workflowsToStop;#removes the bracket and number
	if(@workFlowsToStopTrimmed){
		&FT_LOG("INFO: Stopping and disabling ".@workFlowsToStopTrimmed." workflows\n");
		@stopped = quicklyDisableWorkflows(@workFlowsToStopTrimmed);
		print "INFO: Done.\n";
	}
	my @stoppedWorkflows;
	@stoppedWorkflows = @stopped;
	return @stoppedWorkflows;
}

sub disableMatchingWorkflows{
	my @match = @_;#will disable these
	my $wfs="";
	foreach my $wf(@match){
		$wfs.="$wf,";
	}
	if($wfs ne ""){
		&FT_LOG(qq{********************************************************************************
**************************                           ***************************
**************************      ROLLING DISABLE      ***************************
**************************                           ***************************
********************************************************************************
INFO: Disabling workflows related to $wfs in order to reduce load
});
	}
	&FT_LOG("INFO: Disabling all workflows.\n");
	my @allWorkflows = executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist");
	if( @match ){
		my $toMatch = join("|", @match);
		@allWorkflows = grep(/$toMatch/i, @allWorkflows);
	}
	my @workflowsToStop = grep(/EBSL|GPEH|LTEEFA|MSS|SGEH|Sim|eankmuk_4G/, @allWorkflows);
	my @workFlowsToStopTrimmed = map { (split(/ /, $_))[0] } @workflowsToStop;#removes the bracket and number
	if(@workFlowsToStopTrimmed){
		&FT_LOG("INFO: Stopping and disabling ".@workFlowsToStopTrimmed." workflows\n");
		quicklyDisableWorkflows(@workFlowsToStopTrimmed);
		print "INFO: Done.\n";
	}
}

sub setup2g3gWorkflows{
	my $hostname="127.0.0.1";
	my $datagenToStart = shift;
	my $username = "dcuser";
	my $hexIpAddress="0x7f000001000000000000000000000000";
	my $encryptedPass = "585e3c3b2d2b3d2aebef";#dcusers password
	if($datagenToStart == 2){#remote
		$hostname=$dgServer;
		$hexIpAddress=$dgServerHexIpAddress
	}elsif($datagenToStart != 3){#local
		return "";
	}
	
	my $workflows=qq{"ID","Name","[Input]Host","[Input]Username","[Input]Password"
1,"SGSN1","$hostname","$username",$encryptedPass
};
	unlink("/tmp/wfexport$$.csv");
	open(CSV, ">/tmp/wfexport$$.csv");
	print CSV $workflows;
	close (CSV);
	unlink("/tmp/wfexport2$$.csv");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport SGEH.WF01_LoadBalancer /tmp/wfexport2$$.csv");
	
	if(!files_match("/tmp/wfexport$$.csv","/tmp/wfexport2$$.csv")){
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist SGEH.DUMMY.* | xargs -n 2 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf SGEH.WG01_LoadBalancer");
		
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist SGEH.WF01_LoadBalancer.* | awk '{print \$1}' | xargs -n 20 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist SGEH.WF01_LoadBalancer.* | awk '{print \$1}' | xargs -n 20 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupremovewf SGEH.WG01_LoadBalancer");

		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport SGEH.WF01_LoadBalancer /tmp/wfexport$$.csv");
		
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf SGEH.WG01_LoadBalancer SGEH.WF01_LoadBalancer.SGSN1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist SGEH.DUMMY.* | xargs -n 2 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupremovewf SGEH.WG01_LoadBalancer");
	}
	
	sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET IP_ADDRESS=$hexIpAddress where SGSN_NAME='SGSN1'");
	unlink("/tmp/wfexport$$.csv");
	unlink("/tmp/wfexport2$$.csv");
}


#########################################
sub setup2g3g4gWorkflows_like4like{
	my $remoteDatagen=shift;
	my $force=shift;
	my $host=getHostName();
	my $hostname;
	my $totalDirs=5;
	my $result="";
	
	my $linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh | grep ^l'",0);
	my $success=runCommand("ssh dcuser\@ec_sgeh_1 'ls /eniq/data/eventdata/events_oss_1/sgeh/dir1 >/dev/null 2>&1'",0);
	
	my $sgeh_dir="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	my $remote_sgeh_dir="$dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/MSS";
	
	if(($remoteDatagen) || (!$remoteDatagen && ($linkExists || !$success)) || $force==1){
		if($remoteDatagen){
			executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh 2>/dev/null'");
			runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh'",1);
			for(my $i=3;$i<$totalDirs;$i++){
				runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir$i'",1);
			}
			my $success=runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
			if($success==1){
				&FT_LOG("INFO:Symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1 at /eniq/data/eventdata/events_oss_1/sgeh/dir2 on ec_sgeh_1");
			}else{
				&FT_LOG("ERROR:Failed to create symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1 at /eniq/data/eventdata/events_oss_1/sgeh/dir2 on ec_sgeh_1");
			}
		}else{
			if($linkExists){
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh 2>/dev/null'");
			}
			runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh'",1);
			for(my $i=1;$i<$totalDirs;$i++){
				runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir$i'",1);
			}
		}
	}else{
		&FT_LOG("INFO:/eniq/data/eventdata/events_oss_1/sgeh already exists on ec_sgeh_1. Put dataGenStart_2G3G4G force in a config file to force re-creation");
	}
	
	#$success=runCommand("ssh dcuser\@ec_sgeh_1 'ls /eniq/data/eventdata/events_oss_1/sgeh/dir1 >/dev/null 2>&1'",0);
	#if($success==0){
	#	&FT_LOG("ERROR:/eniq/data/eventdata/events_oss_1/sgeh/dir1 doesn't exist on ec_sgeh_1");
	#}
	
	my $dirlinkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir1 | grep ^l'",0);
	if(!$dirlinkExists){
	    &FT_LOG("ERROR:/eniq/data/eventdata/events_oss_1/sgeh/dir1 doesn't exist on ec_sgeh_1, svcadm restart system/filesystem/autofs will be run now!");
	    executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/home/dcuser/automation/RunCommandAsRoot.sh svcadm restart system/filesystem/autofs 2>/dev/null'");
	    sleep (120);
	}
	
	my $dir2linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir2 | grep ^l'",0);
	if(!$dir2linkExists){
	   runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
	}
	
	my $allWorkflowsAdded=1;
	#Here invoke the provision_workflows.sh anyway, just incase there will be future changes that may break our regression
	my @status=executeThisQuiet("ssh dcuser\@ec_sgeh_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_SGEH/bin;./provision_workflows.sh'");
	if(grep(/ERROR/i, @status) || grep(/Can NOT/i, @status) || grep(/failed$/, @status) || grep(/No OSS information found/i,@status)){
		$allWorkflowsAdded=0;
	}
	if($allWorkflowsAdded){
		$result=logAndGenerateTableEntry("Set up 2G3G4G workflows has been done by invoking provision_workflows.sh",1);
	}else{
		$result=logAndGenerateTableEntry("Failed to provision 2G3G4G workflows even by invoking provision_workflows.sh",0);	
	}

	return $result;
}

########################################

sub setup2g3g4gWorkflows{
	my $remoteDatagen=shift;
	my $force=shift;
	my $host=getHostName();
	my $hostname;
	my $totalDirs=5;
	my $result="";
	
	my $linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh | grep ^l'",0);
	my $success=runCommand("ssh dcuser\@ec_sgeh_1 'ls /eniq/data/eventdata/events_oss_1/sgeh/dir1 >/dev/null 2>&1'",0);
	
	my $sgeh_dir="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	my $remote_sgeh_dir="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	
	if(($remoteDatagen) || (!$remoteDatagen && ($linkExists || !$success)) || $force==1){
		if($remoteDatagen){
			executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh 2>/dev/null'");
			runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh'",1);
			for(my $i=3;$i<$totalDirs;$i++){
				runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir$i'",1);
			}
			my $success=runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
			if($success==1){
				&FT_LOG("INFO:Symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1 at /eniq/data/eventdata/events_oss_1/sgeh/dir2 on ec_sgeh_1");
			}else{
				&FT_LOG("ERROR:Failed to create symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1 at /eniq/data/eventdata/events_oss_1/sgeh/dir2 on ec_sgeh_1");
			}
		}else{
			if($linkExists){
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh 2>/dev/null'");
			}
			runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh'",1);
			for(my $i=1;$i<$totalDirs;$i++){
				runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir$i'",1);
			}
		}
	}else{
		&FT_LOG("INFO:/eniq/data/eventdata/events_oss_1/sgeh already exists on ec_sgeh_1. Put dataGenStart_2G3G4G force in a config file to force re-creation");
	}
	
	#$success=runCommand("ssh dcuser\@ec_sgeh_1 'ls /eniq/data/eventdata/events_oss_1/sgeh/dir1 >/dev/null 2>&1'",0);
	#if($success==0){
	#	&FT_LOG("ERROR:/eniq/data/eventdata/events_oss_1/sgeh/dir1 doesn't exist on ec_sgeh_1");
	#}
	
	my $dirlinkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir1 | grep ^l'",0);
	if(!$dirlinkExists){
	    &FT_LOG("ERROR:/eniq/data/eventdata/events_oss_1/sgeh/dir1 doesn't exist on ec_sgeh_1, svcadm restart system/filesystem/autofs will be run now!");
	    executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/home/dcuser/automation/RunCommandAsRoot.sh svcadm restart system/filesystem/autofs 2>/dev/null'");
	    sleep (120);
	}
	
	my $dir2linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir2 | grep ^l'",0);
	if(!$dir2linkExists){
	   runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
	}
	
	my $allWorkflowsAdded=1;
	#Here invoke the provision_workflows.sh anyway, just incase there will be future changes that may break our regression
	my @status=executeThisQuiet("ssh dcuser\@ec_sgeh_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_SGEH/bin;./provision_workflows.sh'");
	if(grep(/ERROR/i, @status) || grep(/Can NOT/i, @status) || grep(/failed$/, @status) || grep(/No OSS information found/i,@status)){
		$allWorkflowsAdded=0;
	}
	if($allWorkflowsAdded){
		$result=logAndGenerateTableEntry("Set up 2G3G4G workflows has been done by invoking provision_workflows.sh",1);
	}else{
		$result=logAndGenerateTableEntry("Failed to provision 2G3G4G workflows even by invoking provision_workflows.sh",0);	
	}

	return $result;
}

sub enable2g3g4gWorkflows{
	my ($datagen2G3G,$datagen4G,$remoteDG)=@_;
	#enable logging workflows
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG00_LogParsing_Inter");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF00_ParsingLog_Inter.logging");
	
	#enable SGEH workflows
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_*");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_Cell_Lookup_Refresh_DB");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.now");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.scheduled");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir*");
	
	#Disable workflows 04 -> 15 because only 00 -> 03 are used
	&FT_LOG("INFO:Disabling unused workflows");
	#executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable SGEH.WF*.0[4-9]");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable SGEH.WF*.1[0-5]");
}

sub enable2g3g4gDatagen{
	## Group Enable Start 10B ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable eankmuk_4G.WG_dataGen_010B");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart eankmuk_4G.WG_dataGen_010B");
	
	## Enable and Start MME1 ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_4G.dataGen_010B.MME1");
	
	## Enable and Start MME2 ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_4G.dataGen_010B.MME2");
	
	## Enable and Start LogParser Group ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG00_LogParsing_Inter");
	
	## Enable and Start 2G3G Processing ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG02_Processing");
	
	## Enable and Start 4G Processing ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG02_Processing_4G");
	
	## Enable and Start PDPS Processing ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG03_PDPS_Processing");
}

sub import2g3g4gDatagen{
	## Import DataGen Workflows ##
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr systemimport -nameconflict re -keyconflict re  /eniq/home/dcuser/automation/DataGenWorkFlows/DataGenWorkFlows4G.zip");
	if(grep(/as invalid/,@status)){
		&FT_LOG("ERROR:eankmuk_4G workflow did not import correctly. Delete the entire workflow folder using the Mediation Zone GUI and run this function again");
		return 0;
	}else{
		return 1;
	}
}

sub checkECSTStatus{
	my $ecstnum = shift ;
    my $result=qq{
	<h3>EC_ST_1 STATUS</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	print("INFO:Checking ECs status now\n");
			
	my $ecstname = "ec_st_".$ecstnum;
    my @contents=executeThisQuiet("ssh dcuser\@${ecstname} 'source ~/.profile; ec status'");

			
	#Trying to restart EC_ST, if not up already
			
   if(grep(/EC_ST_${ecstnum} is running/,@contents)){
		  print("PASS\n");
		  $result.="<tr><td>EC_ST_${ecstnum} is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			
		}else{
		    print("${ecstname} is not running. EC_ST_1/2 restart \n");
			executeThisWithLogging("ssh dcuser\@${ecstname} 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_${ecstnum}'");
			my @results=executeThisQuiet("ssh dcuser\@${ecstname} 'source ~/.profile; ec status'");
			if(grep(/EC_ST_${ecstnum} is running/,@results)){
			    $result.="<tr><td>EC_ST_${ecstnum} is running</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			}else{
			   $result.="<tr><td>EC_ST_${ecstnum} is not running</td><td align=center><font color=darkblue><b>FAIL</b></font></td></tr>\n";
			}
		}
   
    $result.="</TABLE>";
	return $result;

}

sub runEdeLteStreaming{
    
	my $result=qq{
	<h3>LTE STREAMING</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
    my $EDEServer = "/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release";	
	my $User = "root";
	my $Pass = "shroot12";
	my $EdePath = "/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin";
	my $destInfoFile = "$EdePath/destIpFoldersInfoList.txt";
	
	my $ec_1_server = (grepFile("ec_1", "/etc/hosts"))[0];
	my $ec_1 = splitAndGetValue($ec_1_server, " ", 3);
	my @values1 = split( " ", $ec_1_server );
	my $ec_ip = $values1[0];
	
		
	my @ec_st = grepFile("ec_st_1", "/etc/hosts");
	
	
	if(@ec_st == 0){
		print "Adding EC_ST_1 and EC_ST_2 pico\n";
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pico -add EC_ST_1 EC $ec_ip'");
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pico -add EC_ST_2 EC $ec_ip'");
	}
	
	my $destInfo;
	my $cor_server = (grepFile("controlzone", "/etc/hosts"))[0];
	my $cor = splitAndGetValue($cor_server, " ", 3);
	my $ec_server = (grepFile("ec_st_1", "/etc/hosts"))[0];
	my $ec = splitAndGetValue($ec_server, " ", 3);
	my @values = split( " ", $ec_server );
	my $ip = $values[0];
	
	#Check the ede_stream.log present and grep "START" in the file, 
	#if file is not present create the file and write START else exit from the process
	my $logFilePath = "/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/$cor";
	my $logFile = $logFilePath."/ede_stream.log";
	if(-e $logFile){	
	 $result=`/usr/bin/grep START $logFile`;
	 chomp($result);
	if($result eq 'START'){	
	 print"The LTE_Streaming is already running on the server $cor \n";
	 print"Existing from current process \n";
	exit ;	
	 }
	}
	else{
		executeThisWithLogging("echo  > $logFilePath/ede_stream.log");
	}
	
	$destInfo = $ip."#/ede/EDE_multidest/ctr_input#/ede/EDE_multidest/ctr_inter#".$cor."\n";
	print "The content of destIpFoldersInfoList.txt is $destInfo\n";
	system (" echo '$destInfo' > $destInfoFile " );
	
	my $continue = 1;

    my $childPid = fork(); #fork a process
		if($childPid == 0){
		  while($continue==1){
				executeThisWithLogging("cd /eniq/home/dcuser/automation");
				executeThisWithLogging("chmod 777 sshToEdeServer.sh");
				my @response=executeThisWithLogging("./sshToEdeServer.sh");
				
				if(grep(/Error.*Quitting/,@response)){
					open(EDEIN,"<$destInfoFile");
			        my @contents=<EDEIN>;
			        close(EDEIN);
				
			        open(EDEOUT,">$destInfoFile");
				
					my $line;
					foreach $line(@contents){
					   if($line =~ m/$destInfo/){
						  $line =~ s/$destInfo//;
						  print "MATCH\n";
					   }
					   print EDEOUT $line;
					}
					close (EDEOUT);				
				}
				
				
				sleep 600;
				
				open(EDEIN,"<$destInfoFile");
				my @contents=<EDEIN>;
				close(EDEIN);
			   
			   if(@contents == 0){
			       $continue==0;
			   }			
			}
			
			exit(0);
        }else{			
		    my $host = getHostName();

			my $basepath = "/50files/eniq/data/pmdata/eventdata/00/CTRS";	
			
			print("INFO:WorkflowStart_CTR\n");
			my $ctrsDir="/eniq/data/pmdata/eventdata/00/CTRS/ctrs/5min";
			my $executionContextFile="/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml";
			
			my $in;
			my $out;
			my @instrumentFiles;
			my @status;
			
			print("INFO:Shutting Down non-relevant services\n");

			
			# Check and update the service_names file with the EC_ST_1 and EC_ST_2 service names
			
			my $addedInFile = 0;
			
			open my $file1,  '<',  "/eniq/sw/conf/service_names"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
			   if( $_ =~ m/ec_st_1/) {
					 $addedInFile = 1;
				}

			   last if $addedInFile == 1;
			}
		   
			close $in;
			
			if ($addedInFile == 0) {
			
				 my $lineToAdd1 = "";
				 my $lineToAdd2 = "";
				 open my $in,  '<',  "/eniq/sw/conf/service_names"      or die "Can't read file: $!";
				 open my $out, '>', "/tmp/service_names2"      or die "Can't read file: $!";
				 while( <$in> )   # print the lines before the change
				 {
					 print $out $_;
					 if ( $_ =~ m/ec_1/ ) {
						 $lineToAdd1 = $_;
						 $lineToAdd2 = $_;
						}
					last if( $_ =~ m/ec_1/ );
				 }
				 
				$lineToAdd1 =~ s/ec_1/ec_st_1/g;
				print $out $lineToAdd1;
				
				$lineToAdd2 =~ s/ec_1/ec_st_2/g;
				print $out $lineToAdd2;

				while( <$in> )
				{
					 print $out $_;
				}
			
				close $in;
				close $out;
				copy "/tmp/service_names2", "/eniq/sw/conf/service_names";
			}
				
			print("INFO:Start up EC_ST_1 & EC_ST_2\n");
			
			executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_1'");
			executeThisWithLogging("ssh dcuser\@ec_st_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_2'");


			print("INFO:Checking ECs status now\n");
			
			my @contents=executeThisQuiet("ssh dcuser\@ec_st_1 'source ~/.profile; ec status'");

			
			#Trying to restart EC_ST, if not up already
			
			if(grep(/EC_ST_1 is not running/,@contents)){
				 print("EC_ST_1/2 is not running. EC_ST_1/2 restart \n");
				 executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_1'");
				 executeThisWithLogging("ssh dcuser\@ec_st_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_2'");
			}
			
				
			
			# Before starting the workflows, update the ctrs.prop file and the ctrs_ip2fdnmap.txt with data from the EDE FDN mapping file generated.
			
			$addedInFile = 0;

			open my $file1,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
				 if( $_ =~ m/ipfdn.mapping.source.db=false/ ) {
					 $addedInFile = 1;
				 }

				 last if $addedInFile == 1;
			}

			close $in;

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
				 move "/tmp/ctrs.prop2", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop";
			}

			while( 1 )
			{
				 last if (-e "/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/cfg/user/CTR_FDNSourceIPMapping.txt");

				 print "Waiting for EDE script to put the CTR_FDNSourceIPMapping.txt file\n";
				 sleep 30;
			}
			
			copy "/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/cfg/user/CTR_FDNSourceIPMapping.txt", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs_ip2fdnmap.txt";
			
			$addedInFile = 0;

			open my $file1,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
				 if( $_ =~ m/stream.ctr.allowed_events=all/ ) {
					 $addedInFile = 1;
				 }

				 last if $addedInFile == 1;
			}

			close $in;

			if ($addedInFile == 0) {

				 my $lineToAdd = "";
				 open my $in,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";
				 open my $out, '>', "/tmp/ctrs.prop3"      or die "Can't read file: $!";
				 while( <$in> )   # print the lines before the change
				 {
					 if ( $_ =~ m/stream.ctr.allowed_events/ ) {
						 $lineToAdd = $_;
						}
					 last if( $_ =~ m/stream.ctr.allowed_events/ );
					 print $out $_;
				 }
				 $lineToAdd =~ s/stream.ctr.allowed_events=.*/stream.ctr.allowed_events=all/g;
				 print $out $lineToAdd;

				 while( <$in> )   # print the rest of the lines
				 {
					 print $out $_;
				 }

				 close $in;
				 close $out;
				 move "/tmp/ctrs.prop3", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop";
			}
			
			
			#Enabling the workflowgroups and workflows for CTR
			
			my @wfGroupsToEnable=(
				"STREAMING_CTR_ST_1.WG01_CTR_Stream_Listener_5_minute",
				"STREAMING_CTR.WG03_ENB_IP2FDN_Map_Reader",
				"STREAMING_CTR_ST_1.WG02_CTR_Stream_Collector_5_minute",
				"STREAMING_CTR.WG04_CTR_INSTRUMENT_OUTPUT");
				
			my @wfToEnable=(
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.1",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.2",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.3",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.4",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.5",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.6",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.7",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.8",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.9",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.10",
				"STREAMING_CTR.WF03_ENB_IP2FDN_Map_Reader.00",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.00",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.01",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.02",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.03",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.04",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.05",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.06",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.07",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.08",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.09",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.10",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.11",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.12",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.13",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.14",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.15",
				"STREAMING_CTR.WF04_CTR_INSTRUMENT_OUTPUT.00");

			foreach my $wf(@wfToEnable){
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable $wf");
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart $wf");
			}		

			foreach my $group(@wfGroupsToEnable){
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable $group");
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart $group");
			}
			 			 
			# Running the streaming admin script to make sure nothing is missed from the above steps wrt streaming workflows
			
			executeThisWithLogging("cd /eniq/mediation_inter/M_E_CTRS/bin/; ./streaming_admin.sh -f ctr -e startlisten");
			executeThisWithLogging("cd /eniq/mediation_inter/M_E_CTRS/bin/; ./streaming_admin.sh -f ctr -e startoutput");
				
				
			 sleep 120;
			   
			   system (" echo 'START' >  $logFile " );
			   
				# Force the instrument file to get generated before cleanup and start of teh streaming to make sure there is no residue events left at the CTR workflow.

				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart STREAMING_CTR.WF04_CTR_INSTRUMENT_OUTPUT.00");
		 
			# Cleanup the old instrument files here to avoid confusion
			
			my $basepath1 = $basepath."/ctr_instrument";
			@instrumentFiles=executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cd /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/; find . -type f | grep -i celltrace_instrument | grep -v grep'") ;
			chomp(@instrumentFiles);
			
			foreach my $instrumentFile (@instrumentFiles) {
				 executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cd /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/; rm $instrumentFile");
				 
			}		
			
			my $logFile = $logFilePath."/ede_stream.log";
			my $edeEventCount = 0;
		 
			while (1) {
				 

				 my $last = "";
				 open my $in,  '<',  $logFilePath."/ede_stream.log"      or die "Can't read file: $!";

				 while( <$in> )   # print the lines before the change
				 {

					 if ($. == 2) {
						 $edeEventCount = $_;
					 }

					 next if $. < 3; # line number before change
					 $last = $_;
					 last if $. == 3;
				 }
		   
				 close $in;


				 last if $last =~ "DONE";
				
				 print "Waiting for the EDE Streaming to complete\n";
				 
				 sleep 30;
			}
				
			#print "EDE end event count is: $edeEventCount \n";
			&FT_LOG("EDE end event count is: $edeEventCount \n");
			
			#unlink($logFilePath."/ede_stream.log");
			system("rm $logFilePath/ede_stream.log");
			
			# Wait for the streaming files to be received by the workflow	
			sleep 300;
			
			# Force the instrument file to get generated.
			
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart STREAMING_CTR.WF04_CTR_INSTRUMENT_OUTPUT.00");
			

			sleep 600;
			
			# Now lets get the instrument output to compare with the EDE output
			
			my $outputCount = 0;
				
			@status=executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cd /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/; find . -type f | grep -i celltrace_instrument | grep -v grep'") ;
			chomp(@status);
			
			foreach my $status (@status) {
				 substr $status, 0, 1, "";
				my @outputLine=executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cat /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/$status'");
				 chomp(@outputLine);
				 
				 foreach my $outputLine (@outputLine[1 .. $#outputLine]) {

					 my @values = split(/\Q|/, $outputLine);
					 chomp(@values);
					 $outputCount = $outputCount + $values[5];
				} 				 
			}
			
			&FT_LOG("CTRS Workflow output count is: $outputCount \n");
			
			if ($edeEventCount == $outputCount) {
				 print("PASS\n");
				 $result.="<tr><td>EDE end event count: $edeEventCount CTRS Workflow output count: $outputCount</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			}
			else {
				 print("FAIL\n");
				 $result.="<tr><td>EDE end event count: $edeEventCount CTRS Workflow output count: $outputCount</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
			}
			
			open(EDEIN,"</net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin/destIpFoldersInfoList.txt");
			my @contents=<EDEIN>;
			close(EDEIN);
				
			open(EDEOUT,">/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin/destIpFoldersInfoList.txt");
				
				my $line;
				foreach $line(@contents){
				   if($line =~ m/$destInfo/){
					  $line =~ s/$destInfo//;
					  print "MATCH1\n";
				   }
				   print EDEOUT $line;
				}
				close (EDEOUT);
			
        
		    $result.="</TABLE>";
			killProcessAndChildren($childPid,$$);
		}	
        return $result; 
}

sub runEdeLteStreamingOnVapp{
    
	my $result=qq{
	<h3>LTE STREAMING</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	executeThisWithLogging("cd /eniq/home/dcuser/automation");
	executeThisWithLogging("chmod 777 logInToServer.exp sshToShareDatagenEde.exp");
	
	#Log in to vApp datagen server 
	print("INFO:Log in to vApp datagen VM\n");
	executeThisWithLogging("./logInToServer.exp datagen");
	
	#Log in to vApp datagen server for mounting, plumbing and authenticating
	print("INFO:Log in to vApp datagen VM to perform mount, plumb IP and authenticate keys\n");
	executeThisWithLogging("./sshToShareDatagenEde.exp");
	
	executeThisWithLogging("mkdir -p /eniq/home/dcuser/EDE");
	executeThisWithLogging("chmod -R 777 /eniq/home/dcuser/EDE");
	executeThisWithLogging("RunCommandAsRoot.sh mount 192.168.0.212:/ede /eniq/home/dcuser/EDE");
	
    my $EDEServer = "/eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/";	
	my $User = "root";
	my $Pass = "shroot";
	my $EdePath = "/eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/bin";
	my $destInfoFile = "/eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/bin/destIpFoldersInfoList.txt";
	
	my $ec_1_server = (grepFile("ec_1", "/etc/hosts"))[0];
	my $ec_1 = splitAndGetValue($ec_1_server, " ", 3);
	my @values1 = split( " ", $ec_1_server );
	my $ec_ip = $values1[0];
			
	my @ec_st = grepFile("ec_st_1", "/etc/hosts");
		
	if(@ec_st == 0){
		print "Adding EC_ST_1 and EC_ST_2 pico\n";
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pico -add EC_ST_1 EC $ec_ip'");
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pico -add EC_ST_2 EC $ec_ip'");
	}
	
	my $destInfo;
	my $cor_server = (grepFile("controlzone", "/etc/hosts"))[0];
	my $cor = splitAndGetValue($cor_server, " ", 3);
	my $ec_server = (grepFile("ec_st_1", "/etc/hosts"))[0];
	my $ec = splitAndGetValue($ec_server, " ", 3);
	my @values = split( " ", $ec_server );
	my $ip = $values[0];
	
	#Check the ede_stream.log present and grep "START" in the file, 
	#if file is not present create the file and write START else exit from the process
	my $logFilePath = "/eniq/home/dcuser/EDE/$cor";
	my $logFile = $logFilePath."/ede_stream.log";
	if(-e $logFile){	
	 $result=`/usr/bin/grep START $logFile`;
	 chomp($result);
	if($result eq 'START'){	
	 print"The LTE_Streaming is already running on the server $cor \n";
	 print"Existing from current process \n";
	exit ;	
	 }
	}
	else{
		executeThisWithLogging("echo  > $logFilePath/ede_stream.log");
	}
	
	$destInfo = $ip."#/ede/vApp_ctr_input#/ede/vApp_ctr_inter#".$cor."\n";
	print "The content of destIpFoldersInfoList.txt is $destInfo\n";
	system (" echo '$destInfo' > $destInfoFile " );
	
	my $continue = 1;

    my $childPid = fork(); #fork a process
		if($childPid == 0){
		  while($continue==1){
				executeThisWithLogging("cd /eniq/home/dcuser/automation");
				executeThisWithLogging("chmod 777 sshToVappDatagenVM.exp");
				my @response=executeThisWithLogging("./sshToVappDatagenVM.exp");
				
				if(grep(/Error.*Quitting/,@response)){
					open(EDEIN,"<$destInfoFile");
			        my @contents=<EDEIN>;
			        close(EDEIN);
				
			        open(EDEOUT,">$destInfoFile");
				
					my $line;
					foreach $line(@contents){
					   if($line =~ m/$destInfo/){
						  $line =~ s/$destInfo//;
						  print "MATCH\n";
					   }
					   print EDEOUT $line;
					}
					close (EDEOUT);				
				}
				
				
				sleep 600;
				
				open(EDEIN,"<$destInfoFile");
				my @contents=<EDEIN>;
				close(EDEIN);
			   
			   if(@contents == 0){
			       $continue==0;
			   }			
			}
			
			exit(0);
        }else{			
		    my $host = getHostName();
			system("rm -rf /eniq/data/pmdata/eventdata/00/CTRS");
			system("mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/ctrs/5min");
			my $basepath = "/50files/eniq/data/pmdata/eventdata/00/CTRS";	
			
			print("INFO:WorkflowStart_CTR\n");
			my $ctrsDir="/eniq/data/pmdata/eventdata/00/CTRS/ctrs/5min";
			my $executionContextFile="/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml";
			
			my $in;
			my $out;
			my @instrumentFiles;
			my @status;
			
			print("INFO:Shutting Down non-relevant services\n");

			
			# Check and update the service_names file with the EC_ST_1 and EC_ST_2 service names
			
			my $addedInFile = 0;
			
			open my $file1,  '<',  "/eniq/sw/conf/service_names"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
			   if( $_ =~ m/ec_st_1/) {
					 $addedInFile = 1;
				}

			   last if $addedInFile == 1;
			}
		   
			close $in;
			
			if ($addedInFile == 0) {
			
				 my $lineToAdd1 = "";
				 my $lineToAdd2 = "";
				 open my $in,  '<',  "/eniq/sw/conf/service_names"      or die "Can't read file: $!";
				 open my $out, '>', "/tmp/service_names2"      or die "Can't read file: $!";
				 while( <$in> )   # print the lines before the change
				 {
					 print $out $_;
					 if ( $_ =~ m/ec_1/ ) {
						 $lineToAdd1 = $_;
						 $lineToAdd2 = $_;
						}
					last if( $_ =~ m/ec_1/ );
				 }
				 
				$lineToAdd1 =~ s/ec_1/ec_st_1/g;
				print $out $lineToAdd1;
				
				$lineToAdd2 =~ s/ec_1/ec_st_2/g;
				print $out $lineToAdd2;

				while( <$in> )
				{
					 print $out $_;
				}
			
				close $in;
				close $out;
				copy "/tmp/service_names2", "/eniq/sw/conf/service_names";
			}
				
			print("INFO:Start up EC_ST_1 & EC_ST_2\n");
			
			executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_1'");
			executeThisWithLogging("ssh dcuser\@ec_st_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_2'");


			print("INFO:Checking ECs status now\n");
			
			my @contents=executeThisQuiet("ssh dcuser\@ec_st_1 'source ~/.profile; ec status'");

			
			#Trying to restart EC_ST, if not up already
			
			if(grep(/EC_ST_1 is not running/,@contents)){
				 print("EC_ST_1/2 is not running. EC_ST_1/2 restart \n");
				 executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_1'");
				 executeThisWithLogging("ssh dcuser\@ec_st_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_2'");
			}
			
				
			
			# Before starting the workflows, update the ctrs.prop file and the ctrs_ip2fdnmap.txt with data from the EDE FDN mapping file generated.
			
			$addedInFile = 0;

			open my $file1,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
				 if( $_ =~ m/ipfdn.mapping.source.db=false/ ) {
					 $addedInFile = 1;
				 }

				 last if $addedInFile == 1;
			}

			close $in;

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
				 move "/tmp/ctrs.prop2", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop";
			}

			while( 1 )
			{
				 last if (-e "/eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/cfg/user/CTR_FDNSourceIPMapping.txt");

				 print "Waiting for EDE script to put the CTR_FDNSourceIPMapping.txt file\n";
				 sleep 30;
			}
			
			copy "/eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/cfg/user/CTR_FDNSourceIPMapping.txt", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs_ip2fdnmap.txt";
			
			$addedInFile = 0;

			open my $file1,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
				 if( $_ =~ m/stream.ctr.allowed_events=all/ ) {
					 $addedInFile = 1;
				 }

				 last if $addedInFile == 1;
			}

			close $in;

			if ($addedInFile == 0) {

				 my $lineToAdd = "";
				 open my $in,  '<',  "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop"      or die "Can't read file: $!";
				 open my $out, '>', "/tmp/ctrs.prop3"      or die "Can't read file: $!";
				 while( <$in> )   # print the lines before the change
				 {
					 if ( $_ =~ m/stream.ctr.allowed_events/ ) {
						 $lineToAdd = $_;
						}
					 last if( $_ =~ m/stream.ctr.allowed_events/ );
					 print $out $_;
				 }
				 $lineToAdd =~ s/stream.ctr.allowed_events=.*/stream.ctr.allowed_events=all/g;
				 print $out $lineToAdd;

				 while( <$in> )   # print the rest of the lines
				 {
					 print $out $_;
				 }

				 close $in;
				 close $out;
				 move "/tmp/ctrs.prop3", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs.prop";
			}
			
			
			#Enabling the workflowgroups and workflows for CTR
			
			my @wfGroupsToEnable=(
				"STREAMING_CTR_ST_1.WG01_CTR_Stream_Listener_5_minute",
				"STREAMING_CTR.WG03_ENB_IP2FDN_Map_Reader",
				"STREAMING_CTR_ST_1.WG02_CTR_Stream_Collector_5_minute",
				"STREAMING_CTR.WG04_CTR_INSTRUMENT_OUTPUT");
				
			my @wfToEnable=(
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.1",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.2",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.3",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.4",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.5",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.6",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.7",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.8",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.9",
				"STREAMING_CTR_ST_1.WF01_CTR_Stream_Listener.10",
				"STREAMING_CTR.WF03_ENB_IP2FDN_Map_Reader.00",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.00",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.01",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.02",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.03",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.04",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.05",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.06",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.07",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.08",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.09",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.10",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.11",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.12",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.13",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.14",
				"STREAMING_CTR.WF02_CTR_Stream_Collector.15",
				"STREAMING_CTR.WF04_CTR_INSTRUMENT_OUTPUT.00");

			foreach my $wf(@wfToEnable){
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable $wf");
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart $wf");
			}		

			foreach my $group(@wfGroupsToEnable){
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable $group");
				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart $group");
			}
			 			 
			# Running the streaming admin script to make sure nothing is missed from the above steps wrt streaming workflows
			
			executeThisWithLogging("cd /eniq/mediation_inter/M_E_CTRS/bin/; ./streaming_admin.sh -f ctr -e startlisten");
			executeThisWithLogging("cd /eniq/mediation_inter/M_E_CTRS/bin/; ./streaming_admin.sh -f ctr -e startoutput");
				
				
			 sleep 120;
			   
			   system (" echo 'START' >  $logFile " );
			   
				# Force the instrument file to get generated before cleanup and start of teh streaming to make sure there is no residue events left at the CTR workflow.

				executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart STREAMING_CTR.WF04_CTR_INSTRUMENT_OUTPUT.00");
		 
			# Cleanup the old instrument files here to avoid confusion
			system("mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/");
			my $basepath1 = $basepath."/ctr_instrument";
			@instrumentFiles=executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cd /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/; find . -type f | grep -i celltrace_instrument | grep -v grep'") ;
			chomp(@instrumentFiles);
			
			foreach my $instrumentFile (@instrumentFiles) {
				 executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cd /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/; rm $instrumentFile");
				 
			}		
			
			my $logFile = $logFilePath."/ede_stream.log";
			my $edeEventCount = 0;
		 
			while (1) {
				 

				 my $last = "";
				 open my $in,  '<',  $logFilePath."/ede_stream.log"      or die "Can't read file: $!";

				 while( <$in> )   # print the lines before the change
				 {

					 if ($. == 2) {
						 $edeEventCount = $_;
					 }

					 next if $. < 3; # line number before change
					 $last = $_;
					 last if $. == 3;
				 }
		   
				 close $in;


				 last if $last =~ "DONE";
				
				 print "Waiting for the EDE Streaming to complete\n";
				 
				 sleep 30;
			}
				
			#print "EDE end event count is: $edeEventCount \n";
			&FT_LOG("EDE end event count is: $edeEventCount \n");
			
			#unlink($logFilePath."/ede_stream.log");
			system("rm $logFilePath/ede_stream.log");
			
			# Wait for the streaming files to be received by the workflow	
			sleep 300;
			
			# Force the instrument file to get generated.
			
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart STREAMING_CTR.WF04_CTR_INSTRUMENT_OUTPUT.00");
			

			sleep 600;
			
			# Now lets get the instrument output to compare with the EDE output
			
			my $outputCount = 0;
				
			@status=executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cd /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/; find . -type f | grep -i celltrace_instrument | grep -v grep'") ;
			chomp(@status);
			
			foreach my $status (@status) {
				 substr $status, 0, 1, "";
				my @outputLine=executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; cat /eniq/data/pmdata/eventdata/00/CTRS/ctr_instrument/$status'");
				 chomp(@outputLine);
				 
				 foreach my $outputLine (@outputLine[1 .. $#outputLine]) {

					 my @values = split(/\Q|/, $outputLine);
					 chomp(@values);
					 $outputCount = $outputCount + $values[5];
				} 				 
			}
			
			&FT_LOG("CTRS Workflow output count is: $outputCount \n");
			
			if ($edeEventCount == $outputCount) {
				 print("PASS\n");
				 $result.="<tr><td>EDE end event count: $edeEventCount CTRS Workflow output count: $outputCount</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			}
			else {
				 print("FAIL\n");
				 $result.="<tr><td>EDE end event count: $edeEventCount CTRS Workflow output count: $outputCount</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
			}
			
			open(EDEIN,"</eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/bin/destIpFoldersInfoList.txt");
			my @contents=<EDEIN>;
			close(EDEIN);
				
			open(EDEOUT,">/eniq/home/dcuser/EDE/ede-6.0.7-Release/ede-6.0.7-Release/bin/destIpFoldersInfoList.txt");
				
				my $line;
				foreach $line(@contents){
				   if($line =~ m/$destInfo/){
					  $line =~ s/$destInfo//;
					  print "MATCH1\n";
				   }
				   print EDEOUT $line;
				}
				close (EDEOUT);
			
        
		    $result.="</TABLE>";
			killProcessAndChildren($childPid,$$);
		}
		executeThisWithLogging("RunCommandAsRoot.sh umount -f /eniq/home/dcuser/EDE");
        return $result;
}

sub run_ltees_Like4Like
{
    my $ref_server = "$referenceServerHostname";
	$ref_server=~s/ //g;
	chomp $ref_server;
	my $file_name;
	my $teststatus;
	my $hash_id;
	my $automation_dir = "/eniq/home/dcuser/automation";
	my $ltees_tarFileDir = "/eniq/home/dcuser/automation/Like4Like";
	my $ltees_resultFileDir = "$ltees_tarFileDir/reports/comparison/";

	my @like4likeDir = ('setup', 'logs', 'reports', 'bin',);
	my $hostnm = `hostname`;
	chomp $hostnm;
	my $currDate = `date "+%Y-%m-%d %H:%M"`;
	chomp $currDate;
	my $northbound_dir="/eniq/northbound/lte_event_stat_file/events_oss_1/dir18/";

	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=gmtime();
	$year = $year+1900;
	my $month= sprintf("%02d",$mon+1);
	my $day  = sprintf("%02d",$mday);
    my $start_time = "$year-$month-$day";

	my $start_hour = sprintf("%02d",$hour);
	my $start_min=sprintf("00");

	if($min > 0 && $min <=15)
	{
	$start_min=sprintf("00");
	}

	if($min > 15 && $min <=30)
	{
	$start_min=sprintf("15");
	}
	if($min > 30 && $min <=45)
	{
	$start_min=sprintf("30");
    }

	if($min > 45 && $min <60)
	{
	$start_min=sprintf("45");
    }

	if($min ==0)
	{
	$start_min=sprintf("45");
	$start_hour=$start_hour-1;
	}

	$start_time.=" $start_hour:$start_min";
	

	&FT_LOG("Like4Like Start Time for LTE-ES is $start_time\n");
	&FT_LOG("Sleep of 20 mins before starting Like4Like for LTE-ES\n");
	sleep(1200);


	my $result=qq{
        <h3>Like4Like LTE-ES</h3>
        <TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
           <tr>
                 <th>Summary File </th>
                 <th>TEST RESULT</th>
           </tr>
        };

	# Remove the directories from previous execution in like4like testing.
	foreach my $like4likeDir (@like4likeDir)
	{
		if ( -d "$ltees_tarFileDir/$like4likeDir")
		{
			executeThisWithLogging("rm -rf $ltees_tarFileDir/$like4likeDir");
		}
	}

	# Untar the Like4Like tar ball if it is present, else throw error.
	if ( -e "$ltees_tarFileDir/Like4Like.tar" )
	{
		executeThisWithLogging("cd $ltees_tarFileDir; tar -xf $ltees_tarFileDir/Like4Like.tar");
	}
	elsif ( -e "$ltees_tarFileDir/Like4Like.tar.gz" )
	{
		executeThisWithLogging("cd $ltees_tarFileDir; gunzip $ltees_tarFileDir/Like4Like.tar.gz -c | tar xf -");
	}
	else
	{
		&FT_LOG("ERROR: Like4Like tar ball not present under $ltees_tarFileDir.");
		exit(1);
	}


	
	# Update the Referance server and Current Server in LTEES property file.
	executeThisWithLogging("sed s/\"ServerOneHostname.*\"/\"ServerOneHostname=$ref_server\"/g $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$ref_server.lteesProperties");
	executeThisWithLogging("mv /tmp/$ref_server.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	executeThisWithLogging("sed s/\"ServerTwoHostname.*\"/\"ServerTwoHostname=$hostnm\"/g $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$hostnm.lteesProperties");
	executeThisWithLogging("mv /tmp/$hostnm.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");


	# Update the Start time in LTEES property file.
	executeThisWithLogging("sed s/StartDateTime.*/\"StartDateTime=$start_time\"/g $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$ref_server.lteesProperties");
	executeThisWithLogging("mv /tmp/$ref_server.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	# Update the path for LTEES northbound directory in LTEES property file.
	executeThisWithLogging("sed s/\"NorthboundDirectory.*\"/\"NorthboundDirectory=\\/eniq\\/northbound\\/lte_event_stat_file\\/events_oss_1\\/dir18\\/\"/ $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$ref_server.lteesProperties");
	executeThisWithLogging("mv /tmp/$ref_server.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	# Update other attributes LTEES property file.
	executeThisWithLogging("sed s/\"OutputFilesInLocalTime.*\"/\"OutputFilesInLocalTime=false\"/ $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$ref_server.lteesProperties");
	executeThisWithLogging("mv /tmp/$ref_server.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	executeThisWithLogging("sed s/\"TestDuration.*\"/\"TestDuration=15m\"/ $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$ref_server.lteesProperties");
	executeThisWithLogging("mv /tmp/$ref_server.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	executeThisWithLogging("sed s/\"NoOfExecutionsToPerform.*\"/\"NoOfExecutionsToPerform=1\"/ $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt > /tmp/$ref_server.lteesProperties");
	executeThisWithLogging("mv /tmp/$ref_server.lteesProperties $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt; chmod 777 $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");


	#Workaround as last line from Like4Like LTEES property file is geting deleted
	my @var=`grep Frequency $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt`;
	if($var[0] eq ""){
		 executeThisWithLogging("echo 'Frequency=15m' >> $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");
	}

	&FT_LOG("LTE-ES Like4Like property file\n");
	executeThisWithLogging("cat $ltees_tarFileDir/setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	# Create the EE property file.
	executeThisWithLogging("echo \"dburl=jdbc:sybase:Tds:$hostnm.athtem.eei.ericsson.se:2640/dwhdb\" > $ltees_tarFileDir/setup/database/$hostnm.prop");
	executeThisWithLogging("echo 'user=dc' >> $ltees_tarFileDir/setup/database/$hostnm.prop");
	executeThisWithLogging("echo 'password=dc' >> $ltees_tarFileDir/setup/database/$hostnm.prop");
	executeThisWithLogging("echo 'driver=com.sybase.jdbc4.jdbc.SybDriver' >> $ltees_tarFileDir/setup/database/$hostnm.prop");


	sleep 30;

	# Execute the like4like DB Comparision python script.
	executeThisWithLogging("cd $ltees_tarFileDir/bin; $automation_dir/RunCommandAsRoot.sh python ./Like4Like_LTEES_Comparison.py -f ../setup/comparison/Like4Like_LTEES_Comparison_Properties.txt");

	executeThisWithLogging("$automation_dir/RunCommandAsRoot.sh chown -R dcuser:dc5000 $ltees_tarFileDir/logs ");
	executeThisWithLogging("$automation_dir/RunCommandAsRoot.sh chown -R dcuser:dc5000 $ltees_tarFileDir/reports ");

	# Prepare the report for LTEES.

	my @resultDirs;	
	@resultDirs=`ls -ld $ltees_resultFileDir/Like4Like_LTEES_Report_Summary* | awk '{print \$9}'`;

	my $resultFile = "";
	$resultFile=`ls -lrt $ltees_resultFileDir/Like4Like_LTEES_Report_Summary*txt | tail -1 | awk '{print \$NF}' | xargs echo`;


	chomp($resultFile);
	&FT_LOG("Filename : $resultFile");

	if ( $resultFile ne "" )
	{
		open(RESULTS, "<$resultFile") or die "Cannot open $resultFile Or no summary file generated";
		my @results=<RESULTS>;
        	close(RESULTS);

		foreach my $line (@results)
		{
			if ( ( $line =~ m/File\|Hash ID\|File\|Hash ID/ ) || ( $line =~ m/$ref_server\|$hostnm/ ) )
			{
				next;
			}
			my @result_line = split (/|/, $line);
			$file_name = "Results for start time $start_time. Reports can be found in $resultFile" ;
			
			$result.="<tr><td>$file_name </td><td align=center><font color=red><b>FAIL</b></font></td></tr>\n";
		}
	}
	elsif ( @resultDirs ) 
	{
		$file_name = "Results for start time $start_time. Reports can be found in $resultDirs[0]" ;
		
		$result.="<tr><td>$file_name </td><td align=center><font color=green><b>PASS</b></font></td></tr>\n";

	}
	else	
	{
		&FT_LOG("No file summary file or directory present ");
	}	
	$result.="</TABLE>";
	return $result;

}

sub getVappDataPath{
        my $vAppInfo = (grepFile("VAPPDIR","/net/atclvm560.athtem.eei.ericsson.se/eniq/home/dcuser/centralDatagen/DATAGEN_SETTINGS.txt"))[0];
        my $stringToReplace = $vAppInfo;
        chop($vAppInfo);
        my $stringToReplace = $vAppInfo;
        my $increment = chop($vAppInfo);
        my $newIncrement = $increment+1;
        if($newIncrement >= 6){
                 $newIncrement = 0;
        }

        my $newVappInfo = "$vAppInfo".$newIncrement;

        my $file = "/net/atclvm560.athtem.eei.ericsson.se/eniq/home/dcuser/centralDatagen/DATAGEN_SETTINGS.txt";
        open (IN, $file) || die "Cannot open file ".$file." for read";
        my @lines=<IN>;
        close IN;
        my $line2change='';
        open (OUT, ">", $file) || die "Cannot open file ".$file." for write";
        foreach $line2change (@lines)
        {
                 $line2change =~ s/$stringToReplace/$newVappInfo/ig;
                print OUT $line2change;
        }
        close OUT;

        my $vAppVersion = "V".$increment;
        return $vAppVersion;
}




sub like4like_currenttime_setting
{	
		
	&FT_LOG("Sleep of 15 mins before updating start time for Like4Like test cases");
	sleep 900;
my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)= gmtime(time);
my $year = sprintf($year)+1900;
my $month= sprintf("%02d",$mon+1);
my $day  = sprintf("%02d",$mday);


print("$sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst");
print("$year , $month, $day ,$endTime ");

my $end_min=sprintf("00");
if($min > 0 && $min <=15)
{
$end_min=sprintf("00");
}

if($min > 15 && $min <=30)
{
$end_min=sprintf("15");
}

if($min > 30 && $min <=45)
{
$end_min=sprintf("30");
}

if($min > 45 && $min <60)
{
$end_min=sprintf("45");
}


if($min ==0)
{
$end_min=sprintf("45");
$hour=$hour-1;
}

$hour = sprintf("%02d",$hour);

#$#$endTime="$year-$mon-$mday $hour:$end_min";
$endTime="$year-$month-$day $hour:$end_min";

chomp $endTime;
&FT_LOG("Start time for Like4Like will be: $endTime \n");

}


sub datagenStartForArrest_IT{
     # To add exclusive_tac in GROUP_TYPE_E_TAC table

    &FT_LOG("Addtion of Exclusive tac group in DB");
    my @values =sqlSelect("SELECT TAC FROM GROUP_TYPE_E_TAC where group_name='exclusive_tac'");
    if ( grep { $_ eq 99000033} @values )
    {
     &FT_LOG("Exclusive tacs 99000033 is already present in DB");
    }
    else
    {
     my $insert = "INSERT INTO GROUP_TYPE_E_TAC (tac, group_name) values (99000033, 'exclusive_tac')";
     sqlInsert($insert);
     &FT_LOG("Exclusive tac 99000033 added in DB ");
     }

    my @values =sqlSelect("SELECT TAC FROM GROUP_TYPE_E_TAC where group_name='exclusive_tac'");
    if ( grep { $_ eq 99000044} @values )
    {
     &FT_LOG("Exclusive tacs 99000044 is already present in DB");
    }
    else
    {
     my $insert = "INSERT INTO GROUP_TYPE_E_TAC (tac, group_name) values (99000044, 'exclusive_tac')";
     sqlInsert($insert);
     &FT_LOG("Exclusive tac 99000044 added in DB ");
    }

    &FT_LOG("Addition of Exclusive tac group completed");



    my $arg=shift;
	my $force=0;
	my  $result=qq{
	<h3>START 2G3G and/or 4G DATAGEN for Arrest_IT</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	my $host = getHostName();
	my $vAppDir;
	
	my $EdeSgehDir = "/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/$host/50files/eniq/data/eventdata/events_oss_1/sgeh";
	my $remotedir1="/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	#my $remotedir4="/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir4";
	my $dir2="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	#when 3G SESSION BROWSER DATAGEN MOVE TO 560, PLEASE REMOVE THE FOLLOWING LINE!!
	my $dgNfsPath="/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen";
	
	my @ec_servers=("ec_sgeh_1");
	my @tablesToCheck=("dc.event_e_sgeh_raw", "dc.event_e_lte_raw");
	my @dirsToCheck=($remotedir1,$dir2);
	if( !-d "/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/$host"){
			open(TEMP,">/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/tempfile.$$");
			makePath("/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/$host");
		}
    
	runCommand("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh'",1);
	runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $EdeSgehDir /eniq/data/eventdata/events_oss_1'",1);
	
	my $dirlinkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir1 | grep ^l'",0);
	if(!$dirlinkExists){
	    &FT_LOG("ERROR:/eniq/data/eventdata/events_oss_1/sgeh/dir1 doesn't exist on ec_sgeh_1, svcadm restart system/filesystem/autofs will be run now!");
	    executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/home/dcuser/automation/RunCommandAsRoot.sh svcadm restart system/filesystem/autofs 2>/dev/null'");
	    sleep (120);
	}
	if( !-d "$dgNfsPath/$host"){
		open(TEMP,">$dgNfsPath/tempfile.$$");
		print TEMP "temp";
		close TEMP;
		unlink("$dgNfsPath/tempfile.$$");
		makePath("$dgNfsPath/$host");
	}	
	
    
	runCommand("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh/dir2'",1);
	runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $dgNfsPath/$host/50files/ossrc/data/pmMediation/eventData/sgeh/dir1 /eniq/data/eventdata/events_oss_1/sgeh/dir2'",1);
	
	executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
	executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
	
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG00_LogParsing_Inter");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF00_ParsingLog_Inter.logging");
	
	#enable SGEH workflows
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_*");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_Cell_Lookup_Refresh_DB");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.now");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.scheduled");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir*");
	
	
	&FT_LOG("Waiting for 15 mins to load the data for tests.");
	sleep 900;
	my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,40,1,"HIERARCHY_3");
	$result.=$check[1];
	$result.="</TABLE>";
    
	return $result;
	
}


sub runLike4Like
{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
	my $hr_left = 23 - $hour ;
	my $min_left = 59 - $min ;
	my $sec_left = 60 - $sec ;
	my $total_sec_left = $hr_left*3600 + $min_left*60 + $sec_left ;
	my $time_to_wait= $total_sec_left + 3600 ;

	my $sleep_till=localtime($time_to_wait);

	##&FT_LOG("Sleep of $time_to_wait seconds to cross 00:00:00 window before starting Like4Like test cases");
	##sleep($time_to_wait);


	my $feature = shift;
	chomp $feature;
	my $tablename;
	my $teststatus;
	my $reason;
	my $tarFileDir = "/eniq/home/dcuser/automation/Like4Like";
	my $resultDataFileDir = "$tarFileDir/reports/comparison/data/";
	my $resultSchemaFileDir = "$tarFileDir/reports/comparison/schema/";
	my @atomDbDir = ('setup', 'logs', 'reports', 'bin',);
	my $hostnm = `hostname`;
	chomp $hostnm;
	my $currDate = `date "+%Y-%m-%d %H:%M"`;
	my $start_time = `date "+%Y-%m-%d 00:00"`;
	chomp $currDate;
	chomp $start_time;
	
	&FT_LOG("Start time for Like4Like will be: $start_time \n");
	
	
	my $result=qq{
        <h3>Like4Like</h3>
        <TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
           <tr>
                 <th>TEST CASE</th>
                 <th>TEST RESULT</th>
                 <th>FAILURE REASON</th>
           </tr>
        };

###############work around to set like4like start time as current time########################

        like4like_currenttime_setting();

		
########################################################################
		
	# Remove the directories from previous execution in flike4like testing.
	foreach my $atom_dir (@atomDbDir)
	{
		if ( -d "$tarFileDir/$atom_dir")
		{
			executeThisWithLogging("rm -rf $tarFileDir/$atom_dir");
		}
	}

	# Untar the AtomDB tar ball if it is present, else throw error.
	if ( -e "$tarFileDir/AtomDB.tar" )
	{
		executeThisWithLogging("cd $tarFileDir; tar -xf $tarFileDir/AtomDB.tar");
	}
	elsif ( -e "$tarFileDir/AtomDB.tar.gz" )
	{
		executeThisWithLogging("cd $tarFileDir; gunzip $tarFileDir/AtomDB.tar.gz -c | tar xf -");
	}
	else
	{
		&FT_LOG("ERROR: Like4Like tar ball not present under $tarFileDir.");
		exit(1);
	}

	# Copy the executorProperties.txt file.
	executeThisWithLogging("cp $tarFileDir/config_files/executorProperties.txt $tarFileDir/setup/comparison/executorProperties.txt");

	# Update the Current Server property file path in executorProperties.txt file.
	executeThisWithLogging("cat $tarFileDir/setup/comparison/executorProperties.txt | sed s/\"ServerOnePropertyFile.*\"/\"ServerOnePropertyFile=..\\\/setup\\\/database\\\/$hostnm.prop\"/g > /tmp/$hostnm.executorProperties.prop");
	executeThisWithLogging("mv /tmp/$hostnm.executorProperties.prop $tarFileDir/setup/comparison/executorProperties.txt; chmod 777 $tarFileDir/setup/comparison/executorProperties.txt");

	# Update the Start time in the executorProperties.txt file.
	#executeThisWithLogging("cat $tarFileDir/setup/comparison/executorProperties.txt | sed s/StartTime.*/\"StartTime=$start_time\"/g > /tmp/$hostnm.executorProperties.prop");
	executeThisWithLogging("cat $tarFileDir/setup/comparison/executorProperties.txt | sed s/StartTime.*/\"StartTime=$endTime\"/g > /tmp/$hostnm.executorProperties.prop");
	executeThisWithLogging("mv /tmp/$hostnm.executorProperties.prop $tarFileDir/setup/comparison/executorProperties.txt; chmod 777 $tarFileDir/setup/comparison/executorProperties.txt");

	# Update the tablelists file path in the executorProperties.txt file.
	executeThisWithLogging("cat $tarFileDir/setup/comparison/executorProperties.txt | sed s/\"tableListFile.*\"/\"TableListFile=..\\\/setup\\\/comparison\\\/tableLists\\\/${feature}tables.txt\"/g > /tmp/$hostnm.executorProperties.prop");
	executeThisWithLogging("mv /tmp/$hostnm.executorProperties.prop $tarFileDir/setup/comparison/executorProperties.txt; chmod 777 $tarFileDir/setup/comparison/executorProperties.txt");

	
	
	
	# Update the frequency  in the executorProperties.txt file.
	executeThisWithLogging("cat $tarFileDir/setup/comparison/executorProperties.txt | sed s/\"Frequency=.*\"/\"Frequency=15m\"/g > /tmp/$hostnm.executorProperties.prop");
	executeThisWithLogging("mv /tmp/$hostnm.executorProperties.prop $tarFileDir/setup/comparison/executorProperties.txt; chmod 777 $tarFileDir/setup/comparison/executorProperties.txt");
	
	# Update the TestDuration in the executorProperties.txt file.
	executeThisWithLogging("cat $tarFileDir/setup/comparison/executorProperties.txt | sed s/\"TestDuration.*\"/\"TestDuration=15m\"/g > /tmp/$hostnm.executorProperties.prop");
	executeThisWithLogging("mv /tmp/$hostnm.executorProperties.prop $tarFileDir/setup/comparison/executorProperties.txt; chmod 777 $tarFileDir/setup/comparison/executorProperties.txt");
	
	
	# Copy the reference-server.prop file.
	#executeThisWithLogging("cp $tarFileDir/config_files/reference-server.prop $tarFileDir/setup/database/reference-server.prop");

	# Copy the specific tablelist
	executeThisWithLogging("cp $tarFileDir/config_files/${feature}tables.txt $tarFileDir/setup/comparison/tableLists/");	

	# Create the EE property file.
	executeThisWithLogging("echo \"dburl=jdbc:sybase:Tds:$hostnm.athtem.eei.ericsson.se:2640/dwhdb\" > $tarFileDir/setup/database/$hostnm.prop");
	executeThisWithLogging("echo 'user=dc' >> $tarFileDir/setup/database/$hostnm.prop");
	executeThisWithLogging("echo 'password=dc' >> $tarFileDir/setup/database/$hostnm.prop");
	executeThisWithLogging("echo 'driver=com.sybase.jdbc4.jdbc.SybDriver' >> $tarFileDir/setup/database/$hostnm.prop");

	
	
	# Update the Reference Server property file.
	executeThisWithLogging("echo \"dburl=jdbc:sybase:Tds:$referenceServerHostname.athtem.eei.ericsson.se:2640/dwhdb\" > $tarFileDir/setup/database/temp.prop");
	executeThisWithLogging("echo 'user=dc' >> $tarFileDir/setup/database/temp.prop");
	executeThisWithLogging("echo 'password=dc' >> $tarFileDir/setup/database/temp.prop");
	executeThisWithLogging("echo 'driver=com.sybase.jdbc4.jdbc.SybDriver' >> $tarFileDir/setup/database/temp.prop");
	executeThisWithLogging("mv $tarFileDir/setup/database/temp.prop $tarFileDir/setup/database/reference-server.prop; chmod 777 $tarFileDir/setup/database/reference-server.prop");
	
	if (! -d "$tarFileDir/reports/comparison/data")
	{
		executeThisWithLogging("mkdir $tarFileDir/reports/comparison/data");
	}
	if (! -d "$tarFileDir/reports/comparison/schema")
	{
		executeThisWithLogging("mkdir $tarFileDir/reports/comparison/schema");
	}

	### Sleep required if running Like4Like with less data with current time
	&FT_LOG("Sleep of 30 Mins before starting Like4Like test cases");
	sleep 1800;
	

	# Execute the like4like DB Comparision python script.
	executeThisWithLogging("cd $tarFileDir/bin; python ./AtomDbComparisonExecutor.py -f ../setup/comparison/executorProperties.txt -l INFO");

	# Prepare the report for DATA.
	my $resultFile=`ls -lrt $resultDataFileDir/TestSummaryReport*.csv | tail -1 | awk '{print \$NF}' | xargs echo`;
	open(RESULTS, "<$resultFile") or die "ERROR: Cannot open $resultFile";
		
	my @results=<RESULTS>;
        close(RESULTS);
		
	foreach my $line (@results)
	{
		if ( $line =~ m/Table,Test Status,Failure Reason/ )
		{
			next;
		}
		my @testcase = split (/,/, $line);
		$tablename = $testcase[0];
		$teststatus = $testcase[1];
		$reason = $testcase[2];
	
		$result.="<tr><td>DATA: $tablename</td>";
			
		if($teststatus =~ m/FAILED/)
		{
			$result.="<td align=center><font color=red><b>FAIL</b></font></td><td align=center><b>$reason</b></font></td></tr>\n";
		}
		elsif($teststatus =~ m/PASSED/)
		{
			$result.="<td align=center><font color=green><b>PASS</b></font></td><td align=center><b>n/a</b></font></td></tr>\n";
		}
		else
		{
			$result.="<td align=center><font color=blue><b>No Data</b></font></td><td align=center><b>n/a</b></font></td></tr>\n";
		}
		
	}

	# Prepare the report for SCHEMA
	$resultFile=`ls -lrt $resultSchemaFileDir/TestSummaryReport*.csv | tail -1 | awk '{print \$NF}' | xargs echo`;
        open(RESULTS, "<$resultFile") or die "ERROR: Cannot open $resultFile";

	@results=<RESULTS>;
	close(RESULTS);

	foreach my $line (@results)
	{
		if ( $line =~ m/Table,Test Status,Failure Reason/ )
		{
			next;
		}
		my @testcase = split (/,/, $line);
	        $tablename = $testcase[0];
	        $teststatus = $testcase[1];
	        $reason = $testcase[2];

	        $result.="<tr><td>SCHEMA: $tablename</td>";

       		if($teststatus =~ m/FAILED/)
		{
			$result.="<td align=center><font color=red><b>FAIL</b></font></td><td align=center><b>$reason</b></font></td></tr>\n";
		}
		elsif($teststatus =~ m/PASSED/)
		{
			$result.="<td align=center><font color=green><b>PASS</b></font></td><td align=center><b>n/a</b></font></td></tr>\n";
		}
		else
		{
			$result.="<td align=center><font color=blue><b>No Data</b></font></td><td align=center><b>n/a</b></font></td></tr>\n";
		}
	}	
	$result.="</TABLE>";
	return $result;
}


## Function to update the cronfile entry once the data start populating to rn profiler .It will do the flollowing function :

##1. Check for the data in the nothbound and LTE_ES directory  
##2. Update the cron entry to run profiler

sub upadteProfilerCron{


			&FT_LOG("Info:Updating Cron For Profiler to run ...");
			sleep 1800;
				my $dataGenStartTime = "";
				my $timeWarp = "45";
				my $dataGenStartTime = time - (15*60);
				my $numberOFDays = "4";
			    $numberOFDays =`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep NO_OF_DAYS | cut -d ':' -f2`;
				chomp $numberOFDays;
				my $server = (grepFile("controlzone", "/etc/hosts"))[0];
				my @values = split( " ", $server );
				my $cordinator_server = $values[1];
				my @ec_servers=("ec_st_1");
				my @dataGenDir1 =("/eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir1" ,"/eniq/northbound/lte_event_stat_file/events_oss_1/dir18");
				
				my $success=1;
			for(my $i=1;$i<=$timeWarp;$i++){
			&FT_LOG("INFO:$i minutes of a maximum $timeWarp have passed. Checking directories and tables");
			$success=1;
			foreach my $dir(@dataGenDir1){
				my $res = verifyDataGenDir(\@ec_servers,"$dir",$dataGenStartTime,1);
				
				
				if($res=~m/Exception|Fail|Error/){
					$success=0;
					&FT_LOG("INFO:Directories are not yet populating. Continuing to wait");
					last;
				}
			}
			
			if($success){
				&FT_LOG("INFO:Tables and directories are populating ok. Updating the Cron Entry for profiler ");
				last;
			}
			
					sleep 60;

		}
		
		if(!$success){
				&FT_LOG("ERROR: Directories are not populating . Please check it manually . Exiting ...... ");
				exit 127 ;
			}
			else{
				&FT_LOG("INFO:Updating the cron entry to invoke profiler  ");
				&FT_LOG("/eniq/home/dcuser/automation/EST/updateCronsetup.sh   $numberOFDays $cordinator_server  ");
				executeThisWithLogging("/eniq/home/dcuser/automation/EST/updateCronsetup.sh   $numberOFDays $cordinator_server");
			
			}
}


## Function to run EDE for Like4Like Testing . It will do the flollowing function :

##1. Kill the old EDE  processs . 
##2. Truncate the Db table 
##3. Provision the Work FLows . 
##4. Start the EDE 
##5. Run the DBChecker 
##6. Run the The Like4Like

sub runEDELIke4Like{
   

         &FT_LOG("Info:runEDELIke4Like function is called ...");
	
	
	my $destInfoFile = "destIpFoldersInfoList.txt";
	
	my $ec_1_server = (grepFile("ec_1", "/etc/hosts"))[0];
	my $ec_1 = splitAndGetValue($ec_1_server, " ", 3);
	my @values1 = split( " ", $ec_1_server );
	my $ec_ip = $values1[0];
	$like4likeIsRunning = 0 ;
	
	
	foreach my $actionToPerform (@like4likeParam){
	
		chomp($actionToPerform);
		
		if($actionToPerform eq "KILLEDE"){
			&FT_LOG("Info: Killing the Old running EDE instance abd making changes in the UserInput.xml");
			## This section will take the destination IP and edit the same in the destIpFoldersInfoList.txt
			my $destInfo;
			my $cor_server = (grepFile("controlzone", "/etc/hosts"))[0];
			my $cor = splitAndGetValue($cor_server, " ", 3);
			my $ec_server = (grepFile("ec_st_1", "/etc/hosts"))[0];
			my $ec = splitAndGetValue($ec_server, " ", 3);
			my @values = split( " ", $ec_server );
			my $ip = $values[0];
			$destInfo = $ip;
			
		         
			system (" echo $destInfo > /net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/bin/destIpFoldersInfoList.txt " ) ;
	                system (" echo $referenceServer > /net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/bin/referenceServerIp.txt " ) ;
	       
			## Kill the EDE and EDIT the UserInputXML
			
			executeThisWithLogging("./sshTo560Server.sh");
			
			&FT_LOG("copy the IptoFDN mapping file on the $referenceServerHostname ");
			# copy the IptoFDN mapping file on the reference server 
			executeThisWithLogging("./copyIPtoFDNmappingFileToReferenceServer.sh $referenceServer");
            		
			&FT_LOG("copy the IptoFDN mapping  to the server ");			
			# copy the IptoFDN mapping file to the server 
			copy "/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/cfg/user/CTR_FDNSourceIPMapping.txt", "/eniq/mediation_inter/M_E_CTRS/etc/ctrs_ip2fdnmap.txt";
	        }
				
		  ## Truncate thr desired tables on the current running server 
	        if($actionToPerform eq "TRUNCATETABLES"){
			&FT_LOG("Info:Truncating tables on the current ruuning server ");
			print "Truncating tables on the current ruuning server " ;
			executeThisWithLogging (" cd /eniq/home/dcuser/automation/ ;./truncateLikeForLikeTable.pl ") ;
		

			## Truncate thr desired table on the reference server
			print "Truncating tables on the reference  server " ;
			&FT_LOG("Info:Truncating tables on the $referenceServerHostname ");
			executeThisWithLogging("./truncateDBTableOnReferenceServer.sh  $referenceServer");
		}
		     ## this section will create the output and topology directry structure and load topology 
		if($actionToPerform eq "LOADTOPOLOGYFORLTES"){
				&FT_LOG("Info:Loading LTE_ES topolgy on the server ");
				for(my $i=1;$i<51;$i++){
					runCommand("mkdir -p /eniq/data/eventdata/events_oss_1/lteTopologyData/dir$i");
				}
				for(my $i=1;$i<51;$i++){
				runCommand("mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i");
				}
			executeThisWithLogging("cp /net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/topology_for_Like_For_like/* /eniq/data/eventdata/events_oss_1/lteTopologyData/dir1");
			executeThisWithLogging("./prepareReferenceServerForLTES.sh $referenceServer");
		}
		
		 if($actionToPerform eq "RESTARTALLEC"){
			my @ec_st = grepFile("ec_st_1", "/etc/hosts");
			if(@ec_st == 0){
			print "Adding EC_ST_1 and EC_ST_2 pico\n";
			&FT_LOG("Info:Adding EC_ST_1 and EC_ST_2 pico\n ");
			executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pico -add EC_ST_1 EC $ec_ip'");
			executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pico -add EC_ST_2 EC $ec_ip'");
			}
			my $in;
			my $out;
			my @status;
			# Check and update the service_names file with the EC_ST_1 and EC_ST_2 service names
			
			my $addedInFile = 0;
			
			open my $file1,  '<',  "/eniq/sw/conf/service_names"      or die "Can't read file: $!";

			while( <$file1> )   # print the lines before the change
			{
			   if( $_ =~ m/ec_st_1/) {
					 $addedInFile = 1;
				}

			   last if $addedInFile == 1;
			}
		   
			close $in;
			
			if ($addedInFile == 0) {
				 my $lineToAdd1 = "";
				 my $lineToAdd2 = "";
				 open my $in,  '<',  "/eniq/sw/conf/service_names"      or die "Can't read file: $!";
				 open my $out, '>', "/tmp/service_names2"      or die "Can't read file: $!";
				 while( <$in> )   # print the lines before the change
				 {
					 print $out $_;
					 if ( $_ =~ m/ec_1/ ) {
						 $lineToAdd1 = $_;
						 $lineToAdd2 = $_;
						}
					last if( $_ =~ m/ec_1/ );
				 }
				 
				$lineToAdd1 =~ s/ec_1/ec_st_1/g;
				print $out $lineToAdd1;
				
				$lineToAdd2 =~ s/ec_1/ec_st_2/g;
				print $out $lineToAdd2;

				while( <$in> )
				{
					 print $out $_;
				}
			
				close $in;
				close $out;
				copy "/tmp/service_names2", "/eniq/sw/conf/service_names";
			}

				## Start EC_ST_1 and EC_ST_2

				print("INFO:Start up EC_ST_1 & EC_ST_2\n");
			
				executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_1'");
				executeThisWithLogging("ssh dcuser\@ec_st_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_2'");
				executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup ec_lteefa_1'");
				executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup ec_lteefa_2'");
				executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup ec_lteefa_3'");
				executeThisWithLogging("ssh dcuser\@EC_LTEES_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_1'");
				executeThisWithLogging("ssh dcuser\@EC_LTEES_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_2'");
				executeThisWithLogging("ssh dcuser\@EC_LTEES_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_3'");
				executeThisWithLogging("ssh dcuser\@EC_LTEES_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_4'");
			
				my @contents=executeThisQuiet("ssh dcuser\@ec_st_1 'source ~/.profile; ec status'");

			
				#Trying to restart EC_ST, if not up already
				if(grep(/EC_ST_1 is not running/,@contents)){
					print("EC_ST_1/2 is not running. EC_ST_1/2 restart \n");
					executeThisWithLogging("ssh dcuser\@ec_st_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_1'");
					executeThisWithLogging("ssh dcuser\@ec_st_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_ST_2'");
				    
				}
			
				if(grep(/ec_lteefa_1 is not running/,@contents)){
					print("ec_lteefa_1/2 is not running. ec_lteefa_1/2 restart \n");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup ec_lteefa_1'");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup ec_lteefa_2'");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup ec_lteefa_3'");
				   
				}
				 if(grep(/EC_LTEES_1 is not running/,@contents)){
					executeThisWithLogging("ssh dcuser\@EC_LTEES_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_1'");
				    executeThisWithLogging("ssh dcuser\@EC_LTEES_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_2'");
				    executeThisWithLogging("ssh dcuser\@EC_LTEES_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_3'");
				    executeThisWithLogging("ssh dcuser\@EC_LTEES_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_LTEES_4'");
				
			    }
		 
		 }
		
		
		
		if($actionToPerform eq "PROVISIONWORKFLOWS"){
			
		
			#Provision  WorkFlows  CTUM/CTR/LTE_EFA and LTES WorkFlows on the reference server  server 
				
		        print "Initialising workflows on $referenceServerHostname. Please wait it may take some time ... \n";
		        &FT_LOG("Info:Initialising workflows on reference. Please wait it may take some time ...  ");	        
		        executeThisWithLogging("./provisionWorkFlowsOnReferenceServer.sh $referenceServer ");
		        sleep 60;


				
		        &FT_LOG("Info:Initialising workflows on runnning server. Please wait it may take some time ...  ");
			print "Initialising workflows on the running server . Please wait it may take some time ... \n";
			#Provision  WorkFlows  CTUM/CTR/LTE_EFA and LTES WorkFlows on the current server 
			executeThisWithLogging("cd /eniq/home/dcuser/automation/provisioning ; ./auto_provision_LTE.pl ");
				
		        # Wait till all the workflows get started 
				sleep 400;
			 }
			 
			 	 
			 # start the EDE tool on atclvm560
			 

			 if($actionToPerform eq "STARTEDE"){
				print "Starting EDE on atclvm560 server ";
				&FT_LOG("Starting EDE on atclvm560 server");
				executeThisWithLogging("./startEDEonatclvm560.sh");
				# Wait till EDE start sending data to the Server 
				sleep 300;
			 }
			 
			 
			 
			 if($actionToPerform eq "PROVISIONSGEHONREFERENCESERVER"){
				print "Provisining SGEH Work Flows on $referenceServerHostname . Please wait it may take some time ... \n";
				&FT_LOG("Provisining SGEH Work Flows on Reference Server . Please wait it may take some time ... \n");
				executeThisWithLogging("./RegressionOnReferenceServer.sh $referenceServer ");
				sleep 60; 
				
			 }
			 
			 if($actionToPerform eq "DATACHECK"){
			        &FT_LOG("INFO:Starting  Datacheck .........");
				my $ctumDir="/eniq/data/pmdata/eventdata/00/CTRS/ctum/5min";
				my $traceDir="/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir10";
				my @ec_servers=("EC_ST_1","ec_lteefa_1","ec_lteefa_2","ec_lteefa_3");
				my @dirs=($traceDir,$ctumDir);
				my @tables=("dc.event_e_lte_cfa_err_raw","dc.event_e_lte_hfa_err_raw");
				my $tableNames="";
				foreach my $table(@tables){
				$tableNames.="$table,";
			}
				my @check=dataGenCheck(\@ec_servers,\@dirs,\@tables,30,1,"HIER3_ID");
				&FT_LOG("INFO:Will return the status of the relevant workFlows presently");
			 }	
		}

		
	}

sub runESTSgeh{
 
        &FT_LOG("Info:runESTSgeh function is called ...");
	
	## remove the oss mounting 
	runCommand("touch /eniq/connectd/mount_info/events_oss_1/disable_OSS");
	foreach my $actionToPerform (@estParam){
		chomp($actionToPerform);
		 ## Add cron entry of EDE 
		 if($actionToPerform eq "STARTEDE"){
				print "Starting EDE on  server ";
				&FT_LOG("Starting EDE on  server");
				executeThisWithLogging("/eniq/home/dcuser/automation/EST/StartEDEFORSGEH.exp $edeESTserver  $edeESTLocation $edeESTTopology $edeESTInter $edeESTOutput ");
				# Wait till EDE start sending data to the Server 
			 }
		     ## this section will create the output and topology directory structure and load topology 
		if($actionToPerform eq "CREATEDIRECTORYSGEH"){
				&FT_LOG("Info:Creating Directory on Coordintor and MZ blade ");

				## Create and mount the output and topology directory on coordinator
 				runCommand("mkdir -p /eniq/data/eventdata/events_oss_1/sgeh; chmod 777 /eniq/data/eventdata/events_oss_1/sgeh'",1);
				runCommand("/eniq/home/dcuser/automation/RunCommandAsRootEST.sh mount $edeESTserver:$edeESTOutput /eniq/data/eventdata/events_oss_1/sgeh",1);
				runCommand("mkdir -p /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS");
				runCommand("/eniq/home/dcuser/automation/RunCommandAsRootEST.sh mount $edeESTserver:$edeESTTopology  /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS");

				## Create the output directroy on MZ blade 
				 runCommand("ssh dcuser\@ec_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh; chmod 777 /eniq/data/eventdata/events_oss_1/sgeh'",1);
				
				## Mount thhe ouput directory 
				 runCommand("ssh dcuser\@ec_1 '/eniq/home/dcuser/automation/RunCommandAsRootEST.sh mount $edeESTserver:$edeESTOutput /eniq/data/eventdata/events_oss_1/sgeh'",1);

				## Create and mount the mount intermediate location 
				runCommand("ssh dcuser\@ec_1 'mkdir -p /tmp/inter; chmod 777  /tmp/inter'",1);
				runCommand("ssh dcuser\@ec_1 '/eniq/home/dcuser/automation/RunCommandAsRootEST.sh mount $edeESTserver:$edeESTInter /tmp/inter'",1);

		}
		
		if($actionToPerform eq "PROVISIONWORKFLOWSFORESTSGEH"){
			#Provision  SGEH WorkFlows  
		        print "Initialising workflows . Please wait it may take some time ... \n";
		        &FT_LOG("Info:Initialising workflows . Please wait it may take some time ...  ");	        
		        executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/provision_workflows.sh");
		        sleep 60;
			 }	
		}
	}
	
	
sub ec_Movement{
        #Executing EC movement
        &FT_LOG("EC Movement is needed. Running ECMovement Script.\n ");
        print "\nEC Movement is needed. Running ECMovement Script.\n ";
        executeThisWithLogging("/eniq/home/dcuser/automation/EST/ecmovement.exp");
        system("cp /eniq/sw/conf/service_names /tmp/service_names");
        system("cat /eniq/sw/conf/service_names | egrep -vi 'ec_dvtp|ec_lteefa|ec_sgeh' > /tmp/ec_removal");
        system("/usr/bin/rm -rf /eniq/sw/conf/service_names");
        system("usr/bin/mv /tmp/ec_removal /eniq/sw/conf/service_names");

}



sub create_DirectoryLTEES{

		#Creating Directories for EST
		&FT_LOG("Creating Directories for EST\n");
		print "\nCreating Directories for EST\n";

        my $host=`hostname`;
        chomp($host);
        #Creating Directories
        &FT_LOG("Creating Directory Structure on $host\n");
        print"Creating Directory Structure on $host\n";
        system(" mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1; chmod 777 /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1");
        for(my $i=1;$i<51;$i++){
                 system("mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i ; chmod 777 /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i");
        }


        runCommand("ssh dcuser\@ec_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1; chmod 777 /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1'",1);
            for(my $i=1;$i<51;$i++){
                runCommand("ssh dcuser\@ec_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i; chmod 777 /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i'",1);
                }


        runCommand("ssh dcuser\@ec_ltees_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1; chmod 777 /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1'",1);
        for(my $i=1;$i<51;$i++){
                runCommand("ssh dcuser\@ec_ltees_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i; chmod 777 /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir$i'",1);
         }

        #Creating topology directories
        &FT_LOG("Creating topology directories\n");
        print "Creating topology directories\n";
                system("mkdir -p /eniq/data/eventdata/events_oss_1/lteTopologyData");
                runCommand("ssh dcuser\@ec_ltees_1 'mkdir -p /eniq/data/eventdata/events_oss_1/lteTopologyData'");
                runCommand("ssh dcuser\@ec_1 'mkdir -p /eniq/data/eventdata/events_oss_1/lteTopologyData'");


        #Creating northbound directories
        &FT_LOG("Creating Northbound directories\n");
        print "Creating Northbound directories\n";
        for(my $i=1;$i<51;$i++){
                system("mkdir -p /eniq/northbound/lte_event_stat_file/events_oss_1/dir$i");
        }


        for(my $i=1;$i<51;$i++){
                runCommand("ssh dcuser\@ec_ltees_1 'mkdir -p /eniq/northbound/lte_event_stat_file/events_oss_1/dir$i'");
        }

        for(my $i=1;$i<51;$i++){
                runCommand("ssh dcuser\@ec_1 'mkdir -p /eniq/northbound/lte_event_stat_file/events_oss_1/dir$i'");
        }

}


sub runESTonDatagen{

        my $hour;
        my $hoursOfData;
        my $ESTautomationpackage="/eniq/home/dcuser/automation/EST";
        my $automationpackage="/eniq/home/dcuser/automation/";
        open(my $fh, '<', "$automationpackage/ESTconfig.prop") or die "Could not open file";

        my $rep=`cat $automationpackage/ESTconfig.prop | grep NO_OF_DAYS | cut -d ':' -f2`;
        chomp $rep;

        my $hoursOfData=`cat $automationpackage/ESTconfig.prop | grep HOURSOFDATA | cut -d ':' -f2`;
        chomp $hoursOfData;

        my $node=`cat $automationpackage/ESTconfig.prop | grep CTRNODESTOSIMULATE | cut -d ':' -f2`;
        chomp($node);

        my $path=`cat $automationpackage/ESTconfig.prop | grep PATH | cut -d ':' -f2`;
        chomp($path);

        my $datapath=`cat $automationpackage/ESTconfig.prop |  grep Intermediate_DataLocation | cut -d ':' -f2`;
        chomp($datapath);

        my $topologyinter=`cat $automationpackage/ESTconfig.prop | grep Topology_InterLocation | cut -d ':' -f2`;
        chomp($topologyinter);

        my $topologydestination=`cat $automationpackage/ESTconfig.prop | grep Topology_DestinationLocation | cut -d ':' -f2`;
        chomp($topologydestination);

        my $datagen_server=`cat $automationpackage/ESTconfig.prop | grep Datagen_server | cut -d ':' -f2`;
        chomp($datagen_server);

        my $Cordinator_server=`cat $automationpackage/ESTconfig.prop | grep Cordinator_server | cut -d ':' -f2`;

        chomp($Cordinator_server);

        my $datagenip=`/eniq/home/dcuser/automation/RunCommandAsRoot.sh nslookup $datagen_server | sed 's/ //g' | grep Address | cut -d':' -f2 | tail -1`;
        chomp($datagenip);

        #Creating EDE Directory Structure
        &FT_LOG("Creating EDE Directory Structure\n");
		system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh mkdir -p /EST/");
        system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh chown dcuser:dc5000 /EST; chmod 777 /EST");
        system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh cp /net/atclvm560.athtem.eei.ericsson.se/ede/EST/ede_installer_R2A02.zip /EST/");
        system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh unzip /EST/ede_installer_R2A02.zip -d /EST/ede");
        system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh chown dcuser:dc5000 /EST/ede/");
        my $edepackage=`ls /EST/ede`;
        chomp($edepackage);
        $path="/EST/ede/$edepackage/$edepackage";
        system("/eniq/home/dcuser/automation/RunCommandAsRoot.sh chown dcuser:dc5000 /EST/ede/*; chmod 777 /EST/ede/*");
        system("cd /EST/ede/$edepackage;  /eniq/home/dcuser/automation/EST/edeInstall.exp");


        my $dgt=`echo $hoursOfData | wc -m`;
        if($dgt==4){
        $hour  = substr $hoursOfData, 0, 1;
        }
        elsif($dgt==5){
        $hour  = substr $hoursOfData, 0, 2;
        }
        else{
        $hour = 1;
        }

        my $requiredduration= $rep*24;
        print "Required to run EST for $requiredduration hours\n";
        my $repetefactor=$requiredduration/$hoursOfData;
        $repetefactor = floor($repetefactor);
        if(($requiredduration%$hour)!=0)
        {
                $repetefactor+=1;
        }
        
        system(" cp /net/atclvm560.athtem.eei.ericsson.se/ede/EST/UserInputXMLV1.0.xml_standard $path/cfg/user/UserInputXMLV1.0.xml");
        if( -e "$path/cfg/user/UserInputXMLV1.0.xml"){

        my $inputxml="$path/cfg/user/UserInputXMLV1.0.xml";
        my $inputxmlorig="$path/cfg/user/UserInputXMLV1.0.xml_old";
        my $inputxmlnew="$path/cfg/user/UserInputXMLV1.0.xml_new";
        
        system(" touch $inputxmlorig $inputxmlnew");
		system(" chmod 777 $inputxml $inputxmlorig $inputxmlnew\n");

        open(INPUTXML, "+<$inputxml");
        open(INPUTXMLORIG, ">inputxmlorig");
        open(INPUTXMLNEW, "+>>$inputxmlnew");


        my @inputxml = <INPUTXML>;
        my @inputxmlnew = <INPUTXMLNEW>;
        my @inputxmlorig = <INPUTXMLORIG>;
        my $cnt=0;

        foreach my $line(@inputxml){
                if($line=~ m/MatchEventTimeWithSourceData/){
                print INPUTXMLNEW "<MatchEventTimeWithSourceData>YES</MatchEventTimeWithSourceData>\n";
                }
                if($line=~ m/POSTPROCESSING/){
                print INPUTXMLNEW "<Mode factor=\"$repetefactor\" name=\"POSTPROCESSING\" required=\"YES\"/>\n";
                }
                elsif($line=~ m/IntermediateFileLocation/){
                print INPUTXMLNEW "<IntermediateFileLocation>$datapath</IntermediateFileLocation>\n";
                }
                elsif($line=~ m/TempFullTopologyFileLocation/){
                print INPUTXMLNEW "<TempFullTopologyFileLocation>$topologyinter</TempFullTopologyFileLocation>\n";
                }
                elsif($line=~ m/DestinationFullTopologyFileLocation/){
                print INPUTXMLNEW "<DestinationFullTopologyFileLocation>$topologydestination</DestinationFullTopologyFileLocation>\n";
                }
                elsif($line=~ m/<IPAddress>/){
                        if($cnt>=3){
                                print INPUTXMLNEW "$line";
                        }
                        else{
                                print INPUTXMLNEW "\<IPAddress\>$datagenip\<\/IPAddress\>\n";
                                $cnt+=1;
                        }
                }
                else{
                print INPUTXMLNEW "$line";
                }
        }

        close(INPUTXML);
        close(INPUTXMLNEW);
        close(INPUTXMLORIG);
	
	 system("mv $inputxmlnew $inputxml");
        }

        system("sed s///g $path/cfg/user/UserInputXMLV1.0.xml > $path/cfg/user/UserInputXMLV1.0.xml_EST; cp $path/cfg/user/UserInputXMLV1.0.xml_EST $path/cfg/user/UserInputXMLV1.0.xml");


        #Memory settings

        system("/EST/ede/$edepackage/$edepackage/bin/memoryTest.sh>/EST/ede/$edepackage/$edepackage/memoryresult");
        my $freememory=`cat /EST/ede/$edepackage/$edepackage/memoryresult | grep 'Free Mem' | cut -d ':' -f2 | tr -d ' ' | cut -d '.' -f1`;
        print "Available memory is $freememory\n";
        my $mainengine_mem;
        $mainengine_mem=$freememory/3;

        my $controller_mem;
        $controller_mem=$mainengine_mem*2;
        chomp($mainengine_mem);
        chomp($controller_mem);
        $mainengine_mem=floor($mainengine_mem);
        $controller_mem=floor($controller_mem);
#       my $mainengine_mem=25;
#       my $controller_mem=50;



        
        my $inputconfig="$path/cfg/config.properties";
        my $inputconfignew="$path/cfg/config.properties_new";
       
        system(" touch $inputconfignew");
        system(" chmod 777 $inputconfig $inputconfignew\n");

        open(INPUTCONFIG, "+<$inputconfig");
        open(INPUTCONFIGNEW, "+>>$inputconfignew");


        my @inputconfig = <INPUTCONFIG>;
        my @inputconfignew = <INPUTCONFIGNEW>;


	foreach my $line(@inputconfig){
                if($line=~ m/LEAD_TIME/){
                print INPUTCONFIGNEW "LEAD_TIME=1\n";
                }
                elsif($line=~ m/STREAMING_DELAY_IN_SECONDS/){
                print INPUTCONFIGNEW "STREAMING_DELAY_IN_SECONDS=40\n";
                }
                elsif($line=~ m/CONTROLLER_POSTPROCESSING_MEMORY/){
                print INPUTCONFIGNEW "CONTROLLER_POSTPROCESSING_MEMORY=${controller_mem}g\n";
                }
                elsif($line=~ m/MAINENGINE_POSTPROCESSING_MEMORY/){
                print INPUTCONFIGNEW "MAINENGINE_POSTPROCESSING_MEMORY=${mainengine_mem}g\n";
                }
                else{
                print INPUTCONFIGNEW "$line";
                }
        }

        close(INPUTCONFIG);
        close(INPUTCONFIGNEW);

        system("mv $inputconfignew $inputconfig");



        #To get the plumbed IPs

        my $res=system("/eniq/home/dcuser/automation/RunCommandAsRootEST.sh ifconfig -a | grep -w 10.148.64.4");

       
        if($res == 1){
        &FT_LOG("Plumbing the IPs\n");
        print "\nPlumbing the IPs\n";
        executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh /eniq/home/dcuser/automation/plumb.sh");
        }

        my $totalipplumbed=`/eniq/home/dcuser/automation/RunCommandAsRootEST.sh ifconfig -a  | grep broadcast | cut -d ' ' -f2 | wc -l`;
        if ($totalipplumbed >= $node+40 ){
                print " Inside IF!!!!!\n";
                executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRootEST.sh ifconfig -a  | grep broadcast | cut -d ' ' -f2 | head -$node > /tmp/tmp.txt ; mv /tmp/tmp.txt /$path/cfg/user/ctr_ipv4_sourceip.txt ; chmod 755 /$path/cfg/user/ctr_ipv4_sourceip.txt");
                if(! -e "/$path/cfg/user/ctr_ipv4_sourceip.txt"){
                &FT_LOG("Error in plumbing the IPs or redirecting the plumbed IPs to /$path/cfg/user/ctr_ipv4_sourceip.txt.\n");
                }


	
#               Run IPTOFDNMapping
                &FT_LOG("Running IPtoFDN mapping \n");


                executeThisWithLogging("rm $path/cfg/user/CTR_FDNSourceIPMapping.txt ; $path/bin/startEdeTool.sh Generateipfdnmapping");
                if(! -e "$path/cfg/user/CTR_FDNSourceIPMapping.txt"){
                        &FT_LOG("Error in IPtoFDN Mapping\n");
                        exit 0;
                }

        }

        executeThisWithLogging("share $topologydestination");

         for(my $i=1;$i<51;$i++){
                 system("mkdir -p $topologydestination/dir$i");
         }


        executeThisWithLogging("cp $path/cfg/user/CTR_FDNSourceIPMapping.txt $path/cfg/user/ctrs_ip2fdnmap.txt; /eniq/home/dcuser/automation/EST/transfer.sh $Cordinator_server $path/cfg/user/ctrs_ip2fdnmap.txt /eniq/mediation_inter/M_E_CTRS/etc");
	 
}


sub runEST{

        my $datagen_server;
        my $day;
        my $ctrnodes;
        my $help;
        my $pathtodata;
        my $ESTautomationpackage = "/eniq/home/dcuser/automation/EST";
        my $automationpackage = "/eniq/home/dcuser/automation/";
        my $path=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep PATH | cut -d ':' -f2`;
        my $datagentopologyinter=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep Topology_InterLocation | cut -d ':' -f2`;
        my $datagentopologydest=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep Topology_DestinationLocation | cut -d ':' -f2`;
        my $datagenServerHostname=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep Datagen_server | cut -d ':' -f2`;
        my $datagenUser=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep Datagen_User | cut -d ':' -f2`;
        my $datagenPasswd=`cat /eniq/home/dcuser/automation/ESTconfig.prop | grep Datagen_Passwd | cut -d ':' -f2`;
        chomp($datagenServerHostname);
        chomp($datagentopologyinter);
        chomp($datagenUser);
        chomp($datagenPasswd);
        chomp($datagentopologydest);

        my $lines=`cat /eniq/home/dcuser/automation/ESTconfig.prop | wc -l | awk '{print \$NF}'`;
        chomp $lines;
        $lines -=1;
        system("cat /eniq/home/dcuser/automation/ESTconfig.prop | head -$lines > /eniq/home/dcuser/automation/ESTconfig.prop_tmp; mv /eniq/home/dcuser/automation/ESTconfig.prop_tmp /eniq/home/dcuser/automation/ESTconfig.prop");

        #Plumbing IP on MZ
        runCommand("ssh dcuser\@ec_st_1 '/eniq/home/dcuser/automation/RunCommandAsRootEST.sh ifconfig bnxe4 plumb 10.148.64.2 netmask 255.255.192.0 up'");

        &FT_LOG("IP plumbed on MZ\n");

        &FT_LOG("Logging to the Datagen Server\n");
        print "Logging to the Datagen Server\n";
        print ("$ESTautomationpackage/transfer.sh $datagenServerHostname $automationpackage/ESTconfig.prop $ESTautomationpackage\n");
        executeThisWithLogging("$ESTautomationpackage/transfer.sh $datagenServerHostname $automationpackage/ESTconfig.prop $ESTautomationpackage");
	executeThisWithLogging("touch $automationpackage/EST/EST.txt\n");
        executeThisWithLogging("$ESTautomationpackage/login.sh $datagenServerHostname $datagenUser $datagenPasswd");

        print "Mounting on Cordinator server\n";
        &FT_LOG("Mounting on Cordinator server\n");

        executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh mount $datagenServerHostname:$datagentopologydest /eniq/data/eventdata/events_oss_1/lteTopologyData");
        runCommand("ssh dcuser\@ec_1 '/eniq/home/dcuser/automation/RunCommandAsRoot.sh mount $datagenServerHostname:$datagentopologydest/eniq/data/eventdata/events_oss_1/lteTopologyData'",1);
        runCommand("ssh dcuser\@ec_ltees_1 '/eniq/home/dcuser/automation/RunCommandAsRoot.sh mount $datagenServerHostname:$datagentopologydest /eniq/data/eventdata/events_oss_1/lteTopologyData'",1);
}




sub provisionWorkflowsEST{

        &FT_LOG("Checking whether EC's are up\n");
        my $ec_ltees=`cat /etc/hosts | grep ec_ltees | head -1 | cut -d ' ' -f5`;


        my @contents=executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; ec status; exit'");
        push @contents,executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; ec status;exit'");

        if(grep(/EC_LTEES_\d is not running/,@contents))
        {
                &FT_LOG("EC_LTEES is down...Bringing it up\n");
                executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEES_1 '");
                executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEES_2 '");
                executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEES_3 '");
                executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/drstartup  EC_LTEES_4 '");


        }elsif(grep(/EC1 is not running/,@contents)){
                &FT_LOG("EC1 is not running...Bringing it up");
                executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startupEC1'");
        }
        else{
                &FT_LOG("all EC's are up and running!!!!\n");
        }

        #checking for EC after restart
        my @contents=executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; ec status; exit'");
        push @contents,executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; ec status;exit'");

        if(grep(/is not running/,@contents)){
        &FT_LOG("EC's are not coming up. Bring them up manually. Exiting...\n");
		exit 0;
        }
        print "\nLTEES provisioning\n";
		&FT_LOG("\nLTEES Provisioning\n");
        system("/eniq/home/dcuser/automation/provisioning/auto_provision_LTEES_EST.pl");
        my $automationpackage="/eniq/home/dcuser/automation";
        my $datagen_server=`cat $automationpackage/ESTconfig.prop | grep Datagen_server | cut -d ':' -f2`;
        chomp($datagen_server);

        #Adding CRON entry to run EDE
		&FT_LOG("\nAdding CRON Entry for EST\n");
		system("/eniq/home/dcuser/automation/EST/EDEcron.sh $datagen_server");
}

sub addCrontabEST{
		
	my $edepackage=`ls /EST/ede`;
        chomp($edepackage);
        my $path="/EST/ede/$edepackage/$edepackage";
        my ($nsec,$nmin,$nhour,$nday,$nmon) = localtime(time() + 60*10);
        $nmon+=1;
        system("/eniq/home/dcuser/automation/RunCommandAsRootCrontab.sh crontab -l | grep -v /EST/ede  > /eniq/home/dcuser/automation/crontab.txt");
        my $hostserver=`hostname`;
        system("sed '1,2d' </eniq/home/dcuser/automation/crontab.txt > /eniq/home/dcuser/automation/crontab.txt_tmp; cat /eniq/home/dcuser/automation/crontab.txt_tmp | sed s///g >/eniq/home/dcuser/automation/crontab.txtnew");
       
        chomp($path);
        system("echo '$nmin $nhour $nday $nmon * $path/bin/startEdeTool.sh POSTPROCESSING START >/dev/null 2>&1' >> /eniq/home/dcuser/automation/crontab.txtnew");
        
        system("/eniq/home/dcuser/automation/RunCommandAsRootCrontab.sh crontab /eniq/home/dcuser/automation/crontab.txtnew");
		#executeThisWithLogging("rm $automationpackage/EST/EST.txt");

}

sub runGritTool{

	my $feature_rule_file=shift;
	chomp($feature_rule_file);
	print "Rule is : $feature_rule_file\n";
	my $ruleTag = `echo $feature_rule_file | cut -f1 -d'.'`;
	chomp($ruleTag);
	
	my $result;
	my $gritPath="/eniq/home/dcuser/automation/GRIT";
	my $javaPath="/eniq/sw/runtime/jdk/bin/java";
	my $host=hostname;
	my $newHost;
	if($host eq "eniqe"){
		$newHost = $host.".vts.com";
	}
	else {
		$newHost = $host.".athtem.eei.ericsson.se";
	}
	chomp($newHost);
	my $feature_directory;
	my $featuregritpath;
	my $gritResultPath;
	my @featureInputDirs;
	my $rawTable;
	my $testFeature;
	my $fileName;
	my $compareRuleDate;
	my $runOtherSqlFileFlag;
	my $fullDate;
	my @fileNameString;
	my $fileDateStr;
	my @rawTableEventCount;
	my $changeTimeScript;
	
	&FT_LOG("Grit is running for feature : $feature\n");
	if(($feature =~ /LTECFA/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="lte_cfa";
		@featureInputDirs=("/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1");
		$rawTable="event_e_lte_cfa_err_raw";
		$testFeature="CTRS";
		$runOtherSqlFileFlag="YES";
	}
	elsif(($feature =~ /CFAERAB/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="lte_cfa_erab";
		@featureInputDirs=("/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1");
		$rawTable="event_e_lte_cfa_array_erab_err_raw";
		$testFeature="CTRS";
		$runOtherSqlFileFlag="YES";
	}
	elsif(($feature =~ /LTEHFA/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="lte_hfa";
		@featureInputDirs=("/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1");
		$rawTable="event_e_lte_hfa_err_raw";
		$testFeature="CTRS";
		$runOtherSqlFileFlag="YES";
	}
	elsif(($feature =~ /4GSGEHERR/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="4g_sgeh";
		@featureInputDirs=("/eniq/data/eventdata/events_oss_1/sgeh/dir1");
		$rawTable="event_e_lte_err_raw";
		$testFeature="SGEH";
		$runOtherSqlFileFlag="NO";
	}
	elsif(($feature =~ /4GSGEHEXTENDED/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="4g_sgeh_extended";
		@featureInputDirs=("/eniq/data/eventdata/events_oss_1/sgeh/dir1");
		$rawTable="event_e_lte_err_extended_raw";
		$testFeature="SGEH";
		$runOtherSqlFileFlag="NO";
	}
	elsif(($feature =~ /LTERF/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="lte_rf";
		@featureInputDirs=("/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1","/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa_rf/5min/dir1");
		$rawTable="event_e_lte_cfa_array_erab_err_raw";
		$testFeature="CTRS";
		$runOtherSqlFileFlag="YES";
	}
	elsif(($feature =~ /WCDMA/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="wcdma_cfahfa";
		$testFeature="GPEH";
		$runOtherSqlFileFlag="NO";
	}
	elsif(($feature =~ /4GSGEHIMSI/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="4g_imsi";
		@featureInputDirs=("/eniq/data/eventdata/events_oss_1/sgeh/dir1");
		$rawTable="event_e_lte_imsi_suc_raw";
		$testFeature="SGEH";
		$runOtherSqlFileFlag="NO";
	}
	elsif(($feature =~ /4GSGEHAGGREGATION/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="4g_aggregation";
		@featureInputDirs=("/eniq/data/eventdata/events_oss_1/sgeh/dir1");
		if(grep(/SUC/,$feature_rule_file)){
			$rawTable="event_e_lte_suc_raw";
		}
		else{
			$rawTable="event_e_lte_err_raw";
		}
		$testFeature="SGEH";
		$runOtherSqlFileFlag="NO";
	}
	elsif(($feature =~ /CELLTRAFFIC/) && ($feature_rule_file ne "CTUM")){
		$feature_directory="lte_cfa_celltraffic";
		@featureInputDirs=("/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1");
		$rawTable="event_e_lte_cfa_cell_traffic_report_raw";
		$testFeature="CTRS";
		$runOtherSqlFileFlag="NO";
	}
	elsif($feature_rule_file eq "CTUM"){
		$feature_directory="ctum";
		@featureInputDirs=("/eniq/data/pmdata/eventdata/00/CTRS/ctum/5min");
		$testFeature="CTUM";
		$runOtherSqlFileFlag="YES";
	}
	
	$featuregritpath="$gritPath/$feature_directory";
	my $schemaFilepath="$gritPath/$feature_directory/schemaFile";
	my $gritInputFilePath="$gritPath/$feature_directory/gritInputFiles";
	my $mzInputFilePath="$gritPath/$feature_directory/mzInputFiles";	
	
	if($gritToolFlag eq "1"){
		#Check existing python version
		my $requiredPythonVersion = "Python 3.1.2";
		my $pythonVersion = qx(python -V);
		chomp($pythonVersion);
		
		#Updating python version
		if(parseVersion($pythonVersion) < parseVersion($requiredPythonVersion)){
			&FT_LOG("Updating pyhton version\n");
			executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh ln -sf /usr/local/bin/python3.1 /usr/bin/python");
		}
	
		#Copy GRIT package from datagen server to execute GRIT tool and unzip it
		if( !-d "$gritPath"){		
			executeThisWithLogging("rm -rf $gritPath");
			executeThisWithLogging("cp -R /net/atclvm559.athtem.eei.ericsson.se/package/GRIT /eniq/home/dcuser/automation");
			executeThisWithLogging("chmod -R 777 $gritPath");
			my $gritZipPackage = ` ls $gritPath/*.zip`;
			executeThisWithLogging("cd $gritPath; unzip -o $gritZipPackage");
		}
	}
	
	$gritResultPath="/eniq/home/dcuser/automation/grit_results/$feature_directory";
	if(!-d "$gritResultPath"){
		executeThisWithLogging("mkdir -p $gritResultPath; chmod -R 777 $gritResultPath");
	}
	
	if($feature !~ /WCDMA/){
		$fileName = `ls $gritPath/$feature_directory/mzInputFiles | head -1`;
		print "File : $fileName\n";
		chomp($fileName);
		if($feature !~ /4GSGEH/){
			@fileNameString = split /\./, $fileName;
			$fileDateStr = $fileNameString[0];
		}
		else{
			@fileNameString = split /\_/, $fileName;
			my $filedateString = $fileNameString[1];
			my @fileDateString = split /\./, $filedateString;
			$fileDateStr = $fileDateString[0];
		}
	
		$fullDate = substr($fileDateStr, 1, 8);
		my $fileYear = substr($fileDateStr, 1, 4);
		my $fileMonth = substr($fileDateStr, 5, 2);
		my $fileDate = substr($fileDateStr, 7, 2);		
		$compareRuleDate="$fileYear-$fileMonth-$fileDate\n";
		print "COMPARE DATE :: $compareRuleDate\n";
	}
	else{
		my $currentDate = fileNameDate();
		chomp($currentDate);
		my $year = substr($currentDate, 0, 4);
		my $mon = substr($currentDate, 4, 2);
		my $mday = substr($currentDate, 6, 2);
		$compareRuleDate= $year."-".$mon."-".$mday;
		chomp($compareRuleDate);
		print "COMPARE DATE ::: $compareRuleDate\n";
		&FT_LOG("The compare rule date is $compareRuleDate\n");
	}
	
	if($gritToolFlag eq "1"){
		###### For CSV generation on radiator page
		$gritcsvlogs = "Full_CSV_$feature.txt";
	    executeThisWithLogging("rm -rf /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
       	executeThisWithLogging("touch /eniq/home/dcuser/automation/Full_CSV_$feature.txt");

		##### Special settings for 4GSGEH IMSI feature
		if($feature =~ /4GSGEHIMSI/ || $feature =~ /4GSGEHAGGREGATION/ || $feature =~ /4GSGEHEXTENDED/){
			####Unmounting and removing the sgeh directory
			&FT_LOG("Running special setting done for 4GSGEH");
			&FT_LOG("Removing the mounting of ede server");
			executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh umount /net/atclvm560.athtem.eei.ericsson.se/ede");
			executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh'");
			###Truncating the table
			#my $count = 0;
			print "featuregritpath = $featuregritpath\n";
			if($feature =~ /4GSGEHAGGREGATION/){
				opendir(RULEDIR,"$featuregritpath/rulesFiles/");
				while (my $file = readdir(RULEDIR)){

					print "ruleFile is $file\n\n";
					my $truncateTable = `echo '$file' |sed -e 's/.rules//g' | sed -e 's/Rule.*[0-9]_//g'`;
					print "truncate Table is $truncateTable\n\n";
					chomp($truncateTable);
					my $tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like '$truncateTable%'";
					print $tables_query;
					my @truncate_queries = sqlSelect($tables_query);
					my @truncate_queries=(@truncate_queries);
					for my $truncate_query (@truncate_queries) {
						print "Truncate Query: $truncate_query\n";
						sqlDelete($truncate_query);
					}
				}

			}else{
				&FT_LOG("Running GRIT for $feature");
				&FT_LOG("truncating the $rawTable table");
				###Truncating the table
				my $tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like '$rawTable%'";
				my @truncate_queries = sqlSelect($tables_query);
				my @truncate_queries2=(@truncate_queries);
				for my $truncate_query (@truncate_queries2) {
					print "Truncate Query: $truncate_query\n";
					sqlDelete($truncate_query);
				}
				sqlDelete("delete from event_e_lte_err_dubcheck");
				sqlDelete("delete from event_e_lte_err_extended_dubcheck");
			}
			close(RULEDIR);
			## creating the directory again
			executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir1'");
			if($feature =~ /4GSGEHIMSI/){
				&FT_LOG("Changing the parameter of Success handling");
				&FT_LOG("Glassfish and ec_sgeh_1 will be restart");
				###Changing the IMSI settings and restarting the ec_sgeh and glassfish
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh shutdown EC_SGEH_1' ");
				runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish stop '",1);
				system("perl -i -pe 's/SUCCESS_DATA_HANDLING.*/SUCCESS_DATA_HANDLING=AGGREGATES/g' /eniq/mediation_inter/M_E_SGEH/etc/configuration.prop");
				my @successParameter=executeThisWithLogging("cat /eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml| grep 'ENIQ_EVENTS_SUC_RAW'");
				if(grep(/false/,@successParameter))
				{
					&FT_LOG("The parameter value of ENIQ_EVENTS_SUC_RAW is already false");
				}
				else{
					system("perl -i -pe 's/ENIQ_EVENTS_SUC_RAW\" value=\"true\"/ENIQ_EVENTS_SUC_RAW\" value=\"false\"/g' /eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml");
					&FT_LOG("The parameter value of ENIQ_EVENTS_SUC_RAW is set to false");
				}
				runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish start '",1);
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh startup EC_SGEH_1' ");
				### Checking that glassfish and ec_sgeh is atrted successfully.
				&FT_LOG("Checking the status of glassfish");
				my $glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
				if($glassfishStatus == 1)
				{
					&FT_LOG("Glassfish is Online");
				}
				else
				{
					&FT_LOG("Glassfish is Offline");
					exit 1;
				}
				my $ecSgehStatus=runCommand("ssh dcuser\@engine ' source ~/.profile; svcs -a | grep engine | grep online'",0);
				if($ecSgehStatus == 1)
				{
					&FT_LOG("EC_sgeh_1 is Online");
				}
				else
				{
					&FT_LOG("ec_sgeh_1 is Offline");
					exit 1;
				}
			}
			elsif ($feature =~ /4GSGEHAGGREGATION/ || $feature =~ /4GSGEHEXTENDED/)
			{
				changingIMSI_Setting_To_deafult();
			}
				
			#### Provisioning the worflows
			&FT_LOG("Provisioning the workflows after ec start");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG00_LogParsing_Inter");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF00_ParsingLog_Inter.logging");
	
			####enable SGEH workflows
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_*");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_Cell_Lookup_Refresh_DB");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.now");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.scheduled");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir*");
			
			if($feature !~ /4GSGEHEXTENDED/){
				## Removing any extra files for mzInput directory and Grit Input directory
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/home/dcuser/automation/GRIT/$feature_directory/mzInputFiles/*' ");
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/home/dcuser/automation/GRIT/$feature_directory/gritInputFiles/*' ");
			
				### copying the current file from 560 server
				&FT_LOG("Copying the file from the 560 server to mzInput files directory in GRIT");
				my $datefile = `date "+%Y%m%d%H%M"`;
				print "My date is $datefile";
				chomp($datefile);
				my $dir_sgeh = $datefile."_processed";
				print "DIR SGEH IS $dir_sgeh\n";
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'cp /net/atclvm560.athtem.eei.ericsson.se/edefiles/AtomDB/inter/temp/$dir_sgeh/* $gritPath/$feature_directory/mzInputFiles/' ");
				system("ls /eniq/home/dcuser/automation/GRIT/$feature_directory/mzInputFiles/*");
				if($? != 0){
					&FT_LOG("Copying failed because ede is not running. Copying again after 4 minutes of wait.");
					sleep 240;
					my $datefile = `date "+%Y%m%d%H%M"`;
					print "My date is $datefile";
					chomp($datefile);
					my $dir_sgeh = $datefile."_processed";
					executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'cp /net/atclvm560.athtem.eei.ericsson.se/edefiles/AtomDB/inter/temp/$dir_sgeh/* $gritPath/$feature_directory/mzInputFiles/' ");

				}
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'cp $gritPath/$feature_directory/mzInputFiles/* $gritPath/$feature_directory/gritInputFiles/' ");
				executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'chmod 777 $mzInputFilePath/* $gritInputFilePath/*'");
				my $filename_sgeh = `ls /eniq/home/dcuser/automation/GRIT/$feature_directory/gritInputFiles/ | sed 's/^.*_//g'`;
				system("mv /eniq/home/dcuser/automation/GRIT/$feature_directory/gritInputFiles/* /eniq/home/dcuser/automation/GRIT/$feature_directory/gritInputFiles/$filename_sgeh");			
			}
		}
		
		if($feature =~ /LTERF/){
			####Unmounting and removing the CTRS directory
			&FT_LOG("Running GRIT for LTE RF Using EDE");
			&FT_LOG("Removing the mounting of ede server and truncating the EVENT_E_LTE_CFA_ARRAY_ERAB_ERR_RAW table");
			executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh umount /net/atclvm560.athtem.eei.ericsson.se/ede");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS'");
			###Truncating the table
			my $lterf_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_LTE_CFA_ARRAY_ERAB_ERR_RAW%'";
			my @lterf_truncate_queries = sqlSelect($lterf_tables_query);
			my @truncate_queries=(@lterf_truncate_queries);
			for my $truncate_query (@truncate_queries) {
				print "Truncate Query: $truncate_query\n";
				sqlDelete($truncate_query);
			}
			## creating the directory again
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_cfa_rf/5min/dir1'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/ctum/5min");
						
			## Removing any extra files for mzInput directory and Grit Input directory
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'rm -rf /eniq/home/dcuser/automation/GRIT/lte_rf/mzInputFiles/*' ");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'rm -rf /eniq/home/dcuser/automation/GRIT/lte_rf/gritInputFiles/*' ");
			
			&FT_LOG("INFO:Enable relevant WFs and WFGroups by invoking provision_workflow.sh");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
			
			### copying the current file from 560 server
			&FT_LOG("Copying the file from the 560 server to mzInput files directory in GRIT");
			my $utcdate = `date -u '+%Y%m%d%H%M'`;
			chomp($utcdate);
			print "utcdate :: $utcdate\n";
			my $min = `date +%M`;
			chomp($min);
			print "min :: $min\n";
			my $modmin = $min%5;
			chomp($modmin);
			my $datefile = $utcdate - $modmin;

			chomp($datefile);
			my $dir_lteefa = $datefile."_processed";
			print "Processed Directory is $dir_lteefa\n";
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'cp /net/atclvm560.athtem.eei.ericsson.se/ede/data/inter/ctr/temp/$dir_lteefa/* $gritPath/$feature_directory/mzInputFiles/' ");
			system("ls /eniq/home/dcuser/automation/GRIT/lte_rf/mzInputFiles/*");
			if($? != 0){
				&FT_LOG("Copying failed because ede is not running. Copying again after 5 minutes of wait.");
				sleep 300;
				executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'cp /net/atclvm560.athtem.eei.ericsson.se/ede/data/inter/ctr/temp/$dir_lteefa/* $gritPath/$feature_directory/mzInputFiles/' ");
			}
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'cp $gritPath/$feature_directory/mzInputFiles/* $gritPath/$feature_directory/gritInputFiles/' ");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'chmod 777 $mzInputFilePath/* $gritInputFilePath/*'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'gunzip -f $gritInputFilePath/*'");
			my @gritFiles = `ls $gritInputFilePath`;
			foreach my $gritFile(@gritFiles){
				&FT_LOG("unzip each file present at $gritInputFilePath and rename it");
				chomp($gritFile);
				print "File :: $gritFile\n";
				my $newFileName = `echo $gritFile | cut -f3 -d"="`;
				chomp($newFileName);			
				executeThisWithLogging("cd $gritInputFilePath; mv $gritFile $newFileName");
			}
		}
		
		if($feature =~ /CELLTRAFFIC/){
			####Unmounting and removing the CTRS directory
			&FT_LOG("Running GRIT for LTE RF Using EDE");
			&FT_LOG("Removing the mounting of ede server and truncating the EVENT_E_LTE_CFA_CELL_TRAFFIC_REPORT_RAW table");
			executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh umount /net/atclvm560.athtem.eei.ericsson.se/ede");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS'");
			###Truncating the table
			my $lterf_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_LTE_CFA_CELL_TRAFFIC_REPORT_RAW%'";
			my @lterf_truncate_queries = sqlSelect($lterf_tables_query);
			my @truncate_queries=(@lterf_truncate_queries);
			for my $truncate_query (@truncate_queries) {
				print "Truncate Query: $truncate_query\n";
				sqlDelete($truncate_query);
			}
			## creating the directory again
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/lte_cfa_rf/5min/dir1'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/CTRS/ctum/5min");
			
			&FT_LOG("INFO:Enable relevant WFs and WFGroups by invoking provision_workflow.sh");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
			
			##Copy topology file to enrich parameters
			&FT_LOG("INFO:Copy topology file at /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS");
			my $topologyFile = `ls $featuregritpath/sqlFiles/*.xml`;
			chomp($topologyFile);
			executeThisWithLogging("cp $topologyFile /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS");
			executeThisWithLogging("chmod 777 /eniq/data/eventdata/eniq_events_topology/lte/topologyData/ERBS/*");
			&FT_LOG("INFO:wait for 10  minutes to load topology file");
			sleep 600;
		}
		
		if($feature !~ /WCDMA/){
			if($feature_rule_file ne "CTUM"){
				@rawTableEventCount=sqlSelect("SELECT count(*) from $rawTable where date_id='$compareRuleDate'");
				my $count = @rawTableEventCount[0];
				print "RAW TABLE COUNT : $count\n";
				if($count == '0'){
					if($feature !~ /4GSGEH/){
						foreach my $featureInputDir(@featureInputDirs){
							executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'mkdir -p $featureInputDir' ");
							executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cp $gritPath/$feature_directory/mzInputFiles/* $featureInputDir' ");
							executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cd $featureInputDir; chmod 777 *'$fullDate'* changeFileTime.pl' ");
							executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cd $featureInputDir; ./changeFileTime.pl' ");
							executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cd $featureInputDir; rm changeFileTime.pl' ");
					}
					&FT_LOG("Wait for 15 minutes to load the file into DB");
					sleep 900;
					}
					else{
						foreach my $featureInputDir(@featureInputDirs){
							executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'mkdir -p $featureInputDir' ");
							executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'cd $featureInputDir; cp $gritPath/$feature_directory/mzInputFiles/*.bin $featureInputDir' ");
							executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'cd $featureInputDir; chmod 777 *'$fullDate'* ' ");
						}
						&FT_LOG("Wait for 15 minutes to load the file into DB");
						sleep 900;
					}
				}
			}
			else{
				print "Running for CTUM\n";
				foreach my $featureInputDir(@featureInputDirs){
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'mkdir -p $featureInputDir' ");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cp $gritPath/$feature_directory/mzInputFiles/* $featureInputDir ' ");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cd $featureInputDir; chmod 777 *'$fullDate'* ctumFileTimeChange.pl ' ");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cd $featureInputDir; ./ctumFileTimeChange.pl ' ");
					executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'cd $featureInputDir; rm ctumFileTimeChange.pl ' ");
				}
				&FT_LOG("Wait for 15 minutes to load the file into DB");
				sleep 900;
			}
		}
		#Upadating database server
		&FT_LOG("--------------Upadting db.cfg file--------------------");		
		my $dbConfigFile = "$gritPath/etc/db.cfg";
		my $setupHost=`cat $dbConfigFile | grep "hostName =" | cut -f2 -d'='`;
		my $dbFilePassword=`cat $dbConfigFile | grep "password ="`;
		chomp($setupHost);
		chomp($dbFilePassword);
		my $newpswd = "password = "."$temp_pwd";
		chomp($newpswd);
		print "SETUP HOST :: $setupHost\n";
		executeThisWithLogging("perl -pi -e 's/$setupHost/$newHost/g' $gritPath/etc/db.cfg");
		executeThisWithLogging("perl -pi -e 's/$dbFilePassword/$newpswd/g' $gritPath/etc/db.cfg");
		executeThisWithLogging("cp $featuregritpath/new_table.cfg $gritPath/etc/new_table.cfg");

		if($feature !~ /4GSGEHAGGREGATION/){
			#Parsing schema and input rop file
			&FT_LOG("--------------parsing schema file and creating schema sql file-------------------------");
			my @schemafiles = ` ls $schemaFilepath/*.xml`;
			foreach my $schemafile (@schemafiles){
				&FT_LOG("Loading $testFeature File  :: $schemafile");
				executeThisWithLogging("cd $schemaFilepath; $javaPath -jar $gritPath/Grit.jar -a 10dash -s $testFeature -f $testFeature -t $schemafile");
			}
	
			&FT_LOG("--------------parsing rop file and creating rop sql file------------------------");
			my @files = ` ls $gritInputFilePath/*.bin`;
			foreach my $file (@files){
				&FT_LOG("Loading File $file");
				executeThisWithLogging("cp $schemaFilepath/$testFeature.gz $gritInputFilePath");
				executeThisWithLogging("cd $gritInputFilePath; $javaPath -jar $gritPath/Grit.jar action=raw schema=$testFeature inFile=$file");
			}

			#creating grit table and inserting values
			&FT_LOG("-------------Creating grit table for each event-------------------");
			my $schemasqlfile = ` ls $schemaFilepath/*.sql`;
		
			#Deleting grit table if exists
			executeThisWithLogging("cd $schemaFilepath; $javaPath -jar $gritPath/Grit.jar action=cleanup db=$gritPath/etc/db.cfg file=$schemasqlfile");
			executeThisWithLogging("cd $schemaFilepath; $javaPath -jar $gritPath/Grit.jar action=sql db=$gritPath/etc/db.cfg file=$schemasqlfile");
		
			&FT_LOG("----------------Inserting values in event grit table-------------------");	
			my @files = ` ls $gritInputFilePath/*.sql`;
			foreach my $file (@files){
				&FT_LOG("Loading File $file");
				executeThisWithLogging("cd $gritPath; $javaPath -jar $gritPath/Grit.jar action=sql db=$gritPath/etc/db.cfg file=$file");
	
			}

			if ($runOtherSqlFileFlag eq "YES") {
				&FT_LOG("----------------Inserting  other values in event grit table-------------------");
				my @sqlFiles = `ls $featuregritpath/sqlFiles/*.sql`;
				foreach my $sqlFile(@sqlFiles){
					&FT_LOG("Loading sql File $sqlFile");
					executeThisWithLogging("cd $featuregritpath; $javaPath -jar $gritPath/Grit.jar action=sql db=$gritPath/etc/db.cfg file=$sqlFile");
				}
			}
		}
	}
	
	if($feature_rule_file ne "CTUM"){
		$gritToolFlag = '0';
		my $outTable;
		my $inTable;
		if($feature !~ /4GSGEHAGGREGATION/){
			&FT_LOG("Updating the new_table.cfg");
			$inTable = `echo '$feature_rule_file' | cut -f2,3 -d"_"`;
			$outTable = `echo '$feature_rule_file' | cut -d '_' -f4- | cut -f1 -d"."`;
		}
		else{
			if (grep(/SUC/,$feature_rule_file)){
				$inTable = "EVENT_E_LTE_SUC_RAW";
			}
			else{
				$inTable = "EVENT_E_LTE_ERR_RAW";
			}
			$outTable = $feature_rule_file;
			$outTable =~ s/.rules//;
		}
		chomp($inTable);
		chomp($outTable);
		my $replaceTable = `cat $gritPath/etc/new_table.cfg  | grep "EVENT" | grep -v "#" | head -1`;
		chomp($replaceTable);
		my ($replaceTable1,$replaceTable2) = split(/=/,$replaceTable);
		chomp($replaceTable1);
		chomp($replaceTable2);
		
		executeThisWithLogging("perl -pi -e 's/$replaceTable1/$inTable/g' $gritPath/etc/new_table.cfg");
		executeThisWithLogging("perl -pi -e 's/$replaceTable2/$outTable/g' $gritPath/etc/new_table.cfg");
		
		&FT_LOG("--------------Executing rule file-----------------");
		system("mkdir -p $gritResultPath/CSV");
		system("touch $gritResultPath/CSV/$feature_rule_file.csv");
		&FT_LOG("Feature Rule File :  $feature_rule_file");
		if(($feature =~ /4GSGEHIMSI/) || ($feature =~ /LTERF/) || ($feature =~ /CELLTRAFFIC/) || ($feature =~ /4GSGEHEXTENDED/) || ($feature =~ /4GSGEHAGGREGATION/)){
			&FT_LOG("For $feature: Executing the rules Files");
			print "executing extended rules without compare date\n";
			executeThisWithLogging("cd $featuregritpath/rulesFiles; $javaPath -jar $gritPath/Grit.jar action=grit -t $gritPath/etc/new_table.cfg -d $gritPath/etc/db.cfg -r $feature_rule_file -csv $gritResultPath/CSV/$feature_rule_file.csv -v -e -N > $gritResultPath/$feature_rule_file.txt");
		}
		else{
			&FT_LOG("For $feature: Executing the rules Files");
			executeThisWithLogging("cd $featuregritpath/rulesFiles; $javaPath -jar $gritPath/Grit.jar action=grit -t $gritPath/etc/new_table.cfg -d $gritPath/etc/db.cfg -r $feature_rule_file -csv $gritResultPath/CSV/$feature_rule_file.csv -v -e -D '$compareRuleDate' > $gritResultPath/$feature_rule_file.txt");	
		}

		##############Printing HTML PAGE############
        my @sampleFile = `cat /eniq/home/dcuser/automation/gritcsv_template`;
        my $csvdir = "$gritResultPath/CSV";
		my $file = "$feature_rule_file.csv";
		executeThisWithLogging("cat $csvdir/$file >> /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
        executeThisWithLogging("echo '' >> /eniq/home/dcuser/automation/Full_CSV_$feature.txt");
        my $csvfile = "$csvdir/$file";
        open(GRITCSV, $csvfile) || die "Can not open file $csvfile";
        my @Lines=<GRITCSV>;
        close(GRITCSV);
		if($feature !~ /4GSGEHAGGREGATION/){
			$file =~ s/Rule_//;
			$file =~ s/_[A-Z].*//;
		}
		
        $file =~ s/.csv//;
		$result.=qq{
			<h3>Event_$file</h3>
			<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
			<th>Event_Id</th>
			<th>Counter/Field_Tested</th>
			<th>Result</th>
			</tr>
		};
		foreach my $line (@Lines){
			next if ($line =~ m/Event_Id|Updating Alias/);
			my ($Event_Id,$InTable,$OutTable,$Field_Tested,$Rule,$Result,$Left_Status,$Right_Status,$Left_Verbose,$Right_Verbose) = split(/,/,$line);
			$result.="<tr>";
			if (grep(/Event_Id/,@sampleFile)){
				$result.="<td align=center>$Event_Id</td>";
			}
			if (grep(/InTable/,@sampleFile)){
				$result.="<td align=center>$InTable</td>";
			}
            if (grep(/OutTable/,@sampleFile)){
				$result.="<td align=center>$OutTable</td>";
			}
            if (grep(/Field_Tested/,@sampleFile)){
				$result.="<td align=center>$Field_Tested</td>";
            }
            if (grep(/Rule/,@sampleFile)){
				$result.="<td align=center>$Rule</td>";
            }
            if (grep(/Result/,@sampleFile)){
				if ($Result =~ /FAIL/m){
					$result.="<td align=center><b><font color=ff0000>$Result</b></font></td>";
				}
				elsif($Result =~ /PASS/m){
					$result.="<td align=center><b><font color=darkblue>$Result</b></font></td>";
				}
                else{
					$result.="<td align=center><b>$Result<b></td>";
				}
			}
			if (grep(/Left Status/,@sampleFile)){
				$result.="<td align=center>$Left_Status</td>";
			}
			if (grep(/Right Status/,@sampleFile)){
				$result.="<td align=center>$Right_Status</td>";
			}
            if (grep(/Left_Verbose/,@sampleFile)){
				$result.="<td align=center>$Left_Verbose</td>";
			}
            if (grep(/Right_Verbose/,@sampleFile)){
				$result.="<td align=center>$Right_Verbose</td>";
			}
            $result.="</tr>";
		}
	}
	else{
		$result=qq{
			<h3>GRIT_$ruleTag</h3>
			<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
			<th>TEST CASE</th>
			<th>OVERALL RESULT</th>
			</tr>
		};
		@rawTableEventCount=sqlSelect("SELECT count(*) from CTUM_0");
		my $count = @rawTableEventCount[0];
		print "GRIT TABLE COUNT : $count\n";
		$result.="<tr><td>$feature_rule_file</td>";
		if($count != '0'){			
			$result.="<td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}
		else{
			$result.="<td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
	}
	$result.="</TABLE>";
	$result.="</br>";
	return $result;
}

sub changingIMSI_Setting_To_deafult{
	executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh shutdown EC_SGEH_1' ");
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish stop '",1);
	system("perl -i -pe 's/SUCCESS_DATA_HANDLING.*/SUCCESS_DATA_HANDLING=ROP_RAW/g' /eniq/mediation_inter/M_E_SGEH/etc/configuration.prop");
	system("perl -i -pe 's/ENIQ_EVENTS_SUC_RAW\" value=\"false\"/ENIQ_EVENTS_SUC_RAW\" value=\"true\"/g' /eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml");
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish start '",1);
	executeThisWithLogging("ssh dcuser\@ec_sgeh_1 '/eniq/mediation_sw/mediation_gw/bin/mzsh startup EC_SGEH_1' ");
	&FT_LOG("Checking the status of glassfish");
    my $glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
    if($glassfishStatus == 1)
    {
	    &FT_LOG("Glassfish is Online");
    }
    else
    {
	    &FT_LOG("Glassfish is Offline");
	}
	my $ecSgehStatus=runCommand("ssh dcuser\@engine ' source ~/.profile; svcs -a | grep engine | grep online'",0);
	if($ecSgehStatus == 1)
    {
		&FT_LOG("EC_sgeh_1 is Online");
    }
	else
	{
		&FT_LOG("ec_sgeh_1 is Offline");
	}
}
sub runArrest_IT_general{
	$dataloading_status=1;
	#EQEV-22189: Modifications to exclude test case execution where RAW tables are not populated.
	if ($dataloading_status == 0){
		&FT_LOG("Data loading is not successful. Arrest-IT test cases will not be executed\n");
		$result.=qq{
		<br/>ARREST-IT test cases will not be executed as data loading was not successful.
		};
		return;
	}
	
	&FT_LOG("Waiting 30 mins before changes in properties files.");
	sleep 1800;
   
	my $feature_config_file=shift;
	chomp($feature_config_file);
	
	#ENC-2327 : wait 30 mins for KPI analysis
	if($feature_config_file =~ m/kpi/i){
		&FT_LOG("Waiting 30 mins more for KPI Analysis before changes in properties files.");
		sleep 1800;	
	}
	
    my $result=qq{
	<h3>Arrest_IT</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST CASE</th>
		 <th>TEST RESULT</th>
		 <th>FAILURE REASON</th>
	   </tr>
	};
	my $requiredPythonVersion = "Python 2.6.4";
	my $pythonVersion = qx(python -V);
	chomp($pythonVersion);
	
	#Updating python version
	if(parseVersion($pythonVersion) > parseVersion($requiredPythonVersion)){
		&FT_LOG("Updating pyhton version\n");
		executeThisWithLogging("/eniq/home/dcuser/automation/RunCommandAsRoot.sh ln -sf /usr/bin/python2.6 /usr/bin/python");
	}
   
    #my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
	#ENC-2164, ENC-2165 Changes related to Inclusion of ARREST-IT Test cases for WCDMACFAHFA in CAR.
	#Chnages required to set fix time window of 10:00 to 10:29 on previous day for WCDMA Arrest-IT testing
    my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst);
	if ( $feature_config_file =~ m/wcdma/i ){
		&FT_LOG("This is WCDMA Arrest-IT test cases. Time window for arrest-it testing will be set to one day old with end time of 10:29");
		($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=gmtime(time-86400);
	}
	else{
		($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=gmtime();
	}
	$year = $year+1900;
	my $month= sprintf("%02d",$mon+1);
	my $day  = sprintf("%02d",$mday);
    my $endTime = "$year-$month-$day";
	#$endTime.="T$hour:00";
	#$endTime.="T$hour:$min";
    my $tarFileDir = "/eniq/home/dcuser/automation/arrest_it";
	my $db = (grepFile("dwhdb", "/etc/hosts"))[0];
	my $db_server = splitAndGetValue($db, " ", 3);
	my $ui = (grepFile("glassfish", "/etc/hosts"))[0];
	my $ui_server = splitAndGetValue($ui, " ", 3);
	my $host = hostname;


	my $end_min=sprintf("00");
	if($min > 0 && $min <=15)
	{
	$end_min=sprintf("00");
      
	}

	if($min > 15 && $min <=30)
	{
	$end_min=sprintf("15");
	}
	if($min > 30 && $min <=45)
	{
	$end_min=sprintf("30");
    }

	if($min > 45 && $min <60)
	{
	$end_min=sprintf("45");
    }

	if($min ==0)
	{
	$end_min=sprintf("45");
	$hour=$hour-1;
	}
	if ( $feature_config_file =~ m/wcdma/i ){
		$endTime.="T10:29";					##ENC-2164, ENC-2165 Changes related to Inclusion of ARREST-IT Test cases for WCDMACFAHFA in CAR.
	}
	else{
		$endTime.="T$hour:$end_min";
	}
	&FT_LOG("The End Time is $endTime\n");

	my $dgServerArrestIt="atclvm559.athtem.eei.ericsson.se";
    #executeThisWithLogging("cd $tarFileDir");
	my $newArrestPackage = `ls -lt /net/$dgServerArrestIt/ossrc/package/arrest/ARREST_IT_R* | head -1 | cut -f7 -d'/'`;
	chomp($newArrestPackage);
	&FT_LOG("Update Arrest_IT Package :: $newArrestPackage\n");
	print "New Package :: $newArrestPackage\n";

	if(!-e "$tarFileDir/$newArrestPackage"){
		&FT_LOG("Delete old ArrestIt pacakge and copy $newArrestPackage package from DG server at $tarFileDir\n");
		executeThisWithLogging("rm -rf $tarFileDir/ARREST_IT*");
		executeThisWithLogging("cp /net/$dgServerArrestIt/ossrc/package/arrest/$newArrestPackage $tarFileDir");
		executeThisWithLogging("cd $tarFileDir; gunzip -f $newArrestPackage");
		executeThisWithLogging("cd $tarFileDir; /usr/sfw/bin/gtar -xvf ARREST*.tar");
	}

	if( !-e "/eniq/home/dcuser/automation/arrest_it/bin/execute_ARREST_IT.py"){
		&FT_LOG("untar $newArrestPackage package $tarFileDir\n");
		executeThisWithLogging("cd $tarFileDir; /usr/sfw/bin/gtar -xvf ARREST*.tar");
	}


	if( -e "/eniq/home/dcuser/automation/arrest_it/config/properties.txt"){
	   open(PRO,"</eniq/home/dcuser/automation/arrest_it/config/properties.txt");
		my @contents=<PRO>;
		close(PRO);
		foreach(@contents){
			if(isVirtualApp()){
			  s/vAppUsingVPN=.*$/vAppUsingVPN=true/;
			  s/csv=.*$/csv=false/;				#ENC-2164, ENC-2165 as Arrest-IT csv test cases will fail on vapp with csv=true.
			}else{
			  s/vAppUsingVPN=.*$/vAppUsingVPN=false/;
			}
			
			#ENC-2327 : Set 30 min time for KPI Analysis
			if ($feature_config_file =~ m/kpi/i){
				s/times=.*$/times=30/;
			}else{
				s/times=.*$/times=15/;
			}
			s/endTime=.*$/endTime=$endTime/;
			s/timezone=.*$/timezone=-0000/;
			#s/timezone=.*$/timezone=+0100/;
			s/uiServerName=.*$/uiServerName=$ui_server/;
			s/dbServerName=.*$/dbServerName=$db_server/;	
			s/dbPass=.*$/dbPass=$temp_pwd/;
		}
		open(PRO, ">/eniq/home/dcuser/automation/arrest_it/config/properties.txt");
		print PRO @contents;
		close (PRO);
		&FT_LOG("/eniq/home/dcuser/automation/arrest_it/config/properties.txt has been modified");
		
	}else{
	    &FT_LOG("ERROR:Could not find /eniq/home/dcuser/automation/arrest_it/config/properties.txt");
	}
	
	
	&FT_LOG("Waiting for 30 mins  before starting ARREST_IT tests.");
	sleep 1800;

	&FT_LOG("START RUNNING ARREST_IT TESTS: ");
	executeThisWithLogging("chmod 777 /eniq/home/dcuser/automation/arrest_it/bin/execute_ARREST_IT.py");
	&FT_LOG("Config file $feature_config_file\n");
	executeThisWithLogging("cd /eniq/home/dcuser/automation/arrest_it/bin; python execute_ARREST_IT.py -f $feature_config_file -u 4");
	
	my @resultsFile = executeThisWithLogging("ls /eniq/home/dcuser/automation/arrest_it/results | grep Arrest_It_Basic_Summary.*txt");
	my $resultFile = "/eniq/home/dcuser/automation/arrest_it/results/$resultsFile[0]";
	
	    open(RESULTS, "<$resultFile") or die "ERROR: Cannot open $resultFile";
        my @results=<RESULTS>;
        close(RESULTS);
        foreach my $line (@results){
		    my $testCaseName;
			my $testResults;
			my $failureReason;
			
			if($line =~ m/Test Case Name/){
			   my @testCase = split(/: /, $line);
			   $testCaseName = $testCase[1];
			   $result.="<tr><td>$testCaseName</td>";
			   #print "abc\n";
			}
		    
			if($line =~ m/Test Result: Passed/){
			  # my @tmp = split(/: /, $line);
			  # if($tmp[1] == "Passed"){
			     $testResults = "PASS";
				 $result.="<td align=center><font color=darkblue><b>PASS</b></font></td><td align=center><b>n/a</b></font></td></tr>\n";
			  #}
			}
            if($line =~ m/Test Result: Failed/){
       			$testResults = "FAIL";
                 #my	@failure = split(/     /,$line);
				 $failureReason = $line;
				 $failureReason =~ s/Test Result.*Reason: //;
				 #$failureReason=~ s/Test Result: Failed //;
				 #print "Failure Reason is $failureReason\n";
                 $result.="<td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center><b>$failureReason</b></font></td></tr>\n";				 
			  }
			  
			
			
			if($line =~ m/^\d+:Launch/){
			   $testResults = "FAIL";
			   $failureReason = "No Data";
			   $result.="<tr><td>$line</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center><b>$failureReason</b></font></td></tr>\n";
			}
		}
		$result.="</TABLE>";
		
	return $result;

}


sub dataGenStart_DVTP{
    my $result=qq{
	<h3>START DVTP DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	
	my $remoteDateGenjardir="/net/$dgServer/tmp/DatagenStore/DVTP/";
	my @ec_servers = ("ec_dvtp_1","ec_dvtp_2");
	my @dirsToCheck = ();
	my @tablesToCheck = ("event_e_dvtp_dt_raw");
	my $ec_server = (grepFile("ec_dvtp_1", "/etc/hosts"))[0];
	my $ec = splitAndGetValue($ec_server, " ", 3);
	my @values1 = split( " ", $ec_server );
	my $ec_ip = $values1[0];
	my $mzHostName = "$ec";
	if($ec eq "eniqe" ){
          $mzHostName = "$ec";
    }else{
         $mzHostName = $ec.".athtem.eei.ericsson.se";
    }
	#my $runJar = $remoteDateGenjardir."java/jdk1.7.0_51/bin/java -jar dvtp_automation_datagen.jar --connector=Basic --stream=Pgw --host=$mzHostName --port=3210";
	
	#my $childPid = fork(); #fork a process

	    executeThisWithLogging("ssh dcuser\@controlzone 'source ~/.profile; chmod 777 ~/automation/topology_data_for_integrity'");
		executeThisWithLogging("ssh dcuser\@controlzone 'source ~/.profile; ~/automation/topology_data_for_integrity'");
	
		if(! -e "$concurrent_present"){	
		&FT_LOG("INFO: This is sequential execution for $feature");
		&FT_LOG("INFO:Start up EC_1, EC_DVTP_1 and EC_DVTP_2 anyway to make sure they are running");
		executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
		executeThisWithLogging("ssh dcuser\@ec_dvtp_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_DVTP_1'");
		executeThisWithLogging("ssh dcuser\@ec_dvtp_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_DVTP_2'");
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC1'");
		}
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
		executeThisWithLogging("ssh dcuser\@ec_dvtp_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_DVTP_1'");
		executeThisWithLogging("ssh dcuser\@ec_dvtp_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_DVTP_2'");
		
		executeThisWithLogging("/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh -e create");
		executeThisWithLogging("/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh -e startlisten");
		executeThisWithLogging("/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh -e startoutput");
		executeThisWithLogging("/eniq/mediation_inter/M_E_DVTP/bin/streaming_admin.sh -e startprocessing");
		
		#Star refresh topology workflow to update the DB
		executeThisWithLogging("ssh dcuser\@ec_dvtp_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart STREAMING_DVTP.TopologyRefreshDB.now -b'");
				
		sleep 300;
		
		my $childPid = fork(); #fork a process
		if($childPid == 0){
			executeThisWithLogging("cd $remoteDateGenjardir; /net/atclvm559.athtem.eei.ericsson.se/tmp/DatagenStore/DVTP/java/jdk1.7.0_51/bin/java -jar dvtp_automation_datagen.jar --connector=Basic --stream=Pgw --host=$ec_ip --port=3210");
			exit(0);
        }else{	
		    sleep 3600;
			my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,30,1,"TAC","9628700");
			$result.=$check[1];
			$result.="</TABLE>";
		   # sleep 5400;
		   #killProcessAndChildren($childPid,$$);
			return $result;
        }
	    
}

sub dataGenStop_DVTP{

  my $pid;
  while(1){
	  
	  my @procs=`/usr/bin/ps -ef | grep java`;
	  my @var=grep(/\/net\/atclvm559.athtem.eei.ericsson.se\/tmp\/DatagenStore\/DVTP\/java\/jdk1.7.0_51\//,@procs);
	  #print "var0 is $var[0]";
	  if($var[0] eq ""){
		 &FT_LOG("INFO: DVTP DataGen Process Has Been Killed");
		 last;
		}else{
			 my @sp = split(/\s+/, $var[0]);
			 print "ID is $sp[2]\n";
		     $pid = $sp[2];
			 executeThisWithLogging("kill -9 $pid");
			 #last;		
			}
	}
	  
   }
   ############################################
   sub dataGenStart_2G3G4G_Like4Like{
        &FT_LOG("Inside new function ");
        my $arg=shift;
        my $force=0;
        my  $result=qq{
        <h3>START 2G3G and/or 4G DATAGEN</h3>
        <TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
           <tr>
                 <th>TEST STAGES</th>
                 <th>STATUS</th>
           </tr>
        };
        my $host = getHostName();
        my $datagen2G3G = 0;
        my $datagen4G = 0;
        my $newIMEI = 0;
        my $twoGThreeGDatagenToStart = $remoteDG;#0 = local, 1 = remote, 2 = remote via sftp, 3 = local via sftp
	my $engineStatus;
        my @checkEngine;
        my $glassfishStatus;
        my @checkGlassfish;
		
		
        my $dir2g3g="/tmp/OMS_LOGS/ebs/ready";
        my $dir4g="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
        my $remotedir4g="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
     

        my $sgeh_dir="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
        my $remote_sgeh_dir="$dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/MSS";

        my @ec_servers=("ec_sgeh_1");
        my @tablesToCheck=();
        my @dirsToCheck=();
        &FT_LOG("INFO:dataGenStart_2G3G4G_Like4Like() 2G3G,4G\n");
        if($remoteDG==1){
                $dir4g=$remotedir4g;
        }
        if($arg =~ m/2G3G/i){
                $datagen2G3G = 1;
                push(@tablesToCheck,"dc.event_e_sgeh_raw");
                if(!$remoteDG){
                        push(@dirsToCheck,$dir2g3g);
                }else{
                      #   push(@dirsToCheck,$remotedir2g3g);
                }
                &FT_LOG("INFO:2G3G TRUE");
        }
        if($arg =~ m/2G3Gsftp/i){
                if($remoteDG){
                        $twoGThreeGDatagenToStart = 2;
                }else{
                      $twoGThreeGDatagenToStart = 3;
                }
        }
        if($arg =~ m/4G/i){
                $datagen4G = 1;
                push(@tablesToCheck,"dc.event_e_lte_raw");
                push(@dirsToCheck,$dir4g);
                &FT_LOG("INFO:4G TRUE");
        }
        if($arg =~ m/force/i){
                $force=1;
        }
        if($arg =~ m/newIMEI/i){
                $newIMEI=1;
        }
        create4GAndMSSGroups();
        my $host = getHostName();
        if($remoteDG){
                if( !-d "$dgNfsPath/$host"){
                        open(TEMP,">$dgNfsPath/tempfile.$$");
                        print TEMP "temp";
                        close TEMP;
                        unlink("$dgNfsPath/tempfile.$$");
                        makePath("$dgNfsPath/$host");
                }
        }else{
                if(changeConfSetting("Input_2G_3G",$datagen2G3G)){
                        $force=1;
                }
                &FT_LOG("INFO:Setting conf_4G.prop, 2G3G data gen $datagen2G3G");

                if(changeConfSetting("Input_4G",$datagen4G)){
                        $force=1;
                }
                &FT_LOG("INFO:Setting conf_4G.prop, 4G data gen $datagen4G");

                if(changeConfSetting("doNewImsis",$newIMEI)){
                        $force=1;
                }
        }

        &FT_LOG("Delete the UserPreferences table.");
        sqlRepDbDelete("delete from dwhrep.UserPreferences where username = 'admin'");
        my $linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir2 | grep ^l'",0);
        if(!$linkExists){
           runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
        }

        if(!$force){
                my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,40,0,"HIERARCHY_3");
                #push (@check, dataGenCheck(\@dirsToCheck,\@tablesToCheck,40,1,"ne_version","4,11"));
                if($check[0]==1){
                        $result.=$check[1];
                #       $result.=$check[3];
                        #$result.=dbChecker_2G3GData();
                        $result.="</TABLE>";
                        &FT_LOG("INFO:Skipping dataGenStart_2G3G4G because it's been done. To force it to load put STARTDATAGEN 2G3G,4G,force in a config file");
                        &FT_LOG("INFO:Start up EC_1 and EC_SGEH_1 anyway to make sure they are running");
                        executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
                        executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
                        return $result;
                }
        }
        &FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
        my @ebsl=("EBSL");
        disableMatchingWorkflows(@ebsl);
        executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
        executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
        my @stopped;
        if($arg!~m/noRollingDisable/){
                if(!isMultiBladeServer()){
                        my @wf=("eankmuk","SGEH","MSS");
                        @stopped=disableAllWorkflows(@wf);
                }
        }
        my $stillActive=0;
        my @wfGroupList=();
        if(!$remoteDG){
                copyBatchFiles($force);
                copy2g3g4gWcdmaConfFiles();
                commitDgMzps();
                push(@wfGroupList,"eankmuk_4G.WG_dataGen_010B");
        }
        $stillActive=disableWorkflowGroup("eankmuk_4G.WG_dataGen_010B","eankmuk_4G.dataGen_010B");
        if($stillActive){
                $stillActive=disableWorkflowGroup("eankmuk_4G.WG_dataGen_010B","eankmuk_4G.dataGen_010B");
        }

        if($twoGThreeGDatagenToStart==2){
                my $hexIpAddress=$dgServerHexIpAddress;
                sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET IP_ADDRESS=$hexIpAddress where SGSN_NAME='SGSN1'");
                sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET PASSWORD='central1' where SGSN_NAME='SGSN1'");
        }elsif($twoGThreeGDatagenToStart==3){
                my $hexIpAddress="0x7f000001000000000000000000000000";
                sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET IP_ADDRESS=$hexIpAddress where SGSN_NAME='SGSN1'");
        }

        $result.=setup2g3g4gWorkflows_like4like($remoteDG,$force);

        if($twoGThreeGDatagenToStart>1){
                &FT_LOG("INFO:Running /eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
                my @response=executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
                if(grep(/libdblib11/,@response) && grep(/fatal/,@response) && grep(/open failed/,@response)){
                        &FT_LOG("INFO:populate.sh failed. Waiting an hour");
                        sleep 3600;
                        @response=executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
                        if(grep(/libdblib11/,@response) && grep(/fatal/,@response) && grep(/open failed/,@response)){
                                &FT_LOG("ERROR:populate.sh failed again. Restarting repdb and dwhdb");
                        }
                        executeThisWithLogging("repdb restart");
                        executeThisWithLogging("dwhdb restart");
                        sleep 60;
                        @response=executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
                        if(grep(/libdblib11/,@response) && grep(/fatal/,@response) && grep(/open failed/,@response)){
                                &FT_LOG("ERROR:populate.sh failed again. Skipping");
                        }
                }
        }

        ## Refresh Topology ##
        &FT_LOG("INFO:mg_topology_refresh.sh");
        my $success=refreshTopologyAndWait("cd /eniq/mediation_inter/M_E_SGEH/bin;./mg_topology_refresh.sh",1200);
    checkRefreshTopologyResult($success,"cd /eniq/mediation_inter/M_E_SGEH/bin;./mg_topology_refresh.sh");

        if(!$remoteDG){
	if(!$stillActive){
                        my $success=import2g3g4gDatagen();
                        if($success){
                                $result.="<tr><td>eankmuk_4G workflow import</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
                        }else{
                                $result.="<tr><td>eankmuk_4G workflow import</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
                        }
                }else{
                        &FT_LOG("INFO:Not importing DataGenWorkFlows4G.zip. The workflows could not be stopped so the import won't work properly");
                        $result.="<tr><td>eankmuk_4G workflow import</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
                }
                enable2g3g4gDatagen();
        }
        enable2g3g4gWorkflows($datagen2G3G,$datagen4G,$twoGThreeGDatagenToStart);

        if($datagen2G3G){
                if($twoGThreeGDatagenToStart>1){
                        push(@wfGroupList,"SGEH.WFG_SGEH_Processing_Node");
                        disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir5_*");
                }else{
                        push(@wfGroupList,"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_5a","SGEH.WFG_SGEH_Processing_NFS_OSSRC1_5b");
                        disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN1");
                }
        }else{
                disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir5_*");
                disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN1");
        }

        if($datagen4G){
                push(@wfGroupList,"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_1a","SGEH.WFG_SGEH_Processing_NFS_OSSRC1_1b");
                push(@wfGroupList,"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2a","SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2b");
        }else{
                disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir1_*");
                disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir2_*");
        }

        # Disable other SGEH workflows not used in any case
        disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN[2345]");
        disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN_*");
        disableWorkflow("SGEH.WF_SGEH_Processing_Node.MME*");

        my $dataGenStartTimePR = getTimeSpecLocal(time);
        &FT_LOG("INFO:DataGen started at:$dataGenStartTimePR \n");
        my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,40,1,"HIERARCHY_3");
       push (@check, dataGenCheck(\@dirsToCheck,\@tablesToCheck,40,1,"ne_version","4,11"));
        &FT_LOG("INFO:Will return the status of the relevant workFlows presently");
        workflowStatus(\@wfGroupList);
        $result.=$check[1];
        #$result.=$check[3];
        #$result.=dbChecker_2G3GData();
        $result.="</TABLE>";
        if($arg!~m/noRollingDisable/){
                if(!isMultiBladeServer()){
                        executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");
                }
        }
        return $result;


}
   
   
   ##########################################
   

sub dataGenStart_2G3G4G{
	my $arg=shift;
	my $force=0;
	my  $result=qq{
	<h3>START 2G3G and/or 4G DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	my $host = getHostName();
	my $datagen2G3G = 0;
	my $datagen4G = 0;
	my $newIMEI = 0;
	my $twoGThreeGDatagenToStart = $remoteDG;#0 = local, 1 = remote, 2 = remote via sftp, 3 = local via sftp
	
	my $engineStatus;
        my @checkEngine;
        my $glassfishStatus;
        my @checkGlassfish;
	my $dir2g3g="/tmp/OMS_LOGS/ebs/ready";
	my $dir4g="/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	my $remotedir4g="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	my $remotedir2g3g="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	
	my $sgeh_dir="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	my $remote_sgeh_dir="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	
	my @ec_servers=("ec_sgeh_1");
	my @tablesToCheck=();
	my @dirsToCheck=();
	&FT_LOG("INFO:dataGenStart_2G3G4G() 2G3G,4G\n");
	if($remoteDG==1){
		$dir4g=$remotedir4g;
	}
	if($arg =~ m/2G3G/i){
		$datagen2G3G = 1; 	
		push(@tablesToCheck,"dc.event_e_sgeh_raw");
		if(!$remoteDG){
			push(@dirsToCheck,$dir2g3g);
		}else{
			push(@dirsToCheck,$remotedir2g3g);
		}
		&FT_LOG("INFO:2G3G TRUE");
	}
	if($arg =~ m/2G3Gsftp/i){
		if($remoteDG){
			$twoGThreeGDatagenToStart = 2;
		}else{
			$twoGThreeGDatagenToStart = 3;
		}
	}
	if($arg =~ m/4G/i){
		$datagen4G = 1;
		push(@tablesToCheck,"dc.event_e_lte_raw");
		push(@dirsToCheck,$dir4g);
		&FT_LOG("INFO:4G TRUE");
	}
	if($arg =~ m/force/i){
		$force=1;
	}
	if($arg =~ m/newIMEI/i){
		$newIMEI=1;
	}
	create4GAndMSSGroups();
	my $host = getHostName();
	if($remoteDG){
		if( !-d "$dgNfsPath/$host"){
			open(TEMP,">$dgNfsPath/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("$dgNfsPath/tempfile.$$");
			makePath("$dgNfsPath/$host");
		}
	}else{
		if(changeConfSetting("Input_2G_3G",$datagen2G3G)){
			$force=1;
		}
		&FT_LOG("INFO:Setting conf_4G.prop, 2G3G data gen $datagen2G3G");
		
		if(changeConfSetting("Input_4G",$datagen4G)){
			$force=1;
		}
		&FT_LOG("INFO:Setting conf_4G.prop, 4G data gen $datagen4G");
		
		if(changeConfSetting("doNewImsis",$newIMEI)){
			$force=1;
		}
	}
	
	&FT_LOG("Delete the UserPreferences table.");
	sqlRepDbDelete("delete from dwhrep.UserPreferences where username = 'admin'");
	
	#JIRA-18003 Automation of the pre/post upgrade tests for Paging Aggs
	&FT_LOG("Automation of the pre/post upgrade tests for Paging Aggs");
	@checkEngine=executeThisWithLogging("cat /eniq/sw/conf/static.properties | grep 'EVENT_E_LTE.PagingErrorAggregationEnabler=false'");
        if(grep(/false/,@checkEngine))
        {
	    &FT_LOG("Updating the PagingErrorAggregationEnabler to true in static.properties file and restarting the engine");
            #Updating the PagingErrorAggregationEnabler to true in static.properties file
            runCommand("ssh dcuser\@engine 'cat /eniq/sw/conf/static.properties | sed s/.*EVENT_E_LTE.PagingErrorAggregationEnabler.*/EVENT_E_LTE.PagingErrorAggregationEnabler=true/g > /tmp/result.txt'",1);
            runCommand("ssh dcuser\@engine 'mv /tmp/result.txt /eniq/sw/conf/static.properties; chmod 644 /eniq/sw/conf/static.properties'",1);
    
            runCommand("ssh dcuser\@engine ' source ~/.profile; engine restart '",1);
            sleep(20);
	    &FT_LOG("Checking the status of engine");
            $engineStatus=runCommand("ssh dcuser\@engine ' source ~/.profile; svcs -a | grep engine | grep online'",0);
	    if($engineStatus == 1)
            {
	        &FT_LOG("Engine is Online");
            }
	    else
	    {
	        &FT_LOG("Engine is Offline");
	    }
        }
    
	@checkGlassfish=executeThisWithLogging("cat /eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml| grep 'ENABLED\" value=\"false\"'");
        if(grep(/false/,@checkGlassfish))
        {
           #Updating the  PAGING.SUCCESS.AGGREGATION.ENABLED to true in domain.xml file
	   &FT_LOG("Updating the  PAGING.SUCCESS.AGGREGATION.ENABLED to true in domain.xml file and restarting the glassfish");
           system("perl -pi -e 's/PAGING.SUCCESS.AGGREGATION.ENABLED\" value=\"false\"/PAGING.SUCCESS.AGGREGATION.ENABLED\" value=\"true\"/g' /eniq/glassfish/glassfish/glassfish/domains/domain1/config/domain.xml");
           
           runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish restart '",1);
	   sleep(20);
	   &FT_LOG("Checking the status of glassfish");
           $glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
           if($glassfishStatus == 1)
           {
	           &FT_LOG("Glassfish is Online");
           }
           else
           {
	           &FT_LOG("Glassfish is Offline");
           }
        }
	&FT_LOG("Automation of the pre/post upgrade tests for Paging Aggs Completed");

	
	my $linkExists=runCommand("ssh dcuser\@ec_sgeh_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh/dir2 | grep ^l'",0);
	if(!$linkExists){
	   runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
	}
	
	if(!$force){
		my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,40,0,"HIERARCHY_3");
		#push (@check, dataGenCheck(\@dirsToCheck,\@tablesToCheck,40,1,"ne_version","4,11"));
		if($check[0]==1){
			$result.=$check[1];
		#	$result.=$check[3];
			#$result.=dbChecker_2G3GData();
			$result.="</TABLE>";
			&FT_LOG("INFO:Skipping dataGenStart_2G3G4G because it's been done. To force it to load put STARTDATAGEN 2G3G,4G,force in a config file");
			&FT_LOG("INFO:Start up EC_1 and EC_SGEH_1 anyway to make sure they are running");
			if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
			executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
			}
			
			executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
			return $result;
		}
	}
	&FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
	my @ebsl=("EBSL");
	disableMatchingWorkflows(@ebsl);

	if(! -e "$concurrent_present"){
		&FT_LOG("INFO: This is sequential execution for $feature");
		executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
	}
	executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
	executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
	my @stopped;
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			my @wf=("eankmuk","SGEH","MSS");
			@stopped=disableAllWorkflows(@wf);
		}
	}
	my $stillActive=0;
	my @wfGroupList=();
	if(!$remoteDG){
		copyBatchFiles($force);
		copy2g3g4gWcdmaConfFiles();
		commitDgMzps();
		push(@wfGroupList,"eankmuk_4G.WG_dataGen_010B");
	}
	$stillActive=disableWorkflowGroup("eankmuk_4G.WG_dataGen_010B","eankmuk_4G.dataGen_010B");
	if($stillActive){
		$stillActive=disableWorkflowGroup("eankmuk_4G.WG_dataGen_010B","eankmuk_4G.dataGen_010B");
	}
		
	if($twoGThreeGDatagenToStart==2){
		my $hexIpAddress=$dgServerHexIpAddress;
		sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET IP_ADDRESS=$hexIpAddress where SGSN_NAME='SGSN1'");
		sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET PASSWORD='central1' where SGSN_NAME='SGSN1'");
	}elsif($twoGThreeGDatagenToStart==3){
		my $hexIpAddress="0x7f000001000000000000000000000000";
		sqlUpdate("UPDATE DIM_E_SGEH_SGSN SET IP_ADDRESS=$hexIpAddress where SGSN_NAME='SGSN1'");
	}
	
	$result.=setup2g3g4gWorkflows($remoteDG,$force);
	
	if($twoGThreeGDatagenToStart>1){
		&FT_LOG("INFO:Running /eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
		my @response=executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
		if(grep(/libdblib11/,@response) && grep(/fatal/,@response) && grep(/open failed/,@response)){
			&FT_LOG("INFO:populate.sh failed. Waiting an hour");
			sleep 3600;
			@response=executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
			if(grep(/libdblib11/,@response) && grep(/fatal/,@response) && grep(/open failed/,@response)){
				&FT_LOG("ERROR:populate.sh failed again. Restarting repdb and dwhdb");
			}
			executeThisWithLogging("repdb restart");
			executeThisWithLogging("dwhdb restart");
			sleep 60;
			@response=executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/populate.sh");
			if(grep(/libdblib11/,@response) && grep(/fatal/,@response) && grep(/open failed/,@response)){
				&FT_LOG("ERROR:populate.sh failed again. Skipping");
			}
		}
	}
	
	## Refresh Topology ##
	&FT_LOG("INFO:mg_topology_refresh.sh");
	my $success=refreshTopologyAndWait("cd /eniq/mediation_inter/M_E_SGEH/bin;./mg_topology_refresh.sh",1200);
    checkRefreshTopologyResult($success,"cd /eniq/mediation_inter/M_E_SGEH/bin;./mg_topology_refresh.sh");
	
	if(!$remoteDG){
		if(!$stillActive){
			my $success=import2g3g4gDatagen();
			if($success){
				$result.="<tr><td>eankmuk_4G workflow import</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			}else{
				$result.="<tr><td>eankmuk_4G workflow import</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
			}
		}else{
			&FT_LOG("INFO:Not importing DataGenWorkFlows4G.zip. The workflows could not be stopped so the import won't work properly");
			$result.="<tr><td>eankmuk_4G workflow import</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		enable2g3g4gDatagen();
	}
	enable2g3g4gWorkflows($datagen2G3G,$datagen4G,$twoGThreeGDatagenToStart);
	
	if($datagen2G3G){
		if($twoGThreeGDatagenToStart>1){
			push(@wfGroupList,"SGEH.WFG_SGEH_Processing_Node");
			disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir5_*");
		}else{
			push(@wfGroupList,"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_5a","SGEH.WFG_SGEH_Processing_NFS_OSSRC1_5b");
			disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN1");
		}
	}else{
		disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir5_*");
		disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN1");
	}
	
	if($datagen4G){
		push(@wfGroupList,"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_1a","SGEH.WFG_SGEH_Processing_NFS_OSSRC1_1b");
		push(@wfGroupList,"SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2a","SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2b");
	}else{
		disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir1_*");
		disableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir2_*");
	}
	
	# Disable other SGEH workflows not used in any case
	disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN[2345]");
	disableWorkflow("SGEH.WF_SGEH_Processing_Node.SGSN_*");
	disableWorkflow("SGEH.WF_SGEH_Processing_Node.MME*");
	
	my $dataGenStartTimePR = getTimeSpecLocal(time);
	&FT_LOG("INFO:DataGen started at:$dataGenStartTimePR \n");
	my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,40,1,"HIERARCHY_3");
	#push (@check, dataGenCheck(\@dirsToCheck,\@tablesToCheck,40,1,"ne_version","4,11"));
	&FT_LOG("INFO:Will return the status of the relevant workFlows presently");
	workflowStatus(\@wfGroupList);
	$result.=$check[1];
	#$result.=$check[3];
	#$result.=dbChecker_2G3GData();
	$result.="</TABLE>";
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");
		}
	}
	return $result;
}

sub dbChecker_2G3GData{

    my $output;
	my $command;
	my @outputLines;
	my @files = ` ls /net/atclvm559.athtem.eei.ericsson.se/tmp/DatagenStore/eniq/data/eventdata/events_oss_1/sgeh/dir1/MME*ebs.* | tail -1`;
	my $fileType;
	my $logFile = '/eniq/home/dcuser/automation/tmp2.log';
	my $errorCount;
	my @values;
	my $dbValue=0;
	my $results;
	
	&FT_LOG("About to sleep 11 minutes for files to load \n");
	sleep(660);
	
	executeThisWithLogging("chmod 777 /eniq/home/dcuser/automation/check_decoded_file_against_db.pl");
	executeThisWithLogging("chmod 777 /eniq/home/dcuser/automation/parse_ebm_log_generic_mod.pl");
	
	foreach(@files){
		&FT_LOG("Loading File $_");
		
		if($_ =~ m/ebs_4G/){
			$fileType = "0";
			#print "Comparing 4G SGEH file \n";
		} elsif ($_ =~ m/ebs\./){
			#print "Comparing 2G3G SGEH file \n";
			$fileType = "1";
		}
		&FT_LOG("Running Command : /eniq/home/dcuser/automation/check_decoded_file_against_db.pl -f $_ \n");
		$output = `./check_decoded_file_against_db.pl -f $_`;
		
		@outputLines = split /\n/, $output;
		foreach(@outputLines){
				if($_ =~ m/events in total/){
				#	print "\n$_\n";
					@values = split('found ', $_);
					&FT_LOG("Number of failed events processed : $values[1]\n");
					$dbValue = $values[1];
				}
		}
		
		if(-e $logFile){
			system(`rm -rf $logFile`);
		}
		
		if($fileType == "1"){
		    &FT_LOG("Running Command: /eniq/home/dcuser/automation/parse_ebm_log_generic_mod.pl -o /eniq/home/dcuser/automation/DBChecker/tmp2.log -f $_");
			$command = `./parse_ebm_log_generic_mod.pl -o /eniq/home/dcuser/automation/tmp2.log -f $_`;
			$errorCount=checkLogsForFailures();
	     }
		
       my $message = "Checking the number of events that appears in the data binary files and in the database for 2G3G data";
		
		if($errorCount == $dbValue){
			print "End Result : PASS\n";
			$results="<tr><td>$message</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}else{
			print "\n End Result : FAIL\n";
			$results="<tr><td>$message</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		
		@outputLines  = ();
		$output = "";
	}
	
	return $results;

}

sub checkLogsForFailures{
	my $errorCount;
	$errorCount = 0;
	my $logFile = '/eniq/home/dcuser/automation/tmp2.log';
	
	open (MYFILE, $logFile) or die "ERROR: Cannot open logfile";
	while (<MYFILE>) {
		chomp;
		if($_ =~ m/event_result = reject/){
		$errorCount++;
		}
	}
	&FT_LOG("Number of failed events in data file : $errorCount \n");
	return $errorCount;
}

### START SGEH DVDT DATA GENERATION ###
sub dataGenStart_SGEH_DVDT{
	my  $result=qq{
	<h3>START 2G3G SGEHDVDT DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};

	my $deployment = 'FT';
	my $datagenday = getDay();

	## These are just the prefixes of the table names. The next step finds the largest number table on the system, e.g. event_e_sgeh_err_raw_03 ##
	my @tablesToClear=("event_e_sgeh_err_raw","event_e_sgeh_suc_raw","event_e_gsn_dt_raw","event_e_gsn_dtpdp_raw");
	my $sql1="";
	my $deletePassed=1;
	foreach my $table(@tablesToClear){
		$table=lc($table);
		my @tables=sqlSelect("SELECT b.name + '.' + a.name FROM sysobjects a, sysusers b WHERE a.type IN ('U', 'S') AND a.uid = b.uid and a.name like \"$table"."_"."%\" ORDER BY a.name DESC");
		if (lc($tables[0]) =~ m/dc\.$table/){
			$sql1.="delete from $tables[0] where day_id = $datagenday\ngo\n";
		}else{
			&FT_LOG("ERROR:Did not find tables to clear");
			$deletePassed=0;
		}
	}
	
	if( $sql1 ne ""){
		$sql1.="EOF";
		$result.="<tr><td>Clearing SGEH_CDR Raw Tables for day[$datagenday]</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Clearing SGEH_CDR Raw Tables for day[$datagenday]\n");
		open(CLEAR,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql1 |");
		my @clear=<CLEAR>;
		chomp(@clear);
		close(CLEAR);
	}else{
		&FT_LOG("ERROR:Did not find tables to clear");
		$deletePassed=0;
	}
	
	if ( $deletePassed==0){
		$result.="<tr><td>Clearing SGEH_CDR Raw Tables for day[$datagenday]</td><td align=center><font color=darkblue><b>FAIL</b></font></td></tr>\n";
	}
	
	## Determine if we are using a standalone or multi blade system ##
	my @co_server=executeThisWithLogging("grep -iw engine /etc/hosts | cut -d ' ' -f 3");
	chomp(@co_server);
	my @ec_server=executeThisWithLogging("grep -iw ec_1 /etc/hosts | cut -d ' ' -f 3");
	chomp(@ec_server);

	if($co_server[0] eq $ec_server[0]){
		$deployment = 'FT';
		&FT_LOG("Standalone Deployment ($co_server[0])\n");
	}else{
		$deployment = 'MULTI';
		&FT_LOG("Multi Blade Deployment (CO=$co_server[0],EC=$ec_server[0])\n");
	}

	my @status;

	if($deployment eq 'FT'){
		@status=executeThisWithLogging("mkdir -p /tmp/OMS_LOGS/ebs/ready/12/2G3G/GGSN1");

		@status=executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_2G3G_DVDT.prop /tmp/bin/input/conf.prop");
		@status=executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn_2G3G_DVDT.prop /tmp/bin/input/sgsn.prop");

		@status=executeThisWithLogging("ec stop");
		&FT_LOG("ec stop\n");
	}
	else #multi blade
	{
		@status=executeThisWithLogging("ssh dcuser\@$ec_server[0].$domain 'mkdir -p /tmp/OMS_LOGS/ebs/ready/12/2G3G/GGSN1");

		@status=executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_2G3G_DVDT.prop /tmp/bin/input/conf.prop");
		@status=executeThisWithLogging("cp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn_2G3G_DVDT.prop /tmp/bin/input/sgsn.prop");

		@status=executeThisWithLogging("ssh dcuser\@$ec_server[0].$domain 'mkdir -p /tmp/bin/input' ");
		@status=executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology2G3G_DVDT/input.zip dcuser\@$ec_server[0].$domain:/tmp/bin");
		@status=executeThisWithLogging("ssh dcuser\@$ec_server[0].$domain 'unzip -o /tmp/bin/input.zip -d /tmp/bin/input' ");
		@status=executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_2G3G_DVDT.prop dcuser\@$ec_server[0].$domain:/tmp/bin/input/conf.prop");
		@status=executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn_2G3G_DVDT.prop dcuser\@$ec_server[0].$domain:/tmp/bin/input/sgsn.prop");

		@status=executeThisWithLogging("ssh dcuser\@$ec_server[0].$domain 'source ~/.profile; ec stop' ");
		&FT_LOG("On $ec_server[0] - Created /tmp/bin/input and 'ec stop' \n");
	}

	## Insert the Package file into the system ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pcommit /eniq/home/dcuser/automation/DataGenWorkFlows/random.mzp");
	&FT_LOG("pcommit random.mzp\n");

	## Clear the Topology Caches ##
	@status=executeThisWithLogging("rm -rf /eniq/mediation_inter/cache/topology/MZ*");
	&FT_LOG("Delete MZ topology cache (/eniq/mediation_inter/cache/topology/MZ*)\n");
	@status=executeThisWithLogging("rm -rf /tmp/OMS_LOGS/ebs/MZ*");
	&FT_LOG("Delete MZ topology cache (/tmp/OMS_LOGS/ebs/MZ*)\n");
	@status=executeThisWithLogging("rm -rf /eniq/home/dcuser/test_deepa/MZ*");
	&FT_LOG("Delete MZ topology cache (/eniq/home/dcuser/test_deepa/MZ*)\n");

	## Restart the ControlZone on the Co-ordinator ##
	@status=executeThisWithLogging("controlzone restart");
	&FT_LOG("controlzone restart\n");

	if ($deployment eq 'FT'){
		@status=executeThisWithLogging("ec start");
		&FT_LOG("ec start\n");
	}else{
		@status=executeThisWithLogging("ssh dcuser\@$ec_server[0].$domain 'source ~/.profile; ec start' ");
		&FT_LOG("ssh dcuser\@$ec_server[0].$domain 'source ~/.profile; ec start'\n");
	}

	## Refresh Topology ##
	&FT_LOG("mg_topology_refresh.sh\n");
	my $sgehRefreshTopologySuccess = refreshTopologyAndWait("/eniq/mediation_inter/M_E_SGEH/bin/mg_topology_refresh.sh",1200);
	checkRefreshTopologyResult($sgehRefreshTopologySuccess,"/eniq/mediation_inter/M_E_SGEH/bin/mg_topology_refresh.sh");

	## Import DataGen Workflow 1 ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr systemimport -nameconflict re -keyconflict re /eniq/home/dcuser/automation/DataGenWorkFlows/DataGenWorkFlows_SGEH_DVDT_1.zip  > /eniq/home/dcuser/automation/audit/MZimportLog_dvdt_1.$run_date.txt");
	&FT_LOG("systemimport -nameconflict re -keyconflict re DataGenWorkFlows_SGEH_DVDT_1.zip > /eniq/home/dcuser/automation/audit/MZimportLog_dvdt_1.$run_date.txt\n");

	## Import DataGen Workflow 2 ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr systemimport -nameconflict re -keyconflict re /eniq/home/dcuser/automation/DataGenWorkFlows/DataGenWorkFlows_SGEH_DVDT_2.zip  > /eniq/home/dcuser/automation/audit/MZimportLog_dvdt_2.$run_date.txt");
	&FT_LOG("systemimport -nameconflict re -keyconflict re DataGenWorkFlows_SGEH_DVDT_2.zip > /eniq/home/dcuser/automation/audit/MZimportLog_dvdt_2.$run_date.txt\n");

	## If dvdt_populate.sh has not already run, then execute it ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist > /eniq/mediation_sw/mediation_gw/bin/dvtpflows");

	## Check if DVTP has been provisioned ##
	@status=executeThisWithLogging("egrep -c '(DVTP.WF01_PreProcessing.GGSN1)' /eniq/mediation_sw/mediation_gw/bin/dvtpflows");
	if($status[0] == 0){
		&FT_LOG("DVTP not provisioned - running dvtp_populate.sh\n");
		@status=executeThisWithLogging("/eniq/mediation_inter/M_E_GSN/bin/dvtp_populate.sh 1");
		&FT_LOG("Execute /eniq/mediation_inter/M_E_GSN/bin/dvtp_populate.sh,\tStatus=($status[0])\n");
	}else{
		&FT_LOG("DVTP is already provisioned\n");
	}

	## Modify dvdt path for datagen simulator ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport DVTP.WF01_PreProcessing /eniq/mediation_sw/mediation_gw/bin/dvtp.csv");
	@status=executeThisWithLogging("/usr/bin/sed -e 's/eniq\\/data\\/pushData\\/12\\/2G3G\\/GGSN1/tmp\\/OMS_LOGS\\/ebs\\/ready\\/12\\/2G3G\\/GGSN1/' /eniq/mediation_sw/mediation_gw/bin/dvtp.csv >/eniq/mediation_sw/mediation_gw/bin/dvtp1.csv");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport DVTP.WF01_PreProcessing /eniq/mediation_sw/mediation_gw/bin/dvtp1.csv > /eniq/home/dcuser/automation/audit/WFimportLog_dvdt_2.$run_date.txt\n");
	&FT_LOG("wfimport DVTP.WF01_PreProcessing,\tStatus=($status[0])\n");
	#@status=executeThisWithLogging("rm -rf /eniq/mediation_sw/mediation_gw/bin/dvtp*");

	## Stop and Disable  ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1 ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1");
	&FT_LOG("Stop Workflow (ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1");
	&FT_LOG("Disable Workflow (ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1),\tStatus=($status[0])\n");

	## Stop and Disable ENIQ_SIM.RealtimeSimulator.Simulator ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop ENIQ_SIM.RealtimeSimulator.Simulator");
	&FT_LOG("Stop Workflow (ENIQ_SIM.RealtimeSimulator.Simulator),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable ENIQ_SIM.RealtimeSimulator.Simulator");
	&FT_LOG("Disable Workflow (ENIQ_SIM.RealtimeSimulator.Simulator),\tStatus=($status[0])\n");

	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1");
	&FT_LOG("Enable Workflow (ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1,\tStatus=($status[0])\n");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1");
	&FT_LOG("Start Workflow (ENIQ_SIM.RealtimeSimToFile_GGSN.workflow_1),\tStatus=($status[0])\n");

	## Start and enable ENIQ_SIM.RealtimeSimulator.Simulator ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable ENIQ_SIM.RealtimeSimulator.Simulator");
	&FT_LOG("Enable Workflow (ENIQ_SIM.RealtimeSimulator.Simulator),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart ENIQ_SIM.RealtimeSimulator.Simulator");
	&FT_LOG("Start Workflow (ENIQ_SIM.RealtimeSimulator.Simulator,\tStatus=($status[0])\n");

	## Start and enable Reserved_SGEH_SIM.dataGen_10B.workflow_1 ##
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable Reserved_SGEH_SIM.dataGen_10B.workflow_1");
	&FT_LOG("Enable Workflow (Reserved_SGEH_SIM.dataGen_10B.workflow_1),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart Reserved_SGEH_SIM.dataGen_10B.workflow_1");
	&FT_LOG("Start Workflow (Reserved_SGEH_SIM.dataGen_10B.workflow_1),\tStatus=($status[0])\n");

	## Enable and Start LogParser Group ##
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG00_LogParsing_Inter");
	&FT_LOG("Enable Group WF (SGEH.WG00_LogParsing_Inter),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart SGEH.WG00_LogParsing_Inter");
	&FT_LOG("Start Group WF (SGEH.WG00_LogParsing_Inter),\tStatus=($status[0])\n");

	###########################################
	# Enable and Start LoadBalancer Group     #
	# Add TEMP Workflow to LoadBalancer Group #
	# Enable and Start LoadBalancer TEMP Wflow#
	###########################################
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG01_LoadBalancer");
	&FT_LOG("Enable Group WF (SGEH.WG01_LoadBalancer),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart SGEH.WG01_LoadBalancer");
	&FT_LOG("Start Group WF (SGEH.WG01_LoadBalancer),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf SGEH.WG01_LoadBalancer SGEH.WF01_LoadBalancer.SGSN1");
	&FT_LOG("Add WF to Group WF (SGEH.WG01_LoadBalancer SGEH.WF01_LoadBalancer.SGSN1),\tStatus=$status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF01_LoadBalancer.SGSN1");
	&FT_LOG("Enable WF (SGEH.WF01_LoadBalancer.SGSN1),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart SGEH.WF01_LoadBalancer.SGSN1");
	&FT_LOG("Start WF (SGEH.WF01_LoadBalancer.SGSN1),\tStatus=($status[0])\n");

	## Enable and Start 2G3G Processing ##
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG02_Processing");
	&FT_LOG("Enable Group WF (SGEH.WG02_Processing),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart SGEH.WG02_Processing");
	&FT_LOG("Start Group WF (SGEH.WG02_Processing),\tStatus=($status[0])\n");

	## Enable and Start PDPS Processing ##
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WG03_PDPS_Processing");
	&FT_LOG("Enable Group WF (SGEH.WG03_PDPS_Processing),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart SGEH.WG03_PDPS_Processing");
	&FT_LOG("Start Group WF (SGEH.WG03_PDPS_Processing),\tStatus=($status[0])\n");

	## Enable and Start DVDT Processing ##
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable DVDT.WG01_PreProcessing_1min");
	&FT_LOG("Enable Group WF (DVDT.WG01_PreProcessing_1min),\tStatus=($status[0])\n");
	@status=executeThisWithLogging("eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart DVDT.WG01_PreProcessing_1min");
	&FT_LOG("Start Group WF (DVDT.WG01_PreProcessing_1min),\tStatus=($status[0])\n");

	## Take the current time for the start of Data Generation ##
	my $dataGenDir1 = '/tmp/OMS_LOGS/ebs/ready/DR_TMP_DIR';
	my $dataGenDir2 = '/tmp/OMS_LOGS/ebs/ready/12/2G3G/GGSN1';
	my $dataGenStartTime = time;
	my $dataGenStartTimePR = getTimeSpecLocal(time);

	&FT_LOG("DataGen started at:$dataGenStartTimePR\n");
	&FT_LOG("Sleeping for $DGtimeWarp mins to allow Raw_Tables to populate\n");
	sleep $DGtimeWarp*60;
	my @ec_servers=("ec_dvtp_1");

	if ($deployment eq 'FT'){
		my $dir = verifyDataGenDir(\@ec_servers,$dataGenDir1,$dataGenStartTime,1);
		$_ = $dir;
		if(/Exception|Fail|Error/){
			$result.="<tr><td>$dir</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
			&FT_LOG("$dir - FAIL\n");
		}else{
			$result.="<tr><td>$dir</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			&FT_LOG("$dir - PASS\n");
		}
	}

	if ($deployment eq 'FT'){
		my $dir = verifyDataGenDir(\@ec_servers,$dataGenDir2,$dataGenStartTime,1);
		$_ = $dir;
		if(/Exception|Fail|Error/){
			$result.="<tr><td>$dir</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
			&FT_LOG("$dir - FAIL\n");
		}else{
			$result.="<tr><td>$dir</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			&FT_LOG("$dir - PASS\n");
		}
	}

	## For SGEH CDR we have 3 Raw tables to check in ##
	my $msg = verifyDataGenLoading('dc.event_e_sgeh_raw');
	$_ = $msg;
	if(/Exception|Fail|Error/){
		$result.="<tr><td>$msg</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		&FT_LOG("$msg - FAIL\n");
	}else{
		$result.="<tr><td>$msg</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("$msg - PASS\n");
	}

	$msg = verifyDataGenLoading('dc.event_e_gsn_dt_raw');
	my $dataIsLoading = "true";

	$_ = $msg;
	if(/Exception|Fail|Error/){
		$result.="<tr><td>$msg</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		&FT_LOG("$msg - FAIL\n");
		$dataIsLoading = "false";
	}else{
		$result.="<tr><td>$msg</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("$msg - PASS\n");
	}

	$msg = verifyDataGenLoading('dc.event_e_gsn_dtpdp_raw');
	$_ = $msg;
	if(/Exception|Fail|Error/){
		$result.="<tr><td>$msg</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		&FT_LOG("$msg - FAIL\n");
		$dataIsLoading = "false";
	}else{
		$result.="<tr><td>$msg</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("$msg - PASS\n");
	}

	if($dataIsLoading eq "true"){
		my $dataGenEndTime = getTimeSpecGmt(time);
		my $dataGenEndTimeWarp = getDateTimewarp();

		my $gsnTable="dc.EVENT_E_GSN_DT_RAW_07";
		my @latestTable=sqlSelect("SELECT b.name + '.' + a.name FROM sysobjects a, sysusers b WHERE a.type IN ('U', 'S') AND a.uid = b.uid and a.name like \"EVENT_E_GSN_DT_RAW_%\" ORDER BY a.name DESC");
		if (lc($latestTable[0]) =~ m/dc\.EVENT_E_GSN_DT_RAW_/){
			$gsnTable=$latestTable[0];
		}

		my $sql2=qq{
		insert into dc.DIM_E_IMSI_MSISDN (
		IMSI,    
		MSISDN,
		TIMESTAMP_ID,
		VENDOR,
		STATUS,
		CREATED,
		MODIFIED,
		MODIFIER    
		)
		select
		raw.IMSI,
		raw.MSISDN,
		max(raw.TIMESTAMP_ID_PART) as TIMESTAMP_ID,
		(select distinct VENDOR from dc.EVENT_E_GSN_DT_RAW),
		'ACTIVE',
		'$dataGenEndTimeWarp',
		'$dataGenEndTime',
		'ENIQ_EVENTS'    
		from (
		select
		IMSI,
		MSISDN,
		max(DATETIME_ID) as TIMESTAMP_ID_PART
		from $gsnTable
		where DATETIME_ID >= dateadd(minute, -480, '$dataGenEndTime')
		and IMSI is not null
		and MSISDN is not null
		group by
		IMSI,
		MSISDN
		) as raw
		left outer join
		dc.DIM_E_IMSI_MSISDN
		on raw.IMSI = dc.DIM_E_IMSI_MSISDN.IMSI
		and raw.MSISDN = dc.DIM_E_IMSI_MSISDN.MSISDN
		where dc.DIM_E_IMSI_MSISDN.IMSI is null
		or dc.DIM_E_IMSI_MSISDN.MSISDN is null
		group by
		raw.IMSI,
		raw.MSISDN;
go
EOF
		};

		$result.="<tr><td>Reset Events Timing [DIM_E_IMSI_MSISDN]</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Reset Events Timing [DIM_E_IMSI_MSISDN]\n");
		open(EVENTS,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql2 |");
		my @events=<EVENTS>;
		chomp(@events);
		close(EVENTS);
	}

	$result.="</TABLE>";
	return $result;
}

###########################################
###########################################

sub preDeleteWcdmaFiles{
	######
	#Delete pre-existing temp files to prevent PreProcessing workflow aborting
	my @dir;
	my @fileMatch;
	my @subDirMatch;
	my @subDirExclude;
	push(@dir,"/eniq/data/pmdata/eventdata");
	push(@fileMatch,"^MZ");
	push(@fileMatch,"^A");
	push(@subDirMatch,"GPEHEvents");
	&findAndDeleteFiles(\@dir,\@fileMatch,\@subDirMatch,\@subDirExclude);
}

sub wcdmaDbSetup{
	## Database stuffs
	my $insert=qq{
	insert into mapping_gpeh_d 
    select 
    dc.dim_e_ran_rncfunction.rncid,
    dc.dim_e_sgeh_hier321_cell.cid,
    dc.dim_e_ran_rncmodule.rncmodule,
    dc.dim_e_ran_rnc.rnc_name
    from
    dc.dim_e_sgeh_hier321, 
    dc.dim_e_sgeh_hier321_cell, 
    dc.dim_e_ran_rnc, dc.dim_e_ran_rncfunction , dc.dim_e_ran_rncmodule
    where
    dc.dim_e_sgeh_hier321.hier321_id = dc.dim_e_sgeh_hier321_cell.hier321_id 
    and dc.dim_e_sgeh_hier321.hierarchy_3 = dc.dim_e_ran_rnc.alternative_fdn 
    and dc.dim_e_ran_rnc.rnc_fdn = dc.dim_e_ran_rncfunction.sn
    and dc.dim_e_ran_rnc.rnc_fdn = dc.dim_e_ran_rncmodule.sn;
    };
	sqlCreate("create table dc.MAPPING_gpeh_D (rncId INTEGER NULL, cId INTEGER NULL, RncModule INTEGER NULL, RNC_NAME VARCHAR(50) NULL);");
	sqlInsert($insert);
}

sub disableWorkflowGroup{
	my $wfGroup=shift;
	my $wf=shift;
	my @status;
	my $stillActive=1;
	
	@status=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist $wfGroup");
	if(!grep(/$wfGroup/,@status)){
		$stillActive=0;
		&FT_LOG("INFO:$wfGroup not present. Skipping disable");
	}else{
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop $wfGroup");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable $wfGroup");
		my @workflows=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist $wf.* | awk '{print \$1}'");
		my $stopTimeout=0;
		while ($stillActive && $stopTimeout<120){
			$stillActive=0;
			foreach my $workflow(@workflows){
				$workflow=~s/\n//g;
				@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop $workflow");
				if(grep(/No such workflow/,@status)){
					$stillActive=0;
				}elsif(!grep(/Configuration not activated/,@status)){
					$stillActive=1;
					@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable $workflow");
					if(!grep(/Already/,@status)){
						$stillActive=1;
					}
				}
			}
			if($stillActive){
				&FT_LOG("INFO:Waiting 30 seconds to confirm that workflows have stopped");
				sleep 30;
			}else{
				&FT_LOG("INFO:Workflows stopped and disabled");
			}
			if($stopTimeout==60){
				&FT_LOG("INFO: Workflows haven't stopped yet. Restarting controlzone and ec");
				foreach my $ec(@ecHosts){
					executeThisWithLogging("ssh dcuser\@$ec 'source ~/.profile; ec stop' ");
					&FT_LOG("INFO:On $ec - 'ec stop' \n");
				}
				## Restart the ControlZone on the Co-ordinator ##
				executeThisWithLogging("controlzone stop");
				&FT_LOG("INFO:controlzone stop\n");
			
				## Clear the Topology Cache ##
				executeThisWithLogging("rm -rf /eniq/mediation_inter/cache/topology/MZ*");
				&FT_LOG("INFO:Delete MZ topology cache\n");
				
				executeThisWithLogging("controlzone start");
				&FT_LOG("INFO:controlzone start\n");
				foreach my $ec(@ecHosts){
					executeThisWithLogging("ssh dcuser\@$ec 'source ~/.profile; ec start' ");
					&FT_LOG("INFO:On $ec - 'ec start' \n");
				}
			}
			$stopTimeout++;
		}
	}
	
	if($stillActive){
		&FT_LOG("ERROR:Failed to stop datagen workflows. Continuing anyway");
	}
	return $stillActive;
}

sub disableWorkflow{
	my $workflow=shift;
	my @status;
	my $stillActive=1;
	my $stopTimeout=0;
	while ($stillActive && $stopTimeout<120){
		$stillActive=0;
		$workflow=~s/\n//g;
		@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop $workflow");
		if(grep(/No such workflow/,@status)){
			$stillActive=0;
		}else{
			$stillActive=0;
			@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable $workflow");
			if(!grep(/Already/,@status)){
				$stillActive=1;
			}
		}
		if($stillActive){
			&FT_LOG("INFO:Waiting 30 seconds to confirm that workflow has stopped");
			sleep 30;
		}else{
			&FT_LOG("INFO:Workflow stopped and disabled");
		}
		if($stopTimeout==60){
			&FT_LOG("INFO: Workflow hasn't stopped yet. Restarting controlzone and ec");
			foreach my $ec(@ecHosts){
				executeThisWithLogging("ssh dcuser\@$ec 'source ~/.profile; ec stop' ");
				&FT_LOG("INFO:On $ec - 'ec stop' \n");
			}
			## Restart the ControlZone on the Co-ordinator ##
			executeThisWithLogging("controlzone stop");
			&FT_LOG("INFO:controlzone stop\n");
			## Clear the Topology Cache ##
			executeThisWithLogging("rm -rf /eniq/mediation_inter/cache/topology/MZ*");
			&FT_LOG("INFO:Delete MZ topology cache\n");
			
			executeThisWithLogging("controlzone start");
			&FT_LOG("INFO:controlzone start\n");
			foreach my $ec(@ecHosts){
				executeThisWithLogging("ssh dcuser\@$ec 'source ~/.profile; ec start' ");
				&FT_LOG("INFO:On $ec - 'ec start' \n");
			}
		}
		$stopTimeout++;
	}
	if($stillActive){
		&FT_LOG("ERROR:Failed to stop workflow. Continuing anyway");
	}
	return $stillActive;
}

sub quicklyDisableWorkflows{
	my @workflow = @_;
	my @stopped;
	my $disableCommand = "/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable ";
	my $stopCommand = "/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop ";
	if(@workflow){
		#must keep track of what I actually disable
		#disable
		my @disableStatus = executeThisQuiet($disableCommand . "@workflow");
		foreach my $wf (@workflow){
			if( grep(/Already disabled/, grep(/$wf/, @disableStatus))==0){
				#meaning we stopped this one
				push(@stopped, $wf);
			}
		}
		
		#stop
		executeThisQuiet($stopCommand . "@workflow");
	}
	return @stopped;
}

sub importWcdmaDatagen{
	## Import DataGen Workflows ##
	my $workflowZip = "DatagenWorkflows_WCDMA_13082012.zip";
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr systemimport -nameconflict re -keyconflict re  /eniq/home/dcuser/automation/DataGenWorkFlows/$workflowZip");
	if(grep(/as invalid/,@status)){
		&FT_LOG("ERROR:eankmuk_WCDMA_RESERVED workflow did not import correctly. Delete the entire workflow folder using the Mediation Zone GUI and run this function again");
		return 0;
	}else{
		return 1;
	}
}

sub enableWcdmaDatagen{
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC01");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC05");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC06");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC07");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable eankmuk_WCDMA_RESERVED.W11A_CF");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart eankmuk_WCDMA_RESERVED.W11A_CF");
	
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC01");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC05");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC06");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC07");
}

sub makeSymbolicLink{
	my $origionalFile = shift;
	my $linkLocation = shift;
	my $linkPath=dirname($linkLocation);
	if(!-d $linkPath){
		makePath($linkPath);
	}
	return symlink($origionalFile, $linkLocation);
}

sub populateGPEHWorkflows{
	my $result="";
	my $allWorkflowsAdded=1;
	my @status;

	my @wfs=("RNC01","RNC05","RNC06","RNC12");
	my $workflows = qq{"ID","Name","Filename","Profile"};
	my $i=1;
	
	&FT_LOG("Checking workflows in GPEH.WG01_LoadBalancer");
	foreach my $wf (@wfs){
		if($allWorkflowsAdded){
			my @status=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf GPEH.WG01_LoadBalancer GPEH.WF01_LoadBalancer.$wf");
			if(!grep(/skipped/,@status)){
				$allWorkflowsAdded=0;
			}
		}
		$workflows.="\n$i,\"$wf\",\"A20.*${wf}_.*rnc_gpehfile.*.bin.gz\",\"GPEH.GpehImsiMapping_AP_0$i\"";
		$i++;
	}
	
	if($allWorkflowsAdded==1){
		$result.=logAndGenerateTableEntry("All relevant workflows were successfully created and added to group GPEH.WG01_LoadBalancer",1);
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable GPEH.WG01_LoadBalancer");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable GPEH.WF01_*RNC*");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable GPEH.WF02_*.0[0-3]");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable GPEH.WF03_*.0[0-3]");
		&FT_LOG("INFO:Disabling unused workflows");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable GPEH.WF*.0[4-9]");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable GPEH.WF*.1[0-5]");
		return $result;
	}else{
		&FT_LOG("INFO:All relevant workflows were not created and/or not added to the group GPEH.WG01_LoadBalancer.");
		&FT_LOG("INFO:Running /eniq/mediation_inter/M_E_GPEH/bin/populate.sh");
		executeThisWithLogging("/eniq/mediation_inter/M_E_GPEH/bin/populate.sh");
	}
	
	$allWorkflowsAdded = 1;
	
	foreach my $wf (@wfs){
		my @status=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf GPEH.WG01_LoadBalancer GPEH.WF01_LoadBalancer.$wf");
		if(!grep(/skipped/,@status)){
			$allWorkflowsAdded=0;
			last;
		}
	}
	
	if($allWorkflowsAdded==1){
		$result.=logAndGenerateTableEntry("All relevant workflows were successfully created and added to group GPEH.WG01_LoadBalancer using populate.sh",1);
	}else{
		$result.=logAndGenerateTableEntry("All relevant workflows were not created and/or were not added to the group GPEH.WG01_LoadBalancer using populate.sh. Adding manually",0);	
		&FT_LOG("INFO:All relevant workflows were not created and/or were not added to the group GPEH.WG01_LoadBalancer. Adding manually");			

		#generate the gpeh_result.csv file
		unlink("/tmp/.gpeh_result.csv");
		open(CSV, ">/tmp/.gpeh_result.csv");
		print CSV $workflows;
		close (CSV);
				
		### Modify Workflows ###
		# STOP MZ PRE-PROCESSING WORKFLOWS

		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable \"GPEH.WG01_LoadBalancer\"");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable \"GPEH.WF01_LoadBalancer*\"");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop -immediate \"GPEH.WF01_LoadBalancer*\"");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop -immediate \"GPEH.WF01_LoadBalancer*\"");

		#ADD DUMMY WORKFLOW INSTANCE TO WG01 GROUP
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist GPEH.DUMMY.* | xargs -n 2 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf GPEH.WG01_LoadBalancer");


		#THIS WILL ALLOW YOU TO REMOVE ALL INSTANCES FROM WG01 GROUP
		#REMOVE EXISTING WORKFLOW INSTANCES FROM WG01 GROUP
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist GPEH.WF01_LoadBalancer.* | xargs -n 20 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupremovewf GPEH.WG01_LoadBalancer");

		#IMPORT .CSV FILE TO POPULATE WORKFLOW INSTANCES
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport GPEH.WF01_LoadBalancer /tmp/.gpeh_result.csv");
		executeThisWithLogging("rm /tmp/.gpeh_result.csv");

		#ADD NEW WORKFLOW INSTANCES TO WG01 GROUP
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist GPEH.WF01_LoadBalancer.* | xargs -n 20 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf GPEH.WG01_LoadBalancer");

		#REMOVE DUMMY WORKFLOW INSTANCE FROM WG01 GROUP
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist GPEH.DUMMY.* | xargs -n 2 /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupremovewf GPEH.WG01_LoadBalancer");

		#ENABLE MZ PRE-PROCESSING WORKFLOWS
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable \"GPEH.WG01_LoadBalancer*\"");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable \"GPEH.WF01_LoadBalancer*\"");

		#ENABLE MZ PROCESSING WORKFLOWS
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable 'GPEH.WG02*'");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable 'GPEH.WF02*'");

		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable 'GPEH.WG00*'");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable 'GPEH.WF00*'");

		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable 'GPEH.WG03*'");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable 'GPEH.WF03*'");

		#ENABLE MZ SYSTEM TASK WORKFLOWS.
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable 'SystemTask.Archive_Cleaner_grp'");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable 'GPEH.WG03_SystemTask_FailedArchiveCleaner'");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable 'SystemTask.Archive_Cleaner*'");
					
		unlink("/tmp/.gpeh_result.csv");
			
		$allWorkflowsAdded = 1;
	
		foreach my $wf (@wfs){
			my @status=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf GPEH.WG01_LoadBalancer GPEH.WF01_LoadBalancer.$wf");
			if(!grep(/skipped/,@status)){
				$allWorkflowsAdded=0;
				last;
			}
		}
		
		if($allWorkflowsAdded == 1){
			$result.=logAndGenerateTableEntry("All relevant workflows were successfully created and added to group GPEH.WG01_LoadBalancer when added manually",1);
		}else{
			$result.=logAndGenerateTableEntry("All relevant workflows were not created and/or were not added to the group GPEH.WG01_LoadBalancer when added manually",0);	
		}
	}
	&FT_LOG("INFO:Disabling unused workflows");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable GPEH.WF*.0[4-9]");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable GPEH.WF*.1[0-5]");
	return $result;
}

sub dataGenStart_WCDMA{
	my $arg=shift;
	my $result=qq{
	<h3>START WCDMA DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>TEST STAGES</th>
	 <th>STATUS</th>
	</tr>
	};
	my $host = getHostName();
	my $force=0;
	&FT_LOG("INFO:dataGenStartWCDMA() WCDMA");
	my $rnc01Dir="/eniq/data/eventdata/events_oss_1/GPEHEvents/dir1";
	my $rnc06Dir="/eniq/data/eventdata/events_oss_1/GPEHEvents/dir6";
	
	
	if($remoteDG==1){
		if( !-d "$dgNfsPath/$host"){
			open(TEMP,">$dgNfsPath/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("$dgNfsPath/tempfile.$$");
			makePath("$dgNfsPath/$host");
		}
	}
	my @ec_servers=("ec_1");
	my @dirsToCheck=($rnc01Dir,$rnc06Dir);
	my @tablesToCheck=("dc.event_e_ran_cfa_err_raw");
	if($arg!~m/force/){		
		my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,40,0,"HIER3_ID");
		if($check[0]==1){
			$result.=$check[1];
			$result.="</TABLE>";
			&FT_LOG("INFO:Skipping dataGenStart_WCDMA because it's been done. To force it to load put dataGenStart_WCDMA force in a config file");
			&FT_LOG("INFO:Start up EC_1 anyway to make sure they are running");
			
			if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
				executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
			}
			executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			
			return $result;
		}
	}else{
		$force=1;
	}
	&FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
	my @ebsl=("EBSL");
	disableMatchingWorkflows(@ebsl);
	
	if(! -e "$concurrent_present"){
		&FT_LOG("INFO: This is sequential execution for $feature");
		executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
	}
	executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			
	my @stopped; #array of workflows we stopped
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			my @wf = ("eankmuk_WCDMA","GPEH");
			#disable all workflows EXCEPT @wf ones for singleblade. Reduces load
			@stopped = disableAllWorkflows(@wf);
		}
	}
	
	wcdmaDbSetup();
	disableWorkflowGroup("eankmuk_WCDMA_RESERVED.W11A_CF","eankmuk_WCDMA_RESERVED.W11B_CF_rsv");
	if(!$remoteDG){
		copyBatchFiles($force);
		executeThisWithLogging("ssh dcuser\@ec_1 'rm /eniq/data/eventdata/events_oss_1/GPEHEvents/remotefiles 2>/dev/null'");
		executeThisWithLogging("ssh dcuser\@ec_1 'rm -rf /eniq/data/eventdata/events_oss_1/GPEHEvents 2>/dev/null'");
		unlink("/eniq/data/eventdata/events_oss_1/GPEHEvents/remotefiles");
		unlink("/eniq/data/eventdata/events_oss_1/GPEHEvents");
		commitDgMzps();
		copy2g3g4gWcdmaConfFiles();
		preDeleteWcdmaFiles();
		&FT_LOG("INFO:System import");
		importWcdmaDatagen();
		enableWcdmaDatagen();
		
		$result.=populateGPEHWorkflows();
		
		&FT_LOG("INFO:Running /eniq/mediation_inter/M_E_GPEH/bin/mg_topology_refresh.sh");
		my $success = refreshTopologyAndWait("/eniq/mediation_inter/M_E_GPEH/bin/mg_topology_refresh.sh",1200);
		checkRefreshTopologyResult($success,"/eniq/mediation_inter/M_E_SGEH/bin/mg_topology_refresh.sh");
	}else{
		$result.=populateGPEHWorkflows();
		
		&FT_LOG("INFO:Running /eniq/mediation_inter/M_E_GPEH/bin/mg_topology_refresh.sh");
		my $success = refreshTopologyAndWait("/eniq/mediation_inter/M_E_GPEH/bin/mg_topology_refresh.sh",1200);
		checkRefreshTopologyResult($success,"/eniq/mediation_inter/M_E_GPEH/bin/mg_topology_refresh.sh");
		my $linkExists=runCommand("ssh dcuser\@ec_1 'ls -ld /eniq/data/eventdata/events_oss_1/GPEHEvents | grep ^l'",0);
		if(!$linkExists || $arg=~m/force/){
			executeThisWithLogging("ssh dcuser\@ec_1 'rm -rf /eniq/data/eventdata/events_oss_1/GPEHEvents 2>/dev/null'");
			runCommand("ssh dcuser\@ec_1 'mkdir -p /eniq/data/eventdata/events_oss_1'",1);
			my $success=runCommand("ssh dcuser\@ec_1 'ln -s $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/GPEHEvents /eniq/data/eventdata/events_oss_1/GPEHEvents'",1);
			if($success==1){
				&FT_LOG("INFO:Symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/GPEHEvents at /eniq/data/eventdata/events_oss_1/GPEHEvents on ec_1");
			}else{
				&FT_LOG("ERROR:Failed to create symbolic link created to $dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/GPEHEvents at /eniq/data/eventdata/events_oss_1/GPEHEvents on ec_1");
			}
		}else{
			&FT_LOG("INFO:Symbolic already exists at /eniq/data/eventdata/events_oss_1/GPEHEvents on ec_1. Put dataGenStart_WCDMA force in a config file to force re-creation");
		}
	}
	
	#Add Required groups needed for Events UI Groups
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/ControllerWCDMA.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/MCC_MNC_WCDMA.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_Auto_cell1.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_Auto_IMSI1.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_Auto_IMSI2.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_Auto_IMSI3.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_rnc1.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_tac1.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_tac3.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_Two_TACs.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/GPEH_Auto_cell3.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/Core_IMSI1.xml");
	
	## Take the current time for the start of Data Generation ##
	my $dataGenStartTimePR = getTimeSpecLocal(time);
	&FT_LOG("INFO:DataGen started at:$dataGenStartTimePR \n");
	my @check=dataGenCheck(\@ec_servers,\@dirsToCheck,\@tablesToCheck,30,1,"HIER3_ID");
	$result.=$check[1];
	$result.="</TABLE>";
	
	if($arg=~/waitifstarting/){
		my $time = ($remoteDG?10:150);#10 mins for remote datagen, 150 mins for local
		&FT_LOG("INFO:Waiting $time minutes for data to load");
		sleep($time * 60);
	}

	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");
		}
	}
	
	return $result;
}

sub updateReservedDataTimeRanges{
	if(-e "$reservedDataLocation"){
		open(CSV,"<$reservedDataLocation");
		my @contents=<CSV>;
		close(CSV);
		foreach(@contents){
			#Removing the 5 minutes time range from this list as its not supported in the new UI 
			#s/TIME_RANGES=.*$/TIME_RANGES=5 minutes,15 minutes,30 minutes,1 hour,2 hours/;
			s/TIME_RANGES=.*$/TIME_RANGES=15 minutes,30 minutes,1 hour,2 hours/;
			s/TIME_RANGES_LTE=.*$/TIME_RANGES_LTE=15 minutes/;
			s/KPI_TIME_RANGES.*$/KPI_TIME_RANGES=5 minutes,15 minutes,30 minutes,1 hour,2 hours/;
			s/NUMBER_OF_NODES=\d+/NUMBER_OF_NODES=3/;
			s/NUMBER_OF_SUBSCRIBERS=\d+/NUMBER_OF_SUBSCRIBERS=3/;
		}
		open(CSV, ">$reservedDataLocation");
		print CSV @contents;
		close (CSV);
		&FT_LOG("INFO:Updated time ranges in reservedData.csv to be 2 hours max");
	}else{
		&FT_LOG("ERROR:Could not find reserved data file at location: $reservedDataLocation");
	}
}

### START KPI Notification DATA GENERATION ###

sub enableKpiNotificationWorkflows{
	my $startedWorkflows=0;
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir3_*");
	if(!grep(/Already/,@status)){
		$startedWorkflows=1;
	}
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir4_*");
	if(!grep(/Already/,@status)){
		$startedWorkflows=1;
	}
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_3* SGEH.WFG_SGEH_Processing_NFS_OSSRC1_4*");
	if(!grep(/Already/,@status)){
		$startedWorkflows=1;
	}
	return $startedWorkflows;
}

sub dataGenStart_kpi{
	my $result=qq{
	<h3>START KPI DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>TEST STAGES</th>
	 <th>STATUS</th>
	</tr>
	};
	my $printErrors=0;
	my $host = getHostName();
	my $dir="/eniq/data/eventdata/events_oss_1/sgeh/dir3";
	my $remotedir="$dgNfsPath/$host/50files/eniq/data/eventdata/events_oss_1/sgeh/dir3";
	if($remoteDG==1){
		$dir=$remotedir;
	}
	
	my @ec_servers=("ec_sgeh_1");
	my @dirs=($remotedir);
	my @tables=("event_e_lte_raw");
	my $arg=shift;
	&FT_LOG("INFO:dataGenStart_Kpi()");
	my @status;
	my $force=0;
	if($arg=~m/force/){
		$force=1;
	}
	my @stopped; #array of workflows we stopped
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			my @wf = ("eankmuk","SGEH","MSS");
			#disable all workflows EXCEPT $wf ones for singleblade. Reduces load
			@stopped = disableAllWorkflows(@wf);
		}
	}
	
	$result.=setup2g3g4gWorkflows($remoteDG,$force);
	enable2g3g4gWorkflows(1,1,1);
	$printErrors=enableKpiNotificationWorkflows();
	if($printErrors){ ##Only disable the EBSL workflows if the KPI notification workflows weren't already enabled
		&FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
		my @ebsl=("EBSL");
		disableMatchingWorkflows(@ebsl);

		if(! -e "$concurrent_present"){
			&FT_LOG("INFO: This is sequential execution for $feature");		
			executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
		}
	}		
	executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
	executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
	my $host = getHostName();
	if(!$remoteDG){
		copyBatchFiles($force);
		commitDgMzps();
		copy2g3g4gWcdmaConfFiles();
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_4G.dataGen_011A.MME1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart eankmuk_4G.dataGen_011A.MME1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable eankmuk_4G.dataGen_011A.MME2");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstart eankmuk_4G.dataGen_011A.MME2");
	}elsif( !-d "$dgNfsPath/$host"){
		open(TEMP,">$dgNfsPath/tempfile.$$");
		print TEMP "temp";
		close TEMP;
		unlink("$dgNfsPath/tempfile.$$");
		makePath("$dgNfsPath/$host");
	}
	my $timeStamp=time;
	my $result1=dataGenStart_MSS($arg);
	
	if(time>$timeStamp+300){
		$printErrors=0;#greather than 5 mins.
	}
	my @check=dataGenCheck(\@ec_servers,\@dirs,\@tables,40,$printErrors,"ne_version","4,13");
	$result.=$check[1];
	$result.="</TABLE>";
	$result.=$result1;
	
	if($printErrors && $arg=~/waitifstarting/){
		my $time = ($remoteDG?10:150);#10 mins for remote datagen, 150 mins for local
		&FT_LOG("INFO:Waiting $time minutes for data to load");
		sleep($time * 60);
	}
	
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped")
		}
	}
	
	return $result;
}

#
### START MSS DATA GENERATION ###
#
sub copyMssConfFiles{
	my @status;
	if(! -d "/tmp/bin"){
		mkdir("/tmp/bin");
	}
	copy("/eniq/home/dcuser/automation/DataGenWorkFlows/mss_conf.prop","/tmp/bin");
	foreach my $ec(@ecHosts){	
		executeThisWithLogging("ssh dcuser\@$ec \"ls /tmp/bin >/dev/null 2>&1 || mkdir /tmp/bin\"");
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/mss_conf.prop dcuser\@$ec:/tmp/bin/");
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn.prop dcuser\@$ec:/tmp/bin/");
	}
}

sub commitDgMzps{
	my @plugins=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr plist | /usr/bin/egrep 'random_plugin|mg_dummy_batch_input'");
	if(!grep(/random_plugin/,@plugins) || !grep(/mg_dummy_batch_input/,@plugins)){
		foreach my $ec(@ecHosts){
			executeThisWithLogging("ssh dcuser\@$ec 'source ~/.profile; ec stop' ");
			&FT_LOG("INFO:On $ec - 'ec stop'");
		}
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pcommit /eniq/home/dcuser/automation/DataGenWorkFlows/random.mzp");
		&FT_LOG("INFO:pcommit random.mzp");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr pcommit /eniq/home/dcuser/automation/DataGenWorkFlows/mg_dummy_batch_input.mzp");
		&FT_LOG("INFO:pcommit mg_dummy_batch_input.mzp");
		executeThisWithLogging("controlzone stop");
		&FT_LOG("INFO:controlzone stop");
		executeThisWithLogging("rm -rf /eniq/mediation_inter/cache/topology/MZ*");
		&FT_LOG("INFO:Delete MZ topology cache");
		executeThisWithLogging("controlzone start");
		&FT_LOG("INFO:controlzone start");

		foreach my $ec(@ecHosts){
			executeThisWithLogging("ssh dcuser\@$ec 'source ~/.profile; ec start' ");
			&FT_LOG("INFO:On $ec - 'ec start'");
		}
	}else{
		&FT_LOG("INFO:random.mzp and mg_dummy_batch_input.mzp have already been committed");
	}
}

sub preDeleteMssFiles{
	my @dir;
	my @fileMatch;
	my @subDirMatch;
	my @subDirExclude;
	push(@dir,"/eniq/data/pmdata/eventdata");
	push(@dir,"/eniq/data/pushData");
	push(@fileMatch,"^MZ");
	push(@fileMatch,"^MSS");
	push(@subDirMatch,"MSS");
	push(@subDirExclude,"archive");
	&FT_LOG("INFO:Finding and deleting files");
	&findAndDeleteFiles(\@dir,\@fileMatch,\@subDirMatch,\@subDirExclude);
}

sub importMssDg{
	my $result="";
	## Import DataGen Workflows ##
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr systemimport -nameconflict re -keyconflict re /eniq/home/dcuser/automation/DataGenWorkFlows/DataGenWorkFlowsMSS.zip");
	if(grep(/as invalid/,@status)){
		&FT_LOG("ERROR:MSS.MSSSimulator workflow did not import correctly. Delete the entire workflow folder using the Mediation Zone GUI and run this function again");
		$result.="<tr><td>MSS.MSSSimulator workflow import</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
	}else{
		$result.="<tr><td>MSS.MSSSimulator workflow import</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}
	return $result;
}

sub updateMssDgWorkflows{
	unlink("/tmp/mssworkflows.csv");
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport MSS.WF01_PreProcessing /tmp/mssworkflows.csv");
	if(grep(/Export finished/,@status) && -f "/tmp/mssworkflows.csv"){
		&FT_LOG("INFO:MSS.WF01_PreProcessing workflow exported successfully");
	}else{
		&FT_LOG("ERROR:MSS.WF01_PreProcessing workflow did not export");
	}
	unlink("/tmp/mssdatagenworkflows.csv");
	@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport MSS.MSSSimulator /tmp/mssdatagenworkflows.csv");
	if(grep(/Export finished/,@status) && -f "/tmp/mssdatagenworkflows.csv"){
		&FT_LOG("INFO:MSS.MSSSimulator workflow exported successfully");
	}else{
		&FT_LOG("ERROR:MSS.MSSSimulator workflow did not export");
	}

	open(MSS,"</tmp/mssworkflows.csv");
	my @workflows=<MSS>;
	close(MSS);
	open(MSS,"</tmp/mssdatagenworkflows.csv");
	my @datagenWorkflows=<MSS>;
	close(MSS);
	unlink("/tmp/mssworkflows.csv");
	unlink("/tmp/mssdatagenworkflows.csv");

	my $dataGenDir = '';
	foreach (@datagenWorkflows){
		foreach my $workflow(@workflows){
			if($workflow =~ m/MSS_/){
				my @dirNum = ($workflow =~ m/\/eniq\/data\/pushData\/(.*)\/mss/g);
				my @mssName = ($workflow =~ m/mss\/(.*)\"/g);
				s/\/eniq\/data\/pushData\/.*\/mss\/$mssName[0]/\/eniq\/data\/pushData\/$dirNum[0]\/mss\/$mssName[0]/g;
				s/MSS_5/MSS_4/g;
				if($mssName[0] eq "MSS_3"){
					$dataGenDir="/eniq/data/pushData/$dirNum[0]/mss/MSS_3";
				}
			}
		}
	}
	open(MSS,">/tmp/msseditedworkflows.csv");
	foreach(@datagenWorkflows){
		print MSS;
	}
	close(MSS);

	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport MSS.MSSSimulator /tmp/msseditedworkflows.csv");
	unlink("/tmp/msseditedworkflows.csv");
	return $dataGenDir;
}

sub enableMssDatagenWorkflows{
	&FT_LOG("INFO:Enable WF (MSS.MSSSimulator.workflow_3)");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable MSS.MSSSimulator.workflow_3");
	&FT_LOG("INFO:Enable WF (MSS.MSSSimulator.workflow_4)");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable MSS.MSSSimulator.workflow_4");
	&FT_LOG("INFO:Enable WFgroup (MSS.WorkFlow_MSS)");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable MSS.WorkFlow_MSS");
	&FT_LOG("INFO:Start WFgroup (MSS.WorkFlow_MSS)");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart MSS.WorkFlow_MSS");
	my @wfGroupList=("MSS.WG01_PreProcessing", "MSS.WG02_Processing","MSS.WorkFlow_MSS");
	&FT_LOG("INFO:Will return the status of the relevant workFlows presently");
	sleep 10;
	workflowStatus(\@wfGroupList);
}

sub updateMssPreprocessingWorkflow{
	my $mss3Dir=shift;
	my $mss4Dir=shift;
	unlink("/tmp/mssworkflows.csv");
	unlink("/tmp/msseditedworkflows.csv");
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport MSS.WF01_PreProcessing /tmp/mssworkflows.csv");
	if(grep(/Export finished/,@status) && -f "/tmp/mssworkflows.csv"){
		&FT_LOG("INFO:MSS.WF01_PreProcessing workflow exported successfully");
	}else{
		&FT_LOG("ERROR:MSS.WF01_PreProcessing workflow did not export");
	}
	open(MSS,"</tmp/mssworkflows.csv");
	my @workflows=<MSS>;
	close(MSS);
	my $updatedWorkflows=0;
	foreach my $workflow(@workflows){
		if($workflow =~ m/MSS_3/){
			$workflow=~s/\/.*\"/$mss3Dir\"/;
			$updatedWorkflows++;
		}
		if($workflow =~ m/MSS_4/){
			$workflow=~s/\/.*\"/$mss4Dir\"/;
			$updatedWorkflows++;
		}
	}
	if($updatedWorkflows<2){
		&FT_LOG("ERROR:Did not find MSS_3 and MSS_4 in MSS.WF01_PreProcessing workflow:");
		foreach my $workflow(@workflows){
			&FT_LOG($workflow);
		}
	}
	open(MSS,">/tmp/msseditedworkflows.csv");
	foreach(@workflows){
		print MSS;
	}
	close(MSS);
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport MSS.WF01_PreProcessing /tmp/msseditedworkflows.csv");
	unlink("/tmp/mssworkflows.csv");
	unlink("/tmp/msseditedworkflows.csv");
}

sub restartWorkflows{
	my ($wfGroupsToRestart,$wfToRestart)=@_;
	my @status;
	my $stillActive=1;
	my $stopTimeout=0;
	while ($stillActive && $stopTimeout<120){
		$stillActive=0;
		foreach my $workflow(@$wfToRestart){
			$workflow=~s/\n//g;
			@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop $workflow");
			if(!grep(/Configuration not activated/,@status)){
				$stillActive=1;
			}
			@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable $workflow");
			if(!grep(/Already/,@status)){
				$stillActive=1;
			}
		}
		foreach my $workflow(@$wfGroupsToRestart){
			$workflow=~s/\n//g;
			@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop $workflow");
			if(!grep(/Configuration not activated/,@status)){
				$stillActive=1;
			}
			@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable $workflow");
			if(!grep(/Already/,@status)){
				$stillActive=1;
			}
		}
		if($stillActive){
			&FT_LOG("INFO:Waiting 30 seconds to confirm that workflows have stopped");
			sleep 30;
		}else{
			&FT_LOG("INFO:Workflows stopped and disabled");
		}
		$stopTimeout++;
	}
	foreach my $workflow(@$wfToRestart){
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable $workflow");
	}
	foreach my $group(@$wfGroupsToRestart){
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable $group");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart $group");
	}
}

sub dataGenStart_MSS{
	my $arg=shift;
	my $force=0;
	my $host = getHostName();
	my $host = getHostName();
	my $dgFilePath="/eniq/data/pushData";
	my $dataGenDir=$dgFilePath."/00/mss/MSS_3";
	my $datetime = strftime "%F %H:%M:%S", gmtime;
	chomp($datetime);
	my $mss_insert = "INSERT INTO dc.DIM_E_SGEH_HIER321(OSS_ID,RAT,HIERARCHY_3,HIERARCHY_2,HIERARCHY_1,RAC,MCC,MNC,LAC,CELL_ID,ACCESS_AREA_ID,GLOBAL_CELL_ID,START_TIME,END_TIME,MSC,FPDCH,SITE_NAME,CELL_TYPE,CELL_BAND,CELL_LAYER,HIER3_ID,HIER32_ID,HIER321_ID,VENDOR,STATUS,CREATED,MODIFIED,MODIFIER) VALUES('eniq_oss_4',0,'BSC1','','CELL1',0,'460','00',1,2,2,'','$datetime','','',0,'','','GSM900','2',5386564559998864911,9178779967770509109,4948639634796658772,'Ericsson','ACTIVE','2009-04-20 14:45:09','2015-11-24 09:13:11','ENIQ_EVENTS')"; 
	sqlInsert($mss_insert);
	my @nfs_dirs=("$dgNfsPath/$host/50files/eniq/data/pushData/04/mss/MSS_3","$dgNfsPath/$host/50files/eniq/data/pushData/03/mss/MSS_4");
	my @mss_dirs=("/eniq/data/pushData/04/mss/MSS_3","/eniq/data/pushData/03/mss/MSS_4");
	my @ec_servers=("ec_1");
	my $result=qq{
	<h3>START MSS DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>TEST STAGES</th>
	 <th>STATUS</th>
	</tr>
	};
	&FT_LOG("INFO:dataGenStartMSS() MSS");
	
	if($remoteDG==1){
		$dataGenDir=$mss_dirs[0];
		if( !-d "$dgNfsPath/$host"){
			open(TEMP,">$dgNfsPath/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("$dgNfsPath/tempfile.$$");
			makePath("$dgNfsPath/$host");
		}
	}
	updateReservedDataTimeRanges();
	create4GAndMSSGroups();
	
	opendir (DIR, "$dgFilePath");
	my @dirs = grep /[0-9]+/, readdir(DIR);
	closedir DIR;
	my $res = "";
	@mss_dirs=();
	foreach my $dir (@dirs){
		opendir (DIR, "$dgFilePath/$dir/mss");
		my @sub_dirs = grep /^MSS/,readdir(DIR);
		closedir DIR;
		foreach my $sub_dir(@sub_dirs){
			foreach my $nfs_dir(@nfs_dirs){
				my $temp_nfs_dir = $nfs_dir;
				$temp_nfs_dir =~ s%.*/%%;
				if($sub_dir =~ m/$temp_nfs_dir/){
					push @mss_dirs, "/eniq/data/pushData/$dir/mss/$sub_dir";
				}
			}
		}
	}
	if(@mss_dirs==0){
		@mss_dirs=("/eniq/data/pushData/04/mss/MSS_3","/eniq/data/pushData/03/mss/MSS_4");
	}
	
	if($arg!~m/force/){
		my @tables=("dc.event_e_mss_voice_cdr_raw","dc.event_e_mss_sms_cdr_raw");
		my @check=dataGenCheck(\@ec_servers,\@mss_dirs,\@tables,40,0,"HIER3_ID");
		if($check[0]==1){
			$result.=$check[1];
			$result.="</TABLE>";
			&FT_LOG("INFO:Skipping datagenStart_MSS because it's been done. To force it to load put STARTDATAGEN_MSS force or STARTDATAGEN_KPI force in a config file");
			&FT_LOG("INFO:Start up EC_1 anyway to make sure they are running");
	
			if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
			executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
			}
			executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			
			return $result;
		}
	}else{
		$force=1;
	}
	my @stopped; #array of workflows we stopped
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			my @wf = ("eankmuk","SGEH","MSS");
			#disable all workflows EXCEPT MSS ones for singleblade. Reduces load
			@stopped = disableAllWorkflows(@wf);
		}
	}
	&FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
	my @ebsl=("EBSL");
	disableMatchingWorkflows(@ebsl);
	if(! -e "$concurrent_present"){
		&FT_LOG("INFO: This is sequential execution for $feature");
		executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
	}
	executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			
	if(!$remoteDG){
		copyBatchFiles($force);
		copyMssConfFiles();
		commitDgMzps();
		my $stillActive=disableWorkflowGroup("MSS.Workflow_MSS","MSS.MSSSimulator");
		preDeleteMssFiles();
		
		&FT_LOG("INFO:Running mss_topology_refresh.sh");
		executeThisWithLogging("/eniq/mediation_inter/M_E_MSS/bin/mss_topology_refresh.sh");
		if(!$stillActive){
			$result.=importMssDg();
		}else{
			&FT_LOG("INFO:Not importing DataGenWorkFlowsMSS.zip. The workflows could not be stopped so the import won't work properly");
			$result.="<tr><td>MSS.MSSSimulator workflow import</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}

		$ENV{"LOGNAME"} = "root";
		sleep(5);
		&FT_LOG("INFO:Running mss_upgrade_pushdata_permission_correction.sh");
		executeThisWithLogging('/eniq/home/dcuser/automation/RunCommandAsRoot.sh /eniq/mediation_inter/M_E_MSS/bin/mss_upgrade_pushdata_permission_correction.sh');
		$ENV{"LOGNAME"} = "dcuser";
		sleep(5);
		&FT_LOG("INFO:Running mss_populate.sh");
		executeThisWithLogging("/eniq/mediation_inter/M_E_MSS/bin/mss_populate.sh");
		my $res=updateMssDgWorkflows();
		if($res ne ""){
			$dataGenDir=$res;
		}
		enableMssDatagenWorkflows();
	}else{
		
		&FT_LOG("INFO:Running mss_topology_refresh.sh");
		executeThisWithLogging("/eniq/mediation_inter/M_E_MSS/bin/mss_topology_refresh.sh");
		$ENV{"LOGNAME"} = "root";
		sleep(5);
		&FT_LOG("INFO:Running mss_upgrade_pushdata_permission_correction.sh 1");
		executeThisWithLogging('/eniq/home/dcuser/automation/RunCommandAsRoot.sh /eniq/mediation_inter/M_E_MSS/bin/mss_upgrade_pushdata_permission_correction.sh');
		$ENV{"LOGNAME"} = "dcuser";
		sleep(5);
		&FT_LOG("INFO:Running mss_populate.sh");
		executeThisWithLogging("/eniq/mediation_inter/M_E_MSS/bin/mss_populate.sh");
		
		@mss_dirs=@nfs_dirs; # temporary fix to nss issue
		if(!grep(/^$/,@mss_dirs)){
			$dataGenDir=$mss_dirs[0];
			updateMssPreprocessingWorkflow($mss_dirs[0],$mss_dirs[1]);
			my @wfToRestart=(
			"MSS.WF01_PreProcessing.MSS_3",
			"MSS.WF01_PreProcessing.MSS_4");
			my @wfGroupsToRestart=(
			"MSS.WG01_PreProcessing");
			restartWorkflows(\@wfGroupsToRestart,\@wfToRestart);
			&FT_LOG("INFO:Running mss_topology_refresh.sh");
			&FT_LOG("INFO:Disabling unused workflows");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable MSS.WF*.0[4-9]");
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable MSS.WF*.1[0-5]");
			executeThisWithLogging("/eniq/mediation_inter/M_E_MSS/bin/mss_topology_refresh.sh");
		}else{
			&FT_LOG("ERROR:Remote datagen for MSS_3 and/or MSS_4 at $dgFilePath are not being populated");
		}
	}

	## Take the current time for the start of Data Generation ##
	my $dataGenStartTimePR = getTimeSpecLocal(time);
	&FT_LOG("INFO:DataGen started at:$dataGenStartTimePR \n");
	my @dirs=($dataGenDir);
	my @tables=("dc.event_e_mss_voice_cdr_raw","dc.event_e_mss_sms_cdr_raw");
	my $tableNames="";
	foreach my $table(@tables){
		$tableNames.="$table,";
	}
	my @check=dataGenCheck(\@ec_servers,\@dirs,\@tables,40,1,"HIER3_ID");
	$result.=$check[1];
	$result.="</TABLE>";
	
	if($arg=~/waitifstarting/){
		my $time = ($remoteDG?10:150);#10 mins for remote datagen, 150 mins for local
		&FT_LOG("INFO:Waiting $time minutes for data to load");
		sleep($time * 60);
	}
	
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
		executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");
		}
	}
	
	return $result;
}

sub dataGenCheck{
	my ($ec_servers,$dirs,$tables,$timeWarp,$printErrorsAndDoWait,$columnToCheck,$columnValue)=@_;
	my $resInTable = "";
	my $ctumPopulating=0;
	my $success=1;
	my $timeWarpSeconds=$timeWarp*60;
	my $startTime=time-($timeWarpSeconds);
	my @tablesDates=();
	my $tableNames=join(',',@$tables);
	
	if($printErrorsAndDoWait){
		&FT_LOG("INFO:Checking latest date in event tables");
		$startTime=time;
		
		foreach my $table(@$tables){
			my @dat=sqlSelect("SELECT max(datetime_id) from $table");
			if(lc($dat[0]) !~ m/:/){
				&FT_LOG("INFO:Current latest date in table $table is null. After waiting, any valid date from the last two days will be accepted");
				push(@tablesDates,2880);
			}else{
				&FT_LOG("INFO:Current latest date in table $table: $dat[0]");
				push(@tablesDates,$dat[0]);
			}
		}
		
		&FT_LOG("INFO:Sleeping for up to $timeWarp mins to allow raw tables '$tableNames' to populate");
		for(my $i=1;$i<=$timeWarp;$i++){
			sleep 60;
			&FT_LOG("INFO:$i minutes of a maximum $timeWarp have passed. Checking directories and tables");
			$success=1;
			foreach my $dir(@$dirs){
				my $res = verifyDataGenDir($ec_servers,"$dir",$startTime,0);
				if($res=~m/Exception|Fail|Error/){
					$success=0;
					&FT_LOG("INFO:Directories are not yet populating. Continuing to wait");
					last;
				}
			}
			if($success){
				my $i=0;
				foreach my $table(@$tables){
					my $res = verifyDataGenLoading("$table",$tablesDates[$i],$columnToCheck,$columnValue,0);
					$i++;
					if($res=~m/Exception|Fail|Error/){
						&FT_LOG("INFO:Tables are not yet populating. Continuing to wait");
						$success=0;
						last;
					}
				}
			}
			if($success){
				&FT_LOG("INFO:Tables and directories are populating ok. Proceeding to log info to table");
				last;
			}
		}
	}else{
		foreach my $table(@$tables){
			push(@tablesDates,$timeWarp);
		}
	}
	
	# Log results for directories
	$success=1;
	foreach my $dir(@$dirs){
		my $res = verifyDataGenDir($ec_servers,"$dir",$startTime,$printErrorsAndDoWait);
		if($res!~m/Exception|Fail|Error/){
			$resInTable.=logAndGenerateTableEntry($res,1);
		}else{
			if($printErrorsAndDoWait==1){
				$resInTable.=logAndGenerateTableEntry($res,0);
			}
			$success=0;
		}
	}
	
	# Log results for tables iff directories are loading
	if($success==1 || $feature=~m/DVTP/){
		my $i=0;
		foreach my $table(@$tables){
			my $res = verifyDataGenLoading("$table",$tablesDates[$i],$columnToCheck,$columnValue,$printErrorsAndDoWait);
			$i++;
			if($res!~m/Exception|Fail|Error/){
				$resInTable.=logAndGenerateTableEntry($res,1);
			}else{
				if($printErrorsAndDoWait==1){
					$resInTable.=logAndGenerateTableEntry($res,0);
				}
				$success=0;
			}
		}
	}
	
	#EQEV-22189: Modifications to exclude test case execution where RAW tables are not populated.
	if ($success==0){
		&FT_LOG("Data Loading is not successful\n");
		$dataloading_status = 0;
	}
	return ($success,$resInTable);
}

sub dataGenCheck_onCEP{
	my ($dirs,$tables,$timeWarp,$printErrorsAndDoWait,$columnToCheck,$columnValue)=@_;
	my $resInTable = "";
	my $ctumPopulating=0;
	my $success=1;
	my $timeWarpSeconds=$timeWarp*60;
	my $startTime=time-($timeWarpSeconds);
	my @tablesDates=();
	my $tableNames=join(',',@$tables);
	
	if($printErrorsAndDoWait){
		&FT_LOG("INFO:Checking latest date in event tables");
		$startTime=time;
		
		foreach my $table(@$tables){
			my @dat=sqlSelect("SELECT max(datetime_id) from $table");
			if(lc($dat[0]) !~ m/:/){
				&FT_LOG("INFO:Current latest date in table $table is null. After waiting, any valid date from the last two days will be accepted");
				push(@tablesDates,2880);
			}else{
				&FT_LOG("INFO:Current latest date in table $table: $dat[0]");
				push(@tablesDates,$dat[0]);
			}
		}
		
		&FT_LOG("INFO:Sleeping for up to $timeWarp mins to allow raw tables '$tableNames' to populate");
		for(my $i=1;$i<=$timeWarp;$i++){
			sleep 60;
			&FT_LOG("INFO:$i minutes of a maximum $timeWarp have passed. Checking directories and tables");
			$success=1;
			foreach my $dir(@$dirs){
				my $res = verifyDataGenDir_onCEP("$dir",$startTime,0);
				if($res=~m/Exception|Fail|Error/){
					$success=0;
					&FT_LOG("INFO:Directories are not yet populating. Continuing to wait");
					last;
				}
			}
			if($success){
				my $i=0;
				foreach my $table(@$tables){
					my $res = verifyDataGenLoading("$table",$tablesDates[$i],$columnToCheck,$columnValue,0);
					$i++;
					if($res=~m/Exception|Fail|Error/){
						&FT_LOG("INFO:Tables are not yet populating. Continuing to wait");
						$success=0;
						last;
					}
				}
			}
			if($success){
				&FT_LOG("INFO:Tables and directories are populating ok. Proceeding to log info to table");
				last;
			}
		}
	}else{
		foreach my $table(@$tables){
			push(@tablesDates,$timeWarp);
		}
	}
	
	# Log results for directories
	$success=1;
	foreach my $dir(@$dirs){
		my $res = verifyDataGenDir_onCEP("$dir",$startTime,$printErrorsAndDoWait);
		if($res!~m/Exception|Fail|Error/)
		{
			$resInTable.=logAndGenerateTableEntry($res,1);
		}else{
			if($printErrorsAndDoWait==1){
				$resInTable.=logAndGenerateTableEntry($res,0);
			}
			$success=0;
		}
	}
	
	# Log results for tables iff directories are loading
	if($success==1){
		my $i=0;
		foreach my $table(@$tables){
			my $res = verifyDataGenLoading("$table",$tablesDates[$i],$columnToCheck,$columnValue,$printErrorsAndDoWait);
			$i++;
			if($res!~m/Exception|Fail|Error/){
				$resInTable.=logAndGenerateTableEntry($res,1);
			}else{
				if($printErrorsAndDoWait==1){
					$resInTable.=logAndGenerateTableEntry($res,0);
				}
				$success=0;
			}
		}
	}
	
	#EQEV-22189: Modifications to exclude test case execution where RAW tables are not populated.
	if ($success==0){
		&FT_LOG("Data Loading is not successful\n");
		$dataloading_status = 0;
	}
	return ($success,$resInTable);
}

sub dataGenCheck_tablesOnly {
	my ($tables,$timeWarp,$printErrorsAndDoWait,$columnToCheck,$columnValue)=@_;
	my $resInTable = "";
	my $ctumPopulating=0;
	my $success=1;
	my $timeWarpSeconds=$timeWarp*60;
	my $startTime=time-($timeWarpSeconds);
	my @tablesDates=();
	my $tableNames=join(',',@$tables);
	
	if($printErrorsAndDoWait){
		&FT_LOG("INFO:Checking latest date in event tables");
		$startTime=time;
		
		foreach my $table(@$tables){
			my @dat=sqlSelect("SELECT max(datetime_id) from $table");
			if(lc($dat[0]) !~ m/:/){
				&FT_LOG("INFO:Current latest date in table $table is null. After waiting, any valid date from the last two days will be accepted");
				push(@tablesDates,2880);
			}else{
				&FT_LOG("INFO:Current latest date in table $table: $dat[0]");
				push(@tablesDates,$dat[0]);
			}
		}
		
		&FT_LOG("INFO:Sleeping for up to $timeWarp mins to allow raw tables '$tableNames' to populate");
		for(my $i=1;$i<=$timeWarp;$i++){
			sleep 60;
			&FT_LOG("INFO:$i minutes of a maximum $timeWarp have passed. Checking tables");
			$success=1;
			
			if($success){
				my $i=0;
				foreach my $table(@$tables){
					my $res = verifyDataGenLoading("$table",$tablesDates[$i],$columnToCheck,$columnValue,0);
					$i++;
					if($res=~m/Exception|Fail|Error/){
						&FT_LOG("INFO:Tables are not yet populating. Continuing to wait");
						$success=0;
						last;
					}
				}
			}
			if($success){
				&FT_LOG("INFO:Tables are populating ok. Proceeding to log info to table");
				last;
			}
		}
	}else{
		foreach my $table(@$tables){
			push(@tablesDates,$timeWarp);
		}
	}
	
	my $i=0;
	foreach my $table(@$tables){
		my $res = verifyDataGenLoading("$table",$tablesDates[$i],$columnToCheck,$columnValue,$printErrorsAndDoWait);
		$i++;
		if($res!~m/Exception|Fail|Error/){
			$resInTable.=logAndGenerateTableEntry($res,1);
		}else{
			if($printErrorsAndDoWait==1){
				$resInTable.=logAndGenerateTableEntry($res,0);
			}
			$success=0;
		}
	}
	
	#EQEV-22189: Modifications to exclude test case execution where RAW tables are not populated.
	if ($success==0){
		&FT_LOG("Data Loading is not successful\n");
		$dataloading_status = 0;
	}
	return ($success,$resInTable);
}

sub dataGenStart_LTE{
	my $arg=shift;
	my $host = getHostName();
	my $force=0;
	my $result=qq{
	<h3>START LTE DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>TEST STAGES</th>
	 <th>STATUS</th>
	</tr>
	};
	&FT_LOG("INFO:dataGenStartLTE() LTE\n");
	my @arr=();
	my $ctumDir="/eniq/data/pmdata/eventdata/00/CTRS/ctum/5min";
	my $traceDir="/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1";
	my $lteCfaRfDir="/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa_rf/5min/dir1";
	my $rfDatasetEC2Dir="/eniq/mediation_inter/cache/M_E_LTEEFA/ue_eval/EC_LTEEFA_2";
	my $rfDatasetEC3Dir="/eniq/mediation_inter/cache/M_E_LTEEFA/ue_eval/EC_LTEEFA_3";
	
	my @ec_servers=("ec_lteefa_1","ec_lteefa_2","ec_lteefa_3");
	
	updateReservedDataTimeRanges();
	if($remoteDG){
		if( !-d "$dgNfsPath/$host"){
			open(TEMP,">$dgNfsPath/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("$dgNfsPath/tempfile.$$");
			makePath("$dgNfsPath/$host");
		}
	}
	
	if($arg!~m/force/){
		my @dirs=($traceDir,$ctumDir);
		my @rfDirs=($lteCfaRfDir,$rfDatasetEC2Dir,$rfDatasetEC3Dir);
		my @tables=("dc.event_e_lte_cfa_err_raw","dc.event_e_lte_hfa_err_raw");
		my @rfTables=("dc.event_e_lte_cfa_array_erab_err_raw");
		my @check=dataGenCheck(\@ec_servers,\@dirs,\@tables,30,0,"HIER3_ID");
		my @rfCheck=dataGenCheck(\@ec_servers,\@rfDirs,\@rfTables,30,0,"SERVING_RSRP");
		
		if($check[0]==1){
			$result.=$check[1];
			$result.="</TABLE>";
			&FT_LOG("INFO:Skipping datagenStart_LTE because it's been done. To force it to load put STARTDATAGEN_LTE force in a config file");
			&FT_LOG("INFO:Start up EC_1, EC_LTEEFA_1,EC_LTEEFA_2 and EC_LTEEFA_3 anyway to make sure they are running");
			if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
				executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
			}
		    executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_1 '");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_2 '");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_3 '");			
			return $result;
		}
	}else{
		$force=1;
	}

	if(!$remoteDG){
		copyBatchFiles($force);
		commitDgMzps();
		$result.=installLCHS($force);
		if( runCommand("ssh dcuser\@ec_1 'ls -ld /eniq/data/pmdata/eventdata/00/CTRS | grep ^l'",0) ){
			executeThisWithLogging("ssh dcuser\@ec_1 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS 2>/dev/null'");
		}
	}else{
		my $linkExists=runCommand("ssh dcuser\@ec_lteefa_1 'ls -ld /eniq/data/pmdata/eventdata/00/CTRS | grep ^l'",0);
		if(!$linkExists || $arg=~m/force/){
			executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS 2>/dev/null'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS 2>/dev/null'");
			executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'rm -rf /eniq/data/pmdata/eventdata/00/CTRS 2>/dev/null'");
			runCommand("ssh dcuser\@ec_lteefa_1 'mkdir -p /eniq/data/pmdata/eventdata/00/'",1);
			runCommand("ssh dcuser\@ec_lteefa_2 'mkdir -p /eniq/data/pmdata/eventdata/00/'",1);
			runCommand("ssh dcuser\@ec_lteefa_3 'mkdir -p /eniq/data/pmdata/eventdata/00/'",1);
			my $success=runCommand("ssh dcuser\@ec_lteefa_1 'cd /eniq/data/pmdata/eventdata/00; ln -s $dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/CTRS'",1);
			runCommand("ssh dcuser\@ec_lteefa_2 'cd /eniq/data/pmdata/eventdata/00; ln -s $dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/CTRS'",1);
			runCommand("ssh dcuser\@ec_lteefa_3 'cd /eniq/data/pmdata/eventdata/00; ln -s $dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/CTRS'",1);
			if($success==1){
				&FT_LOG("INFO:Symbolic link created to $dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/CTRS at /eniq/data/pmdata/eventdata/00/CTRS");
			}else{
				&FT_LOG("ERROR:Failed to create symbolic link created to $dgNfsPath/$host/50files/eniq/data/pmdata/eventdata/00/CTRS at /eniq/data/pmdata/eventdata/00/CTRS on ec_lteefa_1");
			}
		}else{
			&FT_LOG("INFO:Symbolic already exists at /eniq/data/pmdata/eventdata/00/CTRS on ec_lteefa_1. Put dataGenStart_LTE force in a config file to force re-creation");
		}
	}	

	&FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
	my @ebsl=("EBSL");
	disableMatchingWorkflows(@ebsl);
	if(! -e "$concurrent_present"){
		&FT_LOG("INFO: This is sequential execution for $feature");
		executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
		executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
	}
	executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
	executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_1 '");
	executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_2 '");
	executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_3 '");
		
	&FT_LOG("INFO:Checking ECs status now");
	my @contents=executeThisQuiet("ssh dcuser\@ec_lteefa_1 'source ~/.profile; ec status'");
	push @contents,executeThisQuiet("ssh dcuser\@ec_lteefa_2 'source ~/.profile; ec status'");
	push @contents,executeThisQuiet("ssh dcuser\@ec_lteefa_3 'source ~/.profile; ec status'");
	if(grep(/EC_LTEEFA_\d is not running/,@contents)){
		&FT_LOG("Some or all of the EC_LTEEFA processes is not running. EC_LTEEFA_1|2|3 restart");
		executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_1 '");
	    executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_2 '");
	    executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup  EC_LTEEFA_3 '");
	}elsif(grep(/EC1 is not running/,@contents)){
		&FT_LOG("EC1 is not running. EC1 restart");
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
	}
	sleep 120;
	my @stopped; #array of workflows we stopped
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			my @wf = ("LTEEFA", "LCHS");
			#disable all workflows EXCEPT $wf ones for singleblade. Reduces load
			@stopped = disableAllWorkflows(@wf);
		}
	}
	my @wfToEnable=(
		"LTEEFA_EE.WF00_ParsingLog_Inter.logging",
		"LTEEFA_EE.WF05_LoadBalanceCtumEvents.workflow_1",
		"LTEEFA_EE.WF05_RefreshTopologyCache.now",
		"LTEEFA_EE.WF05_RefreshTopologyCache.scheduled",
		"LTEEFA_EE.WF06_Processor.workflow_1",
		"LTEEFA_EE.WF06_Processor.workflow_2",
		"LTEEFA_EE.WF06_Processor.workflow_3",
		"LTEEFA_EE.WF06_Processor.workflow_4",
		"LTEEFA_EE.WF06_Processor.workflow_5",
		"LTEEFA_EE.WF07_RefreshTopologyCache.schedule");
		
	my @wfGroupsToEnable=(
		"LTEEFA_EE.WG00_LogParsing_Inter",
		"LTEEFA_EE.WG05_PreLoading",
		"LTEEFA_EE.WG06_Processing",
		"LTEEFA_EE.WG07_Processing",
		"LTEEFA_EE.WG08_Processing",
		"LTEEFA_EE.WG09_Processing",
		"LTEEFA_EE.WG10_Processing");

	my @status;
	&FT_LOG("INFO:mg_topology_refresh.sh");
	my $success = refreshTopologyAndWait("/eniq/mediation_inter/M_E_LTEEFA/bin/mg_topology_refresh.sh",1200);
	checkRefreshTopologyResult($success,"/eniq/mediation_inter/M_E_LTEEFA/bin/mg_topology_refresh.sh");	
	if(!$remoteDG){
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop LCHS.Analysis_eNodeB_CT_11B.MME1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop LCHS.Analysis_eNodeB_CT_12A.MME1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop LCHS.Analysis_eNodeB_CT_12B.MME1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop LCHS.Analysis_mme_Ctum.MME1");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop LCHS.Analysis_mme_Ctum.MME2");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop LCHS.Analysis_mme_Ctum.MME3");
		
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop LCHS.WFG_LCHS_11B");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop LCHS.WFG_LCHS_12A");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop LCHS.WFG_LCHS_12B");	
		
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable LCHS.WFG_LCHS_11B");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable LCHS.WFG_LCHS_12A");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable LCHS.WFG_LCHS_12B");
	
		my @workflows=("LCHS.Analysis_eNodeB_CT_11B","LCHS.Analysis_eNodeB_CT_12A","LCHS.Analysis_eNodeB_CT_12B","LCHS.Analysis_mme_Ctum");
		my @wfDirs=($traceDir,$traceDir,$traceDir,$ctumDir);
		my $i=0;
		foreach my $workflow(@workflows){
			unlink("/tmp/ltedatagenworkflows.csv");
			@status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport $workflow /tmp/ltedatagenworkflows.csv");
			if(grep(/Export finished/,@status) && -f "/tmp/ltedatagenworkflows.csv"){
				&FT_LOG("INFO:$workflow workflow exported successfully\n");
			}
			else{
				&FT_LOG("ERROR:$workflow workflow did not export\n");
			}

			open(CSV,"</tmp/ltedatagenworkflows.csv");
			my @datagenWorkflows=<CSV>;
			close(CSV);
			foreach (@datagenWorkflows){
				if(m/MME/){
					s/\"\/.*\"$/\"$wfDirs[$i]\"/;
					print;
				}
			}
			unlink("/tmp/ltedatagenworkflows.csv");
			open(CSV,">/tmp/lteeditedworkflows.csv");
			foreach(@datagenWorkflows){
				print CSV;
			}
			close(CSV);
			executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport $workflow /tmp/lteeditedworkflows.csv");
			unlink("/tmp/lteeditedworkflows.csv");
			$i++;
		}
		push(@wfToEnable,"LCHS.Analysis_eNodeB_CT_11B.MME1");
		push(@wfToEnable,"LCHS.Analysis_eNodeB_CT_12A.MME1");
		push(@wfToEnable,"LCHS.Analysis_eNodeB_CT_12B.MME1");
		push(@wfToEnable,"LCHS.Analysis_mme_Ctum.MME1");
		push(@wfToEnable,"LCHS.Analysis_mme_Ctum.MME2");
		push(@wfToEnable,"LCHS.Analysis_mme_Ctum.MME3");
		
		push(@wfGroupsToEnable,"LCHS.WFG_LCHS_11B");
		push(@wfGroupsToEnable,"LCHS.WFG_LCHS_12A");
		push(@wfGroupsToEnable,"LCHS.WFG_LCHS_12B");
	}
	#We use provision_workflows.sh to schedue relevant WFs and WFGroup instead of enable them manually
	&FT_LOG("INFO:Enable relevant WFs and WFGroups by invoking provision_workflow.sh");
	executeThisWithLogging("ssh dcuser\@ec_lteefa_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
	executeThisWithLogging("ssh dcuser\@ec_lteefa_2 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
	executeThisWithLogging("ssh dcuser\@ec_lteefa_3 'source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh'");
	
	#foreach(@wfToEnable){
	#	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable $_");
	#}
	#foreach my $group(@wfGroupsToEnable){
	#	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable $group");
	#	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart $group");
	#}
	
	## Take the current time for the start of Data Generation ##
	my $dataGenStartTimePR = getTimeSpecLocal(time);
	&FT_LOG("INFO:DataGen started at:$dataGenStartTimePR \n");
	my @dirs=($traceDir,$ctumDir);
	my @rfDirs=($lteCfaRfDir,$rfDatasetEC2Dir,$rfDatasetEC3Dir);
	my @tables=("dc.event_e_lte_cfa_err_raw","dc.event_e_lte_hfa_err_raw");
	my @rfTables=("dc.event_e_lte_cfa_array_erab_err_raw");
	my $tableNames="";
	foreach my $table(@tables){
		$tableNames.="$table,";
	}
	my @check=dataGenCheck(\@ec_servers,\@dirs,\@tables,60,1,"HIER3_ID");
	foreach my $table(@rfTables){
		$tableNames.="$table,";
	}
	my @rfcheck=dataGenCheck(\@ec_servers,\@rfDirs,\@rfTables,60,1,"SERVING_RSRP");
	&FT_LOG("INFO:Will return the status of the relevant workFlows presently");
	workflowStatus(\@wfGroupsToEnable);
	$result.=$check[1];
	$result.=$rfcheck[1];
	$result.="</TABLE>";
	if($arg=~/waitifstarting/){
		if(isMultiBladeServer()){
			my $time = ($remoteDG?10:150);#10 mins for remote datagen, 150 mins for local
			&FT_LOG("INFO:Waiting $time minutes for data to load");
			sleep($time * 60);
		}else{
			sleep(600);#sleep more time for ST deployment
		}
	}
	
	if(@stopped){
		executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");	
	}
	
	return $result;
}

sub dataGenStart_3GSessionBrowser{
	my $arg=shift;
	my $force=0;
	my  $result=qq{
		<h3>START 3G Session Browser DATAGEN</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		   <tr>
			 <th>TEST STAGES</th>
			 <th>STATUS</th>
		   </tr>
	};
	my $host = getHostName();
	
	&FT_LOG("INFO:dataGenStart_3GSessionBrowser()\n");
	
	# cannot start 3g Session Browser loading if no cep blade
	if(!hasCepBlade()){
		$result.=logAndGenerateTableEntry("No CEP blade found on server, cannot load 3g Session Browser Data",1);
		$result.="</TABLE>";
		return $result;
	}
	
	my @dirsToCheck_cep=("/ossrc/data/pmMediation/eventData/GpehEvents_CEP/SubNetwork=RNC09/MeContext=RNC09",
				"/ossrc/data/pmMediation/eventData/SgehEvents_CEP/ManagedElement=SGSN09","/ossrc/data/pmMediation/eventData/PCPEvents_CEP/PCP1/staple/3G/tcpta_partial/454_06_8900",
				"/ossrc/data/pmMediation/eventData/PCPEvents_CEP/PCP1/captool/3G/454_06_8900");
	my @tablesToCheck_cep=("EVENT_E_RAN_HFA_SOHO_ERR_RAW","EVENT_E_RAN_HFA_IFHO_ERR_RAW","EVENT_E_RAN_HFA_IRAT_ERR_RAW","EVENT_E_RAN_HFA_HSDSCH_ERR_RAW",
				"EVENT_E_RAN_CFA_ERR_RAW","EVENT_E_RAN_SESSION_RAW","EVENT_E_CORE_SESSION_RAW","EVENT_E_RAN_SESSION_CELL_VISITED_RAW","EVENT_E_RAN_SESSION_INTER_OUT_HHO_RAW",
				"EVENT_E_RAN_SESSION_INTER_SYS_UTIL_RAW","EVENT_E_RAN_SESSION_RRC_MEAS_RAW","EVENT_E_RAN_SESSION_SUC_HSDSCH_CELL_CHANGE_RAW","EVENT_E_USER_PLANE_TCP_RAW","EVENT_E_USER_PLANE_CLASSIFICATION_RAW");
	
	my $sgeh_dir="/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	my $remote_sgeh_dir="$dgNfsPath/$host/50files/ossrc/data/pmMediation/eventData/sgeh/dir1";
	my @tablesToCheck_sgeh=("EVENT_E_SGEH_ERR_RAW"); #,"EVENT_E_SGEH_SUC_RAW"
	my @dirsToCheck_sgeh=("$sgeh_dir");
	
	my @check_sgeh=();
	my @check_cep=();
	my @ec_servers=("ec_sgeh_1");
	if($arg =~ m/force/i){
		$force=1;
		@check_sgeh=(0);
		@check_cep=(0);
	}
	
	if($remoteDG){
		if( !-d "$dgNfsPath/$host"){
			open(TEMP,">$dgNfsPath/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("$dgNfsPath/tempfile.$$");
			makePath("$dgNfsPath/$host");
		}
	}
	
	$result.=dataGenStart_OnCEP();
	
	if(!$force){
		@check_sgeh=dataGenCheck(\@ec_servers,\@dirsToCheck_sgeh,\@tablesToCheck_sgeh,30,0,"hierarchy_3","smartone_R:RNC09:RNC09");
		@check_cep=dataGenCheck_onCEP(\@dirsToCheck_cep,\@tablesToCheck_cep,45,0,"imsi");
		if($check_sgeh[0] == 1){
			&FT_LOG("INFO:Skipping dataGenStart_3GSessionBrowser on MZ because it's been done. To force it to load put STARTDATAGEN_3GSESSIONBROWSER force in a config file");
			&FT_LOG("INFO:Start up EC_1 and EC_SGEH_1 anyway to make sure they are running");
			if(! -e "$concurrent_present"){
				&FT_LOG("INFO: This is sequential execution for $feature");
				executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
				executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
			}
			executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
			executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
			$result.=$check_sgeh[1];
		}
		
		if($check_cep[0] == 1){
			&FT_LOG("INFO:Skipping dataGenStart_3GSessionBrowser on CEP because it's been done. To force it to load put STARTDATAGEN_3GSESSIONBROWSER force in a config file");
			$result.=$check_cep[1];
		}
		
		if($check_sgeh[0] == 1 && $check_cep[0] == 1){
			$result.="</TABLE>";
			return $result;
		}
	}
	
	## SGEH Datagen ##
	my @stopped;
	my $totalDirs=5;
	if($check_sgeh[0] == 0){
		&FT_LOG("INFO:Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
		my @ebsl=("EBSL");
		disableMatchingWorkflows(@ebsl);
		if(! -e "$concurrent_present"){
			&FT_LOG("INFO: This is sequential execution for $feature");
			executeThisWithLogging("ssh dcuser\@ec_ltees_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_2 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_2 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_3 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_3 '");
			executeThisWithLogging("ssh dcuser\@ec_ltees_4 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_4 '");
		}
		executeThisWithLogging("ssh dcuser\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'");
		executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC_SGEH_1'");
		
		
		if($arg!~m/noRollingDisable/){	
			if(!isMultiBladeServer()){
				my @wf=("SGEH");
				@stopped=disableAllWorkflows(@wf);
			}
		}
		
		executeThisWithLogging("ssh dcuser\@ec_sgeh_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh/dir2 2>/dev/null'");
		runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh'",1);
		runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir1'",1);
		runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir3'",1);
		runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir4'",1);
		
		#for(my $i=1;$i<$totalDirs;$i++){
		#		runCommand("ssh dcuser\@ec_sgeh_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir$i'",1);
		#	}
		my $success=runCommand("ssh dcuser\@ec_sgeh_1 'ln -s $remote_sgeh_dir $sgeh_dir'",1);
		executeThisQuiet("ssh dcuser\@ec_sgeh_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_SGEH/bin;./provision_workflows.sh'");
		executeThisWithLogging("/eniq/mediation_inter/M_E_SGEH/bin/mg_topology_refresh.sh");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2a");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2b");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2a SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir2_a");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupaddwf SGEH.WFG_SGEH_Processing_NFS_OSSRC1_2b SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir2_b");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir2_a");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir2_b");
		#enable SGEH workflows
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_SGEH_Processing_NFS_OSSRC1_*");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable SGEH.WFG_Cell_Lookup_Refresh_DB");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.now");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_Cell_Lookup_Refresh_DB.scheduled");
		executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir*");

	}
	
	my $dataGenStartTimePR = getTimeSpecLocal(time);
	&FT_LOG("INFO:DataGen started at:$dataGenStartTimePR \n");
	if($check_sgeh[0] == 0){
		@check_sgeh=dataGenCheck(\@ec_servers,\@dirsToCheck_sgeh,\@tablesToCheck_sgeh,65,1,"hierarchy_3","smartone_R:RNC09:RNC09");
		$result.=$check_sgeh[1];
	}
	
	if($check_cep[0] == 0){
		@check_cep=dataGenCheck_onCEP(\@dirsToCheck_cep,\@tablesToCheck_cep,45,1,"imsi");
		$result.=$check_cep[1];
	}
	$result.="</TABLE>";
	
	if($arg!~m/noRollingDisable/){	
		if(!isMultiBladeServer()){
			executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable @stopped");
		}
	}
	return $result;
}

sub editCEPConfigFiles{
	my $cep_ip;
	my $cep_host;

	open(FILE,"</eniq/installation/config/cep_mediation.ini");
	my @cep_file=<FILE>;
	close(FILE);
	
	($cep_ip)=grep(/SERVICES_IP=/,@cep_file);
	$cep_ip=~s/SERVICES_IP=//;
	chomp($cep_ip);
	
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml /eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml /eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml'");
	my @cep_config_files = ("/eniq/home/dcuser/automation/topology/CEP_configs/network_elements.xml","/eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml","/eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml");
	foreach my $cep_config_file (@cep_config_files){
		open(FILE,"<$cep_config_file");
		my @cep_config=<FILE>;
		close(FILE);
		
		open(FILE2,">/tmp/cep_config.xml");
		foreach my $line (@cep_config){
			if($line =~ m/ip="\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}"/){
				$line=~s/ip="\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}"/ip="$cep_ip"/;
			}
			if($line =~ m%<interface>\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}</interface>%){
				$line=~s%<interface>\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}</interface>%<interface>$cep_ip</interface>%;
			}
			print FILE2 $line;
		}
		close(FILE2);
		executeThisWithLogging("cp /tmp/cep_config.xml $cep_config_file 2>/dev/null");
	}
}

sub setCEPForWcdmaBacklog{
	my $path_to_two_rops = "/tmp/wcdma";
	my $remote_path_to_two_rops;
	if($feature =~ /GritWCDMA/){
		$remote_path_to_two_rops = "/net/atclvm559.athtem.eei.ericsson.se/package/GRIT/wcdma_cfahfa/mzInputFiles/*.gz";
	}
	else {
		$remote_path_to_two_rops = "/net/atclvm559.athtem.eei.ericsson.se/ossrc/package/WCDMACFAHFA_ROPS.tar";
	}	
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/rm -rf $path_to_two_rops 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 'mkdir -p $path_to_two_rops 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/bin/cp $remote_path_to_two_rops $path_to_two_rops 2>/dev/null'");
	if($feature !~ /GritWCDMA/){
		executeThisWithLogging("ssh dcuser\@cep_med_1 'cd $path_to_two_rops; /bin/tar xvf $path_to_two_rops/WCDMACFAHFA_ROPS.tar 2>/dev/null'");
		$path_to_two_rops = "$path_to_two_rops/NewSeleniumRop/";
	}
	#Remake path to RNC09 directory
	my ($rnc09_directory) = executeThisWithLogging("ssh dcuser\@cep_med_1 'grep RNC09 /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml | grep directory'");
	chomp($rnc09_directory);
	$rnc09_directory=~s%\s*</?directory>\s*%%g;
	$rnc09_directory.="/MeContext=RNC09";
	print "\n\nRNC09 directory: >$rnc09_directory<\n\n";
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/rm -rf /ossrc/data/pmMediation/eventData 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/mkdir -p $rnc09_directory 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/rm -f $rnc09_directory/A* 2>/dev/null'");
	
	if($feature !~ /GritWCDMA/){
		#Get Yesterday date
		my ($yesterday_date) = executeThisWithLogging("ssh dcuser\@cep_med_1 '/bin/date  --date=\"1 days ago\" +\"%Y%m%d\"'");
		chomp($yesterday_date);
	
		#Update date on .gz files
		my @rop_files = executeThisWithLogging("ssh dcuser\@cep_med_1 'ls -l $path_to_two_rops'");
		@rop_files = grep(/.*\.gz/,@rop_files);
		foreach my $rop_file (@rop_files){
			chomp($rop_file);
			$rop_file=~s/.*A2014/A2014/;
			my $new_rop_file = $rop_file;
			$new_rop_file=~s/20141104/$yesterday_date/;
			executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/mv $path_to_two_rops/$rop_file $path_to_two_rops/$new_rop_file 2>/dev/null'");
		}
	}
	else {
		my $wcdmaFileName = `ls /net/atclvm559.athtem.eei.ericsson.se/package/GRIT/wcdma_cfahfa/mzInputFiles | cut -d "." -f1 | sort | uniq`;
		chomp($wcdmaFileName);

		my $currentFileName1 = fileNameDate();
		chomp($currentFileName1);
		my $currentFileName = "A"."$currentFileName1";
		chomp($currentFileName);
		&FT_LOG("CurrentFileName for GritWCDMA is $currentFileName \n");
		
		my @rop_files = executeThisWithLogging("ssh dcuser\@cep_med_1 'ls $path_to_two_rops'");
		@rop_files = grep(/.*\.gz/,@rop_files);
		foreach my $rop_file (@rop_files){
			chomp($rop_file);
			my $new_rop_file = `echo $rop_file | sed s/$wcdmaFileName/$currentFileName/g`;
			executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/mv $path_to_two_rops/$rop_file $path_to_two_rops/$new_rop_file 2>/dev/null'");
		}
	}
	#Move .gz files to RNC09 directory
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/mv $path_to_two_rops/*.gz $rnc09_directory 2>/dev/null'");
	
	#Update Config.xml file for backlog recovery
	my $cep_config_file="/eniq/home/dcuser/cep_config.xml";
	executeThisWithLogging("ssh dcuser\@cep_med_1 'cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/config.xml $cep_config_file 2>/dev/null'");
	open(FILE,"<$cep_config_file");
	my @cep_config=<FILE>;
	close(FILE);	
	open(FILE,">$cep_config_file");
	foreach my $line (@cep_config){
		$line=~s%value>75%value>2880%;
		print FILE $line;
	}
	close(FILE);
	executeThisWithLogging("ssh dcuser\@cep_med_1 'cp $cep_config_file /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/config.xml 2>/dev/null'");
	
	#Remove /output/*.last
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/rm -rf /output/*.last 2>/dev/null'");
}


sub dataGenStart_OnCEP{
	&FT_LOG("INFO:dataGenStart_OnCEP()\n");
	editCEPConfigFiles();
	my $result = "";
	my ($hostname) = executeThisWithLogging("ssh dcuser\@cep_med_1 'hostname 2>/dev/null'");
	chomp($hostname);
	
	#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
	if ( $hostname eq "cep" ){ 
		$hostname = getHostName();
	}
	
	my $remotePath = '/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/'.$hostname.'/50files/ossrc/data/pmMediation/eventData';
	my $localPath = '/ossrc/data/pmMediation/eventData';
	
	#add cron to check if datagen folders are mounted correctly
	#on the CEP mediation server (Stale NFS file handle issue on /net).
	my @cron_table = executeThisQuiet("crontab -l");
	if(!grep(/check_stale_nfs.pl/,@cron_table)){
		open(FILE, ">/tmp/tmp_cron.$$");
		foreach my $cron_entry (@cron_table){
			print FILE $cron_entry;
		}
		print FILE "00,05,10,15,20,25,30,35,40,45,50,55 * * * * cd /eniq/home/dcuser/automation; ./check_stale_nfs.pl\n";
		close(FILE);
		executeThisWithLogging("chmod 755 /eniq/home/dcuser/automation/check_stale_nfs.pl");
		executeThisWithLogging("crontab /tmp/tmp_cron.$$");
		unlink("/tmp/tmp_cron.$$");
	}
	
	if( !-d "/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/$hostname"){
		open(TEMP,">/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$");
		print TEMP "temp";
		close TEMP;
		unlink("/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$");
		makePath("/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/$hostname");
	}
	
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml.bkp'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml.bkp'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml.bkp'");
	
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py ntpdate nasconsole 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation stop 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /eniq/home/dcuser/automation/topology/CEP_configs/network_elements.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py rm -rf /ossrc/data/pmMediation/eventData 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py mkdir -p /ossrc/data/pmMediation 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py ln -s $remotePath $localPath 2>/dev/null'");

#	EQEV-17898
#	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service autofs restart 2>/dev/null'");
#	sleep 120;
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation start 2>/dev/null'");
	
	wcdmaDbSetup();
	
	my @cep_status = executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation status 2>/dev/null'");
	if(grep(/Application is running/,@cep_status)){
		$result.=logAndGenerateTableEntry("cep-mediation service started on cep blade",1);
	}else{
		$result.=logAndGenerateTableEntry("Failed to start cep-mediation service on cep blade",0);
	}
	
	return $result;
}

sub dataGenStart_WCDMA_OnCEP{
	&FT_LOG("INFO:dataGenStart_WCDMA_OnCEP()\n");
	
	if((! -e "$wcdmaData_complete") && (-e "$concurrent_present")){
		system "touch $wcdmaData_complete";
	}
	######EQEV-22042 : Topology setup issue for WCDMACFA\HFA Automation suite Test cases####################################
    #For WCDMA feature in table GROUP_TYPE_E_MCC_MNC, the value for MCC and MNC for GROUP_NAME HOMENETWORK should 454 and 06
    #Fix is added to set the MCC and MNC values of HOMENETWORK to 454 and 06 respectively and change the GROUP_NAME for
    #HOMENETWORK to some random name if there are any other values found

	my @count=sqlSelect("Select COUNT(*) from group_type_e_mcc_mnc where GROUP_NAME = 'HOMENETWORK'");
	my $count1 = @count[0];
	if ($count1 == '0'){
		&FT_LOG("INFO:No Results found for GROUP_NAME 'HOMENETWORK' hence need to insert\n");
		sqlInsert("INSERT INTO GROUP_TYPE_E_MCC_MNC ( GROUP_NAME, START_TIME, STOP_TIME, MCC, MNC ) VALUES ( 'HOMENETWORK', '', '','454','06')");
	}
	elsif ($count1 == '1'){
		&FT_LOG("INFO:One Result found for GROUP_NAME 'HOMENETWORK',need to compare with correct values '454 and '06'\n");
		my @val1=sqlSelect("Select * from group_type_e_mcc_mnc where GROUP_NAME='HOMENETWORK'");
		if ("@val1" =~ /\b454 06\b/) {
			&FT_LOG("INFO:Result matches the correct values\n");
		}
		else{
			&FT_LOG("INFO:Result does not matches the correct values,Insert and Update with correct values '454 and '06'\n");
			my @mcc=sqlSelect("select MCC from group_type_e_mcc_mnc where GROUP_NAME='HOMENETWORK'");
			my @mnc=sqlSelect("select MNC from group_type_e_mcc_mnc where GROUP_NAME='HOMENETWORK'");
			sqlInsert("INSERT INTO GROUP_TYPE_E_MCC_MNC ( GROUP_NAME, START_TIME, STOP_TIME, MCC, MNC ) VALUES ( 'TEST', '', '','@mcc','@mnc')");
			sqlUpdate("update group_type_e_mcc_mnc set MCC='454', MNC ='06' where group_name='HOMENETWORK'");
		}
	}
	elsif ($count1 == '2'){
		&FT_LOG("INFO:Two Results found for GROUP_NAME 'HOMENETWORK',need to compare with correct values '454 and '06'\n");
		my @val2=sqlSelect("Select * from group_type_e_mcc_mnc where GROUP_NAME='HOMENETWORK'");
		my $str="@val2";
		my $output = join ",", split " ", $str;
		my @like = split(/,/, $output);
		my $mcc1 = $like[3];
		my $mnc1 = $like[4];
		my $mcc2 = $like[8];
		my $mnc2 = $like[9];

		if ($mcc1 == 454 and $mnc1 == 06){
			&FT_LOG("INFO:Found correct value in first row,updating second row\n");
			sqlUpdate("update group_type_e_mcc_mnc set GROUP_NAME='TEST1' where MCC='$mcc2' and MNC='$mnc2'");
		}
		elsif ($mcc2 == 454 and $mnc2 == 06){
			&FT_LOG("INFO:Found correct value in second row,updating first row\n");
			sqlUpdate("update group_type_e_mcc_mnc set GROUP_NAME='TEST2' where MCC='$mcc1' and MNC='$mnc1'");
		}
		else{
			&FT_LOG("INFO:Found incorrect values,need to update both rows and insert correct values '454 and '06'\n");
			sqlUpdate("update group_type_e_mcc_mnc set GROUP_NAME='TEST1' where MCC='$mcc2' and MNC='$mnc2'");
			sqlUpdate("update group_type_e_mcc_mnc set GROUP_NAME='TEST2' where MCC='$mcc1' and MNC='$mnc1'");
			sqlInsert("INSERT INTO GROUP_TYPE_E_MCC_MNC ( GROUP_NAME, START_TIME, STOP_TIME, MCC, MNC ) VALUES ( 'HOMENETWORK', '', '','454','06')");
		}
	}

	my $arg=shift;
	my $force=0;
	my $result=qq{
		<h3>START WCDMA DATAGEN</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
		</tr>
	};
	my $host = getHostName();
	if(!hasCepBlade()){
		$result.=logAndGenerateTableEntry("No CEP blade found on server, cannot load WCDMA Data",1);
		$result.="</TABLE>";
		return $result;
	}
	
	my @dirsToCheck = ("/ossrc/data/pmMediation/eventData/GpehEvents_CEP/SubNetwork=RNC01/MeContext=RNC01",
			"/ossrc/data/pmMediation/eventData/GpehEvents_CEP/SubNetwork=RNC06/MeContext=RNC06");
	my @tablesToCheck = ("EVENT_E_RAN_HFA_SOHO_ERR_RAW","EVENT_E_RAN_HFA_IFHO_ERR_RAW",
			"EVENT_E_RAN_HFA_IRAT_ERR_RAW","EVENT_E_RAN_HFA_HSDSCH_ERR_RAW","EVENT_E_RAN_CFA_ERR_RAW");
	my @check=();
	
	if($arg =~ m/force/i){
		$force=1;
	}
	
	if($remoteDG && ($feature !~ /GritWCDMA/)){
		if( !-d "$dgNfsPath/$host"){
			open(TEMP,">$dgNfsPath/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("$dgNfsPath/tempfile.$$");
			makePath("$dgNfsPath/$host");
		}
	}
	
	$result.=setupCEPBladeForWcdma();
	
	if(!$force){
		@check=dataGenCheck_tablesOnly(\@tablesToCheck,45,0,"imsi");
		
		if($check[0] == 1){
			&FT_LOG("INFO:Skipping dataGenStart_WCDMA on CEP because it's been done. To force it to load put STARTDATAGEN_WCDMA force in a config file");
			$result.=$check[1];
			$result.="</TABLE>";
			return $result;
		}
	}
	
	#Add Required groups needed for Events UI Groups
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/AutomationAccessAreaGroup.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/AutomationControllerGroupTest.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/AutomationIMSIGroup.xml");
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add  -f /eniq/home/dcuser/automation/topology/WCDMAgroups/AutomationTerminalGroupTest.xml");
	
	@check=dataGenCheck_tablesOnly(\@tablesToCheck,45,1,"imsi");

	$result.=$check[1];
	$result.="</TABLE>";
	
	&FT_LOG("INFO:Sleeping for 45 min to allow all binaries to go to DB.\n");
	sleep(45 * 60);

	unlink "$wcdmaData_complete";
	return $result;
}

sub setupCEPBladeForWcdma{
	&FT_LOG("INFO:setupCEPBladeForWcdma()\n");
	editCEPConfigFiles();
	my $result = "";
	my ($hostname) = executeThisWithLogging("ssh dcuser\@cep_med_1 'hostname 2>/dev/null'");
	chomp($hostname);
	
	#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
	if($hostname eq "cep"){ 
		$hostname = getHostName();
	}
	
	my $remotePath;
	if($feature =~ /GritWCDMA/){
		$remotePath = '/net/atclvm559.athtem.eei.ericsson.se/package/GRIT/wcdma_cfahfa/mzInputFiles/*.gz';
	}
	else {
		$remotePath = '/net/atclvm559.athtem.eei.ericsson.se/ossrc/package/WCDMACFAHFA_ROPS.tar';
	}
	my $localPath = '/tmp/wcdmaRop';
	
	#add cron to check if datagen folders are mounted correctly
	#on the CEP mediation server (Stale NFS file handle issue on /net).
	if($feature !~ /GritWCDMA/){
		my @cron_table = executeThisQuiet("crontab -l");
		if(!grep(/check_stale_nfs.pl/,@cron_table)){
			open(FILE, ">/tmp/tmp_cron.$$");
			foreach my $cron_entry (@cron_table){
				print FILE $cron_entry;
			}
			print FILE "00,05,10,15,20,25,30,35,40,45,50,55 * * * * cd /eniq/home/dcuser/automation; ./check_stale_nfs.pl\n";
			close(FILE);
			executeThisWithLogging("chmod 755 /eniq/home/dcuser/automation/check_stale_nfs.pl");
			executeThisWithLogging("crontab /tmp/tmp_cron.$$");
			unlink("/tmp/tmp_cron.$$");
		}	
	
		if( !-d "/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/$hostname"){
			open(TEMP,">/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$");
			print TEMP "temp";
			close TEMP;
			unlink("/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$");
			makePath("/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/$hostname");
		}
	}
	else {	
		my @cron_table = executeThisQuiet("crontab -l");
		if(grep(/check_stale_nfs.pl/,@cron_table)){
			executeThisWithLogging("crontab -l | grep -v check_stale_nfs  > /tmp/cron.txt");
			executeThisWithLogging("crontab /tmp/cron.txt");
			unlink("/tmp/cron.txt");
		}	
	}
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml.bkp'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml.bkp'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml.bkp'");
	
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py ntpdate nasconsole 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation stop 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /eniq/home/dcuser/automation/topology/CEP_configs/network_elements.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp /eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml'");
	
	setCEPForWcdmaBacklog();
	
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation restart 2>/dev/null'");
	
	my @cep_status = executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation status 2>/dev/null'");
	if(grep(/Application is running/,@cep_status)){
		$result.=logAndGenerateTableEntry("cep-mediation service started on cep blade",1);
	}else{
		$result.=logAndGenerateTableEntry("Failed to start cep-mediation service on cep blade",0);
	}
	
	return $result;
}


sub startUpNodeServices{
#implement old_domain count
	my ($ip) = @_;
	my $find = "EniqEventsServices";
	my $replace = "eniq_ws_stub";
	my $servicesUriLine = "ENIQ_EVENTS_SERVICES_URI";
	my $jndiName = "jndi-name=\"Eniq_Event_Properties\"";
	my $currentStub = "eniq_ws_stub.war";
	my $propStubHost = "\n<property name=\"STUB_HOST\" value=\"$ip\"></property>\n";
	my $isDeployed = 0;

	if(!($ip =~ /^http/)){
		die ("Not a valid ip");
	}
	
	my $glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	my $ecStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	
	if($glassfishStatus || $ecStatus){
		&FT_LOG("INFO: Stopping glassfish and ec services... (This may take a few mins)\n");
		runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish stop '",1);
		runCommand("ssh dcuser\@ec_1 ' source ~/.profile; ec stop '",1);
	} else {
		&FT_LOG ("INFO: Glassfish and ec services already stopped\n");
	}
	$glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	
	if(!$glassfishStatus){ #Only runs if glassfish is stopped
		&FT_LOG("INFO: Cleaning any previous stub setup.");
		#next 4 if's clean any previous stub setup
		if (-d $applicationsFolder."eniq_ws_stub") {
			rmtree($applicationsFolder."eniq_ws_stub");
		}
		if (-e $autoDeployPath.$currentStub) {
			unlink($autoDeployPath.$currentStub);
		}
		if (-e $autoDeployPath.$currentStub."_deployed") {
			unlink($autoDeployPath.$currentStub."_deployed");
		}
		if (-e $autoDeployPath.$currentStub."_deployFailed") {
			unlink($autoDeployPath.$currentStub."_deployFailed");
		}
	
		&FT_LOG("\nINFO: Initiating war file deployment.\n");
		copy($currentStub,$autoDeployPath) || die ("Cannot copy eniq_ws_stub.war File");

		if(!-e $glassfishConfigPath."preNodeJS_domain.xml"){
			&FT_LOG("INFO: Backing up domain.xml");
			copy($glassfishConfigPath."domain.xml", $glassfishConfigPath."preNodeJS_domain.xml") || die ("Cannot backup domain.xml File");
		} else {
			&FT_LOG("INFO: Domain already backed up from previous run.");
		}
		open(DOMAIN, $glassfishConfigPath."domain.xml") || die ("Cannot open domain.xml File");
		my @lines = <DOMAIN>;
		close(DOMAIN);

		open(DOMAIN, "+>".$glassfishConfigPath."domain.xml") || die ("Cannot open domain.xml File");

		foreach my $line (@lines){
			if($line =~ /$servicesUriLine/){
				$line =~ s/$find/$replace/;
			}	
			
			print DOMAIN $line;
			
			if($line =~ /$jndiName/){
				print DOMAIN $propStubHost;
			}	
		}

		close(DOMAIN);
		
		&FT_LOG("INFO: domain.xml has been updated.\n");
	}
	
	&FT_LOG("INFO: Re-starting glassfish.... (This may take a few mins)\n");
	runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish start '",1);
	runCommand("ssh dcuser\@ec_1 ' source ~/.profile; ec start '",1);
	
	$glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	$ecStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	if (!$glassfishStatus || !$ecStatus) {
		&FT_LOG("WARNING: Glassfish or ec service failed to restart\n");
	}
	
	my $i;
	for($i = 0; $i <= 5; $i++){
		sleep(60);
		if (-M $autoDeployPath.$currentStub."_deployed" <= -M $autoDeployPath.$currentStub){
			&FT_LOG("INFO: War file successfully deployed\n");
			$isDeployed = 1;
			last;
		}
	}

	if($isDeployed == 0){
		&FT_LOG("INFO: War file didn't deploy.\n");
		return 0;
	}
	
	return 1;
}

sub tearDownNodeServices{
	&FT_LOG("INFO: Reverting from a Node Services setup to default setup.\n");
	my $glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	my $ecStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	
	if($glassfishStatus || $ecStatus){
		&FT_LOG("INFO: Stopping glassfish and ec services... (This may take a few mins)\n");
		runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish stop '",1);
		runCommand("ssh dcuser\@ec_1 ' source ~/.profile; ec stop '",1);
	} else {
		&FT_LOG("INFO: Glassfish and ec services already stopped\n");
	}

	&FT_LOG("INFO: Restoring origional domain.xml.\n");
	unlink($glassfishConfigPath."domain.xml");
	copy($glassfishConfigPath."preNodeJS_domain.xml", $glassfishConfigPath."domain.xml") 
		|| &FT_LOG("INFO: Warning: Issue with restoring origional domain.xml");
		
	$glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	$ecStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	
	if(!$glassfishStatus || !$ecStatus){
		&FT_LOG("INFO: Starting glassfish and ec services... (This may take a few mins)\n");
		runCommand("ssh dcuser\@glassfish ' source ~/.profile; glassfish start '",1);
		runCommand("ssh dcuser\@ec_1 ' source ~/.profile; ec start '",1);
	} 
	
	$glassfishStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	$ecStatus=runCommand("ssh dcuser\@glassfish ' source ~/.profile; svcs -a | grep glassfish | grep online'",0);
	
	if(!$glassfishStatus || !$ecStatus){
		&FT_LOG("Warning : Issue restarting glassfish or ec service\n");
	} 
}

sub didNodeServicesFail{
	
	my $isInstalled = shift;
	my $result="";
	my $verbose = "";
	
	$verbose.=qq{
		<h3>Node Services Failed</h3>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
			<tr>
				<th>CMD</th>
				<th>DESCRIPTION</th>
				<th>STATUS</th>ec 
			</tr>
	};
	
	$verbose .= "<tr><td>Deploy War Stub</td><td>Deploy node stub to run selinium test against</td><td>".($isInstalled?"<font color=darkblue><b>PASS</b></font>":"<font color=red><b>FAIL</b></font>")."</td></tr>";
	
	$verbose.=qq{</TABLE>};
	$result .= $verbose;
	return $result;
	
}

sub truncateWcdmaTables{
        my $cfa_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_RAN_CFA%'";
        my $hfa_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'EVENT_E_RAN_HFA%'";
        my $imsi_imei_mapping_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'DIM_E_IMSI_IMEI%'";
        my $e_sgeh_hier321_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'DIM_E_SGEH_HIER321%'";
        my $z_sgeh_hier321_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'DIM_Z_SGEH_HIER3%'";
        my $ran_tables_query = "select 'truncate table '||Table_name||';' from SYSTABLE where Table_Name NOT LIKE ('SYS%') and table_type not like 'VIEW' and Table_name like 'DIM_E_RAN_RNC%'";

        my @cfa_truncate_queries = sqlSelect($cfa_tables_query);
        my @hfa_truncate_queries = sqlSelect($hfa_tables_query);
        my @imsi_imei_truncate_queries = sqlSelect($imsi_imei_mapping_tables_query);
        my @e_sgeh_hier321_truncate_queries = sqlSelect($e_sgeh_hier321_tables_query);
        my @z_sgeh_hier321_truncate_queries = sqlSelect($z_sgeh_hier321_tables_query);
        my @ran_truncate_queries = sqlSelect($ran_tables_query);
		my @truncate_queries;
		if($feature !~ /GritWCDMA/){
			@truncate_queries = (@imsi_imei_truncate_queries,@e_sgeh_hier321_truncate_queries,@z_sgeh_hier321_truncate_queries,@ran_truncate_queries,@cfa_truncate_queries,@hfa_truncate_queries);
		}
		else{
			@truncate_queries = (@cfa_truncate_queries,@hfa_truncate_queries);
		}
		for my $truncate_query (@truncate_queries) {
			print "Truncate Query: $truncate_query\n";
			sqlDelete($truncate_query);
		}

        # WCDMA Regression requires any exclusive tac's to be 1st deleted from GROUP_TYPE_E_TAC
        # We will export, delete and reimport after the WCDMA Selenium.
        executeThisWithLogging("/eniq/sw/bin/gpmgt -e -f /eniq/home/dcuser/tac_exported.xml -g TAC:EXCLUSIVE_TAC");
        executeThisWithLogging("/eniq/sw/bin/gpmgt -i -delete -f /eniq/home/dcuser/tac_exported.xml");	
}

#
## Stop DataGen Workflows ##
#
sub dataGenStop_2G3G4G{
	my $linkExists=runCommand("ssh dcuser\@ec_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh | grep ^l'",0);
	if($linkExists){
		my $success=runCommand("ssh dcuser\@ec_1 'rm /eniq/data/eventdata/events_oss_1/sgeh'",1);
	}
	
	## Group Stop Disable 10B ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable eankmuk_4G.WG_dataGen_010B");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop eankmuk_4G.WG_dataGen_010B");

	## Stop and Disable MME1 ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable eankmuk_4G.dataGen_010B.MME1");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop eankmuk_4G.dataGen_010B.MME1");

	## Stop and Disable MME2 ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable eankmuk_4G.dataGen_010B.MME2");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop eankmuk_4G.dataGen_010B.MME2");
}

#
## Stop SGEH_DVDT DataGen Workflows ##
#
sub dataGenStop_SGEH_DVDT{
	my  $result=qq{
	<h3>STOP 2G3G4G DATAGEN</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};

	# Disable Group Work flows
	my @status=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable eankmuk_4G.WG_dataGen_010B");
	&FT_LOG("Stop GroupWorkflow (eankmuk_4G.WG_dataGen_010B),\tStatus=($status[0])\n");

	$result.="<tr><td>2G3G4G DataGen Group Workflow Disabled</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	$result.="</TABLE>";
	return $result;
}

sub dataGenStop_3GSESSIONBROWSER{
	executeThisWithLogging("ssh dcuser\@ec_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh/dir2 2>/dev/null'");
	executeThisWithLogging("ssh dcuser\@cep_med_1 '/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation stop 2>/dev/null'");
	
	sleep 600;
	truncateWcdmaTables();
}

#
## Stop KPI DataGen Workflows
#
sub dataGenStop_KPI{
	my $arg=shift;
	
	&FT_LOG("INFO:dataGenStop_KPI()\n");
	
	#Stop Processing workflows
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable SGEH.WF01_LoadBalancer_NFS.events_oss_1_dir3*");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop SGEH.WF01_LoadBalancer_NFS.events_oss_1_dir3*");
	
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable SGEH.WF01_LoadBalancer_NFS.events_oss_1_dir4*");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop SGEH.WF01_LoadBalancer_NFS.events_oss_1_dir4*");
	
	#Stop Datagen Workflows
	## Group Stop Disable 10B ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable eankmuk_4G.dataGen_011A");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop eankmuk_4G.dataGen_011A");

	## Stop and Disable MME1 ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable eankmuk_4G.dataGen_011A.MME1");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop eankmuk_4G.dataGen_011A.MME1");
	
	## Stop and Disable MME2 ##
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable eankmuk_4G.dataGen_011A.MME2");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop eankmuk_4G.dataGen_011A.MME2");
	
	if($arg == 1){
		dataGenStop_MSS();
	}
}

#
## Stop LTE DataGen Workflows
#
sub dataGenStop_LTE{
	my $linkExists=runCommand("ssh dcuser\@ec_1 'ls -ld /eniq/data/pmdata/eventdata/00/CTRS | grep ^l'",0);
	if($linkExists){
		runCommand("ssh dcuser\@ec_1 'rm /eniq/data/pmdata/eventdata/00/CTRS",0);
	}
	
	my @wfToDisable=(
		"LCHS.Analysis_eNodeB_CT_11B.MME1",
		"LCHS.Analysis_eNodeB_CT_12A.MME1",
		"LCHS.Analysis_eNodeB_CT_12B.MME1",
		"LCHS.Analysis_eNodeB_CT_13A.MME1",
		"LCHS.Analysis_eNodeB_CT_13B.MME1",
		"LCHS.Analysis_mme_Ctum.MME1",
		"LCHS.Analysis_mme_Ctum.MME2",
		"LCHS.Analysis_mme_Ctum.MME3",
		"LCHS.Analysis_mme_Ctum.MME4",
		"LCHS.Analysis_mme_Ctum.MME5"
	);
		
	my @wfGroupsToDisable=(
		"LCHS.WFG_LCHS_11B",
		"LCHS.WFG_LCHS_12A",
		"LCHS.WFG_LCHS_12B",
		"LCHS.WFG_LCHS_13A",
		"LCHS.WFG_LCHS_13B"
	);
		
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable @wfToDisable");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop @wfToDisable");

	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable @wfGroupsToDisable");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop @wfGroupsToDisable");
}

#
## Stop MSS DataGen Workflows
#
sub dataGenStop_MSS{
	updateMssPreprocessingWorkflow("/eniq/data/pushData/04/mss/MSS_3","/eniq/data/pushData/03/mss/MSS_4");
	#/eniq/data/pushData/04/mss/MSS_3 - mss3
	#/eniq/data/pushData/03/mss/MSS_4 - mss 4
	
	######Disable each WG01_PreProcessing & WG02_Processing######
	my @wfToDisable=(
		#others
		"MSS.MSSSimulator.workflow_3",
		"MSS.MSSSimulator.workflow_4",
		
		"MSS.WF01_PreProcessing.MSS_3",
		"MSS.WF01_PreProcessing.MSS_4"
	);
		
	my @wfGroupsToDisable=(
		"MSS.WorkFlow_MSS",
		"MSS.WG01_PreProcessing",
		"MSS.WG02_Processing"
	);
	
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable @wfToDisable");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop @wfToDisable");

	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable @wfGroupsToDisable");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop @wfGroupsToDisable");	
}

sub dataGenStop_WCDMA{
	#remove Symbolic link
	my $linkExists=runCommand("ssh dcuser\@ec_1 'ls -ld /eniq/data/eventdata/events_oss_1/GPEHEvents | grep ^l'",0);
	if($linkExists){
		my $success=runCommand("ssh dcuser\@ec_1 'rm /eniq/data/eventdata/events_oss_1/GPEHEvents'",1);
	}
	
	#disable and stop wf + wf groups	
	my @wfToDisable=(
		"eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC01",
		"eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC05",
		"eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC06",
		"eankmuk_WCDMA_RESERVED.W11B_CF_rsv.RNC07"
	);
		
	my @wfGroupsToDisable=(
		"eankmuk_WCDMA_RESERVED.W11A_CF",
		"eankmuk_WCDMA_RESERVED.W11B_CF_rsv"
	);
	
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable @wfToDisable");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop @wfToDisable");
	
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable @wfGroupsToDisable");
	executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop @wfGroupsToDisable");
}

sub restoreTACs_WCDMA{
	executeThisWithLogging("/eniq/sw/bin/gpmgt -i -add -f /eniq/home/dcuser/tac_exported.xml");
}

sub sbGetIMSIs{
	&FT_LOG("INFO: Get IMSIs for Session Browser");
		
	my $statement = $_[0];
	my $event = $_[1];
	my $resDataImsiLine="SB_IMSI_$event=var1;";
		
	&FT_LOG("INFO:$statement");
	my @cols=sqlSelect($statement);
	
	&FT_LOG("INFO:$cols[0]");
	# update reservedData.csv file only if query returned result
	if(@cols){
		$resDataImsiLine=~s/var1/$cols[0]/;
		if(-e "$reservedDataLocation"){
			open(CSV,"<$reservedDataLocation");
			my @contents=<CSV>;
			close(CSV);
			foreach(@contents){
				s/SB_IMSI_$event.*$/$resDataImsiLine/;
			}
			open(CSV, ">$reservedDataLocation");
			print CSV @contents;
			close (CSV);
		}else{
			&FT_LOG("ERROR:Could not find reserved data file at location: $reservedDataLocation");
		}
	}
	
	return @cols;
}

sub sbFindIMSIs{
	my $overallResult=qq{
	<h3>SB FIND IMSI</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	if(!hasCepBlade()){
		$overallResult.=logAndGenerateTableEntry("No CEP blade found on server thus no data expected on database.",1);
		$overallResult.="</TABLE>";
		return $overallResult;
	}
	
	my @query = (
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_DATA_BEARER_SESSION FROM (SELECT DISTINCT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_RAW where IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_ATTACH FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 0 AND HIERARCHY_3!='Unknown'  AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_PDP_ACTIVATE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 1 AND HIERARCHY_3!='Unknown'  AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_RAU FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 2 AND HIERARCHY_3!='Unknown'  AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_ISRAU FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 3 AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_DEACTIVATE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 4 AND HIERARCHY_3!='Unknown' AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_SERVICE_REQUEST FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 15 AND HIERARCHY_3!='Unknown' AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_SUC_HSDSCH_CELL_CHANGE_RAW WHERE EVENT_ID = 432  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_CALL_SETUP_FAILURES FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_CFA_ERR_RAW WHERE EVENT_ID = 456  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_SYSTEM_RELEASE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_CFA_ERR_RAW WHERE EVENT_ID = 438  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_OUT_HARD_HANDOVER FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_INTER_OUT_HHO_RAW WHERE EVENT_ID = 458  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_RRC_MEASUREMENT_REPORT FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_RRC_MEAS_RAW WHERE EVENT_ID = 8  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_FOR_CELL_VISITED_CHART FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_CELL_VISITED_RAW WHERE  IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_SOHO_ERR_RAW WHERE EVENT_ID = 408  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_IFHO_HANDOVER_EXECUTION_FAILURE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_IFHO_ERR_RAW WHERE EVENT_ID = 423  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_HSDSCH_ERR_RAW WHERE EVENT_ID = 433 AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_HSDSCH_NO_CELL_CHANGE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_HSDSCH_ERR_RAW WHERE EVENT_ID = 436 AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
			"SELECT TOP 1 APPLICATION.IMSI ||','|| APPLICATION.DATETIME_ID||';' AS SB_IMSI_APPLICATION_CHART FROM (SELECT IMSI, DATETIME_ID, COUNT(*) AS x FROM EVENT_E_USER_PLANE_CLASSIFICATION_RAW WHERE IMSI!=NULL AND IMSI!=0 AND APN!=NULL AND FIVE_MIN_AGG_TIME!=NULL AND PACKETS_DOWNLINK>0 AND PACKETS_UPLINK>0 GROUP BY IMSI, DATETIME_ID) AS APPLICATION WHERE APPLICATION.x >2 ORDER BY APPLICATION.DATETIME_ID DESC",
			"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME||';' AS SB_IMSI_TCP_PERFORMANCE_CHART FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_USER_PLANE_TCP_RAW WHERE IMSI!=NULL AND IMSI!=0 AND START_APN!=NULL AND START_APN!=NULL AND DATETIME_ID!=NULL) a ORDER BY a.EVENT_TIME DESC"
	);
		
	my @event_type = (
			"DATA_BEARER_SESSION",
			"ATTACH",
			"PDP_ACTIVATE",
			"RAU",
			"ISRAU",
			"DEACTIVATE",
			"SERVICE_REQUEST",
			"INT_SUCCESSFUL_HSDSCH_CELL_CHANGE",
			"INT_CALL_SETUP_FAILURES",
			"INT_SYSTEM_RELEASE",
			"INT_OUT_HARD_HANDOVER",
			"RRC_MEASUREMENT_REPORT",
			"FOR_CELL_VISITED_CHART",
			"INT_SOFT_HANDOVER_EXECUTION_FAILURE",
			"INT_IFHO_HANDOVER_EXECUTION_FAILURE",
			"INT_FAILED_HSDSCH_CELL_CHANGE",
			"INT_HSDSCH_NO_CELL_SELECTED",
			"APPLICATION_SUM_CHART",
			"TCP_PERFORMANCE_CHART"
	);
	
	my $wait_time= 30;
	
	while($wait_time){
		my $success = 1;
		for(my $i = 0; $i < @event_type; $i++){
			my $query_result = sbGetIMSIs($query[$i],$event_type[$i]);
			if(!$query_result){
				$success = 0;
			}
		}
		
		if($success){
			$overallResult.=logAndGenerateTableEntry("All SB IMSIs required for UI tests populated",1);
			last;
		}
		&FT_LOG("Not all IMSIs are available, waiting for a max of $wait_time minutes",0);
		$wait_time--;
		sleep 60;
	}
	
	if(!$wait_time){
		$overallResult.=logAndGenerateTableEntry("Not all SB IMSIs required for UI tests populated",0);
	}
	$overallResult.="</TABLE>";
	return $overallResult;
}

#
#This sub reports back the state of workflows. Anything other than "Aborted" returns "success"
#
sub workflowStatus{
	my ($wfGroupList)=@_;
	foreach my $group(@$wfGroupList){
		my @status=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist | grep $group");
		$status[0] =~ s/\n//g;
		if($status[0] =~ m%Aborted%){
			&FT_LOG($status[0],0);
		}
		else{
			&FT_LOG($status[0],1);
		}
	}
}

#------------------------------------------------------------	
#	SUBROUTINES - ADMIN UI, USER ADMIN (1)
#
#	1.	VerifyLDAP()
#
#------------------------------------------------------------

#
# VERIFY LDAP
# This subroutine checks AdminUI and User Management 
# greps the webpage for User Management, if present it passes the test
sub VerifyLDAP{
	my  $result=qq{
	<h3>VERIFY LDAP</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST</th>
		 <th>STATUS</th>
	   </tr>
	};

	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/verifyldap.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/UserManagement\"");

	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/verifyldap.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/UserManagement\"");

	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/verifyldap.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data  \"action=/servlet/UserManagement?action=adduser&Account_Type=UI+Users&First_Name=john&Last_Name=keegan&Login_Name=jklogin&Password=jkpassword&Confirm=jkpassword&submit=Submit\" \"$LOCALHOST/adminui/servlet/UserManagement?action=adduser\"");
	 
	my @status=executeThisWithLogging("grep -c 'User Management' /eniq/home/dcuser/automation/html/verifyldap.html");
	if($status[0] > 0){
		&FT_LOG("Verify LDAP PASS\n");
		$result.="<tr><td>Verify LDAP</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	}else{ 
		&FT_LOG("Verify LDAP FAIL\n");
		$result.="<tr><td>Verify LDAP</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
	} 

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");

	$result.="</TABLE>";
	return $result;
}

#------------------------------------------------------------	
#	SUBROUTINES - DATA LOADING (3)
#
#	1.	SystemStatus()
#	2.	DwhStatus()
#	3.	RepStatus()
#
#------------------------------------------------------------

#------------------------------------------------------------	
#	SUBROUTINES - WAIT UNTIL PROC DONE (1)
#
#	1.	waitUntilProcessesDone()
#
#------------------------------------------------------------

# 
# WAIT UNTIL NO PROCESSES ARE IN EXECUTION OR IN QUEUE 
# This test case only runs 2 commands:
# engine -e  showSetsInExecutionSlots 
# engine -e  showSetsInQueue 
# and counts the output , if both are 0 then it finishes, and the test is passed.
# This is very helpful to check is the regression is finished loading and aggregating.
#

sub waitUntilProcessesDone{
	my $processesExecution=1;
	my $processesQueue    =1;
	do{
		my @execution=executeThisWithLogging("/eniq/sw/bin/engine -e  showSetsInExecutionSlots | egrep -v '(----|TechPack SetName|Finished|Querying sets|Connecting engine )' | wc -l");
		chomp(@execution);
		$processesExecution=$execution[0];
		my @queue    =executeThisWithLogging("/eniq/sw/bin/engine -e  showSetsInQueue | egrep -v '(----|TechPack SetName|Finished|Querying sets|Connecting engine )' | wc -l");
		chomp(@queue);
		$processesQueue    =$queue[0];
		&FT_LOG("ProcessesinExecutionQueue: $processesExecution  ProcessesInQueue: $processesQueue sleep 1min\n");
		sleep(60);
	}while(!($processesExecution==0 && $processesQueue==0));
	&FT_LOG("PASS\n");
}

#------------------------------------------------------------	
#	SUBROUTINES - SHOW LOADINGS AND AGGREGATION (8)
#
#	1.	verifyAggregations()
#
#------------------------------------------------------------

# 
# VERIFY AGGREGATIONS
# 
# This process goes to AdminUI and checks the aggregation based on the TIMEWARP variable
# for example if TIMEWARP = -24 then it will check the loadings from yesterday
# The algorithm is basically to check if there are green boxes in the table and count them
# if they are green for the TIMEWARP date then the test is passed, else fail
#

sub verifyAggregations{
	my $result;
	my $year   =$YEARTIMEWARP;  #getYearTimewarp();
	my $month  =$MONTHTIMEWARP; #getMonthTimewarp();
	my $day    =$DAYTIMEWARP;   #getDayTimewarp();
	system("rm /eniq/home/dcuser/automation/html/cookies.txt");

	foreach my $tp (@aggini){
		$_=$tp;
		next if(/^$/);
		$result.="<h3>$tp</h3><BR>\n";
		# SAVE COOKIES
		system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&type=$tp&action=/servlet/ShowAggregations&value='Get Information'\"  \"$LOCALHOST/adminui/servlet/ShowAggregations\"");
		#sleep(1);
		# SEND USR AND PASSWORD
		system("/usr/sfw/bin/wget  --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
		#sleep(1);
		# GET AGGREGATION
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/aggregations_$tp.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&type=$tp&value='Get Information'\"  \"$LOCALHOST/adminui/servlet/ShowAggregations\"");


		open(AGG,"< /eniq/home/dcuser/automation/html/aggregations_$tp.html");
		my @aggregations=<AGG>;
		close(AGG);
		system("rm /eniq/home/dcuser/automation/html/aggregations_$tp.html");
		my $tpack=0;
		my $found=0;
		my $green=0;
		my $yellow=0;
		my $red=0;
		$result.=qq{    
		<br>
		<h3>ADMINUI: SHOW AGGREGATIONS </>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		<th>TABLE</th>
		<th bgcolor="#047e01">GREEN</th>
		<th bgcolor="#ffff00">YELLOW</th>
		<th bgcolor="#CC0000">RED</th>
		<th>RESULT</th>
		</tr>
		};

		foreach my $aggregations (@aggregations){
			$_=$aggregations;
			if(/                  <tr><td width=.230.><font size=.-1.>/){
				$aggregations=~s/.*1.>//;
				$aggregations=~s/<.*//;
				$result.="<tr><td>$aggregations</td>";
				&FT_LOG("$aggregations\n");
				$found=1;
			}
			if($found==1 && /047e01/) { $green++;}
			if($found==1 && /ffff00/) { $yellow++;}
			if($found==1 && /ff0000/) { $red++;}
			if($found==1 && /<!-- one row ends here-->/){
				print  "GREEN :$green\n";
				print  "YELLOW:$yellow\n";
				print  "RED   :$red\n";
				$result.=  "<td align=center>$green</td>";
				$result.=  "<td align=center>$yellow</td>";
				$result.=  "<td align=center>$red</td>";
				if($green==0){ 
					$result.=  "<td align=center><font color=#ff0000><b>FAIL</b></font></td>\n";
				}else{
					$result.=  "<td align=center><font color=darkblue><b>PASS</b></font></td>\n";
				}
				$result.=  "</tr>";
				$found=0;
				$green=0;
				$yellow=0;
				$red=0;
			}
		}
		$result.="</TABLE>\n";

		my @tables= getAllTables4TP($tp);
		$result.=qq{
		<br>
		<h3>SQL AGGREGATION DB STATUS</>
		<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
		<th>TABLE</th>
		<th>ROWS</th>
		<th>RESULT</th>
		</tr>
		};
		foreach my $table (@tables){
			$_=$table;
			next if(/^$/);
			next if(/ /);
			next if(/affected/);
			next if(/_RAW/);
			my @data=getLoading($table);
			# GET INFO FOR TABLE
			if(/_DAY/){
				$table=~s/_DAY//;
			}
			
			foreach my $data (@data){
				$_=$data;
				next if(/^$/);
				next if(/affected/);
				$data=~ s/\t//g;
				$data=~ s/\s//g;
				$data=~ s/^/<tr><td>/g;
				$data=~ s/\|/<\/td><td align=center>/g;
				#      $data=~ s/$/<\/td><tr>/g;
				$data=~ s/$/<\/td><td align=center>RESULT<\/td><tr>/g;
				$_=$data;
				if(/<td align=center>0<.td>/){
					$data=~ s/$numRops/<font color=#660000>0<\/font>/g;
					$data=~ s/RESULT/<font color=#660000><b>FAIL<\/b><\/font>/;
				}
				else #(/<td align=center>$numRops<.td>/)
				{
					$data=~ s/$numRops/<font color=#darkblue>$numRops<\/font>/g;
					$data=~ s/RESULT/<font color=#darkblue><b>PASS<\/b><\/font>/;
				}
				$result.="$data\n";
				#&FT_LOG("$data<br>\n");
			}
		}
		$result.="</TABLE>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

#------------------------------------------------------------	
#	SUBROUTINES - SELENIUM (2)
#
#	1.	initSelenium()
#	2.	runSelenium()
#	3.  runAAC()
#	4.	seleniumUI()
#
#------------------------------------------------------------

sub doFirefoxVersion{
	my $version=shift;
	my $new_version = shift;
	my $current_version = `firefox -version`;
	
	$new_version = 10 unless $new_version != 0;
	chomp($current_version);
	$current_version =~ s/Mozilla Firefox (\d*)\.(\d*).*/$1/;
	printf "Current Version: %s \nNew Version: %s\n",$current_version,$new_version;
	if($current_version == $new_version){
		&FT_LOG("INFO:/usr/lib/firefox version is already in version $new_version");
	}else{
		if($version eq "new"){
			my $backupFolder="";
			my $runDate = getRunDate();
			
			my @arr=grep(/firefox.*$new_version.*tar.bz2$/,ls("/eniq/home/dcuser/automation","t"));
			if(@arr){
				my $firefoxBzipFile=$arr[0];
				&FT_LOG("INFO:Unzipping /eniq/home/dcuser/automation/$firefoxBzipFile");
				executeThisWithLogging("cd /eniq/home/dcuser/automation;/usr/bin/bunzip2 $firefoxBzipFile");
			}
			
			@arr=grep(/firefox.*$new_version.*tar$/,ls("/eniq/home/dcuser/automation","t"));
			if(!@arr){
				&FT_LOG("ERROR:Could not find any firefox.*tar files in /eniq/home/dcuser/automation");
			}else{
				my $firefoxTarFile=$arr[0];
				my $firefoxVersion=$firefoxTarFile;
				$firefoxVersion=~s/.tar//;
				$firefoxVersion=~s/firefox//;
				
				my @alreadyUntarredVersion=grepFile("^Version=", "/eniq/home/dcuser/automation/firefox/application.ini");
					
				if( ! -e "/eniq/home/dcuser/automation/firefox/firefox-bin" && -e "/eniq/home/dcuser/automation/$firefoxTarFile"){
					&FT_LOG("INFO:Untarring /eniq/home/dcuser/automation/$firefoxTarFile");
					executeThisWithLogging("cd /eniq/home/dcuser/automation;/usr/bin/tar xvf $firefoxTarFile");
				}elsif( ! -e "/eniq/home/dcuser/automation/$firefoxTarFile"){
					&FT_LOG("ERROR:/eniq/home/dcuser/automation/firefox.*tar does not exist");
				}elsif(!grep(/^Version=$firefoxVersion/,@alreadyUntarredVersion)) {
					&FT_LOG("INFO:Removing old Firefox version");
					executeThisQuiet("ps -ef | grep firefox| grep -v grep | awk '{print \$2}' | xargs kill -9 2>/dev/null");
					executeThisWithLogging("rm -rf /eniq/home/dcuser/automation/firefox");
					executeThisWithLogging("rm -rf /eniq/home/dcuser/automation/firefox");
					&FT_LOG("INFO:Untarring /eniq/home/dcuser/automation/$firefoxTarFile");
					executeThisWithLogging("cd /eniq/home/dcuser/automation;/usr/bin/tar xvf $firefoxTarFile");
				}
				
				if( ! -e "/eniq/home/dcuser/automation/firefox/firefox-bin"){
					&FT_LOG("ERROR:/eniq/home/dcuser/automation/firefox/firefox-bin does not exist");
				}else{
					if( ! -l "/usr/lib/firefox"){
						my $runDate = getRunDate();
						$backupFolder = "/usr/lib/firefox-BACKUP-${runDate}";
						executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh mv /usr/lib/firefox $backupFolder");
						if ( !-e $backupFolder){
							&FT_LOG("ERROR:Failed to back up /usr/lib/firefox. Aborting");
						}
					}else{
						executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh rm /usr/lib/firefox");
					}
				}
			}
			
			if( -e "/eniq/home/dcuser/automation/firefox/firefox-bin" && ! -e "/usr/lib/firefox"){
				executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh ln -s /eniq/home/dcuser/automation/firefox /usr/lib/firefox");
				if( -l "/usr/lib/firefox"){
					&FT_LOG("INFO:/usr/lib/firefox linked to /eniq/home/dcuser/automation/firefox");
				}else{
					&FT_LOG("ERROR:/usr/lib/firefox could not be linked to /eniq/home/dcuser/automation/firefox.");
					if( -e $backupFolder){
						&FT_LOG("INFO:Restoring backup");
						executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh mv $backupFolder /usr/lib/firefox");
						if ( !-e "/usr/lib/firefox"){
							&FT_LOG("ERROR:$backupFolder could not be restored to /usr/lib/firefox");
						}else{
							&FT_LOG("INFO:$backupFolder restored to /usr/lib/firefox");
						}
					}
				}
			}
		}elsif($version eq "old"){
			my @arr=grep(/firefox-BACKUP/,ls("/usr/lib","t"));
			if(!@arr){
				&FT_LOG("INFO:No /usr/lib/firefox-BACKUP* files exist");
			}else{
				if( -l "/usr/lib/firefox"){
					my $backupFolder=$arr[0];
					executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh rm /usr/lib/firefox");
					executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh mv /usr/lib/$backupFolder /usr/lib/firefox");
					if( -e "/usr/lib/firefox" && !-l "/usr/lib/firefox"){
						&FT_LOG("INFO:/usr/lib/firefox restored");
					}else{
						&FT_LOG("ERROR:/usr/lib/firefox could not be restored");
					}
				}else{
					&FT_LOG("ERROR:/usr/lib/firefox is not a symbolic link. Backup cannot be restored");
				}
			}
		}else{
			&FT_LOG("ERROR:This sub requires either new or old as an argument");
		}
	}
}

#
# INIT_SELENIUM
#
sub initSelenium{
	my $rcServer=shift;
	my $startCommand="start";
	my $stopCommand ="stop";
	my $serverHost="";
	if($rcServer eq "localhost"){
		chmod 0755,"/eniq/home/dcuser/automation/localSeleniumRC.sh";
		executeThisWithLogging("/eniq/home/dcuser/automation/localSeleniumRC.sh >/dev/null 2>&1 &");
		$startCommand="startUnix";
		$stopCommand ="stopUnix";
		sleep(5);
	}elsif($rcServer=~m/host=/){
		$rcServer=~s/host=//g;
		$serverHost="-DSERVERHOST=$rcServer";
	}
	my  $result=qq{
	<h3>INIT SELENIUM</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>TEST STAGES</th>
	 <th>STATUS</th>
	</tr>
	};
	
	updateSeleniumPort();
	
	my @browser=executeThisQuiet("grep -iw glassfish /etc/hosts | cut -d ' ' -f 3");
	chomp(@browser);
	executeThisQuiet("cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient stop 2>/dev/null;/eniq/sw/runtime/java/bin/java RunSeleniumClient stopUnix 2>/dev/null;/eniq/sw/runtime/java/bin/java stopHub 2>/dev/null");
	my $browserURL = "http://$browser[0].$domain";
	&FT_LOG("Launch Selenium HUB on localhost\n");
	
	executeThisQuiet("ps -ef | grep ant-launcher | grep -v grep | awk '{print \$2}' | xargs kill -9 2>/dev/null");
	my @status1=executeThisWithLogging("cd /eniq/home/dcuser/selenium/selenium-grid-1.0.8; /eniq/sw/runtime/ant/bin/ant launch-hub > hub.out &");
	sleep(10);
	$result.="<tr><td>Launch Selenium HUB on $browser[0]</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
	my @status2=executeThisWithLogging("cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient $startCommand");
	if($startCommand eq "start"){
		$result.="<tr><td>Activate Selenium RC (atdl785esxvm8.athtem.eei.ericsson.se)</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Activate Selenium RC (atdl785esxvm8.athtem.eei.ericsson.se)\n");
	}else{
		$result.="<tr><td>Activate Selenium RC (localhost)</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Activate Selenium RC (localhost)\n");
	}
		
	## Start Execution of the Dummy Selenium Test Case for confirm Hub-RC Connectivity ##

	&FT_LOG("Run SeleniumUI Dummy Test\n");
	executeSeleniumAndWait("/eniq/sw/runtime/java/bin/java -DHOST=$browserURL $serverHost -DTEST_GROUP=DummyTestGroup -DTEST_BLADE -jar /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/selenium_events_tests.jar",18000);
	&FT_LOG("Finished SeleniumUI Dummy Test\n");
	my @seleniumFull=grep(/.*testresults\.log$/, ls("/eniq/home/dcuser/automation"));
	my $resultsFile = $seleniumFull[0];
	foreach my $logFile (@seleniumFull){
		move( "/eniq/home/dcuser/automation/$logFile", "/eniq/home/dcuser/automation/RegressionLogs" );
	}
    
	## Find the Test Results Log file that was produced ##
	if( -e "/eniq/home/dcuser/automation/RegressionLogs/$resultsFile" ){
		&FT_LOG("Selenium LogFile:/eniq/home/dcuser/automation/RegressionLogs/$resultsFile\n");
	}else{
		&FT_LOG("Selenium LogFile:ERROR: Not copied\n");
	}
	## Extract  from the logfile TestTag, TestName, TestResult, FailReason ##
        
	open(FILE, "/eniq/home/dcuser/automation/RegressionLogs/$resultsFile" );
	my @exe = <FILE>;
	close(FILE);
	my @exe = grep( /TestResult/, @exe);

	if (!@exe) {
		&FT_LOG("Selenium Test Execution DID NOT RUN - FAIL\n");
		$result.="<tr><td>Selenium Test Execution didn't even run. Reason Unknown: Check WS, check Selenium files</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		$trafficLightColour = "red";
	}

    foreach my $exe (@exe){
		$_=$exe;
		my $testresult = $exe;
		my $testtag = $exe;
		my $testname = $exe;
		my $failurereason = $exe;

		$testtag =~ s/.*TestTag: //;
		$testtag =~ s/TestResult.*//;
		$testname =~ s/.*TestName: //;
		$testname =~ s/TestTag.*//;
		$testresult =~ s/.*TestResult: //;
		$testresult =~ s/FailureReason.*//;
		$failurereason =~ s/.*FailureReason://;

		if($testresult =~ /PASS/){
			&FT_LOG("Selenium Test Execution ($testname) - PASS\n");
			$result.="<tr><td>Selenium Test Execution ($testname)</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		}elsif($testresult =~ /FAIL/){
			&FT_LOG("Selenium Test Execution ($testname) - FAIL\n");
			$result.="<tr><td>Selenium Test Execution ($testname)</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
	}

	@status2=executeThisWithLogging("cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient $stopCommand;/eniq/sw/runtime/java/bin/java stopHub");
	&FT_LOG("De-activate Selenium RC and Stop HUB \n");
	if($rcServer eq "localhost"){
		chmod 0755,"/eniq/home/dcuser/automation/localSeleniumRC.sh";
		if(! -e "$concurrent_present"){
		executeThisWithLogging("/eniq/home/dcuser/automation/localSeleniumRC.sh kill undofirefoxfix >/dev/null 2>&1");
		}
	}
	$result.="</TABLE>\n";
	return $result;
}

sub updateSeleniumPort{
	my $seleniumProperties = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/selenium.properties";
	open(PROP, "$seleniumProperties");
	my @prop = <PROP>;
	close(PROP);
	open(UPDATED, ">${seleniumProperties}.NEW");
	my $updated = 0;
	foreach my $line (@prop){
		if($line =~ m/server\.port/ && $line !~ m/4444/ ){
			&FT_LOG("INFO: server.port is not 4444 in selenium.properties");
			$line =~ s/server\.port ?= ?\d+/server.port = 4444/;
			$updated = 1;
		}
		print UPDATED $line;
	}
	close(UPDATED);
	if($updated){
		&FT_LOG("INFO: Updating \"${seleniumProperties}\"\n");
		move("$seleniumProperties.NEW", $seleniumProperties);
	}else{
		unlink("${seleniumProperties}.NEW");
	}
}

sub startLocalSeleniumRC{
	my $command = shift;
	my $timeout = shift;
	my $childPid = fork(); #fork a process

	if($childPid == 0){
		#Launch Local Selenium RC in child process
		executeThisWithLogging($command);
		exit(0);
	}else{
		sleep 10;
		while($timeout > 0){
			my @status = grep(/localSeleniumRC.sh/,executeThisQuiet("ps -ef | grep localSeleniumRC.sh | grep -v grep"));
			if(@status){
				&FT_LOG("INFO: Local selenium RC server started, status: $status[0]");
				last;
			}
			sleep 1;
			--$timeout;
		}
		if($timeout == 0){
			&FT_ERROR("Failed to launch selenium RC server.");
		}
	}
}

sub runSelenium{
	$dataloading_status=1;

	#EQEV-22189: Modifications to exclude test case execution where RAW tables are not populated.
	if ($dataloading_status == 0){
		&FT_LOG("Data loading is not successful. Selenium test cases will not be executed\n");
		$result.=qq{
		<br/>Selenium test cases will not be executed as data loading was not successful.
		};
		return;
	}
	
	my $args=shift;
	my @spArgs = split(/,/, $args);
	@spArgs=reverse(@spArgs);
	my @testGroups=();
	my $rcServer="";
	my $rcServerVersion=39;
	my $serverHost="10.42.33.82";
	my $localhostArg="";
	my $portArg="";
	my $startCommand="start";
	my $stopCommand ="stop";
	my $seleniumJarPath = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/selenium_events_tests.jar";
	executeThisWithLogging("perl -i -pe 's/^db.pwd.*/db.pwd = $temp_pwd/g' /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/selenium.properties");
	
	my $fireFoxVersion = `firefox -version`;
	if (!$fireFoxVersion){
		&FT_LOG("No Firefox info, install Firefox version 10 by default\n");
		doFirefoxVersion("old");
	}
	foreach my $arg(@spArgs){
		if($arg =~ m/rc_server_version/){
			$arg=~s/rc_server_version=//g;
			$rcServerVersion=$arg;
			$arg="";
		}
	}
	foreach my $arg(@spArgs){
		if($arg=~m/^localhost/){
			($rcServer,$sel_port) = split(/=/, $arg);

			if (! defined $sel_port){
				$sel_port = 4566;
			}

			#$rcServer=$arg;
			$serverHost="$rcServer";
			chmod 0755,"/eniq/home/dcuser/automation/localSeleniumRC.sh";		
			if(! -e "$concurrent_present"){
			 executeThisWithLogging("/eniq/home/dcuser/automation/localSeleniumRC.sh kill undofirefoxfix >/dev/null 2>&1");
			}
			startLocalSeleniumRC("/eniq/home/dcuser/automation/localSeleniumRC.sh -port $sel_port $rcServerVersion >/dev/null 2>&1",600);
			
			$startCommand="startUnix";
			$stopCommand ="stopUnix";
			$localhostArg="-DRUN_LOCATION=localhost";
			$portArg="-DSERVERPORT=$sel_port";
			sleep(5);
		}elsif($arg=~m/host=/){
			$arg=~s/host=//g;
			$serverHost="$arg";
		}elsif($arg=~m/\.jar/){
			$seleniumJarPath = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/$arg";
		}elsif($arg ne ""){
			push(@testGroups,$arg);
		}
	}

	my $result="";
	my $eniq_version = "1".getVersion();
	
    ###############################################################################
	# If the Data Generator has just started we  may want to refresh the APN table#
	# rather than wait for the 24hr interval update                               #
	# This is configured in the Param REFRESH_TOPOLOGY                            #
	###############################################################################
	if($refreshTopology  eq "true"){
		&FT_LOG("Issue a Refresh APN table command and wait 5 mins to complete.\n");
		my @status=executeThisWithLogging("/eniq/sw/bin/engine -e startSet DIM_E_SGEH Update_DIM_E_SGEH_APN");
		sleep(5*60);
		$refreshTopology = "false";
	}

	#
	## Start Execution of the Selenium Test Cases ##
	#
	
	open(HOSTS,"/etc/hosts");
	my @contents=<HOSTS>;
	close(HOSTS);
	my @glassfish=grep /glassfish/, @contents;
	my @spGlassfish = split(/\s+/, $glassfish[0]);
	
	my $host = hostname;
	my $browserURL = "http://$spGlassfish[1].$domain";
	my $hostURL = "http://$host.$domain";
	my $totParams = @testGroups;
	#$numberOfFeatureInSuit stores features' number when run regression in suits, which will be used in main
	$numberOfFeatureInSuit = @testGroups;
	&FT_LOG("Starting SeleniumUI Tests....\n");
	&FT_LOG("Selenium Browser URL=$browserURL\n");
	&FT_LOG("Selenium Host URL=$hostURL\n");
	$ENV{DISPLAY}="$host:0.0";
	
	## Execute the Selenium JAR file ##
	#
	my $childPid = 0;
	if ($totParams == 0){
		&FT_LOG("ERROR:No test groups specified");
	}else{
		my @childPids=();
		foreach my $group(@testGroups){
			#$result.="<h3>Selenium $group</h3>\n";
			&FT_LOG("Selenium TestGroup=$group\n");
			&FT_LOG("Executing in child process\n");
			
			$childPid = fork(); #fork a process
			if($childPid == 0){				
				if($listOfTestsToRun ne ""){
					executeSeleniumAndWait("/eniq/sw/runtime/java/bin/java $localhostArg $portArg -DHOST=$browserURL -DSERVERHOST=$serverHost -DENIQVERSION=$eniq_version -DHOSTADMINUI=$hostURL -DTEST_GROUP=$group $listOfTestsToRun -DTEST_BLADE -jar $seleniumJarPath",60000); # temporarily increase timeout from 18000 to 60000 for WCDMA
				}else{
					executeSeleniumAndWait("/eniq/sw/runtime/java/bin/java $localhostArg $portArg -DHOST=$browserURL -DSERVERHOST=$serverHost -DENIQVERSION=$eniq_version -DHOSTADMINUI=$hostURL -DTEST_GROUP=$group -DTEST_BLADE -jar $seleniumJarPath",60000); # temporarily increase timeout from 18000 to 60000 for WCDMA
				}
				exit(0);
			}else{
				&FT_LOG("INFO:$group being executed in child process $childPid");
				push(@childPids,$childPid);
			}
			
			sleep 2;
		}
		
		my $stillRunning=1;
		while($stillRunning){
			$stillRunning=0;
			foreach my $pid(@childPids){
				#Wait in the parent process for the child process to finish
				my $running = `ps -p $pid`;
				if( $running =~ /^{$pid}\s/ && $running !~ /defunct/){
					$stillRunning=1;
					last;
				}
			}
			sleep 2;
		}
	}
	
	opendir (DIR, "/eniq/home/dcuser/automation");
	my @logpath = grep /.*testresults.log$/, readdir(DIR);
	my @logfile = ();
	foreach my $group (@testGroups){
		foreach my $file (@logpath){
			if($file =~ m/$group/){
				push @logfile, $file;
			}
		}
	}
	
	closedir DIR;
	
	#
	## Extract  from the logfile TestTag, TestName, TestResult, FailReason ##
	#
	my @resultsPages=();
	foreach my $path(@logfile){
		my $resultInCommon=$result;
		&FT_LOG("Selenium LogFiles:$path\n");
		if($resultsPath eq ""){
			$resultInCommon.="<br/><b>Selenium log file for below table(s): <a href='../logs/$path'>$path</a></b>";
		}else{
			my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
			$resultInCommon.="<br/><b>Selenium log file for below table(s): <a href='$url/logs/$path'>$path</a></b>";
		}
		if($path =~ m/AacTestGroup/i){
			$resultInCommon.=getAacResults();
		}else{
			foreach my $group (@testGroups){
				if($path =~ $group){
					if ($separateResultsFlag){
						$group = "";
					}
					$resultInCommon.=getTestResults("/eniq/home/dcuser/automation/$path",$group);
				}
			}
		}
		rename("/eniq/home/dcuser/automation/$path","/eniq/home/dcuser/automation/$path.tmp");
		$resultInCommon.=getTimedOutCommands();
		push @resultsPages, $resultInCommon;
	}
	if($rcServer eq "localhost"){
		chmod 0755,"/eniq/home/dcuser/automation/localSeleniumRC.sh";
		if(! -e "$concurrent_present"){
		executeThisWithLogging("/eniq/home/dcuser/automation/localSeleniumRC.sh kill undofirefoxfix >/dev/null 2>&1");
		}
	}
 	return @resultsPages;
}

sub getAacResults{
	my @files=grep(/.html$/,ls("/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources","lt"));
	my $aacHTMLPath = $files[0];
	$aacHTMLPath =~ s/\n/ /g;
	
	@files=grep(/.log$/,ls("/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources","lt"));
	my $aacLogPath = $files[0];
	$aacLogPath=~ s/\n/ /g;
	
	#add aac log to audit log
	my @logexe=();
	if(!-e "$aacLogPath"){
		&FT_LOG("ERROR:AAC log $aacLogPath does not exist");
	}else{
		open(LOG_FILE, "$aacLogPath");
		@logexe=<LOG_FILE>;
		close(LOG_FILE);
	}
	
	my $aacLogString = join("",@logexe);	
	if(open (AUDIT, ">> $audit_log")){
		print AUDIT "$aacLogString\n";
		close AUDIT;
	}
	
	## Extract tables from AAC html ##
	my @exe=();
	if(!-e "$aacHTMLPath"){
		&FT_LOG("ERROR:AAC HTML log $aacHTMLPath does not exist");
	}else{
		open(LOG_FILE, "$aacHTMLPath");
		@exe=<LOG_FILE>;
		close(LOG_FILE);
	}
	
	my $aacString = join("",@exe);
	
	## regex to remove first two tables and start of html ##
	$aacString =~ s/.*?<\/TABLE>//;
	$aacString =~ s/.*?<\/TABLE>//;
	
	## remove end html ##
	$aacString =~ s/<\/BODY>.*<\/HTML>//g;
	
	## make headings that getSummary() will pick up test suite names ##
	$aacString =~ s/<BR><BR>/<BR>/g;
	$aacString =~ s/(<BR>)(.*?)(<TABLE)/<h3>$2<\/h3><br><TABLE/g;
	$aacString =~ s/(<h3>)(.*?)(<\/h3>)/$1$2$3<br>$2/g;
	
	## some more regex to make formatting the same ##
	$aacString =~ s/<FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>FAIL<\/FONT>/<font color=#ff0000><b>FAIL<\/b><\/font>/g;
	$aacString =~ s/<FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>PASS<\/FONT>/<font color=darkblue><b>PASS<\/b><\/font>/g;
	
	## format html for multiple selenium run
	my $tagIndex = 0;
	my $numberOfTag = () = ($aacString =~ /<\/TR><\/TABLE>/g);
	$aacString =~ s/(<\/TR><\/TABLE>)/++$tagIndex==$numberOfTag? "<\/TR>\n<\/TABLE>":$1/ge;
	#done with the AAC html and log files so delete them
	unlink($aacHTMLPath);
	unlink($aacLogPath);
	return $aacString;
}

sub getTestResults{
	my $path = shift;
	my $testGroup = shift;
	open(EXE,"$path");
	my @contents=<EXE>;
	close(EXE);
	my $result=qq{
	<h3>$testGroup</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	<th>TEST TAG</th>
	<th>TEST NAME</th>
	<th>TEST RESULT</th>
	<th>FAILURE REASON</th>
	</tr>
	};
			
	my @exe=grep /TestResult/, @contents;
	my @startedTests=grep /Starting Test:/, @contents;
	
	if (!@exe) {
		&FT_LOG("Selenium Test Execution DID NOT RUN - FAIL\n");
		$result.="<tr><td>Selenium Test Execution didn't even run for $path</td><td align=center>No Tests</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>Unknown: Check WS, check Selenium files</td></tr>\n";
		$trafficLightColour = "red";
	}
	
	foreach my $exe (@exe){
		$_=$exe;
		my $testresult = $exe;
		my $testtag = $exe;
		my $testname = $exe;
		my $failurereason = "";
		$failurereason = $exe;

		$testtag =~ s/.*TestTag: //;
		$testtag =~ s/TestResult.*//;
		$testname =~ s/.*TestName: //;
		$testname =~ s/TestTag.*//;
		$testresult =~ s/.*TestResult: //;
		$testresult =~ s/FailureReason.*//;
		$failurereason =~ s/.*FailureReason://;

		$testname=~s/\r\n*//g;
		$testname=~s/\n*//g;
		$testname=~s/\r*//g;
		$testname=~s/\n\s*//g;
		$testname=~s/\s//g;
		@startedTests=grep(!/$testname/,@startedTests);
		
		if($testresult =~ /PASS/){
			&FT_LOG("TestTag:$testtag, TestName:$testname, TestResult:$testresult FailureReason:$failurereason\n");
			$result.="<tr><td>$testtag</td><td>$testname</td><td align=center><font color=darkblue><b>PASS</b></font></td><td align=center>n/a</td></tr>\n";
		}elsif($testresult =~ /FAIL/){
			&FT_LOG("TestTag:$testtag, TestName:$testname, TestResult:$testresult, FailureReason:$failurereason\n");
			if($failurereason ne ""){
				$result.="<tr><td>$testtag</td><td>$testname</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>$failurereason</td></tr>\n";
			}else{
				$result.="<tr><td>$testtag</td><td>$testname</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>unknown</td></tr>\n";			
			}
		}else{
			&FT_LOG("TestTag:$testtag, TestName:$testname, TestResult:No result line found, FailureReason:$failurereason\n");
			if($failurereason ne ""){
				$result.="<tr><td>$testtag</td><td>$testname</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>$failurereason and no result line found</td></tr>\n";
			}else{
				$result.="<tr><td>$testtag</td><td>$testname</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>unknown and no result line found</td></tr>\n";			
			}
		}
	}
	
	foreach my $startedTest (@startedTests){
		$startedTest=~ s/.*Starting Test: //g;
		&FT_LOG("TestTag:N/A, TestName:$startedTest, TestResult:Test started but no TestResult line found FailureReason:Unknown\n");
		$result.="<tr><td>N/A</td><td>$startedTest</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>Test started but no TestResult line found</td></tr>\n";			
	}
	$result.="</TABLE>\n";
	return $result;
}

sub getTimedOutCommands{
	my $path=shift;
	open(EXE,"$path");
	my @contents=<EXE>;
	close(EXE);
	my $foundTimedOut=0;
	my $result=qq{
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	<th>TIMED OUT SELENIUM COMMANDS</th>
	<th>TEST RESULT</th>
	<th>FAILURE REASON</th>
	</tr>
	};
	
	opendir (DIR, "/eniq/home/dcuser/automation");
	my @logpath = grep /.*timed-out.log$/, readdir(DIR);
	closedir DIR;
	
	#
	## Extract  from the logfile TestTag, TestName, TestResult, FailReason ##
	#
	foreach my $path(@logpath){
		&FT_LOG("Timed out command record file:$path\n");
		open(RES,"</eniq/home/dcuser/automation/$path");
		my @contents=<RES>;
		close(RES);
		if(@contents){
			$result.="<tr><td>$contents[0]</td><td align=center><font color=ff0000><b>FAIL</b></font></td><td align=center>Timed out</td></tr>\n";
			$foundTimedOut=1;
		}
		unlink("/eniq/home/dcuser/automation/$path");
	}
	
	$result.="</TABLE>\n";
	if(!$foundTimedOut){
		$result="";
	}
	return $result;
}

#------------------------------------------------------------	
#	SUBROUTINES - READ LOGS (1)
#
#	1.	verifyLogs()
#
#------------------------------------------------------------

# 
# METHOD TO GREP ALL THE LOGS AND OUTPUT THE ERRORS, EXCEPTIONS, WARNINGS, SO ON
# Test to grep all the log directories 
# can take a parameter which is a subdirectory
# This process greps the following patterns: 
#"error",
#"exception",
#"fatal",
#"severe", 
#"warning",
#"not found",
#"cannot",
#"not supported",
#"reactivated",
##########################
sub verifyLogs{
	my $subDir=shift;
	my $result="<h3>VERIFY LOGS</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
			<th>Path</th>
			<th>error</th>
			<th>exception</th>
			<th>fatal</th>
			<th>severe</th>
			<th>warning</th>
			<th>not found</th>
			<th>cannot</th>
			<th>not supported</th>
			<th>reactivated</th>
			<th>RESULT</th>
		</tr>
	";

	my @filters=(
	"error",
	"exception",
	"fatal",
	"severe",
	"warning",
	"not found",
	"cannot",
	"not supported",
	"reactivated",
	#";;","null"
	);

	$verifylogs_results = "verifyLogsLog_$run_date.html";
	my $verlogpath = "RegressionLogs/$verifylogs_results";
	open FILE, ">$verlogpath" or die "cant open $verlogpath: $!";
	my @result=undef;
	my $logDir="";
	if( hasSwLog()>3 ){
		$logDir="/eniq/sw/log".$subDir;
	}else{
		$logDir="/eniq/log/sw_log".$subDir;
	}
	
	&FT_LOG("$logDir");
	open(LS,"file $logDir/* |");
	my @ls=<LS>;
	close(LS);
	open(LS2,"file $logDir/engine/* |");
	my @ls2=<LS2>;
	close(LS2);
	my @directories=(@ls,@ls2);
	chomp(@directories);

	foreach my $logDirs (@directories){
		$_=$logDirs;
		if(/directory/){
			my @filterArray = ();
			$logDirs=~s/:.*//;
			my $filterResult = "<td align=center><font color=darkblue><b>PASS</b></font></td>";
			#$result.=  "<h4>$logDirs</h4>";
			print FILE "<h4>$logDirs</h4>";
			#&FT_LOG("file $logDirs/ \n");
			open(LS1,"file $logDirs/ |");
			my @files=<LS1>;
			chomp(@files);
			close(LS1);
			foreach my $files (@files){
				$_=$files;
				$files=~s/:.*//;
				foreach my $filter (@filters){
					#$result.=  "\nFILTER : $filter<br>\n";
					print FILE "\nFILTER : $filter<br>\n";
					open GREP, "egrep -i \"($filter)\" $files/* | egrep -v \"(FINEST| succesfully |Partition created|permissions to |.LOG_AggregationStatus_|inflating|_ERROR)\" |  sed \"s/[0-9][0-9].[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9] //\" | sed \"s/:[0-9]* /:/\" | sed \"s/[0-9][0-9].[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9]. //\" | sed \"s/ 00000..... Exception Thrown/ Exception Thrown/\" | sed \"s/.* O.S Err/ Err/\"  | sort -u | " || &FT_LOG("ERROR: $!\n");
					my @arr=<GREP>;
					close(GREP);
					my $numOfFilterLines = 0;

					foreach my $arr ( @arr ){
						$_=$arr;
						if(/java.lang.|ASA Error|SEVERE|reactivated/){
							#$result.=  "<font color=660000><b>$arr</b></font><br>";
							print FILE "<font color=660000><b>$arr</b></font><br>";
							&FT_LOG("FAIL $arr");
							$filterResult = "<td align=center><font color=#ff0000><b>FAIL</b></font></td>";
						}else{
							$numOfFilterLines = $numOfFilterLines +1;
							#$result.=  "$arr<br>";
							print FILE "$arr<br>";
							print  "$arr";
						}
					} # FOR LINES FOUND
					push (@filterArray, $numOfFilterLines);
				}# FOR FILTER
				$result.="<tr><td>$logDirs</td><td>$filterArray[0]</td><td>$filterArray[1]</td><td>$filterArray[2]</td><td>$filterArray[3]</td><td>$filterArray[4]</td><td>$filterArray[5]</td><td>$filterArray[6]</td><td>$filterArray[7]</td><td>$filterArray[8]</td>$filterResult</tr>\n";
			} # FOR EACH DIRECTORY
		}# IF
	} # FOR

	# foreach my $logDirs (@directories)
	# {
		# $_=$logDirs;
		# if(/directory/)
		# {
			# $logDirs=~s/:.*//;
			# $result.=  "<h4>$logDirs</h4>";
			#&FT_LOG("file $logDirs/ \n");
			# open(LS1,"file $logDirs/ |");
			# my @files=<LS1>;
			# chomp(@files);
			# close(LS1);
			# foreach my $files (@files)
			# {
				# $_=$files;
				# $files=~s/:.*//;
				# foreach my $filter (@filters)
				# {
					# $result.=  "\nFILTER : $filter<br>\n";
					# open GREP, "egrep -i \"($filter)\" $files/* | egrep -v \"(FINEST| succesfully |Partition created|permissions to |.LOG_AggregationStatus_|inflating|_ERROR)\" |  sed \"s/[0-9][0-9].[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9] //\" | sed \"s/:[0-9]* /:/\" | sed \"s/[0-9][0-9].[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9]. //\" | sed \"s/ 00000..... Exception Thrown/ Exception Thrown/\" | sed \"s/.* O.S Err/ Err/\"  | sort -u | " || &FT_LOG("ERROR: $!\n"); 
					# my @arr=<GREP>;
					# close(GREP);
					# foreach my $arr ( @arr )
					# {
						# $_=$arr;
						# if(/java.lang.|ASA Error|SEVERE|reactivated/)
						# {
							# $result.=  "<font color=660000><b>$arr</b></font><br>";
							# &FT_LOG("FAIL $arr");
						# }
						# else
						# {
							# $result.=  "$arr<br>";
							# print  "$arr";
						# }
					# } # FOR LINES FOUND
				# }# FOR FILTER
			# } # FOR EACH DIRECTORY
		# }# IF
	# } # FOR

	close FILE;
	$result.="</TABLE> <br>\n";
	return $result;
}

############################################################
# This is a utility subroutine
# just to check
# if the /eniq/sw/log has files
############################################################
sub hasSwLog{
	open(SWLOG,"ls -altr  /eniq/sw/log | wc -l | ");
	my @swlog=<SWLOG>;
	close(SWLOG);
	return $swlog[0];
}

#------------------------------------------------------------	
#	SUBROUTINES - ADMINUI, CONFIGURATION (10)
#
#	1.	MonitoringRules()
#	2.	TypeConfig()
#	3.	DWHConfig()
#	4.	LoggingInfo()
#	5.	LoggingSevere()
#	6.	LoggingWarning()
#	7.	LoggingConfig()
#	8.	LoggingFine()
#	9.	LoggingFiner()
#	10.	LoggingFinest()
#
#------------------------------------------------------------

#############################
# MONITORINGRULES           # 
# This test is not finished.#
# Currently is just a stub  #
#############################
sub MonitoringRules{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	my @status=executeThisWithLogging("egrep -c '(is running OK)' /eniq/home/dcuser/automation/html/liclog.html") ;
	if($status[0] > 20 )
	{
		&FT_LOG("PASS\n");
		#$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.="FAIL<br>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
}

############################### 
# TYPECONFIG                  #
# This test is not finished.  #
# Currently is just a stub    #
###############################
sub TypeConfig{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	my @status=executeThisWithLogging("egrep -c '(is running OK)' /eniq/home/dcuser/automation/html/liclog.html") ;
	if($status[0] > 20){
		&FT_LOG("PASS\n");
		#$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.="FAIL<br>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
}

#####################################################################################
# DWHCONFIG                                                                         #
# This subroutine goes to AdminUI, to DWH Configuration                             # 
# just checks that the different partitions exist, but does not configure anything  #
# because can result in data loss or database failure                               # 
#####################################################################################
sub DWHConfig{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/ShowPartitionPlan\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/partplan.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/ShowPartitionPlan\"");
	my @status=executeThisWithLogging("egrep '(EditPartitionPlan)' /eniq/home/dcuser/automation/html/partplan.html") ;
	my $result=qq{
	<h3>RUN DWHCONFIG CHECK FOR PRESENCE OF PARTITIONS</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>PARTITION</th>
	 <th>STATUS</th>
	</tr>
	};
	
	foreach my $status (@status){
		$_=$status;
		$status=~s/.*<font size.*><a.*">//;
		$status=~s/.*<font size.*">//;
		$status=~s/<.a><.font>//;
		$status=~s/<.font>//;
		if(/extralarge_count|extralarge_day|extralarge_daybh|extralarge_plain|extralarge_rankbh|extralarge_raw|extrasmall_count|ext
		rasmall_day|extrasmall_daybh|extrasmall_plain|extrasmall_rankbh|extrasmall_raw|large_count|large_day|large_daybh|large_plain|
		large_rankbh|large_raw|medium_count|medium_day|medium_daybh|medium_plain|medium_rankbh|medium_raw|small_count|small_day|small
		_daybh|small_plain|small_rankbh|small_raw|sgeh_15min|sgeh_1min|sgeh_day|sgeh_raw|sgeh_raw_lev2|sgehextralarge_15min|sgehextra
		large_1min|sgehextralarge_day|sgehextrasmall_15min|sgehextrasmall_1min|sgehextrasmall_day|sgehlarge_15min|sgehlarge_1min|sgeh
		large_day|sgehlarge_raw|sgehmedium_15min|sgehmedium_1min|sgehmedium_day|sgehmedium_raw|sgehsmall_15min|sgehsmall_1min|sgehsma
		ll_day|sgehsmall_raw/)
		{
			$result.="<tr><td>$status:</td><td><font color=darkblue><b>PASS</b></font></td></tr>\n"
		}else{
			$result.="<tr><td>$status:</td><td><font color=ff0000><b>FAIL</b></font></td></tr>\n"
		}
	}

	#LOGOUT
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

# 
# LOGGING_INFO
# TODO
sub LoggingInfo{
	#$LOCALHOST/servlet/EditLogging
}

# 
# LOGGING_SEVERE
# TODO
sub LoggingSevere{
	#$LOCALHOST/servlet/EditLogging
}

# 
# LOGGING_WARNING
# TODO
sub LoggingWarning{
	#$LOCALHOST/servlet/EditLogging
}

# 
# LOGGING_CONFIG
# TODO
sub LoggingConfig{
	#$LOCALHOST/servlet/EditLogging
}

# 
# LOGGING_FINE
# TODO
sub LoggingFine{
	#$LOCALHOST/servlet/EditLogging
}

# 
# LOGGING_FINER
# TODO
sub LoggingFiner{
	#$LOCALHOST/servlet/EditLogging
}

# 
# LOGGING_FINEST
# TODO
sub LoggingFinest{
	#$LOCALHOST/servlet/EditLogging
}

#------------------------------------------------------------	
#	SUBROUTINES - ADMIN UI, DATA FLOW MONITORING (6)
#
#	1.	ShowLoadingFutureDates()
#	2.	ShowProblematic()
#	3.	ShowAggFutureDates()
#	4.	Reaggregation()
#
#------------------------------------------------------------

#
# SHOW LOADING FUTURE DATES
# Show loadings with future dates should display  XXXXXXXXXXXX for the dates, 
# if not is failed
# if so is passed.
#
sub ShowLoadingFutureDates{
	my $year   =getYearTimewarp();
	my $month  ="12";
	my $day    ="31";
	my $tp     ="-";
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/future.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&techPackName=$tp&getInfoButton='Get Information'\"  \"$LOCALHOST/adminui/servlet/ShowLoadStatus\"");
	#sleep(1);
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	#sleep(1);
	# GET LOADING
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/future.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&techPackName=$tp&getInfoButton='Get Information'\"  \"$LOCALHOST/adminui/servlet/ShowLoadStatus\"");

	my $result="";

	open(WO,"< /eniq/home/dcuser/automation/html/future.html ");
	my @wo=<WO>;
	close(WO);
	my $found=0;
	foreach my $wo (@wo){
		$_=$wo;
		if(/&nbsp;X&nbsp;/){
			$found++;
		}
	}
	
	if($found == 24){
		&FT_LOG("PASS\n");
		$result.= "<font color=darkblue><b>PASS</b></font><br>\n";
	}else{
		&FT_LOG("FAIL\n");
		$result.= "<font color=#ff0000><b>FAIL</b></font><br>\n";
	}
	#LOGOUT
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

#
# SHOW PROBLEMATIC
# This is to be coded, still this part of AdminUI does not work properly
#
sub ShowProblematic{
#$LOCALHOST/servlet/ShowLoadStatus
}

#
# SHOW AGG FUTURE DATES
# This test checks that AdminUI in ShowAggregation for future dates 
# displays 'No Day Data'
#
sub ShowAggFutureDates{
	my $year   =getYearTimewarp();
	my $month  ="12";
	my $day    ="31";
	my $tp     ="-";

	# SAVE COOKIES
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&type=$tp&action=/servlet/ShowAggregations&value='Get Information'\"  \"$LOCALHOST/adminui/servlet/ShowAggregations\"");
	#sleep(1);
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget  --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	#sleep(1);
	# GET AGGREGATION
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/futagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&type=$tp&value='Get Information'\"  \"$LOCALHOST/adminui/servlet/ShowAggregations\"");
	my $result="";

	open(WO,"< /eniq/home/dcuser/automation/html/futagg.html ");
	my @wo=<WO>;
	close(WO);
	my $found=0;
	foreach my $wo (@wo){
		$_=$wo;
		if(/No Day Data/){
			$found++;
		}
	}
	if($found == 1){
		&FT_LOG("PASS\n");
		$result.= "<font color=darkblue><b>PASS</b></font><br>\n";
	}else{
		&FT_LOG("FAIL\n");
		$result.= "<font color=#ff0000><b>FAIL</b></font><br>\n";
	}
	
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

#
# REAGGREGATION
# This subroutine is in charge or running ALL possible reaggregations
# WARNING: when this test is run it creates a huge queue of reaggregations!!!
#
sub Reaggregation{
	my $level= shift;
	my $result= "";
	my @tps=getAllTechPacksAgg();
	#my @tps=("DC_E_SNMP:((4)):&DC_E_SNMP");
	chomp(@tps);
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=localtime(time);

	$year=$year+1900;
	my $month= sprintf("%02d",$mon+1);
	my $day  = sprintf("%02d",$mday);
	my $week = int($yday / 7) + 1;
	if($level eq "DAY"){ 
		foreach my $tp (@tps){
			$_=$tp;
			my $oops=$tp;
			$oops=~s/:.*//;
			$tp=~s/:&/&/g;
			$tp=~s/:/\\%3A/g;
			$tp=~s/&/\%26/g;
			$tp=~s/\(/\\%28/g;
			$tp=~s/\)/\\%29/g;
			&FT_LOG("$oops $level\n");
			my @alltables=getAllTables4TP($oops);
			my $cmd=build_post("$year-$month-$day", \@alltables);
			#&FT_LOG("$cmd\n");
			system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"aggregate=Aggregate&timelevel_changed=&level=$level&year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&batch_name=$tp&checkall=on&$cmd\" \"$LOCALHOST/adminui/servlet/Aggregation\"");
			# SEND USR AND PASSWORD
			system("/usr/sfw/bin/wget  --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
			# post Information
			system("/usr/sfw/bin/wget  --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/dayagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"aggregate=Aggregate&timelevel_changed=&level=$level&year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&batch_name=$tp&checkall=on&$cmd\" \"$LOCALHOST/adminui/servlet/Aggregation\"");
			my @status=executeThisWithLogging("grep -c 'window.location=.ShowAggregations'  /eniq/home/dcuser/automation/html/dayagg.html");
			if($status[0] eq "1"){
				&FT_LOG("PASS\n");
			}else{
				&FT_LOG("FAIL\n");
			}
		}
	}elsif( $level eq "WEEK"){
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"timelevel_changed=yes&level=$level&\" $LOCALHOST/adminui/servlet/Aggregation");
		# SEND USR AND PASSWORD
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/listtpsweekagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
		my @listtps=executeThisWithLogging("grep '		  				    <option value=.' /eniq/home/dcuser/automation/html/listtpsweekagg.html ");
		chomp(@listtps);
		my @tps=();
		foreach my $tps (@listtps){
			 $_=$tps;
			 $tps=~s/.*="//;
			 $tps=~s/">.*//;
			 $tps=~s/<.option>//;
			 push @tps, $tps;
		}
		#&FT_LOG("\nGOT TPS: @tps\n");
		foreach my $tp (@tps){
			$_=$tp;
			my $oops=$tp;
			$oops=~s/:.*//;
			$tp=~s/:&/&/g;
			$tp=~s/:/%3A/g;
			$tp=~s/&/%26/g;
			$tp=~s/\(/%28/g;
			$tp=~s/\)/%29/g;
			&FT_LOG("$oops $level\n");
			# get table names
			system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/listweekagg0.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"list=List&timelevel_changed=&level=$level&year_1=$year&week_1=1&year_2=$year&week_2=53&batch_name=$tp&\" $LOCALHOST/adminui/servlet/Aggregation");

			# post Information
			my @list=executeThisWithLogging("grep '      	.td class=.white_row_10...input type=.checkbox. name..aggregated. value=.' /eniq/home/dcuser/automation/html/listweekagg0.html ");
			my @alltables=();
			foreach my $list (@list){
				$_=$list;
				$list=~s/.*="//;
				$list=~s/.*="//;
				$list=~s/.*="//;
				$list=~s/.*="//;
				$list=~s/">.*//;
				$list=~s/<.option>//;
				push @alltables, $list;
			}
			&FT_LOG("ALL TABLES: @alltables\n");
			my $cmd=build_post("", \@alltables);
			# DO REAGGREGATIONS PLEASE
			system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/weekagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"aggregate=Aggregate&timelevel_changed=&level=$level&year_1=$year&week_1=$week&year_2=$year&week_2=$week&batch_name=$tp&checkall=on&$cmd\" $LOCALHOST/adminui/servlet/Aggregation");
			my @status=executeThisWithLogging("grep -c 'window.location=.ShowAggregations'  /eniq/home/dcuser/automation/html/weekagg.html");
			if($status[0] eq "1"){
				&FT_LOG("PASS\n");
			}else{
				&FT_LOG("FAIL\n");
			}
		}
	}elsif($level eq "MONTH"){
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"timelevel_changed=yes&level=$level&\" $LOCALHOST/adminui/servlet/Aggregation");
		# SEND USR AND PASSWORD
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/listtpsmonthagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
		my @listtps=executeThisWithLogging("grep '		  				    <option value=.' /eniq/home/dcuser/automation/html/listtpsmonthagg.html ");
		chomp(@listtps);
		my @tps=();
		foreach my $tps (@listtps){
			$_=$tps;
			$tps=~s/.*="//;
			$tps=~s/">.*//;
			$tps=~s/<.option>//;
			push @tps, $tps;
		}
		#&FT_LOG("\nGOT TPS: @tps\n");
		foreach my $tp (@tps){
			$_=$tp;
			my $oops=$tp;
			$oops=~s/:.*//;
			$tp=~s/:&/&/g;
			$tp=~s/:/%3A/g;
			$tp=~s/&/%26/g;
			$tp=~s/\(/%28/g;
			$tp=~s/\)/%29/g;
			&FT_LOG("$oops $level\n");
			# get table names
			system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/listmonthagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"list=List&timelevel_changed=&level=$level&year_1=$year&month_1=1&year_2=$year&month_2=12&batch_name=$tp&\" $LOCALHOST/adminui/servlet/Aggregation");

			# post Information
			my @list=executeThisWithLogging("grep '      	.td class=.white_row_10...input type=.checkbox. name..aggregated. value=.' /eniq/home/dcuser/automation/html/listmonthagg.html ");
			my @alltables=();
			foreach my $list (@list){
				$_=$list;
				$list=~s/.*="//;
				$list=~s/.*="//;
				$list=~s/.*="//;
				$list=~s/.*="//;
				$list=~s/">.*//;
				$list=~s/<.option>//;
				push @alltables, $list;
			}
			
			&FT_LOG("ALL TABLES: @alltables\n");
			my $cmd=build_post("", \@alltables);
			# DO REAGGREGATIONS PLEASE
			system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/monthagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"aggregate=Aggregate&timelevel_changed=&level=$level&year_1=$year&month_1=$month&year_2=$year&month_2=$month&batch_name=$tp&checkall=on&$cmd\" $LOCALHOST/adminui/servlet/Aggregation");
			print("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/monthagg.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"aggregate=Aggregate&timelevel_changed=&level=$level&year_1=$year&month_1=$month&year_2=$year&month_2=$month&batch_name=$tp&checkall=on&$cmd\" $LOCALHOST/adminui/servlet/Aggregation\n");
			my @status=executeThisWithLogging("grep -c 'window.location=.ShowAggregations'  /eniq/home/dcuser/automation/html/monthagg.html");
			if($status[0] eq "1"){
				&FT_LOG("PASS\n");
			}else{
				&FT_LOG("FAIL\n");
			}   
		}
	}
	
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

# 
# SESSIONLOGS
# This subroutine goes to AdminUI and checks that the Session logs do not display /ERROR|EXCEPTION|FAILED|NOT FOUND/i
# else the test is failed, is so then is passed
#
sub SessionLogs{
	my $result = "";
	my $st=0;
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=localtime(time);

	$year=$year+1900;
	my $month = sprintf("%02d",$mon+1);
	my $day  = sprintf("%02d",$mday);

	# Adaptor Log
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&a_status=ERROR&selectedtable=0&selectedpack=&source=&a_filename=&action=/Servlet/ETLSessionLog&search=Search\" $LOCALHOST/adminui/servlet/ETLSessionLog");

	# Adaptor Log SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# Adaptor Log - post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/adaptorsessionlog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&a_status=ERROR&selectedtable=0&selectedpack=&source=&a_filename=&action=/Servlet/ETLSessionLog&search=Search\" $LOCALHOST/adminui/servlet/ETLSessionLog");

	# Loader Log
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&a_status=OK&selectedtable=1&selectedpack=&source=&a_filename=&action=/Servlet/ETLSessionLog&search=Search\" $LOCALHOST/adminui/servlet/ETLSessionLog");

	# Loader Log SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# Loader Log - post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/loadersessionlog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&a_status=OK&selectedtable=1&selectedpack=&source=&a_filename=&action=/Servlet/ETLSessionLog&search=Search\" $LOCALHOST/adminui/servlet/ETLSessionLog");

	# Aggregator Log
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&a_status=OK&selectedtable=2&selectedpack=&source=&a_filename=&action=/Servlet/ETLSessionLog&search=Search\" $LOCALHOST/adminui/servlet/ETLSessionLog");

	# Aggregator Log SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# Aggregator Log - post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/aggregatorsessionlog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&a_status=OK&selectedtable=2&selectedpack=&source=&a_filename=&action=/Servlet/ETLSessionLog&search=Search\" $LOCALHOST/adminui/servlet/ETLSessionLog");

	# Search Adaptor Logs for Errors
	&FT_LOG("Checking Adaptor Logs\n");
	$result.="Checking Adaptor Logs<br>\n";

	my @status=executeThisWithLogging("cat /eniq/home/dcuser/automation/html/adaptorsessionlog.html") ;
	chomp(@status);
	my $ptr=0;
	foreach my $status (@status) {
		$_=$status;

		if(/<TABLE border=.1. width=.800. cellpadding=.1. cellspacing=.1.>/){
			$ptr=1;
		}
		if(/			<.table>/){
			$ptr=0;
		}

		if($ptr==1){
			$status=~s/\s+//g;
			$status=~s/<td.*><.*>//g;
			$status=~s/<.font><.td>//g;
			if(/ERROR|EXCEPTION|FAILED|NOT FOUND/i)
			{$st++;}
		}
	}

	# Search Loader logs for Errors
	&FT_LOG("Checking Loader Logs\n");
	$result.="Checking Loader Logs<br>\n";

	@status=executeThisWithLogging("cat /eniq/home/dcuser/automation/html/loadersessionlog.html") ;
	chomp(@status);
	$ptr=0;
	foreach my $status (@status){
		$_=$status;

		if(/<TABLE border=.1. width=.800. cellpadding=.1. cellspacing=.1.>/){
			$ptr=1;
		}
		if(/          <.table>/){
			$ptr=0;
		}

		if($ptr==1){
			$status=~s/\s+//g;
			$status=~s/<td.*><.*>//g;
			$status=~s/<.font><.td>//g;
			if(/ERROR|EXCEPTION|FAILED|NOT FOUND/i)
			{$st++;}
		}
	}

	# Search Aggregator Logs for Errors
	&FT_LOG("Checking Aggregator Logs\n");
	$result.="Checking Aggregator Logs<br>\n";

	@status=executeThisWithLogging("cat /eniq/home/dcuser/automation/html/aggregatorsessionlog.html") ;
	chomp(@status);
	$ptr=0;
	foreach my $status (@status){
		$_=$status;

		if(/<TABLE border=.1. width=.800. cellpadding=.1. cellspacing=.1.>/){
			$ptr=1;
		}
		if(/          <.table>/){
			$ptr=0;
		}

		if($ptr==1){
			$status=~s/\s+//g;
			$status=~s/<td.*><.*>//g;
			$status=~s/<.font><.td>//g;
			if(/ERROR|EXCEPTION|FAILED|NOT FOUND/i)
			{$st++;}
		}
	}

	if($st ==0 ){
		&FT_LOG("PASS\n");
		$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		$result.="FAIL<br>\n";
	}

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

# 
# DATASOURCELOGS
# This subroutine goes to AdminUI and checks that the DataSource logs do not display /ERROR|EXCEPTION|FAILED|NOT FOUND/i
# else the test is failed, is so then is passed
#
sub DataSourceLogs{
	my $st=0;
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=localtime(time);

	$year=$year+1900;
	my $month= sprintf("%02d",$mon+1);
	my $day  = sprintf("%02d",$mday);

	my $result=qq{
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	 <th>SOURCE</th>
	 <th>UNAVL MINS</th>
	 <th>PERIOD FROM</th>
	 <th>PERIOD TO</th>
	</tr>
	};

	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"submitted=true&flag=0&year_1=$year&month1=$month&day_1=$day&year_2=$year&month_2=$month&day_2=$day&start_hour=0&end_hour=23&start_min=0&end_min=0&action=/Servlet/NELog&search=Search\" $LOCALHOST/adminui/servlet/NELog");

	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");

	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/datasourcelog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt  --post-data \"submitted=true&flag=0&year_1=$year&month_1=$month&day_1=$day&year_2=$year&month=$month&day_2=$day&start_hour=0&end_hour=23&start_min=0&end_min=0&action=/Servlet/NELog&search=Search\" $LOCALHOST/adminui/servlet/NELog");

	my @status=executeThisWithLogging("cat /eniq/home/dcuser/automation/html/datasourcelog.html") ;
	chomp(@status);
	my $ptr=0;
	
	foreach my $status (@status){
		$_=$status;

		if(/	<table style=.border:.px solid ......... width=.500. cellpadding=.0. cellspacing=.0.>/){
			$ptr=1;
		}
		if(/	<.table>/){
			$ptr=0;
		}

		if($ptr==1){
			# Match on Source
			if(/					<td  style=.border-bottom:1px solid .*border-left.*size=.1.>.*<.font><.td>/){
				$status=~ s/.*size.....//;
				$status=~ s/<.font.*//;
				$result.= "<tr><td>$status</td>";
			}
			# Match on Unavl minutes
			elsif(/					<td  style=.border-bottom:1px solid .*align..center.*size=.1.>.*<.font><.td>/){
				$status=~ s/.*size.....//;
				$status=~ s/<.font.*//;
				$result.= "<td>$status</td>";
			}
			# Match on From:
			elsif(/						<td align=.center.*size=.1.>.*&.*<.font><.td>/){
				$status=~ s/.*size.....//;
				$status=~ s/&.*<.font.*//;
				$result.= "<td>$status</td>";
			}
			# Match on To:
			elsif(/						<td  align=.center.*size=.1.>.*<.font><.td>/){
				$status=~ s/.*size.....//;
				$status=~ s/<.font.*//;
				$result.= "<td>$status</td></tr>\n";
			}
		}
	}

	if($st ==0 ){
		&FT_LOG("PASS\n");
		#$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.="FAIL<br>\n";
	}

	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	$result.=qq{
	</TABLE>
	};
	return $result;
}

sub verifyDataGenTimes{


	my  $result=qq{
	<br/>
	<br/>
	<h3>Basic Datagen Time Check</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	my @dataTables = ('event_e_sgeh_raw',
		'event_e_lte_raw',
#		'event_e_mss_voice_cdr_raw',
#		'event_e_mss_sms_cdr_raw',
		'event_e_lte_cfa_err_raw',
		'event_e_lte_hfa_err_raw',
#		'EVENT_E_RAN_HFA_SOHO_ERR_RAW',
#		'EVENT_E_RAN_HFA_IFHO_ERR_RAW',
#		'EVENT_E_RAN_HFA_IRAT_ERR_RAW',
#		'EVENT_E_RAN_HFA_HSDSCH_ERR_RAW',
#		'EVENT_E_RAN_CFA_ERR_RAW',
               'EVENT_E_LTE_TRAC_PAGING_SUC_15MIN',
		'EVENT_E_LTE_TRAC_PAGING_ERR_15MIN',);

	
	my $msg;
	foreach(@dataTables){
		$msg = verifyDataGenLoading($_, 60);
		$_ = $msg;

		if(/Exception|Fail|Error/){
			$result.="<tr><td>$msg</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
			&FT_LOG("$msg - No Data \n");
		}else{
			$result.="<tr><td>$msg</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
			&FT_LOG("$msg - PASS\n");
		}
	}
	$result.="</TABLE>";
	
	my  $result2=qq{
	<br/>
	<br/>
	<h3>CEP Datagen Time Check</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	my @cepDataTables = (
		'EVENT_E_RAN_SESSION_RAW',
		'EVENT_E_CORE_SESSION_RAW',
		'EVENT_E_RAN_SESSION_CELL_VISITED_RAW',
		'EVENT_E_RAN_SESSION_INTER_OUT_HHO_RAW',
		'EVENT_E_RAN_SESSION_INTER_SYS_UTIL_RAW',
		'EVENT_E_RAN_SESSION_RRC_MEAS_RAW',
		'EVENT_E_RAN_SESSION_SUC_HSDSCH_CELL_CHANGE_RAW',
		'EVENT_E_USER_PLANE_TCP_RAW',
		'EVENT_E_USER_PLANE_CLASSIFICATION_RAW');
		
	
	
	if (parseVersion(get_eniq_version()) > parseVersion('3.0.15')) {
		my $msg;
		foreach(@cepDataTables){
			$msg = verifyDataGenLoading($_, 60);
			$_ = $msg;

			if(/Exception|Fail|Error/){
				$result2.="<tr><td>$msg</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
				&FT_LOG("$msg - No Data \n");
			}else{
				$result2.="<tr><td>$msg</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
				&FT_LOG("$msg - PASS\n");
			}
		}
		$result2.="</TABLE>";
		$result =$result.$result2;
	}
		
	my  $result3=qq{	
	<br/>
	<br/>
	<h3>CTUM Workflow status</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};

	
	my $disablesCTUMWorkflowsCount = `/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep STREAMING_CTUM* | wc -l`;

  
	my @disabledWorkflows = executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep STREAMING_CTUM*");;
	if ($disablesCTUMWorkflowsCount > 0) {
		#$result3.="<tr><td>Error - Some workflows are aborting</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		foreach my $failed_workflow (@disabledWorkflows){
			$result3.="<tr><td>Error - $failed_workflow</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		&FT_LOG("Some workflows are failing - FAIL\n");
		#&FT_LOG("Some workflows are failing - FAIL\n@disabledWorkflows\n");
	}else{
		$result3.="<tr><td>CTUM Workflow status</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Workflows are running correctly - PASS\n");
	}
	
	$result3.="</TABLE>";
	$result =$result.$result3;
	
	
	my  $result4=qq{	
	<br/>
	<br/>
	<h3>CTR Workflow status</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	my $disablesCTRWorkflowsCount = `/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep STREAMING_CTR* |  grep -v STREAMING_CTUM* | wc -l`;

  
	my @disabledWorkflows = executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep STREAMING_CTR* |  grep -v STREAMING_CTUM*");;
	if ($disablesCTRWorkflowsCount > 0) {
		#$result6.="<tr><td>Error - Some workflows are aborting</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		foreach my $failed_workflow (@disabledWorkflows){
			$result4.="<tr><td>Error - $failed_workflow</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		&FT_LOG("Some workflows are failing - FAIL\n");
		#&FT_LOG("Some workflows are failing - FAIL\n@disabledWorkflows\n");
	}else{
		$result4.="<tr><td>CTR Workflow status</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Workflows are running correctly - PASS\n");
	}
	
	$result4.="</TABLE>";
	$result =$result.$result4;
	
	my  $result5=qq{	
	<br/>
	<br/>
	<h3>LTE Workflow status</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	my $disablesLTEWorkflowsCount = `/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep EBSL.* | wc -l`;

	my @disabledWorkflows = executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep EBSL.*");;

	if ($disablesLTEWorkflowsCount > 0) {
		#$result5.="<tr><td>Error - Some workflows are aborting</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		foreach my $failed_workflow (@disabledWorkflows){
			$result5.="<tr><td>Error - $failed_workflow</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		&FT_LOG("Some workflows are failing - FAIL\n");
		#&FT_LOG("Some workflows are failing - FAIL\n@disabledWorkflows\n");
	}else{
		$result5.="<tr><td>LTE Workflow status</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Workflows are running correctly - PASS\n");
	}
	
	
	$result5.="</TABLE>";
	$result =$result.$result5;

	my  $result6=qq{	
	<br/>
	<br/>
	<h3>SGEH Workflow status</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	my $disablesSGEHWorkflowsCount = `/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep SGEH.* | grep OSSRC1 | wc -l`;

	my @disabledWorkflows = executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode D | grep SGEH.* | grep OSSRC1");;

	if ($disablesSGEHWorkflowsCount > 0) {
		#$result6.="<tr><td>Error - Some workflows are aborting</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		foreach my $failed_workflow (@disabledWorkflows){
			$result6.="<tr><td>Error - $failed_workflow</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		&FT_LOG("Some workflows are failing - FAIL\n");
		#&FT_LOG("Some workflows are failing - FAIL\n@disabledWorkflows\n");
	}else{
		$result6.="<tr><td>SGEH Workflow status</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("Workflows are running correctly - PASS\n");
	}
	
	
	$result6.="</TABLE>";
	$result =$result.$result6;
	
	

	my  $result7=qq{
	<br/>
	<br/>
	<h3>Core File Check</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	   <tr>
		 <th>TEST STAGES</th>
		 <th>STATUS</th>
	   </tr>
	};
	
	my @core_files=executeThisWithLogging("find / -perm 600 -name *core* -print 2>/dev/null");
	

	if(grep(/core/,@core_files)){
		$result7.="<tr><td>Error - Core files found</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		foreach my $core_file (@core_files){
			$result7.="<tr><td>Core file: $core_file</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		&FT_LOG("Error - Core files found in the system \n @core_files - FAIL\n");
	}else{
		$result7.="<tr><td>No Core files found in system</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("No core files in the system - PASS\n");
	}
	$result7.="</TABLE>";
	$result =$result.$result7;
	
	my  $result8=qq{
	<br/>
	<br/>
	<h3>TOR WorkFlow Check</h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
		<tr>
			<th>TEST STAGES</th>
			<th>STATUS</th>
		</tr>
	};

	my @tor_wf=executeThisWithLogging("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist -mode E | grep Tor | cut \-f1 \-d \" \"");
	if(grep(/Tor/,@tor_wf)){
		$result8.="<tr><td>Error - Tor WF in Enabled State</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		foreach my $tor_wf_nm (@tor_wf){
			$result8.="<tr><td>WF Name: $tor_wf_nm</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
		}
		&FT_LOG("Error - TOR WF in Enabled State \n @tor_wf - FAIL\n");
	}else{
		$result8.="<tr><td>No TOR WFs found enabled in system</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
		&FT_LOG("No TOR WFs found enabled in system - PASS\n");
	}
	$result8.="</TABLE>";
	$result =$result.$result8;
	
	
	my  $result9=qq{
        <br/>
        <br/>
        <h3>EXCEPTION Check</h3>
        <TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
                <tr>
                        <th>TEST STAGES</th>
                        <th>STATUS</th>
                </tr>
        };
#       my $today=strftime "%Y_%m_%d_%H:%M:%S", localtime;

        my $exceptionlog = verifyexception();
	 &FT_LOG("Exception log is $exceptionlog \n");
	chomp($exceptionlog);
        if(-e $exceptionlog){
                $result9.="<tr><td>Error - Exception(s) found. Please find the exceptions at $exceptionlog</td><td align=center><font color=ff0000><b>FAIL</b></font></td></tr>\n";
                &FT_LOG("Error - Exceptions found - FAIL\n");
        }else{
                $result9.="<tr><td>No Exceptions found</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>\n";
                &FT_LOG("No Exceptions - PASS\n");
        }
        $result9.="</TABLE>";
        $result =$result.$result9;
	
	return $result;
}

#------------------------------------------------------------	
#	SUBROUTINES - ADMIN UI, DATA VERIFICATION (4)
#
#	1.	DataRowInfo()
#	2.	ShowRefTables()
#	3.	busyhour()
#	4.	RankBh()
#
#------------------------------------------------------------

#
# DATAROWINFO
# This test is not finished.
# This subroutine should go to AdminUI and verify each of the DataRow Info tables for certain dates.
# Currently is just a stub
#
sub DataRowInfo{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	my @status=executeThisWithLogging("egrep -c '(is running OK)' /eniq/home/dcuser/automation/html/liclog.html") ;
	if($status[0] > 20){
		&FT_LOG("PASS\n");
		#$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.="FAIL<br>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
}

# 
# SHOWREFTABLES
# This test is not finished.
# Currently is just a stub
#
sub ShowRefTables{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	my @status=executeThisWithLogging("egrep -c '(is running OK)' /eniq/home/dcuser/automation/html/liclog.html") ;
	if($status[0] > 20){
		&FT_LOG("PASS\n");
		#$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.="FAIL<br>\n";
	}
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
}

#
# BUSYHOUR 
# This process was coded with Liam Burke
# It goes to AdminUI and checks the Busyhour results
# if the techpack has BH information it passes the tc.
#
sub busyhour{
	my $result;
	my $year     =getYearTimewarp();
	my $month_2  =getMonthTimewarp();
	my $day      =getDayTimewarp();
	my $year_2   =getYearTimewarp();
	my $month    =sprintf("%02d",getMonthTimewarp()-1);
	my $day_2    ="01";
	system("rm /eniq/home/dcuser/automation/html/cookies.txt");
	# SAVE COOKIES
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/bh.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/ViewBHInformation\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# GET BUSYHOUR Information
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/bh.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/ViewBHInformation\"");
	open(BHTABLES," < bh.html");
	my @bhtablesRAW=<BHTABLES>;
	chomp(@bhtablesRAW);
	close(BHTABLES);
	#system("rm /eniq/home/dcuser/automation/html/bh.html");
	my @bhtables=undef;
	
	foreach my $bhtables (@bhtablesRAW){
		$_=$bhtables;
		if(/																						<option value=/){
			$bhtables =~s/																						<option value=.//;
			$bhtables =~s/".*//;
			# " - comment added for editor compatibility
			push @bhtables, $bhtables;
		} 
	}
	
	$result.=qq{
	<h3>ADMINUI: SHOW BUSY HOUR STATUS </h3>
	<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>
	<tr>
	<th>TABLENAME</th>
	<th >DESCRIPTION</th>
	<th >RESULT</th>
	</tr>
	};

	foreach my $tp (@bhtables){
		$_=$tp;
		next if(/^$/);
		#$result.="<br><h3>$tp</h3><BR>\n";
		# SAVE COOKIES
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/bh_$tp.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month_2&day_2=$day&search_string=$tp&search_done=true&submit='Get BH Information'\"  \"$LOCALHOST/adminui/servlet/ViewBHInformation\"");
		#sleep(1);
		# SEND USR AND PASSWORD
		system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
		#sleep(1);
		# GET BUSYHOUR Information
		system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/bh_$tp.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"year_1=$year&month_1=$month&day_1=$day&year_2=$year&month_2=$month_2&day_2=$day&search_string=$tp&search_done=true&submit='Get BH Information'\"  \"$LOCALHOST/adminui/servlet/ViewBHInformation\"");

		open(BHTable,"< /eniq/home/dcuser/automation/html/bh_$tp.html");
		my @BHTables=<BHTable>;
		chomp(@BHTables);
		close(BHTable);
		system("rm /eniq/home/dcuser/automation/html/bh_$tp.html");

		my $tpack=0;
		my $found=0;

		foreach my $BHTables (@BHTables){
			$_=$BHTables;
			$BHTables=~ s/	//g;
			if(/Day Busyhour|Month Busyhour/i){
				$result.="<tr><td>$tp</td><td>$BHTables</td><td align=center><font color=darkblue><b>PASS</b></font></td></tr>";
				&FT_LOG("	$tp	$BHTables	PASS\n");
				$found=1;
			}
		}
		if($found==0){
			$result.="<tr><td>$tp</td><td></td><td align=center><font color=#ff0000><b>FAIL</b></font></td></tr>";
			&FT_LOG("       $tp     FAIL\n");
		}
	}
	
	$result.="</TABLE>\n";
	#LOGOUT
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
	return $result;
}

#
# RANKBH
# This test is not finished.
# Currently is just a stub
#
sub RankBh{
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt  \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	# SEND USR AND PASSWORD
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /dev/null --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data 'action=j_security_check&j_username=$AdminUIUsername&j_password=$AdminUIPassword' $LOCALHOST/adminui/j_security_check");
	# post Information
	system("/usr/sfw/bin/wget --quiet --no-check-certificate -O /eniq/home/dcuser/automation/html/liclog.html --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt \"$LOCALHOST/adminui/servlet/LicenseLogsViewer\"");
	my @status=executeThisWithLogging("egrep -c '(is running OK)' /eniq/home/dcuser/automation/html/liclog.html") ;
	
	if($status[0] > 20 ){
		&FT_LOG("PASS\n");
		#$result.="PASS<br>\n";
	}else{
		&FT_LOG("FAIL\n");
		#$result.="FAIL<br>\n";
	}
	
	#LOGOUT 
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");
}

############################################################
# ENGINE 
# this subroutine is in charge of Start any set using cli engine tests, the 
# parameter are:
# tp = techpack for example any techpack or DWH_MONITOR, DWH_BASE
# process= for example UpdateMonitoringTypes
# It executes the process and checks the output,
# if the process throws an error or exception or fail then the test is failed
# else passed.
sub engineProcess{
	my $tp= shift;
	my $process= shift;
	my $result = "";
	&FT_LOG("engine -e  startSet $tp $process Start\n");
	my @process=executeThisWithLogging("engine -e  startSet $tp $process Start");
	&FT_LOG("@process\n");
	my $out=0;
	
	foreach my $process (@process){
		$_=$process;
		if(/Exception|Error|Fail/i){
			#    print $process;
			&FT_LOG("FAIL\n");
			$result.="$process";
			$result.=" FAIL\n";
			$out++;
		}
	}
	if($out==0){
		$result.="$process";
		&FT_LOG("PASS\n");
	}
	return $result;
}

#------------------------------------------------------------	
#	SUBROUTINES - MISC
#
#	1.	getHostName()
#	2.	verifyVersion()
#	3.	getHtmlHeader()
#	4.	getHtmlTail()
#	5.	writeHtml()
#	6.	info()
#	8.	getTime()
#	9.	MAIN()
#
#------------------------------------------------------------

#makes all folders and subfolders in a path. can take more than one path.
sub makePath{
	my @paths = @_;
	chomp(@paths);
	my $path;
	foreach my $pathToMake (@paths){
		my @subFolders = split(/\//, $pathToMake);
		chomp(@subFolders);
		$path = "";
		foreach my $folder (@subFolders){
			$path.=$folder."/";
			if( !-d $path ){
				mkdir($path);# or print "Already Exists - $path\n";
			}
		}
	}
}

sub getVersion{
	my $srce = "/eniq/admin/version/eniq_status";
	my $string="ENIQ_STATUS";
	open(FH, $srce);
	my @buffer = <FH>;
	close(FH);
	my @lines = grep (/$string/, @buffer);
	my $line=$lines[0];
	my $doingVersion=0;
	my $version="";
	for(my $i=0;$i<length($line);$i++){
		my $char=substr($line,$i,1);
		if ( $char =~ /^[0-9]+$/ && $doingVersion==0) {
			$doingVersion=1;
		}
		if ( $char eq " " && $doingVersion==1){
			$doingVersion=0;
			last;
		}
		if($doingVersion==1){
			$version.=$char;
		}
	}
	return $version;
}	
	
# 1. GET HOST NAME
# This is a utility to get the host name
#
sub getHostName{
	open(HOST,"hostname |");
	my @host=<HOST>;
	close(HOST);
	chomp(@host);
	
	#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
	if ( $host[0] eq "eniqe" ){
		open(HOST,"/etc/HOSTNAME");
		my @host=<HOST>;
		chomp(@host);
		close(HOST);
		return $host[0];
	}
	else{
		return $host[0];
	}
}

# 2. VERIFY THE INSTALLED VERSION
# This is a utility to get the version from the eniq_status file
#
sub get_eniq_version{
	my $version="";
	open(VER,"cat /eniq/admin/version/eniq_status |");
	my @eniq_status_file=<VER>;
	close(VER);
	
	$version = "@eniq_status_file";
	chomp($version);
	$version =~ s/\r//;
	$version =~ s/INST_DATE.*$//;
	$version =~ s/ENIQ_STATUS ENIQ_Events_Shipment_//;
	$version =~ s/ AOM.*//;
	$version =~ s/\s+$//;
	$version =~ s/^\s+//;
	chomp($version);
	
	return $version;
}

sub parseVersion{
	my $version=shift;
	my @version_digits = split(/\./,$version);
	
	$version = sprintf("%02d%02d%02d",@version_digits);
	return $version;
}

# 3. GET THE HTML HEADER
# This is a utility for the log output file in HTML 
#

sub getSummary{
	#summary table
	my $report = "<TABLE class='summary'><tr ALIGN = CENTER><th>Test Group</th><th>Pass</th><th>Fail</th><th>Pass Rate</th></tr>";
	my $reportIn = shift;
	my $time = shift;
	my $totalTests = 0;
	
	#regex to make array of titles. Found by matching <h3>s
	my @newh3s = ($reportIn =~ m/<h3>.*?<\/h3>/sg);
	#regex to make array of result tables. these will be sent to getpassfail()
	my @newTables = ($reportIn =~ m/BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>.*?<\/TABLE/sg);
	my $index = 0;
	
	foreach(@newh3s){
		my $noTests = $newh3s[$index];
		$noTests =~ s/.*TotTests=//sg;
		#@h3s[$index] = @newh3s[$index];
		$newh3s[$index].= " TotTests=$noTests";
		$index = $index +1;
	}
	
	#get each table take the name for the summary and pass the table to getPassFail()
	my $title = "";
	my $titleFull = "";
	my $tableId = 0;
	my $count = 0;
	#for each table
	foreach(@newTables){
		$titleFull = $newh3s[$count];
		$title = $titleFull;
		#regex to remove <h3> from around titles
		$title =~ s/<h3>//;
		$title =~ s/<\/h3>//;
		$title =~ s/TotTests.*//sg;
		$count = $count + 1;
		
		if($time eq "end"){
			$pageMiddle =~ s/<h3>.*?<\/h3>//sg;
		}
		$tableId = $tableId +1;
		my $passFail = getPassFail($_);
		#row for summary table
		$report.="<tr ALIGN = CENTER><td ALIGN = LEFT><a href='#$tableId'>$title</a></td>$passFail</tr>";
	}
	my $successRate = 0;
	if($totalPass > 0){
		$successRate = $totalPass/($totalPass+$totalFail);
		$successRate = sprintf('%.2f',$successRate*100);
		$successRate = sprintf '%.0f%%', $successRate;
	}
	if(($totalFail > $failure_threshold_orange) && ($trafficLightColour ne "<font color='red'>red</font>")){
		$trafficLightColour = "orange";
	}
	if($totalFail > $failure_threshold_red){
		$trafficLightColour = "red";
	}
	
	$report.="\n<!-- SUMMARYofSUMMARY --><tr ALIGN = CENTER><td ALIGN = LEFT>Total</td><td>$totalPass</td><td><font color='red'>$totalFail</font></td><td>$successRate</td></tr>\n";
	$totalPass = 0;
	$totalFail = 0;
	$report.="</TABLE>\n<div id=\"bar1\"></div>";
	
	#give each table an id for internal links
	my @tableId = ($reportIn =~ m/<TABLE.*?BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>/sg);
	$tableId = 0;
	foreach(@tableId){
		$tableId = $tableId + 1;
		if($time eq "end"){
			$pageMiddle =~ s/$_/<TABLE id='$tableId' BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>/s;
		}
	}
	
	return $report;
}

sub getPassFail{
	my $reportIn = shift;
	my $report = "";
	my $pass = 0;
	my $fail = 0;
	my $passStr = "PASS";
	$pass = () = $reportIn =~ /$passStr/g;
	
	my $failStr = "FAIL";
	$fail = () = $reportIn =~ /$failStr/g;
	my $notRealFail = "FAILURE";
	my $notFail = 0;
	$notFail = () = $reportIn =~ /$notRealFail/g;
	$fail = $fail - $notFail;
	
		
	my $successRate = 0;
	$totalFail = $totalFail + $fail;
	$totalPass = $totalPass + $pass;
	if($pass > 0){
		$successRate = $pass/($pass+$fail);
		$successRate = sprintf('%.2f',$successRate*100);
		$successRate = sprintf '%.0f%%', $successRate;
	}elsif($pass == 0){
		$successRate = "0%";
	}elsif($fail == 0){
		$successRate = "0%";
	}

	if($fail > 0){
		$report = "<td>$pass</td><td><font color='red'>$fail</font></td><td>$successRate</td>";
	}else{
		$report = "<td>$pass</td><td>$fail</td><td>$successRate</td>";
	}

	return $report;
}

sub getHtmlHeader{
	return qq{<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<HTML>
	<HEAD>
	<title>
	ENIQ Regression Feature Test
	</title>
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			google.load("visualization", "1", {
				packages: ["corechart"]
			});
			google.setOnLoadCallback(drawChart);

			function drawChart() {
				var passResult = \$(".summary tbody tr:last td:nth-child(2)").text();
				var failResult = \$(".summary tbody tr:last td:nth-child(3)").text();
				var grade = \$(".environment tbody tr:first td:last").text();
				
				//Colours of pass fail for each grade
				var grades = {
					na: ['#808080', '#C0C0C0'],
					green: ['#008000', '#00FF00'],
					red: ['#C00000', '#FF0000'],
					orange: ['#FFA500', '#FFCC66'],
				};
				
				\$("#newPass").val(passResult);
				\$("#newFail").val(failResult);
				var total = parseInt(failResult) + parseInt(passResult);
				var data = google.visualization.arrayToDataTable([
					['string', 'number'],
					['Pass', Math.round(passResult / total * 100)],
					['Fail', Math.round(failResult / total * 100)]
				]);
				var options = {
					title: 'Overall Results Pie Chart',
					//legend.position: 'none',
					legend: 'none',
					//colors: ['grey', 'silver'],
					colors: ['green', 'red'],
					backgroundColor: 'white',
					width: 300,
					height: 200,
					titleTextStyle: {color: 'darkblue'},
					//is3D: true
					pieSliceBorderColor: '#CCCCCC',
					pieSliceText: 'label',
					tooltipTrigger: 'none',
				};
				//Set the colours from the grade
				options.colors = grades[grade];
				var chart = new google.visualization.PieChart(document.getElementById('bar1'));
				chart.draw(data, options);

			}
			
			function csv(){
				var hasInnerText = (document.getElementsByTagName("body")[0].innerText != undefined) ? true : false;
				var tables = document.getElementsByTagName("TABLE");
				var comma = "%2C";
				var lf = "%0A";//line feel
				var space = "%20";
				var csv = "";
				var lastHeaddings = tables[tables.length-1].rows[0].cells;
				//SET UP HEADDINGS. GOT FROM LAST TABLE HEADDINGS
				for(var iHeaddings = 0; iHeaddings < lastHeaddings.length; iHeaddings++){
					if(hasInnerText){//chrome + Operah + ie
						csv += lastHeaddings[iHeaddings].innerText;
					}else{//firefox
						csv += lastHeaddings[iHeaddings].textContent;
					}
					if(iHeaddings === lastHeaddings.length-1){
						csv += lf;
					}else{
						csv += comma;
					}
				}
				for(var iTable = 0; iTable < tables.length; iTable++){
					var rows = tables[iTable].getElementsByTagName("tr");
					if(rows[0].cells[0].innerHTML === lastHeaddings[0].innerHTML){//want this
						for(var iRows = 1; iRows < rows.length; iRows++){
							for(var iCell = 0; iCell < rows[iRows].cells.length; iCell++){
								if(hasInnerText){//chrome + Operah + ie
									csv += rows[iRows].cells[iCell].innerText;
								}else{//firefox
									csv += rows[iRows].cells[iCell].textContent;
								}
								if(iCell === rows[iRows].cells.length-1){
									csv += lf;
								}else{
									csv += comma;
								}
							}
						}
					}
				}
				alert("When the file has downloaded change the extension to .csv and you can then open it with Excell or another CSV viewer/editor.");
				csv = csv.replace(new RegExp(" ", 'g'), space);
				var a = document.getElementById("csv");
				a.href = "data:csv/download;charset=utf-8,"+csv;
			}
		</script>
	<STYLE TYPE="text/css">

	body {
		color:darkblue;
		font-family: verdana;
		font-size: 12px;
	}
	a:link {
		color:darkblue
	}
	.environment {
		border: 0px solid black;
		font: medium verdana;
		color:black;
	}
	.environment td {
		border: 0px solid black;
		font: medium verdana;
		color:black;
	}
	.environment th {
		border: 0px solid black;
		font: medium verdana;
		color:black;
	}
	.summary td {
		font-weight:bold;
		color: darkblue;
	}
	table {
		width:100%;
		font: verdana;
		font-size: 12px;
		color:darkblue;
		border-width: 1px;
		border-spacing: 2px;
		border-style: outset;
		#border-color: gray;
		border-collapse: separate;
		background-color: white;
		border-collapse: separate;
		border-spacing: 2px;
		*border-collapse: expression('separate', cellSpacing='2px');
	}
	th {
		color: white;
		font-weight:bold;
		border-width: 1px;
		padding: 1px;
		border-style: inset;
		border-color: #0B3861;
		background-color: #0B3861;
		-moz-border-radius: 0px 0px 0px 0px;
	}
	td {
		color:black;
		border-width: 1px;
		padding: 1px;
		border-style: inset;
		#border-color: gray;
		background-color: white;
		-moz-border-radius: 0px 0px 0px 0px;
	}
	.banner {
		border-collapse: collapse;
		background-color: #143A67;
		border: 0;
	}
	.environment {
		width:50%;
	}
	.environment tr td:first-child {
		background: #CCC;
	}
	th font {
		color: white;
	}
	h6 {
		margin-bottom: 0;
		font-size: 1em;
	}
	#bar1 {
		position: absolute;
		top: 100px;
		right: 0;
		width: 300px;
		height: 200px;
		z-index: -100;
	}

	</STYLE>
	</head>
	<table class="banner" border="0" width="100%">
			<tr>
				<td class="banner" bgcolor="#143A67" width="50%" align="left">
					<img id="_x0000_i1025" src="http://atdl785esxvm8.athtem.eei.ericsson.se/html/banner_eniq_events.jpg"
					alt="Ericsson Logo" height="100">
				</td>
				<td class="banner" bgcolor="#143A67" width="50%" align="right">
					<img id="_x0000_i1025" src="http://eniqdmt.lmera.ericsson.se/img/deft2.jpg"
					alt="Ericsson Logo" height="100">
				</td>
			</tr>
		</table>
		<br />
	};
}

# 4. GET HTML TAIL
# This a utility for the log output file in HTML 
#
sub getHtmlTail{
	return qq{
	</TABLE>
	<br>
	<p align="center">
			<img id="_x0000_i1025" src="http://eniqdmt.lmera.ericsson.se/img/banner.jpg"
			alt="Ericsson Logo" width="1200" height="20">
		</p>
	</BODY></HTML>
	};
}

# 5. WRITE HTML
# This is a utility for the log output file in HTML 
#
sub writeHtml{		
	my $server = shift;
	my $out    = shift;		
	open(OUT," > $LOGS_DIR/$server\_$initDate.html");
	print  OUT $out;
	close(OUT);		
	$html_path = "$server\_$initDate.html";
	return "$LOGS_DIR/$server\_$initDate.html\n";
}

sub getHTMLTime{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) =localtime(time);
	$mon++;
	$year=1900+$year;
	my $date =sprintf("%4d%02d%02d%02d%02d",$year,$mon,$mday,$hour,$min);
	return $date;
}

sub getPackageName{
	my $directory = "/eniq/home/dcuser";
	opendir(DIR,$directory) or die "Can not open that directory";
	my @files = readdir(DIR);
	my $file;
	closedir(DIR);
	foreach(sort @files){
		chomp($_);
		if(/ENIQ_EVENTS_AUTO_TESTS_R\d+[A-Z]\d+\.zip/){
			s/ENIQ_EVENTS_AUTO_TESTS_//;
			s/\.zip//;
			#print $_,"\n";
			$file = $_;
		}
	}
	return $file;
}

sub getRemoteTopology{
	my $dwhdbTopology = `cat /net/atclvm559.athtem.eei.ericsson.se/package/dwhdbInUse.txt`;
	$dwhdbTopology =~ s/dwhdb//;
	#String contains "IPAddress HostName so we cut out the IP"
	my @values = split(' ', $dwhdbTopology);
	return $values[1];
}

sub countTestCaes{
	my $pageMiddle = shift;
	my $pageContent = $pageMiddle;
	if ($pageContent =~ m/RUN_SELENIUM - \(UI Screenshots stored/){
		$pageContent =~ s/(.*\n)+?\s*RUN_SELENIUM//;
		my $pass = () = $pageContent =~ /<b>PASS<\/b>/gi;
		my $fail = () = $pageContent =~ /<b>FAIL<\/b>/gi;
		my $successRate = $pass/($pass+$fail);
		$successRate=sprintf('%.2f',$successRate*100);
		$successRate=sprintf '%.0f', $successRate;
		$pageMiddle = "\n<!-- TESTCASESUMMARY PASS:$pass FAIL: $fail PASSRATE: $successRate% -->\n".$pageMiddle;
	}
	return $pageMiddle;
	
}
sub getHTMLStart{
	my $last = shift;
	my $HTML = "";
	$HTML.=getHtmlHeader();
	$HTML.= "<TABLE class='environment' cellspacing='0' cellpadding='0' border='0'><tbody>";
	if($last eq "true"){
		if($traffic_light_display eq "true"){
			if($trafficLightColour eq "red"){
				$HTML.= "<tr align='left'><td><H6>GRADE: </h6></td><td></td><td><H6><font color='red'>red</font></h6></td></tr>";
			}
			if($trafficLightColour eq "green"){
				$HTML.= "<tr align='left'><td><H6>GRADE: </h6></td><td></td><td><H6><font color='green'>green</font></h6></td></tr>";
			}
			if($trafficLightColour eq "orange"){
				$HTML.= "<tr align='left'><td><H6>GRADE: </h6></td><td></td><td><H6><font color='orange'>orange</font></h6></td></tr>";
			}
		}else{
			$HTML.= "<tr align='left'><td><H6>GRADE: </h6></td><td></td><td><H6>na</h6></td></tr>";
		}
	}else{
	    $HTML.= "<tr align='left'><td><H6>GRADE: </h6></td><td></td><td><H6><font color='black'>Testing Ongoing</font></h6></td></tr>";
	}
	$HTML.= "<tr align='left'><td><H6>HOST:</h6></td><td></td><td><h6>";
	$HTML.= getHostName()."</h6></td></tr>";
	$HTML.= "<tr align='left'><td><H6>FEATURE:</h6></td><td></td><td><h6>${feature}</h6></td></tr>";
	$HTML.= "<tr align='left'><td><H6>REMOTE TOPOLOGY HOST:</h6></td><td></td><td><H6>";
	$HTML.= getRemoteTopology()."</h6></td></tr>";
	$HTML.= "<tr align='left'><td><H6>SHIPMENT:</h6></td><td></td><td><h6>";
	$HTML.= getVersion()."</h6></td></tr>";
	$HTML.= "<tr align='left'><td><H6>START TIME:</h6></td><td></td><td><H6>";
	$HTML.= $startTime."</h6></td></tr>";	
	$HTML.= "<tr align='left'><td><H6>END TIME:</h6></td><td></td><td><H6>";
	if($last eq "true"){
		$HTML.= getTime()."</h6></td></tr>";
	}else{
	    $HTML.="Testing Ongoing</h6></td></tr>";
	}
	$HTML.= "<tr align='left'><td><H6>PACKAGE NAME:</h6></td><td></td><td><H6>";
	$HTML.= getPackageName()."</h6></td></tr>";
	
	if($last eq "true"){
		$HTML.= "<tr align='left'><td><H6>Status:</h6></td><td></td><td><H6>Testing Complete</h6></td></tr>\n";
	}else{
		$HTML.= "<tr align='left'><td><H6>Status:</h6></td><td></td><td><H6>Testing Underway</h6></td></tr>\n";
	}
	
	$HTML.= "<tr align='left'><td><H6>Audit Log File:</h6></td><td></td><td><H6>";
	my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
	if($last eq "true"){
		$HTML.= "<a href='$url/logs$audit_path'>Click here >></a></h6></td></tr>";
	}else{
	    $HTML.= "Audit Log File Is Not Ready</h6></td></tr>";
	}
	
	if($ltees_results ne ""){
        my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
		$HTML.= "<tr align='left'><td><H6>LTEES Log File:</h6></td><td></td><td><H6>";
		$HTML.= "<a href='$url/logs/$ltees_results'>Click here >></a></h6></td></tr>";
	}
	if($feature =~ /GRIT/){
		my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
		##$HTML.= "<tr align='left'><td><H6>GRIT CSV:</h6></td><td></td><td><H6>";
		###$HTML.= "<a href='$url/logs/$gritcsvlogs'>Click here >></a></h6></td></tr>";
		$HTML .= qq{
                        <![if !IE]>
                        <tr align='left'><td><H6>GRIT CSV*:</h6></td><td></td><td><H6><a href="$url/logs/$gritcsvlogs" download="${feature}.csv" >Click here >></a></h6></td></tr>
	<tr align='left'><td>Note:Detailed CSV cab be downloaded from above link</td></tr>
        <![endif]>};
	}
	if($kpi_results ne ""){
	    my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
		$HTML.= "<tr align='left'><td><H6>KPI Log File:</h6></td><td></td><td><H6>";
		$HTML.= "<a href='$url/logs/$kpi_results'>Click here >></a></h6></td></tr>";
	}
	
	if($ltees_counterlog ne ""){
	    my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
		$HTML.= "<tr align='left'><td><H6>LTEES Counter Log File:</h6></td><td></td><td><H6>";
		$HTML.= "<a href='$url/logs/$ltees_counterlog'>Click here >></a></h6></td></tr>";
	}
	
	if($verifylogs_results ne ""){   
	    my $url = "http://atdl785esxvm8.athtem.eei.ericsson.se".$resultsPath;
		$HTML.= "<tr align='left'><td><H6>Log Verification Log File:</h6></td><td></td><td><H6>";
		$HTML.= "<a href='$url/logs/$verifylogs_results'>Click here >></a></h6></td></tr>";
	}
	if($feature !~ /GRIT/){
		$HTML .= qq{
		<![if !IE]>
		<tr align='left'><td><H6>CSV File: </h6></td><td></td><td><H6><a onclick="csv()" id="csv" download="${feature}.csv" href="#" >Click here >></a></h6></td></tr>
		<![endif]>};
	}
	$HTML.= "</tbody></TABLE><br />";
	
	return $HTML;
}

# 6. HELP INFO
# This is a utility, used in case the script is used without params
#
sub info{
	return qq{
Wrong number of parameters.
EniqEventsRegress.sh <conf file>
};
}

# GET DATE WITH TIMEWARP
# 
sub getDateTimewarp{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time+$timeWarp*3600);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}

# GET DATA GEN WITH TIMEWARP (5 mins)
# 
sub getDateDGTimewarp{
	#  my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time-$DGtimeWarp*60);
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time-$DGtimeWarp*60);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}

sub getDateWithArg{
	my $warp=shift;
	#  my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time-$DGtimeWarp*60);
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time-$warp*60);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}


sub verifyexception{

my $current = `date "+%Y-%m-%d_%H:%M"`;
my $todaydate = strftime "%Y_%m_%d", localtime;
$todaydate =~ s/_/-/g;

my ($year, $month, $day) = split/-/,$todaydate;
my $time = timegm(0, 0, 12, $day, $month-1, $year);
my $yesterdate= strftime "%y-%m-%d", gmtime($time - 24*60*60);
$yesterdate =~ s/-/_/g;
$todaydate =~ s/-/_/g;
$yesterdate = "20".$yesterdate;


my $enginelog = "/eniq/log/sw_log/engine/engine-$todaydate.log";
my $yesterday_enginelog="/eniq/log/sw_log/engine/engine-$yesterdate.log";
my $rc = `mkdir -p $BASE_DIR/exceptions`;
my @exceptionlist = ('SEVERE etlengine.engine.ExceptionHandler : Execution failed exceptionally',
                        'com.sybase.jdbc4.jdbc.SybSQLException: SQL Anywhere Error',
                        'java.io.IOException',
                        'java.sql.SQLException:',);
my @exclusionlist = ('AlarmMarkupActionWrapper');
my @finalarray = ();
my @exceptionlog = ();
my @temparray = ();
my $flag=0;
my $cnt=1;
#my $tmpfile = "/eniq/home/dcuser/automation/tmp.txt";
#open(TMPFILE, $tmpfile);
my $exceptionlog = "/eniq/home/dcuser/automation/exceptions/exceptionlog_$current";

&FT_LOG("Yesterdays file is $yesterday_enginelog && todays file is $enginelog\n");


open(YFILE, $yesterday_enginelog);
my @logfile = <YFILE>;
close(YFILE);

open(FILE, $enginelog);
push @logfile, <FILE>;

#open(TMPFILE, ">$tmpfile");
#foreach my $line (@logfile){
#print TMPFILE "$line";
#} 



foreach my $exception (@exceptionlist){
	$cnt=1;
	for my $line (@logfile){
		$cnt++;	
		if (@logfile[$cnt] =~ /$exception/){
				if (@logfile[$cnt+2] =~ /@exclusionlist/){
					next;	
				}	
				else{
					$flag=0;	
				
					foreach my $line (@finalarray){
						if(@logfile[$cnt+2] eq $line){
							$flag=1;	
						}
					}
					if ($flag==0){

						@temparray = ('***********************',@logfile[$cnt],@logfile[$cnt+1],@logfile[$cnt+2],@logfile[$cnt+3],@logfile[$cnt+4]);
						push @finalarray, @temparray;
					} 
       					
				}
		}
	}
}
if (@finalarray){
	open(EXCEPTIONLOG, ">$exceptionlog");
	foreach my $line (@finalarray){
		print EXCEPTIONLOG "$line";
	}
} 	

return $exceptionlog;
}



sub verifyDataGenLoadingAtomdb{
	my $rawTable = shift;
	my $columnToCheck = shift;
	my $columnvalue = shift;
	my $result="";	
	if($columnToCheck ne ""){
		my $sql=qq{
SELECT $columnToCheck FROM $rawTable
go
EOF
	};
	open(DATA,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
	my @data=<DATA>;
	chomp(@data);
	my $showNullError=1;
	foreach my $dat(@data){
		chomp($dat);
		$dat=trim($dat);
		if(lc($dat) =~ m/\(0 rows affected\)/){
			$result .="Error: Raw Table[$rawTable] not populating";
			my $errOrInfo="ERROR";
			&FT_LOG("$errOrInfo:Raw Table[$rawTable] not populating");
			$showNullError=0;
			last;
		}
		elsif
		(
			lc($dat) ne "null" &&
			$dat ne "" && 
			$dat !~ m/rows affected\)/ && 
			(
				$columnvalue eq "" || 
				(
					$columnvalue ne "" && 
					lc($dat) eq lc($columnvalue)
				)
			)
		){
				$result .="Raw Table[$rawTable] is populating ok including column $columnToCheck";
				&FT_LOG("INFO:Found data in $rawTable:$dat");
				$showNullError=0;
				last;
		}
			
		}
		if($showNullError){
			$result .="Error: Raw Table[$rawTable] is populating but with invalid $columnToCheck";
			my $errOrInfo="ERROR";
			&FT_LOG("$errOrInfo:Raw Table[$rawTable] is populating but with invalid $columnToCheck");
						
		}
	}
	
	return $result;
}

sub verifyDataGenLoading{
	my $rawTable = shift;
	my $timeWarp = shift;
	my $columnToCheck = shift;
	my $columnvalue = shift;
	my $printErrors = shift;
	my $cnt=0;
	my $result="";
	my $dataGenVerifyTimePRLocal = getTimeSpecLocal(time);
	my $dataGenVerifyTimePRGmt = getTimeSpecGmt(time);
	my $date = getDateDGTimewarp();
	if ($timeWarp =~ /^[0-9]+$/) {
		$date = getDateWithArg($timeWarp);
	}elsif($timeWarp =~ m/:/){
		$date=$timeWarp;
	}
	if($columnToCheck ne ""){
		my $sql=qq{
SELECT $columnToCheck FROM $rawTable WHERE datetime_id > '$date'
go
EOF
	};
		open(DATA,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
		my @data=<DATA>;
		chomp(@data);
		my $showNullError=1;
		foreach my $dat(@data){
			chomp($dat);
			$dat=trim($dat);
			if(lc($dat) =~ m/\(0 rows affected\)/){
				$result .="Error: Raw Table[$rawTable] not populating at [$dataGenVerifyTimePRLocal], DB time: [$dataGenVerifyTimePRGmt]. Checked times > $date";
				my $errOrInfo="INFO";
				if($printErrors){
					my $errOrInfo="ERROR";
					&FT_LOG("$errOrInfo:Raw Table[$rawTable] not populating at [$dataGenVerifyTimePRLocal], DB time: [$dataGenVerifyTimePRGmt]. Checked times > $date\nSQL statement: $sql\n");
				}
				$showNullError=0;
				last;
			}elsif
			(
				lc($dat) ne "null" && 
				$dat ne "" && 
				$dat !~ m/rows affected\)/ && 
				(
					$columnvalue eq "" || 
					(
						$columnvalue ne "" && 
						lc($dat) eq lc($columnvalue)
					)
				)
			){
				$result .="Raw Table[$rawTable] is populating ok at [$dataGenVerifyTimePRLocal] including column $columnToCheck";
				&FT_LOG("INFO:Found data in $rawTable:$dat");
				$showNullError=0;
				last;
			}
			
		}
		if($showNullError){
			$result .="Error: Raw Table[$rawTable] is populating but with invalid $columnToCheck at [$dataGenVerifyTimePRLocal], DB time: [$dataGenVerifyTimePRGmt]. Checked times > $date";
			my $errOrInfo="INFO";
			if($printErrors){
				my $errOrInfo="ERROR";
				&FT_LOG("$errOrInfo:Raw Table[$rawTable] is populating but with invalid $columnToCheck at [$dataGenVerifyTimePRLocal], DB time: [$dataGenVerifyTimePRGmt]. Checked times > $date\nSQL statement: $sql\n");
			}			
		}
	}else{
		my $sql=qq{
SELECT COUNT(*) FROM $rawTable WHERE datetime_id > '$date'
go
EOF
	};
	
		open(DATA,"$ISQL -Udc -$dBPassword -h0 -Sdwhdb -w 50 -b << EOF $sql |");
		my @data=<DATA>;
		chomp(@data);
		$cnt = trim($data[0]);
		if($cnt==0){
			$result .="Error: Raw Table[$rawTable] not populating at [$dataGenVerifyTimePRLocal], DB time: [$dataGenVerifyTimePRGmt]. Checked times > $date";
			if($printErrors){
				&FT_LOG("ERROR:Raw Table[$rawTable] not populating at [$dataGenVerifyTimePRLocal], DB time: [$dataGenVerifyTimePRGmt]. Checked times > $date\nSQL statement: $sql\n");
			}
		}else{
			$result .="Raw Table[$rawTable] is populating ok at [$dataGenVerifyTimePRLocal]";
		}
	}
	
	return $result;
}

#
# Subroutine to check if Data Generation is writing files
#
sub verifyDataGenDir{
	my $ec_servers=$_[0];
	my $dataDir = $_[1];
	my $dataGenStartTime = $_[2];
	my $printErrors = $_[3];
	my $result = "";
	my $dataGenVerifyTimePR = getTimeSpecLocal(time);
	my $proc=$$;
	if(-l $dataDir){
		$dataDir = abs_path($dataDir);
	}
	my $file="$dataDir";
	my $cmd=qq{#!/usr/bin/perl
	my \$var=(stat("$file"))[9];
	print \$var;
	};
	
	if(open (CMD, "> /tmp/cmdfile.$proc")){
			print CMD "$cmd";
	}else{
			print("ERROR:Could not run verifyDataGenDir for $dataDir");
	}
	close(CMD);
	
	foreach my $ec(@$ec_servers){
		my @status=executeThisQuiet("scp /tmp/cmdfile.$proc dcuser\@$ec:/tmp");
		@status=executeThisQuiet("ssh dcuser\@$ec 'perl /tmp/cmdfile.$proc'");
		my $dataGenLatestTime = $status[0];
		@status=executeThisQuiet("ssh dcuser\@$ec 'rm /tmp/cmdfile.$proc'");
		my $dataGenLatestTimePR = getTimeSpecLocal($dataGenLatestTime);
		if($printErrors){
			&FT_LOG("INFO:Timestamp of DataGen Files in $dataDir:$dataGenLatestTimePR\n");
		}
		if($dataGenLatestTime >= $dataGenStartTime){
			$result.="$dataDir is populating ok at [$dataGenVerifyTimePR]";
			return $result;
		}
	}
	$result .="Error: $dataDir not populating at [$dataGenVerifyTimePR]";
	return $result;
}

#
# getDomainName
# Checks the /etc/resolv.conf to get the domain name.
# Previously this was hardcoded to be 'athtem.eei.ericsson.se'
# with the addition of vApps sometimes it's .vts.com
# added by epagarv
#
sub getDomainName{
	my $file = "/etc/resolv.conf";
	my $domainName = "athtem.eei.ericsson.se";
	unless(open FILE, $file){
		print "\nUnable to open $file";
	}

	# Read the file one line at a time.
	if(-e $file){
		while(my $line = <FILE>) {
			if($line =~ /domain/){
				$line =~ s/domain//;
				$line =~ s/^\s+|\s+$//g ;
				$domainName = $line;
				last;
			}
		}
	}else{
		$domainName = "athtem.eei.ericsson.se";
		print "\nUsing default domain '$domainName'";
	}

	close FILE;
	return $domainName;
}

sub verifyDataGenDir_onCEP{
	my $dataDir = $_[0];
	my $dataGenStartTime = $_[1];
	my $printErrors = $_[2];
	my $result = "";
	my $dataGenVerifyTimePR = getTimeSpecLocal(time);
	my $proc=$$;
	if(-l $dataDir){
		$dataDir = abs_path($dataDir);
	}
	my $file="$dataDir";
	my $cmd=qq{#!/usr/bin/perl
	my \$var=(stat("$file"))[9];
	print \$var;
	};
	
	if (open (CMD, "> /tmp/cmdfile.$proc")) {
		print CMD "$cmd";
	}else{
		print("ERROR:Could not run verifyDataGenDir for $dataDir");
	}
	close(CMD);
	my @cepHosts=("cep_med_1");
	foreach my $cep(@cepHosts){
		my @status=executeThisQuiet("scp /tmp/cmdfile.$proc dcuser\@$cep:/tmp");
		@status=executeThisQuiet("ssh dcuser\@$cep 'perl /tmp/cmdfile.$proc'");
		my $dataGenLatestTime = $status[0];
		@status=executeThisQuiet("ssh dcuser\@$cep 'rm /tmp/cmdfile.$proc'");
		my $dataGenLatestTimePR = getTimeSpecLocal($dataGenLatestTime);
		if($printErrors){
			&FT_LOG("INFO:Timestamp of DataGen Files in $dataDir:$dataGenLatestTimePR\n");
		}
		if($dataGenLatestTime >= $dataGenStartTime){
			$result.="$dataDir is populating ok at [$dataGenVerifyTimePR]";
			return $result;
		}
	}
	$result .="Error: $dataDir not populating at [$dataGenVerifyTimePR]";
	return $result;
}

# 8. GET TIMESTAMP
#
sub getDay{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=gmtime(time);
	return sprintf "%02d", $mday;
}

sub getTime{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=localtime(time);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d\n", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}

sub getTimeSpecGmt{
	my $mytime = shift;
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=gmtime($mytime);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}

sub getTimeSpecLocal{
	my $mytime = shift;
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime($mytime);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}

sub getRunDate{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
	return sprintf "%4d%02d%02d%02d%02d", $year+1900,$mon+1,$mday,$hour,$min;
}

# GET YEAR WITH TIMEWARP
# 
sub getYearTimewarp{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday, $yday,$isdst)=localtime(time+$timeWarp*3600);
	return sprintf "%4d", $year+1900;
}

# TRANSFER RESULTS
#
sub transferResults{
	my $status;
	my $auditName = substr($audit_path, 1);
	#If selenium run by suits, we divide them and uplode them separately
	if($separateResultsFlag){
		my $featureName = shift;
		my $html_path_new = $html_path;
		$html_path_new =~ s/_\w+_PLUS_\w+_/_${featureName}_/;
		copy ("$LOGS_DIR/${html_path}","$LOGS_DIR/${html_path_new}") or die "Can not copy file $!";
		unlink ("/eniq/home/dcuser/automation/RegressionLogs/$html_path");
		$html_path = $html_path_new;
	}
	# FTP html/log files onto Remote Windows Server
	&FT_LOG("Transfer to Server:$resultsServer\n");
	&FT_LOG("Transfer to Path:$resultsPathNew\n");
	&FT_LOG("Html Results File:$html_path\n");
	&FT_LOG("Audit Log File:$auditName\n");
	&FT_LOG("\n\nFull PATH : http://$resultsServer$resultsPathNew/$html_path\n\n");
	my $seleniumLogUploadText="";
	foreach my $file(@seleniumResultsFiles){
		$file =~ s/\.tmp//g;
		$seleniumLogUploadText.=qq{
			lcd /eniq/home/dcuser/automation/RegressionLogs
			mkdir $resultsPath/logs
			cd $resultsPath/logs
			put $file
		};
	}
	if($verifylogs_results ne ""){
		&FT_LOG("Log Verification Log File:$verifylogs_results\n");
	}
	if($ltees_results ne ""){
		&FT_LOG("LTEES Log File:$ltees_results\n");
	}
	if($ltees_counterlog ne ""){
		&FT_LOG("LTEES Counter Log File:$ltees_counterlog\n");
	}
	#LAST LINE TO FINISH THE HTML
	my $wrapperUploadText=qq{
	lcd /eniq/home/dcuser/automation
	cd $resultsPathNew
	put wrapper_log.txt
};
	if(!$doWrapperUpload){
		$wrapperUploadText="";
	}

	my $ftp=qq{
	ftp  -n << EOF
	open $resultsServer
	user $resultsUser $resultsPass
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	mkdir $resultsPathNew
	cd $resultsPathNew
	put $html_path
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	cd $resultsPath/logs
	put $ltees_results
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	cd $resultsPath/logs
	put $kpi_results
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	cd $resultsPath/logs
	put $ltees_counterlog
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	cd $resultsPath/logs
	put $verifylogs_results
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	cd $resultsPath/logs
	put $event_err_raw_csv
	
	lcd /eniq/home/dcuser/automation/RegressionLogs
	cd $resultsPath/logs
	put $event_suc_raw_csv

	lcd /eniq/home/dcuser/automation/audit
	cd $resultsPath/logs
	put $auditName
	
	lcd /eniq/home/dcuser/automation/
	cd $resultsPath/logs
	put $gritcsvlogs
	
	$wrapperUploadText
	
	$seleniumLogUploadText
	bye
EOF
	};

	# EXECUTE FTP
	open(FTP,"$ftp |")|| die "cannot contact $resultsServer\n";
	my @ftpOut=<FTP>;
	close(FTP);
	
	updateFTPDirsTimestamp();
}

# Updates timestamp on folders so when viewing results, newest files will be in newest folders
#
sub updateFTPDirsTimestamp{
	my $tempFile = "timeUpdater.txt";
	open( FILE, ">/tmp/$tempFile");
	print FILE "This file is used to update timestamp on FTP server folders.\nDelete me, Damien.";
	close(FILE);
	my $ftp=Net::FTP -> new ($resultsServer) or return 3;
	$ftp ->login($resultsUser,$resultsPass);
	my @dirs = split(/\//,$resultsPathNew);
	foreach my $dir (@dirs){
		$ftp ->cwd($dir);
		if( $dir !~ m/^$/ and $dir !~ m/^home$/ ){
			$ftp ->put("/tmp/".$tempFile);# or print "INFO: Put($tempFile) failed: " . $ftp->code() . ": " . $ftp->message()." in '$dir'\n";
			$ftp ->delete($tempFile);# or print "INFO: Delete($tempFile) failed: " . $ftp->code() . ": " . $ftp->message()." in '$dir'\n";
		}
	}
	$ftp ->quit;
	unlink($tempFile);
	return 0;
}

# Error Logging
#
sub FT_ERROR {
	my ($error) = @_;
	chomp $error;
	if(open (AUDIT, ">> $error_log")){
		my ($SS, $MM, $HH, $dd, $mm, $yy) = (localtime)[0..5];
		my ($time) = sprintf("%d-%02d-%02d %02d:%02d:%02d",$yy+1900, $mm+1, $dd, $HH, $MM, $SS);
		print AUDIT "$time : $error\n";
		close AUDIT;
	}else{
		print STDERR "ERROR: Could not open $error_log for writing\n";
	}
	die ("ERROR: $error\n");
}

# SubRoutine: auto_log
#
# Job Logging
sub FT_LOG {
	my ($message) = @_;
	chomp $message;
	if (open (AUDIT, ">> $audit_log")) {
		my ($SS, $MM, $HH, $dd, $mm, $yy) = (localtime)[0..5];
		my ($time) = sprintf("%d-%02d-%02d %02d:%02d:%02d",$yy+1900, $mm+1, $dd, $HH, $MM, $SS);
		print AUDIT "$time $message\n";
		close AUDIT;
	}else{
		print STDOUT "$message\n";
		&FT_ERROR ("Could not open $audit_log for writing");
	}
	print STDOUT "$message\n";
}

##SUMMARY PAGE STUFF
sub newFolder{
	if( -e $tempSummary ){
		print "$tempSummary exists..\n";
	}else{
		makeHtml();
	}
	my $url = "$resultsPathNew";
		$url =~ s%.*/results/%results/%;
	open HTML, ">> $tempSummary";
	print HTML qq{
		<h2><a href="$url" >$url</a></h2>
		<table width="100%" border="1px" border-collapse="collapse" >
			<tr>
				<th>Grade</th>
				<th>Name</th>
				<th>Server</th>
				<th>Time</th>
				<th>Pass</th>
				<th>Fail</th>
				<th>Total</th>
			</tr>
		
		</table>
	};
	close( HTML );
}

sub updateFiles{
	open HTML, ">> $tempSummary";
	open PREVSUMMARY, "< $tempSummary";
	my @prevSummary = <PREVSUMMARY>;
	close( PREVSUMMARY );
	my $url = "$resultsPathNew";
	$url =~ s%.*/results/%results/%;
	chomp( $url );
	if ( grep ( /$url/, @prevSummary ) ){
		my @matches = grep( m/<h2><a href="results\/.*" >(.*)<\/a><\/h2>/, @prevSummary );
		my $first = shift(@matches);
		$first =~ m/<h2><a href="results\/.*" >(.*)<\/a><\/h2>/;
		$first = $1;
		if( $first eq $url ){#see if server folder is first
			#was the first server, concinate to end. be grand
			#print "Same folder, just concinating\n";
		}else{
			#Not most recent folder. Maaking folder
			close( HTML );
			rearrangeFile($url);
			open HTML, ">> $tempSummary";
			
		}
	}else{#does not contain the server folder already
		close( HTML );
		newFolder();#write folder heading to summary
		rearrangeFile($url);
		open HTML, ">> $tempSummary";
	}	
	close( HTML );
	#removeTotal();
	open HTML, ">> $tempSummary";#################################
	opendir DIR, $LOGS_DIR;
	my @dirs = readdir(DIR);
	closedir(DIR);
	open PREVSUMMARY, "< $tempSummary";
	@prevSummary = <PREVSUMMARY>;
	close( PREVSUMMARY );
	my $totalPre = 0;#used to get average
	my $sum = 0;#used to get average
	
	#Get a list of files to include in summary.
	my $ftp=Net::FTP->new($resultsServer,Timeout=>240);
	$ftp->login($resultsUser, $resultsPass);
	$ftp->cwd($resultsPathNew);
	my @files = $ftp->ls();#list of files on server, only use what is necessary
	$ftp->quit;
	
	foreach my $file (@dirs){
		if($file !~ m/\d\.html/){
			next;#exclude summary.html, ., and ..
		}
		if(grep( /$file/, @files ) == 0){
			#print "$file not on server\n";
			next;#not on server, not related. Dont include
		}else{
			#print "$file on server\n";
		}
		if( grep( /$file/, @prevSummary ) > 0){
			#print "CONTAINS! = $file\n";
			next;#contains file in summary, skip
		}else{
			#print "NOT CONTAINS!$file\n";
		}
		$sum++;#used for average
		
		open RESULT, "< $LOGS_DIR/$file";
		my ($pass, $fail, $total, $grade);
		my @pageContent = <RESULT>;
		foreach (@pageContent){
			if ( $_ =~ /<!-- SUMMARYofSUMMARY -->/ ){
				my $string = $_;
				$string =~ m/<!-- SUMMARYofSUMMARY --><tr ALIGN = CENTER><td ALIGN = LEFT>Total<\/td><td>(\d+)<\/td><td><font color='\w*'>(\d+)<\/font><\/td><td>(\d+)%?<\/td><\/tr>/;
				$pass = $1;
				$fail = $2;
				$total = $3;
			}elsif ( $_ =~ /<H6>GRADE:/ ){
				#<H6>GRADE: </h6></td><td></td><td><H6><font color='green'>green</font></h6>
				#<H6>na</h6>
				$grade = $_;
				
				$grade =~ s/.*GRADE: <\/h6><\/td><td><\/td><td><H6\b[^>]*>//;
				$grade =~ s/<\/h6>.*//;
				$grade =~ s/<font\b[^>]*>//;
				$grade =~ s/<\/font>//;
				chomp $grade;
			}
		}
		#Overwrite pass and fail numbers in results summary page [EQEV-7196]
		my $numberOfSeleniumRan = grep (/RUN_SELENIUM - \(UI Screenshots stored/, @pageContent);

		#extract only unit test cases numbers
		my $pageContent = join '', @pageContent;
		$pageContent =~ s/(.*\n)+?\s*RUN_SELENIUM//;
		$pass = () = $pageContent =~ /<b>PASS<\/b>/gi;
		$fail = () = $pageContent =~ /<b>FAIL<\/b>/gi;
		$total = $pass/($pass+$fail);
		$total=sprintf('%.2f',$total*100);
		$total=sprintf '%.0f', $total;
		
		close( RESULT );
		$totalPre += $total;#contains total

		#atrcxb1928_DATAGEN_DMD_201206251545.html
		my ($server, $name, $timestamp);
		$file =~ m/(^[a-zA-Z0-9]*)/;
		$server = $1;
		$file =~ m/(\d*)\.html/;
		$timestamp = $1;
		$file =~ m/^[a-zA-Z0-9]*_(.*)_\d*.\html/;
		$name = $1;
		$name =~ s/_/ /g;
		
		my @timeInfo = sortOutTime($timestamp);
		#/home/deftftauto/html/dev/results/atrcxb1926/atrcxb1926_DAMO_201206251054.html
		my $url = "$resultsPathNew/$file";
		$url =~ s%/home/.*/html/.*/results/%results/%;
		$url =~ s%/home/deftftauto/html/.*/results/%results/%;
		my $newRow = qq{
			<tr>
				<td class="$grade" >$grade</td>
				<td><a href="$url">$name</a></td>
				<td>$server</td>
				<td>$timeInfo[-2]:$timeInfo[-1] on $timeInfo[-3]/$timeInfo[-4]/$timeInfo[-5]</td>
				<td>$pass</td>
				<td>$fail</td>
				<td>$total%</td>
			</tr>
		};
		insertOnTop($newRow);
	}
	
	#my $avg = sprintf('%.2f', getAvg($url));
	#print "AVG = $avg";
	#print HTML qq{
	#		<tr>	
	#			<td class="right" colspan="6" >Overall Pass: </td>
	#			<td>$avg%</td>
	#		</tr>
	#	};
	close (HTML);
}

sub insertOnTop{
	my $newRow = shift;
	my $firstTime = 1;
	open UPDATESUM, "< $tempSummary";
	my @summary = <UPDATESUM>;
	close UPDATESUM;
	open UPDATESUM, "> $tempSummary";
	foreach my $line (@summary){
		print UPDATESUM $line;
		if( $firstTime == 1 and $line =~ m/<\/tr>/ ){
			$firstTime = 0;
			print UPDATESUM "";
			print UPDATESUM $newRow;
		}
	}
	close UPDATESUM;
}

sub rearrangeFile{
	my $folder = shift;
	my @tempHTML = undef;
	open SUMMARY, "< $tempSummary";
	my @tempSummaryArray = <SUMMARY>;
	close (SUMMARY);
	open UPDATESUM, "> $LOGS_DIR/summaryTEMP.html";
	my $cut = 0;
	foreach my $HTMLLine (@tempSummaryArray){
		if($HTMLLine =~ /<h2><a href="$folder" >$folder<\/a><\/h2>/){
			$cut =1;
		}
		if($HTMLLine =~ /<\/table>/ and $cut == 1){
			push @tempHTML,$HTMLLine;
			$cut =0;
			next;
		}
		if($cut == 1){
			push @tempHTML, $HTMLLine;
		}else{
			print UPDATESUM $HTMLLine;
		}
	}
	close (UPDATESUM);
	open UPDATESUM, "< $LOGS_DIR/summaryTEMP.html";
	my @summaryLessFolder = <UPDATESUM>;
	close (UPDATESUM);
	open UPDATESUM, "> $LOGS_DIR/summaryTEMP.html";
	my $firstTime = 1;
	foreach my $line (@summaryLessFolder){
		print UPDATESUM $line;
		if( $firstTime == 1 and $line =~ m/<\/table>/ ){
			$firstTime = 0;
			foreach my $folder (@tempHTML){
				print UPDATESUM $folder;
			}
		}
	}
	close (UPDATESUM);
	move("$LOGS_DIR/summaryTEMP.html","$tempSummary");
}

sub makeHtml{
	my $sumHead = $resultsPath;
	$sumHead =~ s%.*html/%%;
	$sumHead =~ s%/results.*%%;
	$sumHead = uc($sumHead);
	
	open( FILE, "> $tempSummary" );
	print FILE qq{
<html>
	<head>
		<title>Summary</title>
		<script type="text/javascript" >
			function iframe(){
				if (top === self) {
					//NOT AN IFRAME
				}else{//IFRAME
					var h1s = document.getElementsByTagName('h1');
					var headding = h1s[0]
					var headdingWords=headding.innerHTML.split(" ");
					headding.innerHTML=headdingWords[0];
					//headding.innerHTML="HI PAT!";
				}
			}
		</script>
		<style type="text/css" >
			.red
			{
				background-color: #CC0000;
				color: #CC0000;
				text-align:center;
			}
			.green
			{
				background-color: #66CC00;
				color: #66CC00;
				text-align:center;
			}
			.orange
			{
				background-color: orange;
				color: orange;
				text-align:center;
			}
			.na
			{
				#background-color: red;
				color: gray;
				text-align:center;
			}
			h1
			{
				text-align:center;
				text-shadow: 5px -5px 1px black;
				text-transform: uppercase;
				#background-color:#0B3861;
				color: white;
				font-size: 100px;
				margin-top: -10px;
				padding: 5px;
				height: 95px;
				padding: 10px;
				background-image: url('http://eniqdmt.lmera.ericsson.se/img/deft2.jpg');
				background-repeat: none;
			}
			h2
			{
				border-top: 2px solid black;
				
			}
			table
			{
				width: 90%;
				margin-left: auto;
				margin-right: auto;
			}
			.right 
			{
				text-align:right;
			}
			body
			{
				padding: 0px;
				padding-top: 0px;
			}
			th
			{
				color: white;
				font-weight:bold;
				border-width: 1px;
				padding: 1px;
				border-style: inset;
				border-color: #0B3861;
				background-color: #0B3861;
				width: 14%;
			}
			tr:first-child th:first-child
			{
				width: 2%;
			}
		</style>
	</head>
	<body>
		<h1>$sumHead SUMMARY</h1>
		<table>
			
		</table>
};
	close( FILE );
}

sub ftpToServer{
	my $summaryPath = $resultsPath;
	$summaryPath =~ s/\/results//;
	my $ftp=Net::FTP->new($resultsServer,Timeout=>240);
	$ftp->login($resultsUser, $resultsPass);
	$ftp->cwd($summaryPath);
	$ftp->put($tempSummary) or print "ERROR: NOT FTPed TO SERVER($resultsServer)!\n";
	$ftp->quit;
	unlink($tempSummary);
}

sub getResults{
	my $summaryPath = $resultsPath;
	$summaryPath =~ s/\/results//;
	my $ftp=Net::FTP->new($resultsServer,Timeout=>240);
	$ftp->login($resultsUser, $resultsPass);
	$ftp->cwd($summaryPath);
	$ftp->get("summary.html","$LOGS_DIR/summary.html") or makeHtml();
	$ftp->quit;
}

sub limitTo{
	my $folderLimit = shift;
	my $fileLimit = $folderLimit;
	
	open( FILE, "< $tempSummary" );
	my @file = <FILE>;
	close( FILE );
	
	open( NEW, "> $tempSummary" );
	my $trCount = 0;
	my $tableCount = 0;
	
	foreach my $line (@file){
		if( $line =~ m%<tr>% ){
			$trCount++;
		}
		if( $line =~ m%</table>% ){
			$trCount = 0;#new table
			$tableCount++;
		}
		if($tableCount > $folderLimit){
			print NEW $line;
			last;
		}
		if( $trCount <= $fileLimit + 1 ){
			print NEW $line;
		}
	}	
	close( NEW );
}

sub isLteesOnlyServer{
	if ( -e "/eniq/mediation_inter/M_E_LTEES" && !-e "/eniq/mediation_inter/M_E_SGEH" && !-e "/eniq/mediation_inter/M_E_MSS" && !-e "/eniq/mediation_inter/M_E_LTEEFA"){
		return 1;
	}else{
		return 0;
	}
}

sub isVirtualApp{
	my $var = `cat /etc/hosts | grep glassfish`;
	if ($var =~ /eniqe/) {
		return 1;
	} else {
		return 0;
	}
}

sub isSonVisOnlyServer{
	my @command = `cat /eniq/admin/version/eniq_status`;

	if ( grep(/ENIQ_Events_Shipment_\d\.\d\.\d+_SV/,@command)){
		return 1;
	}else{
		return 0;
	}
}

sub sortOutTime{
	my $timestamp = shift;
	my @timeInfo = undef;
	push( @timeInfo, substr( $timestamp, 0, 4) );#year
	push( @timeInfo, substr( $timestamp, 4, 2) );#month
	push( @timeInfo, substr( $timestamp, 6, 2) );#day
	push( @timeInfo, substr( $timestamp, 8, 2) );#hour
	push( @timeInfo, substr( $timestamp, 10, 2) );#minute	
	return @timeInfo;
}

sub fileNameDate{
	my $DateMonth = `date "+%Y%m"`;
	my $Month = `date "+%m"`;
	my $Onlydate = `date "+%d"`;
	chomp($Month);
	if (($Month = 2) || ($Month = 4) || ($Month = 6) || ($Month = 9) || ($Month = 11) || ($Month = 01) ) {
		if ($Onlydate == 1){
			$dateIs = 31;
		}
		else{
			$dateIs = $Onlydate - 1;
		}
	}
	else{
		if ($Onlydate == 1){
			$dateIs = 30;
		}
		else{
			$dateIs = $Onlydate - 1;
		}
	}
	chomp($DateMonth);
	chomp($dateIs);
	$WCDMAFiledate = "$DateMonth"."$dateIs";
	return $WCDMAFiledate;
}
sub fileNameDateTimeFuntion{
my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=gmtime();
$mon= sprintf("%02d",$mon+1);
$mday  = sprintf("%02d",$mday);
$hour  = sprintf("%02d",$hour);
$min  = sprintf("%02d",$min);
$year = $year+1900;

my $currentDate = $year."-".$mon."-"."$mday";
chomp($currentDate);

my $start_min;
my $start_hour=sprintf("00");
my $end_min=sprintf("00");
	if($min > 0 && $min <=15)
	{
	$end_min=sprintf("00");
     $start_min= sprintf("45");
	 $start_hour=$hour-1;
	 $start_hour  = sprintf("%02d",$start_hour);
	}

	if($min > 15 && $min <=30)
	{
	$end_min=sprintf("15");
	$start_min= sprintf("00");
	$start_hour=$hour;
	}
	if($min > 30 && $min <=45)
	{
	$end_min=sprintf("30");
	$start_min= sprintf("15");
	$start_hour=$hour;
    }

	if($min > 45 && $min <60)
	{
	$end_min=sprintf("45");
	$start_min= sprintf("30");
	$start_hour=$hour;
    }

	if($min ==0)
	{
	$end_min=sprintf("45");
	$start_min= sprintf("30");
	$hour=$hour-1;
	$start_hour=$hour;
	}
	
	my $featureFileName = "A"."$year$mon$mday"."."."$start_hour$start_min"."-"."$hour$end_min";
	chomp($featureFileName);
	return $featureFileName;
}
#############################################################################
# 1O. MAIN
# This is a simple main that starts the generation of the HTML log file and 
# calls the parseParam subroutine that controls the execution of the script
# Once the script has executed the summary table is created which is put into the HTML
# before the rest of the page.
# Then when all tests are finished writes the log HTML file in the same directory 
# where this script is executed. Finally html/log files are transferred and
# hosted on a remote server.
##############################################################################
{
	if(hostname eq "atrcus190"){
		print "ERROR:Run this script as dcuser on your ENIQ server, not on atrcus190\n";
		exit 1;
	}else{
		my @coordServer=grepFile("engine", "/etc/hosts");
		my $host=hostname;
		if (!grep(/$host/,@coordServer) && !isLteesOnlyServer()) {
			if(! -e "/eniq/home/dcuser/automation/EST/EST.txt")
			{
				print "ERROR:Run this script as dcuser on your ENIQ server's coordinator\n";
				exit 1;
			}
		}
	}
	
	mkdir("/eniq/home/dcuser/automation/html");
	unlink("/eniq/home/dcuser/automation/html/cookies.txt");
	system("/usr/sfw/bin/wget --quiet  --no-check-certificate -O /dev/null  --keep-session-cookies --save-cookies /eniq/home/dcuser/automation/html/cookies.txt --post-data \"action=/servlet/CommandLine&command=Installed+tech+packs&submit=Start\" \"$LOCALHOST/adminui/servlet/CommandLine\"");
	if(grepFile("8443","/eniq/home/dcuser/automation/html/cookies.txt")){
		$LOCALHOST="https://localhost:8443";
	}
	system("/usr/sfw/bin/wget --no-check-certificate -O /dev/null --quiet --cache=on --save-headers --keep-session-cookies --load-cookies /eniq/home/dcuser/automation/html/cookies.txt $LOCALHOST/adminui/servlet/Logout  ");

	if(-e "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData_original.csv" && -e "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv" && -e "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData2g3g.csv"){
		system("rm /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData_original.csv");
	}

	if(-e "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData_original.csv" && -e "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv" && !-e "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData2g3g.csv"){
		system("mv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData2g3g.csv");
        system("mv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData_original.csv /eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv");
	}

	push(@ecHosts,"ec_1");
	push(@ecHosts,"ec_sgeh_1");
	push(@ecHosts,"ec_lteefa_1");
	push(@ecHosts,"ec_lteefa_2");
	push(@ecHosts,"ec_lteefa_3");
	push(@ecHosts,"ec_ltees_1");
	push(@ecHosts,"ec_ltees_2");
	push(@ecHosts,"ec_ltees_3");
	push(@ecHosts,"ec_ltees_4");
	push(@ecHosts,"ec_ltees_8");
	if(isSevenBlade()){
		push(@ecHosts,"ec_2");
	}
	my $username = $ENV{LOGNAME} || $ENV{USER} || getpwuid($<);
	if($username ne "dcuser"){
		print "ERROR:This script must be run as dcuser\n";
		exit(1);
	}
	if($ARGV[0]=~m/DATAGEN/){
		open(INPUT,"< $ARGV[0]");
		my @input=<INPUT>;
		chomp(@input);
		close(INPUT);
		foreach my $input(@input){
			print "$input\n";
		}
		exit(0);
	}
	
	if((@ARGV)==0){
		print info();
		exit(0);
	}
	if((@ARGV)>1){
		$packageRunStartTime = "_$ARGV[1]";
		$doWrapperUpload=1;	
	}
	mkdir("$LOGS_DIR");
	mkdir("$AUDIT_DIR");
	mkdir("$HTML_DIR");
	mkdir("$TOPOLOGY_DIR");
	mkdir("$DGTOPOLOGY_DIR");
	mkdir("$DGWORKFLOWS_DIR");

	#Set names for summary table to populate it before test run. Not vital! Without this table will still populate from empty. HARDCODED NUMBER OF TESTS = BAD PRACTICE

	#start building the report
	opendir (DIR, "/eniq/home/dcuser/automation");
	my @files = grep /.*testresults.log.tmp/, readdir(DIR);
	closedir DIR;
	foreach my $file(@files){
		unlink("/eniq/home/dcuser/automation/$file");
	}
	
	#####Remove legacy crontab
	my @crontab=executeThisQuiet("crontab -l");
	if( grep(/cleardirsafterabort\.pl \>\/dev/,@crontab)!=0 ){
		open(CRON,">/tmp/cron.$$");
		foreach(@crontab){
			if($_ !~ m/cleardirsafterabort/){
				print CRON $_;
			}
		}
		close(CRON);
		executeThisWithLogging("crontab /tmp/cron.$$");
		unlink ("/tmp/cron.$$");
	}
	if( !isMultiBladeServer() and !-e "/eniq/home/dcuser/automation/.workaroundsDone" and !isLteesOnlyServer() and !isSonVisOnlyServer()){
		print "INFO: CHECKING WORKAROUNDS\nThis might take 10-15 minutes\n";
		chmod 0755,"/eniq/home/dcuser/automation/workAround.pl";
		executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh /eniq/home/dcuser/automation/workAround.pl");
		print "INFO: DONE CHECKING WORKAROUNDS\n";
		open(WORK, ">/eniq/home/dcuser/automation/.workaroundsDone");
		print WORK "Workarounds done on ".getTime();
		close(WORK);
	}
	
	
	###Set up correct RAM values for executioncontext.xml
	open(INPUT,"< $ARGV[0]");
	my @input=<INPUT>;
	chomp(@input);
	close(INPUT);
	
	if(grep(/SKIP_EC_XML_SETUP/,@input)){
		$skipEcXmlSetup=1;
	}else{
		$skipEcXmlSetup=0;
	}
	
	my $changedFile=0;
	if(!isLteesOnlyServer() and !isSonVisOnlyServer()){
		if(!isMultiBladeServer()){
			#$changedFile=compare("/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml.small.default","/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml");
			#if($changedFile==1  && !$skipEcXmlSetup){
			#	copy("/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml.small.default","/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml");
			#print "INFO:Setting up executioncontext.xml and EC processes.\n";
			#}else{
			#	print "INFO:Skipping setup of executioncontext.xml\n";
			#}
			my @changedFile=executeThisQuiet("perl /eniq/mediation_inter/bin/ec_mem.pl");
			if (grep(/INFO:executioncontext.xml not changed/, @changedFile)){
				print "INFO:Skipping setup of executioncontext.xml\n";
			}else{
				print "INFO:Setting up executioncontext.xml and EC processes by invoking \/eniq\/mediation_inter\/bin\/ec_mem.pl\n";
			}
			print "INFO:The ec service will be disabled because this is a single blade but the EC processes will be started as required\n";
			## Flag is to prevent the ec to stop while LIkeForLike execution
			if(!$like4likeIsRunning){
			executeThisQuiet("ssh dcuser\@ec_1 'source ~/.profile; ec stop'");
			}
			
		}
	}
	
	##make sure /eniq/data/eventdata is not link to /eniq/upgrade/automation
	my @link_state=executeThisQuiet("ssh dcuser\@ec_1 'ls -ltr /eniq/data | grep eventdata | grep ^l'");
	if(grep(/eventdata/,@link_state)){
		print "\n/eniq/data/eventdata is a link\n";
		executeThisQuiet("ssh dcuser\@ec_1 '/usr/bin/rm /eniq/data/eventdata'");
		executeThisQuiet("ssh dcuser\@ec_sgeh_1 '/usr/bin/rm /eniq/data/eventdata'");
		executeThisQuiet("ssh dcuser\@ec_lteefa_1 '/usr/bin/rm /eniq/data/eventdata'");
		executeThisQuiet("ssh dcuser\@ec_lteefa_2 '/usr/bin/rm /eniq/data/eventdata'");
		executeThisQuiet("ssh dcuser\@ec_lteefa_3 '/usr/bin/rm /eniq/data/eventdata'");
	}
	
	##Disable OSS Mounts
	my @no_of_ecltees=executeThisQuiet("cat /eniq/sw/conf/service_names | grep ec_ltees_*");
	for (my $i=1;$i<=@no_of_ecltees;$i++){
		executeThisQuiet("ssh dcuser\@ec_ltees_${i} '/eniq/home/dcuser/automation/RunCommandAsRoot.sh /usr/bin/touch /eniq/connectd/mount_info/events_oss_1/disable_OSS'");
		##Create /eniq/data/eventdata/events_oss_2|3|4
		for (my $j=1;$j<=4;$j++){
			executeThisQuiet("ssh dcuser\@ec_ltees_${i} '/eniq/home/dcuser/automation/RunCommandAsRoot.sh mkdir -p /eniq/data/eventdata/events_oss_${j}'");
			executeThisQuiet("ssh dcuser\@ec_ltees_${i} '/eniq/home/dcuser/automation/RunCommandAsRoot.sh chown dcuser:dc5000 /eniq/data/eventdata/events_oss_${j}'");
		}		
	}
	executeThisQuiet("/eniq/home/dcuser/automation/RunCommandAsRoot.sh /usr/bin/touch /eniq/connectd/mount_info/events_oss_1/disable_OSS");

	$pageMiddle.= parseParam();	
	opendir (DIR, "/eniq/home/dcuser/automation");
	@seleniumResultsFiles = grep /.*testresults.log.tmp$/, readdir(DIR);
	closedir DIR;
	my @oldSeleniumResultsFiles = grep /.*testresults.log.*/, readdir(DIR);
	my $arrSize=@seleniumResultsFiles;
	# remove old selenium log files
	
	for(my $i=0;$i<$arrSize;$i++){
		my $time = time;
		my $filetime = fileTime("/eniq/home/dcuser/automation/$oldSeleniumResultsFiles[$i]");
		if(($time - $filetime) > 64800){
			unlink("/eniq/home/dcuser/automation/$oldSeleniumResultsFiles[$i]");
		}
	}
	
	for(my $i=0;$i<$arrSize;$i++){
		my $newName=$seleniumResultsFiles[$i];
		$newName =~ s/\.tmp//g;
		move("/eniq/home/dcuser/automation/$seleniumResultsFiles[$i]","/eniq/home/dcuser/automation/RegressionLogs/$newName");
	}
	
	#Separate results page if run parallel as selenium suits
	if($separateResultsFlag){
		my $featureResultTableIndex = 0;
		my @separatedFeatureNames = split /_PLUS_/,$feature;
		#Loop to remove other features' table
		for (my $i=1; $i<= $numberOfFeatureInSuit; $i++){
			my $pageMiddlePart = $pageMiddle;
			$pageMiddlePart =~ s/(<br>(.*\n.*)RUN_SELENIUM(.*\n)+?<\/TABLE>)/++$featureResultTableIndex == $i? $1:""/ge;
			push (@pageMiddleArray, $pageMiddlePart);
			$featureResultTableIndex = 0;		
		}
		my $singleFeatureTrafficLightColour = $trafficLightColour;
		foreach my $pageMiddle (@pageMiddleArray){
			unless ($pageMiddle =~ m/<font color=#?ff0000><b>FAIL<\/b><\/font>/){
				$trafficLightColour = "green";
			}else{
				$trafficLightColour = $singleFeatureTrafficLightColour;
			}
			$pageMiddle = countTestCaes($pageMiddle);
			$fullPage = getHTMLStart("true");
			$fullPage.= getSummary($pageMiddle, "end");
			$fullPage.= $pageMiddle;
			$fullPage.= getHtmlTail();
		
			#Separate feature's name from Config file parameter
			$feature = shift (@separatedFeatureNames);
			&FT_LOG("Feature:$feature\n");
			&FT_LOG("Complete Html File:$complete\n");
			writeHtml($complete,$fullPage);
			
			if( $ftpLogin  ){
				transferResults($feature);
			}else{
				&FT_LOG("FTP login info INCORRECT\nResults not sent to $resultsServer");
			}
			sleep 5;
		}
	}else{
		$pageMiddle = countTestCaes($pageMiddle);
		$fullPage = getHTMLStart("true");
		$fullPage.= getSummary($pageMiddle, "end");
		$fullPage.= $pageMiddle;
		$fullPage.= getHtmlTail();
		
		&FT_LOG("Feature:$feature\n");
		&FT_LOG("Complete Html File:$complete\n");
		writeHtml($complete,$fullPage);
		if( $ftpLogin  ){
			transferResults();
		}else{
			&FT_LOG("FTP login info INCORRECT\nResults not sent to $resultsServer");
		}
	}
	
	$pageMiddle = "";
	if($ftpLogin == 1){#NOT FINISHED..... NOT IN SPRINT. ASK DAMIEN OR ZHAO
		getResults();
		updateFiles();
		limitTo(30);
		ftpToServer();
	}
	if(!isMultiBladeServer()){
		print "INFO:The ec service will be started to leave server in normal state (offline ec misleading in CI checks).\n";
		executeThisQuiet("ssh dcuser\@ec_1 'source ~/.profile; ec start'");
	}
}
