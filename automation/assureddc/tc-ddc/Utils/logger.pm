package Utils::logger;

# Modules

use strict;
use File::Basename;
use Data::Dumper;
use POSIX qw(strftime);
use Time::HiRes qw/gettimeofday/;
use Exporter 'import';

# Variables

# Command line variables.
use vars qw($opt_debug);

# Script performance measurement variables.
use vars qw($seconds $start_ms $end_ms);

# Log Variables
our $logdir = "log/";
our $logname = basename($0);
our $time = strftime "%F_%X",localtime;
our $logfile = $logdir."assure_ddc_logger_$time.log";

# Export Variable
our @EXPORT = qw (
   InitLog
   EndLog
   RotateLogs
   LogMsg
   LogInfo
   LogError
   $opt_debug
);

# Log Utilities

sub InitLog {
   #in scalar context it returns a fractional number of seconds
   $seconds = gettimeofday();

   #convert into milliseconds
   $start_ms = int($seconds*1000);
   unless (-d $logdir) {
      mkdir $logdir;
   }

   RotateLogs();

   unless (-f $logfile) {
       open LOGFILE, ">", $logfile or die $!;
       close LOGFILE;
   }

}

sub EndLog {
   #convert into milliseconds
   $seconds = gettimeofday();
   $end_ms  = int($seconds*1000);

   my $total_exe_time = ($end_ms - $start_ms);

   open LOGFILE, ">>", $logfile or die $!;
   close LOGFILE;
}

sub RotateLogs {
    my $no_of_logs = 10;

    for(my $y = $no_of_logs - 1; $y >= 1; $y--)
    {
        my $x = $y - 1;
        rename "$logfile.$x", "$logfile.$y" if (-f "$logfile.$x");
    }
    rename $logfile, "$logfile.0" if (-f $logfile);
}

sub LogMsg {
    my @msgs = @_;

    if (defined $opt_debug) {
       print "@msgs"."\n";
    }

    open(LOG, ">>$logfile") || die "Cannot open logfile $logfile: $!";
    print LOG "@msgs"."\n";
    close(LOG);

}

sub LogInfo {
    my ($first, @msgs) = @_;
    my @output = ();
    push @output,     "INFO: $first";
    push @output, map "      $_", @msgs;
    chomp @output;
    @output = map "$_", @output;
    LogMsg(@output);
}

sub LogError {
    my ($first, @msgs) = @_;
    my @output = ();
    push @output,     "ERROR: $first";
    push @output, map "      $_", @msgs;
    chomp @output;
    @output = map "$_\n", @output;
    LogMsg(@output);
}
1;
