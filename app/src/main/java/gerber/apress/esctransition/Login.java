package gerber.apress.esctransition;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterTxt,mresetTxt;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    //Creation of Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mRegisterTxt = findViewById(R.id.RegisterTxt);
        mresetTxt =findViewById(R.id.resetTxt);

        fAuth = FirebaseAuth.getInstance(); //Getting current instance of the Firebase
        progressBar = findViewById(R.id.progressBar);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim(); //gets email object, converts it to a string and formats it.
                String password = mPassword.getText().toString().trim(); //Same as email but with the password.

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

                //Authenticating the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(Login.this, "Logging In", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       }
                       else {
                           Toast.makeText(Login.this, "Error, User Not Recognised. Please check your details" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                       }
                    }
                });
            }
        });
        mRegisterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        mresetTxt.setOnClickListener(new View.OnClickListener() { //Forgotten Password Reset
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext()); //Allows the user to enter their email
                AlertDialog.Builder passwordReset = new AlertDialog.Builder(v.getContext());
                passwordReset.setTitle("Password Reset");
                passwordReset.setMessage("Please enter your email and you will be sent a link");
                passwordReset.setView(resetMail);

                passwordReset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get email and send link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "A Reset Link Has Been Emailed To You.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error, Link Has Not Been Sent"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordReset.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close Dialog

                    }
                });
                passwordReset.create().show();
            }
        });
    }
}
