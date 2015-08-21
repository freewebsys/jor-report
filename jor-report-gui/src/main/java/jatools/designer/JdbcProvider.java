package jatools.designer;


import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.data.reader.sql.Connection;



public class JdbcProvider extends Connection {
    String name;

    public JdbcProvider() {
    }

    public JdbcProvider(Connection conn) {
        setDriver(conn.getDriver());
        setUrl(conn.getUrl());
        setUser(conn.getUser());
        setPassword(conn.getPassword());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            new PropertyDescriptor(ComponentConstants.PROPERTY_DRIVER, String.class),
            

            //
            new PropertyDescriptor(ComponentConstants.PROPERTY_USER, String.class),
            

            //
            new PropertyDescriptor(ComponentConstants.PROPERTY_PASSWORD, String.class),
            

            //
            new PropertyDescriptor(ComponentConstants.PROPERTY_URL, String.class)
        };
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object o) {
        boolean eq = super.equals(o);

        if (eq) {
            JdbcProvider that = (JdbcProvider) o;
            Object thisValue = getName();
            Object thatValue = that.getName();

            if (!((thisValue == null) ? (thatValue == null) : thisValue.equals(thatValue))) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
