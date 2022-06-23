package DAO;

import Services.DataService;
import Util.DBConnection;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserBin {
    private int id;
    private String login;
    private String password;
    private String salt;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private LocalDate startDate;
    private LocalDate fireDate;
    private String status;
    private Boolean fired;
    private String[] AvailableActions = new String[0];
    private String[] Groups;

    public UserBin() {
    }

    public UserBin(int id, String login, String password, String salt, String firstName, String middleName, String lastName, String email, LocalDate startDate, LocalDate fireDate, String status, Boolean fired) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.salt = salt;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.startDate = startDate;
        this.fireDate = fireDate;
        this.status = status;
        this.fired = fired;
    }

    public static boolean createUser(int id, String login, String password, String salt, String first_name,
                                     String middle_name, String last_name, String email, LocalDate start_date,
                                     String status) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement(
                    "INSERT INTO USERS " +
                            "(id, login, password, salt, first_name, middle_name, last_name, email, start_date, " +
                            "status, fired) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setInt(1, id);
            statement.setString(2, login);
            statement.setString(3, password);
            statement.setString(4, salt);
            statement.setString(5, first_name);
            statement.setString(6, middle_name);
            statement.setString(7, last_name);
            statement.setString(8, email);
            statement.setObject(9, start_date);
            statement.setString(10, status);
            statement.setBoolean(11, false);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(int id, String login, String firstName, String middleName, String lastName, String email, LocalDate startDate, LocalDate fireDate, String status, boolean fired) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("UPDATE USERS SET id = ?, login = ?, first_name = ?, middle_name = ?, last_name = ?, email = ?, start_date = ?, fire_date = ?, status = ?, fired = ? WHERE id = ?");

            statement.setInt(1, id);
            statement.setString(2, login);
            statement.setString(3, firstName);
            statement.setString(4, middleName);
            statement.setString(5, lastName);
            statement.setString(6, email);
            statement.setObject(7, startDate);
            statement.setObject(8, fireDate);
            statement.setString(9, status);
            statement.setBoolean(10, fired);
            statement.setInt(11, id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUser(int id) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM USERS WHERE id = ?");

            statement.setInt(1, id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserBin findUserById(int userId, boolean fired) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, login, password, salt, first_name, middle_name, last_name, email, start_date, fire_date, status, fired FROM USERS WHERE id = ? AND fired = ?");

            statement.setInt(1, userId);
            statement.setBoolean(2, fired);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserBin(resultSet.getInt("id"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("salt"),
                        resultSet.getString("first_name"), resultSet.getString("middle_name"),
                        resultSet.getString("last_name"), resultSet.getString("email"),
                        resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("fire_date", LocalDate.class),
                        resultSet.getString("status"), resultSet.getBoolean("fired"));
            }

            return new UserBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserBin findUserByLogin(String login, boolean fired) {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, login, password, salt, first_name, middle_name, last_name, email, start_date, fire_date, status, fired FROM USERS WHERE login = ? AND fired = ?");

            statement.setString(1, login);
            statement.setBoolean(2, fired);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserBin(resultSet.getInt("id"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("salt"),
                        resultSet.getString("first_name"), resultSet.getString("middle_name"),
                        resultSet.getString("last_name"), resultSet.getString("email"),
                        resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("fire_date", LocalDate.class),
                        resultSet.getString("status"), resultSet.getBoolean("fired"));
            }

            return new UserBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<UserBin> findAllUsers(boolean fired) {
        Set<UserBin> users = new HashSet<>();
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement(
                    "SELECT id, login, password, salt, first_name, middle_name, last_name, " +
                            "email, start_date, fire_date, status, fired FROM USERS WHERE fired = ?");

            statement.setBoolean(1, fired);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new UserBin(resultSet.getInt("id"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("salt"),
                        resultSet.getString("first_name"), resultSet.getString("middle_name"),
                        resultSet.getString("last_name"), resultSet.getString("email"),
                        resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("fire_date", LocalDate.class),
                        resultSet.getString("status"), resultSet.getBoolean("fired")));
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<UserBin> findAllUserByLeader(int leaderId, boolean fired) {
        Set<UserBin> users = new HashSet<>();
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement(
                    "SELECT id, login, password, salt, first_name, middle_name, last_name, email, start_date, fire_date, status, fired " +
                            "FROM users " +
                            "INNER JOIN " +
                            "(SELECT user_groups.user_id u_id " +
                            "FROM groups " +
                            "LEFT JOIN user_groups " +
                            "ON groups.id = user_groups.group_id " +
                            "WHERE groups.leader_id = ?) " +
                            "AS users_from_leader " +
                            "ON users.id = users_from_leader.u_id WHERE fired = ?");

            statement.setInt(1, leaderId);
            statement.setBoolean(2, fired);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new UserBin(resultSet.getInt("id"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("salt"),
                        resultSet.getString("first_name"), resultSet.getString("middle_name"),
                        resultSet.getString("last_name"), resultSet.getString("email"),
                        resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("fire_date", LocalDate.class),
                        resultSet.getString("status"), resultSet.getBoolean("fired")));
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getMaxUserId() {
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT MAX(id) FROM USERS");
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

    @JsonGetter("Login")
    public String getLogin() {
        return login;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonGetter("FirstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonGetter("MiddleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonGetter("LastName")
    public String getLastName() {
        return lastName;
    }

    @JsonGetter("Email")
    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonGetter("StartedDate")
    public String getStartedDateString() {
        return DataService.getFormattedDateLocal(startDate);
    }

    @JsonIgnore
    public LocalDate getFireDate() {
        return fireDate;
    }

    @JsonGetter("FiredDate")
    public String getFiredDateString() {
        if (fireDate != null) return DataService.getFormattedDateLocal(fireDate);
        return "";
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    @JsonGetter("Roles")
    public String[] getStatuses() {
        return new String[]{status};
    }

    @JsonGetter("Fired")
    public Boolean getFired() {
        return fired;
    }

    @JsonGetter("AvailableActions")
    public String[] getAvailableActions() {
        return AvailableActions;
    }

    @JsonGetter("Groups")
    public String[] getGroups() {
        return Groups;
    }

    @JsonIgnore
    public String getSalt() {
        return salt;
    }

    public void setAvailableActions(String[] availableActions) {
        AvailableActions = availableActions;
    }

    public void setGroups(String[] groups) {
        Groups = groups;
    }

    @Override
    public String toString() {
        return "UserBin{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", first_name='" + firstName + '\'' +
                ", middle_name='" + middleName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", start_date=" + startDate +
                ", fire_date=" + fireDate +
                ", status='" + status + '\'' +
                ", fired=" + fired +
                ", AvailableActions=" + Arrays.toString(AvailableActions) +
                ", Groups=" + Arrays.toString(Groups) +
                '}';
    }
}
