
package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {   
    // Este métodobusca automáticamente la convocatoria que tenga el estado que le pasemos
    Optional<Convocatoria> findByEstado(String estado);

    //Método que ordena dando prioridad al estado ACTIVA y luego por fecha
    @Query("SELECT c FROM Convocatoria c ORDER BY CASE WHEN c.estado = 'ACTIVA' THEN 1 WHEN c.estado = 'BORRADOR' THEN 2 ELSE 3 END, c.fechaInicio DESC")
    List<Convocatoria> findAllOrdered();
}
