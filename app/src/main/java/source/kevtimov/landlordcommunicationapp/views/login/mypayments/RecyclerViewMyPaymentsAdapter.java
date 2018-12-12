package source.kevtimov.landlordcommunicationapp.views.login.mypayments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.Payment;
import source.kevtimov.landlordcommunicationapp.utils.Constants;

public class RecyclerViewMyPaymentsAdapter extends RecyclerView.Adapter<RecyclerViewMyPaymentsAdapter.ViewHolder> {


    private List<Payment> mPayments;
    private Context mContext;
    private OnPaymentClickListener onPaymentClickListener;

    RecyclerViewMyPaymentsAdapter(Context context){
        this.mContext = context;
        this.mPayments = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_my_payments_adapter, viewGroup, false);

        return new RecyclerViewMyPaymentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Payment payment = mPayments.get(i);
        viewHolder.bindView(payment);
    }

    public void add(Payment payment){
        this.mPayments.add(payment);
    }

    void addAll(List<Payment> payments){
        this.mPayments.addAll(payments);
    }

    public void clear(){
        this.mPayments.clear();
    }

    void setOnPaymentClickListener(OnPaymentClickListener listener){
        this.onPaymentClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return this.mPayments.size();
    }

    public Payment getItem(int position){
        return this.mPayments.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_money)
        ImageView mImageViewMoney;

        @BindView(R.id.tv_date)
        TextView mTextViewDate;

        @BindView(R.id.tv_amount)
        TextView mTextViewAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        void bindView(Payment payment){
            mTextViewAmount.setText(Constants.AMOUNT + Objects.requireNonNull(payment).getAmount());
            mTextViewDate.setText(Constants.DATE + payment.getDate());

            initFont();
        }

        private void initFont() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            int selectedFont = Integer.parseInt(sharedPreferences.getString(Constants.FONT_LIST, "1"));

            switch (selectedFont) {
                case 1:
                    mTextViewAmount.setTypeface(EasyFonts.droidSerifBold(mContext));
                    mTextViewDate.setTypeface(EasyFonts.droidSerifBold(mContext));
                    break;
                case 2:
                    mTextViewAmount.setTypeface(EasyFonts.funRaiser(mContext));
                    mTextViewDate.setTypeface(EasyFonts.funRaiser(mContext));
                    break;
                case 3:
                    mTextViewAmount.setTypeface(EasyFonts.walkwayBold(mContext));
                    mTextViewDate.setTypeface(EasyFonts.walkwayBold(mContext));
                    break;

            }
        }

        @Override
        public void onClick(View v) {
            if (onPaymentClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onPaymentClickListener.onPaymentClick(position);
                }
            }
        }
    }

    public interface OnPaymentClickListener{
        void onPaymentClick(int position);
    }
}
