package ca.openosp.openo.integration.ebs.client.ng;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.logging.log4j.Logger;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import ca.openosp.openo.utility.MiscUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Custom CXF interceptor that dynamically configures WSS4J security processing based on
 * encrypted message content analysis.
 *
 * <p>This interceptor analyzes incoming SOAP messages to detect encryption patterns and
 * dynamically configures the appropriate WSS4J security actions. It counts the number of
 * EncryptedKey elements in the message and configures a matching number of Encryption
 * actions to properly decrypt multi-file MCEDT responses.</p>
 *
 * <p>The interceptor handles three scenarios:</p>
 * <ul>
 *   <li>No encryption: Configures only Timestamp and Signature verification</li>
 *   <li>Single file: Configures one Encryption action</li>
 *   <li>Multiple files: Configures N Encryption actions for N EncryptedKey elements</li>
 * </ul>
 *
 * @since 2025-08-13
 */
public class DynamicWSS4JInInterceptor extends AbstractPhaseInterceptor<Message> {

    private final EdtClientBuilder clientBuilder;
    private static final Logger logger = MiscUtils.getLogger();

    /**
     * Constructs a new DynamicWSS4JInInterceptor with the specified EDT client builder.
     *
     * <p>The interceptor is configured to run in the CXF RECEIVE phase, before WSS4J
     * security processing begins. This allows dynamic configuration based on message content.</p>
     *
     * @param clientBuilder EdtClientBuilder the client builder providing WSS4J configuration
     */
    public DynamicWSS4JInInterceptor(EdtClientBuilder clientBuilder) {
        super(Phase.RECEIVE);
        this.clientBuilder = clientBuilder;
    }

    /**
     * Handles incoming CXF messages by detecting encryption and configuring appropriate
     * WSS4J security actions.
     *
     * <p>This method analyzes the message content to detect encryption patterns, counts
     * the number of EncryptedKey elements, and dynamically builds a WSS4J action string
     * with the correct number of Encryption actions. The configured WSS4J interceptor
     * is then added to the message processing chain.</p>
     *
     * @param message Message the incoming CXF SOAP message to process
     * @throws Fault if an error occurs during message processing or encryption detection
     */
    @Override
    public void handleMessage(Message message) {
        try {
            EncryptionDetectionResult detection = detectEncryption(message);

            Map<String, Object> wssProps = clientBuilder.newWSSInInterceptorConfiguration();

            String action;
            if (!detection.hasEncryption) {
                // No encryption → only timestamp and signature verification
                action = WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE;
                wssProps.put(WSHandlerConstants.ACTION, action);
            } else {
                // Build action string with correct number of Encryption actions based on EncryptedKey count
                StringBuilder actionBuilder = new StringBuilder();
                actionBuilder.append(WSHandlerConstants.TIMESTAMP).append(" ")
                            .append(WSHandlerConstants.SIGNATURE);

                // Add one Encryption action for each EncryptedKey element
                int encryptionCount = detection.encryptedKeyCount > 0 ? detection.encryptedKeyCount : 1;
                for (int i = 0; i < encryptionCount; i++) {
                    actionBuilder.append(" ").append(WSHandlerConstants.ENCRYPTION);
                }

                action = actionBuilder.toString();
                wssProps.put(WSHandlerConstants.ACTION, action);
            }

            // Add WSS4J interceptor to chain with appropriate configuration
            WSS4JInInterceptor wssInterceptor = new WSS4JInInterceptor(wssProps);
            message.getInterceptorChain().add(wssInterceptor);

        } catch (Exception e) {
            throw new Fault(e);
        }
    }

    /**
     * Data class holding the results of encryption detection analysis.
     *
     * <p>Contains flags indicating the presence of body and attachment encryption,
     * as well as a count of EncryptedKey elements which determines how many
     * Encryption actions WSS4J needs to process.</p>
     */
    private static class EncryptionDetectionResult {
        /** Indicates if SOAP body encryption is present (EncryptedData elements) */
        boolean hasEncryption;

        /** Indicates if attachment encryption is present (Attachment-Content-Only marker) */
        boolean hasAttachmentEncryption;

        /** Count of EncryptedKey elements in the SOAP message */
        int encryptedKeyCount;
    }

    /**
     * Detects encryption patterns in the incoming SOAP message and counts EncryptedKey elements.
     *
     * <p>This method reads the message input stream, analyzes the XML content for encryption
     * markers, and counts the number of EncryptedKey elements. The stream is reset after
     * analysis to allow downstream processing by CXF.</p>
     *
     * <p>Detection includes:</p>
     * <ul>
     *   <li>SOAP body encryption (wsse:EncryptedData or xenc:EncryptedData tags)</li>
     *   <li>Attachment encryption (Attachment-Content-Only marker)</li>
     *   <li>Count of xenc:EncryptedKey elements for action configuration</li>
     * </ul>
     *
     * @param message Message the CXF SOAP message to analyze for encryption
     * @return EncryptionDetectionResult containing encryption flags and EncryptedKey count
     */
    private EncryptionDetectionResult detectEncryption(Message message) {
        EncryptionDetectionResult result = new EncryptionDetectionResult();
        try {
            InputStream is = message.getContent(InputStream.class);
            if (is == null) {
                logger.warn("No InputStream found in message when detecting encryption.");
                return result;
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            String xml = bos.toString("UTF-8");

            // Reset stream so CXF can still process it downstream
            message.setContent(InputStream.class,
                    new java.io.ByteArrayInputStream(bos.toByteArray()));

            // Detect body encryption markers
            if (xml.contains("<wsse:EncryptedData") || xml.contains("<xenc:EncryptedData")) {
                result.hasEncryption = true;
            }

            // Detect attachment encryption markers
            if (xml.contains("Attachment-Content-Only")) {
                result.hasAttachmentEncryption = true;
            }

            // Count EncryptedKey elements to determine how many Encryption actions are needed
            result.encryptedKeyCount = countOccurrences(xml, "<xenc:EncryptedKey");

            logger.debug("Encryption detection result: hasEncryption={}, hasAttachmentEncryption={}, encryptedKeyCount={}",
                    result.hasEncryption, result.hasAttachmentEncryption, result.encryptedKeyCount);

        } catch (Exception e) {
            logger.error("Error reading message content for encryption detection", e);
        }
        return result;
    }

    /**
     * Counts the number of occurrences of a substring within a larger text string.
     *
     * <p>This helper method performs a case-sensitive sequential search through the text,
     * counting each non-overlapping occurrence of the specified substring. Used to count
     * EncryptedKey elements in SOAP XML content.</p>
     *
     * @param text String the text to search within
     * @param substring String the substring pattern to count
     * @return int the number of times the substring appears in the text (0 if not found)
     */
    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}
