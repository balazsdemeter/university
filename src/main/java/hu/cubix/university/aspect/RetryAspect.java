package hu.cubix.university.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Aspect
@Component
public class RetryAspect {
    private static final int MAX_RETRIES = 5;
    private static final long DELAY = 500;

    @Pointcut("@annotation(hu.cubix.university.aspect.Retryable) || @within(hu.cubix.university.aspect.Retryable)")
    public void pointCut() {}

    @Around("hu.cubix.university.aspect.RetryAspect.pointCut()")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                return joinPoint.proceed();
            } catch (TimeoutException e) {
                attempt++;
                System.out.format("Number of tries:%d", attempt);
                if (attempt >= MAX_RETRIES) {
                    System.out.format("Number of maximum retries has been reached: %d", attempt);
                    throw new TimeoutException("Timeout exception occurred.");
                }
                Thread.sleep(DELAY);
            }
        }
        return null;
    }
}
