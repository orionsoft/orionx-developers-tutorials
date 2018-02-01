import ujson
import requests
import hmac
import time

# Se importa la funcion hash sha512
from hashlib import sha512


def hmac_sha512(secret_key, timestamp, body):
  # para usar hmac es necesario convertir el secret key 
  # de string utf-8 a bytearray
  key = bytearray(secret_key, 'utf-8')
  msg = str(timestamp) + str(body)

  # ademas sha512 requiere de strings codificados
  msg = msg.encode('utf-8')

  return hmac.HMAC(key, msg, sha512).hexdigest()


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
signature = str(hmac_sha512(secret_key, timestamp, body))

headers = {
  'Content-Type': 'application/json',
  'X-ORIONX-TIMESTAMP': timestamp, # Marca de tiempo actual
  'X-ORIONX-APIKEY': api_key, # API Key
  'X-ORIONX-SIGNATURE': signature, #  Firma
}

# url del servidor
url = 'https://api2.orionx.io/graphql'

# Se envia usando POST y 
# se usa el parametro data para enviar los datos del body
response = requests.post(url=url, headers=headers, data=body)

# levanta un error si la peticion fue rechazada
response.raise_for_status()

# se decodifican los datos desde json
data = ujson.loads(response.text)

print(data)