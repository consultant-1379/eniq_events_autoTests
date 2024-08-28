#!/usr/bin/perl

use strict;
use Sys::Hostname;
use POSIX;

my $mzsh = "/eniq/home/dcuser/mz/bin/launchmzsh.sh";
my $username = "mzadmin";
my $password = "central1";

my $datagenserver = hostname;
my $timeDiffThreshold = 1860; #31 minutes

my $homePath=$ENV{"HOME"};
my $configFile="$homePath/centralDatagen/DATAGEN_SETTINGS.txt";
my $centralDatagenDir;
my $remoteDatagenStore;
my $localDatagenStore;

my $serverRoot = "/tmp/CentralDatagen/monitor";

my @profiles;
my @datagenDirs;

my @errors;

sub parseParameters{
	if(!-e $configFile){
		push(@errors, "Cannot find 'DATAGEN_SETTINGS.txt' file. Can't go on...");
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
		}elsif( $line =~ m/^CENTRALDATAGENDIR=(.*)/){
			chomp($centralDatagenDir = $1);
			next;
		}elsif( $line =~ m/^DATAGENSTORE=(.*)/){
			chomp($remoteDatagenStore = "/net/$datagenserver".$1);
			chomp($localDatagenStore = $1);
			next;
		}elsif( $line =~ m/^SERVERS/){
			$serverFeed = 1;	
			next;
		}elsif( $line =~ m/^PROFILES/){
			$profileFeed = 1;	
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
		}elsif($profileFeed){
			chomp($line);
			push( @profiles, [split(/,/, $line)] );
		}elsif($dirFeed){
			chomp($line);
			$line =~ s/\/+$//;#Remove trailing /
			if($line =~ m/,/){
				my @decoded = sortOutCommaDirectories($line);
				push( @datagenDirs, @decoded);
			}else{
				push( @datagenDirs, $line);
			}
		}
	}
}

sub run{
	my $command = shift;
	my @output = `$command`;
	return @output;
}

sub fileTime{
        my $file = shift;
        my $time;
        $time = (stat($file))[9] or $time = 0;
        chomp($time);
        return $time;
}

sub sortOutCommaDirectories{
	my $line = shift;
	my @dirs;
	my @args = split(/,/, $line );
	shift(@args);
	for( my $i = 0; $i < @args; $i += 2){
		push( @dirs, $args[$i] );
	}
	return @dirs;
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
		return @list[$pos];
	}
	return @list;
}

sub checkFiles{
	my @newFiles;
	foreach my $directory (@datagenDirs){
		foreach my $profile (@profiles){
			my @array = @$profile;
			my $profileName = @array[0];
			my $newestFile = ls("$serverRoot/$profileName$directory","lt", 0);
			if($newestFile ne ""){
				push(@newFiles, $newestFile);
			}
		}
	}
	my $curTime = time;
	if (!@newFiles ){#serious problem
		push(@errors, "No directories are creating any files. Something is seriously wrong");
	}else{
		foreach my $file (@newFiles){
			my $fileTime = fileTime($file);
			if($curTime - $fileTime > $timeDiffThreshold){
			#if($curTime - $fileTime > 1){
				my $diff = $curTime - $fileTime;
				$file =~ m/((\/.*)*\/)/;
				my $dir = $1;
				push(@errors, "Curr Tme: '$curTime'.File tme: '$fileTime'.File:'$file'. Last file in '$dir' was over ".floor($diff/60)." minutes ago");
			}			
		}
	}
}

sub diskSpace{
	my $toReturn = 1;
	my $threshold = 90;
	my @result = run("df -hk");
	foreach my $precent (@result){
		chomp($precent);
		if($precent=~m%/net/%){
			next;
		}
		if($precent=~ m/(\d+)%/){
			
			if( $1 > $threshold ){
				$toReturn = 0;
				push(@errors, "Disk above $threshold% - $precent");
			}
		}
	}
	return $toReturn;
}

#print "Begin!\n";	
mkdir($serverRoot);
parseParameters();
checkFiles();
diskSpace();
if( !@errors ){
	print "INFO: Server is running correctly\n";
	#print "FAILURE SIMULATOR!!!!!\n";
}else{
	foreach my $val (@errors){
		print "ERROR: $val\n";
	}
}
