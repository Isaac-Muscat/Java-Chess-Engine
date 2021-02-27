# Java-Chess-Engine
I have always enjoyed the game of chess and been amazed by the power of computers, so I decided to create a work-in-progress chess engine in java using guidlines from the [Chessprogrmming Wiki](https://www.chessprogramming.org/Main_Page "Title"). Currently, the move generation is completely debugged using the perft algorithm (it was worth it). The average perft speed using complicated positions is around 3 million moves/second on my Macbook Air (CPU: 1.8 GHz Dual-Core Intel Core i5, RAM: 8 GB 1600 MHz DDR3).

## Board Representation
* Uses bitboards stored in a Java long to store positions of a certain piece type
* All piece types extend the Piece class
* The board uses make and unmake move methods rather than copy-make (which failed in my first attempt)
* All piece objects are stored in static variables within their class (like a singleton)
* Color and PieceEnum are used to store the type of piece
* The full-move and half-move clock is not implemented yet

## Move Generation
* "Move" is extended by different types of moves
* Object Creation: new Move(pieceToBeMoved, startingPosition, endingPosition, pieceToBeCaptured
