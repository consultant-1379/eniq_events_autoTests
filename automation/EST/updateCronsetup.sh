#!/bin/bash

noOfRepetation=$1
server=$2
i=0    
timeFactor=24
startTime=`date "+%Y-%m-%d %H:%M:%S"`
updateDate () {


		hour=`date | cut -d ' ' -f4 |cut -d ':' -f1`
        hour=`expr $hour + 2`
        if [ "$hour" -gt "$timeFactor" ] ; then
                hour=`expr $hour - $timeFactor`
        fi


        min=`date | cut -d ' ' -f4 | cut -d ':' -f2`
        today=`date | cut -d ' ' -f3`

        while [ $i -lt  $noOfRepetation ]
                do
                var=`TZ=CST-$timeFactor date +%m/%d/%Y | cut -d "/" -f2`
                timeFactor=`expr $timeFactor + 24`

                if [ "$i" -eq 0 ] ; then
                        baseday=$var
                else
                        baseday=$baseday","$var
                fi



        i=`expr $i + 1`
        done

        }

#call the function to upadte the current time
updateDate

       
                crontab -l | grep -v runProfiler.sh > crontemp.txt
       
             
        

echo "$min $hour $baseday * * /eniq/home/dcuser/automation/EST/runProfiler.sh $server $noOfRepetation $startTime  &> /dev/null 2>&1" >> crontemp.txt

crontab crontemp.txt

		if [ $? -eq 0 ]
			then
		echo " Made the following cron entry $min $hour $baseday * * /eniq/home/dcuser/automation/EST/runProfiler.sh $server $noOfRepetation $startTime " 
		fi


## Remove temp file 

rm -rf crontemp.txt
