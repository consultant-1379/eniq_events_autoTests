#!/usr/local/bin/expect
set timeout -1
spawn su root -c "ssh atclvm560.athtem.eei.ericsson.se" 
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
			send "cd /net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin\n"
			expect { 
			"# " {
			send "./tshell.sh stream\n"
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
		send "cd /net/atclvm560.athtem.eei.ericsson.se/ede/EDE_multidest/ede-6.0.7-Release/bin\n"
		expect {
			"# " {
				send "./tshell.sh stream\n"
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
