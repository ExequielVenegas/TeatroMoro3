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
    static int contadorVenta = 0;
    static int contadorCliente = 1;

    static Venta[] ventas = new Venta[capacidadSala];
    static String[] asientos = new String[capacidadSala];
    static Cliente[] clientes = new Cliente[capacidadSala];

    static List<Descuento> descuentos = new ArrayList<>();
    static List<Reserva> reservas = new ArrayList<>();

    static class Venta {
        int idVenta;
        String ubicacionAsiento;
        double costoBase;
        double descuentoAplicado;
        double costoFinal;
        int idCliente;

        Venta(int idVenta, String ubicacionAsiento, double costoBase, double descuentoAplicado, double costoFinal, int idCliente) {
            this.idVenta = idVenta;
            this.ubicacionAsiento = ubicacionAsiento;
            this.costoBase = costoBase;
            this.descuentoAplicado = descuentoAplicado;
            this.costoFinal = costoFinal;
            this.idCliente = idCliente;
        }

        @Override
        public String toString() {
            return "Venta N°: " + idVenta + ", Asiento: " + ubicacionAsiento +
                   ", Costo Base: $" + String.format("%.2f", costoBase) +
                   ", Descuento: " + String.format("%.2f%%", descuentoAplicado * 100) +
                   ", Costo Final: $" + String.format("%.2f", costoFinal) +
                   ", Cliente ID: " + idCliente;
        }
    }

    static class Cliente {
        int idCliente;
        String tipoCliente;

        Cliente(int idCliente, String tipoCliente) {
            this.idCliente = idCliente;
            this.tipoCliente = tipoCliente;
        }

        @Override
        public String toString() {
            return "Cliente ID: " + idCliente + ", Tipo: " + tipoCliente;
        }
    }

    static class Descuento {
        String tipoCliente;
        double porcentajeDescuento;

        Descuento(String tipoCliente, double porcentajeDescuento) {
            this.tipoCliente = tipoCliente;
            this.porcentajeDescuento = porcentajeDescuento;
        }

        @Override
        public String toString() {
            return "Descuento para " + tipoCliente + ": " + String.format("%.2f%%", porcentajeDescuento * 100);
        }
    }

    static class Reserva {
        int idReserva;
        int idCliente;
        String ubicacionAsiento;

        Reserva(int idReserva, int idCliente, String ubicacionAsiento) {
            this.idReserva = idReserva;
            this.idCliente = idCliente;
            this.ubicacionAsiento = ubicacionAsiento;
        }

        @Override
        public String toString() {
            return "Reserva ID: " + idReserva + ", Cliente ID: " + idCliente + ", Asiento: " + ubicacionAsiento;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        descuentos.add(new Descuento("estudiante", 0.10));
        descuentos.add(new Descuento("terceraedad", 0.15));

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
                case 7 -> eliminarVentaPorId(scanner);
                case 8 -> actualizarVentaPorId(scanner);
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
        System.out.println("7. Eliminar venta por ID");
        System.out.println("8. Actualizar venta por ID");
        System.out.println("5. Salir");
    }

    static boolean ubicacionAsientoValida(String ubicacion) {
        return ubicacion.equalsIgnoreCase("VIP") || ubicacion.equalsIgnoreCase("Platea") || ubicacion.equalsIgnoreCase("Balcón");
    }

    static boolean tipoClienteValido(String tipo) {
        return tipo.equalsIgnoreCase("normal") || tipo.equalsIgnoreCase("estudiante") || tipo.equalsIgnoreCase("terceraedad");
    }

    static double obtenerCostoBase(String ubicacion) {
        if (ubicacion.equalsIgnoreCase("VIP")) {
            return 25000.0;
        } else if (ubicacion.equalsIgnoreCase("Platea")) {
            return 15000.0;
        } else if (ubicacion.equalsIgnoreCase("Balcón")) {
            return 8000.0;
        } else {
            System.out.println("Ubicación inválida.");
            return -1;
        }
    }

    static double obtenerDescuento(String tipoCliente) {
        for (Descuento descuento : descuentos) {
            if (descuento.tipoCliente.equalsIgnoreCase(tipoCliente)) {
                return descuento.porcentajeDescuento;
            }
        }
        return 0.0; // Descuento del 0% para clientes normales o no reconocidos
    }

    static boolean asientoOcupado(String ubicacion) {
        for (int i = 0; i < totalEntradasVendidas; i++) {
            if (asientos[i] != null && asientos[i].equalsIgnoreCase(ubicacion)) {
                return true;
            }
        }
        return false;
    }

    static void realizarVenta(Scanner scanner) {
        if (totalEntradasVendidas >= capacidadSala) { // Corregido aquí
            System.out.println("Lo sentimos, no hay más entradas disponibles.");
            return;
        }

        System.out.println("\n--- Venta de Entrada ---");
        System.out.print("Ubicación del asiento (VIP, Platea, Balcón): ");
        String ubicacionAsiento = scanner.nextLine().trim();
        if (!ubicacionAsientoValida(ubicacionAsiento)) {
            System.out.println("Ubicación del asiento inválida. Por favor, ingrese VIP, Platea o Balcón.");
            return;
        }
        if (asientoOcupado(ubicacionAsiento)) {
            System.out.println("El asiento seleccionado no está disponible.");
            return;
        }

        double costoBase = obtenerCostoBase(ubicacionAsiento);
        if (costoBase == -1) return;

        System.out.print("Seleccione el tipo de cliente (Normal, Estudiante, TerceraEdad): ");
        String tipoCliente = scanner.nextLine().trim();
        if (!tipoClienteValido(tipoCliente)) {
            System.out.println("Tipo de cliente inválido. Se considerará como Normal.");
            tipoCliente = "Normal";
        }

        double descuentoAplicado = obtenerDescuento(tipoCliente);
        double costoFinal = costoBase - (costoBase * descuentoAplicado);

        Cliente nuevoCliente = new Cliente(contadorCliente++, tipoCliente);
        clientes[totalEntradasVendidas] = nuevoCliente;
        int idClienteActual = nuevoCliente.idCliente;

        Venta nuevaVenta = new Venta(++contadorVenta, ubicacionAsiento, costoBase, descuentoAplicado, costoFinal, idClienteActual);
        ventas[totalEntradasVendidas] = nuevaVenta;
        asientos[totalEntradasVendidas] = ubicacionAsiento;

        ingresosTotales += costoFinal;
        totalEntradasVendidas++;
        entradasDisponibles--;

        System.out.println("Venta realizada exitosamente. Detalles:");
        System.out.println(nuevaVenta);
        System.out.println(nuevoCliente);
    }

    static void mostrarResumenVentas() {
        System.out.println("\n--- Resumen de Ventas ---");
        if (totalEntradasVendidas == 0) {
            System.out.println("No se han realizado ventas aún.");
        } else {
            for (int i = 0; i < totalEntradasVendidas; i++) {
                if (ventas[i] != null) {
                    System.out.println(ventas[i]);
                }
            }
        }
    }

    static void generarBoleta(Scanner scanner) {
        System.out.print("Ingrese el número de venta para generar la boleta: ");
        int numeroVenta = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < totalEntradasVendidas; i++) {
            if (ventas[i] != null && ventas[i].idVenta == numeroVenta) {
                System.out.println("\n--------------------------");
                System.out.println("          Teatro Moro");
                System.out.println("--------------------------");
                System.out.println("Ubicación: " + ventas[i].ubicacionAsiento);
                System.out.println("Costo Base: $" + String.format("%.2f", ventas[i].costoBase));
                System.out.println("Descuento Aplicado: " + String.format("%.2f%%", ventas[i].descuentoAplicado * 100));
                System.out.println("Costo Final: $" + String.format("%.2f", ventas[i].costoFinal));
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

    static void eliminarVentaPorId(Scanner scanner) {
        System.out.print("Ingrese el ID de la venta a eliminar: ");
        int idVentaEliminar = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < totalEntradasVendidas; i++) {
            if (ventas[i] != null && ventas[i].idVenta == idVentaEliminar) {
                ventas[i] = null;
                asientos[i] = null;
                System.out.println("Venta con ID " + idVentaEliminar + " eliminada (marcada como nula).");
                return;
            }
        }
        System.out.println("No se encontró ninguna venta con el ID " + idVentaEliminar + ".");
    }

    static void actualizarVentaPorId(Scanner scanner) {
        System.out.print("Ingrese el ID de la venta a actualizar: ");
        int idVentaActualizar = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nueva ubicación del asiento (VIP, Platea, Balcón): ");
        String nuevaUbicacion = scanner.nextLine().trim();
        System.out.print("Nuevo tipo de cliente (Normal, Estudiante, TerceraEdad): ");
        String nuevoTipoCliente = scanner.nextLine().trim();

        for (int i = 0; i < totalEntradasVendidas; i++) {
            if (ventas[i] != null && ventas[i].idVenta == idVentaActualizar) {
                if (ubicacionAsientoValida(nuevaUbicacion) && !asientoOcupado(nuevaUbicacion)) {
                    double costoBaseOriginal = ventas[i].costoBase;
                    double descuentoOriginal = ventas[i].descuentoAplicado;

                    ventas[i].ubicacionAsiento = nuevaUbicacion;
                    asientos[i] = nuevaUbicacion;

                    double nuevoCostoBase = obtenerCostoBase(nuevaUbicacion);
                    if (nuevoCostoBase != -1) {
                        ventas[i].costoBase = nuevoCostoBase;
                        double nuevoDescuento = obtenerDescuento(nuevoTipoCliente);
                        ventas[i].descuentoAplicado = nuevoDescuento;
                        ventas[i].costoFinal = nuevoCostoBase - (nuevoCostoBase * nuevoDescuento);

                        for (Cliente cliente : clientes) {
                            if (cliente != null && cliente.idCliente == ventas[i].idCliente) {
                                cliente.tipoCliente = nuevoTipoCliente;
                                break;
                            }
                        }

                        System.out.println("Venta con ID " + idVentaActualizar + " actualizada.");
                        System.out.println(ventas[i]);
                        return;
                    }
                } else {
                    System.out.println("La nueva ubicación del asiento no es válida o ya está ocupada.");
                    return;
                }
            }
        }
        System.out.println("No se encontró ninguna venta con el ID " + idVentaActualizar + ".");
    }
}
