package Variable;

public class ENUMS {
    public enum Role {
        USER,
        DIRECTOR,
        ACCOUNTANT,
        ADMIN,
        DEFAULT
    }

    public enum Status{
        ACCEPTED_BY_LEADER, 				//= "Утверждено руководителем";
        ACCEPTED_BY_DIRECTOR, 				//= "Утверждено ген. директором";
        ACCEPTED_BY_ACCOUNTANT, 			//= "Утверждено гл. бухгалтером";
        REFUSED_BY_LEADER, 					//= "Отклонено руководителем";
        REFUSED_BY_DIRECTOR, 				//= "Отклонено ген. директором";
        REFUSED_BY_ACCOUNTANT, 				//= "Отклонено гл. бухгалтером";
        MOVED, 								//= "Перенесено";
        IN_VACATION, 						//= "В отпуске";
        DONE,                               //= "Использован";
        CREATED                             //= "Создан";
    }

    public enum Result{
        REQUEST_ERROR,
        SUCCESS,
        LOGIN_ALREADY_EXIST,
        NO_SUCH_GROUP,
        NO_SUCH_CONNECTION,
        CONNECTION_ALREADY_EXIST,
        NO_SUCH_LEADER,
        NO_USERS,
        NO_SUCH_GROUP_NAME,
        NO_REQUESTS,
        NO_SUCH_REQUEST,
        SOMEONE_ELSE_REQUEST,
        REQUEST_ALREADY_IN_PROCESS,
        YOUR_REQUEST,
        NOT_LEADER_AND_REGULAR_USER,
        STATUS_NOT_IN_COMPETITION,
        NO_GROUPS,
        GROUP_NAME_ALREADY_EXIST,
        NOT_AVAILABLE,
        WRONG_INPUT,
        NO_SUCH_YEAR_FOR_THIS_USER,
        DIFFERENT_YEARS,
        MORE_DAYS_THAN_AVAILABLE,
        MORE_PARTS_THAN_AVAILABLE,
        USER_YEAR_CONNECTION_ALREADY_EXIST,
        INCORRECT_PASSWORD_OR_LOGIN,
        UNKNOWN_COMMAND,
        FATAL_ERROR,
        GROUP_ALREADY_HAS_NO_LEADER,
        THAT_DAY_ALREADY_PASSED,
        NOT_CURRENT_YEAR,
        START_DATE_AFTER_END_DATE,
        LESS_THEN_MINIMUM_DAYS,
        NO_SUCH_USER
    }

    public enum Operation{
        SEE_ALL_USER_REQUESTS,
        SEE_ALL_REQUESTS_FOR_MY_SUBORDINATES,
        SEE_ALL_REQUESTS_FOR_USER_SUBORDINATES,
        MAKE_REQUEST_FOR_VACATION_FOR_ME, MAKE_REQUEST_FOR_VACATION_FOR_USER, MOVE_MY_REQUEST, MOVE_USER_REQUEST, REMOVE_MY_REQUEST, REMOVE_USER_REQUEST, APPROVE_USER_REQUEST, REJECT_USER_REQUEST, SEE_ALL_REQUESTS_FOR_EVERYONE, SEE_ALL_USERS, SEE_ALL_GROUPS, ADD_USER, FIRE_USER, DELETE_USER, ADD_USER_TO_GROUP, REMOVE_USER_FROM_GROUP, ADD_GROUP_WITH_LEADER, ADD_GROUP_NO_LEADER, DELETE_GROUP, ADD_GROUP_LEADER, REMOVE_GROUP_LEADER, SEE_ALL_MY_REQUESTS
    }

    public enum Vars{
        USER_ID,
        GROUP_ID,
        REQUEST_ID,
        STATUS,
        ROLE,
        LOGIN,
        PASSWORD,
        FIRST_NAME,
        MIDDLE_NAME,
        LAST_NAME,
        EMAIL,
        USER_CREATION_DATE,
        FIRED_DATE,
        FIRED,
        REQUEST_CREATION_DATE,
        START_DATE,
        END_DATE,
        REASON,
        DECLINE_REASON,
        TRANSFER_INFO,
        NAME,
        LEADER_ID,
        YEAR,
        DAYS_TOTAL,
        PARTS_TOTAL
    }
}
