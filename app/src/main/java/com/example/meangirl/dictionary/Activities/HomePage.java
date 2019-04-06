package com.example.meangirl.dictionary.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.meangirl.dictionary.AddItemDialog;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;
import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    Button searchBtn, browseBtn, addBtn, flashCardsBtn, quizBtn, manageDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        connectWithXML();

        //on click listeners for each button
        /**
         * On click listner for the search button
         * Clicking on this button shows a dialog box with text field to enter item that is being searched
         * By clicking ok, if the item is found user is redirected to the new view where he can view the item.
         * If nothing is found, toast msg is displayed instead
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            String textToSearch;
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("Title");
                // Set up the input
                final EditText input = new EditText(HomePage.this);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String,String> listHashMap = new HashMap<>();
                        textToSearch = input.getText().toString();

                        if (!TextUtils.isEmpty(textToSearch)) {
                            String searchQuery = "SELECT * FROM " + Contract.TermsSchema.TABLE_NAME +
                                    " WHERE " + Contract.TermsSchema.FIELD_TERM + " LIKE '%" + textToSearch + "%'";
                            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            Cursor c = db.rawQuery(searchQuery, null);
                            if (c.moveToFirst()) {
                                do {
                                    // Passing values
                                    String term = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_TERM));
                                    String definition = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_DEFINITION));
                                    listHashMap.put(term,definition);
                                } while (c.moveToNext());
                                Intent intent = new Intent(getBaseContext(), BrowseContent.class);
                                intent.putExtra("itemList", listHashMap);
                               startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "It looks like no item was found for your search. Please try the search again or add the missing item.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "It looks like you have not entered any value in the search box. Please try again!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        /**
         * This button redirects the user to the Expandable List View that will display all of the entered information
         */
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BrowseContent.class);
                startActivity(intent);

            }
        });

        /**
         * This button shows a popup dialog window where users can add an item into the sql
         */
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog addItemDialog = new AddItemDialog(HomePage.this);
                addItemDialog.show();

            }
        });

        /**
         * This button redirects user to the flashCards screen where they can browse the terms one by one
         */
        flashCardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlashCards.class);
                startActivity(intent);

            }
        });
        /**
         * This button redirects the user to the quiz screen
         */
        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Quiz.class);
                startActivity(intent);

            }
        });
        /**
         * This button redirects the user to the manage Data screen
         */
        manageDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageData.class);
                startActivity(intent);

            }
        });

    }

    /**
     * Method to connect XML components with java code
     */
    void connectWithXML(){
        searchBtn = findViewById(R.id.searchBtn);
        browseBtn = findViewById(R.id.browseBtn);
        addBtn = findViewById(R.id.addBtn);
        flashCardsBtn = findViewById(R.id.flashCardsBtn);
        quizBtn = findViewById(R.id.quizBtn);
        manageDataBtn = findViewById(R.id.manageDataBtn);

    }
}
