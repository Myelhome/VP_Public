package Services;

import DAO.DBRequest;
import DAO.UserBin;
import Session.VacationSession;
import Session.DBSessionFactory;
import Session.User;
import Util.OperationResult;
import Util.PasswordHashing;
import Variable.ENUMS;

import javax.xml.bind.DatatypeConverter;

public class AuthService {
    public static VacationSession authorization(String login, String password){
        OperationResult result = DBRequest.getUserBinByLogin(login);
        ENUMS.Role role = ENUMS.Role.DEFAULT;
        ENUMS.Result errorCode = ENUMS.Result.INCORRECT_PASSWORD_OR_LOGIN;
        int userId = 0;

        try {
            if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
                UserBin userBin = (UserBin) result.getBody();
                byte[] password_hash = PasswordHashing.generateHash(password, DatatypeConverter.parseHexBinary(userBin.getSalt()));
                if (password_hash != null && userBin.getPassword().equals(PasswordHashing.bytesToString(password_hash)) && !userBin.getFired()) {
                        role = ENUMS.Role.valueOf(userBin.getStatus());
                        userId = userBin.getId();
                        errorCode = ENUMS.Result.NOT_AVAILABLE;
                }
            }
        }catch (IllegalArgumentException | ClassCastException e){
            e.printStackTrace();
        }

        DBSessionFactory sessionFactory = new DBSessionFactory();
        return sessionFactory.getSession(role, new User(userId, errorCode));
    }
}
