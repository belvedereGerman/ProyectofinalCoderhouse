package com.coderhouse.ventas.repositorios;

import com.coderhouse.ventas.modelos.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepositorio extends JpaRepository<Factura, Long> {
}