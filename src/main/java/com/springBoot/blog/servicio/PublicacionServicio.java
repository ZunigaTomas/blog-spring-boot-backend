package com.springBoot.blog.servicio;

import com.springBoot.blog.dto.PublicacionDTO;
import com.springBoot.blog.dto.PublicacionRespuesta;

import java.util.List;

public interface PublicacionServicio {
    public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO);

    public PublicacionRespuesta obtenerPublicaciones(int pageNo, int pageSize, String orderBy, String sortDir);

    public PublicacionDTO obtenerPublicacion(Long id);

    public PublicacionDTO editarPublicacion(Long id, PublicacionDTO publicacionDTO);

    public void eliminarPublicacion(Long id);
}
