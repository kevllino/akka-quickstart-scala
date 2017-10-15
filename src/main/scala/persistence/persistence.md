# Akka persistence 

Need to retain / persist interna state if actor crashes. 
Akka uses the fact that only actor internal state is persisted
but not current internal state. Events with how internal states can be recovered are stored. 

Persistent actor receives a new message that calls a command 
which is first validated if it cant be applied to the current state 
if validation succeedes, events are generated from the command that are representing effects of the command 
events are then peristed. When persisted actor needs to be recovered, only 
successful events can be replied, because we know they can generate results. 

## Architecture

**Persistent Actor** stateful actor persisting events in a journal. 
Can be used to implement Commands and Event source actors. 
 
**Persistent View** that receives journaled messages, Updated internal stated only from 
 a persistent's actor's  replicated message stream. 
 
**Async journal**
 An application that control which messages are journaled
 
 Recommended db is Cassandra or jdbc backend driver.  
 
**Snapshot store**
Persist snapshots of a persistent actor, used to optimize recovery time. 

Persistent actor handles: 
- command: instructions from outside world
- event: stored operations from the journal 