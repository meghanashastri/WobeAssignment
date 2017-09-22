package com.example.admin.wobeassignment.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.model.TransactionModel;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.FontManager;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Admin on 21-09-2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private Context context;
    private List<TransactionModel> transactionModelList;

    public TransactionAdapter(Context context) {
        this.context = context;
        transactionModelList = new ArrayList<>();
    }

    public void setDataInAdapter(List<TransactionModel> transaction) {
        this.transactionModelList = transaction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_recent_transaction, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TransactionModel model = transactionModelList.get(position);
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        holder.ivTransactionImage.setTypeface(iconFont);

        if (model != null) {

            if (model.getTransactionDate() != null) {
                holder.tvTransactionTimestamp.setText(model.getTransactionDate());
            } else {
                holder.tvTransactionTimestamp.setVisibility(View.GONE);
            }


            if (SharedPreferenceManager.getInstance(context).getString(Constants.CUSTOMER_ID).
                    equalsIgnoreCase(model.getFromCustomerID().toString())) {
                holder.ivTransactionImage.setText(context.getResources().getString(R.string.sent_icon));
                if (model.getToFirstName() != null && model.getToLastName() != null) {
                    holder.tvTransactionName.setText(model.getToFirstName() + " " + model.getToLastName());
                } else if (model.getToFirstName() != null) {
                    holder.tvTransactionName.setText(model.getToFirstName());
                } else {
                    holder.tvTransactionName.setVisibility(View.GONE);
                }
                holder.ivTransactionImage.setTextColor(context.getResources().getColor(R.color.google_red));
            } else if (SharedPreferenceManager.getInstance(context).getString(Constants.CUSTOMER_ID).
                    equalsIgnoreCase(model.getToCustomerID().toString())) {
                if (model.getFromCustomerID().compareTo(BigInteger.valueOf(999999999)) == 0) {
                    holder.ivTransactionImage.setText(context.getResources().getString(R.string.added_icon));
                    holder.ivTransactionImage.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    if (model.getFromFirstName() != null && model.getFromLastName() != null) {
                        holder.tvTransactionName.setText(model.getFromFirstName() + " " + model.getFromLastName());
                    } else if (model.getFromFirstName() != null) {
                        holder.tvTransactionName.setText(model.getFromFirstName());
                    } else {
                        holder.tvTransactionName.setVisibility(View.GONE);
                    }
                } else {
                    holder.ivTransactionImage.setText(context.getResources().getString(R.string.received_icon));
                    holder.ivTransactionImage.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    if (model.getFromFirstName() != null && model.getFromLastName() != null) {
                        holder.tvTransactionName.setText(model.getFromFirstName() + " " + model.getFromLastName());
                    } else if (model.getFromFirstName() != null) {
                        holder.tvTransactionName.setText(model.getFromFirstName());
                    } else {
                        holder.tvTransactionName.setVisibility(View.GONE);
                    }
                }
            }


            if (model.getCredits().toString() != null) {
                holder.tvCredits.setText(model.getCredits().toString());
            } else {
                holder.tvCredits.setVisibility(View.GONE);
            }

            if (model.getDescription() != null) {
                holder.tvTransactionDescription.setText(model.getDescription());
            } else {
                holder.tvTransactionDescription.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView tvTransactionTimestamp, tvCredits, tvTransactionName, tvTransactionDescription;
        private TextView ivTransactionImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTransactionTimestamp = (TextView) itemView.findViewById(R.id.tvTransactionTimestamp);
            tvCredits = (TextView) itemView.findViewById(R.id.tvCredits);
            tvTransactionName = (TextView) itemView.findViewById(R.id.tvTransactionName);
            tvTransactionDescription = (TextView) itemView.findViewById(R.id.tvTransactionDescription);
            ivTransactionImage = (TextView) itemView.findViewById(R.id.ivTransactionImage);
            mView = itemView;
        }

    }
}
