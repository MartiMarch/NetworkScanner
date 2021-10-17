package com.dybbukk.obtainarpofgateway;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ARPTest {
    
    public ARPTest() {
    }

    @Test
    public void testValidarIP() {
        System.out.println("validarIP");
        String ip = "19.168.1.53";
        ARP instance = new ARP();
        boolean expResult = true;
        boolean result = instance.validarIP(ip);
        assertEquals(expResult, result);
    }

    @Test
    public void testComparacion() {
        System.out.println("comparacion");
        String ip1 = "192.168.1.47";
        String ip2 = "192.168.1.47";
        ARP instance = new ARP();
        int expResult = 0;
        int result = instance.comparacion(ip1, ip2);
        assertEquals(expResult, result);
    }

    @Test
    public void testIncrementarIP() {
        System.out.println("incrementarIP");
        String ip = "192.168.1.0";
        ARP instance = new ARP();
        String expResult = "192.168.1.1";
        String result = instance.incrementarIP(ip);
        assertEquals(expResult, result);
    }

    @Test
    public void testDecrementarIP() {
        System.out.println("decrementarIP");
        String ip = "192.168.1.1";
        ARP instance = new ARP();
        String expResult = "192.168.1.0";
        String result = instance.decrementarIP(ip);
        assertEquals(expResult, result);
    }

    @Test
    public void testObtenerPartes() {
        System.out.println("obtenerPartes");
        String ip = "192.168.1.100";
        ARP instance = new ARP();
        ArrayList<Integer> expResult = new ArrayList<Integer>();
        expResult.add(100);
        expResult.add(1);
        expResult.add(168);
        expResult.add(192);
        ArrayList<Integer> result = instance.obtenerPartes(ip);
        for(int i = 0; i < expResult.size(); ++i)
        {
            assertEquals(expResult.get(i), result.get(i));
        }
    }    
}
