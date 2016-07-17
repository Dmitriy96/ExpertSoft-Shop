package by.expertsoft.phone_shop.persistence;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class ConnectionPool {
    private Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
    private final int POOL_SIZE = 10;
    private BlockingQueue<Connection> availableConnections = new LinkedBlockingQueue<>(POOL_SIZE);
    private List<Connection> connections = new ArrayList<>(POOL_SIZE);
    private final String DB_DRIVER;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;


    private static class SingletonHolder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private ConnectionPool() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            logger.error(e);
            throw new ExceptionInInitializerError(e);
        }
        DB_DRIVER = properties.getProperty("db_driver");
        DB_URL = properties.getProperty("db_url");
        DB_USER = properties.getProperty("db_user");
        DB_PASSWORD = properties.getProperty("db_password");
        try {
            Class.forName(DB_DRIVER);
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                availableConnections.add(connection);
                connections.add(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public Connection getConnection() throws InterruptedException {
        return availableConnections.take();
    }

    public void releaseConnection(Connection connection) throws InterruptedException {
        if (connection == null)
            throw new NullPointerException();
        if (!connections.contains(connection))
            throw  new IllegalArgumentException();
        availableConnections.put(connection);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for (Connection connection : availableConnections) {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }
}
