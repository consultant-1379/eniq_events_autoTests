#!/usr/bin/perl -C0
use strict;
use File::Copy;

#This script runs every 30 minutes and if it finds that a mediation zone workflow has aborted, it clears out the input and output directories of those workflows to attempt to fix the problems
#Currently it just works with MSS workflows

my $logFile="/eniq/home/dcuser/automation/cleardirlog.log";
my $cron="0,30 * * * * perl /eniq/home/dcuser/automation/cleardirsafterabort.pl >/dev/null 2>&1";
sub executeThisQuiet
{
	my $command = shift;
	open(CMD,"$command |");
	my @cmd=<CMD>;
	close(CMD);
	return @cmd;
}

sub executeThisWithLogging
{
	my @cmd;

	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>)
	{
		&FT_LOG($_);
		push(@cmd, $_);
	}
	close(CMD);
	return @cmd;
}

sub executeThisAndGiveReturnCode
{
	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>)
	{
		&FT_LOG($_);
	}
	close(CMD);
	return $?;
}

sub executeAndWaitForTimeout
{
	my $command = shift;
	my $timeout = shift;
	
	if($timeout==""){
		$timeout=36000;
	}
	&FT_LOG("Executing: $command with timeout:$timeout");
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

sub killProcessAndChildren
{
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

sub FT_LOG {
	my ($message) = @_;
	chomp $message;
	if (open (AUDIT, ">> $logFile")) {
		my ($SS, $MM, $HH, $dd, $mm, $yy) = (localtime)[0..5];
		my ($time) = sprintf("%d-%02d-%02d %02d:%02d:%02d",$yy+1900, $mm+1, $dd, $HH, $MM, $SS);
		print AUDIT "$time $message\n";
		close AUDIT;
	}
	else {
		print STDOUT "$message\n";
		&FT_ERROR ("Could not open $logFile for writing");
	}
	print STDOUT "$message\n";
}
sub getTimeSpecLocal{
	my $mytime = shift;
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime($mytime);
	return sprintf "%4d-%02d-%02d %02d:%02d:%02d", $year+1900,$mon+1,$mday,$hour,$min,$sec;
}
sub dirToArray{
	my $directory = shift;
	opendir( THEDIR, $directory );
	my @contents = readdir(THEDIR);
	closedir( THEDIR);
	return @contents;
}

sub deleteArchiveFilesOlderThan24Hours{
	my $topDir = "/eniq/data/pmdata/eventdata/";
	my @topDirs = dirToArray($topDir);
	foreach my $directory (@topDirs){
		my $folder = "$topDir/$directory";
		my @contents = dirToArray($folder);
		my $archiveDir = "";
		foreach my $subDir (@contents) {
			$archiveDir = "$folder/$subDir/archive";
			if(! -d $archiveDir ){
				next;
			}
			my @archivedFiles = dirToArray($archiveDir);
			foreach my $file (@archivedFiles) {
				if(! -d "$archiveDir/$file" ){
					my $mtime=(stat("$archiveDir/$file"))[9];
					if(time - $mtime > 86400 ){
						&FT_LOG("INFO: Deleting $archiveDir/$file\n");
						unlink("$archiveDir/$file")	;
						
					}
				}
			}
		}
	}
}

sub deleteFilesOlderThan2Hours{
	my @directoriesToCheck = ("/tmp/OMS_LOGS/ebs/ready","/tmp/OMS_LOGS/ebs/ready1","/eniq/data/events_data/events_oss_1/sgeh/dir1","/eniq/data/events_data/events_oss_1/sgeh/dir2","/eniq/data/events_data/events_oss_1/sgeh/dir3","/eniq/data/events_data/events_oss_1/sgeh/dir4","/eniq/data/events_data/events_oss_1/sgeh/dir5","/eniq/data/events_data/events_oss_1/sgeh/dir6");
	
	foreach my $directory (@directoriesToCheck){
		my @files = dirToArray($directory);
		foreach my $file (@files){
			if(-f "$directory/$file"){
				my $mtime = (stat("$directory/$file"))[9];
				if(time - $mtime > 7200){
					&FT_LOG("INFO: Deleting $directory/$file");
					unlink("$directory/$file");
				}
			}
		}
	}
}


my @crontab=executeThisQuiet("crontab -l");
if( grep(/cleardirsafterabort\.pl \>\/dev/,@crontab)==0 ){
	push(@crontab,$cron."\n");
	open(CRON,">/tmp/cron.$$");
	foreach(@crontab){
		if($_ !~ m/cleardirsafterabort/ || $_ =~ m/dev/){
			print CRON $_;
		}
	}
	close(CRON);
	executeThisWithLogging("crontab /tmp/cron.$$");
	unlink ("/tmp/cron.$$");
}

	executeThisWithLogging("ssh dcuser\@ec_1 'mkdir -p /tmp/OMS_LOGS/ebs/ready'");
	executeThisWithLogging("ssh dcuser\@ec_1 'mkdir -p /tmp/bin/input' ");
	if(!-d "/tmp/bin/input"){
		mkdir("/tmp/bin");
		mkdir("/tmp/bin/input");
	}
	#4G
	if (!-e "/tmp/bin/input/conf_4G.prop"){										
		copy("/eniq/home/dcuser/automation/DataGenWorkFlows/conf_4G.prop","/tmp/bin/input/");
	}
	my $success=executeThisAndGiveReturnCode("ssh dcuser\@ec_1 'ls /tmp/bin/input/conf_4G.prop >/dev/null 2>&1");
	if($success != 0){
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/conf_4G.prop dcuser\@ec_1:/tmp/bin/input/");
	}
	executeThisWithLogging("ssh dcuser\@ec_1 'mkdir -p /tmp/bin/input' ");
	
	#WCDMA
	if (!-e "/tmp/bin/input/conf_WCDMA.prop"){
		copy("/eniq/home/dcuser/automation/DataGenWorkFlows/conf_WCDMA.prop","/tmp/bin/input/" );
	}
	$success=executeThisAndGiveReturnCode("ssh dcuser\@ec_1 'ls /tmp/bin/input/conf_WCDMA.prop >/dev/null 2>&1");
	if($success != 0){
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/mss_conf.prop dcuser\@ec_1:/tmp/bin/input/");
	}	
		
	#MSS
	if (!-e "/tmp/bin/mss_conf.prop"){
		copy("/eniq/home/dcuser/automation/DataGenWorkFlows/mss_conf.prop","/tmp/bin/");
	}
	$success=executeThisAndGiveReturnCode("ssh dcuser\@ec_1 'ls /tmp/bin/mss_conf.prop >/dev/null 2>&1");
	if($success != 0){
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/mss_conf.prop dcuser\@ec_1:/tmp/bin/");
	}
	
	#SGSN
	if (!-e "/tmp/bin/input/sgsn.prop"){
		copy("/eniq/home/dcuser/automation/DataGenWorkFlows/sgsn.prop","/tmp/bin/input/");
	}
	$success=executeThisAndGiveReturnCode("ssh dcuser\@ec_1 'ls /tmp/bin/input/sgsn.prop >/dev/null 2>&1");
	if($success != 0){
		executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenWorkFlows/sgsn.prop dcuser\@ec_1:/tmp/bin/input/");
	}
	$success=executeThisAndGiveReturnCode("ssh dcuser\@ec_1 'ls /tmp/bin/input/batch5000 >/dev/null 2>&1");
	if($success != 0){
		if(!-e "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/input.zip"){
			if(!-e "/tmp/DataGenTopology4G/input.zip"){
				executeThisWithLogging("unzip /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G.zip -d /tmp >/dev/null");
			}
			executeThisWithLogging("scp /tmp/DataGenTopology4G/input.zip dcuser\@ec_1:/tmp/bin");
		}else{
			executeThisWithLogging("scp /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/input.zip dcuser\@ec_1:/tmp/bin");
		}
		executeThisWithLogging("ssh dcuser\@ec_1 'unzip -o /tmp/bin/input.zip -d /tmp/bin/input >/dev/null' ");
	}


my @groups=executeThisQuiet("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist");

my @wcdmaAborted=grep /eankmuk_WCDMA\.W[oG].*Aborted/,@groups;
if(@wcdmaAborted){
##opendir (DIR, "/eniq/data/eventdata/events_oss_1/GPEHEvents/dir1/Event files");
	opendir (DIR, "/eniq/data/eventdata/");
	while(my $event_oss = readdir(DIR)){
		if($event_oss =~ /events_oss_/){
			&FT_LOG("Checking /eniq/data/eventdata/$event_oss/ for GPEHEvents\n");
			if(-d "/eniq/data/eventdata/$event_oss/GPEHEvents"){
				&FT_LOG("Opening: /eniq/data/eventdata/$event_oss/GPEHEvents\n");
				opendir (eventsFolder, "/eniq/data/eventdata/$event_oss/GPEHEvents/");
				while(my $dirNo = readdir(eventsFolder)){
					opendir (dirFolder, "/eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo");	
					while(my $events = readdir(dirFolder)){
						if($events !~ /DR_TMP_DIR/){
							&FT_LOG("Deleting: /eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events\n");
							unlink "/eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events";
						}
						elsif($events =~ /DR_TMP_DIR/){
							opendir (tmpDirFolder, "/eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events");
							while(my $tmpEvents = readdir(tmpDirFolder)){
								&FT_LOG("Deleteing: /eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events/$tmpEvents\n");
								unlink "/eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events/$tmpEvents";
								&FT_LOG("Deleteing: /eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events/$tmpEvents\n");
								rmdir "/eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events";
							}
							closedir tmpDirFolder;
							&FT_LOG("Closing: /eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo/$events\n");
						}
					}
					closedir dirFolder;
					&FT_LOG("Closeing: /eniq/data/eventdata/$event_oss/GPEHEvents/$dirNo\n");
				}
				closedir eventsFolder;
				&FT_LOG("Closing: /eniq/data/eventdata/$event_oss/GPEHEvents\n");
			}			
		}
	}
}

my @mssAborted=grep /MSS\.W[oG].*Aborted/,@groups;
if(@mssAborted){
	for(my $i=10;$i>0;$i--){
		my $num=$i+1;
		if( -f "$logFile.$i"){
			if($i==10){
				unlink("$logFile.$i");
			}else{
				move("$logFile.$i","$logFile.$num");
			}
		}
	}
	if( -f "$logFile"){
		move("$logFile","$logFile.1");
	}
	&FT_LOG(getTimeSpecLocal(time));
	&FT_LOG("Found aborted workflows:");
	foreach(@mssAborted){
		&FT_LOG($_);
	}
	opendir (DIR, "/eniq/data/pmdata/eventdata");
	my @dirs = grep /[0-9]+/, readdir(DIR);
	closedir DIR;
	foreach my $dir (@dirs) {
		opendir (DIR, "/eniq/data/pushData/$dir/mss");
		my @subdirs = grep /^MSS/,readdir(DIR);
		closedir DIR;
		foreach my $subdir (@subdirs) {
			opendir (DIR, "/eniq/data/pushData/$dir/mss/$subdir");
			my @subfiles = grep /^MSS/,readdir(DIR);
			closedir(DIR);
			foreach my $subfile (@subfiles) {
				&FT_LOG("Deleting: /eniq/data/pushData/$dir/mss/$subdir/$subfile");
				unlink ("/eniq/data/pushData/$dir/mss/$subdir/$subfile");
			}
		}
	}
	open(PROP,"</eniq/mediation_inter/M_E_MSS/etc/configuration.prop");
	my @dirLines=grep /FOLDER_/,<PROP>;
	close(PROP);
	foreach my $dirLine (@dirLines) {
		if ($dirLine!~ /archive/){
			my @sp=split(/=/,$dirLine);
			my $dir=$sp[1];
			chomp($dir);
			opendir (DIR, "$dir");
			my @files = grep {/^MSS/} readdir(DIR);
			closedir DIR;
			foreach my $file (@files) {
				&FT_LOG("Deleting: $dir/$file");
				unlink("$dir/$file");
			}
			if( -d "$dir/DR_TMP_DIR"){
				opendir (DIR, "$dir/DR_TMP_DIR");
				my @files = grep {/^MZ/}  readdir(DIR);
				closedir DIR;
				foreach my $file (@files) {
					&FT_LOG("Deleting: $dir/DR_TMP_DIR/$file");
					unlink("$dir/DR_TMP_DIR/$file");
				}
			}
		}
	}
}

# script to kill EC_1 if it appear to hang
if(!executeAndWaitForTimeout("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr status EC1",60)){
	&FT_LOG("EC1 appears to be hanging. Executing Kill and Restart.");
	executeThisWithLogging("ssh ec_1 \"ps -ef | egrep \\\"16000M|1026M\\\" | egrep -v egrep| awk '{print \\\$2}' | xargs kill -9 2>/dev/null\"");
	executeThisWithLogging("ssh ec_1 \"/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr restart EC1\"");
}

deleteArchiveFilesOlderThan24Hours();
deleteFilesOlderThan2Hours();