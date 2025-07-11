package org.oscarehr.integration.ebs.client.ng;

import java.io.IOException;
import java.util.Map;

import org.apache.cxf.attachment.LazyAttachmentCollection;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * A CXF interceptor that processes incoming attachments and message properties after a message has been invoked.
 * 
 * <h3>Purpose</h3>
 * 
 * The {@code DownloadInInterceptor} is designed to handle attachments after the main invocation phase 
 * of a message, typically used in scenarios where the client downloads and processes attachments 
 * from a response message.
 * 
 * <h3>Usage</h3>
 * 
 * This interceptor operates in the {@link Phase#POST_INVOKE} phase, meaning it processes the 
 * message after it has been fully invoked. It retrieves and logs all attachments from the message 
 * and prints out all key-value pairs from the message's internal map.
 * 
 * <h3>Implementation notes</h3>
 * 
 * - The attachments are accessed via the {@link LazyAttachmentCollection}, which allows the 
 *   attachments to be loaded lazily (i.e., only when needed).
 * - It prints the attachment ID and content to the standard output.
 * - If an {@link IOException} occurs when reading the attachment data, the exception is swallowed 
 *   and logged as part of the message processing.
 */
public class DownloadInInterceptor extends AbstractPhaseInterceptor<Message> {

	/**
	 * Constructs a new {@code DownloadInInterceptor} and sets it to operate in the POST_INVOKE phase.
	 */
	public DownloadInInterceptor() {
		super(Phase.POST_INVOKE);
	}

	/**
	 * Handles the incoming message by processing its attachments and logging message properties.
	 * 
	 * @param message The incoming {@link Message} to be processed.
	 * @throws Fault If there is a problem during message processing.
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
		LazyAttachmentCollection attachments = (LazyAttachmentCollection) 
				message.get(org.apache.cxf.message.Message.ATTACHMENTS);
		if (attachments != null) {
			for(Attachment a : attachments.getLoadedAttachments()) {
				System.out.println(a.getId());
				try {
					System.out.println(IOUtils.readStringFromStream(a.getDataHandler().getInputStream()));
				} catch (IOException e) {
					// swallow
				}
			}
		}
		
		for(Map.Entry<String, Object> entry : message.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
	}

}
