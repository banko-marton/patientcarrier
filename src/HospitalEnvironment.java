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
    for(String s : args)
        System.out.println(s);
	int hospitalSize = 24;
	int numOfCarriers = 1;


        hospitalModel = new HospitalModel(hospitalSize, numOfCarriers);
        hospitalView = new HospitalView(hospitalModel);
        hospitalView.setEnv(this);
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
            hospitalModel.placeAgent(i);
            carrierAgents.add(new Carrier(i, new Location(x+i, 1)));
        }

        clearAllPercepts();
    }

    @Override
    public boolean executeAction(String agName, Structure act) {
        boolean result = false;

        // .. if act.equals(act1) then else..

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
        //clearAllPercepts();


    }

    public HashMap<Department, Location> getDepartments(){
        return departments;
    }

    public ArrayList<Carrier> getCarrierAgents(){
        return carrierAgents;
    }

    public void addPatient(SicknessType type){
        Random r = new Random();
        int age = 10 + r.nextInt(90);
        Patient p = new Patient(age, type);
        reception.placePatient(p);

        addPercept("testManager", Literal.parseLiteral("newPatient(" + p.getId() +"," + p.getId() +","+ p.getType() + ")"));
    }

}
