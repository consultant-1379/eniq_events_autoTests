#!/bin/bash

newServer=$1
oldServer=`cat /eniq/home/dcuser/automation/atom_db/config_files/reference-server.prop | head -1 | nawk -F"Tds:" '{print $2}' | nawk -F".athtem" '{print $1}'`

sed "s/$oldServer/$newServer/" <  /eniq/home/dcuser/automation/atom_db/config_files/reference-server.prop > new.prop 


cp  new.prop /eniq/home/dcuser/automation/atom_db/config_files/reference-server.prop

rm new.prop
