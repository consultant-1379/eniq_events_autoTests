#!/usr/bin/bash
# -------------------------------------------------------------------
# Ericsson Network IQ ETLC engine control script
#
# Usage : change_glassfish_security.sh disable|enable|status"
#
# -------------------------------------------------------------------
# Copyright (c) 1999 - 2013 AB Ericsson Oy All rights reserved.
# --------------------------------------------------------------------

AWK=/usr/bin/awk
CP=/usr/bin/cp
ECHO=/usr/bin/echo
ED=/usr/bin/ed
EXPR=/usr/bin/expr
GREP=/usr/bin/grep
RM=/usr/bin/rm
SED=/usr/bin/sed


#######################
# SUPPORTING FUNCTIONS
#######################

check_file_presence(){
	IS_FILE_PRESENT=0
	fileName=$1
	if [ -r $fileName ]
	then
		IS_FILE_PRESENT=`$EXPR $IS_FILE_PRESENT + 1`
	else
		IS_FILE_PRESENT=`$EXPR $IS_FILE_PRESENT + 0`
	fi
}

remove_lock_file(){
	if [ -f $LOCK_FILE ]
        then
			log " Removing Log File before exiting: $LOCK_FILE "
			$RM -rf $LOCK_FILE 2>&1 > /dev/null
        fi
}

log(){
	mess=$1
	dTime=`date +'%m/%d/%Y %H:%M:%S'`
	term=`who am i | $AWK  -F' ' '{print $2}'`
	$ECHO " $dTime :: $term :: $mess " >> $GF_LOG_FILE
}

error_exit(){
	errStr=$1
	dTime=`date +'%m/%d/%Y %H:%M:%S'`
	term=`who am i | $AWK  -F' ' '{print $2}'`
	$ECHO " $dTime :: $term :: Error: $errStr " >> $GF_LOG_FILE
	$ECHO " Error: $errStr !!!!Exiting script.... "
	revert_domain_backup
	if [ -z ${GF_ADMIN_PASSWORD_FILE} ]
	then
		$ECHO "GF_ADMIN_PASSWORD_FILE is undefined" 
	else
		rm ${GF_ADMIN_PASSWORD_FILE}
	fi
	remove_lock_file
	exit 2
}

take_backup(){
	log "Taking backup"
	$CP $1 $1.backup 2>&1 > /dev/null
	status_02=`$ECHO $?`
	if [ $status_02 -eq 0 ]
	then
		log " Backup of original files has been taken. $1 ==> $1.backup "
		$ECHO " Backup of original files has been taken. "
	else
		$ECHO " Error : Failed to take the backup of original files. "
		log " Error: : Failed to take the backup. $1 ==> $1.backup "
		error_exit " Failed to take the backup.   $1 ==> $1.backup "
	fi
}

revert_back(){
	if [ -f $1.backup ]
	then
		$CP $1.backup $1 2>&1 > /dev/null
	fi
	status_02=`$ECHO $?`
	if [ $status_02 -eq 0 ]
    then
		log " Reverting back original files. $1.backup ==> $1 "
		$ECHO " Reverting back original files. "
	else
		log " Error : Failed to revert back the original files. $1.backup ==> $1 "
		$ECHO " Error : Failed to revert back the original files. "
	fi
}

copy_and_del_temp_file(){
	log " Replacement was successfull. Created the TMP_FILE: $1 with the changes. "
	log " Copying the TEMP_FILE: $1 to ORIG FILE: $2"
	$CP $1 $2
	log " Copied successfully. "
	if [ -f $1 ]
	then
		log " Removing the TEMP_FILE: $1 "
		$RM -rf $1 2>&1 > /dev/null
		log " Removed successfully."
	fi
}

replace_in_file(){

	take_backup $2
	
	# When asadmin updates domain.xml, it leaves the file with no final newline character
	# If this file is run through sed, the final line (closing </domain> tag) is lost
	#  and GlassFish will not start.
	# So... use ed to make sure there is a final newline character before doing the replace 
	# (ed opens and then saves the file, adding a final newline if required)
	$ED -s $2 <<< w

	# do the replace. rollback if there is a problem.
	$SED "$1" < $2 > $3
	status=`$ECHO $?`
	if [ $status -eq 0 ]
	then
		copy_and_del_temp_file $3 $2
	else
		revert_back $2
		error_exit " Error occur while enabling security in file : $2 " 
	fi
}	

call_asadmin(){
	if [ -z "$1" ]
	then
		error_exit "No parameter specified for call_asadmin" 
	else
	
		if [ -z "${GF_ADMIN_PASSWORD_FILE}" ]
		then
			error_exit "GF_ADMIN_PASSWORD_FILE is undefined" 
		else
			echo "AS_ADMIN_PASSWORD=admin" > ${GF_ADMIN_PASSWORD_FILE}
		
			${GF_ASADMIN_COMMAND} --port ${GF_ADMIN_PORT} -u ${GF_ADMIN_USER} --passwordfile ${GF_ADMIN_PASSWORD_FILE} $1
			asadmin_call_status=`$ECHO $?`
			if [ ${asadmin_call_status} -ne 0 ]
			then
				error_exit " Problem running asadmin command: $1 " 
			fi
		
			rm ${GF_ADMIN_PASSWORD_FILE}
		fi
	fi
}

restart_glassfish(){
	log " About to restart GlassFish... "
	${GF_COMMAND_FILE} restart
	glassfish_status=`$ECHO $?`
	if [ $glassfish_status -eq 0 ]
	then
		log " GlassFish restarted successfully. "
	else
		error_exit " Problem restarting GlassFish. " 
	fi
}

backup_domain_xml(){
	TIMESTAMP=`date +%d.%m.%y_%H:%M:%S`
	GF_DOMAIN_FILE_BACKUP="${GF_DOMAIN_FILE}.${TIMESTAMP}"
	
	$CP ${GF_DOMAIN_FILE} ${GF_DOMAIN_FILE_BACKUP}
	backup_status=`$ECHO $?`
	if [ ${backup_status} -ne 0 ]
	then
		error_exit " Problem backing up domain.xml to file: ${GF_DOMAIN_FILE_BACKUP} " 
	else
		log " Backed up domain.xml to file: ${GF_DOMAIN_FILE_BACKUP} "
	fi
}

revert_domain_backup(){

	if [ -z ${GF_DOMAIN_FILE_BACKUP} ]
	then
		log "No domain.xml backup file defined." 
	else
		if [ -f ${GF_DOMAIN_FILE_BACKUP} ]
		then
			$CP ${GF_DOMAIN_FILE_BACKUP} ${GF_DOMAIN_FILE} 2>&1 > /dev/null
		fi
		rollback_status=`$ECHO $?`
		if [ ${rollback_status} -eq 0 ]
		then
			log " Reverting domain.xml back to previous version. ${GF_DOMAIN_FILE_BACKUP} ==> ${GF_DOMAIN_FILE} "
			$ECHO " Reverting domain.xml back to previous version. "
		else
			$ECHO " Error : Failed to revert domain.xml back to previous version. "
			log " Error : Failed to revert domain.xml back to previous version. ${GF_DOMAIN_FILE_BACKUP} ==> ${GF_DOMAIN_FILE} "
		fi
	fi

}

#######################
# MAIN WORKER FUNCTIONS
#######################

gf_is_glassfish_server(){
	IS_GLASSFISH_SERVER=`cat $CONF_DIR/service_names | grep "glassfish" | grep \`/usr/bin/hostname\` | wc -l`
}


#####
# check if ssl has been configured for GlassFish. 
#####
gf_check_ssl(){
	IS_SSL_SETUP=`$KEYTOOL -list -alias $SSL_ALIAS -keystore $KEYSTORE -storepass $STOREPASS | $GREP "Certificate fingerprint" | wc -l`
}

#####
# Set up ssl for GlassFish
#####
gf_setup_ssl(){
	FULLNAME=`echo \`$HOST $HOSTNAME\` | awk '{print $1;}'`

	gf_check_ssl
	if [ ${IS_SSL_SETUP} -eq 1 ]
	then
		error_exit " SSL is already set up. "
	else
		log " Setting up SSL"
		$ECHO " Setting up SSL"

		log "Generating JKS Keystore"	
		$KEYTOOL -genkeypair -keystore $KEYSTORE -storepass $STOREPASS -keypass $KEYPASS -alias $SSL_ALIAS -keysize 2048 -keyalg RSA -sigalg SHA256withRSA -dname "CN=$FULLNAME" -validity 3650

		log "Generating Certificate Signing Request"
		$KEYTOOL -certreq -keystore $KEYSTORE -storepass $STOREPASS -keypass $KEYPASS -alias $SSL_ALIAS -storetype JKS -file $CSRFILE

		log "Convert the JKS keystore into PKCS#12 format"
		$KEYTOOL -importkeystore -srckeystore $KEYSTORE -destkeystore $KEYSTORE_PKCS12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass $STOREPASS -deststorepass $STOREPASS -srcalias $SSL_ALIAS -destalias $SSL_ALIAS -srckeypass $KEYPASS -destkeypass $KEYPASS

		log "Export private key from the PKCS#12 keystore by openssl"
		$OPEN_SSL pkcs12 -in $KEYSTORE_PKCS12 -nocerts -out $PRIVATE_KEY_FILE -passin pass:$STOREPASS -passout pass:$STOREPASS

		log " Done - SSL Setup Complete"
		$ECHO " Done - SSL Setup Complete"
	fi
}

#####
# check if https has been configured for GlassFish
#####

gf_check_https_setup() {
	IS_HTTPS_SETUP=`cat $GF_DOMAIN_FILE | grep "protocol=\"https-redirect\"" | wc -l`
}

#####
# Set up https for GlassFish BUT DOES NOT ENABLE HTTPS.
#####
gf_setup_https(){
	log " About to set up HTTPS & Port Unification on GlassFish. "

	if [ $IS_ALREADY_RUNNING -eq 1 ]
        then
			$ECHO "One instance of this process is already running. Can not continue..."
			exit 3
    fi

	gf_check_https_setup
	if [ $IS_HTTPS_SETUP -eq 1 ]
	then
		error_exit " HTTPS is already set up. "
	else
	
		backup_domain_xml
		
		# set up ssl cert on listener 2 and enable https
		log " Setting up SSL & HTTPS on http-listener-2. "
		call_asadmin 'set server.network-config.protocols.protocol.http-listener-2.security-enabled=false'
		call_asadmin 'delete-ssl --type http-listener http-listener-2'
		call_asadmin 'create-ssl --type http-listener --certname eniq http-listener-2'
		call_asadmin 'set server.network-config.protocols.protocol.http-listener-2.security-enabled=true'

		# set up port unification
		log " Setting up port unification - http to https. "
		call_asadmin 'create-protocol --securityenabled=false http-redirect'
		call_asadmin 'create-http-redirect --redirect-port 8181 --secure-redirect true http-redirect'
		call_asadmin 'create-protocol --securityenabled=false pu-protocol-HTTP-HTTPS'
		call_asadmin 'create-protocol-finder --protocol pu-protocol-HTTP-HTTPS --targetprotocol http-listener-2 --classname com.sun.grizzly.config.HttpProtocolFinder http-finder'
		call_asadmin 'create-protocol-finder --protocol pu-protocol-HTTP-HTTPS --targetprotocol http-redirect --classname com.sun.grizzly.config.HttpProtocolFinder http-redirect'

		log " Setting up port unification - https to http. "
		call_asadmin 'create-protocol --securityenabled=true https-redirect'
		call_asadmin 'create-http-redirect --redirect-port 18080 --secure-redirect false https-redirect'
		call_asadmin 'create-ssl --certname eniq --type network-listener --ssl2enabled=false --ssl3enabled=false --tlsenabled=true --tlsrollbackenabled=true --clientauthenabled=false https-redirect'
		call_asadmin 'create-protocol --securityenabled=false pu-protocol-HTTPS-HTTP'
		call_asadmin 'create-protocol-finder --protocol pu-protocol-HTTPS-HTTP --targetprotocol http-listener-1 --classname com.sun.grizzly.config.HttpProtocolFinder http-finder'
		call_asadmin 'create-protocol-finder --protocol pu-protocol-HTTPS-HTTP --targetprotocol https-redirect --classname com.sun.grizzly.config.HttpProtocolFinder https-redirect'

		# initially set glassfish to http
		call_asadmin 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-1.protocol=http-listener-1'
		call_asadmin 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-2.protocol=pu-protocol-HTTPS-HTTP'
		
		CHANGED_HTTPS_CONFIG=1
		log " Done - SSL & HTTPS is now set up (but is not yet enabled)."
		$ECHO " Done - SSL & HTTPS is now set up (but is not yet enabled). "
	fi
}

#####
# Get https status for GlassFish
#####
gf_check_http_status() {
	IS_HTTPS_ENABLED=0
	is_enabled=`cat $GF_DOMAIN_FILE | grep "ENIQ_EVENTS_SERVICES_URI\" value=\"https" | wc -l`
	if [ $is_enabled -ge 1 ]
	then
		IS_HTTPS_ENABLED=`$EXPR $IS_HTTPS_ENABLED + 1`
	else
		IS_HTTPS_ENABLED=`$EXPR $IS_HTTPS_ENABLED + 0`
	fi
}

#####
# Report https status 
#####
gf_http_status(){
	log " About to check HTTPS status. "
	gf_check_http_status
	if [ $IS_HTTPS_ENABLED -ge 1 ]
	then
		log " HTTPS is enabled. HTTP connections will be redirected to HTTPS. "
		$ECHO " HTTPS is enabled. HTTP connections will be redirected to HTTPS. "
	else
		log " HTTPS is disabled. HTTPS connections will be redirected to HTTP. "
		$ECHO  " HTTPS is disabled. HTTPS connections will be redirected to HTTP. "
	fi
}

#####
# Check that both ssl and https are set up. If not, run setup
#####
check_ssl_http_setup() {
	gf_check_ssl
	if [ ${IS_SSL_SETUP} -eq 0 ]
	then
		log " SSL has not been set up. Running setup now. "
		gf_setup_ssl
	fi

	gf_check_https_setup
	if [ $IS_HTTPS_SETUP -eq 0 ]
	then
		log " HTTPS has not been set up. Running setup now. "
		gf_setup_https
	fi
}

#####
# Enable https 
#####
https_enable(){
	log " About to enable HTTPS. "

	if [ $IS_ALREADY_RUNNING -eq 1 ]
        then
			$ECHO "One instance of this process is already running. Can not continue..."
			exit 3
    fi

	# make sure ssl and https are set up before attempting to change the configuration
	check_ssl_http_setup
	
	gf_check_http_status
	if [ $IS_HTTPS_ENABLED -eq 1 ]
	then
		error_exit " HTTPS is already enabled. "
	else
	
		backup_domain_xml
		
		# switch glassfish to https
		call_asadmin 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-1.protocol=pu-protocol-HTTP-HTTPS'
		call_asadmin 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-2.protocol=http-listener-2'

		# enable secure admin - force https for GlassFish Admin Console
		call_asadmin 'enable-secure-admin'
		
		# change URLs in domain.xml
		log " Replacing HTTP URLs in domain.xml "
		replace_in_file 's/18080\/geoserver/8181\/geoserver/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/GEO_SERVER_URI\" value=\"http\:/GEO_SERVER_URI\" value=\"https:/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/GEO_SERVER_URI\" value=\"http\:/GEO_SERVER_URI\" value=\"https:/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/BIS_SOAP_SERVICE_URL\" value=\"http\:\/\/bisserver\:8080/BIS_SOAP_SERVICE_URL\" value=\"https\:\/\/bisserver\:8443/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/18080\/EniqEventsServices/8181\/EniqEventsServices/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/ENIQ_EVENTS_SERVICES_URI\" value=\"http\:/ENIQ_EVENTS_SERVICES_URI\" value=\"https:/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		
		restart_glassfish

		log " Done - HTTPS is now enabled. HTTP connections will be redirected to HTTPS. "
		$ECHO " Done - HTTPS is now enabled. HTTP connections will be redirected to HTTPS. "
	fi
}

#####
# Disable https 
#####
https_disable() {
	log " About to disable HTTPS. "

	if [ $IS_ALREADY_RUNNING -eq 1 ]
        then
			$ECHO "One instance of this process is already running. Can not continue..."
			exit 3
    fi

	# make sure ssl and https are set up before attempting to change the configuration
	check_ssl_http_setup
	
	gf_check_http_status
	if [ $IS_HTTPS_ENABLED -eq 0 ]
	then
		if [ $CHANGED_HTTPS_CONFIG -eq 1 ]
		then
			# HTTPS configuration has been updated (initial set up), so restart
			restart_glassfish
		else 
			error_exit " HTTPS is already disabled. "
		fi
	else
	
		backup_domain_xml
		
		# switch glassfish to http
		call_asadmin 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-1.protocol=http-listener-1'
		call_asadmin 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-2.protocol=pu-protocol-HTTPS-HTTP'
		
		# NOTE: For GlassFish 3.1.2, access to the GlassFish Admin console must be secure and use HTTPS - so don't disable it...

		# change URLs in domain.xml
		log " Replacing HTTPS URLs in domain.xml "
		replace_in_file 's/8181\/geoserver/18080\/geoserver/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/GEO_SERVER_URI\" value=\"https:/GEO_SERVER_URI\" value=\"http\:/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/BIS_SOAP_SERVICE_URL\" value=\"https\:\/\/bisserver\:8443/BIS_SOAP_SERVICE_URL\" value=\"http\:\/\/bisserver\:8080/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/8181\/EniqEventsServices/18080\/EniqEventsServices/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		replace_in_file 's/ENIQ_EVENTS_SERVICES_URI\" value=\"https:/ENIQ_EVENTS_SERVICES_URI\" value=\"http\:/g' $GF_DOMAIN_FILE $TEMP_DOMAIN_FILE
		
		restart_glassfish
		
		log " Done - HTTPS is now diabled. HTTPS connections will be redirected to HTTP. "
		$ECHO " Done - HTTPS is now diabled. HTTPS connections will be redirected to HTTP. "
	fi

}

#######################
# Processing starts here
#######################

#####
# Global Variables
#####

LOCK_FILE='/eniq/sw/installer/.gflock.tmp'

HOST=/usr/sbin/host 	
HOSTNAME=`/usr/bin/hostname`

GF_COMMAND_FILE='/eniq/sw/bin/glassfish'

GF_LOG_FILE='/eniq/log/sw_log/glassfish/security.log'
GF_DIR='/eniq/glassfish/glassfish3'

GF_ASADMIN_COMMAND="${GF_DIR}/bin/asadmin"
GF_ADMIN_PORT="14848"
GF_ADMIN_USER="admin"
GF_ADMIN_PASSWORD_FILE="/tmp/gfpwdfile.txt"

GF_DOMAIN_FILE="${GF_DIR}/glassfish/domains/domain1/config/domain.xml"
GF_DOMAIN_FILE_BACKUP=""
TEMP_DOMAIN_FILE="/tmp/domain.xml.tmp"

KEYTOOL="${RT_DIR}/java/bin/keytool"
KEYSTORE="${GF_DIR}/glassfish/domains/domain1/config/keystore.jks"
KEYSTORE_PKCS12="${GF_DIR}/glassfish/domains/domain1/config/keystore.pkcs12"
STOREPASS='changeit'
KEYPASS='changeit'
SSL_ALIAS='eniq'

CSRFILE="${GF_DIR}/glassfish/domains/domain1/config/$HOSTNAME.csr"
PRIVATE_KEY_FILE="${GF_DIR}/glassfish/domains/domain1/config/$HOSTNAME-private.pem"

OPEN_SSL="/usr/sfw/bin/openssl"

IS_SSL_SETUP=1
IS_HTTPS_SETUP=1
IS_FILE_PRESENT=0
IS_HTTPS_ENABLED=0
CHANGED_HTTPS_CONFIG=0
IS_ALREADY_RUNNING=0
IS_GLASSFISH_SERVER=0

#####
# Check User. Only dcuser should be allowed to run the script
#####
isDCUSER=`id | $AWK -F' ' '{print $1}' | grep -i dcuser | wc -l`
isDCUSER=`$EXPR $isDCUSER + 0`
if [ $isDCUSER -ne 1 ]
then
	$ECHO " This script can be run only as dcuser. "
	exit 5
fi

#####
# Check Arguments
#####
if [ $# -ne 1 ]
then
        $ECHO "Usage: change_glassfish_security.sh enable|disable|ssl|https|status "
        exit 4
fi

#####
# Check if already running
#####
check_file_presence $LOCK_FILE
if [ $IS_FILE_PRESENT -eq 1 ]
then
	IS_ALREADY_RUNNING=`$EXPR $IS_ALREADY_RUNNING + 1`
else
        touch $LOCK_FILE
        log " Created log file: $LOCK_FILE to know the running instance of this script. "
fi

#####
# Run requested option
#####
case "$1" in 
enable)
	https_enable
     	;;
disable)
	https_disable
     	;;
ssl)
	gf_check_ssl
	if [ ${IS_SSL_SETUP} -eq 0 ]
	then
		gf_setup_ssl
	else
		error_exit " SSL is already set up. "
	fi
     	;;
https)
	gf_check_https_setup
	if [ $IS_HTTPS_SETUP -eq 0 ]
	then
		gf_setup_https
	else
		error_exit " HTTPS is already set up. "
	fi
     	;;
status) 
	gf_http_status
     	;;
*) 
	$ECHO " Usage : change_glassfish_security.sh enable|disable|ssl|https|status "
   	remove_lock_file
   	exit 10
		;; 
esac

remove_lock_file
