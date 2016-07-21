package co.edu.usbcali.notificaciones.logica;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;
import co.edu.usbcali.notificaciones.dto.MensajeRetiro;
import co.edu.usbcali.notificaciones.dynamo.db.IConsignacionDynamoDB;
import co.edu.usbcali.notificaciones.dynamo.db.IRetiroDynamoDB;
import co.edu.usbcali.notificaciones.sqs.IConsignacionSQS;
import co.edu.usbcali.notificaciones.sqs.IRetiroSQS;

@Service
@Scope("singleton")
public class NotificacionTransacciones implements INotificacionTransacciones {

	private static final Logger log = LoggerFactory.getLogger(NotificacionTransacciones.class);

	@Autowired
	private IConsignacionSQS consignacionSQS;

	@Autowired
	private IConsignacionDynamoDB consignacionDynamoDB;

	@Autowired
	private IRetiroSQS retiroSQS;

	@Autowired
	private IRetiroDynamoDB retiroDynamoDB;

	public void notificarMensajesConsignacion() throws Exception {
		List<MensajeConsignacion> mensajesConsignaciones;
		mensajesConsignaciones = consignacionSQS.leerMensajesConsignacion();
		consignacionDynamoDB.grabarMensajesConsignacion(mensajesConsignaciones);
	}

	@Override
	public void notificarMensajesRetiro() throws Exception {
		List<MensajeRetiro> mensajesRetiros;
		mensajesRetiros = retiroSQS.leerMensajesRetiro();
		retiroDynamoDB.grabarMensajesRetiro(mensajesRetiros);

	}

	@Override
	public ArrayList lecturaMensajesConsignacion(String numeroCuenta) throws Exception {
		return consignacionDynamoDB.consultarMensajesConsignacion(numeroCuenta);
	}

	@Override
	public ArrayList lecturaMensajesRetiro(String numeroCuenta) throws Exception {
		return retiroDynamoDB.consultarMensajesRetiro(numeroCuenta);
	}

}
