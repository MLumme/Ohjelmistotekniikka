Structure
=========

Structurally the program is divided in three packages according to their function as depicted in the following diagram:

![Architectural diagram](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/pkg.png)

here gravitationalintegrator.ui contains the UI-class running the user interface, gravitationalintegrator.domain contains the classes necessary for integration, and gravitationalintegrator.io contains the class for reading and writing files.

User Interface
==============
The user interface provides six functions, which are loading system parameters, loading sets of integration steps, and saving steps, setting timestep and total simulation runtime sizes, running the integration, and finally showing the state of the system at currently last timestep on a table.

for most functions the UI only calls IntegratorHandler-object in domain, which stores and modifies most integration-adjacent variables, with the exception of integration function itself, where domain builds the Task-implementing object, and UI sets its operations on successful completion, and the calls domain to run the Task.

Domain
======
Domains Class-relations, or at least the more permanent relations, and its relationships with the other packages, are depicted in the following UML-diagram.

![Architectural diagram](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/uml_pkg.png)

The domains functions are controlled by the IntegratorHandler-object, which provides UI with methods which controls integration parameters, starting integration, saving and loading data, and access to parameters of current state of integrated system's bodies. Saving and loading is completed by calling methods in FileHandler-object in io-package, while the rest use classes in domain-package.

Input/Output
============

The package contains only single class, FileHandler, which provides IntegratorHandler with three methods, first for loading gravitational and location-velocity phase-space-parameters from file. Second, saving integration steps with time from start of integration and same set gravitational and phase-space parameters, and finally to load saved files. File format tested is plaintext .dat.

File structure of input file for parameters
-------------------------------------------
Input rows shall be as follows:

GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0

Here GM is the standard gravitational parameter in km^3/s^2, x,y,z:s are the components of the body's location vector in km, and vx,vy,vz:are the components of velocity vector in km/s.

File structure of saved output
------------------------------
nSteps nBodies<br>
time1 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1 ...<br>
time2 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1 ...<br>
time3 GM_0 x_0 y_0 z_0 vx_0 vy_0 vz_0 GM_1 x_1 y_1 z_1 vx_1 vy_1 vz_1 ...<br>
...

and so on for the rest of the objects and timesteps, time is in seconds, rest same as in input file. nSteps is the number of timesteps stored in file, and nBodies is the number of simulated objects. timeX is the simulation time at current timestep, and the rest are the gravitational parameters and phase-space coordinates for the objects.

Program function descriptions
=============================
Loading parameters
------------------
When loading system paramters into the program by clicking Load File from programs File-menu, the following sequence unfolds:

![Sequence diagram for loading parameters](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/seq_load_sys.png)

as can be seen, the process has two endpoints, depending on whether input contained some issue which resulted in exception being throw, whic include unopenable files, wrong formatting, unacceptable number of objects, and errors in parsing values as doubles.

Loading integration steps
-------------------------
Loading steps from earlier integrations begins similarly as loading parameters by selecting Load Simulation from sane File-menu, with sequence being:  

![Sequence diagram for loading integration steps](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/seq_load_steps.png)

while chart is similar to loading parameters, it has more exception producing conditions, as unexpectedly having less steps than what is given on first line, different number of bodies than given.

Saving integration steps
------------------------
Saving happens by selecting Save simulation from same old File-menu, and starts the following sequence:

![Sequence diagram for saving integration steps](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/seq_save_sys.png)

Integration
-----------
Integration is started by pressing the Run-button, which begins a comapratively lengthy and complicated sequence described below:

![Sequence diagram for integration](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/seq_run_int.png)

Initial calls to intHandler check that both the system to be simulated and time-values used in integration are usable. Unfortunately, operation of integration is not completely limited to intHandler, but Task-object responsible for computing the integration is returned to UI for awkwardly setting up actions following its onCompletion-event, as I'm uncertain on more elegant way to implement it.

Changing integration values
---------------------------
Unlike the sequences given above, modifying the integrations time-parameters is only a simple method-call from UI to IntegrationHandler, as T and dT are stored inside the latter and are used only when IntegrationTask is being constructed during preparation of integration.

Program features in need of improvement
=======================================
Domain
------
The aforementioned way intTask is partially used from UI and not solely from intHandler would be main target for improvement, as currently I feel UI and domain are not separate enough.

Input/Outpu
-----------
For I/O, target for improvement wold be changing its methods so that method doing the actual file manipulation would receive a Scanner-object as argument, as now testing them would require making dedicated test files.