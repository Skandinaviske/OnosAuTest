import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

        String projectPath = ShellUtils.getrelativePath();
        String privatekeyLocation = projectPath+"/id_rsa";
        //String publickeyLocation = projectPath+"/id_rsa.pub";

        SshServerUtils sshobj = new SshServerUtils();

        //ArrayList<String> arrayList = new ArrayList<String>();

        //Connect to manage machine (ubuntu user)
        if (sshobj.connect("ubuntu", "", manageMachineIP, 22, privatekeyLocation)) {
        } else {
            LOG.error("Check your ipaddress or username");
        }

        sshobj.shellCmd("cell oneNodeDemo; cd ~/OnosSystemTest/TestON/bin/; ./cleanup.sh");

        sshobj.closeSession();
    }
}
