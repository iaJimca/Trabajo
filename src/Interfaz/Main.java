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

/** Menú por consola. Solo pide datos y muestra mensajes; el resto lo hace Inventario. */
public class Main {

    private static Inventario inventario = new Inventario();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            String entrada = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            // Si algo sale mal, el modelo lanza IllegalArgumentException
            // y aquí se muestra el mensaje sin modificar el inventario.
            try {
                switch (opcion) {
                    case 1: registrarProducto(); break;
                    case 2: buscarProducto(); break;
                    case 3: actualizarStock(); break;
                    case 4: eliminarProducto(); break;
                    case 5: listarProductos(); break;
                    case 6: mostrarStockBajo(); break;
                    case 7: mostrarVencimientos(); break;
                    case 8: mostrarValorTotal(); break;
                    case 0: System.out.println("Hasta pronto."); break;
                    default: System.out.println("Opción no válida.");
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

    // ---------- Opciones del menú ----------

    private static void registrarProducto() {
        String codigo = pedir("Código: ");
        String nombre = pedir("Nombre: ");
        String categoria = pedir("Categoría (Enter = General): ");
        String proveedor = pedir("Proveedor (Enter = Sin proveedor): ");
        int cantidad = aEntero(pedir("Cantidad: "), "La cantidad", "obligatoria");
        String txtMinimo = pedir("Stock mínimo (Enter = 0): ");
        int stockMinimo = txtMinimo.isBlank() ? 0 : aEntero(txtMinimo, "El stock mínimo", "obligatorio");
        double compra = aDecimal(pedir("Precio de compra: "), "El precio de compra", "obligatorio");
        double venta = aDecimal(pedir("Precio de venta: "), "El precio de venta", "obligatorio");
        String txtIngreso = pedir("Fecha de ingreso AAAA-MM-DD (Enter = hoy): ");
        LocalDate hoy = txtIngreso.isBlank() ? LocalDate.now() : aFecha(txtIngreso);
        String esPerecedero = pedir("¿Es perecedero? (s/n): ");

        Producto p;
        if (esPerecedero.equalsIgnoreCase("s")) {
            String txtCad = pedir("Fecha de caducidad (AAAA-MM-DD): ");
            if (txtCad.isBlank()) {
                throw new IllegalArgumentException("La fecha de caducidad es obligatoria.");
            }
            LocalDate caducidad = aFecha(txtCad);
            p = new ProductoPerecedero(codigo, nombre, categoria, proveedor,
                    cantidad, stockMinimo, compra, venta, hoy, caducidad);
        } else {
            p = new ProductoNoPerecedero(codigo, nombre, categoria, proveedor,
                    cantidad, stockMinimo, compra, venta, hoy);
        }
        inventario.registrar(p);
        System.out.println("Producto registrado: " + p);
    }

    private static void buscarProducto() {
        String texto = pedir("Código o nombre a buscar: ");
        if (texto.isBlank()) {
            throw new IllegalArgumentException("Escriba un código o un nombre.");
        }
        // Primero se busca por código; si no existe, por nombre.
        try {
            System.out.println(inventario.buscarPorCodigo(texto));
        } catch (IllegalArgumentException e) {
            List<Producto> encontrados = inventario.buscarPorNombre(texto);
            if (encontrados.isEmpty()) {
                throw e; // mensaje: No se encontró ningún producto con el código ...
            }
            for (Producto p : encontrados) {
                System.out.println(p);
            }
        }
    }

    private static void actualizarStock() {
        String codigo = pedir("Código del producto: ");
        int cambio = aEntero(pedir("Unidades (positivo = entrada, negativo = salida): "),
                             "La cantidad", "obligatoria");
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
        List<Producto> lista = inventario.productosConStockBajo();
        System.out.println("Productos con stock bajo:");
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
        System.out.println(String.format(Locale.US,
                "Valor total del inventario = $%.2f", inventario.valorTotal()));
        for (Producto p : inventario.listar()) {
            System.out.println(String.format(Locale.US, " %s (%s) -> $%.2f",
                    p.getNombre(), p.getTipo(), p.calcularValorInventario()));
        }
    }

    // ---------- Métodos de ayuda para leer datos ----------

    private static String pedir(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int aEntero(String texto, String sujeto, String adjetivo) {
        if (texto.isBlank()) {
            throw new IllegalArgumentException(sujeto + " es " + adjetivo + ".");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(sujeto + " debe ser un número entero.");
        }
    }

    private static double aDecimal(String texto, String sujeto, String adjetivo) {
        if (texto.isBlank()) {
            throw new IllegalArgumentException(sujeto + " es " + adjetivo + ".");
        }
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(sujeto + " debe ser un número.");
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
