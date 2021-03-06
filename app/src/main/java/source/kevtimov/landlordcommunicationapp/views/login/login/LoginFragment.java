package source.kevtimov.landlordcommunicationapp.views.login.login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.muddzdev.styleabletoast.StyleableToast;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.parsers.GsonJsonParser;
import source.kevtimov.landlordcommunicationapp.parsers.base.JsonParser;
import source.kevtimov.landlordcommunicationapp.utils.Constants;

import static com.facebook.GraphRequest.TAG;

public class LoginFragment extends Fragment implements ContractsLogin.View {

    private ContractsLogin.Presenter mPresenter;
    private ContractsLogin.Navigator mNavigator;
    private CallbackManager mFacebookCallbackManager;

    @BindView(R.id.fb_login_button)
    LoginButton mFacebookButton;

    @BindView(R.id.tv_username)
    TextView mTextViewUsername;

    @BindView(R.id.tv_password)
    TextView mTextViewPassword;

    @BindView(R.id.et_username)
    EditText mEditTextUsername;

    @BindView(R.id.et_password)
    EditText mEditTextPassword;

    @BindView(R.id.progressBar)
    CircleProgressBar mLoadingView;

    @BindView(R.id.iv_login)
    ImageView mImageViewLogIn;

    @Inject
    JsonParser<User> mJsonParser;


    private String mEmailFacebook;
    private String mFacebookFirstName;
    private String mFacebookLastName;
    private String mProfPicture;


    @Inject
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);
        initFonts();

        getActivity()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setUpFacebookLogin();

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        checkIfNotAlreadyLoggedInUser();

    }

    @Override
    public void onPause() {
        super.onPause();
        //mPresenter.unsubscribe();
    }


    @Override
    public void setPresenter(ContractsLogin.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mTextViewUsername.setVisibility(View.INVISIBLE);
        mTextViewPassword.setVisibility(View.INVISIBLE);
        mEditTextUsername.setVisibility(View.INVISIBLE);
        mEditTextPassword.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.INVISIBLE);
        mTextViewUsername.setVisibility(View.VISIBLE);
        mTextViewPassword.setVisibility(View.VISIBLE);
        mEditTextUsername.setVisibility(View.VISIBLE);
        mEditTextPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNavigator(ContractsLogin.Navigator navigator) {
        mNavigator = navigator;
    }

    @Override
    public void showError(Throwable error) {
        StyleableToast.makeText(getContext(), error.getMessage(),
                Toast.LENGTH_LONG, R.style.accept_login_toast).show();
    }

    @Override
    public void cancelLogin() {

        StyleableToast.makeText(getContext(), Constants.INCORRECT_USERNAME_OR_PASSWORD,
                Toast.LENGTH_LONG, R.style.reject_login_toast).show();
        YoYo.with(Techniques.Bounce)
                .duration(700)
                .repeat(5)
                .playOn(mTextViewUsername);

        YoYo.with(Techniques.Bounce)
                .duration(700)
                .repeat(5)
                .playOn(mTextViewPassword);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void welcomeUser(User user) {

        StyleableToast.makeText(getContext(), Constants.WELCOME + user.getFirstName() + " " + user.getLastName() + " !",
                Toast.LENGTH_LONG, R.style.accept_login_toast).show();

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(mTextViewUsername);

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(mTextViewPassword);

        mPresenter.setBitmapToCache(user.getPicture());
        mNavigator.navigateToHome(user);
    }

    @Override
    public void alertUserForBlankInfo() {
        StyleableToast.makeText(getContext(), Constants.USERNAME_AND_PASSWORD_EMPTY,
                Toast.LENGTH_LONG, R.style.reject_login_toast).show();

        YoYo.with(Techniques.Flash)
                .duration(700)
                .repeat(5)
                .playOn(mEditTextUsername);

        YoYo.with(Techniques.Flash)
                .duration(700)
                .repeat(5)
                .playOn(mEditTextPassword);
    }

    @Override
    public void alertUserForLengthConstraints() {
        StyleableToast.makeText(getContext(), Constants.USERNAME_AND_PASSWORD_LENGTH_CONSTRAINT,
                Toast.LENGTH_LONG, R.style.reject_login_toast).show();

        YoYo.with(Techniques.Flash)
                .duration(700)
                .repeat(5)
                .playOn(mEditTextUsername);

        YoYo.with(Techniques.Flash)
                .duration(700)
                .repeat(5)
                .playOn(mEditTextPassword);
    }

    @Override
    public void facebookRegisterAlert() {
        StyleableToast.makeText(getContext(), Constants.ADD_ADDITIONAL_INFO_ABOUT_USER,
                Toast.LENGTH_LONG, R.style.facebook_login_toast).show();

        Bundle fbInfo = new Bundle();
        fbInfo.putString("intent_purpose", "facebook");
        fbInfo.putString("fb_first_name", mFacebookFirstName);
        fbInfo.putString("fb_last_name", mFacebookLastName);
        fbInfo.putString("fb_email", mEmailFacebook);
        fbInfo.putString("fb_username", mEmailFacebook);

        mNavigator.navigateToSignUp(fbInfo);
    }

    @Override
    public void proceedToSignUp() {
        Bundle userInfo = new Bundle();
        userInfo.putString("intent_purpose", "custom");
        mNavigator.navigateToSignUp(userInfo);
    }


    @OnClick(R.id.btn_login)
    public void onClickLogin() {
        String enteredUsername = mEditTextUsername.getText().toString();
        String enteredPassword = mEditTextPassword.getText().toString();
        mPresenter.checkLogin(enteredUsername, enteredPassword);
    }

    @OnClick(R.id.btn_signup)
    public void onClickSignUp() {
        mPresenter.allowSignUp();
    }

    private void setUpFacebookLogin() {
        mFacebookCallbackManager = CallbackManager.Factory.create();

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        //for reading permission
        mFacebookButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        mFacebookButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {


                        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

                        getUserProfile(AccessToken.getCurrentAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                        error.printStackTrace();
                    }
                }
        );

    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            mFacebookFirstName = object.getString("first_name");
                            mFacebookLastName = object.getString("last_name");
                            mEmailFacebook = object.getString("email");
                            String id = object.getString("id");
                            //mProfPicture = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            mPresenter.checkFacebookUserByUsername(mEmailFacebook);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void initFonts() {
        mFacebookButton.setFragment(this);
        mTextViewUsername.setTypeface(EasyFonts.funRaiser(getContext()));
        mTextViewPassword.setTypeface(EasyFonts.funRaiser(getContext()));
    }

    private void checkIfNotAlreadyLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_USER_INFO, "").length() != 0) {
            User user = mJsonParser.fromJson(sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_USER_INFO, ""));
            welcomeUser(user);
        }
    }
}
