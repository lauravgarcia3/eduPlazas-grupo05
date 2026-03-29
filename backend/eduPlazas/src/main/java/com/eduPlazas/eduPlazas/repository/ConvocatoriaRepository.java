package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {
}
