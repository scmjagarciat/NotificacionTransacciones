package co.edu.usbcali.notificaciones.logica;

import java.util.ArrayList;

public interface INotificacionTransacciones {

	public void notificarMensajesConsignacion() throws Exception;
	public void notificarMensajesRetiro() throws Exception;
	public ArrayList lecturaMensajesConsignacion(String numeroCuenta) throws Exception;
	public ArrayList lecturaMensajesRetiro(String numeroCuenta) throws Exception;
}
