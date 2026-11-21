package Modelo;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Clase abstracta: tiene los datos y métodos que todos los productos comparten.
 * Los atributos son privados y los setters revisan que los datos sean válidos
 * (encapsulamiento).
 */
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

    // ---------- Métodos abstractos (cada hija los hace a su manera) ----------

    /** Dinero que vale este producto en bodega. */
    public abstract double calcularValorInventario();

    /** "Perecedero" o "No perecedero". */
    public abstract String getTipo();

    /** Texto sobre la caducidad. */
    public abstract String getDetalleEspecifico();

    // ---------- Métodos comunes ----------

    /** Hay stock bajo cuando la cantidad es menor o igual al mínimo. */
    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }

    /** Suma (entrada) o resta (salida) unidades al stock. */
    public void ajustarCantidad(int cambio) {
        if (cantidad + cambio < 0) {
            throw new IllegalArgumentException(
                "No hay suficiente stock. Solo quedan " + cantidad + " unidades.");
        }
        cantidad += cambio;
    }

    // ---------- Getters ----------

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getProveedor() { return proveedor; }
    public int getCantidad() { return cantidad; }
    public int getStockMinimo() { return stockMinimo; }
    public double getPrecioCompra() { return precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    // ---------- Setters con validación ----------

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        this.codigo = codigo.trim();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void setCategoria(String categoria) {
        this.categoria = (categoria == null || categoria.isBlank()) ? "General" : categoria.trim();
    }

    public void setProveedor(String proveedor) {
        this.proveedor = (proveedor == null || proveedor.isBlank()) ? "Sin proveedor" : proveedor.trim();
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
