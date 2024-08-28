package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class KPIBaseUnitTest extends EniqEventsWebDriverBaseUnitTest {

	/**
	 * Select the required option for a group of options in a dropdown
	 * @param kpiName
	 * @param options
	 */
	public void selectKPIFromDropdown(String kpiName, List<WebElement> options){
		for(WebElement we : options){
			if(we.getText().equals(kpiName)){
				we.click();
				break;
			}
		}
	}
	
	public void mouseOver(final WebElement element) {
        try {
            final Actions builder = new Actions(driver);
            final Action action = builder.moveToElement(element).build();
            action.perform();        
            Thread.sleep(5000L);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
