package com.company;

// represents a node in the search tree
public class Node {
    public State state;
    public Node parent;
    public Board.Direction action;
    public int pathCost;
    public int heuristicVal;
    public int gValue;

    // constructs a node
    public Node(State s, Node p, Board.Direction a, int hf){
        this.state = s;
        this.parent = p;
        this.action = a;
        if(hf==1) heuristicVal = s.getHeuristicValue();
        else heuristicVal = s.getHeuristic2Value();
        if(p!=null) this.gValue = 1 + p.getGValue();
        else this.gValue = 0;
        this.pathCost = heuristicVal + gValue;
    }

    // gets the path cost of this node
    public int getPathCost() {
        return pathCost;
    }

    // gets gValue of this node;
    public int getGValue(){
        return this.gValue;
    }

    // gets heuristicVal
    public int getHeuristicVal(){
        return heuristicVal;
    }

    // gets the state corresponding to this node
    public State getState() {
        return state;
    }
}
