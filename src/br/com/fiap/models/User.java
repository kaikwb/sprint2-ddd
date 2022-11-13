package br.com.fiap.models;

import br.com.fiap.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class User {
    private static final String table_name = "Users";
    private final int user_code;
    private String name;
    private String last_name;
    private String mail;
    private String password;
    private boolean is_subscriber;
    private Date subscription_date;
    private final DatabaseAssistent dba;

    private User(int user_code, String name, String last_name, String mail, String password, boolean is_subscriber, Date subscription_date, DatabaseAssistent dba) {
        this.user_code = user_code;
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.password = password;
        this.is_subscriber = is_subscriber;
        this.subscription_date = subscription_date;
        this.dba = dba;
    }

    private static void createTable(DatabaseAssistent dba) {
        String sql_create = String.format(
                "CREATE TABLE %s (" +
                        "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name text NOT NULL," +
                        "last_name text NOT NULL," +
                        "mail text NOT NULL," +
                        "password text NOT NULL," +
                        "is_subscriber INTEGER," +
                        "subscription_date TEXT" +
                        ");",
                table_name
        );

        dba.executeSQL(sql_create);
    }

    public static User createUser(String name, String last_name, String mail, String password, DatabaseAssistent dba) {
        if (!dba.checkTableExists(table_name))
            createTable(dba);

        String sql_insert = String.format(
                "INSERT INTO %s (name, last_name, mail, password) " +
                        "VALUES ('%s', '%s', '%s', '%s');", table_name, name, last_name, mail, password
        );

        int user_code = dba.executeInset(sql_insert);

        return new User(user_code, name, last_name, mail, password, false, new Date(), dba);
    }

    private static User createFromResultSet(DatabaseAssistent dba, ResultSet rs) throws SQLException, ParseException {
        return new User(
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getInt(6) != 0,
                Utils.dateFromDbFormat(rs.getString(7)), dba);
    }

    public static User[] loadAllUsers(DatabaseAssistent dba) throws SQLException, ParseException {
        if (!dba.checkTableExists(table_name))
            createTable(dba);

        ResultSet rs = dba.loadAll(table_name);

        if (rs != null) {
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(createFromResultSet(dba, rs));
            }

            return users.toArray(User[]::new);
        }

        return null;
    }

    public void saveUser() {
        if (!dba.checkTableExists(table_name))
            createTable(dba);

        String sql_update = String.format(
                "UPDATE %s " +
                "SET name='%s', last_name='%s', mail='%s', password='%s', is_subscriber=%d, subscription_date='%s' " +
                "WHERE user_id=%d;",
                table_name,
                name, last_name, mail, password, is_subscriber ? 1 : 0, Utils.dateToDbDate(subscription_date),
                user_code
        );

        dba.executeSQL(sql_update);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIs_subscriber() {
        return is_subscriber;
    }

    public void setIs_subscriber(boolean is_subscriber) {
        this.is_subscriber = is_subscriber;
    }

    public Date getSubscription_date() {
        return subscription_date;
    }

    public void setSubscription_date(Date subscription_date) {
        this.subscription_date = subscription_date;
    }
}
