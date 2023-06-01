package com.springBoot.blog.servicio;

import com.springBoot.blog.dto.PublicacionDTO;
import com.springBoot.blog.dto.PublicacionRespuesta;
import com.springBoot.blog.entidades.Publicacion;
import com.springBoot.blog.excepciones.ResourceNotFoundException;
import com.springBoot.blog.repositorio.PublicacionRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicacionServicioImpl implements PublicacionServicio{

    private final ModelMapper modelMapper;
    private final PublicacionRepositorio publicacionRepositorio;
    @Autowired
    public PublicacionServicioImpl(PublicacionRepositorio publicacionRepositorio, ModelMapper modelMapper) {
        this.publicacionRepositorio = publicacionRepositorio;
        this.modelMapper = modelMapper;
    }
    @Override
    public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO) {
        Publicacion publicacion = convertirAEntidad(publicacionDTO);

        publicacionRepositorio.save(publicacion);

        return convertirADTO(publicacion);
    }
    @Override
    public PublicacionRespuesta obtenerPublicaciones(int pageNo, int pageSize, String orderBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Publicacion> pagina = publicacionRepositorio.findAll(pageable);
        List<Publicacion> publicaciones = pagina.getContent();
        List<PublicacionDTO> contenido = publicaciones.stream().map(this::convertirADTO).toList();

        PublicacionRespuesta publicacionRespuesta = new PublicacionRespuesta();

        publicacionRespuesta.setContenido(contenido);
        publicacionRespuesta.setNroPagina(pagina.getNumber());
        publicacionRespuesta.setMedidaPagina(pagina.getSize());
        publicacionRespuesta.setTotalElementos(pagina.getTotalElements());
        publicacionRespuesta.setTotalPaginas(pagina.getTotalPages());
        publicacionRespuesta.setUltima(pagina.isLast());

        return publicacionRespuesta;
    }

    @Override
    public PublicacionDTO obtenerPublicacion(Long id) {
        Publicacion publicacion = publicacionRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));
        return convertirADTO(publicacion);
    }

    @Override
    public PublicacionDTO editarPublicacion(Long id, PublicacionDTO publicacionDTO) {
        Publicacion publicacion = publicacionRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));
        publicacion.setTitulo(publicacionDTO.getTitulo());
        publicacion.setDescripcion(publicacionDTO.getDescripcion());
        publicacion.setContenido(publicacionDTO.getContenido());

        Publicacion publicacionActualizada = publicacionRepositorio.save(publicacion);
        return convertirADTO(publicacionActualizada);
    }

    @Override
    public void eliminarPublicacion(Long id) {
        Publicacion publicacion = publicacionRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));
        publicacionRepositorio.delete(publicacion);
    }

    private PublicacionDTO convertirADTO(Publicacion publicacion) {
        return modelMapper.map(publicacion, PublicacionDTO.class);
    }

    private Publicacion convertirAEntidad(PublicacionDTO publicacionDTO) {
        return modelMapper.map(publicacionDTO, Publicacion.class);
    }

}
