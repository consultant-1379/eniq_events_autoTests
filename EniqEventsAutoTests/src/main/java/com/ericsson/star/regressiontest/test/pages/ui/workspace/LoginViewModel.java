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

public class LoginViewModel implements ViewModel{

	private final String loginButtonXpath = "/html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[2]/table/tbody/tr[2]/td/div/div[1]/div/div[3]";
	
	@UiComponentMapping(id="<<xpath>>"+loginButtonXpath)
	private Button loginButton;

	@UiComponentMapping(id="<<xpath>>/html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[2]/table/tbody/tr[2]/td/div/div[1]/div/input")
	private TextBox usernameBox;

	@UiComponentMapping(id="<<xpath>>/html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[2]/table/tbody/tr[2]/td/div/div[1]/div/div[2]/input[1]")
	private TextBox passwordBoxToEnterText;

	//When you click this it is no longer visible in DOM, and other TextBox becomes visible
	@UiComponentMapping(id="<<xpath>>//input[@class='EInputBox EInputBox-light EInputBox-prompt'][@type='text'][1]")
	private Button passwordBoxToClickToGetFocus;

	@UiComponentMapping(id="<<xpath>>//*[text()='Launch Menu']")
	private TextBox launchMenuHeaderLabel;

	@UiComponentMapping(id="<<xpath>>//td[text()='Ranking']")
	private Button rankingTab;
	
	
	public LoginViewModel(UiMediator mediator, UiComponentAutowirer componentInitializer) {
		super();
	}

	public void setUsername(String username) {
		usernameBox.setText(username);

	}

	public void setPassword(String password) {
		passwordBoxToClickToGetFocus.click();

		passwordBoxToEnterText.setText(password);
	}

	public void clickLoginButton() {
		loginButton.click();
	}

	public String getHeaderText(){


		return launchMenuHeaderLabel.getText();
	}

	public void clickRankingButton(){
		
		rankingTab.click();
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
