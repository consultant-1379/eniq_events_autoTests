#!/opt/sfw/bin/expect -f

set timeout 30

set server [lindex $argv 0]
set user [lindex $argv 1]
set pass [lindex $argv 2]

spawn ssh $user@$server "source ~/.profile;/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 -Ddwhdb -Sdwhdb -w 50 -b <<EOF
select count(distinct hierarchy_3) from dim_e_sgeh_hier321 union all 
select count(distinct hierarchy_3) from dim_z_sgeh_hier321 union all
select count(distinct hierarchy_3) from dim_e_lte_hier321 union all 
select count(distinct sgsn_name) from dim_e_sgeh_sgsn_d
select CALL_POSITION from DIM_E_MSS_CALL_POSITION
select distinct mnc from dim_e_sgeh_hier321
go
quit
EOF
"
expect {
  "> " { }
  "$ " { }
  "\} #: " { }
  "\} # " { }
   "^# " { }
  "assword: " {
        send "$pass\n"
		expect {
			"> " { }
			"$ " { }
			"\} #: " { }
			"\} # " { }
			"^# " { }
			"assword: " { exit 1}
		}
  }
  "(yes/no)? " {
        send "yes\n"
		expect {
		"assword:" {
			send "$pass\n"
			expect {
				"> " { }
				"$ " { }
				"\} #: " { }
				"\} # " { }
				"^# " { }
				"assword: " { exit 1}
			}
		}
		expect {
			"> " { }
			"$ " { }
			"\} #: " { }
			"\} # " { }
			"^# " { }
		}
	}
  }
}
