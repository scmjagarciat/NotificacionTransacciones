package co.edu.usbcali.notificaciones.dynamo.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;

@Service
@Scope("singleton")
public class ConsignacionDynamoDB implements IConsignacionDynamoDB {

	private static final Logger log = LoggerFactory.getLogger(ConsignacionDynamoDB.class);
	private AmazonDynamoDBClient dynamoDB;

	@Override
	public void grabarMensajesConsignacion(List<MensajeConsignacion> consignaciones) throws Exception {
		conectarDynamoDB();
		try {

			// Create a table with a primary hash key named
			// 'numeroCuenta'
			// 'numeroConsignacion'
			String tableName = "consignaciones";
			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
					.withKeySchema(new KeySchemaElement().withAttributeName("numeroCuenta").withKeyType(KeyType.HASH),
							new KeySchemaElement().withAttributeName("numeroConsignacion").withKeyType(KeyType.RANGE))
					.withAttributeDefinitions(
							new AttributeDefinition().withAttributeName("numeroConsignacion")
									.withAttributeType(ScalarAttributeType.S),
							new AttributeDefinition().withAttributeName("numeroCuenta")
									.withAttributeType(ScalarAttributeType.S))
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
			// Create table if it does not exist yet
			TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
			// wait for the table to move into ACTIVE state
			TableUtils.waitUntilActive(dynamoDB, tableName);
			// Describe our new table
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
			TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
			log.debug("Table Description: " + tableDescription);
			// Add an items
			for (MensajeConsignacion mensaje : consignaciones) {
				Map<String, AttributeValue> item = crearRegistro(mensaje);
				PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
				PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
				log.debug("Result: " + putItemResult);
			}
		} catch (AmazonServiceException ase) {
			log.debug("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			log.debug("Error Message:    " + ase.getMessage());
			log.debug("HTTP Status Code: " + ase.getStatusCode());
			log.debug("AWS Error Code:   " + ase.getErrorCode());
			log.debug("Error Type:       " + ase.getErrorType());
			log.debug("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}

	}

	private void conectarDynamoDB() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}
		dynamoDB = new AmazonDynamoDBClient(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);
	}

	private Map<String, AttributeValue> crearRegistro(MensajeConsignacion mensaje) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("numeroConsignacion", new AttributeValue("" + mensaje.getNumeroConsignacion()));
		item.put("numeroCuenta", new AttributeValue(mensaje.getNumeroCuenta()));
		item.put("fecha", new AttributeValue(mensaje.getFechaConsignacion()));
		item.put("descripcion", new AttributeValue(mensaje.getDescripcionConsignacion()));
		item.put("valor", new AttributeValue("" + mensaje.getValorConsignacion()));
		return item;
	}

	@Override
	public ArrayList consultarMensajesConsignacion(String numeroCuenta) throws Exception {

		conectarDynamoDB();
		ArrayList mensajesConsignacion =new ArrayList();
		ScanResult scanResult = null;
		try {
			String tableName = "consignaciones";
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS(numeroCuenta));
			scanFilter.put("numeroCuenta", condition);
			ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
			scanResult = dynamoDB.scan(scanRequest);
			log.debug("Result: " + scanResult);
		} catch (AmazonServiceException ase) {
			log.debug("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			log.debug("Error Message:    " + ase.getMessage());
			log.debug("HTTP Status Code: " + ase.getStatusCode());
			log.debug("AWS Error Code:   " + ase.getErrorCode());
			log.debug("Error Type:       " + ase.getErrorType());
			log.debug("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
		for (Map<String, AttributeValue> item : scanResult.getItems()){
			MensajeConsignacion mensajeConsignacion = new MensajeConsignacion();
			mensajeConsignacion.setNumeroCuenta(item.get("numeroCuenta").getS());
			mensajeConsignacion.setNumeroConsignacion(new Long(item.get("numeroConsignacion").getS()));
			mensajeConsignacion.setFechaConsignacion(item.get("fecha").getS());
			mensajeConsignacion.setDescripcionConsignacion(item.get("descripcion").getS());
			mensajeConsignacion.setValorConsignacion(new BigDecimal(item.get("valor").getS()));
			mensajesConsignacion.add(mensajeConsignacion);
		}

		return mensajesConsignacion;

	}

}
