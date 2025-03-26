package com.cartelera.spring.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.cartelera.spring.entity.Pelicula;
import com.cartelera.spring.repository.PeliculaRepository;

@Controller
public class HomeController {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @GetMapping("/home")
    public String home(Model model, Authentication authentication, @RequestParam(value = "logout", required = false) String logout) {

        if (logout != null) {
            model.addAttribute("logout", "Sesión cerrada exitosamente.");
        }

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());
        }
        model.addAttribute("peliculas", peliculaRepository.findByEstadoTrue());
        return "home";
    }

    @GetMapping("/pelicula/{id}")
    public String verDetallesPelicula(@PathVariable ObjectId id, Model model, Authentication authentication) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        model.addAttribute("pelicula", pelicula);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());
        }
        return "user/pelicula-detalles";
    }

    @GetMapping("/search")
    public String buscarPeliculas(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            Model model) {

        model.addAttribute("peliculas",
                peliculaRepository.findByNombreAndGenero(
                        title != null ? title : "",
                        genre != null ? genre : ""));
        return "fragments/peliculas-grid :: #movieGrid";
    }
}