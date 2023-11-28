package com.example.servicenovigrad;
import static org.junit.Assert.*;
import org.junit.Test;
public class TestsUnitaires {
    @Test
    public void testChampNonVide1() {
        assertTrue(MainActivity.champNonVide("Novigrad"));
    }
    @Test
    public void testChampNonVide2() {
        assertFalse(MainActivity.champNonVide(""));
    }
    @Test
    public void testChampNonVide3() {
        assertFalse(MainActivity.champNonVide(null));
    }
    @Test
    public void testEmail1() {
        assertTrue(RegisterActivity.estEmail("test@gmail.com"));
    }
    @Test
    public void testEmail2() {
        assertFalse(RegisterActivity.estEmail("test.gmail@com"));
    }
    @Test
    public void testEmail3() {
        assertFalse(RegisterActivity.estEmail("test@@gmail.com"));
    }
    @Test
    public void testEmail4() {
        assertFalse(RegisterActivity.estEmail("test"));
    }
    @Test
    public void testHeures1() {
        assertFalse(EditSuccur.heuresCorrectes(10,10));
    }
    @Test
    public void testHeures2() {
        assertTrue(EditSuccur.heuresCorrectes(8,16));
    }
    @Test
    public void testChamps1() {
        assertFalse(EditSuccur.champsCorrects("Novigrad12","Ottawa","12 Rue Lincoln",""));
    }
    @Test
    public void testChamps2() {
        assertTrue(EditSuccur.champsCorrects("Novigrad12","Ottawa","12 Rue Lincoln","6131149562"));
    }
}
