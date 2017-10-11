# Akka 

## Supervision vs Monitoring 
### Supervision
Describes a dependency between actors when parent actor delegates task to their children.
When failure happens, parent takes decision as it is unique. 

Different akka strategies: 
- OneForOneStrategy: parent will apply his decision on only the failing child
- OneForAllStrategy: parent will apply his choice on all children. 


### Monitoring  
Monitoring is used to tie one actor to another, so that it may react to the other actor's termination. 

Actor A monitors actor B as it wants to be notified when B terminates. 

### Difference
The difference is that supervision reacts to actor's termination 
whereas monitoring reacts to failure.