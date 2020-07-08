import com.jcraft.jsch.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SshServerUtils {
    private static Session session;
    //Set up log information
    public static Log LOG = LogFactory.getLog(SshServerUtils.class);

    //Connect to the server,establish a session
    public static boolean connect(String username, String passwd, String host, int port, String privatekeyLocation) {
        try {
            JSch jsch = new JSch();
            //Get sshSession
            //jsch.setKnownHosts("/home/liang/.ssh/known_hosts");
            jsch.addIdentity(privatekeyLocation, passwd);
            session = jsch.getSession(username, host, port);

            //Add password
            //session.setPassword(passwd);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            //Start sshSession connect
            session.connect(4000);
            LOG.info("Server connection successful.");
            return true;
        } catch (JSchException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return false;
        }
    }

    //Execute general commands
    public static void execCmd(String command) {

        BufferedReader reader = null;

        String resultJson = null;
        ChannelExec channelExec = null;
        if (command != null) {
            try {
                //connect(username, passwd, host, port);
                //System.out.println(session.get);
                channelExec = (ChannelExec) session.openChannel("exec");
                // Set the shell command to be executed
                channelExec.setCommand(command);
                //System.out.println("linux命令:" + command);
                channelExec.setInputStream(null);
                channelExec.setErrStream(System.err);
                InputStream in = channelExec.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                channelExec.connect();

                LOG.info("The remote command is: " + command);

                String buf;
                LOG.info("Run the command");
                while ((buf = reader.readLine()) != null) {
                    LOG.info(buf);
                }
                LOG.info("The commmand is completed");
                LOG.info("");
                reader.close();
            } catch (JSchException | IOException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            } finally {
                if (null != channelExec) {
                    channelExec.disconnect();
                }
            }
        }
        return;
    }


    //Upload file by using SFTP
    public static void sftpUpload(String directory, String uploadFile) {

        BufferedReader reader = null;

        String resultJson = null;
        ChannelSftp channelSftp = null;

        if ((directory != null) && (uploadFile != null)) {
            try {
                //connect(username, passwd, host, port);
                Channel channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp) channel;
                channelSftp.cd(directory);
                //execCmd("ls");
                File file = new File(uploadFile);
                FileInputStream fileInputStream = new FileInputStream(file);
                channelSftp.put(fileInputStream, file.getName());
                fileInputStream.close();

            } catch (JSchException | SftpException | FileNotFoundException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            } finally {
                if (null != channelSftp) {
                    channelSftp.disconnect();
                }
            }
        }
        return;
    }

    //Read ip addresses from oneNodeDemo
    public static ArrayList sftpgetIP(String file) {

        BufferedReader reader = null;

        ArrayList<String> arrayList = new ArrayList<String>();

        String resultJson = null;
        ChannelSftp channelSftp = null;
        Pattern pattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Matcher matcher;

        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            InputStream stream = sftpChannel.get(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                if(matcher.find()) {
                    arrayList.add(matcher.group(0));
                }
                System.out.println(line);
            }
        }catch (JSchException | SftpException | IOException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }

        return arrayList;
    }


    //Execute shell commands
    public static int shellCmd(String command) {
        String time =dateFormatStr();
        LOG.info("Start the shell command:" + command);
        int returnCode = -1;
        try {
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            InputStream in = channel.getInputStream();
            channel.setPty(true);
            channel.connect();
            OutputStream os = channel.getOutputStream();
            os.write((command + "\r\n").getBytes());
            os.write("exit\r\n".getBytes());
            os.flush();
            LOG.info("The remote command is: " + command);
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String output = new String(tmp, 0, i);
                    LOG.info(output);
                    //addinFile(getrelativePath() + "/output" + time, output);
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    returnCode = channel.getExitStatus();
                    LOG.info("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
            os.close();
            in.close();

            channel.disconnect();
        }catch (IOException | JSchException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        return returnCode;
    }

    //Execute shell commands and log information in TestResults
    public static int shellCmdandLogResult(String command, String testcase) {
        String time =dateFormatStr();
        LOG.info("Start the shell command:" + command);
        int returnCode = -1;
        try {
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            InputStream in = channel.getInputStream();
            channel.setPty(true);
            channel.connect();
            OutputStream os = channel.getOutputStream();
            os.write((command + "\r\n").getBytes());
            os.write("exit\r\n".getBytes());
            os.flush();
            LOG.info("The remote command is: " + command);
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String output = new String(tmp, 0, i);
                    LOG.info(output);
                    addinFile(ShellUtils.getrelativePath() + "/TestResults/" + testcase + time, output);
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    returnCode = channel.getExitStatus();
                    LOG.info("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
            os.close();
            in.close();

            channel.disconnect();
        }catch (IOException | JSchException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        return returnCode;
    }

    //Close session
    public static void closeSession() {
        if (session != null) {
            session.disconnect();
            LOG.info("Server disconnect successfully.");
        }
    }

    //Write file
    public static void addinFile(String file, String conent) throws IOException {
        File inputfile = new File(file);
        if (!inputfile.exists()) {
            inputfile.createNewFile();
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }
    }

    //Get current time
    private static String dateFormatStr(){
        Date date = new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time;
    }
}