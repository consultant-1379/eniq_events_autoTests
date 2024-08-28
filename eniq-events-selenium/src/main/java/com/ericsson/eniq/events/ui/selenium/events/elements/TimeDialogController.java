/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.SeleniumUtils;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 *
 */
public class TimeDialogController implements TimeDialogControls {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    @Autowired
    private EricssonSelenium selenium;
    //private NewEricssonSelenium selenium  = NewEricssonSelenium.getSharedInstance();

    private final String timeRangeDropDownButtonXPath = "//div[@id='selenium_tag_time']/img";

    private final String timeRangeDropDownItemXPath = "//div[@class='x-combo-list-item ' or @class='x-combo-list-item  x-combo-selected']";

    private final String timeDialogOKButtonXPath = "//button[contains(text(),'Ok') or contains(text(),'OK') or contains(text(),'Update')]";

    private final String timeDialogUpdateButtonXPath = "//div[@class='x-window-bwrap']//div[@class='x-window-body dlgBody']//td[1]/button[@class='gwt-Button'][contains(text(),'Update')]";

    
    private final String timeDialogCancelButtonXPath = "//*[contains(text(),'Cancel')]";

    private final String id;

    private final String timeButtonXPath;

    private final String windowXPath;

    public TimeDialogController(final String divId) {
        //id = "selenium_tag_BaseWindow_" + divId;
        //FOR_NEW_UI
    	id = "selenium_tag_baseWindow" ;
        windowXPath = "//*[contains(@id, '" + id + "')]";

        final String eniqVersion = PropertyReader.getInstance().getEniqVersion();
        if (eniqVersion.equals("12.1")) {
            timeButtonXPath = "//table[@id='btnTime']";
        } else {
            //12.2.X
            //FOR_NEW_UI
        	//timeButtonXPath = windowXPath + "//div[@id='btnTime']";
            
            timeButtonXPath =  "//div[@id='btnTime']";
        }
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.TimeDialogControls#openTimeDialog()
     */
    private void openTimeDialog() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + timeButtonXPath);
        selenium.waitForElementToBePresent(timeButtonXPath,SeleniumConstants.DEFAULT_TIMEOUT);
        selenium.click(timeButtonXPath);
        selenium.waitForElementToBePresent("//div[@id='selenium_tag_time']", SeleniumConstants.DEFAULT_TIMEOUT);
    }

    //@Override
    public String getTimeRange() {
        openTimeDialog();
        selenium.waitForElementToBePresent("//div[contains(@class, ' x-component')]", "10000");
        final String value = selenium.getValue("//input[@id='selenium_tag_time-input']");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + timeDialogCancelButtonXPath);
        selenium.click(timeDialogCancelButtonXPath);
        return value;
    }

    //@Override
    public void setTimeRange(final TimeRange timeRange) throws PopUpException {
        openTimeDialog();
        selenium.waitForElementToBePresent("//div[contains(@class, ' x-component')]", "10000");
        selenium.click("//span[@id='selenium_tag_timeRange']/input[@type='radio']");
        selenium.type("//input[@id='selenium_tag_time-input']", timeRange.getLabel());
        
        
		// Additional if clause introduced to differentiate the Xpaths of update button in 13.0.7. 

		if (selenium.isEditable(timeDialogOKButtonXPath)) {

			selenium.click(timeDialogOKButtonXPath);

		}

		else if (selenium.isEditable(timeDialogUpdateButtonXPath)) {

			selenium.click(timeDialogUpdateButtonXPath);

		}
       
        selenium.waitForPageLoadingToComplete();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.TimeDialogControls#setTimeAndDateRange()
     */
    //@Override
    public void setTimeAndDateRange(final Date startDate, final TimeCandidates startTimeCandidate, final Date endDate,
            final TimeCandidates endTimeCandidate) throws PopUpException {
        final int startTimeIndex = startTimeCandidate.getIndex();
        final int endTimeIndex = endTimeCandidate.getIndex();

        openTimeDialog();
        selenium.waitForElementToBePresent("//div[contains(@class, ' x-component')]", "10000");
        selenium.click("//span[@id='selenium_tag_dateTimeRange']/input[@type='radio']");
        loggerDuplicate.log(Level.INFO, "The Element ID : "
                + "//span[@id='selenium_tag_dateTimeRange']/input[@type='radio']");
        // Start date and time
        selenium.type("//input[@id='selenium_tag_fromDate-input']", formatDate(startDate));
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//input[@id='selenium_tag_fromDate-input']");
        SeleniumUtils.pause(2000);
        selenium.waitForElementToBePresent("//input[@id='selenium_tag_fromTime-input']", SeleniumConstants.DEFAULT_TIMEOUT);
        selenium.type("//input[@id='selenium_tag_fromTime-input']", " ");
        SeleniumUtils.pause(6000);
        selenium.waitForElementToBePresent("//div[@id='selenium_tag_fromTime']/img", SeleniumConstants.DEFAULT_TIMEOUT);
        selenium.click("//div[@id='selenium_tag_fromTime']/img");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//div[@id='selenium_tag_fromTime']/img");
        selenium.mouseDown("//div[contains(@class, 'x-combo-list x-ignore x-component x-border')][contains(@style, 'visibility: visible')]"
                + "//div[contains(@class,'x-view x-combo-list-inner x-component x-unselectable')]//div["
                + startTimeIndex + "]");

        // End date and time
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//input[@id='selenium_tag_toDate-input']");
        selenium.type("//input[@id='selenium_tag_toDate-input']", formatDate(endDate));
        selenium.type("//input[@id='selenium_tag_toTime-input']", "");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//div[@id='selenium_tag_toTime']/img");
        selenium.click("//div[@id='selenium_tag_toTime']/img");
        selenium.mouseDown("//div[contains(@class, 'x-combo-list x-ignore x-component x-border')][contains(@style, 'visibility: visible')]"
                + "//div[contains(@class,'x-view x-combo-list-inner x-component x-unselectable')]//div["
                + endTimeIndex
                + "]");

        // OK
        loggerDuplicate.log(Level.INFO, "The Element ID : " + timeDialogOKButtonXPath);
        
        if (selenium.isEditable(timeDialogOKButtonXPath)) {
            selenium.click(timeDialogOKButtonXPath);
        } 
        else if (selenium.isEditable(timeDialogUpdateButtonXPath)) {   
                selenium.click(timeDialogUpdateButtonXPath);   
        }
        
        selenium.waitForPageLoadingToComplete();

    }

    private String formatTime(final Date time) {
        final SimpleDateFormat timePattern = new SimpleDateFormat("h:mm a");
        return timePattern.format(time);
    }

    private String formatDate(final Date date) {
        final SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        return datePattern.format(date);
    }

    public List<String> getAvailableTimeRanges() {
        final List<String> timeRanges = new ArrayList<String>();

        openTimeDialog();
        selenium.click(timeRangeDropDownButtonXPath);
        loggerDuplicate.log(Level.INFO, "The Element ID : " + timeRangeDropDownButtonXPath);
        final int itemCount = (Integer) selenium.getXpathCount(timeRangeDropDownItemXPath);
        for (int i = 1; i < itemCount + 1; i++) {
            timeRanges.add(selenium.getText(timeRangeDropDownItemXPath + "[" + i + "]"));
        }

        // Cancel
        loggerDuplicate.log(Level.INFO, "The Element ID : " + timeDialogCancelButtonXPath);
        selenium.click(timeDialogCancelButtonXPath);
        loggerDuplicate.log(Level.INFO, "The Element ID : " + windowXPath);
        selenium.waitForWindowToBeReady(windowXPath, "30000");

        return timeRanges;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.TimeDialogControls#getDate(int, int, int)
     */
    //@Override
    public Date getDate(final int year, final int month, final int day) {
        // TODO Auto-generated method stub
        final GregorianCalendar date = new GregorianCalendar();

        if (year > 0 && month > 0 && day > 0 && month < 13 && day < 32) {
            date.set(year, month - 1, day);
        }
        return date.getTime();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.TimeDialogControls#getTimeLabelText()
     */
    //@Override
    public String getTimeLabelText() {
        // TODO Auto-generated method stub
        final String timeLabelText = selenium.getText(windowXPath
                + "//table[@class='x-toolbar-ct']//table[@class='x-toolbar-right-ct']"
                + "//tr[@class='x-toolbar-right-row']//label");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + timeLabelText);
        return timeLabelText;
    }

    //@Override
    public void setTimeRangeBasedOnSeleniumPropertiesFile() throws PopUpException {
        final String timeRangeString = PropertyReader.getInstance().getTimeRange();
        //System.out.println("TIME RANGE: " + timeRangeString);
        if (timeRangeString.equals("30 minutes")) {
            //Do nothing - 30 minutes is the default time range
        } else {
            final TimeRange timeRange = TimeRange.getTimeRange(timeRangeString);
            setTimeRange(timeRange);
        }
    }
}
