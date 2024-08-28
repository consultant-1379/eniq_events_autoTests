Hi, if you are reading this something must have gone wrong.
I will try put all the information here needed to configure this virtual server stuff

Here is a WIKI to help: http://atrclin2.athtem.eei.ericsson.se/wiki/index.php/Setting_Up_A_DataGen_Server


#CRONTAB

Add the following to the crontab

58 * * * * /eniq/home/dcuser/centralDatagen/CentralDatagen.pl >/dev/null 2>&1
7,17,27,37,47,57 * * * * /eniq/home/dcuser/centralDatagen/ClearDatagenDirs.pl >/dev/null 2>&1
0,30 * * * * perl /eniq/home/dcuser/automation/cleardirsafterabort.pl >/dev/null 2>&1
0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58 * * * * /eniq/home/dcuser/centralDatagen/DBChecker.pl >/dev/null 2>&1

#SERVICES NEEDED/NOT NEEDED
	
The services should look like this:
disabled       17:07:44 svc:/eniq/postgresql_og:default_64bit
disabled       17:07:44 svc:/eniq/esm:default
disabled       17:07:44 svc:/eniq/roll-snap:default
disabled       17:09:41 svc:/eniq/dwh_reader:default
disabled        9:18:12 svc:/eniq/webserver:default
disabled        9:18:26 svc:/eniq/engine:default
disabled        9:18:43 svc:/eniq/glassfish:default
disabled        9:31:32 svc:/eniq/scheduler:default
disabled        9:32:38 svc:/eniq/repdb:default
disabled        9:33:06 svc:/eniq/connectd:default
disabled       10:27:17 svc:/eniq/dwhdb:default
online          9:30:35 svc:/eniq/rmiregistry:default
online          9:32:09 svc:/eniq/licmgr:default
online          9:32:35 svc:/eniq/controlzone:default
online         10:06:36 svc:/eniq/dbmon:default
online         10:07:05 svc:/eniq/ec:default



#EXECUTION CONTEXT

Remove all ec's except ec 1..

/eniq/mediation_sw/mediation_gw/etc/executioncontext.xml

should look SIMILAR to:

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configlist prompt="true">
	<config name="EC1">
		<startclass class="com.digitalroute.picostart.PicoStart"/>
		<jdkarg value="-Xmx1000M"/>
		<jdkarg vendor="sun,hp" value="-server"/>
	<jdkarg value="-Djava.awt.headless=true"/>
		<jdkarg value="-Xms512M"/>
		<classpath path="lib/picostart.jar"/>
		<property value="585a3c2a0eae6eb5ebef" name="ec.httpd.password"/>
		<property value="9090" name="ec.httpd.port"/>
		<property value="mzadmin" name="ec.httpd.user"/>
		<property value="20000" name="ec.shutdown.time"/>
		<property value="/eniq/mediation_sw/mediation_gw/tmp" name="ecsa.backlog.dir"/>
		<property value=" com.digitalroute.wf.ECEventSender, com.digitalroute.wf.pico.ECSAHTTPD, com.digitalroute.wf.pico.ExecutionContextFactoryImpl, com.digitalroute.wfc.aggregation.storage.AggregationStorageImpl, com.digitalroute.wfc.dupudr2.RDupUDR2StorageImpl, com.digitalroute.wfc.interwf.storagehandler.BatchStorageImpl, com.digitalroute.ui.cmdline.sysinfo.RemoteCommandImpl" name="pico.bootstrapclass"/>
		<property value="log/${config.name}.log" name="pico.stderr"/>
		<property value="log/${config.name}.log" name="pico.stdout"/>
		<property value="ec" name="pico.type"/>
		<jdkarg value="-d64"/>
		<jdkarg value="-XX:-DontCompileHugeMethods"/>
		<property value="32000" name="mz.aggregation.max_file_cache_size"/>
		<property value="1000000" name="mz.aggregation.min_session_file_size"/>
		<property value="35" name="sybase.iq.pool.maxlimit"/>
		<jdkarg value="-Dcom.sun.management.jmxremote"/>
		<property value="true" name="mz.aggregation.storage.profile_session_cache"/>
		<jdkarg value="-XX:MaxPermSize=256M"/>
		<jdkarg value="-Djava.util.logging.config.file=/eniq/sw/conf/streaming-probe-logging.properties"/>
		<jdkarg value="-Dorg.omg.CORBA.ORBInitialHost=glassfish"/>
		<jdkarg value="-Dorg.omg.CORBA.ORBInitialPort=3700"/>
		<classpath path="./../../glassfish/glassfish/glassfish/lib/gf-client.jar"/>
		<jdkarg value="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger"/>
		<jdkarg value="-Dxml-auto.config.file=/eniq/sw/conf/xml-auto-config.properties"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/commons-lang3.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/commons-logging.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/eniq_analysis_base.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/eniq_analysis_ap_event_beans.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/xml-auto-event-dec.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/xml-auto-counter-logic.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/xml-auto-counter-logic-java.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/xml-auto-xml-writer.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/xml-auto-commons.jar"/>
		<classpath path="./../../mediation_inter/M_E_LTEES/lib/log4j.jar"/>
	</config>
</configlist>
</xml>
