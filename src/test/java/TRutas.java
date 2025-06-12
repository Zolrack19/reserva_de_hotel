import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.example.hotel.util.Rutas;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

public class TRutas {
  
  @Test
  public void testCargarFxmlConRutas() throws IOException {
    Platform.startup(() -> {});
    FXMLLoader.load(Rutas.INICIO.getUrlVista());
  }

  @Test
  public void testUsarTtl() throws IOException {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> ttlTask = Rutas.INICIO.getTtlTask();
    if (ttlTask != null && !ttlTask.isDone()) {
      ttlTask.cancel(false);
    }
    ttlTask = scheduler.schedule(() -> {
      System.out.println("hola mundo");
    }, 5, TimeUnit.SECONDS);
    Rutas.INICIO.setTtlTask(ttlTask);
  }
}