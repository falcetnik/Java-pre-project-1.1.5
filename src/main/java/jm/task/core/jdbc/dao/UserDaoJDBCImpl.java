package jm.task.core.jdbc.dao;

import com.mysql.cj.jdbc.PreparedStatementWrapper;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    @Override
    public void createUsersTable() {
        String sql = "create table text.new_table \n" +
                "(\n" +
                "\tid int not null auto_increment primary key,\n" +
                "    name varchar(45) null,\n" +
                "    lastName varchar(45) null,\n" +
                "    age int null\n" +
                ")";

        try (
                Connection connection = Util.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "drop table if exists text.new_table;";

        try (
                Connection connection = Util.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "insert into text.new_table(name, lastName, age) values (?, ?, ?)";

        try (
                Connection connection = Util.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "delete from text.new_table where id = ?;";

        try (
                Connection connection = Util.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "select * from text.new_table";

        try (
                Connection connection = Util.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

        ) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "truncate text.new_table";

        try (
                Connection connection = Util.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
