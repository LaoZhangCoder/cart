package weimob.cart.server.conf;

import cart.exception.ServiceException;
import cart.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle<T> {
    @ExceptionHandler(value = ServiceException.class)
    public Response<T> exceptionHandle(ServiceException e){
        Response<T> response = new Response<>();
        log.error(e.getMessage());
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()+"");
        response.setError(e.getMessage());
        response.setSuccess(false);
        return response;
    }
}
