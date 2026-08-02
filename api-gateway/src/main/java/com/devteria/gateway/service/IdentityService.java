package com.devteria.gateway.service;

import com.devteria.gateway.dto.request.IntrospectRequest;
import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.dto.response.IntrospectResponse;
import com.devteria.gateway.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityService {
    private final IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect (String token) {
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }
}
