package com.android.smarto.architecture.contacts;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.smarto.R;
import com.android.smarto.data.Contact;
import com.android.smarto.db.model.User;
import com.android.smarto.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
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
        holder.mContactPhone.setText(mData.get(position).getMobileNumber());
        Picasso.with(mContext).load(mData.get(position).getProfileImagePath())
                .resize(100,100)
                .centerCrop().into(holder.mProfileImage);

        holder.setItemClickListener(new ContactItemClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
                mContactItemClickListener.onClick(view, position, mData.get(position).getUniqueId());
            }
        });
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
            mContactItemClickListener.onClick(v, getAdapterPosition(), "");
        }
    }

}
