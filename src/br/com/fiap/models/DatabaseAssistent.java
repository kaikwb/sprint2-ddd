package br.com.fiap.models;

import java.sql.*;
import java.util.Objects;

public class DatabaseAssistent {
    private Connection conn;
    private Statement stm;

    public DatabaseAssistent(String db_file) throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + db_file);
        this.stm = this.conn.createStatement();
    }

    public ResultSet executeQuery(String sql) {
        try {
            return this.stm.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void executeSQL(String sql) {
        try {
            this.stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeInset(String sql) {
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                rs.close();

                return id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public boolean checkTableExists(String table_name) {
        try {
            ResultSet rs = this.executeQuery(String.format("SELECT name FROM sqlite_master WHERE type='table' AND name='%s';", table_name));

            boolean table_exist = Objects.equals(rs.getString(1), table_name);
            rs.close();

            return table_exist;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ResultSet loadAll(String table_name) {
        return this.executeQuery(String.format("SELECT * FROM %s;", table_name));
    }
}
