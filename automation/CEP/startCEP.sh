#!/usr/bin/bash
USER_NAME="$user_name"
PASSWD="$password"
GREP="/usr/bin/grep -oE"
CLOUD="https://atvcloud3.athtem.eei.ericsson.se/"
CURL="curl -u"
VAPPNAME="$vApp_name"
CLOUDNAME="9ef76949-7c4a-43cf-86fe-b1223c382fe1"


echo "NAS Mounting on CEP is not present . Re-starting CEP ..."
## Get the list of the Vapps in CI Cloud

        $CURL $USER_NAME:$PASSWD  --insecure "$CLOUD"Vapps/index_api/orgvdc_id:urn:vcloud:orgvdc:9ef76949-7c4a-43cf-86fe-b1223c382fe1/.xml  > /tmp/temp.xml

# Get the Vapp Id

        VAPPID=`$GREP ".{0,150}"$VAPPNAME".{0,1}" /tmp/temp.xml | $GREP  '.{0,1}vapp:.{1,100}' | cut -d "<" -f1 | cut -d ":" -f3`

# Get the List of Vms on the Vapp server

        $CURL $USER_NAME:$PASSWD --insecure "$CLOUD"Vms/vapp_index_api/vapp_id:urn:vcloud:vapp:$VAPPID/.xml > /tmp/cep.xml

##Get the CEP Id 

        CEPID=`$GREP ".{0,1}master_cep.{0,100}" /tmp/cep.xml |  $GREP ".{0,1}vm:.{1,90}" | cut -d ":" -f3 | cut -d "<" -f1`

## Stop the CEP

         $CURL $USER_NAME:$PASSWD  --insecure "$CLOUD"/Vms/power/vm_id:urn:vcloud:vm:$CEPID/vapp_id:urn:vcloud:vapp:$VAPPID/power_action:shutdown/orgvdc_id:urn:vcloud:orgvdc:"$CLOUDNAME"/.xml

sleep 5m


## Start the CEP
echo "Starting  CEP . It may take some time ......."
         $CURL $USER_NAME:$PASSWD  --insecure "$CLOUD"/Vms/power/vm_id:urn:vcloud:vm:$CEPID/vapp_id:urn:vcloud:vapp:$VAPPID/power_action:poweron/orgvdc_id:urn:vcloud:orgvdc:"$CLOUDNAME"/.xml

sleep 5m

## Remove the temp files created
        rm -rf /tmp/temp.xml /tmp/cep.xml