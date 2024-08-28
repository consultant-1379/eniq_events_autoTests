#!/usr/bin/perl -w

use FindBin;
use lib "$FindBin::Bin";

use XML::Parser;
use Data::Dumper;
use Utils::hqmon_Utils;
use Utils::logger;
use File::Copy;

our $resourceId;
our $failureReason = "N.A.";
our $agentToServerRedirect = "/opt/hyperic-agent/ericsson/bin/setAgentToServerLayer.bash";
our $hqapiRootLocation = "/opt/hyperic-agent/hqapi1-client-5.0.0";
our $pluginJarLocation = "/opt/hyperic-agent/agent-4.6.6.1-EE/bundles/agent-4.6.6.1/pdk/plugins";
our $acceptanceTestDir = "$FindBin::Bin";
our $snapValid;

# The below array is for the 2 basic acceptance criterion of a plugin
# The discovery and the metrics list.
# Add any new plugin in the "PROTOTYPE => METRICS IN UI" format.
# The framework will take care of the rest.

our %pluginsToTest = (
   "EE Mediation-SGEH" => [ "2G/3G Success Events per second",
                            "2G/3G Error Events per second",
                            "4G Success Events per second",
                            "4G Error Events per second",
                            "Availability",
                            "Total Events per Second",
                            "Files per Hour",
                            "Volume per Hour"
                           ],

   "Rolling Snapshot" => [ "Time since last rolling snapshot",
                           "Availability"
                         ],

   "EE Mediation-LTEEFA" => [ "Availability",
                              "CFA Events per Second",
                              "HFA Events per Second",
                              "Total Events per Second",
                              "Files per Hour",
                              "Volume per Hour"
                            ],

   "EE Mediation-LTEES" => [ "Availability",
                             "Counter Files Generated per Hour",
                             "Events per Second",
                             "Files Consumed per Hour",
                             "Volume per Hour"
                           ]

);

sub main
{
    setup();
    InitLog();

    foreach ( keys %pluginsToTest ) {
      if ( $_ =~ m/Rolling Snapshot/ ) {
          $snapValid = checkRollSnapValidity();
          if ( $snapValid == 0 ) {
             runTestCases($_ , @{$pluginsToTest{$_}});
          }
      }
      else {
         runTestCases($_ , @{$pluginsToTest{$_}});
      }
    }

    EndLog();
    cleanup();
}

sub cleanup {
    unlink("/tmp/resourceXML");
    unlink("/tmp/metricXML");
    unlink("$hqapiRootLocation/conf/client.properties");
}

sub redirectAgentToHost {
    my $ossMtServerIp;
    open SOURCE, "$acceptanceTestDir/config/client.properties" or die "ERROR: Unable to open file $acceptanceTestDir/config/client.properties: $!";
    foreach my $line (<SOURCE>) {
       if ($line =~ /host=(\d*\.\d*\.\d*\.\d*)/) {
          $ossMtServerIp = $1;
       }
    }
    close(SOURCE);
    chomp($ossMtServerIp);

    system "$agentToServerRedirect $ossMtServerIp";
    if ( $? ) {
       print STDERR "\nERROR in redirecting the hyperic-agent to the hyperic-server at IP $ossMtServerIp.";
       exit 1;
    }
}

sub waitForAutoApproval {
    my $waitForSeconds = 300;

    # grab whole file and set autoflush.
    # Autoflush is needed to print something when using sleep.
    undef local $/;
    local $| =  1;

    while ($waitForSeconds--) {
       print ".";
       sleep(1);
    }
    print "\n";
}


sub copyClientPropertiesFile {
   copy("$acceptanceTestDir/config/client.properties" , "$hqapiRootLocation/conf/client.properties") or die "Copy failed: $!";
}

sub setup {
   cleanup();
   redirectAgentToHost();
   waitForAutoApproval();
   copyClientPropertiesFile();
}

sub getValue{
    my($command) = @_;
    my $result = `$command`;
    if( $? ) {
        print STDERR "\nERROR in fetching Hyperic Server Information\n";
        exit 1;
    }

    if(my($Value) = $result =~ /.*=(.*)/)
    {
        return $Value;
    }
}

sub runTestCases{
    my $prototype = shift;
    my @metric_list = @_;

    print "\n######################## TEST CASES FOR $prototype plugin ###########\n";

    #TC 1: Check if the plugin is is discovered
    assertUtils(testPluginDiscovery("$prototype"),"Plugin Discovery check for $prototype","testPluginDiscovery",$failureReason);

    #TC 2: Correct metric are monitored
    assertUtils(testMetricList($prototype, @metric_list),"Metric list are correct for $prototype","testMetricList",$failureReason);

    print "######################### All Test Cases for $prototype are executed ##################\n";
}

sub testPluginDiscovery{
    my $plugin_prototype = shift;
    chomp($plugin_prototype);
    my $response = `$hqapiRootLocation/bin/hqapi.sh resource list --prototype="$plugin_prototype" >/tmp/resourceXML`;
    if( $? ) {
        print STDERR "\nERROR in fetching resource information for $plugin_prototype\n";
        $failureReason = "ERROR in fetching resource information for $plugin_prototype";
        return 1;
    }

    ($response , $failureReason) = getResponseStatus();
    return $response;
}

sub testMetricList{
    my $plugin_prototype = shift;
    my @plugin_metric_list = @_;

    my $resourceId = getResourceId();
    my $response = `$hqapiRootLocation/bin/hqapi.sh metric list --id=$resourceId --enabled > /tmp/metricXML`;
    print "$hqapiRootLocation/bin/hqapi.sh metric list --id=$resourceId --enabled > /tmp/metricXML\n";
    if( $? ) {
        print STDERR "\nERROR in fetching metric information for $plugin_prototype\n";
        $failureReason = "ERROR in fetching metric information for $plugin_prototype";
        return 1;
    }

    ($response , $failureReason) = getMetricList(@plugin_metric_list);
    return $response;
}

sub assertUtils
{
    my ($output,$testCaseName,$testTag,$failureReason) = @_;
    if($output eq 0){
        LogInfo("TestName: \"$testCaseName\" TestTag: \"$testTag\" TestResult: \"PASS\"  FailureReason: \"N.A.\"");
        print "TestName: \"$testCaseName\" TestTag: \"$testTag\" TestResult: \"PASS\"  FailureReason: \"N.A.\"\n";
    }
    else{
        LogInfo("TestName: \"$testCaseName\" TestTag: \"$testTag\" TestResult: \"FAIL\"  FailureReason: \"$failureReason\"");
        print "TestName: \"$testCaseName\" TestTag: \"$testTag\" TestResult: \"FAIL\"  FailureReason: \"$failureReason\"\n";
    }
}

sub checkRollSnapValidity
{
    my $CheckFile = "/eniq/local_logs/rolling_snapshot_logs/prep_roll_snap.log";
    if( -f $CheckFile ) {
       return 0;
    }
    else {
       return 1;
    }
}

main();
