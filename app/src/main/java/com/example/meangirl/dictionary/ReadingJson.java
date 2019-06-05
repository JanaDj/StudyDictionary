package com.example.meangirl.dictionary;

import android.content.Context;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadingJson {
    /**
     *
     * @param context
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static ArrayList<DictionaryItem> readDictJsonFile(Context context, String json) throws IOException, JSONException {

        JSONObject jsonRoot = new JSONObject(json);
        JSONArray jsonArray = jsonRoot.getJSONArray("terms");
        ArrayList<DictionaryItem> dictItems = new ArrayList<>();

        for(int i = 0; i <jsonArray.length(); i++){
            JSONObject obj = (JSONObject) jsonArray.get(i);
            String term = obj.getString("name");
            String definition = obj.getString("definition");
            DictionaryItem dictTerm = new DictionaryItem(term,definition);
            dictItems.add(dictTerm);
        }
        return dictItems;
    }

    /**
     *
     * @param is
     * @return
     */
    public static String readJsonFile(InputStream is){
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    /**
     *
     * @param context
     * @param resId
     * @return
     * @throws IOException
     */
    private static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br = new BufferedReader(new InputStreamReader((is)));

        StringBuilder sb = new StringBuilder();
        String s = null;
        while((s = br.readLine())!= null){
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
