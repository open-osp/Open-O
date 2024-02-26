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
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.managers.NioFileManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;
import oscar.OscarProperties;
import oscar.form.util.FormTransportContainer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A utility that converts HTML into a PDF and returns an Oscar eDoc object.
 * 
 * This is useful for converting Forms and eForms with well structured HTML into 
 * PDF documents that can be attached to Consultation Requests, Faxes or transferred 
 * to other file systems.
 * 
 * NOT ALL DOCUMENTS ARE CONVERTABLE. USE AT OWN RISK.
 */
public class ConvertToEdoc {

	private static final Logger logger = MiscUtils.getLogger();

	public enum DocumentType { eForm, form }
	private enum PathAttribute { src, href }
	private enum FileType { pdf, css, jpeg, png, gif }

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
	 * 
	 * Returns an EDoc Object with a filename that is saved in this class' 
	 * temporary DEFAULT_FILE_PATH. This file can be persisted by moving from the 
	 * temporary path to a file storage path prior to persisting this Object to the 
	 * database. 
	 */
	public synchronized static EDoc from( EFormData eform ) {
		
		String eformString = eform.getFormData();	
		String demographicNo = eform.getDemographicId() + "";
		String filename = buildFilename( eform.getFormName(), demographicNo );
		String eDocDescription = "".equals(eform.getSubject().trim()) ? eform.getFormName() : eform.getSubject();
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
		String eDocDescription = "".equals(eForm.getSubject().trim()) ? eForm.getFormName() : eForm.getSubject();
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
	 * 
	 * Returns an EDoc Object with a filename that is saved in this class' 
	 * temporary DEFAULT_FILE_PATH. This file can be persisted by moving from the 
	 * temporary path to a file storage path prior to persisting this Object to the 
	 * database. 
	 * 
	 * Example use:
	 * 
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
		}

		return path;
	}

	/**
	 * creates a filename
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
			throws DocumentException, IOException {

        HashMap<String, String> htmlToPdfSettings = new HashMap<String, String>() {{
			put("load.blockLocalFileAccess", "false");
			put("web.enableIntelligentShrinking", "true");
	        put("web.minimumFontSize", "10");
	        put("load.zoomFactor", "0.92");
	        put("web.printMediaType", "true");
	        put("web.defaultEncoding", "utf-8");
	        put("T", "10mm");
	        put("L", "8mm");
	        put("R", "8mm");			
			put("web.enableJavascript", "false");
		}};

		try(InputStream inputStream = HtmlToPdf.create()
						.object(HtmlToPdfObject.forHtml(document, htmlToPdfSettings))
						.pageSize(PdfPageSize.Letter)
						.convert()
		){
			IOUtils.copy(inputStream, os);
		} catch (Exception e) {
			logger.error("Document conversion exception thrown, attempting with alternate conversion library.", e);
			ITextRenderer renderer = new ITextRenderer();
			SharedContext sharedContext = renderer.getSharedContext();
			sharedContext.setPrint(true);
			sharedContext.setInteractive(false);
			sharedContext.setReplacedElementFactory(new ReplacedElementFactoryImpl());
			sharedContext.getTextRenderer().setSmoothingThreshold(0);
			renderer.setDocument( getDocument(document),null);
			renderer.layout();
			renderer.createPDF( os, true );
		}
	}

	/**
	 * Build this HTML document. 
	 * - adds translated image paths
	 * - inserts custom stylesheets.
	 */
	private static Document buildDocument( final String documentString ) {
		Document document = getDocument( documentString );
		if( document != null ) {
			translateResourcePaths( document );
			setHeadElement( document );
			addCss( document );
		}
		return document;
	}
	
	/**
	 * Get a W3C XML document from well formed XML
	 */
	private static Document getDocument( final String documentString ) {

		DocumentBuilder builder;
		Document document = null;					

		try(ByteArrayInputStream bais = new ByteArrayInputStream( documentString.getBytes() )) {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(bais);			
		} catch (SAXException | ParserConfigurationException | IOException e) {
			logger.error( "", e );
		}

		return document;
	}
	
	/**
	 * Adds custom CSS templates to the Document.
	 * 
	 * Normally the stylesheets should be included with the HTML being converted. This method may be
	 * required to alter the current style for better print to PDF. Or if the original stylesheet gets 
	 * stripped out of the HTML like with the Rich Text Letter Editor
	 * 
	 * A stylesheet reference can be set into the origin HTML document with a hidden input tag: 
	 * <input type="hidden" id="customStylesheet" name="customStylesheet" value="<stylesheet filename>" />
	 * This tag would be inserted between the section tag of a Rich Text Letter Template. 
	 * The custom stylesheet will be retrieved from Oscar's images directory. Only the filename needs to be 
	 * given by the input tag. This method will build the filepath.

	 * - Adds a head element to the document if one does not exist.
	 *   
	 */
	private static void addCss( Document document ) {

		XPath xpath = XPathFactory.newInstance().newXPath();
		Element styleSheetElement = null;

		try {
			styleSheetElement = (Element) xpath.evaluate("//*[@id='" + CUSTOM_STYLESHEET_ID + "']", document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			logger.error("Error", e);
		}

		if( styleSheetElement != null ) {
			setParameterInjectedCss( document, styleSheetElement.getAttribute("value") );
		}
	}
	
	/**
	 * It is critical that a head element is present for this given document. 
	 */
	private static void setHeadElement( Document document ) {
		
		Node headNode = null;
		Node htmlNode = document.getDocumentElement();
		NodeList nodeList = htmlNode.getChildNodes();
		
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			Node node = nodeList.item(i);

			if( "head".equals( node.getNodeName() )) {
				headNode = node;
			}
		}
		
		if( headNode == null ) {              
	      	htmlNode.appendChild( document.createElement("head") );
		}
	}
	
	/**
	 * Returns the head element directly from the given Document 
	 */
	private static Element getHeadElement( Document document ) {
		return (Element) document.getElementsByTagName("head").item(0);
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
			linkElement.setAttribute("rel", "stylesheet");
			linkElement.setAttribute("href", filename);
			getHeadElement( document ).appendChild( linkElement );
		}		
	}

	/**
	 * HTTP request paths routed through Struts need to be 
	 * translated into a relative path to the global images directory.
	 */
	private static void translateResourcePaths( Document document ) {
		translateLinkPaths( document );
		translateImagePaths( document );
	}
	
	/**
	 * This method handles paths set into a link element
	 */
	private static void translateLinkPaths( Document document ) {
		NodeList linkNodeList = document.getElementsByTagName("link");
		if( linkNodeList != null ) {
			translatePaths( linkNodeList, PathAttribute.href );
		}
	}
	
	/**
	 * This method handles paths set into an image element.
	 */
	private static void translateImagePaths( Document document ) {
		NodeList imageNodeList = document.getElementsByTagName("img");
		if( imageNodeList != null ) {
			translatePaths( imageNodeList, PathAttribute.src );
		}
	}
	
	/**
	 * Translate any given Link or Image element resource path from 
	 * a Struts HTTP request parameter or HTTP relative context path.
	 * 
	 * All resource links in the document must be absolute for the PDF 
	 * creator to work.
	 */
	private static void translatePaths( NodeList nodeList, PathAttribute pathAttribute ) {

		for( int i = 0; i < nodeList.getLength(); i++ ) {
			
			Element element = (Element) nodeList.item(i);			
			String path = element.getAttribute( pathAttribute.name() );
			String validLink;
			String parameters = null;
			String[] parameterList = null;		
			List<String> potentialFilePaths = new ArrayList<>();

			if( path.contains("?") ) {
				// image or link paths with parameters
				parameters = path.split("\\?")[1];
			} else {
				// these are most likely relative context paths
				path = getRealPath( path );
				potentialFilePaths.add( path );
			}
			
			/* parse the parameters and test if any are links to the eForm
			 * images library. Otherwise these resources are no good.
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

			/* there really should be only one valid path.
			 * Only use the one that validates
			 */
			validLink = validateLink( potentialFilePaths );
			
			/* change the element resource link to something absolute
			 * that can be used by the PDF creator.
			 */
			if( validLink != null ) {
				element.setAttribute( pathAttribute.name(), validLink );
			}
		}
	}
	
	/**
	 * Feed this method a filename and it will return a full path to the Oscar images directory. 
	 */
	private static String buildImageDirectoryPath( String filename ) {
		return Paths.get(getImageDirectory(), filename).toString();
	}
	
	/**
	 * Convert a given context path into a file system absolute path.
	 */
	private static String getRealPath(String uri ) {
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
	 * Returns only 1 valid file link.
	 */
	private static String validateLink( String potentialLink ) {

		File file = null;
		String absolutePath = null;
				
		for( FileType fileType : FileType.values() ) {
			if( ( potentialLink.endsWith( fileType.name().toLowerCase() ) )) {
				file = new File( potentialLink );
			}			
		}

		if( file != null && file.isFile() ) {
			absolutePath = file.getAbsolutePath(); 
			
			logger.debug( "Validated path " + absolutePath );
		}

		return absolutePath;
	}

	/**
	 * Clean up any artifacts or poorly formed XML
	 */
	private static String tidyDocument( final String documentString ) {

		Tidy tidy = getTidy();
		StringReader reader = new StringReader( documentString );
		StringWriter writer = new StringWriter();
		String correctedDocument = "";

		/*
		 * clean the HTML document with JTidy tools
		 */
		Document document = tidy.parseDOM( reader, writer );

		/*
		 * Use the w3c Document output to interpret the external image
		 * and css links into absolute links that can be
		 * read by the HTMLtoPDF parser.
		 */
		translateResourcePaths( document );
		setHeadElement( document );
		addCss( document );

		/*
		 * Pretty print the final HTML output for use with the HTMLtoPDF parser.
		 */
		try(OutputStream os = new ByteArrayOutputStream()) {
			tidy.pprint(document, os);
			correctedDocument = os.toString();
		} catch (IOException e) {
			logger.error("JTidy pretty print document error ", e);
		}

		writer.flush();

		try {
			writer.close();
		} catch (IOException e) {
			logger.error( "Error closing writer stream for JTidy", e );
		}

		return correctedDocument;
	}
	
	/**
	 * Instantiate the Tidy HTML validator
	 */
	private static Tidy getTidy() {
		Tidy tidy = new Tidy();

		/* Properties intentionally hard-coded.
		 * These settings cannot be overriden in the properties file.
		 */
		tidy.setForceOutput( Boolean.TRUE ); 	// output the XHTML even if it fails the validator.
		tidy.setDropEmptyParas(Boolean.TRUE);
		tidy.setMakeClean( Boolean.TRUE );
		tidy.setLogicalEmphasis( Boolean.TRUE ); // replace the b and em tags with proper <strong> tags
		tidy.setInputEncoding("UTF-8");
		tidy.setOutputEncoding("UTF-8");

		// For debug logging only.
		if( logger.isDebugEnabled() ) {
			tidy.setHideComments( Boolean.FALSE );
			tidy.setQuiet( Boolean.FALSE );
		} else {
			tidy.setHideComments( Boolean.TRUE );
			tidy.setQuiet( Boolean.TRUE );
		}

		try (InputStream is = ConvertToEdoc.class.getClassLoader().getResourceAsStream( "/oscar/documentManager/ConvertToEdoc.properties" )) {
			if( is != null ) {
				Properties properties = new Properties();
				properties.load( is );
				tidy.getConfiguration().addProps( properties );
			}
		} catch (IOException e) {
			logger.warn("Could not load Tidy properties ", e);
		}

		logger.debug( printTidyConfig( tidy ) );
		
		return tidy;
	}

	private static String getImageDirectory() {
		return DEFAULT_IMAGE_DIRECTORY;
	}
	
	/**
	 * Prints the document contents to console. Used for debugging.
	 */
	private static String printDocument( Document doc ) {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    StreamResult streamResult = null;
	    try {
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			streamResult = new StreamResult();
			streamResult.setOutputStream( new ByteArrayOutputStream() );
			transformer.transform( new DOMSource(doc), streamResult );

		} catch (Exception e) {
			logger.error("error debugging document " + e );
		} finally {
			if( streamResult != null ) {
				try {
					streamResult.getOutputStream().close();
				} catch (IOException e) {
					logger.error("error debugging document " + e );
				}
			}
		}

		assert streamResult != null;
		return streamResult.getOutputStream().toString();
	}
	
	public static String printTidyConfig( Tidy tidy ) {

		String log = "No config found";
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     OutputStreamWriter osw = new OutputStreamWriter( baos );){
			tidy.getConfiguration().printConfigOptions( osw, true );
			log = baos.toString();
		} catch (IOException e) {
			logger.error("Error", e);
		}

		return log;
	}
}
