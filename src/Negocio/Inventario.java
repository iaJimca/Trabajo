package Negocio;

import Modelo.Producto;
import Modelo.ProductoPerecedero;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private List<Producto> productos = new ArrayList<>();

    public void registrar(Producto p) {
        if (buscarPorCodigo(p.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código " + p.getCodigo() + ".");
        }
        productos.add(p);
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo.trim())) {
                return p;
            }
        }
        return null;
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> encontrados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(texto.trim().toLowerCase())) {
                encontrados.add(p);
            }
        }
        return encontrados;
    }

    public void actualizarStock(String codigo, int cambio) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            throw new IllegalArgumentException("No se encontró ningún producto con el código " + codigo.trim() + ".");
        }
        p.ajustarCantidad(cambio);
    }

    public void eliminar(String codigo) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            throw new IllegalArgumentException("No se encontró ningún producto con el código " + codigo.trim() + ".");
        }
        productos.remove(p);
    }

    public List<Producto> listar() {
        return productos;
    }

    public List<Producto> productosConStockBajo() {
        List<Producto> lista = new ArrayList<>();
        for (Producto p : productos) {
            if (p.tieneStockBajo()) {
                lista.add(p);
            }
        }
        return lista;
    }

    public List<ProductoPerecedero> productosVencidos() {
        List<ProductoPerecedero> lista = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero pp = (ProductoPerecedero) p;
                if (pp.estaVencido()) {
                    lista.add(pp);
                }
            }
        }
        return lista;
    }

    public List<ProductoPerecedero> productosProximosAVencer() {
        List<ProductoPerecedero> lista = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero pp = (ProductoPerecedero) p;
                if (pp.estaProximoAVencer()) {
                    lista.add(pp);
                }
            }
        }
        return lista;
    }

    public double valorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total = total + p.calcularValorInventario();
        }
        return total;
    }
}
