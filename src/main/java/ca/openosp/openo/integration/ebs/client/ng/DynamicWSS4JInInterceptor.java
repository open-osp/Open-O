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
 * @since 2026-01-30
 */
public class DynamicWSS4JInInterceptor extends AbstractPhaseInterceptor<Message> {

    private final EdtClientBuilder clientBuilder;
    private static final Logger logger = MiscUtils.getLogger();

    /**
     * Maximum number of EncryptedKey elements allowed in a SOAP message.
     *
     * <p>This limit prevents denial-of-service attacks where malicious messages contain
     * an excessive number of EncryptedKey substrings, which would cause excessive CPU and
     * memory usage during action string construction. Real MCEDT multi-file downloads
     * typically contain 1-5 files, so 20 provides a safe upper bound while preventing abuse.</p>
     */
    private static final int MAX_ENCRYPTED_KEYS = 20;

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
                int encryptionCount;
                if (detection.encryptedKeyCount > 0) {
                    // Apply security limit to prevent DoS attacks from crafted messages
                    // with excessive EncryptedKey substrings
                    encryptionCount = Math.min(detection.encryptedKeyCount, MAX_ENCRYPTED_KEYS);
                    if (detection.encryptedKeyCount > MAX_ENCRYPTED_KEYS) {
                        logger.warn("DynamicWSS4JInInterceptor detected {} EncryptedKey elements, " +
                                    "capping at {} to prevent denial-of-service. This may indicate " +
                                    "a malicious message or malformed MCEDT response.",
                                    detection.encryptedKeyCount, MAX_ENCRYPTED_KEYS);
                    }
                } else {
                    // Unexpected state: encryption detected but no EncryptedKey elements found.
                    // Log for diagnostics and default to a single Encryption action to preserve behavior.
                    logger.warn("DynamicWSS4JInInterceptor detected encryption (hasEncryption=true) " +
                                "but found zero EncryptedKey elements. Defaulting to a single " +
                                "Encryption action. This may indicate an unexpected message structure.");
                    encryptionCount = 1;
                }
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

        /**
         * Indicates if attachment encryption is present (Attachment-Content-Only marker).
         *
         * <p>This flag is currently used only within {@link #detectEncryption(Message)} for
         * diagnostic logging and troubleshooting of incoming messages. It is intentionally
         * retained to support future enhancements where attachment-level encryption may
         * need to be handled differently from SOAP body encryption in the WSS4J action
         * configuration.</p>
         */
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
     * <p><b>Error Handling:</b> This method uses fail-fast error handling. Any I/O errors
     * or exceptions are propagated to the caller rather than being swallowed, preventing
     * silent misconfiguration of WSS4J security actions that would cause downstream
     * decryption failures.</p>
     *
     * @param message Message the CXF SOAP message to analyze for encryption
     * @return EncryptionDetectionResult containing encryption flags and EncryptedKey count
     * @throws java.io.IOException if an error occurs reading the message content
     */
    private EncryptionDetectionResult detectEncryption(Message message) throws java.io.IOException {
        EncryptionDetectionResult result = new EncryptionDetectionResult();

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

        // Count EncryptedKey elements to determine how many Encryption actions are needed.
        // Uses simple string matching which is sufficient for well-formed SOAP Security headers.
        // MCEDT responses follow WS-Security standards and don't contain comments or CDATA in
        // security headers that could cause false matches.
        result.encryptedKeyCount = countOccurrences(xml, "<xenc:EncryptedKey");

        logger.debug("Encryption detection result: hasEncryption={}, hasAttachmentEncryption={}, encryptedKeyCount={}",
                result.hasEncryption, result.hasAttachmentEncryption, result.encryptedKeyCount);

        return result;
    }

    /**
     * Counts the number of occurrences of a substring within a larger text string.
     *
     * <p>This helper method performs a case-sensitive sequential search through the text,
     * counting each non-overlapping occurrence of the specified substring. Used to count
     * EncryptedKey elements in SOAP XML content.</p>
     *
     * <p><b>Input Validation:</b> The substring parameter must not be null or empty.
     * If the text parameter is null or empty, the method returns 0 (no matches found).
     * This defensive approach prevents NullPointerException and handles edge cases gracefully.</p>
     *
     * @param text String the text to search within (null or empty returns 0)
     * @param substring String the substring pattern to count (must not be null or empty)
     * @return int the number of times the substring appears in the text (0 if not found)
     * @throws IllegalArgumentException if substring is null or empty
     */
    private int countOccurrences(String text, String substring) {
        // Validate substring parameter - must not be null or empty
        if (substring == null || substring.isEmpty()) {
            throw new IllegalArgumentException("substring must not be null or empty");
        }

        // Handle null or empty text gracefully - no matches possible
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}
