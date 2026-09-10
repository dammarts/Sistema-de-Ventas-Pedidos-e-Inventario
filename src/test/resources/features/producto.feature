# language: es
Característica: Gestión de productos
  Como administrador del sistema
  Quiero registrar productos en el catálogo
  Para poder venderlos y controlarlos en inventario

  Escenario: Crear producto con datos válidos
    Dado que no existe ningún producto registrado
    Cuando registro un producto llamado "Mouse inalámbrico" con precio 50000 y stock 10
    Entonces el producto queda registrado con estado ACTIVO
    Y el producto puede recuperarse por su id

  Escenario: Rechazar la creación de un producto con precio inválido
    Dado que no existe ningún producto registrado
    Cuando intento registrar un producto llamado "Teclado" con precio -1 y stock 5
    Entonces la operación falla con el código de error "PRECIO_INVALIDO"
