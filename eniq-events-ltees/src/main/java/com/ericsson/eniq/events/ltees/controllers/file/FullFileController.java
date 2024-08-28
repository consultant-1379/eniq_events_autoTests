/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ltees.controllers.file;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class FullFileController extends DefaultHandler {

    private boolean eNBIdStatus ;
    private final String ENBId  = "eNBId";
    private final String PASS  = "PASS";
    private final String FAIL  = "FAIL";
    private final String INPUT_VALID_TOPOLOGY = "/eniq/home/dcuser/automation/attrAutoTopoFiles/SubNetwork_ONRM_RootMo_R_MeContext_LTE01ERBS00003.xml";
    private final String INPUT_INVALID_TOPOLOGY =  "/eniq/home/dcuser/automation/attrAutoTopoFiles/SubNetwork_ONRM_ROOT_MO_R_SubNetwork_LTELi_MeContext_lienb2476.xml";
    public String  eNBIdStsForValidTopology() throws IOException,ParserConfigurationException, SAXException {
        eNBIdStatus  = false;
        File file = new File(INPUT_VALID_TOPOLOGY);
        parseFile( file);
        return eNBIdStatus==true ? PASS:FAIL;
    }

    public String eNBIdStsForCorruptedTopology() throws IOException,ParserConfigurationException, SAXException {
        eNBIdStatus  = false;
        File file = new File(INPUT_INVALID_TOPOLOGY);
        parseFile( file);
        return eNBIdStatus==false ? PASS:FAIL;
    }

    private void parseFile( File file)
            throws IOException,ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        if(file.exists()){
            parser.parse(file.getAbsolutePath(), this);
        }else{
            System.out.println("Topology file not found : "+file.getAbsolutePath());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        for (int i = 0; i < attributes.getLength(); i++) {
            if(attributes.getValue(i).equals(ENBId)){
                eNBIdStatus = true ;
                break;
            }
        }
    }
 }