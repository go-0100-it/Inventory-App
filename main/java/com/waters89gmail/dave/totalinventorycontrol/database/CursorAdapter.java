package com.waters89gmail.dave.totalinventorycontrol.database;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waters89gmail.dave.totalinventorycontrol.R;

/**
 * Created by WatersD on 8/12/2016.
 */
public class CursorAdapter extends android.widget.CursorAdapter {

    private Cursor mCursor;
    private Context mContext;
    private final LayoutInflater cursorInflater;

    public CursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();

        final View rowView = ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, parent, false);
        viewHolder.productNameTxt = (TextView) rowView.findViewById(R.id.product_name);
        viewHolder.qtyOnHandTxt = (TextView) rowView.findViewById(R.id.quantity_on_hand);
        viewHolder.priceTxt = (TextView) rowView.findViewById(R.id.price_txt);
        viewHolder.saleBtn = (ImageButton) rowView.findViewById(R.id.sale_btn);
        viewHolder.listLayout = (RelativeLayout) rowView.findViewById(R.id.list_layout);

        rowView.setTag(viewHolder);
        return rowView;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.productNameTxt.setText(cursor.getString(1));
        if(viewHolder.productNameTxt.getLineCount()>1){
            viewHolder.priceTxt.setPaddingRelative(0,0,0,40);
        }else{
            viewHolder.priceTxt.setPaddingRelative(0,0,0,20);
        }
        viewHolder.priceTxt.setText("Price: " + String.valueOf(cursor.getDouble(5)));


        // If stock for product is above 0 then setting text to "IN STOCK" and color to green. Also enabling "SALE" button.
        // If stock for product is less than 1 setting text to "OUT OF STOCK" and color to red. Also disabling "SALE" button.
        SpannableString wordsToSpan;
        if(cursor.getLong(6)<1){
            viewHolder.saleBtn.setEnabled(false);
            viewHolder.saleBtn.setBackgroundResource(R.drawable.my_button_disabled);
           wordsToSpan = new SpannableString("OUT OF STOCK");
           wordsToSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            viewHolder.saleBtn.setEnabled(true);
            viewHolder.saleBtn.setBackgroundResource(R.drawable.my_button);
            wordsToSpan = new SpannableString("IN STOCK: " + String.valueOf(cursor.getLong(6)));
            wordsToSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        viewHolder.qtyOnHandTxt.setText(wordsToSpan);

        // Setting OnClickListener for "SALE" button for each item in list.
        viewHolder.saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You selected the " + cursor.getString(2), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ViewHolder{

        TextView productNameTxt;
        TextView qtyOnHandTxt;
        TextView priceTxt;
        ImageButton saleBtn;
        RelativeLayout listLayout;
    }
}
