package org.oscarehr.documentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import io.woo.htmltopdf.PdfPageSize;

public class InternalEDocConverter implements EDocConverterInterface {
  /**
   * Converts HTML to PDF using the internal io.woo.htmltopdf library.
   * Only use this if you've bundled the required native .so file (e.g., libwkhtmltox.ubuntu.noble.amd64.so)
   * and WKHTMLTOPDF_COMMAND=internal is set.
   * @param document the complete HTML string to convert to PDF
   * @param os the {@link ByteArrayOutputStream} where the generated PDF content will be written
   * @throws Exception if the external process fails or PDF conversion is unsuccessful
  */
  @Override
  public void convert(String document, OutputStream os) throws IOException {
    Map<String,String> settings = Map.of(
      "load.blockLocalFileAccess","false",
      "web.enableIntelligentShrinking","true",
      "web.minimumFontSize", "10",
      "web.printMediaType", "true",
      "web.defaultEncoding", "utf-8",
      "T", "10mm",
      "L", "8mm",
      "R", "8mm",
      "web.enableJavascript","false"
    );
    try(InputStream in = HtmlToPdf.create()
        .object(HtmlToPdfObject.forHtml(document, settings))
        .pageSize(PdfPageSize.Letter)
        .convert())
    {
      IOUtils.copy(in, os);
    }
  }
}