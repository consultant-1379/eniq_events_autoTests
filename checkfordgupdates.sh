#!/bin/bash


#Run this as a cron job on the server ath-linux.lmera.ericsson.se.
#It monitors workflow zip files and notifies when updated



VIEWNAME=`/usr/atria/bin/cleartool lsview | grep ^\* | awk '{ print $2}' | grep $USER`
if [ $? -eq 1 ]
	then
	VIEWNAME=eseamor_eniq_dynamic
fi

#/usr/atria/bin/cleartool setview -exec "find /vobs/eniq_events/eniq_events_mg/test_tools -name workflows.zip | xargs ls -la" $VIEWNAME | awk '{$8=""; print}' >wfsnow.txt 2>/dev/null
/usr/atria/bin/cleartool setview -exec "find /vobs/eniq_events/eniq_events_mg/test_tools -name workflows.zip | xargs ls -tr" $VIEWNAME >wfsnow.txt 2>/dev/null
cd

/usr/bin/diff wfsprev.txt wfsnow.txt >/dev/null 2>&1
if [ $? -ne 0 ];then
	echo "Before:-----------------------------------------" > wfstemp.txt
	cat wfsprev.txt >> wfstemp.txt
	echo "After:------------------------------------------" >> wfstemp.txt
	cat wfsnow.txt >> wfstemp.txt
	mail -s "A datagen workflow file was updated in clearcase" john.j.keegan@ericsson.com damien.o.sullivan@ericsson.com patrick.garvey@ericsson.com seamus.morton@ericsson.com < wfstemp.txt
	cat wfsnow.txt > wfsprev.txt
	rm wfsnow.txt wfstemp.txt >/dev/null 2>&1
fi
