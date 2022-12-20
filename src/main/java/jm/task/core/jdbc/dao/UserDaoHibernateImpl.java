package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class UserDaoHibernateImpl implements UserDao {
    private final static String createHibernateSequence = "CREATE TABLE hibernate_sequence ( next_val BIGINT )";
    public static final String insert = "insert into hibernate_sequence (next_val) VALUES (1)";

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getHibernateSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("create table if not exists text.new_table (id int not null auto_increment primary key, name varchar(45) not null, lastName varchar(45) not null, age int not null)")
                    .executeUpdate();
            session.createSQLQuery(createHibernateSequence).executeUpdate();
            session.createSQLQuery(insert).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            ofNullable(transaction).ifPresent(Transaction::rollback);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getHibernateSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("drop table if exists text.new_table")
                    .executeUpdate();
            session.createSQLQuery("drop table if exists text.hibernate_sequence")
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            ofNullable(transaction).ifPresent(Transaction::rollback);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getHibernateSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            ofNullable(transaction).ifPresent(Transaction::rollback);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getHibernateSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            ofNullable(transaction).ifPresent(Transaction::rollback);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> userList = new ArrayList<>();
        try (Session session = Util.getHibernateSession()) {
            transaction = session.beginTransaction();
            userList = session.createQuery("SELECT u FROM User u", User.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            ofNullable(transaction).ifPresent(Transaction::rollback);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getHibernateSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("truncate text.new_table")
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            ofNullable(transaction).ifPresent(Transaction::rollback);
        }
    }
}
