Consultas con Python
====================

En este tutorial se explicará una forma sencilla de poder enviar consultas usando Python 2 y 3.

Introducción
============

La API es un servidor con [GraphQL](http://graphql.org/) que permite controlar la plataforma mediante consultas enviadas en formato [JSON](https://es.wikipedia.org/wiki/JSON) usando métodos [POST](https://en.wikipedia.org/wiki/POST_(HTTP)) a través de HTTP.

Esta API esta pensada para utilizarse con cierto grado de seguridad. En concreto, es necesario enviar una serie de [headers http](https://diego.com.es/headers-del-protocolo-http) conteniendo una llave y una firma las cuales se indican a continuación:

```python
{
  'X-ORIONX-TIMESTAMP': timestamp, # Marca de tiempo actual
  'X-ORIONX-APIKEY': api_key, # API Key
  'X-ORIONX-SIGNATURE': signature # Firma 
}
```

La firma se genera del lado del cliente (Python) y es un código [HMAC](https://es.wikipedia.org/wiki/HMAC). Según su definición, se calcula de la siguiente manera:

![alt text][hmacdef]

Donde:

* H sera la función Hash SHA256
* K sera la Secret Key
* m sera el [timestamp](https://es.wikipedia.org/wiki/Marca_temporal) + el contenido del [body](https://en.wikipedia.org/wiki/HTTP_message_body)
* Para el resto de las variables y operadores véase la definición [acá](https://es.wikipedia.org/wiki/HMAC#Definici%C3%B3n_(de_RFC_2104))

Para obtener una `API Key` y `Secret Key` se pueden seguir los pasos de [este tutorial](https://www.orionx.io/developers/tutorials/creacion-api-key)

La API no requiere de nada más, asi que lo que sigue es comenzar a escribir el código.

Implementación
==============

Primero, algunos pasos de inicialización. La mayoría de estos pasos se deben hacer una sola vez.
Se necesitarán algunas librerías para realizar el cálculo del HMAC, otra para generar el contenido de hacia GraphQL usando el formato JSON y por último otra librería que permita enviar toda esta información a traves de internet usando el protocolo HTTP.

Para calcular el HMAC, Python ofrece las librerías `hashlib` y `hmac` que ya vienen preinstaladas. Asi que no es necesario añadir nada. Para generar el contenido de la consulta se utilizará `ujson` y para enviar la información a GraphQl se utilizará la librería `requests`. Para instalar estas librerías se utilizará pip; si no esta instalada se pueden seguir [estas instrucciones](https://pip.pypa.io/en/stable/installing/)

Se instalan las librerías:

```bash
$ pip install requests 
$ pip install ujson
```

* [Documentación de requests](http://docs.python-requests.org/en/master/)
* [Documentación de ujson](https://docs.micropython.org/en/latest/pyboard/library/ujson.html)

Se crea un archivo llamado `query.py` donde se escribirá el código. Lo primero será crear la función `hmac_sha256` que permitirá calcular el HMAC tomando la Secret Key, el timestamp y el contenido de la consulta:

```python
# query.py

# se importan todos los modulos necesarios
import ujson
import requests
import hmac
import time

# Se importa la funcion hash sha256
from hashlib import sha256


# funcion que permite calular el signature
def hmac_sha256(secret_key, timestamp, body):
  # para usar hmac es necesario convertir el secret key 
  # de string utf-8 a bytearray
  key = bytearray(secret_key, 'utf-8')
  msg = str(timestamp) + str(body)

  # ademas sha256 requiere de strings codificados
  msg = msg.encode('utf-8')

  return hmac.HMAC(key, msg, sha256)

```

Posteriormente se crea la consulta:

```python

# string de consulta: pide a graphql 
# la informacion actual del mercado
#   precio de cierre
#   volumen de transacciones
#   variacion
query_str = '''
  query getMarketStats($marketCode: ID!, $aggregation: MarketStatsAggregation!) {
    curr: marketCurrentStats(marketCode: $marketCode, aggregation: $aggregation) {
      close
      volume
      variation
    }
  }
'''

variables = {
  # codigo del mercado:
  # Chaucha - Pesos Chilenos
  'marketCode': 'CHACLP',

  # h1 es cada 1 hora
  'aggregation': 'h1'
}

# se junta en una sola variable
query = {
  'query': query_str,
  'variables': variables
}

```

Documentación de las consultas disponibles en http://api.orionx.io/graphiql en la sección `Docs` ubicada en la pestaña superior derecha del sitio. Esta utilidad también permite interactuar con las consultas en el mismo lugar antes de pasarla al codigo.

Ahora que ya se tiene el contenido, se procede a generar los Headers:

```python
# Contenido total de la consulta en formato JSON
body = ujson.dumps(query)

# Marca de tiempo en segundos convertido a string
# (el header solo acepta strings)
timestamp = str(time.time())

# Llenar con la Api Key
api_key = 'API_KEY'

# Llenar con el secret key
secret_key = 'SECRET_KEY'

# String del codigo HMAC
signature = str(hmac_sha256(secret_key, timestamp, body))

headers = {
  'X-ORIONX-TIMESTAMP': timestamp,
  'X-ORIONX-APIKEY': api_key,
  'X-ORIONX-SIGNATURE': signature
}
```

Finalmente se envía todo al servidor:

```python
# url del servidor
url = 'http://api.orionx.io/graphql'

# Se envia usando POST y el body a traves del parametro json
# Requests detecta si se usa este parametro y automaticamente
# usa el formato JSON y ademas añade el content-type: json
response = requests.post(url=url, headers=headers, json=query)

# levanta un error si la peticion fue rechazada
response.raise_for_status()

# se decodifican los datos desde json
data = ujson.loads(response.text)

print(data)
```

Se guarda todo en el archivo query.py y se ejecuta:
```bash
$ python query.py
```

El resultado debiese ser la información del mercado de la chaucha. Cambiando solo `query_str` y `variables` se puede ejercer practicamente cualquier consulta sobre la plataforma.

En resumen, el código final quedaría así:

```python
import ujson
import requests
import hmac
import time

# Se importa la funcion hash sha256
from hashlib import sha256


def hmac_sha256(secret_key, timestamp, body):
  key = bytearray(secret_key, 'utf-8')
  msg = str(timestamp) + str(body)
  msg = msg.encode('utf-8')
  return hmac.HMAC(key, msg, sha256)


# string de consulta: pide a graphql 
# la informacion actual del mercado
#   precio de cierre
#   volumen de transacciones
#   variacion
query_str = '''
  query getMarketStats($marketCode: ID!, $aggregation: MarketStatsAggregation!) {
    curr: marketCurrentStats(marketCode: $marketCode, aggregation: $aggregation) {
      close
      volume
      variation
    }
  }
'''

variables = {
  # codigo del mercado:
  # Chaucha - Pesos Chilenos
  'marketCode': 'CHACLP',

  # h1 es cada 1 hora
  'aggregation': 'h1'
}

# Contenido total de la consulta
query = {
  'query': query_str,
  'variables': variables
}

# Contenido total de la consulta en JSON
body = ujson.dumps(query)

# Marca de tiempo en segundos convertido a string
# (el header solo acepta strings)
timestamp = str(time.time())

# Llenar con la Api Key
api_key = 'API_KEY'

# Llenar con el secret key
secret_key = 'SECRET_KEY'

# String del codigo HMAC
signature = str(hmac_sha256(secret_key, timestamp, body))

headers = {
  'X-ORIONX-TIMESTAMP': timestamp, # Marca de tiempo actual
  'X-ORIONX-APIKEY': api_key, # API Key
  'X-ORIONX-SIGNATURE': signature # Firma
}

# url del servidor
url = 'http://api.orionx.io/graphql'

# Se envia usando POST y el body a traves del parametro json
# Requests detecta si se usa este parametro y automaticamente
# usa el formato JSON y ademas añade Content-Type: application/json a los Headers
response = requests.post(url=url, headers=headers, json=query)

# levanta un error si la peticion fue rechazada
response.raise_for_status()

# se decodifican los datos desde json
data = ujson.loads(response.text)

print(data)
```

Descargar el código [aquí](https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/codes/query.py)

Conclusión
==========

GraphQl es una poderosa herramienta para crear APIs de manera sencilla y obtener alta escalabilidad lo que se ve demostrado en la plataforma de Orionx.

Este tutorial se escribió para ser fácil de seguir. Para desarrolladores experimentados se recomienda usar Query Batching, lo que simplemente significa enviar cada una de las consultas en una lista y el servidor devolverá los resultados en orden. Esto permite ahorrar tiempo y overhead de conexión.

El proyecto [orionx-api-client](https://github.com/itolosa/orionx-api-client) tiene como objetivo simplificar las conexiones y comenzar a realizar consultas de inmediato. Algunas de las features son la validación de las consultas y generacion de Query Batching transparentes y asíncronas. Las contribuciones son bienvenidas a traves de pull-requests.

Autor
=====

* Ignacio Tolosa Guerrero [itolosa](http://github.com/itolosa)

[hmacdef]: https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/images/hmacdef.png "Definicion HMAC"
