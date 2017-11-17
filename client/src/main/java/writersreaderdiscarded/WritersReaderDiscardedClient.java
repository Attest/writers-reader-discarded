package writersreaderdiscarded;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WritersReaderDiscardedClient {

  private static final Logger logger = LoggerFactory.getLogger(WritersReaderDiscardedClient.class);

  private final Client client;
  private final String baseUrl;

  public WritersReaderDiscardedClient(final Client client, final String baseUrl) {
    this.client = client;
    this.baseUrl = baseUrl;
  }

  void count() {
    logger.info("Request to '{}'", baseUrl);
    final Response response = client.target(baseUrl).request(MediaType.APPLICATION_JSON_TYPE).post(null);

    final String raw = response.readEntity(String.class);
    logger.info("Response status: {}", response.getStatus());
    logger.info("Response body: '{}'", raw);
    response.close();

    if (response.getStatus()  != 200) {
      String message = String.format("Got HTTP status code %d. Body was '%s'", response.getStatus(), raw);
      throw new RuntimeException(message);
    }
  }
}
