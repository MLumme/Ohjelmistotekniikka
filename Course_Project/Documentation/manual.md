User Manual
===========
Startup
-------
Program started with 

```
java -jar GravitationalIntegrator.jar
```

Loading in new parameters
-------------------------
From File-menu on program top-left selec "Load Data," and use the opened file browser to select the file following format described in Readme.md.

Loading in old integration results
----------------------------------
Similarly as above, select "Load Simulation" from File-menu, and again use the file browser to select output file.

Saving integration results
--------------------------
From the same File menu, select "Save simulation," and select allready existing file or create a new one from file browser.

Running integrations
--------------------
Before integration can be started, user must input runtime T and timestep dT values into textboxes on window's lower left, theese are masured, for now, in seconds. Theese are NOT reset on integration completion or upon loading new data. After this, they are sublitted for the progra with "Submit"-button, after which user can press "Run"-button, with progress bar next to it indicating integration progress.

After integration is finnished, or old results have been loaded in, integration can be continued with old time values, or they can be changed, in which case upon saving results timevalues will naturally not have same intervals.