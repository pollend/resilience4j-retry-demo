package resilience4j.micronaut.demo.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Flowable;
import io.reactivex.Single;
import resilience4j.micronaut.demo.service.RetryService;

import javax.inject.Inject;
import java.io.IOException;

@Controller("/retry")
public class RetryController {

    private final RetryService retry;

    @Inject
    public RetryController(RetryService retryService) {
        this.retry = retryService;
    }

    @Get(value = "singleTimeout", produces = MediaType.TEXT_PLAIN)
    public Single<String> singleTimeout() {
        return retry.singleTimeout();
    }

    @Get(value = "flowableTimeout", produces = MediaType.TEXT_PLAIN)
    public Flowable<String> flowableTimeout() {
        return retry.flowableTimeout();
    }

    @Get(value = "timeout", produces = MediaType.TEXT_PLAIN)
    public String timeout() throws IOException {
        return retry.timeout();
    }


}
