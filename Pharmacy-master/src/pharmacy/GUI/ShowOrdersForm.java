package pharmacy.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pharmacy.MainClasses.Order;
import pharmacy.MainClasses.PharmacyController;
import pharmacy.PersonClasses.Customer;

public class ShowOrdersForm {
    
    private final ObservableList <Order> CustomerOrders = FXCollections.observableArrayList();

    public void View(Customer C) {
        
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle(C.getFirstName() + "'s Orders");
        

        for (int i = 0; i < PharmacyController.OrderTable.getOrders_Number(); i++) {
            if (PharmacyController.OrderTable.getOrderOfIndex(i).getOrderCustomer().getID() == C.getID()) {
                CustomerOrders.add(PharmacyController.OrderTable.getOrderOfIndex(i));
            }
        }
        
        Image x = new Image (MsgBox.class.getResourceAsStream("icons/8.png"));
        ImageView Img = new ImageView(x);
        Img.setFitHeight(50);
        Img.setPreserveRatio(true);
        Img.setLayoutX(20);
        Img.setLayoutY(28);

        Label CustomerName = new Label("Customer #" + C.getFirstName());
        CustomerName.setLayoutX(90);
        CustomerName.setLayoutY(30);



        Label NumberOrders = new Label("Total Orders : " + CustomerOrders.size());
        NumberOrders.setLayoutX(90);
        NumberOrders.setLayoutY(55);


        TableView tableShowOrders = new TableView();
        tableShowOrders.setPlaceholder(new Label("This customer didn't made any orders !"));
        tableShowOrders.setLayoutX(20);
        tableShowOrders.setLayoutY(110);
        tableShowOrders.setMinWidth(624); 
        tableShowOrders.setMaxWidth(624); 
        tableShowOrders.setMinHeight(360); 
        tableShowOrders.setMaxHeight(360); 
        tableShowOrders.setEditable(true);

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
        
        tableShowOrders.getColumns().setAll(TC_OrderId , TC_OrderDate , TC_OrderTime , TC_OrderPrice , TC_OrderStatus);
        tableShowOrders.setItems(CustomerOrders);

        
        
        Button btnShowOrderDetails = new Button("Show Order Details");
        btnShowOrderDetails.setId("submit");
        btnShowOrderDetails.setMinHeight(40);
        btnShowOrderDetails.setMinWidth(210);
        btnShowOrderDetails.setLayoutX(20);
        btnShowOrderDetails.setLayoutY(490);
        btnShowOrderDetails.setDisable(true);
        
        Pane Pane_ShowOrders_Body = new Pane();
        Pane_ShowOrders_Body.setStyle("-fx-background : #fff;");
        Pane_ShowOrders_Body.getChildren().setAll(Img , CustomerName , NumberOrders , tableShowOrders , btnShowOrderDetails);
        
               
        tableShowOrders.setOnMouseClicked(event -> {
            if ((Order) tableShowOrders.getSelectionModel().getSelectedItem() != null) {
                btnShowOrderDetails.setDisable(false);
            }
        });
        
        
        btnShowOrderDetails.setOnMouseClicked(event -> {
            if ((Order) tableShowOrders.getSelectionModel().getSelectedItem() != null) {
                OrderDetailsForm.View((Order) tableShowOrders.getSelectionModel().getSelectedItem());
            }
            btnShowOrderDetails.setDisable(true);
        });
        
        Scene scene = new Scene(Pane_ShowOrders_Body , 664 , 580);
        scene.getStylesheets().add("pharmacy/GUI/style.css");
        window.setScene(scene);
        window.showAndWait();

 
        
    }
    
}