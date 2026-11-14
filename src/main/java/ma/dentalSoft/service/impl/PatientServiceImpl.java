package ma.dentalSoft.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.dentalSoft.domain.Patient;
import ma.dentalSoft.repository.api.PatientRepo;
import ma.dentalSoft.service.api.PatientService;

@Data @AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    // Couplage faible
    private final PatientRepo repo;

    @Override
    public List<Patient> getPatientsSortedByName() {
        System.out.println("Call for PatientService to sort patients by name ");
        return repo.findAll()
                .stream()
                .sorted(Comparator.comparing(Patient::getNom))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Patient findById(Long idPatient) {
        return repo.findById(idPatient);
    }

    @Override
    public Patient save(Patient patient) {
        return repo.save(patient);
    }

    @Override
    public boolean update(Patient patient) {
        return repo.update(patient);
    }

    @Override
    public boolean delete(Patient patient) {
        return repo.delete(patient);
    }

    @Override
    public boolean deleteById(Long idPatient) {
        return repo.deleteById(idPatient);
    }

    @Override
    public List<Patient> findByNomLike(String nom) {
        return repo.findByNomLike(nom);
    }
}
