package source.kevtimov.landlordcommunicationapp.chat.sessions;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import source.kevtimov.landlordcommunicationapp.async.base.SchedulerProvider;
import source.kevtimov.landlordcommunicationapp.models.ChatSession;
import source.kevtimov.landlordcommunicationapp.models.Place;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.services.base.ChatSessionService;
import source.kevtimov.landlordcommunicationapp.services.base.PlaceService;
import source.kevtimov.landlordcommunicationapp.services.base.UserService;

public class ChatSessionPresenter implements ContractsChatSession.Presenter {

    private ContractsChatSession.View mView;
    private SchedulerProvider mSchedulerProvider;
    private ChatSessionService mChatSessionService;
    private User mUser;
    private PlaceService mPlaceService;
    private UserService mUserService;


    @Inject
    public ChatSessionPresenter(SchedulerProvider schedulerProvider,
                                PlaceService placeService, UserService userService, ChatSessionService chatSessionService){
        this.mSchedulerProvider = schedulerProvider;
        this.mPlaceService = placeService;
        this.mUserService = userService;
        this.mChatSessionService = chatSessionService;
    }

    @Override
    public void setUser(User user) {
        this.mUser = user;
    }

    @Override
    public void subscribe(ContractsChatSession.View view) {
        this.mView = view;
    }

    @Override
    public void unsubscribe() {
        mView = null;
    }

    @Override
    public void loadUsers() {
        mView.showLoading();
        Disposable observal = Observable
                .create((ObservableOnSubscribe<List<Place>>) emitter -> {
                    List<Place> places = mPlaceService.getAllPlacesByUserId(mUser.getUserId());
                    emitter.onNext(places);
                    emitter.onComplete();
                })
                .subscribeOn(mSchedulerProvider.background())
                .observeOn(mSchedulerProvider.ui())
                .doFinally(mView::hideLoading)
                .subscribe(this::loadOtherUsers,
                        error -> mView.showError(error));
    }

    @Override
    public void checkForChatSession(User otherUser) {
        if(mUser.isLandlord()){
            Disposable observal = Observable
                    .create((ObservableOnSubscribe<List<ChatSession>>) emitter -> {
                        List<ChatSession> chats = mChatSessionService
                                .checkIfChatSessionExistsByTenantIdAndLandlordId(otherUser.getUserId(), mUser.getUserId());
                        emitter.onNext(chats);
                        emitter.onComplete();
                    })
                    .subscribeOn(mSchedulerProvider.background())
                    .observeOn(mSchedulerProvider.ui())
                    .doFinally(mView::hideLoading)
                    .subscribe(chats -> manageChatSessionWithTenant(chats, otherUser),
                            error -> mView.showError(error));
        }else{
            Disposable observal = Observable
                    .create((ObservableOnSubscribe<List<ChatSession>>) emitter -> {
                        List<ChatSession> chats = mChatSessionService
                                .checkIfChatSessionExistsByTenantIdAndLandlordId(mUser.getUserId(), otherUser.getUserId());
                        emitter.onNext(chats);
                        emitter.onComplete();
                    })
                    .subscribeOn(mSchedulerProvider.background())
                    .observeOn(mSchedulerProvider.ui())
                    .doFinally(mView::hideLoading)
                    .subscribe(chats -> manageChatSessionWithLandlord(chats, otherUser),
                            error -> mView.showError(error));
        }
    }

    private void manageChatSessionWithLandlord(List<ChatSession> chatSessions, User landlord) {
        if(chatSessions.isEmpty()){

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);

            ChatSession  chatSession = new ChatSession(strDate, mUser.getUserId(), landlord.getUserId());

            Disposable observal = Observable
                    .create((ObservableOnSubscribe<ChatSession>) emitter -> {
                        ChatSession chat = mChatSessionService.createChat(chatSession);
                        emitter.onNext(chat);
                        emitter.onComplete();
                    })
                    .subscribeOn(mSchedulerProvider.background())
                    .observeOn(mSchedulerProvider.ui())
                    .doFinally(mView::hideLoading)
                    .subscribe(chat -> mView.navigateToMessageView(chat, landlord),
                            error -> mView.showError(error));
        }else{
            mView.navigateToMessageView(chatSessions.get(0), landlord);
        }
    }

    private void manageChatSessionWithTenant(List<ChatSession> chatSessions, User tenant) {
        if(chatSessions.isEmpty()){

            Date date = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);

            ChatSession  chatSession = new ChatSession(strDate, tenant.getUserId(), mUser.getUserId());

            Disposable observal = Observable
                    .create((ObservableOnSubscribe<ChatSession>) emitter -> {
                        ChatSession chat = mChatSessionService.createChat(chatSession);
                        emitter.onNext(chat);
                        emitter.onComplete();
                    })
                    .subscribeOn(mSchedulerProvider.background())
                    .observeOn(mSchedulerProvider.ui())
                    .doFinally(mView::hideLoading)
                    .subscribe(chat -> mView.navigateToMessageView(chat, tenant),
                            error -> mView.showError(error));
        }else{
            mView.navigateToMessageView(chatSessions.get(0), tenant);
        }
    }

    //these are incoming places for logged in user
    private void loadOtherUsers(List<Place> places) {
        if(mUser.isLandlord()){
            for (Place place:places) {
                if(place.getTenantID() != 0){
                    Disposable observal = Observable
                            .create((ObservableOnSubscribe<User>) emitter -> {
                                User user = mUserService.getUserById(place.getTenantID());
                                emitter.onNext(user);
                                emitter.onComplete();
                            })
                            .subscribeOn(mSchedulerProvider.background())
                            .observeOn(mSchedulerProvider.ui())
                            .doFinally(mView::hideLoading)
                            .subscribe(mView::addUser,
                                    error -> mView.showError(error));
                }
            }
        }else{
            for (Place place:places) {
                Disposable observal = Observable
                        .create((ObservableOnSubscribe<User>) emitter -> {
                            User user = mUserService.getUserById(place.getLandlordID());
                            emitter.onNext(user);
                            emitter.onComplete();
                        })
                        .subscribeOn(mSchedulerProvider.background())
                        .observeOn(mSchedulerProvider.ui())
                        .doFinally(mView::hideLoading)
                        .subscribe(mView::addUser,
                                error -> mView.showError(error));
            }
        }
    }
}
