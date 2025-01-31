package com.coderhouse.ventas.servicios;


import com.coderhouse.ventas.dtos.FacturaDTO;
import com.coderhouse.ventas.modelos.Cliente;
import com.coderhouse.ventas.modelos.Factura;
import com.coderhouse.ventas.modelos.Producto;
import com.coderhouse.ventas.modelos.DetalleVentas;
import com.coderhouse.ventas.repositorios.ClienteRepositorio;
import com.coderhouse.ventas.repositorios.FacturaRepositorio;
import com.coderhouse.ventas.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepositorio facturaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public List<FacturaDTO> obtenerTodasLasFacturas() {
        return facturaRepositorio.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public FacturaDTO obtenerFacturaPorId(Long id) {
        return facturaRepositorio.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
    }

    public FacturaDTO guardarFactura(FacturaDTO facturaDTO) {
        Factura factura = convertirAEntidad(facturaDTO);
        Cliente cliente = clienteRepositorio.findById(facturaDTO.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + facturaDTO.getClienteId()));

        factura.setCliente(cliente);

        for (DetalleVentas detalle : factura.getDetalleVentas()) {
            Producto producto = productoRepositorio.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + detalle.getProducto().getId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente para el producto con id: " + producto.getId());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepositorio.save(producto);
        }

        Factura facturaGuardada = facturaRepositorio.save(factura);
        return convertirADTO(facturaGuardada);
    }

    public FacturaDTO actualizarFactura(Long id, FacturaDTO facturaDTOActualizada) {
        return facturaRepositorio.findById(id)
                .map(factura -> {
                    factura.setFechaHora(facturaDTOActualizada.getFechaHora());
                    factura.setTotal(facturaDTOActualizada.getTotal());
                    factura.setCantidadProductos(facturaDTOActualizada.getCantidadProductos());
                    Factura facturaActualizada = facturaRepositorio.save(factura);
                    return convertirADTO(facturaActualizada);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
    }

    public void eliminarFactura(Long id) {
        if (facturaRepositorio.existsById(id)) {
            facturaRepositorio.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id);
        }
    }

    private FacturaDTO convertirADTO(Factura factura) {
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setId(factura.getId());
        facturaDTO.setClienteId(factura.getCliente().getId());
        facturaDTO.setFechaHora(factura.getFechaHora());
        facturaDTO.setTotal(factura.getTotal());
        facturaDTO.setCantidadProductos(factura.getCantidadProductos());
		return facturaDTO;
    }
   
    private Factura convertirAEntidad(FacturaDTO facturaDTO) {
        Factura factura = new Factura();
        factura.setId(facturaDTO.getId());
        factura.setFechaHora(facturaDTO.getFechaHora());
        factura.setTotal(facturaDTO.getTotal());
        factura.setCantidadProductos(facturaDTO.getCantidadProductos());
        factura.setDetalleVentas(facturaDTO.getDetalleVentas().stream()
                .map(DetalleVentas -> {
                    DetalleVentas detalleVenta = new DetalleVentas();
                    detalleVenta.setId(DetalleVentas.getId());
                    detalleVenta.setCantidad(DetalleVentas.getCantidad());
                    detalleVenta.setPrecioProducto(DetalleVentas.getPrecioProducto());
                 
                    return detalleVenta;
                })
                .collect(Collectors.toList()));
        return factura;
    }
}
