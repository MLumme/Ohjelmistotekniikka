package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoOikein(){
        assertEquals(10,kortti.saldo());
    }
    
    @Test
    public void kortinSaldoKasvaaOikein(){
        kortti.lataaRahaa(20);
        assertEquals(30,kortti.saldo());
    }
    
    @Test
    public void kortinSaldoPieneneeKunRahaa(){
        kortti.otaRahaa(5);
        assertEquals(5,kortti.saldo());
    }
    
    @Test
    public void kortinSaldoPieneneeJosTasan(){
        kortti.otaRahaa(10);
        assertEquals(0,kortti.saldo());
    }
    
    @Test
    public void kortinSaldoEiMuttuJosLiikaa(){
        kortti.otaRahaa(20);
        assertEquals(10,kortti.saldo());
    }
    
    @Test
    public void metodiPalauttaaTosiJosOttaa(){
        assertEquals(true,kortti.otaRahaa(10));
    }
    
    @Test
    public void metodiPalattaaEpätosiJosEiOta(){
        assertEquals(false,kortti.otaRahaa(11));
    }
    
    @Test
    public void testaaToString(){
        assertEquals("saldo: 0.10",kortti.toString());
    }
}
