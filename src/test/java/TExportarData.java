import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;


public class TExportarData {

  @Test
  public void testExportarAExcel() {
    List<String[]> datos = List.of(
      new String[] { "ID", "Nombre", "Correo" },
      new String[] { "1", "Juan", "juan@email.com" },
      new String[] { "2", "María", "maria@email.com" },
      new String[] { "3", "Pedro", "pedro@email.com" });
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Usuarios");

    for (int i = 0; i < datos.size(); i++) {
      Row fila = sheet.createRow(i);
      String[] filaDatos = datos.get(i);
      for (int j = 0; j < filaDatos.length; j++) {
        Cell celda = fila.createCell(j);
        celda.setCellValue(filaDatos[j]);
      }
    }

    AreaReference area = new AreaReference(
      new CellReference(0, 0),
      new CellReference(datos.size() - 1, datos.get(0).length - 1),
      SpreadsheetVersion.EXCEL2007);

    XSSFTable tabla = sheet.createTable(area);
    tabla.setName("UsuariosTabla");
    tabla.setDisplayName("TablaDeUsuarios");

    CTTable cttable = tabla.getCTTable();
    cttable.addNewTableStyleInfo().setName("TableStyleMedium2");
    cttable.setId(1);
    cttable.setRef(area.formatAsString());
    cttable.setDisplayName("TablaUsuarios");
    cttable.setName("TablaUsuarios");
    cttable.setTotalsRowShown(false);

    cttable.setTableColumns(cttable.addNewTableColumns());
    cttable.getTableColumns().setCount(datos.get(0).length);

    for (int i = 0; i < datos.get(0).length; i++) {
      CTTableColumn col = cttable.getTableColumns().addNewTableColumn();
      col.setId(i + 1);
      col.setName(datos.get(0)[i]);
    }

    tabla.setStyleName("TableStyleMedium2");

    for (int i = 0; i < datos.get(0).length; i++) {
      sheet.autoSizeColumn(i);
    }

    try (FileOutputStream out = new FileOutputStream("usuarios.xlsx")) {
      workbook.write(out);
      workbook.close();
    } catch(IOException e) {
      e.printStackTrace();
    }

    System.out.println("Archivo creado con tabla.");
  }
}
