package com.waters89gmail.dave.totalinventorycontrol.product_activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.Adapters.ProductCursorAdapter;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;

/**
 * A fragment to list product Items.
 * <p/>
 */
public class ProductListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ProductCursorAdapter adapter;
    private ListView listView;
    private TextView emptyViewText;
    private ScrollView emptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductListFragment() {}

    @SuppressWarnings("unused")
    public static ProductListFragment newInstance(int columnCount) {return new ProductListFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_list_fragment, container, false);

        //setting view variables by findView
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ProductCursorAdapter(getActivity(),null,false);
        emptyView = (ScrollView) view.findViewById(R.id.empty_view);
        emptyViewText = (TextView) view.findViewById(R.id.empty_view_text);

        //setting adapter to list view and setting OnClickListener
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);

        //starting Cursor Loader and returning created view
        getActivity().getSupportLoaderManager().restartLoader(C.PRODUCT_LIST_LOADER, null, this);
        return view;
    }

    /**
     * Overriding onResume to restart the loader to refresh cursor if frag has been recreated.
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(C.PRODUCT_LIST_LOADER, null, this);
    }

    /**
     * Implementing the OnItemClickListener from the AdapterView Class to listen for list item clicks to...
     * Get the clicked position and move the cursor to that position
     * Extract the data at the clicked position and create a new Product object from the extracted data.
     * Send the Product object as an intent extra to the ProductDetailActivity and start the activity.
     */
    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // moving the cursor the selected items position.
            Cursor cursor = ((ProductCursorAdapter)listView.getAdapter()).getCursor();
            cursor.moveToPosition(position);

            // Creating a new Product Object with values from cursor at the selected position.
            Product product = new Product(cursor.getInt(0),cursor.getString(1),cursor.getLong(2),
                    cursor.getString(3),cursor.getString(4),cursor.getDouble(5),cursor.getInt(6),cursor.getString(7),cursor.getString(8));
            cursor.close();

            // Starting Product Detail activity with the selected Product Object passed as an extra.
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra(C.PRODUCT, product);
            startActivity(intent);
        }
    };

    // Passing params to loader @onCreateLoader to load all fields and rows of the ProductsTable
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
        Uri uri = DataBaseProvider.PRODUCTS_URI;
        return new CursorLoader(getContext(),uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //setting the appropriate views and visibility if the loader returned data(not null).
        String emptyViewMessage = (getString(R.string.empty_view_products_list) + "\n" + getString(R.string.empty_view_get_started));
        if(data != null){
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter.swapCursor(data);
            adapter.notifyDataSetChanged();
            if(!data.moveToFirst()){
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                emptyViewText.setText(emptyViewMessage);
            }
        }else{
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            emptyViewText.setText(emptyViewMessage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
