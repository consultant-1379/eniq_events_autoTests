#!/usr/bin/perl

use strict;
use File::Basename;
use File::Copy;
use Sys::Hostname;

my @datagenDirs;
my @servers;
my @profiles;
my @SCPDirs;
my @logger;
my $datagenserver = hostname.".athtem.eei.ericsson.se";
my $SCPServer;
my $centralDatagenDir;
my $localDatagenStore;
my $remoteDatagenStore;
my $processes;
my $logging = 0;
my $printing = 0;
my $delay;
my $logDir;
my $logFile;
my $noOfLogs;
my $olderThan; 
my $homePath=$ENV{"HOME"};
my $configFile="$homePath/centralDatagen/DATAGEN_SETTINGS.txt";
my $host;

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
	my $profileFeed = 0;
	my $SCPFeed = 0;
	foreach my $line (grep(!/^#|^$/, @file)){
		if( $line =~ m/^DATAGENSERVER=(.*)/){
			#chomp($datagenserver = $1);
			next;
		}elsif( $line =~ m/^SCPSERVER=(.*)/){
			chomp($SCPServer = $1);
			next;
		}elsif( $line =~ m/^CENTRALDATAGENDIR=(.*)/){
			chomp($centralDatagenDir = $1);
			next;
		}elsif( $line =~ m/^PROCESSES=(.*)/){
			chomp($processes = $1);
			next;
		}elsif( $line =~ m/^DATAGENSTORE=(.*)/){
			chomp($remoteDatagenStore = "/net/$datagenserver".$1);
			chomp($localDatagenStore = $1);
			next;
		}elsif( $line =~ m/^LOGDIR=(.*)/){
			chomp($logDir = $1);
			$logFile = "$logDir/log.txt";
			next;
		}elsif( $line =~ m/^NUMOFLOGS=(.*)/){
			chomp($noOfLogs = $1);
			next;
		}elsif( $line =~ m/^LOGGING=(.*)/){
			if($1 =~ m/ON|1/i){
				$logging = 1;
			}else{
				$logging = 0;
			}
			next;
		}elsif( $line =~ m/^PRINTING=(.*)/){
			if($1 =~ m/ON|1/i){
				$printing = 1;
			}else{
				$printing = 0;
			}
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
		}elsif( $line =~ m/^PROFILES/){
			$profileFeed = 1;	
			next;
		}elsif( $line =~ m/^SCPFILES/){
			$SCPFeed = 1;	
			next;
		}elsif( $line =~ m/^DIRECTORIES/){
			$dirFeed = 1;	
			next;
		}elsif( $line =~ m/^-$/){
			$serverFeed = 0;
			$dirFeed = 0;
			$profileFeed = 0;
			$SCPFeed = 0;
			next;
		}elsif($serverFeed){
			chomp($line);
			push( @servers, $line);
		}elsif($profileFeed){
			chomp($line);
			push( @profiles, [split(/,/, $line)] );
		}elsif($SCPFeed){
			chomp($line);
			push( @SCPDirs, [split(/,/, $line)] );
		}elsif($dirFeed){
			chomp($line);
			$line =~ s/\/+$//;#Remove trailing /
			push( @datagenDirs, $line);
		}
	}
}

sub makePath{
	my $pathToMake = shift;
	my $path = "";

	my @subFolders = split(/\//, $pathToMake);
	chomp(@subFolders);
	$path = "";
	foreach my $folder (@subFolders){
		$path.=$folder."/";
		if( !-d $path ){
			mkdir($path);# or print "Already Exists - $path\n";
			chmod 0777, $path;
		}
	}
	return $path;
}

sub makeSymbolicLink{
	my $origionalFile = shift;
	my $linkLocation = shift;
	my $linkPath=dirname($linkLocation);
	if(!-d $linkPath){
		makePath($linkPath);
	}
	return symlink($origionalFile, $linkLocation);	#sym links /eniq/pull/mss/dir1 and /atrcxb1929/eniq/pull/mss/dir1
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

sub moveAndLink{
	my ($origionalFile,$newPath,$namePrefix)=@_;
	my $origionalPath=dirname($origionalFile);
	my $origionalName=basename($origionalFile);
	my @newDirs=@$newPath;
	my @newPrefixes=@$namePrefix;
	if(@newDirs==0){
		push(@newDirs,$origionalPath);
	}
	push(@newPrefixes,"");
	
	#move
         	if( !-d "$localDatagenStore$origionalPath" ){
		makePath("$localDatagenStore$origionalPath");
	}
	move( $origionalFile, "$localDatagenStore$origionalFile" );
	
	#link
	foreach my $server (@servers){
		#my $success = makeSymbolicLink("$remoteDatagenStore$origionalFile", "$centralDatagenDir/$server$origionalFile");
		toLog("INFO: Linking \"$remoteDatagenStore$origionalFile\" to $server.");
		if(@profiles){
			foreach my $oneProfile (@profiles){
				my $i=0;
				foreach my $dir(@newDirs){
					my @profile = @$oneProfile;
					#$profile[0] contains name of dir and $profile[1] contains number of files to keep
					if( -d "$centralDatagenDir/$server"){
						my $success = makeSymbolicLink("$remoteDatagenStore$origionalFile", "$centralDatagenDir/$server/".$profile[0]."$dir/$newPrefixes[$i]$origionalName");
						if(!$success){
							my $run = `$homePath/centralDatagen/RunCommandAsRoot.sh chmod 777 $centralDatagenDir/$server`;
							$success = makeSymbolicLink("$remoteDatagenStore$origionalFile", "$centralDatagenDir/$server/".$profile[0]."$dir/$newPrefixes[$i]$origionalName");
						}
						toLog("\tWith profile ".$profile[0].". ".($success?"SUCCESS":"FAIL")." ");
						
						my @files = sortFilesNewestFirst("$centralDatagenDir/$server/".$profile[0]."$dir");
						if( @files > $profile[1]){
							toLog("\tDeleting ".(@files - $profile[1])." file(s) out of ".@files.". Leaving ".$profile[1]." files");
							for(my $i = ($profile[1] - 1); $i < @files; $i++ ){
								unlink("$centralDatagenDir/$server/".$profile[0]."$dir/".$files[$i]);
							}
						}
						#print "\n";#Leave me here >:/
					}
					$i++;
				}
			}
		}
		chmod 0777,"$localDatagenStore$origionalFile";
		chmod 0777,"$remoteDatagenStore$origionalFile";

	}
}

sub serverFolders{
	opendir(DIR, $centralDatagenDir);
	my @dirs = readdir(DIR);
	close(DIR);
	chomp (@dirs);
	@servers = grep(!/\.\.?/, @dirs);
	my $serverFile = $homePath."/centralDatagen/SERVERS_BACKUP.txt";
	open( FILE, ">${serverFile}");
	foreach my $server (@servers){
		print FILE $server."\n";
	}
	close( FILE );
}

sub uniq{
    return keys %{{ map { $_ => 1 } @_ }};
}

sub shareFolderOnNetwork{
	my $folder = shift;
	my $permission = shift;
	logger("INFO: Sharing $folder on the network.");
	if($permission eq ""){
		my $run = `$homePath/RunCommandAsRoot.sh /usr/sbin/share -o rw -F nfs $folder`;
	}else{
		my $run = `$homePath/RunCommandAsRoot.sh /usr/sbin/share -o $permission -F nfs $folder`;
	}
}
sub restoreServerBackup{
	my $serverFile = $homePath."/centralDatagen/SERVERS_BACKUP.txt";
	if( -e $serverFile){
		open( FILE, $serverFile);
		my @fileContents = <FILE>;
		close( FILE );
		chomp(@fileContents);
		foreach(@fileContents){
			makePath("/tmp/CentralDatagen/$_");
		}
		serverFolders();
	}
}

sub initialise{
	#Ste variables from Config file
	restoreServerBackup();
	setVariables();
	#share
	shareFolderOnNetwork($centralDatagenDir, "rw");
	shareFolderOnNetwork($localDatagenStore, "rw");
	#location of packages
	shareFolderOnNetwork("/package", "ro");
}

sub checkIfAlreadyRunning{
	my @ps = `/usr/bin/ps -ef`;

	my @centralDatagenProcesses = grep(/perl.*CentralDatagen.pl/, @ps);
	my @centralDatagenProcesses = grep(!/\s$$\s/, @centralDatagenProcesses);
	if( $ARGV[0] =~ /-?force|f$/i ){
		print "FORCING other processes to stop and running this script.\n";
		if(@centralDatagenProcesses>=2){
			foreach my $p (@centralDatagenProcesses){
				my $id = (split(" ", $p))[1];
				print "id = $id\n";
				kill 9, $id;
			}
		}
		return 1;
	}else{
		my $arrCount=@centralDatagenProcesses;
		if($arrCount>=2){
			return 0;
		}
		return 1;
	}
}

sub setVariables{
	#Empty array to fill arrays again or else it duplicates values. Also allows removal
	@datagenDirs = ();
	@servers = ();
	@profiles = ();
	parseParameters();
	serverFolders();
	#make paths
	makePath($centralDatagenDir);
	makePath($localDatagenStore);
	makePath($logDir);
	unlink("$logFile.lck");
}

#returns an array of x arrays
sub splitArray{
	my $splitIn = shift;
	my @toSplit = @_;
	my @toReturn = ();
	my $ammountInEach = (@toSplit / $splitIn);
	my @temp = ();
	for( my $i = 0; $i < @toSplit; $i += $ammountInEach){
		@temp = ();
		foreach( my $j = $i; $j < ($i + $ammountInEach); $j++ ){
			push( @temp, $toSplit[$j] );
		}
		push( @toReturn, [@temp] );
	}
	return ( @toReturn );
}

sub SCPToServer{
	my $file = shift;
	my $server = shift;
	my $serverPath = shift;
	#make directory
	my @dirs = `ssh $server 'mkdir -p $serverPath'`;
	#copy file
	my @res = `scp $file ${server}:${serverPath}`;
	if( $? == 0){
		return 1;
	}else{
		return 0;
	}
}

sub runningUpdate{
	my $scriptUpdateTime = shift;
	#Account for config file changes :)
	if($scriptUpdateTime < fileTime($configFile)){
		$scriptUpdateTime = fileTime($configFile);
		#Re-set variables from config file
		setVariables();
	}
	#Re-set the folders.
	serverFolders();
	return $scriptUpdateTime;
}

sub logger{
	if($logging){
		my @toLog = @_;
		if($logFile ne ""){
			if( -s $logFile > 5000000 ){#keep record and dont let them get too big
				for( my $i = ($noOfLogs - 1); $i >0; $i-- ){
					if( -e "${logFile}.$i" ){
						move("${logFile}.$i", "${logFile}.".($i+1));
					}
				}
				move($logFile, "${logFile}.1");
			}
			
			open( LOGFILE, ">>$logFile");
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
}

sub toLog{#not really needed but printing can be done in here
	if($logging){
		my $toLog = shift;
		my ($SS, $MM, $HH, $dd, $mm, $yy) = (localtime)[0..5];
		my ($time) = sprintf("%02d/%02d/%d %02d:%02d:%02d",$dd, $mm+1, $yy+1900, $HH, $MM, $SS);
		if($printing){
			print "$time: $toLog\n";
		}
		push(@logger, "$time: $toLog");
	}elsif($printing){
		my $toPrint = shift;
		print "$toPrint\n";
	}
}

{#MAIN
	my $continue = checkIfAlreadyRunning();
	my $scriptUpdateTime = fileTime($configFile);
	if( !$continue ){
		print "ERROR: Script is already running.\nExiting. Re-run with -force option to kill others.";
		exit(0);
	}
	initialise();
	
#CHILDREN
	my @arrays = splitArray($processes, @datagenDirs);
	my $origionalNumOfProcesses = $processes;#cannot be changed during run...

	for( my $i = 0; $i < @arrays; $i++ ){#Creates necessary children...
		my $pointer = $arrays[$i];
		my @dirs = @$pointer;	
		if( fork() == 0 ){#CHILD
			logger("Sleeping(child ".($i+1).") for ".(($delay / $origionalNumOfProcesses) * $i)." seconds to offset processes.");
			sleep (($delay / $origionalNumOfProcesses) * $i);
			my $updateCount = 0;
			while(1){#LOOP PROGRAM
				my $startTime = time;
				@logger = ();
				if($updateCount >= 2){
					my $newScriptUpdateTime = runningUpdate($scriptUpdateTime);
					if($newScriptUpdateTime > $scriptUpdateTime){
						$scriptUpdateTime = $newScriptUpdateTime;
						#update dirs
						my @all = splitArray($origionalNumOfProcesses, @datagenDirs);#keep origional size of processes AS cannot change ammount of processes
						$pointer = $all[$i];#makes sure each process gets a different set of folders.
						@dirs = @$pointer;
						$updateCount = 0;
					}
				}
				$updateCount++;
				#move and link these files
				foreach my $dir (@dirs) {
					my @vals=();
					if($dir=~ m/,/){
						@vals=split(/,/,$dir);
					}else{
						@vals=($dir);
					}
					my $arrSize=@vals;
					my @newDirs=();
					my @prefixes=();
					for(my $i=1;$i<$arrSize;$i+=2){
						push(@newDirs,$vals[$i]);
						push(@prefixes,$vals[$i+1]);
					}
					opendir( DIR, $vals[0] );
					my @files = readdir(DIR);
					closedir(DIR);
					foreach my $file (@files) {
						if( !-d "$vals[0]/$file" ){
							moveAndLink("$vals[0]/$file",\@newDirs,\@prefixes);
						}
					}
				}
				logger(@logger);
				logger("Sleeping(child ".($i+1).") for ".($delay - (time-$startTime))." seconds...");
				#Account for time it took to run
				sleep ($delay - (time-$startTime));
			}	
		}
	}
	
#PARENT
	#SCP these files
	my $updateCount = 0;
	while(1){
		my $startTime = time;
		@logger = ();
		if($updateCount >= 2){
			my $scriptUpdateTime = runningUpdate($scriptUpdateTime);
			$updateCount = 0;
		}
		foreach my $dirsToAndFromPointer (@SCPDirs) {
			my @dirsToAndFrom = @$dirsToAndFromPointer;
			my $fromDir = $dirsToAndFrom[0];
			my $toDir = $dirsToAndFrom[1];
			opendir( DIR, $fromDir );
			my @files = readdir(DIR);
			closedir(DIR);
			foreach my $file (@files) {
				if( !-d "$fromDir/$file" ){
					if( SCPToServer("$fromDir/$file", "support\@${SCPServer}", $toDir) ){
						toLog("INFO: SCP'd $fromDir/$file to ${SCPServer} in directory $toDir. SUCCESS");
						unlink("$fromDir/$file");
					}else{
						toLog("ERROR: SCP'd $fromDir/$file to ${SCPServer} in directory $toDir. FAIL");
					}
				}
			}
		}
		$updateCount++;
		logger(@logger);
		logger("Sleeping(parent) for ".($delay - (time-$startTime))." seconds...");
		#Account for time it took to run
		sleep ($delay - (time-$startTime));
	}

}
