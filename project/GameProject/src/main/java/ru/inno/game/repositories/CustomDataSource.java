package ru.inno.game.repositories;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

// дает подключиться к БД
public class CustomDataSource implements DataSource {

    private Connection connection;

    private String user;
    private String password;
    private String url;

    public CustomDataSource(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // если нет connection или он закрыт
        if (connection == null || connection.isClosed()) {
            // попросить новый connection
            this.connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new NotImplementedException();
    }
}
