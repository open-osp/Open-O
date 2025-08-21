//CHECKSTYLE:OFF
package org.apache.xml.security.encryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.parser.XMLParserException;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Converts <code>String</code>s into <code>Node</code>s and visa versa.
 */
public class DocumentSerializer extends AbstractSerializer {

    public DocumentSerializer(boolean secureValidation) throws InvalidCanonicalizerException {
        this(Canonicalizer.ALGO_ID_C14N_PHYSICAL, secureValidation);
    }

    public DocumentSerializer(String canonAlg, boolean secureValidation) throws InvalidCanonicalizerException {
        super(canonAlg, secureValidation);
    }

    /**
     * @param source
     * @param ctx
     * @return the Node resulting from the parse of the source
     * @throws XMLEncryptionException
     */
    public Node deserialize(byte[] source, Node ctx) throws XMLEncryptionException, IOException {
        try {
            return attemptDeserialize(source, ctx);
        } catch (Exception e) {
            return attemptDeserialize(Base64.encodeBase64(source), ctx);
        }
    }

    private Node attemptDeserialize(byte[] source, Node ctx) throws XMLEncryptionException, IOException {
        byte[] fragment = createContext(source, ctx);
        try (InputStream is = new ByteArrayInputStream(fragment)) {
            return deserialize(ctx, is);
        }
    }

    /**
     * @param source
     * @param ctx
     * @return the Node resulting from the parse of the source
     * @throws XMLEncryptionException
     */
    public Node deserialize(String source, Node ctx) throws XMLEncryptionException, IOException {
        byte[] fragment = createContext(source.getBytes(), ctx);
        try (InputStream is = new ByteArrayInputStream(fragment)) {
            return deserialize(ctx, is);
        }
    }

    /**
     * @param ctx
     * @param inputSource
     * @return the Node resulting from the parse of the source
     * @throws XMLEncryptionException
     */
     private Node deserialize(Node ctx, InputStream inputStream) throws XMLEncryptionException {
        try {
            Document d = XMLUtils.read(inputStream, secureValidation);

            Document contextDocument = null;
            if (Node.DOCUMENT_NODE == ctx.getNodeType()) {
                contextDocument = (Document)ctx;
            } else {
                contextDocument = ctx.getOwnerDocument();
            }

            Element fragElt =
                    (Element) contextDocument.importNode(d.getDocumentElement(), true);
            DocumentFragment result = contextDocument.createDocumentFragment();
            Node child = fragElt.getFirstChild();
            while (child != null) {
                fragElt.removeChild(child);
                result.appendChild(child);
                child = fragElt.getFirstChild();
            }
            return result;
        } catch (XMLParserException pce) {
            throw new XMLEncryptionException(pce);
        }
    }

}
