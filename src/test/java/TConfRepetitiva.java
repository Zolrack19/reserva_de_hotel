import org.junit.jupiter.api.Test;

import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Hotel;

import javafx.application.Platform;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TConfRepetitiva {

  @Test
  public void testConfiguracionCalendarios() {
    Platform.startup(() -> {});
    DatePicker fechaEntrada = new DatePicker();
    DatePicker fechaSalida = new DatePicker();
    ConfRepetitiva.confCalendarios(fechaEntrada, fechaSalida);
  }
  
  @Test
  public void testEstiloCalendario() {
    Platform.startup(() -> {});
    DatePicker fechaEntrada = new DatePicker();
    ConfRepetitiva.confEstiloCalendario(fechaEntrada);
  }

  @Test
  public void testConfListaSugerencia() {
    Platform.startup(() -> {});
    ListView<Hotel> sugerencias = new ListView<>();
    TextField txtfield = new TextField();
    ConfRepetitiva.confListaSugerencia(sugerencias, txtfield);
  }

  @Test
  public void testSetFondoContenedor() {
    Platform.startup(() -> {});
    VBox contenedor = new VBox();
    ConfRepetitiva.setBackground(contenedor, "una url");
  }

}