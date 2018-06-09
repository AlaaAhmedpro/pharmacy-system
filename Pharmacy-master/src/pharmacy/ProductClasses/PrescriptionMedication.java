package pharmacy.ProductClasses;
import pharmacy.ExtraClasses.DateTime;

import java.util.ArrayList;

public class PrescriptionMedication extends AbstractMedication {
    protected ArrayList<String> sideEffect;
    public PrescriptionMedication(String name, int code, double price
            , int quantity, String purpose, String adultDose, 
            String childDose, String activeIngredient, 
            DateTime expiredDate, String manufacturer) 
    
    {
        super(name, code, price, quantity, purpose ,adultDose ,childDose, activeIngredient ,expiredDate ,manufacturer);
    }
    public void setSideEffect(ArrayList<String> sideEffect) {
        this.sideEffect = sideEffect;
    }
    public ArrayList<String> getSideEffect() {
        return sideEffect;
    }
    public void AddSideEffect(String sideEffect){
        this.sideEffect.add(sideEffect);
    }

}