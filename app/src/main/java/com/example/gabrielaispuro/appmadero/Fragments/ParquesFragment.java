package com.example.gabrielaispuro.appmadero.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.Activity.MainActivity;
import com.example.gabrielaispuro.appmadero.Activity.MapsActivity;
import com.example.gabrielaispuro.appmadero.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParquesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParquesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParquesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private ImageView imgView_nearest;
    private View mProgressView;
    private ListView listViewParques;
    private TextView txtView_nearest;
    private ParquesTask mParquesTask;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ParquesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParquesFragment newInstance(int param1) {
        ParquesFragment fragment = new ParquesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ParquesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParam1 = getArguments().getString(ARG_PARAM1);

        View v = inflater.inflate(R.layout.fragment_parques, container, false);
        mProgressView = v.findViewById(R.id.progressBar_parques);
        imgView_nearest = (ImageView) v.findViewById(R.id.imgView_nearest_parques);
        txtView_nearest = (TextView) v.findViewById(R.id.lbl_nearest_parques);
        listViewParques = (ListView) v.findViewById(R.id.list_parques);

        mParquesTask = new ParquesTask();
        mParquesTask.execute();
        showProgress(true);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_PARAM1));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            listViewParques.setVisibility(show ? View.GONE : View.VISIBLE);
            imgView_nearest.setVisibility(show ? View.GONE : View.VISIBLE);
            txtView_nearest.setVisibility(show ? View.GONE : View.VISIBLE);
            listViewParques.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listViewParques.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            listViewParques.setVisibility(show ? View.GONE : View.VISIBLE);
            imgView_nearest.setVisibility(show ? View.GONE : View.VISIBLE);
            txtView_nearest.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Parece que tu GPS está deshabilitado, ¿Quieres habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public class ParquesTask extends AsyncTask<Void,  Void, List<ParseObject>> {

        List<ParseObject> result;
        List<ParseObject> listImage;
        ArrayList<ParseObject> listImagePrincipal = new ArrayList<ParseObject>();

        @Override
        protected List<ParseObject> doInBackground(Void... params) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Lugar");
            ParseQuery<ParseObject> queryImage = ParseQuery.getQuery("Imagen");
            query.whereEqualTo("tipoLugar", "Parque");
            query.orderByAscending("nombre");
            query.include("ubicacion");

            //Getting data from parse.com
            try {

                result = query.find();

                queryImage.whereMatchesQuery("idLugar", query);
                queryImage.orderByAscending("nombre");
                listImage = queryImage.find();

                for(ParseObject object : listImage){

                    if(object.getString("nombre").contains("principal")){
                        listImagePrincipal.add(object);
                    }

                }

                try {
                    // Simulate network access.
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(), "Error doInBackground", Toast.LENGTH_SHORT).show();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(final List<ParseObject> result){

            ListCDAdapter listAdapter = new ListCDAdapter(getActivity(), result, listImagePrincipal);
            listViewParques.setAdapter(listAdapter);
            showProgress(false);

            listViewParques.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ShowParquesFragment fragment = ShowParquesFragment.newInstance(3, result.get(position).getString("nombre"));
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment);
                    ft.addToBackStack("replacingCDFragment");
                    ft.commit();

                }
            });

            imgView_nearest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {

                        double lat[] = new double[result.size()];
                        double lng[] = new double[result.size()];
                        String names[] = new String[result.size()];
                        ParseObject place;
                        ParseObject ubicacion = new ParseObject("Ubicacion");

                        //Getting the coordinates from all places in the coord list
                        for (int i = 0; i < result.size(); i++) {

                            place = result.get(i);
                            ubicacion = place.getParseObject("ubicacion");

                            lat[i] = ubicacion.getParseGeoPoint("geoPoint").getLatitude();
                            lng[i] = ubicacion.getParseGeoPoint("geoPoint").getLongitude();
                            names[i] = place.getString("nombre");

                        }

                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MapsActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lng);
                        intent.putExtra("method", "nearest");
                        intent.putExtra("names", names);
                        startActivity(intent);
                    }
                }
            });

        }
    }

}
