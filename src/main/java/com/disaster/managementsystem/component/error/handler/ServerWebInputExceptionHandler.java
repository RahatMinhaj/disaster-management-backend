package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

public class ServerWebInputExceptionHandler extends AbstractApiExceptionHandler {
    public ServerWebInputExceptionHandler(HttpStatusMapper httpStatusMapper,
                                          ErrorCodeMapper errorCodeMapper,
                                          ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ServerWebInputException
                // WebExchangeBindException should be handled by BindApiExceptionHandler
                && !(exception instanceof WebExchangeBindException);
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        ServerWebInputException ex = (ServerWebInputException) exception;
        HttpStatusCode status = ex.getStatusCode();
        ApiErrorResponseDto response =
                ApiErrorResponseDto.builder()
                        .status(ex.getStatusCode().value())
                        .message(ex.getMessage())
                        .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                        .build();
        MethodParameter methodParameter = ex.getMethodParameter();
        if (methodParameter != null) {
            response.getError().getProperties().put("parameterName", methodParameter.getParameterName());
            response.getError().getProperties().put("parameterType", methodParameter.getParameterType().getSimpleName());
        }
        return response;
    }
}
