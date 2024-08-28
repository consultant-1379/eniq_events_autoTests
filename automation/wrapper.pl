#!/usr/bin/perl -C0

my $dt = `date`;chomp $dt;
system "echo \"START : $dt\" >> /tmp/executionTime_log.txt";
use strict;
use File::Copy;
my $scriptDirectory="/eniq/home/dcuser/automation";
my $scriptFileName="EniqEventsRegress.sh";
my $configFileDirectory=$scriptDirectory;
my $scriptPath="$scriptDirectory/$scriptFileName";
my $logFile="$scriptDirectory/wrapper_log.txt";
my $priority = 1;
my $priorityDirectory = $scriptDirectory."/priority";
my $priorityMatch = "Config.*PASS\.txt";
my @priorityFiles;#global. Leave here!

my $concurrent = 0;
my $concurrentDirectory = $scriptDirectory."/parallel";
my @concurrentFiles;

#Test groups can be defined in the @testGroups variable below. Each array's first element defines the switch that will be used to execute that group and the rest of the elements define test config files. #NB put Config.*_PASS.txt in $priorityDirectory folder.
#E.g. the line ["all","aac","baseline","sanity","datagen","lte-es"] means that calling './nmi_auto_test_wrapper.pl -all' will execute tests with config files named Config_AAC.txt, Config_BASELINE.txt, Config_CORE.txt etc

my %testGroups = (
	"all" => ["adminui","allseleniumtests","lte-es"],
	"nmi" => ["adminui","init","wait 900","2g3gsgeh","kpinotification","mss","aac","ltecfa","ltehfa","lte-es","workspace","dvtp","3gsessbrowser","ltestreaming","wcdmacfahfa","3gkpianalysis","atomdb"], 
	"events" => ["adminui","init","wait 3600","mss","aac","workspace","kpinotification","kpiphaseone","ltecfa","ltehfa","2g3gsgeh","lte-es","3gsessbrowser"],
	"cr" => ["adminui","init","wait 3600","mss","aac","workspace","kpinotification","kpiphaseone","ltecfa","ltehfa","2g3gsgeh","lte-es","3gsessbrowser"],
	"data" => ["adminui","init","ltees14adata"],
	"killdata" => ["DataStopAll"],
	"13adata" => ["adminui","13adata","13alteesdata"],
	"13bdata" => ["adminui","init","13alteesdata"],
	"lite" => ["adminui","lite"],
	"test" => ["adminui","3gsessbrowser"],
	"check" => ["adminui","quickcheck"],
	"datacdb" => ["cdbdata"],
	"checkcdb" => ["cdbquickcheck"],
	"nmicdb" => ["cdb4Greg","cdbltecfareg","cdbltehfareg"],
	"datakgbltecfahfa" => ["kgbltecfahfadata"],
	"nmikgbltecfahfa" => ["kgbltecfahfareg"],
	"datakgbltehfa" => ["kgbltehfadata"],
	"nmikgbltecfa" => ["kgbltecfareg"],
	"nmikgbltehfa" => ["kgbltehfareg"],
	"datakgb4g" => ["kgb4Gdata"],
	"nmikgb4g" => ["kgb4Greg"],
	"datakgb2g3gsgeh" => ["kgb2g3gsgehdata"],
    "nmikgb2g3gsgeh" => ["kgb2g3gsgehreg"],
    "datakgbaac" => ["kgbaacdata"],
    "nmikgbaac" => ["kgbaacreg"],
    "datakgbwcdmacfahfa" => ["kgbwcdmacfahfadata"],
    "nmikgbwcdmacfahfa" => ["kgbwcdmacfahfareg"],
    "datakgbwcdmahfa" => ["kgbwcdmahfadata"],
    "nmikgbwcdmahfa" => ["kgbwcdmahfareg"],
	"datakgbdvtp" => ["kgbdvtpdata"],
	"nmikgbdvtp" => ["kgbdvtpreg"],
	"datakgbkpinotification" => ["kgbkpinotificationdata"],
	"nmikgbkpinotification" => ["kgbkpinotificationreg"],
	"datakgb3gkpianalysis" => ["kgb3gkpianalysisdata"],
	"nmikgb3gkpianalysis" => ["kgb3gkpianalysisreg"],
	"datakgbltees" => ["kgblteesdata"],
	"nmikgbltees" => ["kgblteesreg"],
	"datakgbmss" => ["kgbmssdata"],
	"nmikgbmss" => ["kgbmssreg"],
	"nmikgbltestreaming" => ["kgbltestreaming"],	
	"installede" => ["installede"],
	"grit" => ["gritltecfa","gritltehfaerr","grit4hsgeherr","grit4gsgehextended","gritcfaerab"],
	"seq1" => ["cdbtopology"],
	"seq2" => ["cdblte-es","cdb4gdata","cdb4greg"],
	"seq3" => ["cdbkpinotificationdata","cdbkpinotificationreg","cdbmssdata","cdbmssreg"],
	"seq4" => ["cdbltecfahfadata","cdbltehfareg","cdbltecfareg","cdbltestreaming","cdb2g3gsgehdata","cdb2g3gsgehreg","cdbatomdb","cdb3gsessbrowserdata","cdb3gsessbrowserreg","cdb3gkpianalysisdata","cdb3gkpianalysisreg"],
	"seq5" => ["cdbwcdmacfahfadata","cdbdvtpreg"],
	"seq6" => ["cdbwcdmacfa-subter-reg","cdbwcdmacfa-ranking-reg","cdbwcdmacfa-network-reg"],
	"seq7" => ["cdbwcdmahfa-network-reg","cdbwcdmahfa-subter-reg","cdbwcdmahfa-ranking-reg"],
	"seq8" => ["cdbaacreg"],
	
	"concurrentcdb" => ["seq1","seq2","seq3","seq4","seq5","seq6","seq7"]

);

my %featureTimeouts = (
	"aac" => 10800,
	"like4like_4gsgeh" => 10800,
	"quickcheck" => 10800,
	"baseline" => 10800,
	"13adata" => 18000,
	"13alteesdata" => 18000,
	"ltees14adata" => 18000,
	"sanity" => 10800,
	"datagen" => 10800,
	"ltees" => 16200,
	"lte-es" => 16200,
	"sgehdvdt" => 10800,
	"kpinotification" => 10800,
	"mss" => 21600,
	"aac" => 10800,
	"uiimprovements" => 14400,
	"kpiphaseone" => 10800,
	"ltecfa" => 10800,
	"wcdma" => 10800,
	"2g3gsgeh" => 10800,
	"dvtp" => 10800,
	"workspace" => 21600,
	"3gsessbrowser" => 21600,
	"3gkpianalysis" => 21600,
	"sonvis" => 10800,
	"lite" => 10800,
	"wcdmacfahfa" => 60000, # temporarily increase timeout from 21600 to 60000 for WCDMA
	"kpinodeservices" => 18000,
	"ltestreaming" => 18000,
	"firefox_10" => 14400,
	"firefox_16" => 14400,
	"firefox_24" => 14400,
	"kpinotification_plus_2g3gsgeh_plus_aac_plus_uiimprovements" => 18000,
    "kgb4Gdata" => 10800,
	"kgbltecfahfadata" => 10800,
	"kgbltehfadata" => 10800,
	"kgbwcdmacfahfadata" => 10800,
    "kgbwcdmahfadata" => 10800,
    "kgbaacdata" => 10800,
    "kgb2g3gsgehdata" => 10800,
	"kgbdvtpdata" => 10800,
	"kgbkpinotificationdata" => 10800,
	"kgb3gkpianalysisdata" => 10800,
	"kgblteesdata" => 10800,
	"kgbmssdata" => 10800,
	"kgb4Greg" => 14400,
	"kgbltecfahfareg" => 16200,
	"kgbltecfareg" => 14400,
	"kgbltehfareg" => 14400,
	"kgbwcdmacfahfareg" => 14400,
    "kgbwcdmahfareg" => 14400,
    "kgbaacreg" => 10800,
    "kgb2g3gsgehreg" => 14400,
	"kgbdvtpreg" => 14400,
	"kgbkpinotificationreg" => 14400,
	"kgb3gkpianalysisreg" => 16200,
	"kgblteesreg" => 14400,
	"kgbmssreg" => 14400,
	"kgbltestreaming" => 10800,
	"cdbtopology" => 1800,
	"cdbaacreg" => 7200,
    "cdb4Greg" => 10800,
	"cdbltecfareg" => 10800,
	"cdbltehfareg" => 10800,
    "cdbquickcheck" => 10800,
    "cdbdata" => 10800,	
	"DataStopAll"  => 18000,
	"installede" => 1800,
	
);

sub get_eniq_version{
	my $ver="";
	open(VER,"cat /eniq/admin/version/eniq_status |");
	my @eniq_status_file=<VER>;
	close(VER);

	$ver = "@eniq_status_file";
	chomp($ver);
	$ver =~ s/\r//;
	$ver =~ s/INST_DATE.*$//;
	$ver =~ s/ENIQ_STATUS ENIQ_Events_Shipment_//;
	$ver =~ s/ AOM.*//;
	$ver =~ s/\s+$//;
	$ver =~ s/^\s+//;
	chomp($ver);

	return $ver;
}

sub parseVersion{
	my $ver=shift;
	my @ver_digits = split(/\./,$ver);

	$ver = sprintf("%02d%02d%02d",@ver_digits);
	return $ver;
}

sub executeThis{
	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>)
	{
		print $_;
	}
	close(CMD);
}

sub autolog{
	open (LOGFILE, ">>$logFile");
	print LOGFILE getDateTime().":".$_[0]."\n";
	close (LOGFILE);
}

sub killProcessAndChildren{
	my $pid=shift;
	my $ppid=shift;
	my @pids;
	while(1){
		push(@pids,$pid);
		my @procs=`/usr/bin/ps -ef`;
		my @var=grep(/ $pid /,@procs);
		if ($ppid =~ /^[0-9]+$/) {
			@var=grep(!/ $ppid /,@var);
		}
		if($var[0] eq ""){
			last;
		}else{
			$ppid=$pid;
			my @sp = split(/ /, $var[0]);
			foreach my $item(@sp){
				if ($item =~ /^[0-9]+$/) {
					$pid=$item;
					last;
				}
			}
		}
	}
	kill 9,@pids;
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

sub updateCoreConfigVersion{
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
	my @files = <$scriptDirectory/Config_CORE.txt>;
	foreach my $file (@files) {
		my $alreadyUpdatedFile=0;
		open(FILE,"<$file");
		open(FILE2,">/tmp/autotempfile.$$");
		while (my $line=<FILE>) {
			if ( $line =~ "/ENIQ_EVENTS\/$version\/eniq_base_sw\//"){
				$alreadyUpdatedFile=1;
			}
			$line=~s/ENIQ_EVENTS\/.*\/eniq_base_sw\//ENIQ_EVENTS\/$version\/eniq_base_sw\//;
			print FILE2 $line;
		}
		close(FILE);
		close(FILE2);
		if($alreadyUpdatedFile==1){
			unlink("/tmp/autotempfile.$$");
		}else{
			move("/tmp/autotempfile.$$","$file");
		}
	}
}

sub updateResultPathInFiles{
	my $arg=shift;
	$arg =~ s/-//;
	$arg =~ s/13a//;
	foreach my $dir ($configFileDirectory, $priorityDirectory){
		opendir(DIR, $dir);
		my @files = readdir(DIR);
		closedir(DIR);
		foreach my $file (grep( /^Config_.*\.txt$/, @files)) {
			my $updatedFile=0;
			open( FILE, "<$dir/$file" );
			my @contents = <FILE>;
			close(FILE);
			foreach my $resulthPath (grep( /^RESULTSPATH/, @contents)) {
				$updatedFile=1;
				$resulthPath =~ s%/html/(\w*)/results%/html/$arg/results%;
			}
			if($updatedFile){
				autolog("Updated $file with $arg log path. Saving in ${file}.nmi");
				open( NMI, ">$dir/$file.nmi" );
				foreach my $line (@contents){
					print NMI $line;
				}
				close(NMI);
			}else{
				autolog("File already contains $arg. No need to change");
			}
		}
	}
}

sub executeAndWaitForTimeout{
	my $command = shift;
	my $timeout = shift;
	
	if($timeout==""){
		$timeout=36000;
	}
	autolog("Executing: $command with timeout:$timeout");
	my $childPid = fork(); #fork a process
	
	if($childPid == 0)
	{
		#Execute tests in the child process
		my $startTime=time();
		executeThis($command);
		my $endTime=time();
		autolog("Finished executing $command");
		if($endTime<$startTime+61){
			#sleep for 61 seconds to ensure that two sets of tests aren't put into the same log file. Log file names contain the current minute
			autolog("Sleeping to prevent overlap of log files");
			sleep(61);
		}
		exit(0);
	}
	else
	{
		autolog("Executing tests in process: $childPid");
		#Wait in the parent process for the child process to finish
		for(my $i=0;$i<=$timeout;$i++){
			my $running = `ps -p $childPid`;
			if( $running !~ /^{$childPid}\s/ || $running =~ /defunct/){
				autolog("Child PID $childPid not found. Ending timeout loop");
				last;
			}
			if($i==$timeout){
				autolog("Timeout of $timeout seconds reached. Killing child PID $childPid and Selenium processes");
				executeThis("cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient quit;/eniq/sw/runtime/java/bin/java stopHub");
				killProcessAndChildren($childPid,$$);
				$command =~ m/Config_(.*)\.txt.*/;
				
				#EQEV-23838 Changes to use gateway hostname instead of hostname(eniqe) on vApp
				my $host_name = `hostname`;
			
				if ( $host_name eq "eniqe" ){
					open(HOST,"/etc/HOSTNAME");
					my @host=<HOST>;
					chomp(@host);
					close(HOST);
					$host_name = $host[0];
					chomp($host_name);
				}
								
				autolog("Logs at /eniq/home/dcuser/automation/$1"."TestGroup-".getTime()."-testresults.log and /eniq/home/dcuser/automation/audit/Audit.".$host_name."_$1_".getTime().".txt\n");
			    my $feature = $1;
				$feature =~ s/_.*//;
				print "Feature Name is ${feature}\n";
				my $htmlFile =  `ls /eniq/home/dcuser/automation/RegressionLogs/*_${feature}*.html `;
				open(PRO,"<${htmlFile}");
		        my @contents=<PRO>;
		        close(PRO);
		        foreach(@contents){
					s%color='black'>Testing Ongoing%color='red'>red%;
					s%<H6>Testing Ongoing%<H6>Testing Timeout%;
					s%<H6>Testing Underway%<H6>Testing Not Complete With Timeout%
					
		        }
				open(PRO, ">${htmlFile}");
				print PRO @contents;
				close (PRO);
				
			}else{
				if( $i % 60 == 0){
					if($i==0){
						autolog("$i seconds have passed. Waiting up to $timeout seconds for command to finish:$command");
					}else{
						autolog("$i seconds have passed");
					}
				}
				sleep(1);
			}
		}
	}
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

sub isMultiBladeServer{
	my $co_server = (grepFile("engine", "/etc/hosts"))[0];
		my $engine = splitAndGetValue($co_server, " ", 3);
	my $ec_server = (grepFile("ec_1", "/etc/hosts"))[0];
		my $ec = splitAndGetValue($ec_server, " ", 3);
	if ($ec eq $engine) {
		#This is a Standalone server
		return 0;
	}
	else {
		#This is a Multi-blade server
		return 1;
	}
}

sub usage
{
	use Cwd 'abs_path';
	my $options=" [";
	
	foreach my $switch (keys(%testGroups)){
		$options.=" -".$switch." |";
	}
	$options.=" -all ]";
	print "Usage:\t".abs_path($0).$options."\n";
	print "\tGive any feature name as an argument in the form -feature to execute tests from a file named Config_FEATURE.txt\n\te.g. ".abs_path($0)." -myfeature\n";
}

sub getTime{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) =localtime(time);
	$mon++;
	$year=1900+$year;
	my $date =sprintf("%4d%02d%02d%02d%02d",$year,$mon,$mday,$hour,$min);
	return $date;
}

sub getDateTime{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) =localtime(time);
	$mon++;
	$year=1900+$year;
	my $date =sprintf("%02d-%02d-%4d %02d:%02d:%02d",$mon,$mday,$year,$hour,$min,$sec);
	return $date;
}

sub setPriorities{
	my $feature = shift;
	my $priorityRequest = shift;
	$feature =~ s/-//;
	if( $testGroups{$feature} ){
		if($priorityRequest =~ m/-?all/i){
			$priority = 0;
		}
		if($priorityRequest =~ m/-?p1/i){
			$priority = 1;
			$priorityMatch = "Config.*PASS\.txt";
		}
		if($priorityRequest =~ m/-?p2/i){
			$priority = 1;
			$priorityMatch = "Config.*FAIL\.txt";
		}
	}
}

#######MAIN
{
	my $initTime = getTime();
	if( -f "$logFile.2"){
		move("$logFile.2","$logFile.3");
	}
	if( -f "$logFile.1"){
		move("$logFile.1","$logFile.2");
	}
	if( -f "$logFile"){
		move("$logFile","$logFile.1");
	}
	updateCoreConfigVersion();
	if($ARGV[0] =~ m/^-?(nmi|cr|data|13adata|13bdata)$/){
		updateResultPathInFiles($ARGV[0]);
	}
	chmod 0755,$scriptPath;
	if(@ARGV==0){
		usage();
		exit(0);
	}elsif(@ARGV > 1){
		print $ARGV[0]."\n";
		setPriorities(@ARGV);
	}
	
	foreach my $arg(@ARGV){
		my $matchedSwitch=0;
		if($arg =~ m/^-?(h|help)$/){
			usage();
		}else{
			$arg =~ s/^-//;
			if($testGroups{$arg}){
				$matchedSwitch=1;
				my @tempTestGroup=@{$testGroups{$arg}};
				autolog("Matched switch $arg");
				if($arg =~ m/killdata/){
				  $priority=0;
				  autolog("Killing data loading");
				}
				if($arg =~ m/^concurrent/){
				  $concurrent = 1;
				  $priority=0;
				  autolog("Running Concurrent Tests");
				}
				if ( isLteesOnlyServer()){
					autolog("this is an LTEES only server. Only running LTEES tests");
					@tempTestGroup=("lte-es");
				}
				if ( isSonVisOnlyServer()){
					autolog("this is an SON Vis only server. Only running SON Vis tests");
					@tempTestGroup=("adminui","sonvis");
					$priority=0;
				}
				if(!isMultiBladeServer() && !$concurrent && !isVirtualApp()){
					autolog("this is a single blade server. It cannot run 3g Session Browser tests");
					if (grep {$_ eq '3gsessbrowser'} @tempTestGroup) {
						my $index = 0;
						$index++ until $tempTestGroup[$index] eq '3gsessbrowser'; 
						splice(@tempTestGroup,$index,1);
						autolog("SessionBrowser no longer present in this run");
					}
				}
				if(!isMultiBladeServer() && !$concurrent && !isVirtualApp()){
					autolog("this is a single blade server. It cannot run 3g KPIAnalysis tests");
					if (grep {$_ eq '3gkpianalysis'} @tempTestGroup) {
						my $index = 0;
						$index++ until $tempTestGroup[$index] eq '3gkpianalysis';
						splice(@tempTestGroup,$index,1);
					}
				}
				if(!isMultiBladeServer() && !$concurrent && !isVirtualApp()){
					autolog("this is a single blade server. It cannot run wcdmacfahfa tests");
					if (grep {$_ eq 'wcdmacfahfa'} @tempTestGroup) {
						my $index = 0;
						$index++ until $tempTestGroup[$index] eq 'wcdmacfahfa';
						splice(@tempTestGroup,$index,1);
						 autolog("wcdma-cfa/hfa no longer present in this run");
					}
				}
				if (isVirtualApp()) {
					autolog("this is a vApp server");
					#$priority=0;
				}
				
				if (parseVersion(get_eniq_version()) < parseVersion('6.0.0')){
					autolog("Like4Like tests is not applicable in this release.");
					if (grep {$_ eq 'like4like_4gsgeh'} @tempTestGroup) {
						my $index = 0;
						$index++ until $tempTestGroup[$index] eq 'like4like_4gsgeh';
						splice(@tempTestGroup,$index,1);
						autolog("Like4Like testing no longer present in this run");
					}
				}
				
				if($priority){
					autolog("Running Priority Tests");
					opendir( DIR, $priorityDirectory );
					@priorityFiles = readdir(DIR);
					closedir(DIR);
					if($priorityMatch ne ""){
						@priorityFiles = grep( /$priorityMatch/, @priorityFiles);
					}
					@priorityFiles=map {"$priorityDirectory/".$_} @priorityFiles;#caoncat priority dir on before to feed into script
					push(@priorityFiles, ( "$configFileDirectory/Config_INIT.txt", "$configFileDirectory/Config_ADMINUI.txt", "$configFileDirectory/Config_AAC.txt" ));#Pushed to end. Can be overridden by dedicated priority config file
				}
				if (!$concurrent)
				{
					if (-e "/tmp/concurrent_halt"){
						system "rm -f /tmp/concurrent_halt";
				}
				for my $command (@tempTestGroup){
					if($command =~ m/^wait/){
						my $wait=$command;
						$wait=~s/wait//g;
						$wait=~s/ //g;
						if($wait =~ /^[0-9]+$/ ){
							autolog("Waiting for $wait seconds");
							sleep($wait);
						}else{
							autolog("Waiting for 3600 seconds");
							sleep(3600);
						}
					}
					if($priority){
						foreach my $priorityFile (grep(/\w?_$command/i, grep( /\.txt$/, @priorityFiles))){#ending in .txt and containing feature name(command). Excludes .txt.nmi. handled below						
							my $timeout=36000;
							if( $featureTimeouts{$command} ){
								$timeout = $featureTimeouts{$command};
							}
							if( -f "${priorityFile}.nmi" ){#nmi updated files here!!
								autolog( "EXECUTING - $scriptDirectory/$scriptFileName ${priorityFile}.nmi $initTime" );
								executeAndWaitForTimeout("$scriptDirectory/$scriptFileName ${priorityFile}.nmi $initTime",$timeout);
								unlink("${priorityFile}.nmi");
							}else{
								autolog( "EXECUTING - $scriptDirectory/$scriptFileName $priorityFile $initTime" );
								executeAndWaitForTimeout("$scriptDirectory/$scriptFileName $priorityFile $initTime",$timeout);
							}
							last;#If 2 config files found, just do first. That way can override INIT, ADMINUI, and AAC above..
						}
					}
					#elsif($concurrent){
						#opendir( DIR, $concurrentDirectory );
						#@concurrentFiles = readdir(DIR);
						#closedir(DIR);
					    #foreach my $concurrentFile (grep(/$command/i, grep( /\.txt$/, @concurrentFiles))){
								#my $timeout=39600;
								#print "EXECUTING - $scriptDirectory/$scriptFileName $concurrentDirectory/$concurrentFile $initTime";
								#autolog( "EXECUTING - $scriptDirectory/$scriptFileName $concurrentDirectory/$concurrentFile $initTime" );
								#executeAndWaitForTimeout("$scriptDirectory/$scriptFileName $concurrentDirectory/$concurrentFile $initTime",$timeout);
							#}						  
					#}
					else{
						my	$configFile = "";
						if($command =~ /stop/i){
							$configFile = "$configFileDirectory/Config_$command.txt";
						}else {
							$configFile = "$configFileDirectory/Config_".uc($command).".txt";
						}
						autolog("Searching for $configFile .\n\n" );
						if( -f "${configFile}.nmi"){
							$configFile = "${configFile}.nmi";
						}
						if( -f $configFile){
							my $timeout=36000;
							if( $featureTimeouts{$command} ){
								$timeout = $featureTimeouts{$command};
							}
							autolog( "EXECUTING - $scriptDirectory/$scriptFileName $configFile $initTime");
							executeAndWaitForTimeout("$scriptDirectory/$scriptFileName $configFile $initTime",$timeout);
							if($configFile =~ m/\.nmi$/){
								unlink($configFile);
							}
						}
						}
					}
				}
				else{
					system "touch /tmp/concurrent_halt";
					for my $command (@tempTestGroup){
						my $pid = fork();
						if ($pid == 0){
							opendir( DIR, $concurrentDirectory );
							@concurrentFiles = readdir(DIR);
							closedir(DIR);
							foreach my $switch (@{$testGroups{$command}}){
								foreach my $concurrentFile (grep(/$switch/i, grep( /\.txt$/, @concurrentFiles))){
									my $timeout=39600;
									my $dt = `date`; chomp $dt;
									print "EXECUTING - $scriptDirectory/$scriptFileName $concurrentDirectory/$concurrentFile $initTime \n";
									autolog( "EXECUTING - $scriptDirectory/$scriptFileName $concurrentDirectory/$concurrentFile $initTime" );
									executeAndWaitForTimeout("$scriptDirectory/$scriptFileName $concurrentDirectory/$concurrentFile $initTime",$timeout);
								}
							}
							exit(0);
						}
						else {
							# print "PARENT ID: $$\n";
						}
					}
					while (wait() != -1){}
					last;
				}
			}

			if(!$matchedSwitch){
				$arg =~ s/-//;
				autolog("Looking for $configFileDirectory/Config_".uc($arg).".txt");
				if( -f "$configFileDirectory/Config_".uc($arg).".txt"){
					autolog("Found $configFileDirectory/Config_".uc($arg).".txt");
					my $timeout=36000;
					if( $featureTimeouts{$arg} ){
						$timeout = $featureTimeouts{$arg};
					}
					executeAndWaitForTimeout("$scriptDirectory/$scriptFileName Config_".uc($arg).".txt $initTime",$timeout);
				}else{
					autolog("Config file does not exist:$configFileDirectory/Config_".uc($arg).".txt");
				}
			}
		}
	}
	autolog("Finished tests. Creating $scriptDirectory/RegressionLogs/auto_done.txt");
	foreach my $dir ($configFileDirectory, $priorityDirectory){
		opendir(DIRR, $dir);
		my @dirr = readdir(DIRR);
		close( DIRR);
		foreach my $nmi (grep( /\.txt\.nmi/ , @dirr)){
			unlink("$dir/$nmi");
		}
	}
	#sleep(900);
	open (MYFILE, ">$scriptDirectory/RegressionLogs/auto_done.txt");
	print MYFILE getDateTime()."\n";
	close (MYFILE);
	#Restart taking too long and failing on certain single blades.
	#
	#if(!isMultiBladeServer()){
	#	autolog("Restart ec service for single blade");
	#	executeThis("ssh dcuser\@ec_1 'source ~/.profile; ec restart'");
	#}
	my $dt = `date`;chomp $dt;
	system "echo \"END : $dt\" >> /tmp/executionTime_log.txt";
}
