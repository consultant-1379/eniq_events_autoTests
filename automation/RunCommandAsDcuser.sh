#!/usr/local/bin/expect
set timeout -1
spawn su - dcuser -c "$argv"
expect {
    "assword: " {
        send "dcuser\n"
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
