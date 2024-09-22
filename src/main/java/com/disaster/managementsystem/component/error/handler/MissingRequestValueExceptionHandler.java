package com.disaster.managementsystem.component.error.handler;


import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.*;

public class MissingRequestValueExceptionHandler extends AbstractApiExceptionHandler {
    public MissingRequestValueExceptionHandler(HttpStatusMapper httpStatusMapper,
                                               ErrorCodeMapper errorCodeMapper,
                                               ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof MissingRequestValueException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {

        ApiErrorResponseDto response = ApiErrorResponseDto.builder()
                .status(getHttpStatus(exception).value())
                .message(getErrorMessage(exception))
                .build();

        if (exception instanceof MissingMatrixVariableException) {
            response.getError().getProperties().put("variableName", ((MissingMatrixVariableException) exception).getVariableName());
            addParameterInfo(response, ((MissingMatrixVariableException) exception).getParameter());
        } else if (exception instanceof MissingPathVariableException) {
            response.getError().getProperties().put("variableName", ((MissingPathVariableException) exception).getVariableName());
            addParameterInfo(response, ((MissingPathVariableException) exception).getParameter());
        } else if (exception instanceof MissingRequestCookieException) {
            response.getError().getProperties().put("cookieName", ((MissingRequestCookieException) exception).getCookieName());
            addParameterInfo(response, ((MissingRequestCookieException) exception).getParameter());
        } else if (exception instanceof MissingRequestHeaderException) {
            response.getError().getProperties().put("headerName", ((MissingRequestHeaderException) exception).getHeaderName());
            addParameterInfo(response, ((MissingRequestHeaderException) exception).getParameter());
        } else if (exception instanceof MissingServletRequestParameterException) {
            String parameterName = ((MissingServletRequestParameterException) exception).getParameterName();
            String parameterType = ((MissingServletRequestParameterException) exception).getParameterType();
            response.getError().getProperties().put("parameterName", parameterName);
            response.getError().getProperties().put("parameterType", parameterType);
        }


        return response;
    }

    private void addParameterInfo(ApiErrorResponseDto response, MethodParameter parameter) {
        response.getError().getProperties().put("parameterName", parameter.getParameterName());
        response.getError().getProperties().put("parameterType", parameter.getParameterType().getSimpleName());
    }


    private HttpStatus getHttpStatus(Throwable exception) {
        if (exception instanceof MissingPathVariableException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.BAD_REQUEST;
    }
}