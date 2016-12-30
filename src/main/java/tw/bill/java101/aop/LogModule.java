package tw.bill.java101.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by bill33 on 2016/2/11.
 */

@Aspect
@Component
public class LogModule {
    private Logger log = LoggerFactory.getLogger(LogModule.class);

    public LogModule() {
        log = LoggerFactory.getLogger(getClass());
    }

    @Pointcut("execution(* tw.bill.java101.service.*.*(..))")
    private void logServiceLayer() {}

    @Pointcut("execution(* tw.bill.java101.web.*.*(..))")
    private void logControllerLayer() {}

    @Before("logServiceLayer()")
    public void beforeServiceAdvice(JoinPoint joinPoint) {
        log.info("before enter service, {}...", joinPoint.getSignature().toShortString());
        log.info("arg size => {}", joinPoint.getArgs().length);
        log.info("signature => {}", joinPoint.getSignature().toLongString());
        Arrays.stream(joinPoint.getArgs()).forEach(item -> {log.info("parameter => {}", item.toString());});
    }

    @After("logServiceLayer()")
    public void afterServiceAdvice(JoinPoint joinPoint) {
        log.info("after leave service, {}...", joinPoint.getSignature().toShortString());
    }

    @Before("logControllerLayer()")
    public void beforeControllerAdvice(JoinPoint joinPoint) {
        log.info("before enter controller, {}...", joinPoint.getSignature().toShortString());
    }

    @After("logControllerLayer()")
    public void afterControllerAdvice(JoinPoint joinPoint) {
        log.info("after leave controller, {}...", joinPoint.getSignature().toShortString());
    }
}

