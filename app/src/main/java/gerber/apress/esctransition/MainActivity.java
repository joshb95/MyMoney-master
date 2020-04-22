package gerber.apress.esctransition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
