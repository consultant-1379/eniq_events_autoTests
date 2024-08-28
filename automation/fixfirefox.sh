#!/bin/ksh
user=`/usr/ucb/whoami`
myDirectory=/eniq/home/dcuser/automation
if [ "$user" == "root" ];then
	firefoxDir=/usr/lib/firefox

	if [ "$1" == "undo" ]; then
		if [ -f $firefoxDir/firefox-binselenium ]; then
			orig=`ls -t $firefoxDir/run-mozilla.shbkup* 2>/dev/null| head -1`
			[ ${#orig} -gt 0 ] && mv $orig $firefoxDir/run-mozilla.sh
			rm $firefoxDir/firefox-bin
			mv $firefoxDir/firefox-binselenium $firefoxDir/firefox-bin
		else
			echo "INFO:No changes to undo. Quitting" | tee -a $myDirectory/localSeleniumLog.log
		fi
	else
		if [ ! `grep "selenium" $firefoxDir/run-mozilla.sh` ]; then
			dat=$(date +"%Y%m%d%H%M%S")
			ls -l $firefoxDir/run-mozilla.sh | grep 10100 >/dev/null 2>&1
			if [ $? -ne 0 ]; then
				if [ `grep -c "if \\[ ! \\-x \\"\\$MOZ_PROGRAM\\" \\]" $firefoxDir/run-mozilla.sh` -eq 1 ]; then
					echo "WARNING: $firefoxDir/run-mozilla.sh is not the expected version. Continuing anyway" | tee -a $myDirectory/localSeleniumLog.log
					echo "$firefoxDir/run-mozilla.sh renamed to $firefoxDir/run-mozilla.shbkup$dat" | tee -a $myDirectory/localSeleniumLog.log
				else
					echo "ERROR:$firefoxDir/run-mozilla.sh does not contain the expected text. Quitting" | tee -a $myDirectory/localSeleniumLog.log
					exit 1
				fi
			fi
			cat $firefoxDir/run-mozilla.sh | sed "s|^if \[ ! \-x \"\$MOZ_PROGRAM\" \]|MOZ_PROGRAM=$firefoxDir/firefox-binselenium&|" | sed 's/binselenium/&\
/' 			> /tmp/runmoz.$$
			mv $firefoxDir/run-mozilla.sh $firefoxDir/run-mozilla.shbkup$dat
			mv /tmp/runmoz.$$ $firefoxDir/run-mozilla.sh
			chown root:root $firefoxDir/run-mozilla.sh
			chmod 755 $firefoxDir/run-mozilla.sh
		else
			echo "INFO:Changes already made. Quitting" | tee -a $myDirectory/localSeleniumLog.log
			exit 1
		fi
		if [ ! -f $firefoxDir/firefox-binselenium ]; then
			ls -la $firefoxDir/firefox-bin | grep "^l" >/dev/null 2>&1
			if [ $? -ne 0 ]; then
				mv $firefoxDir/firefox-bin $firefoxDir/firefox-binselenium
				ln -s $firefoxDir/firefox $firefoxDir/firefox-bin
			else
				echo "ERROR: $firefoxDir/firefox-bin is already a symbolic link" | tee -a $myDirectory/localSeleniumLog.log
			fi
		fi
	fi
else
	cat > /tmp/exp.$$ <<DELIM
#!/usr/local/bin/expect
spawn su root -c "$0 $1"
expect {
	"assword: " {
	send "shroot\n"
	expect {
		"> " { }
		"$ " { }
		"\} #: " { }
		"\} # " { }
		"^# " { }
		"assword: " { exit 1}
		}
	}
}
DELIM
	chmod 755 /tmp/exp.$$
	/tmp/exp.$$
	rm /tmp/exp.$$
fi
