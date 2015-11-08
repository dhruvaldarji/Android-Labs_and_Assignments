package com.ddarji.assignment2.birthdaycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.Date;

public class BirthdayAdapter extends ArrayAdapter {

    private ArrayList<BirthdayItem> data;
    private Context context;

    public BirthdayAdapter(Context context, int resource, ArrayList<BirthdayItem> data) {
        super(context, resource, data);
        this.data = data;
        this.context = getContext();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public BirthdayItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BirthdayItem item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.package_row, parent, false);
            holder = new ViewHolder();
            holder.idText = (TextView) convertView.findViewById(R.id.textID);
            holder.nameText = (TextView) convertView.findViewById(R.id.textName);
            holder.birthdayText = (TextView) convertView.findViewById(R.id.textBirthday);
            holder.editButton = (Button) convertView.findViewById(R.id.editButton);
            holder.deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView) parent).recycle(convertView, position);

        String i = item.getId() + " : ";
        String n = item.getName() + " - ";
        String b = item.getBirthday().toString();

        holder.idText.setText(i);
        holder.nameText.setText(n);
        holder.birthdayText.setText(b);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton(position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButton(position);
            }
        });

        return convertView;
    }

    public void editButton(int p) {
        int position = p;
//        Toast.makeText(context, R.string.editBirthday + "@ "+position, Toast.LENGTH_SHORT).show();
    }

    public void deleteButton(int p) {
        int position = p;
//        Toast.makeText(context, R.string.delete_birthday+ "@ "+position, Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder {
        TextView idText;
        TextView nameText;
        TextView birthdayText;
        Button editButton;
        Button deleteButton;
    }
}
