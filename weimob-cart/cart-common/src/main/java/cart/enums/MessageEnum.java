package cart.enums;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
public enum MessageEnum {
    /**
     * 操作成功
     */
    OPERATION_SUCCESS(200, "the operation is success!"),
    /**
     * 操作失败
     */
    OPERATION_FAIL(500, "the operation is fail!");
    private int value;
    private String reasonPhrase;


    MessageEnum(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }
}
