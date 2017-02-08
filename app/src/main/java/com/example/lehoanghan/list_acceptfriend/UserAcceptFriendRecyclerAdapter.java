package com.example.lehoanghan.list_acceptfriend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;


public class UserAcceptFriendRecyclerAdapter extends RecyclerView.Adapter<UserAcceptFriendViewHolder> {

    View view;

    private List<UserFriend> ListUserAccept;
    private Firebase firebase;
    private String GetMail;
    private String GetName;
    private int Test=0;

    public UserAcceptFriendRecyclerAdapter(List<UserFriend> listUser, String mail, String name)
    {
        ListUserAccept=listUser;
        GetMail=mail;
        GetName=name;
    }


    @Override
    public UserAcceptFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_acceptfriend, parent, false);
        Firebase.setAndroidContext(view.getContext());
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        return new UserAcceptFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAcceptFriendViewHolder holder, final int position) {
        final UserFriend userFindFriend =ListUserAccept.get(position);
        holder.TxtUserNameAcceptFriend.setText(userFindFriend.getName());
        holder.TxtUserMailAcceptFriend.setText(userFindFriend.getMail());
        holder.BtnUserAcceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final  String change=userFindFriend.getMail().replace(".","&");
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        firebase.child("My_friend").child(GetMail).child(change).setValue(userFindFriend.getName());
                        firebase.child("My_friend").child(change).child(GetMail).setValue(GetName);
                       // firebase.child("Add_friend").child(GetMail).child(change).setValue(null);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
               /* firebase.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                        firebase.child("My_friend").child(GetMail).child(change).setValue(userFindFriend.getName());
                        firebase.child("My_friend").child(change).child(GetMail).setValue(GetName);
                        //firebase.child("Add_friend").child(GetMail).child(change).setValue(null);
                    }

                   @Override
                   public void onCancelled(FirebaseError firebaseError) {

                   }
                });*/
                ListUserAccept.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, ListUserAccept.size());
                //notifyItemChanged(position);
               // if(holder.BtnUserAcceptFriend.getVisibility()==view.VISIBLE)
                holder.BtnUserAcceptFriend.setVisibility(view.INVISIBLE);

            }
        });


    }

    @Override
    public int getItemCount() {
        return ListUserAccept.size();
    }

}