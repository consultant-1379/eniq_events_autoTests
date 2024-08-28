#!/bin/ksh
user=`/usr/ucb/whoami`
if [ "$user" == "root" ];then
	if grep 'class="XDISPLAY" name="5"' /usr/openwin/server/etc/OWconfig >/dev/null; then
		cp /usr/openwin/server/etc/OWconfig /tmp/OWconfig
		cat /tmp/OWconfig | sed 's/class="XDISPLAY" name="5"/class="XDISPLAY" name="0"/' >/usr/openwin/server/etc/OWconfig
		rm /tmp/OWconfig
	elif ! egrep '^class="XDISPLAY" name="0"' /usr/openwin/server/etc/OWconfig >/dev/null; then
		cat <<-! >> /usr/openwin/server/etc/OWconfig
		# Virtual X Display
		class="XDISPLAY" name="0"
				coreKeyboard="NKBD" corePointer="NMOUSE"
				dev1="vfb";
		!
	fi
else
	cat > /tmp/exp.$$ <<DELIM
#!/usr/local/bin/expect
spawn su root -c "$0"
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