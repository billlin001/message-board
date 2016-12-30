package tw.bill.java101.domain;

/**
 * Created by bill33 on 2016/3/28.
 */
public enum Degree {
    DOCTOR("DOCTOR"),
    MASTER("MASTER"),
    COLLEGE("COLLEGE"),
    HIGH_SCHOOL("HIGH_SCHOOL");

    public static final Degree[] ALL = {DOCTOR, MASTER, COLLEGE, HIGH_SCHOOL};

    private final String name;

    public static Degree forName(final String name) {
        switch (name) {
            case "DOCTOR":
                return DOCTOR;
            case "MASTER":
                return MASTER;
            case "COLLEGE":
                return COLLEGE;
            case "HIGH_SCHOOL":
                return HIGH_SCHOOL;
            default:
                throw new IllegalArgumentException("Name not define");
        }
    }


    private Degree(final String name) {
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
