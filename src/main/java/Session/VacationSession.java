package Session;

import Services.VacationService;
import Util.OperationResult;
import Variable.ENUMS;

import java.time.LocalDate;

public abstract class VacationSession {
    public final User user;

    public VacationSession(User user) {
        this.user = user;
    }


    public OperationResult showAllRequestsForUserSession() {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult showAllRequestsForUserSession(int userId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult showAllRequestsForLeaderSession() {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult showAllRequestsForLeaderSession(int leaderId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult makeRequestForVacationSession(LocalDate startDate, LocalDate endDate, String reason) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult makeRequestForVacationSession(int userId, LocalDate startDate, LocalDate endDate, String reason) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult changeRequestDatesSession(int requestId, LocalDate startDate, LocalDate endDate, String transferInfo) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult changeRequestDatesSession(int userId, int requestId, LocalDate startDate, LocalDate endDate, String transferInfo) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeRequestSession(int requestId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeRequestSession(int userId, int requestId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult approveRequestSession(int requestId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult rejectRequestSession(int requestId, String declineReason) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult showAllRequestsForAllUsersSession() {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult showAllUsersSession() {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult showAllGroupsSession() {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult addUserSession(String login, String password, String firstName, String middleName, String lastName, String email, String status) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult fireUserSession(int userId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeUserSession(int userId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult addUserToGroupSession(int userId, int groupId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeUserFromGroupSession(int userId, int groupId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult addGroupWithLeaderSession(String name, int leaderId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult addGroupNoLeaderSession(String name) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeGroupSession(int groupId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult setGroupLeaderSession(int groupId, int leaderId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeGroupLeaderSession(int groupId) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult addDaysForUserSession(int userId, int year, int daysTotal, int partsTotal) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult removeDaysForUserSession(int userId, int year) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult updateDaysForUserSession(int userId, int year, Integer daysTotal, Integer partsTotal) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult updateRequestSession(int userId, int requestId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, ENUMS.Status status, String reason, String declineReason, String transferInfo) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult updateUserSession(int userId, String login, String firstName, String middleName, String lastName, String email, LocalDate startDate, LocalDate fireDate, String status, Boolean fired) {
        return new OperationResult(user.getErrorCode(), null);
    }

    public OperationResult updateGroupSession(int groupId, String name, Integer leaderId){
        return new OperationResult(user.getErrorCode(), null);
    }
}