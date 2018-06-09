package pharmacy.PersonClasses;

import pharmacy.ExtraClasses.DateTime;
import pharmacy.MainClasses.PharmacyController.ProductTable;
public class AdminWarehouse extends Staff {
    
    int itemsAddedAmount;
    public AdminWarehouse(String username, String password, String firstName, String lastName, String phone, String gender, DateTime dateOfBirth) {
        super(username, password, firstName, lastName, phone, gender, dateOfBirth);
    }

    public void BringMissingProducts(){
        for (int i = 0; i < ProductTable.getProducts_Number(); i++){
            if (ProductTable.getProductOfIndex(i).getQuantity() < 10){
                ProductTable.getProductOfIndex(i).AddQuantity(10);
            }
        }
    }

    public int getItemsAddedAmount() {
        return itemsAddedAmount;
    }

    public void setItemsAddedAmount(int itemsAddedAmount) {
        this.itemsAddedAmount = itemsAddedAmount;
    }

    @Override
    public String toString() {
        return "AdminWarehouse{" + "itemsAddedAmount=" + itemsAddedAmount + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdminWarehouse other = (AdminWarehouse) obj;
        if (this.itemsAddedAmount != other.itemsAddedAmount) {
            return false;
        }
        return true;
    }
    
    
}
