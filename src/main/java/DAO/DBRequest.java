package DAO;

import Util.OperationResult;
import Variable.ENUMS;

import java.time.LocalDate;
import java.util.Set;

public class DBRequest {

    public static OperationResult insertUser(int id, String login, String passwordHash, String salt, String firstName, String middleName, String lastName, String email, LocalDate startDate, String status) {
        if(UserBin.createUser(id, login, passwordHash, salt, firstName, middleName, lastName, email, startDate, status)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult insertRequest(int id, int userId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, ENUMS.Status status, String reason, int duration){
        if(RequestBin.createRequest(id, userId, creationDate, startDate, endDate, status, reason, duration)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getUserId(){
        Integer newUserId = UserBin.getMaxUserId();
        if(newUserId != null) {
            return new OperationResult(ENUMS.Result.SUCCESS, newUserId + 1);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getGroupId() {
        Integer newGroupId = GroupBin.getMaxGroupId();
        if(newGroupId != null) {
            return new OperationResult(ENUMS.Result.SUCCESS, newGroupId + 1);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getRequestId(){
        Integer newRequestId = RequestBin.getMaxRequestId();
        if(newRequestId != null) {
            return new OperationResult(ENUMS.Result.SUCCESS, newRequestId + 1);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getUserBinByLogin(String login){
        UserBin userBin = UserBin.findUserByLogin(login, false);
        if (userBin != null) {
            if(userBin.getLogin() != null){
                return new OperationResult(ENUMS.Result.SUCCESS, userBin);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_USER, userBin);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getUserBinById(int userId){
        UserBin userBin = UserBin.findUserById(userId, false);
        if (userBin != null) {
            if(userBin.getLogin() != null){
                return new OperationResult(ENUMS.Result.SUCCESS, userBin);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_USER, userBin);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getAllUsersBins(){
        Set<UserBin> users = UserBin.findAllUsers(false);
        if(users != null) {
            if(users.size() > 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, users);
            }
            return new OperationResult(ENUMS.Result.NO_USERS, users);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getAllUsersFromLeader(int leaderId){
        Set<UserBin> users = UserBin.findAllUserByLeader(leaderId, false);
        if (users != null) {
            if(users.size() > 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, users);
            }
            return new OperationResult(ENUMS.Result.NO_USERS, users);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult updateUser(int id, String login, String firstName, String middleName, String lastName, String email, LocalDate startDate, LocalDate fireDate, String status, boolean fired){
        if(UserBin.updateUser(id, login, firstName, middleName, lastName, email, startDate, fireDate, status, fired)) return new OperationResult(ENUMS.Result.SUCCESS, null);
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getGroupBinById(int groupId) {
        GroupBin group = GroupBin.findGroupById(groupId);
        if (group != null) {
            if(group.getName() != null) {
                return new OperationResult(ENUMS.Result.SUCCESS, group);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_GROUP, group);
        }

        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getGroupBinByName(String name) {
        GroupBin group = GroupBin.findGroupByName(name);
        if (group != null) {
            if(group.getName() != null) {
                return new OperationResult(ENUMS.Result.SUCCESS, group);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_GROUP_NAME, group);
        }

        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getGroupByLeader(int leaderId){
        GroupBin group = GroupBin.findGroupByLeaderId(leaderId);
        if (group != null) {
            if(group.getName() != null) {
                return new OperationResult(ENUMS.Result.SUCCESS, group);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_LEADER, group);
        }

        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult insertGroup(int id, String name, Integer leaderId){
        if(GroupBin.createGroup(id, name, leaderId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getUserGroupsBin(int userId, int groupId){
        UserGroupsBin userGroup = UserGroupsBin.findByUserIdGroupId(userId, groupId);
        if (userGroup != null) {
            if(userGroup.getUserId() != 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, userGroup);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_CONNECTION, userGroup);
        }

        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult createUserGroupsConnection(int userId, int groupId) {
        if(UserGroupsBin.createUserGroups(userId, groupId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult removeUserGroupsConnection(int userId, int groupId){
        if(UserGroupsBin.deleteUserGroups(userId, groupId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult removeUserGroupsConnectionForGroup(int groupId){
        if(UserGroupsBin.deleteUserGroupsByGroupId(groupId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult removeUserGroupsConnectionForUser(int userId){
        if(UserGroupsBin.deleteUserGroupsByUserId(userId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getAllRequestBinsForUser(int userId){
        Set<RequestBin> requests = RequestBin.findRequestsByUserId(userId);
        if(requests != null) {
            if(requests.size() > 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, requests);
            }
            return new OperationResult(ENUMS.Result.NO_REQUESTS, requests);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getRequest(int id){
        RequestBin request = RequestBin.findRequestById(id);
        if (request != null) {
            if(request.getStatus() != null) {
                return new OperationResult(ENUMS.Result.SUCCESS, request);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_REQUEST, request);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult updateRequest(int id, int userId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, String status, String reason, String declineReason,String transferInfo, int duration){
        if(RequestBin.updateRequest(id, userId, creationDate, startDate, endDate, status, reason, declineReason, transferInfo, duration)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult deleteRequest(int id){
        if(RequestBin.deleteRequest(id)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult deleteRequestForUser(int userId){
        if(RequestBin.deleteRequestByUserId(userId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getAllRequestBins(){
        Set<RequestBin> requests = RequestBin.findAllRequests();
        if(requests != null) {
            if(requests.size() > 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, requests);
            }
            return new OperationResult(ENUMS.Result.NO_REQUESTS, requests);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getAllGroupsBins(){
        Set<GroupBin> groups = GroupBin.findAllGroups();
        if(groups != null) {
            if(groups.size() > 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, groups);
            }
            return new OperationResult(ENUMS.Result.NO_GROUPS, groups);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult deleteGroup(int id){
        if(GroupBin.deleteGroup(id)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult updateGroup(int id, String name, Integer leaderId){
        if(GroupBin.updateGroup(id, name, leaderId)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getAllGroupsBinsForUser(int userId){
        Set<GroupBin> groups = GroupBin.findAllGroupsForUser(userId);
        if(groups != null) {
            if(groups.size() > 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, groups);
            }
            return new OperationResult(ENUMS.Result.NO_GROUPS, groups);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult deleteUser(int id) {
        if(UserBin.deleteUser(id)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult getUserDaysBin(int userId, int year){
        UserDaysBin userDays = UserDaysBin.findUserDaysByUserIdYear(userId, year);
        if (userDays != null) {
            if(userDays.getUserId() != 0) {
                return new OperationResult(ENUMS.Result.SUCCESS, userDays);
            }
            return new OperationResult(ENUMS.Result.NO_SUCH_YEAR_FOR_THIS_USER, userDays);
        }

        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult createUserDaysConnection(int userId, int year, int daysTotal, int partsTotal) {
        if(UserDaysBin.createUserDays(userId, year, daysTotal, partsTotal)){
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult removeUserDaysConnection(int userId, int year) {
        if (UserDaysBin.deleteUserDays(userId, year)) {
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult removeUserDaysConnectionForUser(int userId) {
        if (UserDaysBin.deleteUserDaysByUserId(userId)) {
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }

    public static OperationResult updateUserDaysConnection(int userId, int year, int daysTotal, int partsTotal) {
        if (UserDaysBin.updateUserDays(userId, year, daysTotal, partsTotal)) {
            return new OperationResult(ENUMS.Result.SUCCESS, null);
        }
        return new OperationResult(ENUMS.Result.REQUEST_ERROR, null);
    }
}
