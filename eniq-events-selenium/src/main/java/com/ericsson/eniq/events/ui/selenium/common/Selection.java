/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.common;


import org.springframework.stereotype.Component;

@Component
public class Selection {
    private String timeRange;
    private String dimension;
    private String dimensionValue;
    private String windowCategory;
    private String windowOption;
    private String terminalModel;
    private String tac;
    private boolean isGroup;
    private boolean isTerminalType;
    private boolean isNetworkType;

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public void setDimensionValue(String dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public void setWindowCategory(String windowCategory) {
        this.windowCategory = windowCategory;
    }

    public void setWindowOption(String windowOption) {
        this.windowOption = windowOption;
    }

    public void setTerminalModel(String terminalModel) {
        this.terminalModel = terminalModel;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public void setIsTerminal(boolean isTerminalType) {
        this.isTerminalType = isTerminalType;
    }

    public void setIsNetwork(boolean isNetworkType) {
        this.isNetworkType = isNetworkType;
    }

    public String getTimeRange() {
        return timeRange;
    }
    public String getDimension() {
        return dimension;
    }
    public String getDimensionValue() {
        return dimensionValue;
    }
    
    public String getWindowCategory() {
        return windowCategory;
    }
    public String getWindowOption() {
        return windowOption;
    }

    public String getTerminalModel() {
        return terminalModel;
    }

    public String getTac() {
        return tac;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public boolean getIsTerminalType() {
        return isTerminalType;
    }

    public boolean getIsNetworkType() {
        return isNetworkType;
    }

    public void distroy() {
        timeRange = null;
        dimension = null;
        dimensionValue = null;
        windowCategory = null;
        windowOption = null;
    }

    public String toString() {
        StringBuffer objectValue = new StringBuffer();
        objectValue.append("\n  Time Range : " + timeRange);
        objectValue.append("\n  Dimension   	: " + dimension);
        objectValue.append("\n  Dimension value : " + dimensionValue);
        objectValue.append("\n  Window Category : " + windowCategory);
        objectValue.append("\n  Window Option   : " + windowOption);
        return objectValue.toString();
    }
}
