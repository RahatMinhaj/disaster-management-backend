package com.disaster.managementsystem.component.error;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ApiFieldError;
import com.disaster.managementsystem.support.ApiGlobalError;
import com.disaster.managementsystem.support.ApiParameterError;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@JsonComponent
public class ApiErrorResponseSerializer extends JsonSerializer<ApiErrorResponseDto> {

    private final ErrorHandlingProperties properties;

    public ApiErrorResponseSerializer(ErrorHandlingProperties properties) {
        this.properties = properties;
    }

    @Override
    public void serialize(ApiErrorResponseDto errorResponse,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (properties.isHttpStatusInJsonResponse()) {
            jsonGenerator.writeNumberField("status", errorResponse.getStatus());
        }
        ErrorHandlingProperties.JsonFieldNames fieldNames = properties.getJsonFieldNames();
        jsonGenerator.writeStringField(fieldNames.getCode(), errorResponse.getError().getCode());
        jsonGenerator.writeStringField(fieldNames.getMessage(), errorResponse.getMessage());

        List<ApiFieldError> fieldErrors = errorResponse.getError().getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            jsonGenerator.writeArrayFieldStart(fieldNames.getFieldErrors());
            for (ApiFieldError fieldError : fieldErrors) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(fieldNames.getCode(), fieldError.getCode());
                jsonGenerator.writeStringField(fieldNames.getMessage(), fieldError.getMessage());
                jsonGenerator.writeStringField("property", fieldError.getProperty());
                jsonGenerator.writeObjectField("rejectedValue", fieldError.getRejectedValue());
                jsonGenerator.writeObjectField("path", fieldError.getPath());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }

        List<ApiGlobalError> globalErrors = errorResponse.getError().getGlobalErrors();
        if (!globalErrors.isEmpty()) {
            jsonGenerator.writeArrayFieldStart(fieldNames.getGlobalErrors());
            for (ApiGlobalError globalError : globalErrors) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(fieldNames.getCode(), globalError.getCode());
                jsonGenerator.writeStringField(fieldNames.getMessage(), globalError.getMessage());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }

        List<ApiParameterError> parameterErrors = errorResponse.getError().getParameterErrors();
        if (!parameterErrors.isEmpty()) {
            jsonGenerator.writeArrayFieldStart(fieldNames.getParameterErrors());
            for (ApiParameterError parameterError : parameterErrors) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(fieldNames.getCode(), parameterError.getCode());
                jsonGenerator.writeStringField(fieldNames.getMessage(), parameterError.getMessage());
                jsonGenerator.writeStringField("parameter", parameterError.getParameter());
                jsonGenerator.writeObjectField("rejectedValue", parameterError.getRejectedValue());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }

        Map<String, Object> properties = errorResponse.getError().getProperties();
        for (String property : properties.keySet()) {
            jsonGenerator.writeObjectField(property, properties.get(property));
        }

        jsonGenerator.writeEndObject();
    }
}
