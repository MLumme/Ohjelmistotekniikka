Unit and integrationt testing
=============================
UI
--
As per instructions, contents of gravitationalintegrator.ui-package has been left outside of automated testing.

Domain
------
In the domain the difference between integration and unit testing is somewhat unclear sometimes, due to how much classes are interconnected.

What could be called the integration tests for main function of the program, integration of orbits, is in IntegratorHandlerTest. It, among its own unit tests, test the entire chain of calls from UI to set up, start and handle results post-completion twice, for both correctness of results when compared to just running the integrator alone, and to check that returned array contains right amount of steps for given integration times.

According to review of Week 6:s returned code there appears to be a potential error resulting from a trick to get JavaFX toolkit to run in JUnit-testing, which I'm unable to reproduce. The method is to initialize a JFXPanel an @Before method, and running Platform.exit() in @After, according to searching error might be caused by calls being repeated  for each test. Attempted to fix this by doing initialization and exit in @BeforeClass and @AfterClass, resulting to only one event each, but due to my inability to reproduce the problem I am unsure if problem persists. Hence IntegratorTaskTest and IntegratorHandlerTest might be rendered unusable.

File Input/Output
-----------------
Unit testing of file input/output-methods in FileHandler-class was implemented use TemporaryFolders to create group of temporary input and output files, majority of which are each broken in specific ways to trigger their corresponding error conditions, and one per method to validate correct output. No integration testing was done for FileHandler.

Test coverage
-------------
Instructions coverage is 96%, and branch coverage is 98%

![Testing coverage](https://github.com/MLumme/Ohjelmistotekniikka/blob/master/Course_Project/Documentation/test_coverage.png)

majority of missed instructions are the lacking tests for IntegratorHandlers calls to FileHandler.

System testing
==============
During system testing all functions from manual and requirements specifications were tested to be functional on Windows 10 on both NetBeans launched version and packaged .jar-file. Additionally, handling of errors for wrong inputs in UI and I/O-files were tested manually to confirm correct messages, as the method used in unit and integration testing could tell what kind of Exception was thrown, but not if it had the correct error message. 

Finally the correct function of the orbital integration was confirmed by loading in Test_Input.dat, containing parameters for the Sun and four gas giants, and running the integration over one Jovian year and confirming that Jupiter returned to nearly the same coordinates it started from.

Possible improvements on testing
================================
Currently testing of Tasks and Threads are somewhat questionable, as on one hand tests need to start a JavaFX-component to gain access to its toolkit to be able to run them. Second, there is the issue of using Task.get with a timeout to force the test to wait for integration thread to finish its work, as using onCompletion does not work with JUnit-tests, and it might under some conditions fail, if the thread somehow manages to use too much time to complete.