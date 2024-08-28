#!/usr/bin/bash

USER_NAME="$user_name"
PASSWD="$password"
GREP="grep -oE"
CLOUD="https://atvcloud3.athtem.eei.ericsson.se/"
CURL="curl -u"
VAPPNAME="$vApp_name"

echo $VAPPNAME
## Get the list of the Vapps in CI Cloud

        $CURL $USER_NAME:$PASSWD  --insecure "$CLOUD"Vapps/index_api/orgvdc_id:urn:vcloud:orgvdc:9ef76949-7c4a-43cf-86fe-b1223c382fe1/.xml  > /tmp/temp.xml

# Get the Vapp Id for the cloud whose CEP needs to be deleted

        VAPPID=`$GREP ".{0,150}"$VAPPNAME".{0,1}" temp.xml | $GREP  '.{0,1}vapp:.{1,100}' | cut -d "<" -f1 | cut -d ":" -f3`

   
## Get the List of Vms on the Vapp server

        $CURL $USER_NAME:$PASSWD --insecure "$CLOUD"Vms/vapp_index_api/vapp_id:urn:vcloud:vapp:$VAPPID/.xml > /tmp/cep.xml

##Get the CEP Id to delete

        CEPID=`$GREP ".{0,1}master_cep.{0,100}" cep.xml |  $GREP ".{0,1}vm:.{1,90}" | cut -d ":" -f3 | cut -d "<" -f1`


## Stop the Vapp beforre deletion

echo "Stopping  Vapp before CEP deletion . It may take some time ......."
       $CURL $USER_NAME:$PASSWD  --insecure "$CLOUD"Vapps/stop_vapp_api/vapp_id:urn:vcloud:vapp:$VAPPID/.xml

sleep 10m
# Now Delete the CEP

echo "Deleting CEP from the Vapp ....."
		$CURL $USER_NAME:$PASSWD --insecure "$CLOUD"Vms/delete_external_api/vm_id:urn:vcloud:vm:$CEPID/.xml

## Remove the temp files created
        rm -rf /tmp/temp.xml  /tmp/cep.xml