/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core;

import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author ericker
 * @since 2010
 * 
 */
@Deprecated
public class EricssonSeleniumTestRunner extends SpringJUnit4ClassRunner {

	/**
	 * @param className
	 * @throws InitializationError
	 */
	public EricssonSeleniumTestRunner(final Class<?> className)
			throws InitializationError {
		super(className);
		Filter f = new Runner();
		try {
			f.apply(this);
		} catch (NoTestsRemainException ex) {
			throw new RuntimeException(ex);
		}
	}

	/*
	 * Override withAfters() so we can append to the statement which will invoke
	 * the test method. We don't override methodBlock() because we wont be able
	 * to reference the target object.
	 */
	@Override
	protected Statement withAfters(final FrameworkMethod method,
			final Object target, Statement statement) {
		statement = withAfterFailures(method, target, statement);
		statement = super.withAfters(method, target, statement);
		return statement;
	}

	protected Statement withAfterFailures(final FrameworkMethod method,
			final Object target, final Statement statement) {
		final List<FrameworkMethod> failures = getTestClass()
				.getAnnotatedMethods(AfterFailure.class);
		return new RunAfterFailures(statement, failures, target);
	}
}
