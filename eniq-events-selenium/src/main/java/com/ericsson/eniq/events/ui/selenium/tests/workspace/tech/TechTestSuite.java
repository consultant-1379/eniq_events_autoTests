/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech;

import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.accessarea.AccessAreaTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.accessareagroup.AccessAreaGroupTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.apn.APNTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.apngroup.ApnGroupTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.controller.ControllerTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.controllergroup.ControllerGroupTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.imsi.ImsiTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.imsigroup.ImsiGroupTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.ptmsi.PtmsiTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.sgsn.SgsnTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.sgsngroup.SgsnGroupTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author eeikbe
 * @since 12/2013
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccessAreaTestSuite.class,
        AccessAreaGroupTestSuite.class,
        APNTestSuite.class,
        ApnGroupTestSuite.class,
        ControllerTestSuite.class,
        ControllerGroupTestSuite.class,
        SgsnTestSuite.class,
        SgsnGroupTestSuite.class,
        ImsiTestSuite.class,
        ImsiGroupTestSuite.class,
        PtmsiTestSuite.class
})
public class TechTestSuite {
}
