package com.ericsson.star.regressiontest.test.pages.ui.optionsmenu;

import java.util.List;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentAutowirer;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;

public class AboutOptionViewModel implements ViewModel{
	@UiComponentMapping(id="<<xpath>>//div[contains(@class, 'about')]")
	private Button aboutOptionWindow;
	
	@UiComponentMapping(id="<<xpath>>//div[contains(@class, 'about')]//div[contains(@class, 'close')]")
	private Button xButton;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010923']")
	private Button featureLabelCXC4010923;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010924']")
	private Button featureLabelCXC4010924;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010925']")
	private Button featureLabelCXC4010925;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010926']")
	private Button featureLabelCXC4010926;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010927']")
	private Button featureLabelCXC4010927;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010928']")
	private Button featureLabelCXC4010928;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010929']")
	private Button featureLabelCXC4010929;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010930']")
	private Button featureLabelCXC4010930;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4010933']")
	private Button featureLabelCXC4010933;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011049']")
	private Button featureLabelCXC4011049;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011158']")
	private Button featureLabelCXC4011158;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011159']")
	private Button featureLabelCXC4011159;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011268']")
	private Button featureLabelCXC4011268;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011278']")
	private Button featureLabelCXC4011278;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011279']")
	private Button featureLabelCXC4011279;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011280']")
	private Button featureLabelCXC4011280;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011281']")
	private Button featureLabelCXC4011281;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011291']")
	private Button featureLabelCXC4011291;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011309']")
	private Button featureLabelCXC4011309;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011318']")
	private Button featureLabelCXC4011318;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011324']")
	private Button featureLabelCXC4011324;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011325']")
	private Button featureLabelCXC4011325;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011380']")
	private Button featureLabelCXC4011380;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011382']")
	private Button featureLabelCXC4011382;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011426']")
	private Button featureLabelCXC4011426;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011452']")
	private Button featureLabelCXC4011452;
	
	@UiComponentMapping(id="<<xpath>>//div[text() = 'Version : CXC4011470']")
	private Button featureLabelCXC4011470;
	
	@UiComponentMapping(id="<<xpath>>//div[contains(@class, 'about')]//div/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div")
	private Button vScrollBar;
	
	@UiComponentMapping(id="<<xpath>>//div[contains(@class, 'about')]//table/tbody/tr/td/img[@class='gwt-Image']")
	private Label logoLabel;
	
	@UiComponentMapping(id="<<xpath>>//div[contains(@class, 'about')]//td/table/tbody/tr[1]/td/div[@class='gwt-Label']")
	private Label versionLabel;
	
	@UiComponentMapping(id="<<xpath>>//div[contains(@class, 'about')]/div[2]/div[1]/div/div/div/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div[@class='gwt-Label']")
	private Label copyrightLabel;
	
	private Button[] featureLabel = {featureLabelCXC4010923, featureLabelCXC4010924, featureLabelCXC4010925, featureLabelCXC4010926, featureLabelCXC4010927, featureLabelCXC4010928, featureLabelCXC4010929,
			featureLabelCXC4010930, featureLabelCXC4010933, featureLabelCXC4011049, featureLabelCXC4011158, featureLabelCXC4011159, featureLabelCXC4011268, featureLabelCXC4011278, featureLabelCXC4011279, featureLabelCXC4011280,
			featureLabelCXC4011281, featureLabelCXC4011291,featureLabelCXC4011309, featureLabelCXC4011318,featureLabelCXC4011324, featureLabelCXC4011325, featureLabelCXC4011380, featureLabelCXC4011382, featureLabelCXC4011426,featureLabelCXC4011452, featureLabelCXC4011470};
	
	public AboutOptionViewModel(UiMediator mediator, UiComponentAutowirer componentInitializer){
		super();
	}
	
	public void clickXButton(){
		xButton.click();
	}
	
	public boolean isVisible(){
		return aboutOptionWindow.isDisplayed();
	}
	
	public boolean areAllInstalledFeaturesListed(){
		int i = 1;
		for(Button fLabel : featureLabel){
			System.out.println("Count: " + i);
			if(!fLabel.isDisplayed()){
				return false;
			}
			pause(2000);
			i++;
		}
		return true;
	}
	
	public boolean isEricssonLogoPresent(){
		return logoLabel.isDisplayed();
	}
	
	public boolean isEniqEventsVersionDisplayed(){
		return versionLabel.isDisplayed();
	}
	
	public boolean isEricssonCopyrightInfoDisplayed(){
		return copyrightLabel.isDisplayed();
	}
	
	public boolean isVerticalScrollBarDisplayedOnAboutOptionMenu(){
		return vScrollBar.isDisplayed();
	}
	
	/******************************* Private Methods *********************************/
	
	private void pause(int timeout){
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			System.out.println("Sleep Interrupted Exception Msg: " + e.getMessage());
		}
	}

	@Override
	public Button getButton(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Button getButton(SelectorType arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getLabel(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getLabel(SelectorType arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Link getLink(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Link getLink(SelectorType arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Select getSelect(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Select getSelect(SelectorType arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextBox getTextBox(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextBox getTextBox(SelectorType arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UiComponent getViewComponent(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends UiComponent> T getViewComponent(String arg0, Class<T> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends UiComponent> T getViewComponent(SelectorType arg0,
			String arg1, Class<T> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends UiComponent> List<T> getViewComponents(String arg0,
			Class<T> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends UiComponent> List<T> getViewComponents(SelectorType arg0,
			String arg1, Class<T> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasComponent(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasComponent(SelectorType arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrentView() {
		// TODO Auto-generated method stub
		return false;
	}
}
