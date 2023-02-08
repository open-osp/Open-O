/**
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
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

import javax.servlet.ServletContext;

import org.apache.logging.log4j.Logger;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class NioFileManager {
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;

	private static Logger log = MiscUtils.getLogger();
	private static final String DOCUMENT_CACHE_DIRECTORY = "document_cache";
	public static final String DOCUMENT_DIRECTORY = "document";
	private static final String TEMP_PDF_DIRECTORY = "tempPDF";
	private static final String DEFAULT_FILE_SUFFIX = "pdf";
	private static final String BASE_DOCUMENT_DIR = oscar.OscarProperties.getInstance().getProperty("BASE_DOCUMENT_DIR");
	
	public Path hasCacheVersion2(LoggedInInfo loggedInInfo, String filename, Integer pageNum) {
		
		if ( ! securityInfoManager.hasPrivilege( loggedInInfo, "_edoc", SecurityInfoManager.READ, "" ) ) {
            throw new RuntimeException("Read Access Denied _edoc for provider " + loggedInInfo.getLoggedInProviderNo() );
        }
				
		if(filename.contains(File.separator))
		{
			filename.replace(File.separator, "");
		}

		Path documentCacheDir = getDocumentCacheDir(loggedInInfo);		
		Path outfile = Paths.get(documentCacheDir.toString(), filename + "_" + pageNum + ".png");

		if (! Files.exists(outfile)) {
			outfile = null;
		}
		return outfile;
	}
	
	public Path getDocumentCacheDir(LoggedInInfo loggedInInfo) {
	
		if ( ! securityInfoManager.hasPrivilege( loggedInInfo, "_edoc", SecurityInfoManager.READ, "" ) ) {
            throw new RuntimeException("Read Access Denied _edoc for provider " + loggedInInfo.getLoggedInProviderNo() );
        }

		Path cacheDir = Paths.get(BASE_DOCUMENT_DIR, context.getContextPath(), DOCUMENT_CACHE_DIRECTORY);

		if (! Files.exists(cacheDir)) {
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
	 * 
	 * Returns a file path to the cached version of the given PDF
	 * 
	 */
	public Path createCacheVersion2(LoggedInInfo loggedInInfo, String sourceDirectory, String filename, Integer pageNum){
		
		if ( ! securityInfoManager.hasPrivilege( loggedInInfo, "_edoc", SecurityInfoManager.WRITE, "" ) ) {
            throw new RuntimeException("Read Access Denied _edoc for provider " + loggedInInfo.getLoggedInProviderNo() );
        }
		
		if(filename.contains(File.separator))
		{
			filename.replace(File.separator, "");
		}
		
		Path cacheFilePath = hasCacheVersion2(loggedInInfo, filename, pageNum);
		
		/*
		 * create a new cache file if an existing cache file is not returned.
		 */
		if(cacheFilePath == null)
		{
			Path sourceFile = Paths.get(sourceDirectory, filename);		
			Path documentCacheDir = getDocumentCacheDir(loggedInInfo);
			cacheFilePath = Paths.get(documentCacheDir.toString(), filename + "_" + pageNum + ".png");
	
			PdfDecoder decode_pdf = new PdfDecoder(true);
			FontMappings.setFontReplacements();
			decode_pdf.useHiResScreenDisplay(true);
			decode_pdf.setExtractionMode(0, 96, 96/72f);
			
			try(InputStream inputStream = Files.newInputStream(sourceFile))
			{
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

		Path documentCacheDir = getDocumentCacheDir(loggedInInfo);
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
	 * @throws IOException 
	 * 
	 */
	public Path saveTempFile(final String fileName, ByteArrayOutputStream os, String fileType ) throws IOException {
		Path directory = Files.createTempDirectory(TEMP_PDF_DIRECTORY + System.currentTimeMillis());
		if(fileType == null)
		{
			fileType = DEFAULT_FILE_SUFFIX;
		}
		Path file = Files.createFile( Paths.get(directory.toString(), String.format("%1$s.%2$s", fileName, fileType)) );
		return Files.write(file, os.toByteArray());
	}
	
	public final Path saveTempFile(final String fileName, ByteArrayOutputStream os) throws IOException {
		return saveTempFile(fileName, os, null);
	}
	
	/**
	 * Delete a temp file. Do this often.
	 * @param fileName
	 */
	public final boolean deleteTempFile(final String fileName){
		Path tempfile = Paths.get(fileName);
		try {
			return Files.deleteIfExists(tempfile);
		} catch (IOException e) {
			log.error("Error while deleting temp cache image file " + fileName, e);
		}
		return false;
	}

}
