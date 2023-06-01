package com.springBoot.blog.repositorio;

import com.springBoot.blog.entidades.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepositorio extends JpaRepository<Publicacion, Long> {
}
