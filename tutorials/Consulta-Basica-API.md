**Consulta Básica API**
===

En este breve tutorial se mostrará, con un código de ejemplo, cómo conectarse a la API 2.0 de [Orionx.io](http://orionx.io).

El código mostrado en este tutorial está creado en **JavaScript**. Sin embargo, la metodología aquí empleada se puede implementar en otros lenguajes de programación, tales como C++, Java, Pyton, PHP, entre otros.

---
## **Requisitos Previos**

Para poder implementar el código de ejemplo en JavaScript se requerirá de lo siguiente:

1. [NodeJS](https://nodejs.org/es/) (ver > 8.x): Permite utilizar JavaScript como lenguaje de servidor.
2. Módulo -> [Node-Fetch](https://github.com/bitinn/node-fetch): Permite utilizar el recurso *fetch*.
3. Módulo -> [JsSHA](https://github.com/Caligatio/jsSHA): Permite realizar cifrado *SHA*.

Cabe mencionar que al instalar NodeJS, este instalará también [**NPM**](https://www.npmjs.com/) en nuestro sistema operativo (necesario).

---
## **Comprendiendo Antes de programar**

Antes de programar, siempre es necesario entender el problema a resolver.

### **Antecedentes**

Al pasar de la API 1.0 a la API 2.0 de Orionx:

1. Se incorporó un sistema de autentificación en las llamadas a la API.

    Ahora el *Header* debe tener los siguientes campos:
    ```Javascript
    // Header mensaje POST
    {
    'Content-Type': 'application/json',
    'X-ORIONX-TIMESTAMP': timeStamp, // tiempo actual
    'X-ORIONX-APIKEY': apiKey, // Orionx public API Key (personal)
    'X-ORIONX-SIGNATURE': signature, // Firma generada por cifrado SHA-256
    'Content-Length': body.length // Largo de la consulta (query)
    }
    ```
2. Se eliminaron/modificaron/crearon algunas estructuras en las consultas a la API por medio de GraphQL (este punto se trata con mayor detalle en el tutorial Estructura GraphQL de Orionx).
3. Se incorporaron 3 tipos de permisos asociados a la API Key: **Trade**, **Stats** y **Send**.

### **Abordando el Problema**

El "plan" a seguir será el siguiente:

1. Encriptar la *Secret Api Key* para meterla en la firma del *header*.

2. Generar el *Header* completo.

3. Crear la Query

4. Realizar la consulta

---
## **Código de Ejemplo**

### **Instalacion**

1. Se iniciará instalando [NodeJS](https://nodejs.org/es/) en nuestro ordenador. Esto, a su vez, instalará NPM ("*Node Package Manager*", el cual hoy en día se convirtió en un instalador universal de paquetes).
*Seguir las instruciones de la web*

2. Se debe abrir una consola/terminal (CMD en caso de Windows, y un terminal o consola en el caso de Linux/Mac OS).

3. En la consola, ir a la carpeta donde se realizará el proyecto. Luego ejecutar los siguientes comandos:

    ```bash
    # Instalar Módulos Necesarios
    npm install node-fetch
    npm install jssha
    ```

4. Crear el archivo "connection.js" en la carpeta (en este archivo realizaremos nuestra prueba).

### **Cargar Módulos**

Antes que todo, se llamarán (cargarán) los módulos:

```Javascript
/* Cargando los módulos necesarios */
const fetch = require('node-fetch');
const jsSHA = require('jssha');
```

### **Preparación de los Datos**

Se preparan los datos para utilizarlos ya sea en el *Header* o en el *Body* de la consulta.

```Javascript
// Tiempo Actual
let timeStamp = new Date().getTime() / 1000;

// Creación del objeto SHA256
const shaObj = new JSSHA('SHA-512', 'TEXT');    // según creador del módulo
shaObj.setHMACKey(apiSecretKey, 'TEXT');        // según creador del módulo
let body = JSON.stringify(query);               // Convierte un objeto JS en String
shaObj.update(timeStamp + body);                // según creador del módulo
let signature = shaObj.getHMAC('HEX');          // según creador del módulo
// Se crea la variable Signature con los datos necesarios para la autentificación encriptados. Este mezcla la Secret Key con el tiempo actual y la consulta a realizar.


```

### **Crear la Consulta (Query)**

Crearemos una consulta básica para la API de Orionx. En esta oportunidad, consultaremos cuál fue el último precio transado de la Chaucha en el mercado CHA-CLP (Chauchas y Pesos Chilenos).

*No se detallará aquí la estructura GraphQL de la API de Orionx. Eso está en el tutorial Estructura GraphQL de Orionx.*

Crearemos una variable que contenga el *String* de la consulta:

```Javascript
/* Consulta precio CHA en GrapghQL */
let priceCHA = {
  query: `{market(code: "CHACLP"){
  lastTrade{
    price
  }
}}
`};
```

### **Función para realizar la Consulta**

Una vez los preparativos están listos, se procede a crear la función que realizará la consulta a la API.

```Javascript
// Función asíncrona que envía la consulta y espera la respuesta.
async function sendQuery(url, query, apiKey, apiSecretKey) {
  try {
    // Se envía la consulta por medio de FETCH. Consulta tipo POST.
    let res = await fetch(url, {
      method: 'POST',
      // Cabecera
      headers: {
        'Content-Type': 'application/json',
        'X-ORIONX-TIMESTAMP': timeStamp,
        'X-ORIONX-APIKEY': apiKey,
        'X-ORIONX-SIGNATURE': signature,
        'Content-Length': body.length,
      },
      // Cuerpo del Mensaje (query)
      body,
    });
    // Se retorna el cuerpo de la respuesta con formato objeto JS.
    return res.json();
  } catch (e) {
    throw(e);
  }

```

### **Programa Principal**

Por último, se crea el programa principal, el cual hará la llamada a la función de sendQuery.

```Javascript
// Función principal
async function main() {
  try {
    // Llamando a sendQuery
    let res = await sendQuery(
      'http://api.orionx.io/graphql',   // Dirección de la API de Orionx
      priceCHA,                         // query creada
      '<API Key>',                      // Aquí va la API Key
      '<Secret API Key>'                // Aquí va la Secret API Key
    );

    console.log('*** Respuesta ***');   // Se imprime la respuesta que llega
    console.log(res.data);
  } catch (e) {
    throw(e);
  }
}

// Se ejecuta el programa principal
main();
```

Finalmente guardar y cerrar el archivo.

---
## **Ejecutar el Programa**

Para ejecutar el programa, volvemos al terminal (consola). Debemos asegurarnos de estar en la carpeta del programa.

```bash
# Se ejecuta el programa
node connection.js                    # En Linux sería: node ./connection.js
```

Esto nos devolverá un resultado, como el siguiente:
```bash
# Respuesta del terminal
*** Response ***
{ market: { lastTrade: { price: 4198 } } }
```
---
## **Descarga el código de Conexión**

[**Connection.js**](https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/codes/connection.js)

---
## Autor

*Tutorial creado por **Alexis Apablaza (aapablaza)***
