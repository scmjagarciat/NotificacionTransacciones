/**
 * 
 */
package co.edu.usbcali.notificaciones.dynamo.db.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;
import co.edu.usbcali.notificaciones.dto.MensajeRetiro;
import co.edu.usbcali.notificaciones.dynamo.db.IConsignacionDynamoDB;
import co.edu.usbcali.notificaciones.dynamo.db.IRetiroDynamoDB;
import co.edu.usbcali.notificaciones.sqs.test.ConsignacionSQSTest;

/**
 * @author jhoanalejandro
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/appContext.xml")

public class RetiroDynamoDBTest {
	private static final Logger log=LoggerFactory.getLogger(RetiroDynamoDBTest.class);

	@Autowired
	private IRetiroDynamoDB retiroDynamoDB;

	@Test
	public void test() {
		MensajeRetiro mensaje = new MensajeRetiro();
		mensaje.setNumeroCuenta("123-3459-3");
		mensaje.setNumeroRetiro(1221398L);
		mensaje.setDescripcionRetiro("retiro_prueba");
		mensaje.setFechaRetiro("2016-07-20");
		mensaje.setValorRetiro(new BigDecimal(100350.45));
		
		try{
		List<MensajeRetiro> mensajesRetiros = new ArrayList();
		mensajesRetiros.add(mensaje);
		retiroDynamoDB.grabarMensajesRetiro(mensajesRetiros);
		}catch (Exception e)
		{
			log.debug("Error :"+e.toString());	
		}

	}

}
