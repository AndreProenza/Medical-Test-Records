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


## Introdução em Português

As instituições de saúde recolhem e armazenam informação sensível dos doentes. Os sistemas de informação devem permitir o acesso fino e contextualizado aos registos ao pessoal relevante, como Médicos e Enfermeiros. Outro pessoal com diferentes necessidades de acesso aos dados inclui: assistentes clínicos (cuidam da limpeza da enfermaria), assistentes de serviços aos doentes (trazem refeições e bebidas), carregadores (cuidam do levantamento e transporte dos doentes), voluntários (ajudam na angariação de fundos e visitas à enfermaria), e funcionários da enfermaria (pessoal das mesas de recepção da enfermaria).

Um dos tipos de dados relevantes armazenados são os resultados dos testes. Alguns dos testes médicos podem ser realizados dentro de um laboratório hospitalar, mas em muitos casos, os testes são realizados em laboratórios parceiros, que têm uma infra-estrutura distinta, distante da infra-estrutura do hospital, que precisa de estar interligada. Além disso, os dados dos testes médicos têm de ser arquivados de forma a que a sua autenticidade possa ser verificada mais tarde, se necessário.

É obrigatório que a solução externalize a política de segurança com uma linguagem de política, como a XACML, com pontos de autoria e aplicação de políticas separados, e diferentes partes do registo do paciente podem ser acedidas por pessoas diferentes e em contextos diferentes. Para demonstrar a vantagem desta separação, a solução deve ter um modo normal de funcionamento e um modo pandémico, onde as regras de acesso aos dados são significativamente diferentes. A mudança entre modos deve ser feita com alterações aos documentos da apólice, mas sem alterações ao código da aplicação.

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

### Problem

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

### Plan

### Milestones

Describe basic, intermediate and advanced versions of the work and when are they expected to be achieved.
The basic version is the minimum security functionality.
The intermediate version includes the most important security mechanisms.
The advanced version should address more attacks.

### Effort commitments

Include a Gantt chart describing the planned tasks and how much time is allocated to the implementation
(for example, consider a total of ~180 Person/hour, i.e., 3 weeks for a team of 3 people, each with 50% allocation of a 40-hour work week).
This Gantt chart should illustrate the tasks for the different milestones and the expected conclusion date for each.

### References

Bibliographic references cited in the project proposal.

---
---
---

### Problem

Patient's medical records are extremely sensitive data. The historical records made by doctors
facilitate the process of diagnosing a patient, ensuring their quality, which helps clinical staff to treat
quickly and accordingly.
This data should be kept private, allowing only the discriminating staff to access it. We believe, that
all healthcare facilities should have access to this type of information so that patients can receive
healthcare anywhere and at any time. Therefore, the data should be protected from external agents
(i.e., outside the medical institutions) and from unauthorised people within the institutions.

### Solution Requirements

#### As a user (depending on my privilege) I should be able to:

- Read/write medical records (send requests to the system);
- Receive responses from the system (receive replies from the system).

#### As a Doctor, Nurse I should be able to:

- Read/Update medical records

#### As a Patient service assistants, Porters I should be able to:

- Read patients specific information in the medical records

#### As a Ward clerk I should be able to:

- Register a patient in the system

#### As a Patient service assistants, Volunteers I should be able to:

- Read your medical record

#### As a Patient I should be able to:

- Read my medical record

#### As the system administrator I should be able to:

- Modify/set user privileges.
- Create new personnel records within the institutions

#### Security requirements:

As a system, I must:

- Ensure confidentiality and integrity of medical records;
- Ensure confidentiality and integrity of communications with the web application;
- Ensure that only authorised medical staff and patients have an account;
- Ensure different “roles” have access to different privileges;
- Ensure there is only one account per citizen;
- Prevent access to medical records if the user does not have privileges;
- Allow user A to change the privileges of user B, if user A is a system administrator;
- Ensure successful authentication of users;
- Mitigate brute force attacks on the authentication system (e.g. blocking IP’s);
- Minimize the impact of failures within the system (solution: do an "I'm alive with timeout" to the
backup server);
- Minimize the impact of attacks inside the system.
2
As a user, I cannot repudiate my actions

### Trust assumptions

### Overview

### Deployment

### Secure channel(s) to configure

### Secure protocol(s) to develop

### Plan

### Milestones

### Effort commitments

### References
