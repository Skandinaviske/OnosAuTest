# OnosAuTest

This README file shows how to run ONOS System testcases with 1 node, 3nodes and 7 nodes.
For basic information of ONOS System test, please read:
https://wiki.onosproject.org/display/ONOS/System+Testing+Guide

## Setup test script with 1 node:
How to setup the environment and run the test script with 1 node?

### On the compute Canada cloud
１．Create 3 VMs manually (1 manage machine, 1 mininet machine, 1 target machine).

２．Make sure the private key in your computer can ssh to the manage machine, mininet machine and target machine without password. So the public key you upload in Compute Canada Cloud should match the private key in your computer.

### Open Test1node folder:
#### onosAuTest1node.jar: 
We use this one to setup all the test environments. It will install the all softwares on the manage machine, mininet machine and target machine. And it will also create user sdn on all those machines. After that it will try to run the sample testcase with 1 node (SAMPstartTemplate_1node) on the manage machine and record  the result in TestResults folder. 

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

## Setup test script with 3 node:
How to setup the environment and run the test script with 3 nodes?

### On the compute Canada cloud
１．Create 5 VMs manually (1 manage machine, 1 mininet machine, 3 target machines).

２．Make sure the private key in your computer can ssh to the manage machine, mininet machine and target machines without password. So the public key you upload in Compute Canada Cloud should match the private key in your computer.

### Open Test3nodes folder:
#### onosAuTest3node.jar: 
We use this one to setup all the test environments. It will install all the softwares on the manage machine, mininet machine and target machines. And it will also create user sdn on all those machines. After that it will try to run the sample testcase with 3 node (SAMPstartTemplate_3node) on the manage machine and record  the result in TestResults folder. 

We can run it by input the following command:
`java -jar onosAuTest3node.jar`
Then we input 5 ip addresses (the public network ip for manage machine and the intranet ip for the other machines) and the location of the public key and private key.
The source code of onosAuTest3nodes.jar is in **onosAuTest3nodes** folder．

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
This file is used in onosAuTest1node.jar and onosAuTest3node.jar, it will install the required softwares and create user sdn in the target machines from the manage machine. The source code of SetupMininetMachine.jar is in **SetupTargetMachine ** folder．

#### CleanupMachine.jar
This file is used in Starttest.jar, it will delete the ~/.ssh/known_hosts and ~/.ssh/known_hosts.old files in the target machines. Because I find it sometimes will cause some errors when we run the testcases. The source code of CleanupMachine.jar is in **CleanupMachine** folder．

