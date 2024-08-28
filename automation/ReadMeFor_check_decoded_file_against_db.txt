Introduction:
------------------------------------------------

check_decoded_file_against_db.pl is used in conjunction with "parse_ebm_log_generic_mod.pl" (an updated version of the original ETH SGEH Perl Parser) to ensure that 4G SGEH events contained in SGEH ebm files are stored in the ENIQ Events database. 

------------------------------------------------
History:
------------------------------------------------

check_decoded_file_against_db.pl was developed by Seamus ("I've nothing better to do on a Saturday morning") Morton.


------------------------------------------------
Script Usage: 
------------------------------------------------

 ./check_decoded_file_against_db.pl -f <input file>|-d <input directory> [-n <SGSN/MME NAME]

Note: "-n <SGSN/MME NAME" should be used if the SGEH file comes straight from the node => file will not have the node name in the file name so node name must be given as an argument in order to facilitate query against DB.


------------------------------------------------
How it works - single file usage:
------------------------------------------------

Syntax: 
./check_decoded_file_against_db.pl -f /path/<file_name>

The check_decoded_file_against_db.pl uses parse_ebm_log_generic_mod.pl to produce summary of results for the the file e.g. header information, event stats.  It then checks those event stats against the event_e_lte_raw table for the timestamp on the file. 

There are various built-in checks in the check_decoded_file_against_db.pl script e.g. 
- if the file is young (e.g. 5 min), it is not checked since there is no guarantee that the events will be in the database.
- if the file is < 1 hour old, then only unsuccessful event counts are checked (since there is a one-hour delay in loading successful events to the db)

Note that script can be run against all files in a directory (-d option).

------------------------------------------------
