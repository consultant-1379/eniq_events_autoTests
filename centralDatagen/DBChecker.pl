#!/usr/bin/perl

use strict;
use Net::Ping;
use IO::Socket;

my $resultString;
my @servers = (#preferred servers
"atrcxb1361",
"atrcxb1842",
"atrcxb1732",
"atrcxb1726",
"atrcxb1304",
"atrcxb1361",
"atrcxb1926"
);

sub executeThisQuiet
{
	my $command = shift;
	my @cmd=();
	open(CMD,"$command |");
	while(<CMD>)
	{
		print $_;
		push(@cmd, $_);
	}
	close(CMD);
	return @cmd;
}

#takes in atrcxb1926 and returns 159.107.173.12
sub hostToIP{
	my $hostname=shift; 
	my($addr)=inet_ntoa((gethostbyname($hostname))[4]);
	return "$addr";
}

sub checkServer{
	my $host=shift;
	if(ping($host)){
		###See sample output at the bottom of the file to know how to set $resultString
		#$resultString="77.*9.*756.*3.*0.*1.*2.*3.*255.*00.*77";
		#$resultString="10.*9.*767.*3.*0.*1.*2.*3.*255.*6.*00.*77"; # new pattern
		my $command="/eniq/home/dcuser/centralDatagen/ssh_check.sh $host dcuser dcuser";
		my @res=executeThisQuiet($command);
		my $result = join("",@res);
		$result=~s/\r\n//g;
		$result =~ m/:\s+(\w*)\s+(\w*)\s+(\w*)\s+(\w*)\s\(\w+.*?\)\s+(\w*)\s+(\w*)\s+(\w*)\s+(\w*)\s+(\w*)\s\(\w+.*?\)\s(\w+)\s+(\w+)\s+(\w+)/;
		if (($1>=9)&($2>=9)&($3>750)&($4>=3)&($5==0)&($6==1)&($7==2)&($8==3)&($9>=255)&($10>5)&($11==00)&($12>=06)){
			return 1;
		}
		if(grep(/Offending key in/,@res)){
			my @arr=grep(/Offending key in/,@res);
			my $known_hosts=$arr[0];
			my $known_hostsLineNum=$arr[0];
			$known_hosts=~s/Offending key in (.*):.*/\1/;
			$known_hostsLineNum=~s/Offending key in .*:(.*)/\1/;
			chomp($known_hostsLineNum);
			chomp($known_hosts);
			open(HOSTS,"$known_hosts");
			my @contents=<HOSTS>;
			close(HOSTS);
			open(HOSTS,">$known_hosts");
			for(my $i=0;$i<@contents;$i++){
				if($i!=$known_hostsLineNum-1){
					print HOSTS $contents[$i];
				}
			}
			close(HOSTS);
			my @res=executeThisQuiet($command);
			my $result = join("",@res);
			$result=~s/\r\n//g;
			$result =~ m/:\s+(\w*)\s+(\w*)\s+(\w*)\s+(\w*)\s\(\w+.*?\)\s+(\w*)\s+(\w*)\s+(\w*)\s+(\w*)\s+(\w*)\s\(\w+.*?\)\s(\w+)\s+(\w+)\s+(\w+)/;
			if (($1>=9)&($2>=9)&($3>750)&($4>=3)&($5==0)&($6==1)&($7==2)&($8==3)&($9>=255)&($10>5)&($11==00)&($12>=06)){
				return 1;
			}
		}
	}
	return 0;
}

#reads current server folders and adds to @servers to check and/or use for dwhdb
sub serversFromDir{
	my $serverDir = "/tmp/CentralDatagen/";
	opendir( DIR, $serverDir);
	my @dirs = readdir(DIR);
	closedir(DIR);
	foreach my $dir (@dirs){
		if(grep(!/$dir|^\./, @servers)){
			push(@servers, $dir);
		}
	}
}

sub ping{#returns 1 if alive
	my $server = shift;
	my $p = Net::Ping->new();
	my $toReturn = 0;
	if($p->ping($server)){
		$toReturn = 1;
	}
	$p->close();
	return $toReturn;
}

sub updateFile{
	my $server = shift;
	my $ipAddress = hostToIP($server);
	open(HOSTS,"/etc/hosts");
	my @contents=<HOSTS>;
	close(HOSTS);
	if(grep(/dwhdb/,@contents)){
		open(HOSTS,">/tmp/hosts");
		foreach (@contents){
			s/.*dwhdb/$ipAddress $server dwhdb/;
			print HOSTS;
		}
		close(HOSTS);
	}else{
		push(@contents,"$ipAddress $server dwhdb\n");
		open(HOSTS,">/tmp/hosts");
		print HOSTS @contents;
		close(HOSTS);
	}
	open( TMP, "</tmp/hosts");
	@contents = <TMP>;
	close(TMP);
	if(grep(/dwhdb/,@contents)){
		executeThisQuiet("/eniq/home/dcuser/centralDatagen/RunCommandAsRoot.sh cat /tmp/hosts \\> /etc/hosts");
		unlink("/tmp/hosts");
	}
}

#MAIN
#&serversFromDir(); #Commenting this out to use only the servers we know in above list
if($ARGV[0] ne ""){
	my $server = $ARGV[0];
	print "INFO: Using argument: $server\n";
	if(checkServer($server)){
		print "INFO: $server is OK... Updating /etc/hosts\n";
		updateFile($server);
	}else{
		print "INFO: $server is not a suitable server...\n";
	}
	print "Exiting\n";
	exit(0);
}


if(!checkServer("dwhdb")){
	foreach my $server(@servers){
		if(checkServer($server)){
			updateFile($server);
			last;
		}else{
			print "INFO: $server is not a suitable server...\n";
		}
	}
}else{
	print "INFO: Current Server(".hostToIP("dwhdb").") is grand!!\n";
}

########Sample output from ssh_checker.sh. Possibly out of date but it shows how to set $resultString in sub checkServer. The numbers in the output should match the numbers in $resultString in the right order
#Currently $resultString="77.*9.*756.*3.*0.*1.*2.*3.*255.*00.*77";
#
#spawn ssh dcuser@dwhdb source ~/.profile;/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 -Ddwhdb -Sdwhdb -w 50 -b <<EOF
#select count(distinct hierarchy_3) from dim_e_sgeh_hier321 union all
#select count(distinct hierarchy_3) from dim_z_sgeh_hier321 union all
#select count(distinct hierarchy_3) from dim_e_lte_hier321 union all
#select count(distinct sgsn_name) from dim_e_sgeh_sgsn_d
#select CALL_POSITION from DIM_E_MSS_CALL_POSITION
#select distinct mnc from dim_e_sgeh_hier321
#go
#quit
#EOF
#
#Password:
#                                             77
#                                              9
#                                            756
#                                              3
#
#(4 rows affected)
#             0
#             1
#             2
#             3
#           255
#
#(5 rows affected)
# 00
# 77
#
#(2 rows affected)
#INFO: Current Server(10.45.192.117) is grand!!
