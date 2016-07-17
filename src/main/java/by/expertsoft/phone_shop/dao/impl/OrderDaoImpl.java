package by.expertsoft.phone_shop.dao.impl;


import by.expertsoft.phone_shop.dao.OrderDao;
import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderItem;
import by.expertsoft.phone_shop.entity.OrderStatus;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.persistence.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OrderDaoImpl implements OrderDao {
    private static Logger logger = LogManager.getLogger(OrderDaoImpl.class.getName());

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to read orders", e);
        }
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `order` o JOIN phone p ON o.phone_id = p.id")) {
            Set<Order> orderSet = new HashSet<>();
            Map<String, List<OrderItem>> orderItemMap = new HashMap<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong("o.id"));
                order.setName(resultSet.getString("o.name"));
                order.setSurname(resultSet.getString("o.surname"));
                order.setPhoneNo(resultSet.getString("o.phone_number"));
                order.setDate(resultSet.getString("o.date"));
                order.setStatus(OrderStatus.valueOf(resultSet.getString("o.status")));
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(resultSet.getInt("o.quantity"));
                Phone phone = new Phone();
                phone.setId(resultSet.getLong("p.id"));
                phone.setName(resultSet.getString("p.name"));
                phone.setCode(resultSet.getString("p.code"));
                phone.setPrice(resultSet.getDouble("p.price"));
                orderItem.setPhone(phone);
                if (orderItemMap.get(order.getDate()) == null) {
                    List<OrderItem> orderItems = new ArrayList<>();
                    orderItems.add(orderItem);
                    orderItemMap.put(order.getDate(), orderItems);
                } else
                    orderItemMap.get(order.getDate()).add(orderItem);
                order.setTotalPrice(resultSet.getBigDecimal("o.total_price"));
                orderSet.add(order);
            }
            orders.addAll(orderSet);
            for (Order order : orders) {
                order.setOrderItems(orderItemMap.get(order.getDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read orders", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to read orders", e);
                }
            }
        }
        return orders;
    }

    @Override
    public Order get(long id) {
        Order order = null;
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to read order", e);
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `order` o JOIN phone p ON o.phone_id = p.id WHERE o.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<OrderItem> orderItems = new ArrayList<>();
                while (resultSet.next()) {
                    order = new Order();
                    order.setId(resultSet.getLong("o.id"));
                    order.setName(resultSet.getString("o.name"));
                    order.setSurname(resultSet.getString("o.surname"));
                    order.setPhoneNo(resultSet.getString("o.phone_number"));
                    order.setDate(resultSet.getString("o.date"));
                    order.setStatus(OrderStatus.valueOf(resultSet.getString("o.status")));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(resultSet.getInt("o.quantity"));
                    Phone phone = new Phone();
                    phone.setId(resultSet.getLong("p.id"));
                    phone.setName(resultSet.getString("p.name"));
                    phone.setCode(resultSet.getString("p.code"));
                    phone.setPrice(resultSet.getDouble("p.price"));
                    orderItem.setPhone(phone);
                    orderItems.add(orderItem);
                    order.setTotalPrice(resultSet.getBigDecimal("o.total_price"));
                    order.setOrderItems(orderItems);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read order", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to read order", e);
                }
            }
        }
        return order;
    }

    @Override
    public void save(Order order) {
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to save order", e);
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `order` (`name`, surname, phone_number, `date`, status, phone_id, quantity, total_price) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
            for (OrderItem orderItem : order.getOrderItems()) {
                statement.setString(1, order.getName());
                statement.setString(2, order.getSurname());
                statement.setString(3, order.getPhoneNo());
                statement.setString(4, order.getDate());
                statement.setString(5, order.getStatus().name());
                statement.setLong(6, orderItem.getPhone().getId());
                statement.setInt(7, orderItem.getQuantity());
                statement.setBigDecimal(8, order.getTotalPrice());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save order", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to save order", e);
                }
            }
        }
    }

    @Override
    public void update(Order order) {
        Connection connection = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to update order", e);
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE `order` SET `name` = ?, surname = ?, phone_number = ?, `date` = ?," +
                        " status = ?, phone_id = ?, quantity = ?, total_price = ? WHERE id = ?")) {
            for (OrderItem orderItem : order.getOrderItems()) {
                statement.setString(1, order.getName());
                statement.setString(2, order.getSurname());
                statement.setString(3, order.getPhoneNo());
                statement.setString(4, order.getDate());
                statement.setString(5, order.getStatus().name());
                statement.setLong(6, orderItem.getPhone().getId());
                statement.setInt(7, orderItem.getQuantity());
                statement.setBigDecimal(8, order.getTotalPrice());
                statement.setLong(9, order.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order", e);
        } finally {
            if (connection != null) {
                try {
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to update order", e);
                }
            }
        }
    }
}
