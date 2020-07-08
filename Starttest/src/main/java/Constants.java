//Constant class

public class Constants {

    public static final String mkdir = "";
    public static final String update = "sudo apt-get update";
    public static final String upgrade = "sudo apt-get upgrade -y";
    public static final String installgit = "sudo apt-get install git-all -y";
    public static final String installzip = "sudo apt-get install zip -y";
    public static final String installunzip = "sudo apt-get install unzip -y";
    public static final String installcurl = "sudo apt-get install curl -y";
    //public static final String installgit = "sudo apt-get install git-all";
    public static final String installpy2 = "sudo apt-get install python -y && sudo apt-get install python-pip python-dev python-setuptools -y";
    public static final String installpy3 = "sudo apt-get install python3 -y && sudo apt-get install python3-pip python3-dev python3-setuptools -y";
    public static final String installpip = "pip3 install --upgrade pip";
    public static final String installselenium = "pip3 install selenium";
    public static final String installBazelisk = "wget https://github.com/bazelbuild/bazelisk/releases/download/v1.4.0/bazelisk-linux-amd64 && chmod +x bazelisk-linux-amd64 && sudo mv bazelisk-linux-amd64 /usr/local/bin/bazel";
    public static final String installJava11 = "sudo apt-get install openjdk-11-jdk openjdk-11-jre -y";
    public static final String installrequests= "pip install requessudo adduser --disabled-password sdnts";

    public static final String addUsersdn= "echo -ne '\n' |sudo adduser --disabled-password sdn";
    public static final String addsdntosudoGroup= "sudo usermod -aG sudo sdn ";
    public static final String changeshelltoSH = "sudo usermod -s /bin/bash sdn";
    //public static final String sudo = "sudo -i";
    public static final String setsudowithoutpassword = "echo 'sdn ALL=(ALL) NOPASSWD:ALL' | sudo tee --append /etc/sudoers";
    //public static final String createKeyPair = "ssh-keygen -t rsa";

    public static final String gitOnos = "git clone https://gerrit.onosproject.org/onos";
    public static final String gitOnosSystemtest = "git clone https://gerrit.onosproject.org/OnosSystemTest";
    public static final String gitMininet = "git clone https://github.com/jhall11/mininet.git;cd mininet;git checkout -b dynamic_topo origin/dynamic_topo;cd util;sudo ./install.sh -3fvn";
    public static final String cellPath = "/home/ubuntu/onos/tools/test/cells";
    public static final String mininetclidriverPath = "/home/ubuntu/OnosSystemTest/TestON/drivers/common/cli/emulator";
    public static final String installvlan = "sudo apt-get install vlan";
    public static final String configurationofvlan = "sudo modprobe 8021q";
    public static final String configurationPermanent = "sudo su -c 'echo \"8021q\" >> /etc/modules'";
    public static final String installScapy = "sudo apt-get install -y scapy";
}
