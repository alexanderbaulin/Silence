package com.example.alex.recycleviewmultitouchtutorial;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.alex.recycleviewmultitouchtutorial.database.Base;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Base db;

    @Before
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new Base(appContext);
        assertEquals("com.example.alex.recycleviewmultitouchtutorial", appContext.getPackageName());
    }

    @Test
    public void testDataBase() {
        testDrop();
        testCreate();

        testSelect();
        testInsert();
        testSelect();
       // testDelete();
       // testSelect();
       // testUpdate();
       // testSelect();
    }

    private void testUpdate() {
        assertEquals(Integer.valueOf(db.update(1, testData2())), Integer.valueOf(1));
    }

    private void testDelete() {
        assertEquals(Integer.valueOf(db.delete(2)), Integer.valueOf(1));
        assertEquals(Integer.valueOf(db.delete(3)), Integer.valueOf(1));
    }

    private void testSelect() {
        db.select();
    }

    private void testDrop() {
        db.drop();
    }

    private void testCreate() {
        db.create();
    }

    private void testInsert() {
        for(int i = 0; i < 3; i++)
            assertNotEquals(Long.valueOf(db.insert(testData())), Long.valueOf(-1));
    }

    private Data testData() {
        Data data = new Data();
        data.description = "description";
        data.checkedDays = new boolean[]{true, true, true, true, true, true, true};
        data.timeBegin = new int[] { 12, 20 };
        data.timeEnd = new int[] { 13, 30 };
        data.isAlarmOn = true;
        data.isVibrationAllowed = true;
        return data;
    }

    private Data testData2() {
        Data data = new Data();
        data.description = "description2";
        data.checkedDays = new boolean[]{false, false, false, false, false, false, true};
        data.timeBegin = new int[] { 14, 40 };
        data.timeEnd = new int[] { 15, 50 };
        data.isAlarmOn = false;
        data.isVibrationAllowed = false;
        return data;
    }


}
