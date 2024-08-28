#!/bin/bash
##########################################################################################################
# Function: Usage
#
# Description: This function display this tool usage information.
##########################################################################################################

echo $2
EDE="$1"
DIR1="$2"
DIR2="$3"
DIR3="$4"
BIN="bin"
echo $EDE
KillEDE(){
  cat 1.txt | while read line
   do
        pide=`echo $line |  awk '{print $2}'`
     kill -9 $pide
  done

  rm -f 1.txt
      }

Share_dir(){
        echo "Sharing the directories"
    share $DIR1
    share $DIR2
    share $DIR3
}



Update_Cron(){
        min_factor=60
        current_min=`date | awk '{print $4}' | cut -d ":" -f2`
        current_hr=`date | awk '{print $4}' | cut -d ":" -f1`
        min_diff=`expr $min_factor - $current_min`
        today=`date |  awk '{print $3}'`

        if [  $min_diff -lt  2 ]; then
                min="02"
                hr=`expr $current_hr + 1 `
        else
                min=`expr $current_min + 2`
                hr="$current_hr"
        fi

        echo "$EDE/$BIN/startEdeTool.sh"

      crontab -l | grep -v "$EDE/$BIN/startEdeTool.sh"  > crontemp.txt

      echo "$min $hr $today * * $EDE/$BIN/startEdeTool.sh POSTPROCESSING START  &> /dev/null 2>&1" >> crontemp.txt

      crontab crontemp.txt
        rm -rf crontemp.txt

 if [ $? -eq 0 ]
  then
 echo "Made the following cron entry $min $hour $today * * $EDE/$BIN/startEdeTool.sh POSTPROCESSING START  "
 fi
        }


Add_Swap(){
        swap -a /dev/zvol/dsk/eniq_sp_1/connectd/swap1
        swap -a /dev/zvol/dsk/eniq_sp_1/swap1
        swap -a /dev/zvol/dsk/eniq_sp_1/swap
}






      ###Main Script

        ps -ef | grep "$BIN/bin/startEdeTool.sh" | grep -v "grep" > 1.txt
#       ps -ef | grep "$EDE_LOCATION/jre/bin/amd64/java " | grep -v "grep" >> 1.txt

    if [ -s 1.txt ] ;
    then
        echo "EDE is already Running. Killing the the current session "
                KillEDE
    else
        echo "No process is running"
    fi

        Update_Cron
        Share_dir
#       Add_Swap
