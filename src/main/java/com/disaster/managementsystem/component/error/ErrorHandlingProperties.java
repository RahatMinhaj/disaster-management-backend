package com.disaster.managementsystem.component.error;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ConfigurationProperties("error.handling")
@Component
@Primary
public class ErrorHandlingProperties {
    private boolean enabled = true;

    private JsonFieldNames jsonFieldNames = new JsonFieldNames();

    private ExceptionLogging exceptionLogging = ExceptionLogging.MESSAGE_ONLY;

    private List<Class<? extends Throwable>> fullStacktraceClasses = new ArrayList<>();

    private List<String> fullStacktraceHttpStatuses = new ArrayList<>();

    private Map<String, LogLevel> logLevels = new HashMap<>();

    private DefaultErrorCodeStrategy defaultErrorCodeStrategy = DefaultErrorCodeStrategy.ALL_CAPS;

    private boolean httpStatusInJsonResponse = false;

    private Map<String, HttpStatus> httpStatuses = new HashMap<>();

    private Map<String, String> codes = new HashMap<>();

    private Map<String, String> messages = new HashMap<>();

    private boolean addPathToError = true;

    private boolean searchSuperClassHierarchy = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setJsonFieldNames(JsonFieldNames jsonFieldNames) {
        this.jsonFieldNames = jsonFieldNames;
    }

    public void setExceptionLogging(ExceptionLogging exceptionLogging) {
        this.exceptionLogging = exceptionLogging;
    }

    public void setFullStacktraceClasses(List<Class<? extends Throwable>> fullStacktraceClasses) {
        this.fullStacktraceClasses = fullStacktraceClasses;
    }

    public void setFullStacktraceHttpStatuses(List<String> fullStacktraceHttpStatuses) {
        this.fullStacktraceHttpStatuses = fullStacktraceHttpStatuses;
    }

    public void setLogLevels(Map<String, LogLevel> logLevels) {
        this.logLevels = logLevels;
    }

    public void setDefaultErrorCodeStrategy(DefaultErrorCodeStrategy defaultErrorCodeStrategy) {
        this.defaultErrorCodeStrategy = defaultErrorCodeStrategy;
    }

    public void setHttpStatusInJsonResponse(boolean httpStatusInJsonResponse) {
        this.httpStatusInJsonResponse = httpStatusInJsonResponse;
    }

    public void setHttpStatuses(Map<String, HttpStatus> httpStatuses) {
        this.httpStatuses = httpStatuses;
    }

    public void setCodes(Map<String, String> codes) {
        this.codes = codes;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public void setSearchSuperClassHierarchy(boolean searchSuperClassHierarchy) {
        this.searchSuperClassHierarchy = searchSuperClassHierarchy;
    }

    public void setAddPathToError(boolean addPathToError) {
        this.addPathToError = addPathToError;
    }

    public enum ExceptionLogging {
        NO_LOGGING,
        MESSAGE_ONLY,
        WITH_STACKTRACE
    }

    public enum DefaultErrorCodeStrategy {
        FULL_QUALIFIED_NAME,
        ALL_CAPS
    }

    public static class JsonFieldNames {
        private String code = "code";
        private String message = "message";
        private String fieldErrors = "fieldErrors";
        private String globalErrors = "globalErrors";
        private String parameterErrors = "parameterErrors";

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(String fieldErrors) {
            this.fieldErrors = fieldErrors;
        }

        public String getGlobalErrors() {
            return globalErrors;
        }

        public void setGlobalErrors(String globalErrors) {
            this.globalErrors = globalErrors;
        }

        public String getParameterErrors() {
            return parameterErrors;
        }

        public void setParameterErrors(String parameterErrors) {
            this.parameterErrors = parameterErrors;
        }
    }
}
