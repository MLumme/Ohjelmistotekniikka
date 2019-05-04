User Manual
===========
Getting the program
-------------------
Easiest way is to download [final release jar](https://github.com/MLumme/Ohjelmistotekniikka/releases/tag/loppupalautus), or packaged from project files with
```
mvn package
```

Start up
-------
Program is started with 

```
java -jar GravitationalIntegrator.jar
```

Loading in new parameters
-------------------------
From File-menu on program top-left select "Load Data," and use the opened file browser to select the file following format described in Readme.md. If something is wrong with the parameters or file, the program will refuse to load them and provide feedback on the reason.

Loading in old integration results
----------------------------------
Similarly as above, select "Load Simulation" from File-menu, and again use the file browser to select output file. If something is wrong with these earlier steps or file itself, the program will refuse to load them and provide feedback on the reason.

Saving integration results
--------------------------
From the same File menu, select "Save simulation," and select already existing file or create a new one from file browser. In case of unusable file feedback will be provided to the user.

Running integrations
--------------------
Before integration can be started, user must input runtime T and timestep dT values into textboxes on window's lower left, these are measured in days. These are NOT reset on integration completion or upon loading new data. After this, they are submitted for the program with "Submit"-button, with program providing feedback in the case of unusable inputs. T does not need to be larger than dT, nor does it need to be exactly divisible with dT, in these cases integration will run past T by value less than dT. After these values are set, or exists from earlier, the user can press "Run"-button to start the integration, with progress bar next to it indicating integration progress.

After the integration is finished, or old results have been loaded in, integration can be continued with old time values, or they can be changed, in which case upon saving results time values will naturally not have same intervals. As a clarification to avoid any potential misunderstandings, T is only there to limit timesteps taken, it does not mean that internally or in output files time associated with each step is between 0 and T+\<dT, which is the case for first integration, but for successive runs time keeps counting from last runs endpoint.

Example input and output
------------------------
Program project folder contains files ``Test_Input.dat``, retrieved from Jet Propulsion Laboratory's [HORIZONS-system](https://ssd.jpl.nasa.gov/?horizons), and ``Test_Output.dat``, first containing input for the Sun and the four gas giants, and latter is sample output integrated for 1000 days with timestep of 1 day.

Help inside the program
-------------------
Program contains a menu containing these same instructions as here and description of file formatting used with it, labelled "Help".