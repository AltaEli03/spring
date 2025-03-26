package com.cartelera.spring.controller;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cartelera.spring.entity.Pelicula;
import com.cartelera.spring.repository.PeliculaRepository;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaRepository peliculaRepository;

    // Listar todas las películas
    @GetMapping
    public String listarPeliculas(Model model) {
        model.addAttribute("peliculas", peliculaRepository.findAll());
        return "admin/list";
    }

    // Mostrar el formulario para crear una nueva película
    @GetMapping("/create")
    public String crearPeliculaForm(Model model) {
        return "admin/create";
    }

    // Guardar una nueva película
    @PostMapping("/save")
    public String guardarPelicula(
            @RequestParam String nombre,
            @RequestParam String genero,
            @RequestParam String sinopsis,
            @RequestParam String horario,
            @RequestParam String sala,
            @RequestParam String imagenUrl,
            @RequestParam String videoEmbed,
            @RequestParam Boolean estado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal) {

        Pelicula pelicula = new Pelicula();

        if (videoEmbed.contains("youtube.com/watch")) {
            String videoId = videoEmbed.split("v=")[1].split("&")[0];
            videoEmbed = "https://www.youtube-nocookie.com/embed/" + videoId;
        }

        pelicula.setNombre(nombre);
        pelicula.setGenero(genero);
        pelicula.setSinopsis(sinopsis);
        pelicula.setHorario(horario);
        pelicula.setSala(sala);
        pelicula.setImagenUrl(imagenUrl);
        pelicula.setVideoEmbed(videoEmbed);
        pelicula.setEstado(estado);
        pelicula.setFechaInicio(fechaInicio);
        pelicula.setFechaFinal(fechaFinal);
        peliculaRepository.save(pelicula);

        return "redirect:/peliculas";
    }

    // Mostrar los detalles de una película
    @GetMapping("/show/{idmovie}")
    public String mostrarPelicula(@PathVariable ObjectId idmovie, Model model) {
        Pelicula pelicula = peliculaRepository.findById(idmovie)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        model.addAttribute("pelicula", pelicula);
        return "admin/show";
    }

    // Eliminar una película
    @PostMapping("/delete")
    public String eliminarPelicula(@RequestParam ObjectId idmovie) {
        Pelicula pelicula = peliculaRepository.findById(idmovie)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        peliculaRepository.delete(pelicula);
        return "redirect:/peliculas";
    }

    // Mostrar formulario para editar una película
    @GetMapping("/edit/{idmovie}")
    public String editarPeliculaForm(@PathVariable ObjectId idmovie, Model model) {
        Pelicula pelicula = peliculaRepository.findById(idmovie)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        model.addAttribute("pelicula", pelicula);
        return "admin/edit";
    }

    // Actualizar una película
    @PostMapping("/update")
    public String actualizarPelicula(
            @RequestParam ObjectId idmovie,
            @RequestParam String nombre,
            @RequestParam String genero,
            @RequestParam String sinopsis,
            @RequestParam String horario,
            @RequestParam String sala,
            @RequestParam String imagenUrl,
            @RequestParam String videoEmbed,
            @RequestParam Boolean estado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal) {

        Pelicula pelicula = peliculaRepository.findById(idmovie)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        if (videoEmbed.contains("youtube.com/watch")) {
            String videoId = videoEmbed.split("v=")[1].split("&")[0];
            videoEmbed = "https://www.youtube-nocookie.com/embed/" + videoId;
        }

        pelicula.setNombre(nombre);
        pelicula.setGenero(genero);
        pelicula.setSinopsis(sinopsis);
        pelicula.setHorario(horario);
        pelicula.setSala(sala);
        pelicula.setImagenUrl(imagenUrl);
        pelicula.setVideoEmbed(videoEmbed);
        pelicula.setEstado(estado);
        pelicula.setFechaInicio(fechaInicio);
        pelicula.setFechaFinal(fechaFinal);
        peliculaRepository.save(pelicula);

        return "redirect:/peliculas";
    }
}
