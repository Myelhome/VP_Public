package Session;

import Services.VacationService;
import Util.OperationResult;
import Variable.ENUMS;

import java.time.LocalDate;

public class DirectorVacationSession extends VacationSession {
    public DirectorVacationSession(User user) {
        super(user);
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
}
