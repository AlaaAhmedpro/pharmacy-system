package pharmacy.ProductClasses;
public class Cosmetics extends Product {
    //Attributes
    private String category;

    //Constructor
    public Cosmetics(String name, int code, double price, int quantity, String category) {
        super(name, code, price, quantity);
        this.category = category;
    }

    //Mutators
    public void setCategory(String category) {
        this.category = category;
    }

    //Accessors
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Cosmetics{" + "category=" + category + '}';
    }

    
    
}