package com.example.lazyplant.plantdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

public class ClimateZoneGetter {
    private List<List<String>>  postcodeZoneMapping;

    public ClimateZoneGetter() {
        this.postcodeZoneMapping = new ArrayList<>();
        this.postcodeZoneMapping.add(Arrays.asList("670","1"));
        this.postcodeZoneMapping.add(Arrays.asList("671","1"));
        this.postcodeZoneMapping.add(Arrays.asList("672","1"));
        this.postcodeZoneMapping.add(Arrays.asList("673","1"));
        this.postcodeZoneMapping.add(Arrays.asList("6740","1"));
        this.postcodeZoneMapping.add(Arrays.asList("6743","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0800","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0810","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0812","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0820","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0828","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0829","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0830","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0832","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0835","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0836","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0837","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0838","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0840","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0841","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0845","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0846","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0847","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0850","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0852","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0862","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0872","1"));
        this.postcodeZoneMapping.add(Arrays.asList("0880","1"));

        this.postcodeZoneMapping.add(Arrays.asList("4823","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4825","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4830","1"));
        this.postcodeZoneMapping.add(Arrays.asList("487","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4890","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4891","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4865","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4868","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4869","1"));
        this.postcodeZoneMapping.add(Arrays.asList("488","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4890","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4891","1"));
        this.postcodeZoneMapping.add(Arrays.asList("4895","1"));
        this.postcodeZoneMapping.add(Arrays.asList("481","1"));
        this.postcodeZoneMapping.add(Arrays.asList("480","1"));

        this.postcodeZoneMapping.add(Arrays.asList("46","2"));
        this.postcodeZoneMapping.add(Arrays.asList("45","2"));
        this.postcodeZoneMapping.add(Arrays.asList("42","2"));

        this.postcodeZoneMapping.add(Arrays.asList("43","3"));
        this.postcodeZoneMapping.add(Arrays.asList("44","3"));
        this.postcodeZoneMapping.add(Arrays.asList("47","3"));
        this.postcodeZoneMapping.add(Arrays.asList("08","3"));
        this.postcodeZoneMapping.add(Arrays.asList("48","3"));
        this.postcodeZoneMapping.add(Arrays.asList("67","3"));

        this.postcodeZoneMapping.add(Arrays.asList("63","4"));
        this.postcodeZoneMapping.add(Arrays.asList("65","4"));
        this.postcodeZoneMapping.add(Arrays.asList("64","4"));
        this.postcodeZoneMapping.add(Arrays.asList("36","4"));
        this.postcodeZoneMapping.add(Arrays.asList("35","4"));
        this.postcodeZoneMapping.add(Arrays.asList("27","4"));
        this.postcodeZoneMapping.add(Arrays.asList("26","4"));
        this.postcodeZoneMapping.add(Arrays.asList("28","4"));
        this.postcodeZoneMapping.add(Arrays.asList("54","4"));
        this.postcodeZoneMapping.add(Arrays.asList("57","4"));

        this.postcodeZoneMapping.add(Arrays.asList("43","5"));
        this.postcodeZoneMapping.add(Arrays.asList("20","5"));
        this.postcodeZoneMapping.add(Arrays.asList("21","5"));
        this.postcodeZoneMapping.add(Arrays.asList("53","5"));
        this.postcodeZoneMapping.add(Arrays.asList("52","5"));
        this.postcodeZoneMapping.add(Arrays.asList("55","5"));
        this.postcodeZoneMapping.add(Arrays.asList("56","5"));
        this.postcodeZoneMapping.add(Arrays.asList("60","5"));
        this.postcodeZoneMapping.add(Arrays.asList("61","5"));
        this.postcodeZoneMapping.add(Arrays.asList("62","5"));

        this.postcodeZoneMapping.add(Arrays.asList("20","6"));
        this.postcodeZoneMapping.add(Arrays.asList("21","6"));
        this.postcodeZoneMapping.add(Arrays.asList("25","6"));
        this.postcodeZoneMapping.add(Arrays.asList("27","6"));
        this.postcodeZoneMapping.add(Arrays.asList("30","6"));
        this.postcodeZoneMapping.add(Arrays.asList("31","6"));
        this.postcodeZoneMapping.add(Arrays.asList("32","6"));
        this.postcodeZoneMapping.add(Arrays.asList("33","6"));
        this.postcodeZoneMapping.add(Arrays.asList("37","6"));
        this.postcodeZoneMapping.add(Arrays.asList("38","6"));
        this.postcodeZoneMapping.add(Arrays.asList("39","6"));
        this.postcodeZoneMapping.add(Arrays.asList("52","6"));

        this.postcodeZoneMapping.add(Arrays.asList("33","7"));
        this.postcodeZoneMapping.add(Arrays.asList("38","7"));
        this.postcodeZoneMapping.add(Arrays.asList("7","7"));
    }

    public int getZone(String postcode) {
        if (postcode.length() != 4){
            //throw new IllegalArgumentException("That ain't no valid postcode.");
            return -1;
        }
        int longest_match = 0;
        int zone = -1;
        for (List<String> l : this.postcodeZoneMapping){
            int matches = this.match(l.get(0), postcode);
            if (matches > longest_match){
                longest_match = matches;
                zone = Integer.valueOf(l.get(1));
            }
        }
        /*if(zone == -1){
            throw new IllegalArgumentException("That ain't no valid postcode.");
        }*/
        return zone;
    }

    public int match(String match_term, String postcode){
        int matches = 0;
        int min_length = min(match_term.length(), postcode.length());
        for (int i = 0; i < min_length; i++){
            if(match_term.charAt(i) == postcode.charAt(i)){
                matches++;
            }else{
                break;
            }
        }
        return matches;
    }
}
