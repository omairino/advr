package com.example.lenovo.start.Models;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lenovo.start.R;

import java.util.List;



public class ArrayListClass extends ArrayAdapter<userInfo>{
    public Activity context;
    public List<userInfo>list;

    public ArrayListClass(Activity context,List<userInfo>list){
        super(context, R.layout.places,list);
        this.context=context;
        this.list=list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View listViewItem=layoutInflater.inflate(R.layout.places,null,true);

        TextView nameTxt=(TextView)listViewItem.findViewById(R.id.name);
        TextView propertyTxt=(TextView)listViewItem.findViewById(R.id.property);
        TextView addTxt=(TextView)listViewItem.findViewById(R.id.address);
        TextView numTxt=(TextView)listViewItem.findViewById(R.id.number);
        TextView descTxt=(TextView)listViewItem.findViewById(R.id.desc);

        userInfo user=list.get(position);


        propertyTxt.setText(user.getProperty());
        nameTxt.setText(user.getUsername());
        addTxt.setText(user.getAddress());
        numTxt.setText(user.getNumber());
        descTxt.setText(user.getdescription());

        return listViewItem;


    }


}
