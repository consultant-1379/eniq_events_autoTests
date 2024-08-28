
package com.ericsson.star.regressiontest.getters.ui;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ericsson.cifwk.taf.UiGetter;
/**
 *
 *	UI Context Getter for executing Test Cases for UITool
 */
public class UIToolUiGetter implements UiGetter{

                  // Dummy Test Code Generated to show inter-workings of TAF Classes 
                  private static List<String> printedStrings;
                  Logger logger = Logger.getLogger(this.getClass().getName());

                  public List<String> getPrintedStrings(){
                      if (printedStrings == null)
                          printedStrings = new ArrayList<String>();
                      return printedStrings;
                  };
                  public void print(String stringToPrint){
                      logger.info(stringToPrint);
                      getPrintedStrings().add(stringToPrint);        
                  };

                  public UIToolUiGetter getTestedService(){
                      return this;
                  };
}	
