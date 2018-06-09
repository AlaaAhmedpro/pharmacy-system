package pharmacy.ProductClasses;
import pharmacy.ExtraClasses.DateTime;
public class Medication extends AbstractMedication {
    
    String type; // pills ,Liquid
    public Medication(String name, int code, double price
            , int quantity, String purpose, String adultDose, 
            String childDose, String activeIngredient, 
            DateTime expiredDate, String manufacturer) 
    {
        super(name, code, price, quantity, purpose ,adultDose ,childDose, activeIngredient ,expiredDate ,manufacturer);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Medication{" + "type=" + type + '}';
    }
 
}