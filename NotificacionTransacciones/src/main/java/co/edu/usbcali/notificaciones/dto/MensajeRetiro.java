package co.edu.usbcali.notificaciones.dto;

import java.math.BigDecimal;

public class MensajeRetiro {
	private long numeroRetiro;
	private String numeroCuenta;
	private BigDecimal valorRetiro;
	private String fechaRetiro;
	private String descripcionRetiro;

	public long getNumeroRetiro() {
		return numeroRetiro;
	}

	public void setNumeroRetiro(long numeroRetiro) {
		this.numeroRetiro = numeroRetiro;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public BigDecimal getValorRetiro() {
		return valorRetiro;
	}

	public void setValorRetiro(BigDecimal valorRetiro) {
		this.valorRetiro = valorRetiro;
	}

	public String getDescripcionRetiro() {
		return descripcionRetiro;
	}

	public void setDescripcionRetiro(String descripcionRetiro) {
		this.descripcionRetiro = descripcionRetiro;
	}

	public String getFechaRetiro() {
		return fechaRetiro;
	}

	public void setFechaRetiro(String fechaRetiro) {
		this.fechaRetiro = fechaRetiro;
	}
	public String toString() {

		return "Numero Cuenta: " + this.getNumeroCuenta() + " Numero Retiro: " + this.getNumeroRetiro()
				+ " Fecha: " + this.getFechaRetiro() + " Descripcion: " + this.getDescripcionRetiro()
				+ " Valor: " + this.getValorRetiro();

	}
}
