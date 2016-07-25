package by.expertsoft.phone_shop.entity;


import javax.validation.Valid;
import java.util.List;

public class PhoneQuantityListWrapper {
    @Valid
    private List<PhoneQuantityWrapper> phonesQuantityList;

    public List<PhoneQuantityWrapper> getPhonesQuantityList() {
        return phonesQuantityList;
    }

    public void setPhonesQuantityList(List<PhoneQuantityWrapper> phonesQuantityList) {
        this.phonesQuantityList = phonesQuantityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneQuantityListWrapper)) return false;

        PhoneQuantityListWrapper that = (PhoneQuantityListWrapper) o;

        return phonesQuantityList != null ? phonesQuantityList.equals(that.phonesQuantityList) : that.phonesQuantityList == null;

    }

    @Override
    public int hashCode() {
        return phonesQuantityList != null ? phonesQuantityList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PhoneQuantityListWrapper{" +
                "phonesQuantityList=" + phonesQuantityList +
                '}';
    }
}
