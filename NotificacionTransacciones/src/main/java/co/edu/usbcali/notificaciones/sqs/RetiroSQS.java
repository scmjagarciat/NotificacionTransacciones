package co.edu.usbcali.notificaciones.sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.usbcali.notificaciones.dto.MensajeRetiro;

@Service
@Scope("singleton")
public class RetiroSQS implements IRetiroSQS {
	private static final Logger log = LoggerFactory.getLogger(RetiroSQS.class);
	private List<MensajeRetiro> mensajesRetiros;
	@Autowired
	String queueRetiro;

	@Override
	public List<MensajeRetiro> leerMensajesRetiro() throws Exception {
		String qurl = queueRetiro.toString();
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/.aws/credentials), and is in valid format.", e);
		}

		AmazonSQS sqs = new AmazonSQSClient(credentials);

		log.debug("Getting Started with Amazon SQS");

		try {

			// Receive messages
			log.debug("Receiving messages from MyQueue.\n");

			List<Message> messages = null;
			mensajesRetiros = new ArrayList();
			while ((messages = sqs.receiveMessage(new ReceiveMessageRequest(qurl)).getMessages()).size() > 0) {
				for (Message message : messages) {
					MensajeRetiro mensajeRetiro = null;
					log.debug("  Message");
					log.debug("    MessageId:     " + message.getMessageId());
					log.debug("    ReceiptHandle: " + message.getReceiptHandle());
					log.debug("    MD5OfBody:     " + message.getMD5OfBody());
					log.debug("    Body:          " + message.getBody());
					for (Entry<String, String> entry : message.getAttributes().entrySet()) {
						log.debug("  Attribute");
						log.debug("    Name:  " + entry.getKey());
						log.debug("    Value: " + entry.getValue());
					}
					/* Convertir JSON a Objeto */
					try {
						ObjectMapper mapper = new ObjectMapper();
						mensajeRetiro = mapper.readValue(message.getBody(), MensajeRetiro.class);

					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					}
					// Adicionar objeto mensaje a list
					log.debug("Adicionando en Arreglo" + mensajeRetiro.getNumeroRetiro());

					mensajesRetiros.add(mensajeRetiro);
					// Delete a message
					log.debug("Deleting a message.\n");
					String messageRecieptHandle = message.getReceiptHandle();
					sqs.deleteMessage(new DeleteMessageRequest(qurl, messageRecieptHandle));

				}

			}
		} catch (AmazonServiceException ase) {
			log.debug("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			log.debug("Error Message:    " + ase.getMessage());
			log.debug("HTTP Status Code: " + ase.getStatusCode());
			log.debug("AWS Error Code:   " + ase.getErrorCode());
			log.debug("Error Type:       " + ase.getErrorType());
			log.debug("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not "
					+ "being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
		return mensajesRetiros;

	}

}
