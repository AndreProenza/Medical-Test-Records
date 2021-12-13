# Medical-Test-Records

## Introduction

Health care institutions gather and store sensitive information from patients. The information systems should allow fine-grained and contextualized access to the records to the relevant staff, like Doctors and Nurses. Other staff with different data access needs include: clinical assistants (take care of ward housekeeping), patient services assistants (bring meals and drinks), porters (take care of patient lifting and transport), volunteers (help with fundraising and ward visits), and ward clerks (staff the ward reception desks).

One of the relevant types of data stored are test results. Some of the medical tests can be performed inside a hospital lab, but in many cases, tests are done in partner labs, that have a distinct infrastructure, remote from the infrastructure of the hospital, that need to be interconnected. Also, the medical test data has to be archived in a way such that its authenticity can be verified later, if necessary.

It is mandatory that the solution externalizes the security policy with a policy language, like XACML, with separate policy authoring and enforcement points, and different parts of the patient record can be accessed by different people and in different contexts.
To demonstrate the advantage of this separation, the solution should have a Normal mode of operation and a Pandemic mode, where the rules for data access are significantly different. The switch between modes should be done with changes to the policy documents but without changes to the application code.

Suggested references:

- [RFC2753](https://datatracker.ietf.org/doc/html/rfc2753) -- Framework for Policy-based Admission Control
- [XACML](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#other) -- security policy language (there are other languages)
- [XACML](https://github.com/wso2/balana) library -- example XACML implementation (there are other libraries)


---

## [Project Overview](https://github.com/tecnico-sec/Project-Overview-2022_1)

---

## TODO until 17/12/2021

### Write proposal

After having a topic assigned, your group should prepare a proposal document. The project proposal should describe the problem and the proposed solution.

You can bring a draft of the proposal to your lab session or office hours to present it and receive feedback.

### Technical core requirements

The planned project will need to have, at least:

a set of separate (virtual) machines, with network isolation;
a secure communication tunnel (e.g. TLS, SSH) using correct configuration;
the design and deployment of one mechanism using a custom security protocol.

### Document requirements

PDF format;
Mandatory file name CXX_WWW_HHMM_L_proposal.pdf (where C is A for Alameda, T for Tagus, XX is the Fenix group number with two digits, WWW is the weekday of the lab shift – Mon, Tue, Wed, Thu, Fri – HHMM is the time – Hours and Minutes – and L is the lab room number);
Report cover: Project title. Headed by course name, group campus, group number. In the next row: group members sorted by ascending student number. For each student, include the number, name and professional photo with face clearly visible;
Report body: The font should be no smaller than 11pt, with standard line and character spacing;
Limit of 4 pages (excluding cover);
Pages should be numbered (preferably with a label like Page X of Y);
The use of UML diagrams (or other standard notations) is recommended for clear and concise communication.

### Document structure (mandatory)

Problem
Given the chosen scenario, where is security necessary?
What is the main problem being solved?
Use around 200 words.

### Solution Requirements

Which security requirements were identified for the solution?
Present as a list. Identify each requirement with R1, R2, ...

### Trust assumptions

Who will be fully trusted, partially trusted, or untrusted?
Write down the trust relationships to make them explicit.
Proposed solution

### Overview

What are the main components of the solution? How do they relate?
Diagram and explanation with, at most, 200 words.
If you do not use UML or another standard notation, include a legend.

### Deployment

Describe distinct machines and how they will be interconnected.

### Secure channel(s) to configure

Who will communicate?
Identify communication entities.
What existing security protocol will be used?
Choose existing TLS or SSH library/tool to use.
What keys will exist and how will they be distributed?

### Secure protocol(s) to develop

Who will communicate?
Identify communication entities and the messages they exchange with a sequence or collaboration diagram.
Which security properties will be protected?
Identify the security properties to ensure.
What keys will exist and how will they be distributed?
Considered technologies
Succinctly describe the technologies that are being considered, e.g., programming languages, frameworks, libraries, tools, etc.
Plan
4.1. Milestones
Describe basic, intermediate and advanced versions of the work and when are they expected to be achieved.
The basic version is the minimum security functionality.
The intermediate version includes the most important security mechanisms.
The advanced version should address more attacks.
4.2. Effort commitments
Include a Gantt chart describing the planned tasks and how much time is allocated to the implementation
(for example, consider a total of ~180 Person/hour, i.e., 3 weeks for a team of 3 people, each with 50% allocation of a 40-hour work week).
This Gantt chart should illustrate the tasks for the different milestones and the expected conclusion date for each.
References
Bibliographic references cited in the project proposal.
