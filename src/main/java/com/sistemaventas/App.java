package com.sistemaventas;

import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Sistema de Ventas, Pedidos e Inventario");
    }

    public int sumar(int a, int b) {
        return a + b;
    }
}
