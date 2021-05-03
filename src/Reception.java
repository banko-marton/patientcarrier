import java.util.LinkedList;
import java.util.Random;

public class Reception implements HospitalElement {
    private LinkedList<Patient> waitingPatients;
    private long idCounter;

    public Reception(int nInitPatients){
        waitingPatients = new LinkedList<>();
        // generate patients

        int nSicknesses = SicknessType.values().length;
        for(int i = 0; i < nInitPatients; i++){
            Random r = new Random();
            Patient patient = new Patient(10 + r.nextInt(90), SicknessType.values()[r.nextInt(nSicknesses)]);
            patient.setId(i);
            waitingPatients.add(patient);
        }
        idCounter = nInitPatients;
    }
    @Override
    public Patient takePatient() {
        if(waitingPatients.isEmpty())
            return null;
        return waitingPatients.removeFirst();
    }

    @Override
    public void placePatient(Patient patient) {
        patient.setId(idCounter++);
        waitingPatients.add(patient);
    }

    public LinkedList<Patient> getWaitingPatients() {
        return waitingPatients;
    }
}
