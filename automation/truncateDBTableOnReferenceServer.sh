#!/usr/local/bin/expect
set timeout -1 

set arg1 [lindex $argv 0]

                                                                          
spawn su root -c "ssh $arg1 'cd /eniq/home/dcuser/automation/ ;./truncateLikeForLikeTable.pl'" 
expect {
	"assword: " {
	send "shroot\n"
	expect {
		"Are you sure you want to continue connecting (yes/no)?" {
			send "yes\n"
			expect {
			"assword: " {
			send "dcuser\n"
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
	send "shroot\n"
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