#!/bin/bash
 
home_path=$(pwd)
logs_path=../logs
user_path=/ede/EDE_multidest/ede-6.0.7-Release/cfg/user
app_path=$home_path/../cfg/app
precookSuccess="AggregateEventRateStats.run : Source event rate file written for dataSource"


##########################################################################################################
# Function: Usage
#
# Description: This function display this tool usage information.
##########################################################################################################
Usage(){

   echo ""
   echo "Usage: ./tshell.sh <mode(preprocess or postprocessing or stream)>"
}

Help(){

   echo ""
   echo " Description: "
   echo ""
   echo " Preprocess / Postprocessing / Stream(Mandatory):"
   echo "   -If Preprocess option is provided, the script will run EDE Precook and "
   echo "    IP-FDN Mapping"
   echo "   -If Postprocessing option is provided, the script will stream data "
   echo "    (considering precook data is available)"
   echo "   -If Stream is provided, the script will run all the EDE steps to stream data"
   echo ""
   echo "   Input: "
   echo ""
   echo "   -Input data needs to be provided in the file destIpFoldersInfoList.txt"
   echo "   -Data should be provided in the format <DestinationIP>#<Input folder "
   echo "    absolute path>#<Intermediate folder absolute path>"
   echo "   -In case streaming needs to be performed to multiple IPs, data can be "
   echo "    provided in multiple rows in the file"
}

KillEDE(){

   ps -ef | grep EDE_multidest | grep -v "grep" > /tmp/cde.txt
   cat /tmp/cde.txt | while read line1
   do
      pide=$(echo $line1 | cut -d ' ' -f 2)
      kill -9 $pide
   done

   rm -f /tmp/cde.txt
}
##########################################################################################
##########################################################################################
#                                    Main Function                                       #
##########################################################################################
##########################################################################################



fArg=$( echo "$1" | tr '[:lower:]'  '[:upper:]' )

shopt -s nocasematch
case $fArg in
 PREPROCESS | preprocess | POSTPROCESSING | postprocessing | stream | STREAM);;
     *) Usage
        Help
        exit 1;;
esac

echo "Here one"

ps -ef | grep EDE_multidest | grep -v "grep" > /tmp/abc.txt

    if [ -s /tmp/abc.txt ]
    then
        echo "EDE is already Running. Exit..."
        exit 0
    fi

	# As the requirement is to currently just support single input output folder, we do not need to run the 
	# precook everytime. Hence used this variable to skip precook for the consecutive IPs.
	# If precookOnce=1, it will run only once.
	# If set to 0, it will run everytime.
	precookOnce=1
	skipPrecook=0
	
	if [[ "$fArg" == "preprocess" || "$fArg" == "PREPROCESS" ]]; then
      runAll=1
    elif [[ "$fArg" == "postprocessing" || "$fArg" == "POSTPROCESSING" ]]; then
      runAll=2
    else
      runAll=0
    fi
	
	if [ $runAll -eq 2 ]; then
	     skipPrecook=1
    fi
	
echo "Here two"

    cat destIpFoldersInfoList.txt | while read line
    do
echo "Here three"

       SKIP=0
       KillEDE
       #echo "$line"
       #echo "Inside first loop."

       destIP=$(echo $line | cut -d "#" -f 1)
       input_Folder=$(echo $line | cut -d "#" -f 2)
       intermediateFolder=$(echo $line | cut -d "#" -f 3)
	   destHostname=$(echo $line | cut -d "#" -f 4)
 
       if [ -d "$logs_path" ] && [ -d "$intermediateFolder" ] && [ -d "$input_Folder" ]; then
	  
	         if [ "$destHostname" == "" -o "$logs_path" == "" -o "$intermediateFolder" == "" -o "$input_Folder" == "" ]; then
			 
			     echo "Error: Null value provided as input"
				 continue
			 fi
	   else
	         echo "Error: One of the directories provided as input is incorrect. Please check the input/intermediate and logs path directories present"
		     continue
	   fi

	   destHostname=$( echo "$destHostname" | tr '[:upper:]'  '[:lower:]' )
       toolLog="/net/atclvm560.athtem.eei.ericsson.se/ede/data/CentralDatagen/""$destHostname"
	   #logFilePath = "/net/atclvm559.athtem.eei.ericsson.se/tmp/DatagenStore/LTEStreaming"
	   
       if [ $runAll -eq 1 -o $runAll -eq 0 ] && [ $skipPrecook -eq 0 ]; then 
         echo "Cleaning up the EDE Log directory ..."
         rm -rf "$logs_path"/*

         echo "Cleaning up the intermediate directory before Precook"

         rm -rf "$intermediateFolder"/*
       fi
	   

       echo "Setting the config file with the input provided in the destIpFoldersInfoList.txt file. " | tee -a
       echo "Destination IP: " $destIP
       echo "Input Folder: " $input_Folder
       echo "Intermediate Folder: " $intermediateFolder
	   
	   ##chmod 777 modifyEDEConf.pl
	   echo "HERE IS FOUR."
#	   perl modifyEDEConf.pl
           # calling the script to edit IP in the XML   
           /ede/EDE_multidest/changeDesIP.sh  
 
       #Changing IP address, input and intermediate folders in UserInputXMLV1.0.xml
       #sed -e "s|\(<StreamLocation><IPAddress>\)[^<>]*\(</IPAddress>\)|\1${destIP}\2|" "$user_path"/UserInputXMLV1.0.xml > "$user_path"/UserInputXMLV11.0.xml
	   # sed -e "s|<StreamLocation><IPAddress>.*</IPAddress>|<StreamLocation><IPAddress>$destIP</IPAddress>|" "$user_path"/UserInputXMLV1.0.xml > "$user_path"/UserInputXMLV11.0.xml
	   # mv "$user_path"/UserInputXMLV11.0.xml "$user_path"/UserInputXMLV1.0.xml
       # sed -e "s|\(<SourceFileLocation>\)[^<>]*\(</SourceFileLocation>\)|\1${input_Folder}\2|" "$user_path"/UserInputXMLV1.0.xml > "$user_path"/UserInputXMLV11.0.xml
       # mv "$user_path"/UserInputXMLV11.0.xml "$user_path"/UserInputXMLV1.0.xml
       # sed -e "s|\(<IntermediateFileLocation>\)[^<>]*\(</IntermediateFileLocation>\)|\1${intermediateFolder}\2|" "$user_path"/UserInputXMLV1.0.xml > "$user_path"/UserInputXMLV11.0.xml
       # mv "$user_path"/UserInputXMLV11.0.xml "$user_path"/UserInputXMLV1.0.xml
	   
	   # Enabling the Analysis mode on in ConfigSchemaV1.1.xml
	   
	  #@@# sed -e "s|\(<Analysis value=\)[^<>]*\(>\)|\1"\"true\""\2|" "$app_path"/ConfigSchemaV1.1.xml > "$app_path"/ConfigSchemaV11.1.xml
      #@# mv "$app_path"/ConfigSchemaV11.1.xml "$app_path"/ConfigSchemaV1.1.xml

       if [ $runAll -eq 1 -o $runAll -eq 0 ]; then

          if [ $skipPrecook -eq 0 ]; then
		     
			 echo "Clean up the CTR_FDNSourceIPMapping.txt file."
			 
		     rm -f $user_path/CTR_FDNSourceIPMapping.txt
			 
		     echo "Starting the Precook."
			 
             nohup ./startEdeTool.sh precook START &
             sleep 30
             RETRY=1 
             while [ $RETRY -eq 1 ]
             do

                 sleep 10 
                 cat "$logs_path"/ede_controller.log | grep "$precookSuccess" 

                 if [ $? -eq 0 ]
                 then 
                     RETRY=0

                 else
                      logfile="$logs_path"/ede_controller.log
                      if [ -f $logfile ]; then 
                          tail -1 "$logfile" | grep "ERROR"
                          if [ $? -eq 0 ]
                          then
                              echo "Error during PRECOOK.. Quitting.. Check the Logs Path."
							  #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                              continue 2 
                              #exit 0
                          fi
                      else
                         echo "Error during PRECOOK.. Quitting.. Check the Logs Path."
						 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                         continue 2 
                      fi 
                 fi
              done
    

              echo "PRECOOK completed successfully..."

              #@#cp $logs_path/DataAnalysis.txt ../DataAnalysis.txt

              KillEDE

              echo "Cleaning up the EDE Log directory ..."
              rm -rf "$logs_path"/*

              echo "Starting IP-FDN Mapping ..."

              nohup ./startEdeTool.sh Generateipfdnmapping START &
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
              
			  cp $user_path/CTR_FDNSourceIPMapping.txt $toolLog/CTR_FDNSourceIPMapping.txt
			  chmod 777 $user_path/CTR_FDNSourceIPMapping.txt
			  chmod 777 $toolLog/CTR_FDNSourceIPMapping.txt
	    	  
			  if [ $precookOnce -eq 1 ]; then
		          skipPrecook=1
    		  fi
		  
		  fi
		  

	   
          if [ $runAll -eq 0 ]; then

		  	 # Waiting for signal to start streaming.
             startLoop=1
			 while [ $startLoop -eq 1 ]
			 do

				 cat "$toolLog"/ede_stream.log | grep "START"
				 if [ $? -eq 0 ]
				 then
					 startLoop=0
				 fi
	 
				 sleep 10
				 echo "Waiting for the workflow to be initiated to start the Streaming..."
			 done
		  
             echo "Cleaning up the EDE Log directory ..."
             rm -rf "$logs_path"/*

             echo "Cleaning up the Streaming temp directory ..."
             rm -rf "$intermediateFolder"/temp/*

             echo "Starting Post Processing step for Streaming data to Destination IP: " $destIP
             
			 sleep 300
             nohup ./startEdeTool.sh Postprocessing START &
             sleep 100

             logfile="$logs_path"/ede_controller.log
             if [ -f $logfile ]; then
                 cat "$logfile" | grep "ERROR: Controller.start"
                 if [ $? -eq 0 ]
                 then
                     echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
					 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                     SKIP=1
                     continue
                 fi
             else
                 echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
				 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                 SKIP=1
                 continue
             fi

             RETRY2=1

             while [ $RETRY2 -eq 1 ]
             do

                 sleep 30 
                 cat "$logs_path"/ede_controller.log | grep "Pre-processing completed"
                 if [ $? -eq 0 ]
                 then
                     break 
                 else
                     tail -1 "$logs_path"/ede_controller.log | grep "ERROR"
                     if [ $? -eq 0 ]
                     then
                         SKIP=1
                         echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
                         #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                         continue 2  
                     fi
                 fi
             done

             if [ $SKIP -eq 1 ]; then
                 KillEDE
                 break 
             fi



             while [ "$(ls -A "$intermediateFolder"/temp)" ]
             do
                 echo "Waiting for Streaming to complete ..."
                 sleep 30
                 logfile="$logs_path"/ede_controller.log
                 if [ -f $logfile ]; then
                     tail -1 "$logfile" | grep "ERROR"
                     if [ $? -eq 0 ]
                     then
                         echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
						 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                         SKIP=1
                         continue 2
                     fi
                 else
                     echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
					 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                     SKIP=1
                     continue 2
                 fi
             done

             if [ $SKIP -eq 1 ]; then
                 KillEDE
                 break 
             fi
 
             echo "Post Processing/Streaming Successfully completed... Quitting the tool... Bbye"
			 
			 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/user/UserInputXMLV1.0.xml
			 
			 echo "Delete the nohup.out"
			 rm -rf nohup.out

			 count1=$(grep -i "Total number of events " /ede/EDE_multidest/ede-6.0.7-Release/DataAnalysis.txt | cut -d ":" -f 2)
			 count2=$(grep -i "OTHER_EVENTS" /ede/EDE_multidest/ede-6.0.7-Release/DataAnalysis.txt | cut -d ":" -f 2)
			 totalE=$(($count1 + $count2))
			 #$#echo $totalE >> /net/atclvm559/tmp/DatagenStore/LTEStreaming/ede_stream.log
			 #$#echo "DONE" >> /net/atclvm559/tmp/DatagenStore/LTEStreaming/ede_stream.log
			 #echo $totalE >> /net/atclvm560.athtem.eei.ericsson.se/tmp/DatagenStore/LTEStreaming/ede_stream.log
			 #echo "DONE" >> /net/atclvm560.athtem.eei.ericsson.se/tmp/DatagenStore/LTEStreaming/ede_stream.log
			 
			 echo $totalE >> "$toolLog"/ede_stream.log
		  echo "DONE" >> "$toolLog"/ede_stream.log
         fi

			 
       else
          #Only post processing

		  # Waiting for signal to start streaming.
		  
          startLoop=1
          while [ $startLoop -eq 1 ]
          do
             cat "$toolLog"/ede_stream.log | grep "START"
             if [ $? -eq 0 ]
             then
                 startLoop=0
				 continue
             fi
	 
    	     sleep 10
			 echo "Waiting for the workflow to be initiated to start the Streaming..."
          done
	   
          echo "Cleaning up the EDE Log directory ..."
          rm -rf "$logs_path"/*

          echo "Cleaning up the Streaming temp directory ..."
          rm -rf "$intermediateFolder"/temp/*

          echo "Starting Post Processing step for Streaming data to Destination IP: " $destIP

          nohup ./startEdeTool.sh Postprocessing START &



          sleep 100
          logfile="$logs_path"/ede_controller.log
          if [ -f $logfile ]; then
             tail -1 "$logfile" | grep "ERROR: Controller.start"
             if [ $? -eq 0 ]
             then
                 echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
				 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                 KillEDE
                 continue
             fi
          else
             echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
			 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
             KillEDE
             continue
          fi

          RETRY2=1

          while [ $RETRY2 -eq 1 ]
          do
             #echo "Inside RETRY2"
             sleep 30
             cat "$logs_path"/ede_controller.log | grep "Pre-processing completed"
             if [ $? -eq 0 ]
             then
                 break
             else
                 tail -1 "$logs_path"/ede_controller.log | grep "ERROR"
                 if [ $? -eq 0 ]
                 then
                     SKIP=1
                     echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
					 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                     continue 2
                 fi
             fi
          done
      
          if [ $SKIP -eq 1 ]; then
             KillEDE
             continue
          fi
 
          echo "Execution done"

          while [ "$(ls -A "$intermediateFolder"/temp)" ]
          do
             echo "Waiting for Streaming to complete ..."
             sleep 30
             logfile="$logs_path"/ede_controller.log
             if [ -f $logfile ]; then
                 tail -1 "$logfile" | grep "ERROR"
                 if [ $? -eq 0 ]
                 then
                     echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
					 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                     SKIP=1
                     continue 2
                 fi
             else
                 echo "Error during Post processing/Streaming.. Quitting.. Check the Logs Path."
				 #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
                 SKIP=1
                 continue 2
             fi
          done

          if [ $SKIP -eq 1 ]; then
             #mv "$user_path"/UserInputXMLV1.0.xml_org "$user_path"/UserInputXMLV1.0.xml
			 KillEDE
             continue 
          fi

          echo "Post Processing/Streaming Successfully completed... Quitting the tool... Bbye"
          echo "Delete the nohup.out"
		  rm -rf nohup.out

		  count1=$(grep -i "Total number of events " /ede/EDE_multidest/ede-6.0.7-Release/DataAnalysis.txt | cut -d ":" -f 2)
		  count2=$(grep -i "OTHER_EVENTS" /ede/EDE_multidest/ede-6.0.7-Release/DataAnalysis.txt | cut -d ":" -f 2)
		  totalE=$(($count1 + $count2))
		  echo $totalE >> "$toolLog"/ede_stream.log
		  echo "DONE" >> "$toolLog"/ede_stream.log

       fi

   done
   

KillEDE
rm -f /tmp/abc.txt


