package ca.openosp.openo.documentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import ca.openosp.openo.utility.MiscUtils;

import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import io.woo.htmltopdf.PdfPageSize;

public class InternalEDocConverter implements EDocConverterInterface {

    private static final Logger logger = MiscUtils.getLogger();

    /** The renderer's own resolution: one device pixel per CSS pixel. */
    public static final int DEFAULT_LAYOUT_DPI = 96;

    /**
     * Resolution eForms are laid out at. The renderer rounds font and line sizes to whole
     * device pixels; at ten device pixels per CSS pixel nothing the forms use is rounded, so
     * text wraps where the browser wraps it.
     */
    public static final int EFORM_LAYOUT_DPI = 960;

    private final int dpi;

    /**
     * A converter laying documents out at {@link #DEFAULT_LAYOUT_DPI}.
     */
    public InternalEDocConverter() {
        this(DEFAULT_LAYOUT_DPI);
    }

    /**
     * A converter laying documents out at the given resolution.
     *
     * @param dpi int device pixels per inch the page is laid out at
     */
    public InternalEDocConverter(int dpi) {
        this.dpi = dpi;
    }

    /**
     * Converts HTML to PDF using the internal io.woo.htmltopdf library.
     * Requires the required native .so file to be bundled (e.g.,
     * libwkhtmltox.ubuntu.noble.amd64.so).
     * This is now the only converter used; no configuration property is required.
     * 
     * @param document the complete HTML string to convert to PDF
     * @param os       the {@link ByteArrayOutputStream} where the generated PDF
     *                 content will be written
     * @throws Exception if the external process fails or PDF conversion is
     *                   unsuccessful
     */
    @Override
    public void convert(String document, OutputStream os) throws IOException {
        HashMap<String, String> htmlToPdfSettings = new HashMap<String, String>() {{
            put("load.blockLocalFileAccess", "false");
            put("web.enableIntelligentShrinking", "true");
            put("web.minimumFontSize", "10");
            // put("load.zoomFactor", "0.92");
            put("web.printMediaType", "true");
            put("web.defaultEncoding", "utf-8");
            put("T", "10mm");
            put("L", "8mm");
            put("R", "8mm");
            put("web.enableJavascript", "false");
        }};
        try (InputStream in = HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml(document, htmlToPdfSettings))
                // log fonts and images the renderer could not load; the conversion finishes anyway
                .warning(message -> logger.warn("PDF conversion: " + message))
                .error(message -> logger.error("PDF conversion: " + message))
                .pageSize(PdfPageSize.Letter)
                .dpi(dpi)
                .convert()) {
            IOUtils.copy(in, os);
        }
    }
}