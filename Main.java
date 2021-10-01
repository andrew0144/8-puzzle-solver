package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
     try {
         Board b = new Board(new State("b12 345 678"));
         Scanner scan = new Scanner(new FileReader("C:\\Users\\andre\\IdeaProjects\\CSDS393_P1\\src\\com\\company\\Commands.txt"));
          while(scan.hasNextLine()){
              if(!scan.hasNext()) break;
              String command = scan.next();

              // sets state given the command and data
              if(command.equals("maxNodes")){
                  b.maxNodes(scan.nextInt());
              }

              // sets state given the command and data
              else if(command.equals("setState")){
                  b.setState(new State(scan.nextLine()));
              }

              // prints state on command
              else if(command.equals("printState")){
                  b.printState();
              }

              // moves blank tile on command
              else if(command.equals("move")){
                  switch (scan.next().toLowerCase()){
                      case "up":
                          b.move(b.getState(), Board.Direction.UP);
                          break;
                      case "down":
                          b.move(b.getState(), Board.Direction.DOWN);
                          break;
                      case "right":
                          b.move(b.getState(), Board.Direction.RIGHT);
                          break;
                      case "left":
                          b.move(b.getState(), Board.Direction.LEFT);
                          break;
                  }
              }

              // randomizes the state on command with specified n value
              else if(command.equals("randomizeState")){
                  b.randomizeState(scan.nextInt());
              }

              else if(command.equals("solve")){
                  String s = scan.next();
                  if(s.equals("A-star")){
                      s = scan.next();
                      if(s.equals("h2")){
                          b.solveA_star(2);
                      }
                      else b.solveA_star( 1);
                  }
                  else if(s.equals("beam")){
                    int k = scan.nextInt();
                    b.solveBeam(k);
                  }
              }

          }
          //b.testSearchMethods();
     }
     catch (FileNotFoundException e) {
      e.printStackTrace();
     }


        /*
        Board b = new Board(new State("b12 345 678"));
        b.printState();

        // tests move() in the 4 cardinal directions
        System.out.print(Arrays.toString(b.getState().tiles)+"\n");
        b.move(b.getState(), Board.Direction.DOWN);
        System.out.print("\nh1(n) = "+b.getState().getHeuristicValue() + "\n");
        System.out.print("\nh2(n) = "+b.getState().getHeuristic2Value() + "\n");
        b.printState();
        System.out.print(Arrays.toString(b.getState().tiles)+"\n");

        b.move(b.getState(), Board.Direction.RIGHT);
        System.out.print("\nh1(n) = "+b.getState().getHeuristicValue() + "\n");
        System.out.print("\nh2(n) = "+b.getState().getHeuristic2Value() + "\n");
        b.printState();
        System.out.print(Arrays.toString(b.getState().tiles)+"\n");

        b.move(b.getState(), Board.Direction.UP);
        System.out.print("\nh1(n) = "+b.getState().getHeuristicValue() + "\n");
        System.out.print("\nh2(n) = "+b.getState().getHeuristic2Value() + "\n");
        b.printState();
        System.out.print(Arrays.toString(b.getState().tiles)+"\n");

        b.move(b.getState(), Board.Direction.LEFT);
        System.out.print("\nh1(n) = "+b.getState().getHeuristicValue() + "\n");
        System.out.print("\nh2(n) = "+b.getState().getHeuristic2Value() + "\n");
        b.printState();

        // tests randomizeState() with increasing n values
        System.out.print(Arrays.toString(b.getState().tiles)+"\n NOW RANDOMIZING with n=2 \n");
        b.randomizeState(2);
        b.printState();

        System.out.print(Arrays.toString(b.getState().tiles)+"\n NOW RANDOMIZING with n=5 \n");
        b.randomizeState(5);
        b.printState();

        System.out.print(Arrays.toString(b.getState().tiles)+"\n NOW RANDOMIZING with n=20 \n");
        b.randomizeState(20);
        b.printState();

        // tests solveA_star related methods
        System.out.print("\nNOW TESTING A* SEARCH METHODS\n");
        b.setState(new State("1b2 345 678"));
        System.out.print("h(n) = "+b.getState().getHeuristicValue() + "\n");
        b.printState();
        b.setState(new State("1b2 345 678"));

        System.out.println("\nNOW TESTING COMPARATOR FUNCTION ON EXPAND ARRAYLIST");
        b.testComparator();

        System.out.println("\nNOW TESTING SOLVE A STAR WITH RANDOMIZED STATE (N=5)");
        b.randomizeState(5);
        System.out.println("\nNOW STARTING SOLVE USING ABOVE STATE");
        Node solution = b.solveA_star();

        System.out.println("\nNOW TESTING SOLVE A STAR WITH MAX NODES = 30");
        b.maxNodes(30);
        b.randomizeState(20);
        System.out.println("\nNOW STARTING SOLVE USING ABOVE STATE");
        Node solution2 = b.solveA_star();

        b.testComparator();

        System.out.println("\nNOW TESTING SOLVE BEAM (K=3) WITH RANDOMIZED STATE (N=5)");
        b.randomizeState(5);
        b.printState();
        System.out.println(b.getLegalMoves(b.getState()));
        System.out.println("\nNOW STARTING SOLVE USING ABOVE STATE");
        Node solution3 = b.solveBeam(10);*/

    }



}


