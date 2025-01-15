package com.example.springhibernateneonapp.repository;

import com.example.springhibernateneonapp.entity.Categoria;
import com.example.springhibernateneonapp.entity.ProductoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria, UUID> {

    List<ProductoCategoria> findByProductoId(UUID productoId);

    List<ProductoCategoria> findByCategoriaId(UUID categoriaId);

    ProductoCategoria findByProductoIdAndCategoriaId(UUID productoId, UUID categoriaId);

    void deleteByProductoId(UUID productoId);

    void deleteByCategoriaId(UUID categoriaId);

    @Query("SELECT pc.categoria FROM ProductoCategoria pc WHERE pc.producto.id = :productoId")
    List<Categoria> findCategoriasByProductoId(UUID productoId);
}