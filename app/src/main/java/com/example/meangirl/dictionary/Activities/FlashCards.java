package com.example.meangirl.dictionary.Activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;

import java.util.HashMap;
import java.util.Random;

public class FlashCards extends AppCompatActivity {
    HashMap<String, String> listHashMap;
    TextView termTV, definitionTV;
    Button prevBtn, nextBtn, randomBtn;
    int cardCount;
    int definitionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_cards);
        connectWithXML();
        initData();

        populateFlashCard(cardCount); //at the start, first item from the list is being displayed

        /**
         * On click listner for the previous button
         * It checks the length of the hashMap and if first item is currently displayed, previous button will display the last item from the hashmap, othervise it will display previous item
         */
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardCount == 0){
                    //card count should be set to the last item
                    cardCount = listHashMap.size()-1;
                    populateFlashCard(cardCount);  //we display the last item

                } else {
                    populateFlashCard(--cardCount);
                }
            }
        });
        /**
         * On click listner for the next button
         * It checks the length of the hashmap and if at the last item, it will show the firstone, otherwise it will just show next item in the hashMap
         */
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardCount == listHashMap.size()-1){
                    cardCount = 0;
                    populateFlashCard(cardCount);
                }
                else {
                    populateFlashCard(++cardCount);
                }
            }
        });
        /**
         * On click listner for the random button
         * On click, this button will display a random flash card details
         */
        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = listHashMap.size()-1;
                int min = 0;
                int random = new Random().nextInt((max - min) + 1) + min;
                populateFlashCard(random);
                cardCount = random;

            }
        });


        definitionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                definitionTV.setText(listHashMap.get(listHashMap.keySet().toArray()[definitionIndex])); //getting value for the key as the definition of the term
                definitionTV.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                definitionTV.setTextColor(Color.WHITE);
            }
        });




    }

    void populateFlashCard(int i){
        //key is set as item name (Term)
        termTV.setText(listHashMap.keySet().toArray()[i].toString());
        //definition should be hidden by default:
        definitionTV.setTextColor(getResources().getColor(R.color.colorPrimary));
        definitionTV.setText("To display definition of the Term, click me!");
        definitionTV.setBackgroundColor(Color.rgb(255, 255, 179));
        definitionIndex = i;

    }
    /**
     * Method to get data from sql regarding the items added to dictionary
     * Data is written into a HashMap
     */
    void initData() {
        String searchQuery = "SELECT * FROM " + Contract.TermsSchema.TABLE_NAME;
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);
        if (c.moveToFirst()) {
            do {
                // Passing values
                String term = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_TERM));
                String definition = c.getString(c.getColumnIndex(Contract.TermsSchema.FIELD_DEFINITION));
                listHashMap.put(term, definition);
            } while (c.moveToNext());
        }
    }
    /**
     * Method to connect XML components with Java code
     */
    void connectWithXML(){
        cardCount = 0;
        listHashMap = new HashMap<>();
        definitionTV = findViewById(R.id.definitionET);
        termTV = findViewById(R.id.termTV);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        randomBtn = findViewById(R.id.randomBtn);
    }
}
