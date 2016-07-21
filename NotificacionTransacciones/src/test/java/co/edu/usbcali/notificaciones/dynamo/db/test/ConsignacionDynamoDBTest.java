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
import co.edu.usbcali.notificaciones.dynamo.db.IConsignacionDynamoDB;
import co.edu.usbcali.notificaciones.sqs.IConsignacionSQS;
import co.edu.usbcali.notificaciones.sqs.test.ConsignacionSQSTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/appContext.xml")

public class ConsignacionDynamoDBTest {
	

	private static final Logger log=LoggerFactory.getLogger(ConsignacionSQSTest.class);

	@Autowired
	private IConsignacionDynamoDB consignacionDynamoDB;
	
	@Test
	public void atest() {
		MensajeConsignacion mensaje = new MensajeConsignacion();
		mensaje.setNumeroCuenta("123-3459-3");
		mensaje.setNumeroConsignacion(1221398L);
		mensaje.setDescripcionConsignacion("consignacion_prueba");
		mensaje.setFechaConsignacion("2016-07-20");
		mensaje.setValorConsignacion(new BigDecimal(10050));
		
		try{
		List<MensajeConsignacion> mensajesConsignaciones = new ArrayList();
		mensajesConsignaciones.add(mensaje);
		consignacionDynamoDB.grabarMensajesConsignacion(mensajesConsignaciones);
		}catch (Exception e)
		{
			log.debug("Error :"+e.toString());	
		}

	}

	@Test
	public void btest() {
		
		try{
		String numeroCuenta="123-3459-3";
		ArrayList<MensajeConsignacion> resultados = consignacionDynamoDB.consultarMensajesConsignacion(numeroCuenta);
		for(MensajeConsignacion mensajeConsignacion: resultados)
		System.out.println(mensajeConsignacion.toString()+"\n");
		}catch (Exception e)
		{
			log.debug("Error :"+e.toString());	
		}

	}

}
