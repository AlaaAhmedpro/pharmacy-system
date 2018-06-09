package pharmacy.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MsgBox {

    public enum type { error , success , warning }

    static boolean answer;

    public static boolean ShowMsg(String title , String Text , type T) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle(title);


        Label msg = new Label(Text);
        msg.setLayoutY(35);
        msg.setId("error");
        msg.layoutXProperty().bind((window.widthProperty().subtract(msg.widthProperty()).subtract(40)));

        Button closeBtn = new Button("Close");
        closeBtn.setLayoutX(320);
        closeBtn.setId("closeBtn");
        closeBtn.setLayoutY(80);
        closeBtn.setOnMouseClicked(event -> {
            window.close();
        });

        Button YES = new Button("YES");
        YES.setLayoutX(240);
        YES.setId("closeBtn");
        YES.setLayoutY(80);
        YES.setOnMouseClicked(event -> {
            window.close();
        });

        YES.setOnAction(e -> {
            answer = true;
            window.close();
        });

        closeBtn.setOnAction(e -> {
            answer = false;
            window.close();
        });

        String path = "";

        if (T == type.error) {
            path = "icons/error.png";
        } else if (T == type.success) {
            path = "icons/success.png";
        } else if (T == type.warning) {
            path = "icons/warning.png";
        }

        Image x = new Image (MsgBox.class.getResourceAsStream(path));
        ImageView Img = new ImageView(x);
        Img.setFitHeight(85);
        Img.setPreserveRatio(true);
        Img.setLayoutX(40);
        Img.setLayoutY(30);

        Pane p = new Pane();
        p.getChildren().addAll(Img, msg , closeBtn);
        if (T == type.warning) {
            p.getChildren().add(YES);
        }

        Scene scene = new Scene(p , 440 , 150);
        scene.getStylesheets().add("pharmacy/GUI/style.css");
        window.setScene(scene);
        window.showAndWait();
        return answer;

    }
}