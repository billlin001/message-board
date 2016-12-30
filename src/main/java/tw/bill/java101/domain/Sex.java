package tw.bill.java101.domain;

/**
 * Created by bill33 on 2016/3/28.
 */
public enum Sex {
    MALE("MALE"),
    FEMALE("FEMALE");

    public static final Sex[] ALL = {MALE, FEMALE};

    private final String name;

    public static Sex forName(final String name) {
        switch (name) {
            case "MALE":
                return  MALE;
            case "FEMALE":
                return FEMALE;
            default:
                throw new IllegalArgumentException("Name not define");
        }
    }


    private Sex(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
