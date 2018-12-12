package source.kevtimov.landlordcommunicationapp.views.login.mypayments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.Payment;
import source.kevtimov.landlordcommunicationapp.utils.Constants;

public class MyPaymentsFragment extends Fragment implements ContractsMyPayments.View, RecyclerViewMyPaymentsAdapter.OnPaymentClickListener {

    @BindView(R.id.iv_payment)
    ImageView mImageViewMoney;

    @BindView(R.id.rv_payments)
    RecyclerView mRecViewPayments;

    @BindView(R.id.progressBar)
    CircleProgressBar mProgressBar;


    private ContractsMyPayments.Presenter mPresenter;
    private RecyclerViewMyPaymentsAdapter mPaymentAdapter;
    private AlertDialog mDialog;


    @Inject
    public MyPaymentsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_payments, container, false);
        ButterKnife.bind(this, root);

        mPaymentAdapter = new RecyclerViewMyPaymentsAdapter(getContext());
        mRecViewPayments.setAdapter(mPaymentAdapter);
        mRecViewPayments.setLayoutManager(new LinearLayoutManager(getContext()));
        mPaymentAdapter.setOnPaymentClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        mPresenter.loadPayments();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(ContractsMyPayments.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        StyleableToast.makeText(getContext(), error.getMessage(),
                Toast.LENGTH_LONG, R.style.reject_login_toast).show();
    }

    @Override
    public void showPayments(List<Payment> payments) {
        mPaymentAdapter.clear();
        mPaymentAdapter.addAll(payments);
        mPaymentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyList() {
        StyleableToast.makeText(getContext(), Constants.NO_PAYMENTS_FOUND,
                Toast.LENGTH_LONG, R.style.reject_login_toast).show();
    }

    private void showInfoDialog(Payment payment) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(Constants.PAYMENT_AMOUNT + payment.getAmount() + "\n" + Constants.DATE + payment.getDate() + "\n" + Constants.CARD_NUMBER
                + payment.getCard().getCardNumber() + "\n" + Constants.PLACE + payment.getPlace().getAddress() + "\n"
                + Constants.TENANT + payment.getUser().getFirstName() + " " + payment.getUser().getLastName() + "\n")
                .setPositiveButton(Constants.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                })
                .setTitle(Constants.PAYMENT_INFORMATION)
                .setIcon(R.drawable.money)
                .show();

         mDialog = builder.create();
    }

    @Override
    public void onPaymentClick(int position) {
        Payment payment = mPaymentAdapter.getItem(position);

        showInfoDialog(Objects.requireNonNull(payment));
    }
}
