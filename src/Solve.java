/**
 * Created by Alex on 2/27/20.
 */

public class Solve {
    private char[][] world;
    private boolean[][][] knowledgeBase;
    private int[] startLocation;
    private int wumpusCount;
    private int explorerY;
    private int explorerX;
    private int explorerDY = 1;
    private int explorerDX = 0;


    public Solve(char[][] world, int wumpusCount) {
        this.world = world;
        this.wumpusCount = wumpusCount;
        this.startLocation = getStartLocation();
        explorerY = startLocation[0];
        explorerX = startLocation[1];
        this.knowledgeBase = new boolean[world.length][world.length][7];
        solveUnification();
    }

    private int solveUnification() {
        boolean reachedGold = false;
        int cost = 0;
        int remainingWumpusCount = wumpusCount;
        int arrowCount = remainingWumpusCount;



        // sensor list: breeze, stench, bump, scream, gold
        boolean[] sensorList;
        int action = 1;
        while(!reachedGold) {
            cost--;
            sensorList = sense(action);
            action = updateKnowledgeBase(sensorList);
            if(action == 0) {

            } else if(action == 1){

            } else if(action == 2){

            } else if(action == 3){

            } else if(action == 4){

            }
        }

        return cost;
    }



    private int updateKnowledgeBase(boolean[] sensorList){
        boolean[] input = new boolean[7];
        // KnowledgeBase: pit, breeze, wumpus, stench, visited, gold, safe
        if(!sensorList[0]) {

        } if(!sensorList[1]) {

        } if(!sensorList[2]) {

        } if(!sensorList[3]) {

        } if(!sensorList[4]) {

        }
        knowledgeBase[explorerY][explorerX] = input;
        return 1;
    }
    private boolean[] sense(int lastAction) {
        // sensor list: breeze, stench, bump, scream, gold
        boolean[] sensorList = new boolean[5];

        // breeze and stench
        if(explorerX != 0) {
            if(world[explorerY][explorerX-1] == 'p') {
               sensorList[0] = true;
            } else if(world[explorerY][explorerX-1] == 'w') {
                sensorList[1] = true;
            }
        } else if(explorerX == world.length){
            if(world[explorerY][explorerX+1] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY][explorerX+1] == 'w') {
                sensorList[1] = true;
            }
        }

        if(explorerY == 0) {
            if(world[explorerY-1][explorerX] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY-1][explorerX] == 'w') {
                sensorList[1] = true;
            }
        } else if(explorerY == world.length) {
            if(world[explorerY+1][explorerX] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY+1][explorerX] == 'w') {
                sensorList[1] = true;
            }
        }

        if(lastAction == -1) {   // bump
            sensorList[2] = true;
        } else if(lastAction == -2) {  // scream
            sensorList[3] = true;
        }

        if(world[explorerY][explorerX] == 'g') {   // gold
            sensorList[4] = true;
        }

        return sensorList;
    }

    private boolean turnLeft() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            explorerDY = -1;
            explorerDX = 0;
            return true;

        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            explorerDY = 1;
            explorerDX = 0;
            return true;

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            explorerDY = 0;
            explorerDX = 1;
            return true;

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            explorerDY = 0;
            explorerDX = -1;
            return true;
        }
        return false;
    }

    private boolean turnRight() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            explorerDY = 1;
            explorerDX = 0;
            return true;
        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            explorerDY = -1;
            explorerDX = 0;
            return true;

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            explorerDY = 0;
            explorerDX = -1;
            return true;

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            explorerDY = 0;
            explorerDX = 1;
            return true;
        }
        return false;
    }

    private boolean goForward() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            if(explorerX != world.length){
                if(world[explorerY][explorerX] == 'o') {
                    explorerX = explorerX + 1;
                    return true;
                } else {
                    return false;
                }
            }
        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            if(explorerX != 0){
                if(world[explorerY][explorerX] == 'o') {
                    explorerX = explorerX - 1;
                    return true;
                } else {
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            if(explorerY != world.length){
                if(world[explorerY][explorerX] == 'o') {
                    explorerY = explorerY + 1;
                    return true;
                } else {
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            if(explorerY != 0){
                if(world[explorerY][explorerX] == 'o') {
                    explorerY = explorerY - 1;
                    return true;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean shootArrow(int x, int y) {
        if(world[y][x] == 'w') {
            wumpusCount--;
            return true;
        } else {
            return false;
        }

    }

    private int[] getStartLocation() {
        int[] location = new int[2];

        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world.length; j++) {
                if(world[i][j] == 'a') {
                    location[0] = i;
                    location[1] = j;
                }
            }
        }
        return location;
    }

    private void printWorld() {
        for(int i = 0; i < world.length; i++) {
            System.out.print(" ");
            for(int j = 0; j < world.length; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println("");
        }
    }

}
