package ma.dentalSoft;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import ma.dentalSoft.domain.Patient;
import ma.dentalSoft.repository.oracleImpl.PatientRepoImpl;
import ma.dentalSoft.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void sortsPatientsByName() {
        var service = new PatientServiceImpl(new PatientRepoImpl());
        List<Patient> sorted = service.getPatientsSortedByName();

        assertEquals(5, sorted.size());
        assertEquals("Amine", sorted.get(0).getNom());
        assertEquals("Tarek", sorted.get(4).getNom());
    }
}
