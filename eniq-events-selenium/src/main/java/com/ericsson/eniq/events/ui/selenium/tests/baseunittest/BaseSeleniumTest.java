/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.baseunittest;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.TagNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSeleniumTestRunner;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.login.EventsLogin;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsLogin;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import com.thoughtworks.selenium.SeleniumException;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(EricssonSeleniumTestRunner.class)
@ContextConfiguration(locations = { "classpath:resources/appContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public abstract class BaseSeleniumTest extends TestCase {

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    @Autowired
    protected EventsLogin eventsLogin;

    @Autowired
    protected EniqEventsLogin eventsWebDriverLogin;

    @Deprecated
    @Autowired
    protected EricssonSelenium selenium;

    protected NewEricssonSelenium webDriverSelenium;
    protected WebDriver driver;
    @Autowired
    protected ReservedDataHelper reservedDataHelper;
    String name = "";

    public BaseSeleniumTest() {
        name = this.getClass().getSimpleName();
    }

    @Rule
    public MethodRule watchman = new TestWatchman() {

        @Override
        public void starting(final FrameworkMethod method) {
            logger.info("Starting Test: " + method.getName());
        }

        @Override
        public void failed(final Throwable e, final FrameworkMethod method) {
            final File file = createFile(name, method.getName());
            logger.log(Level.SEVERE, getStackTrace(e) + "SCREENSHOT: " + file.getAbsoluteFile() + "\nTestName: " + method.getName() + " TestTag: "
                    + getTestTagName(method) + getTestResultMessage(e));
            takeScreenShotFromBlade(file);

            try {
                String feature = getFeatureName(method);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/eniq/home/dcuser/automation/priority/runResults/" + feature
                        + ".FAIL.txt", true)));
                out.println(method.getName());
                out.close();
            } catch (IOException e1) {

            }
            try {
				tearDown();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
        }

        @Override
        public void succeeded(final FrameworkMethod method) {
            logger.info("TestName: " + method.getName() + " TestTag: " + getTestTagName(method) + " TestResult: PASS FailureReason: \n");
            try {
                String feature = getFeatureName(method);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/eniq/home/dcuser/automation/priority/runResults/" + feature
                        + ".PASS.txt", true)));
                out.println(method.getName());
                out.close();
            } catch (IOException e1) {
                System.out.println("Minor Warning: Priority Result Not Passed");
            }
            try {
				tearDown();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
        }
       
    };

    public String getFeatureName(final FrameworkMethod method) {
        String feature = method.getMethod().toString();
        feature = feature.split(" throws")[0];
        feature = feature.split(".tests.")[1];
        feature = feature.split("\\.")[0];
        return feature;
    }

    public void pause(final int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (final InterruptedException e) {
        }
    }

    public void captureLogAndScreenShotOnFailure(final Throwable failure) {

        String testMethodName = "";
        for (final StackTraceElement stackTrace : failure.getStackTrace()) {
            if (stackTrace.getClassName().equals(this.getClass().getName())) {
                testMethodName = stackTrace.getMethodName();
                break;
            }
        }
        final DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
        final Date date = new Date();

        final File file = new File(PropertyReader.getInstance().getScreenShotFolder() + "/" + dayFormat.format(date) + "/");
        if (!file.exists()) {
            file.mkdirs();
        }

        logger.log(Level.SEVERE, getStackTrace(failure) + "\n" + "SCREENSHOT: " + file.getAbsolutePath() + "/" + timeFormat.format(date) + "-"
                + this.getClass().getName() + testMethodName + ".png");

        selenium.captureEntirePageScreenshot(file.getAbsolutePath() + "/" + timeFormat.format(date) + "-" + this.getClass().getName()
                + testMethodName + ".png", "");
    }

    private static String getStackTrace(final Throwable throwable) {
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }

    /**
     * Extract out test number which is situated at the end of test name by convention i.e. testTemporalReferencesForRanking_4_7_2() then combining
     * the number with a test TAG name based on feature group of VS
     * 
     * @param method
     * @return TAG name i.e. ENIQ_E_4.7.2
     */
    private static String getTestTagName(final FrameworkMethod method) {
        final StringBuilder buffer = new StringBuilder();
        if (method.getMethod().toString().contains("twogthreeg.dvdt")) {

            buffer.append(TagNameConstants.TAGNAME_2G3G_DVDT);
        } else if (method.getMethod().toString().contains("mss")) {
            buffer.append(TagNameConstants.TAGNAME_MSS);
        } else {
            buffer.append(TagNameConstants.PREFIX_TAGNAME);
        }

        final int firstOfUnderscore = method.getName().indexOf(GuiStringConstants.UNDERSCORE);
        if (firstOfUnderscore != -1) {
            final String testNumberWithUnderscore = method.getName().substring(firstOfUnderscore + 1, method.getName().length());
            for (final String currentNumber : testNumberWithUnderscore.split(GuiStringConstants.UNDERSCORE)) {
                buffer.append(currentNumber);
                buffer.append(GuiStringConstants.DOT);
            }

            final int startOfLastComma = buffer.lastIndexOf(GuiStringConstants.DOT);
            buffer.delete(startOfLastComma, buffer.length());
        }
        return buffer.toString();
    }

    public void checkStringListContainsArray(final List<String> list, final String... values) {
        for (final String value : values) {
            assertTrue(value + " is not in list - " + list, list.contains(value));
        }
    }

    public void waitForPageLoadingToComplete() throws PopUpException {
        selenium.waitForPageLoadingToComplete();
    }

    private String getTestResultMessage(final Throwable e) {

        final String ELEMENT_NOT_FOUND = "ERROR: Element";

        if (e instanceof NoDataException) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.NO_DATA + "\n";
        }
        if (e instanceof PopUpException) {
            return " TestResult: FAIL FailureReason: " + e + "\n";
        }
        if (getStackTrace(e).contains("Timed out")) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.REQUEST_TIMEOUT + "\n";
        }
        if (getStackTrace(e).contains("events found for IMSI")) {
            return " TestResult: NO DATA FailureReason: " + FailureReasonStringConstants.EVENT_TYPE_NA_FOR_IMSI + "\n";
        }
        if (getStackTrace(e).contains("values identified for IMSI")) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.NON_MATCHING_VALUES + "\n";
        }
        if (getStackTrace(e).contains("Differing number of events for")) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.DIFFERING_NUMBER_OF_EVENT + "\n";
        }

        if (getStackTrace(e).contains(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED)) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + "\n";
        }

        if (getStackTrace(e).contains(FailureReasonStringConstants.HEADER_MISMATCH)) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.HEADER_MISMATCH + "\n";
        }

        if (getStackTrace(e).contains(ELEMENT_NOT_FOUND) || getStackTrace(e).contains("Unable to locate element")) {
            return " TestResult: FAIL FailureReason: " + FailureReasonStringConstants.UI_NAVIGATION_FAILED + "\n";
        }

        if (getStackTrace(e).contains("services are not running correctly")) {
            return " TestResult: FAIL FailureReason: " + e.toString() + "\n";
        }

        if (getStackTrace(e).contains("Failed to resolve var value")) {
            return " TestResult: FAIL FailureReason: Login Timeout" + "\n";
        }

        if (getStackTrace(e).contains("AssertionFailedError")) {
            int beginIndex = getStackTrace(e).indexOf("AssertionFailedError: ") + "AssertionFailedError: ".length();
            int endIndex = getStackTrace(e).indexOf("at junit.framework.Assert.fail") - 1;
            return " TestResult: FAIL FailureReason: " + getStackTrace(e).substring(beginIndex, endIndex) + "\n";
        }

        return " TestResult: FAIL FailureReason: \n";
    }

    /**
     * Take a screenshot when test is fail.
     * 
     * @param file
     */
    private void takeScreenShotFromBlade(final File file) {

        try {
            File scrshot = ((TakesScreenshot) (webDriverSelenium.getWrappedDriver())).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrshot, file);
        } catch (Exception ex) {
            selenium.captureScreenshot(file.getAbsolutePath());
        }
    }

    /**
     * create a screenshot file i.e /tmp/ENIQ_Events_UI_Selenium/2011-01-26/2011- 01-26-19.54.07-ENIQ_E_4_5_9_testIMSIEventAPNDrillDown.png
     * 
     * @param methodName
     * @return file
     */
    private File createFile(final String className, final String methodName) {
        final DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
        final Date date = new Date();

        final String screenShotDirToday = PropertyReader.getInstance().getScreenShotFolder() + "/" + dayFormat.format(date) + "/" + className + "/";
        final File dir = new File(screenShotDirToday);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        final File file = new File(screenShotDirToday + "/" + dayFormat.format(date) + "-" + timeFormat.format(date) + "-" + methodName + ".png");
       selenium.captureScreenshot(file.getAbsolutePath());
        return file;
    }

    public void setTimeRangeToOneWeek(final CommonWindow commonWindow) throws PopUpException {
        commonWindow.setTimeRange(TimeRange.ONE_WEEK);
    }

    public void setTimeRangeToOneDay(final CommonWindow commonWindow) throws PopUpException {
        commonWindow.setTimeRange(TimeRange.ONE_DAY);
    }

    /**
     * get one certain time range from properties file //ewandaf
     * 
     */
    protected TimeRange getTimeRangeFromProperties() {
        final String timeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGE);
        return getTimeRangeByLabel(timeLabel);
    }

    /**
     * get time range by label //ewandaf
     * 
     * @param label e.g. 5 minutes, 15 minutes, 1 hour, 2 hours, 1 day
     */
    protected TimeRange getTimeRangeByLabel(final String label) {
        final int minutes = TimeRange.getTimeRange(label.trim()).getMiniutes();
        switch (minutes) {
            case 5:
                return TimeRange.FIVE_MINUTES;
            case 15:
                return TimeRange.FIFTEEN_MINUTES;
            case 1 * 60:
                return TimeRange.ONE_HOUR;
            case 2 * 60:
                return TimeRange.TWO_HOURS;
            case 6 * 60:
                return TimeRange.SIX_HOURS;
            case 12 * 60:
                return TimeRange.TWELVE_HOURS;
            case 24 * 60:
                return TimeRange.ONE_DAY;
            default:
                return TimeRange.THIRTY_MINUTES;

        }
    }

    /**
     * Set date and time in time dialog window with a lag of configurable time. For example : By selecting 15 mins on EventAnalysis window, it will
     * display data with a specific time lag. i.e if current time is 11:30, ideally it should display data between 11:15 and 11:30, but due to delay
     * in updation of UI at current time, it fails to display all the expected data till 11:30. Therefore to display all the expected datathis
     * function creates a time lag for example of 5 mins and fetches data from 11:10 to 11:25. Similarly for all other time time ranges, it displays
     * data with a time lag of 5 mins. Note : This time lag can be configurable from properties.
     * 
     * @param TimeRange ex: 15mins, 30mins etc. NOTE : Cannot set timeRange for 5 mins due to UI constraints.
     */
    public void delayAndSetTimeRange(final CommonWindow window, final TimeRange timeRange) throws PopUpException {
        final GregorianCalendar date = new GregorianCalendar();
        final DateFormat minFormatter = new SimpleDateFormat("mm");
        final DateFormat AMPMFormatter = new SimpleDateFormat("a");
        final Formatter startDatefmt = new Formatter();
        final Formatter endDatefmt = new Formatter();

        if (timeRange == TimeRange.FIVE_MINUTES) {
            logger.log(Level.WARNING, "Cannot set TimeRange for 5 mins due to UI constraints.");
            return;
        }

        final Date currentDate = date.getTime();
        logger.log(Level.INFO, "Current Date : " + currentDate);

        final long currentTime = currentDate.getTime(); // gets time in
                                                        // milliseconds

        final int timeRangeInMins = timeRange.getMiniutes();

        long endDateTime = currentTime - (15 * 60 * 1000); // 15 minutes delay
                                                           // time - TODO this
                                                           // value should be
                                                           // configurable by
                                                           // adding to
                                                           // properties.

        date.setTimeInMillis(endDateTime);
        Date endDate = date.getTime();

        final int minutesValue = Integer.parseInt(minFormatter.format(endDate.getTime()));

        if (minutesValue < 15) {
            endDateTime = endDateTime - (minutesValue * 60 * 1000);
        } else {
            final int reminderValue = minutesValue % 15;
            endDateTime = endDateTime - (reminderValue * 60 * 1000);
        }

        if (timeRange == TimeRange.ONE_DAY) {
            endDateTime = endDateTime - (TimeRange.ONE_DAY.getMiniutes() * 60 * 1000);
        }

        date.setTimeInMillis(endDateTime);
        endDate = date.getTime();

        final long startDateTime = endDateTime - (timeRangeInMins * 60 * 1000);

        date.setTimeInMillis(startDateTime);
        final Date startDate = date.getTime();

        startDatefmt.format("%tl", startDate);
        String hourStartDate = startDatefmt.toString();
        int lengthOfHour = hourStartDate.length();
        if (lengthOfHour == 1) {

            hourStartDate = "0" + hourStartDate;
        }

        endDatefmt.format("%tl", endDate);
        String hourEndDate = endDatefmt.toString();
        lengthOfHour = hourEndDate.length();
        if (lengthOfHour == 1) {

            hourEndDate = "0" + hourEndDate;
        }

        final String startDateTimeCandidate = AMPMFormatter.format(startDate.getTime()) + "_" + hourStartDate + "_"
                + minFormatter.format(startDate.getTime());
        final String endDateTimeCandidate = AMPMFormatter.format(endDate.getTime()) + "_" + hourEndDate + "_"
                + minFormatter.format(endDate.getTime());

        logger.log(Level.INFO, "Duration : " + timeRangeInMins + " minutes. Start Date Time Candidate : " + startDate + " " + startDateTimeCandidate
                + " and End Date Time Candidate : " + endDate + " " + endDateTimeCandidate);
        window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate, TimeCandidates.valueOf(endDateTimeCandidate));
    }


    public void tearDown() throws Exception {

        super.tearDown();
    }

    private void close() {
        try {
            eventsLogin.logOut();
            if (selenium != null) {
                selenium.close();
                selenium.stop();
                selenium = null;
            }
        } catch (SeleniumException ex) {
            eventsWebDriverLogin.logOut();
            if (webDriverSelenium != null) {
                webDriverSelenium.stop();
            }
        }
    }

}
