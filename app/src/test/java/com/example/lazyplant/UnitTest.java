package com.example.lazyplant;

import com.example.lazyplant.plantdata.ClimateZoneGetter;

import org.junit.Test;

import static org.junit.Assert.*;


public class UnitTest {
    @Test
    public void zoneGetter() {
        ClimateZoneGetter czg = new ClimateZoneGetter();
        assertEquals(czg.match("3342","4342"), 0);
        assertEquals(czg.match("9876","1234"), 0);
        assertEquals(czg.match("5535","5235"), 1);
        assertEquals(czg.match("4777","4235"), 1);
        assertEquals(czg.match("3800","38"), 2);
        assertEquals(czg.match("5625","5637"), 2);
        assertEquals(czg.match("4321","432"), 3);
        assertEquals(czg.match("6363","6363"), 4);

        assertEquals(6, czg.getZone("3800"));
        assertEquals(2, czg.getZone("4522"));
        assertEquals(5, czg.getZone("6060"));
        assertEquals(5, czg.getZone("2195"));
        assertEquals(4, czg.getZone("2795"));
        assertEquals(7, czg.getZone("7133"));
        assertEquals(3, czg.getZone("4443"));
        assertEquals(1, czg.getZone("0845"));

    }



}