# Akka basic tools 

## Actor Ref 
Interface of actor instance. ActorRef points to Actor or DeadLetter when it dies. 
Does not change during lifecycle.

## Actor Path 

Sequence of actor names of actor system tree path. New instance of actor with difference ref. 

## Actor Selection 

Representation of actor, created with actor path/name. Stays valid even when actor dies and another instance is created. 
system.actorOf => actorRef under /user/