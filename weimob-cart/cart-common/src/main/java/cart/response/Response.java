package cart.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */
 public class Response<T> implements Serializable {
    private static final long serialVersionUID = -750644833749014618L;
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private boolean success;
    private T result;
    private String code;
    private String error;
    private String sourceIp;
    private String sourceStack;

    public Response() {
    }
    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public boolean errorOf(String codeToCompare) {
        return Objects.equals(this.code, codeToCompare);
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getSourceIp() {
        return this.sourceIp;
    }

    public void setSourceStack(String sourceStack) {
        this.sourceStack = sourceStack;
    }

    public String getSourceStack() {
        return this.sourceStack;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.success = true;
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.success = false;
        this.error = error;
    }

    public void setError(String code, String error) {
        this.success = false;
        this.code = code;
        this.error = error;
    }

    public void setError(String code, String error, String sourceStack) {
        this.success = false;
        this.code = code;
        this.error = error;
        this.sourceStack = sourceStack;
    }


    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response();
        resp.setError(error);
        return resp;
    }

    public static <T> Response<T> get(Supplier<T> supplier, String errorCode) {
        try {
            T result = supplier.get();
            return ok(result);
        } catch (Exception var3) {
            logger.error("error when get web.response call", var3);
            return fail(errorCode);
        }
    }

    public static <T> Response<T> fail(String code, String error) {
        Response<T> resp = new Response();
        resp.setError(code, error);
        return resp;
    }

    public static <T> Response<T> fail(String code, String error, String sourceStack) {
        Response<T> resp = new Response();
        resp.setError(code, error, sourceStack);
        return resp;
    }

    public Response<T> code(String code) {
        this.code = code;
        return this;
    }

    public Response<T> sourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
        return this;
    }

    public Response<T> sourceStack(String sourceStack) {
        this.sourceStack = sourceStack;
        return this;
    }
}
