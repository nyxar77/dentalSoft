package ma.dentalSoft.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import ma.dentalSoft.domain.Patient;

/**
 * Centralise les fonctions qui transforment une ligne SQL en objet métier.
 */
public final class RowMappers {

    private RowMappers() {
    }

    public static Patient toPatient(ResultSet resultSet) throws SQLException {
        return new Patient(
                resultSet.getLong("id_patient"),
                resultSet.getString("nom"),
                resultSet.getInt("age")
        );
    }
}
