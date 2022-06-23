package DAO;

import Util.DBConnection;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class UserDaysBin {

    private int userId;
    private int year;
    private int daysTotal;
    private int partsTotal;
    private int partsAvailable;
    private int daysAvailable;
    private Set<RequestBin> requests;

    public UserDaysBin(int userId, int year, int daysTotal, int partsTotal) {
        this.userId = userId;
        this.year = year;
        this.daysTotal = daysTotal;
        this.partsTotal = partsTotal;
    }

    public UserDaysBin() {}

    public static boolean createUserDays(int userId, int year, int daysTotal, int partsTotal){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("INSERT INTO USER_DAYS (user_id, year, days_total, parts_total) VALUES(?, ?, ?, ?)");

            statement.setInt(1, userId);
            statement.setInt(2, year);
            statement.setInt(3, daysTotal);
            statement.setInt(4, partsTotal);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUserDays(int userId, int year, int daysTotal, int partsTotal){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("UPDATE USER_DAYS SET user_id = ?, year = ?, days_total = ?, parts_total = ? WHERE user_id = ? AND year = ?");

            statement.setInt(1, userId);
            statement.setInt(2, year);
            statement.setInt(3, daysTotal);
            statement.setInt(4, partsTotal);
            statement.setInt(5, userId);
            statement.setInt(6, year);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserDays(int userId, int year){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM USER_DAYS WHERE user_id = ? AND year = ?");

            statement.setInt(1, userId);
            statement.setInt(2, year);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserDaysByUserId(int userId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM USER_DAYS WHERE user_id = ?");

            statement.setInt(1, userId);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserDaysBin findUserDaysByUserIdYear(int userId, int year){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT user_id, year, days_total, parts_total FROM USER_DAYS WHERE user_id = ? AND year = ?");

            statement.setInt(1, userId);
            statement.setInt(2, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserDaysBin(resultSet.getInt("user_id"), resultSet.getInt("year"),
                        resultSet.getInt("days_total"), resultSet.getInt("parts_total"));
            }

            return new UserDaysBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setPartsAvailable(int partsAvailable) {
        this.partsAvailable = partsAvailable;
    }

    public void setDaysAvailable(int daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public void setRequests(Set<RequestBin> requests) {
        this.requests = requests;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDaysTotal(int daysTotal) {
        this.daysTotal = daysTotal;
    }

    public void setPartsTotal(int partsTotal) {
        this.partsTotal = partsTotal;
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonGetter("Year")
    public int getYear() {
        return year;
    }

    @JsonIgnore
    public int getDaysTotal() {
        return daysTotal;
    }

    @JsonGetter("DaysInYear")
    public int getDaysUsed(){
        return daysTotal - daysAvailable;
    }

    @JsonGetter("PartsTotal")
    public int getPartsTotal() {
        return partsTotal;
    }

    @JsonGetter("PartsAvailable")
    public int getPartsAvailable() {
        return partsAvailable;
    }

    @JsonGetter("RestYearEnd")
    public int getDaysAvailable() {
        return daysAvailable;
    }

    @JsonGetter("Vacations")
    public Set<RequestBin> getRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return "UserDaysBin{" +
                "userId=" + userId +
                ", year=" + year +
                ", daysTotal=" + daysTotal +
                ", partsTotal=" + partsTotal +
                ", partsAvailable=" + partsAvailable +
                ", daysAvailable=" + daysAvailable +
                ", requests=" + requests +
                '}';
    }
}
