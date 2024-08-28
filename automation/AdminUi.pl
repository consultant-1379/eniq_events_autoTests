#!/usr/bin/perl 
#-------------------------------------------------------------
#AdminUi.pl script must be run by root. This script tests the 
#Admin User Interface for JIRA ENC-2667.
#The server used for testing should be on load 7.0.4 or later.
#-------------------------------------------------------------
#-------------------------------------------------------------

my $run_date = &getRunDate();
my $hostname = getHostName();
my $AdminPath = "/eniq/home/dcuser/automation/Admin_UI";
my $glassfishIP = `cat /etc/hosts | grep glassfish | cut -d ' ' -f3`;
chomp($glassfishIP);

#### Functions ####


sub getRunDate{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst)=localtime(time);
	return sprintf "%4d%02d%02d%02d%02d", $year+1900,$mon+1,$mday,$hour,$min;
}


#Admin UI testing by selenium
sub AdminUiTest{
	opendir ( JAVADIR, "/eniq/sw/runtime/" ) || print "/eniq/sw/runtime/ not found\n";
	if(!-e "$AdminPath/AdminUI-0.0.1-SNAPSHOT-jar-with-dependencies.jar"){
		print "$AdminPath/AdminUI-0.0.1-SNAPSHOT-jar-with-dependencies.jar file does not exist\n";
		exit(1);
	}
	while(my $JavaJDK = readdir(JAVADIR)){
		if($JavaJDK =~ /jdk1.7.0_95/){
			print "/eniq/sw/runtime/$JavaJDK/bin/java -jar $AdminPath/AdminUI-0.0.1-SNAPSHOT-jar-with-dependencies.jar $AdminPath/Test.properties > $AdminPath/AdminUITest_$run_date.txt \n\n";
			executeThisWithLogging("/eniq/sw/runtime/$JavaJDK/bin/java -jar $AdminPath/AdminUI-0.0.1-SNAPSHOT-jar-with-dependencies.jar $AdminPath/Test.properties > $AdminPath/AdminUITest_$run_date.txt");
			last; 
		}elsif($JavaJDK =~ /jdk/){
			print "/eniq/sw/runtime/$JavaJDK/bin/java -jar $AdminPath/AdminUI-0.0.1-SNAPSHOT-jar-with-dependencies.jar $AdminPath/Test.properties > $AdminPath/AdminUITest_$run_date.txt \n\n";
			executeThisWithLogging("/eniq/sw/runtime/$JavaJDK/bin/java -jar $AdminPath/AdminUI-0.0.1-SNAPSHOT-jar-with-dependencies.jar $AdminPath/Test.properties > $AdminPath/AdminUITest_$run_date.txt");
			last;
		}
	}
}

##### Updating hostname in Test.properties file ####
sub UpdatePropertiesFile{
	executeThisWithLogging("perl -pi -e 's#browser.admin.host =.*#browser.admin.host = https\:\/\/$hostname.athtem.eei.ericsson.se#g' $AdminPath/Test.properties");
	executeThisWithLogging("perl -pi -e 's#browser.events.host =.*#browser.events.host = https\:\/\/$glassfishIP.athtem.eei.ericsson.se#g' $AdminPath/Test.properties");
	executeThisWithLogging("perl -pi -e 's/^db.host =.*/db.host = $hostname.athtem.eei.ericsson.se/g' $AdminPath/Test.properties");
	executeThisWithLogging("perl -pi -e 's/repdb.host =.*/repdb.host = $hostname.athtem.eei.ericsson.se/g' $AdminPath/Test.properties");
}

sub executeThisWithLogging{
	my @cmd;
	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>){
		push(@cmd, $_);
	}
	close(CMD);
	return @cmd;
}

sub getHostName{
	open(HOST,"hostname |");
	my @host=<HOST>;
	close(HOST);
	chomp(@host);
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

###### Main Script #####
{
	my $username = $ENV{LOGNAME} || $ENV{USER} || getpwuid($<);
	if($username ne "root"){
		print "ERROR:This script must be run as root user\n";
		exit(1);
	}
	print "---------Testing ADMIN UI---------\n";
	UpdatePropertiesFile();	
	AdminUiTest();
	system("/eniq/home/dcuser/automation/RunCommandAsDcuser.sh /eniq/home/dcuser/automation/EniqEventsRegress.sh /eniq/home/dcuser/automation/priority/Config_AdminUI.txt");
}
