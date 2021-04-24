package java.model.utils;

import java.model.Patient;

public interface HospitalElement {
    public Patient takePatient();
    public void placePatient(Patient patient);
}
