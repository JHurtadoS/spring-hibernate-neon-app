package com.example.springhibernateneonapp.service;

import com.example.springhibernateneonapp.DTOs.Product.ProductoRequest;
import com.example.springhibernateneonapp.DTOs.Product.ProductoUpdateRequest;
import com.example.springhibernateneonapp.entity.Empresa;
import com.example.springhibernateneonapp.entity.Producto;
import com.example.springhibernateneonapp.repository.EmpresaRepository;
import com.example.springhibernateneonapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, EmpresaRepository empresaRepository) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
    }

    /**
     * Crea un producto.
     */
    public Producto createProducto(ProductoRequest productoRequest, UUID empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la empresa con el ID: " + empresaId));

        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setStock(productoRequest.getStock());
        producto.setEmpresa(empresa);

        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto.
     */
    public Producto updateProducto(UUID id, ProductoUpdateRequest updateRequest) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con el ID: " + id));

        // Actualizar solo los campos que no son nulos en la solicitud
        if (updateRequest.getNombre() != null) {
            producto.setNombre(updateRequest.getNombre());
        }
        if (updateRequest.getDescripcion() != null) {
            producto.setDescripcion(updateRequest.getDescripcion());
        }
        if (updateRequest.getPrecio() != null) {
            producto.setPrecio(updateRequest.getPrecio());
        }
        if (updateRequest.getStock() != null) {
            producto.setStock(updateRequest.getStock());
        }
        if (updateRequest.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(updateRequest.getEmpresaId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró la empresa con el ID: " + updateRequest.getEmpresaId()));
            producto.setEmpresa(empresa);
        }

        return productoRepository.save(producto);
    }


    /**
     * Deshabilita un producto (cambia su estado a false).
     */
    public Producto disableProducto(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con el ID: " + id));

        producto.setEstado(false); // Marca el producto como deshabilitado.
        return productoRepository.save(producto);
    }

    /**
     * Obtiene un producto por ID, solo si está activo.
     */
    public Optional<Producto> getProductoById(UUID id) {
        return productoRepository.findByIdAndEstadoTrue(id);
    }

    /**
     * Obtiene todos los productos activos de una empresa con paginación.
     */
    public Page<Producto> getProductosByEmpresa(UUID empresaId, Pageable pageable) {
        return productoRepository.findActiveProductosByEmpresa(empresaId, pageable);
    }

    /**
     * Obtiene todos los productos activos con paginación.
     */
    public Page<Producto> getAllActiveProductos(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }
}