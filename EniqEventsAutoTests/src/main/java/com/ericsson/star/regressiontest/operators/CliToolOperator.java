
package com.ericsson.star.regressiontest.operators;

import com.ericsson.cifwk.taf.GenericOperator; 
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.star.regressiontest.operators.cli.CliToolCliOperator;
import java.util.Map;

/**
*
*Operator for executing Test Cases for CliTool
*/
public class CliToolOperator implements GenericOperator{
		 
	CliToolCliOperator clitoolCliOperator = new CliToolCliOperator();			
	
	/**
	* Calls CLI Contextual operator for: svcs1
	* @param Map<String, Object> containing arguments for command
	* @return 
	*/
	public boolean runCommand (Map<String,Object> args){ 
		return clitoolCliOperator.runCommand(args);
	}
	
	public boolean enableHttps (Map<String,Object> args){ 
		return clitoolCliOperator.enableHttps(args);
	}
	
	public boolean disableHttps (Map<String,Object> args){ 
		return clitoolCliOperator.disableHttps(args);
	}
	
	public boolean runWget (Map<String,Object> args){ 
		return clitoolCliOperator.runWget(args);
	}
	
	public boolean checkWgetResult (Map<String,Object> args){ 
		return clitoolCliOperator.checkWgetResult(args);
	}
	

	/**
	* Calls CLI Contextual operator for: svcs1
	* @param Map<String, Object> containing arguments for command
	* @return 
	*/
	public boolean loadTopology (Map<String,Object> args){ 
		return clitoolCliOperator.loadTopology(args);
	}
	
	public boolean checkTopologyTables(Map<String,Object> args){
		return clitoolCliOperator.checkTopologyTables(args);
	}
	
	public int checkInterface(Map<String,Object> args){
		return clitoolCliOperator.checkInterface(args);
	}
	
	public boolean moveTopologyToFolder(Map<String,Object> args){
		return clitoolCliOperator.moveTopologyToFolder(args);
	}
	
	public boolean activateEngineSet(Map<String,Object> args){
		return clitoolCliOperator.activateEngineSet(args);
	}
	
	public boolean loadHomeNetworkGroup(Map<String,Object> args){
		return clitoolCliOperator.loadHomeNetworkGroup(args);
	}
		           
	/**
	* Calls CLI Contextual operator for: https_status
	* @param Map<String, Object> containing arguments for command
	* @return 
	*/
	public boolean https_statusCommand (Map<String,Object> args){ 
		return clitoolCliOperator.https_statusCommand(args);
	}
	
	public boolean prepareServerForLTEES(){
		return clitoolCliOperator.prepareServerForLTEES();
	}
		  	
	/**
	* Returns the ExitCode for the last command in the session
	* @return 
	*/		
	public int getExitCode(){
		return clitoolCliOperator.getExitCode();
	}
	
	/**
	* Returns the StdOut content for the last command in the session
	* @return 
	*/		
	public String getStdOut(){
		return clitoolCliOperator.getStdOut();
	}

	/**
	* Returns the StdErr content for the last command in the session
	* @return 
	*/		
	public String getStdErr(){
		return clitoolCliOperator.getStdErr();
	}
		
	/**
	* Disconnect from the session
	* @return 
	*/
	public boolean closeSession(){
		return clitoolCliOperator.disconnect();
	}

	public void registerHostOnDatagen(Host host) {
		clitoolCliOperator.registerHostOnDatagen(host);
	}

	public boolean checkDataLoading(String mediation_host, String[] dirsToCheck,
			String[] tablesToCheck, int timeWrap, boolean doWait, String[] column, String[] columnValue) {
		return clitoolCliOperator.checkDataLoading(mediation_host, dirsToCheck,tablesToCheck,
				timeWrap,doWait,column,columnValue);
	}

	public boolean disableMatchingWorkflows(String pattern) {
		return clitoolCliOperator.disableMatchingWorkflows(pattern);
	}

	public boolean provisionSgehWorkflows() {
		return clitoolCliOperator.provisionSgehWorkflows();
	}
	
	public boolean provisionMssWorkflows() {
		return clitoolCliOperator.provisionMssWorkflows();
	}

	public boolean disableUnRequiredECs(String[] unwantedEC) {
		return clitoolCliOperator.disableUnRequiredECs(unwantedEC);
	}

	public boolean enableRequiredECs(String[] wantedEC) {
		return clitoolCliOperator.enableRequiredECs(wantedEC);
	}

	public boolean refreshSgehTopology() {
		return clitoolCliOperator.refreshSgehTopology();
	}

	public boolean enable2g3gWorkflows() {
		return clitoolCliOperator.enable2g3gWorkflows();
	}

	public boolean enableKpiNotificationWorkflows() {
		return clitoolCliOperator.enableKpiNotificationWorkflows();
	}
	
	public boolean create4GAndMSSGroup(boolean b) {
		
		return clitoolCliOperator.create4GAndMSSGroup(b);
	}

	public boolean refreshLteefaTopology() {
		return clitoolCliOperator.refreshLteefaTopology();
	}

	public boolean disableAllWorkflows(String[] exceptions) {
		return clitoolCliOperator.disableAllWorkflows(exceptions);
	}

	public boolean provisionLteefaWorkflows() {
		return clitoolCliOperator.provisioLteefaWorkflows();
	}

	public boolean updateReservedDataTimeRanges() {
		return clitoolCliOperator.updateReservedDataTimeRanges();
	}

	public boolean startDataLoadingOnCEP() {
		return clitoolCliOperator.startDataLoadingOnCEP();
	}

	public boolean getCepServiceStatus() {
		return clitoolCliOperator.getCepServiceStatus();
	}

	public boolean createDatagenLinks(String hostname, String[] localdir,
			String[] remotedir) {
		return clitoolCliOperator.createDatagenLinks(hostname,localdir,remotedir);
	}

	public boolean editCEPConfigFiles() {
		return clitoolCliOperator.editCEPConfigFiles();
	}

	public boolean checkCepBlade() {
		return clitoolCliOperator.checkCepBlade();
	}
	
	public boolean provisionKPINotificationWorkflows(boolean remoteDG, boolean force, Map<String,Object> args){
		return clitoolCliOperator.provisionKPINotificationWorkflows(remoteDG, force, args);
	}
	
	public boolean loadKPINotificationData(Map<String,Object> args){
		return clitoolCliOperator.loadKPINotificationData(args);
	}
	
	public boolean loadMSSData(boolean remoteDG, boolean force, Map<String,Object> args){
		return clitoolCliOperator.loadMSSData(remoteDG, force, args);
	}

	public boolean updateMssPreprocessingWorkflows(String[] dirs) {
		return clitoolCliOperator.updateMssPreprocessingWorkflows(dirs);
	}

	public boolean restartWorkflows(String[] wfToRestart,
			String[] wfGroupsToRestart) {
		return clitoolCliOperator.restartWorkflows(wfToRestart,wfGroupsToRestart);
	}

	public boolean refreshMssTopology() {
		return clitoolCliOperator.refreshMssTopology();
	}

	public boolean runSelenium(Map<String, Object> args) {
		return clitoolCliOperator.runSelenium(args);
	}

	public boolean areKpiAnalysisMapsLoaded(Map<String, Object> args) {
		return clitoolCliOperator.areKpiAnalysisMapsLoaded(args);
	}

	public boolean updateSessionBrowserImsis(Map<String, Object> args) {
		return clitoolCliOperator.updateSessionBrowserImsis(args);
	}

	public boolean parseSeleniumLog(Map<String, Object> args) {
		return clitoolCliOperator.parseSeleniumLog(args);
	}

	public boolean create2g3gGroups(Map<String, Object> args) {
		return clitoolCliOperator.create2g3gGroups(args);
	}

	public boolean createKpiPhaseOneGroups(Map<String, Object> args) {
		return clitoolCliOperator.createKpiPhaseOneGroups(args);
	}

	public boolean createLteGroups(Map<String, Object> args) {
		return clitoolCliOperator.createLteGroups(args);
	}
}