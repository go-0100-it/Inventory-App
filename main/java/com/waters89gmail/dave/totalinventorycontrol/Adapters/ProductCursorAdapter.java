package com.waters89gmail.dave.totalinventorycontrol.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;

public class ProductCursorAdapter extends android.widget.CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();

        final View rowView = ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.product_list_item, parent, false);
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
        final int pos = cursor.getPosition();
        viewHolder.productNameTxt.setText(cursor.getString(1));
        if(viewHolder.productNameTxt.getLineCount()>1){
            viewHolder.priceTxt.setPaddingRelative(C.NULL,C.NULL,C.NULL,40);
        }else{
            viewHolder.priceTxt.setPaddingRelative(C.NULL,C.NULL,C.NULL,20);
        }
        String priceText = context.getString(R.string.price)+ C.SPACE + String.valueOf(cursor.getDouble(5));
        viewHolder.priceTxt.setText(priceText);

        // If stock for product is above 0 then setting text to "IN STOCK" and color to green. Also enabling "SALE" button.
        // If stock for product is less than 1 setting text to "OUT OF STOCK" and color to red. Also disabling "SALE" button.
        SpannableString wordsToSpan;
        if(cursor.getLong(6)<1){
            viewHolder.saleBtn.setEnabled(false);
            viewHolder.saleBtn.setBackgroundResource(R.drawable.my_button_disabled);
           wordsToSpan = new SpannableString(context.getString(R.string.out_of_stock));
           wordsToSpan.setSpan(new ForegroundColorSpan(Color.RED), C.NULL, wordsToSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            viewHolder.saleBtn.setEnabled(true);
            viewHolder.saleBtn.setBackgroundResource(R.drawable.my_button);
            wordsToSpan = new SpannableString(context.getString(R.string.in_stock) + C.SPACE + String.valueOf(cursor.getLong(6)));
            wordsToSpan.setSpan(new ForegroundColorSpan(Color.BLACK), C.NULL, wordsToSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        viewHolder.qtyOnHandTxt.setText(wordsToSpan);

        // Setting OnClickListener for "SALE" button for each item in list.
        viewHolder.saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(pos);
                long productId = cursor.getLong(0);
                int qty = cursor.getInt(6);

                //Reducing product quantity on hand if greater than zero.
                if(qty > C.NULL){
                    qty = qty - 1;
                }else {
                    qty = C.NULL;
                }

                //updating DataBase with newly reduced value
                if (DataBaseUtils.updateProductQty(context,productId,qty) > C.NULL){
                    //Notifying user the update was successful.
                    Snackbar.make(v, context.getString(R.string.inventory_was_reduced), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
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
