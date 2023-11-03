/*
 * Automatic inheritance type resolver for Jackson.
 * @author root_talis<https://github.com/root-talis>
 *
 *
 * Usage:
 *
 * @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "discriminator")
 * @JsonTypeIdResolver(InheritanceTypeIdResolver.class)
 * class Animal {  // can be abstract or interface
 *    // ...
 * }
 *
 * // no additional annotations needed to parse json into this class
 * class Cat extends Animal {
 *    // ...
 * }
 *
 * // no additional annotations needed to parse json into this class
 * class Lion extends Cat {
 *    // ...
 * }
 *
 * // no additional annotations needed to parse json into this class
 * class Dog extends Animal {
 *    // ...
 * }
 *
 *
 * Maven dependency:
 *
 * <dependency>
 *    <groupId>org.reflections</groupId>
 *    <artifactId>reflections</artifactId>
 *    <version>0.9.9-RC1</version>
 * </dependency>
 *
 *
 *
 *
 *  see https://gist.github.com/root-talis/36355f227ff5bb7a057ff7ad842d37a3
 * and
 * https://stackoverflow.com/questions/30362446/deserialize-json-with-jackson-into-polymorphic-types-a-complete-example-is-giv
 * */

package org.schlunzis.kurtama.common.util;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class InheritanceTypeIdResolver implements TypeIdResolver {
    private final Map<String, JavaType> typeMap = new HashMap<>();
    private JavaType baseType;

    @Override
    public void init(JavaType javaType) {
        baseType = javaType;

        Class<?> clazz = baseType.getRawClass();

        Reflections reflections = new Reflections("org.schlunzis.kurtama.common"); // root package to scan for subclasses
        @SuppressWarnings("unchecked")
        Set<Class<?>> subtypes = (Set<Class<?>>) reflections.getSubTypesOf(clazz);

        int classModifiers = clazz.getModifiers();

        if (!Modifier.isAbstract(classModifiers) && !Modifier.isInterface(classModifiers)) {
            subtypes.add(clazz);
        }

        subtypes.forEach(type -> {
            String key = type.getSimpleName();

            if (typeMap.containsKey(key)) {
                throw new IllegalStateException("Type name \"" + key + "\" already exists.");
            }

            typeMap.put(type.getSimpleName(), TypeFactory.defaultInstance().constructSpecializedType(baseType, type));
        });
    }

    @Override
    public String idFromValue(Object o) {
        return idFromValueAndType(o, o.getClass());
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, baseType.getRawClass());
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        String name = aClass.getSimpleName();

        if (typeMap.containsKey(name)) {
            return typeMap.get(name).getRawClass().getSimpleName();
        }

        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String s) throws IOException {
        if (typeMap.containsKey(s)) {
            return typeMap.get(s);
        }

        throw new IOException("Cannot find class for type id \"" + s + "\"");
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}