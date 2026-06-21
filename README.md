# Microservicio Soporte

Gestiona tickets de soporte, categorias y respuestas asociadas.

## Clases principales

- `TicketSoporte`: registra solicitudes de soporte de clientes.
- `CategoriaSoporte`: clasifica los tickets.
- `RespuestaTicket`: almacena respuestas o comentarios del equipo de soporte.

## Endpoints principales

- `POST /api/v1/soporte/tickets`
- `GET /api/v1/soporte/tickets`
- `GET /api/v1/soporte/tickets/{id}`
- `GET /api/v1/soporte/tickets/cliente/{idCliente}`
- `GET /api/v1/soporte/tickets/categoria/{idCategoria}`
- `GET /api/v1/soporte/tickets/usuario/{idUsuario}`
- `GET /api/v1/soporte/tickets/estado/{estado}`
- `PUT /api/v1/soporte/tickets/{id}`
- `PATCH /api/v1/soporte/tickets/{id}/cerrar`
- `PATCH /api/v1/soporte/tickets/{id}/estado`
- `PATCH /api/v1/soporte/tickets/{id}/prioridad`
- `DELETE /api/v1/soporte/tickets/{id}`
- `POST /api/v1/soporte/categorias`
- `GET /api/v1/soporte/categorias`
- `PUT /api/v1/soporte/categorias/{id}`
- `PATCH /api/v1/soporte/categorias/{id}/activar`
- `PATCH /api/v1/soporte/categorias/{id}/desactivar`
- `POST /api/v1/soporte/tickets/{id}/respuestas`
- `GET /api/v1/soporte/tickets/{id}/respuestas`
- `PUT /api/v1/soporte/respuestas/{id}`
- `DELETE /api/v1/soporte/respuestas/{id}`

## Pruebas

```powershell
mvn test
```
