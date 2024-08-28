 #!/usr/local/bin/perl
 
#This batch file runs a local Selenium RC on a port after 4444, i.e. if port 4444
#It also updates the local selenium.properties file to reflect the new port by changing the line 'server.port = xxxx' to 'server.port  = 4445' (or whatever port it finds to be available).
#Ensure that selenium.properties is writable; an error will be displayed if it's not

use Cwd;
use warnings;
use strict;

my $pwd = cwd();
my($line);
my($currentPort);
my($seleniumPropertiesTargetPath, $seleniumPropertiesResourcePath, $propertyReaderPath, $seleniumJarFile );
my($defaultFireFProfile);

#checkSnapshotView();
setFilePaths(); 
checkPropReader(); 
selectPort();
createTempProp();
tempToResourcePath();
tempToTargetPath();
cleanUp();
setFireFoxProfile();


sub checkSnapshotView{	 #Check Run from a snapshot directory
	my $driveLetter = substr($pwd, 0, 1); #returns the first character of pwd	
	if ($driveLetter ne "E"){
		print "This batch file should be used from a SNAPSHOT VIEW\n";
		print "Are you running from a SNAPSHOT VIEW? (Y/N)? ";
		chomp(my $resp = <>);
		if ((uc($resp) eq "Y") || (uc($resp) eq "YES")){
			print "Continuing\n\n";} 
		elsif ((uc($resp) eq "N")||(uc($resp) eq "NO")){
			die "EXITING: Not a snapshot view\nTherefore will not run\n\n\n\n";
		} 
		else{
			print "Invalid Response: $resp\n Please enter\n'Y' or 'N'\n\n\n\n"; checkSnapshotView();
		}
	}
}
	
sub UpDirectory{		 #Goes up a folder in the director path.
		
	my @args = @_;
	my $Current = $args[0];
	my $Ups = $args[1];
	while ($Ups > 0) {
		$Current =~ s/\/\w*$//;
		$Ups--;
	}
	return $Current;
}	
	
sub setFilePaths{		 #Sets file paths for sel.prop / PropRead.jar / stanalone-*
		
	##Get selenium.properties target Path
	my $BaseDir = UpDirectory($pwd, 3);
	$seleniumPropertiesTargetPath = "$BaseDir"."/target/classes/resources/selenium.properties";
	unless (-e $seleniumPropertiesTargetPath){ 
		die "File  /target/classes/resources/selenium.properties Doesn't Exist!\n";
	}
		
	##Get selenium.properties Resource Path
	$seleniumPropertiesResourcePath = "$BaseDir"."/src/main/resources/selenium.properties";
	##$seleniumPropertiesResourcePath = $pwd.'/selenium.properties';
	unless (-e $seleniumPropertiesResourcePath){
		die "File src/main/resources/selenium.properties Doesn't Exist!\n";
	}
			
	##Get selenium-server-standalone
	my $JarPath =  UpDirectory($pwd, 3);
	my $JarName;		
	my $seleniumJarPath = "$JarPath"."/../selenium/selenium-grid-1.0.8/vendor/";		#/../../ used because UpDir wont let me go higher than Up 3
	opendir ( DIR, $seleniumJarPath ) || die "Error in opening $seleniumJarPath\n";
	my $JarCount = 0;
	while(my $filename = readdir(DIR)){
		if($filename =~ /selenium-server-standalone/){
			$JarName = $filename;
			$JarCount++;
		}
	}
	if ($JarCount > 1){
		print "Multiple selenium-server-standalone jars\nShould only have one!\nRunning $JarName\n\n";
	}else{
		$JarCount = 0;
	}
	closedir(DIR);
	$seleniumJarFile = "$seleniumJarPath"."$JarName";
	unless (-e $seleniumJarFile){ 
		print "File /jars/selenium/selenium-server/selenium-server-standalone* Doesn't Exist!\n";
	}

	#Get PropertyReader.java
	$BaseDir = UpDirectory($pwd, 3);
	$propertyReaderPath = "$BaseDir"."/src/main/java/com/ericsson/eniq/events/ui/selenium/common/PropertyReader.java";
	unless (-e $propertyReaderPath){
		print "File $propertyReaderPath Doesn't Exist!\n";
	}
}

sub checkPropReader{	 #Check that the PropertyReader.java is pointing to correct System.prop
	open (PropertyFile , "$propertyReaderPath") or die " Property Reader File not found" ;
	my $driveLetter = substr($pwd, 0, 1);

	while(<PropertyFile>){
		my $line = $_;
		my $commented = "//";
		if(($line =~ /String resourcePathInVobs/) &&($line !~ /$commented/ )){	
			if ((index($line , getlogin() ne -1))||							#path contains username		
				(index($line , "\\target\\classes\\resources" != -1))||		#Path contains directory
				(index($line , "$driveLetter:" != -1))){}						#Path contains Drive letter				 
			else{
				die "PropertyReader.java at location $propertyReaderPath \nis not set to use the correct selenium.properties file at location: $seleniumPropertiesTargetPath\n\n";			
			}
		}
	}
	close PropertyFile;
}	
	
sub selectPort{			 #Chooses a random port to open
	system 'netstat -an > ports.txt ';
	$currentPort = int( rand(156)) + 4444;
	open PortFile , "ports.txt" or die "Temp File ports.txt not found" ;
	my @FilePorts = <PortFile>;	
	close(PortFile);
	my $portMatch = 1;

	while($portMatch != 75){
		if((grep(/:$currentPort\s/, @FilePorts) > 0)){
			$currentPort = int( rand(156)) + 4444; 
			$portMatch++;
		}else{
			last;
		} 
	}
	if($portMatch==75){
		die "Could not find a free port\n";
	}
	print"---------------------------------\n";
	print"Using Port: $currentPort\n";
	print"---------------------------------\n";
}

sub createTempProp{	 	 #Create Temporary selenium.property file.
	open(TempProp,'>tempProp.txt') or die "Can't create tempProp.txt: $!";
	open (Propfile , "$seleniumPropertiesResourcePath") or die " File $seleniumPropertiesResourcePath not found" ;
		
	while(<Propfile>){
		$line = $_;
		if($line =~ /server.port =/){	
			print TempProp "server.port = $currentPort\n";
		}
		else{
			print TempProp "$line";
		}
	}
	close TempProp;
	close Propfile;
}	
	
sub tempToResourcePath{	 #Copys the tempProp to Resource Sel.prop / checks
	open TempProp , "tempProp.txt" or die " File tempProp.txt not found";
	open ResourcePath , "+>$seleniumPropertiesResourcePath" or die " File $seleniumPropertiesResourcePath not found" ;

	while(<TempProp>){
		$line = $_;
		print ResourcePath "$line" or die "Copy failed: Is file checkedout?";			
	}
	close TempProp;
		
	my @CheckPort = <ResourcePath>;
	if((grep(/server.port = $currentPort/, @CheckPort) > 0)){
		die "Copy Failed in src/main/resources/selenium.properties: Was file checked out!?";
	}
	close ResourcePath;
}

sub tempToTargetPath{	 #Copys the tempProp to target Sel.prop / checks
	open TempProp , "tempProp.txt" or die " File not found";
	open TargetPath , "+>$seleniumPropertiesTargetPath" or die " File not found" ;
	while(<TempProp>){
		$line = $_;
		print TargetPath "$line" or die "Copy failed: Is file checkedout?";			
	}
	close TempProp;
	
	my @PortCheck = <TargetPath>;
	if(grep(/server.port = $currentPort/, @PortCheck) > 0){
		die "\n Copy Failed to /target/classes/resources/selenium.properties: Was file checked out!?";
	}
	close TargetPath;
}

sub setFireFoxProfile{	#Sets the default firefox Profile to launch.	
	my $user = getlogin();
	my $profilePath="C:/Users/$user/AppData/Roaming/Mozilla/Firefox/Profiles";
	opendir ( Profiles, $profilePath ) || die "Error in opening Firefox Profile Dir\n";
	my $ProfileName = 0;
	while(my $filename = readdir(Profiles))
	{
		if($filename =~ /default/){
			$defaultFireFProfile = "$profilePath"."/$filename";
			$ProfileName++;}
	}			
	if($ProfileName > 1){
		print "\nWARNING: Multiple firefox default profiles, Choosing one...\n";
	}		
}

sub cleanUp{			 #deletes Temp Files created
	if (unlink("tempProp.txt") == 0) {
		print "File tempProp.txt NOT deleted.\n";
	}
				
	if (unlink("ports.txt") == 0) {
		print "File ports.txt NOT deleted.\n";
	}
}

system ("java -jar $seleniumJarFile -port $currentPort -firefoxProfileTemplate $defaultFireFProfile -browserTimeout 6000");