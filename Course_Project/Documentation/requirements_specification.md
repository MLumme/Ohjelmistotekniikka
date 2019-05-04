Purpose of the software
=======================

Goal is to enable user to run solar system-scale gravitational simulations for bodies using classical physics, relativistic effects will not be accounted for.

Implemented functionality
=========================

* User is able to load parameters for a system to do an orbital integration on.
* User is able to load old integration outputs into the program.
* Use is able to set parameters T and dT for the integrator to use in integration
* User is able to run integration as many times as they wish, and continue old loaded integrations.
* User is able to save integration steps to a file for use in some other purpose.

Improvement ideas
================
* Implementing some kind of plotting or animation for results would be the firs improvment, as it was in the original specification, but JavaFX had issues with large datasets with the way I had planned to plot them, or there was some issue in my abortive implementation, and other functions were eating more time than expected.