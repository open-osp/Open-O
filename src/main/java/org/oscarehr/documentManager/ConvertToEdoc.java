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
package org.oscarehr.documentManager;

import com.lowagie.text.DocumentException;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import io.woo.htmltopdf.PdfPageSize;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.email.core.EmailData;
import org.oscarehr.managers.NioFileManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;
import oscar.OscarProperties;
import oscar.form.util.FormTransportContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A utility that converts HTML into a PDF and returns an Oscar eDoc object.

 * This is useful for converting Forms and eForms with well structured HTML into 
 * PDF documents that can be attached to Consultation Requests, Faxes or transferred 
 * to other file systems.

 * NOT ALL DOCUMENTS ARE CONVERTABLE. USE AT OWN RISK.
 */
public final class ConvertToEdoc {

	private static final Logger logger = MiscUtils.getLogger();

	public enum DocumentType { eForm, form }
	public enum ElementAttribute { src, href, value, name, id, title, type, rel, media }
	private enum FileType { pdf, css, jpeg, png, gif, js, jpg }
	public static final String CUSTOM_STYLESHEET_ID = "pdfMediaStylesheet";
	private static final String DEFAULT_IMAGE_DIRECTORY = String.format( "%1$s", OscarProperties.getInstance().getProperty( "eform_image" ) );
	private static final String DEFAULT_FILENAME = "temporaryPDF";
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private static final String DEFAULT_CONTENT_TYPE = "application/pdf";
	private static final String SYSTEM_ID = "-1";
	private static String contextPath;
	private static String realPath;
	private static final NioFileManager nioFileManager = SpringUtils.getBean( NioFileManager.class );

	/**
	 * Convert EForm to EDoc

	 * Returns an EDoc Object with a filename that is saved in this class' 
	 * temporary DEFAULT_FILE_PATH. This file can be persisted by moving from the 
	 * temporary path to a file storage path prior to persisting this Object to the 
	 * database. 
	 */
	public synchronized static EDoc from( EFormData eform ) {
		
		String eformString = eform.getFormData();	
		String demographicNo = eform.getDemographicId() + "";
		String filename = buildFilename( eform.getFormName(), demographicNo );
		String eDocDescription = eform.getSubject().trim().isEmpty() ? eform.getFormName() : eform.getSubject();
		EDoc edoc = null;		
		Path path = execute( eformString, filename );
		
		if(Files.isReadable(path)) {
			edoc = buildEDoc( path.getFileName().toString(),
					eDocDescription, 
					null,
					eform.getProviderNo(), 
					demographicNo, 
					DocumentType.eForm,
					path.getParent().toString());
		} else {
			logger.error("Could not read temporary PDF file " + filename);
		}		
		
		return edoc;	
	}

	public synchronized static EDoc from(EFormData eForm, Path eFormPDFPath) throws PDFGenerationException {
		String demographicNo = eForm.getDemographicId() + "";
		String filename = buildFilename( eForm.getFormName(), demographicNo );
		String eDocDescription = eForm.getSubject().trim().isEmpty() ? eForm.getFormName() : eForm.getSubject();
		EDoc edoc = null;

		if(Files.isReadable(eFormPDFPath)) {
			edoc = buildEDoc( eFormPDFPath.getFileName().toString(),
					eDocDescription, 
					null,
					eForm.getProviderNo(), 
					demographicNo, 
					DocumentType.eForm,
					eFormPDFPath.getParent().toString());
		} else {
			throw new PDFGenerationException("Could not read temporary PDF file " + filename);
		}		
		
		return edoc;
	}

	/**
	 * Convert Form to EDoc

	 * Returns an EDoc Object with a filename that is saved in this class' 
	 * temporary DEFAULT_FILE_PATH. This file can be persisted by moving from the 
	 * temporary path to a file storage path prior to persisting this Object to the 
	 * database. 

	 * Example use:
	 * public ActionForward eDocument(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			FormBean bpmh = (FormBean) form;

			FormTransportContainer formTransportContainer = new FormTransportContainer( 
					response, request, mapping.findForward("success").getPath() );

			formTransportContainer.setDemographicNo( bpmh.getDemographicNo() );
			formTransportContainer.setProviderNo( bpmh.getProvider().getProviderNo() );
			formTransportContainer.setSubject( "BPMH Form ID " + bpmh.getFormId() );
			formTransportContainer.setFormName( "bpmh" );
			formTransportContainer.setRealPath( getServlet().getServletContext().getRealPath( File.separator ) );
			
			FormsManager#saveFormDataAsEDoc( LoggedInInfo.getLoggedInInfoFromSession(request), formTransportContainer );
			
			return ActionForward;
		}
	 * 
	 */
	public synchronized static EDoc from( FormTransportContainer formTransportContainer ) {
	
		String htmlString = formTransportContainer.getHTML();
		String demographicNo = formTransportContainer.getDemographicNo();
		String filename = buildFilename( formTransportContainer.getFormName(), demographicNo );
		String subject = formTransportContainer.getSubject();
		String providerNo = formTransportContainer.getProviderNo();
		if( providerNo == null ) {
			providerNo = formTransportContainer.getLoggedInInfo().getLoggedInProviderNo();
		}
		// this should be the same for every thread.
		ConvertToEdoc.contextPath = formTransportContainer.getContextPath();
		ConvertToEdoc.realPath = formTransportContainer.getRealPath();
		
		EDoc edoc = null;		
		Path path = execute( htmlString, filename );
		
		if(Files.isReadable(path)) {
			edoc = buildEDoc( filename, 
					subject,
					null,
					providerNo, 
					demographicNo, 
					formTransportContainer.getDocumentType(), 
					path.toString());			
		} else {
			logger.error("Could not read temporary PDF file " + filename);
		}
		
		return edoc;
	}
	
	/**
	 * 
	 * Save the document as a temporary PDF. Does not save or return an eDoc entity. 
	 * This is a temporary file location. Ensure that the file is deleted after it's used. 
	 * 
	 * @return temporary path to the produced PDF.. 
	 */
	public synchronized static Path saveAsTempPDF( EFormData eform ) {
		String eformString = eform.getFormData();
		String filename = buildFilename( eform.getFormName(), eform.getDemographicId()+"" );
		return execute( eformString, filename );
	}
	
	/**
	 * 
	 * Save the document as a temporary PDF. Does not save or return an eDoc entity.
	 * This is a temporary file location. Ensure that the file is deleted after it's used.  
	 * 
	 * @return temporary path to the produced PDF. 
	 */
	public synchronized static Path saveAsTempPDF(FormTransportContainer formTransportContainer) {
		String htmlString = formTransportContainer.getHTML();
		ConvertToEdoc.contextPath = formTransportContainer.getContextPath();
		ConvertToEdoc.realPath = formTransportContainer.getRealPath();
		String filename = buildFilename( formTransportContainer.getFormName(), formTransportContainer.getDemographicNo() );
		return execute( htmlString, filename );
	}

	/**
	 * 
	 * Save the document as a temporary PDF. Does not save or return an eDoc entity. 
	 * This is a temporary file location. Ensure that the file is deleted after it's used. 
	 * 
	 * @param emailData An object representing email-related data, including HTML content.
	 * @return temporary path to the produced PDF.. 
	 */
	public synchronized static Path saveAsTempPDF(EmailData emailData) {
		String htmlString = emailData.getEncryptedMessage();
		String filename = buildFilename("emailbody_", "");
		return execute(htmlString, filename);
	}
	
	/**
	 * Execute building and saving PDF to temp directory.
	 */
	private static Path execute( final String eformString, final String filename ) {
		Path path = null;
		String document = tidyDocument( eformString );
		try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			renderPDF( document, os );
			path = nioFileManager.saveTempFile( filename, os );
		} catch (DocumentException e1) {
			logger.error( "Exception parsing file to PDF. File not saved. ", e1 );
		} catch (IOException e) {
			logger.error("Problem while writing PDF file to filesystem. " + filename, e);
		} catch (PDFGenerationException e) {
			logger.error("Problem while generating PDF file. " + filename, e);
		}

		return path;
	}

	/**
	 * create a well-formed filename
	 */
	private static String buildFilename( String filename, String demographicNo ) {
		
		if( filename == null || filename.isEmpty() ) {
			filename = DEFAULT_FILENAME;
		}
		
		filename = filename.trim();
		filename = filename.replaceAll(" ", "_");
		filename = filename.replaceAll("/", "_");
		filename = String.format("%1$s_%2$s", filename, demographicNo );
		filename = String.format("%1$s_%2$s", filename, new Date().getTime() );
		return filename;
	}
	
	/**
	 * Builds an EDoc instance.
	 */
	public static EDoc buildEDoc( final String filename, final String subject, final String sourceHtml,
			final String providerNo, final String demographicNo, final DocumentType documentType, final String filePath) {

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( DEFAULT_DATE_FORMAT ); 		
		final String todayDate = simpleDateFormat.format( new Date() );
		
		EDoc eDoc = new EDoc(
				( subject == null ) ? "" : subject,
				documentType.name(), 
				filename, 
				sourceHtml, 
				SYSTEM_ID, 
				providerNo, 
				"",
				EDocFactory.Status.ACTIVE.getStatusCharacter(), 
				todayDate,
				"", 
				new Date().toString(),
				EDocFactory.Module.demographic.name(), 
				demographicNo,
				Boolean.FALSE );
		
		eDoc.setContentType( DEFAULT_CONTENT_TYPE );
		eDoc.setContentDateTime( new Date() );
		eDoc.setNumberOfPages(EDocUtil.getPDFPageCount(filePath + "/" + filename));
		eDoc.setFilePath(filePath);

		return eDoc;
	}

	/**
	 * Use the io.woo.htmltopdf tools for HTML to PDF or
	 * use the Flying Saucer tools if there is a failure.
	 * Flying Saucer requires a well formed w3c XHTML document - which is usually
	 * not the case.
	 */
	private static void renderPDF( final String document, ByteArrayOutputStream os )
			throws DocumentException, IOException, PDFGenerationException {

        HashMap<String, String> htmlToPdfSettings = new HashMap<String, String>() {{
			put("load.blockLocalFileAccess", "false");
			put("web.enableIntelligentShrinking", "true");
	        put("web.minimumFontSize", "10");
//	        put("load.zoomFactor", "0.92");
	        put("web.printMediaType", "true");
	        put("web.defaultEncoding", "utf-8");
	        put("T", "10mm");
	        put("L", "8mm");
	        put("R", "8mm");			
			put("web.enableJavascript", "false");
		}};
		logger.debug("htmlToPdfSettings: " + htmlToPdfSettings);
		logger.debug("Rendering PDF document from document string: " + document);
		try(InputStream inputStream = HtmlToPdf.create()
						.object(HtmlToPdfObject.forHtml(document, htmlToPdfSettings))
						.pageSize(PdfPageSize.Letter)
						.convert()
		){
			IOUtils.copy(inputStream, os);
		} catch (Exception e) {
			throw new PDFGenerationException("Document conversion exception thrown, attempting with alternate conversion library.",e);
//			logger.error("Document conversion exception thrown, attempting with alternate conversion library.", e);
//			ITextRenderer renderer = new ITextRenderer();
//			SharedContext sharedContext = renderer.getSharedContext();
//			sharedContext.setPrint(true);
//			sharedContext.setInteractive(false);
//			sharedContext.setReplacedElementFactory(new ReplacedElementFactoryImpl());
//			sharedContext.getTextRenderer().setSmoothingThreshold(0);
//			renderer.setDocumentFromString(document,null);
//			renderer.layout();
//			renderer.createPDF( os, true );
		}
	}

	/**
	 * Clean and parse the HTML document string into a manageable DOM
	 * with JSoup tools
	 * Also validates all URL paths to resources.
	 * @param documentString raw HTML string
	 * @return org.jsoup.nodes.Document JSoup DOM
	 */
	public static Document getDocument(final String documentString ){
		String incomingDocumentString = "";

		// null check
		if(documentString != null) {
			incomingDocumentString = documentString;
		}

		// DOCTYPE declarations are mandatory. HTML5 if none is declared.
//		if(! incomingDocumentString.startsWith("<!DOCTYPE") || ! incomingDocumentString.startsWith("<!doctype")) {
//			incomingDocumentString = "<!DOCTYPE html>" + incomingDocumentString;
//		}

		//TODO: COMING SOON.  EForms should be selectively sanitized against potential injection attacks and etc...
//		Safelist safelist = Safelist.relaxed();
//		safelist.addTags().addTags() etc...
//		String sanitized = Jsoup.clean(documentString, safeList);

		Document document = Jsoup.parse(incomingDocumentString);

		//TODO add any other custom Document.OutputSettings here.
		document.outputSettings().prettyPrint(Boolean.FALSE);

		/*
		 * remove invalid, suspicious, and CDN links
		 */
		validateResourcePaths(document);

		/*
		 * Returns a Document object.
		 * Document will contain a blank HTML page if the incoming HTML
		 * string is NULL, empty, or if an error occurs.
		 */
		return document;
	}
	
	/**
	 * Adds custom CSS templates to the Document.
	 * Normally the stylesheets should be included with the HTML being converted. This method may be
	 * required to alter the current style for better print to PDF. Or if the original stylesheet gets 
	 * stripped out of the HTML like with the Rich Text Letter Editor
	 * A stylesheet reference can be set into the origin HTML document with a hidden input tag: 
	 * <input type="hidden" id="customStylesheet" name="customStylesheet" value="<stylesheet filename>" />
	 * This tag would be inserted between the section tag of a Rich Text Letter Template. 
	 * The custom stylesheet will be retrieved from Oscar's images directory. Only the filename needs to be 
	 * given by the input tag. This method will build the filepath.
	 * - Adds a head element to the document if one does not exist.
	 *   
	 */
	private static void addCss( Document document ) {
		Element styleSheetElement = document.getElementById(CUSTOM_STYLESHEET_ID);
		if( styleSheetElement != null && "hidden".equalsIgnoreCase(styleSheetElement.attributes().getIgnoreCase("type")) ) {
			setParameterInjectedCss( document, styleSheetElement.attributes().getIgnoreCase("value") );
		}
	}
	
	/**
	 * It is critical that a head element is present.
	 */
	private static void setHeadElement( Document document ) {
		if(! document.head().isBlock()) {
			document.appendElement("head");
		}
	}
	
	/**
	 * Returns the head element directly from the given Document 
	 */
	private static Element getHeadElement( Document document ) {
		setHeadElement(document);
		return document.head();
	}
	
	/**
	 * Create a Link element from the CSS filename that was inserted into the 
	 * origin HTML as a hidden input element. 
	 */
	private static void setParameterInjectedCss( Document document, String filename ) {
		filename = buildImageDirectoryPath( filename );
		filename = validateLink( filename );

		// add a stylesheet ie: link <link rel="stylesheet" href=".." />
		if( filename != null ) {
			Element linkElement = document.createElement("link");
			linkElement.attr("rel", "stylesheet");
			linkElement.attr("href", filename);
			getHeadElement( document ).appendChild( linkElement );
		}		
	}

	/**
	 * HTTP request paths routed through Struts need to be 
	 * translated into an absolute path to the global image directory.
	 */
	private static void translateResourcePaths( Document document ) {
		Map<List<String>, Element> pathTranslationMap = new HashMap<>();
		translateLinkPaths( document, pathTranslationMap );
		translateImagePaths( document, pathTranslationMap );

		for(Map.Entry<List<String>, Element> pathSet : pathTranslationMap.entrySet()) {
			if(! pathSet.getKey().isEmpty()) {
				Element element = pathSet.getValue();
				List<String> path = pathSet.getKey();

				if(element.hasAttr(ElementAttribute.href.name())) {
					element.attr(ElementAttribute.href.name(), path.get(0));
				}

				if(element.hasAttr(ElementAttribute.src.name())) {
					element.attr(ElementAttribute.src.name(), path.get(0));
				}
			}
		}
	}
	
	/**
	 * CSS (link) resource links
	 */
	private static void translateLinkPaths( Document document, Map<List<String>, Element> pathTranslationMap ) {
		Elements linkNodeList = document.getElementsByTag("link");
		translatePaths(linkNodeList, ElementAttribute.href, pathTranslationMap);
	}

	/**
	 * Javascript resource links
	 */
	private static void translateJavascriptPaths( Document document, Map<List<String>, Element> pathTranslationMap ) {
		Elements linkNodeList = document.getElementsByTag("script");
		translatePaths(linkNodeList, ElementAttribute.src, pathTranslationMap);
	}
	
	/**
	 * Image resource links
	 */
	private static void translateImagePaths( Document document, Map<List<String>, Element> pathTranslationMap ) {
		Elements imageNodeList = document.getElementsByTag("img");
		translatePaths( imageNodeList, ElementAttribute.src , pathTranslationMap);
	}

	/**
	 * Translate any given Link or Image element resource path from
	 * a Struts HTTP request parameter or HTTP relative context path.
	 * All resource links in the document must be absolute for the PDF
	 * creator to work.
	 * TODO filter out relative URI's and external URL's without a proper place holder so they can be removed
	 * @param nodeList jsoup Elements collection of potential resource links
	 * @param pathAttribute jsoup ElementAttribute to be translated to an absolute link
	 * @param pathTranslationMap Map to collect translated URI's
	 */
	private static void translatePaths( Elements nodeList, ElementAttribute pathAttribute, Map<List<String>, Element> pathTranslationMap) {
		for(Element element : nodeList) {
			// go no further if there is no link attribute.
			if(! element.hasAttr(pathAttribute.name())) {
				continue;
			}

			String path = element.attributes().get( pathAttribute.name() );
			String parameters = null;
			String[] parameterList = null;		
			List<String> potentialFilePaths = new ArrayList<>();

			/*
			 * NO EXTERNAL LINKS. These are removed.
			 * eForms are often imported from unknown sources.
			 * Developers tend to use insecure CDN's, links to images, tracking tokens,
			 * and advertisements.
			 */
			if(path.startsWith("http") || path.startsWith("HTTP")) {
				element.remove();
			}

			// internal GET links are validated.
			else if( path.contains("?") ) {
				// image or link paths with parameters
				parameters = path.split("\\?")[1];
			}

			else if(! path.isEmpty()) {
				// these are most likely relative context paths
				path = getRealPath( path );
				if(! path.isEmpty()) {
					potentialFilePaths.add(path);
				}
			}
			
			/* parse the parameters and test if any are links to the eForm
			 * images library. Otherwise, these resources are no good.
			 */
			if( parameters != null && parameters.contains("&") ) {
				parameterList = parameters.split("&");
			}
			
			if( parameterList != null ) {
				for( String parameter : parameterList ) {
					if( parameter.contains("=") ) {
						// these are file names that need a path.
						path = buildImageDirectoryPath( parameter.split("=")[1] );
						potentialFilePaths.add( path );
					}	
				}
			} else if( parameters != null && parameters.contains("=") ) {
				path = buildImageDirectoryPath( parameters.split("=")[1] );
				potentialFilePaths.add( path );
			}

			if(! potentialFilePaths.isEmpty()) {
				pathTranslationMap.put(potentialFilePaths, element);
			}
		}
	}
	
	/**
	 * Feed this method a filename, it will return a full path to the Oscar images directory.
	 */
	private static String buildImageDirectoryPath( String filename ) {
		return Paths.get(getImageDirectory(), filename).toString();
	}

	/**
	 * Convert a given URI into a file system absolute path.
	 * @param uri URI input
	 * @return String fully resolved absolute path
	 */
	private static String getRealPath(String uri) {
		String contextRealPath = "";
		
		logger.debug( "Context path set to " + contextPath );
		
		if( ConvertToEdoc.contextPath != null && ConvertToEdoc.realPath != null ) {
			logger.debug( "Relative file path " + uri );
			contextRealPath = Paths.get(ConvertToEdoc.realPath, ConvertToEdoc.realPath).toAbsolutePath().toString();
		}

		logger.debug( "Absolute file path " + contextRealPath );

		return contextRealPath;
	}

	/**
	 * remove paths to the filesystem or to external sources that are not valid.
	 */
	private static void validateResourcePaths(Document document) {
		Map<List<String>, Element> pathTranslationMap = new HashMap<>();
		translateLinkPaths( document, pathTranslationMap );
		translateImagePaths( document, pathTranslationMap );
		translateJavascriptPaths( document, pathTranslationMap );
		for(Map.Entry<List<String>, Element> pathSet : pathTranslationMap.entrySet()) {
			Element element = pathSet.getValue();
			List<String> paths = pathSet.getKey();
			if(validateLink(paths) == null) {
				// vamoose
				element.remove();
			}
		}
	}
	
	/**
	 * Returns a List of valid file links from a list of potential valid links.
	 */
	private static List<String> validateLinks( List<String> potentialLinks ) {

		List<String> finalLinks = null;
		String validLink;
		
		for( String potentialLink : potentialLinks ) {
			if( potentialLink.isEmpty() ) {
				continue;
			}
			
			validLink = validateLink( potentialLink );
			
			if( finalLinks == null && validLink != null ) {
				finalLinks = new ArrayList<>();
			}

			if( validLink != null ) {
				finalLinks.add( validLink );
			}
		}
		
		return finalLinks;
	}

	/**
	 * Returns the first valid file link from a list of potential valid links.
	 * Used in conjunction with as an overload method.
	 * See use in methods: setParameterInjectedCss() and validateLinks
	 * @param potentialLinks Collection of links to validate. A Collection is
	 *                       as a wrapper for a single link ie: potentialLinks.get(0)
	 * @return String a single valid link
	 */
	private static String validateLink( List<String> potentialLinks ) {

		logger.debug( "Validating potential file paths " + potentialLinks );
		
		List<String> validLinks = validateLinks( potentialLinks );
		
		if( validLinks != null ) {
			return validLinks.get(0);
		}
		
		return null;
	}

	/**
	 * Main link validation method
	 * See overload above
	 * @param potentialLink String link to validate
	 * @return String valid link
	 */
	private static String validateLink( String potentialLink ) {

		String absolutePath = null;
		Path path = null;

		potentialLink = filterFileType(potentialLink);

		if(potentialLink != null) {
			path = Paths.get(potentialLink);
		}

		if(path != null && Files.exists(path)) {
			absolutePath = path.toAbsolutePath().toString();
			
			logger.debug( "Validated path " + absolutePath );
		}

		return absolutePath;
	}

	/**
	 * File type filtering.  Links with invalid filetypes
	 * will be removed.
	 * See Enum: ConvertToEdoc FileType for complete list.
	 */
	private static String filterFileType(String path) {
		String pathFileType = FilenameUtils.getExtension(path);
		for( FileType legalFileType : FileType.values() ) {
			if (legalFileType.name().equalsIgnoreCase(pathFileType)) {
				return path;
			}
		}
		return null;
	}

	/**
	 * Clean up any artifacts or poorly formed XHTML
	 * and fetch the HTML template resources.
	 */
	private static String tidyDocument( final String documentString) {
		Document document = getDocument(documentString);

		/*
		 * Use the w3c Document output to interpret the external image
		 * and css links into absolute links that can be
		 * read by the HTMLtoPDF parser.
		 */
		translateResourcePaths(document);
		addCss(document);

		/*
		 * Convert edited Document object back to String
		 * Mostly because the htmltopdf tools require String input
		 * for some strange reason.
		 */
		return documentToString(document);
	}

	/**
	 * fetch the default EForm image directory in the host file system
	 * @return String directory path
	 */
	private static String getImageDirectory() {
		return DEFAULT_IMAGE_DIRECTORY;
	}

	/**
	 * Print a well-formed HTML Document object to String using Jtidy tools
	 * @param document w3c DOM
	 * @return String HTML output
	 */
	public static String documentToString(Document document) {
		return document.outerHtml();
	}

}
