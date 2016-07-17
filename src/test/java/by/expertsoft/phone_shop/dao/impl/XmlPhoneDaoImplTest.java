package by.expertsoft.phone_shop.dao.impl;


import by.expertsoft.phone_shop.entity.Phone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class XmlPhoneDaoImplTest {

    private File tmpPhonesFile = File.createTempFile("tmpPhones", "xml");

    public XmlPhoneDaoImplTest() throws IOException {
    }


    private XmlPhoneDaoImpl createTempPhoneFileWithData() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = new XmlPhoneDaoImpl(){
            @Override
            protected File getPhonesFile() {
                return tmpPhonesFile;
            }
        };
        Files.deleteIfExists(tmpPhonesFile.toPath());
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setName("samsung");
        phone.setCode("abc");
        phone.setPrice(123.4);
        xmlPhoneDao.save(phone);
        return xmlPhoneDao;
    }

    private XmlPhoneDaoImpl assureTempPhoneFileNotExists() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = new XmlPhoneDaoImpl(){
            @Override
            protected File getPhonesFile() {
                return tmpPhonesFile;
            }
        };
        Files.deleteIfExists(tmpPhonesFile.toPath());
        return xmlPhoneDao;
    }

    @After
    public void deleteTempPhonesFile() throws IOException {
        Files.deleteIfExists(tmpPhonesFile.toPath());
    }

    @Test
    public void findAllPhones() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = createTempPhoneFileWithData();
        List<Phone> phones = xmlPhoneDao.findAll();
        Assert.assertNotNull(phones);
        Assert.assertTrue(phones.size() > 0);
    }

    @Test
    public void getPhone() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = createTempPhoneFileWithData();
        Phone phone = xmlPhoneDao.get(1L);
        Assert.assertNotNull(phone);
        Assert.assertEquals("samsung", phone.getName());
        Assert.assertEquals("abc", phone.getCode());
        Assert.assertEquals(new Double(123.4), phone.getPrice());
    }

    @Test
    public void savePhone() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = createTempPhoneFileWithData();
        Phone phone = new Phone();
        phone.setId(2L);
        phone.setName("nokia");
        phone.setCode("qwerty");
        phone.setPrice(234.5);
        xmlPhoneDao.save(phone);
        Phone savedPhone = xmlPhoneDao.get(1L);
        Assert.assertNotNull(savedPhone);
        Assert.assertEquals("samsung", savedPhone.getName());
        Assert.assertEquals("abc", savedPhone.getCode());
        Assert.assertEquals(new Double(123.4), savedPhone.getPrice());
        Phone secondSavedPhone = xmlPhoneDao.get(2L);
        Assert.assertNotNull(secondSavedPhone);
        Assert.assertEquals("nokia", secondSavedPhone.getName());
        Assert.assertEquals("qwerty", secondSavedPhone.getCode());
        Assert.assertEquals(new Double(234.5), secondSavedPhone.getPrice());
        List<Phone> phones = xmlPhoneDao.findAll();
        Assert.assertNotNull(phones);
        Assert.assertTrue(phones.size() == 2);
    }

    @Test
    public void findAllPhonesWithMissingFile() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = assureTempPhoneFileNotExists();
        List<Phone> phones = xmlPhoneDao.findAll();
        Assert.assertNull(phones);
    }

    @Test
    public void getPhoneWithMissingFile() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = assureTempPhoneFileNotExists();
        Phone phone = xmlPhoneDao.get(1L);
        Assert.assertNull(phone);
    }

    @Test
    public void savePhoneWithMissingFile() throws IOException {
        XmlPhoneDaoImpl xmlPhoneDao = assureTempPhoneFileNotExists();
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setName("samsung");
        phone.setCode("abc");
        phone.setPrice(123.4);
        xmlPhoneDao.save(phone);
        Phone savedPhone = xmlPhoneDao.get(1L);
        Assert.assertNotNull(savedPhone);
        Assert.assertEquals("samsung", savedPhone.getName());
        Assert.assertEquals("abc", savedPhone.getCode());
        Assert.assertEquals(new Double(123.4), savedPhone.getPrice());
    }
}
