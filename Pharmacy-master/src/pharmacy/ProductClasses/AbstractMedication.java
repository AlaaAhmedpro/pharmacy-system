package pharmacy.ProductClasses;
import java.util.ArrayList;
import pharmacy.ExtraClasses.DateTime;
public abstract class AbstractMedication extends Product {
    //--------(Attributes)----------//
    protected String purpose;
    protected String adultDose;
    protected String childDose;
    protected String activeIngredient;
    protected DateTime expiredDate;
    protected String expiredDateS;
    protected String manufacturer;
    //---------(Methods)-------------//
    
    //Constructor
    public AbstractMedication(String name, int code
            , double price, int quantity, String purpose
            , String adultDose, String childDose
            , String activeIngredient, DateTime expiredDate
            , String manufacturer) 
    {
        super(name, code, price, quantity);
        this.purpose = purpose;
        this.adultDose = adultDose;
        this.childDose = childDose;
        this.activeIngredient = activeIngredient;
        this.expiredDate = expiredDate;
        this.manufacturer = manufacturer;
        this.expiredDateS = expiredDate.getDate();
    }
    
    //Mutator Methods (Setter)
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setAdultDose(String adultDose) {
        this.adultDose = adultDose;
    }

    public void setChildDose(String childDose) {
        this.childDose = childDose;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setExpiredDate(DateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public void setExpiredDateS(String expiredDateS) {
        this.expiredDateS = expiredDateS;
    }
    
    //Accessor Methods (Getter)
    public String getPurpose() {
        return purpose;
    }

    public String getAdultDose() {
        return adultDose;
    }

    public String getChildDose() {
        return childDose;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public DateTime getExpiredDate() {
        return expiredDate;
    }

    public String getExpiredDateS() {
        return expiredDateS;
    }

    @Override
    public String toString() {
        return "AbstractMedication{" + "purpose=" + purpose + ", adultDose=" + adultDose + ", childDose=" + childDose + ", activeIngredient=" + activeIngredient + ", expiredDate=" + expiredDate + ", expiredDateS=" + expiredDateS + ", manufacturer=" + manufacturer + '}';
    }
    
    
    
}