package br.com.houseseeker.configuration;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GraphQLExceptionResolverConfiguration extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(
            @NotNull Throwable throwable,
            @NotNull DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return switch (throwable) {
            case ConstraintViolationException e -> resolveFrom(e, dataFetchingEnvironment);
            case ResponseStatusException e -> resolveFrom(e, dataFetchingEnvironment);
            case StatusRuntimeException e -> resolveFrom(e, dataFetchingEnvironment);
            default -> super.resolveToSingleError(throwable, dataFetchingEnvironment);
        };
    }

    private GraphQLError resolveFrom(
            ConstraintViolationException constraintViolationException,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return GraphqlErrorBuilder.newError()
                                  .errorType(ErrorType.ValidationError)
                                  .message(constraintViolationException.getMessage())
                                  .path(dataFetchingEnvironment.getExecutionStepInfo().getPath())
                                  .location(dataFetchingEnvironment.getField().getSourceLocation())
                                  .build();
    }

    private GraphQLError resolveFrom(
            ResponseStatusException responseStatusException,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return GraphQLError.newError()
                           .errorType(ErrorType.DataFetchingException)
                           .message(responseStatusException.getMessage())
                           .path(dataFetchingEnvironment.getExecutionStepInfo().getPath())
                           .location(dataFetchingEnvironment.getField().getSourceLocation())
                           .build();
    }

    private GraphQLError resolveFrom(
            StatusRuntimeException statusRuntimeException,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return GraphQLError.newError()
                           .errorType(ErrorType.DataFetchingException)
                           .message(statusRuntimeException.getMessage())
                           .path(dataFetchingEnvironment.getExecutionStepInfo().getPath())
                           .location(dataFetchingEnvironment.getField().getSourceLocation())
                           .build();
    }

}
