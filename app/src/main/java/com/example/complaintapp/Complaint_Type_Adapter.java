package com.example.complaintapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class Complaint_Type_Adapter extends ArrayAdapter<Complaint_Type_Data> {
    public Context context;
    public List<Complaint_Type_Data> Spinner_Datas;

    public Complaint_Type_Adapter(@NonNull Context context, int resource,List<Complaint_Type_Data> Spinner_Datas) {
        super(context, resource,Spinner_Datas);
        this.context = context;
        this.Spinner_Datas = Spinner_Datas;
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
        TextView Complaint_Type_Title = (TextView) customView.findViewById(R.id.Complaint_Type_Title);
        CircleImageView  Complaint_Type_Image = (CircleImageView) customView.findViewById(R.id.Complaint_Type_Image);

        Complaint_Type_Title.setText(Spinner_Datas.get(position).iconName);
        Complaint_Type_Image.setImageResource(Spinner_Datas.get(position).icon);

        return customView;
    }

    public void updateList(List<Complaint_Type_Data> l1){
        this.Spinner_Datas.clear();
        this.Spinner_Datas = l1;
        this.notifyDataSetChanged();
    }
}
