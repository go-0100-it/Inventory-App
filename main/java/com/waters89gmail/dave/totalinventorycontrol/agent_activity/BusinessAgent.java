package com.waters89gmail.dave.totalinventorycontrol.agent_activity;

import java.io.Serializable;

public class BusinessAgent implements Serializable {

    long mAgentId;
    String mAgentName, mAddress1, mAddress2, mEmail, mContact1, mContact2, mPreferredContact, mWebSite, mCompanyName;


    public BusinessAgent(int mAgentId, String mAgentName, String mAddress1, String mAddress2, String mEmail, String mContact1,
                         String mContact2, String mPreferredContact, String mWebSite, String mCompanyName) {
        this.mAgentId = mAgentId;
        this.mAgentName = mAgentName;
        this.mAddress1 = mAddress1;
        this.mAddress2 = mAddress2;
        this.mEmail = mEmail;
        this.mContact1 = mContact1;
        this.mContact2 = mContact2;
        this.mPreferredContact = mPreferredContact;
        this.mWebSite = mWebSite;
        this.mCompanyName = mCompanyName;
    }


    public BusinessAgent(String mAgentName, String mAddress1, String mAddress2, String mEmail, String mContact1, String mContact2,
                         String mPreferredContact, String mWebSite, String mCompanyName) {
        this.mAgentName = mAgentName;
        this.mAddress1 = mAddress1;
        this.mAddress2 = mAddress2;
        this.mEmail = mEmail;
        this.mContact1 = mContact1;
        this.mContact2 = mContact2;
        this.mPreferredContact = mPreferredContact;
        this.mWebSite = mWebSite;
        this.mCompanyName = mCompanyName;
    }

    public long getmAgentId() {
        return mAgentId;
    }

    public void setmAgentId(long id){
        this.mAgentId = id;
    }

    public String getmAgentName() {
        return mAgentName;
    }

    public String getmAddress1() {
        return mAddress1;
    }

    public String getmAddress2() {
        return mAddress2;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmContact1() {
        return mContact1;
    }

    public String getmContact2() {
        return mContact2;
    }

    public String getmPreferredContact() {
        return mPreferredContact;
    }

    public String getmWebSite() {
        return mWebSite;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }
}
