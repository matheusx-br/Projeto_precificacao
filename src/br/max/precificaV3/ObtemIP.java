package br.max.precificaV3;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.*;

import java.io.IOException;

public class ObtemIP {
    public String main(String url) throws IOException, IOException {
        HttpHost proxy = new HttpHost("zproxy.lum-superproxy.io", 22225);
        String res = Executor.newInstance()
                .auth(proxy, "lum-customer-c_ade1ce4e-zone-zone2-country-br-state-sp-city-saojosedoscampos", "upniwyow8oxg")
                .execute(Request.Get(url).viaProxy(proxy))
                .returnContent().asString();
       return res;
    }
    }

