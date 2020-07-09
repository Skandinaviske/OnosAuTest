import java.util.Scanner;

//Setup Target machine including installing softwares and create sdn user

public class main extends SshServerUtils {

    public static void main(String[] args) throws Exception
    {
        //System.out.println("Please enter the user name:");
        System.out.println(args[0]);
        //System.out.println("Please enter the ip address for target machine:");
        System.out.println(args[1]);

        //String username = System.getProperty("user.name");
        //System.out.println("username = " + username);
        if(connect(args[0],"",args[1],22)) {
            execCmd(Constants.update);
            execCmd(Constants.upgrade);
            execCmd(Constants.installcurl);
            execCmd(Constants.installJava11);
            execCmd(Constants.addUsersdn);
            execCmd(Constants.addsdntosudoGroup);
            execCmd(Constants.changeshelltoSH);
            execCmd(Constants.setsudowithoutpassword);

            sftpUpload("/home/ubuntu/.ssh", "/home/ubuntu/.ssh/id_rsa");
            sftpUpload("/home/ubuntu/.ssh", "/home/ubuntu/.ssh/id_rsa.pub");

            execCmd("sudo chmod 600 .ssh/id_rsa");
            execCmd("sudo chmod 644 .ssh/id_rsa.pub");
            execCmd("sudo su - sdn -c 'mkdir .ssh'");
            execCmd("sudo cp /home/ubuntu/.ssh/id_rsa.pub /home/sdn/.ssh/id_rsa.pub");
            execCmd("sudo cp /home/ubuntu/.ssh/id_rsa /home/sdn/.ssh/id_rsa");
            execCmd("sudo cp /home/ubuntu/.ssh/id_rsa.pub /home/sdn/.ssh/authorized_keys");
//            execCmd(Constants.gitOnos);
////
            execCmd("echo 'export ONOS_ROOT=~/onos' | sudo tee --append /home/ubuntu/.bashrc");
            execCmd("echo 'source $ONOS_ROOT/tools/dev/bash_profile' | sudo tee --append /home/ubuntu/.bashrc");
            execCmd("source .bashrc");

            execCmd("sudo chgrp sdn /home/sdn/.ssh/id_rsa");
            execCmd("sudo chown sdn /home/sdn/.ssh/id_rsa");
            execCmd("sudo chown sdn /home/sdn/.ssh/id_rsa.pub");
            execCmd("sudo chgrp sdn /home/sdn/.ssh/id_rsa.pub");
            execCmd("sudo chgrp sdn /home/sdn/.ssh/authorized_keys");
            execCmd("sudo chown sdn /home/sdn/.ssh/authorized_keys");
            closeSession();

        }else{
            LOG.error("Check your ipaddress or username");
        }
    }
}
