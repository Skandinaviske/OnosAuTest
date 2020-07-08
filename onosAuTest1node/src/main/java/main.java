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

        System.out.println("Please enter the private key location:");
        String privatekeyLocation=input.next();

        System.out.println("Please enter the public key location:");
        String publickeyLocation=input.next();

        String projectPath = ShellUtils.getrelativePath();

        ShellUtils.execShell("sudo cp "+privatekeyLocation+" "+projectPath+"/id_rsa");
        ShellUtils.execShell("sudo cp "+publickeyLocation+" "+projectPath+"/id_rsa.pub");

        privatekeyLocation = projectPath+"/id_rsa";
        publickeyLocation = projectPath+"/id_rsa.pub";

        ShellUtils.execShell("sudo chgrp " + System.getProperty("user.name") + " " + privatekeyLocation);
        ShellUtils.execShell("sudo chown " + System.getProperty("user.name") + " " + privatekeyLocation);
        ShellUtils.execShell("sudo chown " + System.getProperty("user.name") + " " + publickeyLocation);
        ShellUtils.execShell("sudo chgrp " + System.getProperty("user.name") + " " + publickeyLocation);

        SshServerUtils sshobj = new SshServerUtils();

        //Connect to Manage machine(ubuntu user)

        Thread.sleep(2000);
        if(sshobj.connect("ubuntu","",manageMachineIP,22, privatekeyLocation)) {

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

            sshobj.execCmd("echo 'export ONOS_ROOT=~/onos' | sudo tee --append /home/ubuntu/.bashrc");
            sshobj.execCmd("echo 'source $ONOS_ROOT/tools/dev/bash_profile' | sudo tee --append /home/ubuntu/.bashrc");
            sshobj.execCmd("source .bashrc");

            sshobj.execCmd(Constants.installconfigObj);
            sshobj.execCmd(Constants.gitOnosSystemtest);
            sshobj.execCmd("cd OnosSystemTest/TestON/ ; ./install.sh");

            //System.out.println("sudo cp "+ projectPath + "/src/main/resources/OneNodeDemo "+projectPath+"/src/main/resources/oneNodeDemo");

            //Create oneNodeDemo
            ShellUtils.execShell("cp "+ projectPath + "/OneNodeDemo "+projectPath+"/oneNodeDemo");
            Thread.sleep(2000);

            //Write the ip addresses in oneNodeDemo
            ShellUtils.replaceSelected(targetMachineIP1,"IPadd1");
            ShellUtils.replaceSelected(mininetMachineIP,"IPadd2");

            //Upload oneNodeDemo under cell
            sshobj.sftpUpload(Constants.cellPath, projectPath + "/oneNodeDemo");

            //Upload SetupMininetMachine.jar and SetupTargetMachine.jar to the manage machine
            sshobj.sftpUpload("/home/ubuntu/OnosSystemTest", projectPath + "/SetupMininetMachine.jar");
            sshobj.sftpUpload("/home/ubuntu/OnosSystemTest", projectPath + "/SetupTargetMachine.jar");

            //Set up mininet machine
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupMininetMachine.jar ubuntu "+mininetMachineIP);
            //sshobj.shellCmd("cd OnosSystemTest; java -jar SetupMininetMachine.jar ubuntu "+mininetMachineIP);

            //Set up target machine
            sshobj.shellCmd("cd OnosSystemTest; java -jar SetupTargetMachine.jar ubuntu "+targetMachineIP1);

            sshobj.closeSession();

            //Connect to Manage machine(sdn user)
            sshobj.connect("sdn","",manageMachineIP,22, privatekeyLocation);
            sshobj.execCmd("sudo chmod 700 /home/sdn/.ssh");
            sshobj.execCmd("sudo chmod 644 /home/sdn/.ssh/authorized_keys");
            sshobj.execCmd(Constants.gitOnos);
            sshobj.execCmd("echo 'export ONOS_ROOT=~/onos' | sudo tee --append /home/sdn/.bashrc");
            sshobj.execCmd("echo 'source $ONOS_ROOT/tools/dev/bash_profile' | sudo tee --append /home/sdn/.bashrc");
            sshobj.execCmd("source .bashrc");
            sshobj.closeSession();

            //Connect to Manage machine(sdn user)
            sshobj.connect("sdn","",manageMachineIP,22, privatekeyLocation);
            sshobj.shellCmd("cd onos; bazel build onos");
            sshobj.execCmd("sudo chgrp sdn /home/sdn/.ssh/id_rsa");
            sshobj.execCmd("sudo chown sdn /home/sdn/.ssh/id_rsa");
            sshobj.execCmd("sudo chown sdn /home/sdn/.ssh/id_rsa.pub");
            sshobj.execCmd("sudo chgrp sdn /home/sdn/.ssh/id_rsa.pub");
            sshobj.execCmd("sudo chgrp sdn /home/sdn/.ssh/authorized_keys");
            sshobj.execCmd("sudo chown sdn /home/sdn/.ssh/authorized_keys");
            sshobj.closeSession();

            //Connect to Manage machine(ubuntu user)
            sshobj.connect("ubuntu","",manageMachineIP,22, privatekeyLocation);
            //Run the start sample test commands
            sshobj.shellCmdandLogResult("cell oneNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cli.py run SAMPstartTemplate_1node","SAMPstartTemplate_1node");
            sshobj.closeSession();
        }else{
            LOG.error("Check your ipaddress or username");
        }
    }
}
