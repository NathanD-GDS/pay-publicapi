package uk.gov.pay.api.exception.mapper;

import com.google.common.base.CaseFormat;
import io.dropwizard.jersey.validation.JerseyViolationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.pay.api.model.RequestError;

import javax.annotation.Priority;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static uk.gov.pay.api.model.RequestError.Code.CREATE_PAYMENT_MISSING_FIELD_ERROR;
import static uk.gov.pay.api.model.RequestError.Code.CREATE_PAYMENT_VALIDATION_ERROR;
import static uk.gov.pay.api.model.RequestError.aRequestError;

@Priority(1)
public class ViolationExceptionMapper implements ExceptionMapper<JerseyViolationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViolationExceptionMapper.class);

    @Override
    public Response toResponse(JerseyViolationException exception) {
        LOGGER.info(exception.getMessage());
        ConstraintViolation<?> firstException = exception.getConstraintViolations().iterator().next();
        String fieldName = getApiFieldName(firstException.getPropertyPath());
        
        RequestError requestError;
        if (firstException.getConstraintDescriptor() != null &&
                firstException.getConstraintDescriptor().getAnnotation() != null &&
                firstException.getConstraintDescriptor().getAnnotation().annotationType() == NotBlank.class ||
                firstException.getConstraintDescriptor().getAnnotation().annotationType() == NotEmpty.class ||
                firstException.getConstraintDescriptor().getAnnotation().annotationType() == NotNull.class) {
            requestError = aRequestError(fieldName, CREATE_PAYMENT_MISSING_FIELD_ERROR);
        } else {
            requestError = aRequestError(fieldName, CREATE_PAYMENT_VALIDATION_ERROR, StringUtils.capitalize(firstException.getMessage()));
        }
        
        return Response.status(422)
                .entity(requestError)
                .build();
    }

    private String getApiFieldName(Path path) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getFieldNameFromPath(path));
    }
    
    private String getFieldNameFromPath(Path path) {
        String[] pathParts = path.toString().split("\\.");
        return pathParts[pathParts.length - 1];
    }
}

