maven-jee6
==========

This contains the latest examples for my book "Realizing Enterprise Architectural Patterns with Maven and JEE6"

The audience of the book is for those aspiring to be the "Missing Enterprise 
Architect" which is the end-to-end enterprise architect.  This book is intended
so that they have a cursory glance at a wide variety of components but still
have a working reference examples that they can fall back on when needed.


## Enterprise technology stack

The enterprise technology stack limits the tooling used to ensure sustainability
as new people come in and old people leave.  It simplifies support since there
is a limited number of technologies to deal with.

However, we do not want to fully limit what developers can or cannot use.  We
just need to ensure that everyone has a stable base to work with.  For the
purposes of the book, we are using the following technology stack for
development.

* Eclipse based IDE
* Java EE 6 environment (i.e. Glassfish and WebSphere)
* Hudson
* Java language
* Maven

## License objectives

In order to reduce costs, the use of open source software must be used.  In
fact most large systems use open source software, but they wrap the license
with commercial support.

For the purposes of the book, we are only limiting to open source software
in order to reduce the cost of learning for the readers.

* No GPL or LGPL in delivered work products.  (Some Eclipse or Maven plugins
  may use GPL, but they will not be part of the deployed code).
* Eclipse or Apache licensed open source only.

## Quality objectives

* All work are validated with full warnings turned on.
* Keep up with the latest versions of the tooling.
* Contract first web services
* No IDE specific files in source tree.
* 100% coverage on packages, classes and methods.

## Branches

master

> This branch is where the recommended Enterprise technology stack is 
enforced.

emerging-technologies

> This branch is where there is use of emerging techologies such as OSGi,
Scalatest, MongoDB and newer Spring based integrations.  An architect 
should also look into these technologies in order to evaluate whether
they should go into the Enterprise technology stack.  Having this branch
allows the book to try and keep current with the latest trends but
still keep the master branch pure.

> Another thing to note is that this branch does not put all the versions
in the parent POM, but only on the POMs that directly use the technologies.
This allows easy removal of new technologies if they are no longer deemed
necessary.
