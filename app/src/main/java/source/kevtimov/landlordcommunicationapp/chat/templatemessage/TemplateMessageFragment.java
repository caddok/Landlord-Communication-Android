package source.kevtimov.landlordcommunicationapp.chat.templatemessage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import source.kevtimov.landlordcommunicationapp.R;

public class TemplateMessageFragment extends Fragment implements ContractsTemplateMessage.View{


    @BindView(R.id.tv_choose_message)
    TextView mTextViewTemplateMessage;

    @BindView(R.id.lv_templ_messages)
    ListView mListViewTemplMessages;

    private ContractsTemplateMessage.Presenter mPresenter;
    private ContractsTemplateMessage.Navigator mNavigator;
    private TemplateMessageAdapter mMessageAdapter;

    @Inject
    public TemplateMessageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_template_message, container, false);
        ButterKnife.bind(this, root);
        mMessageAdapter = new TemplateMessageAdapter(getContext());
        mListViewTemplMessages.setAdapter(mMessageAdapter);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        mPresenter.loadTemplateMessages();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(ContractsTemplateMessage.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMessages(String message) {
        mMessageAdapter.add(message);
        mMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToChat(String message) {
        mNavigator.navigateBackToChat(message);
    }

    @Override
    public void setNavigator(ContractsTemplateMessage.Navigator navigator) {
        this.mNavigator = navigator;
    }

    @OnItemClick(R.id.lv_templ_messages)
    public void onMessageClick(AdapterView<?> parent, View view, int position, long id){
        String templateMessage = mMessageAdapter.getItem(position);

        mPresenter.allowNavigationToChat(templateMessage);
    }
}