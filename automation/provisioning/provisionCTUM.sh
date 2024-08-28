#!/usr/local/bin/expect
set timeout -1
spawn su dcuser -c "/eniq/mediation_inter/M_E_CTRS/bin/streaming_admin.sh -f ctum -e create"
expect {
        "assword: " {
                send "dcuser\n"

                expect {
                        "(localtime + offset)" {
                                send "1\n"

                                expect {
                                        "E.G. E.G. 4010-4014,4019" {
                                                send "4010-4019\n"

                                                expect {
                                                        "above values right for ec_st_1?" {
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
