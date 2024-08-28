#!/bin/bash
#set -x 
home_path=$(pwd)
#logs_path=../logs
#user_path=$home_path/../cfg/user
user_path=/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/cfg/user
bin_dir=/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin
app_path=$home_path/../cfg/app
logs_path=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-6.0.7-Release/logs
xml_path=/net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-6.0.7-Release/cfg/user/UserInputXMLV1.0.xml
destinationIp=/net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin/destIpFoldersInfoList.txt


##########################################################################################################
# Function: Usage
#
# Description: This function display this tool usage information.
##########################################################################################################

KillEDE(){

   ps -ef | grep "/ede/LikeForLike/ede-6.0.7-Release/bin/startEdeTool.sh " | grep -v "grep" > 1.txt
   ps -ef | grep "/ede/LikeForLike/ede-6.0.7-Release/jre/bin/amd64/java " | grep -v "grep" >> 1.txt
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


editUserInputXml(){

cd $user_path

cp   UserInputXMLV1.0.xml UserInputXMLV1.0.xml_orig

oldIp=`grep IPAddress UserInputXMLV1.0.xml | grep -v 10.59.130.118  | cut -d ">" -f2 |  cut  -d "<" -f1  | head -1` 

echo " oldIp  $oldIp "
newIp=`cat $destinationIp | cut -d "#" -f1 `

echo "newIP $newIp "

sed "s/$oldIp/$newIp/" <  UserInputXMLV1.0.xml > new.xml

cp new.xml UserInputXMLV1.0.xml 

rm -rf  new.xml

echo "edited the user input xml " 
}



 #Start of Main Script 

echo " started Configuring EDE on atclvm560 " 

editUserInputXml
