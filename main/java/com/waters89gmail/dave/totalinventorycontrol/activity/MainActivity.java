package com.waters89gmail.dave.totalinventorycontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.CursorAdapter;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.object_classes.Product;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>,SearchView.OnQueryTextListener {

    public static final String ACTIVITY_REQUEST = "activity_request";
    private static final String REQUEST_TYPE = "add";
    public static final String PRODUCT = "product";
    private static final int ADD_PRODUCT_REQUEST_CODE = 100;

    long productId;
    View d;
    CursorAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new CursorAdapter(this,null,false);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportLoaderManager().initLoader(1, null, this);
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Cursor cursor = ((CursorAdapter)listView.getAdapter()).getCursor();
            cursor.moveToPosition(position);

            Product product = new Product(cursor.getLong(0),cursor.getString(1),cursor.getLong(2),
                    cursor.getString(3),cursor.getString(4),cursor.getDouble(5),cursor.getInt(6),cursor.getString(7),cursor.getString(8));
            Toast.makeText(MainActivity.this, product.getmName(), Toast.LENGTH_SHORT).show();
            cursor.close();

            Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
            intent.putExtra(PRODUCT, product);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader( 1, null, this);
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

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Action bar item clicks handled here. Action bar is
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_add_product) {

            Intent intent = new Intent(MainActivity.this, NewItemDetailActivity.class);
            intent.putExtra(ACTIVITY_REQUEST, REQUEST_TYPE);
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
            return true;
        }else if(id == R.id.search) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

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
    }//onNavigationItemSelected

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

       String[] projection = {
                DataBaseContract.ProductsTable._ID,
                DataBaseContract.ProductsTable.NAME,
                DataBaseContract.ProductsTable.NUMBER,
                DataBaseContract.ProductsTable.DESCRIPTION,
                DataBaseContract.ProductsTable.CATEGORY,
                DataBaseContract.ProductsTable.PRICE,
                DataBaseContract.ProductsTable.QTY_ON_HAND,
                DataBaseContract.ProductsTable.TYPE,
                DataBaseContract.ProductsTable.IMAGE_SOURCE};
        Uri uri = DataBaseProvider.CONTENT_URI;

        //return new CursorLoader(this,uri,projection,selection,selectionArgs,null);
        return new CursorLoader(this,uri,projection,null,null,null);
    }//onCreateLoader

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null){
           Toast.makeText(this,R.string.successfully_loaded_new_data,Toast.LENGTH_LONG).show();
            adapter.swapCursor(data);
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this,R.string.new_data_has_not_been_loaded,Toast.LENGTH_LONG).show();
        }
    }//onLoadFinished

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
        Toast.makeText(MainActivity.this, "Loader reset", Toast.LENGTH_SHORT).show();
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_PRODUCT_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                getSupportLoaderManager().restartLoader(1, null, this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO
            }
        }
    }//onActivityResult

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
