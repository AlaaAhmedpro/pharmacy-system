package pharmacy.MainClasses;

import java.io.*;
import java.util.ArrayList;
import pharmacy.ProductClasses.*;
import pharmacy.PersonClasses.*;
import pharmacy.ExtraClasses.*;
import pharmacy.ExceptionClasses.*;

public abstract class PharmacyController {
    public static abstract class OrderTable {
        //--------(Attributes)----------//
        private static ArrayList<Order> orderContainer = new ArrayList<Order>();

        //---------(Methods)-------------//
        public static boolean addOrder(Order o) {
            try {
                orderContainer.add(o);
                return true;
            }catch(Exception e){
                return false;
            }
        }

        public static Order deleteOrder(int orderID) throws NullPointerException{
            Order Temp = null;
            for (int i = 0; i < orderContainer.size(); i++) {
                if (orderContainer.get(i).getOrderId() == orderID) {
                    Temp = orderContainer.remove(i);
                    break;
                }
            }
            if (Temp != null)
                return Temp;
            else
                throw new NullPointerException("There is no order with ID (" + orderID + ")");
        }
        
        public static Order getOrder(int orderID) {
            for (int i = 0; i < orderContainer.size(); i++) {
                if (orderContainer.get(i).getOrderId() == orderID) {
                    return orderContainer.get(i);
                }
            }
            return null;
        }
        
        public static Order getOrderOfIndex(int index) throws ArrayIndexOutOfBoundsException {
            if (index >= 0 && index < orderContainer.size())
                return orderContainer.get(index);
            else
                throw new ArrayIndexOutOfBoundsException("Invalid index (" + index + ") Out of bounds");
        }

        public static int getOrders_Number(){
            return orderContainer.size();
        }
        
        public static boolean updateOrder_Customer(int orderID, Customer c){
            Order temp = getOrder(orderID);
            if (temp != null){
                temp.setOrderCustomer(c);
                return true;
            }
            return false;
        }
        
        public static boolean updateOrder_AddNewProduct(int orderID, Product p){
            Order temp = getOrder(orderID);
            if (temp != null){
                temp.addProduct(p);
                return true;
            }
            return false;
        }
        
        
        public static int getMaxId(){
            int maxId = 0;
            for (Order o : orderContainer){
                if (o.getOrderId()> maxId)
                    maxId = o.getOrderId();
            }
            return maxId;
        }
        
        public static void WriteToFile() throws IOException{
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = new FileOutputStream("orders.ser");
                oos = new ObjectOutputStream(fos);
                    
                oos.writeObject(orderContainer);  
            }
            catch (IOException e){
                    throw e;
            }
            finally{
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            }
        }
        
        public static void ReadFromFile() throws IOException, ClassNotFoundException{
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            
            try {
                fis = new FileInputStream("orders.ser");
                ois = new ObjectInputStream(fis);
            
                orderContainer = (ArrayList<Order>)ois.readObject();
            
            }
            catch (IOException | ClassNotFoundException e){
                throw e;
            }
            finally {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            }
        }
       
    }
    public static abstract class PeopleTable {
        //--------(Attributes)----------//
        private static ArrayList < Person > peopleContainer = new ArrayList < Person > ();
        
        //---------(Methods)-------------//
        public static boolean addPerson(Person p) throws RepeatedObjectException{
            Person personWithSameID = getPersonByUsername(p.getUsername());
            if (personWithSameID == null)
                return peopleContainer.add(p);
            else
                throw new RepeatedObjectException("This username taken already, try another one !");
        }
        
        public static Person deletePerson(int personID) throws NullPointerException {
            Person Temp = null;
            for (int i = 0; i < peopleContainer.size(); i++) {
                if (peopleContainer.get(i).getID() == personID) {
                    Temp = peopleContainer.remove(i);
                    break;
                }
            }
            if (Temp != null)
                return Temp;
            else
                throw new NullPointerException("There is no person with ID (" + personID + ")");
        }
        
        public static Person getPersonByID(int personID) throws NullPointerException{
            Person Temp = null;
            for (int i = 0; i < peopleContainer.size(); i++){
                if (peopleContainer.get(i).getID() == personID) {
                    Temp = peopleContainer.get(i);
                    break;
                }
            }
            return Temp;
        }
        
        public static Person getPersonByUsername(String username) {
            Person Temp = null;
            for (int i = 0; i < peopleContainer.size(); i++){
                if (peopleContainer.get(i).getUsername().equalsIgnoreCase(username)) {
                    Temp = peopleContainer.get(i);
                    break;
                }
            }
            return Temp;
        }
        
        public static Person getPersonByFirstname(String username) {
            Person Temp = null;
            for (int i = 0; i < peopleContainer.size(); i++){
                if (peopleContainer.get(i).getFirstName().equalsIgnoreCase(username)) {
                    Temp = peopleContainer.get(i);
                    break;
                }
            }
            return Temp;
        }
        
        public static Person getPersonOfIndex(int index) throws ArrayIndexOutOfBoundsException{
            if (index >= 0 && index < peopleContainer.size())
                return peopleContainer.get(index);
            else
                throw new ArrayIndexOutOfBoundsException("Invalid index (" + index + ") Out of bounds");
        }
        
        public static int getPeople_Number(){
            return peopleContainer.size();
        }
        
        public static boolean updatePerson_firstName(int personID, String newName){
            Person temp = getPersonByID(personID);
            if (temp != null){
               temp.setFirstName(newName);
               return true;
            }
            return false;
        }
        
        public static boolean updatePerson_lastName(int personID, String newName){
            Person temp = getPersonByID(personID);
            if (temp != null){
               temp.setLastName(newName);
               return true;
            }
            return false; 
        }
        
        public static boolean updatePerson_Phone(int personID, String number){
            Person temp = getPersonByID(personID);
            if (temp != null){
               temp.setPhone(number);
               return true;
            }
            return false;
        }
        
        public static boolean updatePerson_Adress(int personID, String address){
            Person temp = getPersonByID(personID);
            if (temp != null){
               temp.setFirstName(address);
               return true;
            }
            return false;
        }
        
        public static boolean updatePerson_DateOfBirth(int personID, DateTime dt){
            Person temp = getPersonByID(personID);
            if (temp != null){
               temp.setDateOfBirth(dt);
               return true;
            }
            return false;
        } 
        
        public static int getMaxId(){
            int maxId = 0;
            for (Person p : peopleContainer){
                if (p.getID() > maxId)
                    maxId = p.getID();
            }
            return maxId;
        }
        
        public static void WriteToFile() throws IOException{
            
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            
            try {
                fos = new FileOutputStream("persons.ser");
                oos = new ObjectOutputStream(fos);
                    
                oos.writeObject(peopleContainer);
                    
            }
            catch (IOException e){
                    throw e;
            }
            finally {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            }
        }
        
        public static void ReadFromFile() throws IOException, ClassNotFoundException{
            
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            
            try {
                fis = new FileInputStream("persons.ser");
                ois = new ObjectInputStream(fis);
            
                peopleContainer = (ArrayList<Person>)ois.readObject();
            
            }
            catch (IOException | ClassNotFoundException e){
                throw e;
            }
            finally {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            }
        }
        //---------------GUI Functions----------------//
        public static Person userExist(String username) throws InvalidLoginException{
            Person Temp = null;
            for (int i = 0; i < PeopleTable.getPeople_Number(); i++){
                if (PeopleTable.getPersonOfIndex(i).getUsername().equalsIgnoreCase(username)){
                    Temp = PeopleTable.getPersonOfIndex(i);
                }
            }
            if (Temp != null)
                return Temp;
            else
                throw new InvalidLoginException("There is no person with username (" + username + ")");
        }
        
        public static Person Login(String username, String password) throws InvalidLoginException {
            Person Temp = userExist(username);
            if (Temp.getPassword().equals(password))
                return Temp;
            else
                throw new InvalidLoginException("Invalid Password for username ("+ username + ")");
        }
        
        public static ArrayList<String> getAvailableDelivery(){
            ArrayList <String> availableDelivery = new ArrayList<>();
            for (int i = 0; i < peopleContainer.size(); i++){
                if (peopleContainer.get(i) instanceof Delivery) {
                    if (((Delivery) peopleContainer.get(i)).isAvailable()) {
                        availableDelivery.add(((Delivery) peopleContainer.get(i)).getFirstName());
                    }
                }
            }
            return availableDelivery;

        }
        
    }
    public static abstract class ProductTable{
        //--------(Attributes)----------//
        private static ArrayList < Product > productContainer = new ArrayList < Product > ();
        
       //---------(Methods)-------------//
        
        public static boolean addProduct(Product p){
            try {
                productContainer.add(p);
                return true;
            }
            catch(Exception e){
                return false;
            }
        }
        
        public static Product deleteProduct(int productCode){    
            Product Temp = null;
            for (int i = 0; i < productContainer.size(); i++) {
                if (productContainer.get(i).getCode()== productCode) {
                    Temp = productContainer.remove(i);
                    break;
                }
            }
            if (Temp != null)
                return Temp;
            else
                throw new NullPointerException("There is no product with code (" + productCode + ")");
        }
        
        public static Product getProduct(int productCode){
            for (int i = 0; i < productContainer.size(); i++){
                if (productContainer.get(i).getCode() == productCode){
                    return productContainer.get(i);
                }
            }
            return null; 
        }
        
        public static int getProducts_Number(){
            return productContainer.size();
        }
        
        public static Product getProductOfIndex(int index) throws ArrayIndexOutOfBoundsException{
            if (index >= 0 && index < productContainer.size())
                return productContainer.get(index);
            else
                throw new ArrayIndexOutOfBoundsException("Invalid index (" + index + ") Out of bounds");
        }
        
        public static boolean updateProduct_name(int productCode, String name){
            Product temp = getProduct(productCode);
            if (temp != null){
                temp.setName(name);
                return true;
            }
            return false;
        }
        
        public static boolean updateProduct_price(int productCode, double price){
            Product temp = getProduct(productCode);
            if (temp != null){
                temp.setPrice(price);
                return true;
            }
            return false;
        }
        public static boolean updateProduct_quantity(int productCode, int q){
            Product temp = getProduct(productCode);
            if (temp != null){
                temp.setQuantity(q);
                return true;
            }
            return false;
        }
        
        public static void WriteToFile() throws IOException{
            
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            
            try {
                fos = new FileOutputStream("products.ser");
                oos = new ObjectOutputStream(fos);
                    
                oos.writeObject(productContainer);
                    
            }
            catch (IOException e){
                    throw e;
            }
            finally {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            }
        }
        
        public static void ReadFromFile() throws IOException, ClassNotFoundException{
            
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            
            try {
                fis = new FileInputStream("products.ser");
                ois = new ObjectInputStream(fis);
            
                productContainer = (ArrayList<Product>)ois.readObject();
            
            }
            catch (IOException | ClassNotFoundException e){
                throw e;
            }
            finally {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            }
        }
    }
    public static abstract class PrescriptionTable {
        private static ArrayList<Prescription> PrescriptionContainer = new ArrayList<Prescription>();

        public static boolean addPrescription(Prescription newPrescription) {
            try {
                PrescriptionContainer.add(newPrescription);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        
        public static Prescription deletePrescription(int prescriptionCode){
            for (int i = 0; i < PrescriptionContainer.size(); i++) {
                if (PrescriptionContainer.get(i).getCode()== prescriptionCode) {
                    return PrescriptionContainer.get(i);
                }
            }
            return null;
        }
        
        public static Prescription getPrescription(int prescriptionCode){
            for (int i = 0; i < PrescriptionContainer.size(); i++){
                if (PrescriptionContainer.get(i).getCode() == prescriptionCode){
                    return PrescriptionContainer.get(i);
                }
            }
            return null;
        }
        
        public static boolean updatePrescription_Date(int prescriptionCode, DateTime dt){
            Prescription temp = getPrescription(prescriptionCode);
            if (temp != null){
               temp.setDate(dt);
               return true;
            }
            return false;
        }
        
        public static boolean prescriptionContain_Medication(String prescriptionCode , PrescriptionMedication PM) {
            for (Prescription current : PrescriptionContainer) {
                if (prescriptionCode.equals(Integer.toString(current.getCode()))) {
                    if (current.isContainMedication(PM.getCode())) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public static boolean isValidPrescription(String PrescriptionCode , PrescriptionMedication SelectedMedicatin) {

            for (Prescription current : PrescriptionContainer) {
                if (PrescriptionCode.equals(Integer.toString(current.getCode()))) {
                    if (current.isContainMedication(SelectedMedicatin.getCode())) {
                        return true;
                    }
                }
            }

            return false;
        }
        
        public static void WriteToFile() throws IOException{
            
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            
            try {
                fos = new FileOutputStream("prescription.ser");
                oos = new ObjectOutputStream(fos);
                    
                oos.writeObject(PrescriptionContainer);
                    
            }
            catch (IOException e){
                    throw e;
            }
            finally {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            }
        }
        
        public static void ReadFromFile() throws IOException, ClassNotFoundException{
            
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            
            try {
                fis = new FileInputStream("prescription.ser");
                ois = new ObjectInputStream(fis);
            
                PrescriptionContainer = (ArrayList<Prescription>)ois.readObject();
            
            }
            catch (IOException | ClassNotFoundException e){
                throw e;
            } 
            finally {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            }
        }
    }
    
}