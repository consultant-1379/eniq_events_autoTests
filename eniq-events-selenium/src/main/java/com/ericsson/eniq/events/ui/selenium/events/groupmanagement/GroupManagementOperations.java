package com.ericsson.eniq.events.ui.selenium.events.groupmanagement;


import static com.ericsson.eniq.events.ui.selenium.common.SeleniumUtils.*;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.TagNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

/**
 * @author elukpot
 * @since 4.0.16
 */
@Component
public class GroupManagementOperations {

	/**
	 * Written by elukpot.
	 *
	 * Adds the specified TACs to the Group name, it will create the group with the default TAC "123", if it does not exist.
	 *
	 * @param tac
	 *        The {@code String} representation of the TAC to be added to the Group.
	 * @param terminalGroupName
	 *        The name of the Terminal Group to add the TAC to.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void addTacToTerminalGroup(final String tac, final String terminalGroupName, final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {

		try {
			groupManagementWindow.launchGroupType(GuiStringConstants.TERMINAL);
		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		if (!groupManagementWindow.isGroupNameExist(terminalGroupName))
			createTerminalGroup(terminalGroupName, groupManagementWindow, groupManagementEditWindow);

		groupManagementWindow.selectItem(terminalGroupName);
		groupManagementWindow.clickEditButton();
		groupManagementEditWindow.waitForLoading("Loading Group Elements...");
		groupManagementEditWindow.enterGroupItems(tac);
		groupManagementEditWindow.clickSave();
		groupManagementEditWindow.waitForLoading("Saving Group...");
		pause(500);
		groupManagementWindow.clickOpenDialogDismissButton();
		pause(500);
		groupManagementWindow.closeGroupManagementWindow();
	}

	/**
	 * Written by elukpot.
	 *
	 * Adds the specified TACs to the Group name, it will create the group with the default TAC "123", if it does not exist.
	 *
	 * @param tacs
	 *        The {@code List<String>} of TACs to be added to the Group.
	 * @param terminalGroupName
	 *        The name of the Terminal Group to add the TACs to.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void addTacsToTerminalGroup(final List<String> tacs, final String terminalGroupName,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {

		String tacsToAddToGroup = "";
		for (final String tac : tacs)
			tacsToAddToGroup += tac + ",";
		tacsToAddToGroup = tacsToAddToGroup.substring(0, tacsToAddToGroup.length() - 1);

		try {
			groupManagementWindow.launchGroupType(GuiStringConstants.TERMINAL);
		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		if (!groupManagementWindow.isGroupNameExist(terminalGroupName))
			createTerminalGroup(terminalGroupName, groupManagementWindow, groupManagementEditWindow);

		groupManagementWindow.selectItem(terminalGroupName);
		groupManagementWindow.clickEditButton();
		groupManagementEditWindow.waitForLoading("Loading Group Elements...");
		groupManagementEditWindow.enterGroupItems(tacsToAddToGroup);
		groupManagementEditWindow.clickSave();
		groupManagementEditWindow.waitForLoading("Saving Group...");
		pause(500);
		groupManagementWindow.clickOpenDialogDismissButton();
		pause(500);
		groupManagementWindow.closeGroupManagementWindow();
	}

	/**
	 * Written by elukpot.
	 *
	 * Creates the Terminal Group with the name specified, there is a default element of "123" in the group.
	 *
	 * @param tacGroupName
	 *        The name of the Terminal Group to create.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void createTerminalGroup(final String tacGroupName, final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {
		try {
			groupManagementWindow.clickNewButton();
		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		groupManagementEditWindow.enterGroupName(tacGroupName);
		groupManagementEditWindow.enterGroupItems("123");
		groupManagementEditWindow.clickAddButton();
		groupManagementEditWindow.clickSave();
		groupManagementWindow.waitForLoading("Saving Group...");
		groupManagementWindow.clickOpenDialogDismissButton();
	}

	/**
	 * Written by eboojus.
	 *
	 * Creates the Terminal Group with the name specified, with the tacs from supplied list.
	 *
	 * @param tacGroupName
	 *        The name of the Terminal Group to create.
	 * @param tacsToAdd
	 *        List of Tacs to Add to List.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void createTerminalGroupFromList(final String tacGroupName, final List<String> tacsToAdd,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {
		String commaSeperatedTacs = "";

		try {
			groupManagementWindow.clickNewButton();
		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		groupManagementEditWindow.enterGroupName(tacGroupName);
		for (final String tac : tacsToAdd) {
			commaSeperatedTacs += tac + ",";
		}
		groupManagementEditWindow.enterGroupItems(commaSeperatedTacs);
		groupManagementEditWindow.clickAddButton();
		groupManagementEditWindow.clickSave();
		groupManagementWindow.waitForLoading("Saving Group...");
		groupManagementWindow.clickOpenDialogDismissButton();
	}

	public static void createAccessAreaGroupFromList(String groupName,List<String> topFailingAccessAreas,GroupManagementWindow groupManagementWindow,GroupManagementEditWindow groupManagementEditWindow) throws PopUpException{
		groupManagementWindow.clickNewButton();

		groupManagementEditWindow.enterGroupName(groupName);
		groupManagementEditWindow.enterGroupItemsUsingFilter(topFailingAccessAreas, TagNameConstants.ACCESS_AREA_GROUP_MGMT_TAG);
		pause(1000);
		groupManagementEditWindow.clickSave();
		groupManagementEditWindow.waitForLoading("Saving Group...");
		pause(1000);
		groupManagementWindow.clickOpenDialogDismissButton();    	
	}

	/**
	 * Written by elukpot.
	 *
	 * Removes the specified TACs from the Terminal Group.
	 *
	 * @param tac
	 *        The {@code String} representation of the TAC to be removed to the Group.
	 * @param terminalGroupName
	 *        The name of the Terminal Group to remove the TAC from.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void removeTacFromTerminalGroup(final String tac, final String terminalGroupName,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {

		try {
			groupManagementWindow.launchGroupType(GuiStringConstants.TERMINAL);
		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		groupManagementWindow.selectItem(terminalGroupName);
		groupManagementWindow.clickEditButton();
		groupManagementEditWindow.waitForLoading("Loading Group Elements...");
		groupManagementEditWindow.removeGroupElement(tac);
		groupManagementEditWindow.clickSave();
		groupManagementEditWindow.waitForLoading("Saving Group...");
		pause(500);
		groupManagementWindow.clickOpenDialogDismissButton();
		pause(500);
		groupManagementWindow.closeGroupManagementWindow();
	}

	/**
	 * Written by elukpot.
	 *
	 * Removes the specified TACs from the Terminal Group.
	 *
	 * @param tacs
	 *        The {@code List<String>} of TACs to be removed to the Group.
	 * @param terminalGroupName
	 *        The name of the Terminal Group to remove the TACs from.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void removeTacsFromTerminalGroup(final List<String> tacs, final String terminalGroupName,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {

		try {
			groupManagementWindow.launchGroupType(GuiStringConstants.TERMINAL);
		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		groupManagementWindow.selectItem(terminalGroupName);
		groupManagementWindow.clickEditButton();
		groupManagementEditWindow.waitForLoading("Loading Group Elements...");
		groupManagementEditWindow.removeGroupElements(tacs);
		groupManagementEditWindow.clickSave();
		groupManagementEditWindow.waitForLoading("Saving Group...");
		pause(500);
		groupManagementWindow.clickOpenDialogDismissButton();
		pause(500);
		groupManagementWindow.closeGroupManagementWindow();
	}

	/**
	 * Written by elukpot.
	 *
	 * Checks if a group of the name specified exists for the Group Type.
	 *
	 * @param groupName
	 *        The name of the group to check for.
	 * @param groupType
	 *        The type of group to check in, e.g. APN, Terminal, etc...
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @return True, if the group exists. False, if the group does not exists.
	 */
	public static boolean isExistingGroup(final String groupName, final String groupType, final GroupManagementWindow groupManagementWindow) {

		try {
			groupManagementWindow.launchGroupType(groupType);

		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		final boolean isExistingGroup = groupManagementWindow.isGroupNameExist(groupName);

		groupManagementWindow.closeGroupManagementWindow();

		return isExistingGroup;
	}

	/**
	 * Written by elukpot.
	 *
	 * Creates an APN Group with the specified name and APNs.
	 *
	 * @param apnGroupName
	 *        The name to give the newly created group.
	 * @param apnsToAddToGroup
	 *        The APNs to add to the group.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void createApnGroup(final String apnGroupName, final List<String> apnsToAddToGroup,
			final GroupManagementWindow groupManagementWindow, final GroupManagementEditWindow groupManagementEditWindow) {

		if (isExistingGroup(apnGroupName, GuiStringConstants.APN, groupManagementWindow)) {
			return;
		}

		try {
			groupManagementWindow.launchGroupType(GuiStringConstants.APN);
			groupManagementWindow.clickNewButton();

			groupManagementEditWindow.enterGroupName(apnGroupName);
			groupManagementEditWindow.enterGroupItemsUsingFilter(apnsToAddToGroup, GuiStringConstants.APN);
			pause(1000);
			groupManagementEditWindow.clickSave();
			groupManagementEditWindow.waitForLoading("Saving Group...");
			pause(1000);
			groupManagementWindow.clickOpenDialogDismissButton();
			pause(1000);
			groupManagementWindow.closeGroupManagementWindow();

		} catch (final PopUpException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Written by elukpot.
	 *
	 * Creates an IMSI Group with the specified name and IMSIs.
	 *
	 * @param imsiGroupName
	 *        The name to give the newly created group.
	 * @param imsisToAddToGroup
	 *        The IMSIs to add to the group.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void createImsiGroup(final String imsiGroupName, final List<String> imsisToAddToGroup,
			final GroupManagementWindow groupManagementWindow, final GroupManagementEditWindow groupManagementEditWindow) {

		if (isExistingGroup(imsiGroupName, GuiStringConstants.IMSI, groupManagementWindow)) {
			return;
		}

		try {
			groupManagementWindow.launchGroupType(GuiStringConstants.IMSI);
			groupManagementWindow.clickNewButton();

			groupManagementEditWindow.enterGroupName(imsiGroupName);

			String commaSeparatedImsis = "";
			for (final String imsi : imsisToAddToGroup)
				commaSeparatedImsis += imsi + ",";
			commaSeparatedImsis = commaSeparatedImsis.substring(0, commaSeparatedImsis.length() - 1);

			groupManagementEditWindow.enterGroupItems(commaSeparatedImsis);

			groupManagementEditWindow.clickSave();
			groupManagementEditWindow.waitForLoading("Saving Group...");
			pause(500);
			groupManagementWindow.clickOpenDialogDismissButton();
			pause(500);
			groupManagementWindow.closeGroupManagementWindow();

		} catch (final PopUpException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Written by elukpot.
	 *
	 * Creates a SGSN-MME group with the specified name and SGSN-MMEs.
	 *
	 * @param sgsnMmeGroupName
	 *        The name to give the newly created group.
	 * @param sgsnsToAddToGroup
	 *        The SGSN-MMEs to add to the group.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 */
	public static void createSgsnGroup(final String sgsnMmeGroupName, final List<String> sgsnsToAddToGroup,
			final GroupManagementWindow groupManagementWindow, final GroupManagementEditWindow groupManagementEditWindow) {

		if (isExistingGroup(sgsnMmeGroupName, GuiStringConstants.SGSN__MME, groupManagementWindow)) {
			return;
		}

		try {

			groupManagementWindow.launchGroupType(GuiStringConstants.SGSN__MME);
			groupManagementWindow.clickNewButton();

			groupManagementEditWindow.enterGroupName(sgsnMmeGroupName);
			groupManagementEditWindow.enterGroupItemsUsingFilter(sgsnsToAddToGroup, GuiStringConstants.SGSN__MME);
			groupManagementEditWindow.clickSave();
			groupManagementEditWindow.waitForLoading("Saving Group...");
			pause(500);
			groupManagementWindow.clickOpenDialogDismissButton();
			pause(500);
			groupManagementWindow.closeGroupManagementWindow();

		} catch (final PopUpException e) {
			e.printStackTrace();
		}
	}

	public static void createControllerGroup(final String controllerGroupName, final List<String> controllersToAddToGroup,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {

		if (isExistingGroup(controllerGroupName, GuiStringConstants.CONTROLLER, groupManagementWindow)) {
			return;
		}

		try {

			groupManagementWindow.launchGroupType(GuiStringConstants.CONTROLLER);
			groupManagementWindow.clickNewButton();
			pause(10000);
			groupManagementEditWindow.enterGroupName(controllerGroupName);
			groupManagementEditWindow.enterGroupItemsUsingFilter(controllersToAddToGroup, GuiStringConstants.CONTROLLER.toUpperCase());
			groupManagementEditWindow.clickSave();
			groupManagementEditWindow.waitForLoading("Saving Group...");
			pause(2000);
			groupManagementWindow.clickOpenDialogDismissButton();
			pause(500);
			groupManagementWindow.closeGroupManagementWindow();

		} catch (final PopUpException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Written by eboojus.
	 *
	 * Get all group items for a given group name.
	 *
	 * @param groupName
	 *        The name of the group to retrieve items from.
	 * @param groupType
	 *        The type of group to be searched for.
	 * @param groupManagementWindow
	 *        Pass a reference to the Group Management Window.
	 * @param groupManagementEditWindow
	 *        Pass a reference to the Group Management Edit Window.
	 * @return A list of group items
	 */
	public static List<String> getASpecificGroupsItems(final String groupName, final String groupType,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) {

		List<String> itemsToReturn = new ArrayList<String>();

		if (!isExistingGroup(groupName, groupType, groupManagementWindow)) {
			return itemsToReturn;
		}

		try {
			groupManagementWindow.launchGroupType(groupType);
			pause(1000);
			groupManagementWindow.selectItem(groupName);
			pause(2000);
			groupManagementWindow.clickEditButton();
			pause(3000);
			itemsToReturn = groupManagementEditWindow.getCurrentGroupItems();

			pause(1000);

			groupManagementEditWindow.clickCancel();
			pause(1000);
			groupManagementWindow.closeGroupManagementWindow();

		} catch (final PopUpException e) {
			e.printStackTrace();
		}

		return itemsToReturn;
	}
	
	public static void createTrackingAreaGroupFromList(final String groupName, final List<String> trackingAreasToAdd,
			final GroupManagementWindow groupManagementWindow,
			final GroupManagementEditWindow groupManagementEditWindow) throws PopUpException{
		
		groupManagementWindow.launchGroupType(GuiStringConstants.TRACKING_AREA);
		groupManagementWindow.clickNewButton();

		groupManagementEditWindow.enterGroupName(groupName);
		groupManagementEditWindow.enterGroupItemsUsingFilter(trackingAreasToAdd, TagNameConstants.TRACKING_AREA_GROUP_MGMT_TAG);
		pause(1000);
		groupManagementEditWindow.clickSave();
		groupManagementEditWindow.waitForLoading("Saving Group...");
		pause(1000);
		groupManagementWindow.clickOpenDialogDismissButton();
	}
}
