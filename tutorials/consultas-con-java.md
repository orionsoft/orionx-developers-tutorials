# Ejecución de consultas en OrionX desde Java

En este tutorial se desarrolla una solución en Java para que cualquiera pueda hacer consultas sobre la API de OrionX desde cualquier máquina conectada a internet. 

No están todas las consultas disponibles en la plataforma API, sólo las básicas. Con ello aprenderás a manejarte con la [API de OrionX en GraphQL](https://www.orionx.io/developers/test) para comenzar con tus desarrollos. En este tutorial se expone la base del desarrollo del `cliente` que consume la API de OrionX.

# Prerequisitos

Para poder ejecutar el programa, debes primero tener un compilador de Java. Recomiendo descargar la última versión disponible de [Eclipse](http://www.eclipse.org/downloads/) o bien de [NetBeans](https://netbeans.org/downloads/).

Ya que Java es un lenguaje [Multiplataforma](https://es.wikipedia.org/wiki/Multiplataforma), no hay escusas par no poder ejecutar el software. Esta característica, te permitirá correrlo en Windows, en MAC, obviamente en GNU/Linux y practicamente en cualquier dispositivo. 

# Obtención de código


Para obtener el código fuente, puedes descargarlo en un zip desde [Aquí](https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/JavaZip/Zipped-API2-OrionX_v1.0.zip), o bien desde el repositorio de origen [aquí](https://github.com/panterozo/API2-OrionX/archive/master.zip), o simplemente [clonandolo](https://git-scm.com/book/es/v1/Fundamentos-de-Git-Obteniendo-un-repositorio-Git).

```sh
$ git clone https://github.com/panterozo/API2-OrionX.git
```

# Trabajando con Eclipse

Una vez descargado, debes importar el proyecto dentro del [IDE](https://es.wikipedia.org/wiki/Entorno_de_desarrollo_integrado) descargado anteriormente. En mi caso, utilizaré Eclipse.

Al abrir Eclipse, debes ir al menú principal y dar click en `Archivo => Importar...`. Esto desplegará un conjunto de opciones. En ellas, tienes que indicarle que importarás desde un proyecto existente, tal como se muestra en la imagen:

<div align="center">
		 
	<a href="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/ExistingProject.png">
		<img style="width: 50%" src="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/ExistingProject.png">
	</a>
</div>

Luego, seleccionas la carpeta donde descargaste el proyecto:

<div align="center">
	<a href="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/SelectFolder.png">
		<img style="width: 50%" src="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/SelectFolder.png">
	</a>
</div>

* Importante: Si descargaste el archivo `.zip`, debes descomprimirlo para poder realizar la importación dentro de Eclipse.

Si todo salió bien hasta este punto, el proyecto debería haberse importado correctamente y deberías ver las `clases` como en la siguiente imagen:

<div align="center">
	<a href="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/ClassesImage.png">
		<img style="width: 50%" src="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/ClassesImage.png">
	</a>
</div>

Ahora es momento de incluir tus KEYS creadas en OrionX. Si aún no lo has hecho, puedes seguir [este tutorial](https://www.orionx.io/developers/tutorials/creacion-api-key). Debes incluir las llaves en el código siguiente para que puedas realizar consultas en OrionX:

```sh
user.setApiKeyPublic("AQUI TIENES QUE PONER TU API KEY");
user.setSecretKey("AQUI TIENES QUE PONER TU SECRET KEY");
```

<div align="center">
	<a href="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/settingUpKeys.png">
		<img style="width: 50%" src="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/settingUpKeys.png">
	</a>
</div>

Debes incluir tus KEYS en el código, de tal manera que quede así:

```sh
user.setApiKeyPublic("ERssdrbrajh8o6a744fdVFvdfvSPYPqz");
user.setSecretKey("WsdKztw9CcnnYrOM8SExKSN5sqiEr5hw9P");
```

Ahora podrás ejecutar el programa sin problemas. Para ello, debes clickear con el botón derecho del mouse sobre la clase OrionXBase.java, y seleccionar `Run As... Java Application`, como se muestra en la imagen abajo.

<div align="center">
	<a href="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/RunProgram.png">
		<img style="width: 50%" src="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/RunProgram.png">
	</a>
</div>


Con la ejecución, deberías obtener los resultados en la consola de Eclipse.

<div align="center">
	<a href="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/Reponses.png">
		<img style="width: 50%" src="https://raw.githubusercontent.com/orionsoft/orionx-developers-tutorials/master/tutorials/panterozo/img/Reponses.png">
	</a>
</div>

Con la ejecución del programa acabamos de obtener tus datos personales, el valor mercado, el libro de órdenes, la información de tu wallet y las estadísticas por hora.

# Código

En el código encontrarás tres clases, Operations, OrionXBase y User.

* Clase Operations: Esta clase genera los elementos JSON que serán enviados por método POST a la API de OrionX

* Clase User: Esta clase es utilizada para guardar los datos del dueño de la KEY que se utilizó para logearse

* Clase OrionXBase: Esta clase es donde comienza el programa, y contiene el método `main()`

Internamente, se hace un ciclo para que vaya una a una realizando las operaciones en la API. Así podrás ver diferentes implementaciones para lo que necesites.

```sh
for(int i=0; i<5; i++){
  /*Se genera objeto JSON a enviar*/
  String type = "";
  switch(i){
    case 0:
      type="Me";
      operaciones.Me();
      break;
    case 1:
      type="Market";
      operaciones.Market(marketCode);
      break;
    case 2:
      type="MarketBook";
      operaciones.MarketBook(marketCode);
      break;
    case 3:
      type="UserWallet";
      operaciones.UserWallet();
      break;
    case 4:
      type="MarketStats";
      /*Se obtiene la estadística por hora.*/
      operaciones.MarketStats(marketCode, "h1");
      break;
  }

```

Cada vez que ingresa al ciclo, se genera un [header](https://es.wikipedia.org/wiki/Anexo:Cabeceras_HTTP) nuevo por cada llamada, de la siguiente manera:

```sh

String url = "https://api2.orionx.io/graphql";
URL obj = new URL(url);
HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			
/*Se setea la información de User-Agent*/
con.setRequestMethod("POST");
con.setRequestProperty("User-Agent", "Mozilla/5.0");
con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
/*Se genera timestamp*/
long value = new Date().getTime();
String timestamp = String.valueOf(value);
/*Se genera el valor del header con el objeto, timestamp y tu secret key*/
String apiKeySignature = getHeaderApi2(timestamp,jsonObject,secretKey);
/*apiKeySignature contiene el valor encriptado*/
apiKeySignature = apiKeySignature.toLowerCase();/*Se pasa a minúsculas*/
/*Se setean los valores del header*/
con.setRequestProperty("X-ORIONX-TIMESTAMP", timestamp);
con.setRequestProperty("X-ORIONX-APIKEY", apiKeyPublic);
con.setRequestProperty("X-ORIONX-SIGNATURE", apiKeySignature);

```

El método encargado de la encriptación del mensaje es `getHeaderApi2`:

```sh
public static String getHeaderApi2(String timestamp, JSONObject jsonObject, String privateKey){
    Mac sha512_HMAC = null;
    String result = null;
    /*Se concatena el timestamp con el objeto a enviar*/
    String mesagge=timestamp+jsonObject;

    try{
        byte [] byteKey = privateKey.getBytes("UTF-8");
        final String HMAC_SHA512 = "HmacSHA512";
        sha512_HMAC = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
        sha512_HMAC.init(keySpec);
        byte [] mac_data = sha512_HMAC.doFinal(mesagge.getBytes("UTF-8"));
        result = bytesToHexForApi2(mac_data);
    }catch (Exception e){

    }
    return result;
}

```

No se hace necesario abordar a fondo el cómo de la encriptación. Si quieres abordar este problema, puedes ver el siguiente [tutorial](https://www.orionx.io/developers/tutorials/consultas-con-python) realizado por [@itolosa](https://github.com/itolosa).

# Sobre Mí

Mi nombre es [Ignacio Álvarez Arenas](https://github.com/panterozo), y soy un apasionado por la tecnología y soluciones innovadoras. Espero puedas utilizar sin problemas el código fuente, y ante cualquier duda o consulta, puedes contactarme conmigo y te responderé a la brevedad.

Te dejo mis wallets en caso que te animes a aportarme con criptomonedas. Te agradezco profundamente cualquier aporte, por mínimo que sea.


Chaucha: `ceoNBCv1JF6GgeAhsFLk6efbRkCX5n8kZn`

Bitcoins: `1CWpA2XnFnfTnbAJN94MpQWyunJgoKYz8K`

Ethereum: `0x9f2688B1a76CC24dA8610E90767c319fb2EA2Af0`

Ripple: `rhQrkgoV4uza557mW6HbBWMtTDkB7u5yrs`

Litecoin: `LVBocBQpqD7FrXiUNrAtrkQtw1JdG6CBQt`

Bitcoin Cash: `14X2cEXpT6BLhFtFhYizCdUgoKtAXede4Z`

Dash: `XytqZKeKyMgG1k5meGocmtGGgPnqH7kRiM`


También puedes revisar mis otros proyectos por [aquí](https://panterozo.github.io/Donaciones/) o por [acá](https://panterozo.github.io/Utils).

Happy hacking!

