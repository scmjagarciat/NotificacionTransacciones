package co.edu.usbcali.notificaciones.logica.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;
import co.edu.usbcali.notificaciones.dto.MensajeRetiro;
import co.edu.usbcali.notificaciones.logica.INotificacionTransacciones;
import co.edu.usbcali.notificaciones.sqs.IConsignacionSQS;
import co.edu.usbcali.notificaciones.sqs.test.ConsignacionSQSTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/appContext.xml")

public class NotificacionTransaccionesTest {

	private static final Logger log = LoggerFactory.getLogger(NotificacionTransaccionesTest.class);

	@Autowired
	private INotificacionTransacciones notificacionTransacciones;

	@Test
	public void atest() {
		try {
			notificacionTransacciones.notificarMensajesConsignacion();
			log.debug("Inserto mensajes de consignacion en DynamoDB");
		} catch (Exception e) {

			log.debug("Error en inserción mensajes de consignacion en DynamoDB " + e.toString());
		}

	}

	@Test
	public void btest() {
		try {
			String numeroCuenta = "4008-5305-0040";
			ArrayList<MensajeConsignacion> mensajesConsignaciones = notificacionTransacciones.lecturaMensajesConsignacion(numeroCuenta);
			for(MensajeConsignacion mensajeConsignacion : mensajesConsignaciones)
				System.out.println(mensajeConsignacion.toString()+"\n");
			
			
		} catch (Exception e) {

			log.debug("Error en consulta de mensajes de consignacion en DynamoDB " + e.toString());
		}

	}
	@Test
	public void ctest() {
		try {
			notificacionTransacciones.notificarMensajesRetiro();
			log.debug("Inserto mensajes de Retiros en DynamoDB");
		} catch (Exception e) {

			log.debug("Error en inserción mensajes de retiros en DynamoDB " + e.toString());
		}

	}

	@Test
	public void dtest() {
		try {
			String numeroCuenta = "4008-5305-0020";
			ArrayList<MensajeRetiro> mensajesRetiros = notificacionTransacciones.lecturaMensajesRetiro(numeroCuenta);
			for(MensajeRetiro mensajeRetiro : mensajesRetiros)
				System.out.println(mensajeRetiro.toString()+"\n");
			
			
		} catch (Exception e) {

			log.debug("Error en consulta de mensajes de consignacion en DynamoDB " + e.toString());
		}

	}

}
