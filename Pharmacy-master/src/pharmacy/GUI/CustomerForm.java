package pharmacy.GUI;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pharmacy.ExceptionClasses.InvalidInputException;
import pharmacy.ExceptionClasses.InvalidPrescriptionCode;
import pharmacy.ExceptionClasses.MissingDataException;
import pharmacy.ExceptionClasses.QuantityLimitException;
import pharmacy.PersonClasses.*;
import pharmacy.ProductClasses.*;
import pharmacy.MainClasses.*;


public class CustomerForm {
    private Customer currentuser;
    private final ObservableList<Product> tableCurrentOrder_data = FXCollections.observableArrayList();
    private final ObservableList<Order> tableCustomerOrders_data = FXCollections.observableArrayList();
    private final ObservableList<Product> tablePM_data  = FXCollections.observableArrayList();
    private final ObservableList<Product> tableM_data = FXCollections.observableArrayList();
    private final ObservableList<Product> tableC_data = FXCollections.observableArrayList();
    
    public void setCurrentUser(Customer c){
        currentuser = c;
    }
    
    public void ChangeDB(ObservableList<Product> Cart) {
        for (int j = 0; j < Cart.size(); j++) {
            int qty = PharmacyController.ProductTable.getProduct(Cart.get(j).getCode()).getQuantity();
            PharmacyController.ProductTable.updateProduct_quantity(Cart.get(j).getCode(), qty-Cart.get(j).getQuantity());
        }
    }
    public double getTotalDue() {
        double total = 0.0;
        for (Product item : tableCurrentOrder_data) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
    private void FillTableViewByProduct(ObservableList<Product> data, Product.ProductType type){
        for (int i = 0; i < PharmacyController.ProductTable.getProducts_Number(); i++){
            Product temp = PharmacyController.ProductTable.getProductOfIndex(i);
            if (temp.getQuantity() == 0)
                continue;
            if (type == type.All) data.add(temp);
            else if (type == type.PM){
                if (temp instanceof PrescriptionMedication){
                    data.add(temp);  
                }
            }
            else if (type == type.Medication){
                if (temp instanceof Medication){
                    data.add(temp);
                }
            }
            else if (type == type.Cosmetics){
                if (temp instanceof Cosmetics){
                    data.add(temp);  
                }
            }

        }
    }
    private void FillCustomerOrderHistory(){
        tableCustomerOrders_data.clear();
        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            if (PharmacyController.OrderTable.getOrderOfIndex(i).getOrderCustomer().getID() == currentuser.getID()) {
                tableCustomerOrders_data.add(0 , PharmacyController.OrderTable.getOrderOfIndex(i));
            }
        }
        btnOrderHistory.setText("Orders History ( " + tableCustomerOrders_data.size() + " )");
    }
    private void FillProductsTables(){
        FillTableViewByProduct(tablePM_data, Product.ProductType.PM);
        FillTableViewByProduct(tableM_data, Product.ProductType.Medication);
        FillTableViewByProduct(tableC_data, Product.ProductType.Cosmetics);
    }

    public void AddToCart(ObservableList<Product> data,Product p) throws QuantityLimitException {
        int Index = 0;
        for (Product current : tableCurrentOrder_data) {
            if (current.getName().equalsIgnoreCase(p.getName())) {
                if (tableCurrentOrder_data.get(Index).getQuantity() > 1 && p.getQuantity() - tableCurrentOrder_data.get(Index).getQuantity() < 8 && (p instanceof  PrescriptionMedication || p instanceof  Medication)) {
                    String msg = "You can't order more than " + tableCurrentOrder_data.get(Index).getQuantity() + " of this product";
                    throw new QuantityLimitException(msg);
                } else {
                    if (p instanceof PrescriptionMedication)
                        tableCurrentOrder_data.set(Index, new PrescriptionMedication(p.getName(), p.getCode(), p.getPrice(), current.getQuantity() + 1, ((PrescriptionMedication)p).getPurpose(), ((PrescriptionMedication)p).getAdultDose(), ((PrescriptionMedication)p).getChildDose(), ((PrescriptionMedication)p).getActiveIngredient(), ((PrescriptionMedication)p).getExpiredDate(), ((PrescriptionMedication)p).getManufacturer()));
                    else if (p instanceof Medication)
                        tableCurrentOrder_data.set(Index, new Medication(p.getName(), p.getCode(), p.getPrice(), current.getQuantity() + 1, ((Medication)p).getPurpose(), ((Medication)p).getAdultDose(), ((Medication)p).getChildDose(), ((Medication)p).getActiveIngredient(), ((Medication)p).getExpiredDate(), ((Medication)p).getManufacturer()));
                    else if (p instanceof Cosmetics)
                        tableCurrentOrder_data.set(Index, new Cosmetics(p.getName(), p.getCode(), p.getPrice(), current.getQuantity() + 1, ((Cosmetics) p).getCategory()));
                    if (p.getQuantity() - tableCurrentOrder_data.get(Index).getQuantity() == 0) {
                        data.remove(p);
                    }
                }
                return;
            }
            Index++;
        }
        if (p instanceof PrescriptionMedication)
            tableCurrentOrder_data.add(new PrescriptionMedication(p.getName(), p.getCode(), p.getPrice(), 1, ((PrescriptionMedication)p).getPurpose(), ((PrescriptionMedication)p).getAdultDose(), ((PrescriptionMedication)p).getChildDose(), ((PrescriptionMedication)p).getActiveIngredient(), ((PrescriptionMedication)p).getExpiredDate(), ((PrescriptionMedication)p).getManufacturer()));
        else if (p instanceof Medication)
             tableCurrentOrder_data.add(new Medication(p.getName(), p.getCode(), p.getPrice(), 1, ((Medication)p).getPurpose(), ((Medication)p).getAdultDose(), ((Medication)p).getChildDose(), ((Medication)p).getActiveIngredient(), ((Medication)p).getExpiredDate(), ((Medication)p).getManufacturer()));
        else if (p instanceof Cosmetics)
            tableCurrentOrder_data.add(new Cosmetics(p.getName(), p.getCode(), p.getPrice(), 1, ((Cosmetics) p).getCategory()));
        if (p.getQuantity() - tableCurrentOrder_data.get(Index).getQuantity() == 0) {
            data.remove(p);
        }
    }

    private void btnAddPM_OnClick() throws InvalidPrescriptionCode{
        if (tablePM.getSelectionModel().getSelectedItem() != null) {
            PrescriptionMedication p = (PrescriptionMedication)tablePM.getSelectionModel().getSelectedItem();
            if (PharmacyController.PrescriptionTable.isValidPrescription(txtPrescriptionCode.getText() , p)) {
                txtPrescriptionCode.setStyle("-fx-border-color: #3bd374;-fx-padding: 2px 14px 2px 40px !important;");
                IMGV_Correct.setVisible(true);
                IMGV_Wrong.setVisible(false);
                try{
                    AddToCart(tablePM_data, p);
                    btnConfirmOrder.setDisable(false);
                    btnCancelOrder.setDisable(false);
                    lblTotalDue.setText("Total Due : " + String.valueOf(getTotalDue()) + " LE");
                }
                catch(QuantityLimitException ex){
                    MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
                }
            } else {
                txtPrescriptionCode.setStyle("-fx-border-color: #ee5f67;-fx-padding: 2px 14px 2px 40px !important;");
                IMGV_Correct.setVisible(!true);
                IMGV_Wrong.setVisible(!false);
                if (txtPrescriptionCode.getText().equals("")) {
                    throw new InvalidPrescriptionCode("You must enter prescription code !");
                } else if (txtPrescriptionCode.getText().length() != 8) {
                    throw new InvalidPrescriptionCode("Prescription code must be 8 digits only !");
                } else {
                    throw new InvalidPrescriptionCode("Prescription code is invalid or not  selected medication !");
                }
            }
        }
    }
    private void btnAddM_OnClick(){
        if (tableM.getSelectionModel().getSelectedItem() != null) {
            Medication p = (Medication)tableM.getSelectionModel().getSelectedItem();
            try{
                AddToCart(tableM_data, p);
                btnConfirmOrder.setDisable(false);
                btnCancelOrder.setDisable(false);
                lblTotalDue.setText("Total Due : " + String.valueOf(getTotalDue()) + " LE");
            }
            catch(QuantityLimitException ex){
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            }
        }
    }
    private void btnAddC_OnClick(){
        if (tableC.getSelectionModel().getSelectedItem() != null) {
            Cosmetics p = (Cosmetics)tableC.getSelectionModel().getSelectedItem();
            try{
                AddToCart(tableC_data, p);
                btnConfirmOrder.setDisable(false);
                btnCancelOrder.setDisable(false);
                lblTotalDue.setText("Total Due : " + String.valueOf(getTotalDue()) + " LE");
            }
            catch(QuantityLimitException ex){
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            }
        }
    }
    private void btnConfirmOrder_OnClick(){
        ArrayList<Product> Cartlist = new ArrayList<>();
        Cartlist.addAll(tableCurrentOrder_data);
        Order currentOrder = null;
        try {
            currentOrder = new Order(getTotalDue() , (Customer) currentuser , Cartlist);
        } catch (InvalidInputException ex) {
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            return;
        }
        PharmacyController.OrderTable.addOrder(currentOrder);
        ChangeDB(tableCurrentOrder_data);
        tableCurrentOrder_data.clear();
        FillCustomerOrderHistory();
        String msg = "Your order with id #" + currentOrder.getOrderId()+ " has be confirmed !";
        MsgBox.ShowMsg("DONE", msg , MsgBox.type.success);
        lblTotalDue.setText("Total Due : 0 LE");
        btnConfirmOrder.setDisable(true);
        btnCancelOrder.setDisable(true);
        btnOrderHistory.setText("Orders History ( " + (tableCustomerOrders_data.size()) + " )");
        try{
            pharmacy.MainClasses.PharmacyController.PeopleTable.WriteToFile();
            pharmacy.MainClasses.PharmacyController.OrderTable.WriteToFile();
            pharmacy.MainClasses.PharmacyController.ProductTable.WriteToFile();
        }
        catch (IOException ex){
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
        } 
    }
    private void btnSave_OnClick() throws MissingDataException{
        if (txtFirstname.getText().equals(""))
            throw new MissingDataException("First name field is empty !");
        
        if (txtLastname.getText().equals(""))
            throw new MissingDataException("Last name field is empty !");
        
        if (txtUsername.getText().equals(""))
            throw new MissingDataException("Username field is empty !");
        
        if (txtPhone.getText().equals(""))
            throw new MissingDataException("Phone field is empty !");
        
        if (txtAddress.getText().equals(""))
            throw new MissingDataException("Address field is empty !");
        
        
        if (!txtPassword.getText().equals(""))
            currentuser.setPassword(txtPassword.getText());
        
        
        currentuser.setUsername(txtUsername.getText());
        currentuser.setPhone(txtPhone.getText());
        currentuser.setGender(CBGender.getValue().toString());
        try {
            currentuser.setDateOfBirth(Integer.parseInt(CBDay.getValue().toString()),Integer.parseInt(CBMonth.getValue().toString()),Integer.parseInt(CBYear.getValue().toString()));
        } catch (InvalidInputException ex) {
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            return;
        }
        currentuser.setShippingAddress(txtAddress.getText());
        currentuser.setFirstName(txtFirstname.getText());
        currentuser.setLastName(txtLastname.getText());
        
        lblUsername.setText(currentuser.getFirstName() + " " + currentuser.getLastName());
        try{
            pharmacy.MainClasses.PharmacyController.PeopleTable.WriteToFile();
        }
        catch (IOException ex){
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
        } 
    }
    public void Show() {
        
        InitializeComponents();
        FillProductsTables();
        FillCustomerOrderHistory();
        
        Scene scene = new Scene(Pane_Main , 1160, 600);
        scene.getStylesheets().add("pharmacy/GUI/style.css");
        MainWindow.setScene(scene);
        MainWindow.show();


        btnPM.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_PM_Body , Pane_Cart_Body);
            Pane_Selected.setLayoutY(100);
            MainWindow.setTitle("Pharmacy - Prescription Medications");
        });


        btnM.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_M_Body , Pane_Cart_Body);
            Pane_Selected.setLayoutY(160);
            MainWindow.setTitle("Pharmacy - Medications");
        });

        btnC.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_C_Body , Pane_Cart_Body);
            Pane_Selected.setLayoutY(220);
            MainWindow.setTitle("Pharmacy - Cosmetics");
        });

        btnOrderHistory.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_OrderHistory_Body, Pane_Cart_Body);
            Pane_Selected.setLayoutY(280);
            MainWindow.setTitle("Pharmacy - Orders History");
            btnOrderDetails.setDisable(true);
            FillCustomerOrderHistory();
            
        });

        btnSetting.setOnMouseClicked(event -> {
            Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_Setting_Body , Pane_Cart_Body);
            Pane_Selected.setLayoutY(340);
            MainWindow.setTitle("Pharmacy - Settings");
        });

        btnLogout.setOnMouseClicked(event -> {
            if (MsgBox.ShowMsg("Logout","Are you sure you want to log out and quit ?",MsgBox.type.warning)) {
                tableCurrentOrder_data.clear();
                tablePM_data.clear();
                LoginForm obj = new LoginForm();
                obj.start(new Stage());
                MainWindow.hide();
            }
        });

        btnAddPM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    btnAddPM_OnClick();
                }
                catch(InvalidPrescriptionCode ex){
                    MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
                }
            }
        });

        btnAddM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnAddM_OnClick();
            }
        });

        btnAddC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnAddC_OnClick();
            }
        });

        btnConfirmOrder.setOnMouseClicked(event -> {
            btnConfirmOrder_OnClick();
        });

        btnCancelOrder.setOnMouseClicked(event -> {
            tableCurrentOrder_data.clear();
            tablePM_data.clear();
            tableM_data.clear();
            tableC_data.clear();
            lblTotalDue.setText("Total Due : 0 LE");
            btnConfirmOrder.setDisable(true);
            btnCancelOrder.setDisable(true);
            btnAddPM.setDisable(true);
            btnAddM.setDisable(true);
            btnAddC.setDisable(true);

            FillProductsTables();
        });

        tablePM.setOnMouseClicked(event -> {
            if (tablePM.getSelectionModel().getSelectedItem() != null) {
                btnAddPM.setDisable(false);
            }
        });

        tableM.setOnMouseClicked(event -> {
            if (tableM.getSelectionModel().getSelectedItem() != null) {
                btnAddM.setDisable(false);
            }
        });

        tableC.setOnMouseClicked(event -> {
            if (tableC.getSelectionModel().getSelectedItem() != null) {
                btnAddC.setDisable(false);
            }
        });


        tableCurrentOrder.setOnMouseClicked(event -> {
            if (tableCurrentOrder.getSelectionModel().getSelectedItem() != null) {
                btnOrderDetails.setDisable(false);
            }
        });
        
        tableOrderHistory.setOnMouseClicked(event -> {
            if (tableOrderHistory.getSelectionModel().getSelectedItem() != null) {
                btnOrderDetails.setDisable(false);
            }
        });


        btnOrderDetails.setOnMouseClicked(event -> {
            if (tableOrderHistory.getSelectionModel().getSelectedItem() != null) {
                OrderDetailsForm.View((Order)tableOrderHistory.getSelectionModel().getSelectedItem());
            }
        });


        btnSave.setOnMouseClicked(event -> {
            try {
                btnSave_OnClick();
            } catch (MissingDataException ex) {
                MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
                return;
            }
            CBDay.setDisable(!false);
            CBGender.setDisable(!false);
            CBMonth.setDisable(!false);
            CBYear.setDisable(!false);
            btnSave.setDisable(!false);
            txtAddress.setEditable(!true);
            txtPassword.setEditable(!true);
            txtPhone.setEditable(!true);
            txtUsername.setEditable(!true);
            txtLastname.setEditable(!true);
            txtFirstname.setEditable(!true);
            
            txtAddress.setOpacity(.3);
            txtPassword.setOpacity(.3);
            txtPhone.setOpacity(.3);
            txtUsername.setOpacity(.3);
            txtLastname.setOpacity(.3);
            txtFirstname.setOpacity(.3);
            
            txtAddress.setCursor(Cursor.DEFAULT);
            txtPassword.setCursor(Cursor.DEFAULT);
            txtPhone.setCursor(Cursor.DEFAULT);
            txtUsername.setCursor(Cursor.DEFAULT);
            txtLastname.setCursor(Cursor.DEFAULT);
            txtFirstname.setCursor(Cursor.DEFAULT);
            
            
            btnEdit.setDisable(!true);
            btnEdit.setVisible(!false);
            btnCancel.setVisible(!true);
            btnCancel.setDisable(!false);
            Pane_SideMenu.setDisable(!true);
        });


        btnEdit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                CBDay.setDisable(false);
                CBGender.setDisable(false);
                CBMonth.setDisable(false);
                CBYear.setDisable(false);
                btnSave.setDisable(false);
                txtAddress.setEditable(true);
                txtPassword.setEditable(true);
                txtPhone.setEditable(true);
                txtUsername.setEditable(true);
                txtLastname.setEditable(true);
                txtFirstname.setEditable(true);
                
                txtAddress.setOpacity(1);
                txtPassword.setOpacity(1);
                txtPhone.setOpacity(1);
                txtUsername.setOpacity(1);
                txtLastname.setOpacity(1);
                txtFirstname.setOpacity(1);
                
                txtAddress.setCursor(Cursor.TEXT);
                txtPassword.setCursor(Cursor.TEXT);
                txtPhone.setCursor(Cursor.TEXT);
                txtUsername.setCursor(Cursor.TEXT);
                txtLastname.setCursor(Cursor.TEXT);
                txtFirstname.setCursor(Cursor.TEXT);
                
                
                btnEdit.setDisable(true);
                btnEdit.setVisible(false);
                btnCancel.setVisible(true);
                btnCancel.setDisable(false);
                Pane_SideMenu.setDisable(true);

            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                CBDay.setDisable(!false);
                CBGender.setDisable(!false);
                CBMonth.setDisable(!false);
                CBYear.setDisable(!false);
                btnSave.setDisable(!false);
                txtAddress.setEditable(!true);
                txtPassword.setEditable(!true);
                txtPhone.setEditable(!true);
                txtUsername.setEditable(!true);
                txtLastname.setEditable(!true);
                txtFirstname.setEditable(!true);
                
                txtAddress.setOpacity(.3);
                txtPassword.setOpacity(.3);
                txtPhone.setOpacity(.3);
                txtUsername.setOpacity(.3);
                txtLastname.setOpacity(.3);
                txtFirstname.setOpacity(.3);
                
                txtAddress.setCursor(Cursor.DEFAULT);
                txtPassword.setCursor(Cursor.DEFAULT);
                txtPhone.setCursor(Cursor.DEFAULT);
                txtUsername.setCursor(Cursor.DEFAULT);
                txtLastname.setCursor(Cursor.DEFAULT);
                txtFirstname.setCursor(Cursor.DEFAULT);
                
                
                btnEdit.setDisable(!true);
                btnEdit.setVisible(!false);
                btnCancel.setVisible(!true);
                btnCancel.setDisable(!false);
                Pane_SideMenu.setDisable(!true);

                //Old Values
                txtAddress.setText(currentuser.getShippingAddress());
                txtPassword.clear();
                txtPhone.setText(currentuser.getPhone());
                txtUsername.setText(currentuser.getUsername());
                txtFirstname.setText(currentuser.getFirstName());
                txtLastname.setText(currentuser.getLastName());
                if (currentuser.getGender().equalsIgnoreCase("Male"))
                     CBGender.getSelectionModel().select(0);
                else
                     CBGender.getSelectionModel().select(1);
                CBDay.getSelectionModel().select(currentuser.getDateOfBirth().getDay()-1);
                CBMonth.getSelectionModel().select(currentuser.getDateOfBirth().getMonth()-1);
                CBYear.getSelectionModel().select(currentuser.getDateOfBirth().getYear()-1970);
            }
        });




    }
    Stage MainWindow;
    Pane Pane_Main;
    //SideMenu
    Label lblUsername;
    
    Button btnPM;
    Button btnM;
    Button btnC;
    Button btnOrderHistory;
    Button btnSetting;
    Button btnLogout;
    
    Image IMG_User;
    Image IMG_PM;
    Image IMG_M;
    Image IMG_C;
    Image IMG_OrderHistory;
    Image IMG_Setting;
    Image IMG_Logout;
    
    ImageView IMGV_User;
    ImageView IMGV_PM;
    ImageView IMGV_M;
    ImageView IMGV_C;
    ImageView IMGV_OrderHistory;
    ImageView IMGV_Setting;
    ImageView IMGV_Logout;
    
    Pane Pane_User;
    Pane Pane_Selected;
    Pane Pane_SideMenu;
    //End SideMenu
    //---------------------
    Button btnAddPM;
    
    Label lblPM;
    TableView tablePM;

    Image IMG_Prescription;
    Image IMG_Correct;
    Image IMG_Wrong;
    
    ImageView IMGV_Prescription;
    ImageView IMGV_Correct;
    ImageView IMGV_Wrong;
    
    TextField txtPrescriptionCode;
    
    Pane Pane_PM_Header;
    Pane Pane_PM_Body;
    //----------------------------
    Button btnAddM;
    Label lblM;
    TableView tableM;
    Pane Pane_M_Header;
    Pane Pane_M_Body;
    //--------------------------
    Button btnAddC;
    Label lblC;
    
    TableView tableC;
    
    Pane Pane_C_Header;
    Pane Pane_C_Body;
    //----------------------------
    Label lblOrderHistory;
    Pane Pane_OrderHistory_Header;
    TableView tableOrderHistory;
    Pane Pane_OrderHistory_Body;
    Button btnOrderDetails;
    //----------------------------
    Image IMG_EditCustomer;
    Image IMG_FirstName;
    Image IMG_LastName;
    Image IMG_Username;  
    Image IMG_Password;
    Image IMG_Phone;
    Image IMG_Gender;
    Image IMG_Address;
    
    ImageView IMGV_Address;
    ImageView IMGV_Username;
    ImageView IMGV_LastName;
    ImageView IMGV_FirstName;
    ImageView IMGV_EditCustomer;
    ImageView IMGV_Gender;
    ImageView IMGV_Phone;
    ImageView IMGV_Password;
    
    TextField txtFirstname;
    TextField txtLastname;
    TextField txtUsername;
    TextField txtPhone;
    TextField txtAddress;
    
    PasswordField txtPassword;
    
    ChoiceBox CBGender;
    ChoiceBox CBDay;
    ChoiceBox CBMonth;
    ChoiceBox CBYear;
    
    Button btnSave;
    Button btnEdit;
    Button btnCancel;
    
    Pane Pane_Setting_Header;
    Pane Pane_Setting_Body;
    //-------------------------
    Image IMG_Cart;
    ImageView IMGV_Cart;
    
    Label lblMyCart;
    
    Label lblTotalDue;
    Button btnConfirmOrder;
    Button btnCancelOrder;
    
    TableView tableCurrentOrder;
    
    Pane Pane_Cart_Header;
    Pane Pane_Cart_Body;
    //--------------------------
    private void InitializeComponents(){
        MainWindow = new Stage();
        MainWindow.setResizable(false);
        MainWindow.setTitle("Pharmacy - Prescription Medications");
        //-----------------------SideMenu-------------------------------------//
        IMG_User = new Image (CustomerForm.class.getResourceAsStream("icons/UserIcon.png"));
        IMGV_User = new ImageView(IMG_User);;
        IMGV_User.setPreserveRatio(true);
        IMGV_User.setFitHeight(25);
        IMGV_User.setLayoutX(25);
        IMGV_User.setLayoutY(25);
        
        lblUsername = new Label(currentuser.getFirstName() + " " + currentuser.getLastName());
        lblUsername.setId("username");
        lblUsername.setLayoutX(70);
        lblUsername.setLayoutY(25);

        btnPM = new Button("Prescription Medications");
        btnPM.setId("cat");
        btnPM.setLayoutX(70);
        btnPM.setLayoutY(95);
        btnPM.setMinWidth(180);
        btnPM.setMinHeight(55);

        btnM = new Button("Medications");
        btnM.setId("cat");
        btnM.setLayoutX(70);
        btnM.setLayoutY(155);
        btnM.setMinWidth(180);
        btnM.setMinHeight(55);

        
        btnC = new Button("Cosmetics");
        btnC.setId("cat");
        btnC.setLayoutX(70);
        btnC.setLayoutY(215);
        btnC.setMinWidth(180);
        btnC.setMinHeight(55);
        
        btnOrderHistory = new Button("Orders History ( " + tableCustomerOrders_data.size() + " )");
        btnOrderHistory.setId("cat");
        btnOrderHistory.setLayoutX(70);
        btnOrderHistory.setLayoutY(275);
        btnOrderHistory.setMinWidth(180);
        btnOrderHistory.setMinHeight(55);
        
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
        
        IMG_PM = new Image (CustomerForm.class.getResourceAsStream("icons/1.png"));
        IMGV_PM = new ImageView(IMG_PM);
        IMGV_PM.setPreserveRatio(true);
        IMGV_PM.setFitHeight(25);
        IMGV_PM.setLayoutX(25);
        IMGV_PM.setLayoutY(110);
        
        IMG_M = new Image (CustomerForm.class.getResourceAsStream("icons/2.png"));
        IMGV_M = new ImageView(IMG_M);
        IMGV_M.setPreserveRatio(true);
        IMGV_M.setFitHeight(25);
        IMGV_M.setLayoutX(25);
        IMGV_M.setLayoutY(170);
        
        IMG_C = new Image (CustomerForm.class.getResourceAsStream("icons/3.png"));
        IMGV_C = new ImageView(IMG_C);
        IMGV_C.setPreserveRatio(true);
        IMGV_C.setFitHeight(25);
        IMGV_C.setLayoutX(25);
        IMGV_C.setLayoutY(230);
        
        IMG_OrderHistory = new Image (CustomerForm.class.getResourceAsStream("icons/4.png"));
        IMGV_OrderHistory = new ImageView(IMG_OrderHistory);
        IMGV_OrderHistory.setPreserveRatio(true);
        IMGV_OrderHistory.setFitHeight(25);
        IMGV_OrderHistory.setLayoutX(25);
        IMGV_OrderHistory.setLayoutY(290);
        
        IMG_Setting = new Image (CustomerForm.class.getResourceAsStream("icons/5.png"));
        IMGV_Setting = new ImageView(IMG_Setting);
        IMGV_Setting.setPreserveRatio(true);
        IMGV_Setting.setFitHeight(25);
        IMGV_Setting.setLayoutX(25);
        IMGV_Setting.setLayoutY(350);
        
        IMG_Logout = new Image (CustomerForm.class.getResourceAsStream("icons/6.png"));
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
        
        Pane_SideMenu = new Pane();
        Pane_SideMenu.getChildren().addAll(Pane_Selected, Pane_User, btnPM , btnM , btnC , btnOrderHistory , btnSetting , btnLogout , IMGV_PM , IMGV_M , IMGV_C , IMGV_OrderHistory , IMGV_Setting , IMGV_Logout);
        Pane_SideMenu.setId("Sidebar");
        Pane_SideMenu.setMinWidth(250);
        Pane_SideMenu.setMinHeight(600);
        Pane_SideMenu.setLayoutX(0);
        Pane_SideMenu.setLayoutY(0);
        //-----------------------End SideMenu---------------------------------//
        
        //-----------------------PM Section-----------------------------------//
        btnAddPM = new Button("Add To Cart");
        btnAddPM.setId("addtocartBtn");
        btnAddPM.setLayoutX(20);
        btnAddPM.setLayoutY(396);
        btnAddPM.setMinWidth(200);
        btnAddPM.setMaxHeight(40);
        btnAddPM.setMinHeight(40);
        btnAddPM.setDisable(true);
        
        lblPM = new Label("Prescription Medications");
        lblPM.setId("Title");
        lblPM.setLayoutY(25);

        Pane_PM_Header = new Pane();
        Pane_PM_Header.setId("topPage");
        Pane_PM_Header.getChildren().setAll(lblPM);
        lblPM.layoutXProperty().bind(Pane_PM_Header.widthProperty().subtract(lblPM.widthProperty()).divide(2));
        
        tablePM = new TableView();
        tablePM.setId("productsTable");
        tablePM.setPlaceholder(new Label("There is no products !"));
        tablePM.setLayoutX(20);
        tablePM.setLayoutY(100);
        tablePM.setMinWidth(624);
        tablePM.setMaxWidth(624); 
        tablePM.setMinHeight(280); 
        tablePM.setMaxHeight(280); 
        tablePM.setEditable(true);
        
        TableColumn TC_PMNamme = new TableColumn("Product name");
        TC_PMNamme.setCellValueFactory(new PropertyValueFactory<>("name"));
        TC_PMNamme.setMinWidth(110);
        TC_PMNamme.setMaxWidth(110);

        TableColumn TC_PMCode = new TableColumn("Code");
        TC_PMCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        TC_PMCode.setMinWidth(102);
        TC_PMCode.setMaxWidth(102);


        TableColumn TC_PMPrice = new TableColumn("Price");
        TC_PMPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TC_PMPrice.setMinWidth(102);
        TC_PMPrice.setMaxWidth(102);

        TableColumn TC_PMPurpose = new TableColumn("Purpose");
        TC_PMPurpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        TC_PMPurpose.setMinWidth(102);
        TC_PMPurpose.setMaxWidth(102);

        TableColumn TC_PMAdultDose = new TableColumn("Adult dose");
        TC_PMAdultDose.setCellValueFactory(new PropertyValueFactory<>("adultDose"));
        TC_PMAdultDose.setMinWidth(102);
        TC_PMAdultDose.setMaxWidth(102);

        TableColumn TC_PMChildDose = new TableColumn("Child dose");
        TC_PMChildDose.setCellValueFactory(new PropertyValueFactory<>("childDose"));
        TC_PMChildDose.setMinWidth(102);
        TC_PMChildDose.setMaxWidth(102);
        
        tablePM.getColumns().setAll(TC_PMNamme,TC_PMCode,TC_PMPrice,TC_PMPurpose,TC_PMAdultDose,TC_PMChildDose);
        
        IMG_Prescription = new Image (CustomerForm.class.getResourceAsStream("icons/PrescriptionIcon.png"));
        IMGV_Prescription = new ImageView(IMG_Prescription);;
        IMGV_Prescription.setFitHeight(25);
        IMGV_Prescription.setPreserveRatio(true);
        IMGV_Prescription.setLayoutX(454);
        IMGV_Prescription.setLayoutY(404);
        
        IMG_Correct = new Image (CustomerForm.class.getResourceAsStream("icons/Correct.png"));;
        IMGV_Correct = new ImageView(IMG_Correct);;
        IMGV_Correct.setPreserveRatio(true);
        IMGV_Correct.setFitHeight(9);
        IMGV_Correct.setLayoutX(620);
        IMGV_Correct.setLayoutY(412);
        IMGV_Correct.setVisible(false);

        IMG_Wrong = new Image (CustomerForm.class.getResourceAsStream("icons/Wrong.png"));;
        IMGV_Wrong = new ImageView(IMG_Wrong);;
        IMGV_Wrong.setPreserveRatio(true);
        IMGV_Wrong.setFitHeight(9);
        IMGV_Wrong.setLayoutX(620);
        IMGV_Wrong.setLayoutY(412);
        IMGV_Wrong.setVisible(false);
        
        txtPrescriptionCode = new TextField();
        txtPrescriptionCode.setPromptText("Enter Prescription Code");
        txtPrescriptionCode.setMaxHeight(40);
        txtPrescriptionCode.setMinHeight(40);
        txtPrescriptionCode.setLayoutX(444);
        txtPrescriptionCode.setLayoutY(396);
        txtPrescriptionCode.setMinWidth(200);
        txtPrescriptionCode.setStyle("-fx-padding: 2px 14px 2px 40px !important;");
        
        Pane_PM_Body = new Pane();
        Pane_PM_Body.setLayoutX(250);
        Pane_PM_Body.setId("Page1");
        Pane_PM_Body.getChildren().setAll(Pane_PM_Header , tablePM , txtPrescriptionCode , IMGV_Wrong , IMGV_Correct ,IMGV_Prescription , btnAddPM);
        //-----------------------End PM Section-------------------------------//
        //-----------------------Medication Section---------------------------//
        btnAddM = new Button("Add To Cart");
        btnAddM.setId("addtocartBtn");
        btnAddM.setLayoutX(20);
        btnAddM.setLayoutY(396);
        btnAddM.setMinWidth(200);
        btnAddM.setMaxHeight(40);
        btnAddM.setMinHeight(40);
        btnAddM.setDisable(true);

        lblM = new Label("Medication");
        lblM.setId("Title");
        lblM.setLayoutY(25);
        lblM.setLayoutX(200);

        Pane_M_Header = new Pane();
        Pane_M_Header.setId("topPage");
        Pane_M_Header.getChildren().setAll(lblM);
        lblM.layoutXProperty().bind(Pane_M_Header.widthProperty().subtract(lblM.widthProperty()).divide(2));

        TableColumn TC_MNamme = new TableColumn("Product name");
        TC_MNamme.setCellValueFactory(new PropertyValueFactory<>("name"));
        TC_MNamme.setMinWidth(110);
        TC_MNamme.setMaxWidth(110);

        TableColumn  TC_MCode = new TableColumn("Code");
        TC_MCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        TC_MCode.setMinWidth(102);
        TC_MCode.setMaxWidth(102);


        TableColumn  TC_MPrice = new TableColumn("Price");
        TC_MPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TC_MPrice.setMinWidth(102);
        TC_MPrice.setMaxWidth(102);

        TableColumn  TC_MPurpose = new TableColumn("Purpose");
        TC_MPurpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        TC_MPurpose.setMinWidth(102);
        TC_MPurpose.setMaxWidth(102);

        TableColumn  TC_MAdultDose = new TableColumn("Adult dose");
        TC_MAdultDose.setCellValueFactory(new PropertyValueFactory<>("adultDose"));
        TC_MAdultDose.setMinWidth(102);
        TC_MAdultDose.setMaxWidth(102);

        TableColumn  TC_MChildDose = new TableColumn("Child dose");
        TC_MChildDose.setCellValueFactory(new PropertyValueFactory<>("childDose"));
        TC_MChildDose.setMinWidth(102);
        TC_MChildDose.setMaxWidth(102);

        
        tableM = new TableView();
        tableM.setId("productsTable");
        tableM.setPlaceholder(new Label("There is no products !"));
        tableM.setLayoutX(20);
        tableM.setLayoutY(100);
        tableM.setMinWidth(624); //Edited
        tableM.setMaxWidth(624); //Edited
        tableM.setMinHeight(280); //Edited
        tableM.setMaxHeight(280); //Edited
        tableM.setEditable(true);
        tableM.getColumns().setAll(TC_MNamme,TC_MCode,TC_MPrice,TC_MPurpose,TC_MAdultDose,TC_MChildDose);
        
        Pane_M_Body = new Pane();
        Pane_M_Body.setLayoutX(250);
        Pane_M_Body.setId("Page2");
        Pane_M_Body.getChildren().setAll(Pane_M_Header , tableM, btnAddM);
        //-----------------------End Medication Section-----------------------//
        //-----------------------Cosmetics Section----------------------------//
        btnAddC = new Button("Add To Cart");
        btnAddC.setId("addtocartBtn");
        btnAddC.setLayoutX(20);
        btnAddC.setLayoutY(396);
        btnAddC.setMinWidth(200);
        btnAddC.setMaxHeight(40);
        btnAddC.setMinHeight(40);
        btnAddC.setDisable(true);

        lblC = new Label("Cosmetics");
        lblC.setId("Title");
        lblC.setLayoutY(25);
        lblC.setLayoutX(200);

        Pane_C_Header = new Pane();
        Pane_C_Header.setId("topPage");
        Pane_C_Header.getChildren().setAll(lblC);
        lblC.layoutXProperty().bind(Pane_C_Header.widthProperty().subtract(lblC.widthProperty()).divide(2));

        TableColumn TC_CName = new TableColumn("Product name");
        TC_CName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TC_CName.setMinWidth(156);
        TC_CName.setMaxWidth(156);
        
        TableColumn TC_CCode = new TableColumn("Code");
        TC_CCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        TC_CCode.setMinWidth(155);
        TC_CCode.setMaxWidth(155);

        TableColumn TC_CPrice = new TableColumn("Price");
        TC_CPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TC_CPrice.setMinWidth(156);
        TC_CPrice.setMaxWidth(156);

        TableColumn TC_CCategory = new TableColumn("Category");
        TC_CCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        TC_CCategory.setMinWidth(156);
        TC_CCategory.setMaxWidth(156);

        tableC = new TableView();
        tableC.setId("productsTable");
        tableC.setPlaceholder(new Label("There is no products !"));
        tableC.setLayoutX(20);
        tableC.setLayoutY(100);
        tableC.setMinWidth(624);
        tableC.setMaxWidth(624);
        tableC.setMinHeight(280); 
        tableC.setMaxHeight(280);
        tableC.setEditable(true);
        tableC.getColumns().setAll(TC_CName,TC_CCode,TC_CPrice,TC_CCategory);
        
        Pane_C_Body = new Pane();
        Pane_C_Body.setLayoutX(250);
        Pane_C_Body.setId("Page3");
        Pane_C_Body.getChildren().setAll(Pane_C_Header, tableC, btnAddC);
        //-----------------------End Cosmetics Section-----------------------//
        //-----------------------Order History Section-----------------------//
        lblOrderHistory = new Label("Orders History");
        lblOrderHistory.setId("Title");
        lblOrderHistory.setLayoutY(25);
        lblOrderHistory.setLayoutX(200);
        
        btnOrderDetails = new Button("View Order Details");
        btnOrderDetails.setLayoutX(20);
        btnOrderDetails.setLayoutY(400);
        btnOrderDetails.setId("DetailsBtn");
        
        Pane_OrderHistory_Header = new Pane();
        Pane_OrderHistory_Header.setId("topPage");
        Pane_OrderHistory_Header.getChildren().setAll(lblOrderHistory);
        lblOrderHistory.layoutXProperty().bind(Pane_OrderHistory_Header.widthProperty().subtract(lblOrderHistory.widthProperty()).divide(2));
        
        tableOrderHistory = new TableView();
        tableOrderHistory.setPlaceholder(new Label("You didn't made any orders !"));
        tableOrderHistory.setLayoutX(20);
        tableOrderHistory.setLayoutY(100);
        tableOrderHistory.setMinWidth(624); 
        tableOrderHistory.setMaxWidth(624); 
        tableOrderHistory.setMinHeight(280); 
        tableOrderHistory.setMaxHeight(280); 
        tableOrderHistory.setEditable(true);

        TableColumn TC_OrderId = new TableColumn("Order ID");
        TC_OrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        TC_OrderId.setMinWidth(120);
        TC_OrderId.setMaxWidth(120);

        TableColumn TC_OrderDate = new TableColumn("Order Date");
        TC_OrderDate.setCellValueFactory(new PropertyValueFactory("orderDate"));
        TC_OrderDate.setMinWidth(130);
        TC_OrderDate.setMaxWidth(130);

        TableColumn TC_OrderTime = new TableColumn("Order Time");
        TC_OrderTime.setCellValueFactory(new PropertyValueFactory("orderTime"));
        TC_OrderTime.setMinWidth(110);
        TC_OrderTime.setMaxWidth(110);

        TableColumn TC_OrderPrice = new TableColumn("Total Price");
        TC_OrderPrice.setCellValueFactory(new PropertyValueFactory("orderPrice"));
        TC_OrderPrice.setMinWidth(130);
        TC_OrderPrice.setMaxWidth(130);

        TableColumn TC_OrderStatus = new TableColumn("Order Status");
        TC_OrderStatus.setMinWidth(130);
        TC_OrderStatus.setMaxWidth(130);
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
        tableOrderHistory.getColumns().setAll(TC_OrderId , TC_OrderDate , TC_OrderTime , TC_OrderPrice , TC_OrderStatus);
        
        Pane_OrderHistory_Body = new Pane();
        Pane_OrderHistory_Body.setLayoutX(250);
        Pane_OrderHistory_Body.setId("Page4");
        Pane_OrderHistory_Body.getChildren().setAll(Pane_OrderHistory_Header , tableOrderHistory , btnOrderDetails);
        //-----------------------End Order History Section--------------------//
        //-----------------------Setting Section------------------------------//
        IMG_EditCustomer = new Image (CustomerForm.class.getResourceAsStream("icons/editCustomer.png"));;
        IMGV_EditCustomer = new ImageView(IMG_EditCustomer);;
        IMGV_EditCustomer.setFitHeight(100);
        IMGV_EditCustomer.setPreserveRatio(true);
        IMGV_EditCustomer.setLayoutX(280);
        IMGV_EditCustomer.setLayoutY(33);

        IMG_FirstName = new Image (CustomerForm.class.getResourceAsStream("icons/firstname.png"));;
        IMGV_FirstName = new ImageView(IMG_FirstName);;
        IMGV_FirstName.setFitHeight(33);
        IMGV_FirstName.setPreserveRatio(true);
        IMGV_FirstName.setLayoutX(90);
        IMGV_FirstName.setLayoutY(178);

        IMG_LastName = new Image (CustomerForm.class.getResourceAsStream("icons/lastname.png"));;
        IMGV_LastName = new ImageView(IMG_LastName);;
        IMGV_LastName.setFitHeight(33);
        IMGV_LastName.setPreserveRatio(true);
        IMGV_LastName.setLayoutX(350);
        IMGV_LastName.setLayoutY(178);

        IMG_Username = new Image (CustomerForm.class.getResourceAsStream("icons/UserIcon.png"));;
        IMGV_Username = new ImageView(IMG_Username);;
        IMGV_Username.setFitHeight(35);
        IMGV_Username.setPreserveRatio(true);
        IMGV_Username.setLayoutX(90);
        IMGV_Username.setLayoutY(250);

        IMG_Password = new Image (CustomerForm.class.getResourceAsStream("icons/pass.png"));;
        IMGV_Password = new ImageView(IMG_Password);;
        IMGV_Password.setFitHeight(35);
        IMGV_Password.setPreserveRatio(true);
        IMGV_Password.setLayoutX(350);
        IMGV_Password.setLayoutY(250);

        IMG_Phone = new Image (CustomerForm.class.getResourceAsStream("icons/phone.png"));;
        IMGV_Phone = new ImageView(IMG_Phone);;
        IMGV_Phone.setFitHeight(34);
        IMGV_Phone.setPreserveRatio(true);
        IMGV_Phone.setLayoutX(100);
        IMGV_Phone.setLayoutY(322);
  
        IMG_Gender = new Image (CustomerForm.class.getResourceAsStream("icons/gender.png"));;
        IMGV_Gender = new ImageView(IMG_Gender);;
        IMGV_Gender.setFitHeight(30);
        IMGV_Gender.setPreserveRatio(true);
        IMGV_Gender.setLayoutX(355);
        IMGV_Gender.setLayoutY(325);

        IMG_Address = new Image (CustomerForm.class.getResourceAsStream("icons/address.png"));;
        IMGV_Address = new ImageView(IMG_Address);;
        IMGV_Address.setFitHeight(26);
        IMGV_Address.setPreserveRatio(true);
        IMGV_Address.setLayoutX(355);
        IMGV_Address.setLayoutY(395);
        
        txtFirstname = new TextField (currentuser.getFirstName());
        txtFirstname.setEditable(false);
        txtFirstname.setLayoutX(80);
        txtFirstname.setLayoutY(170);
        txtFirstname.setMinWidth(240);
        txtFirstname.setOpacity(.3);

        
        txtLastname = new TextField(currentuser.getLastName());
        txtLastname.setEditable(false);
        txtLastname.setLayoutX(340);
        txtLastname.setLayoutY(170);
        txtLastname.setMinWidth(240);
        txtLastname.setOpacity(.3);

        
        txtUsername = new TextField (currentuser.getUsername());
        txtUsername.setEditable(false);
        txtUsername.setLayoutX(80);
        txtUsername.setLayoutY(240);
        txtUsername.setMinWidth(240);
        txtUsername.setOpacity(.3);
        txtUsername.setCursor(Cursor.DEFAULT);
        
        
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter new Password");
        txtPassword.setEditable(false);
        txtPassword.setLayoutX(340);
        txtPassword.setLayoutY(240);
        txtPassword.setMinWidth(240);
        txtPassword.setOpacity(.3);
        txtPassword.setCursor(Cursor.DEFAULT);
        
        
        txtPhone = new TextField (currentuser.getPhone());
        txtPhone.setEditable(false);
        txtPhone.setLayoutX(80);
        txtPhone.setLayoutY(310);
        txtPhone.setMinWidth(240);
        txtPhone.setOpacity(.3);
        txtPhone.setCursor(Cursor.DEFAULT);
        
        
        txtAddress = new TextField (currentuser.getShippingAddress());
        txtAddress.setEditable(false);
        txtAddress.setLayoutX(340);
        txtAddress.setLayoutY(380);
        txtAddress.setMinWidth(240);
        txtAddress.setOpacity(.3);
        txtAddress.setCursor(Cursor.DEFAULT);
        
        
        CBGender = new ChoiceBox(FXCollections.observableArrayList("Male","Female"));
        if (currentuser.getGender().equalsIgnoreCase("Male"))
            CBGender.getSelectionModel().select(0);
        else
            CBGender.getSelectionModel().select(1);
        CBGender.setId("gender");
        CBGender.setDisable(true);
        CBGender.setLayoutX(340);
        CBGender.setLayoutY(310);
        CBGender.setMinWidth(240);


        
        CBDay = new ChoiceBox(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"));
        CBDay.getSelectionModel().select(currentuser.getDateOfBirth().getDay()-1);
        CBDay.setDisable(true);
        CBDay.setMaxWidth(70);
        CBDay.setMinWidth(70);
        CBDay.setLayoutX(80);
        CBDay.setLayoutY(380);

        
        CBMonth = new ChoiceBox(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12"));
        CBMonth.getSelectionModel().select(currentuser.getDateOfBirth().getMonth()-1);
        CBMonth.setDisable(true);
        CBMonth.setMaxWidth(78);
        CBMonth.setMinWidth(78);
        CBMonth.setLayoutX(160);
        CBMonth.setLayoutY(380);

        
        CBYear = new ChoiceBox(FXCollections.observableArrayList("1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"));
        CBYear.getSelectionModel().select(currentuser.getDateOfBirth().getYear()-1970);
        CBYear.setDisable(true);
        CBYear.setMaxWidth(70);
        CBYear.setMinWidth(70);
        CBYear.setLayoutX(248);
        CBYear.setLayoutY(380);

        
        btnSave = new Button("Save");
        btnSave.setDisable(true);
        btnSave.setLayoutX(340);
        btnSave.setLayoutY(460);
        btnSave.setMinWidth(240);
        btnSave.setId("submit");

        
        btnEdit = new Button("Edit");
        btnEdit.setLayoutX(80);
        btnEdit.setLayoutY(460);
        btnEdit.setMinWidth(240);
        btnEdit.setId("edit");

        
        btnCancel = new Button("Cancel");
        btnCancel.setLayoutX(80);
        btnCancel.setLayoutY(460);
        btnCancel.setMinWidth(240);
        btnCancel.setVisible(false);
        btnCancel.setId("cancel2");

        Pane_Setting_Header = new Pane();
        Pane_Setting_Header.setId("topPage");
        
        Pane_Setting_Body = new Pane();
        Pane_Setting_Body.setLayoutX(250);
        Pane_Setting_Body.setId("Page5");
        Pane_Setting_Body.getChildren().setAll(Pane_Setting_Header , IMGV_EditCustomer, txtFirstname , txtLastname , txtUsername, txtPassword , txtPhone, CBGender , CBDay , CBMonth , CBYear , txtAddress, btnCancel, btnEdit, btnSave, IMGV_FirstName , IMGV_LastName , IMGV_Username, IMGV_Password, IMGV_Phone, IMGV_Gender, IMGV_Address);
        //--------------------------End Setting Section-----------------------//
        //----------------------------Cart Section----------------------------//
        IMG_Cart = new Image (CustomerForm.class.getResourceAsStream("icons/mycart.png"));;
        IMGV_Cart = new ImageView(IMG_Cart);;
        IMGV_Cart.setPreserveRatio(true);
        IMGV_Cart.setFitHeight(25);
        IMGV_Cart.setLayoutX(25);
        IMGV_Cart.setLayoutY(25);


        
        lblMyCart = new Label("My Cart");
        lblMyCart.setId("mycart");
        lblMyCart.setLayoutX(60);
        lblMyCart.setLayoutY(25);

        
        Pane_Cart_Header = new Pane();
        Pane_Cart_Header.setId("CartArea");
        Pane_Cart_Header.setLayoutX(0);
        Pane_Cart_Header.setLayoutY(0);
        Pane_Cart_Header.getChildren().setAll(IMGV_Cart , lblMyCart);

        
        lblTotalDue = new Label("Total Due : 0 LE");
        lblTotalDue.setLayoutX(20);
        lblTotalDue.setLayoutY(396);
        lblTotalDue.setStyle("-fx-text-fill: #fff;");

        
        btnConfirmOrder = new Button("Confirm Order");
        btnConfirmOrder.setId("confirm");
        btnConfirmOrder.setLayoutX(20);
        btnConfirmOrder.setLayoutY(440);
        btnConfirmOrder.setMinWidth(210);
        btnConfirmOrder.setMinHeight(40);
        btnConfirmOrder.setMaxHeight(40);
        btnConfirmOrder.setDisable(true);
        
        
        
        btnCancelOrder = new Button("Clear");
        btnCancelOrder.setId("cancel");
        btnCancelOrder.setLayoutX(20);
        btnCancelOrder.setLayoutY(490); //Edited
        btnCancelOrder.setMinWidth(210);
        btnCancelOrder.setMinHeight(40);
        btnCancelOrder.setMaxHeight(40);
        btnCancelOrder.setDisable(true);

        
        tableCurrentOrder = new TableView();
        tableCurrentOrder.setPlaceholder(new Label("Your cart is empty !"));

        tableCurrentOrder.setStyle(" -fx-border-width: 0 !important;");
        tableCurrentOrder.setLayoutX(20);
        tableCurrentOrder.setLayoutY(100);
        tableCurrentOrder.setMinWidth(210); //Edited
        tableCurrentOrder.setMaxWidth(210); //Edited
        tableCurrentOrder.setMinHeight(276); //Edited
        tableCurrentOrder.setMaxHeight(276); //Edited
        tableCurrentOrder.setEditable(true);

        TableColumn TC_CartProductName = new TableColumn("Product");
        TC_CartProductName.setMinWidth(80);
        TC_CartProductName.setMaxWidth(80);
        
        TableColumn TC_CartProductPrice = new TableColumn("Price");
        TC_CartProductPrice.setMinWidth(55);
        TC_CartProductPrice.setMaxWidth(55);
        
        TableColumn TC_CartProductQuantity = new TableColumn("Quantity");
        TC_CartProductQuantity.setMinWidth(73);
        TC_CartProductQuantity.setMaxWidth(73);

        TC_CartProductName.setCellValueFactory( new PropertyValueFactory<>("name"));
        TC_CartProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TC_CartProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableCurrentOrder.getColumns().addAll(TC_CartProductName, TC_CartProductPrice, TC_CartProductQuantity);
        

        
        Pane_Cart_Body = new Pane();
        Pane_Cart_Body.getChildren().addAll(Pane_Cart_Header , lblTotalDue , btnConfirmOrder, btnCancelOrder, tableCurrentOrder);
        Pane_Cart_Body.setId("Sidebar");
        Pane_Cart_Body.setMinWidth(250);
        Pane_Cart_Body.setMinHeight(600);
        Pane_Cart_Body.setLayoutX(910);
        Pane_Cart_Body.setLayoutY(0);
        //----------------------------Cart Section----------------------------//
        
        Pane_Main = new Pane();
        Pane_Main.setId("Panel");
        Pane_Main.getChildren().setAll(Pane_SideMenu , Pane_PM_Body , Pane_Cart_Body);
        
        tablePM.setItems(tablePM_data);
        tableM.setItems(tableM_data);
        tableC.setItems(tableC_data);
        tableOrderHistory.setItems(tableCustomerOrders_data);
        tableCurrentOrder.setItems(tableCurrentOrder_data);
    }
}