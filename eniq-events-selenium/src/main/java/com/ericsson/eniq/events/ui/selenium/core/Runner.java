/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

/**
 * @author egercor
 * @since 2011
 * 
 */

public final class Runner extends Filter {
	private String[] testCaseNames;
	private String[] testSubSuiteNames;
	private String testSuiteName;

	public Runner() {
		super();
		if (caseSet()) {
			testCaseNames = PropertyReader.getInstance().getTestCaseNames();
		}
		if (suiteSet()) {
			testSuiteName = PropertyReader.getInstance().getTestSuiteName();
		}
		if (subSuiteSet()) {
			testSubSuiteNames = PropertyReader.getInstance()
					.getTestSubSuiteNames();
		}
	}

	@Override
	public boolean shouldRun(Description d) {
		String className = d.getTestClass().getName();
		String[] classNameArray = className.split("\\.");
		String displayName = d.getDisplayName();
		String testNameLong = displayName
				.substring(0, displayName.indexOf('('));
		String pattern = ".*?(_)+?(\\d)(.*)(\\().*";
		String testNameShort = displayName.replaceAll(pattern, "$2$3");
		if (testSuiteName == null)
			return true;

		// didn't specify test names: run all the subSuites. If no subSuites
		// specified run all the main Suite
		if (testCaseNames == null) {
			if (testSubSuiteNames == null) {
				if (testSuiteName
						.equals(classNameArray[classNameArray.length - 2])) {
					return true;
				}
			} else {
				for (int i = 0; i < testSubSuiteNames.length; i++) {
					if (testSubSuiteNames[i]
							.equals(classNameArray[classNameArray.length - 1])
							&& testSuiteName
									.equals(classNameArray[classNameArray.length - 2])) {
						return true;
					}
				}
			}

		}
		// is the test in the selected sub suites
		if (testSubSuiteNames != null) {
			for (int i = 0; i < testSubSuiteNames.length; i++)
				if (testSubSuiteNames[i]
						.equals(classNameArray[classNameArray.length - 1])
						&& testSuiteName
								.equals(classNameArray[classNameArray.length - 2])) {
					System.out.println("TRUE??");
					return true;
				}
		}
		// is the test one of the test cases
		for (int i = 0; i < testCaseNames.length; i++) {
			if (testNameLong.equals(testCaseNames[i])
					&& testSuiteName
							.equals(classNameArray[classNameArray.length - 2]))
				return true;
			else if (testNameShort.equals(testCaseNames[i])
					&& testSuiteName
							.equals(classNameArray[classNameArray.length - 2]))
				return true;
		}
		return false;
	}

	@Override
	public String describe() {
		return "Only run specified tests";
	}

	public static boolean caseSet() {
		if (PropertyReader.getInstance().getTestCaseNames() == null) {
			return false;
		}
		return true;

	}

	public static boolean suiteSet() {
		if (PropertyReader.getInstance().getTestSuiteName() == null) {
			return false;
		}
		return true;

	}

	public static boolean subSuiteSet() {
		if (PropertyReader.getInstance().getTestSubSuiteNames() == null) {
			return false;
		}
		return true;

	}

}
