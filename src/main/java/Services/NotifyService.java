package Services;

import DAO.DBRequest;
import DAO.RequestBin;
import DAO.UserBin;
import Util.MailSender;
import Util.OperationResult;
import Variable.ENUMS;

import java.util.Set;

public class NotifyService {
    public static void checkAllRequests(){
        OperationResult result = DBRequest.getAllRequestBins();
        if(!result.getResult().equals(ENUMS.Result.SUCCESS)) return;
        Set<RequestBin> requestBins = (Set<RequestBin>) result.getBody();

        for(RequestBin requestBin: requestBins){
            result = DBRequest.getUserBinById(requestBin.getUserId());
            if(!result.getResult().equals(ENUMS.Result.SUCCESS)) return;
            UserBin user = (UserBin) result.getBody();

            changeStatusForRequest(requestBin, user.getEmail());
        }
    }

    private static void changeStatusForRequest(RequestBin request, String email){
        if(DataService.isStarted(request.getStartDate())) {
            if (request.getStatus().equals(ENUMS.Status.ACCEPTED_BY_DIRECTOR.toString())) {
                OperationResult result = DBRequest.updateRequest(request.getId(), request.getUserId(), request.getCreationDate(), request.getStartDate(), request.getEndDate(), ENUMS.Status.IN_VACATION.toString(), request.getReason(), request.getDeclineReason(), request.getTransferInfo(), request.getDuration());

                if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
                    MailSender.vacationStartMail(email, request.getStartDate().toString(), request.getEndDate().toString());
                }

            } else if (!(request.getStatus().equals(ENUMS.Status.IN_VACATION.toString())
                    || request.getStatus().equals(ENUMS.Status.DONE.toString())
                    || request.getStatus().equals(ENUMS.Status.REFUSED_BY_LEADER.toString())
                    || request.getStatus().equals(ENUMS.Status.REFUSED_BY_ACCOUNTANT.toString())
                    || request.getStatus().equals(ENUMS.Status.REFUSED_BY_DIRECTOR.toString()))){

                ENUMS.Status status = getStatusForDeclination(request.getStatus());
                if (status != null) {
                    OperationResult result = DBRequest.updateRequest(request.getId(), request.getUserId(), request.getCreationDate(), request.getStartDate(), request.getEndDate(), status.toString(), request.getReason(), request.getDeclineReason(), request.getTransferInfo(), request.getDuration());

                    if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
                        MailSender.declineMail(email, request.getStartDate().toString(), request.getEndDate().toString(), "Запрос не был принят вовремя.");
                    }
                }
            }
        }

        if(DataService.isFinished(request.getEndDate()) && request.getStatus().equals(ENUMS.Status.IN_VACATION.toString())){
            OperationResult result = DBRequest.updateRequest(request.getId(), request.getUserId(), request.getCreationDate(), request.getStartDate(), request.getEndDate(), ENUMS.Status.DONE.toString(), request.getReason(), request.getDeclineReason(), request.getTransferInfo(), request.getDuration());

            if (result.getResult().equals(ENUMS.Result.SUCCESS)) {
                MailSender.vacationEndMail(email, request.getDuration(), request.getEndDate().toString());
            }
        }
    }

    private static ENUMS.Status getStatusForDeclination(String statusString){
        ENUMS.Status status = null;
        ENUMS.Status declineStatus = null;

        try{
            status = ENUMS.Status.valueOf(statusString);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        if (status == null) return null;

        switch (status){
            case MOVED:
            case CREATED:
                declineStatus = ENUMS.Status.REFUSED_BY_LEADER;
                break;
            case ACCEPTED_BY_LEADER:
                declineStatus = ENUMS.Status.REFUSED_BY_ACCOUNTANT;
                break;
            case ACCEPTED_BY_ACCOUNTANT:
                declineStatus = ENUMS.Status.REFUSED_BY_DIRECTOR;
                break;
        }

        return declineStatus;
    }
}
