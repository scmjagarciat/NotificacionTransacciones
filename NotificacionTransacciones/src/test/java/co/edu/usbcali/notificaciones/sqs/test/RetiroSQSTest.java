/**
 * 
 */
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
import co.edu.usbcali.notificaciones.dto.MensajeRetiro;
import co.edu.usbcali.notificaciones.sqs.IRetiroSQS;

/**
 * @author jhoanalejandro
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/appContext.xml")

public class RetiroSQSTest {
	private static final Logger log = LoggerFactory.getLogger(RetiroSQSTest.class);

	@Autowired
	private IRetiroSQS retiroSQS;


	@Test
	public void test() {
		List<MensajeRetiro> mensajesRetiros;
		try {
			mensajesRetiros = retiroSQS.leerMensajesRetiro();
			log.debug("Lectura exitosa de Cola");
			log.debug("Cantidad de objetos en la cola "+mensajesRetiros.size());
			System.out.println("Cantidad de objetos en la cola "+mensajesRetiros.size());
		} catch (Exception e) {
			log.debug("Error en Lectura de Cola:" + e.toString());
		}
	}

}
