package com.cartelera.spring.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cartelera.spring.entity.Pelicula;

public interface PeliculaRepository extends MongoRepository<Pelicula, ObjectId> {
    List<Pelicula> findByEstadoTrue();

    // Búsquedas separadas
    List<Pelicula> findByNombreContainingIgnoreCase(String nombre);

    List<Pelicula> findByGeneroContainingIgnoreCase(String genero);

    // Búsqueda combinada
    default List<Pelicula> findByNombreAndGenero(String nombre, String genero) {
        if (!nombre.isEmpty() && !genero.isEmpty()) {
            return findByNombreContainingIgnoreCaseAndGeneroContainingIgnoreCase(nombre, genero);
        }
        if (!nombre.isEmpty())
            return findByNombreContainingIgnoreCase(nombre);
        if (!genero.isEmpty())
            return findByGeneroContainingIgnoreCase(genero);
        return findByEstadoTrue();
    }

    // Método para búsqueda combinada
    List<Pelicula> findByNombreContainingIgnoreCaseAndGeneroContainingIgnoreCase(String nombre, String genero);
}
