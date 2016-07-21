package co.edu.usbcali.notificaciones.sqs.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;
import co.edu.usbcali.notificaciones.sqs.IConsignacionSQS;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/appContext.xml")
public class ConsignacionSQSTest {

	private static final Logger log = LoggerFactory.getLogger(ConsignacionSQSTest.class);

	@Autowired
	private IConsignacionSQS consignacionSQS;

	@Test
	public void test() {
		List<MensajeConsignacion> mensajesConsignaciones;
		try {
			mensajesConsignaciones = consignacionSQS.leerMensajesConsignacion();
			log.debug("Lectura exitosa de Cola");
			log.debug("Cantidad de objetos en la cola "+mensajesConsignaciones.size());
			System.out.println("Cantidad de objetos en la cola "+mensajesConsignaciones.size());
		} catch (Exception e) {
			log.debug("Error en Lectura de Cola:" + e.toString());
		}
	}

}
