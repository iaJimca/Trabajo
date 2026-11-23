package Modelo;

import java.time.LocalDate;
import java.util.Locale;


public abstract class Producto {

    private String codigo;
    private String nombre;
    private String categoria;
    private String proveedor;
    private int cantidad;
    private int stockMinimo;
    private double precioCompra;
    private double precioVenta;
    private LocalDate fechaIngreso;

    public Producto(String codigo, String nombre, String categoria, String proveedor,
                    int cantidad, int stockMinimo, double precioCompra,
                    double precioVenta, LocalDate fechaIngreso) {
        setCodigo(codigo);
        setNombre(nombre);
        setCategoria(categoria);
        setProveedor(proveedor);
        setCantidad(cantidad);
        setStockMinimo(stockMinimo);
        setPrecioCompra(precioCompra);
        setPrecioVenta(precioVenta);
        this.fechaIngreso = fechaIngreso;
    }

    public abstract double calcularValorInventario();

    public abstract String getTipo();

    public abstract String getDetalleEspecifico();

    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }

    public void ajustarCantidad(int cambio) {
        if (cantidad + cambio < 0) {
            throw new IllegalArgumentException("No hay suficiente stock. Solo quedan " + cantidad + " unidades.");
        }
        cantidad = cantidad + cambio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        this.codigo = codigo.trim();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            this.categoria = "General";
        } else {
            this.categoria = categoria.trim();
        }
    }

    public void setProveedor(String proveedor) {
        if (proveedor == null || proveedor.trim().isEmpty()) {
            this.proveedor = "Sin proveedor";
        } else {
            this.proveedor = proveedor.trim();
        }
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cantidad;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }
        this.stockMinimo = stockMinimo;
    }

    public void setPrecioCompra(double precioCompra) {
        if (precioCompra <= 0) {
            throw new IllegalArgumentException("El precio de compra debe ser mayor que 0.");
        }
        this.precioCompra = precioCompra;
    }

    public void setPrecioVenta(double precioVenta) {
        if (precioVenta <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor que 0.");
        }
        this.precioVenta = precioVenta;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "[%s] %s | %s | cant: %d | mín: %d | compra: $%.2f | venta: $%.2f | %s",
                codigo, nombre, getTipo(), cantidad, stockMinimo,
                precioCompra, precioVenta, getDetalleEspecifico());
    }
}
