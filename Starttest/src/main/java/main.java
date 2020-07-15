import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

    //Set up log information
    public static Log LOG = LogFactory.getLog(main.class);

    public static void main(String[] args) throws Exception {
        String testcaseNumber = "";
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the ip address for manage machine:");
        String manageMachineIP = input.next();

        System.out.println("Please enter testcase name:");
        String testcase = input.next();

        System.out.println("Do you want to run the part of testcase ?( Input Y to represent yes or any other key to represent no, if you input Y, you will choose the testcases you want to run in "+ testcase +")");
        String booleanResult= input.next();

        boolean runTestcaseSeparately = booleanResult.equals("Y") ||booleanResult.equals("Yes")||booleanResult.equals("y")||booleanResult.equals("yes")||booleanResult.equals("YES");
        if(runTestcaseSeparately){
            System.out.println("Please input the testcase number you want to run in "+testcase+". Please Separate them by ',', like '0,1,10'");
            testcaseNumber= input.next();
        }

        System.out.println("testcaseNumber="+testcaseNumber);

        String projectPath = ShellUtils.getrelativePath();
        String privatekeyLocation = projectPath+"/id_rsa";
        //String publickeyLocation = projectPath+"/id_rsa.pub";

        SshServerUtils sshobj = new SshServerUtils();

        ArrayList<String> arrayList = new ArrayList<String>();

        //Connect to manage machine (sdn user)
        if (sshobj.connect("sdn", "", manageMachineIP, 22, privatekeyLocation)) {
            sshobj.shellCmd("if [ -f \"/home/sdn/.ssh/known_hosts\" ]; then sudo rm /home/sdn/.ssh/known_hosts; fi");
            sshobj.shellCmd("if [ -f \"/home/sdn/.ssh/known_hosts.old\" ]; then sudo rm /home/sdn/.ssh/known_hosts.old; fi");

            sshobj.closeSession();
        } else {
            LOG.error("Check your ipaddress or username");
        }

        //Connect to manage machine (ubuntu user)
        if (sshobj.connect("ubuntu", "", manageMachineIP, 22, privatekeyLocation)) {
            arrayList = SshServerUtils.sftpgetIP(Constants.cellPath + "/oneNodeDemo");
        } else {
            LOG.error("Check your ipaddress or username");
        }

        sshobj.shellCmd("if [ -f \"/home/ubuntu/.ssh/known_hosts\" ]; then sudo rm /home/ubuntu/.ssh/known_hosts; fi");
        sshobj.shellCmd("if [ -f \"/home/ubuntu/.ssh/known_hosts.old\" ]; then sudo rm /home/ubuntu/.ssh/known_hosts.old; fi");

        String targetMachineIP1 = arrayList.get(0);
        //Upload CleanupMachine.jar to manage machine
        sshobj.sftpUpload("/home/ubuntu/OnosSystemTest", projectPath + "/CleanupMachine.jar");
        //Clean up target machine 1
        sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP1);
//        String targetMachineIP2 = arrayList.get(1);
//        sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP2);
//        String targetMachineIP3 = arrayList.get(2);
//        sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP3);
//        String targetMachineIP4 = arrayList.get(3);
//        sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP4);
//            String targetMachineIP5 = arrayList.get(4);
//            sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP5);
//            String targetMachineIP6 = arrayList.get(5);
//            sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP6);
//            String targetMachineIP7 = arrayList.get(6);
//            sshobj.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + targetMachineIP7);
//            String mininetMachineIP = arrayList.get(7);

        sshobj.execCmd(Constants.installvlan);
        sshobj.execCmd(Constants.configurationofvlan);
        sshobj.execCmd(Constants.configurationPermanent);
        sshobj.execCmd(Constants.installScapy);

        //Run testcase
        if (runTestcaseSeparately){
            sshobj.shellCmdandLogResult("cell oneNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cli.py run " + testcase +" testcases "+testcaseNumber, testcase +" "+testcaseNumber);
        }else {
            sshobj.shellCmdandLogResult("cell oneNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cli.py run " + testcase, testcase);
            //sshobj.shellCmd("cell oneNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cleanup.sh ");
        }
        //sshobj.shellCmd("cell oneNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cleanup.sh");
        sshobj.closeSession();
    }
}
