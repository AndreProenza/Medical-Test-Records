# Medical-Test-Records

<img src="https://user-images.githubusercontent.com/78174997/151461968-53e7c3a0-c20c-4043-870a-62e9eea75f1e.jpg" width="700">

---

## Problem

- Health care institutions gather and store sensitive information from patients such as medical test records.
- Patients can not always access their medical records in a secure manner
- Information systems do not always ensure fine-grained and contextualized access to medical records for relevant staff.
- Lack of connection and access to medical records of different partner institutions, with different infrastructures.

---

## Solution

- Provide a secure system to allow safe and confidential access to patient's medical records
- Ensure authentication and access control to certain resources for authorized personnel
- Ensure secure interconnection (sending and receiving medical records) between partner health organizations

---

### Built With

* [Java](https://openjdk.java.net/) - Programming Language
* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [Spring Boot](https://spring.io/projects/spring-boot) - Create Java stand-alone Spring applications
* [Spring Boot Security](https://spring.io/projects/spring-security) - A highly customizable authentication and access-control framework
* [Mongo DB Atlas](https://www.mongodb.com/) - Cloud Database
* [Ubuntu Server](https://ubuntu.com/download/server) - Virtual Machines to run the Servers
* [UFW - Uncomplicated Firewall](https://www.linux.com/training-tutorials/introduction-uncomplicated-firewall-ufw/) - Firewall

---

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

---

### Prerequisites

What kind of **hardware** device and which **operating system** do you need to have to install the software.

In this section also include detailed instructions for installing additiona software the application is dependent upon (such as PostgreSQL database, for example). 

```
Give installation command examples
```

---

### Installing

Give step-by-step instructions on building and running the application on the development environment. 

Describe the step.

```
Give the command example
```

And repeat.

```
until finished
```

You can also add screenshots to show expected results, when relevant.

---

## System Features

-	Ensures confidentiality and integrity of medical records;
-	Ensures confidentiality and integrity of communications with the web browser;
-	Ensure successful authentication of citizens;
-	Authenticates citizens in a secure way
-	Ability to change password if citizen is authenticated
-	Authenticated user credentials confirmation sent to email
-	Ensures that only authorized staff and patients have an account;
-	Ensures different “roles” have access to different privileges;
-	Ensures there is only one account per citizen, using citizen ID card number.
-	Prevents access to medical records if the citizen does not have privileges;
-	Allows user A to change the privileges of user B, if user A is a system administrator;
-	Validates and sanitize form input
-	Uses HTTPS to encrypt communications
-	Stores and manage symmetric and asymmetric keys
-	Defines a restricted set of rules on the firewall
-	Establishes mutual authentication and shares medical records with partner institutions
-	Minimizes the impact of attacks inside the system.


---

## Deployment

Add additional notes about how to deploy this on a live system e.g. a host or a cloud provider.

Mention virtualization/container tools and commands.

```
Give an example command
```

Provide instructions for connecting to servers and tell clients how to obtain necessary permissions.

---

## Additional Information

### Authors

* **Ana Albuquerque** - ** ist1102209 ** - [GitHub](https://github.com/albuana)
* **André Proença** - ** ist1102327 ** - [GitHub](https://github.com/AndreProenza)
* **Joel Russo** -  ** ist1102098 ** - [GitHub](https://github.com/jolick)

---

### Versioning

We used [GitHub](https://github.com/) for versioning. 
