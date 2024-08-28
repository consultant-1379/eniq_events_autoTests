#!/usr/bin/perl

#
# File		: parse_ebm_log.pl 
# Purpose	: Parse EBM logs for troubleshooting purposes.
# updated by	: EWENHAN + EENZCHA
# Date		: 2011-01-20
#

require 5.6.1;
use Getopt::Std;
use strict;
use POSIX;
use File::Basename;
use Cwd;
use Symbol;

my $file = "./ebm.xml";
my $cause_xml = "./ebm_cause_codes.xml";

my $OUT = Symbol::gensym();;
$OUT =  \*STDOUT;
local *FH;
my($TTX_OS_HW)=`/tmp/DPE_SC/Scripts/dpe_uname -D`;chomp($TTX_OS_HW);
my($CP,$TAIL)=("cp","tail");
my $prog=basename $0;
my $ebs_log_binary_DIR = "/tmp/OMS_LOGS/ebs/ready";
my $ebs_log_binary;
my $ebs_file_inarg; 
my $total_content;

##Current EBM record
my $this_record_content_BIT;
my $this_record_content_BIT_index;
my $this_record_length; 
my $this_record_length_actual;
my $this_record_content;
my $event_record;
my %event_record;

##Arguments
my %opts;
my $rat_opts;
my $event_type_opts;
my $summary = "false";

##Header
my ($FFV,$FIV,$year,$month,$day,$hour,$minute,$second);

##Format
my $delimiter = "\n";
my $pr_all=0;

##Counters
my $nbr_unknown_event = 0;
my $total_nbr_events = 0;

##Filters
my @filters;
my %filters;
my $gsm = "false";
my $wcdma = "false";
my $unsuccessful = "false";
my $imsi = "false";
my $imsi_pattern = "";
my $tac ="false";
my $tac_pattern = "";
my $cause_code = "false";
my $cause_code_pattern = "";
my $apn = "false";
my $apn_pattern = "";
my $decode_all = "true";

## hash tables for xml parse
## event_id => attend_flag
my %event_list_tab = ();
## event_name => counter_value
my %event_counter_tab = ();
## cause code
my %cause_code = ();
my $ie_types = {};
my $tmp_types = {};
my $enums = {};
my $all_events = {};
my $all_ies = {};
my $event_str = "";

## decode function for each IE type
my %decode_types = (
			'uint' => \&decode_uint,
			'enum' => \&decode_enum,
			'bytearray' => \&decode_bytearray,
			'ipaddress' => \&decode_ipaddress,
			'ipaddressv4' => \&decode_ipaddressv4,
			'ipaddressv6' => \&decode_ipaddressv6,			
			'dnsname' => \&decode_dnsname,
			'tbcd' => \&decode_tbcd,
			'ibcd' => \&decode_ibcd);
			
#	element type (hash, parameter and structure): ie_types
#	{	name,		// string
#		isLeaf,		// 1-true, 0-false
#		type,		// get from xml, atom or structure
#		len,			// atom type length
#		level,		// structure level
#		useValid,		// true|false
#		optional,		// true|false
#		seqLen,		// seqmaxlen
#		childNum,	// length of child array
#		childArray,	// child array of element hash
#	}
######  element type example         #####
#	{ elem1 } = {	name => elem1,
#				isLeaf => 0,
#				childNum => 2,
#				childArray => [	{elem2},		// {elem2} = {....}
#								{elem3} ]
#			     }
#
#   events	(hash, id is the key): all_events
#	{	name,		// string
#		type,		// as key to get detail from element type hash
#		level,		// structure level
#		useValid,		// true|false
#		optional,		// true|false
#		seqLen,		// seqmaxlen
#	}

my ($wId, $sec);
my @working= qw(- \ | /);

#####################################################
sub print_caution {
	my $sep = "\t" . '!' x 60 . "\n\n";
	print $OUT $sep;
	print $OUT "\tCAUTION!\tCAUTION!\tCAUTION!\tCATION!\n\n";
	print $OUT "\t$prog is not intended to be ran on the SGSN-MME node, it might impact the capacity of the SGSN-MME.\n";
	print $OUT "\tRunning this command may cause heavy CPU load, depending on the size of the input file.\n";
	print $OUT "\tPlease install the script on another UNIX machine, where EBM logs are collected.\n";
	print $OUT $sep; 
}

#####################################################
sub usage {
	my $sep = "\t" . '-' x 50 . "\n\n";
	my $pr = 'user@host > ';
	print_caution;
	print $OUT "Usage: \n";
	print $OUT "\n\t$prog {-h| -s [-d directory] [-f logfile] [-o outfile] [-l]| [-p delimiter] [-u] [-r gsm|wcdma] [-e 'att*',...] ";
	print $OUT "[-c cause_code{,cause_code,...}] [-i imsi{,imsi,...} ][-t tac{,tac,...}] [-a {apn,apn,...}] [-d directory] [-f logfile] [-o outfile] [-l]}  \n";
	print $OUT "\n\tThis program can be used to parse EBM logs. The files ebm.xml and ebm_cause_codes.xml should be ";
	print $OUT "put under the same dircetory with this parser script if it's not running on the node.\n";
	print $OUT "\n\t-h: Help.\n";
	print $OUT "\n\t-s: Summary mode, prints the number of found events in log files.\n";
	print $OUT "\n\t-p: Print event records in delimiter separated columns.\n";
	print $OUT "\n\t-u: Filter on unsuccessful events.\n";
	print $OUT "\n\t-r: Filter on rat type.\n";
	print $OUT "\n\t-e: Filter on event type. * can be used to match any character, and the last asterisk can be obmitted.\n";		
	print $OUT "\n\t-c: Filter on cause codes.\n";
	print $OUT "\n\t-i: Filter on imsi numbers.\n";
	print $OUT "\n\t-t: Filter on TACs. TAC=Type Approval Code, first eight digits in IMEI.\n";
	print $OUT "\n\t-a: Filter on APNs.\n";
	print $OUT "\n\t-l: Print as table. If missing option -p,then the default delimiter will be used\n";
	print $OUT "\n\t-f: Specify logfile (with absolute path) to parse, otherwise $prog tries to parse :\n";
	print $OUT "\tall ebm log files in current working directory - if $prog runs outside SGSN-MME node or \n";
	print $OUT "\tall logs located in the $ebs_log_binary_DIR - if the script runs on the SGSN-MME node.\n";
	print $OUT "\n\t-d: Specify log directory - overwrite the default log directory value.\n";
	print $OUT "\n\t-o: Redirect output into file outfile. Please note that it is not possible to use  \n";
	print $OUT "\tUNIX command \">\" for redirecting of output on the SGSN-MME node. Use -o instead.\n";
	print $OUT $sep;
	print $OUT "\tExamples:\n";
	print $OUT $sep;
	print $OUT "\tFilter on unsuccessful events\n";
	print $OUT "\t$pr$prog -u -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tFilter on attach event and all events named with act\n";
	print $OUT "\t$pr$prog -e 'att,*act' -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tFilter on L_DEDICATED_BEARER_ACTIVATE event\n";
	print $OUT "\t$pr$prog -e 'l_d*_a' -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";	
	print $OUT $sep;
	print $OUT "\tFilter on attach, activation and rau events for GSM\n";
	print $OUT "\t$pr$prog -r gsm -e att,rau  -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tFilter on cause code #9 for israu for WCDMA\n";
	print $OUT "\t$pr$prog -r wcdma -e israu -c 9 -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tFilter on the IMSI numbers 240999800416785 240999802602314\n";
	print $OUT "\t$pr$prog -i 240999800416785,240999802602314 -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tFilter on the tac 123456\n";
	print $OUT "\t$pr$prog -t 12345678 -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tFilter on activations on the apn apn2sgsn350-2.ericsson.com.mnc021.mcc123.gprs\n";
	print $OUT "\t$pr$prog -e act -a apn2sgsn350-2.ericsson.com.mnc021.mcc123.gprs -f /tmp/A20081119.0541+0100-20081119.0542+0100_1_ebs.28\n\n";
	print $OUT $sep;
	print $OUT "\tPrint values in table format. Use delimiter '|'\n";
	print $OUT "\t$pr$prog -l -p '|' \n\n";
	print $OUT $sep;
	print $OUT "\tEBM summary for all logs located in the current directory - Note: The script runs on another UNIX machine\n";
	print $OUT "\t$pr$prog -s \n\n";
	print $OUT $sep;
	print $OUT "\tEBM summary for all logs located in the directory /tmp/ebs_logs.\n";
	print $OUT "\t$pr$prog -s -d /tmp/ebs_logs\n\n";
	print $OUT $sep;
	print $OUT "\tPrint this help message\n";
	print $OUT "\t$pr$prog -h \n\n";
	print $OUT $sep;
	exit 0;
}

#####################################################
sub logg {
	my ($msg_type,$msg_txt)=@_;
	my ($sec,$min,$hour,$day,$mon,$year)=localtime;
	my $date=sprintf("%d-%02d-%02d %02d:%02d:%02d",$year+1900,$mon+1,$day,$hour,$min,$sec);
	local $_=$msg_type;

	SWITCH: {
		/^info/		&& do
			{
				print $OUT  ( "$date\tINFO:\t$msg_txt\n");
			};
		/^warn/	&& do
			{
				print $OUT  ("$date\tWARNING:\t$msg_txt\n");
			};
		/^error/	&& do
			{
				print $OUT ( "$date\tERROR:\t$msg_txt\n");
				exit 1;
			};
		/^output/		&& do
			{
				print $OUT  ( "$date\tINFO:\t$msg_txt\n");
			};
		/^nolog_error/	&& do
			{
				print("\tERROR:\t$msg_txt\n");
				exit 1;
			};
		/^no_format/	&& do
			{
				print("$msg_txt");
			};
	}
}

#####################################################
sub modify_opt {
	my ($pattern)=@_;
	my $ret=" ";
	my @arr=split(/\s*\,\s*|\s+/,$pattern);
	foreach my $e (@arr) {
		$ret .= $e . " ";
	}
	return $ret;
}

#####################################################
# It's used to rebuild the regular expression (change * to .*).
#####################################################
sub build_event_list {
	my ($event) = @_;
	my $matched = 0;
	$event =~  s/\*/\.\*/g;	

	foreach my $key (keys %{$all_events}){
		if ( $all_events->{$key}{name} =~ /^$event/i){
			$event_list_tab{$key} = 1;
			$matched = 1;
		}
	}

	if ($matched == 0){
		print("\nError: Event type option \"$event\" can't match any event defined in ebm.xml. Defined events:\n");
		foreach my $key (keys %{$all_events}){
			print "\t$all_events->{$key}{name}\n";
		}
		print(" \nNote: * can be used as wildcard, last asterisk can be obmitted. Not case-sensitive.\n");
		print("Example: \"$prog -e l_d*_a\" to filter event \"L_DEDICATED_BEARER_ACTIVATE\".\n\n");
		exit 0;
	}
}

#####################################################
# It's used to rebuild the regular expression (change * to .*).
#####################################################
sub get_opts {
	if (exists $opts{'u'}) {
		$unsuccessful="true";
	} 
	if (exists $opts{s}) {
		$summary="true";
	} else 
	{
		if (exists $opts{'l'}) {
			$pr_all= 1;
			$delimiter=';';
		}
		if (exists $opts{'p'}) {
			if(length($opts{'p'})>0){
				$delimiter=$opts{'p'}; 
			}else{
				$delimiter=';';
			}
		} 
		if (exists $opts{'r'}) {
			$rat_opts=['gsm','wcdma'];
			if ($opts{'r'}) {
				$opts{'r'}=~ s/^\s+//;
				my @arr=split(/\s*\,\s*|\s+/,$opts{'r'});
				foreach my $e (@arr) {
					if (not (grep(/^$e$/i,@$rat_opts))) {
						print("Error: Radio access type option: $e. Radio access type option can be one of gsm and wcdma. Exiting...\n");
						exit 0;
					} elsif ( $e eq "gsm") {
						$gsm = "true";
					} elsif ( $e eq "wcdma") {
						$wcdma = "true";
					}
				}
			}
		}
		if (exists $opts{'e'}) {
			$decode_all = "false";
			if ($opts{'e'}) {
				$opts{'e'}=~ s/^\s+//;
				my @arr=split(/\s*\,\s*|\s+/,$opts{'e'});
				foreach my $e (@arr) 	{
					&build_event_list($e);
				}
			}
		}
		if (exists $opts{'c'}) {
			if ($opts{'c'}) {
				$cause_code = "true";
				$cause_code_pattern = modify_opt($opts{'c'});
			}
		}
		if (exists $opts{'i'}) {
			if ($opts{'i'}) {
				$imsi = "true";
				$imsi_pattern = modify_opt($opts{'i'});
			}
		}
		if (exists $opts{'t'}) {
			if ($opts{'t'}) {
				$tac = "true";
				$tac_pattern  = modify_opt($opts{'t'});
			}
		}

		if (exists $opts{'a'}) {
			if ($opts{'a'}) {
				$apn = "true";
				$apn_pattern = modify_opt($opts{'a'});
			}
		}
	}

	if (exists $opts{'f'}) {
		$ebs_file_inarg = $opts{'f'};
	}
	&set_default_dir;
}

#####################################################
# Redirect the result into a file.
#####################################################
sub redirect {
	if ($opts{o}=~/\w+/) {
		if (! (open(FH,">> $opts{o}"))) {
			die ( "Failed to open log file $opts{o} - $!");
		}
		print $OUT "The output will be redirected into file: " .  $opts{o} . "\n";
		print $OUT "Please be patient! Parsing can take a while, depending on the size of input files!\n";
		$OUT=\*FH;
		print $OUT "# Created by $prog\n";
		#close(STDERR); #when redirecting the result, ERROR message still should be printed out.
	} else {
		$OUT=\*STDOUT;
	}
}

#####################################################
sub arraydir{
	my $dir = shift;
	opendir my $dh, $dir or logg ('warn',"can't opendir $dir - $!");;
	my @arr = ();
	while( my $file = readdir($dh) ) {
		next if $file =~ m[^\.{1,2}$];
		my $path = $dir .'/' . $file;
		push(@arr, $file) if -d $path;
	}
	return @arr;
}

#####################################################
sub set_default_dir {
	if (exists $opts{'d'}) {
		$ebs_log_binary_DIR = $opts{'d'};
	}else{
		my $snode=&get_sgsn_node;
		if($snode){
			$ebs_log_binary_DIR="/tmp/OMS_LOGS/ebs/ready";
		}else{
			$ebs_log_binary_DIR=cwd() ;
		}
	}
}

#####################################################
# Parse Cause Code and Sub Cause Code defined in ebm_cause_codes.xml.
#####################################################
sub parse_cause
{
	my ($parseing_ptype) = 0;
	my ($parseing_el) = 0;
	my ($cause_name) = 0;
	my ($tcause,$tval) ;
	if ( -r $cause_xml ){
		open(IN, "< $cause_xml") or logg('error',"Failed to open ebm_cause_codes.xml: $cause_xml - $!");
	}else {
		$cause_xml = "/tmp/DPE_SC/ApplicationData/GSN/ebm_cause_codes.xml";
		open(IN, "< $cause_xml") or logg('error',"Failed to open ebm_cause_codes.xml in current directory and node directory '/tmp/DPE_SC/ApplicationData/GSN/' - $!");
	}

	while (<IN>) {
		if ( /\<parametertype\>/) {
			$parseing_ptype = 1;
		}elsif ( /\<\/parametertype\>/) {
			$parseing_ptype = 0;
		}
		if ($parseing_ptype == 1){
			if (/<name\>(\w*)_CODE\<\/name\>/ ) {				
				$cause_name = lc($1);
			}elsif ( /\<enumeration\>/ ) {
				$parseing_el = 1;
			}elsif ( /\<\/enumeration\>/) {
				$parseing_el = 0;
			}	
			if($parseing_el == 1){
				if (/internal="(.*)".*value="(.*)".*\>(.*)\<\/enum/ ) {					
					$tcause = lc($1);
					$tval = $2;
					$tcause =~ s/_/ /g;
					$cause_code{$cause_name}{$tval} = "#".$tval."(".$tcause.")";    	
				}
			}
		}
	}
	close(IN);
}

#####################################################
# Parse IE types (parameter types).
#####################################################
sub add_child
{
	my ($type,$level) = @_;
	if(exists $ie_types->{$type}){
		for my $i ( 0 .. $#{$ie_types->{$type}}) {
			my $elem = $ie_types->{$type}->[$i];
		 	if ($elem->{isLeaf} == 0){
		 		$level += 1;
		 		$elem->{childArray} = add_child($elem->{type},$level);
		 		$elem->{childNum} = $#{$elem->{childArray}};
		 	}
		}
		return $ie_types->{$type};
	}
	return undef;
}

#####################################################
# Parse all IE types (parameter and structure types).
#####################################################
sub parse_all_ie_types
{
	my ($key,$i,$elem);
	foreach $key (keys %{$tmp_types}) {	
		push(@{$ie_types->{$key}},
			{
				isLeaf	=>	1,
				type		=>	$tmp_types->{$key}{type},
				len		=>	$tmp_types->{$key}{len},
			}
		);
	}	
	
	foreach $key (keys %{$ie_types}) {
		for $i ( 0 .. $#{$ie_types->{$key}}) {
			$elem = $ie_types->{$key}->[$i];
		 	if ($elem->{isLeaf} == 0){				 		
		 		$elem->{childArray} = add_child($elem->{type},1);
		 		$elem->{childNum} = $#{$elem->{childArray}} + 1;
		 	}
		}
	}

}

#####################################################
# Parse all IE types and events (parameter and structure types and events).
#####################################################
sub parse_ebm_xml
{
	my ($xmlfile)=@_;
	my ($parseing_el) = 0;
	my ($parseing_ptype) = 0;
	my ($parseing_struct) = 0;
	my ($parseing_event) = 0;
	my ($name,$id,$el_name,$isLeaf,$type,$len,$level,$useValid,$optional,$seqLen,$childNum,$childArray)= ("",-1,"",0,"",0,0,undef,undef,undef,0,undef);
	
	if ( -r $xmlfile ){
		open(IN, "< $xmlfile") or logg('error',"Failed to open ebm.xml: $xmlfile - $!");
	}else {
		$xmlfile = "/tmp/DPE_SC/ApplicationData/GSN/ebm.xml"; 
		open(IN, "< $xmlfile") or logg('error',"Failed to open ebm.xml in current directory and node directory '/tmp/DPE_SC/ApplicationData/GSN/' - $!");
	}

	while (<IN>) {
		if ( /\<parametertypes\>/) {
			$parseing_ptype = 1;
		}elsif ( /\<structuretypes\>/) {
			$parseing_struct = 1;
		}elsif ( /\<events\>/) {
			$parseing_event = 1;
		}elsif ( /\<\/parametertypes\>/) {
			$parseing_ptype = 0;
		}elsif ( /\<\/structuretypes\>/) {
			parse_all_ie_types;
			$parseing_struct = 0;
		}elsif ( /\<\/events\>/) {
			$parseing_event = 0;	
		}
		
		if ($parseing_ptype == 1){
			if ( /\<name\>(.*)\<\/name\>/ ) {
				$name = lc($1);
			}elsif ( /\<type\>(.*)\<\/type\>/ ) {
				$type = lc($1);
			}elsif ( /\<numberofbits\>(.*)\<\/numberofbits\>/) {
				$len = $1;	
			}elsif ( /\<lengthbits\>(.*)\<\/lengthbits\>/) {
				$len = $1;	
			}elsif ( /\<\/parametertype\>/) {
				$tmp_types->{$name}{type} = $type;
                     	$tmp_types->{$name}{len} = $len;
			}elsif ( /internal="(.*)" value="(\d+)"\>(.*)\<\/enum\>/) {		
				$enums->{$name}->{$2} = lc ($1);				
			}			
		}

		if ($parseing_struct == 1){
			$name = lc($1) if ( /\<name\>(.*)\<\/name\>/ );
			$id = $1  if ( /\<id\>(.*)\<\/id\>/ );
			$el_name = lc($1) if ( /"\>(.*)\<\// );
			$optional = lc($1) if ( /optional="(.*?)"/ );
			$useValid = lc($1) if ( /usevalid="(.*?)"/ );
			$seqLen = lc($1) if ( /seqmaxlen="(.*?)"/ );
			$type = lc($1) if ( /type="(.*?)"/ );
			if ( /type="(.*?)"/ ) {	
				push(@{$ie_types->{$name}},
						{
							name	=>	$el_name,
							isLeaf	=>	0,
							type		=>	$type,
							useValid	=>	$useValid,
							optional	=>	$optional,
							seqLen	=>	$seqLen,
							childArray=>	$childArray
						}
					);
				
				($el_name,$isLeaf,$type,$len,$level,$useValid,$optional,$seqLen,$childNum,$childArray)= ("",0,"",0,0,undef,undef,undef,0,undef);				
			}	
		}

		if ($parseing_event == 1){
                     $name = uc($1) if ( /\<name\>(.*)\<\/name\>/ );                     
                     $el_name = lc($1) if ( /"\>(.*)\<\// );
			$optional = lc($1) if ( /optional="(.*?)"/ );
			$useValid = lc($1) if ( /usevalid="(.*?)"/ );
			$seqLen = lc($1) if ( /seqmaxlen="(.*?)"/ );
			$type = lc($1) if ( /type="(.*?)"/ );
			if ( /\<id\>(.*)\<\/id\>/ ){
				$id = $1;
				$all_events->{$id}{name} = $name;			
			}
			if ( /type="(.*?)"/ ) {				
				if (exists $ie_types->{$type} ) {
					push(@{$all_events->{$id}{ies}},
							{
								name	=>	$el_name,
								type		=>	$type,
								type_d	=>	$ie_types->{$type},
								useValid	=>	$useValid,
								optional	=>	$optional,
								seqLen	=>	$seqLen
							}
						);
					if (! $el_name){
						$all_ies->{$type} = "";
					}else{
						$all_ies->{$el_name} = "";
					}
					
					($el_name,$type,$level,$useValid,$optional,$seqLen)= ("","",0,undef,undef,undef);				
				}else{
					print "ERROR\t$type is not defined!\n";
				}
			}			
		}		
	}
	close(IN);
}	

#####################################################
# Parse binary log files.
#####################################################
sub process_ebs_log_files
{
	my @ebs_log_binaries;
	my $start_time;
	my $end_time;
	my @ebs_log_dirs=();
	my @ebs_pdirs=();
	if( defined( $ebs_file_inarg ) ){
		@ebs_log_binaries = ( $ebs_file_inarg );
	}else{ 
		my $ebs_files=qx(ls -1 ${ebs_log_binary_DIR}/A*ebs.* 2>/dev/null);
		chomp($ebs_files);
		if($ebs_files =~/\w+/) {
		@ebs_log_binaries=split('\n',$ebs_files);
		}else{
			#We have logs from different nodes.
			@ebs_log_dirs=&arraydir(${ebs_log_binary_DIR});

			foreach my $ebs_dir (@ebs_log_dirs)
			{
				my $ebs_node_files=qx(ls -1 ${ebs_log_binary_DIR}/${ebs_dir}/A*ebs.* 2>/dev/null);
				chomp($ebs_node_files);
				push(@ebs_log_binaries,split('\n',$ebs_node_files));
			}
		}
		#@ebs_log_binaries = glob( $ebs_log_binary_DIR . "A*ebs.*" );
	}

	foreach $ebs_log_binary (@ebs_log_binaries){
		$total_nbr_events = 0;
		my $new_dir = basename(dirname($ebs_log_binary));
		if(grep(/^$new_dir$/,@ebs_log_dirs)) {
			if(not(grep(/^$new_dir$/,@ebs_pdirs))) {
				print $OUT "###DIRECTORY###\n";
				print $OUT "Current Directory=$new_dir\n\n";
				push(@ebs_pdirs,$new_dir);
			}
		}
		print $OUT "\n###FILE###\n";
		print $OUT "Input file=$ebs_log_binary\n";
		$start_time = localtime();

		open( EBS, $ebs_log_binary ) or die ( "Couldn't open $ebs_log_binary=$!" );
		binmode EBS;
		undef $/;
		$total_content = unpack("H*",<EBS>);
		close EBS;
		$/ = "\n";

		if( length( $total_content ) == 0 )	{
			print $OUT "$prog: Input log file is empty\n"; 
		}

		if ( $summary eq "false" ){
			&parse_content();
		}else{
			&parse_content_header();
			print $OUT "\n###HEADER###\n";
			print $OUT "date=$year-$month-$day\n";
			print $OUT "time=$hour:$minute:$second\n";
			print $OUT "FFV=$FFV\n";
			print $OUT "FIV=$FIV\n\n"; 
			&parse_content_summary();
		}
		$end_time = localtime();
		foreach my $k (keys %event_counter_tab){
			$total_nbr_events = $total_nbr_events + $event_counter_tab{$k};		
		}
		
		$total_nbr_events = $total_nbr_events + $nbr_unknown_event;
		print $OUT "###STATS###\n";
		print $OUT "Processing start time=$start_time\n";
		print $OUT "Processing end time=$end_time\n";
		foreach my $k (keys %event_counter_tab)
		{
			print $OUT "Number of  ".$k."=".$event_counter_tab{$k}."\n";
			$event_counter_tab{$k} = 0;
		}
		print $OUT "Number of unknown event=$nbr_unknown_event\n";
		print $OUT "Total number of events=$total_nbr_events\n";
	}
}

#####################################################
# Parse content in summary, just calculate the number.
#####################################################
sub parse_content_summary
{
	my $type;
	my $event_id;
	my $readp = 0;
	my $total_content_length = length ($total_content);
	my $flag = 0;
	my $event_name;

	while( $total_content_length - $readp > 0 ){
		$this_record_length = oct( "0x" . substr( $total_content, $readp, 4 ) );
		$type = oct( "0x" . substr( $total_content, $readp+4, 2 ) );

		$this_record_content = substr( $total_content, $readp, 12 );
		$readp = $readp + 2 * $this_record_length;

		if( $type == 1 ){
			$this_record_content_BIT = unpack("B48",pack("H12",$this_record_content));
			$event_id = oct( "0b" . substr( $this_record_content_BIT, 24, 8 ) );
			my $event_result = oct( "0b" . substr( $this_record_content_BIT, 32, 2 ) );
			if(($unsuccessful eq "true" && $event_result!=0) || $unsuccessful eq "false"){
				$flag = 1 if exists $all_events->{$event_id};
				if (1 == $flag){
					$event_name = $all_events->{$event_id}{name};
					$event_counter_tab{$event_name}++; 
					$flag = 0;
				}else{
					++$nbr_unknown_event;
				}
			}
		}
	}
}

#####################################################
# Parse the content of each event.
#####################################################
sub parse_content
{
	my $k = 0;
	my $type;
	my $error_type;
	my $termination_cause;
	my $event_id;
	my $readp = 0;
	my $total_content_length = length ($total_content);

	while( $total_content_length - $readp > 0 )
	{
		$this_record_length = oct( "0x" . substr( $total_content, $readp, 4 ) );
		$type = oct( "0x" . substr( $total_content, $readp+4, 2 ) );
		$this_record_content = substr( $total_content, $readp, 2 * $this_record_length );
		$readp = $readp + 2 * $this_record_length;

		#$this_record_length_actual = length( $this_record_content ) / 2;
		#if( not( $this_record_length % 4 == 0 ) ){		
		#	print "$prog: Record Length for Record nr $i is not a multiple of a 32 bit word\n";
		#	exit;
		#}elsif( not( $this_record_length == $this_record_length_actual ) ){
		#	print "$prog: For Record nr $i, specified record length = $this_record_length, actual record length = $this_record_length_actual\n";
		#	exit;
		#};

		#Read record header 
		if( $type == 0 ){
			$year = oct( "0x" . substr( $this_record_content, 10, 4 ) );
			$month = oct( "0x" . substr( $this_record_content, 14, 2 ) );
			if( $month < 10 )	{
				$month = "0" . $month;
			}
			$day = oct( "0x" . substr( $this_record_content, 16, 2 ) );
			if( $day < 10 ){
				$day = "0" . $day;
			};
			$hour = oct( "0x" . substr( $this_record_content, 18, 2 ) );
			$minute = oct( "0x" . substr( $this_record_content, 20, 2 ) );
			if( $minute < 10 )	{
				$minute = "0" . $minute;
			}
			$second = oct( "0x" . substr( $this_record_content, 22, 2 ) );
			if( $second < 10 )	{
				$second = "0" . $second;
			}
			$FFV = oct( "0x" . substr( $this_record_content, 6, 2 ) );
			$FIV = oct( "0x" . substr( $this_record_content, 8, 2 ) );
		}
		#Read record payload
		elsif( $type == 1 )	{
			$this_record_content_BIT = unpack("B*",pack("H*",$this_record_content));
			$hour = oct( "0b" . substr( $this_record_content_BIT, 34, 5 ) );
			$minute = oct( "0b" . substr( $this_record_content_BIT, 39, 6 ) );
			if( $minute < 10 )	{
				$minute = "0" . $minute;
			}
			$second = oct( "0b" . substr( $this_record_content_BIT, 45, 6 ) );
			if( $second < 10 )	{
				$second = "0" . $second;
			}
			
			$event_record{'time'} = "$hour:$minute:$second";
			$event_id = oct( "0b" . substr( $this_record_content_BIT, 24, 8 ) );
			$event_record{'event_id'} = $event_id;
			my $event_result = oct( "0b" . substr( $this_record_content_BIT, 32, 2 ) );
			$event_record{'event_result'} = $event_result;

			$this_record_content_BIT_index = 24;

			if (exists $all_events->{$event_id}){
				my $p_event_name = $all_events->{$event_id}{name};
				$event_counter_tab{$p_event_name}++; 

				if( (($decode_all eq "false") && (1 == $event_list_tab{$event_id})) || ($decode_all eq "true"))	{
					decode_event($event_id);
				}
			}else{
				print $OUT "Unsupported Event ID = $event_id\n";
				exit;
			}
		}
		#Read error record
		elsif( $type == 2 )	{
			$hour = oct( "0x" . substr( $this_record_content, 6, 2 ) );
			$minute = oct( "0x" . substr( $this_record_content, 8, 2 ) );
			if( $minute < 10 )
			{
			$minute = "0" . $minute;
			};
			$second = oct( "0x" . substr( $this_record_content, 10, 2 ) );
			if( $second < 10 )
			{
			$second = "0" . $second;
			};
			$error_type = oct( "0x" . substr( $this_record_content, 12, 2 ) );
		}
		#Read record footer
		elsif( $type == 3 )	{
			$termination_cause = oct( "0x" . substr( $this_record_content, 6, 2 ) );
		}
		#Unsupported Record Type
		else	{
			print $OUT "Unsupported Record Type = $type\n";
			exit;
		}
		&print_out($type,$error_type,$termination_cause,$event_id);
		$event_str = "";

		#Clear some entries for next loop. Necessary for filtering.
		if ( exists $event_record{'apn'} )	{
			delete $event_record{'apn'};
		}
		if ( exists $event_record{'tac'} )	{
			delete $event_record{'tac'};
		}
		if ( exists $event_record{'millisecond'} )	{
			delete $event_record{'millisecond'};
		}
	}
}
sub parse_content_header
{
	my $k = 0;
	my $type;
	my $error_type;
	my $termination_cause;
	my $event_id;
	my $readp = 0;
	my $total_content_length = length ($total_content);

	while( $total_content_length - $readp > 0 )
	{
		$this_record_length = oct( "0x" . substr( $total_content, $readp, 4 ) );
		$type = oct( "0x" . substr( $total_content, $readp+4, 2 ) );
		$this_record_content = substr( $total_content, $readp, 2 * $this_record_length );
		$readp = $readp + 2 * $this_record_length;

		#$this_record_length_actual = length( $this_record_content ) / 2;
		#if( not( $this_record_length % 4 == 0 ) ){		
		#	print "$prog: Record Length for Record nr $i is not a multiple of a 32 bit word\n";
		#	exit;
		#}elsif( not( $this_record_length == $this_record_length_actual ) ){
		#	print "$prog: For Record nr $i, specified record length = $this_record_length, actual record length = $this_record_length_actual\n";
		#	exit;
		#};

		#Read record header 
		if( $type == 0 ){
			$year = oct( "0x" . substr( $this_record_content, 10, 4 ) );
			$month = oct( "0x" . substr( $this_record_content, 14, 2 ) );
			if( $month < 10 )	{
				$month = "0" . $month;
			}
			$day = oct( "0x" . substr( $this_record_content, 16, 2 ) );
			if( $day < 10 ){
				$day = "0" . $day;
			};
			$hour = oct( "0x" . substr( $this_record_content, 18, 2 ) );
			$minute = oct( "0x" . substr( $this_record_content, 20, 2 ) );
			if( $minute < 10 )	{
				$minute = "0" . $minute;
			}
			$second = oct( "0x" . substr( $this_record_content, 22, 2 ) );
			if( $second < 10 )	{
				$second = "0" . $second;
			}
			$FFV = oct( "0x" . substr( $this_record_content, 6, 2 ) );
			$FIV = oct( "0x" . substr( $this_record_content, 8, 2 ) );
			last;
		}
	}
}

#####################################################
# Decode function for each IE type.
#####################################################

sub decode_ie_type 
{
	my ($type_name,$el_name, $type,$len,$level,$useValid) = @_;
	my $result;
	
	if (exists $decode_types{$type}) {
		$result = $decode_types{$type}->($len,$type_name);		
		$event_record{$type_name} = $result;
	}
	$result = "CAUSE_CODE" if ($type_name =~ /^CAUSE_CODE$/i);
	$result = "SUB_CAUSE_CODE" if ($type_name =~ /^SUB_CAUSE_CODE$/i);		
	
	if(!$pr_all) {
		if($el_name) {
			$result = "    " x $level."$el_name = $result\n" ;
		}else{
			$result = "    " x $level."$type_name = $result\n" ;
		}
	}
	
	$event_str .= $result if ($useValid == 0);
	return $result."-";	
}

#####################################################
# Decode one IE.
#####################################################
sub decode_ie
{
	my ($type, $el_name, $level, $useValid) = @_;
	
	if(exists $ie_types->{$type}){
		my $value = "";		
		if ($#{$ie_types->{$type}} > 0) {			
			if ($el_name) {
				$event_str .= "    " x $level."$el_name:\n" ;
			}else{
				$event_str .= "    " x $level."$type:\n";
			}
		}
		
		for my $i ( 0 .. $#{$ie_types->{$type}}) {
			my $elem = $ie_types->{$type}->[$i];			
			my $result = "";
			#containing attribute "useValid"
			if (defined $elem->{useValid}) {
				my $usevalid = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, 1 ) );	
				$usevalid |= $useValid;
				$this_record_content_BIT_index = $this_record_content_BIT_index + 1;				
				if ($elem->{isLeaf} != 1){				 		
		 			$result = decode_ie($elem->{type}, $elem->{name}, $level + 1, $usevalid);
		 		} else{
		 			$result = decode_ie_type($type, $el_name, $elem->{type},$elem->{len},$level,$usevalid);
		 		}		 			
			
			#containing attribute "optional"		 			
			}elsif (defined $elem->{optional}) {
				my $optional = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, 1 ) );
				$this_record_content_BIT_index = $this_record_content_BIT_index + 1;			
				if( $optional == 1 )	{
					$result = "undefined"; 
					$event_str .=  "    " x ($level + 1)."$elem->{type} = undefined\n"; 
				} else {
					if ($elem->{isLeaf} != 1){				 		
			 			$result = decode_ie($elem->{type}, $elem->{name},$level + 1,$useValid);
			 		} else{
			 			$result = decode_ie_type($type, $el_name, $elem->{type},$elem->{len},$level,$useValid); 			
			 		}
				}	
			
			#containing attribute "seqMaxLen"				
			}elsif (defined $elem->{seqLen}) {
				my $seq_length = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, 8 ) );
				$this_record_content_BIT_index = $this_record_content_BIT_index + 8;
				while ($seq_length > 0)
				{
					if ($elem->{isLeaf} != 1){				 		
			 			$result = decode_ie($elem->{type}, $elem->{name},$level + 1,$useValid);
			 		} else{
			 			$result = decode_ie_type($type, $el_name, $elem->{type},$elem->{len},$level,$useValid);
			 		}
					if ($pr_all == 1) {	
						$value .= "$result";
					}			 		
					$seq_length = $seq_length - 1;
				}
			
			#there is no attribute					
			}else{
				if ($elem->{isLeaf} != 1){				 		
		 			$result = decode_ie($elem->{type}, $elem->{name},$level + 1,$useValid);
		 		} else{
		 			$result = decode_ie_type($type, $el_name, $elem->{type},$elem->{len},$level,$useValid);
		 		}
			}

			if ($pr_all == 1) {			
				$value .= "$result";
			}	
		}

		return $value;
	}else{
		print "xxxxxxxxxtype: $type not exists\n";
		exit 0;
	}
	
}

#####################################################
# Decode one Event.
#####################################################
sub decode_event 
{
	my ($event_id) = @_;
	my ($result, $useValid) = ("",undef);
	for my $i ( 0 .. $#{$all_events->{$event_id}{ies}}) {	#all ies		
		my $elem = $all_events->{$event_id}{ies}->[$i];	
		#containing attribute "useValid"		
		if (defined $elem->{useValid}) {
			my $usevalid = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, 1 ) );	
			$this_record_content_BIT_index = $this_record_content_BIT_index + 1;		
			$result = decode_ie($elem->{type}, $elem->{name},0,$usevalid);
			
		#containing attribute "optional"		
		}elsif (defined $elem->{optional}) {
			my $optional = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, 1 ) );
			$this_record_content_BIT_index = $this_record_content_BIT_index + 1;		
			if( $optional == 1 )	{
				$result = "undefined"; 
				$event_str .=  "$elem->{type} = undefined\n"; 
			} else {
				$result = decode_ie($elem->{type}, $elem->{name},0,$useValid);
			}	
			
		#containing attribute "seqMaxLen"				
		}elsif (defined $elem->{seqLen}) {	
			my $seq_length = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, 8 ) );
			$this_record_content_BIT_index = $this_record_content_BIT_index + 8;		
			while ($seq_length > 0)
			{
				$result .= decode_ie($elem->{type}, $elem->{name},0,$useValid);			
				$seq_length = $seq_length - 1;
			}
			
		#there is no attribute					
		}else{
			$result = decode_ie($elem->{type}, $elem->{name},0,$elem->{useValid});
		}
		
		if ($pr_all == 1) {	
			$result =~ s/(.*)-$/$1/;
			if (! $elem->{name}){
				$all_ies->{$elem->{type}} = $result;
			}else{
				$all_ies->{$elem->{name}} = $result;
			}			
		}
	}

	#update cause_code and sub_cause_code
	my ($cause,$sub_cause,$cause_key) = ($event_record{cause_code},$event_record{sub_cause_code},"");
	
	if ($all_events->{$event_id}{name} =~/^l_/i){
		$cause_key = $event_record{l_cause_prot_type};
		if (exists $event_record{l_cause_prot_type} && $event_record{l_cause_prot_type} !~ /_cause$/) {
			$cause_key = $event_record{l_cause_prot_type}."_cause";
		}
	}else{
		$cause_key = $event_record{cause_prot_type};
		if (exists $event_record{cause_prot_type} && $event_record{cause_prot_type} !~ /_cause$/ ) {
			$cause_key = $event_record{cause_prot_type}."_cause";
		}
	}
	
	if (exists $cause_code{$cause_key}{$event_record{cause_code}}) {
		$cause =  $cause_code{$cause_key}{$event_record{cause_code}};
	}
	
	if (exists $cause_code{sub_cause}{$event_record{sub_cause_code}}) {
		$sub_cause =  $cause_code{sub_cause}{$event_record{sub_cause_code}};
	}

	$event_str =~ s/(.*)(SUB_CAUSE_CODE)(.*)/$1$sub_cause$3/m;
	$event_str =~ s/(.*)(CAUSE_CODE)(.*)/$1$cause$3/m;
	$all_ies->{sub_cause_code} = $sub_cause ;
	$all_ies->{cause_code} = $cause ;
	
}

#####################################################
# Decode IE of BCD type.
#####################################################
sub bcd_coding
{
	my $in = $_[0];
	my $out = "";
	while( length( $in ) > 0)
	{
		$out = $out . substr( $in, 1, 1 ) . substr( $in, 0, 1 );
		$in = substr( $in, 2 );
	}

	if( not( $out =~ m/^[fF]+$/ ) )
	{
		$out =~ s/[fF]+//;
	}

	return $out;
}

#####################################################
# Decode IE of uint type.
#####################################################
sub decode_uint
{
	my ($len,$name)=@_;
	my $uint_val = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, $len) );
	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;
	$uint_val = $cause_code{bearer_cause}{$uint_val}  if ($name eq "bearer_cause" && exists $cause_code{bearer_cause}{$uint_val} );
	return $uint_val;    
}

#####################################################
# Decode IE of enum type.
#####################################################
sub decode_enum
{
	my ($len,$name)=@_;
	my $enum_val = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, $len) );
	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;
	if (exists $enums->{$name}->{$enum_val}) {
		return $enums->{$name}->{$enum_val};
	}else {
		return $enum_val;
	}
}

#####################################################
# Decode IE of bytearray type.
#####################################################
sub decode_bytearray
{
	my($len)=@_;

	my $el_len = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) );

	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;

	$el_len = 8 * $el_len;

	my $nr_of_bits_to_next_byte = 8 - $this_record_content_BIT_index % 8;
	if ( $nr_of_bits_to_next_byte == 8 )	{
		$nr_of_bits_to_next_byte = 0;
	}
	$this_record_content_BIT_index = $this_record_content_BIT_index + $nr_of_bits_to_next_byte;


	my $in = unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $el_len ) ) );
	(my $bytearray_val_ascii = $in) =~ s/([a-fA-F0-9]{2})/chr(hex $1)/eg;

	$this_record_content_BIT_index = $this_record_content_BIT_index + $el_len;

	return  $bytearray_val_ascii;
  }

#####################################################
# Decode IE of ipaddress type.
#####################################################
sub decode_ipaddress
{
	my ($len) = @_;

	my $ipv4_val = unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) ) );
	$ipv4_val = hex(substr($ipv4_val,0,2)).".".hex(substr($ipv4_val,2,2)).".".hex(substr($ipv4_val,4,2)).".".hex(substr($ipv4_val,6,2));

	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;

	return $ipv4_val;
}

#####################################################
# Decode IE of ipaddress_v4 type.
#####################################################
sub decode_ipaddressv4
{
	my ($len) = @_;

	my $ipv4_val = unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) ) );
	$ipv4_val = hex(substr($ipv4_val,0,2)).".".hex(substr($ipv4_val,2,2)).".".hex(substr($ipv4_val,4,2)).".".hex(substr($ipv4_val,6,2));

	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;

	return $ipv4_val;
}

#####################################################
# Decode IE of ipaddress_v6 type.
#####################################################
sub decode_ipaddressv6
{
	my ($len) = @_;

	my $ipv6_val = unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) ) );

	$ipv6_val = (substr($ipv6_val,0,4)).":".(substr($ipv6_val,4,4)).":".(substr($ipv6_val,8,4)).":".(substr($ipv6_val,12,4)).
			":".(substr($ipv6_val,16,4)).":".(substr($ipv6_val,20,4)).":".(substr($ipv6_val,24,4)).":".(substr($ipv6_val,28,4));
	$ipv6_val =~ s/:0{1,3}/:/g;
	$ipv6_val =~ s/^0{1,3}//g;

	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;
	return $ipv6_val;
}

#####################################################
# Decode IE of TBCD type.
#####################################################
sub decode_tbcd
{
	my ($len) = @_;
	my $tbcd_val = bcd_coding( unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) ) ) );

	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;

	return $tbcd_val;
}

#####################################################
# Decode IE of dnsname type.
#####################################################
sub decode_dnsname
{
	my ($len) = @_;

	my $apn_length = oct( "0b" . substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) );
	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;		
	$len = 8 * $apn_length;
	#padding, moving start-of-apn to next byte
	my $nr_of_bits_to_next_byte = 8 - $this_record_content_BIT_index % 8;
	if ( $nr_of_bits_to_next_byte == 8 )
	{
		$nr_of_bits_to_next_byte = 0;
	}

	$this_record_content_BIT_index = $this_record_content_BIT_index + $nr_of_bits_to_next_byte;
	my $in = unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) ) );

	my $dnsname_val = "";
	my $index = 0;
	while( $index < length( $in ) )
	{
		my $length_of_one_section = 2 * oct( "0x" . substr( $in, $index, 2 ) );
		my $one_section = substr( $in, 2 + $index, $length_of_one_section );

		(my $one_section_ascii = $one_section) =~ s/([a-fA-F0-9]{2})/chr(hex $1)/eg;
		$dnsname_val = $dnsname_val . $one_section_ascii . ".";

		$index = $index + $length_of_one_section + 2;
	};
	
	#remove end dot
	$dnsname_val = substr( $dnsname_val, 0, length( $dnsname_val ) - 1);
	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;
	return $dnsname_val;
}

#####################################################
# Decode IE of IBCD type.
#####################################################
sub bcd_coding__3hex
{
	my $in = $_[0];
	my $out = substr( $in, 2, 1 ) . substr( $in, 1, 1 ) . substr( $in, 0, 1 );
	$out =~ s/[fF]+//;	
	return $out;
}
sub decode_ibcd
{
    my ($len) = @_;

	my $ibcd_val = bcd_coding__3hex( unpack("H*",pack("B*",substr( $this_record_content_BIT, $this_record_content_BIT_index, $len ) ) ) );

	$this_record_content_BIT_index = $this_record_content_BIT_index + $len;

	return $ibcd_val;
}

#####################################################
# Print out the parse result.
#####################################################
sub print_out 
{
	my $type = $_[0];
	my $error_type = $_[1];
	my $termination_cause = $_[2];
	my $event_id = $_[3];
	my $filter_hit;
	my $skip_next = "false";

	if( $type == 0 )
	{
		if ($pr_all ==1 )
		{
			my $string = "header${delimiter}l_header${delimiter}";

			foreach my $key (sort keys %{$all_ies}) {
				$string .= "$key${delimiter}" if ($key !~ /header$/);
			}
			print $OUT "$string\n";
		} else {
			print $OUT "\n###HEADER###\n";
			print $OUT "date=$year-$month-$day\n";
			print $OUT "time=$hour:$minute:$second\n";
			print $OUT "FFV=$FFV\n";
			print $OUT "FIV=$FIV\n\n"; 
		}
	}
	elsif( $type == 1 )
	{
		my $filter_length = @filters;
		if (  $filter_length != 0 ) {
			$filter_hit = &filter();
		}else{
			$filter_hit=1;
		}

		if ($filter_hit == 1 ){
			if( (($decode_all eq "false") && (1 == $event_list_tab{$event_id})) || ($decode_all eq "true"))	{
				if ($pr_all == 1){	
					&print_all_ies;	
				}else{
					if ($delimiter eq "\n")	{
						print $OUT "======EVENT======\n";
					}
					print $OUT $event_str;	#print out the event result
					print $OUT "\n";
				}
			}
		}
	}		
	elsif( $type == 2 )
	{
		print $OUT "\n###ERROR###\n";
		print $OUT "error_type=$error_type\n\n\n\n\n\n";
	}
	elsif( $type == 3 )
	{
		print $OUT "\n###FOOTER###\n";
		print $OUT "termination_cause=$termination_cause\n\n";
	};
}

#####################################################
# Print the result in table format (with -l option).
#####################################################
sub print_all_ies
{
	my $string = "$all_ies->{header}${delimiter}$all_ies->{l_header}${delimiter}";

	foreach my $key (sort keys %{$all_ies}) {
		$string .= "$all_ies->{$key}${delimiter}" if ($key !~ /header$/);
		$all_ies->{$key} = "";
	}
	print $OUT "$string\n";

}

#####################################################
# Build filter list.
#####################################################
sub build_filter
{
	my $rats;  
	my $event_ids;
	if ( $unsuccessful eq "true" ) {
		my $event_result = " ";
		foreach my $key (keys %{$enums->{event_result}}){
			my $result = $enums->{event_result}->{$key};
			$event_result .= $result." " if ($result !~ /^success$/i);
		}
		push(@filters, "event_result", "$event_result") ;
	}
	$rats .= "gsm" if ( $gsm eq "true" );
	$rats .= "wcdma" if ( $wcdma eq "true" );
	push(@filters, "rat", $rats) if ( $gsm eq "true" || $wcdma eq "true" );
	push(@filters, "cause_code", ${cause_code_pattern}) if ( $cause_code eq "true" )	;
	push(@filters, "imsi", $imsi_pattern) if ( $imsi eq "true" );
	push(@filters, "tac", $tac_pattern)	 if ( $tac eq "true" );
	push(@filters, "apn", $apn_pattern) if ( $apn eq "true" );
	%filters = @filters;
}

#####################################################
# Filter the result (whether print out the event).
#####################################################
sub filter
{
	my $filter_hit = 1;
	my $filter;

	if ( $tac eq "true" )	{
		$event_record{'tac'} = substr($event_record{'imeisv'},0,8);
	}

	foreach $filter (keys %filters){ 
		if (exists($event_record{$filter}) && $filters{$filter} =~ /\s*$event_record{$filter}\s*/)   {
			$filter_hit = $filter_hit & 1;
		}else {
			$filter_hit = $filter_hit & 0;
			last;
		}
	}
	return $filter_hit;
}

#####################################################
# Print out the warning.
#####################################################
sub sgsn_warning {
	my $snode=&get_sgsn_node;
	if($snode){
		#script is running on the sgsn node. 
		print  "This script is not intended to be ran on the SGSN-MME, it might impact the capacity of the SGSN-MME. Are you sure you want to run the script (yes/no)? ";
		my $in=<STDIN>;
		chomp($in);
		if($in =~ /y/i){
			return 0;
		}else{
			print  "Execution is canceled by the user. Exiting...\n";
			exit 0;
		}
	}
}

#####################################################
# Whether it's running on node or GTT.
#####################################################
sub get_sgsn_node {
	if (-r "/tmp/DPE_SC/LoadUnits/ttx/int/bin/load_limit.pl") {
		return 1;
	}
	return 0;
}

#####################################################
# This subroutine checks and returns the CPU load.
#####################################################
sub working
{
    my $now= time();
    if ( $sec != $now ){
        $sec=$now;
        print "$working[$wId]\b";
        $wId++;
        $wId= 0 if ( $wId >3);
    }
}
sub get_cpu_load 
{
    my ($nr) = @_;
    my $ret = 0;
    open(VS, "vmstat 1 $nr |") || die "Cannot run vmstat - $!";
    my (@out) = <VS>;
    close(VS);
    if (defined $out[2] && $out[2] =~ /\d+\s+/) {
        for my $i (3..($nr+1)) {
            working();
            my @arr = split(/\s+/, $out[$i]);
            if ($TTX_OS_HW =~ /sun4u/) {
                $ret += $arr[$#arr];
            }
            else {
                my $ver = `uname -r`;
                if ($ver =~ /\d+\.(\d+)\.(\d+)/ ) {
                    if ($1 > 5 && $2 >= 11 ){
                        $ret += $arr[$#arr-2];
                    }
                    else {
                        $ret += $arr[$#arr-1];
                    }
                }
                else {
                    $ret += $arr[$#arr-1];
                }
            }
        }
    }
    $ret = 100-int($ret/($nr-1));
    return $ret;
}

#####################################################
# This subroutine checks CPU load and decides if the  script is allowed to start.
# If load caused by other processes > max_load, script pauses.
#  If load > max_load for to long, script terminates.
#####################################################
sub check_load {
	# read input parameters
	my $max_load   = shift(@_);
	my $iterations = shift(@_);
	my $flag       = shift(@_);
	my $load = 200;		# set start value greater than a 100%.
	my $i = 0;			# No of load check iterations.

	while ($load >= $max_load) {
		$load = get_cpu_load(5);
		if ($load > $max_load) {
			print ("Execution paused a few seconds due too high CPU load: $load% \n");
			sleep 3;		# Sleep until the load is lower than $max_load.
			$i++;
			if ($i > $iterations) { # After $iterations*3 sec's.
				print ("Script terminated due to persistent overload. \n");
				exit;
			}
		} else {
			if ($flag eq "initial") {
				print("CPU load check passed: load = $load% \n");
			}
			return;
		}
	}
}

sub cpu_load {
	print("Initial CPU Load Check...");
	check_load(40.0, 20, "initial"); # Max CPU load allowed
	print $OUT "\n";
  }

#####################################################
# Set Hardware and OS type, used for checking CPU load
#####################################################
sub set_ttx_env 
{
    my $uname=`uname -m`;
    if ($uname =~ /sun4u/) {
        $TTX_OS_HW="SunOS__sun4u";
    }
    elsif ($uname =~ /ppc/) {
        $TTX_OS_HW="Linux__ppcCommon";
    }
    else {
        $TTX_OS_HW="Linux__$uname";
    }
}

sub REAPER {
	my $child;
	my $count=10;
	my $child=1;
	while (($child >0) && ($count > 0)) {
		$child = waitpid(-1, &WNOHANG);
		$count--;
	}
	$SIG{CHLD} = \&REAPER;	# install *after* calling waitpid
}

#####################################################
# Main
#####################################################	
#set nice
my $nice=10;
setpriority('PRIO_PROCESS',$$,$nice) or print("Cannot set priority for process - $!\n");
set_ttx_env;
my $cpid;
my $snode=&get_sgsn_node;

getopts('hsulp:r:e:c:i:t::a:f:o:d:', \%opts);
if (exists $opts{h}) {
	usage;
	exit 0;
} 
&parse_ebm_xml($file);
&get_opts();
parse_cause;
sgsn_warning;
&build_filter();
redirect;

#####################################################
if ($snode) {
	cpu_load;
	if ($cpid = fork) {
		# parent
		$SIG{CHLD} = \&REAPER;
		exec("/tmp/DPE_SC/LoadUnits/ttx/int/bin/load_limit.pl -s 1 -r 0.10 -p $cpid");
	} elsif (defined $cpid) {
		# child
		setpgrp(0,0);
		$SIG{CHLD} = 'IGNORE';
		process_ebs_log_files();
		#exit 0;
	} else {
		die "Can't fork: $!\n";
	}
} else {
	process_ebs_log_files();
}
if ($$OUT !~ /STDOUT/){
	close($OUT) || die "Could not flush buffer or close output file - $!";
}
exit 0; 

