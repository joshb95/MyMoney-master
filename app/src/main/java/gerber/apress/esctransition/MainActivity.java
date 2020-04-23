package gerber.apress.esctransition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    TextView verifytxt;
    Button verifybtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        verifytxt = findViewById(R.id.verifytxt);
        verifybtn = findViewById(R.id.verifybtn);

        final FirebaseUser user =fAuth.getCurrentUser();

        if (!user.isEmailVerified()){
            verifytxt.setVisibility(View.VISIBLE);
            verifybtn.setVisibility(View.VISIBLE);

            verifybtn.setOnClickListener(new View.OnClickListener() { //Forgotten Password Reset
                @Override
                public void onClick(final View v){

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Please check your email for verification", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "Verification Email failed to send" + e.getMessage());
                        }
                    });
                }
            });
        }
    }


    public void logout (View view){
        FirebaseAuth.getInstance().signOut(); //Will Log the user out
        startActivity(new Intent(getApplicationContext(),Login.class)); //sends the user to the Login Page
        finish();
    }

    public void account (View view){
        startActivity(new Intent(getApplicationContext(),MyAccount.class)); //sends the user to their account Page
        finish();
    }

}
