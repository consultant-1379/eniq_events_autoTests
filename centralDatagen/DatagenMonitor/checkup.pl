#!/usr/bin/perl

use strict;

my $datagenServer = "atclvm559.athtem.eei.ericsson.se";
my $user = "dcuser";
my $pass = "central1";

my $signum = $ENV{HOME};;#/home/[SIGNUM]
$signum =~ s%/home/%%;
my $userRunningScript = $signum;
my $mailFile = "/tmp/mail.txt";
my @emailList = (
	"john.j.keegan\@ericsson.com",
	"moses.jilugu\@ericsson.com",
	"patrick.garvey\@ericsson.com",
	"suraj.pearson\@ericsson.com",
	"zhao.e.li\@ericsson.com",
	"philip.newman\@ericsson.com",
	"raj.kiran\@ericsson.com",
	"cathal.ryan\@ericsson.com",
);

sub run{
	my $command = shift;
	my @output = `$command `;
	return @output;
}

sub mail{
	my @message = @_;
	open( MAIL, ">$mailFile" );
	foreach my $line (@message){
		print MAIL $line;
	}
	close( MAIL );
	
	my $mailCommand = "/usr/bin/mailx -s 'DATAGEN SERVER PROBLEM' @emailList < $mailFile";
	run($mailCommand);
	unlink($mailFile);
}

sub sendEmail{
	my  @output=@_;
	my @errors = grep(/ERROR:/, @output);
	my @notes = grep(/NOTE: /,@output);
	my @email = ("Hello,\n");
	
	if(@notes){
		foreach my $line (@notes){
			chomp($line);
			$line =~ s/\r//;
			$line =~ s/NOTE: //;
			push(@email,"\t$line");
		}
	}
	
	push( @email, "\nThere ".(@errors==1?"is 1 error":"are ".@errors." errors")." on the datagen server($datagenServer):\n\n" );
	if(@errors){
		foreach my $line (@errors){
			$line =~ s/\r//;# <- needed/important/head wreck/keep/headache!!
			push( @email, "\t$line" );
			print "\t:$line\n";
		}
		
		push( @email, "\nPlease see to issue".(@errors==1?"":"s")." as soon as possible.\nThank you" );
	}
	
	mail(@email);
}

sub monitor{
	print "Running Monitor...\n";
	my @output = run("/home/$userRunningScript/expect.exp $datagenServer $user $pass '/usr/bin/perl /eniq/home/dcuser/centralDatagen/DatagenMonitor/monitor.pl' ");
	
	#push(@output,"ERROR: Disk above 90% - /dev/md/dsk/d10         12G    11G   1.1G    91%    /");
	#push(@output,"ERROR: Disk above 90% - /usr/lib/libc/libc_hwcap1.so.1    12G    11G   1.1G    91%    /lib/libc.so.1");
	print "Monitor Result:\n @output\n";
	#If working correctly, do nothing
	if(grep( /Server is running correctly/, @output )){
		print "Datagen server($datagenServer) is working correctly\n";
	}
	#elsif(grep(/^ERROR:/, @output )&&grep(/9[0-9]\%/, @output )){
	#	$user="root";
	#	print "Rebooting ..\n";
	#	run("/home/$userRunningScript/expect.exp $datagenServer $user $pass 'reboot'");
	#	print "Sleeping ..\n";
	#	sleep(3*60);
	#	print "Running Init ..\n";
	#	run("/home/$userRunningScript/expect.exp $datagenServer $user $pass '/usr/bin/perl /eniq/home/dcuser/centralDatagen/init_datagen_server.pl'");
	#	print "Sending Email ..\n";
	#	@output=();
	#	@output = run("/home/$userRunningScript/expect.exp $datagenServer $user $pass '/usr/bin/perl /eniq/home/dcuser/centralDatagen/DatagenMonitor/monitor.pl' ");
	#	push(@output,"NOTE: File System Filling up error found. \n\tReboot Completed OK. Please See current status below \nand correct any further errors.\n\n");
	#	sendEmail(@output);
	#}
	elsif( grep( /^ERROR:/, @output )){
		sendEmail(@output);
	#Something else went wrong with this script or expect.exp or monitor.pl
	}else{
		my @email = ();
		push( @email, "Hello,\nThere is a problem with one or more of the scripts monitoring the datagen server($datagenServer).\n\n" );
		push( @email, "Please check the following scripts:\n" );
		push( @email, "\t/home/$userRunningScript/expect.exp,\n" );
		push( @email, "\t/home/$userRunningScript/checkup.pl, and\n" );
		push( @email, "\t/eniq/home/dcuser/centralDatagen/DatagenMonitor/monitor.pl on datagen server.\n" );
		push( @email, "Crontab should look like:\n" );
		push( @email, "\t0 0,6,12,18 * * * /home/$userRunningScript/checkup.pl >/dev/null 2>&1\n" );
		push( @email, "Output:\n" );
		foreach my $val (@output){
			$val =~ s/\r//;
			push( @email, "\t:$val" );
		}
		push( @email, "\nPlease see to issue as soon as possible.\nThank you" );
		
		print "Sending Email to @emailList\n";
		mail(@email);
	}
}

{#main
	monitor();
}
