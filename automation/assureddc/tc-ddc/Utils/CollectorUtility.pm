package Utils::CollectorUtility;
###############################################################################
# This contains the Utility functions for the Generic collector.
###############################################################################

use File::Copy qw(copy);
use Exporter 'import';

# Variables
our $componentConfLocation = "/eniq/sw/conf/assureddc_conf/";
our $dummyXML = "/eniq/sw/conf/assureddc_conf/dummy.xml";
if ( -d "/eniq/log/ddc_data" ) {
  our $ddcLocation = "/eniq/log/ddc_data";
}
else {
  our $ddcLocation = "/var/tmp/ddc_data";
}
our $CAT = "/usr/bin/cat";
our $RM = "/usr/bin/rm";
our $componentDataDir = `find $ddcLocation/ -name plugin_data`;
chomp($componentDataDir);

# Export Variable
our @EXPORT = qw (
  CreateXMLFile
  CheckTempFile
  $ddcLocation
  RemoveComponentDir
  CheckLogCollection
  CheckLogCollectionDir
);

# This creates the dummy component XML file.
sub CreateXMLFile{
   open (my $file, '>', $dummyXML);
   print $file "<?xml version='1.0'?>\n";
   print $file "<INSTR>\n";
   print $file "   <Component>\n";
   print $file "       <ComponentName>Test</ComponentName>\n";
   print $file "       <Location>/eniq/log/testassureddc</Location>\n";
   print $file "       <Filename>test.log</Filename>\n";
   print $file "    </Component>\n";
   print $file "</INSTR>";
   close $file;
}

# This checks f the Dummy file was read and Test entry is avaiable in temp
# config file
sub CheckTempFile{
  `$CAT ${ddcLocation}/tempConfigFile  | grep "Test:"`;
  if ( $? eq 0 ) {
      return 0;
  }
  return 1;
}

# This Creates the temp config file incase if start fails to do so.
sub CreateTempFile{
  if ( CheckTempFile() eq 0 ) {
      open (my $file, '>', $ddcLocation/tempConfigFile);
      print $file "Test:/eniq/log/testassureddc/test.log:false:false";
      close $file;
  }
}

# This removes the Component dir to check that it is re-created 
sub RemoveComponentDir{
  if( -d "$componentDataDir/Test" ) {
    `$RM -fr $componentDataDir/Test`;
  }
}

# This checks if log collection dir exists
sub CheckLogCollectionDir{
  if( -d "$componentDataDir/Test" ) {
    return 0;
  }
  return 1;
}

# This checks if instrumentation log is collected 
sub CheckLogCollection{
  if( -f "$componentDataDir/Test/test.log" ) {
    return 0;
  }
  return 1;
}
1;
