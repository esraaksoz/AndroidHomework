package com.example.esra.phonecontacthomework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Esra on 26.3.2016.
 */
public class PersonAdapter extends ArrayAdapter<Person> {


    public PersonAdapter(Context context, int resource, List<Person> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_item, null);
        }

        Person p = getItem(position);
        if (p != null) {
            ImageView personImage = (ImageView)v.findViewById(R.id.personImage);
            TextView personName = (TextView)v.findViewById(R.id.personName);
            TextView personNumber = (TextView)v.findViewById(R.id.personPhoneNumber);

            if(personImage != null && personName != null && personNumber != null){
                personName.setText(p.getPersonName());
                personNumber.setText(p.getPersonNumber());

            }
        }
        return v;
    }
}
