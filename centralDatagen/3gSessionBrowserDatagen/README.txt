These are configuration files for the 3g session browser
data generation tool. If you copy these files onto 
/opt/ericsson/3g-session-browser-datagen/etc/, you should
have a working 3g sess browser datagen configuration.

Do the following after installing the 3g Session browser
datagen tool according to this link:
https://confluence-oss.lmera.ericsson.se/display/SB/SessionBrowser+Datagen

Please note: Make sure your git installation transforms Windows EOF to
Unix EOF. Otherwise you may have to do this transformation on individual
files after installation.

The following files should be copied individually onto
/opt/ericsson/3g-session-browser-datagen/etc/.

./common.props
./classificationProperties/*
./GpehGeneratorProperties/*
./SgehGeneratorProperties/*
./tcp_partial_properties/*

After you copy these files, /opt/ericsson/3g-session-browser-datagen/etc/
should look like:

-bash-3.2$ cd /opt/ericsson/3g-session-browser-datagen/etc/
-bash-3.2$ ls -ltr
total 108
-rwxr--r--   1 dcuser   cit         2292 Jun  4  2013 classification_monitor.props
-rwxr--r--   1 dcuser   cit          266 Jun  4  2013 clean_list
-rwxr--r--   1 dcuser   cit         1240 Jun  4  2013 datagen_logging
-rwxr--r--   1 dcuser   cit         2019 Jun  4  2013 gpeh_validator.props
-rwxr--r--   1 dcuser   cit         4522 Jun  4  2013 sgeh_monitor.props
-rwxr--r--   1 dcuser   cit         1571 Jun  4  2013 sgeh_validator.props
-rwxr--r--   1 dcuser   cit         2668 Jun  4  2013 tcp_partial.props
-rwxr--r--   1 dcuser   cit         2379 Jun  4  2013 tcp_partial_monitor.props
-rwxr--r--   1 dcuser   cit         1295 Jun  4  2013 tcp_partial_validator.props
-rwxr--r--   1 dcuser   cit         1283 Jun 10  2013 common.props
-rwxr--r--   1 dcuser   cit         1235 Jun 11  2013 classification_validator.props
-rwxr--r--   1 dcuser   cit         2623 Jun 11  2013 classification.props
-rwxr--r--   1 dcuser   cit         7337 Dec 11  2013 gpeh_monitor.props
-rwxr--r--   1 dcuser   cit         3659 Jun 11 16:28 sgeh.props
-rwxr--r--   1 dcuser   cit         3678 Jun 11 16:28 gpeh.props
-rw-r--r--   1 dcuser   cit         4350 Jun 11 16:38 configurationSGEH.properties
-rw-r--r--   1 dcuser   cit         2387 Jun 11 16:38 configurationGPEH.properties



Open mediation desktop viewer.
Open configuration viewer.
Open SGEH_SESSION
	- Open ConfigurationSettings and make sure
	  it's pointing to /opt/ericsson/3g-session-browser-datagen/etc/configurationSGEH.properties
Open GPEH_3G_SESSION
	- Open Configuration and make sure 
	  it's pointing to /opt/ericsson/3g-session-browser-datagen/etc/configurationGPEH.properties


	  
Make sure to add the shebang line in you launchmzsh.sh script.
It will be something like #!/usr/bin/bash
-bash-3.2$ cd ~/mz2/mz/bin/
-bash-3.2$ cat launchmzsh.sh
#!/usr/bin/bash
java -classpath "/eniq/home/dcuser/mz2/mz/lib/picostart.jar:/eniq/home/dcuser/mz2/mz/lib/codeserver.jar:/eniq/home/dcuser/mz2/mz/lib/codeserver_common.jar:/eniq/home/dcuser/mz2/mz/etc/mz.license" -Dterm.interactive=1 -Dterm.cmdediting=0 -Dmz.hostname=`hostname` -Dmz.home=/eniq/home/dcuser/mz2/mz com.digitalroute.picostart.cmdline.CommandLine $1 $2 $3 $4 $5 $6 $7 $8 $9



Start the 3g session browser datagen tool as root
# /etc/init.d/datagen status
# /etc/init.d/datagen stop
# /etc/init.d/datagen start

Check logs at /var/log/3g-session-browser-datagen/.
Errors due to faulty configuration will be in datagen-output.log.
Examine these errors and review the configuration. Stop the tool
and restart it again.