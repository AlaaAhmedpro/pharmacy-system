package pharmacy.PersonClasses;
import java.util.ArrayList;
import pharmacy.ExtraClasses.DateTime;
import pharmacy.MainClasses.Order;
public abstract class Staff extends Person {
    //--------(Attributes)----------//
    private ArrayList<Order> confirmedOrders;
    //---------(Methods)-------------//
    
    //Constructor
    public Staff(String username, String password, String firstName, String lastName, String phone, String gender, DateTime dateOfBirth) {
        super(username, password, firstName, lastName, phone, gender, dateOfBirth);
        this.confirmedOrders = new ArrayList<Order>();
    }
    
    //Mutator Methods (Setter)

    public void setConfirmedOrders(ArrayList<Order> confirmedOrders) {
        this.confirmedOrders = confirmedOrders;
    }
    
    public void addOrder(Order order){
        this.confirmedOrders.add(order);
    }
    
    //Accessor Methods (Getter)

    public ArrayList<Order> getConfirmedOrders() {
        return confirmedOrders;
    }

    @Override
    public String toString() {
        return "Staff{" + "confirmedOrders=" + confirmedOrders + '}';
    }
    
    
    
}
