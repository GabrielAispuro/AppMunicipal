package com.example.gabrielaispuro.appmadero.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.Fragments.CDFragment;
import com.example.gabrielaispuro.appmadero.Fragments.NavigationDrawerFragment;
import com.example.gabrielaispuro.appmadero.Fragments.NoticiasFragment;
import com.example.gabrielaispuro.appmadero.Fragments.ParquesFragment;
import com.example.gabrielaispuro.appmadero.Fragments.SolicitudesFragment;
import com.example.gabrielaispuro.appmadero.R;
import com.example.gabrielaispuro.appmadero.Fragments.ReporteProblema;
import com.parse.Parse;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Boolean logged = false;
    private int IS_LOGGED = 3;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, "bk8rNWKoCfeqR6yu9X0ailkXOIBVkF1grOh2rubV", "Rw3rQrNlqfW4yxyhkflgZT3dZcEfSNEkzURRhOIu");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        if(!isNetworkAvailable()) {
            Toast.makeText(this, "¡No tienes conexción a Internet!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        switch (position){
            case 0:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = NoticiasFragment.newInstance(position + 1);
                break;
            case 2:
                fragment = CDFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = ParquesFragment.newInstance(position + 1);
                break;
            case 4:
                Bundle bundle = new Bundle();
                bundle.putBoolean("logged", logged);
                fragment = ReporteProblema.newInstance(position + 1);
                fragment.setArguments(bundle);
                break;
            case 5:
                fragment = SolicitudesFragment.newInstance(position + 1);
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        Object fragment = new Object();
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_home);
                break;
            case 2:
                mTitle = getString(R.string.title_news);
                break;
            case 3:
                mTitle = getString(R.string.title_deports);
                break;
            case 4:
                mTitle = getString(R.string.title_parks);
                break;
            case 5:
                mTitle = getString(R.string.title_reports);
                break;
            case 6:
                mTitle = getString(R.string.title_solicitud);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_users && !logged){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(intent, IS_LOGGED);
            return true;
        } else if(id == R.id.action_users && logged == true){
            logged = false;
            Toast.makeText(this, "¡Has terminado sesion!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data){

        int pick = 131073;
        int camera = 131072;
        int mapa = 131074;
        if(reqCode == IS_LOGGED) {
            if (resCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                logged = bundle.getBoolean("logged");

                if (logged) {
                    Toast.makeText(this, "¡Has iniciado sesion exitosamente!", Toast.LENGTH_LONG).show();
                }
            }
        } else if(reqCode == camera){

            fragment.onActivityResult(0, resCode, data);

        } else if(reqCode == pick){

            fragment.onActivityResult(1, resCode, data);

        } else if(reqCode == mapa) {

            fragment.onActivityResult(2, resCode, data);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            TextView about = (TextView) rootView.findViewById(R.id.txtView_about);
            TextView monumentos = (TextView) rootView.findViewById(R.id.txtView_monnumentos_contenido);
            TextView monumentos2 = (TextView) rootView.findViewById(R.id.txtView_monumentos_contenido2);
            TextView artes = (TextView) rootView.findViewById(R.id.txtView_artes2);
            TextView fiestas = (TextView) rootView.findViewById(R.id.txtView_fiestas2);
            TextView local = (TextView) rootView.findViewById(R.id.txtView_localizacion2);
            TextView clima = (TextView) rootView.findViewById(R.id.txtView_clima2);
            TextView orografia = (TextView) rootView.findViewById(R.id.txtView_orografia2);

            about.setText("Ciudad Madero es una ciudad del sureste del Estado de Tamaulipas en " +
                    "México. Cuenta con una importante actividad petrolera, comercial y turística " +
                    "que la hacen una ciudad importante en la Región Huasteca. Su IDH es de 0,9069");
            monumentos.setText("atedral del Sagrado Corazón de Jesús, que data del siglo XIX.");
            monumentos2.setText("Monumentos a Benito Juárez, ubicado en el Tecnológico Regional, a " +
                    "Francisco I. Madero, frente al Palacio Municipal.");
            artes.setText("Del género literario, el libro de poesías intitulado La Estación de los " +
                    "Sentidos, de Marco A. Olguín Amador; de Juan Valles, Poemas a Juárez.");
            fiestas.setText("Previo a la Semana Santa, se lleva a cabo el carnaval con atractivos " +
                    "desfiles de carros alegóricos, bailes, comparsas y multicolores disfraces, " +
                    "que le dan un singular colorido.");
            local.setText("Se encuentra ubicado en la porción sur del Estado y cuenta con una " +
                    "extensión territorial de 46.60 kilómetros cuadrados, que representa el 0.07 " +
                    "por ciento del total de Estado. Es el Municipio de menor superficie en " +
                    "Tamaulipas; su cabecera municipal se localiza a los 22º 14′ de latitud norte y " +
                    "a los 97º 49′ de longitud oeste, a una altitud de 3.08 metros sobre el nivel del mar.\n" +
                    "\n" +
                    "El Municipio colinda al Norte con el Municipio de Altamira; al Sur con el " +
                    "Estado de Veracruz; al Este con el Golfo de México y al Oeste con el Municipio" +
                    " de Tampico. Está constituido por una sola localidad que es Ciudad Madero.");
            clima.setText("El clima del Municipio es de tipo cálido-húmedo, con régimen de lluvias " +
                    "en los meses de junio a septiembre, siendo a la vez los más calurosos; la " +
                    "temperatura promedio anual es de 24ºC, con máxima de 36.8ºC y mínima de 9.7ºC.");
            orografia.setText("El Municipio es plano casi en su totalidad, correspondiente a la " +
                    "llanura costera del Golfo; y algunas zonas al norte y al oeste están formadas " +
                    "por dunas y lomerías.");


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
