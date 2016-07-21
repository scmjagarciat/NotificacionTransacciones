package co.edu.usbcali.notificaciones.dynamo.db;

import java.util.ArrayList;
import java.util.List;

import co.edu.usbcali.notificaciones.dto.MensajeConsignacion;

public interface IConsignacionDynamoDB {
	public void grabarMensajesConsignacion(List<MensajeConsignacion> consignaciones)throws Exception;
	public ArrayList consultarMensajesConsignacion(String numeroCuenta) throws Exception;
}
