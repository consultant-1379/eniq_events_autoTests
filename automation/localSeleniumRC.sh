#!/bin/ksh

seleniumDir=/eniq/home/dcuser/selenium/`ls -t /eniq/home/dcuser/selenium | head -1`/vendor
seleniumJar=`ls -t $seleniumDir/selenium-server-standalone-2.$3.0.jar | head -1`
concurrent_present='/tmp/concurrent_halt'


arg1=$1
arg2=$2
myDirectory=/eniq/home/dcuser/automation


spawn_xvfb() {
  export DISPLAY=`hostname`:0.0
  ps -ef | grep 'Xsu[n] :0' >/dev/null && return
  /usr/openwin/bin/Xvfb :0 >> /tmp/xvfb.log 2>&1 &
  chmod 777 /tmp/xvfb.log 2>/dev/null
  XVFB=$!
}
echo "-----------------------------------"
echo $(date +"%Y-%m-%d %H:%M:%S")
echo "INFO:Arguments: $arg1 $arg2"
if [ "$arg1" == "kill" ]; then
	echo "INFO:Processes to kill:"
	ps -ef | egrep 'SeleniumStarter|firefox|Xsun :[05]|selenium' | egrep -v 'grep|undofirefoxfix'

	ps -ef | grep SeleniumStarter | grep -v grep | awk '{print $2}' | xargs kill -9 >/dev/null 2>&1
	ps -ef | grep selenium | grep -v grep | awk '{print $2}' | xargs kill -9 >/dev/null 2>&1
	ps -ef | grep firefox | egrep -v 'grep|undofirefoxfix' | awk '{print $2}' | xargs kill -9 >/dev/null 2>&1
	ps -ef | grep -i "Xsun :[05]" | grep -v grep | awk '{print $2}' | xargs kill -9 >/dev/null 2>&1
	sleep 5
	echo "INFO:Processes left after kill(if any):"
	ps -ef | egrep 'SeleniumStarter|firefox|Xsun :[05]|selenium' | egrep -v 'grep|undofirefoxfix'
	cd /eniq/home/dcuser/automation/selenium_grid_files; /eniq/sw/runtime/java/bin/java RunSeleniumClient stopUnix >/dev/null 2>&1;/eniq/sw/runtime/java/bin/java stopHub >/dev/null 2>&1
	cd - >/dev/null
	if [ "$arg2" == "undofirefoxfix" ]; then
		echo "INFO:Undoing firefox fix"
		chmod 755 $myDirectory/fixfirefox.sh
		$myDirectory/fixfirefox.sh undo
	fi
else
	if [ ! -e "$concurrent_present" ]; then
	ps -ef | grep "thing=server" | grep -v grep >/dev/null 2>&1 && ps -ef | grep "Xsun :0" | grep -v grep >/dev/null 2>&1 && echo "INFO: Already running. Exitting" && exit 0
	fi
	sleep 1
	echo "INFO:Updating OWconfig"
	chmod 755 $myDirectory/updateowconfig.sh
	$myDirectory/updateowconfig.sh
	spawn_xvfb
	echo "INFO:Fixing firefox"
	chmod 755 $myDirectory/fixfirefox.sh
	$myDirectory/fixfirefox.sh
	echo "INFO:Launching selenium server:$seleniumJar"
	port=4566
	if [ "$arg1" == "-port" ];then
		port=$arg2
	fi
	/eniq/sw/runtime/java/bin/java -Dthing=server -jar $seleniumJar -port $port -trustAllSSLCertificates
fi
