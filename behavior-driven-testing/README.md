This is an example of doing testing combining ScalaTest with JUnit and 
Maven.

Note: throughout the project, Scala must *only* be used for testing (as
such the compile) goal is not defined in the parent POM.

Scala is the only dependency added to the parent and test POMs in order
to promote use of Scala Test for behavior driven testing.
