package com.example.snailscurlup.listycity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<User> {

    private ArrayList<User> users;
    private Context context;

    public CustomList(Context context, ArrayList<User> users){
        super(context,0, users);
        this.users = users;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        User user = users.get(position);

        //ImageView image = view.findViewById(R.id.leader_image);
        TextView Name = view.findViewById(R.id.leader_name);
        TextView score = view.findViewById(R.id.leader_score);
        TextView leaderIndex = view.findViewById(R.id.leader_index);


        //image.setImageIcon(user.getUsername());
        Name.setText(user.getUsername());
        score.setText("Score: "+user.getEmail());
        leaderIndex.setText(Integer.toString(position+1));
        return view;

    }
}
