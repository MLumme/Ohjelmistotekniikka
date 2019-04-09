Current status
==============
If nothing has broken horribly when run, software should open a GUI, where one needs to load in datafile by using Load Data from File-menu in top bar, rest of the options are just placeholders, which should open a file explorer screen, and if loading was succesfull parameters are shown in the center of the GUI.

 Additionally total time of simulation and timestep (dT) must be inserted into the bottom bar and submitted by pressing the sublit button, after which integration can be run by pressing the imaginatively named run-button. after the integration is finished table should update to show the resulting parameters for bodies after integration.

Note that file structure is not nearly finnished, UI.java will be split into smaller components when it works as a whole, and FileHandler.java and the future files concerned with file I/O and formatting will probably be moved into their own folder. 

Input File
==========
Input rows should be as follows:

GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0
 
Here GM is the standard gravitational parameter in km^3/s^2, x,y,z:s are the components of the bodys location vector in km, and vx,vy,vz:are the components of velocity vector in km/s.

Currently, there is an file for testing, Test_Input.dat, with parameters for Sun and Earth in folder GravitationalIntegrator
for running the program.

Documentation
=============
[Software requirements specification](../master/Harjoitustyö/Documentation/requirements_specification.md)

[Record of work hours](../master/Harjoitustyö/Documentation/record_of_work_hours.md)


