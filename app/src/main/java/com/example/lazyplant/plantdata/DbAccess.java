package com.example.lazyplant.plantdata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lazyplant.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains functions that are used to set up and access the stored plant database.
 */
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

    /**
     * Gets some info on a single plant from the database.
     * @param id The id of the plant whose information we want to retrieve.
     * @return An object that contains all the stored information on a plant.
     */
    public PlantInfoEntity getShortPlantInfo(String id){
        PlantInfoEntity pi = new PlantInfoEntity();
        String command = "SELECT * FROM plant_data WHERE species_id = \"" + id + "\"";
        Cursor cursor = database.rawQuery(command, null);
        if(cursor!=null && cursor.getCount()==1){
            //Get all data
            cursor.moveToFirst();
            pi.setId(cursor.getString(cursor.getColumnIndex(Constants.SPECIES_ID_FIELD)));
            pi.setScientific_name(cursor.getString(cursor.getColumnIndex("scientific_name")));
            pi.setCommon_name(cursor.getString(cursor.getColumnIndex("common_name")));
        } else {
            return null;
        }
        return pi;
    }

    /**
     * Gets a single plants information from the database.
     * @param id The id of the plant whose information we want to retrieve.
     * @return An object that contains all the stored information on a plant.
     */
    public PlantInfoEntity getPlantInfo(String id){
        PlantInfoEntity pi = new PlantInfoEntity();
        String command = "SELECT * FROM plant_data WHERE species_id = \"" + id + "\"";
        Cursor cursor = database.rawQuery(command, null);
        if(cursor!=null && cursor.getCount()==1){
            //Get all data
            cursor.moveToFirst();
            pi.setId(cursor.getString(cursor.getColumnIndex(Constants.SPECIES_ID_FIELD)));
            pi.setScientific_name(cursor.getString(cursor.getColumnIndex("scientific_name")));
            pi.setCommon_name(cursor.getString(cursor.getColumnIndex("common_name")));
            pi.setFamily(cursor.getString(cursor.getColumnIndex("family")));
            pi.setHeight_lower(cursor.getString(cursor.getColumnIndex("height_lower")));
            pi.setHeight_upper(cursor.getString(cursor.getColumnIndex("height_upper")));
            pi.setWidth_lower(cursor.getString(cursor.getColumnIndex("width_lower")));
            pi.setWidth_upper(cursor.getString(cursor.getColumnIndex("width_upper")));
            pi.setWatering_interval(cursor.getString(cursor.getColumnIndex("watering_interval")));

            pi.setType(getSinglePlantDataDatum("type", "type", id));
            pi.setOther_names(getSinglePlantDataDatum("name", "alt_names", id));
            pi.setFlowering_period(getSinglePlantDataDatum("flowering_period", "flower_time", id));
            pi.setFrost_tolerance(getSinglePlantDataDatum("frost_tolerance", "frost", id));
            pi.setLight(getSinglePlantDataDatum("light_required", "light", id));
            pi.setZone(getSinglePlantDataDatum("climate_zone", "zone", id));
            pi.setWildlife(getSinglePlantDataDatum("wildlife_attracted", "wildlife", id));
            pi.setSoil_moisture(getSinglePlantDataDatum("moisture", "soil_moisture", id));
            pi.setSoil_pH(getSinglePlantDataDatum("pH_level", "soil_pH", id));
            pi.setSoil_type(getSinglePlantDataDatum("soil_type", "soil_type", id));
        } else {
            return null;
        }
        return pi;
    }

    /**
     * Used to simplify the creation of conditions in SQL conditions. It is used to check if the 'category'
     * field is equal to ANY of the values found in 'checks'. This means it uses 'OR' so matching
     * anything in 'checks' will succeed.
     * @param category The field in a table we want to create conditions for.
     * @param checks A list of values which the field can be equal to.
     * @return Part of an SQL string that contains the conditions. Not it always start with 'OR' so
     * it must follow other conditions. E.g ' OR category = check1 OR category = check2'. This string
     * should be appended to "WHERE 0 = 1" if we want to use the returned conditions only.
     */
    private String getConditions(String category, List<String> checks){
        String conditions = "";
        boolean first = true;
        for (String i : checks){
            if(first){
                first = false;
                conditions += category + "=\"" + i + "\"";
            }else{
                conditions += " OR " + category + "=\"" + i + "\"";
            }
        }
        return conditions;
    }

    /**
     * Find values from records that meet a certain condition. This can only retrieve a single value.
     * The conditions are created with the getConditions function.
     * @param get The field whose value we want to retrieve.
     * @param table The table we wish to search.
     * @param category The field in a table we want to create conditions for.
     * @param checks A list of values which the field can be equal to.
     * @return A list of values from the 'get' field from records that match the given condition .
     */
    public List<String> searchOnConditions(String get, String table, String category, List<String> checks){
        List<String> list = new ArrayList<>();
        String command = "SELECT DISTINCT " + get+ " FROM " + table + " WHERE " +
                getConditions(category, checks);
        Cursor cursor = database.rawQuery(command, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        if(cursor != null)
            cursor.close();
        return list;
    }

    /**
     * Find values from records that meet a certain condition. This can only retrieve a single value.
     * @param get The field whose value we want to retrieve.
     * @param table The table we wish to search.
     * @param conditions The SQL statements that represent a condition. It follows a " WHERE " statement.
     * @return A list of values from the 'get' field from records that match the given condition .
     */
    public List<String> searchOnCondition(String get, String table, String conditions){
        List<String> list = new ArrayList<>();
        String command = "SELECT DISTINCT " + get+ " FROM " + table + " WHERE " + conditions;
        Cursor cursor = database.rawQuery(command, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        if(cursor != null)
            cursor.close();
        return list;
    }

    /**
     * Find values from records where a certain condition is between two bounds. The bounding is
     * [lower_bound, upper_bound)
     * @param get The condition whose value we want to retrieve.
     * @param table The table we wish to search.
     * @param condition The condition we want to use as a condition for comparison.
     * @param lower_bound A value that 'condition' is greater than or equal to.
     * @param upper_bound A value that 'condition' is less than.
     * @return A list of values from the 'get' condition from records that match the given condition.
     */
    public List<String> searchOnConditionBetweenValues(String get, String table, String condition,
                                                       String lower_bound, String upper_bound){
        List<String> list = new ArrayList<>();
        String command = "SELECT DISTINCT " + get + " FROM " + table + " WHERE " + condition +
                " >= " + lower_bound + " AND " + condition + " < " + upper_bound;
        Cursor cursor = database.rawQuery(command, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        if(cursor != null)
            cursor.close();
        return list;
    }


    /**
     * Gets all data from a certain field in a certain table.
     * @param table The table we wish to search.
     * @param field The field we want to get data from.
     * @return A list containing the desired data.
     */
    public List<String> getAllFieldFromTable(String field, String table){
        List<String> list = new ArrayList<>();
        String command = "SELECT " + field + " FROM " + table;
        Cursor cursor = database.rawQuery(command, null); cursor.moveToFirst();
        while (!cursor.isAfterLast()) { list.add(cursor.getString(0));cursor.moveToNext(); }
        if(cursor != null)
            cursor.close();
        return list;
    }

    /**
     * Gets a single piece of data from a given field and table from a record that
     * corresponds to a given id.
     * @param field The field whose value we want to retrieve.
     * @param table The table we wish to search.
     * @param id Id belonging to the record we wish to search.
     * @return A String containing the desired datum.
     */
    private String getSinglePlantDataDatum(String field, String table, String id){
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
        if(cursor != null)
            cursor.close();
        return data;
    }

    /**
     * This functions searches the plant database for plants that meet the requirements of our
     * filters. Each lists elements align to represent a single filter option.
     * @param id_name The field name of the primary id field.
     * @param options_selected List containing selected options, which will be names of what a
     *                         filter option contains.
     * @param search_tables List containing names of tables to search in.
     * @param search_fields List containing names of fields to search for.
     * @param selected_filters List containing lists which contain the selected values to search for.
     * @param height Text that pertains to how the height field is referenced in the database.
     * @param width Text that pertains to how the width field is referenced in the database.
     * @return
     */
    public List<String> searchPlantDatabase(String id_name, List<String> options_selected,
                                                   List<String> search_tables, List<String> search_fields,
                                                   List<List<String>> selected_filters,
                                                   String height, String width){
        List<String> found = new ArrayList<>();
        //Return all if nothing is selected
        if(options_selected.size() == 0){
            found = this.getAllFieldFromTable(Constants.SPECIES_ID_FIELD, "plant_data");
        }else {
            List<List<String>> found_or = new ArrayList<>();
            Pattern p = Pattern.compile("^(" + height + "|" + width + ")_[a-zA-z]*$");
            Pattern pj = Pattern.compile("^([0-9]*)-([0-9]*)$");
            //Search through database with each filter
            for (int i = 0; i < options_selected.size(); i++) {
                Matcher m = p.matcher(search_fields.get(i));
                if (m.find()) {
                    //Size field, making numerical comparison here, size is in format 'x_x'
                    for (int j = 0; j < selected_filters.get(i).size(); j++) {
                        Matcher mj = pj.matcher(selected_filters.get(i).get(j));
                        if (mj.find()) {
                            String lower_size = mj.group(1);
                            String upper_size = mj.group(2);
                            List<String> tmp = this.searchOnConditionBetweenValues(id_name, //id_name,
                                    search_tables.get(i), search_fields.get(i), lower_size, upper_size);
                            found_or.add(tmp);
                        } else {
                            throw new IllegalStateException("Something wrong with getting lower and upper bounds. Regex not working.");
                        }
                    }
                } else {
                    //Other fields, checking if it belongs to a group
                    List<String> tmp = this.searchOnConditions(id_name,
                            search_tables.get(i), search_fields.get(i), selected_filters.get(i));
                    found_or.add(tmp);
                }
            }
            //Combine result AND-wise
            found = new ArrayList<>(found_or.get(0));
            for (int i = 0; i < found_or.size(); i++) {
                found.retainAll(found_or.get(i));
            }
        }
        return found;
    }

    public List<String> searchPlantsByAnimalsAttracted(String animal_type){
        List<String> found = new ArrayList<>();
        String command = "SELECT DISTINCT animals_attracted.species_id" +
                " FROM animals_attracted INNER JOIN animals ON animals_attracted.animal=animals.animal"
                + " WHERE animals.type = \"" + animal_type +"\" UNION " +
                "SELECT DISTINCT species_id FROM wildlife WHERE wildlife_attracted=\"" +
                animal_type + "\"";
        Cursor cursor = database.rawQuery(command, null);
        cursor.moveToFirst();
        boolean first = true;
        while (!cursor.isAfterLast()) {
            found.add(cursor.getString(0));
            cursor.moveToNext();
        }
        if(cursor != null)
            cursor.close();
        return found;
    }

}
