#!/usr/local/bin/expect
 
#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>

set arg1 [lindex $argv 0]
set timeout 60
 
#spawn su root -c "ssh dcuser@atclvm680.athtem.eei.ericsson.se"
spawn su root -c "ssh dcuser@$arg1"

expect {
	"assword: " {
	send "shroot\n"
			expect {
				"assword: " {
				send "dcuser\n"
				expect {
					"#: " {
						send "/eniq/home/dcuser/automation/EniqEventsRegress.sh /eniq/home/dcuser/automation/priority/Config_2G_3G_SGEH_Like4Like_PASS.txt;exit\n"
						send "exit\n";
					}
				}
			}
		}
	}
}

interact
