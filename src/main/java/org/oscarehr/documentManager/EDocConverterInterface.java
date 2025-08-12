package org.oscarehr.documentManager;

import java.io.OutputStream;

public interface EDocConverterInterface {
  void convert(String html, OutputStream os) throws Exception;
}