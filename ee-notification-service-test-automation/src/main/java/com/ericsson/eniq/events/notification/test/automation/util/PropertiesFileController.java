package com.ericsson.eniq.events.notification.test.automation.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ekrchai
 *
 */
public class PropertiesFileController {

    private final Properties prop = new Properties();

    public void loadPropertiesFile(final InputStream propFileDir) throws FileNotFoundException, IOException {
        prop.load(propFileDir);
    }

    public void loadPropertiesFile(final String propFileDir) throws FileNotFoundException, IOException {
        prop.load(getClass().getClassLoader().getResourceAsStream(propFileDir));
    }

    public String getPropertiesSingleStringValue(final String propertiesKeyTag) throws FileNotFoundException,
            IOException {

        final String propTagStringValue = prop.getProperty(propertiesKeyTag);
        return propTagStringValue;
    }
}
