package com.coderhouse.ventas.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.coderhouse.ventas.modelos.DetalleVentas;

public class FacturaDTO {

    private Long id;
    private Long clienteId;
    private LocalDateTime fechaHora;
    private Double total;
    private Integer cantidadProductos;
    private List<DetalleVentas> detalleVentas;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public List<DetalleVentas> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVentas> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
}