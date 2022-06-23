package DAO;

import Util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGroupsBin {
    private int userId;
    private int groupId;

    public UserGroupsBin(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public UserGroupsBin() {
    }

    public static boolean createUserGroups(int userId, int groupId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("INSERT INTO USER_GROUPS (user_id, group_id) VALUES(?, ?)");

            statement.setInt(1, userId);
            statement.setInt(2, groupId);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserGroups(int userId, int groupId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM USER_GROUPS WHERE user_id = ? AND groupId = ?");

            statement.setInt(1, userId);
            statement.setInt(2, groupId);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserGroupsByGroupId(int groupId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM USER_GROUPS WHERE groupId = ?");

            statement.setInt(1, groupId);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserGroupsByUserId(int user_id){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM USER_GROUPS WHERE user_id = ?");

            statement.setInt(1, user_id);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserGroupsBin findByUserId(int userId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT user_id, group_id FROM USER_GROUPS WHERE user_id = ?");

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserGroupsBin(resultSet.getInt("user_id"), resultSet.getInt("group_id"));
            }
            return new UserGroupsBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserGroupsBin findByGroupId(int groupId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT user_id, group_id FROM USER_GROUPS WHERE group_id = ?");

            statement.setInt(1, groupId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserGroupsBin(resultSet.getInt("user_id"), resultSet.getInt("group_id"));
            }
            return new UserGroupsBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserGroupsBin findByUserIdGroupId(int userId, int groupId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT user_id, group_id FROM USER_GROUPS WHERE user_id = ? AND group_id = ?");

            statement.setInt(1, userId);
            statement.setInt(2, groupId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserGroupsBin(resultSet.getInt("user_id"), resultSet.getInt("group_id"));
            }
            return new UserGroupsBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUserId() {
        return userId;
    }

    public int getGroupId() {
        return groupId;
    }
}
