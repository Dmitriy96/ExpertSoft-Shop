package by.expertsoft.phone_shop.dao.impl;

import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.persistence.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPhoneDaoImpl implements PhoneDao {
    private static Logger logger = LogManager.getLogger(JdbcPhoneDaoImpl.class.getName());

    @Override
    public List<Phone> findAll() {
        List<Phone> phones = new ArrayList<>();
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to read phones", e);
        }
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM phone")) {
            while (resultSet.next()) {
                Phone phone = new Phone();
                phone.setId(resultSet.getLong(1));
                phone.setName(resultSet.getString(2));
                phone.setCode(resultSet.getString(3));
                phone.setPrice(resultSet.getDouble(4));
                phones.add(phone);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read phones", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to read phones", e);
                }
            }
        }
        return phones;
    }

    @Override
    public Phone get(long id) {
        Phone phone = null;
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to read phone", e);
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM phone WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    phone = new Phone();
                    phone.setId(resultSet.getLong(1));
                    phone.setName(resultSet.getString(2));
                    phone.setCode(resultSet.getString(3));
                    phone.setPrice(resultSet.getDouble(4));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read phone", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to read phone", e);
                }
            }
        }
        return phone;
    }

    @Override
    public void save(Phone phone) {
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to save phone", e);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO phone (name, code, price) VALUES(?, ?, ?)")) {
            statement.setString(1, phone.getName());
            statement.setString(2, phone.getCode());
            statement.setDouble(3, phone.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save phone", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to save phone", e);
                }
            }
        }
    }
}
