package resilience4j.micronaut.demo.service;

import io.github.resilience4j.micronaut.annotation.Retry;
import io.micronaut.context.annotation.Executable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Singleton
public class RetryService {
    private static final Logger logger = LoggerFactory.getLogger(RetryService.class);

    private static final String BACKEND_A = "backendA";

    @Retry(name = BACKEND_A, fallbackMethod = "singleFallback")
    public Single<String> singleTimeout() {
        return Single.error(() -> {
            logger.info("throwing exception");
            return new IOException();
        });
    }


    @Retry(name = BACKEND_A, fallbackMethod = "flowableFallback")
    public Flowable<String> flowableTimeout() {
        return Flowable.error(() -> {
            logger.info("throwing exception");
            return new IOException();
        });
    }

    @Retry(name = BACKEND_A, fallbackMethod = "fallback")
    public String timeout() throws IOException {
        Try.run(() -> Thread.sleep(5000));
        logger.info("throwing exception");
        throw new IOException("exception");
    }

    @Executable
    public String fallback() {

        logger.info("calling from recovery");
        return "Recovered HttpServerErrorException";
    }

    @Executable
    public CompletableFuture<String> futureFallback() {
        return CompletableFuture.completedFuture("Recovered specific TimeoutException");
    }

    @Executable
    public Single<String> singleFallback() {
        return Single.fromCallable(() -> {
            logger.info("calling from recovery");
            return "Recovered";
        });
    }

    @Executable
    public Flowable<String> flowableFallback() {
        return Flowable.fromCallable(() -> {
            logger.info("calling from recovery");
            return "Recovered";
        });
    }
}
