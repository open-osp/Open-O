//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
 * <p>
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.managers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.Logger;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.OscarProperties;

/**
 * the NioFileManager handles all file input and output of all OscarDocument files
 * by providing several convenience utilities.
 * <p>
 * One goal is to eliminate the use of "OscarProperties.getInstance().getProperty("DOCUMENT_DIR")"
 * in every single page of OSCAR code.
 */
@Service
public class NioFileManagerImpl implements NioFileManager {

    @Autowired(required=false)
    private ServletContext context;

    @Autowired
    private SecurityInfoManager securityInfoManager;

    private static Logger log = MiscUtils.getLogger();
    private static final String DOCUMENT_CACHE_DIRECTORY = "document_cache";
    private static final String TEMP_PDF_DIRECTORY = "tempPDF";
    private static final String DEFAULT_FILE_SUFFIX = "pdf";
    private static final String DEFAULT_GENERIC_TEMP = "tempDirectory";
    private static final String BASE_DOCUMENT_DIR = oscar.OscarProperties.getInstance().getProperty("BASE_DOCUMENT_DIR");

    public Path hasCacheVersion2(LoggedInInfo loggedInInfo, String filename, Integer pageNum) {

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_edoc", SecurityInfoManager.READ, "")) {
            throw new RuntimeException("Read Access Denied _edoc for provider " + loggedInInfo.getLoggedInProviderNo());
        }

        if (filename.contains(File.separator)) {
            filename.replace(File.separator, "");
        }

        Path documentCacheDir = getDocumentCacheDirectory(loggedInInfo);
        Path outfile = Paths.get(documentCacheDir.toString(), filename + "_" + pageNum + ".png");

        if (!Files.exists(outfile)) {
            outfile = null;
        }
        return outfile;
    }

    public Path getDocumentCacheDirectory(LoggedInInfo loggedInInfo) {

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_edoc", SecurityInfoManager.READ, "")) {
            throw new RuntimeException("Read Access Denied _edoc for provider " + loggedInInfo.getLoggedInProviderNo());
        }

        Path cacheDir = Paths.get(BASE_DOCUMENT_DIR, context.getContextPath(), DOCUMENT_CACHE_DIRECTORY);

        if (!Files.exists(cacheDir)) {
            try {
                Files.createDirectory(cacheDir);
            } catch (IOException e) {
                log.error("Error creating DocumentCache directory", e);
            }
        }
        return cacheDir;
    }

    /**
     * First checks to see if a cache version is already available.  If one is not available then a
     * new cached version is created.
     * <p>
     * Returns a file path to the cached version of the given PDF
     */
    public Path createCacheVersion2(LoggedInInfo loggedInInfo, String sourceDirectory, String filename, Integer pageNum) {

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_edoc", SecurityInfoManager.WRITE, "")) {
            throw new RuntimeException("Read Access Denied _edoc for provider " + loggedInInfo.getLoggedInProviderNo());
        }

        if (filename.contains(File.separator)) {
            filename.replace(File.separator, "");
        }

        Path cacheFilePath = hasCacheVersion2(loggedInInfo, filename, pageNum);

        /*
         * create a new cache file if an existing cache file is not returned.
         */
        if (cacheFilePath == null) {
            Path sourceFile = Paths.get(sourceDirectory, filename);
            Path documentCacheDir = getDocumentCacheDirectory(loggedInInfo);
            cacheFilePath = Paths.get(documentCacheDir.toString(), filename + "_" + pageNum + ".png");

            PdfDecoder decode_pdf = new PdfDecoder(true);
            FontMappings.setFontReplacements();
            decode_pdf.useHiResScreenDisplay(true);
            decode_pdf.setExtractionMode(0, 96, 96 / 72f);

            try (InputStream inputStream = Files.newInputStream(sourceFile)) {
                decode_pdf.openPdfFileFromInputStream(inputStream, false);
                BufferedImage image_to_save = decode_pdf.getPageAsImage(pageNum);
                decode_pdf.getObjectStore().saveStoredImage(cacheFilePath.toString(), image_to_save, true, false, "png");
            } catch (IOException e) {
                log.error("Error", e);
            } catch (PdfException e) {
                log.error("Error", e);
            } finally {
                decode_pdf.flushObjectValues(true);
                decode_pdf.closePdfFile();
            }
        }

        return cacheFilePath;

    }

    /**
     * Remove the given file from the cache directory.
     * This is highly recommended function for temporary document preview images.
     *
     * @param loggedInInfo
     * @param fileName
     */
    public final boolean removeCacheVersion(LoggedInInfo loggedInInfo, final String fileName) {

        Path documentCacheDir = getDocumentCacheDirectory(loggedInInfo);
        Path cacheFilePath = Paths.get(fileName);
        cacheFilePath = Paths.get(documentCacheDir.toString(), cacheFilePath.getFileName().toString());

        try {
            return Files.deleteIfExists(cacheFilePath);
        } catch (IOException e) {
            log.error("Error while deleting temp cache image file " + fileName, e);
        }
        return false;
    }

    /**
     * Save a file to the temporary directory from ByteArrayOutputStream
     *
     * @throws IOException
     */
    public Path saveTempFile(final String fileName, ByteArrayOutputStream os, String fileType) throws IOException {
        Path directory = Files.createTempDirectory(TEMP_PDF_DIRECTORY + System.currentTimeMillis());
        if (fileType == null) {
            fileType = DEFAULT_FILE_SUFFIX;
        }
        Path file = Files.createFile(Paths.get(directory.toString(), String.format("%1$s.%2$s", fileName, fileType)));
        return Files.write(file, os.toByteArray());
    }

    public final Path saveTempFile(final String fileName, ByteArrayOutputStream os) throws IOException {
        return saveTempFile(fileName, os, null);
    }

    public Path createTempFile(final String fileName, ByteArrayOutputStream os) throws IOException {
        Path directory = Files.createTempDirectory(DEFAULT_GENERIC_TEMP + System.currentTimeMillis());
        Path file = Files.createFile(Paths.get(directory.toString(), fileName));
        Files.write(file, os.toByteArray());
        return directory;
    }

    /**
     * Delete a temp file. Do this often.
     *
     * @param fileName
     */
    public final boolean deleteTempFile(final String fileName) {
        Path tempfile = Paths.get(fileName);
        try {
            return Files.deleteIfExists(tempfile);
        } catch (IOException e) {
            log.error("Error while deleting temp cache image file " + fileName, e);
        }
        return false;
    }


    /**
     * retrieve given filename from Oscar's document directory path as defined in
     * Oscar properties.
     * Filename string in File out
     */
    public File getOscarDocument(String fileName) {
        Path oscarDocument = Paths.get(getDocumentDirectory(), fileName);
        return oscarDocument.toFile();
    }

    /**
     * Path NIO object in Path out.
     * The incoming path could have been derived from a temporary file.
     */
    public Path getOscarDocument(Path fileNamePath) {
        return getOscarDocument(fileNamePath.getFileName().toString()).toPath();
    }

    /**
     * Copy file from given file path into the default OscarDocuments directory.
     * This method deletes the temporary file after successful copy
     */
    public String copyFileToOscarDocuments(String tempFilePath) {
        String destinationDir = getDocumentDirectory();
        File tempFile = new File(tempFilePath);
        Path destinationFilePath = Paths.get(destinationDir, tempFile.getName());
        try {
            Files.copy(tempFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            if (Files.exists(destinationFilePath)) {
                deleteTempFile(tempFile.toPath().toString());
            }
        } catch (IOException e) {
            log.error("An error occurred while moving the PDF file", e);
        }
        return destinationFilePath.toString();
    }

    /**
     * Get the default OscarDocument directory.
     * Newer versions of OSCAR will only define the path for the BASE_DOCUMENT and
     * not for the full DOCUMENT_DIRECTORY path in Oscar.properties.
     * This method considers both locations.
     */
    private String getDocumentDirectory() {
        String document_dir = DOCUMENT_DIRECTORY;
        if (!Files.isDirectory(Paths.get(document_dir))) {
            document_dir = String.valueOf(Paths.get(BASE_DOCUMENT_DIR, "document"));
        }
        return document_dir;
    }

    /**
     * True if given filename exists in OscarDocument directory.
     * False if file not found.
     */
    public boolean isOscarDocument(String fileName) {
        File oscarDocument = getOscarDocument(fileName);
        return Files.exists(oscarDocument.toPath());
    }


}
