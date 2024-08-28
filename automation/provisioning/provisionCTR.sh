#!/usr/local/bin/expect
set timeout -1
spawn su dcuser -c "/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctr -e create"
expect {
        "assword: " {
                send "dcuser\n"

                        expect {
                                "number of OSS IDs:" {
                                        send "1\n"

                                        expect {
                                                "OSS ID:" {
                                                send "\n"

                                                        expect {
                                                                "values right?" {
                                                                send "Y\n"

                                                                        expect {
                                                                                "(localtime + offset)" {
                                                                                send "1\n"

                                                                                        expect {
                                                                                                "or Enter to skip:" {
                                                                                                send "300\n"

                                                                                                        expect {
                                                                                                                "E.G. E.G. 4000-4004,4009" {
                                                                                                                send "4000-4009\n"
                                                                                                                        expect {
                                                                                                                                "right for ec_st_1? " {
                                                                                                                                send "Y\n"
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
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
