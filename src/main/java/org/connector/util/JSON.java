package org.connector.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class JSON {

    private JSON(){}

    private static final ObjectMapper JACKSON = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
            .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            .configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true)
            .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
            .configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ObjectMapper JACKSON_NO_NULLS = new ObjectMapper().registerModule(new ParameterNamesModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule()).setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

    public static ObjectMapper getJackson() {
        return JACKSON;
    }

    public static ObjectMapper getJacksonNoNulls() {
        return JACKSON_NO_NULLS;
    }
    public static void setConfigure(MapperFeature f, boolean state){
        JACKSON.configure(f, state);
    }

    public static <T> T readValue(JsonParser parser, TypeReference<T> tr) throws Exception {
        return JACKSON.readValue(parser, tr);
    }

    public static <T> T readValue(byte[] bytes, Class<T> t) throws Exception {
        return JACKSON_NO_NULLS.readValue(bytes, t);
    }

    public static <T> T readValue(InputStream inputStream, Class<T> t) throws Exception {
        return JACKSON_NO_NULLS.readValue(inputStream, t);
    }

    public static <T> T readValue(byte[] bytes, Class<?> clazz, Class<?> genericType) throws Exception {
        JavaType jt = JACKSON_NO_NULLS.getTypeFactory().constructParametricType(clazz, genericType);

        return JACKSON_NO_NULLS.readValue(bytes, jt);
    }

    public static byte[] writeAsBytes(Object o) throws Exception {
        return JACKSON.writeValueAsBytes(o);
    }


    public static JsonNode readTree(String value) throws Exception {
        return JACKSON.readTree(value);
    }

    public static <T> T fromMap(Map value, Class<T> clazz) {
        try {
            return JACKSON.convertValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode getObjectNode(String json) throws Exception {
        return JACKSON.readValue(json, ObjectNode.class);
    }

    public static String getObjectFieldValue(String json, String field) throws Exception {
        ObjectNode node = getObjectNode(json);
        return node.get(field).asText();
    }

    public static <T> T fromJson(String value, Class<T> clazz) {
        try {
            return JACKSON.readValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String value, JavaType type) {
        try {
            return JACKSON.readValue(value, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonIgnoreNulls(String value, Class<T> clazz) {
        try {
            JACKSON_NO_NULLS.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return JACKSON_NO_NULLS.readValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param value raw json string
     * @param type  the list and its type
     * @param <T>   type of list
     * @return returned list of specific type
     * <p>
     * use this for deserializing a JSON array sting ex:
     * JSON.fromJson(req.body(), new TypeReference&lt;List&lt;MyClass&gt;&gt;(){});
     */
    public static <T> T fromJson(String value, TypeReference<T> type) {
        try {
            return JACKSON.readValue(value, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param filePath       filename with extension on cp or full system path to file
     * @param collectionType the type of collection set or list
     * @param elementType    the type of elements maintained by this collection
     * @return collection of specific type
     */
    public static <T extends Collection<E>, E> T collectionFromJsonFile(String filePath, Class<? extends Collection> collectionType, Class elementType) {
        try {
            URL fileUrl = getFileUrl(filePath, elementType);
            assert fileUrl != null : String.format("Could not find %s on classpath", filePath);
            return JACKSON.readValue(fileUrl, JACKSON.getTypeFactory().constructCollectionType(collectionType, elementType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CollectionType getCollectionType(Class<? extends Collection> collectionType, JavaType elementType){
        return JACKSON.getTypeFactory().constructCollectionType(collectionType, elementType);
    }

    public static CollectionType getCollectionType(Class<? extends Collection> collectionClass, Class<?> oClass) {
        return TypeFactory.defaultInstance().constructCollectionType(collectionClass, oClass);
    }

    public static <T extends Collection<E>, E> T collectionFromJsonFile(URL url, Class<? extends Collection> collectionType, Class elementType) {
        try {
            return JACKSON.readValue(url, JACKSON.getTypeFactory().constructCollectionType(collectionType, elementType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static URL getFileUrl(String filenameOrPath, Class<?> type) {
        URL result = null;
        try {
            if (filenameOrPath.contains("/") || filenameOrPath.contains("\\"))
                result = new URL(String.format("file:///%s", filenameOrPath));
            else
                result = type.getClassLoader().getResource(filenameOrPath);

            if (result == null)
                result = new URL(String.format("file:%s", filenameOrPath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T> T fromJsonFile(String filenameOnClasspath, Class<T> clazz) {
        try {
            URL fileUrl = getFileUrl(filenameOnClasspath, clazz);
            assert fileUrl != null : String.format("Could not find %s on classpath", filenameOnClasspath);
            return JACKSON.readValue(fileUrl, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonFileIgnoreUnknown(String filenameOnClasspath, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        try {
            URL fileUrl = getFileUrl(filenameOnClasspath, clazz);
            assert fileUrl != null : String.format("Could not find %s on classpath", filenameOnClasspath);
            return objectMapper.readValue(fileUrl, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonFileIgnoreUnknown(URL url, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        try {
            return objectMapper.readValue(url, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jsonString     jsonString to deserialize
     * @param collectionType the type of collection set or list
     * @param elementType    the type of elements maintained by this collection
     * @return collection of specific type
     */
    public static <T extends Collection<E>, E> T collectionFromJson(String jsonString, Class<? extends Collection> collectionType, Class elementType) {
        try {
            return JACKSON.readValue(jsonString, JACKSON.getTypeFactory().constructCollectionType(collectionType, elementType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param value raw json string
     * @param type  the list and its type
     * @param <T>   type of list
     * @return returned list of specific type
     * <p>
     * use this for deserializing a JSON array sting ex:
     * JSON.fromJson(req.body(), new ypeReference&lt;List&lt;MyClass&gt;&gt;(){});
     */
    public static <T> T fromJsonIgnoreUnknown(String value, TypeReference<T> type) {
        try {
            return JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        }
    }

    public static String toJson(Object o) {
        try {
            return JACKSON.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Collection<E>, E> T deepCopyCollection(T o, Class<? extends Collection> collectionType, Class elementType) {
        try {
            String s = JACKSON.writeValueAsString(o);

            return JACKSON.readValue(s, JACKSON.getTypeFactory().constructCollectionType(collectionType, elementType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJsonBytes(Object o) {
        String jsonString = toJson(o);

        return jsonString.getBytes(StandardCharsets.UTF_8);
    }

    public static String toJsonNoNulls(Object o) {
        try {
            return JACKSON_NO_NULLS.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String value, MapType type) {
        try {
            return JACKSON.readValue(value, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonIgnoreModel(String value, Class<T> clazz) {
        try {
            if (clazz.isAssignableFrom(Map.class))
                return fromJson(value, getMapType((Class<? extends Map>) clazz, String.class, String.class));
            else
                return JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        }
    }

    public static <T> T fromJsonIgnoreModel(String value, TypeReference<T> type) {
        try {
            return JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        }
    }

    public static <T> T fromJson(String value, CollectionType type) {
        try {
            return JACKSON.readValue(value, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getNode(Map<?,?> map, Class<T> clazz, String... path) {
        var node = map;
        for (int i = 0; i < path.length - 1; i++) {
            node = (Map) node.get(path[i]);
        }
        return (T) node.get(path[path.length - 1]);
    }

    public static JavaType getParameterizedType (Class<?> targetType, Class<?> parameterType) {
        return TypeFactory.defaultInstance().constructParametricType(targetType, parameterType);
    }

    public static JavaType getParameterizedType (Class<?> targetType, JavaType parameterType) {
        return TypeFactory.defaultInstance().constructParametricType(targetType, parameterType);
    }

    public static MapType getMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return TypeFactory.defaultInstance().constructMapType(mapClass, keyClass, valueClass);
    }

    public static String prettyPrint(Object in) {
        return prettyPrint(toJsonNoNulls(in));
    }


    public static String prettyPrint(String json) {
        String output = json;
        try {
            output = JACKSON_NO_NULLS.writerWithDefaultPrettyPrinter().writeValueAsString(JACKSON_NO_NULLS.readValue(json, Object.class));
        } catch (Exception ignore) {
        }
        return output;
    }

    public static void setConfigure(DeserializationFeature failOnUnknownProperties, boolean state) {
        JACKSON.configure(failOnUnknownProperties, state);
    }

    public <T, P, O> T read(String json, Class<T> tType, Class<P> pType, Class<O> oType) throws IOException {
        JavaType type = JACKSON.getTypeFactory().constructParametricType(tType, pType, oType);
        return JACKSON.readValue(json, type);
    }

    private static final TypeReference<Map<String, String>> STRING_TO_STRING_MAP_KEY = new TypeReference<>() {
    };

    private static final TypeReference<Map<String, Object>> STRING_TO_OBJECT_MAP_KEY = new TypeReference<>() {
    };

    public static String minifyJsonString(String jsonString) throws JsonProcessingException {
        var node = convertToJsonNode(jsonString);
        return node.toString();
    }

    public static <S> Map<String, String> convertToStringMap(final S sourceValue) {
        return JACKSON.convertValue(sourceValue, STRING_TO_STRING_MAP_KEY);
    }

    public static <S> Map<String, Object> convertToObjectMap(final S sourceValue) {
        return JACKSON.convertValue(sourceValue, STRING_TO_OBJECT_MAP_KEY);
    }

    public static Map<String, Object> convertToObjectMap(final String json) throws JsonProcessingException {
        return convertToObjectMap(JACKSON.readTree(json));
    }

    public static JsonNode convertToJsonNode(final String json) throws JsonProcessingException {
        return JACKSON.readTree(json);
    }

    public static String convertToString(final Object value) throws JsonProcessingException {
        return JACKSON.writeValueAsString(value);
    }

    private static class DateModule extends SimpleModule {
        DateModule() {
            addSerializer(Date.class, new DateSerializer());
        }
    }

    private static class DoubleModule extends SimpleModule {
        DoubleModule() {
            addSerializer(Double.class, new DoubleSerializer());
        }
    }

    private static class DoubleSerializer extends JsonSerializer<Double> {
        private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####################0.0######");
        @Override
        public void serialize(final Double value,
                              final JsonGenerator gen,
                              final SerializerProvider serializers) throws IOException {
            final String formattedDouble = DECIMAL_FORMAT.format(value);
            gen.writeNumber(Double.parseDouble(formattedDouble));
        }
    }

}