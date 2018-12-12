package source.kevtimov.landlordcommunicationapp.views.login.myusers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.utils.Constants;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcache.BitmapCache;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.BitmapAgent;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.IBitmapAgent;

public class RecyclerViewMyUsersAdapter extends RecyclerView.Adapter<RecyclerViewMyUsersAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private IBitmapAgent mAgent;
    private BitmapCache mCacher;
    private OnUserClickListener onUserClickListener;

    RecyclerViewMyUsersAdapter(Context context, List<User> users) {
        this.mContext = context;
        this.mUsers = users;
        this.mCacher = BitmapCache.getInstance();
        this.mAgent = new BitmapAgent();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.users_adapter_view, viewGroup, false);

        return new RecyclerViewMyUsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = mUsers.get(i);
        viewHolder.bindView(user);
    }

    @Override
    public int getItemCount() {
        return this.mUsers.size();
    }

    public User getItem(int position){
        return this.mUsers.get(position);
    }

    void setOnUserClickListener(OnUserClickListener listener) {
        this.onUserClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_first_name)
        TextView mFirstName;

        @BindView(R.id.tv_last_name)
        TextView mLastName;

        @BindView(R.id.iv_user)
        RoundedImageView mImageView;

        @BindView(R.id.relative_layout)
        RelativeLayout mRelativeLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindView(User user) {
            mFirstName.setText(user.getFirstName());
            mLastName.setText(user.getLastName());
            mFirstName.setTextColor(Color.WHITE);
            mLastName.setTextColor(Color.WHITE);

            if (user.getPicture() != null) {
                Bitmap profilePic = managePicture(user.getPicture(), user);
                mImageView.setImageBitmap(profilePic);
            }

            initView();
        }

        @Override
        public void onClick(View v) {
            if (onUserClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onUserClickListener.onUserClick(position);
                }
            }
        }

        private void initView() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            int selectedFont = Integer.parseInt(sharedPreferences.getString(Constants.FONT_LIST, "1"));

            switch (selectedFont) {
                case 1:
                    mFirstName.setTypeface(EasyFonts.droidSerifBold(mContext));
                    mLastName.setTypeface(EasyFonts.droidSerifBold(mContext));
                    break;
                case 2:
                    mFirstName.setTypeface(EasyFonts.funRaiser(mContext));
                    mLastName.setTypeface(EasyFonts.funRaiser(mContext));
                    break;
                case 3:
                    mFirstName.setTypeface(EasyFonts.walkwayBold(mContext));
                    mLastName.setTypeface(EasyFonts.walkwayBold(mContext));
                    break;

            }

            int theme = Integer.parseInt(sharedPreferences.getString(Constants.THEME_LIST, "1"));

            switch (theme) {
                case 1:
                    Resources res = mContext.getResources();
                    Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.list_view_selector_purple, null);
                    mRelativeLayout.setBackground(drawable);
                    break;
                case 2:
                    Resources res2 = mContext.getResources();
                    Drawable drawable2 = ResourcesCompat.getDrawable(res2, R.drawable.list_view_selector_green, null);
                    mRelativeLayout.setBackground(drawable2);
                    break;

            }
        }

        private Bitmap managePicture(String picture, User user) {
            Bitmap bitmap = mAgent.convertStringToBitmap(picture);

            if (mCacher.getLruCache().get(user.getUsername() + "_profile_image") == null) {
                mCacher.getLruCache().put(user.getUsername() + "_profile_image", bitmap);
            }

            return bitmap;
        }
    }

    public interface OnUserClickListener {
        void onUserClick(int position);
    }
}
