-Description-
This is simple java-based game server framework

-features-
game update loop
worker thread
worker thread manager(considering modification to 'worker thread pool') -> deprecated
message queue
client handler
client handler pool -> deprecated

game world runs on single worker thread
client messages queued in message queue
worker threads poll client messages from message queue
worker threads updates game world every tick

-how to update code-
code areas that can be frequently changed alongside game feature updates are surrounded by:
// ===============================CAPRICIOUS===============================