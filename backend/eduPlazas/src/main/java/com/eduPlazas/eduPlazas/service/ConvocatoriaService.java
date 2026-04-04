package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;

    public ConvocatoriaService(ConvocatoriaRepository convocatoriaRepository) {
        this.convocatoriaRepository = convocatoriaRepository;
    }

    public List<Convocatoria> findAll() {
        return convocatoriaRepository.findAll();
    }

    public Convocatoria save(Convocatoria convocatoria) {
        return convocatoriaRepository.save(convocatoria);
    }
}
