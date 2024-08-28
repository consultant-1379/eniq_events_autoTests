#!/usr/bin/perl
my $lineToAdd = "";
open my $in,  '<',  "edeInstall.sh"      or die "Can't read file: $!";
open my $out, '>', "edeInstall_temp.sh"      or die "Can't read file: $!";

                while( <$in> )
                 {

                     $lineToAdd = $_;
                     if ( $_ =~ m/Enter the external IP address of/ ) {
                             $lineToAdd="MASTER_SERVER_IP\=\"127.0.0.1\"\n";
                       }

                     if ( $_ =~ m/Do you have any other server to install/ ) {
                              $lineToAdd = "choice\=no\n" ;
                         }

                    print $out " $lineToAdd";

                 }


                close $in;
                close $out;

          system("cp  edeInstall_temp.sh edeInstall.sh");