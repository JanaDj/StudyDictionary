package com.example.meangirl.dictionary;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class AddItemDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity a;
    public Dialog d;
    EditText titleET, definitionET;
    public Button cancel, ok;

    public AddItemDialog(Activity a) {
        super(a);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_item);
        ok = findViewById(R.id.okBtn);
        cancel = findViewById(R.id.cancelBtn);
        titleET = findViewById(R.id.titleET);
        definitionET = findViewById(R.id.definitionET);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okBtn:
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                if(!TextUtils.isEmpty(titleET.getText()) && !TextUtils.isEmpty(definitionET.getText())) {

                    //filling contentValues
                    contentValues.put(Contract.TermsSchema.FIELD_TERM, titleET.getText().toString());
                    contentValues.put(Contract.TermsSchema.FIELD_DEFINITION, definitionET.getText().toString());
                    //inserting into the db
                    db.insert(Contract.TermsSchema.TABLE_NAME, null, contentValues);
                    showToast("Item added successfully!");
                } else {
                    showToast("Something went wrong. It looks like not all of the fields have been filled in. Please fill in all the fields and try again!");
                }

                dismiss();
                break;
            case R.id.cancelBtn:
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
