package java.env;

import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.model.Department;
import java.model.HospitalModel;
import java.model.Patient;
import java.model.Reception;
import java.model.agents.Carrier;
import java.model.agents.Manager;
import java.model.utils.SicknessType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/* Agent BELIEFS as literals */
// ..
/* Agent ACTIONS as literals */
// ..

public class HospitalEnvironment extends Environment {

    private HashMap<Department, Location> departments;
    private HashMap<Department, ArrayList<Location>> routesFromReception;
    private Location receptionPosition;
    private ArrayList<Carrier> carrierAgents;
    private Manager managerAgent;
    private Reception reception;

    private HospitalModel hospitalModel;

    public HospitalEnvironment(int hospitalSize, int numOfCarriers){
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
    }
    @Override
    public void init(String[] args) {

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
        clearAllPercepts();


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
    }

}
