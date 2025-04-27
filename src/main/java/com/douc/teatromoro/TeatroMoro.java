package com.douc.teatromoro;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeatroMoro {

    static final String nombreTeatro = "Teatro Moro";
    static final int capacidadSala = 100;
    static int entradasDisponibles = capacidadSala;
    static double ingresosTotales = 0.0;
    static int totalEntradasVendidas = 0;
    static int contadorEntrada = 1;

    static List<Venta> ventasRealizadas = new ArrayList<>();

    static class Venta {
        int numeroVenta;
        String ubicacion;
        double costoBase;
        double descuentoAplicado;
        double costoFinal;
        String tipoCliente;

        Venta(int numeroVenta, String ubicacion, double costoBase, double descuentoAplicado, double costoFinal, String tipoCliente) {
            this.numeroVenta = numeroVenta;
            this.ubicacion = ubicacion;
            this.costoBase = costoBase;
            this.descuentoAplicado = descuentoAplicado;
            this.costoFinal = costoFinal;
            this.tipoCliente = tipoCliente;
        }

        @Override
        public String toString() {
            return "Venta N°: " + numeroVenta + ", Ubicación: " + ubicacion +
                   ", Costo Base: $" + String.format("%.2f", costoBase) +
                   ", Descuento: " + String.format("%.2f%%", descuentoAplicado * 100) +
                   ", Costo Final: $" + String.format("%.2f", costoFinal) +
                   ", Cliente: " + tipoCliente;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1 -> realizarVenta(scanner);
                case 2 -> mostrarResumenVentas();
                case 3 -> generarBoleta(scanner);
                case 4 -> mostrarIngresosTotales();
                case 5 -> salirDelSistema();
                default -> System.out.println("Opción inválida. Intenta nuevamente.");
            }
            System.out.println();
        } while (opcion != 5);

        scanner.close();
    }

    static void mostrarMenu() {
        System.out.println("\n=== Menú de Opciones - " + nombreTeatro + " ===");
        System.out.println("1. Venta de entradas");
        System.out.println("2. Visualizar resumen de ventas");
        System.out.println("3. Generar boleta");
        System.out.println("4. Calcular ingresos totales");
        System.out.println("5. Salir");
    }

    static void realizarVenta(Scanner scanner) {
        if (entradasDisponibles <= 0) {
            System.out.println("Lo sentimos, no hay más entradas disponibles.");
            return;
        }

        System.out.println("\n--- Venta de Entrada ---");
        System.out.print("Ubicación (VIP/Platea/Balcón): ");
        String ubicacion = scanner.nextLine().trim().toUpperCase();

        double costoBase;
        switch (ubicacion) {
            case "VIP":
                costoBase = 25000.0;
                break;
            case "PLATEA":
                costoBase = 15000.0;
                break;
            case "BALCÓN":
                costoBase = 8000.0;
                break;
            default:
                System.out.println("Ubicación inválida.");
                return;
        }

        System.out.print("Seleccione el tipo de cliente (Normal/Estudiante/TerceraEdad): ");
        String tipoCliente = scanner.nextLine().trim();

        double descuentoAplicado = 0.0;

        switch (tipoCliente.toLowerCase()) {
            case "estudiante":
                descuentoAplicado = 0.10;
                break;
            case "terceraedad":
                descuentoAplicado = 0.15;
                break;
            case "normal":
                descuentoAplicado = 0.0; 
                break;
            default:
                System.out.println("Tipo de cliente no reconocido, se considerará como Normal.");
                tipoCliente = "Normal";
        }

        double costoFinal = costoBase - (costoBase * descuentoAplicado);
        Venta nuevaVenta = new Venta(contadorEntrada++, ubicacion, costoBase, descuentoAplicado, costoFinal, tipoCliente);
        ventasRealizadas.add(nuevaVenta);
        ingresosTotales += costoFinal;
        totalEntradasVendidas++;
        entradasDisponibles--;

        System.out.println("Venta realizada exitosamente. Detalles:");
        System.out.println(nuevaVenta);
    }

    static void mostrarResumenVentas() {
        System.out.println("\n--- Resumen de Ventas ---");
        if (ventasRealizadas.isEmpty()) {
            System.out.println("No se han realizado ventas aún.");
        } else {
            for (Venta venta : ventasRealizadas) {
                System.out.println(venta);
            }
        }
    }

    static void generarBoleta(Scanner scanner) {
        System.out.print("Ingrese el número de venta para generar la boleta: ");
        int numeroVenta = scanner.nextInt();
        scanner.nextLine();

        for (Venta venta : ventasRealizadas) {
            if (venta.numeroVenta == numeroVenta) {
                System.out.println("\n--------------------------");
                System.out.println("        Teatro Moro");
                System.out.println("--------------------------");
                System.out.println("Ubicación: " + venta.ubicacion);
                System.out.println("Costo Base: $" + String.format("%.2f", venta.costoBase));
                System.out.println("Descuento Aplicado: " + String.format("%.2f%%", venta.descuentoAplicado * 100));
                System.out.println("Costo Final: $" + String.format("%.2f", venta.costoFinal));
                System.out.println("--------------------------");
                System.out.println("Gracias por su visita al Teatro Moro");
                System.out.println("--------------------------");
                return;
            }
        }
        System.out.println("No se encontró ninguna venta con el número ingresado.");
    }

    static void mostrarIngresosTotales() {
        System.out.println("\n--- Ingresos Totales ---");
        System.out.println("Los ingresos totales acumulados son: $" + String.format("%.2f", ingresosTotales));
    }

    static void salirDelSistema() {
        System.out.println("\nGracias por su compra.");
        System.out.println("Saliendo del sistema...");
        System.out.println("Total de entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Ingresos totales: $" + String.format("%.2f", ingresosTotales));
    }
}