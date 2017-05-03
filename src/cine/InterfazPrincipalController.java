/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cine;

import accesoaBD.AccesoaBD;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.*;
import modelo.Sala.localidad;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class InterfazPrincipalController implements Initializable {

    @FXML
    private ChoiceBox<String> choicePelicula;
    @FXML
    private ChoiceBox<String> choiceHora;
    @FXML
    private ChoiceBox<String> choiceSala;
    @FXML
    private ChoiceBox<LocalDate> choiceDia;
    ObservableList<String> peliList = FXCollections.observableArrayList();
    ObservableList<String> horaList = FXCollections.observableArrayList();
    ObservableList<String> salaList = FXCollections.observableArrayList();
    ObservableList<LocalDate> dateList = FXCollections.observableArrayList();
    InterfazCineController controllerCine;
    InterfazReservaController controllerReserva;
    public static Stage stage = new Stage(StageStyle.DECORATED);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 1; i <= 9; i++) {
            dateList.add(LocalDate.of(2017, Month.APRIL, i));
        }
        choiceDia.setItems(dateList);
        for (LocalDate d : dateList) {
            for (Pelicula p : Cine.acceso.getPeliculas(d)) {
                if (!peliList.contains(p.getTitulo())) {
                    peliList.add(p.getTitulo());
                }
            }
            for (Proyeccion pr : Cine.acceso.getProyeccionesDia(d)) {
                if (!horaList.contains(pr.getHoraInicio())) {
                    horaList.add(pr.getHoraInicio());
                }
                if (!salaList.contains(pr.getSala().getNombresala())) {
                    salaList.add(pr.getSala().getNombresala());
                }
            }

        }
        choiceHora.setItems(horaList);
        choiceHora.setOnAction(e -> {
            clickChoiceHora();
        });
        choiceDia.setOnAction(e -> {
            clickChoiceDia();
        });
        choicePelicula.setItems(peliList);
        choicePelicula.setOnAction(e -> {
            clickChoicePeli();
        });
        choiceSala.setItems(salaList);
    }

    public void clickChoiceHora() {
        LocalDate dia = choiceDia.getValue();
        String peli = choicePelicula.getValue();
        String sala = choiceSala.getValue();
        if (dia != null && peli != null) {
            actualizarPorDiaPeliculaYHora(choiceHora.getValue(), peli);
        }
        comprobarValorChoice(choiceHora.getValue(), peli, sala);
    }

    public void clickChoicePeli() {
        String hora = choiceHora.getValue();
        LocalDate dia = choiceDia.getValue();
        String sala = choiceSala.getValue();
        if (hora != null && dia != null) {
            actualizarPorDiaPeliculaYHora(hora, choicePelicula.getValue());
        } else {
            if (dia != null) {
                actualizarListaPorDiaYPelicula(choicePelicula.getValue());
            }
        }
        comprobarValorChoice(hora, choicePelicula.getValue(), sala);

    }

    public void clickChoiceDia() {
        String hora = choiceHora.getValue();
        String peli = choicePelicula.getValue();
        String sala = choiceSala.getValue();
        if (hora == null && peli == null) {
            actualizarListaPorDia();
        } else {
            if (hora == null && peli != null) {
                actualizarListaPorDiaYPelicula(peli);
            } else {
                if (hora != null && peli != null) {
                    actualizarPorDiaPeliculaYHora(hora, peli);
                }
            }
        }
        comprobarValorChoice(hora, peli, sala);
    }

    public void actualizarListaPorDia() {
        peliList.clear();
        horaList.clear();
        for (Pelicula p : Cine.acceso.getPeliculas(choiceDia.getValue())) {
            if (!peliList.contains(p.getTitulo())) {
                peliList.add(p.getTitulo());
            }

        }
        for (Proyeccion pr : Cine.acceso.getProyeccionesDia(choiceDia.getValue())) {
            if (!horaList.contains(pr.getHoraInicio())) {
                horaList.add(pr.getHoraInicio());
            }
            if (!salaList.contains(pr.getSala().getNombresala())) {
                salaList.add(pr.getSala().getNombresala());
            }
        }
    }

    public void actualizarListaPorDiaYPelicula(String titulo) {
        horaList.clear();
        salaList.clear();
        for (Proyeccion pr : Cine.acceso.getProyeccion(titulo, choiceDia.getValue())) {
            if (!horaList.contains(pr.getHoraInicio())) {
                horaList.add(pr.getHoraInicio());
            }
            if (!salaList.contains(pr.getSala().getNombresala())) {
                salaList.add(pr.getSala().getNombresala());
            }
        }

    }

    public void actualizarPorDiaPeliculaYHora(String hora, String titulo) {
        Proyeccion pr = Cine.acceso.getProyeccion(titulo, choiceDia.getValue(), hora);
        salaList.clear();
        salaList.add(pr.getSala().getNombresala());

    }

    public void comprobarValorChoice(String hora, String peli, String sala) {
        if (horaList.contains(hora)) {
            choiceHora.setItems(horaList);
            choiceHora.setValue(hora);
        } else {
            choiceHora.setItems(horaList);
        }
        if (peliList.contains(choicePelicula.getValue())) {
            choicePelicula.setItems(peliList);
            choicePelicula.setValue(choicePelicula.getValue());
        } else {
            choicePelicula.setItems(peliList);
        }
        if (salaList.contains(sala)) {
            choiceSala.setItems(salaList);
            choiceSala.setValue(sala);
        } else {
            choiceSala.setItems(salaList);
        }
    }

    @FXML
    private void asignarBoton(ActionEvent event) throws IOException {
        String resp = dialogoBuscarReserva();
        if (resp != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfazCine.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            controllerCine = loader.getController();
            stage.setScene(scene);
            Reserva res = null;
            Proyeccion pr = null;
            for (int i = 1; i <= 9; i++) {
                for (Proyeccion p : Cine.acceso.getProyeccionesDia(LocalDate.of(2017, Month.APRIL, i))) {
                    for (Reserva r : p.getReservas()) {
                        if (r.getTelefono().equalsIgnoreCase(resp)) {
                            res = r;
                            pr = p;
                        }
                    }

                }
            }
            if (res != null && pr != null) {
                String peli = pr.getPelicula().getTitulo();
                LocalDate dia = pr.getDia();
                String hora = pr.getHoraInicio();
                controllerCine.setSalaYProyeccion(pr);
                for (int i = 0; i < 12; i++) {
                    for (int j = 0; j < 18; j++) {
                        controllerCine.añadirAsientos(i, j, Cine.acceso.getProyeccion(peli, dia, hora).getSala().getLocalidades()[j][i], peli, hora, dia, res);
                    }
                }
                stage.showAndWait();
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("No se encuentra la reserva");
                alerta.setContentText("Por favor verifique que el numero de telefono es correcto");
                alerta.initStyle(StageStyle.UTILITY);
                alerta.showAndWait();
            }
        }
    }

    @FXML
    private void reservarBoton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfazReserva.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String hora = choiceHora.getValue();
        String peli = choicePelicula.getValue();
        LocalDate dia = choiceDia.getValue();
        if (hora != null && peli != null && dia != null) {
            Proyeccion pr = Cine.acceso.getProyeccion(peli, dia, hora);
            controllerReserva = loader.getController();
            controllerReserva.obtenerProyeccion(pr);
            stage.setScene(scene);
            stage.showAndWait();
        } else {
            dialogoCamposVacios();
        }
    }

    @FXML
    private void ventaBoton(ActionEvent event) throws IOException, CloneNotSupportedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfazCine.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controllerCine = loader.getController();
        stage.setScene(scene);
        String hora = choiceHora.getValue();
        String peli = choicePelicula.getValue();
        LocalDate dia = choiceDia.getValue();
        if (hora != null && peli != null && dia != null) {
            Proyeccion pr = Cine.acceso.getProyeccion(peli, dia, hora);
            controllerCine.setSalaYProyeccion(pr);
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 18; j++) {
                    controllerCine.añadirAsientos(i, j, Cine.acceso.getProyeccion(peli, dia, hora).getSala().getLocalidades()[j][i], peli, hora, dia, null);
                }
            }
            stage.showAndWait();
        } else {
            dialogoCamposVacios();
        }
    }

    public void dialogoCamposVacios() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Faltan campos por rellenar");
        alerta.setContentText("Por favor rellene todos los campos");
        alerta.initStyle(StageStyle.UTILITY);
        alerta.showAndWait();
    }

    public String dialogoBuscarReserva() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Asignar asientos de la reserva");
        dialogo.setHeaderText("Ingresa numero de telefono de la persona que ha hecho la reserva");
        dialogo.setContentText("Numero de telefono");
        dialogo.initStyle(StageStyle.UTILITY);
        Optional<String> resp = dialogo.showAndWait();
        if (resp.isPresent()) {
            return resp.get();
        } else {
            return null;
        }
    }

}
