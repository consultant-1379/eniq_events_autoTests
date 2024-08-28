package com.ericsson.star.regressiontest.test.pages.ui.workspace;

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

public class MainPageViewModel implements ViewModel{

	@UiComponentMapping(id="<<xpath>>//div[@id='headerPnl']/table/tbody/tr/td[3]/div[text()='Options']")
	private Button optionsButton;
	
	@UiComponentMapping(id="<<xpath>>//*[@id='selenium_tag_timeRangeDropDown']/div/table/tbody/tr/td[1]")
	private Button timeRangeButton;
	
	@UiComponentMapping(id="<<xpath>>//*[@id='selenium_tag_timeRangeDropDown']/div/table/tbody/tr/td[1]")
	private Button dimensionButton;
	
	public MainPageViewModel(UiMediator mediator, UiComponentAutowirer componentInitializer){
		super();
	}
	
	public void clickOptionsButton(){
		optionsButton.click();
	}
	
	public boolean isOptionsMenuLabelDisplayed(){
		return optionsButton.isDisplayed();
	}
	
	public void clickTimeRange(){
		timeRangeButton.click();
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
