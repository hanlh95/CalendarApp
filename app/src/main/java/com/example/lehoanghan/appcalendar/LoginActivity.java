package com.example.lehoanghan.appcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.choosemenu.NavigationActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity implements Validator.ValidationListener {
    private static Animation sShakeAnimation;

    @ViewById(R.id.activity_login_rl_main)
    RelativeLayout rlLogin;

    @ViewById(R.id.activity_login_ll_main)
    LinearLayout llLoin;

    @NotEmpty
    @Email
    @ViewById(R.id.activity_login_et_gmail)
    EditText etMail;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @ViewById(R.id.activity_login_et_password)
    EditText etPass;

    @ViewById(R.id.activity_login_tv_register)
    TextView tvRegister;

    @ViewById(R.id.activity_login_tv_forgot_password)
    TextView tvForgot;

    @ViewById(R.id.activity_login_btn_login)
    Button btnLogin;

    @ViewById(R.id.activity_login_cb_show_hide_password)
    CheckBox cbShowPassword;

    private Firebase aFirebase;

    private String strName;

    private Intent myIntent;

    private int check = 0;

    private String contentMail = "";

    private String contentPass = "";

    private Validator validator;

    @Click(R.id.activity_login_btn_login)
    void setBtnLogin() {
        validator.validate();
    }

    @Click(R.id.activity_login_tv_register)
    void setTvRegister() {
        myIntent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(myIntent);
    }

    @Click(R.id.activity_login_tv_forgot_password)
    void setTvForgot() {
        myIntent = new Intent(LoginActivity.this, ForgotPasswordActivity_.class);
        startActivity(myIntent);
    }

    @CheckedChange(R.id.activity_login_cb_show_hide_password)
    void setCbShowPassword(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            cbShowPassword.setText(R.string.hide_password); // Change checkbox text
            etPass.setInputType(InputType.TYPE_CLASS_TEXT);
            etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//showpass
        } else {
            cbShowPassword.setText(R.string.show_password); // change checkbox text
            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());//hide pass
        }
    }

    @AfterViews
    public void afterViews() {
        giveData();
        Firebase.setAndroidContext(this);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        validator = new Validator(this);
        validator.setValidationListener(this);
        initView();
    }

    public void giveData() {
        if (getIntent().getExtras() != null) {
            contentMail = getIntent().getStringExtra("MailUser");
            contentPass = getIntent().getStringExtra("Password");
        } else {
            contentMail = "";
            contentPass = "";
        }
    }

    public void initView() {
        Log.e("Gmail", contentMail);
        Log.e("Pass", contentPass);
        if (contentMail.compareTo("") != 0 && contentPass.compareTo("") != 0) {
            etMail.setText(contentMail);
            etPass.setText(contentPass);
        }
        //Load shake animation
        sShakeAnimation =
                AnimationUtils.loadAnimation(getApplication(), R.anim.animation_shake);
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        getNameUser();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View contentView = error.getView();
            String message = error.getCollatedErrorMessage(this);

            //Display error message
            if (contentView instanceof EditText) {
                ((EditText) contentView).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                llLoin.startAnimation(sShakeAnimation);
            }
        }
    }

    @Override
    public void onBackPressed() {
        exitView();
    }

    public void exitView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("Do you want to Exit?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void getNameUser() {
        final String STRMAIL = etMail.getText().toString().replace(".", "&");
        aFirebase.child("User").child(STRMAIL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strName = dataSnapshot.getValue(String.class);
                strName = strName.toUpperCase();
                myIntent = new Intent(LoginActivity.this, NavigationActivity.class);
                myIntent.putExtra("NameUser", strName);
                myIntent.putExtra("MailUser", STRMAIL);
                startActivity(myIntent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
