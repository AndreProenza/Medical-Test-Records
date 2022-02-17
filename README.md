# Medical-Test-Records

A secure healthcare system and network, which aims to interconnect different healthcare institutions and provide patients with secure and confidential access to medical records from different organizations.

![med](https://user-images.githubusercontent.com/78174997/154372457-0c44d5fa-cf98-45ba-8437-8ac0920896a0.jpg)

---

# Table of Contents
1. [Introduction](#introduction)
2. [Problem](#problem)
3. [Solution](#solution)
4. [System Features](#system-features)
5. [Architecture](#architecture)
6. [Network Overview](#network-overview)
7. [Secure channels](#secure-channels-configured)
8. [Secure custom protocol](#secure-custom-protocol-developed)
9. [Used Technologies](#used-technologies)
10. [References](#references)
11. [Getting Started](#getting-started)
12. [Prerequisites](#prerequisites)
13. [Setup Virtual Machines](#setup-virtual-machines)
14. [Setup Locally on your machine](#setup-locally-on-your-machine)
15. [Additional Information](#additional-information)
16. [Authors](#authors)

---

## Video live system (Setup - virtual machines)

https://user-images.githubusercontent.com/78174997/154493862-8d8dc3a7-d7fb-4ae2-a46c-24f538e21382.mp4

---

## Introduction

Health care institutions gather and store sensitive information from patients. The information systems should allow fine-
grained and contextualized access to the records to the relevant staff.

One of the relevant types of data stored are test results. Some of the medical tests can be performed inside a hospital lab,
but in many cases, tests are done in partner labs, that have a distinct infrastructure, remote from the infrastructure of the
hospital, that need to be interconnected. In addition, the privacy clause is a key issue for safe and successful access to
patient health information. Current approaches do not always provide patients with the ability to establish appropriate rules
for accessing their information in a secure manner.

A patient's medical records are extremely sensitive data. The historical records made by doctors facilitate the process of
diagnosing a patient, ensuring their quality, which helps clinical staff to treat quickly and accordingly.
This data should be kept private, allowing only the discriminating staff to access it. We believe that all healthcare facilities
should have access to this type of information so that patients can receive healthcare anywhere and at any time. Therefore,
the data should be protected from external agents (i.e., outside the medical institutions) and from unauthorized people within
the institutions.

This project aims to demonstrate a secure system that allows secure and confidential access to patient medical records of
certain healthcare organizations. It also demonstrates the secure interconnection (sending and receiving of medical records)
of partner healthcare organizations. In such manner, a patient will be able to access his/her medical records in every
healthcare organization where he/she is registered.

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


## Architecture

In order to simulate real systems and their interconnection, our solution is based on the development of two systems representing
healthcare institutions. A hospital and a partner laboratory. The goal is to have two completely independent and functional
healthcare institutions, each with its own data storage system and independent web platform, and simultaneously simulate the
sending of confidential patient’s medical records in a secure way from one institution to the other. Authorized hospital and
laboratory staff as well as patients will be able to access their respective hospital and laboratory remotely or locally.

![Medical Test Records (6)](https://user-images.githubusercontent.com/78174997/154532620-4d308740-545f-4656-bf80-88dccad63ed5.jpg)

---

## Spring Security, Security policy language

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard
for securing Spring-based applications.
Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. It has
features like Comprehensive and extensible support for both Authentication and Authorization. Protection against attacks like
session fixation, clickjacking, cross site request forgery, etc...

![Medical Test Records (7)](https://user-images.githubusercontent.com/78174997/154533159-d270aa5b-0b6e-455d-8b09-112c645ab9ca.jpg)

---

## Network Overview

![Medical Test Records (3)](https://user-images.githubusercontent.com/78174997/154372192-a75d32e6-d44c-4708-9c72-c8df2cf39d80.jpg)

As you can see, 7 virtual machines running Ubuntu Server were deployed.

- 1 for the router that will simulate the internet;
- 1 for Hospital Server, which will receive the clinical records from Partner Lab Backend;
- 2 for the hospital and laboratory respective frontends;
- 2 for the hospital and laboratory respective backends;
- 1 for local or remote users which will access the frontend servers. To change the location of users on the network, just change the properties of the VM in the hypervisor (VirtualBox, for example). This way we avoid creating multiple VM's for each local and remote user.

For the configuration of the each vm, the hostname, host, credentials, network were configured. For each machine in the
laboratory and hospital networks, a static ip was set. The patient (Remote user) is connected via DHCP to the NAT Network.
For the router, there were added the hospital network, laboratory network and NAT adapters, so that the communication could
flow between the three interfaces.

---

## Secure channels configured

#### Who will be communicating?

A user accesses any of the health institution platforms through the HTTPS protocol, thus encrypting the communication and
making it secure. Each of the health institutions has a frontend that authenticates a user according to his/her "role",
processes, validates, sanitizes form data and makes requests to the api of the respective backend institution to authenticate
users, or to fetch a user or a medical/clinical specific information, which then makes a request to the respective database.
The connections to the databases, as well as the connections from frontends to backends, use the HTTPS protocol, thus
ensuring content integrity and confidentiality

#### SSL certificates

By default, the backends connect to their databases via HTTPS. SSL is configured in the backends application.properties.
For the frontends and backends, self-signed certificates were created with the help of the keytool. To establish the HTTPS
connection were also configured the application.properties files in the frontends and backends of both institutions.

---

## Secure custom protocol developed

### Who is communicating?

- Each time a partner Laboratory responsible creates a clinical record, the record is automatically sent to the hospital in a secure and confidential way. To establish this connection, it is necessary to ensure a mutual authentication by both the laboratory and the hospital server;

- When the connection is first requested, the hospital server and the laboratory must both authenticate each other and exchange a symmetric key in order to encrypt the messages that will be sent;

- After the exchange of the symmetric key, the laboratory makes a request to the hospital server, asking if the patient exists in the hospital's database. If the patient exists, the clinical record will be sent and then registered in the hospital's database.


### Which keys will exist and how will they be distributed?

- Each Health Institution is a certifying entity that issues its own certificate. Each of these institutions have an asymmetric key pair, stored in a keystore, which will be used to communicate and establish secure connections and in a truststore the public key of the other service, this is, the hospital backend server has the public key of the laboratory backend. Symmetric keys are generated when establishing a connection between laboratory backend server and hospital server;

- When the connection is first requested by the laboratory, the hospital server sends its signed certificate, so the laboratory can verify its legitimacy. The second step is laboratory is to verify the server legitimacy, in order to do this, the server sends a random string to the laboratory, so that the laboratory can sign that string with its private key and the server can verify the laboratory authenticity by decrypting the same random string using the laboratory public key;

- If the authentication is successful, the laboratory creates a symmetric key that is sent to the laboratory, encrypted with its
public key. This key is generated so that the communication is done without overexposing the private keys;

- The symmetric key is temporary and is changed for every medical record that is sent to the hospital server.


![Medical Test Records (4)](https://user-images.githubusercontent.com/78174997/154372352-04c97e5d-1998-4a44-bf53-3f87971d095e.jpg)


![Medical Test Records (5)](https://user-images.githubusercontent.com/78174997/154372373-9108cc1b-9ba2-45a0-b81b-ca8f99ba9e49.jpg)


### Security properties ensured

- Integrity
- Freshness
- Confidentiality

---

## Used Technologies

* [Java](https://openjdk.java.net/) - Programming Language;
* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management;
* [Spring Boot](https://spring.io/projects/spring-boot) - Create Java stand-alone Spring applications;
* [Spring Boot Security](https://spring.io/projects/spring-security) - A highly customizable authentication and access-control framework;
* [Mongo DB Atlas](https://www.mongodb.com/) - Cloud Database;
* [Ubuntu Server](https://ubuntu.com/download/server) - Virtual Machines to run the Servers;
* [UFW - Uncomplicated Firewall](https://www.linux.com/training-tutorials/introduction-uncomplicated-firewall-ufw/) - Firewall.

---

## References

- [Spring Quickstart Guide. (2022). Spring.](https://spring.io/quickstart)
- [Building REST services with Spring. (2022). Spring.](https://spring.io/guides/tutorials/rest/)
- [Securing a Web Application. (2022). Spring.](https://spring.io/guides/gs/securing-web/)
- [Understand Spring Security Architecture and implement Spring Boot Security | JavaInUse. (2022).](https://www.javainuse.com/webseries/spring-security-jwt/chap3)
- [RestTemplate (Spring Framework 5.3.15 API). (2022). Spring.](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html)
- [Validating Form Input. (2022). Spring.](https://spring.io/guides/gs/validating-form-input/)
- [KeyStore (Java Platform SE 7). (2020, June 24). Oracle.](https://docs.oracle.com/javase/7/docs/api/java/security/KeyStore.html)
- [To Use keytool to Create a Server Certificate (The Java EE 6 Tutorial). (2022). Oracle.](https://docs.oracle.com/cd/E19798-01/821-1841/gjrgy/)
- [Krout, E. (2021, August 20). How to Configure a Firewall with UFW. Linode Guides & Tutorials.](https://www.linode.com/docs/guides/configure-firewall-with-ufw/)
- [Get Server | Download. (2022). Ubuntu.](https://ubuntu.com/download/server)
- [Tutorial: Thymeleaf + Spring. (2018). Thymeleaf.](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)
- [To Use keytool to Create a Server Certificate (The Java EE 6 Tutorial). (2010). Docs.Oracle.Com.](https://docs.oracle.com/cd/E19798-01/821-1841/gjrgy/)

---

## Getting Started

The following instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See Additional Information on the bottom of this document to see additional information.

---

## Prerequisites

The project can be executed in two ways:
- Using the virtual machines we provide
- Locally on your machine.

If testing via virtual machines:

- VirtualBox must be installed on your machine. [Install here VirtualBox](https://www.virtualbox.org/wiki/Downloads)

if testing locally on your machine, you can run the jar files. But first you need to install the following:
- [Install here java 17](https://jdk.java.net/17/) (*)
- [Install here Maven](https://maven.apache.org/download.cgi) and select the version "apache-maven-3.8.4-bin.tar.gz",

(*) for Macbook M1 select "macOS/AArch64"

---

## Setup Virtual Machines

To prepare the environment it is necessary to import the vms. 
Please donwload them in following links:



With that done we can procede to import the virutal machines (vms).
In order to import the vms you should go to:

- **Import**, Choose the correct .ova file and import it into virtualbox. 

Import all of them and start them in the green button `start`.

**Note**
You don't need to login in each vm, you just need to start them in the `start` green button in virtual box.
If you want to explore each virtual machine in detail, you can login with the following credentials:

The **credentials for each vm** are the the same as the **hostname**, this is, if hostname is hosf, then **username** is hosf and **password** is hosf. 
The **root crentials** are root:root

| Username          | Password                            
|:-----------------:|:-------------------
| hosf              | hosf  
| hosb              | hosb  
| labf              | labb  
| hosbs             | hosbs  
| patient           | patient  

### To access the frontend for both laboratory and hospital, the virutal machine `patient` should be started. 

Patient Virtual machine credentials:

| Username          | Password                            
|:-----------------:|:-------------------
| patient           | patient          

After logging in, open a browser and it is possible to see two bookmarks:

For hospital:
```
https://10.0.0.4:8443/
```
For laboratory
```
https://10.0.0.104:8443/
```

Click on any of the bookmarks, and you will be automatically redirected to the hospital and laboratory web pages, respectively. From here you can explore both websites.


#### VM's List of IP's

- Hospital Frontend 10.0.0.4
- Hospital Backend 10.0.0.5
- Laboratory Frontend 10.0.0.104
- Laboratory Backend 10.0.0.105
- Hospital Backend Server 10.0.0.6

---

## Setup Locally on your machine

First of all, download the ready to execute folder: [Medical-Test-Records](https://drive.google.com/drive/folders/1UpfKsnA9aQQDwUPoirttpSWe_TlspJwn?usp=sharing)
then, install maven and java. Let's install them

## Maven Instalation

We're gonna use maven. The folder is inclued in our project, so ignore this step. 

But there are the instructions:

Installing **maven**, compatible with java 17:

0) Please click [here](https://maven.apache.org/download.cgi) to download maven;

1) Download Binary tar.gz archive: ```apache-maven-3.8.4-bin.tar.gz```;

2) Unpack the archive with tar/unzip;

---

## Java 17 Instalation

#### LINUX
---

To run the code in a local linux system java 17 and maven must be installed.

Installation of **java 17**:

```
sudo apt install openjdk-17-jre
```
```
sudo apt install openjdk-17-jdk
```

Change java version to the newer one:
```
sudo update-alternatives --config java
```

## 

#### MAC OS
---
In order to make the correct installation of **java 17** follow the following instructions:

0) Download java 17 from [here](https://jdk.java.net/17/) ```NOTE: for Macbook M1 select "macOS/AArch64"```;
1) Now go to your Downloads ```cd Downloads/```;
2) Make ```sudo mv openjdk-17.0.2_macos-aarch64_bin.tar.gz /Library/Java/JavaVirtualMachines/```;
3) ```cd /Library/Java/JavaVirtualMachines/```;
4) ```sudo tar -xzf openjdk-17.0.2_macos-aarch64_bin.tar.gz```;
5) ```sudo rm openjdk-17.0.2_macos-aarch64_bin.tar.gz```;
6) ```export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.0.2.jdk/Contents/Home```.

## 

After the previous installation is complete in your OS, please read the following notes **before** running.

#### ❗️ Notes about running the application ❗️ Mandatory reading ❗️

If you are testing in your local machine, **Do not run all jars at the same time**.
Please note, Hospital and Laboratory frontends are running on port 8443, and their respective backends are also running in port 3000. Due to this, it is **impossible** to run both Hospital and Laboratory at the same time. It is still possible to run Hospital Backened server, that is running on port 4000, in order to test the custom protocol.

But if you deploy the project in the vms, you can run all at the same time (there is no issue with localhost used ports).

Summary:
+ Hospital Frontend: port 8443
+ Hospital Backened: port 3000
+ Laboratory Frontend: port 8443
+ Laboratory Backened: port 3000
+ Hospital Backend Server: port 4000

--- 

### It's time to run the project

##

<h3 align="center"> 
	To run the LABORATORY
</h3>

##

Go to ```Medical-Test-Records/jars``` directory and run: 

```
java -jar Laboratory-Frontend.jar
```

Go to ```Medical-Test-Records/Code/Laboratory-Backend``` directory and run:
```
java -jar Laboratory-Backend.jar
```

Go to ```Medical-Test-Records/Code/Hospital-Backend-Server``` directory and run:

```
apache-maven-3.8.4/bin/mvn spring-boot:run
```

Now open your browser in ```https://localhost:8443/``` and enjoy our laboratory system.

##

<h3 align="center"> 
	To run the HOSPITAL
</h3>

##

Go to ```Medical-Test-Records/jars```directory and run:

```
java -jar Hospital-Frontend.jar
```

```
java -jar Hospital-Backend.jar
```

Now open your browser in ```https://localhost:8443/``` and enjoy our hospital system.

--- 
### Citizen card ID and passwords

The credentials to access the hospital and laboratory can be found below.

##

##### ✔︎ HOSPITAL

##### ❗️Note: You can not register in the web site. Only Admin can register personnel. Only Doctor/Nurse can create Medical Records. Only Ward Clerks can register Patients.

---
| Admin             | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| André Proenza     | 12345678              | Password123!

| Doctor            | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Ana Albuquerque   | 19573526              | Fl4%8!Hd10k4Zc*!

| Ward Clerk        | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Hélder Costa      | 15378965              | V322!!P25KM4&f6b

| Patient           | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Amanda Júlio      | 17645234              | pB0K!*vF85!0&@60

| Porter            | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Henrique Jota     | 17564920              | 7u$%4n&B90%8U!6!

| Volunteer         | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Madalena Afonso   | 17564532              | ```*Mj0T**p*?Z!yv0u```

| Patient_Assistant | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Alexandre Pinto   | 14789078              | I&u5lhmt*nO$f?!1

|Clinical_Assistant | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Mariana Rita      | 18509738              | p$h4M$*x*cHk10q@

##

##### ✔︎ LABORATORY

##### ❗️Note:  You can not register in the web site. Only Admin can register personnel. Only Responsible can create Clinical Records and register Patients.

---
| Admin             | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| André Proenza     | 12345678              | Password123!

| Responsible       | Citizen Card ID       | Password                            
|:-----------------:|:---------------------:|:------------
| Paulo Marques     | 15288625              | y20lH%7pk*1@h0Ou

---

## Additional Information

For deploymend on a live system you should have access to a mongodb database account and a suficient ammount of ram.

For virtualization any hyphervisor should suffice, you just need to be able to create isolated internal networks and and have
access to the internet.

For a more isolated control of any possible vulnerabilities, the use of docker could add an extra layer of protection against attacks to the 
network, this is, it creates an extra difficulty to obtain higher/relevant credentials on the computar that manages the docker/s.

Regarding the system, there is a necessity to create the first admin manually, since the option of creating new user/admins is not available. This is a great advantage in terms of security, but can become a problem in usability, even if it must only be done once.

The Hospital Backend server is able to accept multiple clients (via threads) therefore it possible to easily escalate the system, create multiple laboratories.

Regarding vms and networking, although we had a solution that forwarded all packets from both interfaces via a router, the vms had no internet access, therefore we had to remove the router and put every vm in the same network.

---

## Authors

* **Ana Albuquerque** - [GitHub](https://github.com/albuana)
* **André Proença** - [GitHub](https://github.com/AndreProenza)
* **Joel Russo** - [GitHub](https://github.com/jolick)

---
