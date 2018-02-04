package com.panterozo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrionXBase {

	private static User user = new User();
	
	public static void main(String[] args) {
		
		/*Se setean las API keys y Secret keys necesarias para consultar*/
		user.setApiKeyPublic("AQUI TIENES QUE PONER TU API KEY");
		user.setSecretKey("AQUI TIENES QUE PONER TU SECRET KEY");
		/*Se setea el mercardo en el cual se consultará. En este caso, es Chaucha / Peso */
		String marketCode = "CHACLP";
		
		Operations operaciones = new Operations();		
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
			String response = SendData(user.getApiKeyPublic(), user.getSecretKey(), operaciones.getJson());
			if(response.equals("-1")){
				System.out.println("Ha ocurrido una falla en operacion ["+type+"]");
			}else{
				if(type.equals("Me")){
					int retorno = getValuesJsonMe(response);
					if(retorno!=0){
						showMessage();
					}
				}
				System.out.println("Response ["+type+"]: "+response);
			}			
		}
	}
	
	private static void showMessage(){
		System.out.println("***********************************************");
		System.out.println("");
		System.out.println("Bienvenid@: "+user.getUserName()+", tu email es: "+user.getEmail());
		System.out.println("Espero que este software te ayude un montón.");
		System.out.println("Te invito a que me visites en mi perfil de github https://github.com/panterozo");
		System.out.println("...pero más importante, revisa mi página para recibir donaciones!");
		System.out.println("");
		System.out.println("===> https://panterozo.github.io/Donaciones <===");
		System.out.println("");
		System.out.println("Te dejo mis wallets, por si este proyecto fue de utilidad y deseas aportar con Chauchas, Bitcoins o Ethereums");
		System.out.println("");
		System.out.println("CHA: ceoNBCv1JF6GgeAhsFLk6efbRkCX5n8kZn");
		System.out.println("BTC: 1CWpA2XnFnfTnbAJN94MpQWyunJgoKYz8K");
		System.out.println("ETH: 0x9f2688B1a76CC24dA8610E90767c319fb2EA2Af0");
		System.out.println("");
		System.out.println("Cualquier aporte, por mínimo que sea es bienvenido ;)");
		System.out.println("Y no te olvides, cualquier cosa puedes contáctarme y me comunicaré contigo a la brevedad");
		System.out.println("Happy Hacking!");
		System.out.println("");
		System.out.println("***********************************************\n\n\n");
		/*Esta forma es producto de muchas horas trabajando en bash. Con '\n' me ahorro System.out.println(), pero bueno, que diablos :D*/
	}
	
	private static int getValuesJsonMe(String data){
        try {
            JSONObject object = new JSONObject(data);
            if(object.has("errors")){
                JSONArray Jarray  = object.getJSONArray("errors");
                for (int i = 0; i < Jarray.length(); i++)
                {
                    JSONObject Jasonobject = Jarray.getJSONObject(i);
                    if(Jasonobject.get("message").toString().contains("invalidToken")){
                        System.out.println("Invalid Token");
                        return 0;
                    }
                }
                System.out.println("Ha ocurrido un error en la obtención de información personal");
                return 0;
            }else{
                JSONObject Jarray  = object.getJSONObject("data").getJSONObject("me").getJSONObject("profile");
                JSONObject JarrayEmail  = object.getJSONObject("data").getJSONObject("me");
                user.setEmail(JarrayEmail.getString("email"));                
                if(Jarray.getString("firstName").equals("null")){
                	user.setUserName("Anónimo");
                }else{
                	user.setUserName(Jarray.getString("firstName"));
                }
            }
        } catch (JSONException e) {
        	System.out.println("Ha ocurrido un error en la obtención de datos personales");
            return 0;
        }
        return 1;
    }
	
	
	
	
	private static String SendData(String apiKeyPublic, String secretKey,JSONObject jsonObject){
		try {
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
            
            /*El objeto es pasado a bytes*/
            byte[] postData = jsonObject.toString().getBytes();
            /*Se enviará POST*/
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();
            /*Se obtiene el mensaje de respuesta. 200 es OK*/
            int responseCode = con.getResponseCode();
            /*Ha fallado si es >= 400*/
            if (responseCode >= 400) {
                if (responseCode == 404 || responseCode == 410) {
                    throw new FileNotFoundException(url.toString());
                } else {
                    throw new java.io.IOException(
                            "Server returned HTTP"
                                    + " response code: " + responseCode
                                    + " for URL: " + url.toString());
                }
            }
            /*Todo OK, así que se obtiene la respuesta*/
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            /*Se retorna la respuesta*/
            return response.toString();            
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Exception: "+e);
			return "-1";
		}		
	}
	
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
	
	private static String bytesToHexForApi2(byte[] bytes) {
        final  char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
