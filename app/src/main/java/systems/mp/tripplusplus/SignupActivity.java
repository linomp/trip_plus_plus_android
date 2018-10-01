package systems.mp.tripplusplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends Activity {

    private EditText emailTextView, passwordTextView;
    private FirebaseAuth mAuth;
    private final String TAG = "SIGNUP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.signup_signup_btn).setOnClickListener(e->{
            registerUser();
        });
        
        emailTextView = findViewById(R.id.signup_email);
        passwordTextView = findViewById(R.id.signup_password);

        mAuth = FirebaseAuth.getInstance();

    }

    public void registerUser(){
        String email = emailTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();

        if(isEmailValid(email) && isPasswordValid(password)) {

            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getResources().getString(R.string.loading_msg));
            dialog.setIndeterminate(true);
            //dialog.setCancelable(false);
            dialog.show();
            setProgressBarIndeterminateVisibility(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            dialog.cancel();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignupActivity.this,
                                        getResources().getString(R.string.register_success_msg),
                                        Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SignupActivity.this, FirstScreen.class);
                                        startActivity(i);
                                        //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(SignupActivity.this,
                                            getResources().getString(R.string.duplicate_user_error_msg),
                                            Toast.LENGTH_SHORT).show();
                                }else {

                                    setProgressBarIndeterminateVisibility(false);
                                    Toast.makeText(SignupActivity.this,
                                            getResources().getString(R.string.register_error_msg),
                                            Toast.LENGTH_SHORT).show();
                                }
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

    public boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            emailTextView.setError(getResources().getString(R.string.required_field_error_msg));
            emailTextView.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextView.setError(getResources().getString(R.string.invalid_email));
            emailTextView.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            passwordTextView.setError(getResources().getString(R.string.required_field_error_msg));
            passwordTextView.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            passwordTextView.setError(getResources().getString(R.string.short_password_error_msg));
            passwordTextView.requestFocus();
            return false;
        }
        return true;
    }

}
