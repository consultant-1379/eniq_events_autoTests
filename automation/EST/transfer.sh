#!/usr/local/bin/expect

#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>

set arg1 [lindex $argv 0]
set arg2 [lindex $argv 1]
set arg3 [lindex $argv 2]
set timeout 60

spawn su root -c "scp --chmod=a+rwx $arg2 root@$arg1:$arg3"

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


