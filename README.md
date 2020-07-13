# OnosAuTest

This README file shows how to run ONOS System testcases with 1 node, 3 nodes.  
For basic information of ONOS System test, please read:  
https://wiki.onosproject.org/display/ONOS/System+Testing+Guide  

*Note that You don't need install any softwares or create user on VMs becasue the script will do it automatically. You only need to create the VMs manually.*  


## Environment:

JDK1.8

## Development tools:

IntelliJ IDEA

## Setup test script with 1 node:

How to setup the environment and run the test script with 1 node?

### On the compute Canada cloud
1. First check if you have ssh key in ~/.ssh, if you don't have one, please generate by using the following command:

`ssh-keygen -t rsa -m PEM`

*Note that don't setup the password here.*

After that you need to upload your public key to your Compute Canada cloud's **Key Pairs**.

If you have one, please upload your public key to your Compute Canada cloud's **Key Pairs**.

2．Create 3 VMs manually (1 manage machine, 1 mininet machine, 1 target machine).

### Open Test1node folder:

#### onosAuTest1node.jar: 

We use this one to setup all the test environments. It will install the all softwares on the manage machine, mininet machine and target machine. And it will also create user sdn on all those machines. After that it will try to run the sample testcase with 1 node (*SAMPstartTemplate_1node*) on the manage machine and record  the result in TestResults folder. 

We can run it by input the following command:  

`java -jar onosAuTest1node.jar`  

Then we input 3 ip addresses (the public network ip for manage machine and the intranet ip for the other machines) and the location of the public key and private key.  
The source code of onosAuTest1node.jar is in **onosAuTest1node** folder．  

#### Starttest.jar: 

After run onosAuTest1nodes.jar, we use this one to run the testcase we choose.
We can run it by input the following command:  

`java -jar Starttest.jar`  

Then we input the ip address of the manage machine (public network ip) and the name of the testcase we want to run. The result will be recorded in TestResults folder.    
The source code of Starttest.jar is in **Starttest** folder．  

## Setup test script with 3 nodes:

How to setup the environment and run the test script with 3 nodes?

### On the compute Canada cloud

1. First check if you have ssh key in ~/.ssh, if you don't have one, please generate by using the following command:

`ssh-keygen -t rsa -m PEM`

*Note that don't setup the password here.*

After that you need to upload your public key to your Compute Canada cloud's **Key Pairs**.

If you have one, please upload your public key to your Compute Canada cloud's **Key Pairs**.

2．Create 5 VMs manually (1 manage machine, 1 mininet machine, 3 target machines).

### Open Test3nodes folder:
#### onosAuTest3nodes.jar: 

We use this one to setup all the test environments. It will install all the softwares on the manage machine, mininet machine and target machines. And it will also create user sdn on all those machines. After that it will try to run the sample testcase with 3 node (*SAMPstartTemplate_3node*) on the manage machine and record  the result in TestResults folder. 

We can run it by input the following command:  

`java -jar onosAuTest3node.jar`  

Then we input 5 ip addresses (the public network ip for manage machine and the intranet ip for the other machines) and the location of the public key and private key.  
The source code of onosAuTest3nodes.jar is in **onosAuTest3nodes** folder.  

#### Starttest.jar: 

After run onosAuTest3nodes.jar, we use this one to run the testcase we choose.  
We can run it by input the following command:   

`java -jar Starttest.jar`  

Then we input the ip address of the manage machine (public network ip) and the name of the testcase we want to run. The result will be recorded in TestResults folder.  
The source code of Starttest.jar has a little bit different from **Starttest** folder.  

## Uses of other jar packages in Test1node folder and Test3nodes folder:

#### SetupMininetMachine.jar

This file is used in onosAuTest1node.jar and onosAuTest3node.jar, it will install the required softwares and create user sdn in the mininet machine from the manage machine. The source code of SetupMininetMachine.jar is in **SetupMininetMachine** folder．

#### SetupTargetMachine.jar

This file is used in onosAuTest1node.jar and onosAuTest3node.jar, it will install the required softwares and create user sdn in the target machines from the manage machine. The source code of SetupMininetMachine.jar is in **SetupTargetMachine** folder．

#### CleanupMachine.jar

This file is used in Starttest.jar, it will delete the *~/.ssh/known_hosts* and *~/.ssh/known_hosts.old* files in the target machines. Because I find it sometimes will cause some errors when we run the testcases. The source code of CleanupMachine.jar is in **CleanupMachine** folder．

## Notices

1. If you first run 3 node testcases and then you want to run 1 node testcases, please **Shut off instance** which you don't need. It sometimes affect the test results.  
2. Don't delete id_rsa, id_rsa.pub, oneNodeDemo, threeNodeDemo after run onosAuTest1node.jar or onosAuTest3nodes.jar, because they will be used when you run Starttest.jar.  
3. If you want to know the structure of the test scripts, please read **Tutorial of one node test.odp**.   
4. I will add the 7 node scripts after it runs properly with IOS.  

## Author:

Ailwyn
