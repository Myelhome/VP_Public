import Services.AuthService;
import Session.VacationSession;
import Util.*;

import java.sql.SQLException;

public class Launch {

    public static void main(String[] args) throws SQLException {
        VacationSession session = AuthService.authorization("admin", "admin");

        OperationResult result = session.addUserSession("TEST_USER", "test", "first", "middle", "last", "rgty2000@mail.ru", "USER");

        System.out.println(JsonParser.getJSONString(result));
        System.out.println(JsonParser.parseJSON(result));
    }
}