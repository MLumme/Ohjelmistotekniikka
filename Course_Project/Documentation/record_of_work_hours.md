Date | Time | Task
---  | --- | ---
26.3 | 1   | Writing software requirements specifications and record of work hours.
31.3 | 1.5 | Writing code for classes Body and System.
1.4  | 2   | Writing the integrator-class using and created tests for Body-class, implemented integrator.
2.4  | 4   | Created class for reading data from input file, rudimentary main class to test that integrator actually works and does something, changes corresponding to the change from gravitational constant to standard gravitational parameter.
7.4  | 1.5 | Working on GUI, mostly on top menu and tying it to file input.
8.4  | 3   | More work on GUI, mostly on presenting loaded/computed object parameters on table, and input of integration parameters.
9.4  | 4   | Implemented the ability of actually running the integration from GUI and update shown parameters at the end of integration. Also implemented checkstyle, and fixed most of issues, implemented some additional tests.
14.4 | 4   | Fimally implemented saving all intermediate timesteps to an arraylist. Modified program to use Tasks and Threads to enable responsive GUI while integration is calculated, and tied tem to a progress bar. Implemented saving currently stored simulation to a file.
15.4 | 4   | Wrote tests for Sys-class, fixed an embarrasing error which tests made apparent where computation of system center of mass was not calculated properly. Found and fixed errors and typos in IntegratorTest, which had coincidentally masked a fault in acceleration calculation of gravity was repelling bodies, due to forgotten nagative sign, which should now work correctly. 
16.4 | 3   | Worked on error handling and communicating them to user via Alert-dialogues, tried to break UI-class into smaller chucks (WIP). Implemented a small addition where any changes to parameters or attempts to save or load files are stopped while integrator thread is still running, and this communicated to user. Required tasks for GitHub-release.
20.4 | 4   | Attempted to create plots using javaFX-linecharts, worked, deemed too slow, scrapped, will attempt to find more suitable alternative. Fixed error in gitignore which allowed target-files to repository. Fixed an error where arraylist steps was cleared in wrong way during file input.
25.4 | 3.5 | Added functionality to load old integration timestep-lists from file, witch unfortunately required a rewrite, refactored some of error handling and parsing file into Body-objet into their own functions to remove duplicate code.
26.4 | 7   | Converted pre-method and -class commets to javadoc-format, including private methods for the sake of consistency, added an additional class to domain that acts as a sort of interface between UI and the classes in domain and io-packages, no resulting only in integratorTask being partially operated from UI, rest of parameters and methods are stored in and operated through IntegratorHandler. Built a kludge of a test pair for IntegratorTask-class for testing that thread system works. Wrote initial version of user manual.
28.4 | ~1  | Changed output format to be simpler to manipulate in code by placing all system parameters on same row per timestep.
Total time: 43.5h