package vay.enterwind.auto2000samarinda.pubnub.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.pubnub.Constants;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;

    private static boolean isValid(String username) {
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubnub_login);

        mUsername = (EditText) findViewById(R.id.usernameEdit);
    }

    public void joinChat(View view) {
        String username = mUsername.getText().toString();

        if (!isValid(username)) {
            return;
        }

        SharedPreferences sp = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.DATASTREAM_UUID, username);
        edit.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
