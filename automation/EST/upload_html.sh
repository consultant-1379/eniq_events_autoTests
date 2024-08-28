#!/usr/bin/perl -C0

use Net::Ping;
use Net::FTP;
use Time::Local;
use Sys::Hostname;

my $resultsServer = "atdl785esxvm8.athtem.eei.ericsson.se";
my $resultsPath = "/html/events/results";
my $resultsUser = "deftftauto";
my $resultsPass = 'CentralRegression!!';
my $ftp;

# print "SERVER : $resultsServer\nPATH : $resultsPath\nUSER : $resultsUser\nPASS : $resultsPass\n";
my $host_tmp= hostname;
my $ftpLogin;
my $resultsPathNew;

my @html_path = `ls /eniq/home/dcuser/automation/EST/html/*.html`;
print "HTML FILES : @html_path\n";

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

chomp($host_tmp);
if ( $host_tmp eq "eniqe" ){
	$resultsPathNew =  $resultsPath.'/eniqe_'.getHostName();
}
else{
	$resultsPathNew =  $resultsPath.'/'.getHostName();
}

my $ping = Net::Ping->new();
if($ping->ping($resultsServer)){
	# print "Ftp Server is alive.\n";
	$ftpLogin = 1;
}else{
	# print "Ftp Server is not alive";
	$ftpLogin = 0;
}

$ping->close();

# print "FTP LOGIN : $ftpLogin\n";

if($ftpLogin == 1){
	$ftp=Net::FTP->new($resultsServer,Timeout=>240) or $ftpLogin = 0;
	$ftp->login($resultsUser, $resultsPass) or $ftpLogin = 0;
}

if( $ftpLogin ){
	# print "INFO:FTP login is correct!\n";
}else{
	# print "INFO:FTP login failed. Results will not be uploaded!\n";
	exit 1;
}

$ftp->mkdir($resultsPathNew);
$ftp->cwd($resultsPathNew);
foreach my $html_file (@html_path){
	# $html_file = `basename $html_file`;
	chomp $html_file;
	$ftp->put($html_file);
	$html_file = `basename $html_file`;
	print "\nFull PATH : http://$resultsServer$resultsPathNew/$html_file\n";
}
$ftp->quit;
