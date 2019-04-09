
package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(400);
    }
    
    @Test
    public void kassapaateLuotu(){
        assertTrue(kassa != null);
    }
    
    @Test
    public void kassapaatteenSaldoOikein(){
        assertEquals(100000,kassa.kassassaRahaa());
    }
    
    @Test
    public void myydytMaukkaatNolla(){
        assertEquals(0,kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void myydytEdullisetNolla(){
        assertEquals(0,kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void ostaMaukkaastiKateinen(){
        kassa.syoMaukkaasti(400);
        assertEquals(100400,kassa.kassassaRahaa());
    }
    
    @Test 
    public void ostaMaukkaastiKateinenOikeaVaihto(){
        assertEquals(400,kassa.syoMaukkaasti(800));
    }
    
    @Test
    public void ostaMaukkaastiKateinenEiMuutostaJosVahan(){
        kassa.syoMaukkaasti(300);
        assertEquals(100000,kassa.kassassaRahaa());
    }
    
    @Test
    public void ostaMaukkaastiKateinenPalautusOikeinJosVahan(){
        assertEquals(300,kassa.syoMaukkaasti(300));
    }
    
    @Test
    public void ostaedullisestiKateinen(){
        kassa.syoEdullisesti(240);
        assertEquals(100240,kassa.kassassaRahaa());
    }
    
    @Test 
    public void ostaEdullisestiKateinenOikeaVaihto(){
        assertEquals(60,kassa.syoEdullisesti(300));
    }
    
    @Test
    public void ostaEdullisestiKateinenEiMuutostaJosVahan(){
        kassa.syoEdullisesti(200);
        assertEquals(100000,kassa.kassassaRahaa());
    }
    
    @Test
    public void ostaEdullisestiKateinenPalautusOikeinJosVahan(){
        assertEquals(200,kassa.syoMaukkaasti(200));
    }

    @Test
    public void myydytMaukkaatOikeinKunRiittava(){
        kassa.syoMaukkaasti(400);
        kassa.syoMaukkaasti(400);
        assertEquals(2,kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void myydytEdullisetOikeinKunRittava(){
        kassa.syoEdullisesti(240);
        kassa.syoEdullisesti(240);
        assertEquals(2,kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void myydytMaukkaatOikeinKunEiRiittava(){
        kassa.syoMaukkaasti(400);
        kassa.syoMaukkaasti(300);
        assertEquals(1,kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void myydytEdullisetOikeinKunEiRittava(){
        kassa.syoEdullisesti(240);
        kassa.syoEdullisesti(200);
        assertEquals(1,kassa.edullisiaLounaitaMyyty());
    }   
    
    @Test
    public void edullisenOstoOnnistuuKortilla(){
        assertEquals(true,kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void edullisenOstoEiOnnistuuJosSaldoEiRiita(){
        kortti.otaRahaa(200);
        assertEquals(false,kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void edullisenOstossaSaldoMuuttuu(){
        kassa.syoEdullisesti(kortti);
        assertEquals(160,kortti.saldo());
    }
    
    @Test
    public void josEiSaldoRiitaEdulliseenSaldoEiMuutu(){
        kortti.otaRahaa(200);
        kassa.syoEdullisesti(kortti);
        assertEquals(200,kortti.saldo());
    }
    
    @Test
    public void edullisenOstoKasvattaaMyytyjenMaaraa(){
        kassa.syoEdullisesti(kortti);
        assertEquals(1,kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstonEpaonnistuminenEiKasvataMaaraa(){
        kortti.otaRahaa(200);
        kassa.syoEdullisesti(kortti);
        assertEquals(0,kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoEiMuutaKassanSaldoa(){
        kassa.syoEdullisesti(kortti);
        assertEquals(100000,kassa.kassassaRahaa());
    }
    
    @Test
    public void edullisenEpaonnistuminenEiMuutaKassanSaldoa(){
        kortti.otaRahaa(200);
        kassa.syoEdullisesti(kortti);
        assertEquals(100000,kassa.kassassaRahaa());        
    }
    
    @Test
    public void maukkaanOstoOnnistuuKortilla(){
        assertEquals(true,kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void maukkaanOstoEiOnnistuuJosSaldoEiRiita(){
        kortti.otaRahaa(200);
        assertEquals(false,kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void maukkaanOstossaSaldoMuuttuu(){
        kassa.syoMaukkaasti(kortti);
        assertEquals(0,kortti.saldo());
    }
    
    @Test
    public void josEiSaldoRiitaMaukkaaseenSaldoEiMuutu(){
        kortti.otaRahaa(200);
        kassa.syoMaukkaasti(kortti);
        assertEquals(200,kortti.saldo());
    }
    
    @Test
    public void maukkaanOstoKasvattaaMyytyjenMaaraa(){
        kassa.syoMaukkaasti(kortti);
        assertEquals(1,kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanOstonEpaonnistuminenEiKasvataMaaraa(){
        kortti.otaRahaa(200);
        kassa.syoMaukkaasti(kortti);
        assertEquals(0,kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanOstoEiMuutaKassanSaldoa(){
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000,kassa.kassassaRahaa());
    }
    
    @Test
    public void maukkaanEpaonnistuminenEiMuutaKassanSaldoa(){
        kortti.otaRahaa(200);
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000,kassa.kassassaRahaa());        
    }   
    
    @Test
    public void kortinLatausKasvattaaKortinSaldoa(){
        kassa.lataaRahaaKortille(kortti, 400);
        assertEquals(800,kortti.saldo());
    }
    
    @Test
    public void kortinLatausKasvattaaKassanSaldoa(){
        kassa.lataaRahaaKortille(kortti, 400);
        assertEquals(100400,kassa.kassassaRahaa());
    }
    
    @Test
    public void kortinLatausEiKasvataKortinSaldoaJosSummaPienempiNolla(){
        kassa.lataaRahaaKortille(kortti, -1000);
        assertEquals(400,kortti.saldo());        
    }
    
    @Test
    public void kortinLatausEiKasvataKassanSaldoaJosSummaPienempiNolla(){
        kassa.lataaRahaaKortille(kortti, -1000);
        assertEquals(100000,kassa.kassassaRahaa());        
    }
}
