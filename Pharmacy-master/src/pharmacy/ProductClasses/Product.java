package pharmacy.ProductClasses;
//Abstract Class 

import java.io.Serializable;

public abstract class Product implements Serializable{  
    public static enum ProductType {
        Cosmetics, Medication, PM, All
    };
    //--------(Attributes)----------//
    protected String name;
    protected int code;
    protected double price;
    protected int quantity;
    protected String description;
    
    //---------(Methods)-------------//
    
    //Constructor
    public Product(String name, int code, double price, int quantity) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.quantity = quantity;
    }
    
    //Mutator Methods (Setter)
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public void AddQuantity(int qty){
        this.quantity += qty;
    }
    //Accessor Methods (Getter)
    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public String getDescription(){
        return description;
    }
   //Addition Methods
    public double getTotalPrice(){
        return quantity*price;
    }
    
    public String getFormattedTotalPrice(){
        String result = String.valueOf(this.getTotalPrice());
        for (int i = result.indexOf('.') , j = 0; i >= 0; i-- , j++){
            if (j % 3 == 0 && j != 0)
                result = result.substring(0, i) + "," + result.substring(i , result.length());
        }
        if (result.charAt(0) == ',')
            result = result.substring(1,result.length());
        return result + " £";
    }
    @Override
    public String toString(){
        return "(Code:" + this.code + ", Name:" + this.name + ", Price:" + this.price + ", Qty:" + this.quantity + ")";
    }
}