package web.aop;

import cart.request.AbstractRequest;
import cart.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 * @Desciption 服务层异常统一处理
 */
@Component
@Aspect
@Slf4j
public class ControllerGlobalExceptionHandle {
    /**
     * 指定返回值为Response类型的Controller
     */
    @Pointcut(value = "execution(* web.controller..*.*(..))")
    private void controllerPointcut() {
        // Do nothing just pointcut
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        try {
            if (args != null && args.length > 0) {
                for (int var = 0; var < args.length; var++) {
                    if (pjp.getArgs()[var] instanceof AbstractRequest) {
                        AbstractRequest request = (AbstractRequest) pjp.getArgs()[var];
                        request.checkParam();
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            processException(pjp, args, e);
            return Response.fail(e.getMessage());
        }
        try {
            return pjp.proceed();
            // 业务自定义异常
        } catch (Throwable throwable) {
            processException(pjp, args, throwable);
            return Response.fail("系统异常");
        }
    }

    /**
     * 处理异常
     *
     * @param joinPoint 切点
     * @param args      参数
     * @param throwable 异常
     */
    private void processException(final ProceedingJoinPoint joinPoint, final Object[] args, Throwable throwable) {
        String inputParam = "";
        if (args != null && args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(",");
                sb.append(arg);
            }
            inputParam = sb.toString().substring(1);
        }
        log.warn("\n 方法:{} \n 入参: {} \n", joinPoint.toLongString(), inputParam);
        log.warn("错误信息:{}", throwable.getMessage());
    }

}
