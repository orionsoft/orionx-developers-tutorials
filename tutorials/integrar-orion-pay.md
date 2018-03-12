# Como integrar Orion Pay a tu plataforma

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

Esto se hace haciendo una llamada al api de Orionx. Debes tener una llave en [Orionx Developers](https://orionx.com/developers), ahí mismo puedes hacer las pruebas.

Te recomiendo crear una función donde centralices todas las llamadas al api. Por ejemplo, en NodeJS, [esta](https://gist.github.com/nicolaslopezj/48905ccb5ec2b738f83f1f6034b44269).

Este es un código de ejemplo de como se crea un "Pago".

```js
const createPaymentForProduct = async function({buyerUserId, productId}) {
  const product = await Products.findOne(productId)

  // Es necesario crear un id secreto para que identifiquemos el pago
  const secret = generateId()

  // URL a la que Orionx avisará el resultado del pago. Debes identificarla con el id secreto
  const callbackURL = 'https://mitienda.com/orion-pay-callback/' + secret

  // La URL a la que queremos redirigir al usuario después de pagar
  const returnURL = ''

  // Creamos el pago, nos retorna el ID del Pago, que usaremos para enviar al usuario a la URL
  const {orionxPayment} = await callOrionx(
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
    }, 
    apiKey, 
    apiSecretKey
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

Cualquiera sea el resultado del pago, Orionx te avisará a la URL que especificaste antes.

Si falla el aviso intentaremos de nuevo hasta 3 veces.

```js
createRoute('/orion-pay-callback/:secret', (params, request) => {
  // Buscamos el pago usando el identificador secreto
  const payment = Payments.findOne({
    status: 'waiting',
    orionxPaymentId: request.body._id,
    secret: params.secret
  })

  if (request.body.status === 'done') {
    // si el estado es done, significa que esta todo OK y que ya abonamos los CLP a tu cuenta
  } else {
    // cualquier otra respuesta significa un error. Nosotros nos encargamos de devolverle las criptomonedas al usuario
  }
})
```

Cualquier duda que tengas pregunta en el telegram de Orionx Developers, ahí te podremos responder todas las dudas.

https://t.me/orionxdev
