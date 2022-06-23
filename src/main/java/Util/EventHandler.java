package Util;

import Session.VacationSession;
import Variable.ENUMS;

import java.time.LocalDate;
import java.util.Map;

public class EventHandler {

    public static OperationResult handleSession(VacationSession vacationSession, ENUMS.Operation operation, Map<ENUMS.Vars, Object> vars) {
        if (vacationSession == null) {
            return new OperationResult(ENUMS.Result.INCORRECT_PASSWORD_OR_LOGIN, null);
        }

        OperationResult result = new OperationResult(ENUMS.Result.WRONG_INPUT, null);

        try {
            Integer userId = (Integer) vars.get(ENUMS.Vars.USER_ID);
            Integer groupId = (Integer) vars.get(ENUMS.Vars.GROUP_ID);
            Integer requestId = (Integer) vars.get(ENUMS.Vars.REQUEST_ID);
            ENUMS.Status status = (ENUMS.Status) vars.get(ENUMS.Vars.STATUS);
            ENUMS.Role role = (ENUMS.Role) vars.get(ENUMS.Vars.ROLE);
            String login = (String) vars.get(ENUMS.Vars.LOGIN);
            String password = (String) vars.get(ENUMS.Vars.PASSWORD);
            String firstName = (String) vars.get(ENUMS.Vars.FIRST_NAME);
            String middleName = (String) vars.get(ENUMS.Vars.MIDDLE_NAME);
            String lastName = (String) vars.get(ENUMS.Vars.LAST_NAME);
            String email = (String) vars.get(ENUMS.Vars.EMAIL);
            LocalDate userCreationDate = (LocalDate) vars.get(ENUMS.Vars.USER_CREATION_DATE);
            LocalDate firedDate = (LocalDate) vars.get(ENUMS.Vars.FIRED_DATE);
            Boolean fired = (Boolean) vars.get(ENUMS.Vars.FIRED);
            LocalDate requestCreationDate = (LocalDate) vars.get(ENUMS.Vars.REQUEST_CREATION_DATE);
            LocalDate startDate = (LocalDate) vars.get(ENUMS.Vars.START_DATE);
            LocalDate endDate = (LocalDate) vars.get(ENUMS.Vars.END_DATE);
            String reason = (String) vars.get(ENUMS.Vars.REASON);
            String declineReason = (String) vars.get(ENUMS.Vars.DECLINE_REASON);
            String transferInfo = (String) vars.get(ENUMS.Vars.TRANSFER_INFO);
            String name = (String) vars.get(ENUMS.Vars.NAME);
            Integer leaderId = (Integer) vars.get(ENUMS.Vars.LEADER_ID);
            Integer year = (Integer) vars.get(ENUMS.Vars.YEAR);
            Integer daysTotal = (Integer) vars.get(ENUMS.Vars.DAYS_TOTAL);
            Integer partsTotal = (Integer) vars.get(ENUMS.Vars.PARTS_TOTAL);

            switch (operation) {
                case SEE_ALL_MY_REQUESTS:
                    result = vacationSession.showAllRequestsForUserSession();
                    break;
                case SEE_ALL_USER_REQUESTS:
                    if (userId != null) result = vacationSession.showAllRequestsForUserSession(userId);
                    break;
                case SEE_ALL_REQUESTS_FOR_MY_SUBORDINATES:
                    result = vacationSession.showAllRequestsForLeaderSession();
                    break;
                case SEE_ALL_REQUESTS_FOR_USER_SUBORDINATES:
                    if (userId != null) result = vacationSession.showAllRequestsForLeaderSession(userId);
                    break;
                case MAKE_REQUEST_FOR_VACATION_FOR_ME:
                    if (startDate != null && endDate != null && reason != null)
                        result = vacationSession.makeRequestForVacationSession(startDate, endDate, reason);
                    break;
                case MAKE_REQUEST_FOR_VACATION_FOR_USER:
                    if (userId != null && startDate != null && endDate != null && reason != null)
                        result = vacationSession.makeRequestForVacationSession(userId, startDate, endDate, reason);
                    break;
                case MOVE_MY_REQUEST:
                    if (requestId != null && startDate != null && endDate != null && transferInfo != null)
                        result = vacationSession.changeRequestDatesSession(requestId, startDate, endDate, transferInfo);
                    break;
                case MOVE_USER_REQUEST:
                    if (userId != null && requestId != null && startDate != null && endDate != null && transferInfo != null)
                        result = vacationSession.changeRequestDatesSession(userId, requestId, startDate, endDate, transferInfo);
                    break;
                case REMOVE_MY_REQUEST:
                    if (requestId != null) result = vacationSession.removeRequestSession(requestId);
                    break;
                case REMOVE_USER_REQUEST:
                    if (userId != null && requestId != null)
                        result = vacationSession.removeRequestSession(userId, requestId);
                    break;
                case APPROVE_USER_REQUEST:
                    if (requestId != null) result = vacationSession.approveRequestSession(requestId);
                    break;
                case REJECT_USER_REQUEST:
                    if (requestId != null && declineReason != null)
                        result = vacationSession.rejectRequestSession(requestId, declineReason);
                    break;
                case SEE_ALL_REQUESTS_FOR_EVERYONE:
                    result = vacationSession.showAllRequestsForAllUsersSession();
                    break;
                case SEE_ALL_USERS:
                    result = vacationSession.showAllUsersSession();
                    break;
                case SEE_ALL_GROUPS:
                    result = vacationSession.showAllGroupsSession();
                    break;
                case ADD_USER:
                    if (login != null && password != null && firstName != null && middleName != null && lastName != null && email != null && role != null)
                        result = vacationSession.addUserSession(login, password, firstName, middleName, lastName, email, status.toString());
                    break;
                case FIRE_USER:
                    if (userId != null) result = vacationSession.fireUserSession(userId);
                    break;
                case DELETE_USER:
                    if (userId != null) result = vacationSession.removeUserSession(userId);
                case ADD_USER_TO_GROUP:
                    if (userId != null && groupId != null)
                        result = vacationSession.addUserToGroupSession(userId, groupId);
                    break;
                case REMOVE_USER_FROM_GROUP:
                    if (userId != null && groupId != null)
                        result = vacationSession.removeUserFromGroupSession(userId, groupId);
                    break;
                case ADD_GROUP_WITH_LEADER:
                    if (name != null && leaderId != null)
                        result = vacationSession.addGroupWithLeaderSession(name, leaderId);
                    break;
                case ADD_GROUP_NO_LEADER:
                    if (name != null) result = vacationSession.addGroupNoLeaderSession(name);
                    break;
                case DELETE_GROUP:
                    if (groupId != null) result = vacationSession.removeGroupSession(groupId);
                    break;
                case ADD_GROUP_LEADER:
                    if (groupId != null && leaderId != null)
                        result = vacationSession.setGroupLeaderSession(groupId, leaderId);
                    break;
                case REMOVE_GROUP_LEADER:
                    if (groupId != null) result = vacationSession.removeGroupLeaderSession(groupId);
                    break;
                default:
                    result = new OperationResult(ENUMS.Result.UNKNOWN_COMMAND, null);
                    break;
            }
        } catch (ClassCastException e){
            e.printStackTrace();
        }

        return result;
    }
}
