package source.kevtimov.landlordcommunicationapp.chat.sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class RecyclerViewChatAdapter extends RecyclerView.Adapter<RecyclerViewChatAdapter.ViewHolder> {

    private List<User> mUsersList;
    private IBitmapAgent mAgent;
    private BitmapCache mCacher;
    private Context mContext;
    private OnChatClickListener onChatClickListener;


    RecyclerViewChatAdapter(Context context, List<User> users){
        this.mContext = context;
        this.mCacher = BitmapCache.getInstance();
        this.mAgent = new BitmapAgent();
        this.mUsersList = users;
    }

    public User getItem(int position){
        return this.mUsersList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.users_chat_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewChatAdapter.ViewHolder viewHolder, int i) {

        User user = mUsersList.get(i);

        viewHolder.bindView(user);
    }

    void setOnChatClickListener(OnChatClickListener onListener) {
        this.onChatClickListener = onListener;
    }


    @Override
    public int getItemCount() {
        return this.mUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.relative_layout)
        RelativeLayout mRelativeLayout;

        @BindView(R.id.iv_user)
        ImageView mImageView;

        @BindView(R.id.tv_first_name)
        TextView mFirstName;

        @BindView(R.id.tv_last_name)
        TextView mLastName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bindView(User user){
            mFirstName.setText(user.getFirstName());
            mLastName.setText(user.getLastName());
            mFirstName.setTextColor(Color.WHITE);
            mLastName.setTextColor(Color.WHITE);

            if(user.getPicture() != null){
                Bitmap profilePic = managePicture(user.getPicture(), user);
                mImageView.setImageBitmap(profilePic);
            }

            initView(this);
        }

        @Override
        public void onClick(View v) {
            if (onChatClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onChatClickListener.onChatClick(position);
                }
            }
        }
    }

    private void initView(@NonNull RecyclerViewChatAdapter.ViewHolder viewHolder) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int selectedFont = Integer.parseInt(sharedPreferences.getString(Constants.FONT_LIST, "1"));

        switch (selectedFont) {
            case 1:
                viewHolder.mFirstName.setTypeface(EasyFonts.droidSerifBold(mContext));
                viewHolder.mLastName.setTypeface(EasyFonts.droidSerifBold(mContext));
                break;
            case 2:
                viewHolder.mFirstName.setTypeface(EasyFonts.funRaiser(mContext));
                viewHolder.mLastName.setTypeface(EasyFonts.funRaiser(mContext));
                break;
            case 3:
                viewHolder.mFirstName.setTypeface(EasyFonts.walkwayBold(mContext));
                viewHolder.mLastName.setTypeface(EasyFonts.walkwayBold(mContext));
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

    public interface OnChatClickListener {
        void onChatClick(final int position);
    }
}
