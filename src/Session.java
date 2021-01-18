class Session {
    private static User activeUser;
    private static Status status = Status.LOGOUT;
    private static boolean validDatabaseConnection = false;

    public enum Status {
        LOGIN,
        LOGOUT
    }

     static boolean isValidConnection() {
        return validDatabaseConnection;
    }

     static void setValidConnection(boolean validConnection) {
        Session.validDatabaseConnection = validConnection;
    }

     static User getActiveUser() {
        return activeUser;
    }

     static Status getStatus() {
        return status;
    }



    //Logging in user, sees if username and password match user
    //If so user is set and status is changed to Login
     static boolean login(String userName, String password, Connector connector) {
        activeUser = connector.loginUser(userName, password);
        if (activeUser != null) {
            status = Status.LOGIN;
            return true;
        } else {
            status = Status.LOGOUT;
            return false;
        }
    }

    //Logging out,
    //Sets status to Logout and user to null
     static boolean logout() {
        status = Status.LOGOUT;
        activeUser = null;
        return true;
    }


}