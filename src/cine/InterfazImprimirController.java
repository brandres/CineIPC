/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cine;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class InterfazImprimirController implements Initializable {

    @FXML
    private AnchorPane anchorImprimir;
    @FXML
    private Label labelTitulo;
    @FXML
    private Label labelFecha;
    @FXML
    private Label labelHora;
    @FXML
    private Label labelFila;
    @FXML
    private Label labelAsiento;
    @FXML
    private Label labelSala;
    private Printer printer = Printer.getDefaultPrinter();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void accionBotonSeleccionarImpresora(ActionEvent event) { 
        ChoiceDialog dialog = new ChoiceDialog(Printer.getDefaultPrinter(),
        Printer.getAllPrinters());
        dialog.setHeaderText("Seleccionar la impresora!");
        dialog.setContentText("Seleccionar una impresora de las disponibles");
        dialog.setTitle("Selección Impresora");
        Optional<Printer> opt = dialog.showAndWait();
        if (opt.isPresent()) {
            printer = opt.get();
        }
    }

    @FXML
    private void accionImprimir(ActionEvent event) {
        Stage stage = new Stage(StageStyle.UTILITY);
        hacerPaginaImpresion(anchorImprimir,stage);
        InterfazCineController.stage.close();
    }

    public void escribirEntrada(String titulo, LocalDate fecha, String hora, String sala, int fila, int asiento) {
        labelTitulo.setText(titulo);
        labelFecha.setText(fecha.toString());
        labelHora.setText(hora);
        labelSala.setText(sala);
        labelFila.setText(String.valueOf(fila));
        labelAsiento.setText(String.valueOf(asiento));
    }
    private void hacerPaginaImpresion(Node node, Stage owner){
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null)
        {
            return;
        }
        boolean proceed = job.showPrintDialog(owner);

        if (proceed)

        {

            imprimir(job, node);

        }

    }


    private void imprimir(PrinterJob job,Node node) {
        if (job != null) {
            boolean printed = job.printPage(node);
            if (printed) {
                job.endJob();
            } else {
                System.out.println("Fallo al imprimir");
            }
        } else {
            System.out.println("No puede crearse el job de impresión.");
        }
    }
}
