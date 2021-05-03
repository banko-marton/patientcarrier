import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.*;

public class HospitalModel extends GridWorldModel {
    private int layoutSize;
    private int nCarriers;
    private HashMap<Department, Location> departments;
    private HashMap<Department, ArrayList<Location>> routesFromReception;
    private Location receptionPosition;
    private ArrayList<Carrier> carrierAgents;
    private Manager managerAgent;

    public HospitalModel(int hospitalSize, int numOfCarriers){
        // initializing the layout
        super(hospitalSize, hospitalSize, numOfCarriers);
        layoutSize = hospitalSize;
        nCarriers = numOfCarriers;
    }

    public void placeReception(Location pos){
        receptionPosition = pos;
        add(1, pos);
    }

    public Location placeDepartment(int depId){
        Location position = getFreePos();
        add(5+depId, position);
        return position;
    }

    public void placeAgent(int aId){
        int x = (layoutSize - nCarriers) / 2;
        setAgPos(aId, x+aId, 5);
    }

    private boolean isValid(int x, int y, boolean[][] visited)
    {
        return x >= 0 && y >= 0
                && x < layoutSize
                && y < layoutSize
                && isFreeOfObstacle(new Location(x, y))
                && !visited[x][y];
    }

    private QItem findShortestPath(Location from, Location to){
        QItem source = new QItem(from.y, from.x, 0);
        Queue<QItem> queue = new LinkedList<>();
        queue.add(source);

        boolean[][] visited = new boolean[layoutSize][layoutSize];
        visited[from.y][from.x] = true;

        while (!queue.isEmpty()) {
            QItem p = queue.remove();

            // Destination found;
            if (p.row == to.y && p.col == to.x)
                return p;

            // moving up
            if (isValid(p.row - 1, p.col, visited)) {
                queue.add(new QItem(p.row - 1, p.col,
                        p.dist + 1));
                visited[p.row - 1][p.col] = true;
            }

            // moving down
            if (isValid(p.row + 1, p.col, visited)) {
                queue.add(new QItem(p.row + 1, p.col,
                        p.dist + 1));
                visited[p.row + 1][p.col] = true;
            }

            // moving left
            if (isValid(p.row, p.col - 1, visited)) {
                queue.add(new QItem(p.row, p.col - 1,
                        p.dist + 1));
                visited[p.row][p.col - 1] = true;
            }

            // moving right
            if (isValid(p.row - 1, p.col + 1, visited)) {
                queue.add(new QItem(p.row, p.col + 1,
                        p.dist + 1));
                visited[p.row][p.col + 1] = true;
            }
        }
        return null;
    }

    public ArrayList<Location> findShortestPathFromReception(Location to){
        QItem path = findShortestPath(receptionPosition, to);
        if(path == null){
            System.out.println("ERROR: No route exists from Agent to Reception, shutting down.");
            assert(true);
        }
        ArrayList<Location> route = new ArrayList<>();
        while(path != null){
            route.add(new Location(path.col, path.row));
            path = path.parent;
        }
        return route;
    }

    /* USAGE:
    * We always want to calculate the path from the agent to a selected department, while picking up the patient at the reception. */
    public ArrayList<Location> findShortestPath(Location from, Department to){
        ArrayList<Location> secondHalf = routesFromReception.get(to);
        QItem pathFromAgToRec = findShortestPath(from, receptionPosition);
        if(pathFromAgToRec == null){
            System.out.println("ERROR: No route exists from Agent to Reception, shutting down.");
            assert(true);
        }

        ArrayList<Location> firstHalf = new ArrayList<>();
        while(pathFromAgToRec != null){
            firstHalf.add(new Location(pathFromAgToRec.col, pathFromAgToRec.row));
            pathFromAgToRec = pathFromAgToRec.parent;
        }
        firstHalf.addAll(secondHalf);
        return firstHalf;
    }

    public int findShortestDistance(Location from, Department to){
        return findShortestPath(from, to).size();
    }

    public void moveAgent(Carrier ag, Location l){
        setAgPos(ag.getId(), l);
        ag.currentPosition = l;
    }

    public void placeWalls(int times) {
        for(int i = 0; i < times; i++) {
            Random r = new Random();
            int x = r.nextInt(layoutSize - 5);
            int y = r.nextInt(layoutSize - 2);
            addWall(x, y+1, x+3, y + 1);
        }
    }
}
