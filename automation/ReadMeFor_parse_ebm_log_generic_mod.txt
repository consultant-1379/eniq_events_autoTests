Introduction:
------------------------------------------------

"parse_ebm_log_generic_mod.pl" is an updated version of the original SGEH Perl Parser.
It is used with the check_decoded_file_against_db.pl to check contents of event files (or directories of files) against the events loaded in the database.


------------------------------------------------
History:
------------------------------------------------
"parse_ebm_log_generic_mod.pl" is an adaptation by Seamus ("I've nothing better to do on a Saturday morning") Morton of the original Perl Parser (parse_ebm_log_generic.pl).

Changes from the original SGEH Perl Parser:
(1) Additional Header details in the output summary.
(2) Correct the issue in the original Perl Parser where it wasn't possible to get a summary of unsuccessful events.
(3) Changes the script to comply the "unsuccessful event" criteria in ENIQ Events
In EE, unsuccessful events are those whereby Event Result = 1.  In the original SGEH Perl Parser, unsuccessful events are those whereby Event Result != 0 (successful).  


------------------------------------------------
Script Usage: 
------------------------------------------------

To run parse_ebm_log_generic_mod.pl, copy this script AND the ebm.xml file into any directory on your EE server.

Updated Perl Parser help:
parse_ebm_log_generic_mod.pl -h

Command to get summary of all events:
perl parse_ebm_log_generic_mod.pl -f A20120517.2355+0800-20120517.2356+0800_1_ebs.40 -s

Command to get summary of unsuccessful events:
perl parse_ebm_log_generic_mod.pl -f A20120517.2355+0800-20120517.2356+0800_1_ebs.40 -u -s


------------------------------------------------
Sample Output of All Events Summary:
------------------------------------------------

atrcxb2338[eniq_events] {dcuser} #: perl parse_ebm_log_generic_mod.pl -f A20120517.2355+0800-20120517.2356+0800_1_ebs.40 -s

###FILE###
Input file=A20120517.2355+0800-20120517.2356+0800_1_ebs.40

###HEADER###
date=2012-05-17
time=23:55:00
FFV=4
FIV=11

###STATS###
Processing start time=Mon May 20 11:27:22 2013
Processing end time=Mon May 20 11:27:23 2013
Number of  DETACH=270
Number of  ACTIVATE=2636
Number of  SERVICE_REQUEST=49610
Number of  ATTACH=386
Number of  DEACTIVATE=1646
Number of  ISRAU=19
Number of  RAU=3478
Number of unknown event=0
Total number of events=58045
atrcxb2338[eniq_events] {dcuser} #:



------------------------------------------------
Sample Output of Unsuccessful Events Summary:
------------------------------------------------

atrcxb2338[eniq_events] {dcuser} #: perl parse_ebm_log_generic_mod.pl -f A20120517.2355+0800-20120517.2356+0800_1_ebs.40 -u -s

###FILE###
Input file=A20120517.2355+0800-20120517.2356+0800_1_ebs.40

###HEADER###
date=2012-05-17
time=23:55:00
FFV=4
FIV=11

###STATS###
Processing start time=Mon May 20 11:10:53 2013
Processing end time=Mon May 20 11:10:54 2013
Number of  ACTIVATE=1163
Number of  SERVICE_REQUEST=1549
Number of  DEACTIVATE=119
Number of  ATTACH=98
Number of  RAU=19
Number of  ISRAU=18
Number of unknown event=0
Total number of events=2966
atrcxb2338[eniq_events] {dcuser} #: