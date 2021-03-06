package source.kevtimov.landlordcommunicationapp.views.login.signup;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.utils.Constants;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.IBitmapAgent;

public class SignUpFragment extends Fragment implements ContractsSignUp.View {


    @BindView(R.id.iv_photo)
    ImageView mImageView;

    @BindView(R.id.tv_enter_username)
    TextView mTextViewEnterUsername;

    @BindView(R.id.tv_username_constraints)
    TextView mTextViewUsernameConstraint;

    @BindView(R.id.tv_enter_password)
    TextView mTextViewEnterPassword;

    @BindView(R.id.tv_password_constraints)
    TextView mTextViewPassConstraint;

    @BindView(R.id.tv_retype_password)
    TextView mTextViewRetypePass;

    @BindView(R.id.tv_first_name)
    TextView mTextViewEnterFirstName;

    @BindView(R.id.tv_last_name)
    TextView mTextViewEnterLastName;

    @BindView(R.id.tv_enter_email)
    TextView mTextViewEnterEmail;

    @BindView(R.id.tv_choose_type)
    TextView mTextViewEnterType;

    @BindView(R.id.et_enter_username)
    EditText mEditTextEnterUsername;

    @BindView(R.id.et_enter_password)
    EditText mEditTextEnterPass;

    @BindView(R.id.et_retype_password)
    EditText mEditTextRetypePass;

    @BindView(R.id.et_enter_first_name)
    EditText mEditTextEnterFirstName;

    @BindView(R.id.et_enter_last_name)
    EditText mEditTextEnterLastName;

    @BindView(R.id.et_enter_email)
    EditText mEditTextEnterEmail;

    @BindView(R.id.radiod_select_type)
    RadioGroup mRadioGroup;

    @BindView(R.id.btn_finish)
    Button mButtonContinue;

    @BindView(R.id.btn_finish_2)
    Button mButtonContinue2;

    @BindView(R.id.btn_gallery)
    FloatingActionButton mButtonGallery;

    @BindView(R.id.progress_bar)
    CircleProgressBar mProgressBar;

    private RadioButton radioTypeButton;
    private ContractsSignUp.Presenter mPresenter;
    private ContractsSignUp.Navigator mNavigator;
    private Bundle mUserInfoData;
    private int radioButtonSelectedId;
    private int radioButtonResult;
    private String mSelectedProfilePic;


    @Inject
    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ButterKnife.bind(this, root);

        getActivity()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (mUserInfoData.getString("intent_purpose").equals("facebook")) {
            manageViewVisibility();
        }


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(ContractsSignUp.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setNavigator(ContractsSignUp.Navigator navigator) {
        this.mNavigator = navigator;
    }

    @Override
    public void proceedToPlaceManagement(User user) {

        StyleableToast.makeText(getContext(), Constants.SIGN_UP_SUCCESSFUL,
                Toast.LENGTH_LONG, R.style.accept_login_toast)
                .show();

        mNavigator.navigateToPlaceManagement(user);
    }

    @Override
    public void setBundleWithUserInfo(Bundle userInfo) {
        this.mUserInfoData = userInfo;
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
    public void signUpFail() {
        StyleableToast.makeText(getContext(), Constants.SIGN_UP_FAILED,
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @Override
    public void showError(Throwable error) {
        StyleableToast.makeText(getContext(), error.getMessage(),
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @Override
    public void alertForExistingUsername() {
        StyleableToast.makeText(getContext(), Constants.USERNAME_EXISTS,
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @Override
    public void alertForExistingEmail() {
        StyleableToast.makeText(getContext(), Constants.EMAIL_EXISTS,
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();

    }

    @Override
    public void alertForExistingUsernameAndEmail() {

        StyleableToast.makeText(getContext(), Constants.USERNAME_AND_EMAIL_EXIST,
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @Override
    public void processCheckResult(User user) {
        if (user.getUsername().equals("used") && user.getEmail().equals("used")) {
            alertForExistingUsernameAndEmail();
        } else if (user.getEmail().equals("used")) {
            alertForExistingEmail();
        } else if (user.getUsername().equals("used")) {
            alertForExistingUsername();
        } else {
            if (Objects.equals(mUserInfoData.getString("intent_purpose"), "facebook")) {
                setFacebookDefaultPic();
            }
            mPresenter.registerUser(mUserInfoData);
        }
    }

    // custom sign up
    @OnClick(R.id.btn_finish)
    public void onClickFinishBottom(View root) {

        radioButtonResult = getRadioButtonResult();

        if (mEditTextEnterUsername.getText().toString().length() == 0
                || mEditTextEnterPass.getText().toString().length() == 0
                || mEditTextRetypePass.getText().toString().length() == 0
                || mEditTextEnterEmail.getText().toString().length() == 0
                || mEditTextEnterFirstName.getText().toString().length() == 0
                || mEditTextEnterLastName.getText().toString().length() == 0
                || radioButtonResult == -1) {

            StyleableToast.makeText(getContext(), Constants.DONT_LEAVE_BLANK_INFORMATION,
                    Toast.LENGTH_LONG, R.style.reject_login_toast)
                    .show();
        } else if (mEditTextEnterEmail.getText().toString().length() < 9
                || mEditTextEnterFirstName.getText().toString().length() < 3
                || mEditTextEnterLastName.getText().toString().length() < 3
                || radioButtonResult == -1) {

            StyleableToast.makeText(getContext(), Constants.EMAIL_FIRST_LAST_NAME_CONSTRAINT,
                    Toast.LENGTH_LONG, R.style.reject_login_toast)
                    .show();
        } else if (mEditTextEnterUsername.getText().toString().length() < 6
                || mEditTextEnterPass.getText().toString().length() < 6
                || mEditTextRetypePass.getText().toString().length() < 6) {

            StyleableToast.makeText(getContext(), Constants.PAY_ATTENTION_ON_ALL_CONSTRAINTS,
                    Toast.LENGTH_LONG, R.style.reject_login_toast)
                    .show();
        } else if (!mEditTextEnterPass.getText().toString().equals(mEditTextRetypePass.getText().toString())) {
            StyleableToast.makeText(getContext(), Constants.DIFF_BETWEEN_PASSWORDS,
                    Toast.LENGTH_LONG, R.style.reject_login_toast)
                    .show();
        } else {
            mUserInfoData = fillBundleWithUserData();
            mPresenter.checkUsernameAndEmail(mUserInfoData.getString("username"), mUserInfoData.getString("email"));
        }
    }


    //facebook sign up
    @OnClick(R.id.btn_finish_2)
    public void onClickFinishTop(View root) {

        radioButtonResult = getRadioButtonResult();

        if (radioButtonResult == -1) {
            StyleableToast.makeText(getContext(), Constants.CHOOSE_TYPE_INFORMATION,
                    Toast.LENGTH_LONG, R.style.reject_login_toast)
                    .show();
        } else {
            boolean isLandlord = false;
            if (radioButtonResult == 2) {
                isLandlord = true;
            }

            mUserInfoData.putBoolean("isLandlord", isLandlord);
            mPresenter.checkUsernameAndEmail(mUserInfoData.getString("fb_username"), mUserInfoData.getString("fb_email"));

        }
    }


    @OnClick(R.id.btn_gallery)
    public void onClickGallery(View v) {
        mNavigator.navigateToGallery();
    }

    @Override
    public void setImage(Bitmap bitmap) {
        if(bitmap != null){
            mImageView.setImageBitmap(bitmap);
            mSelectedProfilePic = mPresenter.convertBitmapToString(bitmap);
            mPresenter.setBitmapToCache(bitmap);
        }
    }

    private Bundle fillBundleWithUserData() {

        Bundle filledBundle = new Bundle();
        boolean isLandlord = false;
        if (radioButtonResult == 2) {
            isLandlord = true;
        }

        filledBundle.putBoolean("isLandlord", isLandlord);
        filledBundle.putString("username", mEditTextEnterUsername.getText().toString());
        filledBundle.putString("password", mEditTextEnterPass.getText().toString());
        filledBundle.putString("first_name", mEditTextEnterFirstName.getText().toString());
        filledBundle.putString("last_name", mEditTextEnterLastName.getText().toString());
        filledBundle.putString("email", mEditTextEnterEmail.getText().toString());

        if(mSelectedProfilePic == null){
            Bitmap bm = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            mSelectedProfilePic = mPresenter.convertBitmapToString(bm);
            mPresenter.setBitmapToCache(bm);
        }
        filledBundle.putString("profile_pic", mSelectedProfilePic);

        return filledBundle;
    }

    private int getRadioButtonResult() {

        radioButtonSelectedId = mRadioGroup.getCheckedRadioButtonId();
        radioTypeButton = (RadioButton) Objects.requireNonNull(getActivity()).findViewById(radioButtonSelectedId);

        if (radioButtonSelectedId == -1) {
            return -1;
        } else if (radioTypeButton.getText().equals("Tenant")) {
            return 1;
        } else if (radioTypeButton.getText().equals("Landlord")) {
            return 2;
        }
        return -1;
    }

    private void setFacebookDefaultPic() {
        Bitmap bitmap = BitmapFactory
                .decodeResource(getResources(),
                        R.drawable.com_facebook_profile_picture_blank_portrait);
        String bitmapString = mPresenter.convertBitmapToString(bitmap);
        mPresenter.setBitmapToCache(bitmap);
        mUserInfoData.putString("fb_prof_pic", bitmapString);
    }

    private void manageViewVisibility() {

        mTextViewEnterUsername.setVisibility(View.GONE);
        mTextViewUsernameConstraint.setVisibility(View.GONE);
        mTextViewEnterPassword.setVisibility(View.GONE);
        mTextViewPassConstraint.setVisibility(View.GONE);
        mTextViewRetypePass.setVisibility(View.GONE);
        mTextViewEnterFirstName.setVisibility(View.GONE);
        mTextViewEnterLastName.setVisibility(View.GONE);
        mTextViewEnterEmail.setVisibility(View.GONE);
        mEditTextEnterUsername.setVisibility(View.GONE);
        mEditTextEnterPass.setVisibility(View.GONE);
        mEditTextRetypePass.setVisibility(View.GONE);
        mEditTextEnterFirstName.setVisibility(View.GONE);
        mEditTextEnterLastName.setVisibility(View.GONE);
        mEditTextEnterEmail.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        mButtonGallery.setVisibility(View.GONE);
        mButtonContinue.setVisibility(View.GONE);

        mButtonContinue2.setVisibility(View.VISIBLE);
    }
}
