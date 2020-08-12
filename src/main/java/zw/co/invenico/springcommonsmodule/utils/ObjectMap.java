package zw.co.invenico.springcommonsmodule.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import zw.co.invenico.springcommonsmodule.exception.InvalidRequestException;

@Slf4j
public class ObjectMap {

    public static <T, U> U mapToEntity(T input, Class<U> mappedClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(input, mappedClass);
    }

    public static <T, U> U updateEntity(U mappedClass, T input) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.SKIP));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.updateValue(mappedClass, input);

        } catch (JsonMappingException e) {
            log.error("### fail to map object due to {}", e.getMessage());
            e.printStackTrace();
            throw new InvalidRequestException("Fail to map object due to " + e.getMessage());
        }
    }


}