# FTPServer
This is a JAVA implementation of an FTP server done on the TCP (Transmission Control Protocol) developed for the course CSI 516 at SUNY ALBANY.
The files are .JAVA classes, and needs to be compiled. Any JDK can be used to compile the files.
It throws an error for deprecated API, because of the readUTF() and read() method.
Reason: The code was run on both the remote virtual machine and on a remote client to ensure that it is working fine on both the VM (which had OpenJDK - IcedTea running on a Debian Machine) and on the newer machine that had Oracle JDK 11.0.1 and JDK 8 LTS.
DataOutputStream.writeUTF() and DataInputStream.readUTF() are the way to go, and is the key way to build any client-server architecture.
There were two projects in CSI516 one is based on PAXSON'96 to take measurement of paths on planet-lab.org with servers in various countries and discover fluttering, triangular detours in packet routing and collecting and plotting the data, along with finding out things like congestion with one server in Europe and one in Australia/New Zealand. The other one is the FTP server in TCP and UDP and analyzing the packets on Wireshark.
