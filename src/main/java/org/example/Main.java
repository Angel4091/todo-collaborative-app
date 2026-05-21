package org.example;

import org.example.ui.MainMenu;

// Punto de entrada de la aplicacion. Lo unico que hace es
// arrancar el menu principal de la consola.
public class Main {
    public static void main(String[] args) {
        new MainMenu().start();
    }
}
