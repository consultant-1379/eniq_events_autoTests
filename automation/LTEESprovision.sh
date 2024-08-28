#!/usr/local/bin/expect
set timeout -1
spawn su dcuser -c "/eniq/mediation_inter/M_E_LTEES/bin/configureLTEEventsStatistics.sh -start"
expect {
	"assword: " {
	send "dcuser\n"
		expect {
			"#?" {
			send "3\n"
				expect {
					"continue " {
					send "y\n"
						expect {
							"will be disabled even if previously enabled" {
							send "1\n"
							sleep 10
							expect EOF
							}
						}
					}
				}
			}
		}
	}
}
