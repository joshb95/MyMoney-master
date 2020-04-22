package gerber.apress.esctransition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mName, mEmail, mPassword, mPhoneNumber;
    Button mRegisterBtn;
    TextView mLoginTxt;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    //Creation of Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.Name);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mPhoneNumber = findViewById(R.id.PhoneNumber);
        mRegisterBtn = findViewById(R.id.LoginBtn);
        mLoginTxt = findViewById(R.id.RegisterTxt);

        fAuth = FirebaseAuth.getInstance(); //Getting current instance of the Firebase
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        //Connection to xml resources

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //Sending user to the Main Activity if they are already logged in
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim(); //gets email object, converts it to a string and formats it.
                String password = mPassword.getText().toString().trim(); //Same as email but with the password.
                final String name = mName.getText().toString(); //Retrieving the name of the User
                final String phone = mPhoneNumber.getText().toString(); //Retrieving the Phone number of the user

                if (TextUtils.isEmpty(email)){ //Checking to see if email is empty
                    mEmail.setError("Please Enter an Email Address"); //Error Message
                    return;
                }

                if (TextUtils.isEmpty(password)){ //Checking to see if Password is empty
                    mPassword.setError("Please Enter a Password"); //Error Message
                    return;
                }

                if(password.length() < 6){ //Checking Length of Password
                    mPassword.setError("Please ensure the password has more 6 or more characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE); //Making the Progress Bar Visible

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ //Checking to see if successfully registered to Firebase
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid(); //Retrieving the User ID from Firebase
                            DocumentReference documentReference = fStore.collection("users").document(userID); //Collection created
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("email", email);
                            user.put("Phone Number", phone); //Adding Data to Map Object
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: Profile has been created for"+ userID);
                                }
                            }); //Inserting data into Firestore Database
                            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //Sending user to the Main Activity
                        }
                        else {
                            Toast.makeText(Register.this, "Error, User Not Created" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}
