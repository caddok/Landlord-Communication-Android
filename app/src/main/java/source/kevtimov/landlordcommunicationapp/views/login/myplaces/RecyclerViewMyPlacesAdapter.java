package source.kevtimov.landlordcommunicationapp.views.login.myplaces;

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
import source.kevtimov.landlordcommunicationapp.models.Place;
import source.kevtimov.landlordcommunicationapp.utils.Constants;

public class RecyclerViewMyPlacesAdapter extends RecyclerView.Adapter<RecyclerViewMyPlacesAdapter.ViewHolder> {

    private Context mContext;
    private List<Place> mPlaces;
    private OnPlaceClickListener onPlaceClickListener;

    RecyclerViewMyPlacesAdapter(Context context){
        this.mContext = context;
        this.mPlaces = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_my_places_adapter, viewGroup, false);

        return new RecyclerViewMyPlacesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Place place = mPlaces.get(i);
        viewHolder.bindView(place);
    }

    @Override
    public int getItemCount() {
        return this.mPlaces.size();
    }

    public void add(Place place){
        this.mPlaces.add(place);
    }

    void addAll(List<Place> places){
        this.mPlaces.addAll(places);
    }

    public void clear(){
        this.mPlaces.clear();
    }

    public Place getItem(int position){
        return this.mPlaces.get(position);
    }

    void setOnPlaceClickListener(OnPlaceClickListener listener){
        this.onPlaceClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_house)
        ImageView mImageViewPlaces;

        @BindView(R.id.tv_place_addresses)
        TextView mTextViewPlacesAddress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        void bindView(Place place){
            mTextViewPlacesAddress.setText(Constants.ADDRESS + Objects.requireNonNull(place).getAddress());
            initFont();
        }

        @Override
        public void onClick(View v) {
            if (onPlaceClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onPlaceClickListener.onPlaceClick(position);
                }
            }
        }

        private void initFont() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            int selectedFont = Integer.parseInt(sharedPreferences.getString(Constants.FONT_LIST, "1"));

            switch (selectedFont) {
                case 1:
                    mTextViewPlacesAddress.setTypeface(EasyFonts.droidSerifBold(mContext));
                    break;
                case 2:
                    mTextViewPlacesAddress.setTypeface(EasyFonts.funRaiser(mContext));
                    break;
                case 3:
                    mTextViewPlacesAddress.setTypeface(EasyFonts.walkwayBold(mContext));
                    break;

            }
        }
    }

    public interface OnPlaceClickListener{
        void onPlaceClick(int position);
    }
}
