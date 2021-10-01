package com.company;

import java.util.*;

public class Board {
    public State state; // the current state of the Board instance
    public int maxNodes = 15000; // the maximum amount of nodes that can be checked in a search (set high by default)
    public int h = 1; // int indicating the heuristic function used by searches
    public int numberExpanded = 0;
    public int numberMoves = 0;
    boolean failed = false;


    // constructs a Board instance
    Board(State s) {
        setState(s);
    }

    // solves the puzzle using local beam search
    public Node solveBeam(int k){
        failed = false;
        setHeuristicFunction(1);
        int numExpanded = 0;
        Node node = new Node(getState(), null, null, h);
        PriorityQueue<Node> frontier = new PriorityQueue<>(11, new NodeComparator());
        frontier.add(node);
        HashSet<Node> nodesToExpand = new HashSet<>();
        HashMap<State, Node> reached = new HashMap<>();
        reached.put(node.getState(), node);

        // check if starting node is solution
        if(node.getHeuristicVal() == 0) {
            traceNodePath(node,true);
            return node;
        }
        else{
            for (Node child : expand(node)) {
                frontier.add(child);
            }
        }
        while(!frontier.isEmpty()){
            // poll the first k elements (best h-value) from the frontier and add them to expand list
            for(int i = 0; i<k; i++){
                Node x = frontier.poll();
                if (x == null) {
                    break;
                }
                nodesToExpand.add(x);
            }

            // clear frontier to forget all but best k nodes
            frontier.clear();
            // check if any of the k-best nodes polled from the frontier are the goal state and expand them otherwise
            for(Node n: nodesToExpand){
                numExpanded++;

                // check if solution has been found
                if(n.getHeuristicVal()==0){
                    traceNodePath(n,true);
                    System.out.println("Number of nodes expanded: "+numExpanded+"\nk value = "+k);
                    return n;
                }
                else {
                    reached.put(n.getState(), n);
                    frontier.addAll(expand(n));
                }
                // check if node limit has been found and return
                if (reached.size() >= maxNodes) {
                    traceNodePath(n, false);
                    System.out.println("\nThe maximum number of nodes allowed in the search has been exceeded. Current Node has been returned.");
                    System.out.println("Number of nodes expanded: "+numExpanded+"\nk value = "+k);
                    failed= true;
                    return n;
                }
            }
            nodesToExpand.clear();
        }
        // if the beam search reaches end of tree or hits the max nodes limit without finding the goal, print msg and return the last node
        traceNodePath(node, false);
        System.out.println("Number of nodes expanded: "+numExpanded+"\nk value = "+k);
        System.out.println("\nNo Solution could be found and the most recent node has been returned.");
        failed= true;
        return node;
    }

    // overloaded solveA_star using the current heuristic in this board instance
    public Node solveA_star(){
        return solveA_star(this.h);
    }

    // solves the puzzle using A* search
    public Node solveA_star(int heuristic){
        int numExpanded = 0;

        // set the current heuristic to the specified value if the specified value is legal
        if(heuristic<1 || heuristic>2) throw new IllegalArgumentException();
        else setHeuristicFunction(heuristic);

        // initialize frontier and reached with the starting node
        Node node = new Node(getState(), null, null, heuristic);
        PriorityQueue<Node> frontier = new PriorityQueue<>(11, new NodeComparator());
        frontier.add(node);
        HashMap<State, Integer> reached = new HashMap<>();
        reached.put(getState(), node.getPathCost());

        // while the frontier has elements
        while(!frontier.isEmpty()) {
            // get minimum node in frontier
            node = frontier.poll();
            //System.out.println(node.getGValue());
            // expand each child node one by one
            for (Node child:expand(node)) {
                numExpanded++;
                State s = child.state;

                // if h(n) is 0, then we are at a goal state
                if (child.getHeuristicVal()==0) {
                    traceNodePath(child, true);
                    System.out.println("Number of nodes expanded: "+numExpanded+"\nHeuristic used: h"+h);
                    this.numberExpanded = numExpanded;
                    return child;
                }

                // check if maxNodes has been hit
                if (reached.size() >= maxNodes) {
                    traceNodePath(node, false);
                    System.out.println("\nThe maximum number of nodes allowed in the search has been exceeded and the most recent node has been returned.");
                    System.out.println("Number of nodes expanded: "+numExpanded+"\nHeuristic used: h"+h);
                    this.numberExpanded = numExpanded;
                    return child;
                }
                // if s hasn't been reached or its path cost is minimal, add it to reached and frontier
                if(!reached.containsKey(s) || child.getPathCost() < reached.get(s)) {
                    reached.put(s, child.getPathCost());
                    frontier.add(child);
                }
            }
        }

        System.out.println("\nNo Solution could be found and the most recent node has been returned.");
        return node;
    }

    // traces the input nodes path (used to return solution path)
    public void traceNodePath(Node n, boolean solved){
        System.out.print("NOW TRACING NODE PATH:\n");
        int numMoves = 0;
        StringBuilder sb = new StringBuilder();
        if(solved)sb.append("\nSOLUTION:\n"+n.getState().toString());
        else{
            sb.append("\nFINAL STATE:\n"+n.getState().toString());
            return;
        }
        while(n.parent!=null) {
            numMoves++;
            sb.append("\n" + n.action);
            n = n.parent;
            sb.append("\n" + n.getState().toString());
        }
        this.numberMoves = numMoves;
        sb.append("STARTING STATE SHOWN ABOVE. MOVES MADE ARE SHOWN IN BETWEEN STATES.\n");
        System.out.println(sb.toString() + "Done in " + numMoves + " moves.");
    }

    // expands a node using all of its legal moves
    public ArrayList<Node> expand(Node n){
        State s = new State(n.state);
        ArrayList<Node> children = new ArrayList<>();
        ArrayList<Direction> actions = getLegalMoves(s);
        // for each move(or Direction) in the legal moves, add a node with that action to the list
        for (Direction d: actions) {
            State sNext = checkMove(s, d);
            Node child = new Node(sNext, n, d, h);
            children.add(child);
        }
        return children;
    }

    // randomizes the state by starting at the goal state and making n random moves
    public void randomizeState(int n){
       state = new State("b12 345 678");
        Random rand = new Random();
        rand.setSeed(777);
        Direction direction;
       for(int i = 0; i < n; i++) {
           ArrayList<Direction> legalMoves = getLegalMoves(getState());
           direction = legalMoves.get(rand.nextInt(legalMoves.size()));
           move(getState(), direction);
           // printState();
       }
    }

    // locates the blank tile and moves it in the input direction
    public State move(State s, Direction d){
        State result = s;
        int index = findBlankIndex(result);
        // store tile at the slot b is moving and swap them
        int tileHolder = result.getTiles()[index + d.getTransitionIndex()];
        result.getTiles()[index] = tileHolder;
        result.getTiles()[index + d.getTransitionIndex()] = 0;
        return result;
    }

    // locates the blank tile and returns the state that would result(w/o actually moving it)
    public State checkMove(State s, Direction d){
        State result = new State(s);
        int index = findBlankIndex(result);
        // store tile at the slot b is moving and swap them
        int tileHolder = result.getTiles()[index + d.getTransitionIndex()];
        result.getTiles()[index] = tileHolder;
        result.getTiles()[index + d.getTransitionIndex()] = 0;
        return result;
    }

    // checks legal moves and returns a list of legal moves
    public ArrayList<Direction> getLegalMoves(State s){
        ArrayList<Direction> legalMoves = new ArrayList<>();
        int b = findBlankIndex(s);
        for (Direction d: Direction.values()) {
            int nextPosition = b + d.getTransitionIndex();
            if (d == Direction.UP && nextPosition >= 0){
                legalMoves.add(Direction.UP);
            }
            else if (d == Direction.DOWN && nextPosition <= 8){
                legalMoves.add(Direction.DOWN);
            }
            else if (d == Direction.RIGHT && b != 2 && b!= 5 && b!=8){
                legalMoves.add(Direction.RIGHT);
            }
            else if (d == Direction.LEFT && b!=0 && b!=3 && b!=6){
                legalMoves.add(Direction.LEFT);
            }
        }
        return legalMoves;
    }

    // finds index of b (the blank tile)
    public int findBlankIndex(State s){
        int index = 0;
        for (int i : s.getTiles()) {
            if (i == 0)
                return index;
            else index++;
        }
        return -1;
    }

    // sets the maximum number of nodes to be considered in a search
    public void maxNodes(int n){
        this.maxNodes = n;
    }

    // sets the board's state
    public void setState(State s){
        this.state = s;
    }

    // returns the current state of the board
    public State getState(){
        return this.state;
    }

    // sets the board's current heuristic function( h1(n)=1 and h2(n)=2 )
    public void setHeuristicFunction(int h){
        this.h = h;
    }

    // prints the current state of the board
    public void printState(){
        if(h==1) System.out.println("h1(n) = "+ getState().getHeuristicValue());
        else if(h==2) System.out.print("h2(n) = "+getState().getHeuristicValue() + "\n");
        System.out.print(getState().toString());
    }

    // collects data on the search methods and returns it
    public void testSearchMethods(){

        maxNodes(1000000);
        IntSummaryStatistics statistics1 = new IntSummaryStatistics();
        IntSummaryStatistics statistics2 = new IntSummaryStatistics();
        IntSummaryStatistics statistics3 = new IntSummaryStatistics();
        IntSummaryStatistics statistics4 = new IntSummaryStatistics();
        int cntFailed = 0;
        for(int i = 0; i < 100; i++){
            randomizeState(i+1);
            solveA_star(1);
            if(numberExpanded >= maxNodes || numberExpanded > maxNodes - 4) cntFailed++;
            else if(numberMoves <= 5) statistics1.accept(numberExpanded);
            else if(numberMoves >5 && numberMoves <= 10) statistics2.accept(numberExpanded);
            else if (numberMoves >10 && numberMoves <= 15) statistics3.accept(numberExpanded);
            else statistics4.accept(numberExpanded);
        }
        System.out.println("\nA* with h1 failed " + cntFailed + " times out of 100 tries, n from 1-100");
        System.out.println("Average nodes expanded for numMoves <= 5: "+statistics1.getAverage());
        System.out.println("Average nodes expanded for 5 < numMoves <= 10: "+statistics2.getAverage());
        System.out.println("Average nodes expanded for 10 < numMoves <= 15: "+statistics3.getAverage());
        System.out.println("Average nodes expanded for 15 < numMoves: "+statistics4.getAverage());

        IntSummaryStatistics statistics5 = new IntSummaryStatistics();
        IntSummaryStatistics statistics6 = new IntSummaryStatistics();
        IntSummaryStatistics statistics7 = new IntSummaryStatistics();
        IntSummaryStatistics statistics8 = new IntSummaryStatistics();
        cntFailed = 0;
        for(int i = 0; i < 100; i++){
            randomizeState(i+1);
            solveA_star(2);
            if(numberExpanded >= maxNodes || numberExpanded > maxNodes - 4) cntFailed++;
            else if(numberMoves <= 5) statistics5.accept(numberExpanded);
            else if(numberMoves >5 && numberMoves <= 10) statistics6.accept(numberExpanded);
            else if (numberMoves >10 && numberMoves <= 15) statistics7.accept(numberExpanded);
            else statistics8.accept(numberExpanded);
        }
        System.out.println("\nA* with h2 failed " + cntFailed + " times out of 100 tries, n from 1-100");
        System.out.println("Average nodes expanded for numMoves <= 5: "+statistics5.getAverage());
        System.out.println("Average nodes expanded for 5 < numMoves <= 10: "+statistics6.getAverage());
        System.out.println("Average nodes expanded for 10 < numMoves <= 15: "+statistics7.getAverage());
        System.out.println("Average nodes expanded for 15 < numMoves: "+statistics8.getAverage());

        IntSummaryStatistics statistics9 = new IntSummaryStatistics();
        IntSummaryStatistics statistics10 = new IntSummaryStatistics();
        IntSummaryStatistics statistics11 = new IntSummaryStatistics();
        IntSummaryStatistics statistics12 = new IntSummaryStatistics();
        cntFailed = 0;
        for(int i = 0; i < 100; i++){
            randomizeState(i+1);
            solveBeam(10);
            if(numberExpanded >= maxNodes || failed) cntFailed++;
            else if(numberMoves <= 5) statistics9.accept(numberExpanded);
            else if(numberMoves >5 && numberMoves <= 10) statistics10.accept(numberExpanded);
            else if (numberMoves >10 && numberMoves <= 15) statistics11.accept(numberExpanded);
            else statistics12.accept(numberExpanded);
        }
        System.out.println("\nBeam Search with k=10 failed " + cntFailed + " times out of 100 tries, n from 1-100");
        System.out.println("Average nodes expanded for numMoves <= 5: "+statistics9.getAverage());
        System.out.println("Average nodes expanded for 5 < numMoves <= 10: "+statistics10.getAverage());
        System.out.println("Average nodes expanded for 10 < numMoves <= 15: "+statistics11.getAverage());
        System.out.println("Average nodes expanded for 15 < numMoves: "+statistics12.getAverage());
    }

    // tests NodeComparator (debugging purposes)
    public void testComparator(){
        PriorityQueue<Node> frontier = new PriorityQueue<>(11, new NodeComparator());
        Node node = new Node(new State("1b2 345 678"), null, null, h);
        frontier.add(node);
        frontier.addAll(expand(node));
        System.out.println(frontier.poll().getPathCost());
        System.out.println(frontier.poll().getPathCost());
        System.out.println(frontier.poll().getPathCost());
        System.out.println(frontier.poll().getPathCost());
    }

    // comparator for the priority queue in A* search
    private class NodeComparator implements Comparator<Node>{

        // overrides compare() method of Comparator for minimal h-value
        public int compare(Node s1, Node s2) {
            if (s1.getPathCost() < s2.getPathCost())
                return -1;
            else if (s1.getPathCost() > s2.getPathCost())
                return 1;
            return 0;
        }
    }

    // simple enum for cardinal directions
    public enum Direction{
        UP(-3),
        DOWN(+3),
        LEFT(-1),
        RIGHT(1);

        private int transitionIndex;
        Direction(int i){
            this.transitionIndex = i;
        }

        private int getTransitionIndex(){
            return transitionIndex;
        }
    }


}
