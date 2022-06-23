package Session;

import Services.VacationService;
import Util.OperationResult;
import Variable.ENUMS;

import java.time.LocalDate;

public class AdminVacationSession extends VacationSession {
    public AdminVacationSession(User user) {
        super(user);
    }

    @Override
    public OperationResult showAllRequestsForUserSession() {
        return VacationService.showAllRequestsForUser(user.getUserId());
    }

    @Override
    public OperationResult showAllRequestsForUserSession(int userId) {
        return VacationService.showAllRequestsForUser(userId);
    }

    @Override
    public OperationResult showAllRequestsForLeaderSession() {
        return VacationService.showAllRequestsForLeader(user.getUserId());
    }

    @Override
    public OperationResult showAllRequestsForLeaderSession(int leaderId) {
        return VacationService.showAllRequestsForLeader(leaderId);
    }

    @Override
    public OperationResult makeRequestForVacationSession(LocalDate startDate, LocalDate endDate, String reason) {
        return VacationService.makeRequestForVacation(user.getUserId(), startDate, endDate, ENUMS.Status.CREATED, reason);
    }

    @Override
    public OperationResult makeRequestForVacationSession(int userId, LocalDate startDate, LocalDate endDate, String reason) {
        return VacationService.makeRequestForVacation(userId, startDate, endDate, ENUMS.Status.CREATED, reason);
    }

    @Override
    public OperationResult changeRequestDatesSession(int requestId, LocalDate startDate, LocalDate endDate, String transferInfo) {
        return VacationService.changeRequestDates(user.getUserId(), requestId, startDate, endDate, transferInfo);
    }

    @Override
    public OperationResult changeRequestDatesSession(int userId, int requestId, LocalDate startDate, LocalDate endDate, String transferInfo) {
        return VacationService.changeRequestDates(userId, requestId, startDate, endDate, transferInfo);
    }

    @Override
    public OperationResult removeRequestSession(int requestId) {
        return VacationService.removeRequest(user.getUserId(), requestId);
    }

    @Override
    public OperationResult removeRequestSession(int userId, int requestId) {
        return VacationService.removeRequest(userId, requestId);
    }

    @Override
    public OperationResult approveRequestSession(int requestId) {
        return VacationService.approveRejectRequest(user.getUserId(), requestId, false, null);
    }

    @Override
    public OperationResult rejectRequestSession(int requestId, String declineReason) {
        return VacationService.approveRejectRequest(user.getUserId(), requestId, true, declineReason);
    }

    @Override
    public OperationResult showAllRequestsForAllUsersSession() {
        return VacationService.showAllRequestsForAllUsers(user.getUserId());
    }

    @Override
    public OperationResult showAllUsersSession() {
        return VacationService.showAllUsers();
    }

    @Override
    public OperationResult showAllGroupsSession() {
        return VacationService.showAllGroups();
    }

    @Override
    public OperationResult addUserSession(String login, String password, String firstName, String middleName, String lastName, String email, String status) {
        return VacationService.addUser(login, password, firstName, middleName, lastName, email, status);
    }

    @Override
    public OperationResult fireUserSession(int userId) {
        return VacationService.fireUser(userId);
    }

    @Override
    public OperationResult removeUserSession(int userId) {
        return VacationService.removeUser(userId);
    }

    @Override
    public OperationResult addUserToGroupSession(int userId, int groupId) {
        return VacationService.addUserToGroup(userId, groupId);
    }

    @Override
    public OperationResult removeUserFromGroupSession(int userId, int groupId) {
        return VacationService.removeUserFromGroup(userId, groupId);
    }

    @Override
    public OperationResult addGroupWithLeaderSession(String name, int leaderId) {
        return VacationService.addGroup(name, leaderId);
    }

    @Override
    public OperationResult addGroupNoLeaderSession(String name) {
        return VacationService.addGroup(name, null);
    }

    @Override
    public OperationResult removeGroupSession(int groupId) {
        return VacationService.removeGroup(groupId);
    }

    @Override
    public OperationResult setGroupLeaderSession(int groupId, int leaderId) {
        return VacationService.setGroupLeader(groupId, leaderId);
    }

    @Override
    public OperationResult removeGroupLeaderSession(int groupId) {
        return VacationService.removeGroupLeader(groupId);
    }

    @Override
    public OperationResult addDaysForUserSession(int userId, int year, int daysTotal, int partsTotal) {
        return VacationService.addDaysForUser(userId, year, daysTotal, partsTotal);
    }

    @Override
    public OperationResult removeDaysForUserSession(int userId, int year) {
        return VacationService.removeDaysForUser(userId, year);
    }

    @Override
    public OperationResult updateDaysForUserSession(int userId, int year, Integer daysTotal, Integer partsTotal) {
        return VacationService.updateDaysForUser(userId, year, daysTotal, partsTotal);
    }

    @Override
    public OperationResult updateRequestSession(int userId, int requestId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, ENUMS.Status status, String reason, String declineReason, String transferInfo) {
        return VacationService.updateRequest(userId, requestId, creationDate, startDate, endDate, status, reason, declineReason, transferInfo);
    }

    @Override
    public OperationResult updateUserSession(int userId, String login, String firstName, String middleName, String lastName, String email, LocalDate startDate, LocalDate fireDate, String status, Boolean fired) {
        return VacationService.updateUser(userId, login, firstName, middleName, lastName, email, startDate, fireDate, status, fired);
    }

    @Override
    public OperationResult updateGroupSession(int groupId, String name, Integer leaderId){
        return VacationService.updateGroup(groupId, name, leaderId);
    }
}
