package co.edu.usbcali.notificaciones.sqs;

import java.util.List;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;

public interface IConsignacionSQS {

	public List<MensajeConsignacion> leerMensajesConsignacion()throws Exception;

}
