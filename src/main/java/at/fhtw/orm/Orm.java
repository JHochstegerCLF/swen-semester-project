package at.fhtw.orm;

import at.fhtw.persistence.DBConnector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Orm<T extends Object> {
    private final DBConnector connector;
    private final Class<T> clazz;
    private final String className;
    private final String fields;

    public Orm(Class<T> clazz, DBConnector connector) {
        this.clazz = clazz;
        this.connector = connector;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class is not an entity");
        }
        if (!clazz.getAnnotation(Entity.class).name().isEmpty()) {
            this.className = clazz.getAnnotation(Entity.class).name().toLowerCase();
        } else {
            this.className = clazz.getSimpleName().toLowerCase();
        }
        fields = Arrays.stream(clazz.getDeclaredFields()).map(f -> {
            if (f.isAnnotationPresent(Param.class)) {
                return f.getAnnotation(Param.class).name().toLowerCase();
            } else {
                return f.getName().toLowerCase();
            }
        }).collect(Collectors.joining(","));
    }


    public ArrayList<T> getAll() throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ResultSet results = connector.sendQuery("SELECT (" + fields + ") FROM " + className);
        ArrayList<T> result = new ArrayList<T>();
        while (results.next()) {
            T instance = clazz.getConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                clazz.getMethod("set" + field.getName(), field.getType()).invoke(instance, results.getString(field.getName()));
            }
            result.add(instance);
        }
        return result;
    }

//    public T getByName(String name) {
//        connector.sendQuery()
//        return ""; //TODO Find out how to dynamically create methods
//    }
//
//    public T getById(int id) {
//        return "SELECT (" + fields + ") FROM " + className + "WHERE id = ?";
//    }
//
//    public T persistEntity(T entity) {
//        return "INSERT (" + fields + ") VALUES (" + entity.toString() + ")"; //TODO Parse entity correct
//    }
//
//    public T update(int id, T entity) {
//        return "";
//    }
//
//    public T delete(int id) {
//        return "";
//    }
}
