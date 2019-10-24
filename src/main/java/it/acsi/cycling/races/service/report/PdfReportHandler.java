package it.acsi.cycling.races.service.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import it.acsi.cycling.races.service.dto.RaceSubscriptionDTO;
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
public class PdfReportHandler {

    private final Logger log = LoggerFactory.getLogger(ExcelReportHandler.class);

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
        Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
        Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD);


    public byte[] buildRaceSubscriptionPdfReport(List<RaceSubscriptionDTO> list) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(5);
            table.setSpacingAfter(5);

            PdfPCell pdfCell = table.getDefaultCell();
            pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfCell.setPadding(3);

            int[] columnWidths = {55, 100, 100, 55, 100, 45};
            table.setWidths(columnWidths);
            // t.setBorderColor(BaseColor.GRAY);
            // t.setPadding(4);
            // t.setSpacing(4);
            // t.setBorderWidth(1);

            PdfPCell c1 = new PdfPCell(new Phrase("Data"));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Atleta"));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Contatti"));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Categoria"));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Società"));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Pagato"));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            table.setHeaderRows(1);

            list.stream().forEach(subscription -> {

                DateTimeFormatter formatter =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale( Locale.ITALY )
                        .withZone( ZoneId.systemDefault());

                table.addCell(newDefaultParagraph(
                    formatter.format(
                        subscription.getDate())));

                String athlete = new StringBuilder()
                    .append(subscription.getName())
                    .append(" ")
                    .append(subscription.getSurname())
                    .append("\n")
                    .append(subscription.getBirthDate())
                    .append(" ")
                    .append(subscription.getBirthPlace())
                    .toString();

                PdfPCell athleteCell = new PdfPCell();
                athleteCell.addElement(newDefaultParagraph(athlete));
                table.addCell(athleteCell);

                PdfPCell contactsCell = new PdfPCell();

                String contacts = new StringBuilder()
                    .append(subscription.getEmail())
                    .append("\n")
                    .append(subscription.getPhone())
                    .toString();

                contactsCell.addElement(newDefaultParagraph(contacts));
                table.addCell(contactsCell);

                table.addCell(newDefaultParagraph(subscription.getCategory()));
                table.addCell(newDefaultParagraph(subscription.getTeamName()));

                PdfPCell paidCell = new PdfPCell();
                paidCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                Paragraph paidParagraph = newDefaultParagraph(subscription.getPayed() ? "SI" : "NO");
                paidParagraph.setAlignment(Element.ALIGN_CENTER);
                paidCell.addElement(paidParagraph);
                table.addCell(paidCell);
            });

            document.add(table);
            document.close();

            return outputStream.toByteArray();

        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            throw new RuntimeException(exc);
        }

    }

    private Paragraph newDefaultParagraph(String text) {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(FontFactory.getFont(FontFactory.HELVETICA, 8));
        paragraph.setLeading(10);
        paragraph.setSpacingAfter(2);
        paragraph.add(text);
        return paragraph;
    }
}
