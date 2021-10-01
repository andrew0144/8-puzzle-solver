package com.company;

// represents a single state of the 8-puzzle
public class State {
    // int array used as internal representation of the puzzle
    int[] tiles;

    // reads the string and constructs the array of Tiles as such;
    public State(String s){
        // get rid of all spaces and uppercase letters
        s = s.toLowerCase().replaceAll("\\s", "");
        tiles = new int[s.length()];
        // find index of blank tile
        char blank = 'b';
        int index = s.indexOf(blank);
        char[] chars = s.toCharArray();

        // turn the string into int array with 0 as blank
        for (int i = 0; i < chars.length; i++) {
            if (i == index) {
                tiles[i] = 0;
            }
            else tiles[i] = Character.getNumericValue(chars[i]);
        }
    }

    // constructs a State instance given another State
    public State(State s){
        tiles = new int[s.getTiles().length];
        for (int i=0; i< s.getTiles().length; i++) {
            tiles[i] = s.getTiles()[i];
        }
    }

    // gets the int array of the tiles in the current state
    public int[] getTiles(){
        return tiles;
    }

    // calculates the h1 value of the state
    public int getHeuristicValue(){
        int totalMisplaced=0;
        int index=0;
        for (int x: tiles) {
            if (x != index) {
                totalMisplaced++;
            }
            index++;
        }
        return totalMisplaced;
    }

    // calculates the h2 value of the state
    public int getHeuristic2Value(){
        int sumDistances=0;
        int[] tiles = getTiles();
        int[][] tiles2 = new int[3][3];
        int index=0;
        for (int x: tiles) {
            tiles2[index / 3][index % 3] = tiles[index];
            index++;
        }
        for(int i=0;i<3;i++){
            for(int j =0; j<3; j++){
                int distance1 =0;
                int distance2 =0;
                switch(tiles2[i][j]){
                    case 0:
                        distance1 = i;
                        distance2 = j;
                        break;
                    case 1:
                        distance1 = i;
                        distance2 = j-1;
                        break;
                    case 2:
                        distance1 = i;
                        distance2 = j - 2;
                        break;
                    case 3:
                        distance1 = i-1;
                        distance2 = j;
                        break;
                    case 4:
                        distance1 = i-1;
                        distance2 = j-1;
                        break;
                    case 5:
                        distance1 = i-1;
                        distance2 = j-2;
                        break;
                    case 6:
                        distance1 = i-2;
                        distance2 = j;
                        break;
                    case 7:
                        distance1 = i-2;
                        distance2 = j-1;
                        break;
                    case 8:
                        distance1 = i-2;
                        distance2 = j-2;
                        break;
                    default:
                        break;
                }
                sumDistances += Math.abs(distance1)+Math.abs(distance2);
            }
        }
        return sumDistances;
    }

    // returns the state as a string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < tiles.length;i++) {
            if(tiles[i] > 0){
                sb.append(tiles[i]).append(" ");
            }
            else if(tiles[i] == 0) sb.append("b ");
            if((i + 1) % 3 == 0) sb.append("\n");
        }
        return sb.toString() ;
    }
}
