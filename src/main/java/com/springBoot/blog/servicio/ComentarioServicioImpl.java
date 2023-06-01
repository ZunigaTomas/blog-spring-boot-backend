package com.springBoot.blog.servicio;

import com.springBoot.blog.dto.ComentarioDTO;
import com.springBoot.blog.entidades.Comentario;
import com.springBoot.blog.entidades.Publicacion;
import com.springBoot.blog.excepciones.BlogAppException;
import com.springBoot.blog.excepciones.ResourceNotFoundException;
import com.springBoot.blog.repositorio.ComentarioRepositorio;
import com.springBoot.blog.repositorio.PublicacionRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServicioImpl implements ComentarioServicio{

    private final ModelMapper modelMapper;
    private final ComentarioRepositorio comentarioRepositorio;
    private final PublicacionRepositorio publicacionRepositorio;
    @Autowired
    public ComentarioServicioImpl(ComentarioRepositorio comentarioRepositorio,
                                  PublicacionRepositorio publicacionRepositorio,
                                  ModelMapper modelMapper) {
        this.comentarioRepositorio = comentarioRepositorio;
        this.publicacionRepositorio = publicacionRepositorio;
        this.modelMapper = modelMapper;
    }
    @Override
    public ComentarioDTO crearComentario(Long publicacionId, ComentarioDTO comentarioDTO) {
        System.out.println("ComentarioDTO = " + comentarioDTO.getNombre());
        Comentario comentario = convertirAEntidad(comentarioDTO);
        Publicacion publicacion = publicacionRepositorio.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));

        comentario.setPublicacion(publicacion);
        Comentario nuevoComentario = comentarioRepositorio.save(comentario);
        return convertirADTO(nuevoComentario);
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorIdPublicacion(Long publicacionId) {
        List<Comentario> comentarios = comentarioRepositorio.findByPublicacionId(publicacionId);
        return comentarios.stream().map(this::convertirADTO).toList();
    }

    @Override
    public ComentarioDTO obtenerComentarioPorId(Long publicacionId, Long comentarioId) {
        Publicacion publicacion = publicacionRepositorio.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));
        Comentario comentario = comentarioRepositorio.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

        if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
        }
        return convertirADTO(comentario);
    }

    @Override
    public ComentarioDTO editarComentario(Long publicacionId,Long comentarioId, ComentarioDTO solicitudComentario) {
        Publicacion publicacion = publicacionRepositorio.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));
        Comentario comentario = comentarioRepositorio.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

        if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
        }

        comentario.setNombre(solicitudComentario.getNombre());
        comentario.setCuerpo(solicitudComentario.getCuerpo());
        comentario.setEmail(solicitudComentario.getEmail());

        Comentario comentarioActualizado = comentarioRepositorio.save(comentario);

        return convertirADTO(comentarioActualizado);
    }

    @Override
    public void eliminarComentario(Long publicacionId, Long comentarioId) {
        Publicacion publicacion = publicacionRepositorio.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));
        Comentario comentario = comentarioRepositorio.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

        if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
        }
        comentarioRepositorio.delete(comentario);
    }

    private ComentarioDTO convertirADTO(Comentario comentario) {
        return modelMapper.map(comentario, ComentarioDTO.class);
    }

    private Comentario convertirAEntidad(ComentarioDTO comentarioDTO) {
        return modelMapper.map(comentarioDTO, Comentario.class);
    }
}
