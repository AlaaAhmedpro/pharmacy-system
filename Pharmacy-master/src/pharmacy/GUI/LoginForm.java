package pharmacy.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import pharmacy.ExceptionClasses.InvalidInputException;
import pharmacy.ExceptionClasses.InvalidLoginException;
import pharmacy.ExceptionClasses.MissingDataException;
import pharmacy.ExceptionClasses.RepeatedObjectException;
import pharmacy.PersonClasses.*;
import pharmacy.ExtraClasses.*;
import pharmacy.MainClasses.PharmacyController;
import pharmacy.MainClasses.PharmacyController.PeopleTable;
import pharmacy.MainClasses.PharmacyController.PrescriptionTable;
import pharmacy.MainClasses.PharmacyController.ProductTable;
import pharmacy.ProductClasses.Cosmetics;
import pharmacy.ProductClasses.Medication;
import pharmacy.ProductClasses.PrescriptionMedication;

public class LoginForm extends Application {
    
    private void btnLogin_OnClick() throws MissingDataException{
        if (txtUsername.getText().isEmpty())
            throw new MissingDataException("Enter the username !");
            
        if (txtPassword.getText().isEmpty())
            throw new MissingDataException("Enter the password !");
            
        try{
            Person currentUser = PeopleTable.Login(txtUsername.getText(), txtPassword.getText());
            if (currentUser instanceof Customer) {
                CustomerForm obj = new CustomerForm();
                obj.setCurrentUser((Customer)currentUser);
                obj.Show();
                MainWindow.close();
            }
            else if (currentUser instanceof AdminWarehouse || currentUser instanceof AdminReport){
                AdminForm obj = new AdminForm();
                obj.setCurrentUser((Staff)currentUser);
                obj.Show();
                MainWindow.close();
            }
            else if (currentUser instanceof Delivery){
                DeliveryForm obj = new DeliveryForm();
                obj.setCurrentUser((Delivery)currentUser);
                obj.Show();
                MainWindow.close();
            }
        }
        catch(InvalidLoginException ex){
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
        }
    }
    
    private void btnSignup_OnClick(){
        if (txtFirstName.getText().equals("")) {
            MsgBox.ShowMsg("OPS!" , "First name field is empty !" , MsgBox.type.error);
            return;
        }

        if (txtLastName.getText().equals("")) {
            MsgBox.ShowMsg("OPS!" , "Last name field is empty !" , MsgBox.type.error);
            return;
        }

        if (txtUsernameReg.getText().equals("")) {
            MsgBox.ShowMsg("OPS!" , "Username field is empty !" , MsgBox.type.error);
            return;
        }

        if (txtPasswordReg.getText().equals("")) {
            MsgBox.ShowMsg("OPS!" , "Password field is empty !" , MsgBox.type.error);
            return;
        }

        if (txtPhone.getText().equals("")) {
           MsgBox.ShowMsg("OPS!" , "Phone field is empty !" , MsgBox.type.error);
           return;
        }

        if (txtAddress.getText().equals("")){
            MsgBox.ShowMsg("OPS!" , "Address field is empty !" , MsgBox.type.error);
            return;
        }
        
        Person temp = null;
        try {
            temp = new Customer(txtUsernameReg.getText(),txtPasswordReg.getText(),txtFirstName.getText(),txtLastName.getText() ,txtPhone.getText(), CBGender.getValue().toString() , new DateTime(1,1,1,Integer.parseInt(CBDay.getValue().toString()),Integer.parseInt(CBMonth.getValue().toString()),Integer.parseInt(CBYear.getValue().toString())) , txtAddress.getText() );
            PeopleTable.addPerson(temp);
            PharmacyController.PeopleTable.WriteToFile();
        } catch (InvalidInputException ex) {
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            return;
        }
        catch (RepeatedObjectException ex){
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            return;
        }
        catch (IOException ex){
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
            return;
        }

        MsgBox.ShowMsg("DONE" , "You have registered successfully !" , MsgBox.type.success);
        MainWindow.setScene(LoginScene);
        MainWindow.setTitle("Pharmacy - Login");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    void fillDB() throws InvalidInputException{
        //Fill Users Data
        try{
        PeopleTable.addPerson(new AdminWarehouse("Admin1","admin1","Ahmed","Mohamed","011543736483","Male", new DateTime(1,1,1,20,12,1995)));
        PeopleTable.addPerson(new AdminReport("Admin2","admin2","Hany","Mustafa","011543736483","Male", new DateTime(1,1,1,20,12,1995)));
        PeopleTable.addPerson(new Customer("Customer1","customer1","Mahmoud","Sami","01274373734","Male" ,new DateTime(1,1,1,20,6,1997) , "13 Margani St. Heliopolis"));
        PeopleTable.addPerson(new Customer("Customer2","customer2","Adel","Rafaat","01274373734","Male" ,new DateTime(1,1,1,20,6,1997) , "13 Margani St. Heliopolis"));
        PeopleTable.addPerson(new Delivery("Delivery1","delivery1","Abdelrahman","Ayman","01274373734","Male",new DateTime(1,1,1,20,6,1997)));
        PeopleTable.addPerson(new Delivery("Delivery2","delivery2","Mustafa","Fouad","01274373734","Male",new DateTime(1,1,1,20,6,1997)));
        }
        catch(Exception e){
            
        }
        

        //Fill Prescription Medication Data
        ProductTable.addProduct(new PrescriptionMedication("Avipect", 1435, 6.5, 3, "Productive Cough","3 Times","2 Times","Y" , new DateTime(1,1,1,12,5,2019) ,"Polpharma Group"));
        ProductTable.addProduct(new PrescriptionMedication("Humex", 1436, 1.5, 6, "Fever","3 Times","2 Times","Y" , new DateTime(1,1,1,12,5,2020) ,"Roche"));
        ProductTable.addProduct(new PrescriptionMedication("Panadol", 1437, 2.5, 12, "Migraine","1 Times","1 Times","Y" , new DateTime(1,1,1,12,5,2021) ,"Polpharma Group"));
        ProductTable.addProduct(new PrescriptionMedication("Catafast", 1438, 15, 15, "Migraine","2 Times","1 Times","Y" , new DateTime(1,1,1,12,5,2018) ,"Teva Group"));
        ProductTable.addProduct(new PrescriptionMedication("Aldomet", 1439, 23.5, 1, "Fever","3 Times","1 Times","Y" , new DateTime(1,1,1,12,5,2019) ,"Biofarm"));
        ProductTable.addProduct(new PrescriptionMedication("Eucaphol", 1440, 8.5, 4, "Cough Sedative","2 Times","1 Times","Y" , new DateTime(1,1,1,9,3,2020) ,"GSK Pharma"));


        ProductTable.addProduct(new Medication("Congestal", 1441, 8.5, 20, "Multi-Symptom","2 Times","1 Times","Y" , new DateTime(1,1,1,9,3,2022) ,"Polpharma Group"));
        ProductTable.addProduct(new Medication("Disflatyl", 1442, 9.5, 5, "Cold","2 Times","1 Times","Y" , new DateTime(1,1,1,9,5,2022) ,"UCB"));
        ProductTable.addProduct(new Medication("Librax", 1443, 8.5, 13, "Flu","2 Times","1 Times","Y" , new DateTime(1,1,1,9,3,2021) ,"Biofarm"));
        ProductTable.addProduct(new Medication("Kaptin", 1444, 7.5, 9, "Cold and Flu","3 Times","2 Times","Y" , new DateTime(1,1,1,16,3,2018) ,"Roche"));
        ProductTable.addProduct(new Medication("Comtrex", 1445, 3.5, 1, "Acute Head Cold","2 Times","1 Times","Y" , new DateTime(1,1,1,9,3,2020) ,"Teva Group"));
        ProductTable.addProduct(new Medication("Actifed", 1446, 17.5, 1, "Wet Cough","2 Times","1 Times","Y" , new DateTime(1,1,1,9,3,2020) ,"Teva Group"));


        ProductTable.addProduct(new Cosmetics("Tooth brush", 1447, 4.5, 4, "Health Care"));
        ProductTable.addProduct(new Cosmetics("Gel Haircode 50ml", 1448, 9.5, 2 , "Hair Care"));
        ProductTable.addProduct(new Cosmetics("Pantine Hair Cream 100ml", 1449, 10.5, 8, "Hair Care"));
        ProductTable.addProduct(new Cosmetics("Clear Shampoo 400ml", 1450, 17.5, 10 , "Hair Care"));
        ProductTable.addProduct(new Cosmetics("Signal Toothpaste", 1451, 2.75, 6, "Health Care"));
        ProductTable.addProduct(new Cosmetics("Nivea Protect & Care", 1452, 23, 16 , "Skin & Body Care"));
        
        ArrayList <PrescriptionMedication> MedicationList;

        //Prescription with code 11111111 contains medications with code {1435 , 1436}
        MedicationList = new ArrayList<>();
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1435));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1436));
        PrescriptionTable.addPrescription(new Prescription(11111111, MedicationList));


        //Prescription with code 22222222 contains medications with code {1437 , 1438}
        MedicationList = new ArrayList<>();
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1437));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1438));
        PrescriptionTable.addPrescription(new Prescription(22222222, MedicationList));


        //Prescription with code 33333333 contains medications with code {1439 , 1440}
        MedicationList = new ArrayList<>();
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1437));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1438));
        PrescriptionTable.addPrescription(new Prescription(33333333, MedicationList));


        //Prescription with code 12345678 allows you to buy any or all medications
        MedicationList = new ArrayList<>();
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1435));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1436));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1437));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1438));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1439));
        MedicationList.add((PrescriptionMedication) ProductTable.getProduct(1440));
        PrescriptionTable.addPrescription(new Prescription(12345678, MedicationList));
        
        try{
          PharmacyController.OrderTable.WriteToFile();
        PharmacyController.PeopleTable.WriteToFile();
        PharmacyController.ProductTable.WriteToFile();
        PharmacyController.PrescriptionTable.WriteToFile(); 
        }
        catch(Exception e){
            
        }
        
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        try{
            pharmacy.MainClasses.PharmacyController.PeopleTable.ReadFromFile();
            pharmacy.MainClasses.PharmacyController.OrderTable.ReadFromFile();
            pharmacy.MainClasses.PharmacyController.PrescriptionTable.ReadFromFile();
            pharmacy.MainClasses.PharmacyController.ProductTable.ReadFromFile();
        }
        catch (ClassNotFoundException | IOException ex){
            MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
        }        
        
        MainWindow = primaryStage;
        InitializeComponents();
        
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        txtUsername.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                Pane_Login.requestFocus();
                firstTime.setValue(false);
            }
        });

        final BooleanProperty firstTime2 = new SimpleBooleanProperty(true);
        txtFirstName.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime2.get()){
                Pane_Register.requestFocus();
                firstTime2.setValue(false);
            }
        });

        MainWindow.show();

        //Events Start
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    btnLogin_OnClick();
                } catch (MissingDataException ex) {
                   MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
                }
            }
        });

        btnRegister.setOnMouseClicked(event -> {
            MainWindow.setScene(RegisterScene);
            MainWindow.setTitle("Pharmacy - Register");
        });


        lblBackLogin.setOnMouseClicked(event -> {
            MainWindow.setScene(LoginScene);
            MainWindow.setTitle("Pharmacy - Login");
        });

        btnSignup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnSignup_OnClick();
            }
        });

        //Events End


    }

    
    Stage MainWindow;
    Scene LoginScene;
    Scene RegisterScene;
    Pane Pane_Login;
    Pane Pane_Register;
    //-------Login Components-----------//
    Image IMG_SideLogo;
    Image IMG_Username;
    Image IMG_Password;
    
    ImageView IMGV_SideLogo;
    ImageView IMGV_Username;
    ImageView IMGV_Password;
    
    TextField txtUsername;
    PasswordField txtPassword;
    
    Button btnRegister;
    Button btnLogin;
    //--------Registration Component-----//
    Image IMG_FirstName;
    Image IMG_LastName;
    Image IMG_UsernameReg;
    Image IMG_PasswordReg;
    Image IMG_Phone;
    Image IMG_Gender;
    Image IMG_Address;
    
    ImageView IMGV_FirstName;
    ImageView IMGV_LastName;
    ImageView IMGV_UsernameReg;
    ImageView IMGV_PasswordReg;
    ImageView IMGV_Phone;
    ImageView IMGV_Gender;
    ImageView IMGV_Address;
    
    TextField txtFirstName;
    TextField txtLastName;
    TextField txtUsernameReg;
    TextField txtPhone;
    TextField txtAddress;
    PasswordField txtPasswordReg;
    
    ChoiceBox CBGender;
    ChoiceBox CBDay;
    ChoiceBox CBMonth;
    ChoiceBox CBYear;
    
    Button btnSignup;
    
    Label lblBackLogin;
    
    private void InitializeComponents(){

        MainWindow.setResizable(false);
        MainWindow.setTitle("Pharmacy - Login");
        //Start Login
        IMG_SideLogo = new Image (getClass().getResourceAsStream("icons/1.png"));;
        IMGV_SideLogo = new ImageView(IMG_SideLogo);;
        IMGV_SideLogo.setFitHeight(200);
        IMGV_SideLogo.setPreserveRatio(true);
        IMGV_SideLogo.setLayoutX(50);
        IMGV_SideLogo.setLayoutY(90);

        IMG_Username = new Image (getClass().getResourceAsStream("icons/user.png"));;
        IMGV_Username = new ImageView(IMG_Username);;
        IMGV_Username.setFitHeight(35);
        IMGV_Username.setPreserveRatio(true);
        IMGV_Username.setLayoutX(290);
        IMGV_Username.setLayoutY(110);

        IMG_Password = new Image (getClass().getResourceAsStream("icons/pass.png"));;
        IMGV_Password = new ImageView(IMG_Password);;
        IMGV_Password.setFitHeight(35);
        IMGV_Password.setPreserveRatio(true);
        IMGV_Password.setLayoutX(290);
        IMGV_Password.setLayoutY(175);
   
        txtUsername = new TextField ();
        txtUsername.setPromptText("Enter your Username");
        txtUsername.setLayoutX(280);
        txtUsername.setLayoutY(100);
        txtUsername.setMinWidth(300);
        txtUsername.setId("TextField");
        
        txtPassword = new PasswordField ();
        txtPassword.setPromptText("Enter your Password");
        txtPassword.setLayoutX(280);
        txtPassword.setLayoutY(165);
        txtPassword.setMinWidth(300);

        btnRegister = new Button("Register");
        btnRegister.setLayoutX(280);
        btnRegister.setLayoutY(234);
        btnRegister.setMinWidth(144);
        btnRegister.setId("Register");

        btnLogin = new Button("Login");
        btnLogin.setLayoutX(435);
        btnLogin.setLayoutY(234);
        btnLogin.setMinWidth(144);
        btnLogin.setId("Login");

        //End Login
        
        //Start Register
    

        IMG_FirstName = new Image (getClass().getResourceAsStream("icons/firstname.png"));;
        IMGV_FirstName = new ImageView(IMG_FirstName);;
        IMGV_FirstName.setFitHeight(33);
        IMGV_FirstName.setPreserveRatio(true);
        IMGV_FirstName.setLayoutX(90);
        IMGV_FirstName.setLayoutY(98);
        
        IMG_LastName = new Image (getClass().getResourceAsStream("icons/lastname.png"));;
        IMGV_LastName = new ImageView(IMG_LastName);;
        IMGV_LastName.setFitHeight(33);
        IMGV_LastName.setPreserveRatio(true);
        IMGV_LastName.setLayoutX(350);
        IMGV_LastName.setLayoutY(98);

        IMG_UsernameReg = new Image (getClass().getResourceAsStream("icons/UserIcon.png"));;
        IMGV_UsernameReg = new ImageView(IMG_UsernameReg);;
        IMGV_UsernameReg.setFitHeight(35);
        IMGV_UsernameReg.setPreserveRatio(true);
        IMGV_UsernameReg.setLayoutX(90);
        IMGV_UsernameReg.setLayoutY(170);
        
        IMG_PasswordReg = new Image (getClass().getResourceAsStream("icons/pass.png"));;
        IMGV_PasswordReg = new ImageView(IMG_PasswordReg);;
        IMGV_PasswordReg.setFitHeight(35);
        IMGV_PasswordReg.setPreserveRatio(true);
        IMGV_PasswordReg.setLayoutX(350);
        IMGV_PasswordReg.setLayoutY(170);

        IMG_Phone = new Image (getClass().getResourceAsStream("icons/phone.png"));;
        IMGV_Phone = new ImageView(IMG_Phone);;
        IMGV_Phone.setFitHeight(34);
        IMGV_Phone.setPreserveRatio(true);
        IMGV_Phone.setLayoutX(100);
        IMGV_Phone.setLayoutY(242);

        IMG_Gender = new Image (getClass().getResourceAsStream("icons/gender.png"));;
        IMGV_Gender = new ImageView(IMG_Gender);;
        IMGV_Gender.setFitHeight(30);
        IMGV_Gender.setPreserveRatio(true);
        IMGV_Gender.setLayoutX(355);
        IMGV_Gender.setLayoutY(245);

        IMG_Address = new Image (getClass().getResourceAsStream("icons/address.png"));;
        IMGV_Address = new ImageView(IMG_Address);;
        IMGV_Address.setFitHeight(26);
        IMGV_Address.setPreserveRatio(true);
        IMGV_Address.setLayoutX(355);
        IMGV_Address.setLayoutY(315);
        
        txtFirstName = new TextField ();
        txtFirstName.setPromptText("Enter your first name");
        txtFirstName.setLayoutX(80);
        txtFirstName.setLayoutY(88);
        txtFirstName.setMinWidth(240);

        txtLastName = new TextField ();
        txtLastName.setPromptText("Enter your last name");
        txtLastName.setLayoutX(340);
        txtLastName.setLayoutY(88);
        txtLastName.setMinWidth(240);

        txtUsernameReg = new TextField ();
        txtUsernameReg.setPromptText("Enter your Username");
        txtUsernameReg.setLayoutX(80);
        txtUsernameReg.setLayoutY(160);
        txtUsernameReg.setMinWidth(240);


        txtPasswordReg = new PasswordField ();
        txtPasswordReg.setPromptText("Enter your Password");
        txtPasswordReg.setLayoutX(340);
        txtPasswordReg.setLayoutY(160);
        txtPasswordReg.setMinWidth(240);

        txtPhone = new TextField ();
        txtPhone.setPromptText("Enter your Phone");
        txtPhone.setLayoutX(80);
        txtPhone.setLayoutY(230);
        txtPhone.setMinWidth(240);
        
        txtAddress = new TextField ();
        txtAddress.setPromptText("Enter your address");
        txtAddress.setLayoutX(340);
        txtAddress.setLayoutY(300);
        txtAddress.setMinWidth(240);
        
        CBGender = new ChoiceBox(FXCollections.observableArrayList("Male","Female"));
        CBGender.getSelectionModel().selectFirst();
        CBGender.setId("gender");
        CBGender.setLayoutX(340);
        CBGender.setLayoutY(230);
        CBGender.setMinWidth(240);

        CBDay = new ChoiceBox(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"));
        CBDay.getSelectionModel().selectFirst();
        CBDay.setMaxWidth(70);
        CBDay.setMinWidth(70);
        CBDay.setLayoutX(80);
        CBDay.setLayoutY(300);

        CBMonth = new ChoiceBox(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12"));
        CBMonth.getSelectionModel().selectFirst();
        CBMonth.setMaxWidth(78);
        CBMonth.setMinWidth(78);
        CBMonth.setLayoutX(160);
        CBMonth.setLayoutY(300);

        CBYear = new ChoiceBox(FXCollections.observableArrayList("1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"));
        CBYear.getSelectionModel().selectFirst();
        CBYear.setMaxWidth(70);
        CBYear.setMinWidth(70);
        CBYear.setLayoutX(248);
        CBYear.setLayoutY(300);

        btnSignup = new Button("Sing up");
        btnSignup.setLayoutX(80);
        btnSignup.setLayoutY(380);
        btnSignup.setMinWidth(500);
        btnSignup.setId("submit");

        lblBackLogin = new Label("Back to Login");
        lblBackLogin.setLayoutX(286);
        lblBackLogin.setLayoutY(472);
        
        //------------------------------------
        Pane_Login = new Pane();
        Pane_Login.setId("grad");
        Pane_Login.getChildren().setAll(IMGV_SideLogo, txtUsername, txtPassword , btnRegister, btnLogin , IMGV_Username, IMGV_Password);

        Pane_Register = new Pane();
        Pane_Register.setId("grad");
        Pane_Register.getChildren().setAll(txtFirstName , txtLastName , txtUsernameReg, txtPasswordReg , txtPhone, txtAddress ,CBGender , CBDay , CBMonth , CBYear ,  btnSignup, IMGV_FirstName, IMGV_LastName , IMGV_UsernameReg, IMGV_PasswordReg, IMGV_Phone, IMGV_Gender, IMGV_Address , lblBackLogin);
        
        LoginScene = new Scene(Pane_Login , 660 , 400);
        LoginScene.getStylesheets().add("pharmacy/GUI/style.css");
        
        RegisterScene = new Scene(Pane_Register, 660,550);
        RegisterScene.getStylesheets().add("pharmacy/GUI/style.css");
        
        MainWindow.setScene(LoginScene);
    }
}
