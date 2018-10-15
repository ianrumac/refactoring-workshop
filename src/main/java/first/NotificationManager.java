package first;

public class NotificationManager {

    public static NotificationManager getInstance() {
        return new NotificationManager();
    }

    public void notifyUser(User user, NotificationCallback callback){
        callback.userNotified();
    }


    interface NotificationCallback {
        void userNotified();
        void errorHappened();
    }
}
