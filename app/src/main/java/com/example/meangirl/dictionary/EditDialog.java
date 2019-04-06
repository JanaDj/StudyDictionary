package com.example.meangirl.dictionary;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;

import java.util.List;

public class EditDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity a;
    public Dialog d;
    EditText titleET, definitionET;
    public Button delete, edit;
    FloatingActionButton close;
    String itemText, itemDefinition;
    int itemId;

    public EditDialog(Activity a, String itemText, String itemDefinition, int itemId) {
        super(a);
        this.itemText = itemText;
        this.itemDefinition = itemDefinition;
        this.itemId = itemId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_item);


        edit = findViewById(R.id.editBtn);
        delete = findViewById(R.id.delBtn);
        close = findViewById(R.id.closeBtn);
        titleET = findViewById(R.id.editTitleET);
        definitionET = findViewById(R.id.editDefinitionET);

        //populating data for edit texts:
        titleET.setText(itemText);
        definitionET.setText(itemDefinition);

        //setting onclick listners
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        close.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.editBtn:
                if(!TextUtils.isEmpty(titleET.getText()) && !TextUtils.isEmpty(definitionET.getText())) {
                    //filling contentValues
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.TermsSchema.FIELD_TERM, titleET.getText().toString());
                    contentValues.put(Contract.TermsSchema.FIELD_DEFINITION, definitionET.getText().toString());
                    //updating
                   db.update(Contract.TermsSchema.TABLE_NAME, contentValues,Contract.TermsSchema.FIELD_TERM + "= '" + itemText + "'", null);


                    showToast("Item edited successfully!");
                } else {
                    showToast("Something went wrong. It looks like not all of the fields have been filled in. Please fill in all the fields and try again!");
                }
                dismiss();
                break;
            case R.id.delBtn:
                db.delete(Contract.TermsSchema.TABLE_NAME, Contract.TermsSchema.FIELD_TERM + " = '" + itemText + "'", null);
                dismiss();
                break;
            case R.id.closeBtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data,  Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}

