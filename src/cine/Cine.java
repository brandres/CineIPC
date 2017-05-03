/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cine;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import accesoaBD.*;

public class Cine extends Application {
    InterfazPrincipalController controllerPrincipal;
    public static AccesoaBD acceso = new AccesoaBD();
    @Override
    public void start(Stage stage) throws IOException {
        //Holla
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfazPrincipal.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controllerPrincipal = loader.getController();
        scene.getStylesheets().add("cine/interfazcine.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
