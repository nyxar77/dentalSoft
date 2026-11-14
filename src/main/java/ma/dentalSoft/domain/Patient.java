package ma.dentalSoft.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Java Bean
@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class Patient {
    private Long idPatient;
    private String nom;
    private Integer age;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(idPatient, patient.idPatient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPatient,  nom, age);
    }
}
