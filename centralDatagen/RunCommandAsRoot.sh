#!/opt/sfw/bin/expect
set timeout -1
spawn su root -c "$argv"
expect {
	"assword: " {
	send "central1\n"
	expect {
		"> " { }
		"$ " { }
		"\} #: " { }
		"\} # " { }
		"^# " { }
		"assword: " { }
		}
	}
}
