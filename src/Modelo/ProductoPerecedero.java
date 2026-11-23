package Modelo;

import java.time.LocalDate;

public class ProductoPerecedero extends Producto {

    public static final int DIAS_ALERTA = 30;

    private LocalDate fechaCaducidad;

    public ProductoPerecedero(String codigo, String nombre, String categoria, String proveedor,
                              int cantidad, int stockMinimo, double precioCompra,
                              double precioVenta, LocalDate fechaIngreso, LocalDate fechaCaducidad) {
        super(codigo, nombre, categoria, proveedor, cantidad, stockMinimo,
                precioCompra, precioVenta, fechaIngreso);
        if (fechaCaducidad == null) {
            throw new IllegalArgumentException("La fecha de caducidad es obligatoria.");
        }
        if (!fechaCaducidad.isAfter(fechaIngreso)) {
            throw new IllegalArgumentException("La fecha de caducidad debe ser posterior a la fecha de ingreso (" + fechaIngreso + ").");
        }
        this.fechaCaducidad = fechaCaducidad;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public boolean estaVencido() {
        return fechaCaducidad.isBefore(LocalDate.now());
    }

    public boolean estaProximoAVencer() {
        LocalDate limite = LocalDate.now().plusDays(DIAS_ALERTA);
        return !estaVencido() && !fechaCaducidad.isAfter(limite);
    }

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
            texto = texto + " (VENCIDO)";
        }
        return texto;
    }
}
