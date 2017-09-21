package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.wobeassignment.ApplicationLoader;
import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.adapters.TransactionAdapter;
import com.example.admin.wobeassignment.model.BaseModel;
import com.example.admin.wobeassignment.model.DashboardModel;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();
        makeApiCall(SharedPreferenceManager.getInstance(this).getString(Constants.CUSTOMER_ID));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void makeApiCall(String customerID) {
        String url = String.format(Constants.DASHBOARD_URL, customerID);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                DashboardModel model = new Gson().fromJson
                                        (response.toString(), DashboardModel.class);
                                TextView tvName = (TextView) findViewById(R.id.tvName);

                                if (model.getFirstName() != null) {
                                    tvName.setText(model.getFirstName().trim());
                                    SharedPreferenceManager.getInstance(DashboardActivity.this).
                                            saveData(Constants.FIRST_NAME, model.getFirstName());
                                }
                                if (model.getLastName() != null) {
                                    SharedPreferenceManager.getInstance(DashboardActivity.this).
                                            saveData(Constants.LAST_NAME, model.getLastName());
                                }
                                TextView tvBalance = (TextView) findViewById(R.id.tvBalance);
                                if (model.getCredits() != null) {
                                    tvBalance.setText(model.getCredits());
                                }

                                if (model.getTransaction().size() > 0) {
                                    adapter.setDataInAdapter(model.getTransaction());
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    TextView tvRecentTransactions = (TextView) findViewById(R.id.tvRecentTransactions);
                                    tvRecentTransactions.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        ApplicationLoader.getRequestQueue().add(jsonObjectRequest);
    }


    private void initialiseViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btnSendCredits = (Button) findViewById(R.id.btnSendCredits);
        btnSendCredits.setOnClickListener(this);
        recyclerView = (RecyclerView)
                findViewById(R.id.rvRecentTransactions);
        adapter = new TransactionAdapter(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSendCredits:
                goToNextActivity(SendCreditsActivity.class);
                break;
        }
    }

    protected void goToNextActivity(Class nextActivity) {
        Intent intent = new Intent();
        intent.setClass(this, nextActivity);
        startActivity(intent);
    }
}
