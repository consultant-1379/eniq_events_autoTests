#!/usr/local/bin/expect
 
#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>
 
 set timeout 60
 set arg1 [lindex $argv 0]
spawn su root -c "ssh dcuser@$arg1"

expect {
	"assword: " {
	send "shroot\n"
			expect {
				"assword: " {
				send "dcuser\n"
				expect {
					"#: " {
						send "cp /net/atclvm560.athtem.eei.ericsson.se/ede/LikeForLike/ede-16.A3-Release/cfg/user/CTR_FDNSourceIPMapping.txt /eniq/mediation_inter/M_E_CTRS/etc/ctrs_ip2fdnmap.txt; exit\n"
						send "exit\n"; 
					}
				}
			}
		}
	}
}

interact
