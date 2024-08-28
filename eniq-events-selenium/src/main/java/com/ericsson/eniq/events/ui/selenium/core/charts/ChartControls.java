package com.ericsson.eniq.events.ui.selenium.core.charts;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

public interface ChartControls {

    public void drillOnBarChartObject(final int barChartObjectNumber) throws PopUpException;

    public void chartRefresh() throws InterruptedException;

    public void drillOnPieChartObjectByCountry(int pieChartObjectNumber) throws PopUpException;

    void drillOnPieChartObjectByOperator(int pieChartObjectNumber) throws PopUpException;

    void drilldownOnSinglePieChart() throws PopUpException;

    void openChartView() throws PopUpException;

    void drilldownOnHeaderPortion(String piePortion) throws PopUpException;

    int getIndex(final String pieTitle, final String xPath);

    void drilldownOnBarChartPortion(final String barPortionName) throws PopUpException;

}
