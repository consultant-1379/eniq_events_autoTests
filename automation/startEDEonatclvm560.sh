#!/usr/local/bin/expect 
 
#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>
 
set timeout 60
 
spawn su root -c "ssh dcuser@atclvm560.athtem.eei.ericsson.se"
expect {
	"assword: " {
	send "shroot\n"
			expect {
				"assword: " {
				send "central1\n"
				expect {
					"$ " {
						send "/ede/LikeForLike/ede-16.A3-Release/bin/addCronEntryLike4Like.pl; exit\n"
						send "exit\n";
					}
				}
			}
		}
	}
}

interact












