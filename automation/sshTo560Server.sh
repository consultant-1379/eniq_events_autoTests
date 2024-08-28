#!/usr/local/bin/expect
set timeout -1
#spawn su root -c "ssh atclvm560.athtem.eei.ericsson.se" 
spawn su root -c "ssh atclvm560.athtem.eei.ericsson.se 'cd /ede/LikeForLike/ede-16.A3-Release/bin;./configureEDEonatclvm560.sh '"
expect {
	"assword: " {
	send "shroot\n"
	expect {
		"Are you sure you want to continue connecting (yes/no)?" {
			send "yes\n"
			expect {
			"assword: " {
			send "shroot12\n"
			expect {
			"# " {
			send "\n"
			expect { 
			"# " {
			send "\n"
			expect {
			"> " { }
			"$ " { }
			"\} #: " { }
			"\} # " { }
			}
			}
			}
			}
			}
			}
			}
	}
	"assword: " {
	send "shroot12\n"
	expect {
		"> " { }
		"$ " { }
		"\} #: " { }
		"\} # " { }
		"# " {
		send "\n"
		expect {
			"# " {
				send "\n"
				expect {
					"> " { }
					"$ " { }
					"\} #: " { }
					"\} # " { }
					
				}
			}
			
		}
		}
		}
	}
	}
}
}
