package com.example.gabrielaispuro.appmadero.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.Activity.MainActivity;
import com.example.gabrielaispuro.appmadero.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SolicitudesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SolicitudesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolicitudesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private View view;
    private EditText day;
    private EditText time;
    private EditText direccion;
    private EditText titular;
    private EditText descripcion;
    private Spinner spinner;
    private LinearLayout timePicker;
    private ImageView guardar;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SolicitudesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SolicitudesFragment newInstance(int param1) {
        SolicitudesFragment fragment = new SolicitudesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SolicitudesFragment() {
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

        view = inflater.inflate(R.layout.fragment_solicitudes, container, false);
        timePicker = (LinearLayout) view.findViewById(R.id.layout_time_picker);
        spinner = (Spinner) view.findViewById(R.id.spinner_solicitud);
        guardar = (ImageView) view.findViewById(R.id.save_button);
        titular = (EditText) view.findViewById(R.id.editText_titular);
        direccion = (EditText) view.findViewById(R.id.editText_direccion);
        descripcion = (EditText) view.findViewById(R.id.editText_descripcion);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.tipo_solicitud, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    timePicker.setVisibility(View.VISIBLE);
                } else {
                    timePicker.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        day = (EditText) view.findViewById(R.id.etxt_fromdate);
        day.setInputType(InputType.TYPE_NULL);
        day.requestFocus();
        time = (EditText) view.findViewById(R.id.etxt_time);
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                day.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveData()){
                    Toast.makeText(getActivity(), "¡Favor de llenar todos los campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public Boolean saveData(){

        String fecha = day.getText().toString();
        if(fecha.trim().equals("")){return false;}

        String nombre = titular.getText().toString();
        if(nombre.trim().equals("")){return false;}

        String direc = direccion.getText().toString();
        if(direc.trim().equals("")){return false;}

        String descrip = descripcion.getText().toString();
        if(descrip.trim().equals("")){return false;}

        String tipoSolicitud = spinner.getSelectedItem().toString();
        String tiempo;

        ParseObject solicitud = new ParseObject("Solicitud");
        solicitud.put("fecha", fecha);
        solicitud.put("titular", nombre);
        solicitud.put("direccion", direc);
        solicitud.put("descripcion", descrip);
        solicitud.put("tipoSolicitud", tipoSolicitud);
        if(tipoSolicitud.equals("Fiesta")) {
            tiempo = time.getText().toString();
            if(tiempo.trim().equals("")){return false;}
            solicitud.put("hora", tiempo);
        }

        Toast.makeText(getActivity(), "Guardando..........",
                Toast.LENGTH_SHORT).show();
        solicitud.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    day.setText("");
                    titular.setText("");
                    direccion.setText("");
                    descripcion.setText("");
                    time.setText("");
                    spinner.setSelection(0);
                    Toast.makeText(getActivity(), "Has guardado tu reporte con exito. ¡Gracias por tu contribución!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error al intentar guardar",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;

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
