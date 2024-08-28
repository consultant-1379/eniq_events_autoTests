#!/usr/bin/perl

# Version v_1.02, released 20120619
#
# 

use strict;
use Net::FTP;
use Sys::Hostname;
use POSIX;
use File::Copy;
use Cwd;
use Term::ANSIColor;
use Getopt::Long;

#Variables used in processParameters()
my ($RState, @shipments, $build, $deliver, $new, $override, $force);
my $chooseShipment = 0;
my $gitManage = "N";
my $home = $ENV{HOME};
my $signum = getpwuid ( $< );
my $totalSize;
my $CCPackageFolder = "/vobs/eniq_events/auto_tests/LatestPackage";
my $CCview;
my $pwd = cwd();
my $audit_log="$pwd".'/audit';
my $error_log="$pwd".'/audit';

#Variables used for sending email with package updates
my $mailFile="$home/mailFile.txt";
my @message="";
my @emailList=(
	"shukla.pratibha\@tcs.com", 
	"pamesh.g\@tcs.com", 
	"rupeshk.thakur\@tcs.com",
	"tushar.umbarkar\@tcs.com"
);





#Prints text a defined colour and adds a new line.
#Colour may be omitted
#colours ->	black  	red  	green  	yellow  	blue  	magenta  cyan  	white
#background -> ON_BLACK, ON_RED, ON_GREEN, ON_YELLOW, ON_BLUE, ON_MAGENTA, ON_CYAN, ON_WHITE
#example "black on_red", "white"
sub println{
	if(@_ == 2){
		my $colour = shift;
		$colour =~ s/bold //;
		#bold is easier to read so force it.
		print color "bold " . $colour;
	}
	my $text = shift;
	$text =~ s/\n$//;#remove the new line charachter at end, if any
	print "$text\n";
	print color 'reset';
}

sub info{
return qq{
	
The following switches may be supplied ..

   1) -new  -> Will automatically increment the previous used RState, increment the last digit and use it
   2) -override -> Will automatically increment the previous used RState and use it
   3) -rstate <RSTATE> -> State what RState you want to build
        
   4) -shipment -> The shipment you want to deliver to. Multiple shipments are supported with multiple -s switches
   
   5) -build   -> Just builds the package and places it in your home directory for you to use or deliver manually
   6) -deliver -> Same as build but it ftps it to the server and also runs the delivery script for each shipment
   
   7) -force -> Overrides the the user input required at the start
       
./AutoDeliver.pl (-new|-override|-r <RSTATE>) (-build|-deliver) (-shipment <SHIPMENT>) [-force]
e.g /vobs/eniq_events/auto_tests/AutoDeliver.pl -new -deliver -s 2.2.10
e.g /vobs/eniq_events/auto_tests/AutoDeliver.pl -o -b -s 2.2.11 -s 2.2.12 -force

	
};
}

sub processParameters{
	my $help;
	GetOptions(	"r|rstate:s" => \$RState,
				"v|View:s" => \$CCview,
				"s|ship|shipment:s" => \@shipments,
				"build" => \$build,
				"deliver" => \$deliver,
				"new" => \$new,
				"override" => \$override,
				"force" => \$force,
				"help|?" => \$help
		);
	if($help){
		println("yellow", "Help:");
		println("yellow", info());
		exit(1);
	}	
	if(@shipments && !grep( /^$/, @shipments)){
		foreach my $ship (@shipments){
			if($ship !~ m/^\d\d?\.\d\.\d+/ && $ship !~ m/\d,\d/ ){
				println("red", "Shipment number '$ship' is incorrect.\nMust be in the format 1.1.11\n");
				println("yellow", info());
				exit(1);
			}
		}
	}else{
		$chooseShipment = 1;
	}

	if($RState){
		$RState = getRstate(uc($RState));
	}elsif($override){
		$RState = getRstate("override");
	}elsif($new){
		$RState = getRstate("new");
	}else{
		println("red", "ERROR: Need RState");
		println("yellow", info());
		exit(1);
	}
	if ($CCview eq "" && $deliver){
		println("red", "Clearcase View cannot be NULL\nSet view with arg '-v clearcaseDynamic_view'");
		exit(1);
	}
	

	if($build){
		#println("Just build");
	}elsif($deliver){
		#println("Build and deliver");
	}else{
		println("red", "Must select to either build or deliver");
		println("yellow", info());
		exit(1);
	}
}



#
#	Gets the RState and either increments it and returns it of just returns it
# 	depending on user prefrence i.e. new or override
#
#	Parameters: The users argument, -rstate. 
#
sub getRstate{
	my $arg = shift;
	chomp($arg);
	if( $arg !~ m/new|override|-new|-override/i ){
		if( $arg =~ m/^R\d+[A-Z]\d\d$/ ){
			$RState = $arg;
			return $arg;
		}else{
			println("red", "ERROR: RState error: '$arg'. Must be in the form R1A01");
			println("yellow", info());
			exit(1);
		}
	}else{
		my $parseRstate;
		my $parseNum;
		my $ftp=Net::FTP -> new ("atclvm559.athtem.eei.ericsson.se");
		$ftp->login("dcuser","central1") or die "Can not login to server atclvm559!!!";
		$ftp ->cwd("/ossrc/package");
		my @files = $ftp->ls();
		$ftp ->quit;
		
		sort(@files);
		@files = grep(/R\d\dA\d\d\.zip$/, @files);
		
		my $file = pop(@files);
		chomp($file);
		
		$file=~m/(R\d+[A-Z])(\d+)/;
		$parseRstate= $1;#Gets first part of match i.e R6A
		$parseNum=$2;#Gets second part of match i.e 01
		
		my $rState;
		
		if( $arg =~ m/new|-new/i){
		   if($parseNum == "99"){
			   $parseNum = "01";
			   $parseRstate =~ m/R(\d+)([A-Z])/;
               my $digital = $1+1;
               $parseRstate = "R$digital";
			   $parseRstate.=$2;
			   $rState=sprintf("%02d", ($parseNum));
               $parseRstate.=$rState;			   
			}else{
				$parseNum++;
				$rState=sprintf("%02d", ($parseNum));
				$parseRstate.=$rState;
			}
		}else{
			$rState=sprintf("%02d", $parseNum);
			$parseRstate.=$rState;
		}
		
		$RState = $parseRstate;
		return $parseRstate;
	}
}

#used in mvn() to run maven cmds
sub executeThis{
	my @args = @_;
	system(@args) == 0 or die "system @args failed: $?"
}

#builds the zip file
sub mvn{
	#my $pwd = cwd();
	my $PWD = cwd();
	my $pwd = "$PWD/workspace/EE_Selenium_GIT";
	print "Currently in $pwd \n";
	#chdir("/vobs/eniq_events/auto_tests");
	chdir $pwd;
	executeThis("mvn clean");
	executeThis("mvn compiler:compile");
	executeThis("mvn compiler:testCompile");
	executeThis("mvn resources:resources");
	executeThis("mvn dependency:copy-dependencies");
	executeThis("mvn jar:jar");
	executeThis("mvn assembly:single");
	chdir($pwd);
}

#copys the zip from the local /target/ and adds on the Rstate
sub renameAndCopy{
	my $pwd = cwd();
	my @localPackages=sort (`ls $pwd/target/ENIQ_EVENTS_AUTO_TESTS*`);
	my $zipFile = "$pwd/target/ENIQ_EVENTS_AUTO_TESTS.zip";
	my $newZipFile = "$pwd/target/ENIQ_EVENTS_AUTO_TESTS_$RState.zip";
	if( -e $zipFile){
		println("Changing $zipFile to $newZipFile");
		rename($zipFile, $newZipFile);
		my @copyResult = `cp $newZipFile $home 2>&1`;
		if ((grep /No space left on device/, @copyResult)){
			println ("red", "WARNING: No enough space to store package\n");
			if (scalar @localPackages > 0)
			{
				println ("yellow", "Delete some old packages (Yes/No)?\n");
				my $answer=<>;
				chomp ($answer);
				if($answer =~ /ye?s?/i){
					print 'INFO: Use "Yes/No" to confirm deletion',"\n";
					foreach my $individualPkg (@localPackages){
						system("rm -i $individualPkg");
					}
				}
			}
			println ("yellow", "INFO: Try to copy $newZipFile to $home \n\n");
			executeThisWithLogging("ls");
			copy($newZipFile, $home) or die "Copy failed: \n $home \n$!";
		}
	}else{
		print "File '$zipFile' doesn not exist!\n";
		die "Can't find file: $zipFile\n"
	}
}


sub ftpToDGServer{
	my $pwd = cwd();
	my $maxchild=1;
	for(my $i=0;$i<$maxchild;$i++)
	{
		my $child=fork();	#creat child process
		if($child){			#now parent process
			print "Connecting to atclvm559.athtem.eei.ericsson.se....\n";
			my $ftp=Net::FTP -> new ("atclvm559.athtem.eei.ericsson.se");
			print "Connected to atclvm559.athtem.eei.ericsson.se\n";
			$ftp ->login("dcuser","central1");
			print "Logged In\n";
			$ftp ->cwd("/ossrc/package");
			$ftp ->binary;
			print "Changed directory to /ossrc/package and transfer file\n";
			$ftp ->put("$pwd/target/ENIQ_EVENTS_AUTO_TESTS_$RState.zip");
			print "Sent ENIQ_EVENTS_AUTO_TESTS_$RState.zip\n";
			#$ftp->get("/ossrc/package/baselines_eniq.txt") or die "get failed ", $ftp->message;
			$ftp ->quit;
			print ("Bye\n");
			kill 9, $child;#kill child process
			chdir($pwd);	
		}else{
		progressBar($i);
		}
	}
}


#Method used to draw the progress bar as file copies to the DGserver
sub progressBar{
	local $| = 1;
	my @progress_symbol = ( '>   ', '>>  ', '>>> ', '>>>>' );
	my $n = 0;
	my 	$ftp1=Net::FTP -> new ("atclvm559.athtem.eei.ericsson.se");
	$ftp1 ->login("dcuser","central1");
	$ftp1->cwd("/ossrc/package");
	$ftp1 ->binary;
	$totalSize=(getSize()/1048576);	#get total size of the .zip  file
	print color 'bold yellow';
	while ( 1 )
		{
			my $size=getFTPSize($ftp1);
			printf ("\r $progress_symbol[$n] %-4.2f M (Total: %-4.2f M) ", $size, $totalSize);
			$n = ( $n >= 3 ) ? 0 : $n + 1;
			select( undef, undef, undef, 0.05 );
		}
	print color 'reset';
	$ftp1->quit;
	print "\n";
	local $| = 0;
}

#get the real-time size that file is transferred. Used by Progress bar
sub getFTPSize(){
	my $ftp1 = shift;
	my $file = "ENIQ_EVENTS_AUTO_TESTS_$RState.zip";
	$ftp1->binary;
	my $fileSize = $ftp1->size($file);
	$fileSize/=1048576;#change the unit to MB
	return $fileSize;
}

#get the size of file to be transferred. Used by Progress bar
sub getSize{
	my $file = "$home/ENIQ_EVENTS_AUTO_TESTS_$RState.zip";
	my $fileSize = -s $file;
	return $fileSize;
}

sub sshToCCServer{
	#perl /vobs/dm_eniq/tools/scripts/deliver_eniq -auto <stats|events> <shipment> <Reason> <unittested> <signum> <product number> <docnum(NONE)> <file> autotest
	my ($product, $shipment, $reason, $unitTested, $signums, $productNo, $docLink, $file, $productArea);
	
	$product = "events";
	$reason = "WP0000-DEFT-AUTO";
	$unitTested = "N";	
	$signums = "xprashu,xtusumb,xguppam,xrupeth";	
	$productNo = "CXC1733264";
	$docLink = "NONE";
	$file = "$CCPackageFolder/ENIQ_EVENTS_AUTO_TESTS_$RState.zip";
	$productArea="autotest";
	my $ship ;
	print "\nSSH STUFF \n";
	foreach $ship (@shipments){
		chomp($ship);
		my ($first, $second, $third) = split(/\./, $ship);
		$ship = ($first<10?($first + 10):($first)) . "." . $second ."/" . $first . "." . $second . "." . $third;

	print "\nTHE SHIP VALUE IS $ship \n";
	
		executeThisWithLogging("ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no eniqdmt\@eselivm2v210l.lmera.ericsson.se '/usr/atria/bin/cleartool setview $CCview <<EOF
			/usr/atria/bin/cleartool pwv;
			echo 'Copying from DGserver to CCserver. Might take a few minnutes:';
			cp /net/atclvm559.athtem.eei.ericsson.se/ossrc/package/ENIQ_EVENTS_AUTO_TESTS_$RState.zip $CCPackageFolder/ENIQ_EVENTS_AUTO_TESTS_$RState.zip;
			echo '';
			/usr/atria/bin/cleartool co -nc $CCPackageFolder;
			echo '';
			/usr/atria/bin/cleartool mkelem -nc $CCPackageFolder/ENIQ_EVENTS_AUTO_TESTS_$RState.zip;
			echo '';
			/usr/atria/bin/cleartool ci -c 'tester' $CCPackageFolder/ENIQ_EVENTS_AUTO_TESTS_$RState.zip;
			echo '';
			/usr/atria/bin/cleartool ci -nc $CCPackageFolder;
			echo '';
			cd $CCPackageFolder/;
			echo '';
			/usr/atria/bin/cleartool mklbtype -nc CXC1733264-$RState;
			echo '';
			/usr/atria/bin/cleartool mklabel -recurse CXC1733264-$RState ENIQ_EVENTS_AUTO_TESTS_$RState.zip;
			echo '';
			cd /home/eniqdmt;
			echo 'About to run delivery command';
			perl /vobs/dm_eniq/tools/scripts/deliver_eniq -auto \"$product\" \"$ship\" \"$reason\" \"$unitTested\" \"$signums\" \"$productNo\" \"$docLink\" \"$file\" \"$productArea\";
			echo 'Delivered';

		EOF'");
	}
}

sub chooseShipments{
	my $shipmentFile = "$pwd/baselines_eniq.txt" or die ("Couldn't open shipment file. \
							nPlease run again and manually specify the shipmet eg: -s 4.0.6");
	open( SHP, "<$shipmentFile");
	my @allShipments = <SHP>;
	chomp(@allShipments);
	@allShipments = grep(!/^#|^$/,@allShipments);
	@allShipments = grep (/ENIQ_E/i, @allShipments);
	close(SHP);
	println("Please choose a shipment:");
	for(my $i = 0; $i < @allShipments; $i++){
		println("\t".($i+1).") ".$allShipments[$i]."");
	}
	println("Enter number (multiple can be seperated using comma):");
	my $shipmentInput = <>;
	chomp($shipmentInput);
	$shipmentInput =~ s/ //g;
	my @selection = split(",", $shipmentInput);
	my @ships;
	foreach my $select (@selection){
		my ($release, $shipment) = split(/\//,$allShipments[($select - 1)]);
		push(@ships, $shipment);
	}
	return @ships;
}




	if((@ARGV)==0 ){
		#println(info());
		println("yellow", info() );
			exit(1);
	}
	
	processParameters();		
	my $toDeliver = ($deliver)? ("Yes"):("No");
	
	print color 'bold yellow';
	print qq{Confirm below info:
		RState is: 		$RState;
		Shipment Number is: 	@shipments;
		Your Signum is: 	$signum;
		Clearcase Dynamic View:	$CCview;
		Deliver:		$toDeliver;
	}; 
	print color 'reset';
	my $correct;
	if($force){
		$correct = "y";
	}else{
		println("blue", "\nIs information above correct (Yes/No?): \nYou can also go to path /ossrc/package in atclvm559.athtem.eei.ericsson.se to check the Rstate info");
		$correct = <STDIN>;
		chomp($correct);
	}
	
	if ( !($correct =~/y/i))
	{
		println("red", "INFO not correct, program quit\n");
		exit(1);
	}else{
		#println("yellow", "MAKING & APPLYING LABEL!\n");
		#dealLabel();
		println("yellow", "MAVEN!\n");
		#if(!$force){
			mvn();
		#}
		renameAndCopy();		
		if( $build ){
			println("blue", "\n\nPackage built and saved!");
		}elsif( $deliver ){
		println("yellow", "COPY TO DGServer //Getting Shipments Options\n");
		ftpToDGServer();
		chooseShipments();
		println("yellow", " COPY TO CCserver !\n");
		sshToCCServer();
		}
		
		unlink "$home/ENIQ_EVENTS_AUTO_TESTS_$RState.zip";
		
	}
	
	

	
#Helper classes-------------------------------------------------------
	
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



sub FT_ERROR {
	my ($error) = @_;
	chomp $error;
	if (open (AUDIT, ">> $error_log")) {
		my ($SS, $MM, $HH, $dd, $mm, $yy) = (localtime)[0..5];
		my ($time) = sprintf("%d-%02d-%02d %02d:%02d:%02d",$yy+1900, $mm+1, $dd, $HH, $MM, $SS);
		print AUDIT "$time : $error\n";
		close AUDIT;
	}
	else {
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
	}
	else {
		print STDOUT "$message\n";
		&FT_ERROR ("Could not open $audit_log for writing");
	}
	print STDOUT "$message\n";
}