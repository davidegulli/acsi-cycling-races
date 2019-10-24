package it.acsi.cycling.races.service.report;

import it.acsi.cycling.races.service.dto.RaceSubscriptionDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@Component
public class ExcelReportHandler {

    private final Logger log = LoggerFactory.getLogger(ExcelReportHandler.class);

    public byte[] buildRaceSubscriptionExcelReport(List<RaceSubscriptionDTO> list) {

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Iscrizioni");
            sheet.setColumnWidth(0, 4000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 5500);
            sheet.setColumnWidth(4, 9000);
            sheet.setColumnWidth(5, 3000);
            sheet.setColumnWidth(6, 6000);
            sheet.setColumnWidth(7, 6000);
            sheet.setColumnWidth(8, 2000);

            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Data Iscrizione");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Nome");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Data di Nascita");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(3);
            headerCell.setCellValue("Luogo di Nascita");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(4);
            headerCell.setCellValue("Email");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(5);
            headerCell.setCellValue("Telefono");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(6);
            headerCell.setCellValue("Categoria");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(7);
            headerCell.setCellValue("Società");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(8);
            headerCell.setCellValue("Pagamento");
            headerCell.setCellStyle(headerStyle);

            for(int i = 0, x = list.size(); i < x; i++){

                RaceSubscriptionDTO subscription = list.get(i);

                if(subscription != null) {

                    Row dataRow = sheet.createRow(i+1);

                    Cell dataCell = dataRow.createCell(0);

                    DateTimeFormatter formatter =
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(Locale.ITALY)
                            .withZone(ZoneId.systemDefault());

                    dataCell.setCellValue(formatter.format(subscription.getDate()));

                    dataCell = dataRow.createCell(1);

                    String athleteName = new StringBuilder()
                        .append(subscription.getName())
                        .append(" ")
                        .append(subscription.getSurname())
                        .toString();

                    dataCell.setCellValue(athleteName);

                    dataCell = dataRow.createCell(2);
                    dataCell.setCellValue(subscription.getBirthDate());

                    dataCell = dataRow.createCell(3);
                    dataCell.setCellValue(subscription.getBirthPlace());

                    dataCell = dataRow.createCell(4);
                    dataCell.setCellValue(subscription.getEmail());

                    dataCell = dataRow.createCell(5);
                    dataCell.setCellValue(subscription.getPhone());

                    dataCell = dataRow.createCell(6);
                    dataCell.setCellValue(subscription.getCategory());

                    dataCell = dataRow.createCell(7);
                    dataCell.setCellValue(subscription.getTeamName());

                    dataCell = dataRow.createCell(8);
                    dataCell.setCellValue(subscription.getPayed() ? "SI" : "NO");
                }

            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            throw new RuntimeException(exc.getMessage());
        }

    }
}
