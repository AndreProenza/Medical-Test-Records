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
