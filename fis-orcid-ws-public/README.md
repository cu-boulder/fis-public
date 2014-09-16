fis-orcid-ws-public
===================

FIS ORCID Web Services based on example client application created by
ORCID. (See copyright notice below.)
Copyright (C) 2011-12 by ORCID

Release 1.0 supports the ORCID register and poll processes. Support for
the existing iD process is not included.

Approach
===================

The FIS Orcid Web Service is a service API providing access to
the ORCID registry.

The approach to the design is based on the following assumptions:

   1. Bulk registration of CU-Boulder faculty in ORCID
   2. On-going, periodic polling of the ORCID registry to update
      status of ORCIDs in the FIS cache.
   3. Possible future transfer to UIS for ongoing operations.

Given these assumptions, the application uses a Representational State Transfer
(REST) design approach rather than the activity-oriented Simple Object Access
Protocol (SOAP) approach.

In addition, the application offers a Data Access Object (DAO) architecture for
direct access to FIS packages encapsulating business logic used in the
registration and polling processes associated with ORCID.

Architecture
===================

FIS Orcid Web Service consists of several processing layers on the server side,
each with distinct responsibilities.

A. Tomcat - J2EE servlet container - listens on HTTPS for incoming requests

B. FIS-ORCID-WS WAR File - Web service Java application - Processes web service
request, parses XML request body, passes request to Thin Java Layer. Uses
Spring MVC implementing REST web service and and connection pooling

C. Java Layer - Application logic to marshal requests to PL/SQL layer -
Provides basic parameter validation (required parameters, bounds, data type),
open connection to PL/SQL layer and calls PL/SQL stored procedures

D. PL/SQL - Business logic and data access - Provides business validation rules,
queries database and inserts and updates data

E. Database - Persists data - Maintains data integrity and offers access to data. Custom views were created against our HR data to filter users based on our selection criteria. All access to our internal tables and views are strictly against dedicated tables and views created for ORCID. This allows us flexibility to change ORCID selection criteria without having to update any of our existing tables or any java ORCID client code.

Responses generated from SQL queries are returned from the database back up
the stack listed above.

To address availability and load requirements, the FIS-ORCID-WS is designed to be
accessed by only an administrative control panel or via a scheduled process (for
polling)

APEX Screens
===================

Screen captures for the ORACLE APEX application used to support back office
tasks associated with the ORCID application can be found here in the screens
directory.


License -- inherited from the ORCID example code and also used by the University of Colorado Boulder
==============================================================================================

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
