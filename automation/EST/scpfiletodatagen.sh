#!/usr/local/bin/expect

#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>

set arg1 [lindex $argv 0]
set timeout 60

spawn su root -c "scp ../config.prop root@$arg1:/"

expect {
        "assword: " {
        send "shroot\n"
                        expect {
                                "assword: " {
                                send "shroot\n"
                                expect {
                                        "#: " {

                                                send "exit\n";
                                        }
                                }
                        }
                }
        }
}
