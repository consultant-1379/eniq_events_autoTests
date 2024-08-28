/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.environmentsetup;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author ekeviry
 * @since 2011
 * 
 */

public class ReadWorkflowNamesFromXMLFile {

    protected String[] xmlPropFileElements;

    public String[] readPropertiesFile(final String tagName, final String propertiesFileName) {

        try {
            final File file = new File(propertiesFileName);

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            final NodeList nodeList = doc.getElementsByTagName(tagName);

            final int numberOfElementsXMLPropertiesFile = nodeList.getLength();

            xmlPropFileElements = new String[numberOfElementsXMLPropertiesFile];

            for (int i = 0; i < numberOfElementsXMLPropertiesFile; i++) {

                final Node nodeListElement = nodeList.item(i);

                if (nodeListElement.getNodeType() == Node.ELEMENT_NODE) {
                    xmlPropFileElements[i] = nodeListElement.getTextContent();
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

        return xmlPropFileElements;
    }

}
