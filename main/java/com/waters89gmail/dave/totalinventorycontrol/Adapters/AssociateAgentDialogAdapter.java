package com.waters89gmail.dave.totalinventorycontrol.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.R;

/**
 * A Custom CursorAdapter for populating the custom dialog ListView
 */
public class AssociateAgentDialogAdapter extends android.widget.CursorAdapter {

    public AssociateAgentDialogAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();

        final View rowView = ((LayoutInflater) context.getSystemService(
                //Re-using the AgentListFragment's Custom ListView Layout
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.agent_list_item, parent, false);
        viewHolder.agentName = (TextView) rowView.findViewById(R.id.agent_name);
        viewHolder.companyName = (TextView) rowView.findViewById(R.id.company_name);
        viewHolder.webSiteLayout = (RelativeLayout) rowView.findViewById(R.id.web_site_layout);
        viewHolder.callLayout = (RelativeLayout) rowView.findViewById(R.id.call_layout);
        viewHolder.emailLayout = (RelativeLayout) rowView.findViewById(R.id.email_layout);

        rowView.setTag(viewHolder);
        return rowView;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        final int pos = cursor.getPosition();
        cursor.moveToPosition(pos);

        //setting text, text size and Image button visibility to gone for reuse as a Custom Dialog List
        viewHolder.agentName.setTextSize(18);
        viewHolder.companyName.setTextSize(15);
        viewHolder.agentName.setText(cursor.getString(1));
        viewHolder.companyName.setText(cursor.getString(2));
        viewHolder.callLayout.setVisibility(View.GONE);
        viewHolder.emailLayout.setVisibility(View.GONE);
        viewHolder.webSiteLayout.setVisibility(View.GONE);
    }

    class ViewHolder{

        TextView agentName;
        TextView companyName;
        RelativeLayout webSiteLayout;
        RelativeLayout callLayout;
        RelativeLayout emailLayout;

    }
}
