#!/usr/bin/perl 
use strict;

my $pkgLocation = "/package";
my $storageLog = "$pkgLocation/log.txt";
my $packageNum;
my $packageNumToKeep = 3;
my $packageNumToDelete;

sub checkPackageList{
	my @packageList = sort (`ls ${pkgLocation}/ENIQ_EVENTS_AUTO_TESTS_R*`);
	my $logTime = `date`;
	$packageNum = scalar @packageList;
	
	open (LOG, ">>$storageLog") or die "Unable to open log file";
		print LOG "\n[INFO] $logTime";
	
	if ($packageNum >$packageNumToKeep){
		$packageNumToDelete = $packageNum-$packageNumToKeep;
		print LOG "[INFO] Now delete $packageNumToDelete old packages and keep the latest $packageNumToKeep\n";
		my @deletingPackage = @packageList[0..($packageNumToDelete-1)];
		print LOG "[INFO] Deleting:\n@deletingPackage";
		foreach my $pkg (@deletingPackage){
			chomp($pkg);
			unlink ($pkg) or die "can not delete $!";
		}
		
	}else{
		print LOG "[INFO] $packageNum packages in folder, continue monitoring..\n";
	}
	close (LOG);
}


{
	checkPackageList();
}
