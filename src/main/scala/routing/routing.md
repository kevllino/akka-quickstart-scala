# Routing in akka

forward message means that original sender ref is maintained even though 
message going through mediator like router. 

This type of router actor comes in two distinct flavors:

Pool - The router creates routees as child actors and removes them from the router if they terminate.
Group - The routee actors are created externally to the router and the router sends messages to the specified path using actor selection, without watching for termination.

## Routing strategies

### Random 
### Round-robin 
Equally distributes work to routees / workers

### Balancing 
Redistributes work from busy routees to idle routees. 
Routees share same mail box. 

### Smallest mailbox router
Forward message to the routee with smallest messages in mailbox. 

### Broadcast router
Forward messages to all its routees. 

### Scatter Gather First completed

Send message to all its routees, then, wait for the first message that it gets back. 
Original result send back to its sender, other replies are discarded. 

### Consistent hashing router
Uses consistent hashing to select a routee. 

### Tail chopping
Send message to first routee randomly, then after a small delay to the second routee. 
This allows to send same message on other routee if the first one is not replying. 
=> better latency 
