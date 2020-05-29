package com.myapp.jlam.motivationalbot.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myapp.jlam.motivationalbot.BuildConfig;
import com.myapp.jlam.motivationalbot.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment
{

    public static ListView listViewAbout;
    public static ArrayAdapter aAdapter;
    String resultString;
    public static String lastUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        listViewAbout = root.findViewById(R.id.listViewAbout);

        String[] users = {"App Version: " + BuildConfig.VERSION_NAME};

        ListView lv1 = root.findViewById(R.id.listViewAbout);
        aAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, users);
        lv1.setAdapter(aAdapter);


        return root;
    }

}
