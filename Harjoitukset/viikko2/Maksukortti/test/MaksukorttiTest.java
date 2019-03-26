import maksukortti.Maksukortti;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaksukorttiTest {
    
    Maksukortti kortti;
   
    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }
     
    @Test
    public void konstruktoriAsettaaSaldonOikein(){
        assertEquals("Kortilla on rahaa 10.0 euroa", kortti.toString());
    }
     
    @Test
    public void syoEdullisestiVahentaaSaldoaOikein(){
        kortti.syoEdullisesti();
        assertEquals("Kortilla on rahaa 7.5 euroa", kortti.toString());
    }
    
    @Test
    public void syoMaukkaastiVanhentaaSaldoaOikein(){
        kortti.syoMaukkaasti();
        assertEquals("Kortilla on rahaa 6.0 euroa", kortti.toString());        
    }
    
    @Test
    public void syoEdullisestiEiVieSaldoaNegatiiviseksi(){
        kortti.syoMaukkaasti();
        kortti.syoMaukkaasti();
        kortti.syoEdullisesti();
        assertEquals("Kortilla on rahaa 2.0 euroa", kortti.toString());
    }
    
    @Test
    public void syoMaukkaastiEiVieSaldoaNegatiiviseksi(){
        kortti.syoMaukkaasti();
        kortti.syoMaukkaasti();
        kortti.syoMaukkaasti();
        assertEquals("Kortilla on rahaa 2.0 euroa", kortti.toString());
        
    }
    
    @Test
    public void kortilleVoiLadataRahaa(){
        kortti.lataaRahaa(25);
        assertEquals("Kortilla on rahaa 35.0 euroa", kortti.toString());        
    }
    
    @Test
    public void kortinSaldoEiYlitäMaksimiarvoa(){
        kortti.lataaRahaa(200);
        assertEquals("Kortilla on rahaa 150.0 euroa", kortti.toString());        
    }
    
    @Test
    public void kortilleEiVoiLadataNegatiivistaSummaa(){
        kortti.lataaRahaa(-20);
        assertEquals("Kortilla on rahaa 10.0 euroa", kortti.toString());
    }
    
    @Test
    public void kortillaVoiOstaaEdullisenKunSaldoTasan(){
        Maksukortti kortti2 = new Maksukortti(2.5);
        kortti2.syoEdullisesti();
        assertEquals("Kortilla on rahaa 0.0 euroa", kortti2.toString());
    }
    
    @Test
    public void kortillaVoiOstaaMaukkaanKunSaldoTasan(){
        Maksukortti kortti2 = new Maksukortti(4.0);
        kortti2.syoMaukkaasti();
        assertEquals("Kortilla on rahaa 0.0 euroa", kortti2.toString());
    }
}
