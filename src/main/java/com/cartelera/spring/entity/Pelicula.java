package com.cartelera.spring.entity;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Peliculas")
public class Pelicula {
    @Id
    private ObjectId idmovie;
    private String nombre;
    private String genero;
    private String sinopsis;
    private String horario;
    private String sala;
    private String imagenUrl;//ftp
    private String videoEmbed;//youtube
    private Boolean estado;
    private LocalDate fechaInicio;//isodate
    private LocalDate fechaFinal;//isodate
}
