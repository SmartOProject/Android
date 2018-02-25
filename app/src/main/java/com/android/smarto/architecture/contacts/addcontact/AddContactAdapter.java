package com.android.smarto.architecture.contacts.addcontact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.smarto.R;
import com.android.smarto.db.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public class AddContactAdapter extends RecyclerView.Adapter<AddContactAdapter.AddContactViewHolder> {

    private List<User> unFriendsData;
    private Context mContext;

    public AddContactAdapter(List<User> unFriendsData, Context context) {
        this.unFriendsData = unFriendsData;
        this.mContext = context;
    }

    @Override
    public AddContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.contacts_list_item, parent, false);
        return new AddContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddContactViewHolder holder, int position) {
        holder.mContactName.setText(unFriendsData.get(position).getName());
        holder.mContactPhone.setText(unFriendsData.get(position).getMobileNumber());
        Picasso.with(mContext).load(unFriendsData.get(position).getProfileImagePath())
                .resize(100,100)
                .centerCrop().into(holder.mProfileImage);
    }

    @Override
    public int getItemCount() {
        return unFriendsData.size();
    }

    public class AddContactViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.contact_name) TextView mContactName;
        @BindView(R.id.contact_phone) TextView mContactPhone;
        @BindView(R.id.contact_profile_image) ImageView mProfileImage;

        public AddContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
