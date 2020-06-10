package com.example.complaintapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class Corporation_List_Adapter extends ArrayAdapter<String> {
    public Context context;
    public List<String> Corporation_Keys;

    public Corporation_List_Adapter(@NonNull Context context, int resource,List<String> corporation_Keys) {
        super(context, resource,corporation_Keys);
        this.context = context;
        this.Corporation_Keys = corporation_Keys;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomeSpinnerView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomeSpinnerView(position, convertView, parent);
    }

    public View myCustomeSpinnerView(int position, @Nullable View myView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View customView = layoutInflater.inflate(R.layout.complaint_type_layout,parent,false);
        final CircleImageView Corporation_Profile_Image = (CircleImageView) customView.findViewById(R.id.Title_Image);
        final TextView Corporation_User_Name = (TextView) customView.findViewById(R.id.Title_Text);

        String key = Corporation_Keys.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = Objects.requireNonNull(dataSnapshot.child("User_name").getValue()).toString();
                    String url  = Objects.requireNonNull(dataSnapshot.child("Profile_Image_Url").getValue()).toString();
                    Corporation_User_Name.setText(name);
                    if(!url.equals("")){
                        Picasso.get().load(url).into(Corporation_Profile_Image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return customView;
    }
}
