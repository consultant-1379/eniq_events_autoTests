#!/usr/bin/perl -CO

use strict;
use warnings;

sub getDwhdb{
	my $dwhdb = `cat /etc/hosts | grep dwhdb`;
	$dwhdb =~ s/dwhdb//;
	writeToFile($dwhdb);
}

sub writeToFile{
	my $arg = shift;
	open (MYFILE, '>/package/dwhdbInUse.txt');
	print MYFILE $arg;
	close (MYFILE); 
	#print $arg;
	
	
}

sub main{
	getDwhdb();
}

main();
