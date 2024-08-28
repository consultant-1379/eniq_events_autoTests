#!/usr/local/bin/expect
set timeout -1
spawn su root -c "$argv"
expect {
	"assword: " {
	send "shroot\n"
	expect {
		"#" { }
		}
	}
}
