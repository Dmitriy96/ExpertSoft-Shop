package by.expertsoft.phone_shop.dao.impl;


import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.entity.Phone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class XmlPhoneDaoImpl implements PhoneDao {
    private static Logger logger = LogManager.getLogger(XmlPhoneDaoImpl.class.getName());
    private final String PHONES_FILE_NAME = "phones.xml";
    private final String ROOT_ELEMENT_NAME = "phones";
    private final String ENTITY_ELEMENT_NAME = "phone";

    @Override
    public List<Phone> findAll() {
        List<Phone> phones = null;
        File phonesFile = new File(getPhonesFile().toURI());
        boolean isExists = phonesFile.exists();
        if (isExists) {
            JAXBContext jaxbContext = null;
            try {
                jaxbContext = JAXBContext.newInstance(Wrapper.class, Phone.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                StreamSource streamSource = new StreamSource(getPhonesFile());
                Wrapper<Phone> phonesWrapper = (Wrapper<Phone>) unmarshaller.unmarshal(streamSource, Wrapper.class).getValue();
                phones = phonesWrapper.getItems();
            } catch (JAXBException e) {
                throw new RuntimeException("Failed to read phones", e);
            }
        }
        return phones;
    }

    @Override
    public Phone get(long id) {
        Phone phone = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        try (FileInputStream inputStream = new FileInputStream(getPhonesFile())) {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
            JAXBContext jaxbContext = JAXBContext.newInstance(Phone.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            boolean hasFind = false;
            while(xmlEventReader.hasNext() && !hasFind) {
                if(xmlEventReader.peek().isStartElement() &&
                        xmlEventReader.peek().asStartElement().getName().getLocalPart().equals(ENTITY_ELEMENT_NAME)) {
                    phone = (Phone) unmarshaller.unmarshal(xmlEventReader);
                    if (phone != null && id == phone.getId())
                        hasFind = true;
                } else {
                    xmlEventReader.nextEvent();
                }
            }
        } catch (JAXBException | XMLStreamException e) {
            throw new RuntimeException("Failed to read phone", e);
        } catch (IOException e) {
          return phone;
        }
        if (phone != null && phone.getId() != id) {
            phone = null;
        }
        return phone;
    }

    protected File getPhonesFile() {
        return new File(PHONES_FILE_NAME);
    }

    @Override
    public void save(Phone phone) {
        File phonesFile = getPhonesFile();
        boolean isExists = phonesFile.exists();
        if (isExists) {
            saveToExistingFile(phone);
        } else {
            try {
                saveToNewFile(phone);
            } catch (JAXBException e) {
                throw new RuntimeException("Failed to save phone", e);
            }
        }
    }

    private void saveToExistingFile(Phone phone) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("tmp", "xml");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save phone", e);
        }
        try (FileInputStream phonesInputStream = new FileInputStream(getPhonesFile());
             FileOutputStream phonesOutputStream = new FileOutputStream(tmpFile)) {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(phonesInputStream);
            XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(phonesOutputStream);
            JAXBContext jaxbContext = JAXBContext.newInstance(Phone.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            boolean isPhoneSaved = false;
            while(xmlEventReader.hasNext()) {
                if(!isPhoneSaved && xmlEventReader.peek().isStartElement() &&
                        xmlEventReader.peek().asStartElement().getName().getLocalPart().equals(ENTITY_ELEMENT_NAME)) {
                    Phone savedPhone = (Phone) unmarshaller.unmarshal(xmlEventReader);
                    marshaller.marshal(phone, xmlEventWriter);
                    marshaller.marshal(savedPhone, xmlEventWriter);
                    isPhoneSaved = true;
                } else {
                    xmlEventWriter.add(xmlEventReader.nextEvent());
                }
            }
        } catch (JAXBException | XMLStreamException | IOException e) {
            throw new RuntimeException("Failed to save phone", e);
        }
        if (tmpFile != null) {
            Path tmpFilePath = Paths.get(tmpFile.toURI());
            try {
                Files.move(tmpFilePath, tmpFilePath.resolve(getPhonesFile().getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save phone", e);
            } finally {
                tmpFile.delete();
            }
        }
    }

    private void saveToNewFile(Phone phone) throws JAXBException {
        JAXBContext jaxbContext = null;
        Marshaller marshaller = null;
        jaxbContext = JAXBContext.newInstance(Wrapper.class, Phone.class);
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        QName qName = new QName(ROOT_ELEMENT_NAME);
        Wrapper<Phone> phonesWrapper = new Wrapper<>(Arrays.asList(phone));
        JAXBElement<Wrapper> jaxbElement = new JAXBElement<>(qName, Wrapper.class, phonesWrapper);
        marshaller.marshal(jaxbElement, new File(getPhonesFile().toURI()));
    }
}
