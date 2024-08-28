#!/usr/bin/perl
## The sciprt is used to calculate the highest file size in the northbound directory and to display the cache file.
##The jira's are EQEV-29992 and EQEV-29993

use List::Util qw/max/;
use List::Util qw/sum/;

## To calculate the highest hourly and day file size

my $datentime = `date`;
print $datentime;
my $dateEstStart = $ARGV[0];
my $timeEstStart = $ARGV[1];
$dateEstStart =~ s/-//g;
$timeEstStart =~ s/://g;
my $DateTimeEST = $dateEstStart.$timeEstStart;
system "mkdir -p /eniq/home/dcuser/automation/EST/FileSize/";
my $outputResult = "/eniq/home/dcuser/automation/EST/FileSize/hourly_highest_$DateTimeEST.txt";

if( -f "$outputResult"){
	print "The file for current EST exist. We will append the result";
}
else{
	print "The file does not exists.New file will created";
}
`echo "The hourly highest file are:" >> $outputResult`;
system "ls -lrt /eniq/northbound/lte_event_stat_file/events_oss_1/dir* | egrep -v 'DR_TMP_DIR|\/eniq\/northbound\/lte_event_stat_file\/events_oss_1\/dir'>> /tmp/all_xml_file.txt";

for( my $i=0; $i <= 23; $i++){
	my $j=0;
	if($i < 10){
		$m = $j.$i;
	}
	else{
		$m=$i;
	}
	system "grep $m: /tmp/all_xml_file.txt > /tmp/hourly_file_$m";
	my @file_size_array = `cat /tmp/hourly_file_$m | cut -d ' ' -f14`;
	my $highestvalue = max @file_size_array;
	chomp($highestvalue);
	my $highest_xml_file = `cat /tmp/hourly_file_$m | grep -iw "$highestvalue" >> $outputResult`;
}

print "You can find the hourly highest file values in $outputResult\n";
my @highest_day_value = `cat $outputResult | cut -d ' ' -f14`;
my $highestdayvalue = max @highest_day_value;
chomp($highestdayvalue);
my $highest_day_xml = `cat $outputResult | grep -iw "$highestdayvalue"`;
`echo "\nThe highest file size value is: \n$highest_day_xml" >> $outputResult`;


#### For Calculating the sum of the cache files

system "ls -lrt /eniq/mediation_inter/ltees/cache/topology/ossrc* |egrep -vi 'total|\/eniq\/mediation_inter\/ltees\/cache\/topology\/ossrc' >> /tmp/all_ossrc_file.txt";
`echo "\n\nThe cache files are:" >> $outputResult`;
system "cat /tmp/all_ossrc_file.txt | grep -vi total >> $outputResult";
my @ossrctopologyfile = `cat /tmp/all_ossrc_file.txt | cut -d ' ' -f11`;
my $sumValue = sum @ossrctopologyfile;
`echo "\nThe sum value is:\n$sumValue \n\n" >> $outputResult`;

system "rm -rf /tmp/xml_file.txt /tmp/all_xml_file.txt /tmp/hourly_file_* /tmp/size_of_file* /tmp/all_ossrc_file.txt";
