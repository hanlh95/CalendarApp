package com.example.lehoanghan.appcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by lehoanghan on 4/15/2016.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends Activity {
    @ViewById(R.id.activity_register_et_username)
    EditText etName;

    @ViewById(R.id.activity_register_et_password)
    EditText etPass;

    @ViewById(R.id.activity_register_et_gmail)
    EditText etMail;

    @ViewById(R.id.activity_register_et_confirmpassword)
    EditText etConfPass;

    @ViewById(R.id.activity_register_btn_create)
    Button btnCreate;

    @ViewById(R.id.activity_register_btn_clear)
    Button btnClear;

    @ViewById(R.id.activity_register_btn_map)
    Button btnMap;

    private Firebase aFirebase;

    private User aUser;

    private AlertDialog.Builder alertDialog;

    @Click(R.id.activity_register_btn_create)
    public void btnCreate() {
        checkInform();
    }

    @Click(R.id.activity_register_btn_clear)
    public void btnClear() {
        etName.setText("");
        etMail.setText("");
        etPass.setText("");
        etConfPass.setText("");
    }

    @Click(R.id.activity_register_btn_map)
    public void btnMap() {
        Intent myIntent = new Intent(RegisterActivity.this, MapsActivity.class);
        startActivity(myIntent);
    }

    @AfterViews
    public void afterView() {
        Firebase.setAndroidContext(this);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
    }

    //@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        Firebase.setAndroidContext(this);
//        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
//        btnCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkInform();
//            }
//        });
//        btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etName.setText("");
//                etMail.setText("");
//                etPass.setText("");
//                etConfPass.setText("");
//            }
//        });
//        btnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(RegisterActivity.this, MapsActivity.class);
//                startActivity(myIntent);
//            }
//        });
//}

    public void checkPass() {
        if (etConfPass.getText().toString().compareTo(etPass.getText().toString()) != 0) {
            Toast.makeText(getApplicationContext(),
                    "Enter Conpass error, Enter Conpass again, please", Toast.LENGTH_SHORT).show();
            etPass.setText("");
            etConfPass.setText("");
        } else {
            saveDatabase();
        }
    }

    public void checkInform() {
        if ((etName.getText().toString() == "") || (etMail.getText().toString() == "") ||
                (etPass.getText().toString() == "") || (etConfPass.getText().toString() == "")) {
            {
                Toast.makeText(getApplicationContext(),
                        "You need fill out all inform", Toast.LENGTH_SHORT).show();
            }
        } else {
            checkPass();
        }
    }

    public void saveUser() {
        aUser = new User();
        aUser.setjName(etName.getText().toString());
        aUser.setjMail(etMail.getText().toString());
        aUser.setjPass(etPass.getText().toString());
    }

    public byte[] convertImage() {
        Bitmap aBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        aBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] array = outputStream.toByteArray();
        return array;
    }

    public String covertArraytoString() {
        String picFirebase = Base64.encodeToString(convertImage(), Base64.DEFAULT);
        return picFirebase;
    }

    public void saveDatabase() {

        aFirebase.createUser(etMail.getText().toString(), etPass.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        //aUser.setjId(stringObjectMap.get("uid").toString());
                        String mail = etMail.getText().toString().replace(".", "&");
                        aFirebase.child("User").child(mail).setValue(etName.getText().toString());
                        aFirebase.child("Avata").child(mail).setValue(covertArraytoString());
                        alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                        alertDialog.setMessage("Congratulate, do you want to login?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent =
                                        new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("MailUser", etMail.getText().toString());
                                Log.e("EditGmail", etMail.getText().toString());
                                intent.putExtra("Password", etPass.getText().toString());
                                startActivity(intent);
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(),
                                firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        etPass.setText("");
                        etConfPass.setText("");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.activity_:
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
