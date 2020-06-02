package com.example.complaintapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Tabs_Accessor_Adapter extends FragmentPagerAdapter {
    String User_Type;

    public Tabs_Accessor_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
            if (User_Type.equals("Citizen")) {
                switch (position) {
                    case 0:
                        Citizen_History_Pending_Fragment vCitizen_History_Pending_Fragment = new Citizen_History_Pending_Fragment();
                        return vCitizen_History_Pending_Fragment;

                    case 1:
                        Citizen_History_On_The_Job_Fragment vCitizen_History_On_The_Job_Fragment = new Citizen_History_On_The_Job_Fragment();
                        return vCitizen_History_On_The_Job_Fragment;

                    case 2:
                        Citizen_History_Resolved_Fragment vCitizen_History_Resolved_Fragment = new Citizen_History_Resolved_Fragment();
                        return vCitizen_History_Resolved_Fragment;
                }
            } else {
                switch (position) {
                    case 0:
                        Corporation_Home_Pending_Fragment vCorporation_Home_Pending_Fragment = new Corporation_Home_Pending_Fragment();
                        return vCorporation_Home_Pending_Fragment;

                    case 1:
                        Corporation_Home_On_The_Job_Fragment vCorporation_Home_On_The_Job_Fragment = new Corporation_Home_On_The_Job_Fragment();
                        return vCorporation_Home_On_The_Job_Fragment;

                    case 2:
                        Corporation_Home_Resolved_Fragment vCorporation_Home_Resolved_Fragment = new Corporation_Home_Resolved_Fragment();
                        return vCorporation_Home_Resolved_Fragment;
                }
            }
        return  null;
    }

    @Override
    public int getCount() {
            return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pending";

                case 1:
                    return "On_The_Job";

                case 2:
                    return "Resolved";

                default:
                    return null;
            }
    }
}
