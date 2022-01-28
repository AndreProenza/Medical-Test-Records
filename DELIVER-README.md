# Medical-Test-Records

<img src="https://user-images.githubusercontent.com/78174997/151461968-53e7c3a0-c20c-4043-870a-62e9eea75f1e.jpg" width="700">

---

## Problem

- Health care institutions gather and store sensitive information from patients such as medical test records;
- Patients can not always access their medical records in a secure manner;
- Information systems do not always ensure fine-grained and contextualized access to medical records for relevant staff;
- Lack of connection and access to medical records of different partner institutions, with different infrastructures.

---

## Solution

- Provide a secure system to allow safe and confidential access to patient's medical records;
- Ensure authentication and access control to certain resources for authorized personnel;
- Ensure secure interconnection (sending and receiving medical records) between partner health organizations.

---

### Built With

* [Java](https://openjdk.java.net/) - Programming Language;
* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management;
* [Spring Boot](https://spring.io/projects/spring-boot) - Create Java stand-alone Spring applications;
* [Spring Boot Security](https://spring.io/projects/spring-security) - A highly customizable authentication and access-control framework;
* [Mongo DB Atlas](https://www.mongodb.com/) - Cloud Database;
* [Ubuntu Server](https://ubuntu.com/download/server) - Virtual Machines to run the Servers;
* [UFW - Uncomplicated Firewall](https://www.linux.com/training-tutorials/introduction-uncomplicated-firewall-ufw/) - Firewall.

---

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

---

### Prerequisites

If testing via vms:

- VirtualBox installed on your machine. [Install here VirtualBox](https://www.virtualbox.org/wiki/Downloads)

if testing via jar files:
- java 17
- Maven

---

### Installing

#### Vms
To prepare the environment it is necessary to import the vms. Firstly a new NAT Network must be created in order to successfully setup the network interfaces.

To create a Nat Network go to:
- **File > preferences > Network**. 
- Click on the green icon to add a new Nat Network. 
- The name should be "**rede**", Network CIDR: **10.0.0.0/24** and **DHCP** should be **enabled**. 

With that done we can procede to import the vms.

In order to import the vm you should go to:

- **Import**, Choose the correct .ova file and import it into virtualbox. 

After setting up the network, by choosing the created network interface the vm can be turned on and changing MACs. 
Finally By starting the vm, the code will be running and the server will start without any help.

To access the frontend for both laboratory and hospital, the vm patient should be started. The login is **patient** and password is **patient**. After logging in, when after opening the browser it is possible to see two bookmarks:

For hospital:
```
https://10.0.0.4:8443/
```
For laboratory
```
https://10.0.0.104:8443/
```

The credentials for each vm are the the same as the **hostname**, this is, if hostname is hosf, then username is hosf and password is hosf. 
The root crentials are root:root


#### Jars and maven

To run the code in a local linux system java 17 and maven must be installed.

Installation of java 17 in linux:
```
sudo apt install openjdk-17-jre
sudo apt install openjdk-17-jdk

Change java version to the newer one.
sudo update-alternatives --config java
```

Installing maven, compatible with java 17:
```
0) Please go to: https://maven.apache.org/download.cgi

1) Download Binary tar.gz archive

2) Unpack the archive with tar/unzip

3) A directory called "apache-maven-3.x.y" will be created.

The following steps are unecessary but can be useful to install mvn system wide.

4) Add the apache-maven-3.8.4/bin directory to your PATH, e.g.:
    export PATH=/usr/local/apache-maven-3.x.y/bin:$PATH

5) Run "mvn --version" to verify that it is correctly installed.
```

##### Hospital Frontend 10.0.0.4
To run the Hospital Frontend do:
```
java -jar jars/Hospital-Frontend.jar
```

##### Hospital Backend 10.0.0.5
To run the Hospital Backend do:
```
java -jar jars/Hospital-Backend.jar
```

##### Laboratory Frontend 10.0.0.104
To run the Laboratory Frontend do:
```
java -jar jars/Laboratory-Frontend.jar
```

##### Laboratory Backend 10.0.0.105
To run the Laboratory Backend do:
```
java -jar jars/Laboratory-Backend.jar
```

##### Hospital Backend Server 10.0.0.6
To run the Hospital Backend Server do:
```
cd Hospital-Backend-Server
/path/to/apache-maven-3.8.4/bin/mvn clean deploy
```

#### Notes about running the application

Before running the application it is necessary note that both Hospital and Laboratory frontends are running on port 8443, and their respective backends are also running in port 3000. Due to this, it is impossible to run both Hospital and Laboratory at the same time. It is still possible to run Hospital Backened server, that is running on port 4000, in order to test the custom protocol.

Summarized:
+ Hospital Frontend: port 8443
+ Hospital Backened: port 3000
+ Laboratory Frontend: port 8443
+ Laboratory Backened: port 3000
+ Hospital Backend Server: port 4000


#### limitations

+ Due to lack of time, we were unable to output a error/sucess message to laboratory frontened html, for the sucess/insucess in putting the medical Record of the laboratory in the Hospital database. The message can be seen in the System.Out of the Laboratory backend. 

---

## System Features

-	Ensures confidentiality and integrity of medical records;
-	Ensures confidentiality and integrity of communications with the web browser;
-	Ensure successful authentication of citizens;
-	Authenticates citizens in a secure way;
-	Ability to change password if citizen is authenticated;
-	Authenticated user credentials confirmation sent to email;
-	Ensures that only authorized staff and patients have an account;
-	Ensures different “roles” have access to different privileges;
-	Ensures there is only one account per citizen, using citizen ID card number;
-	Prevents access to medical records if the citizen does not have privileges;
-	Allows user A to change the privileges of user B, if user A is a system administrator;
-	Validates and sanitize form input;
-	Uses HTTPS to encrypt communications;
-	Stores and manage symmetric and asymmetric keys;
-	Defines a restricted set of rules on the firewall;
-	Establishes mutual authentication and shares medical records with partner institutions;
-	Minimizes the impact of attacks inside the system.


---

## Deployment

For deploymend on a live system you should have access to a mongodb database account and a suficient ammount of ram.
The upgrade from iptables into firewall will greatly benefit your network, since with them there comes a better capability
to obtain logs and information about the network.

For virtualization any hyphervisor should suffice, you just need to be able to create isolated internal networks and and have
access to the internet.

For a more isolated control of any possible vulnerabilities, the use of docker could add an extra layer of protection against attacks to the 
network, this is, it creates an extra difficulty to obtain higher/relevant credentials on the computar that manages the docker/s.

Regarding the system, there is a necessity to create the first admin manually, since the option of creating new user/admins is not available. This is a great advantage in terms of security, but can become a problem in usability, even if it must only be done once.

The Hospital Backend server is able to accept multiple clients (via threads) therefore it possible to easily escalate the system, create multiple laboratories.

The credentials for each vm are the the same as the hostname, this is, if hostname is **hosf**, then username is **hosf** and password is **hosf**. The root crentials are root:root

Regarding vm and networking, although we had a solution that forwarded all packets from both interfaces via a router, the vms had no internet access, therefore we had to remove the router and put every vm in the same network. The firewall rules were blocking packets that they shouldn't be blocking, so we disabled all firewall rules in all vms, which is a grave security issue.

---

## Additional Information

### Authors

* **Ana Albuquerque** - ** ist1102209 ** - [GitHub](https://github.com/albuana)
* **André Proença** - ** ist1102327 ** - [GitHub](https://github.com/AndreProenza)
* **Joel Russo** -  ** ist1102098 ** - [GitHub](https://github.com/jolick)

---

### Versioning

We used [GitHub](https://github.com/) for versioning. 
