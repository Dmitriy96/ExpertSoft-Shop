package by.expertsoft.phone_shop.dao.impl;

import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.persistence.ConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class JdbcPhoneDaoImplTest {

    @Before
    public void prepareDB() throws InterruptedException, SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("truncate phone;");
        statement.execute("INSERT INTO phone (NAME, CODE, PRICE) VALUES('samsung', 'abc', 123.4)");
        statement.execute("INSERT INTO phone (NAME, CODE, PRICE) VALUES('samsung', 'qwerty', 234.5)");
        statement.close();
        connectionPool.releaseConnection(connection);
    }

    @Test
    public void findAllTest() {
        PhoneDao jdbcPhoneDao = new JdbcPhoneDaoImpl();
        List<Phone> phoneList = jdbcPhoneDao.findAll();
        Assert.assertNotNull(phoneList);
        Assert.assertTrue(phoneList.size() > 1);
    }

    @Test
    public void getById() {
        PhoneDao jdbcPhoneDao = new JdbcPhoneDaoImpl();
        Phone phone = jdbcPhoneDao.get(1L);
        Assert.assertNotNull(phone);
        Assert.assertEquals("samsung", phone.getName());
        Assert.assertEquals("abc", phone.getCode());
        Assert.assertEquals(new Double(123.4), phone.getPrice());
    }

    @Test
    public void save() {
        PhoneDao jdbcPhoneDao = new JdbcPhoneDaoImpl();
        Phone phone = new Phone();
        phone.setName("nokia");
        phone.setCode("cvb");
        phone.setPrice(456.7);
        List<Phone> phoneList = jdbcPhoneDao.findAll();
        int currentRecordsCount = phoneList.size();
        jdbcPhoneDao.save(phone);
        List<Phone> updatedPhoneList = jdbcPhoneDao.findAll();
        Assert.assertEquals(++currentRecordsCount, updatedPhoneList.size());
    }

    @After
    public void cleanDB() throws InterruptedException, SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("truncate phone;");
        statement.close();
        connectionPool.releaseConnection(connection);
    }
}
