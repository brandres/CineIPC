/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cine;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import modelo.Proyeccion;
import modelo.Reserva;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class InterfazReservaController implements Initializable {

    @FXML
    private TextField fieldNombre;
    @FXML
    private TextField fieldTelefono;
    @FXML
    private TextField fieldNumeroEntradas;
    public Proyeccion proy;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void accionAceptar(ActionEvent event) {
        String nombre = fieldNombre.getText();
        String telefono = fieldTelefono.getText();
        if (!nombre.equalsIgnoreCase("") && !telefono.equalsIgnoreCase("")&& !fieldNumeroEntradas.getText().equalsIgnoreCase("")) {
            int entradas = Integer.parseInt(fieldNumeroEntradas.getText());
            Reserva r = new Reserva(nombre, telefono, entradas);
            proy.addReserva(r);
            InterfazPrincipalController.stage.close();
        } else {
            dialogoCamposVacios();
        }
    }

    @FXML
    private void accionCancelar(ActionEvent event) {
        InterfazPrincipalController.stage.close();
    }

    public void obtenerProyeccion(Proyeccion pr) {
        proy = pr;
    }

    public void dialogoCamposVacios() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Faltan campos por rellenar");
        alerta.setContentText("Por favor rellene todos los campos");
        alerta.initStyle(StageStyle.UTILITY);
        alerta.showAndWait();
    }
}
