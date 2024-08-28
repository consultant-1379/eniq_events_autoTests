#!/bin/bash


source ~/.profile
command="perl ./wrapper.pl"
args=
for var in "$@"
do
    args="$args $var"
done


command="$command $args"
cd /eniq/home/dcuser/automation
$command
