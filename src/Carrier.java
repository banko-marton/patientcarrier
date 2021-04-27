import jason.environment.grid.Location;


public class Carrier {
    private int id;
    private Patient takenPatient = null;
    private Location currentPosition;
    private HospitalElement destination = null;
    private int takenSteps;
    static private int speed = 200;

    public Carrier(int id, Location pos){
        this.id = id;
        this.currentPosition = pos;
    }

    public void takePatient(HospitalElement from){
        takenPatient = from.takePatient();
    }

    public int getId(){return id;}

    public long getTakenId(){
        if(takenPatient != null){
            return takenPatient.getId();
        }
        return -1L;
    }

}