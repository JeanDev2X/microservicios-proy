# 🚀 Sistema de Microservicios de Órdenes (SAGA Pattern)

Este proyecto implementa un flujo de compra de productos utilizando una arquitectura de microservicios, asegurada con JWT y coordinada mediante un patrón SAGA Coreográfico sobre Apache Kafka.
📝 Flujo de Negocio

### 1. Autenticación
El usuario se registra e inicia sesión en `api-auth-service` para obtener un JWT.

### 2. Creación de Orden
El cliente envía una solicitud de compra a `api-order-service`.  
La orden se crea en estado `PENDING`.

### 3. Publicación de Evento
Se publica un evento `OrderCreated` en Kafka.

### 4. Procesamiento de Pago
`api-payment-service`:
- C.


### 5. Gestión de Inventario
`api-inventory-service`:
- Consume el evento `PaymentProcessed`
- Descuenta el stock
- Publica el evento `StockReserved`

### 6. Finalización de Orden
`api-order-service`:
- Escucha los eventos finales
- Actualiza el estado de la orden a `COMPLETED` 

# 🛠️ Microservicios Implementados

# Arquitectura de Microservicios

| Microservicio         | Puerto | Tecnología                                      | Responsabilidad                                      |
|----------------------|--------|--------------------------------------------------|------------------------------------------------------|
| api-auth-service     | 50300  | Spring Security + JWT + Postgres                | Gestión de usuarios y emisión de tokens.              |
| api-order-service    | 60010  | Spring Data JPA + Kafka + Postgres              | Orquestación de la orden y gestión de estados.        |
| api-payment-service  | 60020  | RestClient + Resilience4j + Kafka               | Procesamiento de pagos y comunicación con Visa.       |
| api-inventory-service| 60030  | Spring Data MongoDB + Kafka                     | Gestión de stock y reserva de productos.              |
| api-config-server    | 8888   | Spring Cloud Config                             | Centralización de configuraciones (opcional).         |
| api-eureka-server    | 8761   | Netflix Eureka                                  | Descubrimiento de servicios.                          |

# 🏗️ Requisitos Previos

- Java 17 o superior  
- Docker y Docker Compose  
- Postman (para pruebas)
	
# 🚀 Instrucciones de Ejecución
## 1. Levantar la Infraestructura

Desde la raíz del proyecto, ejecuta el siguiente comando para levantar las bases de datos y el broker de mensajería:

```bash
docker-compose --file docker-proy/infra-compose.yml --profile core up
```

## 2. Ejecutar los Microservicios

Se recomienda seguir este orden de encendido:

    api-auth-service

    Los demás microservicios (order, payment, inventory).

## 🧪 3. Probar el Sistema

1. **Registro / Login**  
   `POST http://localhost:50300/api/v1/auth/login`

2. **Crear Orden**  
   Header: `Authorization: Bearer <TOKEN>`  
   `POST http://localhost:60010/api/v1/orders`

3. **Consultar Orden**  
   `GET http://localhost:60010/api/v1/orders/{orderId}`

4. ** Crear orden
   Se comparte las consultas en postman : MitoMicroservicios_JP.postman_collection.json, para la prueba.
   
   

	
# 📚 Documentación de API (Swagger)

Puedes visualizar y probar los endpoints de órdenes en:
👉 http://localhost:60010/api/v1/swagger-ui/index.html

# 🛡️ Seguridad y Resiliencia

## ⚙️ Consideraciones Técnicas

### 🔐 JWT Local Validation
Cada microservicio valida la firma del token de forma independiente utilizando una clave secreta compartida.  
Esto elimina la necesidad de llamadas remotas al servicio de autenticación en cada request.

---

### 🛡️ Circuit Breaker
Implementado en el cliente de Visa para manejar fallos del servicio externo sin bloquear el flujo de pagos.  
Se utiliza **Resilience4j** para:
- Evitar cascadas de fallos
- Aplicar tolerancia a errores
- Mejorar la resiliencia del sistema

---

### 🔄 Consistencia Eventual
Lograda a través de Kafka, garantizando que los servicios de inventario y pagos se sincronicen con el estado de la orden.  

Este enfoque permite:
- Alta disponibilidad
- Desacoplamiento entre servicios
- Escalabilidad del sistema

	