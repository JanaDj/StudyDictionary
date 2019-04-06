package com.example.meangirl.dictionary;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String,String> listHashMap;

    public ExpandableListAdapter(Context context, HashMap<String, String> listHashMap) {
        this.context = context;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listHashMap.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }
    @Override
    public Object getGroup(int i) {
        return listHashMap.keySet().toArray()[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listHashMap.keySet().toArray()[i]);
        //i = group item
        //i1 = child item
    }
    @Override
    public long getGroupId(int i) {
        return i;
    }
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }
        TextView listHeader = view.findViewById(R.id.listHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);
        return view;
    }
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i,i1);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,null);
        }
        TextView listItem = view.findViewById(R.id.listItem);
        listItem.setText(childText);
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
