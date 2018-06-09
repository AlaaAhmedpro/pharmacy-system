package pharmacy.MainClasses;

import java.io.Serializable;
import java.util.ArrayList;
import pharmacy.ExceptionClasses.InvalidInputException;
import pharmacy.PersonClasses.Customer;
import pharmacy.ExtraClasses.DateTime;
import pharmacy.PersonClasses.Delivery;
import pharmacy.ProductClasses.Product;

public class Order implements Serializable{
    //--------(Attributes)----------//
    private final int orderId;
    private double orderPrice;
    private String orderAddress;
    private Customer orderCustomer;
    private Delivery orderDelivery;
    private String orderDate;
    private String orderTime;
    private String orderStatus;
    private ArrayList<Product> productList;

    //---------(Methods)-------------//
    private int generateId(){
        return PharmacyController.OrderTable.getMaxId() + 1;
    }
    
    //Constructor Methods
    public Order(double orderPrice, Customer c , ArrayList<Product> productList) throws InvalidInputException{
        this.orderId = generateId();
        this.orderPrice = orderPrice;
        this.orderCustomer = c;
        this.orderStatus = "Pending";
        this.orderDate = new DateTime().getDate();
        this.orderTime = new DateTime().getTime();
        this.productList = productList;
    }
    
    //Copy Consturctor
    public Order(Order obj) throws InvalidInputException{
        this.orderId = generateId();
        this.orderPrice = obj.orderPrice;
        this.orderCustomer = obj.orderCustomer;
        this.orderDate = new DateTime().getDate();
        this.orderTime = new DateTime().getTime();
        this.orderStatus = obj.orderStatus;
    }
    //Mutator Methods (Setter)
    public void setOrderCustomer(Customer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public void setOrderDelivery(Delivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }
    

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    
    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
        this.orderPrice = 0;
        for (Product current : this.productList){
            orderPrice += current.getTotalPrice();
        }
    }

    public boolean addProduct(Product p) {
        try{
            productList.add(p);
            orderPrice += p.getTotalPrice();
            return true;
        }
        catch(Exception e){
            return false;
        }

    }
    public Product deleteProduct(int productCode){
        for (int i = 0; i < productList.size(); i++){
            if (productList.get(i).getCode() == productCode){
                orderPrice -= productList.get(i).getTotalPrice();
                return productList.remove(i);
            }
        }
        return null;
    }
    
    //Accessor Methods (Getter)
    public int getOrderId(){ 
        return orderId;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public Customer getOrderCustomer() {
        return orderCustomer;
    }

    public Delivery getOrderDelivery() {
        return orderDelivery;
    }
    
    public String getOrderAddress() {
        return orderAddress;
    }
    

    public String getOrderStatus() {
        return orderStatus; 
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }
    
    public Product productOfIndex(int index) {
        try{
            return productList.get(index); 
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.print(e.toString());
            return null;
        }
    }
    
    public Product getProduct(int productCode) {
        for (int i = 0; i < this.productList.size(); i++){
            if (this.productList.get(i).getCode() == productCode)
                return this.productList.get(i);
        }
        return null;
    }
    
    public int getProductIndex(int productCode) {
        for (int i = 0; i < this.productList.size(); i++){
            if (this.productList.get(i).getCode() == productCode)
                return i;
        }
        return -1;
    }
    
    public int getProductList_Size(){
        return productList.size();
    }
    
    public ArrayList<Product> getProductList() {
        return productList; 
    }
    
    //Additional Methods
    public String toStringProductList(){
        return this.productList.toString();
    }
    
    @Override
    public String toString(){
        return "Order ID: " + this.orderId + ", Order Customer: " + this.orderCustomer.getFirstName() + ", Order Price: " + this.orderPrice + ", Order Location: " + this.orderAddress + ", Order Status: " + this.orderStatus;
    }

    @Override
    public boolean equals(Object p){
        if (!(p instanceof Order)) return false;
        return ((Order)p).getOrderId()== this.getOrderId();
    }
    
}
