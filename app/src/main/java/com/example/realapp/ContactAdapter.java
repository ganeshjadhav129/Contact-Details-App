package com.example.realapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context contextt;
    private List<Contact> people;
    public ContactAdapter(Context context,List<Contact>list) {
        super(context,R.layout.root_layout,list);
        this.contextt=context;
        this.people=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)contextt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.root_layout,parent,false);
        TextView tvResultA=convertView.findViewById(R.id.tvResultA),tvName=convertView.findViewById(R.id.tvNameResult);
        TextView tvNumber=convertView.findViewById(R.id.tvNumberResult);
        tvResultA.setText(people.get(position).getName().toUpperCase().charAt(0)+"");
        tvName.setText(people.get(position).getName());
        tvNumber.setText(people.get(position).getNumber());
        return convertView;
    }
}
