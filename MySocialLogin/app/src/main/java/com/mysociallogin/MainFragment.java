package com.mysociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by kiit on 06-12-2016.
 */
public class MainFragment extends Fragment {


    private CallbackManager mCallbackManager;
    private TextView mTextDetails;
    private TextView mTextDetails1;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private FacebookCallback<LoginResult> mCallback=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken=loginResult.getAccessToken();
            Profile profile=Profile.getCurrentProfile();
            displayWelcomeMessage(profile);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };



    public MainFragment(){
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mCallbackManager=CallbackManager.Factory.create();

        mTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

            }
        };
         mProfileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
              displayWelcomeMessage(newProfile);
            }
        };
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
        // AppEventsLogger.activateApp(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_main1, container, false);
        return v;
    }


    private void displayWelcomeMessage(Profile profile){
       try {
           if (profile != null) {
               mTextDetails.setText("Welcome " + profile.getName());
               mTextDetails1.setText("ID"+profile.getId());
               
               update(true);
           }
           else {
               update(false);
           }

       }catch (NullPointerException e){
           e.printStackTrace();
       }
    }

    public void update(boolean isSignedIn){
        if (isSignedIn){
            mTextDetails.setVisibility(View.VISIBLE);
            mTextDetails1.setVisibility(View.VISIBLE);
        }
        else {
            mTextDetails.setVisibility(View.GONE);
            mTextDetails1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginButton loginButton=(LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);

        loginButton.registerCallback(mCallbackManager,mCallback);

        mTextDetails=(TextView)view.findViewById(R.id.text_details);
        mTextDetails1=(TextView)view.findViewById(R.id.text_details1);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.startTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
