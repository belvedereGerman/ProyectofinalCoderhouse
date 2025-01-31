package com.coderhouse.ventas.controladores;

import com.coderhouse.ventas.dtos.FacturaDTO;
import com.coderhouse.ventas.servicios.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Operation(summary = "Obtener todas las facturas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facturas obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacturaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facturas no encontradas")
    })
    @GetMapping
    public List<FacturaDTO> obtenerTodasLasFacturas() {
        return facturaService.obtenerTodasLasFacturas();
    }

    @Operation(summary = "Obtener una factura por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacturaDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "clienteId": 3,
                                        "fechaHora": "2025-01-13T03:00:00.000+00:00",
                                        "total": 100.0,
                                        "cantidadProductos": 2,
                                        "detalleVentas": [
                                            {
                                                "id": 1,
                                                "facturaId": 1,
                                                "productoId": 3,
                                                "cantidad": 1,
                                                "precioProducto": 50.0
                                            },
                                            {
                                                "id": 2,
                                                "facturaId": 1,
                                                "productoId": 2,
                                                "cantidad": 1,
                                                "precioProducto": 50.0
                                            }
                                        ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public FacturaDTO obtenerFacturaPorId(@PathVariable Long id) {
        return facturaService.obtenerFacturaPorId(id);
    }

    @Operation(summary = "Guardar una nueva factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacturaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacturaDTO guardarFactura(@RequestBody FacturaDTO facturaDTO) {
        return facturaService.guardarFactura(facturaDTO);
    }

    @Operation(summary = "Actualizar una factura existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacturaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PutMapping("/{id}")
    public FacturaDTO actualizarFactura(@PathVariable Long id, @RequestBody FacturaDTO facturaDTO) {
        return facturaService.actualizarFactura(id, facturaDTO);
    }

    @Operation(summary = "Eliminar una factura por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
    }
}
