package com.mp.learning.infrastructure;

public abstract class RestClient {

  private String baseUrl;

  private Integer port;

  public RestClient(String theBaseUrl, Integer thePort) {
    baseUrl = theBaseUrl;
    port = thePort;
  }

  protected String getUrl() {
    String url = baseUrl;
    if (hasPort()) {
      url += ":" + port;
    }

    return url;
  }

  private boolean hasPort() {
    return port != null;
  }
}
