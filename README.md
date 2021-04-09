# Java-Chess-Engine
I have always enjoyed the game of chess and been amazed by the power of computers, so I decided to create a work-in-progress chess engine in java using guidlines from the [Chessprogrmming Wiki](https://www.chessprogramming.org/Main_Page "Title"). Currently, it uses "Negamax" and searches up to a ply of 4 in a second consistently. You can also play against it (Only as white). The move generation is completely debugged using the perft algorithm (it was worth it). The average perft speed using complicated positions is around 3 million moves/second on my Macbook Air (CPU: 1.8 GHz Dual-Core Intel Core i5, RAM: 8 GB 1600 MHz DDR3).

## Quick Install/Run (It's not a virus, but please do not run this file if you don't know me because its probably not a good habit)
1. Download the Chess.jar file
2. Install the following java version https://www.oracle.com/java/technologies/javase-jdk15-downloads.html
3. Using Command Prompt (Windows) or Terminal (Mac), type the follwoing enclosed in quotes "cd "
4. Then drag Chess.jar onto the window and delete the following text off the end of the command "\Chess.jar"
5. Finally, enter the following command "java -jar Chess.jar"

## Board Representation
* Uses bitboards stored in a Java long to store positions of a certain piece type
* All piece types extend the Piece class
* The board uses make and unmake move methods rather than copy-make (which failed in my first attempt)
* All piece objects are stored in static variables within their class (like a singleton)
* Color and PieceEnum are used to store the type of piece
* The full-move counter and half-move clock is not implemented yet

## Move Generation
* "Move" is extended by different types of moves
* Object Creation: new Move(pieceToBeMoved, startingPosition, endingPosition, pieceToBeCaptured)

## GUI
Currently the gui is in its infancy and there are many more features I would like to add to it to make it more enjoyable.
