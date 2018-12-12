package source.kevtimov.landlordcommunicationapp.views.login.myusers;


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
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.User;

public class MyUsersFragment extends Fragment implements ContractsMyUsers.View, RecyclerViewMyUsersAdapter.OnUserClickListener {


    @BindView(R.id.rv_users)
    RecyclerView mRecViewUsers;

    @BindView(R.id.iv_users)
    ImageView mImageViewUsers;

    @BindView(R.id.progress_bar)
    CircleProgressBar mProgressBar;

    private ContractsMyUsers.Presenter mPresenter;
    private ContractsMyUsers.Navigator mNavigator;
    private RecyclerViewMyUsersAdapter mArrayAdapterUser;
    private List<User> users;

    @Inject
    public MyUsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_my_users, container, false);
        ButterKnife.bind(this, root);
        users = new ArrayList<>();

        mArrayAdapterUser = new RecyclerViewMyUsersAdapter(getContext(), users);
        mRecViewUsers.setAdapter(mArrayAdapterUser);
        mRecViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        mArrayAdapterUser.setOnUserClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        users.clear();
        mPresenter.loadUsers();
    }


    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(ContractsMyUsers.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setNavigator(ContractsMyUsers.Navigator navigator) {
        this.mNavigator = navigator;
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
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @Override
    public void addUser(User user) {
        if(!users.contains(user)){
            users.add(user);
            mArrayAdapterUser.notifyDataSetChanged();
        }
    }

    @Override
    public void navigateToDetails(User user) {
        mNavigator.navigateToDetails(user);
    }


    @Override
    public void onUserClick(int position) {
        User user = mArrayAdapterUser.getItem(position);

        mPresenter.allowNavigation(user);
    }
}
