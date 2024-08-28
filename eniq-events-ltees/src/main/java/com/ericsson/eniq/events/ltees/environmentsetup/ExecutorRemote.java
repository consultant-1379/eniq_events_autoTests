/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.eniq.events.ltees.environmentsetup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.ericsson.eniq.events.ltees.applicationdrivers.AppDriver;
import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.LogFileWriter;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * Class is for executing Command on Remote system and capture the results.
 * 
 */
public final class ExecutorRemote {

    private ExecutorRemote() {
    }

    /**
     * This command is support for executing any remote commands from GUI.
     * 
     * @param user
     *            of the remote host
     * @param password
     *            of the remote host
     * @param host
     *            Remote host address
     * @param cmd
     *            command that is needed to run
     * @return returns the output after the execution.
     * @throws JSchException
     * @throws IOException
     * @throws Exception
     */
    public static String executeComand(final String host, final String user, final String password, final String cmd) throws JSchException,
            IOException {

        final StringBuilder result = new StringBuilder();

        final Session session = getSession(host, user, password);

        session.connect();

        final Channel channel = session.openChannel("exec");

        ((ChannelExec) channel).setCommand(cmd);
        channel.setXForwarding(true);

        final InputStream in = channel.getInputStream();
        channel.connect();
        channel.setInputStream(System.in);
        final byte[] tmp = new byte[1];
        final char[] tmpC = new char[1];
        int count = 0;
        final Charset charset = Charset.forName("US-ASCII");
        final CharsetDecoder decoder = charset.newDecoder();

        while (true) {

            while (in.available() > 0) {
                final int i = in.read(tmp, 0, 1);
                if (i < 0) {
                    break;
                }
                CharBuffer cb = CharBuffer.wrap(tmpC);
                decoder.decode(ByteBuffer.wrap(tmp), cb, false);
                result.append(String.valueOf(cb.array()));
                cb = null;
                count += 1;
            }
            if (channel.isClosed()) {
                in.close();
                break;
            }
        }

        channel.disconnect();
        session.disconnect();

        return result.toString();
    }

    public static void executeConfigureLteesScript(final String host, final String user, final String password, final String cmd,
                                                   final String enableDisable) throws JSchException, IOException {
        ExecuteInteractive lteesConfigurationScript = new ExecuteInteractive(host, user, password);

        String result = lteesConfigurationScript.execute(cmd);
        if (AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.FOURTEEN_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.FOURTEEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.FIFTEEN_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.SIXTEEN_A_PMS || AppDriver.executionContext == AppDriver.ExecutionContext.SIXTEEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.SEVENTEEN_A_PMS) {
            result = lteesConfigurationScript.execute("1");
        } else if (AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_CTRS_ROP_FIVE) {
            result = lteesConfigurationScript.execute("2");
        } else if (AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_CTRS_ROP_FIFTEEN
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_FIFTEEN
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_CTRS_ROP_FIFTEEN
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_CTRS_ROP_FIFTEEN) {
            result = lteesConfigurationScript.execute("3");
        } else if (AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_CTRS_ROP_ONE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_ONE) {
            System.out.println("ERROR 1 MIN ROP Functionality is presently not implemented");
            System.exit(0);
        } else {
            System.out.println("Control should not have come here, something wrong");
            System.exit(0);
        }
        if (result.contains("Do you wish to continue [y/n]")) {
            lteesConfigurationScript.execute("y");
        }
        if (result.contains("1) ossrc1")) {
            lteesConfigurationScript.execute("1-4", 80000);
        } else {
            lteesConfigurationScript.execute("-ossrc1 " + enableDisable + " -ossrc2 " + enableDisable + " -ossrc3 " + enableDisable + " -ossrc4 "
                    + enableDisable, 80000);
        }
    }

    public static String executeConfigureLteesScriptStatus(final String host, final String user, final String password, final String cmd)
            throws JSchException, IOException {
        boolean topoCachePrepared = false;
        int waitCycle = 0;
        String result = "";
        ExecuteInteractive lteesConfigurationScript = new ExecuteInteractive(host, user, password);

        while (!topoCachePrepared) {
            result = lteesConfigurationScript.execute(cmd);
            String[] tempResult = result.toUpperCase().split("COMPLETED");

            if (tempResult.length < 4) {
                try {
                    Thread.sleep(420000);
                } catch (final InterruptedException e) {
                    System.out.println("Control should not have come here, something wrong");
                }
                waitCycle++;
                if (waitCycle >= 9) {
                    result = "NOK. Topology Cache generation has taken longer than an HOUR" + result;
                    break;
                }
            } else {
                topoCachePrepared = true;
                result = "OK. " + result;
            }
        }
        return result;
    }

    public static String executeLteesTopoCacheStatus(final String host, final String user, final String password, final String cmd)
            throws JSchException, IOException {
        boolean topoCachePrepared = false;
        int waitCycle = 0;
        String result = "";
        ExecuteInteractive lteesConfigurationScript = new ExecuteInteractive(host, user, password);

        result = lteesConfigurationScript.execute(cmd);

        return result;
    }

    public static String getstuff(InputStream in, byte[] buffer, ByteArrayOutputStream bos, Channel channel, long waitTime) {

        try {
            final long endTime = System.currentTimeMillis() + waitTime;
            while (System.currentTimeMillis() < endTime) {
                while (in.available() > 0) {
                    int count = in.read(buffer, 0, 1024);
                    if (count >= 0) {
                        bos.write(buffer, 0, count);
                    } else {
                        break;
                    }
                }
                if (channel.isClosed()) {

                    break;
                }
                try {

                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String stuff = bos.toString();
        bos.reset();
        return stuff;
    }

    /**
     * This command is support for executing mediation Zone workflow imports.
     * 
     * @param password
     *            of the remote host
     * @param host
     *            Remote host address
     * @param cmd
     *            command that is needed to run
     * @param sleepTime
     *            TODO
     * @param user
     *            of the remote host
     * 
     * @return returns the output after the execution.
     * @throws JSchException
     * @throws IOException
     * @throws Exception
     */
    public static String executeCommandOnRemomteHost(final String user, final String password, final String host, final String cmd, long sleepTime)
            throws JSchException, IOException {
        System.out.println("Remote execution command " + cmd + " on host " + host);
        final StringBuilder result = new StringBuilder();
        try {

            final Session session = getSession(host, user, password);
            session.connect(1000);
            final Channel channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(cmd);
            channel.setXForwarding(true);

            channel.connect();
            OutputStream channelOut = channel.getOutputStream();
            channelOut.write(Integer.parseInt("13"));
            channelOut.flush();
        } catch (ConnectException ignoreMe) {

        } catch (JSchException ex) {
            ex.printStackTrace();
        }
        try {
            System.out.println("Waiting for " + sleepTime + " milliseconds to complete the remote command ");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        result.append("Completed");
        return result.toString();
    }

    /**
     * 
     * @param user
     * @param host
     * @param password
     * @return
     * @throws JSchException
     */
    public static Session getSession(final String host, final String user, final String password) throws JSchException {
        final Session session = new JSch().getSession(user, host, 22);
        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());
        return session;
    }

    public static class MyUserInfo implements UserInfo {

        public String getPassword() {
            return "password";
        }

        public String getPassphrase() {
            return "";
        }

        public boolean promptPassword(final String arg0) {
            return true;
        }

        public boolean promptPassphrase(final String arg0) {
            return true;
        }

        public boolean promptYesNo(final String arg0) {
            return true;
        }

        public void showMessage(final String arg0) {
        }
    }

}

class ExecuteInteractive {
    final private Session session;
    final private Channel channel;
    final private OutputStream stdOut;
    final private InputStream stdIn;
    final private byte[] buffer = new byte[1050];
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final static PropertiesFileController propFile = new PropertiesFileController();
    final static LogFileWriter logWriter = new LogFileWriter();

    public ExecuteInteractive(final String host, final String user, final String password) throws JSchException, IOException {
        session = ExecutorRemote.getSession(host, user, password);
        session.connect();
        channel = session.openChannel("shell");
        channel.connect();
        stdOut = channel.getOutputStream();
        stdIn = channel.getInputStream();
        channel.setXForwarding(true);

    }

    public String execute(final String command) throws IOException {
        return execute(command, 5000);
    }

    public String execute(String command, final int waitTime) throws IOException {
        command = command + "\n";
        stdOut.write((command).getBytes());
        stdOut.flush();
        String output = ExecutorRemote.getstuff(stdIn, buffer, bos, channel, waitTime);
        logWriter.writeToLogFile(topologyLogFile, output);
        System.out.print(output);
        return output;
    }

    protected void finalize() throws Throwable {
        try {
            channel.disconnect();
        } finally {
            super.finalize();
        }
    }

}
