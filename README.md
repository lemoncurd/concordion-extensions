concordion-extensions
=====================

The aim of concordion-extensions is to provide some simple extensions 
to the Concordion (http://www.concordion.org) testing framework.

Current Extensions
------------------

* setMatrix / assertMatrixEquals 
    
  supports the creation of test cases that work with data matrices.

* specLinker
    
  Post-processes result files adding links to child specifications 
  based on the breadcrumb file structure.

Tests
-----

To find out more about the functions, take a look at the concordion tests:

    src/test/resources/spec/

Building
--------

The project uses Ant for building. The default target will compile, run 
the tests and create a jar.
  
  ant

Change Notes
-------
### 0.3
* SpecLinker - Concordion generated breadcrumbs are prepended with a link to the root index page
* SpecLinker - Fixed issue loading default index page when packaged as a JAR
* SpecLinker - Fixed filepath separator handling when running Windows machines
* SpecLinker - Added examples running with Maven and a simple build pom
  
Roadmap
-------
### 0.4
* SpecLinker - Optionally ignore link generation on certain pages
* SpecLinker - Concordion CSS should be added to the generated index page
* SpecLinker - Identify a strategy for dealing with ignored tests
* SpecLinker - Add command line argument to set default index page?
* SpecLinker - Distinguish index links from test links?
