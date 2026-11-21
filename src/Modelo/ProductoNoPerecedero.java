package Modelo;

import java.time.LocalDate;

/** Producto que no caduca (tornillos, cables, herramientas). */
public class ProductoNoPerecedero extends Producto {

    public ProductoNoPerecedero(String codigo, String nombre, String categoria, String proveedor, int cantidad, int stockMinimo, double precioCompra, double precioVenta, LocalDate fechaIngreso) {
        super(codigo, nombre, categoria, proveedor, cantidad, stockMinimo, precioCompra, precioVenta, fechaIngreso);
    }

    @Override
    public double calcularValorInventario() {
        return getCantidad() * getPrecioCompra();
    }

    @Override
    public String getTipo() {
        return "No perecedero";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Sin caducidad";
    }
}
