package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ReportGenerator {

    public static final SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss.SSS MMM dd,yyyy");

    private static final ReportGenerator instance = new ReportGenerator();

    private static final String reportFileName = "AAC";

    private static final String logFilename = "AAC";

    private static String timeStamp = null;

    private static final String resourcePathOnBlade = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources";

    private static final String resourcePathInVobs = "C:\\view_root_4g_12_1\\eniq_events\\eniq_events_auto_tests\\selenium\\resources";

    public static final boolean VM_TEST_BLADE = System.getProperty("TEST_BLADE") != null;

    public static final String VM_PROJECT_ROOT_USED_BY_CI = System.getProperty("eniq.events.vob.root");

    ///private static final StringBuffer html = new StringBuffer();

    //private static final ArrayList<TestCase> testCases = new ArrayList<TestCase>();

    private static HashMap<String, ArrayList<TestCase>> testCaseCollection = new HashMap<String, ArrayList<TestCase>>();

    private ReportGenerator() {

    }

    public static ReportGenerator getInstance() {
        return instance;
    }

    public void addTestCase(final String name, final String group, final String id, final boolean status,
            final String failureReason) {
        if (testCaseCollection.containsKey(group.toUpperCase())) {
            final ArrayList<TestCase> testCases = testCaseCollection.get(group.toUpperCase());
            testCases.add(new TestCase(name, group, id, status, failureReason));
        } else {
            final ArrayList<TestCase> testCases = new ArrayList<TestCase>();
            testCases.add(new TestCase(name, group, id, status, failureReason));
            testCaseCollection.put(group.toUpperCase(), testCases);
        }

    }

    public static void generate(final String hostnameAdmin, final String hostnameEniq, final Date startTime,
            final Date endTime, final boolean loggingFlag, final boolean isLogFileUploadEnable) {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(getReportFileAbsolutePath()));
            out.write(htmlHeader
                    + getEnvironmentTable(hostnameAdmin, hostnameEniq, ReportGenerator.date_format.format(startTime),
                            ReportGenerator.date_format.format(endTime), loggingFlag, isLogFileUploadEnable).toString()
                    + getMainBody().toString() + htmlFooter);
            out.close();
        } catch (final IOException ioe) {
            System.out.println("Cannot write to Report File" + getReportFileAbsolutePath());
        }

        /*        for (final TestCase testCaseElement : testCases) {
                    System.out.println("ID:" + testCaseElement.getId() + " Name:" + testCaseElement.getName() + " Group:"
                            + testCaseElement.getGroup() + " Status" + testCaseElement.getStatus());

                }
                Collections.sort(testCases);
                for (final TestCase testCaseElement : testCases) {

                }
        */}

    private static StringBuilder getMainBody() {
        final StringBuilder summaryTable = new StringBuilder();
        final StringBuilder testCaseTable = new StringBuilder();
        //final ArrayList<StringBuilder> testTables = new ArrayList<StringBuilder>();
        summaryTable.append("<TABLE ID = 'summary' BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>");
        summaryTable.append("<TR ALIGN = CENTER>");
        summaryTable
                .append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Test Group</FONT></TH>");
        summaryTable.append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Pass</FONT></TH>");
        summaryTable.append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Fail</FONT></TH>");
        summaryTable
                .append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Pass Rate</FONT></TH>");
        summaryTable.append("</TR>");

        for (final String keyElement : testCaseCollection.keySet()) {
            testCaseTable.append("<BR><BR>");
            testCaseTable.append(keyElement + " Test Cases:");
            testCaseTable.append("<TABLE ID='" + keyElement + "' BORDER = 1 CELLSPACING = 2 CELLPADDING = 2>");
            testCaseTable.append("<TR ALIGN = CENTER>");
            testCaseTable
                    .append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Test Case ID</FONT></TH>");
            testCaseTable
                    .append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Test Case Name</FONT></TH>");
            testCaseTable
                    .append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Status</FONT></TH>");
            testCaseTable
                    .append("<TH BGCOLOR = #B8B8B8><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>Failure Reason</FONT></TH>");
            testCaseTable.append("</TR>");

            final ArrayList<TestCase> testCaseList = testCaseCollection.get(keyElement);

            summaryTable.append("<TR ALIGN = CENTER>");
            summaryTable.append("<TD ALIGN = LEFT><A HREF='#" + keyElement
                    + "'><B><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>" + keyElement + "</B></A></TD>");

            double fail = 0, pass = 0;
            for (final TestCase testCaseListElement : testCaseList) {
                testCaseTable.append("<TR ALIGN = LEFT>");
                testCaseTable.append("<TD><B><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>"
                        + testCaseListElement.getId() + "</B></TD>");
                testCaseTable.append("<TD>" + testCaseListElement.getName() + "</TD>");

                if (testCaseListElement.getStatus() == TestCase.PASS_STATUS) {
                    testCaseTable.append("<TD><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>" + TestCase.PASS_STATUS
                            + "</FONT></TD>");
                    pass++;
                } else {
                    testCaseTable.append("<TD BGCOLOR = RED><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>"
                            + TestCase.FAIL_STATUS + "</FONT></TD>");
                    fail++;
                }
                testCaseTable.append("<TD>" + testCaseListElement.getFailureReason() + "</TD>");
                testCaseTable.append("</TR>");
            }
            testCaseTable.append("</TABLE>");
            summaryTable.append("<TD><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>" + (int) pass + "</FONT></TD>");
            if (fail > 0) {
                summaryTable.append("<TD BGCOLOR = RED><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>" + (int) fail
                        + "</FONT></TD>");
            } else {
                summaryTable
                        .append("<TD><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>" + (int) fail + "</FONT></TD>");
            }
            summaryTable.append("<TD><FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>"
                    + ((int) (pass / (pass + fail) * 100)) + "%</FONT></TD>");
            summaryTable.append("</TR>");

        }
        summaryTable.append("</TABLE>");
        return summaryTable.append(testCaseTable);
    }

    private static StringBuilder getEnvironmentTable(final String hostnameAdmin, final String hostnameEniq,
            final String startTime, final String endTime, final boolean loggingFlag, final boolean isLogFileUploadEnable) {
        final StringBuilder environmentTable = new StringBuilder();
        environmentTable.append("<BR><BR>");
        environmentTable.append("<TABLE id='environment' BORDER = 0 CELLSPACING = 0 CELLPADDING = 0 >");
        environmentTable.append("<TR align='left'>");
        environmentTable.append("<TD><H6>HOST(Admin):</TD>");
        environmentTable.append("<TD></TD>");
        environmentTable.append("<TD><H6>" + hostnameAdmin + "</TD>");
        environmentTable.append("</TR>");
        environmentTable.append("<TR align='left'>");
        environmentTable.append("<TD><H6>HOST(Eniq):</TD>");
        environmentTable.append("<TD></TD>");
        environmentTable.append("<TD><H6>" + hostnameEniq + "</TD>");
        environmentTable.append("</TR>");
        environmentTable.append("<TR align='left'>");
        environmentTable.append("<TD><H6>START TIME:</TD>");
        environmentTable.append("<TD></TD>");
        environmentTable.append("<TD><H6>" + startTime + "</TD>");
        environmentTable.append("</TR>");
        environmentTable.append("<TR align='left'>");
        environmentTable.append("<TD><H6>END TIME:</TD>");
        environmentTable.append("<TD></TD>");
        environmentTable.append("<TD><H6>" + endTime + "</TD>");
        environmentTable.append("</TR>");
        if (loggingFlag && isLogFileUploadEnable) {
            environmentTable.append("<TR align='left'>");
            environmentTable.append("<TD><H6>Log File:</TD>");
            environmentTable.append("<TD></TD>");
            environmentTable.append("<TD><H6><A href='" + logFilename + getFileNameTimeStamp() + ".log"
                    + "'>Click here >></A></TD>");
            environmentTable.append("</TR>");
        }
        environmentTable.append("</TABLE>");
        return environmentTable;
    }

    private void init() {

    }

    public static String getParentPath() {
        if (VM_TEST_BLADE) {
            return resourcePathOnBlade;
        }
        return resourcePathInVobs;
    }

    private static String getFileNameTimeStamp() {
        if (timeStamp == null) {
            final DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");

            final DateFormat timeFormat = new SimpleDateFormat("HHmmss");

            final Date date = new Date();
            String browserType = System.getProperty("sel.btype");

            timeStamp = "_" + dayFormat.format(date) + "-" + timeFormat.format(date);
            if ((browserType != null) && (!browserType.equals(""))) {
                browserType = browserType.replace(" ", "");
                browserType = browserType.replaceAll("[^a-zA-Z 0-9]+", "");
                timeStamp += "-" + browserType;
            }
        }
        return timeStamp;
    }

    public static String getReportFileName() {

        return reportFileName + getFileNameTimeStamp() + ".html";
    }

    public static String getLogFileName() {

        return logFilename + getFileNameTimeStamp() + ".log";
    }

    public static String getReportFileAbsolutePath() {

        return getParentPath() + File.separator + reportFileName + getFileNameTimeStamp() + ".html";
    }

    public static String getLogFileAbsolutePath() {

        return getParentPath() + File.separator + logFilename + getFileNameTimeStamp() + ".log";
    }

    private static final String htmlHeader = "<HTML>" + "<TITLE>ENIQ Events | AAC Regression Test Summary</TITLE>"
            + "<FONT FACE = VERDANA COLOR = DARKBLUE>" + "<BODY><H2 align='center'>AAC Regression Summary</H2>";

    private static final String htmlFooter = "</BODY></HTML>";

    public class TestCase implements Comparable<TestCase> {

        public static final String PASS_STATUS = "PASS";

        public static final String FAIL_STATUS = "FAIL";

        private final String name;

        private final String group;

        private final String id;

        private final String status;

        private final String failureReason;

        public TestCase(final String name, final String group, final String id, final boolean status,
                final String failureReason) {
            this.name = name;
            this.group = group;
            this.id = id;
            this.failureReason = failureReason;
            if (status) {
                this.status = PASS_STATUS;
                return;
            }
            this.status = FAIL_STATUS;
        }

        public String getName() {
            return name;
        }

        public String getGroup() {
            return group;
        }

        public String getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getFailureReason() {
            return failureReason;
        }

        //@Override
        public int compareTo(final TestCase obj) {
            return this.getGroup().compareToIgnoreCase(obj.getGroup());
        }

    }
}
