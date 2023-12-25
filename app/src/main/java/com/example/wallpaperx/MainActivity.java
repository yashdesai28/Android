package com.example.wallpaperx;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity  {
    Button b1;

    GoogleSignInOptions gso;

    GoogleSignInClient gsc;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



         gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         gsc=GoogleSignIn.getClient(this,gso);

         firebaseAuth=FirebaseAuth.getInstance();


        b1=findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent=new Intent(getApplicationContext(),showWallpaper.class);
//                startActivity(intent);

               signin();
            }
        });
    }

    private void signin() {

        Intent signinintent=gsc.getSignInIntent();
        startActivityForResult(signinintent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                FirebaseAuthWithGoogle(account);
                task.getResult(ApiException.class);

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void FirebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                String uid=firebaseUser.getUid();
                String email=firebaseUser.getEmail();

                Log.d(TAG,"EMAIL= "+email);
                Log.d(TAG,"uid= "+uid);


                if(authResult.getAdditionalUserInfo().isNewUser()){

                    Toast.makeText(MainActivity.this, "Account Create.....\n "+email, Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(MainActivity.this, "Existing User....\n"+email, Toast.LENGTH_SHORT).show();
                }

                navigateToSecondActivity();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this,showWallpaper.class);
        startActivity(intent);
    }
}