package DAO;

import Services.DataService;
import Util.DBConnection;
import Variable.ENUMS;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RequestBin {
    int id;
    int userId;
    LocalDate creationDate;
    LocalDate startDate;
    LocalDate endDate;
    String status;
    String reason;
    String declineReason;
    String transferInfo;
    int duration;
    String[] AvailableActions = new String[0];

    public RequestBin(int id, int userId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, String status, String reason, String declineReason, String transferInfo, int duration) {
        this.id = id;
        this.userId = userId;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
        this.declineReason = declineReason;
        this.transferInfo = transferInfo;
        this.duration = duration;
    }

    public RequestBin() {

    }

    public static boolean createRequest(int id, int userId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, ENUMS.Status status, String reason, int duration) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("INSERT INTO REQUESTS (id, user_id, creation_date, start_date, end_date, status, reason, duration) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.setObject(3, creationDate);
            statement.setObject(4, startDate);
            statement.setObject(5, endDate);
            statement.setString(6, status.toString());
            statement.setString(7, reason);
            statement.setInt(8, duration);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateRequest(int id, int userId, LocalDate creationDate, LocalDate startDate, LocalDate endDate, String status, String reason, String declineReason, String transferInfo, int duration) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("UPDATE REQUESTS SET id = ?, user_id = ?, creation_date = ?, start_date = ?, end_date = ?, status = ?, reason = ?, decline_reason = ?, transfer_info = ?, duration = ? WHERE id = ?");

            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.setObject(3, creationDate);
            statement.setObject(4, startDate);
            statement.setObject(5, endDate);
            statement.setString(6, status);
            statement.setString(7, reason);
            statement.setString(8, declineReason);
            statement.setString(9, transferInfo);
            statement.setInt(10, duration);
            statement.setInt(11, id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRequest(int id) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM REQUEST WHERE id = ?");

            statement.setInt(1, id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRequestByUserId(int userId) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM REQUEST WHERE user_id = ?");

            statement.setInt(1, userId);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static RequestBin findRequestById(int id) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement(
                    "SELECT id, user_id, creation_date, start_date, end_date, status, reason, decline_reason, " +
                            "transfer_info, duration FROM REQUESTS WHERE id = ?");

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new RequestBin(resultSet.getInt("id"), resultSet.getInt("user_id"),
                        resultSet.getObject("creation_date", LocalDate.class),
                        resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("end_date", LocalDate.class),
                        resultSet.getString("status"),
                        resultSet.getString("reason"), resultSet.getString("decline_reason"),
                        resultSet.getString("transfer_info"), resultSet.getInt("duration"));
            }

            return new RequestBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<RequestBin> findRequestsByUserId(int userId) {
        Set<RequestBin> requests = new HashSet<>();
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, user_id, creation_date, start_date, end_date, status, reason, decline_reason, transfer_info, duration FROM REQUESTS WHERE user_id = ?");

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                requests.add(new RequestBin(resultSet.getInt("id"), resultSet.getInt("user_id"),
                        resultSet.getObject("creation_date", LocalDate.class), resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("end_date", LocalDate.class), resultSet.getString("status"),
                        resultSet.getString("reason"), resultSet.getString("decline_reason"),
                        resultSet.getString("transfer_info"), resultSet.getInt("duration")));
            }

            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Set<RequestBin> findAllRequests() {
        Set<RequestBin> requests = new HashSet<>();
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, user_id, creation_date, start_date, end_date, status, reason, decline_reason, transfer_info, duration FROM REQUESTS");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                requests.add(new RequestBin(resultSet.getInt("id"), resultSet.getInt("user_id"),
                        resultSet.getObject("creation_date", LocalDate.class), resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("end_date", LocalDate.class), resultSet.getString("status"),
                        resultSet.getString("reason"), resultSet.getString("decline_reason"),
                        resultSet.getString("transfer_info"), resultSet.getInt("duration")));
            }

            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getMaxRequestId() {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT MAX(id) FROM REQUESTS");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JsonGetter("Id")
    public int getId() {
        return id;
    }

    @JsonGetter("UserId")
    public int getUserId() {
        return userId;
    }

    @JsonIgnore
    public LocalDate getCreationDate() {
        return creationDate;
    }

    @JsonGetter("CreationDate")
    public String getCreationDateString() {
        return DataService.getFormattedDateLocal(creationDate);
    }

    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonGetter("StartDate")
    public String getStartDateString() {
        return DataService.getFormattedDateLocal(startDate);
    }

    @JsonIgnore
    public LocalDate getEndDate() {
        return endDate;
    }

    @JsonGetter("EndDate")
    public String getEndDateString() {
        return DataService.getFormattedDateLocal(endDate);
    }

    @JsonGetter("Status")
    public String getStatus() {
        return status;
    }

    @JsonIgnore
    public String getReason() {
        return reason;
    }

    @JsonGetter("Reason")
    public String getReasonNull() {
        return reason == null ? "" : reason;
    }

    @JsonIgnore
    public String getDeclineReason() {
        return declineReason;
    }

    @JsonGetter("DeclineReason")
    public String getDeclineReasonNull() {
        return declineReason == null ? "" : declineReason;
    }

    @JsonIgnore
    public String getTransferInfo() {
        return transferInfo;
    }

    @JsonGetter("TransferInfo")
    public String getTransferInfoNull() {
        return transferInfo == null ? "" : transferInfo;
    }

    @JsonGetter("Duration")
    public int getDuration() {
        return duration;
    }

    @JsonGetter("AvailableActions")
    public String[] getAvailableActions() {
        return AvailableActions;
    }

    @Override
    public String toString() {
        return "RequestBin{" +
                "id=" + id +
                ", userId=" + userId +
                ", creationDate=" + creationDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", declineReason='" + declineReason + '\'' +
                ", transferInfo='" + transferInfo + '\'' +
                ", duration=" + duration +
                ", AvailableActions=" + Arrays.toString(AvailableActions) +
                '}';
    }
}

