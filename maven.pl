#!/usr/bin/perl

sub executeThis
{
	my $command = shift;
	open(CMD,"$command |");
	while(<CMD>)
	{
		print $_;
	}
	close(CMD);
}

		
#######MAIN
{
	open(PROPS,"<./eniq-events-selenium/src/main/resources/selenium.properties");
	my @contents=<PROPS>;
	close(PROPS);
	executeThis("mvn clean");
	if($? eq 0)
	{
	executeThis("mvn compiler:compile");
	}
	if($? eq 0)
	{
	executeThis("mvn compiler:testCompile");
	}
	if($? eq 0)
	{
	executeThis("mvn resources:resources");
	}
	if($? eq 0)
	{
	executeThis("mvn dependency:copy-dependencies");
	}
	if($? eq 0)
	{
	executeThis("mvn jar:jar");
	}
	if($? eq 0)
	{
	executeThis("mvn assembly:single");
	}
}
