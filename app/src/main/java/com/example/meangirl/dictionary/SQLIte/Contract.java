package com.example.meangirl.dictionary.SQLIte;

import android.provider.BaseColumns;

public class Contract {
    //every table would represent an inner class
    public static final  class TermsSchema implements BaseColumns {
        //table name
        public static final String TABLE_NAME = "dictionary_values";
        //columns
        public static final String _ID = BaseColumns._ID;
        public static final String FIELD_TERM = "term";
        public static final String FIELD_DEFINITION = "definition";
    }
}
