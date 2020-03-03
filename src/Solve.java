/**
 * Created by Alex on 2/27/20.
 */

public class Solve {
    private static final int DEATH_COST = 10000;
    private char[][] world;

    private boolean[][][] knowledgeBase;
    private int[] startLocation;
    private int wumpusCount;
    private int explorerY;
    private int explorerX;
    private int explorerDY = 1;
    private int explorerDX = 0;
    private int cellsExploredReactive;
    private int goldFoundReactive;
    private int wumpusFoundReactive;
    private int wumpusKilledReactive;
    private int explorerReactiveSuicide;
    private int pitFoundReactive;

    private int cellsExploredKB;
    private int goldFoundKB;
    private int wumpusFoundKB;
    private int wumpusKilledKB;



    private int explorerKBSuicide;
    private int pitFoundKB;

    private int cost = 0;
    private int costKB = 0;

    private boolean reachedGold = false;

    private int remainingWumpusCount;
    private int arrowCount;



    public Solve(char[][] world, int wumpusCount) {
        this.world = world;
        this.wumpusCount = wumpusCount;
        this.startLocation = getStartLocation();
        arrowCount = wumpusCount;
        remainingWumpusCount = wumpusCount;
        explorerY = startLocation[0];
        explorerX = startLocation[1];
        this.knowledgeBase = new boolean[world.length][world.length][11];
        solveRecative();

        this.world = world;
        this.wumpusCount = wumpusCount;
        this.startLocation = getStartLocation();
        arrowCount = wumpusCount;
        remainingWumpusCount = wumpusCount;
        explorerY = startLocation[0];
        explorerX = startLocation[1];
//        solveKB();
//        solveKnowdledgeBased();
    }

    private int solveKB(){
        initKnowledgeBase();
        boolean reachedGold = false;
        boolean smelledStench = false;
        int move = 0;
        costKB = 0;

        while (!reachedGold){
//            printWorld1();
//            System.out.println();
//            System.out.println("X: " + explorerX);
//            System.out.println("Y: " + explorerY);


            costKB--;

            if(world[explorerY][explorerX] == 'g'){
                goldFoundKB++;
                System.out.println("Gold found");
                reachedGold = true;
                costKB = costKB + 1000;
                return costKB;

            }
            else if(world[explorerY][explorerX] == 'p'){
                pitFoundKB++;
                System.out.println("Death by pit");
                reachedGold = true;
                costKB = costKB - 10000;
                return costKB;

            }
            else if(world[explorerY][explorerX] == 'w'){
                wumpusFoundKB++;
//                System.out.println("Death by wumpus");
                costKB =costKB - 10000;
                return costKB;
            }
            else if(costKB <= -100000000){
//                System.out.println("Death by suicide");
                printWorld();
                explorerKBSuicide++;
                costKB =costKB - 10000;
                return costKB;

            } else{
                boolean[] sensorList;
                sensorList = sense();
                updateKnowledgeBase(sensorList);
                unification();
                world[explorerY][explorerX] = 'v';
//                printWorld();
//                System.out.println();
//                System.out.println("X: " + explorerX);
//                System.out.println("Y: " + explorerY);


                if(world[explorerY][explorerX + 1] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY-1][explorerX] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY+1][explorerX] == 'w') {
                    smelledStench = true;

                }
                if(world[explorerY ][explorerX - 1] == 'w') {
                    smelledStench = true;
                }

            }

            if(smelledStench == true && arrowCount !=  0){
//                System.out.println("arrow Shot");
//                System.out.println("DX: " + explorerDX);
//                System.out.println("DY: " + explorerDY);
                if(shootArrow()){
//                    System.out.println("Wumpus Killed");
                    wumpusKilledKB++;

                }
                arrowCount--;
                costKB -= 10;
                smelledStench = false;
                world[explorerY][explorerX] = 'v';
            } else {

                boolean topCell = queryKB(explorerY - 1, explorerX); //Top Cell
                boolean bottomCell = queryKB(explorerY  + 1, explorerX); //Bottom Cell
                boolean rightCell = queryKB(explorerY, explorerX + 1);//Right Cell
                boolean leftCell = queryKB(explorerY, explorerX - 1);//Left Cell

                if(explorerDX == 1 && explorerDY == 0) { // facing east
                    if(!topCell && !bottomCell && !rightCell){
                        move = (int) (Math.random() * 4);

                    }
                    else if(topCell){
                        move = 0;

                    }
                    else if(bottomCell){
                        move = 2;

                    }
                    else if(rightCell){
                        move = 1;

                    }

                } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                    if(!topCell && !bottomCell && !leftCell){
                        move = (int) (Math.random() * 4);

                    }
                    else if(bottomCell){
                        move = 2;

                    }
                    else if(leftCell){
                        move = 3;

                    }
                    else if(topCell){
                        move = 0;

                    }


                } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                    if(!rightCell && !bottomCell && !leftCell){
                        move = (int) (Math.random() * 4);

                    }
                    else if(rightCell){
                        move = 1;

                    }
                    else if(leftCell){
                        move = 3;

                    }
                    else if(bottomCell){
                        move = 2;

                    }


                } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                    if(!topCell && !rightCell && !leftCell){
                        move = (int) (Math.random() * 4);

                    }
                    else if(leftCell){
                        move = 3;

                    }
                    else if(topCell){
                        move = 0;

                    }
                    else if(rightCell){
                        move = 1;

                    }

                }







                    if (move == 0) {
//                            System.out.println("move North");
                            if (explorerDX == 1 && explorerDY == 0) { // facing east

                                turnLeft();
                                costKB--;

                            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                                turnRight();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;
                            }
                            if(!goForward())
                            {
                                turnRight();
                                costKB--;
                            }
                    } else if (move == 1) {
//                            System.out.println("move East");
                            if (explorerDX == -1 && explorerDY == 0) { // facing west
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                                turnLeft();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                                turnRight();
                                costKB--;
                            }
                            if(!goForward())
                            {
                                turnLeft();
                                costKB--;
                            }
                    } else if (move == 2) {
//                            System.out.println("move south");
                            if (explorerDX == 1 && explorerDY == 0) { // facing east
                                turnRight();
                                costKB--;
                            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                                turnLeft();
                                costKB--;
                            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;
                            }
                            if(!goForward())
                            {
                                turnLeft();
                                costKB--;
                            }
                    } else if (move == 3) {
//                            System.out.println("move West");
                            if (explorerDX == 1 && explorerDY == 0) { // facing east
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                                turnRight();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                                turnLeft();
                                costKB--;
                            }
                            if(!goForward())
                            {
                                turnLeft();
                                costKB--;
                            }
                    }

                }

            }
            cellsExploredKB++;
        return costKB;
    }

    private boolean queryKB(int newY, int newX) {
        // sensor list: breeze, stench, bump, scream, gold
        // KnowledgeBase: unknown 0 , factSafe 1 , safe 2, factWumpus 3, wumpus 4, factPit 5, pit 6, obstacle 7, breeze 8, stench 9, glitter 10
        if(knowledgeBase[newY][newX][1]){
            return true;
        }
        else if(knowledgeBase[newY][newX][2]){
            return true;
        }
        else if(knowledgeBase[newY][newX][3] || knowledgeBase[newY][newX][5] ){
            return false;
        }
        else if(knowledgeBase[newY][newX][7]){
            return false;
        }
        else if(knowledgeBase[newY][newX][4] || knowledgeBase[newY][newX][6] ){
            return false;
        }
        else{
            return false;
        }
    }


    public void initKnowledgeBase(){
        for(int i = 0; i < knowledgeBase.length; i++) {
            for(int j = 0; j < knowledgeBase.length; j++) {
                knowledgeBase[i][j][0] = true;
                knowledgeBase[i][j][1] = false;
                knowledgeBase[i][j][2] = false;
                knowledgeBase[i][j][3] = false;
                knowledgeBase[i][j][4] = false;
                knowledgeBase[i][j][5] = false;
                knowledgeBase[i][j][6] = false;
            }
        }
    }




    private void updateKnowledgeBase(boolean[] sensorList) {
        // sensor list: breeze, stench, bump, scream, gold
        if(sensorList[2]){// Sensed bump
            if(explorerDX == 1 && explorerDY == 0) { // facing east
                makeObstacle(explorerY,explorerX + 1); //add bump to knowledgeBase
            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                makeObstacle(explorerY,explorerX - 1);//add bump to knowledgeBase
            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                makeObstacle(explorerY + 1,explorerX);//add bump to knowledgeBase
            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                makeObstacle(explorerY - 1,explorerX);//add bump to knowledgeBase
            }
        }
        else if(sensorList[0] && sensorList[1]){
            knowledgeBase[explorerY][explorerX][8] = true;
            knowledgeBase[explorerY][explorerX][9] = true;
            knowledgeBase[explorerY][explorerX][2] = true;
        }
        else if(sensorList[0]){
            knowledgeBase[explorerY][explorerX][8] = true;
            knowledgeBase[explorerY][explorerX][2] = true;
        }
        else if(sensorList[1]){
            knowledgeBase[explorerY][explorerX][9] = true;
            knowledgeBase[explorerY][explorerX][2] = true;
        }
        else{
            makeFactSafe(explorerY,explorerX);
        }

        if (sensorList[4]) {
            knowledgeBase[explorerY][explorerX][10] = true;
        }
        if(sensorList[3]){

        }
    }

    private void unification(){
        // KnowledgeBase: unknown 0 , factSafe 1 , safe 2, factWumpus 3, wumpus 4, factPit 5, pit 6, obstacle 7, breeze 8, stench 9, glitter 10

        for(int i = 1; i < knowledgeBase.length - 1; i++) {
            for (int j = 1; j < knowledgeBase.length - 1; j++) {


                if (knowledgeBase[i][j][0] == false) {
                    if (knowledgeBase[i][j][1]) { //makes places next to factSafe just safe
                        makeSafe(i, j - 1);
                        makeSafe(i, j + 1);
                        makeSafe(i - 1, j);
                        makeSafe(i + 1, j);
                    } else if (knowledgeBase[i][j][8] && knowledgeBase[i][j][9]){
                        makePit(i, j - 1);
                        makePossibleWumpus(i, j - 1);
                        makePit(i, j + 1);
                        makePossibleWumpus(i, j + 1);
                        makePit(i - 1, j);
                        makePossibleWumpus(i - 1, j);
                        makePit(i + 1, j);
                        makePossibleWumpus(i + 1, j);
                    } else if (knowledgeBase[i][j][8]){ //Marks possible pit
                        makePit(i, j - 1);
                        makePit(i, j + 1);
                        makePit(i - 1, j);
                        makePit(i + 1, j);
                    } else if (knowledgeBase[i][j][9]){ // Add all possible wumpus' to knowledge base
                        makePossibleWumpus(i, j - 1);
                        makePossibleWumpus(i, j + 1);
                        makePossibleWumpus(i - 1, j);
                        makePossibleWumpus(i + 1, j);
                    } else if (knowledgeBase[i][j][6]){ // See if a pit is actually a pit
                        if (isFactSafe(i, j - 1) || isFactSafe(i, j + 1) || isFactSafe(i - 1, j) || isFactSafe(i + 1, j)) {
                           makeSafe(i, j);
                        } else if (!isUnknown(i, j - 1) || !isUnknown(i, j + 1) || !isUnknown(i - 1, j) || !isUnknown(i + 1, j)) {
                            makeFactPit(i, j);
                        }
                    } else if (knowledgeBase[i][j][4]){
                        if (isFactSafe(i, j - 1) || isFactSafe(i, j + 1) || isFactSafe(i - 1, j) || isFactSafe(i + 1, j)) {
                            makeSafe(i, j);
                        } else if (!isUnknown(i, j - 1) || !isUnknown(i, j + 1) || !isUnknown(i - 1, j) || !isUnknown(i + 1, j)) {
                            makeFactWumpus(i, j);
                        }
                    }
                }
            }
        }

    }


    private boolean[] sense() {
            // sensor list: breezy, stinky, bump, scream, gold

            boolean[] sensorList = new boolean[5];

            // breeze and stench
            if(world[explorerY][explorerX-1] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY][explorerX-1] == 'w') {
                sensorList[1] = true;
            }

            if(world[explorerY][explorerX+1] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY][explorerX+1] == 'w') {
                sensorList[1] = true;
            }

            if(world[explorerY-1][explorerX] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY-1][explorerX] == 'w') {
                sensorList[1] = true;
            }

            if(world[explorerY+1][explorerX] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY+1][explorerX] == 'w') {
                sensorList[1] = true;
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
                if(world[explorerY][explorerX + 1] != 'o') {
                    explorerX = explorerX + 1;
                    return true;
                } else {
                    return false;
                }
            }
        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            if(explorerX != 0){
                if(world[explorerY][explorerX - 1] != 'o') {
                    explorerX = explorerX - 1;
                    return true;
                } else {
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            if(explorerY != world.length){
                if(world[explorerY + 1][explorerX] != 'o') {
                    explorerY = explorerY + 1;
                    return true;
                } else {
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            if(explorerY != 0){
                if(world[explorerY - 1][explorerX] != 'o') {
                    explorerY = explorerY - 1;
                    return true;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean shootArrow() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            for(int i = explorerX; i < world.length; i++){
                if(world[explorerY][i] == 'w'){
                    world[explorerY][i] = 'x';
                    return true;
                } else if (world[explorerY][i] == 'o'){
                    return false;
                }
            }

        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            for(int i = explorerX; i > 0; i--){
                if(world[explorerY][i] == 'w'){
                    return true;
                } else if (world[explorerY][i] == 'o'){
                    return false;
                }
            }
        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            for(int i = explorerY; i < world.length; i++){
                if(world[i][explorerX] == 'w'){
                    return true;
                } else if (world[i][explorerX] == 'o'){
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            for(int i = explorerY; i < 0; i--){
                if(world[i][explorerX] == 'w'){
                    return true;
                } else if (world[i][explorerX] == 'o'){
                    return false;
                }
            }
        }
        return false;
    }
    public void makeFactSafe(int y, int x){
        try{
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][1] = true;
            knowledgeBase[y][x][2] = true;
            knowledgeBase[y][x][3] = false;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][5] = false;
            knowledgeBase[y][x][6] = false;


        }catch(IndexOutOfBoundsException e){}

    }
    public void makeFactPit(int y, int x){
        try{
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][5] = true;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][6] = false;
        }catch(IndexOutOfBoundsException e){}
    }

    public void makePit(int y, int x){
        try{
            if(!knowledgeBase[y][x][1] && !knowledgeBase[x][y][2] && !knowledgeBase[x][y][7]){
                knowledgeBase[y][x][0] = false;
                knowledgeBase[y][x][6] = true;
            }
        }catch(IndexOutOfBoundsException e){}
    }

    public void makeFactWumpus(int y, int x){
        try{
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][3] = true;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][6] = false;
        }catch(IndexOutOfBoundsException e){}
    }

    public void makePossibleWumpus(int y, int x){
        try{
            if(!knowledgeBase[y][x][1] && !knowledgeBase[y][x][2] && !knowledgeBase[y][x][7]){
                knowledgeBase[y][x][0] = false;
                knowledgeBase[y][x][4] = true;
            }
        }catch(IndexOutOfBoundsException e){}
    }

    public void makeObstacle(int y, int x){
        try{
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][7] = true;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][6] = false;
        }catch(IndexOutOfBoundsException e){}
    }

    public void makeSafe(int y, int x){
        try{
            makeFactSafe(y,x);
            knowledgeBase[y][x][2] = true;
            knowledgeBase[y][x][1] = false;
        }catch(IndexOutOfBoundsException e){}
    }
    public boolean isFactSafe(int y, int x){
        try{
            return knowledgeBase[y][x][1];
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public boolean isUnknown(int y, int x){
        try{
            return knowledgeBase[y][x][0]; //True if unknown
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }


    //=====================================================================================================================
    private int solveRecative(){
        boolean reachedGold = false;
        boolean smelledStench = false;
//        help1
        cost = 0;
        while (!reachedGold){
//            printWorld1();
//            System.out.println();
//            System.out.println("X: " + explorerX);
//            System.out.println("Y: " + explorerY);
            cost--;
            if(world[explorerY][explorerX] == 'g'){
                goldFoundReactive++;
//                System.out.println("Gold found");
                reachedGold = true;
                printWorld();
                cost = cost + 1000;
                return cost;

            }
            else if(world[explorerY][explorerX] == 'p'){
                pitFoundReactive++;
//                System.out.println("Death by pit");
                reachedGold = true;
                cost = cost - 10000;
                printWorld();
                return cost;

            }
            else if(world[explorerY][explorerX] == 'w'){
                wumpusFoundReactive++;
//                System.out.println("Death by wumpus");
                cost = cost - 10000;
                printWorld();
                return cost;
            }
            else if(cost <= -100000000){
//                System.out.println("Death by suicide");
                printWorld();
                explorerReactiveSuicide++;
                cost = cost - 10000;
                return cost;

            } else{
                world[explorerY][explorerX] = 'v';
//                printWorld();
//                System.out.println();
//                System.out.println("X: " + explorerX);
//                System.out.println("Y: " + explorerY);

                if (world[explorerY][explorerX - 1] == 'w') {
                    smelledStench = true;
                }

                if(world[explorerY][explorerX+1] == 'w') {
                    smelledStench = true;
                }

                if(world[explorerY-1][explorerX] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY+1][explorerX] == 'w') {
                    smelledStench = true;

                }

            }

            if(smelledStench == true && arrowCount !=  0){
//                System.out.println("arrow Shot");
//                System.out.println("DX: " + explorerDX);
//                System.out.println("DY: " + explorerDY);
                if(shootArrow()){
//                    System.out.println("Wumpus Killed");

                    wumpusKilledReactive++;
                }
                arrowCount--;
                cost -= 10;
                smelledStench = false;
                world[explorerY][explorerX] = 'v';
                if(!goForward())
                {
                    turnLeft();
                    cost--;
                }

                cost--;
            } else {
                int move = (int) (Math.random() * 4);

                if (move == 0) {
//                    System.out.println("move North");
                    if (explorerDX == 1 && explorerDY == 0) { // facing east

                        turnLeft();
                        cost--;

                    } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                        turnRight();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;
                    }
                } else if (move == 1) {
//                    System.out.println("move East");
                    if (explorerDX == -1 && explorerDY == 0) { // facing west
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                        turnLeft();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                        turnRight();
                        cost--;
                    }


                } else if (move == 2) {
//                    System.out.println("move south");
                    if (explorerDX == 1 && explorerDY == 0) { // facing east
                        turnRight();
                        cost--;
                    } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                        turnLeft();
                        cost--;
                    } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;
                    }
                } else if (move == 3) {
//                    System.out.println("move West");
                    if (explorerDX == 1 && explorerDY == 0) { // facing east
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                        turnRight();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                        turnLeft();
                        cost--;
                    }
                }
                if(!goForward())
                {
                    turnLeft();
                    cost--;
                }

            }
            cellsExploredReactive++;
        }
        return cost;
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
//    private void printWorld1() {
//        for(int i = 0; i < world.length; i++) {
//            System.out.print(" ");
//            for(int j = 0; j < world.length; j++) {
//                System.out.print(world1[i][j] + " ");
//            }
//            System.out.println("");
//        }
//    }
    public int getCellsExploredReactive() {
        return cellsExploredReactive;
    }

    public int getGoldFoundReactive() {
        return goldFoundReactive;
    }

    public int getWumpusFoundReactive() {
        return wumpusFoundReactive;
    }

    public int getWumpusKilledReactive() {
        return wumpusKilledReactive;
    }

    public int getExplorerReactiveSuicide() {
        return explorerReactiveSuicide;
    }

    public int getPitFoundReactive() {
        return pitFoundReactive;
    }

    public int getCellsExploredKB() {
        return cellsExploredKB;
    }

    public int getGoldFoundKB() {
        return goldFoundKB;
    }

    public int getWumpusFoundKB() {
        return wumpusFoundKB;
    }

    public int getWumpusKilledKB() {
        return wumpusKilledKB;
    }

    public int getExplorerKBSuicide() {
        return explorerKBSuicide;
    }

    public int getPitFoundKB() {
        return pitFoundKB;
    }

    public int getCost() {
        return cost;
    }
    public int getCostKB() {
        return costKB;
    }

}
