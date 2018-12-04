package cn.mealkey.mkservice.node.model;

public class NodeAction {
    public static final String TYPE_ADD = "add";
    public static final String TYPE_MODIFY = "modify";
    public static final String TYPE_INCREASING = "increasing";
    public static final String TYPE_DECREASING = "decreasing";
    public String type;
    public String key;
    public String field;
    public Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "NodeAction{" +
                "type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", field='" + field + '\'' +
                ", value=" + value +
                '}';
    }
}
