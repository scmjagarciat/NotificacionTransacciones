package co.edu.usbcali.notificaciones.dynamo.db;

import java.util.ArrayList;
import java.util.List;

import co.edu.usbcali.notificaciones.dto.MensajeRetiro;

public interface IRetiroDynamoDB {
	public void grabarMensajesRetiro(List<MensajeRetiro> retiros)throws Exception;
	public ArrayList consultarMensajesRetiro(String numeroCuenta) throws Exception;

}
