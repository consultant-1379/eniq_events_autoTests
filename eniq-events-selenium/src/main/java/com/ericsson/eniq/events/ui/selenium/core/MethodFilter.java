/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import java.util.Collection;

/**
 * @author eseuhon
 * @since 2011
 *
 */
class MethodFilter extends Filter {

    /**
     * stored methods that have to be filtered later
     */
    private final Collection<String> methods;

    /**
     * constructor. submit the methods that have to be run later.
     * 
     * @param methods
     *            collection of full qualified methods separated by dots with
     *            their class. (e.g.
     *            <code>mycompany.myapp.MyClass.myMethod</code>)
     * @throws IllegalArgumentException
     *             if the collection submitted is null
     */
    public MethodFilter(final Collection<String> methods) {
        if (methods == null) {
            throw new IllegalArgumentException("methods must not be null");
        }
        this.methods = methods;
    }

    @Override
    public String describe() {
        return "Filters tests by full qualified method names.";
    }

    /**
     * Determines based on the stored methods whether or not the provided
     * description has to be run.
     */
    @Override
    public boolean shouldRun(final Description classDescription) {
        if (classDescription.getChildren().size() != 0) {
            for (final Description methodDescription : classDescription.getChildren()) {
                final String methodName = methodDescription.getMethodName();
                final String className = methodDescription.getClassName();
                if (this.methods.contains(String.format("%s.%s", className, methodName))) {
                    return true;
                }
            }
            return false;
        }
        final String methodName = classDescription.getMethodName();
        final String className = classDescription.getClassName();
        return methods.contains(String.format("%s.%s", className, methodName));
    }
}