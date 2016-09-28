package com.waters89gmail.dave.totalinventorycontrol.global_support;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

public class IntentUtils extends AppCompatActivity {

    public IntentUtils(){}

    /**
     * A method to start a new action (ACTION_SENDTO) intent.
     * @param context used to startActivity.  To attach the calling activity to the intent.
     * @param email the BusinessAgents email address.
     * @param subject a string value to populate the subject field of the email.
     * @param body a string value to populate the body of the email.
     * @param chooserTitle a string value to display as the title of the chooser menu.
     */
    public static void sendEmail(Context context, String email, String subject, String body, String chooserTitle){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse(C.EMAIL_URI_START + Uri.encode(email)));

        if(!subject.equals(C.EMPTY_STRING)){
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        if(!body.equals(C.EMPTY_STRING)) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        }
        context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    /**
     * A method to start a new action (ACTION_CALL) intent.
     * @param context used to startActivity.  To attach the calling activity to the intent.
     * @param number the Agents phone number to call.
     */
    public static void phoneAgent(Context context,String number){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(C.PHONE_URI_START + number));
        context.startActivity(intent);
    }

    /**
     * A method to start a new action (ACTION_VIEW) intent.
     * @param context used to startActivity.  To attach the calling activity to the intent.
     * @param url the Url of the BusinessAgents company website.
     */
    public static void getBrowser(Context context,String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}