package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.AACSeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.tests.aac.AdminUIBaseSeleniumTest;
import com.thoughtworks.selenium.SeleniumException;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ejenkav
 * @since 2011
 * 
 */
@Component
public class AACTestReader extends AdminUIBaseSeleniumTest {
	private static final String aacPropertiesFile = "aacProperties.xml";
	// Uncomment below comment for Loading Data
	// private static final String aacPropertiesFile =
	// "aacPropertiesLoadData.xml";

	private static final String resourcePathOnBlade = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources";

	// Update this path to resources folder in your view if you are running
	// tests from eclipse. i.e. -
	// C:/your_view_folder/eniq_events/eniq_events_ui/src/test/selenium/resources
	private static final String resourcePathInVobs = "C:\\egercor_view5\\eniq_events\\auto_tests\\eniq-events-selenium\\target\\classes\\resources";

	public static final boolean VM_TEST_BLADE = System
			.getProperty("TEST_BLADE") != null;

	public static final String VM_PROJECT_ROOT_USED_BY_CI = System
			.getProperty("eniq.events.vob.root");

	public static String failureReason = "-";

	@Override
	@Before
	public void setUp() {
		super.setUp();
		AACSeleniumLogger.setUp();
		init();
	}

	@After
	@Override
	public void tearDown() {

		if (isLogFileUploadEnable) {
			// FTPUpload.copyAllFilesToUnixBox(ReportGenerator.getLogFileAbsolutePath(),
			// ReportGenerator.getLogFileName());
		}
		if (isReportFileUploadEnable) {
			// FTPUpload.copyAllFilesToUnixBox(ReportGenerator.getReportFileAbsolutePath(),ReportGenerator.getReportFileName());
		}
		super.tearDown();
		AACSeleniumLogger.tearDown();
	}

	private void init() {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(getPropertyFile());
			doc.getDocumentElement().normalize();
			initiateTestData(doc);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			aacLogger
					.severe("Can not find a test data XML file. Please ensure the file is existing - "
							+ getPropertyFile());
		} catch (final IOException e) {
			e.printStackTrace();
			aacLogger.severe("Errors occured while loading file - "
					+ getPropertyFile());
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
			aacLogger.severe("Errors occured while building file - "
					+ getPropertyFile());
		} catch (final SAXException e) {
			e.printStackTrace();
			aacLogger.severe("Errors occured while parsing file - "
					+ getPropertyFile());
		}
	}

	private void initiateTestData(final Document document) {
		if (document != null) {

			final NodeList userNodeList = document
					.getElementsByTagName(TOKEN_USER);
			if (userNodeList != null && userNodeList.getLength() > 0) {
				System.out.println("Number of Users: "
						+ userNodeList.getLength());

				for (int i = 0; i < userNodeList.getLength(); i++) {
					final User userObject = getUser((Element) userNodeList
							.item(i));
					if (userObject != null && userObject.getUserID() != null
							&& (!userObject.getUserID().equals(" "))) {
						System.out.println("Test Data user: "
								+ userObject.getUserID());
						testUsers.put(userObject.getUserID(), userObject);
					}
				}
			}

			final NodeList roleNodeList = document
					.getElementsByTagName(TOKEN_ROLE);
			if (roleNodeList != null && roleNodeList.getLength() > 0) {
				System.out.println("Number of Roles: "
						+ roleNodeList.getLength());

				for (int i = 0; i < roleNodeList.getLength(); i++) {
					final Role roleObject = getRole((Element) roleNodeList
							.item(i));
					if (roleObject != null && roleObject.getName() != null
							&& (!roleObject.getName().equals(" "))) {
						System.out.println("Test Data Role: "
								+ roleObject.getName());
						testRoles.put(roleObject.getName(), roleObject);
					}
				}
			}

			final NodeList perissionGroupNodeList = document
					.getElementsByTagName(TOKEN_PERMISSION_GROUP);
			if (perissionGroupNodeList != null
					&& perissionGroupNodeList.getLength() > 0) {
				System.out.println("Number of Permission Groups: "
						+ perissionGroupNodeList.getLength());

				for (int i = 0; i < perissionGroupNodeList.getLength(); i++) {
					final PermissionGroup perissionGroupObject = getPermissionGroup((Element) perissionGroupNodeList
							.item(i));
					if (perissionGroupObject != null
							&& perissionGroupObject.getName() != null
							&& (!perissionGroupObject.getName().equals(" "))) {
						System.out.println("Test Data Permission group: "
								+ perissionGroupObject.getName());
						testPermissionGroups.put(
								perissionGroupObject.getName(),
								perissionGroupObject);
					}
				}
			}

		}
	}

	@Test
	public void executeTestSuite() throws Exception {
		if (doc != null) {
			// Check if following are not null and is not empty

			final Element aacElement = ((Element) doc.getElementsByTagName(
					TOKEN_AAC).item(0));
			final boolean cleanupFlag = "false".equalsIgnoreCase(aacElement
					.getAttribute(ATTRIBUTE_CLEANUP)) ? false : true;
			final boolean loggingFlag = "false".equalsIgnoreCase(aacElement
					.getAttribute(ATTRIBUTE_LOGGING)) ? false : true;
			final boolean eniqUiFlag = "false".equalsIgnoreCase(aacElement
					.getAttribute(ATTRIBUTE_ENIQ_UI)) ? false : true;
			final String uploadValue = (aacElement
					.getAttribute(ATTRIBUTE_UPLOAD) != null && (""
					.equals(aacElement.getAttribute(ATTRIBUTE_UPLOAD)))) ? "all"
					: aacElement.getAttribute(ATTRIBUTE_UPLOAD);

			if (!loggingFlag) {
				aacLogger.setLevel(Level.OFF);
			}
			if ("all".equalsIgnoreCase(uploadValue)) {
				isLogFileUploadEnable = true;
				isReportFileUploadEnable = true;
			} else if ("log".equalsIgnoreCase(uploadValue)) {
				isLogFileUploadEnable = true;
			} else if ("report".equalsIgnoreCase(uploadValue)) {
				isReportFileUploadEnable = true;
			}
			String tcGroupAttribute = aacElement
					.getAttribute(ATTRIBUTE_TC_GROUP);
			String tcIDAttribute = aacElement.getAttribute(ATTRIBUTE_TC_ID);

			// if system variable is set, use it
			if (PropertyReader.getInstance().getTestSubSuiteNamesString() != null) {
				tcGroupAttribute = PropertyReader.getInstance()
						.getTestSubSuiteNamesString();
			}
			if (PropertyReader.getInstance().getTestCaseNamesString() != null) {
				tcIDAttribute = PropertyReader.getInstance()
						.getTestCaseNamesString();
			}
			String hostnameAdmin = PropertyReader.getInstance()
					.getAdminServerHost();
			String hostnameEniq = PropertyReader.getInstance().getEventHost();
			hostnameAdmin = hostnameAdmin
					.substring(hostnameAdmin.indexOf("//") + 2,
							hostnameAdmin.indexOf("."));
			hostnameEniq = hostnameEniq.substring(
					hostnameEniq.indexOf("//") + 2, hostnameEniq.indexOf("."));

			AACSeleniumLogger.writeHeader("\t\t\tAAC REGRESSION TEST LOG");
			AACSeleniumLogger.writeHeader("HOST(Admin): " + hostnameAdmin);
			AACSeleniumLogger.writeHeader("HOST(Eniq): " + hostnameEniq);
			AACSeleniumLogger.writeHeader("Running Test group: "
					+ tcGroupAttribute);
			AACSeleniumLogger
					.writeHeader("Running Test Case: " + tcIDAttribute);
			if (loggingFlag) {
				AACSeleniumLogger.writeHeader("Logging is Enabled");
			} else {
				AACSeleniumLogger.writeHeader("Logging is Disabled");
			}

			if (cleanupFlag) {
				AACSeleniumLogger.writeHeader("Cleanup is Enabled");
			} else {
				AACSeleniumLogger.writeHeader("Cleanup is Disabled");
			}
			if (eniqUiFlag) {
				AACSeleniumLogger
						.writeHeader("Eniq UI Operation testing is Enabled");
			} else {
				AACSeleniumLogger
						.writeHeader("Eniq UI Operation testing is Disabled");
			}

			final NodeList testCaseNodeList = doc
					.getElementsByTagName(TOKEN_TEST_CASE);
			final Date startTime = new Date(); // getCurrentTimeStamp();
			// System.out.println("Number of Test Cases: " +
			// testCaseNodeList.getLength());
			try {
				for (int i = 0; i < testCaseNodeList.getLength(); i++) {
					executeTestCase((Element) testCaseNodeList.item(i),
							cleanupFlag, eniqUiFlag, tcGroupAttribute,
							tcIDAttribute);
					// super.cleanUp();
				}

			} catch (final Exception exception) {
				final StackTraceElement[] st = exception.getStackTrace();
				for (final StackTraceElement se : st) {
					AACSeleniumLogger.writeHeader(se.toString());
				}

			}
			final Date endTime = new Date();
			ReportGenerator.generate(hostnameAdmin, hostnameEniq, startTime,
					endTime, loggingFlag, isLogFileUploadEnable);

		}
	}

	private boolean executeTestCase(final Element item,
			final boolean cleanupFlag, final boolean eniqUiFlag,
			final String tcGroupAttribute, final String tcIDAttribute) {
		final String tcGroup = item.getElementsByTagName(TOKEN_TC_GROUP)
				.item(0).getTextContent();

		boolean isPresent = false;
		for (final String tcGroupAttributeElement : tcGroupAttribute.split(",")) {
			if (tcGroupAttributeElement.equalsIgnoreCase(tcGroup)) {
				isPresent = true;
				break;
			}
		}
		if (!(tcGroupAttribute.equalsIgnoreCase("ALL") || isPresent)) {
			System.out.println("Skipping Test case for Group " + tcGroup);
			return true;
		}

		final String tcID = item.getElementsByTagName(TOKEN_TC_ID).item(0)
				.getTextContent();
		isPresent = false;
		for (final String tcIDAttributeElement : tcIDAttribute.split(",")) {
			if (tcIDAttributeElement.equalsIgnoreCase(tcID)) {
				isPresent = true;
				break;
			}
		}
		if (!(tcIDAttribute.equalsIgnoreCase("ALL") || isPresent)) {
			System.out.println("Skipping Test Case: ID " + tcID);
			return true;
		}
		final NodeList testCaseNameNodeList = item
				.getElementsByTagName(TOKEN_TC_NAME);
		// Check if above not null and is not empty
		final String tcName = testCaseNameNodeList.item(0).getTextContent();
		AACSeleniumLogger
				.writeHeader(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		AACSeleniumLogger.writeHeader("Test Case ID: " + tcID);
		AACSeleniumLogger.writeHeader("Test Case Name: " + tcName);
		AACSeleniumLogger.writeHeader("Test Case Group: " + tcGroup);

		System.out.println("Executing test case " + tcName);

		if (!executeTestSteps(item, cleanupFlag, eniqUiFlag)) {
			AACSeleniumLogger.writeHeader("Test Case Status: FAILS");
			AACSeleniumLogger
					.writeHeader("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			ReportGenerator.getInstance().addTestCase(tcName, tcGroup, tcID,
					false, failureReason);
			return false;
		}

		AACSeleniumLogger.writeHeader("Test Case Status: PASS");
		AACSeleniumLogger
				.writeHeader("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		ReportGenerator.getInstance().addTestCase(tcName, tcGroup, tcID, true,
				failureReason);
		return true;
	}

	private boolean executeTestSteps(final Element item,
			final boolean cleanupFlag, final boolean eniqUiFlag) {
		failureReason = "-";
		final NodeList testCaseChildrensNodeList = item
				.getElementsByTagName("*");
		for (int i = 0; i < testCaseChildrensNodeList.getLength(); i++) {
			String msg = "";
			try {
				final String operationName = testCaseChildrensNodeList.item(i)
						.getNodeName();
				String operationArguments = testCaseChildrensNodeList.item(i)
						.getTextContent();
				if ((null != operationArguments)
						&& (!"".equalsIgnoreCase(operationArguments))) {
					final String[] operationArgumentArray = operationArguments
							.split(",");
					String newOperationArgument = "";
					for (int count = 0; count < operationArgumentArray.length; count++) {
						if (count == operationArgumentArray.length - 1) {
							operationArguments = newOperationArgument
									+ operationArgumentArray[count].trim();
						} else {
							newOperationArgument += operationArgumentArray[count]
									.trim() + ",";
						}
					}

				}
				boolean isNegative = false;
				final String negativeAttribute = ((Element) testCaseChildrensNodeList
						.item(i)).getAttribute(ATTRIBUTE_NEGATIVE);
				if ((negativeAttribute != null)
						&& (!negativeAttribute.equals(""))
						&& (negativeAttribute.equalsIgnoreCase("true"))) {
					isNegative = true;
				}

				if (operationName.equalsIgnoreCase(OPERATION_LOGIN_ADMINUI)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Login to AdiminUI requires exactly two arguments: username, password";
						assertTrue(false);
					}
					final String userName = operationArguments.split(",")[0];
					final String password = operationArguments.split(",")[1];
					msg = "Login to AdiminUI with Username=" + userName
							+ " Password=" + password;
					assertTrue(userMgmt.logIn(userName, password));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_LOGIN_ADMINUI_AS_ADMIN)) {
					final String userName = PropertyReader.getInstance()
							.getAdminUser();
					final String password = PropertyReader.getInstance()
							.getAdminPwd();
					msg = "Login to AdiminUI as Administrator Username="
							+ userName + " Password=" + password;
					assertTrue(userMgmt.logInAsAdmin());
				} else if (operationName
						.equalsIgnoreCase(OPERATION_LOGOUT_ADMINUI)) {
					msg = "Logout of AdminUI";
					assertTrue(userMgmt.logOut());
				} else if (operationName
						.equalsIgnoreCase(OPERATION_LOGIN_ENIQUI)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Login to EniqUI requires exactly two arguments: username, password";
						assertTrue(false);
					}
					final String userName = operationArguments.split(",")[0];
					final String password = operationArguments.split(",")[1];

					if (eniqUiFlag == true) {
						msg = "Login to EniqUI with Username=" + userName
								+ " Password=" + password;
						if (isNegative) {
							assertFalse(eniqMgmt.login(userName, password));
						} else {
							assertTrue(eniqMgmt.login(userName, password));
						}
					} else {
						msg = "Skipping Eniq Operation: Login to EniqUI with Username="
								+ userName + " Password=" + password;
					}

				} else if (operationName
						.equalsIgnoreCase(OPERATION_LOGIN_ENIQUI_AS_ADMIN)) {
					final String userName = PropertyReader.getInstance()
							.getUser();
					final String password = PropertyReader.getInstance()
							.getPwd();
					if (eniqUiFlag == true) {
						msg = "Login to EniqUI as Administrator Username="
								+ userName + " Password=" + password;
						assertTrue(eniqMgmt.logInAsAdmin());
					} else {
						msg = "Skipping Eniq Operation: Login to EniqUI as Administrator Username="
								+ userName + " Password=" + password;
					}
				} else if (operationName
						.equalsIgnoreCase(OPERATION_LOGOUT_ENIQUI)) {
					if (eniqUiFlag == true) {
						msg = "Logout of EniqUI";
						assertTrue(eniqMgmt.logout());
					} else {
						msg = "Skipping Eniq Operation: Logout of EniqUI";
					}
				} else if (operationName
						.equalsIgnoreCase(OPERATION_CHANGE_PASSWORD_ENIQ)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 3)) {
						msg = "Change Password for EniqUI requires exactly three arguments: username, old password and new password";
						assertTrue(false);
					}
					final String userName = operationArguments.split(",")[0];
					final String password = operationArguments.split(",")[1];
					final String newPassword = operationArguments.split(",")[2];
					if (eniqUiFlag == true) {
						msg = "Change Password for EniqUI with Username="
								+ userName + " Old Password=" + password
								+ " New Password=" + newPassword;
						assertTrue(eniqMgmt.changePassword(userName, password,
								newPassword));
					} else {
						msg = "Skipping Eniq Operation: Change Password for EniqUI with Username="
								+ userName
								+ " Old Password="
								+ password
								+ " New Password=" + newPassword;
					}
				} else if (operationName
						.equalsIgnoreCase(OPERATION_CREATE_USER)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Create User requires exactly one arguments: username";
						assertTrue(false);
					}
					final User user = testUsers.get(operationArguments);
					if (cleanupFlag) {
						cleanupUserIDList.add(user.getUserID());
					}
					msg = "Create User with " + user.toString();
					assertTrue(userMgmt.createUser(user));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_CREATE_ROLE)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Create Role requires exactly one arguments: rolename";
						assertTrue(false);
					}
					final Role role = testRoles.get(operationArguments);
					if (cleanupFlag) {
						cleanupRoleNameList.add(role.getName());
					}
					msg = "Create Role with " + role.toString();
					assertTrue(roleMgmt.createRole(role));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_CREATE_PERMISSION_GROUP)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Create Permission Group requires exactly one arguments: permission group name";
						assertTrue(false);
					}
					final PermissionGroup permissionGroup = testPermissionGroups
							.get(operationArguments);
					if (cleanupFlag) {
						cleanupPermissionNameList
								.add(permissionGroup.getName());
					}
					msg = "Create Permission Group with "
							+ permissionGroup.toString();
					assertTrue(permissionGroupMgmt
							.createPermissionGroup(permissionGroup));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_DELETE_USER)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Delete User requires exactly one arguments: username";
						assertTrue(false);
					}

					msg = "Delete User with Username=" + operationArguments;
					assertTrue(userMgmt.deleteUser(operationArguments));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_DELETE_ROLE)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Delete Role requires exactly one arguments: role name";
						assertTrue(false);
					}
					msg = "Delete Role with Name=" + operationArguments;
					assertTrue(roleMgmt.deleteRole(operationArguments));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_DELETE_PERMISSION_GROUP)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Delete Permission Group requires exactly one arguments: permission group name";
						assertTrue(false);
					}
					msg = "Delete Permission Group with Name="
							+ operationArguments;
					assertTrue(permissionGroupMgmt
							.deletePermissionGroup(operationArguments));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_UNLOCK_USER)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Unlock User requires exactly one arguments: username";
						assertTrue(false);
					}
					if (eniqUiFlag) {
						msg = "Unlock User with Username=" + operationArguments;
						assertTrue(userMgmt.unlockUser(operationArguments));
					} else {
						msg = "Skipping Eniq Operation: Unlock User with Username="
								+ operationArguments;
					}
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_EMAIL)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Email for User requires exactly two arguments: username, email";
						assertTrue(false);
					}
					final String userName = operationArguments.split(",")[0];
					final String email = operationArguments.split(",")[1];
					msg = "Modify Email for User " + userName + " to " + email;
					assertTrue(userMgmt.modifyEmail(userName, email));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_PHONE)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Phone for User requires exactly two arguments: username, phone";
						assertTrue(false);
					}
					final String userName = operationArguments.split(",")[0];
					final String phone = operationArguments.split(",")[1];
					msg = "Modify Phone for User " + userName + " to " + phone;
					assertTrue(userMgmt.modifyPhone(userName, phone));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_ORGANIZATION)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Organization for User requires exactly two arguments: username, organization";
						assertTrue(false);
					}

					final String userName = operationArguments.split(",")[0];
					final String organization = operationArguments.split(",")[1];
					msg = "Modify Organization for User " + userName + " to "
							+ organization;
					assertTrue(userMgmt.modifyOrganization(userName,
							organization));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_FIRST_NAME)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify First Name for User requires exactly two arguments: username, firstname";
						assertTrue(false);
					}

					final String userName = operationArguments.split(",")[0];
					final String firstName = operationArguments.split(",")[1];
					msg = "Modify First Name for User " + userName + " to "
							+ firstName;
					assertTrue(userMgmt.modifyFirstName(userName, firstName));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_LAST_NAME)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Last Name for User requires exactly two arguments: username, lastname";
						assertTrue(false);
					}

					final String userName = operationArguments.split(",")[0];
					final String lastName = operationArguments.split(",")[1];
					msg = "Modify Last Name for User " + userName + " to "
							+ lastName;
					assertTrue(userMgmt.modifyLastName(userName, lastName));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_ROLES)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))) {
						if (operationArguments.indexOf(",") == -1) {
							msg = "Modify Roles for User requires atleast two arguments: username, roles";
							assertTrue(false);
						}
					}
					final int firstArgIndex = operationArguments.indexOf(",");
					final String userName = operationArguments.substring(0,
							firstArgIndex);
					final String roles = operationArguments
							.substring(firstArgIndex + 1);
					msg = "Modify Roles for User " + userName + " to " + roles;
					assertTrue(userMgmt.modifyRoles(userName, roles));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_USER_PASSWORD)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 3)) {
						msg = "Modify Password for User requires exactly three arguments: username, password and confirm password";
						assertTrue(false);
					}

					final String userName = operationArguments.split(",")[0];
					final String password = operationArguments.split(",")[1];
					final String confirmPassword = operationArguments
							.split(",")[2];
					msg = "Modify Password for User " + userName + " to "
							+ confirmPassword;
					assertTrue(userMgmt.modifyPassword(userName, password,
							confirmPassword));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_ROLE_TITLE)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Title for Role requires exactly two arguments: rolename, title";
						assertTrue(false);
					}
					final String roleName = operationArguments.split(",")[0];
					final String title = operationArguments.split(",")[1];
					msg = "Modify Title for Role " + roleName + " to " + title;
					assertTrue(roleMgmt.modifyTitle(roleName, title));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_ROLE_DESC)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Description for Role requires exactly two arguments: rolename, description";
						assertTrue(false);
					}
					final String roleName = operationArguments.split(",")[0];
					final String desc = operationArguments.split(",")[1];
					msg = "Modify Description for Role " + roleName + " to "
							+ desc;
					assertTrue(roleMgmt.modifyDescription(roleName, desc));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_ROLE_PERMISSION_GROUPS)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))) {
						if (operationArguments.indexOf(",") == -1) {
							msg = "Modify Permission Groups for Role requires atleast two arguments: rolename, permission groups";
							assertTrue(false);
						}
					}
					final int firstArgIndex = operationArguments.indexOf(",");
					final String roleName = operationArguments.substring(0,
							firstArgIndex);
					final String permissionGroups = operationArguments
							.substring(firstArgIndex + 1);
					msg = "Modify Permission Groups for Role " + roleName
							+ " to " + permissionGroups;
					assertTrue(roleMgmt.modifyPermissionGroupSet(roleName,
							permissionGroups));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_PERMISSION_GROUP_TITLE)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Title for Permission Group requires exactly two arguments: permission group name, title";
						assertTrue(false);
					}
					final String permissionGroupName = operationArguments
							.split(",")[0];
					final String title = operationArguments.split(",")[1];
					msg = "Modify Title for Permission Group "
							+ permissionGroupName + " to " + title;
					assertTrue(permissionGroupMgmt.modifyTitle(
							permissionGroupName, title));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_PERMISSION_GROUP_DESC)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 2)) {
						msg = "Modify Description for Permission Group requires exactly two arguments: permission group name, description";
						assertTrue(false);
					}
					final String permissionGroupName = operationArguments
							.split(",")[0];
					final String desc = operationArguments.split(",")[1];
					msg = "Modify Description for Permission Group "
							+ permissionGroupName + " to " + desc;
					assertTrue(permissionGroupMgmt.modifyDescription(
							permissionGroupName, desc));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_MODIFY_PERMISSION_GROUP_PERMISSIONS)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))) {
						if (operationArguments.indexOf(",") == -1) {
							msg = "Modify Permissions for Permission Group requires atleast two arguments: permission group name, permissions";
							assertTrue(false);
						}
					}
					final int firstArgIndex = operationArguments.indexOf(",");
					final String permissionGroupName = operationArguments
							.substring(0, firstArgIndex);
					final String permissions = operationArguments
							.substring(firstArgIndex + 1);
					msg = "Modify Permission for Permission Group "
							+ permissionGroupName + " to " + permissions;
					assertTrue(permissionGroupMgmt.modifyPermissionSet(
							permissionGroupName, permissions));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_CHECK_ENIQ_TABS_FOR_ROLE)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length < 1)) {
						msg = "Verify Tab for Roles requires exactly one argument: rolenames ";
						assertTrue(false);
					}
					if (eniqUiFlag) {
						msg = "Verify Tab Enabled/Disabled in EniqUI for Roles "
								+ operationArguments;
						assertTrue(isTabEnabledForRoles(operationArguments
								.split(",")));
					} else {
						msg = "Skipping Eniq Operation: Verify Tab Enabled/Disabled in EniqUI for Roles "
								+ operationArguments;
					}
				} else if (operationName
						.equalsIgnoreCase(OPERATION_CHECK_ENIQ_TABS_FOR_PERMISSION_GROUP)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length < 1)) {
						msg = "Verify Tab for Permission Group requires exactly one argument: permission group names";
						assertTrue(false);
					}
					if (eniqUiFlag) {
						msg = "Verify Tab Enabled/Disabled in EniqUI for Permission Group "
								+ operationArguments;
						assertTrue(isTabEnabledForPermissionGroups(operationArguments
								.split(",")));
					} else {
						msg = "Skipping Eniq Operation: Verify Tab Enabled/Disabled in EniqUI for Permission Group "
								+ operationArguments;
					}
				} else if (operationName
						.equalsIgnoreCase(OPERATION_VERIFY_USER_DETAILS)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Verify User requires exactly one arguments: username";
						assertTrue(false);
					}
					final User user = testUsers.get(operationArguments);
					msg = "Verify User " + operationArguments;
					assertTrue(userMgmt.verifyUser(user));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_VERIFY_ROLE_DETAILS)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Verify Role requires exactly one arguments: rolename";
						assertTrue(false);
					}
					final Role role = testRoles.get(operationArguments);
					msg = "Verify Role " + operationArguments;
					assertTrue(roleMgmt.verifyRole(role));
				} else if (operationName
						.equalsIgnoreCase(OPERATION_VERIFY_PERMISSION_GROUP_DETAILS)) {
					if ((operationArguments == null)
							|| (operationArguments.equals(""))
							|| (operationArguments.split(",").length != 1)) {
						msg = "Verify Permission Group requires exactly one arguments: permission group name";
						assertTrue(false);
					}
					final PermissionGroup permissionGroup = testPermissionGroups
							.get(operationArguments);
					msg = "Verify Permission Group " + operationArguments;
					assertTrue(permissionGroupMgmt
							.verifyPermissionGroup(permissionGroup));
				} else {
					if (!(operationName.equalsIgnoreCase(TOKEN_TC_ID)
							|| operationName.equalsIgnoreCase(TOKEN_TC_GROUP) || operationName
								.equalsIgnoreCase(TOKEN_TC_NAME))) {
						aacLogger.warning("Operation " + operationName
								+ " with arguments " + operationArguments
								+ " is unknown to AAC Automation");
					}
					continue;
				}

				aacLogger.info(msg);

			} catch (final AssertionFailedError exception) {
				failureReason = "Operation fails " + msg;
				aacLogger.severe(failureReason);
				if (cleanupFlag) {
					super.cleanUp();
				}
				return false;
			} catch (final SeleniumException se) {
				aacLogger.severe("Timeout/Fail in  Operation " + msg);
				aacLogger.severe("Cause" + se.getMessage());
				if (cleanupFlag) {
					super.cleanUp();
				}
				return false;

			}
		}
		if (cleanupFlag) {
			super.cleanUp();
		}
		return true;
	}

	private String getPropertyFile() {
		if (VM_TEST_BLADE) {
			return resourcePathOnBlade + File.separator + aacPropertiesFile;
		}
		return resourcePathInVobs + File.separator + aacPropertiesFile;
	}

	public PermissionGroup getPermissionGroup(
			final Element permissionGroupElements) {
		final PermissionGroup permissionGroup = new PermissionGroup();
		NodeList permissionGroupChild = permissionGroupElements
				.getElementsByTagName("NAME");
		permissionGroup.setName(permissionGroupChild.item(0).getTextContent());

		permissionGroupChild = permissionGroupElements
				.getElementsByTagName("TITLE");
		permissionGroup.setTitle(permissionGroupChild.item(0).getTextContent());

		permissionGroupChild = permissionGroupElements
				.getElementsByTagName("DESCRIPTION");
		permissionGroup.setDescription(permissionGroupChild.item(0)
				.getTextContent());

		permissionGroupChild = permissionGroupElements
				.getElementsByTagName("PERMISSIONS");
		permissionGroup.setPermissions(permissionGroupChild.item(0)
				.getTextContent());
		return permissionGroup;
	}

	public Role getRole(final Element roleElements) {
		final Role role = new Role();
		NodeList roleChild = roleElements.getElementsByTagName("NAME");
		role.setName(roleChild.item(0).getTextContent());

		roleChild = roleElements.getElementsByTagName("TITLE");
		role.setTitle(roleChild.item(0).getTextContent());

		roleChild = roleElements.getElementsByTagName("DESCRIPTION");
		role.setDescription(roleChild.item(0).getTextContent());

		roleChild = roleElements.getElementsByTagName("PERMISSION_GROUPS");
		role.setPermissionGroups(roleChild.item(0).getTextContent());
		return role;
	}

	public User getUser(final Element userElements) {
		final User user = new User();
		NodeList userChild = userElements.getElementsByTagName("UID");
		user.setUserID(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("PASSWORD");
		user.setPassword(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("CONFIRM_PASSWORD");
		user.setConfirmPassword(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("FIRST_NAME");
		user.setFirstName(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("LAST_NAME");
		user.setLastName(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("EMAIL");
		user.setEmail(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("PHONE");
		user.setPhone(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("ORGANIZATION");
		user.setOrganization(userChild.item(0).getTextContent());

		userChild = userElements.getElementsByTagName("ROLES");
		user.setRoles(userChild.item(0).getTextContent());

		return user;
	}

	public static void main(final String[] args) throws Exception {
		final AACTestReader xmlFileReader = new AACTestReader();
		xmlFileReader.executeTestSuite();

	}

	// private static Logger logger =
	// Logger.getLogger(SeleniumLogger.class.getName());
	private static final String TOKEN_AAC = "AAC";

	private static final String ATTRIBUTE_CLEANUP = "cleanup";

	private static final String ATTRIBUTE_LOGGING = "logging";

	private static final String ATTRIBUTE_TC_GROUP = "tc_group";

	private static final String ATTRIBUTE_TC_ID = "tc_id";

	private static final String ATTRIBUTE_NEGATIVE = "negative";

	private static final String ATTRIBUTE_UPLOAD = "upload";

	private static final String ATTRIBUTE_ENIQ_UI = "eniq_ui";

	private static final String TOKEN_TEST_CASE = "TEST_CASE";

	private static final String TOKEN_USER = "USER";

	private static final String TOKEN_ROLE = "ROLE";

	private static final String TOKEN_PERMISSION_GROUP = "PERMISSION_GROUP";

	private static final String TOKEN_TC_NAME = "TC_NAME";

	private static final String TOKEN_TC_GROUP = "TC_GROUP";

	private static final String TOKEN_TC_ID = "TC_ID";

	private static final String OPERATION_LOGIN_ADMINUI = "LOGIN_ADMINUI";

	private static final String OPERATION_LOGIN_ADMINUI_AS_ADMIN = "LOGIN_ADMINUI_AS_ADMIN";

	private static final String OPERATION_LOGOUT_ADMINUI = "LOGOUT_ADMINUI";

	private static final String OPERATION_LOGIN_ENIQUI = "LOGIN_ENIQUI";

	private static final String OPERATION_LOGIN_ENIQUI_AS_ADMIN = "LOGIN_ENIQUI_AS_ADMIN";

	private static final String OPERATION_LOGOUT_ENIQUI = "LOGOUT_ENIQUI";

	private static final String OPERATION_CHANGE_PASSWORD_ENIQ = "CHANGE_PASSWORD_ENIQ";

	private static final String OPERATION_CREATE_USER = "CREATE_USER";

	private static final String OPERATION_CREATE_ROLE = "CREATE_ROLE";

	private static final String OPERATION_CREATE_PERMISSION_GROUP = "CREATE_PERMISSION_GROUP";

	private static final String OPERATION_DELETE_USER = "DELETE_USER";

	private static final String OPERATION_DELETE_ROLE = "DELETE_ROLE";

	private static final String OPERATION_DELETE_PERMISSION_GROUP = "DELETE_PERMISSION_GROUP";

	private static final String OPERATION_UNLOCK_USER = "UNLOCK_USER";

	private static final String OPERATION_MODIFY_USER_PASSWORD = "MODIFY_USER_PASSWORD";

	private static final String OPERATION_MODIFY_USER_FIRST_NAME = "MODIFY_USER_FIRST_NAME";

	private static final String OPERATION_MODIFY_USER_LAST_NAME = "MODIFY_USER_LAST_NAME";

	private static final String OPERATION_MODIFY_USER_EMAIL = "MODIFY_USER_EMAIL";

	private static final String OPERATION_MODIFY_USER_PHONE = "MODIFY_USER_PHONE";

	private static final String OPERATION_MODIFY_USER_ORGANIZATION = "MODIFY_USER_ORGANIZATION";

	private static final String OPERATION_MODIFY_USER_ROLES = "MODIFY_USER_ROLES";

	private static final String OPERATION_MODIFY_ROLE_TITLE = "MODIFY_ROLE_TITLE";

	private static final String OPERATION_MODIFY_ROLE_DESC = "MODIFY_ROLE_DESC";

	private static final String OPERATION_MODIFY_ROLE_PERMISSION_GROUPS = "MODIFY_ROLE_PERMISSION_GROUPS";

	private static final String OPERATION_MODIFY_PERMISSION_GROUP_TITLE = "MODIFY_PERMISSION_GROUP_TITLE";

	private static final String OPERATION_MODIFY_PERMISSION_GROUP_DESC = "MODIFY_PERMISSION_GROUP_DESC";

	private static final String OPERATION_MODIFY_PERMISSION_GROUP_PERMISSIONS = "MODIFY_PERMISSION_GROUP_PERMISSIONS";

	private static final String OPERATION_CHECK_ENIQ_TABS_FOR_ROLE = "CHECK_ENIQ_TABS_FOR_ROLE";

	private static final String OPERATION_CHECK_ENIQ_TABS_FOR_PERMISSION_GROUP = "CHECK_ENIQ_TABS_FOR_PERMISSION_GROUP";

	private static final String OPERATION_VERIFY_USER_DETAILS = "VERIFY_USER";

	private static final String OPERATION_VERIFY_ROLE_DETAILS = "VERIFY_ROLE";

	private static final String OPERATION_VERIFY_PERMISSION_GROUP_DETAILS = "VERIFY_PERMISSION_GROUP";

	private static Document doc = null;

	private final Hashtable<String, User> testUsers = new Hashtable<String, User>();

	private final Hashtable<String, Role> testRoles = new Hashtable<String, Role>();

	private final Hashtable<String, PermissionGroup> testPermissionGroups = new Hashtable<String, PermissionGroup>();

	protected static final Logger aacLogger = Logger
			.getLogger(AACSeleniumLogger.class.getName());

	private static boolean isLogFileUploadEnable = false;

	private static boolean isReportFileUploadEnable = false;
}
