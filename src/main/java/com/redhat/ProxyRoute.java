package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class ProxyRoute extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

		this.getContext().addComponent("http", new HttpComponent());
		
		rest("/rules")
			.post().route().setBody(constant("{\"lookup\":null,\"commands\":[{\"insert\":{\"object\":{\"com.myspace.niaid.employee\":{\"source\":\"src_db1\"}}}},{\"fire-all-rules\":{}}]}"))
			.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
			.to("https://dm-exec-server-3scale-apicast-production.apps.pam-2b40.open.redhat.com/services/rest/server/containers/instances/NIAID_1.0.0?user_key=edebc557b97ff9232960923199937c55&bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson, true);
	}
}
