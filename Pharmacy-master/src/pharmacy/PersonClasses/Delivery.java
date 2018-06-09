package pharmacy.PersonClasses;

import java.util.ArrayList;
import pharmacy.ExtraClasses.DateTime;
import pharmacy.MainClasses.Order;
import java.util.PriorityQueue;
import pharmacy.MainClasses.PharmacyController;
public class Delivery extends Person{
    //--------(Attributes)----------//
    private boolean available;
    ArrayList <Order> Orders;
    //---------(Methods)-------------//
    
    //Constructor
   public Delivery(String username, String password, String Fname, String Lname, String phone, String gender, DateTime dt) {
        super(username, password, Fname, Lname, phone, gender, dt);
        this.available = true; //Delivery is available
        Orders = new ArrayList<>();
    }
    
    //Mutator Methods (Setter)
    public void setAvailable(){
        this.available = true;
    }
    
    public void setBusy(){
        this.available = false;
    }

    public void setOrders(ArrayList<Order> Orders) {
        this.Orders = Orders;
    }
    
    //Accessor Methods (Getter)
    public boolean isAvailable(){
        return available;
    }
    
    public int MyOrders_Number(){
        return Orders.size();
    }

    public ArrayList<Order> getOrders() {
        return Orders;
    }
    
    //Addition Methods
    public void addOrder(Order ord){
        Orders.add(ord);
    }
        
    @Override
    public String toString() {
        return "ID: " + this.getID()+ ", Name: " + this.getFirstName() + ", Available: " + this.available;
    }
    
    
}
