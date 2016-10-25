package shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.UserProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shanefuzz.arwebsiteevaluator.R;

public class UserSignUp extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    // UI references.
    private EditText mEmailView;
    private EditText mNameView;
    //private EditText mPhoneView;
    private EditText mPasswordView;


    private ConnectivityManager mConMan = null;
    private NetworkInfo mInfo = null;

    private DatabaseHandler_user mDatabaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utils.setThemeToActivity(this);
        setContentView(R.layout.activity_user_sign_up);

        Log.i(TAG, "onCreate");


        // Set up the SignUpActivity details.
        mNameView = (EditText) findViewById(R.id.text_signup_name);
        mEmailView = (EditText) findViewById(R.id.text_signup_email);
        mPasswordView = (EditText) findViewById(R.id.text_signup_password);
        //mPhoneView = (EditText) findViewById(R.id.text_signup_phone);

        // Connect to the database
        mDatabaseHandler = new DatabaseHandler_user(this, null, null, 1);


        // SignUp by pressing the enter key(sign up-key)
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                return signUpTask();

            }


        });


        // Sign up by pressing the SIGN UP button
        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUpTask();

            }
        });


    }


    /**
     * Attempts to sign up.
     * If there are form errors (invalid fields, missing fields, etc.), the
     * errors are presented and no actual sign up attempt is made.
     */
    private boolean signUpTask() {


        emailValidation();
        nameValidation();
        passwordValidation();

        if (!isEmpty(mNameView) && !isEmpty(mEmailView) && isValidPassword(mPasswordView) && isValidEmail(mEmailView.getText().toString()) && !mDatabaseHandler.isEmailStore(mEmailView.getText().toString())) {

            mConMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            mInfo = mConMan.getActiveNetworkInfo();

            if (mInfo == null) {
                Toast.makeText(getApplicationContext(), "Unable to send confirmation email. Please check your Internet connection.", Toast.LENGTH_LONG).show();
            } else {
                sendConfirmationEmail();
                addUser();
            }

        }

        return true;

    }

    /**
     * check email is empty or not
     * check email is valid one
     *
     */
    public boolean emailValidation() {
        if (isEmpty(mEmailView)) {
            mEmailView.setError("This field is required.");
        } else if (mDatabaseHandler.isEmailStore(mEmailView.getText().toString())) {
            mEmailView.setError("This email address is already taken. Please choose another one.");
        } else if (!isValidEmail(mEmailView.getText().toString())) {
            mEmailView.setError("Please enter a valid email address.");
        }

        return true;
    }


    /**
     * check name is empty or not
     * check name is valid one
     *
     */
    public boolean nameValidation() {
        if (isEmpty(mNameView)) {
            mNameView.setError("This field is required.");
        } else {
            mNameView.setError(null);
        }

        return true;
    }


    /**
     * check password is empty or not
     * check password is valid one
     *
     */
    public boolean passwordValidation() {
        if (isEmpty(mPasswordView)) {
            mPasswordView.setError("This field is required.");
        } else if (!isValidPassword(mPasswordView)) {
            mPasswordView.setError("Please enter a valid password.");
        }

        return true;
    }


    /**
     * Go to SignInActivity page
     */
    public void goToSignInActivity(View v) {

        Intent intent = new Intent(this, UserSignIn.class);
        startActivity(intent);
        finish();
    }

    /**
     * Add user to database
     */
    public void addUser() {


        UserProfile user = new UserProfile(mNameView.getText().toString().trim(), mEmailView.getText().toString().trim(), mPasswordView.getText().toString().trim());
        mDatabaseHandler.addUser(user);


        mNameView.setText("");
        mEmailView.setText("");
        mPasswordView.setText("");
        //mPhoneView.setText("");

        Toast toast = Toast.makeText(getApplicationContext(), "Your account has been successfully created. Please check your email for the confirmation.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();


        Log.e(TAG, "Details Added");

        Intent intent = new Intent(this, UserSignIn.class);
        startActivity(intent);
        finish();

    }


    /**
     * Send confirmation email to the user
     */
    private void sendConfirmationEmail() {

        //Getting content for email
        String email = mEmailView.getText().toString();
        String name = mNameView.getText().toString();
        String subject = "AR Website Evaluator: Account created successfully.";
        String message = "Congratulation " + name + ", Your account has been created. \n You can log in to the account by using the both email address and password which you provided.";
        //String message ="Congratulation";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing SendMail to send email
        sm.execute();
    }


    /**
     * Empty field validation
     */
    private boolean isEmpty(EditText field) {

        return field.getText().toString().isEmpty();
    }

    /**
     * Email address validation
     */
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Password validation.
     * At least 8 characters.
     */
    private boolean isValidPassword(EditText filed) {
        return filed.getText().toString() != null && filed.getText().toString().length() > 7;
    }

    /**
     * Phone number validation
     */
    private boolean isValidNo(EditText filed) {

        String NO_PATTERN = "[0-9]+";

        Pattern pattern = Pattern.compile(NO_PATTERN);
        Matcher matcher = pattern.matcher(filed.getText().toString());
        return matcher.matches();


    }


    /**
     * Discard changes and go back
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserSignUp.this);
        if (!isEmpty(mEmailView) || !isEmpty(mPasswordView) || !isEmpty(mNameView)) {

            builder.setMessage("Discard the changes?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    goBackToSignInActivity();
                    UserSignUp.this.finish();

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
            return;

        } else {
            goBackToSignInActivity();
        }


    }


    /**
     * Go back to sign in
     */
    public void goBackToSignInActivity() {

        Intent intent = new Intent(this, UserSignIn.class);
        startActivity(intent);
        finish();

    }
}
