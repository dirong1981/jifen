package com.gljr.jifen.common;


public class ClientType {

    public static int checkClientType(String client_type){
        int clientType = 0;

        if(client_type == null){
            clientType = 0;
        }else if (client_type.equals("mobile")){
            clientType = 1;
        }else if (client_type.equals("pc")){
            clientType = 2;
        }else {
            clientType = 3;
        }

        return clientType;
    }

}
