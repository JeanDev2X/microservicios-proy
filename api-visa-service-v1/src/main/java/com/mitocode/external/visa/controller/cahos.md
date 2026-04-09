# CHAOS Testing Endpoints

## Activa CHAOS: introduce un delay de 5 segundos en todos los endpoints

```
curl -X POST http://localhost:8080/account/chaos \
    -H "Content-Type: application/json" \
    -d '{
        "enabled": true,
        "type": "DELAY",
        "delayMs": 5000,
        "failTimes": 0
    }'
```

## Activa CHAOS: todos los requests fallan inmediatamente con error 500

```
curl -X POST http://localhost:8080/account/chaos \
    -H "Content-Type: application/json" \
    -d '{
        "enabled": true,
        "type": "ERROR",
        "delayMs": 0,
        "failTimes": 0
    }'
```

## Activa CHAOS: los próximos 3 requests fallan, luego el servicio se recupera

```
curl -X POST http://localhost:8080/account/chaos \
    -H "Content-Type: application/json" \
    -d '{
        "enabled": true,
        "type": "ERROR_N_TIMES",
        "delayMs": 0,
        "failTimes": 3
    }'
```

## Desactiva CHAOS: el servicio vuelve a comportamiento normal

```
curl -X POST http://localhost:8080/account/chaos \
    -H "Content-Type: application/json" \
    -d '{
        "enabled": false,
        "type": "NONE",
        "delayMs": 0,
        "failTimes": 0
    }'
```