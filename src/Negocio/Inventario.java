package Negocio;

import Modelo.Producto;
import Modelo.ProductoPerecedero;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Guarda todos los productos (en memoria) y ofrece las operaciones del sistema. */
public class Inventario {

    // La clave es el código del producto, así no se pueden repetir.
    private Map<String, Producto> productos = new LinkedHashMap<>();

    /** RF1: registrar. */
    public void registrar(Producto p) {
        if (productos.containsKey(p.getCodigo())) {
            throw new IllegalArgumentException(
                "Ya existe un producto con el código " + p.getCodigo() + ".");
        }
        productos.put(p.getCodigo(), p);
    }

    /** RF2: buscar por código. */
    public Producto buscarPorCodigo(String codigo) {
        Producto p = productos.get(codigo.trim());
        if (p == null) {
            throw new IllegalArgumentException(
                "No se encontró ningún producto con el código " + codigo.trim() + ".");
        }
        return p;
    }

    /** RF2: buscar por nombre (basta con escribir una parte). */
    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos.values()) {
            if (p.getNombre().toLowerCase().contains(texto.trim().toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /** RF3: entrada (positivo) o salida (negativo) de stock. */
    public void actualizarStock(String codigo, int cambio) {
        buscarPorCodigo(codigo).ajustarCantidad(cambio);
    }

    /** RF4: eliminar. */
    public void eliminar(String codigo) {
        Producto p = buscarPorCodigo(codigo);
        productos.remove(p.getCodigo());
    }

    /** RF5: listar. */
    public Collection<Producto> listar() {
        return productos.values();
    }

    /** RF6: productos con stock bajo. */
    public List<Producto> productosConStockBajo() {
        List<Producto> lista = new ArrayList<>();
        for (Producto p : productos.values()) {
            if (p.tieneStockBajo()) {
                lista.add(p);
            }
        }
        return lista;
    }

    /** RF7: productos vencidos. */
    public List<ProductoPerecedero> productosVencidos() {
        List<ProductoPerecedero> lista = new ArrayList<>();
        for (Producto p : productos.values()) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero pp = (ProductoPerecedero) p;
                if (pp.estaVencido()) {
                    lista.add(pp);
                }
            }
        }
        return lista;
    }

    /** RF7: productos próximos a vencer (30 días o menos). */
    public List<ProductoPerecedero> productosProximosAVencer() {
        List<ProductoPerecedero> lista = new ArrayList<>();
        for (Producto p : productos.values()) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero pp = (ProductoPerecedero) p;
                if (pp.estaProximoAVencer()) {
                    lista.add(pp);
                }
            }
        }
        return lista;
    }

    /** RF8: valor total. No pregunta de qué tipo es cada producto (polimorfismo). */
    public double valorTotal() {
        double total = 0;
        for (Producto p : productos.values()) {
            total += p.calcularValorInventario();
        }
        return total;
    }
}
