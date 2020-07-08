public class test {
//    public static void main(String[] args) throws Exception {
//        SshServerUtils sshobjs = new SshServerUtils();
//
//        String projectPath = ShellUtils.getrelativePath();
//
//        String privatekeyLocation = projectPath+"/id_rsa";
//
//        if (sshobjs.connect("sdn", "", "206.12.88.31", 22, privatekeyLocation)) {
//            sshobjs.shellCmd("if [ -f \"/home/sdn/.ssh/known_hosts\" ]; then sudo rm /home/sdn/.ssh/known_hosts; fi");
//            sshobjs.shellCmd("if [ -f \"/home/sdn/.ssh/known_hosts.old\" ]; then sudo rm /home/sdn/.ssh/known_hosts.old; fi");
//
//            sshobjs.closeSession();
//        } else {
//            System.out.println("Check your ipaddress or username");
//        }
//
//
//        if (sshobjs.connect("ubuntu", "", "206.12.88.31", 22, privatekeyLocation)) {
//            //arrayList = SshServerUtils.sftpgetIP(Constants.cellPath + "/threeNodeDemo");
//        } else {
//            System.out.println("Check your ipaddress or username");
//        }
//
//        sshobjs.shellCmd("if [ -f \"/home/ubuntu/.ssh/known_hosts\" ]; then sudo rm /home/ubuntu/.ssh/known_hosts; fi");
//        sshobjs.shellCmd("if [ -f \"/home/ubuntu/.ssh/known_hosts.old\" ]; then sudo rm /home/ubuntu/.ssh/known_hosts.old; fi");
//        sshobjs.sftpUpload("/home/ubuntu/OnosSystemTest", sshobjs.getrelativePath() + "/CleanupMachine.jar");
//        sshobjs.shellCmd("cd OnosSystemTest; java -jar CleanupMachine.jar " + "192.168.171.28");
//        sshobjs.closeSession();
//    }
}
