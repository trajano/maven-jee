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
