import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Scanner;

//Delete knowhosts on the remote machine.

public class main {

    //public static Log LOG = LogFactory.getLog(main.class);
    public static void main(String[] args) {
        System.out.println("The target machine IP is:"+args[0]);
        String targetMachineIP = args[0];

        SshServerUtils sshobj = new SshServerUtils();


        if (sshobj.connect("sdn", "", targetMachineIP, 22)) {
            sshobj.execCmd("if [ -f \"/home/sdn/.ssh/known_hosts\" ]; then sudo rm /home/sdn/.ssh/known_hosts; fi");
            sshobj.execCmd("if [ -f \"/home/sdn/.ssh/known_hosts.old\" ]; then sudo rm /home/sdn/.ssh/known_hosts.old; fi");
        } else {
            //LOG.error("Please check your ipaddress");
            System.out.println("Please check your ipaddress");
        }
        sshobj.closeSession();
    }
}

