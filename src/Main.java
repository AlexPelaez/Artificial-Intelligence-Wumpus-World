public class Main {

    public static void main(String[] args) {
        int size = 5;
        double pit = .1;
        double obs = .1;
        double wumpus = .1;
        int cellsExploredReactive = 0;
        int goldFoundReactive = 0;
        int wumpusFoundReactive = 0;
        int wumpusKilledReactive = 0;
        int explorerReactiveSuicide = 0;
        int pitFoundReactive = 0;
        int numberOfWorlds = 10;

        double avgCost = 0;
        double avgCells = 0;


//        World w = new World(size, pit, obs, wumpus);
//        Solve s = new Solve(w.getWorld(), w.getWumpusCount());
        World[] worlds = new World[numberOfWorlds];
        Solve[] solves = new Solve[numberOfWorlds];

        for( int i = 0; i < numberOfWorlds; i ++){
            worlds[i] = new World(size, pit, obs, wumpus);
            solves[i] = new Solve(worlds[i].getWorld(), worlds[i].getWumpusCount());
            wumpusFoundReactive += solves[i].getWumpusFoundReactive();
            cellsExploredReactive += solves[i].getCellsExploredReactive();
            goldFoundReactive += solves[i].getGoldFoundReactive();
            wumpusKilledReactive += solves[i].getWumpusKilledReactive();
            explorerReactiveSuicide += solves[i].getExplorerReactiveSuicide();
            pitFoundReactive += solves[i].getPitFoundReactive();
            System.out.println(avgCost);
            avgCost += (double)(solves[i].getCost());
        }
        avgCost = avgCost / (double)(numberOfWorlds);
        avgCells = cellsExploredReactive /numberOfWorlds;
        System.out.println("Wumpus' Found: " + wumpusFoundReactive);
        System.out.println("Gold Found: " + goldFoundReactive);
        System.out.println("Avg Cells Explored: " + avgCells);
        System.out.println("Avg Cost: " + avgCost);
        System.out.println("Explorer Killed Wumpus: " + wumpusKilledReactive);
        System.out.println("Died To Pit: " + pitFoundReactive);

    }
}
