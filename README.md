rti
===

Exercise with RTI (DDS)

Setup
--------------------------
This project is originally build in Eclipse on Ubuntu 12.04 with JAVA 1.7.0_45. 

Remember to setup the mandatory RTI environment variables in /etc/environment.

!Note: You have to set LD_LIRARY_PATH manually in eclipse to point to your <RTI root>/ndds.5.0.0/lib/<architecture>.
!!Note: The eclipse projects needs to reference the <RTI root>/ndds.5.0.0/class/nddsjava.jar.

You can run all three projects inside eclipse for convinience. 

Messages
-------------------------
This project contains the messages (data) sent over the domain including commonly used utility functions. 
- Generic MessageHandler, that deserializes the messages and puts them into a queue
- Serializer that serializes/deserializes objects to/from an byte array

PriceProvider
--------------------------
This project publishes raw prices over domain# 1.
Very simple. 

PriceForecaster
--------------------------
Subscribes to the raw prices and publishes future prices.
The subscription and publication takes place in their own threads - and passes the prices from subscriber to publisher using a blocking queue. 

PriceViewer
--------------------------
Subscribes to both raw and future prices and writes their content in the console (yeah, not very effective). Two separate subscribes exists in their own thread - additional two threads are used to write the contents to the console (yes, they could just write to the console from the same thread they where handled from...). 
