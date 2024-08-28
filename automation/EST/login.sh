#!/usr/local/bin/expect
 
#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>

set arg1 [lindex $argv 0]
set arg2 [lindex $argv 1]
set arg3 [lindex $argv 2]
set timeout 60
 

spawn su root -c "ssh dcuser@$arg1"

expect {
	"assword: " {
	send "shroot\n"
			expect {
				"assword: " {
				send "dcuser\n"
				expect {
					"#: " {
							send "cd /eniq/home/dcuser/automation/;./EniqEventsRegress.sh priority/Config_ESTDTGEN_PASS.txt;exit\n"
							send "exit\n";
					}
				}
			}
		}
	}
}

interact
