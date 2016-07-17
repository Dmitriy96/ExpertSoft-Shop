package by.expertsoft.phone_shop.service;


import by.expertsoft.phone_shop.entity.Phone;

import java.util.List;

public interface PhoneService {
    List<Phone> findAll();
    Phone get(Long id);
    void save(Phone phone);
}
