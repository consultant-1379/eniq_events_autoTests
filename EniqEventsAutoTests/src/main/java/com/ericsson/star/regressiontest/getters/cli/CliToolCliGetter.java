
package com.ericsson.star.regressiontest.getters.cli;
import com.ericsson.cifwk.taf.CliGetter;
import com.ericsson.cifwk.taf.data.DataHandler;

/**
 *
 *	CLI Context Getter for executing Test Cases for CliTool
 */
public class CliToolCliGetter implements CliGetter{

	public static String getCommand(String command){
		return (String) DataHandler.getAttribute(command);
	};
};
