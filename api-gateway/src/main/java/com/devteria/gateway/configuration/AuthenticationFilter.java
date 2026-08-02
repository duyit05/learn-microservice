package com.devteria.gateway.configuration;

import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final IdentityService identityService;
    private final ObjectMapper objectMapper;

    @Value("${app.api-prefix}")
    @NonFinal
    private String API_PREFIX;

    @NonFinal
    private String [] PUBLIC_API = {"/identity/auth/.*","/identity/users/registration"};

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter ......");
        // Lọc api public thì cho qua
        if(isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);


        // Bước 1: Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeader))
           return unauthenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token: {}", token);

        // Bước 2: Verify token -> delegate identity service
        return identityService.introspect(token).flatMap(introspect -> {
            // Bước 3: Tiếp tục filter nếu thành công
            if(introspect.getResult().isValid())
                return chain.filter(exchange);
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));

    }

    @Override
    public int getOrder() {
        // -1 để config chạy lên đầu
        return -1;
    }

    private boolean isPublicEndpoint (ServerHttpRequest request) {
        return Arrays.stream(PUBLIC_API).anyMatch(s ->
            request.getURI().getPath().matches(API_PREFIX + s));
    }

     private Mono<Void> unauthenticated (ServerHttpResponse response){
         ApiResponse<?> apiResponse = ApiResponse
                 .builder()
                 .code(1043)
                 .message("Unauthenticated")
                 .build();
        String body = null;

        try {
            body = objectMapper.writeValueAsString(apiResponse);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
