package com.example.gabrielaispuro.appmadero.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.Activity.MainActivity;
import com.example.gabrielaispuro.appmadero.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowParquesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowParquesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowParquesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private View view;
    private View scroll;
    private View mProgressView;
    private TextView txtNombre;
    private ParseImageView img;
    private ParseImageView imgInstalaciones;
    private TextView txtDireccion;
    private TextView txtDescripcion;
    private ImageView imgView_maps;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private static String nombreComplejo;
    private ParquesShowTask mShowTask;
    private int cont = 1;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShowParquesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowParquesFragment newInstance(int param1, String titulo) {
        ShowParquesFragment fragment = new ShowParquesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        nombreComplejo = titulo;
        return fragment;
    }

    public ShowParquesFragment() {
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

        view = inflater.inflate(R.layout.fragment_show_parques, container, false);
        mProgressView = view.findViewById(R.id.progressBar_parquesShow);
        scroll = view.findViewById(R.id.scroll);
        txtNombre = (TextView) view.findViewById(R.id.txtView_tituloParques);
        img = (ParseImageView) view.findViewById(R.id.parseImageView_img_principal);
        txtDireccion = (TextView) view.findViewById(R.id.txtView_direccionParques);
        txtDescripcion = (TextView) view.findViewById(R.id.txtView_descripcion);
        leftArrow = (ImageView) view.findViewById(R.id.imgView_leftArrow);
        imgInstalaciones = (ParseImageView) view.findViewById(R.id.parseImageView_img_instalaciones);
        rightArrow = (ImageView) view.findViewById(R.id.imgView_rightArrow);
        imgView_maps = (ImageView) view.findViewById(R.id.imgView_google_maps);

        mShowTask = new ParquesShowTask();
        mShowTask.execute();
        showProgress(true);

        return view;
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

            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
            scroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scroll.setVisibility(show ? View.GONE : View.VISIBLE);
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
            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

    public class ParquesShowTask extends AsyncTask<Void,  Void, List<ParseObject>> {

        List<ParseObject> result;
        List<ParseObject> resultImages;

        @Override
        protected List<ParseObject> doInBackground(Void... params) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Lugar").whereEqualTo("nombre", nombreComplejo);
            ParseQuery<ParseObject> queryImages = ParseQuery.getQuery("Imagen");
            query.include("ubicacion");
            queryImages.whereMatchesQuery("idLugar", query);

            try {

                result = query.find();
                resultImages = queryImages.find();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Toast.makeText(getActivity(), "Error doInBackground", Toast.LENGTH_SHORT).show();
            }

            return null;

        }

        @Override
        protected void onPostExecute(List<ParseObject> list){

            ParseObject complejo = result.get(0);
            ParseObject imgObject = resultImages.get(0);
            ParseObject ubicacion = new ParseObject("Ubicacion");
            ubicacion = complejo.getParseObject("ubicacion");

            txtNombre.setText(complejo.getString("nombre"));
            img.setParseFile(imgObject.getParseFile("imagen"));
            img.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    Log.i("ParseImageView", "Data length " + bytes.length);
                }
            });

            txtDireccion.setText(complejo.getString("direccion"));
            txtDescripcion.setText(complejo.getString("descripcion"));

            imgInstalaciones.setParseFile(resultImages.get(cont).getParseFile("imagen"));
            imgInstalaciones.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    Log.i("ParseImageView", "Data length " + bytes.length);
                }
            });

            showProgress(false);

            rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cont < (resultImages.size()-1)) {
                        cont++;
                        imgInstalaciones.setParseFile(resultImages.get(cont).getParseFile("imagen"));
                        imgInstalaciones.loadInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                Log.i("ParseImageView", "Data length " + bytes.length);
                            }
                        });
                    }
                }
            });

            leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cont > 1){
                        cont--;
                        imgInstalaciones.setParseFile(resultImages.get(cont).getParseFile("imagen"));
                        imgInstalaciones.loadInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                Log.i("ParseImageView", "Data length " + bytes.length);
                            }
                        });
                    }
                }
            });

            //using different variable to access to its data in the method below
            final ParseObject finalUbicacion = ubicacion;
            imgView_maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    double lat = finalUbicacion.getParseGeoPoint("geoPoint").getLatitude();
                    double lon = finalUbicacion.getParseGeoPoint("geoPoint").getLongitude();
                    String geopoint = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon;
                    Uri gmmIntentUri = Uri.parse(geopoint);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });

        }

    }

}
