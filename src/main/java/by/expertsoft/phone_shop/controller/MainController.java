package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class MainController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(MainController.class.getName());
    private PhoneService phoneService;

    public String getMainPage(Model model) {
        logger.debug("getMainPage");
        model.addAttribute("phones", phoneService.findAll());
        return "main";
    }

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }
}