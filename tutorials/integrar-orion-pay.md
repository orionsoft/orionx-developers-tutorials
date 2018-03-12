# Como integrar Orion Pay a tu plataform

Puedes usar Orion Pay desde pay.orionx.com sin programar ni una linea de código, pero si quieres una integración con tu plataforma, debes seguir estos pasos.

El flujo es el siguiente:

1. En tu página web, el usuario hace click en "Pagar con Orion Pay"
2. Desde el servidor se crea un nuevo "Pago" llamando al api de Orionx
3. Se envia al usuario a la URL de pago que fue entregada como respuesta en el paso 2
4. Al terminar la transacción Orionx, le avisa a tu servidor el resultado
5. Se redirije al usuario a una URL para mostrar el recibo

Entonces, manos a la obra.

## Crear el "Pago"

Lo primero es crear el "Pago". Esto se hace cuando el usuario va a pagar, ya que expira 15 minutos después de que se crea
y no se puede reutilizar.

Esto se hace haciendo una llamada al api de Orionx. Debes tener una llave en (Orionx Developers)[https://orionx.com/developers], ahí mismo puedes hacer las pruebas.

Te recomiendo crear una función donde centralices todas las llamadas al api. Por ejemplo, en NodeJS, (esta)[https://gist.github.com/nicolaslopezj/48905ccb5ec2b738f83f1f6034b44269].

Este es un código de ejemplo de como se crea un "Pago".

```js
const createPaymentForProduct = async function({buyerUserId, productId}) {
  const product = await Products.findOne(productId)

  // Crear un id secreto para que identifiquemos el pago es necesario
  const secret = generateId()

  // URL a la que Orionx avisará el resultado del pago. Debes identificarla con el id secreto
  const callbackURL = 'http://orionx-pay-server.orionapps.org/callback/' + secret

  // La URL a la que queremos redirigir al usuario después de pagar
  const returnURL = ''

  // Creamos el pago, nos retorna el ID del Pago, que usaremos para enviar al usuario a la URL
  const {orionxPayment} = await callOrionx(
    product.userId,
    gql`
      mutation createPayment(
        $title: String
        $description: String
        $callbackURL: String
        $returnURL: String
        $amount: BigInt
      ) {
        orionxPayment: createPayment(
          title: $title
          description: $description
          amount: $amount
          mainCurrencyCode: "CLP"
          acceptedCurrenciesCodes: ["BTC", "ETH", "LTC", "XRP", "CHA"]
          callbackURL: $callbackURL
          returnURL: $returnURL
        ) {
          _id
          status
        }
      }
    `,
    {
      title: product.name,
      description: product.description,
      callbackURL,
      returnURL,
      amount: product.price
    }
  )

  // Vamos a guardar el registro del pago en la base de datos
  const paymentId = await Payments.insert({
    buyerUserId,
    productId,
    createdAt: new Date(),
    amount: product.price,
    // Guardamos el id del pago
    orionxPaymentId: orionxPayment._id,
    // El estado inicial del pago (waiting)
    status: orionxPayment.status,
    // Guardamos el identificador secreto del pago
    secret
  })

  // Generamos la URL de pago, a donde hay que enviar al usuario
  const paymentURL = `https://orionx.com/pay/${orionxPayment._id}`

  return paymentURL
}
```

Ahora tenemos que enviar el usuario a la URL del pago y esperar.

## Resultado del "Pago"

