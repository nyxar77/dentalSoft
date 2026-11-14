package ma.dentalSoft.repository.oracleImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ma.dentalSoft.domain.Patient;
import ma.dentalSoft.repository.api.PatientRepo;

public class PatientRepoImpl implements PatientRepo {
    @Override
    public List<Patient> findByNomLike(String mc) {
        return List.of();
    }

    @Override
    public List<Patient> findAll() {
        System.out.println("Call for Oracle repo");
        return new ArrayList<>(
                Arrays.asList(
                        new Patient(1L, "Fadoua", 20),
                        new Patient(2L, "Omar", 22),
                        new Patient(4L, "Souhail", 23),
                        new Patient(5L, "Amine", 18),
                        new Patient(6L, "Tarek", 17)

                )
        );
    }

    @Override
    public Patient findById(Long identity) {
        return null;
    }

    @Override
    public Patient save(Patient newElement) {
        return null;
    }

    @Override
    public boolean update(Patient newValuesElement) {
        return false;
    }

    @Override
    public boolean delete(Patient element) {
        return false;
    }

    @Override
    public boolean deleteById(Long identity) {
        return false;
    }
}
