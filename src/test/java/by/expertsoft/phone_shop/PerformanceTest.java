package by.expertsoft.phone_shop;

import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.dao.impl.JdbcPhoneDaoImpl;
import by.expertsoft.phone_shop.dao.impl.XmlPhoneDaoImpl;
import by.expertsoft.phone_shop.entity.Phone;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    @Test
    public void writeToFile() {                 // writeToFile: 100,227,899,520 nanoseconds
        PhoneDao phoneDao = new XmlPhoneDaoImpl();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            Phone phone = new Phone();
            phone.setId(i + 1L);
            phone.setName("samsung");
            phone.setCode("qwerty");
            phone.setPrice(123.4);
            phoneDao.save(phone);
        }
        long finish = System.nanoTime();
        System.out.println("writeToFile: " + (finish - start) + " nanoseconds");
    }

    @Test
    public void readFromFile() {                // readFromFile: 1,185,268,578 nanoseconds
        PhoneDao phoneDao = new XmlPhoneDaoImpl();
        long start = System.nanoTime();
        Phone phone = phoneDao.get(500);
        long finish = System.nanoTime();
        System.out.println(phone);
        System.out.println("readFromFile: " + (finish - start) + " nanoseconds");
    }

    @Test
    public void fillDB() {                      // 44,758,888,807 nanoseconds
        PhoneDao phoneDao = new JdbcPhoneDaoImpl();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            Phone phone = new Phone();
            phone.setName("samsung");
            phone.setCode("qwerty");
            phone.setPrice(123.4);
            phoneDao.save(phone);
        }
        long finish = System.nanoTime();
        System.out.println("fillDB: " + (finish - start) + " nanoseconds");
    }

    @Test
    public void fillDBMultiThreading() throws InterruptedException {                      // fillDBMultiThreading: 8,816,985,727 nanoseconds
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable runnable = () -> {
            PhoneDao phoneDao = new JdbcPhoneDaoImpl();
            for (int i = 0; i < 100; i++) {
                Phone phone = new Phone();
                phone.setName("nokia");
                phone.setCode("asd");
                phone.setPrice(123.4);
                phoneDao.save(phone);
            }
        };
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            executorService.execute(runnable);
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long finish = System.nanoTime();
        System.out.println("fillDBMultiThreading: " + (finish - start) + " nanoseconds");
    }

    @Test
    public void findPhone() {                   // 774,051,897 nanoseconds
        PhoneDao phoneDao = new JdbcPhoneDaoImpl();
        long start = System.nanoTime();
        Phone phone = phoneDao.get(500);
        long finish = System.nanoTime();
        System.out.println(phone);
        System.out.println("findPhone: " + (finish - start) + " nanoseconds");
    }
}
