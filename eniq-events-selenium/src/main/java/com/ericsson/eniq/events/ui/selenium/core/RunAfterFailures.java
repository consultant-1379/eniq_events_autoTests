/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core;

import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ericker
 * @since 2010
 *
 */
@Deprecated
public class RunAfterFailures extends Statement {

    private final Statement fNext;

    private final Object fTarget;

    private final List<FrameworkMethod> fAfterFailures;

    public RunAfterFailures(final Statement next, final List<FrameworkMethod> afterFailures, final Object target) {
        fNext = next;
        fAfterFailures = afterFailures;
        fTarget = target;
    }

    @Override
    public void evaluate() throws Throwable {

        final List<Throwable> fErrors = new ArrayList<Throwable>();
        fErrors.clear();
        try {
            fNext.evaluate();
        } catch (final Throwable e) {
            fErrors.add(e);
            for (final FrameworkMethod each : fAfterFailures) {
                try {
                    each.invokeExplosively(fTarget, e);
                } catch (final Throwable e2) {
                    fErrors.add(e2);
                }
            }
        }
        if (fErrors.isEmpty()) {
            return;
        }
        if (fErrors.size() == 1) {
            throw fErrors.get(0);
        }
        throw new MultipleFailureException(fErrors);
    }
}
