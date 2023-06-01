package com.springBoot.blog.repositorio;

import com.springBoot.blog.entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {
    public List<Comentario> findByPublicacionId(Long publicacionId);
}
