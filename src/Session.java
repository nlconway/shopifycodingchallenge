public class Session {
    private static User user;
    private static Status status = Status.LOGOUT;

    public Session(String userName, String password){
    }

    public static User getUser() {
        return user;
    }

    public static Status getStatus() {
        return status;
    }

    public enum Status{
        LOGIN,
        LOGOUT
    }

    public static boolean login(String userName, String password, Connector connector){
        user = connector.loginUser(userName, password);
        if(user!= null){
            status = Status.LOGIN;
            return true;
        }
        else{
            status = Status.LOGOUT;
            return false;
        }
    }

    public static boolean logout(){
        status = Status.LOGOUT;
        user = null;
        return true;
    }

    public int getUserId(){
        return user.getId();
    }


}