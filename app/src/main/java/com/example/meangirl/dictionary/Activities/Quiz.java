package com.example.meangirl.dictionary.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    HashMap<String, String> listHashMap;
    TextView quizItemTV;
    ToggleButton answerA, answerB, answerC;
    Button showAnswerBtn, submitBtn, nextQuestionBtn, homeBtn;
    String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        connectWithXML();
        initData();

//        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(answerA, 1, 17, 1,
//                TypedValue.COMPLEX_UNIT_DIP);
//        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(answerB, 1, 17, 1,
//                TypedValue.COMPLEX_UNIT_DIP);
//        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(answerC, 1, 17, 1,
//                TypedValue.COMPLEX_UNIT_DIP);


        //quiz can only work if there are at least 3 items in the item table so if there are less than 3 items, quiz will not work
        if (listHashMap.isEmpty() || listHashMap.size() < 3) {
            quizItemTV.setText("At least 3 items required");
            showAnswerBtn.setEnabled(false);
            submitBtn.setEnabled(false);
            nextQuestionBtn.setEnabled(false);
            answerA.setEnabled(false);
            answerB.setEnabled(false);
            answerC.setEnabled(false);
            Toast.makeText(this, "Quiz feature can only work once 3 or more items have been added to the dictionary. Please add more items and try again.", Toast.LENGTH_LONG).show();
        }else {
            populateQuizItem(randomNumberGenerator());
        }

        //on click listners
        /**
         * On click listner for home button. When this button is clicked, user is returned to the home screen
         */
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youAreCorrect = "Correct!";
                if(answerA.isChecked() && answerA.getTextOff().toString().equals(correctAnswer)){
                    answerA.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Toast.makeText(Quiz.this, youAreCorrect, Toast.LENGTH_SHORT).show();
                }
                else if(answerB.isChecked() && answerB.getTextOff().toString().equals(correctAnswer)){
                    answerB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Toast.makeText(Quiz.this, youAreCorrect, Toast.LENGTH_SHORT).show();
                }
                else if(answerC.isChecked() && answerC.getTextOff().toString().equals(correctAnswer)){
                    answerC.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Toast.makeText(Quiz.this, youAreCorrect, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Quiz.this, "Woops. It looks like you have not found the correct answer.Please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /**
         * On click listner for next question. WHen this button is clicked, random question is generated and displayed
         */
        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateQuizItem(randomNumberGenerator());
                resetAnswers();
            }
        });

        showAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAnswers();
                if(answerA.getTextOff().toString().equals(correctAnswer)){
                    answerA.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else if(answerB.getTextOff().toString().equals(correctAnswer)){
                    answerB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else if(answerC.getTextOff().toString().equals(correctAnswer)){
                    answerC.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
            }
        });

    }
    /**
     * Method that populates the quiz with item, correct answer and two false answers
     * @param i int, this is a number of the item that should be displayhed
     */
    void populateQuizItem(int i){
        quizItemTV.setText(listHashMap.keySet().toArray()[i].toString());

        correctAnswer = listHashMap.get(listHashMap.keySet().toArray()[i].toString()); //we save the correct answer
        ArrayList<String> answers = new ArrayList();
        answers.add(correctAnswer);


        //to avoid the same answer repeating twice we need to check what random answer was assigned:
        String falseAnswer ="";
        while(true) {
             falseAnswer = listHashMap.get(listHashMap.keySet().toArray()[randomNumberGenerator()].toString());
            if (!falseAnswer.equals(correctAnswer)) {
                answers.add(falseAnswer);
                break;
            }
        }
        String falseAnswer2 ="";
        while(true) {
            falseAnswer2 = listHashMap.get(listHashMap.keySet().toArray()[randomNumberGenerator()].toString());
            if (!falseAnswer2.equals(correctAnswer) && !falseAnswer2.equals(falseAnswer)) {
                answers.add(falseAnswer2);
                break;
            }
        }

        //shuffling and assinging the answers to the text views
        Collections.shuffle(answers);
        answerA.setText(answers.get(0));
        answerB.setText(answers.get(1));
        answerC.setText(answers.get(2));
        answerA.setTextOff(answers.get(0));
        answerB.setTextOff(answers.get(1));
        answerC.setTextOff(answers.get(2));
        String pickedTExt = "Selected";
        answerA.setTextOn(pickedTExt);
        answerB.setTextOn(pickedTExt);
        answerC.setTextOn(pickedTExt);
    }

    /**
     * Method to reset color of the toggle buttons
     */
    void resetAnswers(){
        answerA.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        answerB.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        answerC.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        answerA.setChecked(false);
        answerB.setChecked(false);
        answerC.setChecked(false);
    }

    /**
     * Method that generates a random number in the range of the items hashMap
     * @return int, returns the generated random number
     */
    int randomNumberGenerator(){
        int max = listHashMap.size()-1;
        int min = 0;
        int random = new Random().nextInt((max - min) + 1) + min;
        return random;
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
     * Method to connect XML components with java code
     */
    void connectWithXML(){
        listHashMap = new HashMap<>();
        correctAnswer = "";
        quizItemTV = findViewById(R.id.quizItemTV);
        answerA = findViewById(R.id.answerA);
        answerB = findViewById(R.id.answerB);
        answerC = findViewById(R.id.answerC);
        showAnswerBtn = findViewById(R.id.showAnswerBtn);
        nextQuestionBtn = findViewById(R.id.nextQuestionBtn);
        homeBtn = findViewById(R.id.homeBtn);
        submitBtn = findViewById(R.id.submitBtn);

    }
}
