package com.tripease.payment.interceptor;

import com.tripease.payment.config.GlobalSecurityStore;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {


//        @Override
//        public void apply(RequestTemplate template) {
//            log.info("Inside FeignClientInterceptor:: apply()");
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth != null) {
//                log.info("X-User-Id", auth.getName());
//                template.header("X-User-Id", auth.getName());
//                template.header("X-User-Roles", auth.getAuthorities().toString());
//                template.header("X-Internal-Secret", "my-gateway-secret-123");
//            }
//        }

    @Override
    public void apply(RequestTemplate template) {

        String[] authData = GlobalSecurityStore.store.get("authData");

        log.info("INTERCEPTOR THREAD: {}", Thread.currentThread().getName());

        log.info("Inside FeignClientInterceptor:: apply()");

            String userId = authData[0];
            String email = authData[1];
            String internalSecret = authData[2];


            log.info("Received Headers: X-Internal-Secret={}, X-User-Id={}, X-User-Email={}", internalSecret, userId, email);

            // 3. Propagate them to the OUTGOING Feign request
            if (userId != null) template.header("X-User-Id", userId);
            if (email != null) template.header("X-User-Email", email);
            if (internalSecret != null) template.header("X-Internal-Secret", internalSecret);
    }
}