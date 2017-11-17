Writer's Reader Discarded
=========================

This is a project that aims to reproduce linkerd's `This writer's reader has been discarded` HTTP 502 error.

`client` is a Java client that makes a request to `server` via linkerd, in a linker-to-linker configuration. It needs to be built with:
```
cd client
gradle build
```

`server` is a Go server that simply returns HTTP 200 with body `done`.

IMPORTANT: change `linkerd.yml:23` and `disco/server:1` to use the IP address of the machine the project is running on.

Once the client is built, simply run `docker-compose build` and then `docker-compose up`.

Request flow:
 1. `client` initiates request to `http://linkerd:4141/server`
 2. `linkerd` is found via the Docker network (`default`), by container name
 3. The outgoing linkerd router listening on 4141 looks up `server` in the namer (`io.l5d.fs`)
 4. Once the IP address and port is found, the port is tranformed to 4041 via a `io.l5d.port` transformer
 5. The request is sent to `http://$ipAddress:4041`, passing `l5d-dst-service: /in/out/server` HTTP header
 6. The incoming linkerd router listening on 4041 looks up `server` in the namer (`io.l5d.fs`)
 7. Any returned host returned by the namer that is not the one specified in the `io.l5d.specificHost` transformer is excluded
 8. The request is sent to `http://$ipAddress:8000`, which is where the server is listening on
 9. The server immediately returns HTTP 200 with response body `done`
 10. Once the client receives the response, start agin from point 1. until the `This writer's reader has been discarded` HTTP 502 error occurs.

The first failing request is usually within the first 30 seconds.

