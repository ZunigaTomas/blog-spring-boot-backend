package com.springBoot.blog.controlador;

import com.springBoot.blog.assets.AppConstantes;
import com.springBoot.blog.dto.PublicacionDTO;
import com.springBoot.blog.dto.PublicacionRespuesta;
import com.springBoot.blog.servicio.PublicacionServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/publicaciones/")
public class PublicacionControlador {
    private final PublicacionServicio publicacionServicio;

    @Autowired
    public PublicacionControlador(PublicacionServicio publicacionServicio) {
        this.publicacionServicio = publicacionServicio;
    }

    @GetMapping
    public PublicacionRespuesta obtenerPublicaciones(
            @RequestParam(value = "pageNo", defaultValue = AppConstantes.NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int noPag,
            @RequestParam(value = "pageSize", defaultValue = AppConstantes.MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaPag,
            @RequestParam(value = "orderBy", defaultValue = AppConstantes.ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = AppConstantes.ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {
        return publicacionServicio.obtenerPublicaciones(noPag, medidaPag, ordenarPor, sortDir);
    }

    @GetMapping("{id}")
    public ResponseEntity<PublicacionDTO> obtenerPublicacion(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(publicacionServicio.obtenerPublicacion(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PublicacionDTO> guardarPublicacion(@Valid @RequestBody PublicacionDTO publicacionDTO) {
        return new ResponseEntity<>(publicacionServicio.crearPublicacion(publicacionDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<PublicacionDTO> editarPublicacion(@PathVariable(name = "id") Long id, @Valid @RequestBody PublicacionDTO publicacionDTO) {
        PublicacionDTO publicacionRespuesta = publicacionServicio.editarPublicacion(id, publicacionDTO);
        return new ResponseEntity<>(publicacionRespuesta,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable(name = "id") Long id) {
        publicacionServicio.eliminarPublicacion(id);
        return new ResponseEntity<>("Publicacion eliminada", HttpStatus.OK);
    }
}
