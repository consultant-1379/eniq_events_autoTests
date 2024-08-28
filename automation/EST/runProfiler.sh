#!/bin/bash
server=$1
noOfRepetation=$2
startTime1=$3
startTime2=$4
tooljar="/net/atclvm560.athtem.eei.ericsson.se/ede/EST/LatencyProfiler/ltees_relative_latency_measurement_v1.jar"
startTime="$startTime1 $startTime2" 
profilerPath="/eniq/backup/ltees_profiling/logs";
db_UserName="xsinkus";
db_Password="Aug@2015";
db_URL="ddprepl.athtem.eei.ericsson.se";
FileGenerationPath="/eniq/home/dcuser/automation/EST/html";


        if [ ! -d "/eniq/backup/ltees_profiling" ]; then
                mkdir -p /eniq/backup/ltees_profiling/bin
                mkdir -p /eniq/backup/ltees_profiling/logs
                chmod 777 /eniq/backup/ltees_profiling/*
                cp $tooljar /eniq/backup/ltees_profiling/bin
                chmod 777 /eniq/backup/ltees_profiling/bin/*
        fi

		#cp  /eniq/backup/ltees_profiling/logs/latency_profile_2016-1-6_12%3A0%3A0%3A532.txt /eniq/backup/ltees_profiling/logs/latency_profile_2016-1-6_12%3A0%3A0%3A5321.txt

## Run the latency profiler 
/eniq/sw/runtime/java/bin/java -jar -d64 -Xmx4096m -Xms2048m -XX:MaxPermSize=256m /eniq/backup/ltees_profiling/bin/ltees_relative_latency_measurement_v1.jar /eniq/backup/ltees_profiling/logs 


##Run the DDP.jar to parse the result of latency profiler
/eniq/sw/runtime/java/bin/java -jar /eniq/home/dcuser/automation/EST/DDP.jar "$server" "$startTime" "$noOfRepetation" "$profilerPath" "$db_UserName" "$db_Password" "$db_URL" "$FileGenerationPath"

##Upload the result 
/eniq/home/dcuser/automation/EST/upload_html.sh 

