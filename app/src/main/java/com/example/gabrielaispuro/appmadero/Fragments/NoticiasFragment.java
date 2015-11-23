package com.example.gabrielaispuro.appmadero.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.Activity.MainActivity;
import com.example.gabrielaispuro.appmadero.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticiasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoticiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticiasFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    List<ParseObject> result = null;
    private ListView listViewNews;
    private View mProgressView;
    View v = null;
    RelativeLayout layout;
    ImageView imgNews;
    private NewsTask mNewsTask = null;

    // TODO: Rename and change types of parameters
    private String mParam1;


    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static NoticiasFragment newInstance(int param1) {
        NoticiasFragment fragment = new NoticiasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NoticiasFragment() {
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

       //Set up the ListView
       v = inflater.inflate(R.layout.fragment_noticias, container, false);
       layout = (RelativeLayout) v.findViewById(R.id.new_layout_news);
       listViewNews = (ListView) v.findViewById(R.id.list_news);
       mProgressView = v.findViewById(R.id.progressBar_News);
       imgNews = (ImageView) v.findViewById(R.id.imageView);

       //ProgressBar
       showProgress(true);

       //Getting data from parse by a thread
       mNewsTask = new NewsTask();
       mNewsTask.execute();

        // Inflate the layout for this fragment
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

            listViewNews.setVisibility(show ? View.GONE : View.VISIBLE);
            listViewNews.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listViewNews.setVisibility(show ? View.GONE : View.VISIBLE);
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
            listViewNews.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

    public class NewsTask extends AsyncTask<Void,  Void, List<ParseObject>>{

        List<ParseObject> list;
        private Boolean running = true;

        @Override
        protected List<ParseObject> doInBackground(Void... params) {

            while(running && (list == null)) {
                //Getting data from parse.com
                try {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Noticias");
                    query.include("imagen");
                    query.orderByDescending("publicacion");
                    list = query.find();

                } catch (com.parse.ParseException e) {
                    Log.i("Parse Query Error", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + e);
                }

                try {
                    // Simulate network access.
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(), "Error doInBackground", Toast.LENGTH_SHORT).show();
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<ParseObject> list) {

            result = list;

            mNewsTask = null;
            showProgress(false);

            ListNewsAdapter adapter = new ListNewsAdapter(getActivity(),result);
            listViewNews.setAdapter(adapter);

            listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ShowNewsFragment fragment = ShowNewsFragment.newInstance(2, position, result);
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container,fragment);
                    ft.addToBackStack("replacingNewsFragment");
                    ft.commit();

                }
            });

        }

        @Override
        protected void onCancelled() {
            running = false;
            showProgress(false);
        }
    }

}
