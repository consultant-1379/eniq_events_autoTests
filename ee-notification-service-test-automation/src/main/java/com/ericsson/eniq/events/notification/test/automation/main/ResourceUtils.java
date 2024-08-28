package com.ericsson.eniq.events.notification.test.automation.main;

import java.io.File;
import java.io.InputStream;

/**
 * @author ekrchai
 *
 */
public class ResourceUtils {
    public static final String sourceDir = "kpi_resources/";

    public static InputStream getResource(final String resourceName) {
        return ResourceUtils.class.getClassLoader().getResourceAsStream(sourceDir + resourceName);
    }

    public static String getResourcePath(final String resourceName) {
        String path = null;
        path = "target/classes/" + sourceDir + resourceName;
        File file = new File(path);
        if (file.isDirectory())
            file = file.getParentFile();
        if (!file.exists()) {
            path = "/" + sourceDir + resourceName;
        }
        return path;
    }
}
