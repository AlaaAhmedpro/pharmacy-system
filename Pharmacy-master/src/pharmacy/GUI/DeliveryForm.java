package pharmacy.GUI;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pharmacy.MainClasses.Order;
import pharmacy.MainClasses.PharmacyController;

import pharmacy.PersonClasses.Delivery;

import pharmacy.ProductClasses.*;

public class DeliveryForm {
    private Delivery currentuser;
    private Order ReceiveOrder;
    private final ObservableList<Product> tableProducts_data = FXCollections.observableArrayList();
    public void setCurrentUser(Delivery c){
        currentuser = c;
    }
    Stage MainWindow;
    Pane Pane_Main;
    
    Image IMG_Scooter;
    Image IMG_Report;
    ImageView IMGV_Report;
    ImageView IMGV_Scooter;
    
    Button btnLogout;
    Button btnDelivered;
   
    Label lblNullOrders;
    Label OrderId;
    Label OrderStatusTxt;
    Label lblOrderStatus;
    Label lblOrderDate;
    Label lblOrderTime;
    Label lblTotalDue;
    Label lblShippingAddress;
            
    TableView tableProductsList;
    private void InitializeComponents(){
        
        
        MainWindow = new Stage();
        MainWindow.setResizable(false);
        MainWindow.setTitle("Pharmacy - Dilvery Panel");
        
        Pane_Main = new Pane();
        Pane_Main.setId("Panel");

        IMG_Scooter = new Image(MsgBox.class.getResourceAsStream("icons/bike.png"));
        IMGV_Scooter = new ImageView(IMG_Scooter);
        IMGV_Scooter.setFitHeight(220);
        IMGV_Scooter.setPreserveRatio(true);
        IMGV_Scooter.setLayoutX(140);
        IMGV_Scooter.setLayoutY(100);

        lblNullOrders = new Label("You have no orders to deliver !");
        lblNullOrders.setLayoutY(360);
        lblNullOrders.setLayoutX(166);

        btnLogout = new Button("Logout");
        btnLogout.setId("cancel");
        btnLogout.setMinWidth(460);
        btnLogout.setMinHeight(50);
        btnLogout.setLayoutX(20);
        btnLogout.setLayoutY(550);
        
        if (ReceiveOrder == null)
            return;
        IMG_Report = new Image(MsgBox.class.getResourceAsStream("icons/report.png"));
        IMGV_Report = new ImageView(IMG_Report);;
        IMGV_Report.setFitHeight(50);
        IMGV_Report.setPreserveRatio(true);
        IMGV_Report.setLayoutX(15);
        IMGV_Report.setLayoutY(28);

        OrderId = new Label("Order ID #" + ReceiveOrder.getOrderId());
        OrderId.setLayoutX(80);
        OrderId.setLayoutY(30);

        OrderStatusTxt = new Label("Status : ");
        OrderStatusTxt.setLayoutX(80);
        OrderStatusTxt.setLayoutY(55);

        lblOrderStatus = new Label(ReceiveOrder.getOrderStatus());
        lblOrderStatus.setLayoutX(126);
        lblOrderStatus.setLayoutY(55);
        if (ReceiveOrder.getOrderStatus().equals("Pending")) {
            lblOrderStatus.setStyle("-fx-text-fill: #cc0000;");
        } else if (ReceiveOrder.getOrderStatus().equals("Is delivering ..")) {
            lblOrderStatus.setStyle("-fx-text-fill: #65879e;");
        } else if (ReceiveOrder.getOrderStatus().equals("Completed")) {
            lblOrderStatus.setStyle("-fx-text-fill: #00a800;");
        }

        lblOrderDate = new Label("Date : " + ReceiveOrder.getOrderDate());
        lblOrderDate.setLayoutX(375);
        lblOrderDate.setLayoutY(30);

        lblOrderTime = new Label("     Time : " + ReceiveOrder.getOrderTime());
        lblOrderTime.setLayoutX(378);
        lblOrderTime.setLayoutY(55);

        lblTotalDue = new Label("Total Due : " + ReceiveOrder.getOrderPrice()+ " LE");
        lblTotalDue.setLayoutX(374);
        lblTotalDue.setLayoutY(450);
 
        tableProductsList = new TableView();
        tableProductsList.setLayoutX(20);
        tableProductsList.setLayoutY(110);
        tableProductsList.setMinWidth(460); //Edited
        tableProductsList.setMaxWidth(460); //Edited
        tableProductsList.setMinHeight(320); //Edited
        tableProductsList.setMaxHeight(320); //Edited
        tableProductsList.setEditable(true);

        tableProducts_data.addAll(ReceiveOrder.getProductList());
        
        TableColumn ProductCode = new TableColumn("Product Code");
        ProductCode.setCellValueFactory(new PropertyValueFactory("code"));
        ProductCode.setMinWidth(96);
        ProductCode.setMaxWidth(96);
        
        TableColumn ProductName = new TableColumn("Product Name");
        ProductName.setCellValueFactory(new PropertyValueFactory("name"));
        ProductName.setMinWidth(160);
        ProductName.setMaxWidth(160);

        TableColumn ProductPrice = new TableColumn("Price");
        ProductPrice.setCellValueFactory(new PropertyValueFactory("Price"));
        ProductPrice.setMinWidth(100);
        ProductPrice.setMaxWidth(100);
 
        TableColumn ProductQuantity = new TableColumn("Quantity");
        ProductQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        ProductQuantity.setMinWidth(100);
        ProductQuantity.setMaxWidth(100);

        tableProductsList.getColumns().setAll(ProductCode, ProductName, ProductPrice, ProductQuantity);
        tableProductsList.setItems(tableProducts_data);

        lblShippingAddress = new Label("Shipping Address : " + ReceiveOrder.getOrderCustomer().getShippingAddress());
        lblShippingAddress.setLayoutX(20);
        lblShippingAddress.setLayoutY(450);

        btnDelivered = new Button("Delivered");
        btnDelivered.setId("confirm");
        btnDelivered.setMinWidth(460);
        btnDelivered.setMinHeight(50);
        btnDelivered.setLayoutX(20);
        btnDelivered.setLayoutY(490);
    }
    public void Show() {
        ReceiveOrder = currentuser.isAvailable() ? null : PharmacyController.OrderTable.getOrder(currentuser.getOrders().get(currentuser.MyOrders_Number() - 1).getOrderId());
        InitializeComponents();
        
        if (!currentuser.isAvailable()) {
            Pane_Main.getChildren().setAll(IMGV_Report, OrderId, OrderStatusTxt, lblOrderStatus, lblOrderDate, lblOrderTime, tableProductsList, lblShippingAddress, lblTotalDue , btnDelivered , btnLogout);
        } else {        
            Pane_Main.getChildren().setAll(IMGV_Scooter , lblNullOrders , btnLogout);
        }

        Scene scene = new Scene(Pane_Main , 500, 620);
        scene.getStylesheets().add("pharmacy/GUI/style.css");
        MainWindow.setScene(scene);
        MainWindow.show();
        
        if (!currentuser.isAvailable()){
            btnDelivered.setOnMouseClicked(event -> {
                ReceiveOrder.setOrderStatus("Completed");
                currentuser.setAvailable();
                try {
                    PharmacyController.OrderTable.WriteToFile();
                    PharmacyController.PeopleTable.WriteToFile();
                } catch (IOException ex) {
                    MsgBox.ShowMsg("OPS!!", ex.getMessage(), MsgBox.type.error);
                }
                MsgBox.ShowMsg("DONE","The order status has been recorded as delivered", MsgBox.type.success);
                Pane_Main.getChildren().setAll(IMGV_Scooter , lblNullOrders , btnLogout);
            });
        }
        
        btnLogout.setOnMouseClicked(event -> {
            if (MsgBox.ShowMsg("Logout", "Are you sure you want to log out and quit ?", MsgBox.type.warning)) {
                LoginForm obj = new LoginForm();
                obj.start(new Stage());
                MainWindow.hide();
            }
        });
    }

}