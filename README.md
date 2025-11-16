# Imatia State Machine

API REST para gestión de estados de seguimiento de pedidos.

## Requisitos

- Java 8+
- Maven 3.x

## Ejecución

```bash
mvn spring-boot:run
```

La aplicación se inicia en el puerto `6080`.

## Endpoints

### POST /order/tracking

Actualiza el estado de seguimiento de pedidos. Acepta JSON o XML.

**Ejemplo JSON:**
```json
{
  "orderTrackings": [
    {
      "orderId": 230688716,
      "trackingStatusId": 1,
      "changeStatusDate": "2021-02-24T10:30:30.000+01:00"
    }
  ]
}
```

**Ejemplo XML:**
```xml
<UpdateOrderTrackingRequest>
    <orderTrackings>
      <orderTracking>
        <orderId>230688716</orderId>
        <trackingStatusId>1</trackingStatusId>
        <changeStatusDate>2021-02-24T10:30:30.000+01:00</changeStatusDate>
      </orderTracking>
    </orderTrackings>
</UpdateOrderTrackingRequest>
```

## Pruebas

Los datos de prueba están disponibles en: `src\test\resources\Imatia_State_Machine_Requests.postman_collection.json`

Importa la colección en Postman para probar el endpoint con ejemplos de JSON y XML.

## Actuator

Métricas y health checks disponibles en el puerto `6081`:
- http://localhost:6081/actuator
