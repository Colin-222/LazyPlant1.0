package com.example.lazyplant;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.GardenPlantDAO;
import com.example.lazyplant.plantdata.PlantCareRecord;
import com.example.lazyplant.plantdata.PlantCareRecordDAO;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;
import com.example.lazyplant.ui.profile.ReminderControl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PlantDataTest {

    private AppDatabase database;
    private FavouriteDAO favouriteDAO;
    private PlantCareRecordDAO plantCareRecordDAO;
    private GardenPlantDAO gardenPlantDAO;
    private PlantNotesDAO plantNotesDAO;
    private ReminderControl reminderControl;

    @Before
    public void createDb() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.database = Room.databaseBuilder(appContext, AppDatabase.class, Constants.GARDEN_DB_TEST)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        this.favouriteDAO = database.getFavouriteDAO();
        this.plantCareRecordDAO = database.getPlantCareRecordDAO();
        this.gardenPlantDAO = database.getGardenPlantDAO();
        this.plantNotesDAO = database.getPlantNotesDAO();
        this.favouriteDAO.deleteAll();
        this.plantCareRecordDAO.deleteAll();
        this.gardenPlantDAO.deleteAll();
        this.plantNotesDAO.deleteAll();
        this.reminderControl = new ReminderControl(appContext, Constants.GARDEN_DB_TEST);
    }

    @Test
    public void testFavourites() throws Exception {
        Favourite x = new Favourite();
        x.setSpecies_id("A1");
        Favourite x2 = new Favourite();
        x2.setSpecies_id("A2");
        Favourite x3 = new Favourite();
        x3.setSpecies_id("A3");
        this.favouriteDAO.insert(x);
        this.favouriteDAO.insert(x2);
        this.favouriteDAO.insert(x3);
        assertEquals(1, this.favouriteDAO.checkIfFavourties("A1"));
        assertEquals(3, this.favouriteDAO.getFavourites().size());
        assertEquals(this.favouriteDAO.getFavourite("A1").getSpecies_id(), x.getSpecies_id());
        this.favouriteDAO.deleteFavourite("A1");
        assertEquals(2, this.favouriteDAO.getFavourites().size());
        assertEquals(0, this.favouriteDAO.checkIfFavourties("A1"));
        this.favouriteDAO.deleteAll();
        assertEquals(0, this.favouriteDAO.getFavourites().size());
    }

    @Test
    public void PlantCareRecordDAO() throws Exception {
        PlantCareRecord x = new PlantCareRecord();
        x.setWatering_id("A1");
        x.setPlant_id("A1");
        x.setEvent("BB");
        Calendar t1 = Calendar.getInstance();
        x.setDate(t1);
        PlantCareRecord x2 = new PlantCareRecord();
        x2.setWatering_id("A2");
        x2.setPlant_id("A2");
        x2.setEvent("BB2");
        Calendar t2 = Calendar.getInstance();
        x2.setDate(t2);
        PlantCareRecord x3 = new PlantCareRecord();
        x3.setWatering_id("A3");
        x3.setPlant_id("A2");
        x3.setEvent("BB2");
        x3.setDate(t2);
        this.plantCareRecordDAO.insert(x);
        this.plantCareRecordDAO.insert(x2);
        this.plantCareRecordDAO.insert(x3);
        assertEquals(3, this.plantCareRecordDAO.getPlantCareRecords().size());
        assertEquals(this.plantCareRecordDAO.getPlantCareRecord("A1").get(0).getDate(), t1);
        assertEquals(this.plantCareRecordDAO.getPlantCareRecord("A2").get(0).getDate(), t2);
        assertEquals(2, this.plantCareRecordDAO.getPlantCareData("A2").size());
        this.plantCareRecordDAO.deleteFavourite("A1");
        assertEquals(2, this.plantCareRecordDAO.getPlantCareRecords().size());
        this.plantCareRecordDAO.deleteAll();
    }

    @Test
    public void GardenPlantDAO() throws Exception {
        GardenPlant x = new GardenPlant();
        Calendar t1 = Calendar.getInstance();
        x.setPlant_id("A1");
        x.setNotes("N1");
        x.setWatering_interval(5);
        x.setLast_watering(t1);
        GardenPlant x2 = new GardenPlant();
        Calendar t2 = Calendar.getInstance();
        x2.setPlant_id("A2");
        x2.setWatering_interval(7);
        x2.setLast_watering(t2);
        GardenPlant x3 = new GardenPlant();
        x3.setPlant_id("A3");
        x3.setWatering_interval(5);
        x3.setLast_watering(t1);
        this.gardenPlantDAO.insert(x);
        this.gardenPlantDAO.insert(x2);
        this.gardenPlantDAO.insert(x3);
        assertEquals(3, this.gardenPlantDAO.getGardenPlants().size());
        assertEquals(this.gardenPlantDAO.getGardenPlant("A1").get(0).getNotes(), "N1");
        assertEquals(t2, this.gardenPlantDAO.getGardenPlant("A2").get(0).getLast_watering());
        x.setNotes("N2");
        this.gardenPlantDAO.update(x);
        assertEquals(this.gardenPlantDAO.getGardenPlant("A1").get(0).getNotes(), "N2");
        this.gardenPlantDAO.deleteAll();
    }

    @Test
    public void PlantNotesDAO() throws Exception {
        PlantNotes x = new PlantNotes();
        x.setLast_edit(Calendar.getInstance());
        x.setSpecies_id("A1");
        x.setNotes("aaa");
        this.plantNotesDAO.insert(x);
        PlantNotes x2 = new PlantNotes();
        x2.setSpecies_id("A2");
        x2.setNotes("bbb");
        x2.setLast_edit(Calendar.getInstance());
        this.plantNotesDAO.insert(x2);
        assertEquals("aaa", this.plantNotesDAO.getNotes("A1"));
        assertEquals(null, this.plantNotesDAO.getNotes("A3"));
        assertEquals(1, this.plantNotesDAO.checkIfExists("A1"));
        assertEquals(0, this.plantNotesDAO.checkIfExists("A3"));
        PlantNotes x3 = this.plantNotesDAO.getPlantNotes("A2");
        x3.setNotes("ccc");
        this.plantNotesDAO.update(x3);
        assertEquals("ccc", this.plantNotesDAO.getNotes("A2"));
        this.plantNotesDAO.deleteAll();
    }

    @Test
    public void reminder() throws IOException {
        String i1 = this.reminderControl.addGardenPlant("AAA", null);
        String i2 = this.reminderControl.addGardenPlant("BBB", null);
        String i3 = this.reminderControl.addGardenPlant("CCC", null);
        assertEquals(3, this.gardenPlantDAO.getGardenPlants().size());
        this.reminderControl.recordPlantWatering(i1, Constants.WATERING);
        this.reminderControl.recordPlantWatering(i3, Constants.WATERING);
        assertEquals(2, this.plantCareRecordDAO.getPlantCareRecords().size());
        this.reminderControl.editNotes(i2, "BLA");
        assertEquals("BLA", this.reminderControl.getGardenPlant(i2).getNotes());
        this.gardenPlantDAO.deleteAll();

        Calendar t = Calendar.getInstance();
        Calendar c5 = Calendar.getInstance();
        Calendar c7 = Calendar.getInstance();

        GardenPlant x = new GardenPlant();
        x.setPlant_id("A1");
        x.setNotes("N1");
        x.setWatering_interval(5);
        x.setLast_watering(t);
        GardenPlant x2 = new GardenPlant();
        x2.setPlant_id("A2");
        x2.setWatering_interval(7);
        x2.setLast_watering(t);
        GardenPlant x3 = new GardenPlant();
        x3.setPlant_id("A3");
        x3.setWatering_interval(5);
        x3.setLast_watering(t);
        this.gardenPlantDAO.insert(x);
        this.gardenPlantDAO.insert(x2);
        this.gardenPlantDAO.insert(x3);

        c5.add(Calendar.DATE, 5);
        c7.add(Calendar.DATE, 7);
        List <GardenPlant> lgp0 = this.reminderControl.getPlantsToWater(t);
        List <GardenPlant> lgp5 = this.reminderControl.getPlantsToWater(c5);
        List <GardenPlant> lgp7 = this.reminderControl.getPlantsToWater(c7);
        assertEquals(0, lgp0.size());
        assertEquals(2, lgp5.size());
        assertEquals(1, lgp7.size());

    }


    @After
    public void closeDb() throws IOException {
        this.database.close();
    }

}
