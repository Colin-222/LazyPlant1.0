package com.example.lazyplant.ui.profile;

import android.content.Context;

import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.GardenPlantDAO;
import com.example.lazyplant.plantdata.PlantCareRecord;
import com.example.lazyplant.plantdata.PlantCareRecordDAO;
import com.example.lazyplant.plantdata.PlantInfoEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ReminderControl {
    private Context context;
    private String db_name;

    public ReminderControl(Context context, String database_name) {
        this.context = context;
        this.db_name = database_name;
    }

    /**
     * Adds a plant that is in the user's garden. It sets watering_interval to value found in plant database.
     * last_watering is today, rain_exposed is false by default.
     * If no plant is found a default of 14 days is used for watering_interval.
     * @param species_id ID of species of plant to add. Can be null.
     * @param name Name of plant that user gives.
     * @return ID of newly created plant.
     */
    public String addGardenPlant(String name, String species_id){
        String plant_id = UUID.randomUUID().toString();
        GardenPlant x = new GardenPlant();
        if (species_id != null){
            DbAccess databaseAccess = DbAccess.getInstance(context);
            databaseAccess.open();
            PlantInfoEntity p = databaseAccess.getPlantInfo(species_id);
            databaseAccess.close();
            x.setWatering_interval(Integer.valueOf(p.getWatering_interval()));
        } else {
            x.setWatering_interval(14);
        }
        x.setPlant_id(plant_id);
        x.setName(name);
        x.setRain_exposed(false);
        x.setNotes("");
        Calendar t = Calendar.getInstance();
        x.setLast_watering(t);
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        dao.insert(x);
        database.close();
        return plant_id;
    }

    /**
     * Gets a plant
     * @param plant_id Plant id of plant to get
     */
    public GardenPlant getGardenPlant(String plant_id){
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        GardenPlant gp = dao.getGardenPlant(plant_id).get(0);
        database.close();
        return gp;
    }

    /**
     * Deletes a plant
     * @param plant_id Plant id of plant to delete
     */
    public void deleteGardenPlant(String plant_id){
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        dao.deleteGardenPlant(plant_id);
        database.close();
    }

    /**
     * Edits notes for a plant
     * @param plant_id Plant id of plant whose notes to edit
     * @param notes New notes
     */
    public void editNotes(String plant_id, String notes){
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        GardenPlant gp = dao.getGardenPlant(plant_id).get(0);
        gp.setNotes(notes);
        dao.update(gp);
        database.close();
    }

    /**
     * Notes that a plant has been watered. Updates it's details accordingly and generates a record.
     * @param plant_id
     */
    public void recordPlantWatering(String plant_id, String source){
        Calendar today = Calendar.getInstance();
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        GardenPlant gp = dao.getGardenPlant(plant_id).get(0);
        gp.setLast_watering(today);
        dao.update(gp);

        PlantCareRecordDAO dao2 = database.getPlantCareRecordDAO();
        PlantCareRecord x = new PlantCareRecord();
        x.setWatering_id(UUID.randomUUID().toString());
        x.setPlant_id(gp.getPlant_id());
        x.setSpecies_id(gp.getSpecies_id());
        x.setDate(today);
        x.setSource(source);
        x.setEvent(Constants.WATERING);
        dao2.insert(x);
        database.close();
    }

    /**
     * Get the next date a plant needs to be watered.
     * @param plant_id ID of the plant whose next watering date we want to find.
     * @return Date of next watering as a Calendar.
     */
    public Calendar getNextWatering(String plant_id){
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        GardenPlant gp = dao.getGardenPlant(plant_id).get(0);
        Calendar c = gp.getLast_watering();
        c.add(Calendar.DATE, gp.getWatering_interval());
        return c;
    }

    /**
     * Get a list of plants to water for a certain day.
     * @param date Date which we should get the plants that need to be watered.
     * @return List of plants that need to be watered.
     */
    public List<GardenPlant> getPlantsToWater(Calendar date) {
        List<GardenPlant> plants_to_water = new ArrayList<>();
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        List<GardenPlant> plants = dao.getGardenPlants();
        database.close();
        for (GardenPlant gp : plants) {
            Calendar c = gp.getLast_watering();
            int wi = gp.getWatering_interval();
            c.add(Calendar.DATE, wi);
            if(wi>0){
                if ((c.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) &&
                        (c.get(Calendar.YEAR) == date.get(Calendar.YEAR))){
                    plants_to_water.add(gp);
                }
                if (c.before(date)){
                    plants_to_water.add(gp);
                }
            }
        }
        return plants_to_water;
    }

}
