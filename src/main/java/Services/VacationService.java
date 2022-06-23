package Services;

import DAO.*;
import Util.MailSender;
import Util.OperationResult;
import Util.PasswordHashing;
import Util.PropertiesSettingsUtil;
import Variable.ENUMS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class VacationService {

    private static final String CURRENT_YEAR_KEY = "vacation.current_year";

    public static OperationResult showAllRequestsForUser(int userId) {
        OperationResult result = DBRequest.getAllRequestBinsForUser(userId);
        if (result.getResult().equals(ENUMS.Result.REQUEST_ERROR)) return result;
        Set<RequestBin> requests = (Set<RequestBin>) result.getBody();
        return DataService.getYearsWithRequests(userId, requests);
    }

    public static OperationResult showAllRequestsForUserOld(int userId) {
        return DBRequest.getAllRequestBinsForUser(userId);
    }

    public static OperationResult showAllRequestsForLeader(int leaderId) {
        OperationResult result = DBRequest.getGroupByLeader(leaderId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;

        result = DBRequest.getAllUsersFromLeader(leaderId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;

        Set<RequestBin> requests = new HashSet<>();

        for (UserBin user : (Set<UserBin>) result.getBody()) {
            OperationResult userResult = DBRequest.getAllRequestBinsForUser(user.getId());
            if (userResult.getResult().equals(ENUMS.Result.REQUEST_ERROR)) return result;
            if (userResult.getResult().equals(ENUMS.Result.SUCCESS)) {
                requests.addAll((Set<RequestBin>) userResult.getBody());
            }
        }

        return DataService.getYearsWithRequests(leaderId, requests);
    }

    public static OperationResult showAllRequestsForLeaderOld(int leaderId) {
        OperationResult result = DBRequest.getGroupByLeader(leaderId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.getAllUsersFromLeader(leaderId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        Set<RequestBin> requests = new HashSet<>();
        for (UserBin user : (Set<UserBin>) result.getBody()) {
            OperationResult userResult = DBRequest.getAllRequestBinsForUser(user.getId());
            if (userResult.getResult().equals(ENUMS.Result.SUCCESS)) {
                requests.addAll((Set<RequestBin>) userResult.getBody());
            }
        }
        return new OperationResult(ENUMS.Result.SUCCESS, requests);
    }

    public static OperationResult makeRequestForVacation(int userId, LocalDate startDate, LocalDate endDate, ENUMS.Status status, String reason) {
        OperationResult result = DBRequest.getRequestId();
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        int requestId = (int) result.getBody();

        if (startDate.getYear() != endDate.getYear()) return new OperationResult(ENUMS.Result.DIFFERENT_YEARS, null);

        if (startDate.getYear() != PropertiesSettingsUtil.getInt(CURRENT_YEAR_KEY)) return new OperationResult(ENUMS.Result.NOT_CURRENT_YEAR, null);

        result = DataService.getAvailableStatsInYear(userId, startDate.getYear());
        UserDaysBin userDaysBin = (UserDaysBin) result.getBody();

        result = DataService.countDaysBetween(startDate, endDate);
        if (result.getResult() != ENUMS.Result.SUCCESS) return result;
        int duration = (int) result.getBody();

        if (userDaysBin.getDaysAvailable() - duration < 0)
            return new OperationResult(ENUMS.Result.MORE_DAYS_THAN_AVAILABLE, null);
        if (userDaysBin.getPartsAvailable() - 1 < 0)
            return new OperationResult(ENUMS.Result.MORE_PARTS_THAN_AVAILABLE, null);

        LocalDate creationDate = LocalDate.now();

        return DBRequest.insertRequest(requestId, userId, creationDate, startDate, endDate, status, reason, duration);
    }

    public static OperationResult changeRequestDates(int userId, int requestId, LocalDate startDate, LocalDate endDate, String transferInfo) {
        OperationResult result = DBRequest.getRequest(requestId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        RequestBin request = (RequestBin) result.getBody();

        if (request.getUserId() != userId) return new OperationResult(ENUMS.Result.SOMEONE_ELSE_REQUEST, null);

        if (!(request.getStatus().equals(ENUMS.Status.ACCEPTED_BY_ACCOUNTANT.toString())
                || request.getStatus().equals(ENUMS.Status.ACCEPTED_BY_LEADER.toString())
                || request.getStatus().equals(ENUMS.Status.ACCEPTED_BY_DIRECTOR.toString())))
            return new OperationResult(ENUMS.Result.REQUEST_ALREADY_IN_PROCESS, null);

        if (startDate.getYear() != endDate.getYear()) return new OperationResult(ENUMS.Result.DIFFERENT_YEARS, null);

        if (startDate.getYear() != PropertiesSettingsUtil.getInt(CURRENT_YEAR_KEY)) return new OperationResult(ENUMS.Result.NOT_CURRENT_YEAR, null);

        result = DataService.getAvailableStatsInYear(userId, startDate.getYear());
        UserDaysBin userDaysBin = (UserDaysBin) result.getBody();

        result = DataService.countDaysBetween(startDate, endDate);
        if (result.getResult() != ENUMS.Result.SUCCESS) return result;
        int duration = (int) result.getBody();

        if (userDaysBin.getDaysAvailable() - duration + request.getDuration() < 0)
            return new OperationResult(ENUMS.Result.MORE_DAYS_THAN_AVAILABLE, null);

        return DBRequest.updateRequest(requestId, userId, request.getCreationDate(), startDate, endDate, ENUMS.Status.MOVED.toString(), request.getReason(), request.getDeclineReason(), transferInfo, duration);
    }

    public static OperationResult removeRequest(int userId, int requestId) {
        OperationResult result = DBRequest.getRequest(requestId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;

        RequestBin request = (RequestBin) result.getBody();
        if (request.getUserId() != userId) return new OperationResult(ENUMS.Result.SOMEONE_ELSE_REQUEST, null);

        if (!(request.getStatus().equals(ENUMS.Status.CREATED.toString())
                || request.getStatus().equals(ENUMS.Status.MOVED.toString())))
            return new OperationResult(ENUMS.Result.REQUEST_ALREADY_IN_PROCESS, null);

        return DBRequest.deleteRequest(requestId);
    }

    public static OperationResult approveRejectRequest(int userId, int requestId, boolean reject, String declineReason) {
        OperationResult result = DBRequest.getRequest(requestId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        RequestBin request = (RequestBin) result.getBody();

        String trueDeclineReason;
        if (declineReason == null) trueDeclineReason = request.getDeclineReason();
        else trueDeclineReason = declineReason;

        result = DBRequest.getUserBinById(userId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        UserBin user = (UserBin) result.getBody();

        if (!user.getStatus().equals(ENUMS.Role.USER.toString())) {
            OperationResult statusChangedResult = changeStatus(user.getStatus(), request.getStatus(), reject);
            if (!statusChangedResult.getResult().equals(ENUMS.Result.SUCCESS)) return result;
            ENUMS.Status statusChanged = (ENUMS.Status) statusChangedResult.getBody();

            result = DBRequest.getUserBinById(userId);
            if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
                UserBin userBin = (UserBin) result.getBody();
                if (statusChanged == ENUMS.Status.ACCEPTED_BY_DIRECTOR)
                    MailSender.approvalMail(userBin.getEmail(), request.getStartDate().toString(), request.getEndDate().toString());
                if (statusChanged == ENUMS.Status.REFUSED_BY_ACCOUNTANT || statusChanged == ENUMS.Status.REFUSED_BY_DIRECTOR)
                    MailSender.declineMail(userBin.getEmail(), request.getStartDate().toString(), request.getEndDate().toString(), trueDeclineReason);
            }

            return DBRequest.updateRequest(requestId, request.getUserId(), request.getCreationDate(), request.getStartDate(), request.getEndDate(), statusChanged.toString(), request.getReason(), trueDeclineReason, request.getTransferInfo(), request.getDuration());
        }

        result = showAllRequestsForLeader(userId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        Set<RequestBin> leaderRequests = (Set<RequestBin>) result.getBody();

        for (RequestBin requestBin : leaderRequests) {
            if (requestBin.getId() == requestId) {
                if (requestBin.getUserId() != userId) {
                    OperationResult statusChangedResult = changeStatus(user.getStatus(), requestBin.getStatus(), reject);
                    if (!statusChangedResult.getResult().equals(ENUMS.Result.SUCCESS)) return result;
                    ENUMS.Status statusChanged = (ENUMS.Status) statusChangedResult.getBody();

                    result = DBRequest.getUserBinById(userId);
                    if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
                        UserBin userBin = (UserBin) result.getBody();
                        if (statusChanged == ENUMS.Status.REFUSED_BY_LEADER)
                            MailSender.declineMail(userBin.getEmail(), request.getStartDate().toString(), request.getEndDate().toString(), trueDeclineReason);
                    }

                    return DBRequest.updateRequest(requestId, requestBin.getUserId(), requestBin.getCreationDate(), requestBin.getStartDate(), requestBin.getEndDate(), statusChanged.toString(), requestBin.getReason(), trueDeclineReason, requestBin.getTransferInfo(), requestBin.getDuration());
                }
                return new OperationResult(ENUMS.Result.YOUR_REQUEST, null);
            }
        }

        return new OperationResult(ENUMS.Result.NOT_LEADER_AND_REGULAR_USER, null);
    }

    public static OperationResult changeStatus(String role, String status, boolean reject) {
        if (role.equals(ENUMS.Role.ACCOUNTANT.toString())) {
            if (status.equals(ENUMS.Status.ACCEPTED_BY_LEADER.toString())) {
                if (!reject) {
                    return new OperationResult(ENUMS.Result.SUCCESS, ENUMS.Status.ACCEPTED_BY_ACCOUNTANT);
                }
                return new OperationResult(ENUMS.Result.SUCCESS, ENUMS.Status.REFUSED_BY_ACCOUNTANT);
            }
        }
        if (role.equals(ENUMS.Role.DIRECTOR.toString())) {
            if (status.equals(ENUMS.Status.ACCEPTED_BY_ACCOUNTANT.toString())) {
                if (!reject) {
                    return new OperationResult(ENUMS.Result.SUCCESS, ENUMS.Status.ACCEPTED_BY_DIRECTOR);
                }
                return new OperationResult(ENUMS.Result.SUCCESS, ENUMS.Status.REFUSED_BY_DIRECTOR);
            }
        }
        if (role.equals(ENUMS.Role.USER.toString())) {
            if (status.equals(ENUMS.Status.CREATED.toString()) || status.equals(ENUMS.Status.MOVED.toString())) {
                if (!reject) {
                    return new OperationResult(ENUMS.Result.SUCCESS, ENUMS.Status.ACCEPTED_BY_LEADER);
                }
                return new OperationResult(ENUMS.Result.SUCCESS, ENUMS.Status.REFUSED_BY_LEADER);
            }
        }
        return new OperationResult(ENUMS.Result.STATUS_NOT_IN_COMPETITION, null);
    }

    public static OperationResult showAllRequestsForAllUsers(int userId) {
        OperationResult result = DBRequest.getAllRequestBins();
        if (result.getResult().equals(ENUMS.Result.REQUEST_ERROR)) return result;
        Set<RequestBin> requests = (Set<RequestBin>) result.getBody();
        return DataService.getYearsWithRequests(userId, requests);
    }

    public static OperationResult showAllRequestsForAllUsersOld() {
        return DBRequest.getAllRequestBins();
    }

    public static OperationResult showAllUsers() {
        OperationResult result = DBRequest.getAllUsersBins();
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        Set<UserBin> users = (Set<UserBin>) result.getBody();
        for (UserBin user : users) {
            ArrayList<String> groups = new ArrayList<>();

            OperationResult groupsResult = DBRequest.getAllGroupsBinsForUser(user.getId());
            if (groupsResult.getResult().equals(ENUMS.Result.SUCCESS)) {
                Set<GroupBin> groupBins = (Set<GroupBin>) groupsResult.getBody();
                for (GroupBin groupBin : groupBins) {
                    groups.add(groupBin.getName());
                }
            }

            String[] groupsArray = new String[groups.size()];
            user.setGroups(groups.toArray(groupsArray));
        }
        return new OperationResult(ENUMS.Result.SUCCESS, users);
    }

    public static OperationResult showAllGroups() {
        return DBRequest.getAllGroupsBins();
    }

    public static OperationResult updateRequest(int userId, int requestId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, ENUMS.Status status, String reason, String declineReason, String transferInfo) {
        OperationResult result = DBRequest.getUserBinById(userId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.getRequest(requestId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        RequestBin request = (RequestBin) result.getBody();

        if (startDate == null) startDate = request.getStartDate();
        if (endDate == null) endDate = request.getEndDate();

        if (startDate.getYear() != endDate.getYear()) return new OperationResult(ENUMS.Result.DIFFERENT_YEARS, null);

        result = DataService.countDaysBetween(startDate, endDate);
        if (result.getResult() != ENUMS.Result.SUCCESS) return result;
        int duration = (int) result.getBody();

        return DBRequest.updateRequest(requestId, userId,
                creationDate == null ? request.getCreationDate() : creationDate,
                startDate, endDate,
                status == null ? request.getStatus() : status.toString(),
                reason == null ? request.getReason() : reason,
                declineReason == null ? request.getDeclineReason() : declineReason,
                transferInfo == null ? request.getTransferInfo() : transferInfo,
                duration);
    }

    public static OperationResult addUser(String login, String password, String firstName, String middleName, String lastName, String email, String status) {
        OperationResult result = DBRequest.getUserId();
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        int userId = (int) result.getBody();

        result = DBRequest.getUserBinByLogin(login);
        if (!result.getResult().equals(ENUMS.Result.NO_SUCH_USER)) {
            if (result.getResult().equals(ENUMS.Result.SUCCESS))
                return new OperationResult(ENUMS.Result.LOGIN_ALREADY_EXIST, null);
            return result;
        }

        LocalDate startDate = LocalDate.now();

        byte[] salt = PasswordHashing.generateSalt();
        byte[] passwordHsh = PasswordHashing.generateHash(password, salt);
        if (passwordHsh == null) return new OperationResult(ENUMS.Result.FATAL_ERROR, null);

        return DBRequest.insertUser(userId, login, PasswordHashing.bytesToString(passwordHsh),
                PasswordHashing.bytesToString(salt), firstName, middleName, lastName, email, startDate, status);
    }

    public static OperationResult fireUser(int id) {
        OperationResult result = DBRequest.getUserBinById(id);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        UserBin user = (UserBin) result.getBody();

        return DBRequest.updateUser(id, user.getLogin(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getStartDate(), LocalDate.now(), user.getStatus(), true);
    }

    public static OperationResult removeUser(int id) {
        OperationResult result = DBRequest.getUserBinById(id);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.deleteUser(id);
        if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
            DBRequest.removeUserGroupsConnectionForUser(id);

            OperationResult operationResult = DBRequest.getGroupByLeader(id);
            if (operationResult.getResult().equals(ENUMS.Result.SUCCESS)) {
                GroupBin groupBin = (GroupBin) operationResult.getBody();
                DBRequest.updateGroup(groupBin.getId(), groupBin.getName(), null);
            }

            DBRequest.removeUserDaysConnectionForUser(id);

            DBRequest.deleteRequestForUser(id);
        }
        return result;
    }

    public static OperationResult updateUser(int userId, String login, String firstName, String middleName, String lastName, String email, LocalDate startDate, LocalDate fireDate, String status, Boolean fired) {
        OperationResult result = DBRequest.getUserBinById(userId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        UserBin userBin = (UserBin) result.getBody();

        if (login != null) {
            result = DBRequest.getUserBinByLogin(login);
            if (!result.getResult().equals(ENUMS.Result.NO_SUCH_USER)) {
                if (result.getResult().equals(ENUMS.Result.SUCCESS))
                    return new OperationResult(ENUMS.Result.LOGIN_ALREADY_EXIST, null);
                return result;
            }
        }

        return DBRequest.updateUser(userId,
                login == null ? userBin.getLogin() : login,
                firstName == null ? userBin.getFirstName() : firstName,
                middleName == null ? userBin.getMiddleName() : middleName,
                lastName == null ? userBin.getLastName() : lastName,
                email == null ? userBin.getEmail() : email,
                startDate == null ? userBin.getStartDate() : startDate,
                fireDate == null ? userBin.getFireDate() : fireDate,
                status == null ? userBin.getStatus() : status,
                fired == null ? userBin.getFired() : fired);
    }

    public static OperationResult addUserToGroup(int userId, int groupId) {
        OperationResult result = DBRequest.getGroupBinById(groupId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.getUserBinById(userId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.getUserGroupsBin(userId, groupId);
        if (!(result.getResult().equals(ENUMS.Result.NO_SUCH_CONNECTION))) {
            if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) {
                return result;
            }
            return new OperationResult(ENUMS.Result.CONNECTION_ALREADY_EXIST, null);
        }

        return DBRequest.createUserGroupsConnection(userId, groupId);
    }

    public static OperationResult removeUserFromGroup(int userId, int groupId) {
        OperationResult result = DBRequest.getGroupBinById(groupId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.getUserBinById(userId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        result = DBRequest.getUserGroupsBin(userId, groupId);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;

        return DBRequest.removeUserGroupsConnection(userId, groupId);
    }

    public static OperationResult addGroup(String name, Integer leaderId) {
        OperationResult result = DBRequest.getGroupId();
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        int groupId = (int) result.getBody();

        result = DBRequest.getGroupBinByName(name);
        if (!result.getResult().equals(ENUMS.Result.NO_SUCH_GROUP_NAME)) {
            if (result.getResult().equals(ENUMS.Result.SUCCESS))
                return new OperationResult(ENUMS.Result.GROUP_NAME_ALREADY_EXIST, null);
            return result;
        }

        if (leaderId != null) {
            result = DBRequest.getUserBinById(leaderId);
            if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        }

        result = DBRequest.insertGroup(groupId, name, leaderId);
        if (result.getResult().equals(ENUMS.Result.SUCCESS) && leaderId != null) {
            DBRequest.createUserGroupsConnection(leaderId, groupId);
        }
        return result;
    }

    public static OperationResult updateGroup(int id, String name, Integer leaderId) {
        OperationResult result = DBRequest.getGroupBinById(id);
        if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        GroupBin groupBin = (GroupBin) result.getBody();

        if (name != null) {
            result = DBRequest.getGroupBinByName(name);
            if (!result.getResult().equals(ENUMS.Result.NO_SUCH_GROUP_NAME)) {
                if (result.getResult().equals(ENUMS.Result.SUCCESS))
                    return new OperationResult(ENUMS.Result.GROUP_NAME_ALREADY_EXIST, null);
                return result;
            }
        }

        if (leaderId != null) {
            result = DBRequest.getUserBinById(leaderId);
            if (!(result.getResult().equals(ENUMS.Result.SUCCESS))) return result;
        }

        return DBRequest.updateGroup(id,
                name == null ? groupBin.getName() : name,
                leaderId == null ? groupBin.getLeaderId() : leaderId);
    }

    public static OperationResult removeGroup(int id) {
        OperationResult result = DBRequest.deleteGroup(id);
        if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
            DBRequest.removeUserGroupsConnectionForGroup(id);
        }
        return result;
    }

    public static OperationResult setGroupLeader(int groupId, int leaderId) {
        OperationResult result = DBRequest.getGroupBinById(groupId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        GroupBin group = (GroupBin) result.getBody();

        result = DBRequest.getUserBinById(leaderId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;

        result = DBRequest.updateGroup(groupId, group.getName(), leaderId);
        if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
            DBRequest.createUserGroupsConnection(leaderId, groupId);
        }
        return result;
    }

    public static OperationResult removeGroupLeader(int groupId) {
        OperationResult result = DBRequest.getGroupBinById(groupId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        GroupBin group = (GroupBin) result.getBody();

        if (group.getLeaderId() == 0) return new OperationResult(ENUMS.Result.GROUP_ALREADY_HAS_NO_LEADER, null);

        result = DBRequest.updateGroup(groupId, group.getName(), null);
        if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
            DBRequest.removeUserGroupsConnection(group.getLeaderId(), groupId);
        }
        return result;
    }

    public static OperationResult addDaysForUser(int userId, int year, int daysTotal, int partsTotal) {
        OperationResult result = DBRequest.getUserBinById(userId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;

        result = DBRequest.getUserDaysBin(userId, year);
        if (!result.getResult().equals(ENUMS.Result.NO_SUCH_YEAR_FOR_THIS_USER)) {
            if (result.getResult().equals(ENUMS.Result.SUCCESS))
                return new OperationResult(ENUMS.Result.USER_YEAR_CONNECTION_ALREADY_EXIST, null);
            return result;
        }


        return DBRequest.createUserDaysConnection(userId, year, daysTotal, partsTotal);
    }

    public static OperationResult removeDaysForUser(int userId, int year) {
        return DBRequest.removeUserDaysConnection(userId, year);
    }

    public static OperationResult updateDaysForUser(int userId, int year, Integer daysTotal, Integer partsTotal) {
        OperationResult result = DBRequest.getUserBinById(userId);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;

        result = DBRequest.getUserDaysBin(userId, year);
        if (!result.getResult().equals(ENUMS.Result.SUCCESS)) return result;
        UserDaysBin userDaysBin = (UserDaysBin) result.getBody();

        return DBRequest.updateUserDaysConnection(userId, year,
                daysTotal == null ? userDaysBin.getDaysTotal() : daysTotal,
                partsTotal == null ? userDaysBin.getPartsTotal() : partsTotal);
    }
}
