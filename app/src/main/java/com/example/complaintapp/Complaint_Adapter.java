package com.example.complaintapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Complaint_Adapter extends RecyclerView.Adapter<Complaint_Adapter.Complaint_View_Holder> {

    public List<Complaint> Complaint_List;
    DatabaseReference databaseReference;
    String Complaint_Status;
    String User_Type;

    public Complaint_Adapter (List<Complaint> complaint_List,String complaint_Status,String user_Type){
        this.Complaint_List = complaint_List;
        this.Complaint_Status = complaint_Status;
        this.User_Type = user_Type;
    }

    public static class Complaint_View_Holder extends RecyclerView.ViewHolder{

        String Complaint_Status;
        public static TextView vComplaint_User_Name,vComplaint_Last_Seen;
        public static TextView vComplaint_Type,vComplaint_Address;
        public static CircleImageView vComplaint_Citizen_Profile_Image,vComplaint_Type_Image;
        //String Sender_Id,Reciever_Id,Complaint_Id;
        //public static TextView vResolved_Complaint_layout_User_name,vResolved_Complaint_layout_Address;

        public Complaint_View_Holder(@NonNull View itemView,String complaint_Status) {
            super(itemView);
            this.Complaint_Status = complaint_Status;

                switch (complaint_Status){
                    case "Pending":
                        vComplaint_User_Name = (TextView) itemView.findViewById(R.id.Pending_Complaint_layout_User_name);
                        vComplaint_Address = (TextView) itemView.findViewById(R.id.Pending_Complaint_layout_Address);
                        vComplaint_Citizen_Profile_Image = (CircleImageView) itemView.findViewById(R.id.Pending_Complaint_layout_Profile_Image);
                        vComplaint_Type = (TextView) itemView.findViewById(R.id.Pending_Complaint_layout_Complaint_type);
                        vComplaint_Last_Seen = (TextView) itemView.findViewById(R.id.Pending_Complaint_layout_Time);
                        vComplaint_Type_Image = (CircleImageView) itemView.findViewById(R.id.Pending_Complaint_layout_Complaint_Image);
                        return;

                    case "Resolved":
                        vComplaint_User_Name = (TextView) itemView.findViewById(R.id.Resolved_Complaint_layout_User_name);
                        vComplaint_Address = (TextView) itemView.findViewById(R.id.Resolved_Complaint_layout_Address);
                        vComplaint_Citizen_Profile_Image = (CircleImageView) itemView.findViewById(R.id.Resolved_Complaint_layout_Profile_Image);
                        vComplaint_Type = (TextView) itemView.findViewById(R.id.Resolved_Complaint_layout_Complaint_type);
                        vComplaint_Last_Seen = (TextView) itemView.findViewById(R.id.Resolved_Complaint_layout_Time);
                        vComplaint_Type_Image = (CircleImageView) itemView.findViewById(R.id.Resolved_Complaint_layout_Complaint_Image);
                        return;

                    case "On_The_Job":
                        vComplaint_User_Name = (TextView) itemView.findViewById(R.id.On_The_Job_Complaint_layout_User_name);
                        vComplaint_Address = (TextView) itemView.findViewById(R.id.On_The_Job_Complaint_layout_Address);
                        vComplaint_Citizen_Profile_Image = (CircleImageView) itemView.findViewById(R.id.On_The_Job_Complaint_layout_Profile_Image);
                        vComplaint_Type = (TextView) itemView.findViewById(R.id.On_The_Job_Complaint_layout_Complaint_type);
                        vComplaint_Last_Seen = (TextView) itemView.findViewById(R.id.On_The_Job_Complaint_layout_Time);
                        vComplaint_Type_Image = (CircleImageView) itemView.findViewById(R.id.On_The_Job_Complaint_layout_Complaint_Image);
                        return;
                }
        }
    }

    @NonNull
    @Override
    public Complaint_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        

            switch (Complaint_Status){
                case "Pending":
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pending_complaint_layout,parent,false);
                    return new Complaint_View_Holder(view,"Pending");

                case "Resolved":
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.resolved_complaint_layout,parent,false);
                    return new Complaint_View_Holder(view,"Resolved");

                case "On_The_Job":
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.on_the_job_complaint_layout,parent,false);
                    return new Complaint_View_Holder(view,"On_The_Job");

                default:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.resolved_complaint_layout,parent,false);
            }


        return new Complaint_View_Holder(view,"");
        /*View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resolved_complaint_layout,parent,false);
        fauth = FirebaseAuth.getInstance();

        return new Complaint_View_Holder(view,"Pending");*/
    }

    @Override
    public void onBindViewHolder(@NonNull Complaint_View_Holder holder, final int position) {

        Complaint complaint = Complaint_List.get(position);
        String Sender_Id,Reciever_Id,Complaint_Id;
        Sender_Id = complaint.Citizen_User_Id;
        Reciever_Id = complaint.Corporation_User_Id;
        Complaint_Id = complaint.Complaint_Id;
        String Citizen_User_Name = complaint.Citizen_User_Name;
        String Complaint_Address = complaint.Address;
        String Complaint_Type = complaint.Type;
        String Complaint_Date = complaint.date;
        String Complaint_Description = complaint.Description;
        String Complaint_Image_Url = complaint.Image_Url;
        String Complaint_Sender_Id = complaint.Citizen_User_Id;

        int icon = getResId("ic_" + Complaint_Type,R.drawable.class);
        int defaultIcon =getResId("default_profile_image",R.drawable.class);
        Complaint_View_Holder.vComplaint_Type_Image.setImageResource(defaultIcon);
        if(icon != -1){
            Complaint_View_Holder.vComplaint_Type_Image.setImageResource(icon);
        }
        Complaint_View_Holder.vComplaint_User_Name.setText(Citizen_User_Name);
        Complaint_View_Holder.vComplaint_Last_Seen.setText(Complaint_Date);
        Complaint_View_Holder.vComplaint_Type.setText(Complaint_Type);
        Complaint_View_Holder.vComplaint_Address.setText(Complaint_Address);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Citizen").child(Complaint_Sender_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String Complaint_Citizen_Profile_Image_Url = dataSnapshot.child("Profile_Image_Url").getValue().toString();
                    if (!Complaint_Citizen_Profile_Image_Url.equals("")) {
                        Picasso.get().load(Complaint_Citizen_Profile_Image_Url).into(Complaint_View_Holder.vComplaint_Citizen_Profile_Image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Bundle bundle = new Bundle();

        bundle.putString("Citizen_User_Id",Sender_Id);
        bundle.putString("Corporation_User_Id",Reciever_Id);
        bundle.putString("Complaint_Id",Complaint_Id);
        bundle.putString("User_Type",User_Type);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context;
                Intent intent;
                    switch (Complaint_Status){
                        case "Pending":
                                context = v.getContext();
                                intent = new Intent(context,Complaint_Pending_Full.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            return;

                        case "Resolved":
                                    context = v.getContext();
                                    intent = new Intent(context,Complaint_Resolved_Full.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                            return;

                        case "On_The_Job":
                                    context = v.getContext();
                                    intent = new Intent(context,Complaint_On_The_Job_Full.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                            return;
                    }

            }
        });

        /*if(this.User_Type.equals("Citizen")){
            switch (this.Complaint_Status){
                case "Pending":
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context,Complaint_Pending_Full.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    return;

                case "Resolved":
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context,Complaint_Resolved_Full.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    return;

                case "On_The_Job":
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context,Complaint_On_The_Job_Full.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    return;
            }
        }
        else if(this.User_Type.equals("Corporation")){
            switch (this.Complaint_Status){
                case "Pending":
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context,Complaint_Pending_Full.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    return;

                case "Resolved":
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context,Complaint_Resolved_Full.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    return;

                case "On_The_Job":
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context,Complaint_On_The_Job_Full.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    return;
            }
        }*/



    }



    @Override
    public int getItemCount() {
        return Complaint_List.size();
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
