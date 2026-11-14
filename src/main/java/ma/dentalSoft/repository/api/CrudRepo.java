package ma.dentalSoft.repository.api;

import java.util.List;
import ma.dentalSoft.domain.Patient;

public interface CrudRepo <T, ID>
{
    List<T> findAll();
    T findById( ID identity );
    T save( T newElement );
    boolean update( T newValuesElement );
    boolean delete( T element );
    boolean deleteById( ID identity  );
}
