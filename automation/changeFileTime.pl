#!/usr/bin/perl -C0

use File::Basename;
use strict;
use POSIX qw(strftime);
	my @files = ` ls A20150312* `;
	my $local_file;
	my $hour = strftime "%H", gmtime;
	my $minute = strftime "%M", gmtime;
	my $end_min=sprintf("00");
	my $start_min=$end_min-05;
	my $newTime="$hour$start_min-$hour$end_min";
	my $previous_hour=$hour-01;

	if($minute >= 00 && $minute <=05){
		$end_min=sprintf("00");
		$start_min=sprintf("55");
		$newTime = "$previous_hour$start_min-$hour$end_min";
	}
	if($minute > 05 && $minute <=10){
		$end_min=sprintf("05");
	}
	if($minute > 10 && $minute <=15){
		$end_min=sprintf("10");
    }
	if($minute > 15 && $minute <=20){
		$end_min=sprintf("15");
    }
	if($minute > 20 && $minute <=25){
		$end_min=sprintf("20");     
	}
	if($minute > 25 && $minute <=30){
		$end_min=sprintf("25");
	}
	if($minute > 30 && $minute <=35){
		$end_min=sprintf("30");
    }
	if($minute > 35 && $minute <=40){
		$end_min=sprintf("35");
    }
	if($minute > 40 && $minute <=45){
		$end_min=sprintf("40");     
	}
	if($minute > 45 && $minute <=50){
		$end_min=sprintf("45");
	}
	if($minute > 50 && $minute <=55){
		$end_min=sprintf("50");
    }
	if($minute > 55 && $minute <60){
		$end_min=sprintf("55");
    }
	
	$start_min=$end_min-05;
	$newTime = "$hour$start_min-$hour$end_min";
	
	print "START_MIN : $start_min\n ;; END_MIN : $end_min\n";

    chomp($newTime);
	print "NEW TIME : $newTime\n";
	foreach my $file (@files){
    chomp $file;
    print("Loading File $file");
    my $str = "$file";
    my @field1 = split /_/, $str;
    my $firstpart = $field1[0];
    my @field2 = split /\./, $firstpart;
    my $filetime = $field2[1];
    $local_file = `echo $file | sed s/$filetime/$newTime/g`;
    print "\nLOC FILE : $local_file\n";
    system ("mv $file $local_file");

}