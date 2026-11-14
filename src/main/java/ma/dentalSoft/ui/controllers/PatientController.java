package ma.dentalSoft.ui.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.dentalSoft.domain.Patient;
import ma.dentalSoft.service.api.PatientService;

@Data @AllArgsConstructor
public class PatientController {

    private final PatientService service;

    public void getAllPatients() {
        System.out.println("Call for Patient Controller to show result in console");
        var result = service.getPatientsSortedByName();
        result.forEach(p ->
                System.out.println("- Patient : " + p.getNom() + " agé de " + p.getAge() + " ans"));
    }

    public Patient findById(Long idPatient) {
        return service.findById(idPatient);
    }

    public Patient save(Patient patient) {
        return service.save(patient);
    }

    public boolean update(Patient patient) {
        return service.update(patient);
    }

    public boolean delete(Patient patient) {
        return service.delete(patient);
    }

    public boolean delete(Long idPatient) {
        return service.deleteById(idPatient);
    }

    public List<Patient> findByNomLike(String nom) {
        return service.findByNomLike(nom);
    }
}
