package com.example.gabrielaispuro.appmadero.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabrielaispuro.appmadero.Activity.MainActivity;
import com.example.gabrielaispuro.appmadero.R;
import com.parse.GetDataCallback;
import com.parse.ParseImageView;
import com.parse.ParseObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowNewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private static List<ParseObject> result;
    private static int positionNews;
    private View view;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShowNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowNewsFragment newInstance(int param1, int position, List<ParseObject> list) {
        ShowNewsFragment fragment = new ShowNewsFragment();
        result = list;
        positionNews = position;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowNewsFragment() {
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

        view = inflater.inflate(R.layout.fragment_show_news, container, false);
        //Getting fagment's components
        TextView txtTitulo = (TextView) view.findViewById(R.id.txtView_titulo);
        TextView txtPublicacion = (TextView) view.findViewById(R.id.txtView_publicacion);
        ParseImageView img = (ParseImageView) view.findViewById(R.id.parseImageView_img);
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtView_descripcion);

        //Getting the data from a news
        ParseObject noticia = result.get(positionNews);
        ParseObject image = new ParseObject("Imagen");
        image = noticia.getParseObject("imagen");

        //Setting-up this layout
        txtTitulo.setText(noticia.getString("titulo"));
        txtPublicacion.setText(noticia.getString("publicacion"));
        img.setParseFile(image.getParseFile("imagen"));
        img.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, com.parse.ParseException e) {
                Log.i("ParseImageView", "Data length " + bytes.length);
            }
        });
        txtDescripcion.setText(noticia.getString("descripcion"));

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

}
