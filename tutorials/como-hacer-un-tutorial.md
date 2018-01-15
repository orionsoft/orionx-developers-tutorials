# Cómo hacer un tutorial

Para hacer un tutorial en Orionx developers debes seguir estos pasos

### 1. Avísanos

Entra al chat de Orionx developers en Telegram y cuentanos que quieres hacer un tutorial y sobre que quieres que sea. Así podemos ver si alguien ya está haciendo uno y te podemos ayudar.

### 2. Escribe el tutorial en Markdown

Escribe el contenido del tutorial en Markdown.

### 2. Agrega tu tutorial al índice

En el repositorio https://github.com/orionsoft/orionx-developers-tutorials debes editar (haciendo un pull request) el archivo index.json y agregar el contenido del tutorial en la carpeta tutorial.

El archivo `index.json` es un arreglo con la información de cada tutorial.

Por ejemplo:

```json
{
  "key": "como-hacer-un-tutorial",
  "title": "Cómo hacer un tutorial",
  "author": "nicolaslopezj",
  "description": "Descubre como hacer tutoriales para Developers de Orionx",
  "image":
    "https://images.unsplash.com/photo-1454789548928-9efd52dc4031?auto=format&fit=crop&w=2800&q=80",
  "tags": ["tutoriales", "developers"]
}
```

* `key`: Es el ID del tutorial, el nombre del archivo debe tener el mismo ID. El ID debe ser en minusculas y usar "-" en vez de espacios.

* `title`: El título del tutorial, no más de 7 palabras.

* `author`: Tu username de Github.

* `description`: Una descripción del tutorial en un parrafo.

* `image`: La URL de una imagen para tu tutorial.

* `tags`: Un arreglo de los tags.
