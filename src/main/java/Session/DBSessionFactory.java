package Session;

import Variable.ENUMS;

public class DBSessionFactory {

    public VacationSession getSession(ENUMS.Role role, User user){
        VacationSession vacationSession = null;
        switch (role) {
            case USER:
                vacationSession = new UserVacationSession(user);
                break;
            case ACCOUNTANT:
                vacationSession = new AccountantVacationSession(user);
                break;
            case DIRECTOR:
                vacationSession = new DirectorVacationSession(user);
                break;
            case ADMIN:
                vacationSession = new AdminVacationSession(user);
                break;
            default:
                vacationSession = new FailedVacationSession(user);
        }
        return vacationSession;
    }

}

