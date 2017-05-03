/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cine;

import static cine.InterfazPrincipalController.stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.Proyeccion;
import modelo.Reserva;
import modelo.Sala;
import modelo.Sala.localidad;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class InterfazCineController implements Initializable {

    @FXML
    private GridPane gridAsientos;
    @FXML
    private Label labelFila;
    @FXML
    private Label labelColumna;
    localidad localidadAntes[][];
    localidad localidadDespues[][];
    Proyeccion proy;
    int contador = 0;
    int restantes = 0;
    Reserva reserva;
    InterfazImprimirController controllerImprimir;
    public static Stage stage = new Stage(StageStyle.DECORATED);
    @FXML
    private Label labelAsientosSeleccionados;
    @FXML
    private Label labelEntrada;
    @FXML
    private Label labelEntradaNumero;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void añadirAsientos(int columna, int fila, localidad l, String titulo, String hora, LocalDate dia, Reserva res) {
        Pane a = new Pane();
        if (res == null) {
            reserva = null;
            restantes = Integer.MAX_VALUE;
            labelEntrada.setVisible(false);
            labelEntradaNumero.setVisible(false);
        } else {
            restantes = res.getNumLocalidades();
            reserva = res;
        }
        labelEntradaNumero.setText(String.valueOf(restantes));
        if (l == localidad.libre) {
            a.setStyle("-fx-background-color: #00e600");
        } else {
            a.setStyle("-fx-background-color: #ff1a1a");
        }
        a.setOnMouseEntered(e -> {
            labelFila.setText(String.valueOf(fila));
            labelColumna.setText(String.valueOf(columna));
        });
        a.setOnMouseClicked(e -> {
            if (a.getStyle().equalsIgnoreCase("-fx-background-color: #00e600")) {
                if (restantes > 0) {
                    a.setStyle("-fx-background-color: #ff1a1a");
                    localidadDespues[fila][columna] = localidad.vendida;
                    contador++;
                    restantes--;
                    labelAsientosSeleccionados.setText(Integer.toString(contador));
                    labelEntradaNumero.setText(Integer.toString(restantes));
                }
            } else {
                if (localidadAntes[fila][columna] != localidad.vendida) {
                    a.setStyle("-fx-background-color: #00e600");
                    localidadDespues[fila][columna] = localidad.libre;
                    contador--;
                    restantes++;
                    labelAsientosSeleccionados.setText(Integer.toString(contador));
                    labelEntradaNumero.setText(Integer.toString(restantes));
                }
            }
        });
        gridAsientos.add(a, columna, fila);

    }

    public void setSalaYProyeccion(Proyeccion proyeccion) {
        localidadDespues = copiarLocalidad(proyeccion.getSala().getLocalidades());
        localidadAntes = copiarLocalidad(proyeccion.getSala().getLocalidades());
        this.proy = proyeccion;
    }

    public void resetContador() {
        contador = 0;
    }

    @FXML
    private void accionAceptarBoton(ActionEvent event) throws IOException {
       if(contador != 0){ 
        if (restantes == 0) {
            mostrarImprimir();
            Cine.acceso.getProyeccion(proy.getPelicula().getTitulo(), proy.getDia(), proy.getHoraInicio()).getSala().setLocalidades(localidadDespues);
            borrarReserva();
            InterfazPrincipalController.stage.close();

        } else {
            if (reserva == null) {
                mostrarImprimir();
                Cine.acceso.getProyeccion(proy.getPelicula().getTitulo(), proy.getDia(), proy.getHoraInicio()).getSala().setLocalidades(localidadDespues);
                InterfazPrincipalController.stage.close();
            } else {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("No se han asignado todos los asientos reservados");
                alerta.setContentText("Por favor asigne los asientos restantes");
                alerta.initStyle(StageStyle.UTILITY);
                alerta.showAndWait();
            }
        }
       }else{
           Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("No se ha seleccionado ningun asiento");
                alerta.setContentText("Por favor seleccione al menos un asiento o todos los asientos reservados");
                alerta.initStyle(StageStyle.UTILITY);
                alerta.showAndWait();
       }
    }

    @FXML
    private void accionCancelarBoton(ActionEvent event) {
        Cine.acceso.getProyeccion(proy.getPelicula().getTitulo(), proy.getDia(), proy.getHoraInicio()).getSala().setLocalidades(localidadAntes);
        InterfazPrincipalController.stage.close();
    }

    public localidad[][] copiarLocalidad(localidad[][] array) {
        localidad aux[][] = new localidad[18][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 18; j++) {
                aux[j][i] = array[j][i];
            }
        }
        return aux;
    }

    public void mostrarImprimir() throws IOException {
        String titulo = proy.getPelicula().getTitulo();
        LocalDate fecha = proy.getDia();
        String hora = proy.getHoraInicio();
        String sala = proy.getSala().getNombresala();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfazImprimir.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controllerImprimir = loader.getController();
        stage.setScene(scene);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 18; j++) {
                if (localidadAntes[j][i] != localidadDespues[j][i]) {
                    controllerImprimir.escribirEntrada(titulo, fecha, hora, sala, j, i);
                    stage.showAndWait();
                }
            }
        }
    }
    public void borrarReserva(){
        proy.getReservas().remove(reserva);
    }
}
