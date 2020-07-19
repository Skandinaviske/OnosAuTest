import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Scanner;

//Main class
public class main{

    //Set up log information
    public static Log LOG = LogFactory.getLog(main.class);
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner (System.in);
        System.out.println("Please enter the ip address for manage machine:");
        String manageMachineIP=input.next();

        System.out.println("Please enter the ip address for mininet machine:");
        String mininetMachineIP=input.next();

        System.out.println("Please enter the ip address for target 1 machine:");
        String targetMachineIP1=input.next();

        System.out.println("Please enter the ip address for target 2 machine:");
        String targetMachineIP2=input.next();

        System.out.println("Please enter the ip address for target 3 machine:");
        String targetMachineIP3=input.next();

        System.out.println("Please enter the ip address for target 4 machine:");
        String targetMachineIP4=input.next();

        System.out.println("Please enter the ip address for target 5 machine:");
        String targetMachineIP5=input.next();

        System.out.println("Please enter the ip address for target 6 machine:");
        String targetMachineIP6=input.next();

        System.out.println("Please enter the ip address for target 7 machine:");
        String targetMachineIP7=input.next();

        System.out.println("Please enter the absolute file path of your private key:");
        String privatekeyLocation=input.next();

        System.out.println("Please enter the absolute file path of your public key:");
        String publickeyLocation=input.next();

        String projectPath = ShellUtils.getrelativePath();

        ShellUtils.execShell("cp "+privatekeyLocation+" "+projectPath+"/id_rsa");
        ShellUtils.execShell("cp "+publickeyLocation+" "+projectPath+"/id_rsa.pub");

        privatekeyLocation = projectPath+"/id_rsa";
        publickeyLocation = projectPath+"/id_rsa.pub";

        SshServerUtils sshobj = new SshServerUtils();

        //String username = System.getProperty("user.name");
        //System.out.println("username = " + username);

        Thread.sleep(2000);
        if(sshobj.connect("ubuntu","",manageMachineIP,22,privatekeyLocation)) {

            //Install softwares on the manage machine
            sshobj.execCmd(Constants.update);
            sshobj.execCmd(Constants.upgrade);
            sshobj.execCmd(Constants.installgit);
            sshobj.execCmd(Constants.installzip);
            sshobj.execCmd(Constants.installunzip);
            sshobj.execCmd(Constants.installcurl);
            sshobj.execCmd(Constants.installpy2);
            sshobj.execCmd(Constants.installpy3);
            sshobj.execCmd(Constants.installpip);
            sshobj.execCmd(Constants.installselenium);
            sshobj.execCmd(Constants.installBazelisk);
            sshobj.execCmd(Constants.installJava11);
            sshobj.execCmd(Constants.installrequests);
            sshobj.execCmd(Constants.installselenium);
            sshobj.execCmd(Constants.addUsersdn);
            sshobj.execCmd(Constants.addsdntosudoGroup);
            sshobj.execCmd(Constants.changeshelltoSH);
            sshobj.execCmd(Constants.setsudowithoutpassword);

            //Upload the private key and public key to the manage machine
            sshobj.sftpUpload("/home/ubuntu/.ssh", privatekeyLocation);
            sshobj.sftpUpload("/home/ubuntu/.ssh", publickeyLocation);

            sshobj.execCmd("sudo chmod 600 .ssh/id_rsa");
            sshobj.execCmd("sudo chmod 644 .ssh/id_rsa.pub");
            sshobj.execCmd("sudo su - sdn -c 'mkdir .ssh'");
            sshobj.execCmd("sudo cp /home/ubuntu/.ssh/id_rsa.pub /home/sdn/.ssh/id_rsa.pub");
            sshobj.execCmd("sudo cp /home/ubuntu/.ssh/id_rsa /home/sdn/.ssh/id_rsa");
            sshobj.execCmd("sudo cp /home/ubuntu/.ssh/id_rsa.pub /home/sdn/.ssh/authorized_keys");
            sshobj.execCmd(Constants.gitOnos);
//
            sshobj.execCmd("echo 'export ONOS_ROOT=~/onos' | sudo tee --append /home/ubuntu/.bashrc");
            sshobj.execCmd("echo 'source $ONOS_ROOT/tools/dev/bash_profile' | sudo tee --append /home/ubuntu/.bashrc");
            sshobj.execCmd("source .bashrc");
//
            sshobj.execCmd(Constants.installconfigObj);
            sshobj.execCmd(Constants.gitOnosSystemtest);
            sshobj.execCmd("cd OnosSystemTest/TestON/ ; ./install.sh");

            //Create sevenNodeDemo

            ShellUtils.execShell("cp "+ projectPath + "/SevenNodeDemo "+projectPath+"/sevenNodeDemo");
            Thread.sleep(2000);

            //Write the ip addresses in sevenNodeDemo
            ShellUtils.replaceSelected(targetMachineIP1,"IPadd1");
            ShellUtils.replaceSelected(targetMachineIP2,"IPadd2");
            ShellUtils.replaceSelected(targetMachineIP3,"IPadd3");
            ShellUtils.replaceSelected(targetMachineIP4,"IPadd4");
            ShellUtils.replaceSelected(targetMachineIP5,"IPadd5");
            ShellUtils.replaceSelected(targetMachineIP6,"IPadd6");
            ShellUtils.replaceSelected(targetMachineIP7,"IPadd7");
            ShellUtils.replaceSelected(mininetMachineIP,"IPadd8");

            //Upload sevenNodeDemo under cell
            sshobj.sftpUpload(Constants.cellPath, projectPath + "/sevenNodeDemo");

            //Upload SetupMininetMachine.jar and SetupTargetMachine.jar to the manage machine
            sshobj.sftpUpload("/home/ubuntu/OnosSystemTest", projectPath + "/SetupMininetMachine.jar");
            sshobj.sftpUpload("/home/ubuntu/OnosSystemTest",projectPath + "/SetupTargetMachine.jar");

            //Set up mininet machine
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupMininetMachine.jar ubuntu "+mininetMachineIP);

            //Set up target machine
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP1);
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP2);
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP3);
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP4);
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP5);
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP6);
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP7);

            sshobj.closeSession();

            //Connect to Manage machine(sdn user)
            sshobj.connect("sdn","",manageMachineIP,22,privatekeyLocation);
            sshobj.execCmd("sudo chmod 700 /home/sdn/.ssh");
            sshobj.execCmd("sudo chmod 644 /home/sdn/.ssh/authorized_keys");
            sshobj.execCmd(Constants.gitOnos);
            sshobj.execCmd("echo 'export ONOS_ROOT=~/onos' | sudo tee --append /home/sdn/.bashrc");
            sshobj.execCmd("echo 'source $ONOS_ROOT/tools/dev/bash_profile' | sudo tee --append /home/sdn/.bashrc");
            sshobj.execCmd("source .bashrc");
            sshobj.closeSession();

            //Connect to Manage machine(sdn user)
            sshobj.connect("sdn","",manageMachineIP,22,privatekeyLocation);
            sshobj.shellCmd("cd onos; bazel build onos");
            sshobj.execCmd("sudo chgrp sdn /home/sdn/.ssh/id_rsa");
            sshobj.execCmd("sudo chown sdn /home/sdn/.ssh/id_rsa");
            sshobj.execCmd("sudo chown sdn /home/sdn/.ssh/id_rsa.pub");
            sshobj.execCmd("sudo chgrp sdn /home/sdn/.ssh/id_rsa.pub");
            sshobj.execCmd("sudo chgrp sdn /home/sdn/.ssh/authorized_keys");
            sshobj.execCmd("sudo chown sdn /home/sdn/.ssh/authorized_keys");
            sshobj.closeSession();

            //Connect to Manage machine(ubuntu user)
            sshobj.connect("ubuntu","",manageMachineIP,22,privatekeyLocation);
            //Run the start sample test commands
            sshobj.shellCmdandLogResult("cell sevenNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cli.py run HAsingleInstanceRestart","HAsingleInstanceRestart");
            sshobj.closeSession();
        }else{
            LOG.error("Check your ipaddress or username");
        }
    }
}
