package ozpasyazilim.utils.datatypes;

public class FiPairStr {

    private String key;
    private String value;

    public FiPairStr(String key, String value) {
        setKey(key);
        setValue(value);
    }

    // Getter and Setter

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
