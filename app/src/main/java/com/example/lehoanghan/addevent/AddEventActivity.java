package com.example.lehoanghan.addevent;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.choosemenu.Menu_Choose;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventName;

    private EditText etDecription;

    private EditText etAlarm;

    private TextView tvSetDayFrom;

    private TextView tvSetTimeFrom;

    private TextView tvSetDayTo;

    private TextView tvSetTimeTo;

    private Button btnSetDayFrom;

    private Button btnSetTimeFrom;

    private Button btnSetDayTo;

    private Button btnSetTimeTo;

    private Button btnFind;

    private AutoCompleteTextView actvPlace;

    private MultiAutoCompleteTextView mactvAddfriend;

    private Spinner spnRepeat;

    private String dateSeclect;

    private String mailUser;

    private String nameUser;

    private String toDay;

    private Firebase firebaseFriend;

    private List<String> listFriendinFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDateSelectFromHome();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Firebase.setAndroidContext(this);
        firebaseFriend = new Firebase("https://appcalendar.firebaseio.com/");
        init();
        tvSetDayFrom.setText(dateSeclect);
        btnSetDayFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(tvSetDayFrom);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        btnSetDayTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(tvSetDayTo);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        btnSetTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectTimeFragment(tvSetTimeFrom);
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });
        btnSetTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectTimeFragment(tvSetTimeTo);
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });
        setDataForPlace();
        setDataForRepeat();
        setDataForAddfriend();
        buttonAddFriend();
    }

    //init value for component
    public void init() {
        etEventName = (EditText) findViewById(R.id.activity_add_event_et_event_name);
        etDecription = (EditText) findViewById(R.id.activity_add_event_et_description);
        etAlarm = (EditText) findViewById(R.id.activity_add_event_et_alarm);
        tvSetDayFrom = (TextView) findViewById(R.id.activity_add_event_tv_set_day_from);
        tvSetTimeFrom = (TextView) findViewById(R.id.activity_add_event_tv_set_time_from);
        tvSetDayTo = (TextView) findViewById(R.id.activity_add_event_tv_set_day_to);
        tvSetTimeTo = (TextView) findViewById(R.id.activity_add_event_tv_set_time_to);
        btnSetDayFrom = (Button) findViewById(R.id.activity_add_event_btn_set_day_from);
        btnSetTimeFrom = (Button) findViewById(R.id.activity_add_event_btn_set_time_from);
        btnSetDayTo = (Button) findViewById(R.id.activity_add_event_btn_set_day_to);
        btnSetTimeTo = (Button) findViewById(R.id.activity_add_event_btn_set_time_to);
        actvPlace = (AutoCompleteTextView) findViewById(R.id.activity_add_event_actv_place);
        mactvAddfriend = (MultiAutoCompleteTextView) findViewById(
                R.id.activity_add_event_mactv_add_friend);
        spnRepeat = (Spinner) findViewById(R.id.activity_add_event_spn_repeat);
        btnFind = (Button) findViewById(R.id.activity_add_event_btn_find_friend);
    }

    public void setDataForPlace() {
        String[] place = getResources().getStringArray(R.array.District);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.adapter_place, place);
        actvPlace.setAdapter(arrayAdapter);
    }

    public void setDataForAddfriend() {
        final List<String> LISTFRIEND = new ArrayList<String>();
        firebaseFriend.child("My_friend").child(mailUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            LISTFRIEND.add(snapShot.getKey().toString().replace("&", "."));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                android.R.layout.simple_spinner_item, LISTFRIEND);
                        mactvAddfriend.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                        mactvAddfriend.setThreshold(1);
                        mactvAddfriend.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

    }

    public void setDataForRepeat() {
        final String[] REPEAT = getResources().getStringArray(R.array.Repeat);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, REPEAT);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnRepeat.setAdapter(arrayAdapter);
    }

    public void getDateSelectFromHome() {
        dateSeclect = getIntent().getStringExtra("ChangeDate");
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
        toDay = getIntent().getStringExtra("ToDay");
    }

    private MenuInflater menuInflater;

    private Menu contentMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        this.contentMenu = menu;
        menuInflater.inflate(R.menu.menu_add_event, menu);
        setTitle("Add Event");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_event_done:
                //saveEventinFirebase();
                checkEntry();
                break;
            case R.id.menu_add_event_back:
                Intent intent = new Intent(AddEventActivity.this, Menu_Choose.class);
                intent.putExtra("NameUserfromAddEvent", nameUser);
                intent.putExtra("NameUser", nameUser);
                intent.putExtra("MailUser", mailUser);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonAddFriend() {
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog DIALOG = new Dialog(AddEventActivity.this);
                DIALOG.setContentView(R.layout.dialog_add_friend);
                DIALOG.setTitle("My friend");
                DIALOG.setCancelable(true);
                final ListView LISTMYFRIENDDIALOG =
                        (ListView) DIALOG.findViewById(R.id.dialog_add_friend_lv_main);
                final List<String> LISTFRIEND = new ArrayList<String>();
                firebaseFriend.child("My_friend").child(mailUser)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    LISTFRIEND.add(snapShot.getKey().toString().replace("&", "."));
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.adapter_place, LISTFRIEND);
                                LISTMYFRIENDDIALOG.setAdapter(arrayAdapter);
                                DIALOG.show();
                                LISTMYFRIENDDIALOG.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent,
                                                                    View view,
                                                                    int position, long id) {
                                                mactvAddfriend.setText(mactvAddfriend.getText()
                                                        + LISTFRIEND.get(position) + "," + " ");
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });

            }
        });
    }

    public void saveEventinFirebase() {
        String listinvite = "";
        String[] listFriendInvite = mactvAddfriend.getText().toString().split(",");
        listFriendinFirebase = new ArrayList<String>();
        listFriendinFirebase.add(mailUser);
        for (int i = 0; i < listFriendInvite.length; i++) {
            listFriendinFirebase.add(listFriendInvite[i].trim().replace(".", "&"));
        }
        for (int i = 0; i < listFriendinFirebase.size(); i++) {
            if (i == (listFriendinFirebase.size() - 1)) {
                listinvite += listFriendinFirebase.get(i);
            } else {
                listinvite += listFriendinFirebase.get(i) + ",";
            }
        }
        Map<String, String> eventValue = new Hashtable<String, String>();
        eventValue.put("NameEvent", etEventName.getText().toString());
        eventValue.put("DateFrom", tvSetDayFrom.getText().toString());
        eventValue.put("TimeFrom", tvSetTimeFrom.getText().toString());
        eventValue.put("DateTo", tvSetDayTo.getText().toString());
        eventValue.put("TimeTo", tvSetTimeTo.getText().toString());
        eventValue.put("Description", etDecription.getText().toString());
        eventValue.put("Place", actvPlace.getText().toString());
        eventValue.put("FriendInvite", listinvite);
        eventValue.put("Alarm", etAlarm.getText().toString());
        eventValue.put("Repeat", spnRepeat.getSelectedItem().toString());

        for (String mail : listFriendinFirebase) {
            firebaseFriend.child("Event").child(mail)
                    .child("New_Event").push().setValue(eventValue);
        }
        //entry();
        Toast.makeText(getApplicationContext(), "Congratulation", Toast.LENGTH_LONG).show();
    }

    public void checkNgay() {
        String error = null;
        if (toDay.replace("-", "a").compareTo(tvSetDayFrom.getText()
                .toString().replace("-", "a")) <= 0) {
            if (tvSetDayFrom.getText().toString().replace("-", "a").compareTo(
                    tvSetDayTo.getText().toString().replace("-", "a")) < 0) {
                saveEventinFirebase();
                entry();
            } else if (tvSetDayFrom.getText().toString().replace("-", "a").compareTo(
                    tvSetDayTo.getText().toString().replace("-", "a")) == 0) {
                if (tvSetTimeFrom.getText().toString().replace(":", "a")
                        .compareTo(tvSetTimeTo.getText().toString().replace(":", "a")) < 0) {
                    saveEventinFirebase();
                    entry();
                } else {
                    error = "error ic_about time to ";
                }
            } else {
                error = "error ic_about date to";
            }

        } else {
            error = "error ic_about date from";
        }
        if (error != null) {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }

    public void checkEntry() {
        String error = "";
        if ((etEventName.getText().toString().compareTo("") == 0) ||
                (tvSetDayFrom.getText().toString().compareTo("") == 0) ||
                (tvSetTimeFrom.getText().toString().compareTo("") == 0) ||
                (tvSetDayTo.getText().toString().compareTo("") == 0) ||
                (tvSetTimeTo.getText().toString().compareTo("") == 0) ||
                (etDecription.getText().toString().compareTo("") == 0) ||
                (actvPlace.getText().toString().compareTo("") == 0) ||
                (etAlarm.getText().toString().compareTo("") == 0) ||
                (spnRepeat.getSelectedItem().toString().compareTo("") == 0)) {
            error = "error entry data";
        } else {
            checkNgay();
        }
        if (error.compareTo("") != 0) {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }

    public void entry() {
        etEventName.setText("");
        tvSetDayFrom.setText("");
        tvSetTimeFrom.setText("");
        tvSetDayTo.setText("");
        tvSetTimeTo.setText("");
        etDecription.setText("");
        actvPlace.setText("");
        mactvAddfriend.setText("");
        etAlarm.setText("");
        spnRepeat.setSelection(0);
    }

}



