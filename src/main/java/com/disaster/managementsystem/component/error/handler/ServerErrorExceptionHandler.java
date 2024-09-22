package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.server.ServerErrorException;

import java.lang.reflect.Method;

@Slf4j
public class ServerErrorExceptionHandler extends AbstractApiExceptionHandler {

    public ServerErrorExceptionHandler(HttpStatusMapper httpStatusMapper,
                                       ErrorCodeMapper errorCodeMapper,
                                       ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ServerErrorException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        ServerErrorException ex = (ServerErrorException) exception;
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

        Method handlerMethod = ex.getHandlerMethod();
        if (handlerMethod != null) {
            response.getError().getProperties().put("methodName", handlerMethod.getName());
            response.getError().getProperties().put("methodClassName", handlerMethod.getDeclaringClass().getSimpleName());
        }

        return response;
    }

}
