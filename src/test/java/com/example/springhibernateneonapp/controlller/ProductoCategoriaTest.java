package com.example.springhibernateneonapp.controlller;

import com.example.springhibernateneonapp.DTOs.EmpresaRequest;
import com.example.springhibernateneonapp.DTOs.Product.ProductoRequest;
import com.example.springhibernateneonapp.DTOs.ProductoCategoria.ProductoCategoriaRequest;
import com.example.springhibernateneonapp.entity.Categoria;
import com.example.springhibernateneonapp.entity.Empresa;
import com.example.springhibernateneonapp.entity.Producto;
import com.example.springhibernateneonapp.repository.CategoriaRepository;
import com.example.springhibernateneonapp.repository.EmpresaRepository;
import com.example.springhibernateneonapp.repository.ProductoCategoriaRepository;
import com.example.springhibernateneonapp.repository.ProductoRepository;
import com.example.springhibernateneonapp.service.EmpresaService;
import com.example.springhibernateneonapp.service.ProductoCategoriaService;
import com.example.springhibernateneonapp.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoCategoriaTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoCategoriaRepository productoCategoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoCategoriaService productoCategoriaService;


    @Test
    void testCrearEmpresaExitosamente() {
        // Crear un request para la empresa
        EmpresaRequest empresaRequest = new EmpresaRequest(
                "EmpresaTest",
                "12345678",
                "Dirección ejemplo",
                "987654321"
        );

        Empresa empresa = new Empresa();
        empresa.setId(UUID.randomUUID());
        empresa.setNombre(empresaRequest.getNombre());
        empresa.setNit(empresaRequest.getNit());
        empresa.setDireccion(empresaRequest.getDireccion());
        empresa.setTelefono(empresaRequest.getTelefono());
        empresa.setEstado(true);

        // Simular la operación del repositorio
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        // Ejecutar la prueba
        Empresa creada = empresaService.createEmpresa(empresaRequest);

        // Verificaciones
        assertNotNull(creada);
        assertEquals(empresaRequest.getNombre(), creada.getNombre());
        assertEquals(empresaRequest.getNit(), creada.getNit());
        assertEquals(empresaRequest.getDireccion(), creada.getDireccion());
        verify(empresaRepository, times(1)).save(any(Empresa.class));
    }

    @Test
    void testCrearProductoExitosamente() {
        // Datos de la empresa
        UUID empresaId = UUID.randomUUID();
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        empresa.setNombre("Empresa Ejemplo");

        // Datos del producto
        ProductoRequest productoRequest = new ProductoRequest(
                "ProductoTest",
                "Descripción ejemplo",
                BigDecimal.valueOf(100.0),
                10,
                empresaId
        );

        Producto producto = new Producto();
        producto.setId(UUID.randomUUID());
        producto.setNombre(productoRequest.getNombre());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setStock(productoRequest.getStock());
        producto.setEmpresa(empresa);

        // Simular repositorios
        when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresa));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Ejecutar la prueba
        Producto creado = productoService.createProducto(productoRequest, empresaId);

        // Verificaciones
        assertNotNull(creado);
        assertEquals(productoRequest.getNombre(), creado.getNombre());
        assertEquals(productoRequest.getDescripcion(), creado.getDescripcion());
        assertEquals(productoRequest.getPrecio(), creado.getPrecio());
        assertEquals(empresa, creado.getEmpresa());
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(empresaRepository, times(1)).findById(empresaId);
    }

    @Test
    void testAsociarProductoConCategorias() {

        UUID productoId = UUID.randomUUID();
        UUID categoriaId1 = UUID.randomUUID();
        UUID categoriaId2 = UUID.randomUUID();

        Producto producto = new Producto();
        producto.setId(productoId);

        Categoria categoria1 = new Categoria();
        categoria1.setId(categoriaId1);

        Categoria categoria2 = new Categoria();
        categoria2.setId(categoriaId2);

        ProductoCategoriaRequest request = new ProductoCategoriaRequest(productoId, null, List.of(categoriaId1, categoriaId2));

        // Simular repositorios
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(categoriaRepository.findAllById(request.getCategoriaIds())).thenReturn(List.of(categoria1, categoria2));

        // Ejecutar la prueba
        productoCategoriaService.associateProductoWithCategorias(request);

        // Verificaciones
        verify(productoCategoriaRepository, times(1)).saveAll(anyList());
        verify(productoRepository, times(1)).findById(productoId);
        verify(categoriaRepository, times(1)).findAllById(any());
    }
}



