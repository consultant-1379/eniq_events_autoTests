#!/usr/bin/perl

use strict;

my $datagenServer = "atclvm559.athtem.eei.ericsson.se";
my $user = "dcuser";
my $pass = "central1";

my $signum = $ENV{HOME};;#/home/[SIGNUM]
	$signum =~ s%/home/%%;
my $userRunningScript = $signum;
my $mailFile = "/tmp/mail.txt";
my @emailList = ("damien.o.sullivan\@ericsson.com",
	"john.j.keegan\@ericsson.com",
	"moses.jilugu\@ericsson.com",
	"patrick.garvey\@ericsson.com",
	"seamus.morton\@ericsson.com",
	"suraj.pearson\@ericsson.com",
	"yao.e.zhang\@ericsson.com",
	"zhao.e.li\@ericsson.com",
);


sub run{
	my $command = shift;
	my @output = `$command `;
	return @output;
}


sub monitor{
	print "Running command: /home/$userRunningScript/expect.pl $datagenServer $user $pass '/usr/bin/perl /eniq/home/dcuser/centralDatagen/monitor.pl'\nCan take up to 90 seconds...\n";
	my @output = run("/home/$userRunningScript/expect.pl $datagenServer $user $pass '/usr/bin/perl /eniq/home/dcuser/centralDatagen/monitor.pl' ");
	
	#If working correctly, do nothing
	if( grep( /Server is running correctly/, @output )){
		print "Datagen server($datagenServer) is working correctly\n";
	#If contains ERROR: then there are errors, report	
	}elsif( grep( /^ERROR:/, @output )){
		
		my @errors = grep(/ERROR:/, @output);
		print "There ".(@errors==1?"is 1 error":"are ".@errors." errors")."\n";
		my @email = ();
		push( @email, "Hello,\nThere ".(@errors==1?"is 1 error":"are ".@errors." errors")." on the datagen server($datagenServer):\n\n" );
		print "Output:\n";
		foreach my $line (@errors){
			$line =~ s/\r//;# <- needed/important/head wreck/keep/headache!!
			push( @email, "\t$line" );
			print "\t:$line\n";
		}
		push( @email, "\nPlease see to issue".(@errors==1?"":"s")." as soon as possible.\nThank you" );
		print "Sending Email to @emailList\n";
		mail(@email);
	#Something else went wrong with this script or expect.pl or monitor.pl
	}else{
		print "Other Error\n";
		print "Output:\n\t-------------------------------\n";
		foreach my $val (@output){
			print "\t:$val";
		}
		print "\t-------------------------------\n";
		my @email = ();
		push( @email, "Hello,\nThere is a problem with one or more of the scripts monitoring the datagen server($datagenServer).\n\n" );
		push( @email, "Please check the following scripts:\n" );
		push( @email, "\t/home/$userRunningScript/expect.exp,\n" );
		push( @email, "\t/home/$userRunningScript/checkup.pl, and\n" );
		push( @email, "\t/eniq/home/dcuser/centralDatagen/monitor.pl on datagen server.\n" );
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

{#main
	monitor();
}