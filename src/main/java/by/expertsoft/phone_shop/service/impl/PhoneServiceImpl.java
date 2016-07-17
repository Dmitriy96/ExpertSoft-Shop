package by.expertsoft.phone_shop.service.impl;


import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

public class PhoneServiceImpl implements PhoneService {

    private PhoneDao phoneDao;

    @Override
    public List<Phone> findAll() {
        return phoneDao.findAll();
    }

    @Override
    public Phone get(Long id) {
        return phoneDao.get(id);
    }

    @Override
    public void save(Phone phone) {
        phoneDao.save(phone);
    }

    public void setPhoneDao(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }
}
