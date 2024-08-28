#!/bin/bash

home_path=$(pwd)
#logs_path=../logs
#user_path=$home_path/../cfg/user
user_path=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/cfg/user
bin_dir=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/bin
app_path=$home_path/../cfg/app
logs_path=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/logs
xml_path=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/cfg/user/UserInputXMLV1.0.xml
destinationIp=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/bin/destIpFoldersInfoList.txt
referenceIp=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/bin/referenceServerIp.txt

##########################################################################################################
# Function: Usage
#
# Description: This function display this tool usage information.
##########################################################################################################

KillEDE(){

   ps -ef | grep "/ede/LikeForLike/ede-16.A3-Release/bin/startEdeTool.sh " | grep -v "grep" > 1.txt
   ps -ef | grep "/ede/LikeForLike/ede-16.A3-Release/jre/bin/amd64/java " | grep -v "grep" >> 1.txt
   cat 1.txt | while read line1
   do
      pide=$(echo $line1 | cut -d ' ' -f 2)
      kill -9 $pide
   done

   rm -f 1.txt
}
##########################################################################################
##########################################################################################
#                                    Main Function                                       #
##########################################################################################
##########################################################################################


 



checkForEDERunning(){
#ps -ef | grep LikeForLike | grep -v "grep" > abc.txt
ps -ef | grep "/ede/LikeForLike/ede-16.A3-Release/bin/startEdeTool.sh " | grep -v "grep" > abc.txt
ps -ef | grep "/ede/LikeForLike/ede-16.A3-Release/jre/bin/amd64/java " | grep -v "grep" >> abc.txt
 
    if [ -s abc.txt ]
    then
        echo "EDE is already Running. Killing the the current session "
		KillEDE
    fi
	rm -f abc.txt

}		

cleanOldLogs(){

cd $logs_path

echo "Cleaninig old logs from the log directory"

rm -rf *.log*
rm -rf *.txt
rm -rf *.csv

rc=$?
    if [ $rc -ne 0 ] ; then 
     echo "Not able to clean all the logs. Please clean all the logs in the $log_dir manually "
    else 
	  echo "sucessfully cleaned all the logs "
    fi
}

editUserInputXml(){

cd $user_path

cp   UserInputXMLV1.0.xml UserInputXMLV1.0.xml_orig

oldIp=`grep IPAddress UserInputXMLV1.0.xml | grep -v 10.59.130.118 |  cut -d ">" -f2 |  cut  -d "<" -f1  | head -1` 
newIp=`cat $destinationIp`
oldRefrenceIp=`grep IPAddress UserInputXMLV1.0.xml | grep -v 10.59.130.118 |  cut -d ">" -f2 |  cut  -d "<" -f1  | tail -1` 
newRefIp=`cat $referenceIp`
echo " oldIp  $oldIp "
echo "newIP $newIp "
echo " oldRefrenceIp  $oldRefrenceIp"
echo "newRefIp  $newRefIp"
sed "s/$oldIp/$newIp/" <  UserInputXMLV1.0.xml > new.xml
sed "s/$oldRefrenceIp/$newRefIp/" <  new.xml > UserInputXMLV1.0.xml




rm -rf  new.xml

echo "edited the user input xml " 
}


 generateIPtoFDNmapping(){

              echo "Starting IP-FDN Mapping ..."
             cd $user_path
			 rm *FDNSourceIPMapping.txt
			 cd $bin_dir
		
              nohup ./startEdeTool.sh Generateipfdnmapping  &
              sleep 30

              RETRY1=1

              while [ $RETRY1 -eq 1 ]
              do

                 sleep 1
                 cat "$logs_path"/ede_controller.log | grep "IP-FDN mapping file generation is completed."
                 if [ $? -eq 0 ]
                 then
                     RETRY1=0

                 else
                     logfile="$logs_path"/ede_controller.log
                     if [ -f $logfile ]; then
                         tail -1 "$logfile" | grep "ERROR"
                         
						 if [ $? -eq 0 ]
                         then
                             echo "Error during IP-FDN Mapping.. Quitting.. Check the Logs Path."
							 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                             SKIP=1
                             continue 2
                         fi
                     else
                         echo "Error during IP-FDN Mapping.. Quitting.. Check the Logs Path."
						 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                         SKIP=1
                         continue 2
                     fi
                 fi
              done
              echo "IP-FDN Mapping completed successfully..."
              
			 # cp $user_path/CTR_FDNSourceIPMapping.txt $toolLog/CTR_FDNSourceIPMapping.txt
			  chmod 777 $user_path/CTR_FDNSourceIPMapping.txt
			 # chmod 777 $toolLog/CTR_FDNSourceIPMapping.txt
			 
			 cleanOldLogs
			  
			  }


	   
          


 #Start of Main Script 

echo "Started Configuring EDE on atclvm560 " 
checkForEDERunning
cleanOldLogs
editUserInputXml
generateIPtoFDNmapping 
         

           




			 
      
         


