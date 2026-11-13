package ma.dentalSoft.repository.api;

import java.util.List;
import ma.dentalSoft.domain.Patient;

public interface PatientRepo extends CrudRepo<Patient, Long>
{
    List<Patient> findByNomLike(String mc);
}
