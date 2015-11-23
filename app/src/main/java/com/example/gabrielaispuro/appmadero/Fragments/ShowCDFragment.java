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
 * {@link ShowCDFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowCDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private static String nombreComplejo = null;
    private View mProgressView;
    private View scroll;
    private CDShowTask mShowTask;
    private TextView txtNombre;
    private TextView txtDireccion;
    private TextView txtCurso1Nombre;
    private TextView txtCurso1Dias;
    private TextView txtCurso1Horaio;
    private TextView txtCurso2Nombre;
    private TextView txtCurso2Dias;
    private TextView txtCurso2Horaio;
    private TextView txtCurso3Nombre;
    private TextView txtCurso3Dias;
    private TextView txtCurso3Horaio;
    private TextView txtCurso4Nombre;
    private TextView txtCurso4Dias;
    private TextView txtCurso4Horaio;
    private TextView txtCurso5Nombre;
    private TextView txtCurso5Dias;
    private TextView txtCurso5Horaio;
    private TextView txtCurso6Nombre;
    private TextView txtCurso6Dias;
    private TextView txtCurso6Horaio;
    private ImageView imgView_leftArrow;
    private ImageView imgView_rightArrow;
    private ImageView imgView_maps;
    private ParseImageView imgInstalaciones;
    private int cont = 1;
    private ParseImageView img;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShowCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowCDFragment newInstance(int param1, String titulo) {
        ShowCDFragment fragment = new ShowCDFragment();
        nombreComplejo = titulo;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowCDFragment() {
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
        // Inflate the layout for this fragmen
        View v = inflater.inflate(R.layout.fragment_show_cd, container, false);
        mProgressView = v.findViewById(R.id.progressBar_CDShow);
        scroll = v.findViewById(R.id.scroll);
        txtNombre = (TextView) v.findViewById(R.id.txtView_tituloCD);
        txtDireccion = (TextView) v.findViewById(R.id.txtView_direccion);
        img = (ParseImageView) v.findViewById(R.id.parseImageView_img_principal);
        //Getting TextView from the layout to set the information of Evento(0)
        txtCurso1Nombre = (TextView) v.findViewById(R.id.txtView_cursos1_nombre);
        txtCurso1Dias = (TextView) v.findViewById(R.id.txtView_cursos1_dias);
        txtCurso1Horaio = (TextView) v.findViewById(R.id.txtView_cursos1_horario);
        //Getting TextView from the layout to set the information of Evento(1)
        txtCurso2Nombre = (TextView) v.findViewById(R.id.txtView_cursos2_nombre);
        txtCurso2Dias = (TextView) v.findViewById(R.id.txtView_cursos2_dias);
        txtCurso2Horaio = (TextView) v.findViewById(R.id.txtView_cursos2_horario);
        //Getting TextView from the layout to set the information of Evento(2)
        txtCurso3Nombre = (TextView) v.findViewById(R.id.txtView_cursos3_nombre);
        txtCurso3Dias = (TextView) v.findViewById(R.id.txtView_cursos3_dias);
        txtCurso3Horaio = (TextView) v.findViewById(R.id.txtView_cursos3_horario);
        //Getting TextView from the layout to set the information of Evento(3)
        txtCurso4Nombre = (TextView) v.findViewById(R.id.txtView_cursos4_nombre);
        txtCurso4Dias = (TextView) v.findViewById(R.id.txtView_cursos4_dias);
        txtCurso4Horaio = (TextView) v.findViewById(R.id.txtView_cursos4_horario);
        //Getting TextView from the layout to set the information of Evento(4)
        txtCurso5Nombre = (TextView) v.findViewById(R.id.txtView_cursos5_nombre);
        txtCurso5Dias = (TextView) v.findViewById(R.id.txtView_cursos5_dias);
        txtCurso5Horaio = (TextView) v.findViewById(R.id.txtView_cursos5_horario);
        //Getting TextView from the layout to set the information of Evento(5)
        txtCurso6Nombre = (TextView) v.findViewById(R.id.txtView_cursos6_nombre);
        txtCurso6Dias = (TextView) v.findViewById(R.id.txtView_cursos6_dias);
        txtCurso6Horaio = (TextView) v.findViewById(R.id.txtView_cursos6_horario);

        imgView_leftArrow = (ImageView) v.findViewById(R.id.imgView_leftArrow);
        imgView_rightArrow = (ImageView) v.findViewById(R.id.imgView_rightArrow);
        imgInstalaciones = (ParseImageView) v.findViewById(R.id.parseImageView_img_instalaciones);

        imgView_maps = (ImageView) v.findViewById(R.id.imgView_google_maps);

        showProgress(true);
        //Do the query in a thread
        mShowTask = new CDShowTask();
        mShowTask.execute();

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

    public class CDShowTask extends AsyncTask<Void,  Void, List<ParseObject>>{

        List<ParseObject> result;
        List<ParseObject> resultImages;
        List<ParseObject> horarios;

        @Override
        protected List<ParseObject> doInBackground(Void... params) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Lugar").whereEqualTo("nombre",nombreComplejo);
            ParseQuery<ParseObject> queryImages = ParseQuery.getQuery("Imagen");
            ParseQuery<ParseObject> queryCurses = ParseQuery.getQuery("Evento");
            query.include("ubicacion");
            queryImages.whereMatchesQuery("idLugar", query);
            queryCurses.whereMatchesQuery("idLugar", query);
            try {

                result = query.find();
                horarios = queryCurses.find();
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

            return result;

        }

        @Override
        protected void onPostExecute(final List<ParseObject> list){

            ParseObject complejo = list.get(0);
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
            //Setting data from Evento(0)
            txtCurso1Nombre.setText(horarios.get(0).getString("nombreEvento"));
            txtCurso1Dias.setText(horarios.get(0).getString("fechas"));
            txtCurso1Horaio.setText(horarios.get(0).getString("horarios"));
            //Setting data from Evento(1)
            txtCurso2Nombre.setText(horarios.get(1).getString("nombreEvento"));
            txtCurso2Dias.setText(horarios.get(1).getString("fechas"));
            txtCurso2Horaio.setText(horarios.get(1).getString("horarios"));
            //Setting data from Evento(2)
            txtCurso3Nombre.setText(horarios.get(2).getString("nombreEvento"));
            txtCurso3Dias.setText(horarios.get(2).getString("fechas"));
            txtCurso3Horaio.setText(horarios.get(2).getString("horarios"));
            //Setting data from Evento(3)
            txtCurso4Nombre.setText(horarios.get(3).getString("nombreEvento"));
            txtCurso4Dias.setText(horarios.get(3).getString("fechas"));
            txtCurso4Horaio.setText(horarios.get(3).getString("horarios"));
            //Setting data from Evento(4)
            txtCurso5Nombre.setText(horarios.get(4).getString("nombreEvento"));
            txtCurso5Dias.setText(horarios.get(4).getString("fechas"));
            txtCurso5Horaio.setText(horarios.get(4).getString("horarios"));
            //Setting data from Evento(5)
            txtCurso6Nombre.setText(horarios.get(5).getString("nombreEvento"));
            txtCurso6Dias.setText(horarios.get(5).getString("fechas"));
            txtCurso6Horaio.setText(horarios.get(5).getString("horarios"));

            imgInstalaciones.setParseFile(resultImages.get(cont).getParseFile("imagen"));
            imgInstalaciones.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    Log.i("ParseImageView", "Data length " + bytes.length);
                }
            });

            imgView_rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cont < 4) {
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

            imgView_leftArrow.setOnClickListener(new View.OnClickListener() {
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
                    String geopoint = "geo:"+ lat + "," + lon + "?q=" + lat + "," + lon;
                    Uri gmmIntentUri = Uri.parse(geopoint);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });

            showProgress(false);

        }

        @Override
        protected void onCancelled() {
            mShowTask = null;
            showProgress(false);
        }
    }

}
