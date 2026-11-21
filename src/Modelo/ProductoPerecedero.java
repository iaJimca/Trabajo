package Modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Producto que sí caduca (pegamentos, siliconas, adhesivos). */
public class ProductoPerecedero extends Producto {

    /** Días antes de la caducidad en los que se avisa. */
    public static final int DIAS_ALERTA = 30;

    private LocalDate fechaCaducidad;

    public ProductoPerecedero(String codigo, String nombre, String categoria, String proveedor, int cantidad, int stockMinimo, double precioCompra, double precioVenta, LocalDate fechaIngreso, LocalDate fechaCaducidad) {
        super(codigo, nombre, categoria, proveedor, cantidad, stockMinimo, precioCompra, precioVenta, fechaIngreso);
        if (fechaCaducidad == null) {
            throw new IllegalArgumentException("La fecha de caducidad es obligatoria.");
        }
        if (!fechaCaducidad.isAfter(fechaIngreso)) {
            throw new IllegalArgumentException(
                "La fecha de caducidad debe ser posterior a la fecha de ingreso ("
                + fechaIngreso + ").");
        }
        this.fechaCaducidad = fechaCaducidad;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    /** Está vencido si la fecha de caducidad ya pasó. */
    public boolean estaVencido() {
        return fechaCaducidad.isBefore(LocalDate.now());
    }

    /** Está próximo a vencer si todavía no vence pero faltan 30 días o menos. */
    public boolean estaProximoAVencer() {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
        return !estaVencido() && dias <= DIAS_ALERTA;
    }

    /** Un producto vencido ya no se puede vender, así que vale $0. */
    @Override
    public double calcularValorInventario() {
        if (estaVencido()) {
            return 0.0;
        }
        return getCantidad() * getPrecioCompra();
    }

    @Override
    public String getTipo() {
        return "Perecedero";
    }

    @Override
    public String getDetalleEspecifico() {
        String texto = "Caduca: " + fechaCaducidad;
        if (estaVencido()) {
            texto += " (VENCIDO)";
        }
        return texto;
    }
}
