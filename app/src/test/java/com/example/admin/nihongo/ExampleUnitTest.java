package com.example.admin.nihongo;

import android.content.Context;
import android.test.mock.MockContext;

import com.example.admin.nihongo.model.Word;
import com.example.admin.nihongo.util.DatabaseOperation;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testDb() {
        Context context = new MockContext();
        DatabaseOperation.init(context, "words.db", 3);
        List<Word> list = DatabaseOperation.getAllWords();
        System.out.println();
    }
}