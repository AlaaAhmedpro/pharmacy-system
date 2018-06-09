package pharmacy.GUI;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pharmacy.ExceptionClasses.InvalidInputException;
import pharmacy.ExceptionClasses.MissingDataException;
import pharmacy.ExtraClasses.*;
import pharmacy.PersonClasses.*;
import pharmacy.ProductClasses.*;
import pharmacy.MainClasses.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminForm {
    private Staff currentuser;
    private final ObservableList<PrescriptionMedication> tablePM_data  = FXCollections.observableArrayList();
    private final ObservableList<Medication> tableM_data  = FXCollections.observableArrayList();
    private final ObservableList<Cosmetics> tableC_data  = FXCollections.observableArrayList();
    private final ObservableList<Order> tableTotalOrders_data = FXCollections.observableArrayList();
    private final ObservableList<Order> tablePendingOrders_data = FXCollections.observableArrayList();
    private final ObservableList<Order> tableInDelOrders_data = FXCollections.observableArrayList();
    private final ObservableList<Order> tableCompletedOrders_data = FXCollections.observableArrayList();
    private final ObservableList<Customer> tableCustomers_data = FXCollections.observableArrayList();
    private final ObservableList<Product> tableMissing_data = FXCollections.observableArrayList();
    private final ObservableList<Report> tableReport_data = FXCollections.observableArrayList();
    public void setCurrentUser(Staff c){
        currentuser = c;
    }
    
    private void FillMissingItems() {
        tableMissing_data.clear();
        for (int i = 0; i < PharmacyController.ProductTable.getProducts_Number(); i++) {
            if (PharmacyController.ProductTable.getProductOfIndex(i).getQuantity() < 10)
                tableMissing_data.add((Product) PharmacyController.ProductTable.getProductOfIndex(i));
        }
        btnMissingProducts.setText("Missing Items ( " + tableMissing_data.size() + " )");
        lblMissingItems.setText("Missing Items ( " + tableMissing_data.size() + " )");
    }
    private void FillProducts() {
        tablePM_data.clear();
        tableM_data.clear();
        tableC_data.clear();
        for (int i = 0; i < PharmacyController.ProductTable.getProducts_Number(); i++) {
            if (PharmacyController.ProductTable.getProductOfIndex(i) instanceof PrescriptionMedication)
                tablePM_data.add((PrescriptionMedication) PharmacyController.ProductTable.getProductOfIndex(i));
            else if (PharmacyController.ProductTable.getProductOfIndex(i) instanceof Medication)
                tableM_data.add((Medication) PharmacyController.ProductTable.getProductOfIndex(i));
            else if (PharmacyController.ProductTable.getProductOfIndex(i) instanceof Cosmetics)
                tableC_data.add((Cosmetics) PharmacyController.ProductTable.getProductOfIndex(i));
        }
        btnPM.setText("Prescription Medications ( " + tablePM_data.size() + " )");
        btnM.setText("Medications ( " + tableM_data.size() + " )");
        btnC.setText("Cosmetics ( " + tableC_data.size() + " )");
        btnBrowseProducts.setText("Browse Products ( " + (tablePM_data.size() + tableM_data.size() + tableC_data.size())  + " )");
        lblBrowseProducts.setText("Browse Customers ( " + (tablePM_data.size() + tableM_data.size() + tableC_data.size()) + " )");
    }
    private void FillCustomers () {
        tableCustomers_data.clear();
        for (int i = 0; i < PharmacyController.PeopleTable.getPeople_Number(); i++) {
            if (PharmacyController.PeopleTable.getPersonOfIndex(i) instanceof Customer)
                tableCustomers_data.add((Customer) PharmacyController.PeopleTable.getPersonOfIndex(i));
        }
        btnBrowseCustomers.setText("Browse Customers ( " + tableCustomers_data.size() + " )");
        lblBrowseCustomers.setText("Browse Customers ( " + tableCustomers_data.size() + " )");
    }
    private void FillOrders () {
        tableTotalOrders_data.clear();
        tablePendingOrders_data.clear();
        tableInDelOrders_data.clear();
        tableCompletedOrders_data.clear();
        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            tableTotalOrders_data.add(PharmacyController.OrderTable.getOrderOfIndex(i));
            if (PharmacyController.OrderTable.getOrderOfIndex(i).getOrderStatus().equals("Pending"))
                tablePendingOrders_data.add(PharmacyController.OrderTable.getOrderOfIndex(i));
            else if (PharmacyController.OrderTable.getOrderOfIndex(i).getOrderStatus().equals("Is delivering .."))
                tableInDelOrders_data.add(PharmacyController.OrderTable.getOrderOfIndex(i));
            else if (PharmacyController.OrderTable.getOrderOfIndex(i).getOrderStatus().equals("Completed"))
                tableCompletedOrders_data.add(PharmacyController.OrderTable.getOrderOfIndex(i)); 
        }
        
        btnPendingOrders.setText("Browse Orders ( " + tableTotalOrders_data.size() + " )");
        lblPendingOrders.setText("Browse Orders ( " + tableTotalOrders_data.size() + " )");
        
        btnTotalOrders2.setText("All Orders ( " + tableTotalOrders_data.size() + " )");
        btnPendingOrders2.setText("Pending Orders ( " + tablePendingOrders_data.size() + " )");
        btnInDelOrders2.setText("In Delivering Orders ( " + tableInDelOrders_data.size() + " )");
        btnCompletedOrders2.setText("Completed Orders ( " + tableCompletedOrders_data.size() + " )");
    }
    private void FillReport() {
        tableReport_data.clear();
        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            Order currentOrder = PharmacyController.OrderTable.getOrderOfIndex(i);
            if (tableReport_data.isEmpty()) {
                Report newDay = new Report(1 , currentOrder.getOrderDate(), 1 , currentOrder.getOrderPrice(),100);
                tableReport_data.add(newDay);
            } else {
                Report lastElementInReport = tableReport_data.get(tableReport_data.size() - 1);
                if (currentOrder.getOrderDate().equals(lastElementInReport.getDate())) {
                    lastElementInReport.setNumSales(lastElementInReport.getNumSales() + 1);
                    lastElementInReport.setTotalSales(lastElementInReport.getTotalSales() + currentOrder.getOrderPrice());
                    if (tableReport_data.size() > 1)
                        lastElementInReport.setSalesPercentage(((lastElementInReport.getTotalSales() - tableReport_data.get(tableReport_data.size() - 2).getTotalSales()) / tableReport_data.get(tableReport_data.size() - 2).getTotalSales())*100);
                } else {
                    Report newDay = new Report(lastElementInReport.getId() + 1, currentOrder.getOrderDate(), 1, currentOrder.getOrderPrice(), (currentOrder.getOrderPrice()- lastElementInReport.getTotalSales())/lastElementInReport.getTotalSales()*100);
                    tableReport_data.add(newDay);
                }
            }
        }
    }
    
    private void btnAssignOrderToDelivery_OnClick() throws MissingDataException{
        String selectedDelivery = (String)CBAvailableDeliverys.getSelectionModel().getSelectedItem();
        Order selectedOrder = (Order)tableOrders.getSelectionModel().getSelectedItem();
        if (selectedDelivery == null) {
            throw new MissingDataException("You must choose a delivery man!");
        } else if (selectedOrder == null) {
            throw new MissingDataException("You must choose order !");
        } else {
            PharmacyController.OrderTable.getOrder(selectedOrder.getOrderId()).setOrderStatus("Is delivering ..");
            MsgBox.ShowMsg("DONE!","Order has been assigned to " + selectedDelivery , MsgBox.type.success);
            CBAvailableDeliverys.getItems().remove(selectedDelivery);
            ((Delivery)PharmacyController.PeopleTable.getPersonByFirstname(selectedDelivery)).setBusy();
            ((Delivery)PharmacyController.PeopleTable.getPersonByFirstname(selectedDelivery)).addOrder(selectedOrder);
            FillOrders();
            lblPendingOrders.setText("Pending Orders ( " + tablePendingOrders_data.size() + " )");
            CBAvailableDeliverys.valueProperty().set(null);
            CBAvailableDeliverys.setDisable(true);
            btnAssign.setDisable(true);
            btnOrderDetails.setDisable(true);
            
            try {
                PharmacyController.OrderTable.WriteToFile();
                PharmacyController.PeopleTable.WriteToFile();
            } catch (IOException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            }
        }
    }
    private void btnSupply_OnClick() throws NumberFormatException , InvalidInputException , MissingDataException{
        if (tableMissing.getSelectionModel().getSelectedItem() != null) {
            Product p = (Product) tableMissing.getSelectionModel().getSelectedItem();
            if (txtQtyToSupply.getText().isEmpty())
                throw new MissingDataException("Ente the quantity you want to supply");
            else if (Integer.parseInt(txtQtyToSupply.getText()) < 1)
                throw new InvalidInputException("You should enter a positive integer number only in quantity field");
            else
                p.setQuantity(p.getQuantity() + Integer.parseInt(txtQtyToSupply.getText()));
            
            try {
                PharmacyController.ProductTable.WriteToFile();
            } catch (IOException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            }
            FillMissingItems();
        } else {
            throw new MissingDataException("You must select the product first");
        }
    }
    private void btnSave_OnClick() throws MissingDataException{
        if (p5_txtFirstname.getText().equals(""))
            throw new MissingDataException("First name field is empty !");
        
        if (p5_txtLastname.getText().equals(""))
            throw new MissingDataException("Last name field is empty !");
        
        if (p5_txtUsername.getText().equals(""))
            throw new MissingDataException("Username field is empty !");
        
        if (p5_txtPhone.getText().equals(""))
            throw new MissingDataException("Phone field is empty !");
        
        if (!p5_txtPassword.getText().equals(""))
            currentuser.setPassword(p5_txtPassword.getText());
        
        
        currentuser.setUsername(p5_txtUsername.getText());
        currentuser.setPhone(p5_txtPhone.getText());
        currentuser.setGender(p5_CbGender.getValue().toString());
        try {
            currentuser.setDateOfBirth(Integer.parseInt(p5_CbDay.getValue().toString()),Integer.parseInt(p5_CbMonth.getValue().toString()),Integer.parseInt(p5_CbYear.getValue().toString()));
        } catch (InvalidInputException ex) {
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            return;
        }
        currentuser.setFirstName(p5_txtFirstname.getText());
        currentuser.setLastName(p5_txtLastname.getText());
        
        lblUsername.setText(currentuser.getFirstName() + " " + currentuser.getLastName());
        try {
            PharmacyController.PeopleTable.WriteToFile();
        } catch (IOException ex) {
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
        }
    }
    
    private double countTotalSales () {
        double sum = 0.0;
        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            sum += PharmacyController.OrderTable.getOrderOfIndex(i).getOrderPrice();
        }
        return sum;
    }
    
    private String getTopCustomer () {
        HashMap <String , Integer> NumberOrdersForEachCustomer = new HashMap<>();
        String TopCustomer = "";
        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            String customer = PharmacyController.OrderTable.getOrderOfIndex(i).getOrderCustomer().getFirstName();
            if (NumberOrdersForEachCustomer.containsKey(customer)) {
                int value = NumberOrdersForEachCustomer.get(customer);
                NumberOrdersForEachCustomer.put(customer, value + 1);
            } else {
                NumberOrdersForEachCustomer.put(customer, 1);
            }
        }
    
        int Max = 0;
        
        for (HashMap.Entry<String, Integer> entry : NumberOrdersForEachCustomer.entrySet()) {
            String ThisCustomer = entry.getKey();
            int value = entry.getValue();

            if (value > Max) {
                Max = value;
                TopCustomer = ThisCustomer;
            }
        }
        return TopCustomer;
    }
    
    
        private String getTopProduct () {
        HashMap <String , Integer> NumberSalesForEachProduct = new HashMap<>();
        String TopProduct = "";
        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            Order thisOrder = PharmacyController.OrderTable.getOrderOfIndex(i);
            for (Product item : thisOrder.getProductList()) {
                String ProductName = item.getName();
                if (NumberSalesForEachProduct.containsKey(ProductName)) {
                    int value = NumberSalesForEachProduct.get(ProductName);
                    NumberSalesForEachProduct.put(ProductName, value + item.getQuantity());
                } else {
                    NumberSalesForEachProduct.put(ProductName, item.getQuantity());
                }
            }
            
        }
    
        int Max = 0;
        
        for (HashMap.Entry<String, Integer> entry : NumberSalesForEachProduct.entrySet()) {

            String ThisProduct = entry.getKey();
            int value = entry.getValue();

            if (value > Max) {
                Max = value;
                TopProduct = ThisProduct;
            }
        }
        return TopProduct;
    }
    
    public void Show() {
        InitializeComponents();
        
        FillProducts();
        FillOrders();
        FillMissingItems();
        FillCustomers();
        FillReport();

        
        
        
        Scene scene = new Scene(Pane_Main , 1160, 600);
        scene.getStylesheets().add("pharmacy/GUI/style.css");
        MainWindow.setScene(scene);
        MainWindow.show();
        
        
        btnBrowseProducts.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_BrowseProducts);
            Pane_Selected.setLayoutY(100);
            MainWindow.setTitle("Pharmacy - Browse Products");
            FillProducts();
        });
        
        
        btnPM.setOnMouseClicked(event -> {
            tablePM.setVisible(true);
            tableM.setVisible(false);
            tableC.setVisible(false);
            btnPM.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
            btnM.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnC.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
        });
        
        
        btnM.setOnMouseClicked(event -> {
            tablePM.setVisible(false);
            tableM.setVisible(true);
            tableC.setVisible(false);
            btnPM.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnM.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
            btnC.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
        });
        
        btnC.setOnMouseClicked(event -> {
            tablePM.setVisible(false);
            tableM.setVisible(false);
            tableC.setVisible(true);
            btnPM.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnM.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnC.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
        });
        
        btnTotalOrders2.setOnMouseClicked(event -> {
            tableOrders.setItems(tableTotalOrders_data);
            btnAssign.setVisible(false);
            CBAvailableDeliverys.setVisible(false);
            btnTotalOrders2.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
            btnPendingOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnInDelOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnCompletedOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
        });
        
        
        btnPendingOrders2.setOnMouseClicked(event -> {
            btnAssign.setVisible(true);
            CBAvailableDeliverys.setVisible(true);
            tableOrders.setItems(tablePendingOrders_data);
            btnTotalOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnPendingOrders2.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
            btnInDelOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnCompletedOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
        });
        
        btnInDelOrders2.setOnMouseClicked(event -> {
            btnAssign.setVisible(false);
            CBAvailableDeliverys.setVisible(false);
            tableOrders.setItems(tableInDelOrders_data);
            btnTotalOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnPendingOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnInDelOrders2.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
            btnCompletedOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
        });
        
        btnCompletedOrders2.setOnMouseClicked(event -> {
            btnAssign.setVisible(false);
            CBAvailableDeliverys.setVisible(false);
            tableOrders.setItems(tableCompletedOrders_data);
            btnTotalOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnPendingOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnInDelOrders2.setStyle("-fx-background-color: #e7e7f0; -fx-text-fill: #696578");
            btnCompletedOrders2.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
        });
        
        btnPendingOrders.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_Orders_Body);
            Pane_Selected.setLayoutY(160);
            MainWindow.setTitle("Pharmacy - Pendding Orders");
            btnOrderDetails.setDisable(true);
            FillOrders();
        });
        
        btnBrowseCustomers.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_BrowseCustomer_Body);
            Pane_Selected.setLayoutY(220);
            MainWindow.setTitle("Pharmacy - Browse Customers");
            FillCustomers();
        });
        
        tableCustomers.setOnMouseClicked(event -> {
            if (tableCustomers.getSelectionModel().getSelectedItem() != null) {
                btnShowOrders.setDisable(false);
            }
        });
        
        btnShowOrders.setOnMouseClicked(event -> {
            if (tableCustomers.getSelectionModel().getSelectedItem() != null) {
                ShowOrdersForm obj = new ShowOrdersForm();
                obj.View((Customer) tableCustomers.getSelectionModel().getSelectedItem());
            }
            btnShowOrders.setDisable(true);
        });
        
        
        btnMissingProducts.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_Missing_Body);
            Pane_Selected.setLayoutY(280);
            MainWindow.setTitle("Pharmacy - Missing Items");
            FillMissingItems();
        });
        
        
        btnReport.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_Report_Body);
            Pane_Selected.setLayoutY(280);
            MainWindow.setTitle("Pharmacy - Generate Report");
            FillReport();
        });
        
        
        btnSupply.setOnMouseClicked(event -> {
            try {
                btnSupply_OnClick();
            } catch (InvalidInputException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            } catch (MissingDataException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            } catch (NumberFormatException ex) {
                MsgBox.ShowMsg("OPS!!", "You must enter only numbers" , MsgBox.type.error);
            }
        });
        
        
        btnSetting.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_Setting_Body);
            Pane_Selected.setLayoutY(340);
            MainWindow.setTitle("Pharmacy - Settings");
        });
        
        
        
        btnLogout.setOnMouseClicked(event -> {
            if (MsgBox.ShowMsg("Logout","Are you sure you want to log out and quit ?",MsgBox.type.warning)) {
                LoginForm obj = new LoginForm();
                obj.start(new Stage());
                MainWindow.hide();
            }
        });
        
        
        tableOrders.setOnMouseClicked(event -> {
            if (tableOrders.getSelectionModel().getSelectedItem() != null) {
                btnAssign.setDisable(false);
                btnOrderDetails.setDisable(false);
                CBAvailableDeliverys.setDisable(false);
            }
        });
        
        btnOrderDetails.setOnMouseClicked(event -> {
            if (tableOrders.getSelectionModel().getSelectedItem() != null) {
                OrderDetailsForm.View((Order)tableOrders.getSelectionModel().getSelectedItem());
            }
        });
        
        btnAssign.setOnMouseClicked((MouseEvent event) -> {
            try {
                btnAssignOrderToDelivery_OnClick();
            } catch (MissingDataException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            }
        });
        
        tableMissing.setOnMouseClicked(event -> {
            if (tableMissing.getSelectionModel().getSelectedItem() != null) {
                btnSupply.setDisable(false);
            }
        });
        
        
        p5_btnSubmit.setOnMouseClicked(event -> {
            try {
                btnSave_OnClick();
            } catch (MissingDataException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
                return;
            }
            
            p5_CbDay.setDisable(!false);
            p5_CbGender.setDisable(!false);
            p5_CbMonth.setDisable(!false);
            p5_CbYear.setDisable(!false);
            p5_btnSubmit.setDisable(!false);
            p5_txtPassword.setEditable(!true);
            p5_txtPhone.setEditable(!true);
            p5_txtUsername.setEditable(!true);
            p5_txtFirstname.setEditable(!true);
            p5_txtLastname.setEditable(!true);
            
            p5_txtPassword.setOpacity(.3);
            p5_txtPhone.setOpacity(.3);
            p5_txtUsername.setOpacity(.3);
            p5_txtFirstname.setOpacity(.3);
            p5_txtLastname.setOpacity(.3);
            
            p5_txtPassword.setCursor(Cursor.DEFAULT);
            p5_txtPhone.setCursor(Cursor.DEFAULT);
            p5_txtUsername.setCursor(Cursor.DEFAULT);
            p5_txtFirstname.setCursor(Cursor.DEFAULT);
            p5_txtLastname.setCursor(Cursor.DEFAULT);
            
            
            p5_btnEdit.setDisable(!true);
            p5_btnEdit.setVisible(!false);
            p5_btnCancel.setVisible(!true);
            p5_btnCancel.setDisable(!false);
            Pane_SideMenu.setDisable(!true);
            
        });
        
        
        p5_btnEdit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                p5_CbDay.setDisable(false);
                p5_CbGender.setDisable(false);
                p5_CbMonth.setDisable(false);
                p5_CbYear.setDisable(false);
                p5_btnSubmit.setDisable(false);
                p5_txtPassword.setEditable(true);
                p5_txtPhone.setEditable(true);
                p5_txtUsername.setEditable(true);
                p5_txtFirstname.setEditable(true);
                p5_txtLastname.setEditable(true);
                
                p5_txtPassword.setOpacity(1);
                p5_txtPhone.setOpacity(1);
                p5_txtUsername.setOpacity(1);
                p5_txtFirstname.setOpacity(1);
                p5_txtLastname.setOpacity(1);
                
                
                p5_txtPassword.setCursor(Cursor.TEXT);
                p5_txtPhone.setCursor(Cursor.TEXT);
                p5_txtUsername.setCursor(Cursor.TEXT);
                p5_txtFirstname.setCursor(Cursor.TEXT);
                p5_txtLastname.setCursor(Cursor.TEXT);
                
                p5_btnEdit.setDisable(true);
                p5_btnEdit.setVisible(false);
                p5_btnCancel.setVisible(true);
                p5_btnCancel.setDisable(false);
                Pane_SideMenu.setDisable(true);
                
            }
        });
        
        p5_btnCancel.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                p5_CbDay.setDisable(true);
                p5_CbGender.setDisable(true);
                p5_CbMonth.setDisable(true);
                p5_CbYear.setDisable(true);
                p5_btnSubmit.setDisable(true);
                p5_txtPassword.setEditable(false);
                p5_txtPhone.setEditable(false);
                p5_txtUsername.setEditable(false);
                p5_txtFirstname.setEditable(false);
                p5_txtLastname.setEditable(false);
                
                p5_txtPassword.setOpacity(.3);
                p5_txtPhone.setOpacity(.3);
                p5_txtUsername.setOpacity(.3);
                p5_txtFirstname.setOpacity(.3);
                p5_txtLastname.setOpacity(.3);
                
                p5_txtPassword.setCursor(Cursor.DEFAULT);
                p5_txtPhone.setCursor(Cursor.DEFAULT);
                p5_txtUsername.setCursor(Cursor.DEFAULT);
                p5_txtFirstname.setCursor(Cursor.DEFAULT);
                p5_txtLastname.setCursor(Cursor.DEFAULT);
                
                p5_btnCancel.setDisable(true);
                p5_btnCancel.setVisible(false);
                p5_btnEdit.setVisible(true);
                p5_btnEdit.setDisable(false);
                Pane_SideMenu.setDisable(false);
                
                //Old Values
                p5_txtPassword.clear();
                p5_txtPhone.setText(currentuser.getPhone());
                p5_txtUsername.setText(currentuser.getUsername());
                p5_txtFirstname.setText(currentuser.getFirstName());
                p5_txtLastname.setText(currentuser.getLastName());
                if (currentuser.getGender().equalsIgnoreCase("Male"))
                    p5_CbGender.getSelectionModel().select(0);
                else
                    p5_CbGender.getSelectionModel().select(1);
                p5_CbDay.getSelectionModel().select(currentuser.getDateOfBirth().getDay()-1);
                p5_CbMonth.getSelectionModel().select(currentuser.getDateOfBirth().getMonth()-1);
                p5_CbYear.getSelectionModel().select(currentuser.getDateOfBirth().getYear()-1970);
                
            }
        });
        
    }
    Stage MainWindow;
    Pane Pane_Main;
    //SideMenu
    Label lblUsername;
    
    Button btnBrowseProducts;
    Button btnPendingOrders;
    Button btnBrowseCustomers;
    Button btnMissingProducts;
    Button btnReport;
    Button btnSetting;
    Button btnLogout;
    
    Image IMG_User;
    Image IMG_BrowseProducts;
    Image IMG_PendingOrders;
    Image IMG_BrowseCustomers;
    Image IMG_MissingProducts;
    Image IMG_Report;
    Image IMG_Setting;
    Image IMG_Logout;
    
    ImageView IMGV_User;
    ImageView IMGV_BrowseProducts;
    ImageView IMGV_PendingOrders;
    ImageView IMGV_BrowseCustomers;
    ImageView IMGV_MissingProducts;
    ImageView IMGV_Report;
    ImageView IMGV_Setting;
    ImageView IMGV_Logout;
    
    Pane Pane_User;
    Pane Pane_Selected;
    Pane Pane_SideMenu;
    //End SideMenu
    Label lblBrowseProducts;
    
    TableView tablePM;
    TableView tableM;
    TableView tableC;
    
    Pane Pane_BrowseProducts_Header;
    Pane Pane_BrowseProducts;
    
    Button btnPM;
    Button btnM;
    Button btnC;
    
    //--------------------
    Label lblPendingOrders;
    Button btnTotalOrders2;
    Button btnPendingOrders2;
    Button btnInDelOrders2;
    Button btnCompletedOrders2;
    ComboBox CBAvailableDeliverys;
    TableView tableOrders;
    Button btnOrderDetails;
    Button btnAssign;
    Pane Pane_PendingOrders_Header;
    Pane Pane_Orders_Body;
    
    //--------------------
    Label lblBrowseCustomers;
    TableView tableCustomers;
    Button btnShowOrders;
    Pane Pane_BrowseCustomer_Header;
    Pane Pane_BrowseCustomer_Body;
    //----------------------
    Label lblMissingItems;
    Pane Pane_Missing_Header;
    TableView tableMissing;
    Button btnSupply;
    TextField txtQtyToSupply;
    Pane Pane_Missing_Body;
    //----------------------
    Label lblReport;
    Button btntotalOrders;
    Button btntotalSales;
    Button btntopCustomer;
    Button btntopProduct;
    TableView tableReport;
    Pane Pane_Report_Header;
    Pane Pane_Report_Body;
    //Setting
    TextField p5_txtFirstname;
    TextField p5_txtLastname;
    TextField p5_txtUsername;
    PasswordField p5_txtPassword;
    TextField p5_txtPhone;
    ChoiceBox p5_CbGender;
    ChoiceBox p5_CbDay;
    ChoiceBox p5_CbMonth;
    ChoiceBox p5_CbYear;
    TextField p5_txtRole;
    Button p5_btnSubmit;
    Button p5_btnEdit;
    Button p5_btnCancel;
    Pane Pane_Setting_Body;
    //-------
    void InitializeComponents(){
        //-----------------------SideMenu-------------------------------------//
        IMG_User = new Image (AdminForm.class.getResourceAsStream("icons/UserIcon.png"));
        IMGV_User = new ImageView(IMG_User);;
        IMGV_User.setPreserveRatio(true);
        IMGV_User.setFitHeight(25);
        IMGV_User.setLayoutX(25);
        IMGV_User.setLayoutY(25);
        
        lblUsername = new Label(currentuser.getFirstName() + " " + currentuser.getLastName());
        lblUsername.setId("username");
        lblUsername.setLayoutX(70);
        lblUsername.setLayoutY(25);
        
        btnBrowseProducts = new Button("Browse Products ( " + (tablePM_data.size() + tableM_data.size() + tableC_data.size())  + " )");
        btnBrowseProducts.setId("cat");
        btnBrowseProducts.setLayoutX(70);
        btnBrowseProducts.setLayoutY(95);
        btnBrowseProducts.setMinWidth(180);
        btnBrowseProducts.setMinHeight(55);
        
        btnPendingOrders = new Button("Pending Orders ( " + tablePendingOrders_data.size() + " )");
        btnPendingOrders.setId("cat");
        btnPendingOrders.setLayoutX(70);
        btnPendingOrders.setLayoutY(155);
        btnPendingOrders.setMinWidth(180);
        btnPendingOrders.setMinHeight(55);
        
        
        btnBrowseCustomers = new Button("Browse Customers ( " + tableCustomers_data.size() + " )");
        btnBrowseCustomers.setId("cat");
        btnBrowseCustomers.setLayoutX(70);
        btnBrowseCustomers.setLayoutY(215);
        btnBrowseCustomers.setMinWidth(180);
        btnBrowseCustomers.setMinHeight(55);
        
        
        btnMissingProducts = new Button("Missing Items ( " + tableMissing_data.size() + " )");
        btnMissingProducts.setId("cat");
        btnMissingProducts.setLayoutX(70);
        btnMissingProducts.setLayoutY(275);
        btnMissingProducts.setMinWidth(180);
        btnMissingProducts.setMinHeight(55);
        btnMissingProducts.setVisible(false);
        
        btnReport = new Button("Generate Report");
        btnReport.setId("cat");
        btnReport.setLayoutX(70);
        btnReport.setLayoutY(275);
        btnReport.setMinWidth(180);
        btnReport.setMinHeight(55);
        btnReport.setVisible(false);
        
        btnSetting = new Button("Settings");
        btnSetting.setId("cat");
        btnSetting.setLayoutX(70);
        btnSetting.setLayoutY(335);
        btnSetting.setMinWidth(180);
        btnSetting.setMinHeight(55);
        
        btnLogout = new Button("Logout");
        btnLogout.setId("cat");
        btnLogout.setLayoutX(70);
        btnLogout.setLayoutY(395);
        btnLogout.setMinWidth(180);
        btnLogout.setMinHeight(55);
        
        IMG_BrowseProducts = new Image (AdminForm.class.getResourceAsStream("icons/7.png"));
        IMGV_BrowseProducts = new ImageView(IMG_BrowseProducts);
        IMGV_BrowseProducts.setPreserveRatio(true);
        IMGV_BrowseProducts.setFitHeight(25);
        IMGV_BrowseProducts.setLayoutX(25);
        IMGV_BrowseProducts.setLayoutY(110);
        
        IMG_PendingOrders = new Image (AdminForm.class.getResourceAsStream("icons/8.png"));
        IMGV_PendingOrders = new ImageView(IMG_PendingOrders);
        IMGV_PendingOrders.setPreserveRatio(true);
        IMGV_PendingOrders.setFitHeight(25);
        IMGV_PendingOrders.setLayoutX(25);
        IMGV_PendingOrders.setLayoutY(170);
        
        IMG_BrowseCustomers = new Image (AdminForm.class.getResourceAsStream("icons/9.png"));
        IMGV_BrowseCustomers = new ImageView(IMG_BrowseCustomers);
        IMGV_BrowseCustomers.setPreserveRatio(true);
        IMGV_BrowseCustomers.setFitHeight(25);
        IMGV_BrowseCustomers.setLayoutX(25);
        IMGV_BrowseCustomers.setLayoutY(230);
        
        IMG_MissingProducts = new Image (AdminForm.class.getResourceAsStream("icons/10.png"));
        IMGV_MissingProducts = new ImageView(IMG_MissingProducts);
        IMGV_MissingProducts.setPreserveRatio(true);
        IMGV_MissingProducts.setFitHeight(25);
        IMGV_MissingProducts.setLayoutX(25);
        IMGV_MissingProducts.setLayoutY(290);
        IMGV_MissingProducts.setVisible(false);
        
        IMG_Report = new Image (CustomerForm.class.getResourceAsStream("icons/13.png"));
        IMGV_Report = new ImageView(IMG_Report);
        IMGV_Report.setPreserveRatio(true);
        IMGV_Report.setFitHeight(25);
        IMGV_Report.setLayoutX(25);
        IMGV_Report.setLayoutY(290);
        IMGV_Report.setVisible(false);
        
        IMG_Setting = new Image (AdminForm.class.getResourceAsStream("icons/11.png"));
        IMGV_Setting = new ImageView(IMG_Setting);
        IMGV_Setting.setPreserveRatio(true);
        IMGV_Setting.setFitHeight(25);
        IMGV_Setting.setLayoutX(25);
        IMGV_Setting.setLayoutY(350);
        
        IMG_Logout = new Image (AdminForm.class.getResourceAsStream("icons/12.png"));
        IMGV_Logout = new ImageView(IMG_Logout);
        IMGV_Logout.setPreserveRatio(true);
        IMGV_Logout.setFitHeight(25);
        IMGV_Logout.setLayoutX(25);
        IMGV_Logout.setLayoutY(410);
        
        Pane_User = new Pane();
        Pane_User.setId("UserArea");
        Pane_User.setLayoutX(0);
        Pane_User.setLayoutY(0);
        Pane_User.getChildren().setAll(IMGV_User , lblUsername);
        
        Pane_Selected = new Pane();
        Pane_Selected.setMinHeight(45);
        Pane_Selected.setMinWidth(6);
        Pane_Selected.setStyle("-fx-background-color: #e2574c;");
        Pane_Selected.setLayoutY(100);
        
        if (((Staff) currentuser) instanceof AdminWarehouse) {
            btnMissingProducts.setVisible(true);
            IMGV_MissingProducts.setVisible(true);
        } else if (((Staff) currentuser) instanceof AdminReport) {
            btnReport.setVisible(true);
            IMGV_Report.setVisible(true);
        }
        
        Pane_SideMenu = new Pane();
        Pane_SideMenu.getChildren().addAll(Pane_Selected, Pane_User, btnBrowseProducts , btnPendingOrders , btnBrowseCustomers , btnMissingProducts, btnReport , btnSetting , btnLogout , IMGV_BrowseProducts , IMGV_PendingOrders , IMGV_BrowseCustomers , IMGV_MissingProducts, IMGV_Report , IMGV_Setting , IMGV_Logout);
        Pane_SideMenu.setId("Sidebar");
        Pane_SideMenu.setMinWidth(250);
        Pane_SideMenu.setMinHeight(600);
        Pane_SideMenu.setLayoutX(0);
        Pane_SideMenu.setLayoutY(0);
        //-----------------------End SideMenu---------------------------------//
        //-----------------------Browse Product-------------------------------//
        lblBrowseProducts = new Label("Browse Products");
        lblBrowseProducts.setId("Title");
        lblBrowseProducts.setLayoutY(25);
        
        
        Pane_BrowseProducts_Header = new Pane();
        Pane_BrowseProducts_Header.setId("topPage2");
        Pane_BrowseProducts_Header.getChildren().setAll(lblBrowseProducts);
        lblBrowseProducts.layoutXProperty().bind(Pane_BrowseProducts_Header.widthProperty().subtract(lblBrowseProducts.widthProperty()).divide(2));
        
        btnPM = new Button("Prescription Medication ( " + tablePM_data.size() + " )");
        btnPM.setLayoutX(20);
        btnPM.setLayoutY(100);
        btnPM.setMinWidth(277);
        btnPM.setMaxHeight(40);
        btnPM.setMinHeight(40);
        btnPM.setId("tab");
        btnPM.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
        
        btnM = new Button("Medication ( " + tableM_data.size() + " )");
        btnM.setId("tab");
        btnM.setLayoutX(317);
        btnM.setLayoutY(100);
        btnM.setMinWidth(277);
        btnM.setMaxHeight(40);
        btnM.setMinHeight(40);
        
        btnC = new Button("Cosmetics ( " + tableC_data.size() + " )");
        btnC.setId("tab");
        btnC.setLayoutX(614);
        btnC.setLayoutY(100);
        btnC.setMinWidth(277);
        btnC.setMaxHeight(40);
        btnC.setMinHeight(40);
        
        tablePM = new TableView();
        tablePM.setId("productsTable");
        tablePM.setPlaceholder(new Label("There is no products !"));
        tablePM.setLayoutX(20);
        tablePM.setLayoutY(160);
        tablePM.setMinWidth(870); //Edited
        tablePM.setMaxWidth(870); //Edited
        tablePM.setMinHeight(420); //Edited
        tablePM.setMaxHeight(420); //Edited
        tablePM.setEditable(true);
        
        TableColumn name1 = new TableColumn("Product name");
        name1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, String>("name"));
        name1.setMinWidth(110);
        name1.setMaxWidth(110);
        
        TableColumn code1 = new TableColumn("Code");
        code1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, Integer>("code"));
        code1.setMinWidth(102);
        code1.setMaxWidth(102);
        
        
        TableColumn price1 = new TableColumn("Price");
        price1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, Double>("price"));
        price1.setMinWidth(102);
        price1.setMaxWidth(102);
        
        TableColumn quantity1 = new TableColumn("Quantity");
        quantity1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, Integer>("quantity"));
        quantity1.setMinWidth(66);
        quantity1.setMaxWidth(66);
        
        TableColumn purpose1 = new TableColumn("Purpose");
        purpose1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, String>("purpose"));
        purpose1.setMinWidth(102);
        purpose1.setMaxWidth(102);
        
        TableColumn expiredDateS1 = new TableColumn("Expired");
        expiredDateS1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, String>("expiredDateS"));
        expiredDateS1.setMinWidth(80);
        expiredDateS1.setMaxWidth(80);
        
        TableColumn adultDose1 = new TableColumn("Adult dose");
        adultDose1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, String>("adultDose"));
        adultDose1.setMinWidth(102);
        adultDose1.setMaxWidth(102);
        
        TableColumn childDose1 = new TableColumn("Child dose");
        childDose1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, String>("childDose"));
        childDose1.setMinWidth(102);
        childDose1.setMaxWidth(102);
        
        TableColumn manufacturer1 = new TableColumn("Manufacturer");
        manufacturer1.setCellValueFactory(new PropertyValueFactory<PrescriptionMedication, String>("manufacturer"));
        manufacturer1.setMinWidth(102);
        manufacturer1.setMaxWidth(102);
        
        tablePM.getColumns().setAll(name1,code1,price1,quantity1,purpose1,expiredDateS1,adultDose1,childDose1,manufacturer1);
        
        tableM = new TableView();
        tableM.setId("productsTable");
        tableM.setVisible(false);
        tableM.setPlaceholder(new Label("There is no products !"));
        tableM.setLayoutX(20);
        tableM.setLayoutY(160);
        tableM.setMinWidth(870); //Edited
        tableM.setMaxWidth(870); //Edited
        tableM.setMinHeight(420); //Edited
        tableM.setMaxHeight(420); //Edited
        tableM.setEditable(true);
        
        TableColumn name2 = new TableColumn("Product name");
        name2.setCellValueFactory(new PropertyValueFactory<Medication, String>("name"));
        name2.setMinWidth(110);
        name2.setMaxWidth(110);
        
        TableColumn code2 = new TableColumn("Code");
        code2.setCellValueFactory(new PropertyValueFactory<Medication, Integer>("code"));
        code2.setMinWidth(102);
        code2.setMaxWidth(102);
        
        
        TableColumn price2 = new TableColumn("Price");
        price2.setCellValueFactory(new PropertyValueFactory<Medication, Double>("price"));
        price2.setMinWidth(102);
        price2.setMaxWidth(102);
        
        TableColumn quantity2 = new TableColumn("Quantity");
        quantity2.setCellValueFactory(new PropertyValueFactory<Medication, Integer>("quantity"));
        quantity2.setMinWidth(66);
        quantity2.setMaxWidth(66);
        
        TableColumn purpose2 = new TableColumn("Purpose");
        purpose2.setCellValueFactory(new PropertyValueFactory<Medication, String>("purpose"));
        purpose2.setMinWidth(102);
        purpose2.setMaxWidth(102);
        
        TableColumn expiredDateS2 = new TableColumn("Expired");
        expiredDateS2.setCellValueFactory(new PropertyValueFactory<Medication, String>("expiredDateS"));
        expiredDateS2.setMinWidth(80);
        expiredDateS2.setMaxWidth(80);
        
        TableColumn adultDose2 = new TableColumn("Adult dose");
        adultDose2.setCellValueFactory(new PropertyValueFactory<Medication, String>("adultDose"));
        adultDose2.setMinWidth(102);
        adultDose2.setMaxWidth(102);
        
        TableColumn childDose2 = new TableColumn("Child dose");
        childDose2.setCellValueFactory(new PropertyValueFactory<Medication, String>("childDose"));
        childDose2.setMinWidth(102);
        childDose2.setMaxWidth(102);
        
        TableColumn manufacturer2 = new TableColumn("Manufacturer");
        manufacturer2.setCellValueFactory(new PropertyValueFactory<Medication, String>("manufacturer"));
        manufacturer2.setMinWidth(102);
        manufacturer2.setMaxWidth(102);
        
        tableM.getColumns().setAll(name2,code2,price2,quantity2,purpose2,expiredDateS2,adultDose2,childDose2,manufacturer2);
        
        
        tableC = new TableView();
        tableC.setId("productsTable");
        tableC.setVisible(false);
        tableC.setPlaceholder(new Label("There is no products !"));
        tableC.setLayoutX(20);
        tableC.setLayoutY(160);
        tableC.setMinWidth(870); //Edited
        tableC.setMaxWidth(870); //Edited
        tableC.setMinHeight(420); //Edited
        tableC.setMaxHeight(420); //Edited
        tableC.setEditable(true);
        
        TableColumn name3 = new TableColumn("Product name");
        name3.setCellValueFactory(new PropertyValueFactory<Cosmetics, String>("name"));
        name3.setMinWidth(175);
        name3.setMaxWidth(175);
        
        TableColumn code3 = new TableColumn("Code");
        code3.setCellValueFactory(new PropertyValueFactory<Cosmetics, Integer>("code"));
        code3.setMinWidth(175);
        code3.setMaxWidth(175);
        
        
        TableColumn price3 = new TableColumn("Price");
        price3.setCellValueFactory(new PropertyValueFactory<Cosmetics, Double>("price"));
        price3.setMinWidth(175);
        price3.setMaxWidth(175);
        
        TableColumn quantity3 = new TableColumn("Quantity");
        quantity3.setCellValueFactory(new PropertyValueFactory<Cosmetics, Integer>("quantity"));
        quantity3.setMinWidth(175);
        quantity3.setMaxWidth(175);
        
        TableColumn category3 = new TableColumn("Category");
        category3.setCellValueFactory(new PropertyValueFactory<Cosmetics, String>("category"));
        category3.setMinWidth(173);
        category3.setMaxWidth(173);
        
        tableC.getColumns().setAll(name3,code3,price3,quantity3, category3);
        
        Pane_BrowseProducts = new Pane();
        Pane_BrowseProducts.setLayoutX(250);
        Pane_BrowseProducts.setId("Page1");
        Pane_BrowseProducts.getChildren().setAll(Pane_BrowseProducts_Header , tablePM , tableM, tableC , btnPM, btnM, btnC);
        //--------------------------------------------------------------------//
        lblPendingOrders = new Label("Pending Orders ( " + tablePendingOrders_data.size() + " )");
        lblPendingOrders.setId("Title");
        lblPendingOrders.setLayoutY(25);
        lblPendingOrders.setLayoutX(200);
        
        btnTotalOrders2 = new Button("All Orders");
        btnTotalOrders2.setLayoutX(20);
        btnTotalOrders2.setLayoutY(100);
        btnTotalOrders2.setMinWidth(204);
        btnTotalOrders2.setMinHeight(40);
        btnTotalOrders2.setId("tab");
        btnTotalOrders2.setStyle("-fx-background-color: #696578; -fx-text-fill: #fff;");
        
        btnPendingOrders2 = new Button("Pending Orders" );
        btnPendingOrders2.setLayoutX(244);
        btnPendingOrders2.setLayoutY(100);
        btnPendingOrders2.setMinWidth(204);
        btnPendingOrders2.setMinHeight(40);
        btnPendingOrders2.setId("tab");
        
        btnInDelOrders2 = new Button("In Delivering Orders");
        btnInDelOrders2.setLayoutX(468);
        btnInDelOrders2.setLayoutY(100);
        btnInDelOrders2.setMinWidth(204);
        btnInDelOrders2.setMinHeight(40);
        btnInDelOrders2.setId("tab");
        
        btnCompletedOrders2 = new Button("Completed Orders");
        btnCompletedOrders2.setLayoutX(692);
        btnCompletedOrders2.setLayoutY(100);
        btnCompletedOrders2.setMinWidth(204);
        btnCompletedOrders2.setMinHeight(40);
        btnCompletedOrders2.setId("tab");
        
        Pane_PendingOrders_Header = new Pane();
        Pane_PendingOrders_Header.setId("topPage2");
        Pane_PendingOrders_Header.getChildren().setAll(lblPendingOrders);
        lblPendingOrders.layoutXProperty().bind(Pane_PendingOrders_Header.widthProperty().subtract(lblPendingOrders.widthProperty()).divide(2));
        
        
        tableOrders = new TableView();
        tableOrders.setId("productsTable");
        tableOrders.setPlaceholder(new Label("There is no orders to view !"));
        tableOrders.setLayoutX(20);
        tableOrders.setLayoutY(160);
        tableOrders.setMinWidth(874);
        tableOrders.setMaxWidth(874);
        tableOrders.setMinHeight(340);
        tableOrders.setMaxHeight(340); 
        tableOrders.setEditable(true);
        
        TableColumn TC_OrderId = new TableColumn("Order ID");
        TC_OrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        TC_OrderId.setMinWidth(174);
        TC_OrderId.setMaxWidth(174);

        TableColumn TC_OrderDate = new TableColumn("Order Date");
        TC_OrderDate.setCellValueFactory(new PropertyValueFactory("orderDate"));
        TC_OrderDate.setMinWidth(174);
        TC_OrderDate.setMaxWidth(174);

        TableColumn TC_OrderTime = new TableColumn("Order Time");
        TC_OrderTime.setCellValueFactory(new PropertyValueFactory("orderTime"));
        TC_OrderTime.setMinWidth(174);
        TC_OrderTime.setMaxWidth(174);

        TableColumn TC_OrderPrice = new TableColumn("Total Price");
        TC_OrderPrice.setCellValueFactory(new PropertyValueFactory("orderPrice"));
        TC_OrderPrice.setMinWidth(174);
        TC_OrderPrice.setMaxWidth(174);

        TableColumn TC_OrderStatus = new TableColumn("Order Status");
        TC_OrderStatus.setMinWidth(174);
        TC_OrderStatus.setMaxWidth(174);
        TC_OrderStatus.setCellValueFactory(new PropertyValueFactory("orderStatus"));
        TC_OrderStatus.setCellFactory(new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
            @Override
            public TableCell<Order, String> call(TableColumn<Order, String> col) {
                final TableCell<Order, String> cell = new TableCell<Order, String>() {
                    @Override
                    public void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(status);
                            if (status.equals("Pending")) {
                                setStyle("-fx-text-fill: #cc0000;");
                            } else if (status.equals("Is delivering ..")) {
                                setStyle("-fx-text-fill: #65879e;");
                            } else if (status.equals("Completed")) {
                                setStyle("-fx-text-fill: #00a800;");
                            }
                        }
                    }
                };
                return cell;
            }
        });
        
        
        tableOrders.getColumns().setAll(TC_OrderId , TC_OrderDate , TC_OrderTime , TC_OrderPrice, TC_OrderStatus);
        
        btnOrderDetails = new Button("View Order Details");
        btnOrderDetails.setLayoutX(20);
        btnOrderDetails.setLayoutY(516);
        btnOrderDetails.setMinWidth(160);
        btnOrderDetails.setMinHeight(40);
        btnOrderDetails.setId("confirm");
        
        
        
        ArrayList <String> AvailableDelivery;
        AvailableDelivery = PharmacyController.PeopleTable.getAvailableDelivery();
        CBAvailableDeliverys = new ComboBox(FXCollections.observableArrayList(AvailableDelivery));
        CBAvailableDeliverys.setStyle("-fx-padding: 2px; -fx-background-color: #fff; -fx-border-width: 2px; -fx-border-color: #efefef; -fx-border-radius: 4px;");
        CBAvailableDeliverys.setPromptText("Select delivery ..");
        CBAvailableDeliverys.setLayoutX(474);
        CBAvailableDeliverys.setLayoutY(516);
        CBAvailableDeliverys.setMinWidth(200);
        CBAvailableDeliverys.setMinHeight(40);
        CBAvailableDeliverys.setDisable(true);
        
        
        btnAssign = new Button("Assign order");
        btnAssign.setId("confirm");
        btnAssign.setLayoutX(694);
        btnAssign.setLayoutY(516);
        btnAssign.setMinWidth(200);
        btnAssign.setMaxHeight(40);
        btnAssign.setMinHeight(40);
        btnAssign.setDisable(true);
        
        
        
        Pane_Orders_Body = new Pane();
        Pane_Orders_Body.setLayoutX(250);
        Pane_Orders_Body.setId("Page2");
        Pane_Orders_Body.getChildren().setAll(Pane_PendingOrders_Header , tableOrders, btnOrderDetails, CBAvailableDeliverys, btnAssign, btnTotalOrders2, btnPendingOrders2, btnInDelOrders2, btnCompletedOrders2);
        //--------------------------------------------------------------------//
        lblBrowseCustomers = new Label("Browse Customers ( " + (tablePM_data.size() + tableM_data.size() + tableC_data.size()) + " )");
        lblBrowseCustomers.setId("Title");
        lblBrowseCustomers.setLayoutY(25);
        lblBrowseCustomers.setLayoutX(200);
        
        
        Pane_BrowseCustomer_Header = new Pane();
        Pane_BrowseCustomer_Header.setId("topPage2");
        Pane_BrowseCustomer_Header.getChildren().setAll(lblBrowseCustomers);
        lblBrowseCustomers.layoutXProperty().bind(Pane_BrowseCustomer_Header.widthProperty().subtract(lblBrowseCustomers.widthProperty()).divide(2));
        
        
        
        tableCustomers = new TableView();
        tableCustomers.setId("customersTable");
        tableCustomers.setPlaceholder(new Label("There is no customers !"));
        tableCustomers.setLayoutX(20);
        tableCustomers.setLayoutY(100);
        tableCustomers.setMinWidth(874); //Edited
        tableCustomers.setMaxWidth(874); //Edited
        tableCustomers.setMinHeight(360); //Edited
        tableCustomers.setMaxHeight(360); //Edited
        tableCustomers.setEditable(true);
        
        TableColumn TC_userName = new TableColumn("Username");
        TC_userName.setCellValueFactory(new PropertyValueFactory<>("username"));
        TC_userName.setMinWidth(145);
        TC_userName.setMaxWidth(145);
        
        TableColumn TC_Fname = new TableColumn("First Name");
        TC_Fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TC_Fname.setMinWidth(145);
        TC_Fname.setMaxWidth(145);
        
        
        TableColumn TC_Lname = new TableColumn("Last Name");
        TC_Lname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TC_Lname.setMinWidth(145);
        TC_Lname.setMaxWidth(145);
        
        
        TableColumn TC_Address = new TableColumn("Address");
        TC_Address.setCellValueFactory(new PropertyValueFactory<>("ShippingAddress"));
        TC_Address.setMinWidth(145);
        TC_Address.setMaxWidth(145);
        
        
        TableColumn TC_Phone = new TableColumn("Phone");
        TC_Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TC_Phone.setMinWidth(145);
        TC_Phone.setMaxWidth(145);
        
        
        TableColumn TC_Gender = new TableColumn("Gender");
        TC_Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TC_Gender.setMinWidth(145);
        TC_Gender.setMaxWidth(145);
        
        
        tableCustomers.getColumns().setAll(TC_userName,TC_Fname,TC_Lname,TC_Address,TC_Phone,TC_Gender);
        
        btnShowOrders = new Button("Show Customer's Orders");
        btnShowOrders.setId("submit");
        btnShowOrders.setMinHeight(40);
        btnShowOrders.setMinWidth(210);
        btnShowOrders.setLayoutX(20);
        btnShowOrders.setLayoutY(480);
        btnShowOrders.setDisable(true);
        
        
        Pane_BrowseCustomer_Body = new Pane();
        Pane_BrowseCustomer_Body.setLayoutX(250);
        Pane_BrowseCustomer_Body.setId("Page3");
        Pane_BrowseCustomer_Body.getChildren().setAll(Pane_BrowseCustomer_Header, tableCustomers, btnShowOrders);
        //--------------------------------------------------------------------//
        lblMissingItems = new Label("Missing Items ( " + tableMissing_data.size() + " )");
        lblMissingItems.setId("Title");
        lblMissingItems.setLayoutY(25);
        lblMissingItems.setLayoutX(200);
        
        Pane_Missing_Header = new Pane();
        Pane_Missing_Header.setId("topPage2");
        Pane_Missing_Header.getChildren().setAll(lblMissingItems);
        lblMissingItems.layoutXProperty().bind(Pane_Missing_Header.widthProperty().subtract(lblMissingItems.widthProperty()).divide(2));
        
        tableMissing = new TableView();
        tableMissing.setId("missingItemsTable");
        tableMissing.setPlaceholder(new Label("There is no missing items !"));
        tableMissing.setLayoutX(20);
        tableMissing.setLayoutY(100);
        tableMissing.setMinWidth(874);
        tableMissing.setMaxWidth(874);
        tableMissing.setMinHeight(357);
        tableMissing.setMaxHeight(357);
        tableMissing.setEditable(true);
        
        
        TableColumn TC_ProductName = new TableColumn("Product name");
        TC_ProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TC_ProductName.setMinWidth(218);
        TC_ProductName.setMaxWidth(218);
        
        TableColumn TC_ProductCode = new TableColumn("Code");
        TC_ProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        TC_ProductCode.setMinWidth(219);
        TC_ProductCode.setMaxWidth(219);
        
        
        TableColumn TC_ProductPrice = new TableColumn("Price");
        TC_ProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TC_ProductPrice.setMinWidth(218);
        TC_ProductPrice.setMaxWidth(218);
        
        TableColumn TC_ProductQty = new TableColumn("Quantity");
        TC_ProductQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TC_ProductQty.setMinWidth(219);
        TC_ProductQty.setMaxWidth(219);
        
        tableMissing.getColumns().setAll(TC_ProductName,TC_ProductCode,TC_ProductPrice,TC_ProductQty);
        
        btnSupply = new Button("Supply");
        btnSupply.setId("confirm");
        btnSupply.setLayoutX(692);
        btnSupply.setLayoutY(483);
        btnSupply.setMinWidth(200);
        btnSupply.setMaxHeight(40);
        btnSupply.setMinHeight(40);
        btnSupply.setDisable(true);
        
        txtQtyToSupply = new TextField();
        txtQtyToSupply.setPromptText("Quantity");
        txtQtyToSupply.setLayoutX(600);
        txtQtyToSupply.setLayoutY(483);
        txtQtyToSupply.setMinWidth(72);
        txtQtyToSupply.setMaxWidth(72);
        txtQtyToSupply.setMaxHeight(40);
        txtQtyToSupply.setMinHeight(40);
        txtQtyToSupply.setStyle("-fx-padding: 2px 6px 2px 6px !important;");
        
        Pane_Missing_Body = new Pane();
        Pane_Missing_Body.setLayoutX(250);
        Pane_Missing_Body.setId("Page4");
        Pane_Missing_Body.getChildren().setAll(Pane_Missing_Header , tableMissing , txtQtyToSupply, btnSupply);
        //--------------------------------------------------------------------//
        lblReport = new Label("Generate Report");
        lblReport.setId("Title");
        lblReport.setLayoutY(25);
        lblReport.setLayoutX(200);
        
        Pane_Report_Header = new Pane();
        Pane_Report_Header.setId("topPage2");
        Pane_Report_Header.getChildren().setAll(lblReport);
        lblReport.layoutXProperty().bind(Pane_Report_Header.widthProperty().subtract(lblReport.widthProperty()).divide(2));
        
        
        int countTotalOrders = PharmacyController.OrderTable.getOrders_Number();
        double countTotalSales = countTotalSales();
        String countTopCustomer = getTopCustomer();
        String countTopProduct = getTopProduct();
        
        btntotalOrders = new Button("Total Orders : " + countTotalOrders);
        btntotalOrders.setLayoutX(20);
        btntotalOrders.setLayoutY(100);
        btntotalOrders.setMinWidth(204);
        btntotalOrders.setMinHeight(60);
        btntotalOrders.setId("btntotalOrders");
        
        
        btntotalSales = new Button("Total Sales : " + countTotalSales + " LE");
        btntotalSales.setLayoutX(244);
        btntotalSales.setLayoutY(100);
        btntotalSales.setMinWidth(204);
        btntotalSales.setMinHeight(60);
        btntotalSales.setId("btntotalSales");
        
        
        btntopCustomer = new Button("Top Customer : " + countTopCustomer);
        btntopCustomer.setLayoutX(468);
        btntopCustomer.setLayoutY(100);
        btntopCustomer.setMinWidth(204);
        btntopCustomer.setMaxWidth(204);
        btntopCustomer.setMinHeight(60);
        btntopCustomer.setId("btntopCustomer");
        
        btntopProduct = new Button("Top Product : " + countTopProduct);
        btntopProduct.setLayoutX(692);
        btntopProduct.setLayoutY(100);
        btntopProduct.setMinWidth(204);
        btntopProduct.setMaxWidth(204);
        btntopProduct.setMinHeight(60);
        btntopProduct.setId("btntopProduct");
        
        
        tableReport = new TableView();
        tableReport.setId("ReportTable");
        tableReport.setPlaceholder(new Label("There is no orders made yet !"));
        tableReport.setLayoutX(20);
        tableReport.setLayoutY(184);
        tableReport.setMinWidth(874); //Edited
        tableReport.setMaxWidth(874); //Edited
        tableReport.setMinHeight(386); //Edited
        tableReport.setMaxHeight(386); //Edited
        tableReport.setEditable(true);
        
        
        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setMinWidth(174);
        id.setMaxWidth(174);
        
        TableColumn date = new TableColumn("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setMinWidth(174);
        date.setMaxWidth(174);
        
        TableColumn sales = new TableColumn("No. Orders");
        sales.setCellValueFactory(new PropertyValueFactory<>("numSales"));
        sales.setMinWidth(174);
        sales.setMaxWidth(174);
        
        TableColumn totalSales = new TableColumn("Total Sales");
        totalSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        totalSales.setMinWidth(174);
        totalSales.setMaxWidth(174);
        totalSales.setCellFactory(new Callback<TableColumn<Report, Double>, TableCell<Report, Double>>() {
            @Override
            public TableCell<Report, Double> call(TableColumn<Report, Double> col) {
                final TableCell<Report, Double> cell = new TableCell<Report, Double>() {
                    @Override
                    public void updateItem(Double salesPercentage, boolean empty) {
                        super.updateItem(salesPercentage, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(salesPercentage+" LE");
                        }
                    }
                };
                return cell;
            }
        });
        
        TableColumn salesPercentage = new TableColumn("Sales %");
        salesPercentage.setCellValueFactory(new PropertyValueFactory<>("salesPercentage"));
        salesPercentage.setMinWidth(174);
        salesPercentage.setMaxWidth(174);
        salesPercentage.setCellFactory(new Callback<TableColumn<Report, Double>, TableCell<Report, Double>>() {
            @Override
            public TableCell<Report, Double> call(TableColumn<Report, Double> col) {
                final TableCell<Report, Double> cell = new TableCell<Report, Double>() {
                    @Override
                    public void updateItem(Double salesPercentage, boolean empty) {
                        super.updateItem(salesPercentage, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(salesPercentage+"%");
                            if (salesPercentage <= 0) {
                                setStyle("-fx-text-fill: #cc0000;");
                            } else {
                                setStyle("-fx-text-fill: #00a800;");
                                setText("+"+salesPercentage+"%");
                            }
                        }
                    }
                };
                return cell;
            }
        });
        
        
        tableReport.getColumns().setAll(id,date,sales,totalSales , salesPercentage);
        
        Pane_Report_Body = new Pane();
        Pane_Report_Body.setLayoutX(250);
        Pane_Report_Body.setId("Page4");
        Pane_Report_Body.getChildren().setAll(Pane_Report_Header, tableReport, btntotalOrders, btntotalSales, btntopCustomer, btntopProduct);
        //---------------------------------------------------------------------//
        Pane Pane_Setting_Header;
        Pane_Setting_Header = new Pane();
        Pane_Setting_Header.setId("topPage2");
        
        Image newCustomer = new Image (CustomerForm.class.getResourceAsStream("icons/editCustomer.png"));
        ImageView NewCustomer = new ImageView(newCustomer);
        NewCustomer.setFitHeight(100);
        NewCustomer.setPreserveRatio(true);
        NewCustomer.setLayoutX(125+280);
        NewCustomer.setLayoutY(33);
        
        Image FN_Img = new Image (CustomerForm.class.getResourceAsStream("icons/firstname.png"));
        ImageView FN_Icon = new ImageView(FN_Img);
        FN_Icon.setFitHeight(33);
        FN_Icon.setPreserveRatio(true);
        FN_Icon.setLayoutX(125+90);
        FN_Icon.setLayoutY(178);
        
        
        Image LN_Img = new Image (CustomerForm.class.getResourceAsStream("icons/lastname.png"));
        ImageView LN_Icon = new ImageView(LN_Img);
        LN_Icon.setFitHeight(33);
        LN_Icon.setPreserveRatio(true);
        LN_Icon.setLayoutX(125+350);
        LN_Icon.setLayoutY(178);
        
        Image user2 = new Image (CustomerForm.class.getResourceAsStream("icons/UserIcon.png"));
        ImageView userIcon2 = new ImageView(user2);
        userIcon2.setFitHeight(35);
        userIcon2.setPreserveRatio(true);
        userIcon2.setLayoutX(125+90);
        userIcon2.setLayoutY(250);
        
        
        Image pass2 = new Image (CustomerForm.class.getResourceAsStream("icons/pass.png"));
        ImageView passIcon2 = new ImageView(pass2);
        passIcon2.setFitHeight(35);
        passIcon2.setPreserveRatio(true);
        passIcon2.setLayoutX(125+350);
        passIcon2.setLayoutY(250);
        
        Image phoneImg = new Image (CustomerForm.class.getResourceAsStream("icons/phone.png"));
        ImageView phoneIcon = new ImageView(phoneImg);
        phoneIcon.setFitHeight(34);
        phoneIcon.setPreserveRatio(true);
        phoneIcon.setLayoutX(125+100);
        phoneIcon.setLayoutY(322);
        
        Image genderImg = new Image (CustomerForm.class.getResourceAsStream("icons/gender.png"));
        ImageView genderIcon = new ImageView(genderImg);
        genderIcon.setFitHeight(30);
        genderIcon.setPreserveRatio(true);
        genderIcon.setLayoutX(125+355);
        genderIcon.setLayoutY(325);
        
        Image roleImg = new Image (CustomerForm.class.getResourceAsStream("icons/job.png"));
        ImageView roleIcon = new ImageView(roleImg);
        roleIcon.setFitHeight(26);
        roleIcon.setPreserveRatio(true);
        roleIcon.setLayoutX(125+355);
        roleIcon.setLayoutY(395);
        
        
        p5_txtFirstname = new TextField (currentuser.getFirstName());
        p5_txtFirstname.setEditable(false);
        p5_txtFirstname.setLayoutX(125+80);
        p5_txtFirstname.setLayoutY(170);
        p5_txtFirstname.setMinWidth(240);
        
        
        
        p5_txtLastname = new TextField(currentuser.getLastName());
        p5_txtLastname.setEditable(false);
        p5_txtLastname.setLayoutX(125+340);
        p5_txtLastname.setLayoutY(170);
        p5_txtLastname.setMinWidth(240);
        
        
        
        p5_txtUsername = new TextField (currentuser.getUsername());
        p5_txtUsername.setEditable(false);
        p5_txtUsername.setLayoutX(125+80);
        p5_txtUsername.setLayoutY(240);
        p5_txtUsername.setMinWidth(240);
        
        
        p5_txtPassword = new PasswordField ();
        p5_txtPassword.setPromptText("Enter new Password");
        p5_txtPassword.setEditable(false);
        p5_txtPassword.setLayoutX(125+340);
        p5_txtPassword.setLayoutY(240);
        p5_txtPassword.setMinWidth(240);

        p5_txtPhone = new TextField (currentuser.getPhone());
        p5_txtPhone.setEditable(false);
        p5_txtPhone.setLayoutX(125+80);
        p5_txtPhone.setLayoutY(310);
        p5_txtPhone.setMinWidth(240);

        p5_CbGender = new ChoiceBox(FXCollections.observableArrayList("Male","Female"));
        if (currentuser.getGender().equalsIgnoreCase("Male"))
            p5_CbGender.getSelectionModel().select(0);
        else
            p5_CbGender.getSelectionModel().select(1);
        p5_CbGender.setId("gender");
        p5_CbGender.setDisable(true);
        p5_CbGender.setLayoutX(125+340);
        p5_CbGender.setLayoutY(310);
        p5_CbGender.setMinWidth(240);

        p5_CbDay = new ChoiceBox(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"));
        p5_CbDay.getSelectionModel().select(currentuser.getDateOfBirth().getDay()-1);
        p5_CbDay.setDisable(true);
        p5_CbDay.setMaxWidth(70);
        p5_CbDay.setMinWidth(70);
        p5_CbDay.setLayoutX(125+80);
        p5_CbDay.setLayoutY(380);

        p5_CbMonth = new ChoiceBox(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12"));
        p5_CbMonth.getSelectionModel().select(currentuser.getDateOfBirth().getMonth()-1);
        p5_CbMonth.setDisable(true);
        p5_CbMonth.setMaxWidth(78);
        p5_CbMonth.setMinWidth(78);
        p5_CbMonth.setLayoutX(125+160);
        p5_CbMonth.setLayoutY(380);
        
        p5_CbYear = new ChoiceBox(FXCollections.observableArrayList("1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"));
        p5_CbYear.getSelectionModel().select(currentuser.getDateOfBirth().getYear()-1970);
        p5_CbYear.setDisable(true);
        p5_CbYear.setMaxWidth(70);
        p5_CbYear.setMinWidth(70);
        p5_CbYear.setLayoutX(125+248);
        p5_CbYear.setLayoutY(380);
        
        
        p5_txtRole = new TextField (currentuser instanceof AdminWarehouse ? "Admin Warehouse" : "Genrate Report");
        p5_txtRole.setEditable(false);
        p5_txtRole.setLayoutX(125+340);
        p5_txtRole.setLayoutY(380);
        p5_txtRole.setMinWidth(240);
        
        
        p5_txtPassword.setOpacity(.3);
        p5_txtPhone.setOpacity(.3);
        p5_txtUsername.setOpacity(.3);
        p5_txtFirstname.setOpacity(.3);
        p5_txtLastname.setOpacity(.3);
        p5_txtRole.setOpacity(.3);
        
        p5_txtPassword.setCursor(Cursor.DEFAULT);
        p5_txtPhone.setCursor(Cursor.DEFAULT);
        p5_txtUsername.setCursor(Cursor.DEFAULT);
        
        
        p5_btnSubmit = new Button("Save");
        p5_btnSubmit.setDisable(true);
        p5_btnSubmit.setLayoutX(125+340);
        p5_btnSubmit.setLayoutY(460);
        p5_btnSubmit.setMinWidth(240);
        p5_btnSubmit.setId("submit");
        
        
        p5_btnEdit = new Button("Edit");
        p5_btnEdit.setLayoutX(125+80);
        p5_btnEdit.setLayoutY(460);
        p5_btnEdit.setMinWidth(240);
        p5_btnEdit.setId("edit");
        
        
        p5_btnCancel = new Button("Cancel");
        p5_btnCancel.setLayoutX(125+80);
        p5_btnCancel.setLayoutY(460);
        p5_btnCancel.setMinWidth(240);
        p5_btnCancel.setVisible(false);
        p5_btnCancel.setId("cancel2");
        
        
        Pane_Setting_Body = new Pane();
        Pane_Setting_Body.setLayoutX(250);
        Pane_Setting_Body.setId("Page5");
        Pane_Setting_Body.getChildren().setAll(Pane_Setting_Header , NewCustomer, p5_txtFirstname , p5_txtLastname , p5_txtUsername, p5_txtPassword , p5_txtPhone, p5_CbGender , p5_CbDay , p5_CbMonth , p5_CbYear, p5_txtRole, p5_btnCancel, p5_btnEdit, p5_btnSubmit, FN_Icon , LN_Icon , userIcon2, passIcon2, phoneIcon, genderIcon, roleIcon);
        //----------------------------------------------------------------------
        tablePM.setItems(tablePM_data);
        tableM.setItems(tableM_data);
        tableC.setItems(tableC_data);
        
        tableOrders.setItems(tableTotalOrders_data);
        tableCustomers.setItems(tableCustomers_data);
        tableMissing.setItems(tableMissing_data);
        tableReport.setItems(tableReport_data);
        
        btnAssign.setVisible(false);
        CBAvailableDeliverys.setVisible(false);
        
        //----------------------------------------------------------------------
        MainWindow = new Stage();
        MainWindow.setResizable(false);
        MainWindow.setTitle("Pharmacy - Browse Products");
        
        Pane_Main = new Pane();
        Pane_Main.setId("Panel");
        Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_BrowseProducts);
        
    }
}