package com.example.lehoanghan.optionmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.list_acceptfriend.UserAcceptFriendRecyclerAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lehoanghan on 3/30/2016.
 */
@EFragment(R.layout.fragment_friend_accept)
public class FriendAcceptFragment extends Fragment {

    private static String sGetName;

    private static String sGetMail;

    @ViewById(R.id.fragment_friend_accept_rcv_listUserFriendAccept)
    RecyclerView rcvListFriendAccept;

    private Bundle bundleGiveMailfromMenu;

    private Firebase gFirebase;

    private List<String> listMail;

    private List<String> listName;

    private List<String> listMailPre;

    private List<String> listNamePre;

    private List<UserFriend> listUserAccept;

    private UserAcceptFriendRecyclerAdapter userAcceptFriendRecyclerAdapter;

    private LinearLayoutManager linearLayoutManager;

    public FriendAcceptFragment() {
    }

    @AfterViews
    void afterView() {
        //using Google firebase
        Firebase.setAndroidContext(getActivity());
        gFirebase = new Firebase("https://appcalendar.firebaseio.com/");

        giveUserfromChoose();
        initView();
        //getDatafromFireBase();
        resetData();
    }

    public void resetData() {
        gFirebase.child("My_friend").child(sGetMail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            //String change = Snapshot.getKey().toString().replace("&", ".");
                            listMailPre.add(snapShot.getKey());
                            listNamePre.add(snapShot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
        gFirebase.child("Add_friend").child(sGetMail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            int check = 0;
                            for (String mail : listMailPre) {
                                if (snapShot.getKey().compareTo(mail) == 0) {
                                    check = 1;
                                    break;
                                }
                            }
                            if (check == 0) {
                                listMail.add(snapShot.getKey().toString().replace("&", "."));
                                listName.add(snapShot.getValue().toString());
                            }
                        }
                        for (int i = 0; i < listName.size(); i++) {
                            listUserAccept.add(new UserFriend(listName.get(i), listMail.get(i)));
                        }

                        userAcceptFriendRecyclerAdapter =
                                new UserAcceptFriendRecyclerAdapter(
                                        listUserAccept, sGetMail, sGetName);
                        userAcceptFriendRecyclerAdapter.notifyDataSetChanged();
                        rcvListFriendAccept.setAdapter(userAcceptFriendRecyclerAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    public void initView() {
        listUserAccept = new ArrayList<UserFriend>();
        listName = new ArrayList<String>();
        listMail = new ArrayList<String>();
        listNamePre = new ArrayList<String>();
        listMailPre = new ArrayList<String>();
        rcvListFriendAccept.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListFriendAccept.setLayoutManager(linearLayoutManager);
    }

    public void giveUserfromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            sGetMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //sGetMail=sGetMail.replace("&", ".");
            sGetName = bundleGiveMailfromMenu.getString("NameforFindFriend");
            sGetName = sGetName.toLowerCase();
        }
    }

}
