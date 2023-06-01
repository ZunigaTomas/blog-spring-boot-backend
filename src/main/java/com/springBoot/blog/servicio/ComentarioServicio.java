package com.springBoot.blog.servicio;

import com.springBoot.blog.dto.ComentarioDTO;

import java.util.List;

public interface ComentarioServicio {

    public ComentarioDTO crearComentario(Long publicacionId, ComentarioDTO comentarioDTO);

    public List<ComentarioDTO> obtenerComentariosPorIdPublicacion(Long publicacionId);

    public ComentarioDTO obtenerComentarioPorId(Long publicacionId, Long comentarioId);

    public ComentarioDTO editarComentario(Long publicacionId, Long comentarioId, ComentarioDTO solicitudComentario);

    public void eliminarComentario(Long publicacionId, Long comentarioId);
}
