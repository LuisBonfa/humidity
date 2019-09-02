import com.humidity.controller.*;
import com.ultraschemer.microweb.controller.*;
import com.ultraschemer.microweb.domain.JwtSecurityManager;
import com.ultraschemer.microweb.domain.RoleManagement;
import com.ultraschemer.microweb.domain.UserManagement;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.vertx.WebAppVerticle;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttTopicSubscription;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/*
 * Entry point principal da aplicação:
 */

public class App extends WebAppVerticle {
    @Override
    public void initialization() {
        // Verify the default user and the default role:
        UserManagement.initializeRoot();

        // Initialize JWT symmetric security keys:
        try {
            JwtSecurityManager.initializeSKey();
        } catch (StandardException e) {
            // No exception is waited here:
            e.printStackTrace();
            System.out.println("Exiting.");
            System.exit(-1);
        }

        // Initialize additional roles:
        RoleManagement.initializeDefault();

        // Initialization Filters:
        registerFilter(new AuthorizationFilter(this.getVertx()));

        // Controllers Registers:
        registerController(HttpMethod.POST, "/v0/login", new LoginController(this.getVertx()));
        registerController(HttpMethod.GET, "/v0/logoff", new LogoffController());

        // Users Management routes
        registerController(HttpMethod.GET, "/v0/user/:userIdOrName", new OtherUsersController());
        registerController(HttpMethod.GET, "/v0/user", new UserController());
        registerController(HttpMethod.GET, "/v0/role", new RoleController());
        registerController(HttpMethod.GET, "/v0/role/:roleIdOrName", new RoleController());
        registerController(HttpMethod.PUT, "/v0/user/password", new UserPasswordUpdateController());
        registerController(HttpMethod.PUT, "/v0/user/alias", new UserAliasUpdateController());
        registerController(HttpMethod.POST, "/v0/user", new UserCreationController());
        registerController(HttpMethod.GET, "/v0/users", new UserListController());
        registerController(HttpMethod.GET, "/v0/users/:userIdOrName", new UserListController());

        //Model Management Routes
        registerController(HttpMethod.GET, "/v0/model", new ListModelsController());

        // Area Management Routes
        registerController(HttpMethod.GET, "/v0/area", new ListAreasController());
        registerController(HttpMethod.GET, "/v0/area/:areaIdOrName", new LoadAreaByIdOrNameController());
        registerController(HttpMethod.POST, "/v0/area", new InsertAreaController());
        registerController(HttpMethod.PUT, "/v0/area/:areaId", new UpdateAreaStatusController());

        //Device Management Routes
        registerController(HttpMethod.GET, "/v0/device", new ListDevicesController());
        registerController(HttpMethod.GET, "/v0/device/:deviceIdOrName", new LoadDeviceByIdOrNameController());
        registerController(HttpMethod.POST, "/v0/device", new InsertDeviceController());
        registerController(HttpMethod.PUT, "/v0/device/:deviceId", new UpdateDeviceController());

        //Humidity Management Routes
        registerController(HttpMethod.GET, "/v0/humidity", new ListHumiditiesController());
        registerController(HttpMethod.GET, "/v0/humidity/:deviceOrAreaId", new ListHumiditiesByIdController());
        registerController(HttpMethod.GET, "/v0/humidity/:deviceOrAreaId/date", new ListHumiditiesByDeviceOrAreaAndDate());
        registerController(HttpMethod.POST, "/v0/humidity", new InsertHumidityController());



        // Registra os filtros de finalização:
        // Bem... eles ainda não existem...
    }

    @Override
    public void mqttStartServer() {
        MqttServer mqttServer = MqttServer.create(vertx);
        mqttServer.endpointHandler(endpoint -> {

            // shows main connect info
            System.out.println("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());

            if (endpoint.auth() != null) {
                System.out.println("[username = " + endpoint.auth().getUsername() + ", password = " + endpoint.auth().getPassword() + "]");
            }
            if (endpoint.will() != null) {
                System.out.println("[will topic = " + endpoint.will().getWillTopic() + " msg = " + new String(endpoint.will().getWillMessageBytes()) +
                        " QoS = " + endpoint.will().getWillQos() + " isRetain = " + endpoint.will().isWillRetain() + "]");
            }

            System.out.println("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

            // accept connection from the remote client
            endpoint.accept(false);

            endpoint.disconnectHandler(v -> {
                System.out.println("Received disconnect from client");
            });

            endpoint.subscribeHandler(subscribe -> {

                List<MqttQoS> grantedQosLevels = new ArrayList<>();
                for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
                    System.out.println("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
                    grantedQosLevels.add(s.qualityOfService());
                }
                // ack the subscriptions request
                endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

            });

            endpoint.unsubscribeHandler(unsubscribe -> {

                for (String t : unsubscribe.topics()) {
                    System.out.println("Unsubscription for " + t);
                }
                // ack the subscriptions request
                endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
            });

            endpoint.publishHandler(message -> {

                System.out.println("Just received message [" + message.payload().toString(Charset.defaultCharset()) + "] with QoS [" + message.qosLevel() + "]");

                endpoint.publish("humidity",
                        Buffer.buffer("Hello from the Vert.x MQTT server"),
                        MqttQoS.EXACTLY_ONCE,
                        false,
                        false);
                if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                    endpoint.publishAcknowledge(message.messageId());
                } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                    endpoint.publishReceived(message.messageId());
                }

            }).publishReleaseHandler(messageId -> {
                endpoint.publishComplete(messageId);
            });

            // specifing handlers for handling QoS 1 and 2
            endpoint.publishAcknowledgeHandler(messageId -> {

                System.out.println("Received ack for message = " + messageId);

            }).publishReceivedHandler(messageId -> {

                endpoint.publishRelease(messageId);

            }).publishCompletionHandler(messageId -> {

                System.out.println("Received ack for message = " + messageId);
            });
        })
                .listen(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("MQTT server is listening on port " + ar.result().actualPort());
                    } else {
                        System.out.println("Error on starting the server");
                        ar.cause().printStackTrace();
                    }
                });
    }
}

