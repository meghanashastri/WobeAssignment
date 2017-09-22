package com.example.admin.wobeassignment.activities;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.wobeassignment.ApplicationLoader;
import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.adapters.TransactionAdapter;
import com.example.admin.wobeassignment.model.DashboardModel;
import com.example.admin.wobeassignment.model.TransactionModel;
import com.example.admin.wobeassignment.utilities.CommonUtils;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.FontManager;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tvAdded, tvSent, tvReceived;
    private TransactionAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();
        if (CommonUtils.isConnectingToInternet(DashboardActivity.this)) {
            makeApiCall(SharedPreferenceManager.getInstance(this).getString(Constants.CUSTOMER_ID));
        } else {
            Toast.makeText(this, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
        }

        initialiseNavigationDrawer();
    }

    private void initialiseNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setEmailAndNameInNavHeader(navigationView, drawer);
    }

    private void setEmailAndNameInNavHeader(NavigationView nameInNavHeader, final DrawerLayout drawer) {
        View header = nameInNavHeader.getHeaderView(0);

        TextView tvUsername = (TextView) header.findViewById(R.id.tvUsername);
        TextView tvEmail = (TextView) header.findViewById(R.id.tvEmail);

        if (SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME) != null) {
            tvUsername.setText(SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME));
        }
        if (SharedPreferenceManager.getInstance(this).getString(Constants.EMAIL) != null) {
            tvEmail.setText(SharedPreferenceManager.getInstance(this).getString(Constants.EMAIL));
        }
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
                                    SharedPreferenceManager.getInstance(DashboardActivity.this).
                                            saveData(Constants.CREDITS, model.getCredits());
                                }

                                if (model.getTransaction().size() > 0) {
                                    List<Integer> list = getSentAndRececivedList(model.getTransaction());
                                    if (list.size()>0){
                                        tvAdded.setText(String.valueOf(list.get(0)));
                                        tvSent.setText(String.valueOf(list.get(1)));
                                        tvReceived.setText(String.valueOf(list.get(2)));
                                    }
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

    private List<Integer> getSentAndRececivedList(List<TransactionModel> transaction) {

        int sent = 0, received = 0, added = 0;

        for (int i = 0; i < transaction.size(); i++) {
            if (SharedPreferenceManager.getInstance(DashboardActivity.this).getString(Constants.CUSTOMER_ID).
                    equalsIgnoreCase(transaction.get(i).getFromCustomerID().toString())) {
                sent = sent + Integer.parseInt(transaction.get(i).getCredits().toString());
            } else if (SharedPreferenceManager.getInstance(DashboardActivity.this).getString(Constants.CUSTOMER_ID).
                    equalsIgnoreCase(transaction.get(i).getToCustomerID().toString())) {
                if (transaction.get(i).getFromCustomerID() == null) {
                    added = added + Integer.parseInt(transaction.get(i).getCredits().toString());
                } else {
                    received = received + Integer.parseInt(transaction.get(i).getCredits().toString());
                }
            }
        }

        List<Integer> list = new ArrayList<Integer>();
        list.add(added);
        list.add(sent);
        list.add(received);

        return list;
    }


    private void initialiseViews() {
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        TextView iconAdded = (TextView) findViewById(R.id.iconAdded);
        iconAdded.setTypeface(iconFont);
        TextView iconSent = (TextView) findViewById(R.id.iconSent);
        iconSent.setTypeface(iconFont);
        TextView iconReceived = (TextView) findViewById(R.id.iconReceived);
        iconReceived.setTypeface(iconFont);
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
        tvAdded = (TextView) findViewById(R.id.tvAdded);
        tvSent = (TextView) findViewById(R.id.tvSent);
        tvReceived = (TextView) findViewById(R.id.tvReceived);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send_credits) {
            goToNextActivity(SendCreditsActivity.class);
        } else if (id == R.id.nav_profile) {
            goToNextActivity(ProfileActivity.class);
        } else if (id == R.id.nav_logout) {
            showLogoutPopUp();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) this.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.delete_custom_dialog, null);
        Button yes = (Button) view.findViewById(R.id.btnDeleteYes);
        Button no = (Button) view.findViewById(R.id.btnDeleteNo);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceManager.getInstance(getApplicationContext()).clearData();
                finish();
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

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

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonUtils.isConnectingToInternet(DashboardActivity.this)) {
            makeApiCall(SharedPreferenceManager.getInstance(this).getString(Constants.CUSTOMER_ID));
        } else {
            Toast.makeText(this, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ApplicationLoader.wasInBackground) {
            Intent intent = new Intent();
            intent.setClass(this, PasscodeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_PASSCODE_ACTIVITY_BUNDLE, Constants.VALUE_SPLASH_SCREEN_ACTIVITY);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            ApplicationLoader.wasInBackground = false;
        }
    }

}
