Program function
================
This programs function is to integrate orbital motion for a set of objects using leapfrog-algorithm, and provide the user with possibility to continue formerly run integrations by loading old output files.

Input File
==========
Input rows shall be as follows:

GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0
 
Here GM is the standard gravitational parameter in km^3/s^2, x,y,z:s are the components of the body’s location vector in km, and vx,vy,vz:are the components of velocity vector in km/s.

Currently, there is an file for testing, Test_Input.dat, with parameters for Sun and the four gas giants in folder GravitationalIntegrator for running the program.

Output File
===========
Output file follows the following structure

nSteps nBodies<br>
T1 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1 ...<br>
T2 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1 ...<br>
T3 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1 ...<br>
...

and so on for the rest of the objects and timesteps, time is in seconds, rest same as in input file. nSteps is the number of timesteps stored in file, and nBodies is the number of simulated objects. timeX is the simulation time at current timestep, and the rest are the gravitational parameters and phase-space coordinates for the objects.
 
Command Line
============

Testing, test coverage and checkstyle
-------------------------------------

Tests can be run with: 
```
mvn test
```
Checkstyle, which ignores contents of ui-package, can be run with
```
mvn jxr:jxr checkstyle:checkstyle
```
with report being generated in ``target/site/checkstyle.html``

Test coverage, which also ignores contents of ui-package, with jacoco can be run with
```
mvn jacoco:report
```
with report being generated in folder ``target/site/jacoco/index.html``

JavaDoc
-------
JavaDoc can be run with
```
mvn jacoco:report
```
with report being generated in folder ``target/site/apidocs/index.html``

Packaging
---------

Packaging can be run with 
```
mvn package
```
producing a ``.jar`` in in the ``target/``-folder with the name of ``GravitationalIntegrator-1.0-SNAPSHOT.jar``

Running
-------

``.jar`` can be run with
```
java -jar GravitationalIntegrator
```

Current Release Version
=======================
[Current release](https://github.com/MLumme/Ohjelmistotekniikka/releases/tag/loppupalautus)

Documentation
=============
[Software requirements specification](../master/Course_Project/Documentation/requirements_specification.md)

[User manual](../master/Course_Project/Documentation/manual.md)

[Record of work hours](../master/Course_Project/Documentation/record_of_work_hours.md)

[Architectural document](../master/Course_Project/Documentation/architecture.md)

[Testing document](../master/Course_Project/Documentation/testing.md)
