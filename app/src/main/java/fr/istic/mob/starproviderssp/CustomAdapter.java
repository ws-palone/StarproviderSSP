package fr.istic.mob.starproviderssp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.istic.mob.starproviderssp.table.BusRoutes;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] txtcolor;
    String[] backcolor;
    String[] linedata;
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, String[] linedata, String[] backcolor,String[] txtcolor) {
        this.context = applicationContext;
        this.linedata = linedata;
        this.backcolor = backcolor;
        this.txtcolor = txtcolor;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return backcolor.length;
    }

    @Override
    public Object getItem(int i) {
        return linedata[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, null);
        if(backcolor[i]!=null) {
            String backcol = "#" + backcolor[i];
            String txtcol = "#" + txtcolor[i];
            view.setBackgroundColor(Color.parseColor(backcol));
            //La création d'un textview est obligatoire pour écrire un texte et changer sa couleur.
            TextView txtview = (TextView) view;
            txtview.setText(linedata[i]);
            txtview.setTextColor(Color.parseColor(txtcol));
        }
        return view;
    }
}
