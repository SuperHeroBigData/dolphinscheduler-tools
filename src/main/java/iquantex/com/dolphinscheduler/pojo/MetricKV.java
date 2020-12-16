package iquantex.com.dolphinscheduler.pojo;

/**
 * @author mujp
 */
public class MetricKV<k,v>{
    k key;
    v value;

    public MetricKV() {
    }

    public k getKey() {
        return key;
    }

    public void setKey(k key) {
        this.key = key;
    }

    public v getValue() {
        return value;
    }

    public void setValue(v value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MetricKV{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
/*    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        return buffer.append("{" +
                "key:" + key +
                ", value:" + value +
                '}').toString();
    }*/
}
