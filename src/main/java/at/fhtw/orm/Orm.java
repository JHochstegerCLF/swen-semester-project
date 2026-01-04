package at.fhtw.orm;

import at.fhtw.converter.JsonConverter;
import at.fhtw.persistence.DBConnector;
import com.google.inject.Inject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Orm<T> {
    private final DBConnector connector;
    private final Class<T> clazz;
    private final String className;
    private final String fields;
    private final String insertFields;
    private final JsonConverter<T> jsonConverter;

    @Inject
    public Orm(Class<T> clazz, DBConnector connector) {
        this.clazz = clazz;
        this.connector = connector;
        this.jsonConverter = new JsonConverter<>(clazz);
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class is not an entity");
        }
        String tableName;
        if (!clazz.getAnnotation(Entity.class).name().isEmpty()) {
            tableName = clazz.getAnnotation(Entity.class).name();
        } else {
            tableName = clazz.getSimpleName().toLowerCase();
        }
        this.className = "\"" + tableName + "\"";

        fields = Arrays.stream(clazz.getDeclaredFields()).map(f -> {
            String name = f.getName();
            if (f.isAnnotationPresent(Param.class)) {
                name = f.getAnnotation(Param.class).name();
            }
            return "\"" + name + "\"";
        }).collect(Collectors.joining(","));

        insertFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Key.class))
                .map(f -> {
                    String name = f.getName();
                    if (f.isAnnotationPresent(Param.class)) {
                        name = f.getAnnotation(Param.class).name();
                    }
                    return "\"" + name + "\"";
                }).collect(Collectors.joining(","));
    }


    public List<T> getAll() {
        try (Connection connection = connector.getConnection();
             ResultSet dbResult = connection.prepareStatement("SELECT row_to_json(t) FROM " + className + " t").executeQuery()) {
            List<T> result = new ArrayList<>();
            while (dbResult.next()) {
                String jsonResult = dbResult.getString(1);
                T instance = this.jsonConverter.deserialize(jsonResult);
                result.add(instance);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<T> getByField(String field, Object value) {
        Optional<Field> optionalField = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.getName().equals(field)).findFirst();
        if (optionalField.isEmpty()) {
            throw new IllegalArgumentException("Field \"" + field + "\" does not exist");
        }
        Field f = optionalField.get();
        if (Collection.class.isAssignableFrom(f.getType()) || f.getType().isArray()) { // To prevent problems with arrays in that implementation
            throw new IllegalArgumentException("Field \"" + field + "\" is a collection or array");
        }
        String sql = "";
        if (f.isAnnotationPresent(Param.class)) {
            sql = "SELECT row_to_json(t) FROM " + className + " t WHERE t.\"" + f.getAnnotation(Param.class).name() + "\" = ?";
        } else {
            sql = "SELECT row_to_json(t) FROM " + className + " t WHERE t.\"" + f.getName() + "\" = ?";
        }
        try (Connection connection = connector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, value);
            try (ResultSet dbResult = stmt.executeQuery()) {
                ArrayList<T> result = new ArrayList<>();
                while (dbResult.next()) {
                    String jsonResult = dbResult.getString(1);
                    T instance = this.jsonConverter.deserialize(jsonResult);
                    result.add(instance);
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public T getById(int id) {
        String sql = "SELECT row_to_json(t) FROM " + className + " t WHERE id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet dbResult = stmt.executeQuery()) {
                if (dbResult.next()) {
                    String jsonResult = dbResult.getString(1);
                    return this.jsonConverter.deserialize(jsonResult);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int persistEntity(T entity) {
        String jsonString = jsonConverter.serialize(entity);
        String sql = "INSERT INTO " + className + " (" + insertFields + ") SELECT " + insertFields + "FROM json_populate_record(NULL::" + className + ", ?::json) ON CONFLICT DO NOTHING RETURNING \"id\"";

        try (Connection connection = connector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Pass the JSON string. Postgres does the parsing and array conversion.
            stmt.setObject(1, jsonString);

            // 3. Execute and get the generated ID
            try (ResultSet dbResult = stmt.executeQuery()) {
                if (dbResult.next()) {
                    return dbResult.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public T update(int id, T entity) {
        String jsonString = jsonConverter.serialize(entity);
        String sql = "UPDATE " + className + " t SET (" + insertFields + ") = (SELECT " + insertFields + "FROM json_populate_record(NULL::" + className + ", ?::json)) WHERE id = ? RETURNING row_to_json(t)";

        try (Connection connection = connector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Pass the JSON string. Postgres does the parsing and array conversion.
            stmt.setObject(1, jsonString);
            stmt.setInt(2, id);


            // 3. Execute and get the generated ID
            try (ResultSet dbResult = stmt.executeQuery()) {
                if (dbResult.next()) {
                    String jsonResult = dbResult.getString(1);
                    return this.jsonConverter.deserialize(jsonResult);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + className + " WHERE id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                // Optional: Throw an exception if the ID didn't exist
                throw new SQLException("Delete failed: No media found with ID " + id);
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
