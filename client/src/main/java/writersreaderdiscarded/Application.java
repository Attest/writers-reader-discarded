package writersreaderdiscarded;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws InterruptedException {
    logger.info("Application started. Base URL '{}'", args[0]);
    WritersReaderDiscardedClient writersReaderDiscardedClient = new WritersReaderDiscardedClient(jerseyClient(), args[0]);

    logger.info("Waiting 10 seconds");
    Thread.sleep(10000);

    while (true) {
      writersReaderDiscardedClient.count();
    }
  }

  private static Client jerseyClient() {
    final ClientConfig clientConfig = new ClientConfig();
    clientConfig.connectorProvider(new ApacheConnectorProvider());
    clientConfig.property(ClientProperties.FOLLOW_REDIRECTS, true);
    clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 500); // This should be really short as it's only for the connection to linkerd
    clientConfig.property(ClientProperties.READ_TIMEOUT, 0); // Set to infinite as linkerd will handle timeouts
    clientConfig.property(ClientProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
    clientConfig.property(ClientProperties.MOXY_JSON_FEATURE_DISABLE, true);
    clientConfig.property(ApacheClientProperties.DISABLE_COOKIES, true);
    return ClientBuilder.newClient(clientConfig);
  }
}
