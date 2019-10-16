package com.sonu.firebasedemo.authentication;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sonu.firebasedemo.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.*;
import android.widget.Button;
import android.view.*;
import android.util.*;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.tasks.*;
import com.google.android.gms.common.api.*;
import android.widget.*;
import com.google.android.gms.common.SignInButton;

public class GoogleSignInActivity extends AppCompatActivity
implements View.OnClickListener{

    private GoogleSignInClient mGoogleSignInClient;
	private final int RC_SIGN_IN=123;
	private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
	Button signOutButton;
	SignInButton signInButton;
	TextView tvName,tvEmail;
	ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_sign_in);
		signInButton=findViewById(R.id.id_btn_signIn);
		signOutButton=findViewById(R.id.id_btn_signOut);
		tvName=findViewById(R.id.id_tv_name);
		tvEmail=findViewById(R.id.id_tv_email);
		pb=findViewById(R.id.id_pb);
		mAuth = FirebaseAuth.getInstance();
		
		//set listeners
		signInButton.setOnClickListener(this);
		signOutButton.setOnClickListener(this);
		
		// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        
        
	}

	@Override
	public void onClick(View p1){
		switch(p1.getId()){
			case R.id.id_btn_signIn:
				signIn();
				break;
			case R.id.id_btn_signOut:
				signOut();
				break;
		}
	}

	
   
	
	// [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
	private void signOut() {
		pb.setVisibility(View.VISIBLE);
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
			new OnCompleteListener<Void>() {
				@Override
				public void onComplete( Task<Void> task) {
					pb.setVisibility(View.GONE);
					signInButton.setEnabled(true);
					tvName.setText(null);
					tvEmail.setText(null);
					signOutButton.setVisibility(View.GONE);
				}
			});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                
            }
        }
	}
	
	// [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		pb.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(Task<AuthResult> task) {
					if (task.isSuccessful()) {
						FirebaseUser user = mAuth.getCurrentUser();
						signOutButton.setVisibility(View.VISIBLE);
						signInButton.setEnabled(false);
						tvName.setText(user.getDisplayName());
						tvEmail.setText(user.getEmail());
						pb.setVisibility(View.GONE);
					} else {
						showInToast(task.getException().getMessage());
						pb.setVisibility(View.GONE);
					}

					
				}
			});
    }
	
	
	
	

	@Override
	protected void onStart(){
		super.onStart();
		FirebaseUser user;
		if((user=mAuth.getCurrentUser()) !=null){
			signOutButton.setVisibility(View.VISIBLE);
			signInButton.setEnabled(false);
			tvName.setText(user.getDisplayName());
			tvEmail.setText(user.getEmail());
		}
		
		
	}
		
 
	
	
	
	private void showInToast(String msg){
		Toast.makeText(GoogleSignInActivity.this,msg,
		Toast.LENGTH_SHORT).show();
	}
	
}
