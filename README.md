Tycher
====================

A game...

 1. [client](client) implements the frontend side of the game
 2. [server](server) implements the backend side of the game
 3. [model](model) contains all models and DTOs used by server
 4. [loadtets](loadtests) is used for running load tests

Run with:

    ./gradlew run

Run with debug (connect remotely to 9099 port):

    ./gradlew -DDEBUG run

Check all project dependencies with:

    ./gradlew dependencies
