#!/usr/local/bin/expect
 
#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>

set arg1 [lindex $argv 0]
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
						send "cd /eniq/home/dcuser/automation/;./EniqEventsRegress.sh priority/Config_loadTopologyLike4Like_PASS.txt; ./provisioning/auto_provision_LTE.pl;  exit\n"
						send "exit\n";
					}
				}
			}
		}
	}
}

interact