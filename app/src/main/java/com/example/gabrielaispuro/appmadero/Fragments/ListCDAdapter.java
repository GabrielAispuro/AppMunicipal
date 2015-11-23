package com.example.gabrielaispuro.appmadero.Fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gabrielaispuro.appmadero.R;
import com.parse.GetDataCallback;
import com.parse.ParseImageView;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Aispuro on 27/05/2015.
 */
public class ListCDAdapter extends BaseAdapter{

    protected List<ParseObject> result;
    protected ArrayList<ParseObject> listImages;
    private Context context;
    LayoutInflater inflater;

    public ListCDAdapter(Context context, List<ParseObject> result, ArrayList<ParseObject> listImages){

        this.result = result;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listImages = listImages;

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.layout_list_item, parent, false);

            holder.txtTitulo = (TextView) convertView.findViewById(R.id.txt_title);
            holder.img = (ParseImageView) convertView.findViewById(R.id.img_news);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject centroDeportivo = result.get(position);
        ParseObject imgParse = listImages.get(position);
        holder.txtTitulo.setText(centroDeportivo.getString("nombre"));
        holder.img.setParseFile(imgParse.getParseFile("imagen"));
        holder.img.loadInBackground(new GetDataCallback(){

                @Override
                public void done(byte[] bytes, com.parse.ParseException e) {
                    Log.i("ParseImageView", "Data length " + bytes.length);
                }
            }
        );

        return convertView;
    }

    private class ViewHolder{
        TextView txtTitulo;
        ParseImageView img;
    }

}