// Cambia el contexto a la base de datos del microservicio
db = db.getSiblingDB("inventorydb");

// Crea el usuario específico para la aplicación
db.createUser({
  user: "inventory_user",
  pwd: "inventory_pass",
  roles: [
    { role: "readWrite", db: "inventorydb" }
  ]
});