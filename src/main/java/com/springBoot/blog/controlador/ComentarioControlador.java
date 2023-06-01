package com.springBoot.blog.controlador;

import com.springBoot.blog.dto.ComentarioDTO;
import com.springBoot.blog.servicio.ComentarioServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class ComentarioControlador {
    private final ComentarioServicio comentarioServicio;

    public ComentarioControlador(ComentarioServicio comentarioServicio) {
        this.comentarioServicio = comentarioServicio;
    }
    @GetMapping("/publicaciones/{idPublicacion}/comentarios")
    public List<ComentarioDTO> obtenerComentarios(@PathVariable("idPublicacion") Long idPublicacion) {
        return comentarioServicio.obtenerComentariosPorIdPublicacion(idPublicacion);
    }

    @GetMapping("/publicaciones/{idPublicacion}/comentarios/{idComentario}")
    public ResponseEntity<ComentarioDTO> obtenerComentarioPorId(@PathVariable("idPublicacion") Long idPublicacion,
                                                                @PathVariable("idComentario") Long idComentario){
        ComentarioDTO comentarioDTO = comentarioServicio.obtenerComentarioPorId(idPublicacion,idComentario);

        return new ResponseEntity<>(comentarioDTO, HttpStatus.OK);
    }
    @PostMapping("/publicaciones/{idPublicacion}/comentarios")
    public ResponseEntity<ComentarioDTO> guardarComentario(@PathVariable("idPublicacion") Long idPublicacion,
                                                         @Valid @RequestBody ComentarioDTO comentarioDTO) {
        return new ResponseEntity<>(comentarioServicio.crearComentario(idPublicacion, comentarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/publicaciones/{idPublicacion}/comentarios/{idComentario}")
    public ResponseEntity<ComentarioDTO> editarComentario(@PathVariable("idPublicacion") Long idPublicacion,
                                                          @PathVariable("idComentario") Long idComentario,
                                                          @Valid @RequestBody ComentarioDTO comentarioDTO){
        ComentarioDTO comentarioDTOResponse = comentarioServicio.editarComentario(idPublicacion,idComentario,comentarioDTO);

        return new ResponseEntity<>(comentarioDTOResponse, HttpStatus.OK);
    }

    @DeleteMapping("/publicaciones/{idPublicacion}/comentarios/{idComentario}")
    public ResponseEntity<String> eliminarComentario(@PathVariable("idPublicacion") Long idPublicacion,
                                                     @PathVariable("idComentario") Long idComentario) {
        comentarioServicio.eliminarComentario(idPublicacion, idComentario);

        return new ResponseEntity<>("Se eliminó el comentario", HttpStatus.OK);
    }
}
