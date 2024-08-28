package com.ericsson.eniq.events.ui.selenium.tests.adminui;

import java.util.ArrayList;

import org.junit.*;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.events.login.AdminLogin;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;

public class AdminUITestGroup extends BaseSeleniumTest {

    private AdminLogin adminLogin = new AdminLogin();
    String adminUser = PropertyReader.getInstance().getAdminUser();
    String adminPass = PropertyReader.getInstance().getAdminPwd();
    private ArrayList<String> servicesInGreen = new ArrayList<String>();
    private ArrayList<String> servicesInRed = new ArrayList<String>();
    private ArrayList<String> servicesInYellow = new ArrayList<String>();
    private ArrayList<String> servicesInGrey = new ArrayList<String>();

    @SuppressWarnings("deprecation")
    @Override
    @Before
    public void setUp() {
        selenium.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath() + "servlet/LoaderStatusServlet");
        selenium.type("id=username", adminUser);
        selenium.type("id=password", adminPass);
        selenium.click("id=submit");
        selenium.waitForPageToLoad("60000");
    }

    @After
    @Override
    public void tearDown() {
        //TODO: Add timestamp
        if (selenium != null) {
            selenium.close();
            selenium.stop();
            selenium = null;
        }
    }

    @Test
    public void checkingServicesStatus() throws Exception {

        Thread.sleep(60000);
        if (!selenium.isTextPresent("System Status")) {
            Thread.sleep(600000);
        }
        selenium.click("link=System Status");
        //selenium.waitForPageToLoad("60000");
        Thread.sleep(60000);

        int numberofBlade = selenium.getXpathCount("//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr").intValue();

        for (int i = 1; i <= numberofBlade; i++) {
            int numberofServices = selenium.getXpathCount(
                    "//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i + "]//table/tbody//td/font[@color='green']").intValue();
            for (int j = 1; j <= numberofServices; j++) {
                String serviceInGreen = selenium.getText("//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i
                        + "]//table/tbody//td/font[@color='green'][" + j + "]");
                servicesInGreen.add(serviceInGreen);
                System.out.println("Service: " + serviceInGreen + " is in color Green!!");
            }
        }

        for (int i = 1; i <= numberofBlade; i++) {
            int numberofServices = selenium.getXpathCount(
                    "//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i + "]//table/tbody//td/font[@color='red']").intValue();
            for (int j = 1; j <= numberofServices; j++) {
                String serviceInRed = selenium.getText("//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i
                        + "]//table/tbody//td/font[@color='red'][" + j + "]");
                if (!serviceInRed.contains("ec_") && numberofBlade < 3) {
                    servicesInRed.add(serviceInRed);
                    System.out.println("Service: " + serviceInRed + " is in color Red!!");
                }
            }
        }

        for (int i = 1; i <= numberofBlade; i++) {
            int numberofServices = selenium.getXpathCount(
                    "//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i + "]//table/tbody//td/font[@color='yellow']").intValue();
            for (int j = 1; j <= numberofServices; j++) {
                String serviceInYellow = selenium.getText("//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i
                        + "]//table/tbody//td/font[@color='yellow'][" + j + "]");
                if (!servicesInYellow.contains("ec_") && numberofBlade < 3) {
                    servicesInYellow.add(serviceInYellow);
                    System.out.println("Service: " + serviceInYellow + " is in color Yellow!!");
                }
            }
        }

        for (int i = 1; i <= numberofBlade; i++) {
            int numberofServices = selenium.getXpathCount(
                    "//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i + "]//table/tbody//td/font[@color='gray']").intValue();
            for (int j = 1; j <= numberofServices; j++) {
                String serviceInGrey = selenium.getText("//html/body/table/tbody/tr[1]/td[2]/table[1]/tbody[2]/tr[" + i
                        + "]//table/tbody//td/font[@color='gray'][" + j + "]");
                servicesInGrey.add(serviceInGrey);
                System.out.println("Service: " + serviceInGrey + " is in color Grey!!");
            }
        }

        if (!servicesInRed.isEmpty() || !servicesInYellow.isEmpty() || !servicesInGrey.isEmpty()) {
            String str = servicesInRed.toString() + servicesInYellow.toString() + servicesInGrey.toString();
            Exception e = new Exception(str + " services are not running correctly!");
            System.out.println(e.toString());
            throw e;
        }

    }

}
