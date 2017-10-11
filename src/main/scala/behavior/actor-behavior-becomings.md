# Replace actor behavior via become and unbecome 

### become 
Change the actor's behavior to become the new receive handler 

### unbecome
revert actor's behavior to the previous one in the actor beghaviour stack 

### Stash

Stash trait allows an actor to temporarily stash away messages that
cannot or should not handle messages using an actor current behavior 

stash() => adds the current message to the actor stash. 
unstashAll() => enqueues messages from stash to the actor mailbox 