package com.panterozo;
import org.json.JSONObject;

public class Operations {

    private JSONObject json;
    
    public Operations(){}
    
    public void Me(){
        JSONObject json= new JSONObject();
        try{
            json.put("query","query getMe {  me {_id email profile {  firstName  __typename} roles __typename  }}");
            this.setJson(json);
        }catch (Exception e ){
            //System.out.println("Ha ocurrido una Excepción: "+e.toString());
        }
    }

    public void Market(String Code){
        JSONObject json= new JSONObject();
        try{
            json.put("query","query getMarket($code: ID) {  market(code: $code) {code name lastTrade {  price  __typename} mainCurrency {  code  units  format  __typename} secondaryCurrency {  code  units  format  __typename}__typename  } }");
            json.put("variables","{  \"code\": \""+Code+"\"}");
            this.setJson(json);
        }catch (Exception e ){
            //System.out.println("Ha ocurrido una Excepción: "+e.toString());
        }
    }
    
    public void MarketBook(String Code){
        JSONObject json= new JSONObject();
        try{
            json.put("query","query  getmarketOrderBook($code: ID!){ marketOrderBook(marketCode: $code ,limit:1) {buy {limitPrice}sell {limitPrice } spread mid}}");
            json.put("variables","{  \"code\": \""+Code+"\"}");
            this.setJson(json);
        }catch (Exception e ){
            //System.out.println("Ha ocurrido una Excepción: "+e.toString());
        }
    }
    
    public void UserWallet(){
        JSONObject json= new JSONObject();
        try{
            json.put("query","query getUserWallets {  me { _id wallets {  currency {code __typename  } availableBalance ...walletListItem  __typename } __typename  }} fragment walletListItem on Wallet {  _id  balance  currency {code units name symbol format isCrypto minimumAmountToSend __typename  }  __typename}");
            this.setJson(json);
        }catch (Exception e ){
            //System.out.println("Ha ocurrido una Excepción: "+e.toString());
        }
    }

    public void MarketStats(String Code, String period){
        JSONObject json= new JSONObject();
        try{
            json.put("query","query getMarketStats($marketCode: ID!, $aggregation: MarketStatsAggregation!) {  marketStats(marketCode: $marketCode, aggregation: $aggregation) {    _id    open    close    high    low    volume    count    fromDate    toDate    __typename  }}");
            json.put("variables","{\"marketCode\": \""+Code+"\", \"aggregation\": \""+period+"\"}");
            this.setJson(json);
        }catch (Exception e ){
            //System.out.println("Ha ocurrido una Excepción: "+e.toString());
        }
    }

    /*Getters & Setters*/
    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }



}
