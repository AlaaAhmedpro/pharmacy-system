package pharmacy.ExtraClasses;

import java.io.Serializable;
import java.util.ArrayList;
import pharmacy.ExceptionClasses.InvalidInputException;
import pharmacy.ProductClasses.PrescriptionMedication;

public class Prescription implements Serializable{
    //--------(Attributes)----------//
    private int code;
    private DateTime date;
    private ArrayList <PrescriptionMedication> Medications;
    //---------(Methods)-------------//
    
    //Constructor Methods
    public Prescription(int code) throws InvalidInputException{
        this.setCode(code);
        this.setDate(new DateTime());
        Medications = new ArrayList <PrescriptionMedication>();
    }
    public Prescription(int code, ArrayList<PrescriptionMedication> Medications) throws InvalidInputException {
        this.setCode(code);
        this.Medications = Medications;
        this.setDate(new DateTime());
    }
    //Mutator Methods (Setter)
    public void setCode(int code) {
        if (String.valueOf(code).length() == 8)
            this.code = code;
        else
            throw new ArithmeticException("Invalid Prescription Code"); 
    }
    public void setDate(DateTime date) {
        this.date = date;
    }
    public void addMedication(PrescriptionMedication pm){
        this.Medications.add(pm);
    }
    public void setMedications(ArrayList<PrescriptionMedication> Medications) {
        this.Medications = Medications;
    }
    //Accessor Methods (Getter)
    public int getCode() {
        return code;
    }
    public ArrayList<PrescriptionMedication> getMedications() {
        return Medications;
    }
    public DateTime getDate() {
        return date;
    }
    //Addition Methods
    public boolean isContainMedication (int MedicationCode) {
        for (PrescriptionMedication current : Medications) {
            if (MedicationCode == current.getCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + "| Date: " + this.date.toString() + "| Medications: " + this.Medications.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.code;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.code != ((Prescription)obj).code) {
            return false;
        }
        return true;
    }
    
}
