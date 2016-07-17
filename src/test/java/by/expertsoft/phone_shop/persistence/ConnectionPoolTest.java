package by.expertsoft.phone_shop.persistence;

import by.expertsoft.phone_shop.persistence.ConnectionPool;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTest {

    @Test
    public void getConnection() throws InterruptedException, SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        Assert.assertNotNull(connection);
        Assert.assertTrue(connection.isValid(0));
        connectionPool.releaseConnection(connection);
    }
}
