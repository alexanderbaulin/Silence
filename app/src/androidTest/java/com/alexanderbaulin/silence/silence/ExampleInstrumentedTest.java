package com.alexanderbaulin.silence.silence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.alexanderbaulin.silence.mvp.model.DataItem;
import com.alexanderbaulin.silence.mvp.model.database.Base;

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
        testDelete();
        testSelect();
        testUpdate();
        testSelect();
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

    private DataItem testData() {
        DataItem dataItem = new DataItem();
        dataItem.description = "description";
        dataItem.checkedDays = new boolean[]{true, true, true, true, true, true, true};
        dataItem.timeBegin = new int[] { 12, 20 };
        dataItem.timeEnd = new int[] { 13, 30 };
        dataItem.isAlarmOn = true;
        dataItem.isVibrationAllowed = true;
        return dataItem;
    }

    private DataItem testData2() {
        DataItem dataItem = new DataItem();
        dataItem.description = "description2";
        dataItem.checkedDays = new boolean[]{false, false, false, false, false, false, true};
        dataItem.timeBegin = new int[] { 14, 40 };
        dataItem.timeEnd = new int[] { 15, 50 };
        dataItem.isAlarmOn = false;
        dataItem.isVibrationAllowed = false;
        return dataItem;
    }


}
