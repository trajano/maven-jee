This is a pure Java implementation of a service bus using Akka actors and MongoDB for persistence.

This provides the following to the OSGi environment:
* a ServiceBus interface and implementation
* an ActorProvider interface that must be extended by actor providers
* a MapReduceActorProvider abstract class that can be extended to perform the map-reduce pattern.

Internally this uses the concept of a Master actor that acts as a message router.  It handles 
three types of messages on its own (not available in OSGi)

ActorRegistration
ActorDeregistration

- for ActorProvider management

Ask

- for Future support

The use case being implemented is to do a word count of a text file in the classpath.


It tries to solve the following problems:
* Simplify provider creation and binding
  * Take advantage of blueprint to associate a set of ActorProvider into the system. These ActorProviders are provided by separate bundles.

* Simplify access to clients
  * Provide a single point of access for OSGi clients to the actor system (done through ServiceBus interface).  The users of the ActorProviders should not know anything about the provider internals just that it accepts a certain message and optionally provides a type that can be asked for by the client.
  * Avoid exposing Akka for normal use.  (Still there for Akka Futures and the ActorSystem is exposed to OSGi for low-level support)

* Solve common use cases
  * Provide implementation of Map/Reduce pattern.
  * Provides an implementation of the Ask pattern.

