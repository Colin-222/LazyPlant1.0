package com.example.lazyplant.plantdata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DbAccess instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DbAccess(Context context) {
        this.openHelper = new DbHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DbAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DbAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }



    public PlantInfo getPlantInfo(String id){
        PlantInfo pi = new PlantInfo();
        String command = "SELECT * FROM plant_data WHERE id = \"" + id + "\"";
        Cursor cursor = database.rawQuery(command, null);
        if(cursor!=null && cursor.getCount()==1){
            //Get all data
            cursor.moveToFirst();
            pi.setId(cursor.getString(cursor.getColumnIndex("id")));
            pi.setScientific_name(cursor.getString(cursor.getColumnIndex("scientific_name")));
            pi.setCommon_name(cursor.getString(cursor.getColumnIndex("common_name")));
            pi.setFamily(cursor.getString(cursor.getColumnIndex("family")));
            pi.setHeight_lower(cursor.getString(cursor.getColumnIndex("height_lower")));
            pi.setHeight_upper(cursor.getString(cursor.getColumnIndex("height_upper")));
            pi.setWidth_lower(cursor.getString(cursor.getColumnIndex("width_lower")));
            pi.setWidth_upper(cursor.getString(cursor.getColumnIndex("width_upper")));

            pi.setType(getPlantDataField("type", "type", id));
            pi.setOther_names(getPlantDataField("name", "alt_names", id));
            pi.setFlower_color(getPlantDataField("color", "flower_color", id));
            pi.setFlowering_period(getPlantDataField("flowering_period", "flower_time", id));
            pi.setFrost_tolerance(getPlantDataField("frost_tolerance", "frost", id));
            pi.setLifespan(getPlantDataField("lifespan", "lifespan", id));
            pi.setLight(getPlantDataField("light_required", "light", id));
            pi.setZone(getPlantDataField("climate_zone", "zone", id));
            pi.setWildlife(getPlantDataField("wildlife_attracted", "wildlife", id));
            pi.setSoil_moisture(getPlantDataField("moisture", "soil_moisture", id));
            pi.setSoil_pH(getPlantDataField("pH_level", "soil_pH", id));
            pi.setSoil_type(getPlantDataField("soil_type", "soil_type", id));
        } else {
            return null;
        }
        return pi;
    }

    public List<String> search(){
        List<String> list = new ArrayList<>();
        String command = "SELECT id FROM plant_data WHERE height_upper > 36";
        Cursor cursor = database.rawQuery(command, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    private String getPlantDataField(String field, String table, String id){
        String data = "";
        String command = "SELECT " + field + " FROM " + table + " WHERE species_id = \"" + id + "\"";
        Cursor cursor = database.rawQuery(command, null);
        cursor.moveToFirst();
        boolean first = true;
        while (!cursor.isAfterLast()) {
            if (!first) {
                data += ", ";
            } else {
                first = false;
            }
            data += cursor.getString(0);
            cursor.moveToNext();
        }
        return data;
    }

}
