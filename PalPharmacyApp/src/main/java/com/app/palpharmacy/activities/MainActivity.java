package com.app.palpharmacy.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.palpharmacy.R;
import com.app.palpharmacy.adapters.RecyclerViewAdapter;
import com.app.palpharmacy.model.Pharmacy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private static int splashtime= 4000;
    private final String JSON_URL = "http://www.palpharmacy.com/index.php/getPharmacies" ;
    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;
    private List<Pharmacy> lstPharmacy;
    private RecyclerView recyclerView ;
    private DrawerLayout mDrawer;


    private DrawerLayout drawer;
    RecyclerViewAdapter myadapter;
EditText searchinput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // loadlocale();
        setContentView(R.layout.activity_main);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));*/

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer= findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
                    drawer.addDrawerListener(toggle);
                       toggle.syncState();
                         searchinput= findViewById(R.id.edittext);

        lstPharmacy = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();

       // Fragment frag = new Fragment();
      //  getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.draw_layout,frag).commit();


       /* Button language =findViewById(R.id.nav_language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showlanguageDiloge();
            }
        }); */



    }
   /* private void showlanguageDiloge (){
        final String [] listitems ={ "English", "Arabic"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder (MainActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (   i == 0) {
                    setlocale ("en");
                    recreate();
                }
                else  if (   i == 1 ) {
                    setlocale ("ar");
                    recreate();
                }
                dialogInterface.dismiss();

            }
        });
      AlertDialog mdialog = mBuilder.create();
      mdialog.show();
    }

    /* void setlocale(String lang) {
        Locale locale = new Locale( lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
       getBaseContext().getResources().updateConfiguration(config, getBaseContext() ,getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
        editor.putString("My Lang" , lang);
        editor.apply();
    }
public void loadlocale(){
        SharedPreferences prefs = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        String language =prefs.getString("My Lang", "");
        setlocale(language); */



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private void jsonrequest() {

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject  = null ;

                for (int i = 0 ; i < response.length(); i++ ) {


                    try {
                        jsonObject = response.getJSONObject(i) ;
                        Pharmacy pharmacy = new Pharmacy() ;
                        pharmacy.setName(jsonObject.getString("name_en"));
                        pharmacy.setDescription(jsonObject.getString("address"));
//                        pharmacy.setRating(jsonObject.getString("Rating"));
//                        pharmacy.setCategorie(jsonObject.getString("categorie"));
//                        pharmacy.setNb_episode(jsonObject.getInt("episode"));
//                        pharmacy.setStudio(jsonObject.getString("studio"));
                        pharmacy.setImage_url(jsonObject.getString("image"));
                        lstPharmacy.add(pharmacy);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                setuprecyclerview(lstPharmacy);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
searchinput.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    myadapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request) ;


    }

    private void setuprecyclerview(List<Pharmacy> lstPharmacy) {


        myadapter = new RecyclerViewAdapter(this, lstPharmacy) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new aboutusfragment()).commit();
                break;
            case R.id.nav_language:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Languagefragment()).commit();
                break;
            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new notificationfragment()).commit();
                break;
            case R.id.nav_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new locationfragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new locationfragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.draw_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
