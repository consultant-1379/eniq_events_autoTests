/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author eseuhon
 * @since 2010
 * 
 */
public class PropertyReader {

    private static PropertyReader instance;

    private Properties properties;

    private static final String seleniumPropertyFile = "selenium.properties";

    private String classDirectory = new File(PropertyReader.class.getProtectionDomain().getCodeSource().getLocation()
            .getPath()).getParent();

    private String resourcesInSubDir = classDirectory + File.separator + "resources";

    private String resourcesInClassesDir = classDirectory + File.separator + "classes" + File.separator + "resources";

    private static final String resourcePathOnBlade = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources";

    // Update this path to resources folder in your view if you are running
    // tests from eclipse
    // i.e.
    // C:/your_view_folder/eniq_events/auto_tests/eniq-events-selenium/src/main/resources
    // private static final String resourcePathInVobs =
    // "C:\\ekeviry_view_12.2\\eniq_events\\auto_tests\\eniq-events-selenium\\src\\main\\resources"

    //private static final String resourcePathInVobs = "E:\\emosjil\\emosjil_snapviewOnServer1\\eniq_events\\auto_tests\\eniq-events-selenium\\target\\classes\\resources";

    public static final boolean VM_TEST_BLADE = System.getProperty("TEST_BLADE") != null;

    public static final String VM_PROJECT_ROOT_USED_BY_CI = System.getProperty("eniq.events.vob.root");

    public static final String VM_HOST_ON_BLADE = System.getProperty("HOST");

    public static final String VM_PORT = System.getProperty("SERVERPORT");

    public static final String VM_SERVER_HOST = System.getProperty("SERVERHOST");

    public static final String VM_TEST_GROUP = System.getProperty("TEST_GROUP");

    public static final String VM_HOSTADMINUI = System.getProperty("HOSTADMINUI");

    public static final String VM_TEST_SUITE = System.getProperty("TEST_SUITE");

    public static final String VM_TEST_SUB_SUITE = System.getProperty("TEST_SUB_SUITE");

    public static final String VM_TESTS = System.getProperty("TESTS");

    public static final String VM_ENIQVERSION = System.getProperty("ENIQVERSION");

    public static final String RUN_LOCATION = System.getProperty("RUN_LOCATION");

    private static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    public static PropertyReader getInstance() {
        if (instance == null) {
            instance = new PropertyReader();
            instance.init();
        }
        return instance;
    }

    private void init() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(getPropertyFile()));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            logger.severe("Can not find a selenium.property file. Please ensure the file exists - " + getPropertyFile());
        } catch (final IOException e) {
            e.printStackTrace();
            logger.severe("Errors occured while loading file - " + getPropertyFile());
        }
    }

    private String getPropertyFile() {
        String propertiesInSubDir = resourcesInSubDir + File.separator + seleniumPropertyFile;
        String propertiesInClassesDir = resourcesInClassesDir + File.separator + seleniumPropertyFile;
        if (new File(resourcePathOnBlade + File.separator + seleniumPropertyFile).exists()) {
            return resourcePathOnBlade + File.separator + seleniumPropertyFile;
        } else if (new File(propertiesInSubDir).exists()) {
            return propertiesInSubDir;
        } else if (new File(propertiesInClassesDir).exists()) {
            return propertiesInClassesDir;
        } else {
            logger.severe("Could not find any selenium.properties files. Returning default");
            return propertiesInClassesDir;
        }
    }

    public String getServerHost() {
        if (VM_SERVER_HOST == null) {
            return properties.getProperty("server.host");
        }
        return VM_SERVER_HOST;
    }

    public int getServerPort() {
        try {
            if (VM_PORT == null) {
                return Integer.parseInt(properties.getProperty("server.port"));
            }
            return Integer.parseInt(VM_PORT);
        } catch (NumberFormatException e) {
            return 4566;
        }
    }

    public String getBrowserType() {
        String browser = System.getProperty("BROWSER_TYPE");
        if (browser == null) {
            return properties.getProperty("browser.type");
        }
        return browser;
    }

    public String getEventHost() {
        if (VM_HOST_ON_BLADE == null) {
            return properties.getProperty("browser.events.host");
        }
        return VM_HOST_ON_BLADE;
    }

    public String getEventPort() {
        return properties.getProperty("browser.events.port");
    }

    public String getEventURL() {
        return getEventHost() + ":" + getEventPort();
    }

    public String getUser() {
        return properties.getProperty("events.username");
    }

    public String getPwd() {
        return properties.getProperty("events.password");
    }

    public String getDashboardUser() {
        return properties.getProperty("dashboard.username");
    }

    public String getDashboardPwd() {
        return properties.getProperty("dashboard.password");
    }

    public String getPath() {
        return properties.getProperty("browser.events.path");
    }

    public String getResourcesLocation() {
        String resDataInSubDir = resourcesInSubDir;
        String resDataInClassesDir = resourcesInClassesDir;
        if (VM_PROJECT_ROOT_USED_BY_CI != null) {
            return VM_PROJECT_ROOT_USED_BY_CI + "/eniq_events_ui/src/test/selenium/resources";
        } else if (new File(resourcePathOnBlade).exists()) {
            return resourcePathOnBlade;
        } else if (new File(resDataInSubDir).exists()) {
            return resDataInSubDir;
        } else if (new File(resDataInClassesDir).exists()) {
            return resDataInClassesDir;
        } else {
            logger.severe("Could not find any reservedData.csv files. Returning default");
            return resDataInClassesDir;
        }
    }

    public String getTestGroup() {
        String testGroup = System.getProperty("TEST_GROUP");
        if (testGroup == null) {
            return properties.getProperty("test.group");
        }
        return testGroup;
    }

    public String getScreenShotFolder() {
        return properties.getProperty("screenshot.folder");
    }

    public String getAdminUser() {
        return properties.getProperty("admin.username");
    }

    public String getAdminPwd() {
        return properties.getProperty("admin.password");
    }

    public String getAdminServerPort() {
        return properties.getProperty("browser.admin.port");
    }

    public String getAdminServerHost() {
        if (VM_HOSTADMINUI == null) {
            return properties.getProperty("browser.admin.host");
        }
        return VM_HOSTADMINUI;
    }

    public String getAdminServerPath() {
        return properties.getProperty("browser.admin.path");
    }

    public String getDbHost() {
        if (VM_HOSTADMINUI == null) {
            return properties.getProperty("db.host");
        }
        return VM_HOSTADMINUI.replace("http://", "");
    }

    public String getDbPort() {
        return properties.getProperty("db.port");
    }

    public String getDbUser() {
        return properties.getProperty("db.user");
    }

    public String getDbPwd() {
        return properties.getProperty("db.pwd");
    }

    public String getDbName() {
        return properties.getProperty("db.name");
    }

    public String getUnixHost() {
        return properties.getProperty("Unix_Host");
    }

    public String getUnixUserName() {
        return properties.getProperty("Unix_UserName");
    }

    public String getUnixPassword() {
        return properties.getProperty("Unix_Password");
    }

    public String getUnixDirectory() {
        return properties.getProperty("Unix_Directory");
    }

    public String getUnixURL() {
        return properties.getProperty("Unix_URL");
    }

    public String getTimeRange() {
        return properties.getProperty("time.range");
    }

    public String getDataIntegrityFlag() {
        return properties.getProperty("data.integrity");
    }

    public String[] getTestCaseNames() {
        if (VM_TESTS != null) {
            String[] cases = VM_TESTS.split("\\,");
            return cases;
        }
        return null;

    }

    public String getTestCaseNamesString() {
        if (VM_TESTS != null) {
            return VM_TESTS;
        }
        return null;

    }

    public String[] getTestSubSuiteNames() {
        if (VM_TEST_SUB_SUITE != null) {
            String[] cases = VM_TEST_SUB_SUITE.split("\\,");
            return cases;
        }
        return null;
    }

    public String getTestSubSuiteNamesString() {
        if (VM_TEST_SUB_SUITE != null) {
            return VM_TEST_SUB_SUITE;
        }
        return null;
    }

    public String getTestSuiteName() {
        if (VM_TEST_SUITE != null) {
            return VM_TEST_SUITE;
        }
        return null;
    }

    public String getEniqVersion() {
        if (VM_ENIQVERSION == null) {
            return properties.getProperty("eniq.version");
        }
        return VM_ENIQVERSION;
    }

    public String getRunLocation() {
        if (RUN_LOCATION == null) {
            return properties.getProperty("run.location");
        }
        return RUN_LOCATION;
    }

    public double getEniqRootVersion() {
        String version = getEniqVersion();
        if (version.startsWith("13.0") || version.startsWith("3.0")) {
            return 13.0;
        } else if (version.startsWith("12.2") || version.startsWith("2.2")) {
            return 12.2;
        } else {
            return 0;
        }
    }

    public String getRepDbPort() {
        return properties.getProperty("repdb.port");
    }

    public String getRepDbHost() {
        return properties.getProperty("repdb.host");
    }

    public String getRepDbName() {
        return properties.getProperty("repdb.name");
    }

    public String getRepDbUser() {
        return properties.getProperty("repdb.user");
    }

    public String getRepDbPwd() {
        return properties.getProperty("repdb.pwd");
    }
}
