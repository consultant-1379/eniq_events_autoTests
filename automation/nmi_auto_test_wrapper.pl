#!/usr/bin/perl
sub executeThisWithLogging
{
	my @cmd;

	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>)
	{
		print $_."\n";
		push(@cmd, $_);
	}
	close(CMD);
	return @cmd;
}
my $command="cd /eniq/home/dcuser/automation;bash ./wrapper.sh";
foreach $arg(@ARGV){
	$command.=" $arg";
}
print $command."\n";
executeThisWithLogging("echo \"$command >/dev/null 2>&1\" | at now");
