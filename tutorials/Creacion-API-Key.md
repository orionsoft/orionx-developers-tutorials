**Creacion API Key**
======

En este breve tutorial se mostrará cómo crear una llave de autentificación (`API Key`) para  la `API` (*Application Programming Interface*) de [**Orionx.io**](http://orionx.io).

## **Contexto**

Orionx.io ha creado una API para que sus usuarios puedan crear programas y aplicaciones utilizando los datos que la API brinda desde los servidores de Orion.

Ejemplos concretos de aplicaciones creadas con este fin, son la web  [chaucha.xyz](http://chaucha.xyz) y las aplicaciones para móviles [chauchómetro](https://play.google.com/store/apps/details?id=cl.victorsanmartin.chauchometro&hl=es) y [etherometer](https://play.google.com/store/apps/details?id=cl.victorsanmartin.etherometer&hl=es).

## **Crear una API key**

A continuación describemos, paso a paso, cómo crear una API Key para poder acceder a los datos que brinda la API de Orionx.

1. Entrar a [orionx.io/developers](https://orionx.io/developers)
2. Dar click sobre el menú **Keys**
3. Dar click sobre **CREAR KEY**
    ![alt text][api01]
    -> *Se abrirá un recuadro de edición.*
4. Dar click sobre **CREAR**
    ![alt text][api02]
    -> *Se creará la API Key y se visualizará sus opciones.*
5. Editar el nombre de la API KEY y los permisos que esta tiene:
    ![alt text][api03]
    - **Trade**: Permite a la aplicación **crear** y **cancerlar** órdenes de compra y venta.
    - **Stats**: Permite visualizar todas tus transacciones.
    - **Send**: Permite realizar **transferencias** (envíos) de tus cryptomonedas a otras billeteras.

    -> *No te olvides de **guardar** los cambios*

Felicidades! Ya tienes tu primera API Key.

Puedes crear varias API Keys con distintos permisos, para tus distintas aplicaciones. 

Por seguridad no compartas tus API Keys con otras personas.

## **Pruebas**

Posteriormente a la creación de la(s) API Key(s), podemos ingresar al sector de pruebas para hacer *testing* de nuestras consultas a la API. Para ello, se utilizará consultas en `GraphQL`.

---
### **GraphQL**

> [**GraphQL**](http://http://graphql.org/) es un lenguaje de consultas (*queries*) para APIs. Este proporciona una descripción detallada y ordenada de los datos, permitiendo obtener toda la data requerida con una sola consulta.

> GraphQL está organizado por tipos y campos, de forma tal que una aplicación sólo pueda acceder a la información que se le desea entregar, entregando errores claros y útiles cuando no se puede entregar la data o un fallo ocurre.

[Aprende GraphQL](http://graphql.org/learn) (en inglés) - [Tutorial corto en Español](https://www.adictosaltrabajo.com/tutoriales/introduccion-a-graphql/)

---
Para ingresar al sector de pruebas debemos tener creada al menos una API Key. Entraremos al sector de pruebas simplemente seleccionando la API Key creada.
![alt text][api04]
-> *En este caso se selecciona "Personal Api Key"

Una vez dentro, se verá una pantalla dividida en dos. El lado izquierdo es el **área de consultas**, mientras que el lado derecho corresponde al **área de respuestas**.
![alt text][api05]
Además, en el menú superior se encuentran los botones de **>**, **Prettify** y **History**.

1. **>** (Ctrl+Enter): es un botón para ejecutar la consulta GraphQL.
2. **Prettify** (Shift+Ctrl+P): es un formateador de código. Aplica un estilo coherente al analizar el código y volver a imprimirlo con sus propias reglas, teniendo en cuenta la longitud de línea máxima y envolviendo el código cuando es necesario.
3. **History** : Muestra un historial con los comandos utilizados.

Con esto acaba este breve tutorial. En otros tutoriales se abordará cómo es la sintaxis de las consultas GraphQL a la API de Orionx, y se mostrarán códigos de ejemplo.

---
## Autor

*Tutorial creado por **Alexis Apablaza (aapablaza)***

[api01]: https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/images/api_key01.PNG "Crear API Key"
[api02]: https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/images/api_key02.PNG "Nueva API Key"
[api03]: https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/images/api_key03.PNG "Edicion API Key"
[api04]: https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/images/api_key04.PNG "Pruebas"
[api05]: https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/images/api_key05.PNG "GraphQL"