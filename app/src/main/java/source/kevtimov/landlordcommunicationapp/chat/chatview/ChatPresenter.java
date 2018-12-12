package source.kevtimov.landlordcommunicationapp.chat.chatview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import source.kevtimov.landlordcommunicationapp.async.base.SchedulerProvider;
import source.kevtimov.landlordcommunicationapp.models.Message;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.services.base.MessageService;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcache.BitmapCache;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.BitmapAgent;
import source.kevtimov.landlordcommunicationapp.utils.bitmapcoder.IBitmapAgent;

public class ChatPresenter implements ContractsChat.Presenter {

    private ContractsChat.View mView;
    private SchedulerProvider mSchedulerProvider;
    private MessageService mMessageService;
    private User mLoggedInUser;
    private User mOtherUser;
    private int mCurrentSession;
    private IBitmapAgent mAgent;
    private BitmapCache mCacher;


    @Inject
    ChatPresenter(SchedulerProvider schedulerProvider, MessageService messageService) {
        this.mSchedulerProvider = schedulerProvider;
        this.mMessageService = messageService;
        this.mCacher = BitmapCache.getInstance();
        this.mAgent = new BitmapAgent();
    }

    @Override
    public void subscribe(ContractsChat.View view) {
        this.mView = view;
    }

    @Override
    public void unsubscribe() {
        mView = null;
    }

    @Override
    public void setLoggedInUser(User user) {
        this.mLoggedInUser = user;
    }

    @Override
    public void setOtherUser(User user) {
        this.mOtherUser = user;
    }

    @Override
    public void setSession(int chatSessionId) {
        this.mCurrentSession = chatSessionId;
    }

    @Override
    public void getAllMessagesByChatId() {

        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Disposable observal = Observable
                        .create((ObservableOnSubscribe<List<Message>>) emitter -> {
                            List<Message> messages = mMessageService.getMessagesByChatId(mCurrentSession);
                            emitter.onNext(messages);
                            emitter.onComplete();
                        })
                        .subscribeOn(mSchedulerProvider.background())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(this::viewMessages,
                                error -> mView.showError(error));

            }

            void viewMessages(List<Message> messages) {

                if (mView == null) {
                    mTimer.cancel();
                } else {
                    if (!messages.isEmpty()) {
                        mView.showMessages(messages);
                    }
                }
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 1500);
    }


    @Override
    public void createMessage(String msgContent) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        Message message = new Message(strDate, msgContent, mLoggedInUser.getUserId(), mOtherUser.getUserId(), mCurrentSession);

        Disposable observal = Observable
                .create((ObservableOnSubscribe<Message>) emitter -> {
                    Message sendingMessage = mMessageService.createMessage(message);
                    emitter.onNext(sendingMessage);
                    emitter.onComplete();
                })
                .subscribeOn(mSchedulerProvider.background())
                .observeOn(mSchedulerProvider.ui())
                .doFinally(mView::hideLoading)
                .subscribe(r -> {},
                        error -> {
                            if (error instanceof NullPointerException) {
                                // in case of null pointer exception
                            } else {
                                mView.showError(error);
                            }
                        });
    }

    @Override
    public void allowNavigationToTemplateMessages() {
        mView.navigateToMessageTemplates();
    }

    @Override
    public Bitmap setOtherUserPicture() {
        Bitmap bitmap = mAgent.convertStringToBitmap(mOtherUser.getPicture());

        if(mCacher.getLruCache().get(mOtherUser.getUsername() + "_profile_image") == null){
            mCacher.getLruCache().put(mOtherUser.getUsername() + "_profile_image", bitmap);
        }

        return bitmap;
    }

}
