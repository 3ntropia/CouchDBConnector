package org.connector.dao.query;

import org.connector.model.Document;

import java.util.List;

public class PersonEntityMock extends Document {

    private List<String> names;

    private List<String> addresses;

    private long longNumber;

    private int telephoneNumber;

    public PersonEntityMock(List<String> names, List<String> addresses, long longNumber, int telephoneNumber) {
        this.names = names;
        this.addresses = addresses;
        this.longNumber = longNumber;
        this.telephoneNumber = telephoneNumber;
    }

    public static class AddressEntityMock extends Document {

        private String street;

        private int CP;

        public AddressEntityMock(String street, int CP) {
            this.street = street;
            this.CP = CP;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public int getCP() {
            return CP;
        }

        public void setCP(int CP) {
            this.CP = CP;
        }
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public long getLongNumber() {
        return longNumber;
    }

    public void setLongNumber(long longNumber) {
        this.longNumber = longNumber;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
