package source.kevtimov.landlordcommunicationapp.views.login.myusers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.utils.Constants;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcache.BitmapCache;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.BitmapAgent;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.IBitmapAgent;

public class UsersAdapter  extends ArrayAdapter<User> {

    private List<User> userList = new ArrayList<User>();
    private IBitmapAgent mAgent;
    private BitmapCache mCacher;

    @BindView(R.id.tv_first_name)
    TextView mFirstName;

    @BindView(R.id.tv_last_name)
    TextView mLastName;

    @BindView(R.id.iv_user)
    RoundedImageView mImageView;

    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;

    public UsersAdapter(Context context) {
        super(context, -1);
        this.mCacher = BitmapCache.getInstance();
        this.mAgent = new BitmapAgent();
    }

    @Override
    public void add(User object) {
        userList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.userList.size();
    }

    @Override
    public User getItem(int index) {
        return this.userList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.users_adapter_view, parent, false);
        }

        ButterKnife.bind(this, view);

        User user = getItem(position);

        mFirstName.setText(user.getFirstName());
        mLastName.setText(user.getLastName());
        mFirstName.setTextColor(Color.WHITE);
        mLastName.setTextColor(Color.WHITE);

        if(user.getPicture() != null){
            Bitmap profilePic = managePicture(user.getPicture(), user);
            mImageView.setImageBitmap(profilePic);
        }

        initView();
        return view;
    }

    private void initView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int selectedFont = Integer.parseInt(sharedPreferences.getString(Constants.FONT_LIST, "1"));

        switch (selectedFont) {
            case 1:
                mFirstName.setTypeface(EasyFonts.droidSerifBold(getContext()));
                mLastName.setTypeface(EasyFonts.droidSerifBold(getContext()));
                break;
            case 2:
                mFirstName.setTypeface(EasyFonts.funRaiser(getContext()));
                mLastName.setTypeface(EasyFonts.funRaiser(getContext()));
                break;
            case 3:
                mFirstName.setTypeface(EasyFonts.walkwayBold(getContext()));
                mLastName.setTypeface(EasyFonts.walkwayBold(getContext()));
                break;

        }

        int theme =  Integer.parseInt(sharedPreferences.getString(Constants.THEME_LIST, "1"));

        switch(theme){
            case 1:
                Resources res = getContext().getResources();
                Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.list_view_selector_purple, null);
                mRelativeLayout.setBackground(drawable);
                break;
            case 2:
                Resources res2 = getContext().getResources();
                Drawable drawable2 = ResourcesCompat.getDrawable(res2, R.drawable.list_view_selector_green, null);
                mRelativeLayout.setBackground(drawable2);
                break;

        }
    }

    private Bitmap managePicture(String picture, User user){
        Bitmap bitmap = mAgent.convertStringToBitmap(picture);

        if(mCacher.getLruCache().get(user.getUsername() + "_profile_image") == null){
            mCacher.getLruCache().put(user.getUsername() + "_profile_image", bitmap);
        }

        return bitmap;
    }
}
