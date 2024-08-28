package Utils::hqmon_Utils;
use XML::Parser;
use Data::Dumper;

require Exporter;
@ISA = qw(Exporter);
@EXPORT = qw(getResourceId getResponseStatus getMetricList);

sub getResponseStatus
{
    my $xmlfile = "/tmp/resourceXML";
    my $result = `cat /etc/hosts|grep controlzone`;
    if(my ($hostname) = $result =~ /\d*\.\d*\.\d*\.\d*  (.*)  controlzone/){
        $result=`cat $xmlfile|grep $hostname`;
        if( $? ) {
            print STDERR "\nERROR hostname not found\n";
            return (1, "ERROR hostname not found");
        }
        else{
            return (0, "N.A");
        }
    }
}

sub getResourceId
{
    my $result = `cat /etc/hosts|grep controlzone`;
    my $xmlfile = "/tmp/resourceXML";
    my $resourceContent = `cat /tmp/resourceXML`;
    if(my ($hostname) = $result =~ /\d*\.\d*\.\d*\.\d*  (.*)  controlzone/){
        if ( my ($resourceId) = $resourceContent =~ /Resource id=\"(.*)\" name=\"$hostname/){
            return $resourceId;
        }
    }
}

sub getMetricList
{
    my @metricList = @_;
    my $status = 0;
    foreach (@metricList)
    {
        chomp($_);
        $result = `cat /tmp/metricXML|grep "$_"`;
        if($?) {
            print STDERR "\nERROR: Metric $_ missing\n";
            return (1, "ERROR: Metric $_ missing");
        }
        else {
            return (0, "N.A");
        }
    }
}
