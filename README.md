# OnosAuTest
How to setup the environment and run the test script with 1 node?

##　On the compute Canada cloud
１．Create 3 VMS manually (1 manage machine, 1 mininet machine, 1 target machine).
２．Make sure the private key in your computer can ssh to the manage machine, mininet machine and target machine without password. So the public key you upload in Compute Canada Cloud should match the private key in your computer.

## In Test1node　folder:
### onosAuTest1node.jar: 
We use this one to setup all the test environments. It will install all the softwares on the manage machine, mininet machine and target machine. And it will also create user sdn on all those machines. After that it will try to run the sample testcase with 1 node (SAMPstartTemplate_1node) on the manage machine and record  the result in TestResults folder. 

We can run it by input “java -jar onosAuTest1node.jar”. Then we input 3 ip addresses (the public network ip for manage machine and the intranet ip for the other machines like the following image) and the location of the public key and private key.

### Starttest.jar: 
After run onosAuTest1nodes.jar, we use this one to run the testcase we choose.

We can run it by input “java -jar Starttest.jar”. Then we input the ip address of the manage machine (public network ip) and the name of the testcase we want to run like the following image.  The result will be recorded in TestResults folder.

You can get the source code from onosAuTest1node　folder and Starttest　folder
