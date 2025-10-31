package ca.openosp.openo.prevention.pageUtil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Reusable page event handler for rendering headers on PDFs.
 * This replaces the deprecated HeaderFooter class from iText 2.x.
 */
public class HeaderPageEvent extends PdfPageEventHelper {
    private Phrase headerPhrase;
    
    public HeaderPageEvent(Phrase headerPhrase) {
        this.headerPhrase = headerPhrase;
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (headerPhrase != null) {
            headerPhrase.add(Chunk.NEWLINE);
        } else {
            System.out.println("HeaderPhrase is null");
        }
    }
}
