package com.sistemaventas;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void sumaDosNumeros() {
        App app = new App();
        assertEquals(5, app.sumar(2, 3));
    }
}
