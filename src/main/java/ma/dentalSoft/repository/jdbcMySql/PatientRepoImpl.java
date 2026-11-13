package ma.dentalSoft.repository.jdbcMySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ma.dentalSoft.domain.Patient;
import ma.dentalSoft.exceptions.RepositoryException;
import ma.dentalSoft.repository.api.PatientRepo;
import ma.dentalSoft.repository.jdbc.JdbcUtil;
import ma.dentalSoft.repository.jdbc.RowMappers;

/**
 * Implémentation JDBC/MySQL du repository des patients.
 */
public class PatientRepoImpl implements PatientRepo {

    private static final String SELECT_COLUMNS = "id_patient, nom, age";
    private final Connection connection;

    public PatientRepoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Patient> findAll() {
        var sql = "SELECT " + SELECT_COLUMNS + " FROM patients ORDER BY id_patient";
        var patients = new ArrayList<Patient>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = JdbcUtil.prepareStatement(connection, sql, false);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                patients.add(RowMappers.toPatient(resultSet));
            }
            return patients;
        } catch (SQLException e) {
            throw JdbcUtil.dataAccessException("Erreur pendant la lecture des patients", e);
        } finally {
            JdbcUtil.closeResource(resultSet);
            JdbcUtil.closeResource(statement);
        }
    }

    @Override
    public Patient findById(Long identity) {
        if (identity == null) {
            return null;
        }

        var sql = "SELECT " + SELECT_COLUMNS + " FROM patients WHERE id_patient = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = JdbcUtil.prepareStatement(connection, sql, false, identity);
            resultSet = statement.executeQuery();
            return resultSet.next() ? RowMappers.toPatient(resultSet) : null;
        } catch (SQLException e) {
            throw JdbcUtil.dataAccessException("Erreur pendant la recherche du patient " + identity, e);
        } finally {
            JdbcUtil.closeResource(resultSet);
            JdbcUtil.closeResource(statement);
        }
    }

    @Override
    public Patient save(Patient newElement) {
        if (newElement == null) {
            throw new IllegalArgumentException("Le patient à enregistrer ne peut pas être null");
        }

        var sql = "INSERT INTO patients (nom, age) VALUES (?, ?)";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            statement = JdbcUtil.prepareStatement(
                    connection,
                    sql,
                    true,
                    newElement.getNom(),
                    newElement.getAge()
            );
            var affectedRows = statement.executeUpdate();
            JdbcUtil.logAffectedRows("Ajout du patient", affectedRows);

            if (affectedRows == 0) {
                throw new RepositoryException("Aucun patient n'a été ajouté");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new RepositoryException("MySQL n'a retourné aucun identifiant patient");
            }
            newElement.setIdPatient(generatedKeys.getLong(1));
            return newElement;
        } catch (SQLException e) {
            throw JdbcUtil.dataAccessException("Erreur pendant l'enregistrement du patient", e);
        } finally {
            JdbcUtil.closeResource(generatedKeys);
            JdbcUtil.closeResource(statement);
        }
    }

    @Override
    public boolean update(Patient newValuesElement) {
        if (newValuesElement == null || newValuesElement.getIdPatient() == null) {
            return false;
        }

        var sql = "UPDATE patients SET nom = ?, age = ? WHERE id_patient = ?";
        PreparedStatement statement = null;

        try {
            statement = JdbcUtil.prepareStatement(
                    connection,
                    sql,
                    false,
                    newValuesElement.getNom(),
                    newValuesElement.getAge(),
                    newValuesElement.getIdPatient()
            );
            var affectedRows = statement.executeUpdate();
            JdbcUtil.logAffectedRows("Modification du patient", affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            throw JdbcUtil.dataAccessException(
                    "Erreur pendant la modification du patient " + newValuesElement.getIdPatient(), e);
        } finally {
            JdbcUtil.closeResource(statement);
        }
    }

    @Override
    public boolean delete(Patient element) {
        return element != null && deleteById(element.getIdPatient());
    }

    @Override
    public boolean deleteById(Long identity) {
        if (identity == null) {
            return false;
        }

        var sql = "DELETE FROM patients WHERE id_patient = ?";
        PreparedStatement statement = null;

        try {
            statement = JdbcUtil.prepareStatement(connection, sql, false, identity);
            var affectedRows = statement.executeUpdate();
            JdbcUtil.logAffectedRows("Suppression du patient", affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            throw JdbcUtil.dataAccessException("Erreur pendant la suppression du patient " + identity, e);
        } finally {
            JdbcUtil.closeResource(statement);
        }
    }

    @Override
    public List<Patient> findByNomLike(String mc) {
        var sql = "SELECT " + SELECT_COLUMNS
                + " FROM patients WHERE LOWER(nom) LIKE LOWER(?) ORDER BY nom";
        var patients = new ArrayList<Patient>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = JdbcUtil.prepareStatement(
                    connection,
                    sql,
                    false,
                    "%" + (mc == null ? "" : mc.trim()) + "%"
            );
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                patients.add(RowMappers.toPatient(resultSet));
            }
            return patients;
        } catch (SQLException e) {
            throw JdbcUtil.dataAccessException("Erreur pendant la recherche des patients par nom", e);
        } finally {
            JdbcUtil.closeResource(resultSet);
            JdbcUtil.closeResource(statement);
        }
    }
}
