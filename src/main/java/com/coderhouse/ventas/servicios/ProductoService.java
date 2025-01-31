package com.coderhouse.ventas.servicios;

import com.coderhouse.ventas.dtos.ProductoDTO;
import com.coderhouse.ventas.modelos.Producto;
import com.coderhouse.ventas.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoRepositorio.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        return productoRepositorio.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + id));
    }

    public ProductoDTO guardarProducto(ProductoDTO productoDTO) {
        Producto producto = convertirAEntidad(productoDTO);
        Producto productoGuardado = productoRepositorio.save(producto);
        return convertirADTO(productoGuardado);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTOActualizado) {
        return productoRepositorio.findById(id)
                .map(producto -> {
                    producto.setDescripcion(productoDTOActualizado.getDescripcion());
                    producto.setPrecio(productoDTOActualizado.getPrecio());
                    producto.setStock(productoDTOActualizado.getStock());
                    Producto productoActualizado = productoRepositorio.save(producto);
                    return convertirADTO(productoActualizado);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + id));
    }

    public void eliminarProducto(Long id) {
        if (productoRepositorio.existsById(id)) {
            productoRepositorio.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + id);
        }
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        return productoDTO;
    }

    private Producto convertirAEntidad(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        return producto;
    }
}