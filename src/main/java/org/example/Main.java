package org.example;

import org.example.ui.MainMenu;

/**
 * Punto de entrada de la aplicacion.
 * Lo unico que hace es arrancar el menu principal de la consola.
 */
public class Main {

    /**
     * Metodo main estandar de Java. Crea el menu y lo arranca.
     *
     * @param args argumentos de linea de comandos (no se usan)
     */
    public static void main(String[] args) {
        new MainMenu().start();
    }
}
