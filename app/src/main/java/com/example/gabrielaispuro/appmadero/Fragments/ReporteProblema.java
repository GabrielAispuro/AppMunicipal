package com.example.gabrielaispuro.appmadero.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.Activity.MainActivity;
import com.example.gabrielaispuro.appmadero.Activity.MapsActivity;
import com.example.gabrielaispuro.appmadero.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReporteProblema.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReporteProblema#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReporteProblema extends Fragment implements Button.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // Activity result key for camera
    private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_A_GEOPOINT = 2;
    private Boolean flag = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private EditText editTelefono;
    private EditText editDescripcion;
    private Spinner spinner;
    private View view;
    private ImageView guardar;
    private ImageView camera;
    private Bitmap img;
    private ImageView imgMap;
    private TextView coord;
    private TextView txtViewTelefono;
    private LinearLayout tipoReporte;

    private String telefono;
    private String descripcion;
    private String tipoDeReporte;
    private ParseFile image;
    private ParseGeoPoint parseGeoPoint;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ReporteProblema.
     */
    // TODO: Rename and change types and number of parameters
    public static ReporteProblema newInstance(int param1) {
        ReporteProblema fragment = new ReporteProblema();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ReporteProblema() {
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
        Bundle bundle = getArguments();
        final Boolean logged = bundle.getBoolean("logged");

        view = inflater.inflate(R.layout.fragment_reporte_problema, container, false);
        if(!isTablet(getActivity())) {
            editTelefono = (EditText) view.findViewById(R.id.editText_telefono);
            editTelefono.setVisibility(View.VISIBLE);
            txtViewTelefono = (TextView) view.findViewById(R.id.txtView_telefono_reporte);
            txtViewTelefono.setVisibility(View.VISIBLE);
        }
        editDescripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        guardar = (ImageView) view.findViewById(R.id.save_button);

        if(logged) {
            tipoReporte = (LinearLayout) view.findViewById(R.id.layout_tipoReporte);
            spinner = (Spinner) view.findViewById(R.id.spinner);
            camera = (ImageView) view.findViewById(R.id.camera_button);
            imgMap = (ImageView) view.findViewById(R.id.google_maps);
            coord = (TextView) view.findViewById(R.id.txtView_reporte_map);

            tipoReporte.setVisibility(View.VISIBLE);
            camera.setVisibility(View.VISIBLE);
            imgMap.setVisibility(View.VISIBLE);
            coord.setVisibility(View.VISIBLE);

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                    R.array.tipos_reportes, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            camera.setOnClickListener((View.OnClickListener) this);

            imgMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
                    PackageManager pm = getActivity().getPackageManager();
                    boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && hasGps) {
                        buildAlertMessageNoGps();
                    } else if(!hasGps || (hasGps && manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        // Setting Dialog Title
                        alertDialog.setTitle("¡Alerta!");
                        // Setting Dialog Message
                        alertDialog.setMessage("Para seleccionar una ubicación solo arrastra la marca en el " +
                                "mapa o clickea la tecla que te lleva atrás en tu dispositivo. Al realizarce" +
                                " la operación aparecerán tus coordenadas debajo de la imagen.");
                        // Setting Positive "Ok" Button
                        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MapsActivity.class);
                                intent.putExtra("method", "report");
                                startActivityForResult(intent, PICK_A_GEOPOINT);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
            });
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saveData(logged)) {
                    Toast.makeText(getActivity(), "¡Favor de llenar todos los campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public Boolean saveData(Boolean logged){

        ParseObject reporte = new ParseObject("Reporte");

        if(!isTablet(getActivity())) {
            telefono = editTelefono.getText().toString();
            if(telefono.trim().equals("")){return false;}
        }
        descripcion = editDescripcion.getText().toString();
        if(descripcion.trim().equals("")){return false;}

        if(logged) {
            tipoDeReporte = spinner.getSelectedItem().toString();

            if(img == null){return false;}
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, blob);
            byte[] imgArray = blob.toByteArray();
            image = new ParseFile("reporte_" + telefono + ".jpeg", imgArray);

            reporte.put("tipoReporte", tipoDeReporte);
            if(!isTablet(getActivity())) {
                reporte.put("telefono", telefono);
            }
            reporte.put("descripcion", descripcion);

            ParseObject imagen = new ParseObject("Imagen");
            imagen.put("imagen", image);
            imagen.put("nombre", image.getName());

            ParseObject geopoint = new ParseObject("Ubicacion");
            if(parseGeoPoint == null){return false;}
            geopoint.put("geoPoint", parseGeoPoint);
            geopoint.put("nombreLugar", "ubicacion_reporte_" + telefono);

            reporte.put("idImagen", imagen);
            reporte.put("idUbicacion", geopoint);

            Toast.makeText(getActivity(), "Guardando..........",
                    Toast.LENGTH_SHORT).show();
            reporte.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
                        params.gravity = Gravity.CENTER;

                        int drawable = R.drawable.camera_icon;
                        if (!isTablet(getActivity())) {
                            editTelefono.setText("");
                        }
                        editDescripcion.setText("");
                        camera.setLayoutParams(params);
                        camera.setImageResource(drawable);
                        coord.setText("Captura tu posición");

                        Toast.makeText(getActivity(), "Has guardado tu reporte con exito. ¡Gracias por tu contribución!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error al intentar guardar",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {

            if(!isTablet(getActivity())) {
                reporte.put("telefono", telefono);
            }
            reporte.put("descripcion", descripcion);

            Toast.makeText(getActivity(), "Guardando..........",
                    Toast.LENGTH_SHORT).show();
            reporte.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        if(!isTablet(getActivity())) {
                            editTelefono.setText("");
                        }
                        editDescripcion.setText("");

                        Toast.makeText(getActivity(), "Has guardado tu reporte con exito. ¡Gracias por tu contribución!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error al intentar guardar" + e,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        return true;

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    //Start the camera by dispatching a camera intent.
    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());
        // Setting Dialog Title
        alertDialog.setTitle("Escoge una opción");
        // Setting Dialog Message
        alertDialog.setMessage("¿De dónde vendrá la imagen?");
        alertDialog.setPositiveButton("Camara",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Camera exists? Then proceed...
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAPTURE_IMAGE_CAPTURE_CODE);
                    }
                });
        alertDialog.setNegativeButton("Galería",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        flag = false;
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                    }
                });
        // Showing Alert Message
        alertDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, 400);
        params.gravity = Gravity.CENTER;

        if (requestCode == CAPTURE_IMAGE_CAPTURE_CODE ) {

            if (resultCode == Activity.RESULT_OK) {
                    img = (Bitmap) data.getExtras().get("data");
                    camera.setLayoutParams(params);
                    camera.setImageBitmap(img);
                    Toast.makeText(getActivity(), "¡Imagen Capturada!", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "¡Has cancelado!", Toast.LENGTH_LONG).show();
            }

        } else if(requestCode == PICK_IMAGE_REQUEST){

            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                img = BitmapFactory.decodeFile(filePath);
                camera.setLayoutParams(params);
                camera.setImageBitmap(img);
                Toast.makeText(getActivity(), "¡Imagen Capturada!", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "¡Has cancelado!", Toast.LENGTH_LONG).show();
            }

            }else if(requestCode == PICK_A_GEOPOINT){

                Bundle bundle = data.getExtras();
                String point = "latitud: " + bundle.getDouble("latitude") + " \nlongitud: " + bundle.getDouble("longitude");
                coord.setText(point);

                parseGeoPoint = new ParseGeoPoint();
                parseGeoPoint.setLatitude(bundle.getDouble("latitude"));
                parseGeoPoint.setLongitude(bundle.getDouble("longitude"));
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

    public void onClick(View v){
        dispatchTakePictureIntent();
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

}
