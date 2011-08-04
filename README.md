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
