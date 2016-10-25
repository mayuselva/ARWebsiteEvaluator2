package shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.UserProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import shanefuzz.arwebsiteevaluator.R;
import shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.TextEvaluation.VuforiaSamples.ui.ActivityList.ActivityLauncher;

public class UserSignIn extends AppCompatActivity {



        private static final String TAG = "SignInActivity";
    /**
     * Generate 6 character random password
     */
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
        public static UserProfile id;
        // UI references.
        private AutoCompleteTextView mEmailView;
        private EditText mPasswordView;
        private TextView mRecovery;
        private ConnectivityManager mConMan = null;
        private NetworkInfo mInfo = null;
        private DatabaseHandler_user mDatabaseHandler;
    /**
     * press back key within 3 seconds for the second time to close the app
     */
    private Boolean exit = false;

    public static String getToken() {
        StringBuilder token = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Utils.setThemeToActivity(this);
            setContentView(R.layout.activity_user_sign_in);

            Log.i(TAG, "onCreate");

            // Set up login details
            mEmailView = (AutoCompleteTextView) findViewById(R.id.text_signin_email);
            mPasswordView = (EditText) findViewById(R.id.text_signin_password);

            mRecovery = (TextView) findViewById(R.id.text_recovery);
            mRecovery.setPaintFlags(mRecovery.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            // Connect to the database
            mDatabaseHandler = new DatabaseHandler_user(this, null, null, 1);

            // Login by pressing the enter key(Sign in-key)
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                    return signInTask();

                }


            });


            // Login by pressing sign in button
            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    signInTask();

                }
            });

            // Recover user password
            mRecovery.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    sendRecoveryPasswordEmail();

                }
            });


        }

        /**
         * Attempts to sign in.
         * If there are form errors (invalid fields, missing fields, etc.), the
         * errors are presented and no actual login attempt is made.
         */
    private boolean signInTask() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (isEmpty(email)) {
            mEmailView.setError("This field is required");
        }

        if (isEmpty(password)) {

            mPasswordView.setError("This field is required");

        } else if (!isEmpty(password) && !isEmpty(email) && !mDatabaseHandler.authenticateUserLogin(email, password)) {

            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email or password. Please try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {
            setUserObject();
        }

        return true;

    }

    /**
     * Send an email with 8 characters new random password to user's email address.
     */
    private void sendRecoveryPasswordEmail() {

        mPasswordView.setError(null);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserSignIn.this);
        builder.setMessage("Are you sure you want to reset your password?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                mConMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                mInfo = mConMan.getActiveNetworkInfo();

                if (mInfo == null) {
                    Toast.makeText(getApplicationContext(), "Unable to send confirmation email. Please check your Internet connection.", Toast.LENGTH_LONG).show();
                } else {
                    sendRecoveryEmail();
                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * Go to SignUpActivity page
     */
    public void goToSignUpPage(View v) {

        Intent intent = new Intent(this, UserSignUp.class);
        startActivity(intent);
        finish();

    }

    /**
     * Return user object from the database
     */
    public void setUserObject() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        findViewById(R.id.email_sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.login_progress).setVisibility(View.VISIBLE);

        UserProfile user = mDatabaseHandler.createUserObject(email, password);

        id=user;

        //Intent intent = new Intent(this, Dump_expenses_list.class);
        //intent.putExtra("user", user);
        //startActivity(intent);
        //SignInActivity.this.finish();

        //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent intent = new Intent(this, ActivityLauncher.class);
        startActivity(intent);



    }

    /**
     * Empty field validation
     */
    private boolean isEmpty(String field) {

        return field.isEmpty();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    /**
     * Send password recovery email to the user
     */
    private void sendRecoveryEmail() {

        String email = mEmailView.getText().toString();


        // Getting content for email
        if (isEmpty(email)) {
            mEmailView.setError("This field is required.");
        } else if (!mDatabaseHandler.isEmailStore(email)) {
            mEmailView.setError("Sorry, Couldn't find an account with this email address. Please try again.");
        } else {

            String token = getToken();
            mDatabaseHandler.resetUserPassword(email, token);

            String subject = "AR Website Evaluator: Forgot your password?";
            String message = "Here is your new password for AR Website Evaluator:" + token + "\n Log in to the application using this password, and remember to change this given password once you log in.";


            // Creating SendMail object
            SendMail sm = new SendMail(this, email, subject, message);

            // Executing SendMail to send email
            sm.execute();

            Toast toast = Toast.makeText(getApplicationContext(), "You will be received an email shortly with a new password.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

}
