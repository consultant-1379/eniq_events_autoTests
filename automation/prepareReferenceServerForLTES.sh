#!/usr/local/bin/expect
 
#Usage sshsudologin.expect <host> <ssh user> <ssh password> <su user> <su password>

set arg1 [lindex $argv 0]
set timeout 60

set topologyDir "/eniq/data/eventdata/events_oss_1/lteTopologyData/dir{1..50}"
set outputDir  " /eniq/data/pmdata/eventdata/00/CTRS/lte_es/5min/events_oss_1/dir{1..50}"
set sourceTopologyLocation "/net/atclvm560/ede/LikeForLike/topology_for_Like_For_like/"
set destinationTopologyLocation "/eniq/data/eventdata/events_oss_1/lteTopologyData/dir1"   

spawn su root -c "ssh dcuser@$arg1"

expect {
	"assword: " {
	send "shroot\n"
			expect {
				"assword: " {
				send "dcuser\n"
				expect {
					"#: " {
						send " mkdir -p $topologyDir ; mkdir -p $outputDir; cp $sourceTopologyLocation/* $destinationTopologyLocation ; exit\n"
						send "exit\n";
					}
				}
			}
		}
	}
}

interact