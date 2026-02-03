/**
 * Copyright (c) 2025. Magenta Health. All Rights Reserved.
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
 * This software was written for
 * Magenta Health
 * Toronto, Ontario, Canada
 */
package ca.openosp.openo.integration.ebs.client.ng;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.message.Message;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DynamicWSS4JInInterceptor encryption detection and action configuration.
 *
 * <p>This test class verifies the dynamic WSS4J security action configuration based on
 * encrypted SOAP message content analysis, specifically for MCEDT multi-file downloads.</p>
 *
 * <p><b>Test Categories:</b></p>
 * <ul>
 *   <li>Action string generation - verifies correct number of Encryption actions</li>
 *   <li>Encryption detection - tests EncryptedKey counting logic</li>
 *   <li>Security - validates DoS prevention via bounded EncryptedKey counting</li>
 *   <li>Edge cases - handles malformed messages and unexpected states</li>
 *   <li>Helper methods - validates countOccurrences utility function</li>
 * </ul>
 *
 * @since 2025-01-30
 */
@Tag("unit")
@Tag("encryption")
@Tag("mcedt")
@DisplayName("DynamicWSS4JInInterceptor Unit Tests")
class DynamicWSS4JInInterceptorTest {

    private DynamicWSS4JInInterceptor interceptor;
    private EdtClientBuilder mockClientBuilder;
    private Message mockMessage;
    private InterceptorChain mockChain;
    private Map<String, Object> wssProps;

    @BeforeEach
    void setUp() {
        mockClientBuilder = mock(EdtClientBuilder.class);
        mockMessage = mock(Message.class);
        mockChain = mock(InterceptorChain.class);
        wssProps = new HashMap<>();

        when(mockClientBuilder.newWSSInInterceptorConfiguration()).thenReturn(wssProps);
        when(mockMessage.getInterceptorChain()).thenReturn(mockChain);

        interceptor = new DynamicWSS4JInInterceptor(mockClientBuilder);
    }

    @Test
    @DisplayName("should configure Timestamp and Signature only when no encryption detected")
    void shouldConfigureTimestampAndSignatureOnly_whenNoEncryptionDetected() {
        // Arrange
        String soapMessage = createSoapMessage(false, false, 0);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
        verify(mockChain, times(1)).add(any(Interceptor.class));
    }

    @Test
    @DisplayName("should configure single Encryption action when one EncryptedKey found")
    void shouldConfigureSingleEncryption_whenOneEncryptedKeyFound() {
        // Arrange
        String soapMessage = createSoapMessage(true, false, 1);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " +
                                WSHandlerConstants.SIGNATURE + " " +
                                WSHandlerConstants.ENCRYPTION;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
    }

    @Test
    @DisplayName("should configure two Encryption actions when two EncryptedKeys found")
    void shouldConfigureTwoEncryptions_whenTwoEncryptedKeysFound() {
        // Arrange
        String soapMessage = createSoapMessage(true, false, 2);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " +
                                WSHandlerConstants.SIGNATURE + " " +
                                WSHandlerConstants.ENCRYPTION + " " +
                                WSHandlerConstants.ENCRYPTION;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
    }

    @Test
    @DisplayName("should configure three Encryption actions when three EncryptedKeys found")
    void shouldConfigureThreeEncryptions_whenThreeEncryptedKeysFound() {
        // Arrange
        String soapMessage = createSoapMessage(true, false, 3);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " +
                                WSHandlerConstants.SIGNATURE + " " +
                                WSHandlerConstants.ENCRYPTION + " " +
                                WSHandlerConstants.ENCRYPTION + " " +
                                WSHandlerConstants.ENCRYPTION;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("should configure N Encryption actions for N EncryptedKeys")
    void shouldConfigureNEncryptions_forNEncryptedKeys(int encryptedKeyCount) {
        // Arrange
        String soapMessage = createSoapMessage(true, false, encryptedKeyCount);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert
        String action = (String) wssProps.get(WSHandlerConstants.ACTION);
        assertThat(action).startsWith(WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE);

        // Count Encryption occurrences in action string
        int encryptionCount = countOccurrences(action, WSHandlerConstants.ENCRYPTION);
        assertThat(encryptionCount).isEqualTo(encryptedKeyCount);
    }

    @Test
    @DisplayName("should fallback to single Encryption when encryption detected but zero EncryptedKeys")
    void shouldFallbackToSingleEncryption_whenEncryptionDetectedButZeroKeys() {
        // Arrange - encryption marker present but no EncryptedKey elements
        String soapMessage = "<?xml version=\"1.0\"?>" +
                           "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                           "<soap:Header>" +
                           "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                           "<xenc:EncryptedData xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">" +
                           "<xenc:CipherData><xenc:CipherValue>test</xenc:CipherValue></xenc:CipherData>" +
                           "</xenc:EncryptedData>" +
                           "</wsse:Security>" +
                           "</soap:Header>" +
                           "<soap:Body></soap:Body>" +
                           "</soap:Envelope>";
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should fallback to 1 Encryption action
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " +
                                WSHandlerConstants.SIGNATURE + " " +
                                WSHandlerConstants.ENCRYPTION;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
    }

    @Test
    @DisplayName("should handle null InputStream gracefully")
    void shouldHandleNullInputStream_gracefully() {
        // Arrange
        when(mockMessage.getContent(InputStream.class)).thenReturn(null);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should configure no-encryption action
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
    }

    @Test
    @DisplayName("should detect attachment encryption marker")
    void shouldDetectAttachmentEncryption_whenMarkerPresent() {
        // Arrange
        String soapMessage = createSoapMessage(true, true, 2);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - attachment encryption is logged but doesn't affect action count
        // Action count should still be based on EncryptedKey count
        String action = (String) wssProps.get(WSHandlerConstants.ACTION);
        int encryptionCount = countOccurrences(action, WSHandlerConstants.ENCRYPTION);
        assertThat(encryptionCount).isEqualTo(2);
    }

    @Test
    @DisplayName("should handle overlapping EncryptedKey patterns correctly")
    void shouldCountNonOverlapping_whenOverlappingPatternsExist() {
        // Arrange - test countOccurrences with edge case (nested EncryptedKey elements)
        String xml = "<?xml version=\"1.0\"?>" +
                    "<soap:Envelope>" +
                    "<soap:Header>" +
                    "<wsse:Security>" +
                    "<xenc:EncryptedData/>" +
                    "<xenc:EncryptedKey><nested><xenc:EncryptedKey></xenc:EncryptedKey></nested></xenc:EncryptedKey>" +
                    "</wsse:Security>" +
                    "</soap:Header>" +
                    "<soap:Body/>" +
                    "</soap:Envelope>";
        mockMessageContent(xml);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should count both occurrences
        String action = (String) wssProps.get(WSHandlerConstants.ACTION);
        int encryptionCount = countOccurrences(action, WSHandlerConstants.ENCRYPTION);
        assertThat(encryptionCount).isEqualTo(2);
    }

    @Test
    @DisplayName("should not count closing tags as EncryptedKey elements")
    void shouldNotCountClosingTags_asEncryptedKeyElements() {
        // Arrange - message with closing tags but no opening tags
        String soapMessage = "<?xml version=\"1.0\"?>" +
                           "<soap:Envelope>" +
                           "</xenc:EncryptedKey>" +
                           "</xenc:EncryptedKey>" +
                           "</soap:Envelope>";
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should not detect encryption
        String expectedAction = WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE;
        assertThat(wssProps.get(WSHandlerConstants.ACTION)).isEqualTo(expectedAction);
    }

    @Test
    @DisplayName("should reset message input stream after detection")
    void shouldResetInputStream_afterDetection() {
        // Arrange
        String soapMessage = createSoapMessage(true, false, 1);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(soapMessage.getBytes());
        when(mockMessage.getContent(InputStream.class)).thenReturn(inputStream);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - verify stream was reset (setContent called with new stream)
        ArgumentCaptor<InputStream> streamCaptor = ArgumentCaptor.forClass(InputStream.class);
        verify(mockMessage).setContent(eq(InputStream.class), streamCaptor.capture());
        assertThat(streamCaptor.getValue()).isNotNull();
        assertThat(streamCaptor.getValue()).isNotSameAs(inputStream);
    }

    @Test
    @Tag("security")
    @DisplayName("should cap EncryptedKey count at security limit to prevent DoS")
    void shouldCapEncryptedKeyCount_atSecurityLimit() {
        // Arrange - simulate malicious message with 100 EncryptedKey elements
        String soapMessage = createSoapMessage(true, false, 100);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should cap at MAX_ENCRYPTED_KEYS (20)
        String action = (String) wssProps.get(WSHandlerConstants.ACTION);
        int encryptionCount = countOccurrences(action, WSHandlerConstants.ENCRYPTION);
        assertThat(encryptionCount).isEqualTo(20);
        assertThat(encryptionCount).isLessThan(100);
    }

    @Test
    @Tag("security")
    @DisplayName("should allow legitimate multi-file downloads under security limit")
    void shouldAllowLegitimateMultiFileDownloads_underSecurityLimit() {
        // Arrange - realistic MCEDT message with 10 files
        String soapMessage = createSoapMessage(true, false, 10);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should process all 10 without capping
        String action = (String) wssProps.get(WSHandlerConstants.ACTION);
        int encryptionCount = countOccurrences(action, WSHandlerConstants.ENCRYPTION);
        assertThat(encryptionCount).isEqualTo(10);
    }

    @Test
    @Tag("security")
    @DisplayName("should handle exactly at security limit boundary")
    void shouldHandleExactly_atSecurityLimitBoundary() {
        // Arrange - exactly 20 EncryptedKeys (at the limit)
        String soapMessage = createSoapMessage(true, false, 20);
        mockMessageContent(soapMessage);

        // Act
        interceptor.handleMessage(mockMessage);

        // Assert - should process all 20 without warning
        String action = (String) wssProps.get(WSHandlerConstants.ACTION);
        int encryptionCount = countOccurrences(action, WSHandlerConstants.ENCRYPTION);
        assertThat(encryptionCount).isEqualTo(20);
    }

    // Helper Methods

    /**
     * Creates a mock SOAP message with specified encryption characteristics.
     *
     * @param hasEncryption boolean whether to include EncryptedData markers
     * @param hasAttachmentEncryption boolean whether to include Attachment-Content-Only marker
     * @param encryptedKeyCount int number of EncryptedKey elements to include
     * @return String the generated SOAP XML message
     */
    private String createSoapMessage(boolean hasEncryption, boolean hasAttachmentEncryption, int encryptedKeyCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>");
        sb.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Header>");

        if (hasEncryption || encryptedKeyCount > 0) {
            sb.append("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">");

            // Add EncryptedKey elements
            for (int i = 0; i < encryptedKeyCount; i++) {
                sb.append("<xenc:EncryptedKey xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">");
                sb.append("<xenc:EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"/>");
                sb.append("<xenc:CipherData><xenc:CipherValue>test").append(i).append("</xenc:CipherValue></xenc:CipherData>");
                sb.append("</xenc:EncryptedKey>");
            }

            // Add EncryptedData if specified
            if (hasEncryption) {
                sb.append("<xenc:EncryptedData xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">");
                if (hasAttachmentEncryption) {
                    sb.append("<xenc:EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" ");
                    sb.append("Type=\"http://www.w3.org/2001/04/xmlenc#Content-Attachment-Content-Only\"/>");
                }
                sb.append("<xenc:CipherData><xenc:CipherValue>encryptedBody</xenc:CipherValue></xenc:CipherData>");
                sb.append("</xenc:EncryptedData>");
            }

            sb.append("</wsse:Security>");
        }

        sb.append("</soap:Header>");
        sb.append("<soap:Body></soap:Body>");
        sb.append("</soap:Envelope>");

        return sb.toString();
    }

    /**
     * Mocks the message content with the specified SOAP XML string.
     *
     * @param soapMessage String the SOAP message content
     */
    private void mockMessageContent(String soapMessage) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(soapMessage.getBytes());
        when(mockMessage.getContent(InputStream.class)).thenReturn(inputStream);
    }

    /**
     * Counts occurrences of a substring in a string (helper for assertions).
     *
     * @param text String the text to search
     * @param substring String the substring to count
     * @return int the number of occurrences
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
