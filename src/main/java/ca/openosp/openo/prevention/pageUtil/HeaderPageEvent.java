package ca.openosp.openo.prevention.pageUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
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
    private boolean hasBorder;
    private Rectangle headerSize;
    private Logger logger = MiscUtils.getLogger();
    
    public HeaderPageEvent(Phrase headerPhrase, boolean hasBorder, Rectangle headerSize) {
        this.headerPhrase = headerPhrase;
        this.hasBorder = hasBorder;
        this.headerSize = headerSize;
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (headerPhrase != null || headerSize != null) {
            PdfContentByte contentByte = writer.getDirectContent();
            ColumnText columnText = new ColumnText(contentByte);

            columnText.setSimpleColumn(headerSize);
            columnText.setAlignment(Element.ALIGN_RIGHT);
            columnText.addText(headerPhrase);

            try {
                columnText.go();
            } catch (DocumentException e) {
                logger.error("Document exception error: " + e);
            }
            
            if (hasBorder) {
                // Draw border line at the bottom of the header
                contentByte.saveState();
                contentByte.setLineWidth(0.90f);
                contentByte.moveTo(document.left(), document.top() + 6);
                contentByte.lineTo(document.right(), document.top() + 6);
                contentByte.stroke();
                contentByte.restoreState();
            }
        } else {
            logger.error("Header phrase or header size is null");
        }
    }
}
