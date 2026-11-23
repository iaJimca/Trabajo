package Interfaz;

import Modelo.Producto;
import Modelo.ProductoNoPerecedero;
import Modelo.ProductoPerecedero;
import Negocio.Inventario;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static Inventario inventario = new Inventario();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            try {
                switch (opcion) {
                    case 1:
                        registrarProducto();
                        break;
                    case 2:
                        buscarProducto();
                        break;
                    case 3:
                        actualizarStock();
                        break;
                    case 4:
                        eliminarProducto();
                        break;
                    case 5:
                        listarProductos();
                        break;
                    case 6:
                        mostrarStockBajo();
                        break;
                    case 7:
                        mostrarVencimientos();
                        break;
                    case 8:
                        mostrarValorTotal();
                        break;
                    case 0:
                        System.out.println("Hasta pronto.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR -> " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== FERRETERÍA EL TORNILLO - INVENTARIO ===");
        System.out.println("1. Registrar producto");
        System.out.println("2. Buscar producto");
        System.out.println("3. Actualizar stock (entrada/salida)");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Listar inventario");
        System.out.println("6. Productos con stock bajo");
        System.out.println("7. Productos vencidos / próximos a vencer");
        System.out.println("8. Valor total del inventario");
        System.out.println("0. Salir");
        System.out.print("Elija una opción: ");
    }

    private static void registrarProducto() {
        String codigo = pedir("Código: ");
        String nombre = pedir("Nombre: ");
        String categoria = pedir("Categoría (Enter = General): ");
        String proveedor = pedir("Proveedor (Enter = Sin proveedor): ");

        String txtCantidad = pedir("Cantidad: ");
        if (txtCantidad.isBlank()) {
            throw new IllegalArgumentException("La cantidad es obligatoria.");
        }
        int cantidad = aEntero(txtCantidad, "La cantidad");

        String txtMinimo = pedir("Stock mínimo (Enter = 0): ");
        int stockMinimo = 0;
        if (!txtMinimo.isBlank()) {
            stockMinimo = aEntero(txtMinimo, "El stock mínimo");
        }

        String txtCompra = pedir("Precio de compra: ");
        if (txtCompra.isBlank()) {
            throw new IllegalArgumentException("El precio de compra es obligatorio.");
        }
        double compra = aDecimal(txtCompra, "El precio de compra");

        String txtVenta = pedir("Precio de venta: ");
        if (txtVenta.isBlank()) {
            throw new IllegalArgumentException("El precio de venta es obligatorio.");
        }
        double venta = aDecimal(txtVenta, "El precio de venta");

        String txtIngreso = pedir("Fecha de ingreso AAAA-MM-DD (Enter = hoy): ");
        LocalDate fechaIngreso = LocalDate.now();
        if (!txtIngreso.isBlank()) {
            fechaIngreso = aFecha(txtIngreso);
        }

        String esPerecedero = pedir("¿Es perecedero? (s/n): ");

        Producto p;
        if (esPerecedero.equalsIgnoreCase("s")) {
            String txtCaducidad = pedir("Fecha de caducidad (AAAA-MM-DD): ");
            if (txtCaducidad.isBlank()) {
                throw new IllegalArgumentException("La fecha de caducidad es obligatoria.");
            }
            LocalDate caducidad = aFecha(txtCaducidad);
            p = new ProductoPerecedero(codigo, nombre, categoria, proveedor,
                    cantidad, stockMinimo, compra, venta, fechaIngreso, caducidad);
        } else {
            p = new ProductoNoPerecedero(codigo, nombre, categoria, proveedor,
                    cantidad, stockMinimo, compra, venta, fechaIngreso);
        }

        inventario.registrar(p);
        System.out.println("Producto registrado: " + p);
    }

    private static void buscarProducto() {
        String texto = pedir("Código o nombre a buscar: ");
        if (texto.isBlank()) {
            throw new IllegalArgumentException("Escriba un código o un nombre.");
        }

        // Primero busca por código y si no lo encuentra busca por nombre
        Producto p = inventario.buscarPorCodigo(texto);
        if (p != null) {
            System.out.println(p);
        } else {
            List<Producto> encontrados = inventario.buscarPorNombre(texto);
            if (encontrados.isEmpty()) {
                throw new IllegalArgumentException("No se encontró ningún producto con el código " + texto.trim() + ".");
            }
            for (Producto producto : encontrados) {
                System.out.println(producto);
            }
        }
    }

    private static void actualizarStock() {
        String codigo = pedir("Código del producto: ");
        String txtCambio = pedir("Unidades (positivo = entrada, negativo = salida): ");
        if (txtCambio.isBlank()) {
            throw new IllegalArgumentException("La cantidad es obligatoria.");
        }
        int cambio = aEntero(txtCambio, "La cantidad");

        inventario.actualizarStock(codigo, cambio);

        Producto p = inventario.buscarPorCodigo(codigo);
        System.out.println("Stock actual = " + p.getCantidad());
        if (p.tieneStockBajo()) {
            System.out.println("ALERTA: " + p.getNombre() + " tiene stock bajo.");
        }
    }

    private static void eliminarProducto() {
        String codigo = pedir("Código del producto a eliminar: ");
        inventario.eliminar(codigo);
        System.out.println("Producto " + codigo.trim() + " eliminado. Total de productos: "
                + inventario.listar().size());
    }

    private static void listarProductos() {
        if (inventario.listar().isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println(inventario.listar().size() + " producto(s) en inventario:");
        for (Producto p : inventario.listar()) {
            System.out.println(" " + p);
        }
    }

    private static void mostrarStockBajo() {
        System.out.println("Productos con stock bajo:");
        List<Producto> lista = inventario.productosConStockBajo();
        if (lista.isEmpty()) {
            System.out.println(" (ninguno)");
        }
        for (Producto p : lista) {
            System.out.println(" " + p);
        }
    }

    private static void mostrarVencimientos() {
        System.out.println("VENCIDOS:");
        List<ProductoPerecedero> vencidos = inventario.productosVencidos();
        if (vencidos.isEmpty()) {
            System.out.println(" (ninguno)");
        }
        for (ProductoPerecedero p : vencidos) {
            System.out.println(" " + p);
        }

        System.out.println("PRÓXIMOS A VENCER (" + ProductoPerecedero.DIAS_ALERTA + " días o menos):");
        List<ProductoPerecedero> proximos = inventario.productosProximosAVencer();
        if (proximos.isEmpty()) {
            System.out.println(" (ninguno)");
        }
        for (ProductoPerecedero p : proximos) {
            System.out.println(" " + p);
        }
    }

    private static void mostrarValorTotal() {
        System.out.println(String.format(Locale.US, "Valor total del inventario = $%.2f", inventario.valorTotal()));
        for (Producto p : inventario.listar()) {
            System.out.println(String.format(Locale.US, " %s (%s) -> $%.2f",
                    p.getNombre(), p.getTipo(), p.calcularValorInventario()));
        }
    }

    private static String pedir(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int aEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " debe ser un número entero.");
        }
    }

    private static double aDecimal(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " debe ser un número.");
        }
    }

    private static LocalDate aFecha(String texto) {
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha debe tener el formato AAAA-MM-DD.");
        }
    }
}
