package ma.dentalSoft.service.api;

import java.util.List;
import ma.dentalSoft.domain.Patient;

public interface PatientService {

    List<Patient> getPatientsSortedByName();

    Patient findById(Long idPatient);

    Patient save(Patient patient);

    boolean update(Patient patient);

    boolean delete(Patient patient);

    boolean deleteById(Long idPatient);

    List<Patient> findByNomLike(String nom);
}
