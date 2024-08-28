#!/usr/bin/perl
use strick;
use warnings;
use File::Copy;


my $backup_folder = "/eniq/backup/natural_earth_map/";
unless(mkdir $backup_folder) {
		print "Unable to create $directory\n";
	}
my $maps = "/eniq/home/dcuser/automation/19089-CXC1735692_Ux_A.tar.gz natural_earth_raster_map_R1A05.tar.gz"
chmod 755,$maps;
copy("$maps",$backup_folder) or print "\nCopy failed: $!";


use Archive::Tar;

my $tar=Archive::Tar->new();

$tar->read('/path/of/tar/test.tar.gz');
$tar->extract();