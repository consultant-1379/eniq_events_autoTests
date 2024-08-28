#!/bin/ksh
#set -xv

CMD=`basename $0`
current_server=`hostname`
usage ()
{
        echo ""
        printf "%s\t%s\n" "Usage: $CMD" "-r repetition\n -c ctrnodestosimulate\n -p path\n -m ecmovementrequired\n -n noofdays\n -e hoursofData\n -i Intermediate_DataLocation\n -t Topology_SourceLocation\n -d Topology_DestinationLocation\n -r datagen_server\n -u datagen_server_username\n -s datagen_server_passwd\n"
	exit 0;
}



while getopts c:C:e:m:M:d:h:i:p:n:r:R:t:u:p:s: opt; do
        case $opt in
        c)      ctrnodestosimulate=$OPTARG
                ctrnodestosimulate=`echo $ctrnodestosimulate |tr '[:upper:]' '[:lower:]'`
                ;;
		m)		ecmovement=$OPTARG
				ecmovement=`echo $ecmovement`
				;;
		n)  	noofdays=$OPTARG
				noofdays=`echo $noofdays |tr '[:upper:]' '[:lower:]'`
				;;
		e)  	hourofData=$OPTARG
				hourofData=`echo $hourofData`
				;;		
		p)  	path=$OPTARG
				path=`echo $path`
				;;
		i)  	Intermediate_DataLocation=$OPTARG
				Intermediate_DataLocation=`echo $Intermediate_DataLocation`
				;;
		t)  	Topology_SourceLocation=$OPTARG
				Topology_SourceLocation=`echo $Topology_SourceLocation`
				;;
		d)  	Topology_DestinationLocation=$OPTARG
				Topology_DestinationLocation=`echo $Topology_DestinationLocation`
				;;
		r)  	datagen_server=$OPTARG
				datagen_server=`echo $datagen_server`
				;;
		u)  	datagen_server_username=$OPTARG
				datagen_server_username=`echo $datagen_server_username`
				;;
		s)  	datagen_server_passwd=$OPTARG
				datagen_server_passwd=`echo $datagen_server_passwd`
				;;
		h|\?)   usage
				;;
        esac
done


# Usage checks
[ "$noofdays" != "" ] || { echo "Error: -n parameter is required"; usage; }
[ "$ctrnodestosimulate" != "" ] || { echo "Error: -c parameter is required"; usage; }
[ "$ecmovement" != "" ] || { echo "Error: -m parameter is required .....value is $ecmovement"; usage;}
[ "$path" != "" ] || { echo "Error: -p parameter is required"; usage; }
[ "$Intermediate_DataLocation" != "" ] || { echo "Error: -i parameter is required"; usage; }
[ "$Topology_SourceLocation" != "" ] || { echo "Error: -t parameter is required"; usage; }
[ "$Topology_DestinationLocation" != "" ] || { echo "Error: -d parameter is required"; usage; }
[ "$datagen_server" != "" ] || { echo "Error: -r parameter is required"; usage; }
[ "$datagen_server_username" != "" ] || { echo "Error: -u parameter is required"; usage; }
[ "$datagen_server_passwd" != "" ] || { echo "Error: -s parameter is required"; usage; }
[ "$hourofData" != "" ] || { echo "Error: -e parameter is required"; usage; }	



echo "NO_OF_DAYS:$noofdays\nHOURSOFDATA:$hourofData\nCTRNODESTOSIMULATE:$ctrnodestosimulate\nPATH:$path\nIntermediate_DataLocation:$Intermediate_DataLocation\nTopology_InterLocation:$Topology_SourceLocation\nTopology_DestinationLocation:$Topology_DestinationLocation\nCordinator_server:$current_server\nEcmovement:$ecmovement\nDatagen_server:$datagen_server\nDatagen_User:$datagen_server_username\nDatagen_Passwd:$datagen_server_passwd">/eniq/home/dcuser/automation/ESTconfig.prop;

chmod 775 /eniq/home/dcuser/automation/ESTconfig.prop;

#Calling EniqeventsRegress
/eniq/home/dcuser/automation/EniqEventsRegress.sh /eniq/home/dcuser/automation/priority/Config_EST_PASS.txt;
