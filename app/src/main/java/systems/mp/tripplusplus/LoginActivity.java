package systems.mp.tripplusplus;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private Button login;
    private TextView emailTextView, passwordTextView;
    private FirebaseAuth mAuth;
    private final String TAG = "LOGIN_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextView = findViewById(R.id.login_email);
        passwordTextView = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.loginConfirm);
        login.setOnClickListener(v -> userLogin());
    }

    private void userLogin(){
        String email = emailTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();

        if(isEmailValid(email) && isPasswordValid(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(LoginActivity.this, NewQueryActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_error_msg),
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
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
