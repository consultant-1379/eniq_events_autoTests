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
package com.ericsson.eniq.events.ui.selenium.events.login;

import org.springframework.stereotype.Component;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;

@Component
public class FourGCoreUser extends AUserLogin {

    @Override
    public String getUserName() {
        return SeleniumConstants.FOUR_G_CORE_USER_ID;
    }

    @Override
    protected String getUserPwd() {
        return SeleniumConstants.FOUR_G_CORE_USER_PASSWORD;
    }

}
