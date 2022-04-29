# CSDS393_P1
In this intro to AI project, I implemented A* search and local beam search to solve varying instances of the 8-puzzle.

## Running this Project
- First, clone this repo to your local machine using `git clone`
- Open the folder in VScode or your IDE of choice
- Edit Commands.txt using the below instructions
- Run Main.java, and the commands you input will execute
- Observe the console output to see how my A* search and local beam search work through the 8-puzzle

### Editing Commands.txt
By editing this file, you are telling my program what to do.
Each command must be separated and on its own line in Commands.txt.

Here is a list of commands you can mess around with:
- setState \<input\> \<input\> \<input\>
  - This command sets the state of the 8-puzzle. Each <input> string consists of 3 numbers, or the character b, which represents the blank tile. There can only be one b in a given setState command. All other characters must be the number 1-8, and these numbers cannot be reused/
- printState
  - This command prints the current state to the console.
- move <direction>
  - This command moves the blank tile in a given direction, which can be up, down, left, or right. 
- randomizeState <numMoves>
  - This command randomizes the state by making the given number of moves from the solution state. Thus, higher numbers make the puzzle harder to solve.
- solve A-star <heuristic>
  - This command runs the A* search algorithm using a given heuristic (h1 or h2) to solve the 8-puzzle. The h1 heuristic uses the total number of misplaced tiles to evaluate how close it is to the goal state. The h2 heuristic uses the sum manhattan distance of each tile from its goal position to evaluate how close it is to the goal state. h2 is typically more accurate than h1.
- solve beam <numBeams>
  - This command runs the local beam search algorithm with a given number of beams to solve the 8-puzzle. More beams = more thorough search of the state space, but at a cost of time.
