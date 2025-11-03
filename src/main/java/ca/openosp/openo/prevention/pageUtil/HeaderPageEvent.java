package ca.openosp.openo.prevention.pageUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.log4j.Logger;

import ca.openosp.openo.caisi_integrator.util.MiscUtils;

/**
 * Reusable page event handler for rendering headers on PDFs.
 * This replaces the deprecated HeaderFooter class from iText 2.x.
 */
public class HeaderPageEvent extends PdfPageEventHelper {
    private Phrase headerPhrase;
    private Logger logger = MiscUtils.getLogger();
    
    public HeaderPageEvent(Phrase headerPhrase) {
        this.headerPhrase = headerPhrase;
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (headerPhrase != null) {
            PdfContentByte contentByte = writer.getDirectContent();
            ColumnText columnText = new ColumnText(contentByte);

            float x1 = document.left();
            float x2 = document.right();
            float y1 = document.top() + 5;   // Bottom of header (just above content)
            float y2 = document.top() + 70;  // Top of header (use most of the 80pt margin)

            columnText.setSimpleColumn(x1, y1, x2, y2);
            columnText.setAlignment(Element.ALIGN_RIGHT);
            columnText.addText(headerPhrase);

            try {
                columnText.go();
            } catch (DocumentException e) {
                logger.error("Document exception error: " + e);
            }
            
            // Draw border line at the bottom of the header
            contentByte.saveState();
            contentByte.setLineWidth(0.5f);
            contentByte.moveTo(document.left(), document.top());
            contentByte.lineTo(document.right(), document.top());
            contentByte.stroke();
            contentByte.restoreState();
        } else {
            logger.error("Header phrase is null!");
        }
    }
}
