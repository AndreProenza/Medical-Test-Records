# Medical-Test-Records

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

![image](https://user-images.githubusercontent.com/78174997/146569869-6563902a-f73b-4118-8c51-d9eac52f1a04.png)


## Spring Security, Security policy language

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard
for securing Spring-based applications.
Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. It has
features like Comprehensive and extensible support for both Authentication and Authorization. Protection against attacks like
session fixation, clickjacking, cross site request forgery, etc...

![image](https://user-images.githubusercontent.com/78174997/146477605-f48aa3c2-85bd-4c03-9e40-32d21c4d4ade.png)


---

### Deployment

As you can see, 6 virtual machines running Ubuntu Server and the router running Seed Ubuntu are deployed.

- 1 for the router that will simulate the internet;
- 1 for Hospital Server, which will receive the clinical records from Partner Lab Backend;
- 2 for the hospital and laboratory respective frontends;
- 2 for the hospital and laboratory respective backends;
- 1 for local or remote users which will access the frontend servers. To change the location of users on the network, just change the properties of the VM in the hypervisor (VirtualBox, for example). This way we avoid creating multiple VM's for each local and remote user.

For the configuration of the each vm, the hostname, host, credentials, network were configured. For each machine in the
laboratory and hospital networks, a static ip was set. The patient is connected via DHCP to the NAT Network.
For the router, there were added the hospital network, laboratory network and NAT adapters, so that the communication could
flow between the three interfaces.

![image](https://user-images.githubusercontent.com/78174997/146569662-0b11f253-7375-4a5e-a726-84b5bc438af3.png)

---

### Secure channels configured

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

### Secure custom protocol developed

#### Who is communicating?

- Each time a partner Laboratory responsible creates a clinical record, the record is automatically sent to the hospital in a secure and confidential way. To establish this connection, it is necessary to ensure a mutual authentication by both the laboratory and the hospital server;

- When the connection is first requested, the hospital server and the laboratory must both authenticate each other and exchange a symmetric key in order to encrypt the messages that will be sent;

- After the exchange of the symmetric key, the laboratory makes a request to the hospital server, asking if the patient exists in the hospital's database. If the patient exists, the clinical record will be sent and then registered in the hospital's database.


#### Which keys will exist and how will they be distributed?

- Each Health Institution is a certifying entity that issues its own certificate. Each of these institutions have an asymmetric key pair, stored in a keystore, which will be used to communicate and establish secure connections and in a truststore the public key of the other service, this is, the hospital backend server has the public key of the laboratory backend. Symmetric keys are generated when establishing a connection between laboratory backend server and hospital server;

- When the connection is first requested by the laboratory, the hospital server sends its signed certificate, so the laboratory can verify its legitimacy. The second step is laboratory is to verify the server legitimacy, in order to do this, the server sends a random string to the laboratory, so that the laboratory can sign that string with its private key and the server can verify the laboratory authenticity by decrypting the same random string using the laboratory public key;

- If the authentication is successful, the laboratory creates a symmetric key that is sent to the laboratory, encrypted with its
public key. This key is generated so that the communication is done without overexposing the private keys;

- The symmetric key is temporary and is changed for every medical record that is sent to the hospital server.

#### Security properties ensured

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

## Plan

### Milestones

#### Basic version
- Design system, set up the login method in Health Institutions frontend servers, set up and configure authentication using Spring Boot Security framework.

#### Intermediate Version
- Ensure secure communication via TLS, Use asymmetric cryptography for communication establishment, custom protocol development, encrypt server databases

#### Advanced Version
- Backup servers, database replication,  fault tolerance.

---

## References

- [AuthzForce (Community Edition)](https://authzforce.ow2.org/#)
