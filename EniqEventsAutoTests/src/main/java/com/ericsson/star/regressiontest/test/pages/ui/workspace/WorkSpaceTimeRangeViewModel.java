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

public class WorkSpaceTimeRangeViewModel implements ViewModel{

	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='15 minutes']")
	private Button fifteen_min_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='30 minutes']")
	private Button thirty_min_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='1 hour']")
	private Button one_hour_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='2 hours']")
	private Button two_hour_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='6 hours']")
	private Button six_hour_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='12 hours']")
	private Button twelve_hour_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='1 day']")
	private Button one_day_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='1 week']")
	private Button one_week_btn;
	
	@UiComponentMapping(id="<<xpath>>//div[@id='selenium_tag_timeRangeDropDown']//*[text()='Custom Time']")
	private Button custom_time_btn;
	
	public WorkSpaceTimeRangeViewModel(UiMediator mediator, UiComponentAutowirer componentInitializer){
		super();
	}
	
	public void selectThirtyMinutes(){
		thirty_min_btn.click();
	}
	
	public void selectOneDay(){
		one_day_btn.click();
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
