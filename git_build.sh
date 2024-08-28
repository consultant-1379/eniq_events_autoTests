#!/bin/bash

if [ "$2" == "" ]; then
    	echo usage: $0 \<Branch\> \<RState\>
    	exit -1
else
	versionProperties=install/version.properties
	theDate=\#$(date +"%c")
	module=$1
	branch=$2
	workspace=$3
	BUILD_USER_ID=$4
    	deliver=$5
    	reason=$6
    	CT=/usr/atria/bin/cleartool
	zipFile=${PWD}/target/ENIQ_EVENTS_AUTO_TESTS.zip
fi

function getReason {
        if [ -n "$reason" ]; then
                reason=`echo $reason | sed 's/$\ /x/'`
                reason=`echo JIRA:::$reason | sed s/" "/,JIRA:::/g`
        else
                reason="CI-DEV"
        fi
}

function getSprint {
        sprint=`cat ${PWD}/build.cfg | grep $module | grep $branch | awk -F " " '{print $5}'`
}

function getProductNumber {
        product=`cat ${PWD}/build.cfg | grep $module | grep $branch | awk -F " " '{print $3}'`
}


function setRstate {

        revision=`cat ${PWD}/build.cfg | grep $module | grep $branch | awk -F " " '{print $4}'`

        if git tag | grep $product-$revision; then
            build_num=`git tag | grep $revision | wc -l`

            if [ "${build_num}" -lt 10 ]; then
				build_num=0$build_num
			fi
			rstate=`echo $revision$build_num | perl -nle 'sub nxt{$_=shift;$l=length$_;sprintf"%0${l}d",++$_}print $1.nxt($2) if/^(.*?)(\d+$)/';`
		else
            ammendment_level=01
            rstate=$revision$ammendment_level
        fi
        echo "Building R-State:$rstate"

}

getSprint
getProductNumber
setRstate
git checkout $branch
git pull origin $branch

#add maven command here
mvn clean
mvn compiler:compile
mvn compiler:testCompile
mvn resources:resources
mvn dependency:copy-dependencies
mvn jar:jar
mvn assembly:single

rsp=$?

if [ $rsp == 0 ]; then
  
  echo "Copying ENIQ_EVENTS_AUTO_TESTS_$rstate.zip to /home/$USER/eniq_events_releases"
  cp ${zipFile} /home/$USER/eniq_events_releases/ENIQ_EVENTS_AUTO_TESTS_$rstate.zip
  echo "Copy comleted."
  if [ ${Branch} == master ]; then
	echo "SCP package to atclvm559.athtem.eei.ericsson.se (CI DATAGEN SERVER) at location /ossrc/package"
	scp /home/$USER/eniq_events_releases/ENIQ_EVENTS_AUTO_TESTS_$rstate.zip dcuser@atclvm559.athtem.eei.ericsson.se:/ossrc/package
	echo "SCP comepleted."
  fi
  git tag $product-$rstate
  git push --tag origin $branch

    echo "Running delivering..."
    getReason
    echo "Zip file: /home/$USER/eniq_events_releases/ENIQ_EVENTS_AUTO_TESTS_$rstate.zip"
    echo "Sprint: $sprint"
    echo "UserId: $BUILD_USER_ID"
    echo "Product Number: $product"
    echo "Running command: /vobs/dm_eniq/tools/scripts/deliver_eniq -auto events $sprint $reason N $BUILD_USER_ID $product NONE /home/$USER/eniq_events_releases/ENIQ_EVENTS_AUTO_TESTS_$rstate.zip"
    $CT setview -exec "/proj/eiffel013_config/fem101/jenkins_home/bin/lxb /vobs/dm_eniq/tools/scripts/deliver_eniq -auto events ${sprint} ${reason} N ${BUILD_USER_ID} ${product} NONE /home/$USER/eniq_events_releases/ENIQ_EVENTS_AUTO_TESTS_$rstate.zip" deliver_ui
fi

exit $rsp