package com.example.meangirl.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonWriter;
import android.widget.Toast;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class WritingJson {
    /**
     *
     * @param output
     * @throws IOException
     */
    public static void writejsonStream(OutputStream output, Context context) throws IOException {

        ArrayList<DictionaryItem> dictItems = getItemsFromDB(context);

        JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(output));


        jsonWriter.beginObject(); //begins root

        jsonWriter.name("terms").beginArray();    //starts the terms array
        for (DictionaryItem term: dictItems) {
            jsonWriter.beginObject();  //starts object (member of array)
            jsonWriter.name("name");
            jsonWriter.value(term.getTerm());

            jsonWriter.name("definition");
            jsonWriter.value(term.getDefinition());

            jsonWriter.endObject();  //ends object (member of array)
        }
        jsonWriter.endArray();  //ends array of terms
        jsonWriter.endObject(); //ends the root
        jsonWriter.close();
        Toast.makeText(context, jsonWriter.toString(), Toast.LENGTH_LONG).show();
    }
    /**
     *
     * @param context
     * @return
     */
    public static ArrayList<DictionaryItem> getItemsFromDB(Context context){
        ArrayList<DictionaryItem> itemList =  new ArrayList<>();
        String searchQuery = "SELECT * FROM " + Contract.TermsSchema.TABLE_NAME;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);
        if (c.moveToFirst()) {
            do {
                // Passing values
                String term = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_TERM));
                String definition = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_DEFINITION));
                DictionaryItem item = new DictionaryItem(term, definition);
                itemList.add(item);
            } while (c.moveToNext());
        }
        return itemList;
    }

}
