package pharmacy.PersonClasses;
import java.util.Objects;
import pharmacy.ExtraClasses.DateTime;
public class Customer extends Person {

    private String shippingAddress;
    public Customer(String username, String password, String firstName, String lastName, String phone, String gender, DateTime dateOfBirth, String shippingAddress) {
        super(username, password, firstName, lastName, phone, gender, dateOfBirth);
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return "Customer{" + "shippingAddress=" + shippingAddress + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.shippingAddress, other.shippingAddress)) {
            return false;
        }
        return true;
    }
}