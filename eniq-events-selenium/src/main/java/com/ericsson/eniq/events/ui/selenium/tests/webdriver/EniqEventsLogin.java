package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * 
 * @author ekumpen 2012
 */
@Component
public class EniqEventsLogin extends WebUserLogin {

	protected static Logger loggerDuplicat = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

	@Override
	public String getUserName() {
		return PropertyReader.getInstance().getUser();
	}

	@Override
	protected String getUserPwd() {
		return PropertyReader.getInstance().getPwd();
	}

}
/*
 * protected static Logger loggerDuplicate =
 * Logger.getLogger(SeleniumLoggerDuplicate.class.getName()); private final
 * EricssonSelenium selenium;
 * 
 * @Autowired public EventsLogin(final EricssonSelenium sel) { super(sel);
 * selenium = sel;
 */