package com.example.meangirl.dictionary.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.meangirl.dictionary.EditDialog;
import com.example.meangirl.dictionary.ExpandableListAdapter;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;

import java.util.HashMap;
import java.util.List;

public class BrowseContent extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private HashMap<String,String> dictionaryData;
    private ExpandableListView dictionaryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_list);
        Intent intent = getIntent();
        HashMap<String,String> listHashMap = (HashMap<String, String>) intent.getSerializableExtra("itemList");
        dictionaryListView = findViewById(R.id.dictList);

        if(listHashMap != null){
        if(!listHashMap.isEmpty()) {
            dictionaryData = listHashMap;
        }
        } else {
            initData();
        }

        listView = findViewById(R.id.dictList);
        listAdapter = new ExpandableListAdapter(this, dictionaryData);
        listView.setAdapter(listAdapter);

        /**
         * setting longclick listner for the list item child for the delete popup to appear
         */
        dictionaryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ExpandableListView browseContentList = (ExpandableListView) parent;
                long pos = browseContentList.getExpandableListPosition(position);
                int itemType = browseContentList.getPackedPositionType(pos); //item type
                final int parentPos = browseContentList.getPackedPositionGroup(pos); //term position
                int childPos = browseContentList.getPackedPositionChild(pos); //definition position
                //if child is clicked:
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                    //getting text values
                    String term = (String) listAdapter.getGroup(parentPos);
                    String definition = (String) listAdapter.getChild(parentPos, childPos);

                    //Activity a, String itemText, String itemDefinition, int itemId
                    EditDialog editDialog = new EditDialog(BrowseContent.this, term, definition, parentPos );
                    editDialog.show();

                    /**
                     * listner for the dialog, so that, once it is dismissed, data can be updated to reflect the changes
                     */
                    editDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            initData();
                            listAdapter = new ExpandableListAdapter(BrowseContent.this, dictionaryData);
                            listView.setAdapter(listAdapter);
                        }
                    });



                    //******************************************************* ovo je deo koji radi ******************************************************************************
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(BrowseContent.this);
//                    builder1.setMessage("Are you sure you want to delete selected item from the dictionary?");
//                    builder1.setCancelable(true);
//                    builder1.setPositiveButton(
//                            "DELETE",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    DatabaseHelper dbHelper = new DatabaseHelper(BrowseContent.this);
//                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
//                                    String termName = (String) listAdapter.getGroup(parentPos);
////                                    String query = "DELETE * FROM " + Contract.TermsSchema.TABLE_NAME + " WHERE " + Contract.TermsSchema.FIELD_TERM + " = " + termName;
//                                    db.delete(Contract.TermsSchema.TABLE_NAME, Contract.TermsSchema.FIELD_TERM + " = '" + termName + "';", null);
////                                    db.rawQuery(query, null);
//                                    db.close();
//                                    dialog.cancel();
//                                    initData();
//                                    listAdapter = new ExpandableListAdapter(BrowseContent.this, dictionaryData);
//                                    listView.setAdapter(listAdapter);
//                                }
//                            });
//                    builder1.setNegativeButton(
//                            "CANCEL",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
                }

                return true;
            }
        });

    }

    /**
     * Method to fill in all the data for expandable list view
     */
    void initData() {

        dictionaryData = new HashMap<>();

        //filling in the hash map:
        String searchQuery = "SELECT * FROM " + Contract.TermsSchema.TABLE_NAME;
        DatabaseHelper dbHelper = new DatabaseHelper(BrowseContent.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {
            do {
                // Passing values
                String term = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_TERM));
                String definition = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_DEFINITION));
                dictionaryData.put(term,definition);
            } while (c.moveToNext());
            //closing db connection
            c.close();
            db.close();
        }

    }

}
