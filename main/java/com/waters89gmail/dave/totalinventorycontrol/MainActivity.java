package com.waters89gmail.dave.totalinventorycontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.AddAgentsActivity;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.AgentListFragment;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.Permissions;
import com.waters89gmail.dave.totalinventorycontrol.product_activity.AddProductActivity;
import com.waters89gmail.dave.totalinventorycontrol.product_activity.ProductListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MultiUseDialog.MultiUseDialogListener{

    private int currentFrag;
    private String title;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            // this code runs if the activity is being recreated.
            title = savedInstanceState.getString(C.TITLE);
            currentFrag = savedInstanceState.getInt(C.CURRENT_FRAG);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Checking if a Fragment exists.  If not, this code runs to start the appropriate Fragment
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_place) == null) {
            Intent intent = getIntent();
            // Checking if this activity has been started via an intent and if the C.FROM_ACTIVITY string extra has been added.
            if(intent != null && intent.getStringExtra(C.FROM_ACTIVITY)!= null && intent.getStringExtra(C.FROM_ACTIVITY).equals(C.EDIT_AGENT)){
                // If intent was started from the EditAgentActivity then create and add the AgentListFragment
                AgentListFragment agentList = new AgentListFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_place, agentList).commit();
                currentFrag = C.AGENT_FRAG;
                title = getString(R.string.agent_list_activity);
            }else{
                // if this activity was not started via an intent from the EditAgentActivity then default to the ProductsListFrag
                ProductListFragment list = new ProductListFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_place, list).commit();
                currentFrag = C.PRODUCT_FRAG;
                title = getString(R.string.product_list_activity);
            }
            if(intent != null && intent.hasExtra(C.DELETED_TYPE)){
                showDeletedSnackBar(intent);
            }
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(C.TITLE, title);
        outState.putInt(C.CURRENT_FRAG, currentFrag);
        super.onSaveInstanceState(outState);
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
        // Handling Option Menu item clicks here.
        switch (item.getItemId()){
            case R.id.action_add_product:
                switch (currentFrag){
                    case C.PRODUCT_FRAG:
                        startActivityForResult(new Intent(MainActivity.this, AddProductActivity.class), C.ADD_PRODUCT_REQUEST_CODE);
                        return true;
                    case C.AGENT_FRAG:
                        startActivityForResult(new Intent(MainActivity.this, AddAgentsActivity.class), C.ADD_AGENT_REQUEST_CODE);
                        return true;
                    default:
                        return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handling navigation view item clicks here.
        // Adding new List Fragment corresponding to item selected.
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.products_list:
                fragment = new ProductListFragment();
                currentFrag = C.PRODUCT_FRAG;
                title = getString(R.string.product_list_activity);
                getSupportActionBar().setTitle(title);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place,fragment).commit();
                break;
            case R.id.agents_list:
                fragment = new AgentListFragment();
                currentFrag = C.AGENT_FRAG;
                title = getString(R.string.agent_list_activity);
                getSupportActionBar().setTitle(title);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place,fragment).commit();
                break;
            case R.id.transactions_list:
                Snackbar.make(toolbar, getString(R.string.transaction_paid_version), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Getting results returned from intents started with startActivityForResults
     * @param requestCode returns the request code set when starting intent, so we know which intent is returning the results
     * @param resultCode returns either RESULT_OK or RESULT_CANCELLED, RESULT_OK is returned if the desired actions have occurred or if the desired data is being return.
     * @param data returns the intent data containing the extras passed back from the activity started for results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case C.ADD_PRODUCT_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Snackbar.make(toolbar, getString(R.string.add_product_success), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(toolbar, getString(R.string.add_product_cancelled), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
                break;
            case C.ADD_AGENT_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Snackbar.make(toolbar, getString(R.string.add_agent_success), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(toolbar, getString(R.string.add_agent_cancelled), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * A method to show results of delete activity.  Used for either Product delete or BusinessAgent delete.
     * @param intent the intent to extract extra data from.
     */
    private void showDeletedSnackBar(Intent intent){

        String deleteType = intent.getStringExtra(C.DELETED_TYPE);
        int result = intent.getIntExtra(C.COUNT,C.NULL);
        switch (deleteType){
            case C.PRODUCT:
                Snackbar.make(toolbar, getString(R.string.deleted_product_success_part_one)+ C.SPACE + result + C.SPACE + getString(R.string.deleted_product_success_part_two), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
                break;
            case C.AGENT:
                Snackbar.make(toolbar, getString(R.string.deleted_agent_success_part_one)+ C.SPACE + result + C.SPACE + getString(R.string.deleted_agent_success_part_two), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
                break;
        }
    }

    /**
     * Overriding custom onClickListener interface to handle dialog positive button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {}

    /**
     * Overriding custom onClickListener interface to handle dialog neutral button clicks.
     * @param dialog returns dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {}

    /**
     * Overriding custom onClickListener interface to handle dialog negative button clicks.
     * @param dialog returns dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        switch (dialog.getTag()){
            // handling call back from phone permissions rationale dialog.
            case C.PHONE_PERMISSIONS_DIALOG:
                Permissions.requestPhonePermission(this);
                break;
            default:
                break;
        }
    }
}
