package com.tui.github.repository.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


@Component
@Order(-1)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof NotAcceptableStatusException notAcceptableStatusException) {
            return writeResponse(exchange, notAcceptableStatusException.getReason(), HttpStatus.NOT_ACCEPTABLE);
        } else if (ex instanceof NotFoundException) {
            return writeResponse(exchange, ex.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return Mono.error(ex);
        }
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) throws JsonProcessingException {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().writeWith(
                Mono.just(
                        exchange.getResponse().bufferFactory().wrap(
                                generateFailureResponseJson(errorMessage, httpStatus).getBytes()
                        )
                )
        );
    }

    private String generateFailureResponseJson(final String message, final HttpStatus httpStatus) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new ErrorResponse()
                .status(httpStatus.value())
                .message(message));
    }
}