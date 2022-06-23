package Session;

import Services.VacationService;
import Util.OperationResult;
import Variable.ENUMS;

import java.time.LocalDate;

public class UserVacationSession extends VacationSession {
    public UserVacationSession(User user) {
        super(user);
    }

    @Override
    public OperationResult showAllRequestsForUserSession() {
        return VacationService.showAllRequestsForUser(user.getUserId());
    }

    @Override
    public OperationResult showAllRequestsForLeaderSession() {
        return VacationService.showAllRequestsForLeader(user.getUserId());
    }

    @Override
    public OperationResult makeRequestForVacationSession(LocalDate startDate, LocalDate endDate, String reason) {
        return VacationService.makeRequestForVacation(user.getUserId(), startDate, endDate, ENUMS.Status.CREATED, reason);
    }

    @Override
    public OperationResult changeRequestDatesSession(int requestId, LocalDate startDate, LocalDate endDate, String transferInfo) {
        return VacationService.changeRequestDates(user.getUserId(), requestId, startDate, endDate, transferInfo);
    }

    @Override
    public OperationResult removeRequestSession(int requestId) {
        return VacationService.removeRequest(user.getUserId(), requestId);
    }

    @Override
    public OperationResult approveRequestSession(int requestId) {
        return VacationService.approveRejectRequest(user.getUserId(), requestId, false, null);
    }

    @Override
    public OperationResult rejectRequestSession(int requestId, String declineReason) {
        return VacationService.approveRejectRequest(user.getUserId(), requestId, true, declineReason);
    }
}
