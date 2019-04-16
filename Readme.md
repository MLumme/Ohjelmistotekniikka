Current status
==============
If nothing has broken horribly when run, software should open a GUI, where one needs to load in datafile by using Load Data from File-menu in top bar.

 Additionally total time of simulation and timestep (dT) must be inserted into the bottom bar and submitted by pressing the sublit button, after which integration can be run by pressing the imaginatively named run-button. after the integration is finished table should update to show the resulting parameters for bodies after integration.

If needed, one can save the simultaion by selectin Save Simulation from File-menu, see section Output File for structure. 

Integration has been separated into its own thread from the ui, so while it is running GUI shoud remain responsive, althought it will block any changes to parameters, but the idea is to show that the program is still  and doing something and has not crashed. 

Input File
==========
Input rows should be as follows:

GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0
 
Here GM is the standard gravitational parameter in km^3/s^2, x,y,z:s are the components of the bodys location vector in km, and vx,vy,vz:are the components of velocity vector in km/s.

Currently, there is an file for testing, Test_Input.dat, with parameters for Sun and the four gas giants in folder GravitationalIntegrator for running the program.

Output File
===========
Output file follows the following structure

time1<br/>
GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0<br/>
GM_0 x_1 y_1 z_1 vx_1 vy_1 vz_1<br/>
GM_2 x_2 y_2 z_2 vx_2 vy_2 vz_2<br/>
...<br/>
time2<br/>
GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0<br/>
GM_0 x_1 y_1 z_1 vx_1 vy_1 vz_1<br/>
GM_2 x_2 y_2 z_2 vx_2 vy_2 vz_2<br/>
...<br/>
time3<br/>
GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0<br/>
GM_0 x_1 y_1 z_1 vx_1 vy_1 vz_1<br/>
GM_2 x_2 y_2 z_2 vx_2 vy_2 vz_2<br/>
...

and so on for the rest of the objects and then timesteps, time is in seconds, rest same as in input file
 
Command Line
============
Tests can be run with: 
```
mvn test
```

Checkstyle can be run with
```
mvn jxr:jxr checkstyle:checkstyle
```

Test coverage with jacoco can be run with
```
mvn jacoco:report
```

Packaging can be run with 
```
mvn package
```

.jar can be run with
```
java -jar GravitationalIntegrator
```

Current Release Version
=======================
[Current release](https://github.com/MLumme/Ohjelmistotekniikka/releases/tag/Week5)

Documentation
=============
[Software requirements specification](../master/Course_Project/Documentation/requirements_specification.md)

[Record of work hours](../master/Course_Project/Documentation/record_of_work_hours.md)

[Architectural diagram and sequence charts](../master/Course_Project/Documentation/architecture.md)


