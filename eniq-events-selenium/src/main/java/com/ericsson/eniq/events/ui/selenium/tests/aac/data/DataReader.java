package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author ejenkav
 * @since 2010
 *
 */
public class DataReader{

    private static DataReader instance;

    private Properties properties;

    private static final String seleniumPropertyFile = "test_data.properties";

    private static final String resourcePathOnBlade = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources";

    // Update this path to resources folder in your view if you are running tests from eclipse
    // i.e. C:/your_view_folder/eniq_events/eniq_events_ui/src/test/selenium/resources
    private static final String resourcePathInVobs = "C:\\EE_cc_storage_Selenium_Latest\\ejenkav_view3\\eniq_events\\eniq_events_ui\\src\\test\\selenium\\resources";

    public static final boolean VM_TEST_BLADE = System.getProperty("TEST_BLADE") != null;

    public static final String VM_PROJECT_ROOT_USED_BY_CI = System.getProperty("eniq.events.vob.root");

    private static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    public static DataReader getInstance() {
        if (instance == null) {
            instance = new DataReader();
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
            logger.severe("Can not find a test data file. Please ensure the file is existing - "
                    + getPropertyFile());
        } catch (final IOException e) {
            e.printStackTrace();
            logger.severe("Errors occured while loading file - " + getPropertyFile());
        }
    }

    private String getPropertyFile() {
        if (VM_TEST_BLADE) {
            return resourcePathOnBlade + File.separator + seleniumPropertyFile;
        }
        return resourcePathInVobs + File.separator + seleniumPropertyFile;
    }


    public String getResourcesLocation() {
        // in case tests running from a CI build server 
        if (VM_PROJECT_ROOT_USED_BY_CI != null) {
            return VM_PROJECT_ROOT_USED_BY_CI + "/eniq_events_ui/src/test/selenium/resources";
        }
        // in case tests running from a Blade server
        if (VM_TEST_BLADE) {
            return resourcePathOnBlade;
        }
        // in case tests are running from Eclipse
        return resourcePathInVobs;
    }

    public String getProperty(String property){
    	return properties.getProperty(property);
    }
}
