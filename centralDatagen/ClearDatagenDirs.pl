#!/usr/bin/perl

use strict;
use File::Basename;
use File::Copy;

my @datagenDirs;
my @servers;
my @profiles;
my @empty;
my @clear;
my @SCPClear;
my @logger;
my $datagenserver;
my $SCPServer;
my $centralDatagenDir;
my $datagenStore;
my $delay;
my $logDir;
my $logFile;
my $noOfLogs;
my $olderThan; 
my $homePath=$ENV{"HOME"};
my $configFile="$homePath/centralDatagen/DATAGEN_SETTINGS.txt";

sub parseParameters{
	if(!-e $configFile){
		print "ERROR: Cannot find 'DATAGEN_SETTINGS.txt' file. Can't go on...\n";
		exit(0);
	}
	open( CONF, "<$configFile" );
	my @file = <CONF>;
	close( CONF );

	my $serverFeed = 0;
	my $dirFeed = 0;
	my $clearFeed = 0;
	my $profileFeed = 0;
	my $emptyFeed = 0;
	my $SCPFeed = 0;
	foreach my $line (grep(!/^#|^$/, @file)){
		if( $line =~ m/^DATAGENSERVER=(.*)/){
			chomp($datagenserver = $1);
			next;
		}elsif( $line =~ m/^SCPSERVER=(.*)/){
			chomp($SCPServer = $1);
			next;
		}elsif( $line =~ m/^CENTRALDATAGENDIR=(.*)/){
			chomp($centralDatagenDir = $1);
			next;
		}elsif( $line =~ m/^DATAGENSTORE=(.*)/){
			chomp($datagenStore = "/net/$datagenserver".$1);
			next;
		}elsif( $line =~ m/^LOGDIR=(.*)/){
			chomp($logDir = $1);
			$logFile = "$logDir/clearlog.txt";
			next;
		}elsif( $line =~ m/^NUMOFLOGS=(.*)/){
			chomp($noOfLogs = $1);
			next;
		}elsif( $line =~ m/^SLEEPDELAY=(.*)/){
			chomp($delay = $1);
			next;
		}elsif( $line =~ m/^OLDESTFILE=(.*)/){
			chomp($olderThan = $1);
			next;
		}elsif( $line =~ m/^SERVERS/){
			$serverFeed = 1;	
			next;
		}elsif( $line =~ m/^DIRECTORIES/){
			$dirFeed = 1;	
			next;
		}elsif( $line =~ m/^PROFILES/){
			$profileFeed = 1;	
			next;
		}elsif( $line =~ m/^SCPFILES/){
			$SCPFeed = 1;	
			next;
		}elsif( $line =~ m/^CLEARONLY/){
			$clearFeed = 1;	
			next;
		}elsif( $line =~ m/^EMPTY/){
			$emptyFeed = 1;	
			next;
		}elsif( $line =~ m/^-$/){
			$serverFeed = 0;
			$dirFeed = 0;
			$clearFeed = 0;	
			$profileFeed = 0;
			$emptyFeed = 0;
			$SCPFeed = 0;
			next;
		}elsif($serverFeed){
			chomp($line);
			push( @servers, $line);
		}elsif($emptyFeed){
			chomp($line);
			push( @empty, $line);
		}elsif($SCPFeed){
			chomp($line);
			my @dirs = split(/,/, $line);
			push( @clear, $dirs[0] );
			push( @SCPClear, $dirs[1] );
		}elsif($dirFeed){
			chomp($line);
			if( $line =~ m/,/ ){#account for big long paths...
				push(@datagenDirs, grep(/\//, split(/,/, $line)));
			}else{
				push( @datagenDirs, $line);
			}
		}elsif($profileFeed){
			chomp($line);
			push( @profiles, [split(/,/, $line)]);
		}elsif($clearFeed){
			chomp($line);
			push( @clear, $line);
		}
		
	}
}
sub executeThisQuiet
{
	my $command = shift;
	open(CMD,"$command |");
	my @cmd=<CMD>;
	close(CMD);
	return @cmd;
}
sub executeAndWaitForTimeout
{
	my $command = shift;
	my $timeout = shift;
	
	if($timeout==""){
		$timeout=36000;
	}
	my $childPid = fork(); #fork a process
	
	if($childPid == 0)
	{
		#Execute tests in the child process
		my @output = executeThisQuiet($command);
		exit(0);
	}
	else
	{

		#Wait in the parent process for the child process to finish
		my $running;
		for(my $i=0;$i<=$timeout;$i++){
			$running = `ps -p $childPid`;
			if( $running !~ /^{$childPid}\s/ || $running =~ /defunct/){
				return 1;
				last;
			}
			if($i==$timeout){
				killProcessAndChildren($childPid,$$);
				return 0;
			}else{
				sleep(1);
			}
		}
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
sub sortFilesNewestFirst{
	my $directory = shift;
	opendir(DIR, $directory);
	my @FilesAlphabetical = readdir(DIR);
	closedir(DIR);
	my @Files;
	foreach my $File (@FilesAlphabetical) { 
		if( $File !~ m/^\.\.?$/ and !-d $File){#skip . and .. and files
			# Push this into a new array with date at front
			push(@Files, fileTime($directory."/".$File)."&&".$File);
		}
	}
	# Sort this array newest first
	@Files = reverse(sort(@Files));
	my @filesNewestFirst;
	foreach my $File (@Files) { 
		# Get the filename back from the string
		$File =~ s/^\d*\&\&//;
		push( @filesNewestFirst, $File);
	}
	return @filesNewestFirst;
}

sub fileTime{
        my $file = shift;
        my $time;
        $time = (stat($file))[9] or $time = 0;
        chomp($time);
        return $time;
}

sub serverFolders{
	opendir(DIR, $centralDatagenDir);
	my @dirs = readdir(DIR);
	close(DIR);
	chomp (@dirs);
	@servers = grep(!/\.\.?/, @dirs);
}

sub runCommandAsRoot{
	my $command = shift;
	my @result = `/eniq/home/dcuser/centralDatagen/RunCommandAsRoot.sh $command`;
	return @result
}

sub clearSyslog{
	
	my $syslogDir = "/var/log";
	toLog("INFO: Clearing syslogs from $syslogDir - ");
	
	if( -e "${syslogDir}/syslog.0"){
		#remove syslog.0, syslog.1 and so on...
		my $deleteCommand = "rm ${syslogDir}/syslog.*";
		runCommandAsRoot($deleteCommand);
	}
	
	#override syslog file. Deleting could cause problems
	my $overrideCommand = "touch ${syslogDir}/file; chown root:sys ${syslogDir}/file; chmod 644 ${syslogDir}/file; mv ${syslogDir}/file ${syslogDir}/syslog;";
	#runCommandAsRoot($overrideCommand);
	runCommandAsRoot("touch ${syslogDir}/file");
	runCommandAsRoot("chown root:sys ${syslogDir}/file");
	runCommandAsRoot("chmod 644 ${syslogDir}/file");
	runCommandAsRoot("mv ${syslogDir}/file ${syslogDir}/syslog");
	
	if( fileTime($syslogDir."/syslog") - time <= 60){
		toLog("\tSuccess");
	}else{
		toLog("\tFAILURE - ${syslogDir}/syslog could not be cleared");
	}
}

sub logger{
	my @toLog = @_;
	if($logFile ne ""){
		for( my $i = ($noOfLogs - 1); $i >0; $i-- ){#archive
			if( -e "${logFile}.$i" ){
				move("${logFile}.$i", "${logFile}.".($i+1));
			}
		}
		move($logFile, "${logFile}.1");
		open( LOGFILE, ">$logFile");
		flock(LOGFILE, 2);#locks and/or waits to write so processes wont interfer
		foreach my $line (@toLog){
			$line =~ s/\\n$//;
			print LOGFILE "$line\n";
		}
		close(LOGFILE);#closing also unlocks file
	}else{
		print "ERROR: Logfile path not in DATAGEN_SETTINGS.txt\n";
	}
}

sub toLog{#not really needed but printing can be done in here
	my $toLog = shift;
	my ($SS, $MM, $HH, $dd, $mm, $yy) = (localtime)[0..5];
	my ($time) = sprintf("%d-%02d-%02d %02d:%02d:%02d",$yy+1900, $mm+1, $dd, $HH, $MM, $SS);
	print "$toLog\n";
	push(@logger, "$time: $toLog");
}

{#MAIN
	print "INFO: Deleting directories...\n";
	parseParameters();
	serverFolders();
	foreach my $dir (@datagenDirs){
		my $time = time;
		my $limit = $olderThan * 60 * 60;
		my $oldestTime = $time - $limit;
		
		my @commaSeparatedDirs=split(/,/, $dir);
		
		foreach my $commaSeparatedDir (@commaSeparatedDirs){
		
			my @files = sortFilesNewestFirst("$datagenStore$commaSeparatedDir");
			my @oldestFirst = reverse(@files);

			foreach my $file (@files){
				if( fileTime("$datagenStore$commaSeparatedDir/$file") < $oldestTime ){#too old, delete
					toLog("INFO: Removing \"$commaSeparatedDir/$file\" from data store.");
					unlink("$datagenStore$commaSeparatedDir/$file");
					foreach my $server (@servers){
						unlink("$centralDatagenDir/$server$commaSeparatedDir/$file");
						foreach my $prof (@profiles){
							unlink("$centralDatagenDir/$server/$prof$commaSeparatedDir/$file");
						}
					}
				}
			}
			
			my @origionalFiles = sortFilesNewestFirst($commaSeparatedDir);
			foreach my $origionalFile (@origionalFiles){
				if( fileTime("$commaSeparatedDir/$origionalFile") < $oldestTime and !-d "$commaSeparatedDir/$origionalFile" ){#too old, delete
					toLog("INFO: Removing \"$commaSeparatedDir/$origionalFile\" from main folder.");
					unlink("$commaSeparatedDir/$origionalFile");
				}
			}
		}
	}
	foreach my $emptyDir (@empty){
		toLog("INFO: Emptying $emptyDir.");
		my @files = sortFilesNewestFirst($emptyDir);
		foreach my $del (@files){
			if( !-d "$emptyDir/$del" ){
				toLog("INFO: Removing \"$emptyDir/$del\"");
				unlink("$emptyDir/$del");
			}
		}
	}
	my $time = time;
	my $limit = 1 * 60 * 60;
	my $oldestTime = $time - $limit;
	foreach my $toClear (@clear){
		toLog("INFO: Clearing $toClear");
		my @files = sortFilesNewestFirst($toClear);
		foreach my $file (@files){
			if( fileTime("$toClear/$file") < $oldestTime and !-d "$toClear/$file" ){
				toLog("INFO: Removing \"$toClear/$file\"");
				unlink("$toClear/$file");
			}
		}
	}
	#scp delete
	foreach my $dir(@SCPClear){
		my $filesToKeep = 60;#2 hours worth = 120. 1 file a minute..
		my @allFiles = `ssh support\@${SCPServer} 'ls -t ${dir}'`;
		chomp(@allFiles);
		if(@allFiles > $filesToKeep){
			my @toDelete;
			for( my $i = $filesToKeep; $i < @allFiles; $i++ ){
				toLog("INFO: Prepairing to delete '$dir/$allFiles[$i]' from $SCPServer");
				push( @toDelete, $dir."/".$allFiles[$i] );
			}
			my $files = join( " ", @toDelete );
			my $rm = `ssh support\@${SCPServer} 'rm $files'`;
		}
	}
	
	#clear syslog
	clearSyslog();
	logger(@logger);
	unlink("/tmp/ecfile");
	if(!executeAndWaitForTimeout("/eniq/home/dcuser/mz2/mz/bin/launchmzsh.sh status EC1 > /tmp/ecfile")){
		print "Timed out. Killing EC1 process and restarting\n";
		executeThisQuiet("ps -ef |  grep \\\"Xmx.*OnOut\\\" | grep -v grep| awk '{print \\\$2}' | xargs kill -9 2>/dev/null\"");
		executeThisQuiet("ps -ef |  grep \\\"Xmx.*OnOut\\\" | grep -v grep| awk '{print \\\$2}' | xargs kill -9 2>/dev/null\"");
		executeThisQuiet("/eniq/home/dcuser/mz2/mz/bin/launchmzsh.sh restart EC1");
	}else{
		print "Status command succeeded. Checking if EC1 is running and restarting if it's not\n";
		if(!grepFile("EC1 is running","/tmp/ecfile")){
			print "EC1 is not running. Restarting\n";
			executeThisQuiet("/eniq/home/dcuser/mz2/mz/bin/launchmzsh.sh restart EC1");
		}else{
			print "EC1 is running. Nothing to do\n";
		}
	}
	unlink("/tmp/ecfile");
}
