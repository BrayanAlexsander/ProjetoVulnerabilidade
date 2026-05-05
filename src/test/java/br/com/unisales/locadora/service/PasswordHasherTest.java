package br.com.unisales.locadora.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    public void testPasswordHasherExists() {
        assertNotNull(new PasswordHasher());
    }
}
