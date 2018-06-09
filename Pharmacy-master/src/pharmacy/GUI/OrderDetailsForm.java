package pharmacy.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.MainClasses.Order;
import pharmacy.ProductClasses.Product;

public class OrderDetailsForm {

    public static void View(Order thisOrder) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle("Order #" + thisOrder.getOrderId());

        Image x = new Image (MsgBox.class.getResourceAsStream("icons/report.png"));
        ImageView Img = new ImageView(x);
        Img.setFitHeight(50);
        Img.setPreserveRatio(true);
        Img.setLayoutX(15);
        Img.setLayoutY(28);

        Label OrderId = new Label("Order ID #" + thisOrder.getOrderId());
        OrderId.setLayoutX(80);
        OrderId.setLayoutY(30);

        Label OrderStatusTxt = new Label("Status : ");
        OrderStatusTxt.setLayoutX(80);
        OrderStatusTxt.setLayoutY(55);


        Label OrderStatus = new Label(thisOrder.getOrderStatus());
        OrderStatus.setLayoutX(126);
        OrderStatus.setLayoutY(55);
        if (thisOrder.getOrderStatus().equals("Pending")) {
            OrderStatus.setStyle("-fx-text-fill: #cc0000;");
        } else if (thisOrder.getOrderStatus().equals("Is delivering ..")) {
            OrderStatus.setStyle("-fx-text-fill: #65879e;");
        } else if (thisOrder.getOrderStatus().equals("Completed")) {
            OrderStatus.setStyle("-fx-text-fill: #00a800;");
        }


        Label OrderDate = new Label("Date : " + thisOrder.getOrderDate());
        OrderDate.setLayoutX(375);
        OrderDate.setLayoutY(30);


        Label OrderTime = new Label("     Time : " + thisOrder.getOrderTime());
        OrderTime.setLayoutX(378);
        OrderTime.setLayoutY(55);


        Label TotalDue = new Label("Total Due : " + thisOrder.getOrderPrice()+ " LE");
        TotalDue.setLayoutX(374);
        TotalDue.setLayoutY(450);


        TableView ProductsList = new TableView();
        ProductsList.setLayoutX(20);
        ProductsList.setLayoutY(110);
        ProductsList.setMinWidth(460); //Edited
        ProductsList.setMaxWidth(460); //Edited
        ProductsList.setMinHeight(320); //Edited
        ProductsList.setMaxHeight(320); //Edited
        ProductsList.setEditable(true);


        ObservableList <Product> ProductsInOrder = FXCollections.observableArrayList();
        ProductsInOrder.addAll(thisOrder.getProductList());

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


        ProductsList.getColumns().setAll(ProductCode, ProductName , ProductPrice , ProductQuantity);
        ProductsList.setItems(ProductsInOrder);


        Label ShippingAddress = new Label("Shipping Address : " + thisOrder.getOrderCustomer().getShippingAddress());
        ShippingAddress.setLayoutX(20);
        ShippingAddress.setLayoutY(450);


        Pane p = new Pane();
        p.setStyle("-fx-background-color: #fff;");
        p.getChildren().addAll(Img, OrderId , OrderStatusTxt , OrderStatus , OrderDate , OrderTime , ProductsList, ShippingAddress, TotalDue);



        Scene scene = new Scene(p , 500 , 500);
        scene.getStylesheets().add("pharmacy/GUI/style.css");
        window.setScene(scene);
        window.showAndWait();

    }
}


