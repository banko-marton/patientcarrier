import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class HospitalEnvironment extends Environment {
    /* Agent BELIEFS as literals */
    // Manager
    public static final Literal newPatient = Literal.parseLiteral("newPatient(action_id,patient_id,loc_to)");


    /// Carrier
    // belief
    public static final Literal position = Literal.parseLiteral("pos(a,x,y)");
    public static final Literal distance = Literal.parseLiteral("dist(a,d)");
    // action
    public static final Literal move_towards = Literal.parseLiteral("move_towards(x,y)");
    public static final Literal pick = Literal.parseLiteral("pick");
    public static final Literal drop = Literal.parseLiteral("drop");


    private HashMap<Department, Location> departments;
    private HashMap<Location, Department> locToDep;
    private HashMap<Department, ArrayList<Location>> routesFromReception;
    private Location receptionPosition;
    private ArrayList<Carrier> carrierAgents;
    private Manager managerAgent;
    private Reception reception;

    private HospitalModel hospitalModel;

    /*public HospitalEnvironment(int hospitalSize, int numOfCarriers){
        hospitalModel = new HospitalModel(hospitalSize, numOfCarriers);
        // placing the front door
        reception = new Reception(20);
        receptionPosition = new Location(hospitalSize / 2, 0);
        hospitalModel.placeReception(receptionPosition);
        // placing the departments randomly
        departments = new HashMap<>();
        routesFromReception = new HashMap<>();
        int depID = 0;
        for(SicknessType depType : SicknessType.values()){
            Department department = new Department(depType);
            Location depPos = hospitalModel.placeDepartment(depID++);
            departments.put(department, depPos);
            routesFromReception.put(department, hospitalModel.findShortestPathFromReception(depPos));
        }

        // initializing Agents
        managerAgent = new Manager(0);
        carrierAgents = new ArrayList<>();
        for(int i=0; i<numOfCarriers; i++){
            int x = (hospitalSize - numOfCarriers) / 2;
            hospitalModel.placeAgent(i+1);
            carrierAgents.add(new Carrier(i+1, new Location(x+i, 1)));
        }
    }*/
    HospitalView hospitalView;

    @Override
    public void init(String[] args) {
	int hospitalSize = 24;
	int numOfCarriers = 1;


        hospitalModel = new HospitalModel(hospitalSize, numOfCarriers);


        // placing the front door
        reception = new Reception(20, this);
        receptionPosition = new Location(hospitalSize / 2, 0);
        hospitalModel.placeReception(receptionPosition);

        hospitalModel.placeWalls(4);

        // placing the departments randomly
        departments = new HashMap<>();
        locToDep = new HashMap<>();
        routesFromReception = new HashMap<>();
        int depID = 0;
        for(SicknessType depType : SicknessType.values()){
            Department department = new Department(depType);
            Location depPos = hospitalModel.placeDepartment(depID++);
            departments.put(department, depPos);
            locToDep.put(depPos, department);
            routesFromReception.put(department, hospitalModel.findShortestPathFromReception(depPos));
        }

        // initializing Agents
        managerAgent = new Manager(0);
        carrierAgents = new ArrayList<>();
        for(int i=0; i<numOfCarriers; i++){
            int x = (hospitalSize - numOfCarriers) / 2;
            hospitalModel.placeAgent(i);
            Location carrierLoc = new Location(x+i, 5);
            carrierAgents.add(new Carrier(i, carrierLoc));
            addPercept(Literal.parseLiteral("pos(r"+ i +","+ carrierLoc.x + "," + carrierLoc.y +")"));
        }


        hospitalView = new HospitalView(hospitalModel, this);
        //hospitalView.setEnv(this);

        //clearAllPercepts();
        addPercept(Literal.parseLiteral("pos(base,"+ receptionPosition.x + "," + receptionPosition.y +")"));
    }

    @Override
    public boolean executeAction(String agName, Structure act) {
        boolean result = false;
        System.out.println(agName +" doing: "+ act);


        //az agent hív egy move_towards(X,Y)-t, ekkor az env-nek egyet kell léptetni a jó irányba
        if(act.toString().contains("move_towards")) {
            //melyik agent
            Carrier c = carrierAgents.get(Integer.parseInt(agName.substring(1))-1);
            //destination parse
            int x = Integer.parseInt(act.toString().substring(act.toString().indexOf('(')+1, act.toString().indexOf(',')));
            int y = Integer.parseInt(act.toString().substring(act.toString().indexOf(',')+1, act.toString().indexOf(')')));

            //ebben tároljuk az útovnalat
            ArrayList<Location> steps;

            //ez a következő lépés
            Location nextLoc;

            //ha a destionation nem department akkor csak a recepció lehet
            if (!locToDep.containsKey(new Location(x, y))) {
                //TODO
                //ez itt nekem mindig csak az agent pozícióját tartalmazza
                steps = hospitalModel.findShortestPathFromReception(c.currentPosition);
                System.out.println("Path from agent to reception");
                for (Location l:
                     steps) {
                    System.out.println(l);
                }
                nextLoc = steps.get(steps.size() - 2);
            }else {
                steps = hospitalModel.findShortestPath(c.currentPosition, locToDep.get(new Location(x, y)));
                nextLoc = steps.get(1);
            }
            System.out.println(nextLoc);

            hospitalModel.moveAgent(c, nextLoc);
            addPercept(Literal.parseLiteral("pos(r"+ c.id +","+ c.currentPosition.x + "," + c.currentPosition.y +")"));
            System.out.println(Literal.parseLiteral("pos(r"+ c.id +","+ c.currentPosition.x + "," + c.currentPosition.y +")"));
            return true;
        }

        if (result) {
            updateBelief();
            try {
                Thread.sleep(100);
            } catch (Exception ignored) {
            }
        }

        return result;
    }

    private void updateBelief(){
        clearAllPercepts();



    }

    public HashMap<Department, Location> getDepartments(){
        return departments;
    }

    public ArrayList<Carrier> getCarrierAgents(){
        return carrierAgents;
    }

    public void advertisePatient(Patient p ) {
        if(p != null) {
            System.out.println("addPercept lefut");
            addPercept("testManager", Literal.parseLiteral("newPatient(" + p.getId() + "," + p.getId() + ",\"" + p.getType() + "\")"));
        }
    }

    public void addPatient(SicknessType type){
        Random r = new Random();
        int age = 10 + r.nextInt(90);
        Patient p = new Patient(age, type);
        reception.placePatient(p);
    }

    public Location getReceptionPosition(){
        return receptionPosition;
    }
}
