package by.expertsoft.phone_shop.dao;


import by.expertsoft.phone_shop.entity.Phone;

import java.util.List;

public interface PhoneDao {
    List<Phone> findAll();
    Phone get(long id);
    void save(Phone phone);
}
