package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConvocatoriaService {

    @Autowired
    private ConvocatoriaRepository repository;

    public void guardarConvocatoria(Convocatoria nuevaConvocatoria) {
        // Si el admin dice que esta nueva va a estar ACTIVA, quitamos la que esté ya comoa activa
        if ("ACTIVA".equals(nuevaConvocatoria.getEstado())) {
            // Buscamos si ya había otra activa en la base de datos
            Optional<Convocatoria> activaAnterior = repository.findByEstado("ACTIVA");
            
            // Si existía, la apagamos (la ponemos en CERRADA) y la guardamos
            if (activaAnterior.isPresent()) {
                Convocatoria anterior = activaAnterior.get();
                anterior.setEstado("CERRADA");
                repository.save(anterior);
            }
        }
        
        // Finalmente, guardamos la nueva
        repository.save(nuevaConvocatoria);
    }

    public List<Convocatoria> obtenerTodas() {
        return repository.findAllOrdered();
    }

    public Optional<Convocatoria> obtenerConvocatoriaActiva() {
        return repository.findByEstado("ACTIVA");
    }
}
