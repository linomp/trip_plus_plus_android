package systems.mp.tripplusplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstScreen extends AppCompatActivity {

    private Button loginButton, signupButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        loginButton.setOnClickListener((v) ->{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });

        signupButton.setOnClickListener((v) ->{
            Intent i = new Intent(this, SignupActivity.class);
            startActivity(i);
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);

        boolean requestedLogOut = false;
        if(getIntent().getExtras()!= null) {
            requestedLogOut = getIntent().getExtras().getBoolean("REQUESTED_LOGOUT");
        }
        if(currentUser != null && !requestedLogOut){
            //Log.d("fb_test", currentUser.getEmail());
            Intent i = new Intent(this, NewQueryActivity.class);
            startActivity(i);
        }

    }

}
