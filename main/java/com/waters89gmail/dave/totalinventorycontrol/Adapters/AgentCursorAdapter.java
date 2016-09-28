package com.waters89gmail.dave.totalinventorycontrol.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.IntentUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.Permissions;

/**
 * Custom CursorAdaptor for populating custom ListView
 */
public class AgentCursorAdapter extends android.widget.CursorAdapter {

    public AgentCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();

        final View rowView = ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.agent_list_item, parent, false);
        viewHolder.agentName = (TextView) rowView.findViewById(R.id.agent_name);
        viewHolder.companyName = (TextView) rowView.findViewById(R.id.company_name);
        viewHolder.webSite = (ImageButton) rowView.findViewById(R.id.web_site_btn);
        viewHolder.call = (ImageButton) rowView.findViewById(R.id.call_btn);
        viewHolder.email = (ImageButton) rowView.findViewById(R.id.email_btn) ;
        viewHolder.webSiteLayout = (RelativeLayout) rowView.findViewById(R.id.web_site_layout);
        viewHolder.callLayout = (RelativeLayout) rowView.findViewById(R.id.call_layout);
        viewHolder.emailLayout = (RelativeLayout) rowView.findViewById(R.id.email_layout);

        rowView.setTag(viewHolder);
        return rowView;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        final int pos = cursor.getPosition();
        cursor.moveToPosition(pos);

        //setting text to views from cursor values
        viewHolder.agentName.setText(cursor.getString(1));
        viewHolder.companyName.setText(cursor.getString(9));

        //setting visibility of views to gone if there is no text to display.
        if(cursor.getString(9).equals(C.EMPTY_STRING)){
            viewHolder.companyName.setVisibility(View.GONE);
        }else {
            viewHolder.companyName.setVisibility(View.VISIBLE);
        }
        //setting imageButton layout visibility to gone if corresponding contact info is null.
        //if info is available set to visible and set onClickListener
        if(cursor.getString(8).equals(C.EMPTY_STRING)){
            viewHolder.webSiteLayout.setVisibility(View.GONE);
        }else{
            viewHolder.webSiteLayout.setVisibility(View.VISIBLE);
            // Setting OnClickListener for "Website button" for each item in list.
            viewHolder.webSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getting agents web site URL from cursor and calling method @IntentUtils.getBrowser to view website in browser.
                    cursor.moveToPosition(pos);
                    String website = cursor.getString(8);
                    IntentUtils.getBrowser(context,website);
                }
            });
        }
        if(cursor.getString(5).equals(C.EMPTY_STRING)){
            viewHolder.callLayout.setVisibility(View.GONE);
        }else {
            viewHolder.callLayout.setVisibility(View.VISIBLE);
            // Setting OnClickListener for "Call button" for each item in list.
            viewHolder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getting agents contact number from cursor and calling method @IntentUtils.phoneAgent to call agent.
                    cursor.moveToPosition(pos);
                    String callNumber = cursor.getString(5);
                    if (Permissions.confirmPhonePermissions((AppCompatActivity)context)){
                        IntentUtils.phoneAgent(context,callNumber);}
                }
            });
        }
        if(cursor.getString(4).equals(C.EMPTY_STRING)){
            viewHolder.emailLayout.setVisibility(View.GONE);
        }else {
            viewHolder.emailLayout.setVisibility(View.VISIBLE);
            // Setting OnClickListener for "Email button" for each item in list.
            viewHolder.email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getting agents email address from cursor and calling method @IntentUtils.sendEmail to send email to agent.
                    cursor.moveToPosition(pos);
                    String address = cursor.getString(4);
                    IntentUtils.sendEmail(context,address, C.EMPTY_STRING,C.EMPTY_STRING,context.getString(R.string.email_send_with));
                }
            });
        }
    }
    class ViewHolder{
        TextView agentName;
        TextView companyName;
        ImageButton webSite;
        ImageButton call;
        ImageButton email;
        RelativeLayout webSiteLayout;
        RelativeLayout callLayout;
        RelativeLayout emailLayout;
    }
}
