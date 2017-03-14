package info.WPLUS.filesecure;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;
    private EditText mPasswordView,mPasswordView2;
    private View mProgressView;
    private View mLoginFormView;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      this.setTitle("Setup Quick password");

      mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView2 = (EditText) findViewById(R.id.password2);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mPasswordView.setError(null);
        mPasswordView2.setError(null);

        String password = mPasswordView.getText().toString();
        String password2 = mPasswordView2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password2)) {
            mPasswordView2.setError(getString(R.string.error_field_required));
            focusView = mPasswordView2;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (0!=password.compareTo(password2)) {
            mPasswordView2.setError(getString(R.string.error_invalid_password2));
            focusView = mPasswordView2;
            cancel = true;
        }



        if (cancel) {
            focusView.requestFocus();
        } else {
            File keyfile=new File(Environment.getExternalStorageDirectory()+"/.WPLUS/defaultkey.Wplus");

            try {
                FileOutputStream fileOutputStream=new FileOutputStream(keyfile);
                fileOutputStream.write(password.getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast toast1 = Toast.makeText(getApplicationContext(), "quick password is saved!", Toast.LENGTH_SHORT);
            toast1.show(); startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }



    private boolean isPasswordValid(String password) {
        return password.length() == 16;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

    return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                //      finish();
            } else {
                // mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}

