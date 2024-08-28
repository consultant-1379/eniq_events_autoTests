#!/usr/bin/perl

use strict;
use File::Copy;
use File::Compare;
use Sys::Hostname;


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

my $host=hostname;
my $NIQFile = "/eniq/sw/conf/niq.ini";
my $platformXmlFile = "/eniq/mediation_sw/mediation_gw/etc/platform.xml";
my $executionContextXmlFile = "/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml";
my $glassFishFile = "/eniq/glassfish/glassfish3/glassfish/domains/domain1/config/domain.xml";
my $runDate = getRunDate();
my $uid =  splitAndGetValue( (grepFile("^dcuser:", "/etc/passwd"))[0] ,":", 3);#for chown
my $gid =  splitAndGetValue( (grepFile("^dc5000:", "/etc/group"))[0] ,":", 3);#for chown
my $machine;
my $totalChanges = 0;
my %NIQValues;
my %executionValues;
my %platformValues;

sub getRunDate{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
	return sprintf "%4d%02d%02d%02d%02d", $year+1900,$mon+1,$mday,$hour,$min;
}

sub isMultiBladeServer{
	my $co_server = (grepFile("engine", "/etc/hosts"))[0];
		my $engine = splitAndGetValue($co_server, " ", 3);
	my $ec_server = (grepFile("ec_1", "/etc/hosts"))[0];
		my $ec = splitAndGetValue($ec_server, " ", 3);
	print "ENGINE: $engine && EC: $ec\n";
	if ($ec eq $engine) {
		#This is a Standalone server
		return 0;
	}
	else {
		#This is a Multi-blade server
		return 1;
	}
}

sub addCronJob{
	my $cron = shift;
	my $toCheck = $cron;
		$toCheck =~ s/\*/\\\*/g;#Replace * with \*
		$toCheck =~ s/\|/\\\|/;#Replace | with \|
	open( CRON , "</var/spool/cron/crontabs/dcuser");
	my @crontab=<CRON>;
	close( CRON );
	if( grep(/$toCheck/,@crontab)==0 ){	
		$totalChanges ++;
		open(CRON,">>/var/spool/cron/crontabs/dcuser");
		print CRON $cron."\n";
		close(CRON);
	}
	
}

sub isMultipleEC{
	
	#counts the number of times 'config name' occurs inexecutioncontext.xml
	#my $line = `grep -c 'config name' /eniq/mediation_sw/mediation_gw/etc/executioncontext.xml`;
	my $line = grepFile("config name", "/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml");
	chomp($line);
	
	my $numberOfEC = $line;
	chomp($numberOfEC);
	
	if ( $numberOfEC > 2 ){
		#This has multiple ECs
		return 1;
	}
	else{
		#This does NOT have multiple ECs
		return 0;
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

sub getMemorySize{
	my $line = `/usr/sbin/prtconf | grep 'Memory size'`;
	chomp($line);
	$line =~ s/Memory size: //;
	$line =~ s/ Megabytes//;
	return $line;
}

sub setNewValues{
	my $ram = getMemorySize();
	my $multiEC = isMultipleEC();
	if($ram < 60000){
	#32 Gig machine
		$machine = 32;
		if($multiEC){
			%NIQValues = ('EngineHeap' => 1024, 'MainCache' => 6144, 'TempCache' => 3072); 
		}else{
			%NIQValues = ('EngineHeap' => 1024, 'MainCache' => 8192, 'TempCache' => 4096); 
		}
		#%platformValues = ('Xmx' => 256, 'Xms' => 128, 'MaxPermSize' => 128);
		if($host eq "eniqe"){
		%platformValues = ('Xmx' => 4096, 'Xms' => 512, 'MaxPermSize' => 512);
		}
		else {
		%platformValues = ('Xmx' => 256, 'Xms' => 128, 'MaxPermSize' => 128);
		}
		%executionValues = ('Xmx' => 1000, 'Xms' => 512, 'MaxPermSize' => 256); 
	}elsif($ram < 70000 ){
	#64 Gig machine
		$machine = 64;
		if($multiEC){
			%NIQValues = ('EngineHeap' => 1024, 'MainCache' => 10240, 'TempCache' => 5120); 
		}else{
			%NIQValues = ('EngineHeap' => 1024, 'MainCache' => 20480, 'TempCache' => 10240); 
		}
		#%platformValues = ('Xmx' => 256, 'Xms' => 128, 'MaxPermSize' => 128);
		if($host eq "eniqe"){
		%platformValues = ('Xmx' => 4096, 'Xms' => 512, 'MaxPermSize' => 512);
		}
		else {
		%platformValues = ('Xmx' => 256, 'Xms' => 128, 'MaxPermSize' => 128);
		}
		%executionValues = ('Xmx' => 1000, 'Xms' => 512, 'MaxPermSize' => 256); 
	}else{
	#128+ Gig machine
	$machine = 128;
		%NIQValues = ('EngineHeap' => 1024, 'MainCache' => 40960, 'TempCache' => 20480); 
	}
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

sub updateExecutionContextFile{
	print "Updating executionContext.xml file\n";
	#Uses values in %executionValues. If not set, doesn't change. 
	my $backupFile = "${executionContextXmlFile}-BACKUP-${runDate}";
	my $tempFile = "/tmp/executionContext.xml.$$";
	copy($executionContextXmlFile, $backupFile);

	open(EXCON, "<$executionContextXmlFile");
	my @execution = <EXCON>;
	close(EXCON);
	my @newExecution;
	my $changes = 0;
	my $headlessPresent = 0;
	foreach my $line (@execution){
		if( $line =~ m/headless/ ){
			$headlessPresent = 1;
		}elsif( $line =~ m/Xmx/ and $line !~ m/Xmx$executionValues{'Xmx'}/ ){
			if($executionValues{'Xmx'}){
				$line =~ s/-Xmx\d*/-Xmx$executionValues{'Xmx'}/;
				$changes++;
			}
		}elsif( $line =~ m/Xms/  and $line !~ m/Xms$executionValues{'Xms'}/ ){
			if($executionValues{'Xms'}){
				$line =~ s/-Xms\d*/-Xms$executionValues{'Xms'}/;
				$changes++;
			}
		}elsif( $line =~ m/-XX:MaxPermSize/  and $line !~ m/MaxPermSize=$executionValues{'MaxPermSize'}/ ){
			if($executionValues{'MaxPermSize'}){
				$line =~ s/-XX:MaxPermSize=\d*/-XX:MaxPermSize=$executionValues{'MaxPermSize'}/;
				$changes++;
			}
		}
		
		if( $line =~ m/Xms/ ){
			if( $headlessPresent == 1 ){
				$headlessPresent = 0;
			}else{
				push(@newExecution, "    <jdkarg value=\"-Djava.awt.headless=true\"/>\n");
				$changes++;
			}
			
		}
		push(@newExecution, $line);
	}
	if( $changes > 0 ){
		$totalChanges += $changes;
		open (EXCON, ">$tempFile");
		foreach my $line (@newExecution){
			print EXCON $line;
		}
		close(EXCON);
		copy($tempFile,"${executionContextXmlFile}");#REMOVE NEW
		print "There were $changes change(s) made to $executionContextXmlFile\n";
		chown $uid, $gid, $executionContextXmlFile, $backupFile;
		chmod 0640, $executionContextXmlFile, $backupFile;
	}else{
		print "No changes made to $executionContextXmlFile\n";
		unlink($backupFile);
	}
}

sub updatePlatformFile{
	print "Updating platform.xml file\n";
	#Uses values in %platformValues. If not set, doesn't change. 
	my $backupFile = "${platformXmlFile}-BACKUP-${runDate}";
	my $tempFile = "/tmp/platform.xml.$$";
	copy($platformXmlFile, $backupFile);
	
	open(PFORM, "<$platformXmlFile");
	my @platform = <PFORM>;
	close(PFORM);
	my @newPlatform;
	my $changes = 0;
	my $headlessPresent = 0;
	foreach my $line (@platform){
		if( $line =~ m/headless/ ){
			$headlessPresent = 1;
		}elsif( $line =~ m/Xmx/ and $line !~ m/Xmx$platformValues{'Xmx'}/ ){
			if($platformValues{'Xmx'}){
				$line =~ s/-Xmx\d*/-Xmx$platformValues{'Xmx'}/;
				$changes++;
			}
		}elsif( $line =~ m/Xms/  and $line !~ m/Xms$platformValues{'Xms'}/ ){
			if($platformValues{'Xms'}){
				$line =~ s/-Xms\d*/-Xms$platformValues{'Xms'}/;
				$changes++;
			}
		}elsif( $line =~ m/-XX:MaxPermSize/  and $line !~ m/MaxPermSize=$platformValues{'MaxPermSize'}/ ){
			if($platformValues{'MaxPermSize'}){
				$line =~ s/-XX:MaxPermSize=\d*/-XX:MaxPermSize=$platformValues{'MaxPermSize'}/;
				$changes++;
			}
		}
		
		if( $line =~ m/Xms/ ){
			if( $headlessPresent == 1 ){
				$headlessPresent = 0;
			}else{
				push(@newPlatform, "                          <jdkarg value=\"-Djava.awt.headless=true\"/>\n");
				$changes++;
			}
			
		}
		push(@newPlatform, $line);
	}
	if( $changes > 0 ){
		open (PFORM, ">$tempFile");
		foreach my $line (@newPlatform){
			print PFORM $line;
		}
		close(PFORM);
		copy($tempFile,"${platformXmlFile}");#REMOVE NEW
		print "There were $changes change(s) made to $platformXmlFile\n";
		chown $uid, $gid, $platformXmlFile, $backupFile;
		chmod 0640, $platformXmlFile, $backupFile;
	}else{
		print "No changes made to $platformXmlFile\n";
		unlink($backupFile);
	}
}

sub updateNIQFile{
	print "Updating niq.ini file\n";
	#Uses values in %NIQValues. If not set, doesn't change. 
	#backup
	my $backupFile = "${NIQFile}-BACKUP-${runDate}";
	my $tempFile = "/tmp/niq.ini.$$";
	copy($NIQFile, $backupFile);
	
	#open
	open(NIQ, "<$NIQFile");
	my @NIQFile = <NIQ>;
	close(NIQ);
	my $isDWHMainCache = 0;
	my $isDWHTempCache = 0;
	my $changes = 0;
	foreach my $line (@NIQFile){
		if($line =~ m/\[DWH\]/){
			$isDWHMainCache = 1;
			$isDWHTempCache = 1;
			#$changes++;
		}
		elsif($line =~ m/EngineHeap/ and $line !~ m/$NIQValues{'EngineHeap'}/ ){
			$line =~ s/EngineHeap=\d*/EngineHeap=$NIQValues{'EngineHeap'}/;
			$changes++;
		}elsif( ($line =~ m/MainCache/ and $isDWHMainCache ) and $line !~ m/$NIQValues{'MainCache'}/ ){
			$line =~ s/MainCache=\d*/MainCache=$NIQValues{'MainCache'}/;
			$isDWHMainCache = 0;
			$changes++;
		}elsif( ($line =~ m/TempCache/ and $isDWHTempCache) and $line !~ m/$NIQValues{'TempCache'}/ ){
			$line =~ s/TempCache=\d*/TempCache=$NIQValues{'TempCache'}/;
			$isDWHTempCache = 0;
			$changes++;
		}
	}
	
	
	if( $changes > 0 ){
		$totalChanges += $changes;
		open (NIQ, ">$tempFile");
		foreach my $line (@NIQFile){
			print NIQ $line;
		}
		close(NIQ);
		copy($tempFile,"${NIQFile}");#REMOVE NEW
		print "There were $changes change(s) made to $NIQFile\n";
		chown $uid, $gid, $NIQFile, $backupFile;
		chmod 0640, $NIQFile, $backupFile;
	}else{
		print "No changes made to $NIQFile\n";
		unlink($backupFile);
	}
}

sub updateGlassFishFile{
	if($machine == 32){#Only for 32GB machines
		print "Updating GlassFish domain.xml file\n";
		my $backupFile = "${glassFishFile}-BACKUP-${runDate}";
		my $tempFile = "/tmp/domain.xml.$$";
		copy($glassFishFile, $backupFile);
		
		open(GFISH, "<$glassFishFile");
		my @gfish = <GFISH>;
		close(GFISH);
		my @newGfish;
		my $changes = 0;
		my $xmxChanged = 0;			#\
		my $xmsChanged = 0;			# |---> To make sure to only change first occurance
		my $maxPermChanged = 0;		#/
		
		foreach my $line (@gfish){
			if( $line =~ m/Xmx/ and !$xmxChanged ){
				$xmxChanged = 1;
				if( $line !~ m/Xmx1024/ ){
					$line =~ s/-Xmx\d*/-Xmx1024/;
					$changes++;
				}
			}elsif( $line =~ m/Xms/  and !$xmsChanged ){
				$xmsChanged = 1;
				if( $line !~ m/Xms1024/ ){
					$line =~ s/-Xms\d*/-Xms1024/;
					$changes++;
				}
			}elsif( $line =~ m/-XX:MaxPermSize/ and !$maxPermChanged){
				$maxPermChanged = 1;
				if( $line !~ m/XX:MaxPermSize=256/ ){
					$line =~ s/-XX:MaxPermSize=\d*/-XX:MaxPermSize=256/;
					$changes++;
				}
			}
			push(@newGfish, $line);
		}
		if( $changes > 0 ){
			$totalChanges += $changes;
			open (GFISH, ">$tempFile");
			foreach my $line (@newGfish){
				print GFISH $line;
			}
			close(GFISH);
			copy($tempFile,"${glassFishFile}");
			print "There were $changes change(s) made to $glassFishFile\n";
			chown $uid, $gid, $glassFishFile, $backupFile;
			chmod 0640, $glassFishFile, $backupFile;
		}else{
			print "No changes made to $glassFishFile\n";
		}
	}else{
		print "No need to update $glassFishFile as this is not a 32GB machine\n";
	}
	
}

sub swapFile{
	my $changes = 0;
	if( -e "/swapfile"){
		print "swapfile exists\n";
	}else{
		#A
		$changes ++;
		print "Creating swapfile\n";
		my $makefile = `mkfile 2G /swapfile`;
		my $swap = `swap -a /swapfile`;
		my $count = grepFile("/swapfile", "/etc/vfstab");
		chomp($count);
		if($count == 0){
			$changes ++;
			open( VFS, ">>/etc/vfstab" );
			print VFS "/swapfile - - swap - no -\n";
			close(VFS);
		}
	}
	if( -e "/dev/zvol/dsk/eniq_sp_1/swap" ){
		print "eniq_sp_1 swapfile exists\n";
	}else{
		#B
		$changes ++;
		my $create = `zfs create -V 20G -b 4k eniq_sp_1/swap 2>/dev/null`;
		if( $? != 0 ){
			$create = `zfs create -V 10G -b 4k eniq_sp_1/swap 2>/dev/null`;
		}
		if( $? != 0 ){
			$create = `zfs create -V 5G -b 4k eniq_sp_1/swap 2>/dev/null`;
		}
		my $swap2 = `swap -a /dev/zvol/dsk/eniq_sp_1/swap`;
		if( $? == 0 ){
			my $count = grepFile("/dev/zvol/dsk/eniq_sp_1/swap", "/etc/vfstab");
			chomp($count);
			if($count == 0){
				$changes ++;
				open( VFS, ">>/etc/vfstab" );
				print VFS "/dev/zvol/dsk/eniq_sp_1/swap - - swap - no -\n";
				close(VFS);
			}
		}		
	}
	$totalChanges += $changes;
}

sub queryPlan{
	my @output = qx{su - dcuser -c "$ISQL -UDBA -Psql -Sdwhdb <<EOF
set option PUBLIC.Query_Plan='OFF'
go
bye
EOF"
};

	foreach my $line (@output){
		print $line;
	}
}

sub addCronTabs{
	print "Updating Crontab\n";
	my @crons = ('0 * * * * find /eniq/data/pmdata/eventdata/00/*/archive -name "A20*" ! -mtime -1 -print | xargs rm',
			'0 * * * * find /eniq/data/pmdata/eventdata/01/*/archive -name "A20*" ! -mtime -1 -print | xargs rm',
			'0 * * * * find /eniq/data/pmdata/eventdata/02/*/archive -name "A20*" ! -mtime -1 -print | xargs rm',
			'0 * * * * find /eniq/data/pmdata/eventdata/03/*/archive -name "A20*" ! -mtime -1 -print | xargs rm'
		);
	foreach my $cron (@crons){
		addCronJob($cron);
	}
}

sub restartAllServices{
	print "Restarting all services...(please allow up to 5 minutes)\n";
	my $restart = `/usr/bin/bash /eniq/admin/bin/manage_eniq_services.bsh -a restart -s ALL -N 2>/dev/null`;
	#make sure all online, if not do once more
	my @svcs = `svcs -a | grep eniq`;
	my @svcsICareAbout = grep( !/postgresql_og|roll-snap|dwh_reader|esm/, @svcs );#dont care about these 4 services
	my $notOnline = grep( !/online/, @svcsICareAbout );
	if( $notOnline > 0 ){
		$restart = `/usr/bin/bash /eniq/admin/bin/manage_eniq_services.bsh -a restart -s ALL -N 2>/dev/null`;
	}
	print "Restarted All Services\n";
}

{#main
	
	my $login = (getpwuid $>);
	if( $login ne "root"){
		die "ERROR: Must be ran as root\n";
	}
	if(isMultiBladeServer()){
		print "MULTI!\n";
		die "INFO: Only for single blade servers\n";
	}
	setNewValues();
	
	#In order of Wiki
	#http://atrclin2.athtem.eei.ericsson.se/wiki/index.php/ENIQ_Events_12.1_FT_JUMPSTART_INFO
	updateNIQFile();
	updateGlassFishFile();
	queryPlan();
	updatePlatformFile();
	###Don't do this anymore. Use hard coded file instead
	#updateExecutionContextFile();
	swapFile();
	addCronTabs();
	if($totalChanges != 0){
		print "A total of $totalChanges change".($totalChanges==1?" was":"s were")." made to your system.\n";
		restartAllServices();
	}else{
		print "No changes made, not restarting services\n";
	}
}
