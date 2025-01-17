package com.example.springhibernateneonapp.service;

import com.example.springhibernateneonapp.DTOs.ProductoCategoria.ProductoCategoriaRequest;
import com.example.springhibernateneonapp.DTOs.ProductoCategoria.ProductoCategoriaUpdateRequest;
import com.example.springhibernateneonapp.entity.Categoria;
import com.example.springhibernateneonapp.entity.Producto;
import com.example.springhibernateneonapp.entity.ProductoCategoria;
import com.example.springhibernateneonapp.repository.CategoriaRepository;
import com.example.springhibernateneonapp.repository.ProductoCategoriaRepository;
import com.example.springhibernateneonapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductoCategoriaService {

    private final ProductoCategoriaRepository productoCategoriaRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoCategoriaService(ProductoCategoriaRepository productoCategoriaRepository, ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoCategoriaRepository = productoCategoriaRepository;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProductoCategoria createProductoCategoria(ProductoCategoriaRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con ID: " + request.getProductoId()));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con ID: " + request.getCategoriaId()));

        ProductoCategoria productoCategoria = new ProductoCategoria();
        productoCategoria.setProducto(producto);
        productoCategoria.setCategoria(categoria);

        return productoCategoriaRepository.save(productoCategoria);
    }

    @Transactional
    public ProductoCategoria updateProductoCategoria(UUID id, ProductoCategoriaUpdateRequest request) {
        ProductoCategoria productoCategoria = productoCategoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la relación con ID: " + id));

        if (request.getProductoId() != null) {
            Producto producto = productoRepository.findById(request.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con ID: " + request.getProductoId()));
            productoCategoria.setProducto(producto);
        }

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con ID: " + request.getCategoriaId()));
            productoCategoria.setCategoria(categoria);
        }

        return productoCategoriaRepository.save(productoCategoria);
    }

    @Transactional
    public void deleteProductoCategoria(UUID id) {
        if (!productoCategoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la relación con ID: " + id);
        }
        productoCategoriaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getProductoCategoriaById(UUID id) {
        ProductoCategoria productoCategoria = productoCategoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la relación Producto-Categoría con el ID: " + id));

        Map<String, Object> productoCategoriaMap = new HashMap<>();
        productoCategoriaMap.put("id", productoCategoria.getId());
        productoCategoriaMap.put("productoId", productoCategoria.getProducto().getId());
        productoCategoriaMap.put("productoNombre", productoCategoria.getProducto().getNombre());
        productoCategoriaMap.put("categoriaId", productoCategoria.getCategoria().getId());
        productoCategoriaMap.put("categoriaNombre", productoCategoria.getCategoria().getNombre());
        return productoCategoriaMap;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoriasByProductoId(UUID productoId) {
        List<ProductoCategoria> productoCategorias = productoCategoriaRepository.findByProductoId(productoId);

        return productoCategorias.stream()
                .map(pc -> {
                    Map<String, Object> categoriaMap = new HashMap<>();
                    categoriaMap.put("categoriaId", pc.getCategoria().getId());
                    categoriaMap.put("nombre", pc.getCategoria().getNombre());
                    return categoriaMap;
                })
                .toList();
    }

    public void associateProductoWithCategorias(ProductoCategoriaRequest request) {

        if (request.getCategoriaId() != null && request.getCategoriaIds() != null) {
            throw new IllegalArgumentException("No se puede proporcionar 'categoriaId' y 'categoriaIds' al mismo tiempo.");
        }
        if (request.getCategoriaId() == null && (request.getCategoriaIds() == null || request.getCategoriaIds().isEmpty())) {
            throw new IllegalArgumentException("Debe proporcionar 'categoriaId' o 'categoriaIds'.");
        }


        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con ID: " + request.getProductoId()));

        if (request.getCategoriaId() != null) {

            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con ID: " + request.getCategoriaId()));

            ProductoCategoria productoCategoria = new ProductoCategoria();
            productoCategoria.setProducto(producto);
            productoCategoria.setCategoria(categoria);
            productoCategoriaRepository.save(productoCategoria);

        } else if (request.getCategoriaIds() != null) {

            List<Categoria> categorias = categoriaRepository.findAllById(request.getCategoriaIds());
            if (categorias.size() != request.getCategoriaIds().size()) {
                throw new IllegalArgumentException("Algunas categorías proporcionadas no existen.");
            }

            List<ProductoCategoria> productoCategorias = categorias.stream()
                    .map(categoria -> {
                        ProductoCategoria productoCategoria = new ProductoCategoria();
                        productoCategoria.setProducto(producto);
                        productoCategoria.setCategoria(categoria);
                        return productoCategoria;
                    })
                    .toList();

            productoCategoriaRepository.saveAll(productoCategorias);
        }
    }

    public List<Producto> getProductosByCategoriaId(UUID categoriaId) {
        return productoCategoriaRepository.findProductosByCategoriaId(categoriaId);
    }



}