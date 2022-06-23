package Session;

import Variable.ENUMS;

public class User {

    private final int userId;
    private final ENUMS.Result errorCode;

    public User(int userId, ENUMS.Result errorCode) {
        this.userId = userId;
        this.errorCode = errorCode;
    }

    public int getUserId() {
        return userId;
    }

    public ENUMS.Result getErrorCode() {
        return errorCode;
    }
}
