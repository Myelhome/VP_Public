package DAO;

import Util.DBConnection;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

public class GroupBin {

    private int id;
    private String name;
    private int leaderId;

    public GroupBin() {
    }

    public GroupBin(int id, String name, int leaderId) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
    }

    public static boolean createGroup(int id, String name, Integer leaderId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("INSERT INTO GROUPS (id, name, leader_id) VALUES(?, ?, ?)");

            statement.setInt(1, id);
            statement.setString(2, name);
            if (leaderId != null) {
                statement.setInt(3, leaderId);
            }else statement.setNull(3, Types.BIGINT);

            statement.executeUpdate();

            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateGroup(int id, String name, Integer leaderId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("UPDATE GROUPS SET id = ?, name = ?, leader_id = ? WHERE id = ?");

            statement.setInt(1, id);
            statement.setString(2, name);
            if (leaderId != null) {
                statement.setInt(3, leaderId);
            }else statement.setNull(3, Types.BIGINT);
            statement.setInt(4, id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteGroup(int id){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("DELETE FROM GROUPS WHERE id = ?");

            statement.setInt(1, id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static GroupBin findGroupById(int groupId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, name, leader_id FROM GROUPS WHERE id = ?");

            statement.setInt(1, groupId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new GroupBin(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("leader_id"));
            }
            return new GroupBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GroupBin findGroupByName(String name){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, name, leader_id FROM GROUPS WHERE name = ?");

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new GroupBin(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("leader_id"));
            }
            return new GroupBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GroupBin findGroupByLeaderId(int leaderId){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, name, leader_id FROM GROUPS WHERE leader_id = ?");

            statement.setInt(1, leaderId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new GroupBin(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("leader_id"));
            }
            return new GroupBin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<GroupBin> findAllGroups(){
        Set<GroupBin> groups = new HashSet<>();
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT id, name, leader_id FROM GROUPS");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                GroupBin groupBin = new GroupBin(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("leader_id"));
                groups.add(groupBin);
            }

            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<GroupBin> findAllGroupsForUser(int userId) {
        Set<GroupBin> groups = new HashSet<>();
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT groups.id, name, leader_id FROM GROUPS INNER JOIN USER_GROUPS ON user_groups.group_id = groups.id WHERE user_groups.user_id = ?");

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                GroupBin groupBin = new GroupBin(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("leader_id"));
                groups.add(groupBin);
            }

            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getMaxGroupId(){
        try {
            PreparedStatement statement = DBConnection.getDbConnection().getConnection().prepareStatement("SELECT MAX(id) FROM GROUPS");
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

    @JsonGetter("Name")
    public String getName() {
        return name;
    }

    @JsonGetter("LeaderId")
    public int getLeaderId() {
        return leaderId;
    }

    @Override
    public String toString(){
        return "id: " + id + ", name: " + name + ", leaderId: " + leaderId;
    }
}
