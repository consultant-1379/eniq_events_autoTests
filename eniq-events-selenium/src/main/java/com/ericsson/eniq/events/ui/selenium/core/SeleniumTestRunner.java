/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.tests.AllTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.fourg.FourGTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt.TwoGThreeGDvdtTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.TwoGThreeGSgehTestSuite;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class SeleniumTestRunner extends JUnitCore {

    static Map<String, Class<?>> testGroupToTestSuiteMap = new HashMap<String, Class<?>>();

    static {
        testGroupToTestSuiteMap.put("4GTestGroup", FourGTestSuite.class);
        testGroupToTestSuiteMap.put("2G3GSgehTestGroup", TwoGThreeGSgehTestSuite.class);
        testGroupToTestSuiteMap.put("2G3GDvdtTestGroup", TwoGThreeGDvdtTestSuite.class);
        testGroupToTestSuiteMap.put("All", AllTestSuite.class);
    }

    public Result runTests() {
        final String testGroup = PropertyReader.getInstance().getTestGroup();

        if (System.getProperty("test.methods") != null) {
            final String[] methodsToRun = System.getProperty("test.methods").split(",");
            final Method[] methods = AllTestSuite.class.getDeclaredMethods();
            final Set<Class<?>> classes = new HashSet<Class<?>>();

            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(methodsToRun[i])) {
                    final Class<?> klass = methods[i].getDeclaringClass();
                    classes.add(klass);
                }
            }
            final Request request = Request.classes(classes.toArray(new Class<?>[0]));
            //        request = request.filterWith(new MethodFilter());
            return run(request);

        }

        final Set<Class<?>> testSuite = new HashSet<Class<?>>();
        testSuite.add(testGroupToTestSuiteMap.get(testGroup));
        return run(testSuite.toArray(new Class<?>[0]));
    }

    /**
     * run certain methods defined in a collection with the following pattern:
     * <code>mycompany.myapp.MyClass.myMethod</code>
     * 
     * @param fullQualifiedMethods
     *            Collection of methods to be run
     * @throws ClassNotFoundException
     *             if one of the classes doesn't exist
     * @throws NoSuchMethodException
     *             if one of the methods doesn't exist
     * @throws SecurityException
     *             see Class.getMethod for reasons on this one
     */
    public Result runMethods(final Collection<String> fullQualifiedMethods) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException {
        final Class<?>[] classes = getClasses(fullQualifiedMethods);
        Request request = Request.classes(classes);
        request = request.filterWith(new MethodFilter(fullQualifiedMethods));
        return run(request);
    }

    /**
     * convert all classes of the provided methods to an array of classes
     * 
     * @param fullQualifiedMethods
     *            collection of full qualified method names (e.g.
     *            <code>mycompany.myapp.MyClass.myMethod</code>)
     * @return an array of the classes provided in the collection
     * @throws ClassNotFoundException
     *             if one of the classes does not exist
     * @throws NoSuchMethodException
     *             if one of the methods doesn't exist
     * @throws SecurityException
     *             see Class.getMethod for reasons on this one
     */
    private static Class<?>[] getClasses(final Collection<String> fullQualifiedMethods) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException {
        final Set<Class<?>> classes = new HashSet<Class<?>>();

        for (final String method : fullQualifiedMethods) {
            final String className = method.substring(0, method.lastIndexOf('.'));
            final String methodName = method.substring(method.lastIndexOf('.') + 1, method.length());
            final Class<?> klass = Class.forName(className);
            classes.add(klass);
            // check if method exists. throws NoSuchMethodException otherwise
            klass.getMethod(methodName, new Class[0]);
        }
        return classes.toArray(new Class<?>[0]);
    }

}
