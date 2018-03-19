package com.im.smarto.ui.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.im.smarto.R;
import com.im.smarto.db.entities.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anatoly Chernyshev on 20.02.2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<User> mData;
    private Context mContext;
    private ContactItemClickListener mContactItemClickListener;

    public ContactsAdapter(List<User> data, Context context, ContactItemClickListener contactItemClickListener) {
        mData = data;
        this.mContext = context;
        this.mContactItemClickListener = contactItemClickListener;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.contacts_list_item, parent, false);
        return new ContactsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.mContactName.setText(mData.get(position).getName());
        holder.mContactPhone.setText(mData.get(position).getPhone());
        Picasso.with(mContext).load(mData.get(position).getImgUrl())
                .resize(100,100)
                .centerCrop().into(holder.mProfileImage);

        holder.setItemClickListener((user) ->
                mContactItemClickListener.onClick(mData.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.contact_name) TextView mContactName;
        @BindView(R.id.contact_phone) TextView mContactPhone;
        @BindView(R.id.contact_profile_image) ImageView mProfileImage;

        private ContactItemClickListener mContactItemClickListener;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ContactItemClickListener listener){
            this.mContactItemClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            mContactItemClickListener.onClick(null);
        }
    }

    public void updateList(List<User> contacts){
        mData.clear();
        mData.addAll(contacts);
        this.notifyDataSetChanged();
    }

}
