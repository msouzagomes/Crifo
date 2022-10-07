package br.com.calcred.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@With
@JsonDeserialize(builder = Error.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class Error {

    String field;
    String message;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {

    }
}
