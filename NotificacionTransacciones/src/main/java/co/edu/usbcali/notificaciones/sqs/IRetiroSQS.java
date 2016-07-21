package co.edu.usbcali.notificaciones.sqs;

import java.util.List;

import co.edu.usbcali.notificaciones.dto.MensajeRetiro;


public interface IRetiroSQS {
	public List<MensajeRetiro> leerMensajesRetiro()throws Exception;

}
