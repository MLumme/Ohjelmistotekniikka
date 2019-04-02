Current status
==============
Currently software should be capable of loading parameters for simulated objects from input file, and run simulation with given timesteps and total simulation time, printing out parameters from file, and parameters after completing integration.

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


