# dopper
dopper is WebSocket server build on netty and spring framework

## install

```
git clone git@github.com:callme001/dopper.git
cd ./dopper
mvn clean install
```

## Example



```Java

// 0.create properties file in resources (app.properties)

server.host=0.0.0.0
server.port=8080
url=/ws

// 1.Application.class 

@ComponentScan(basePackageClasses = Application.class)
public class Application {

    public static void main(String[] args) {
        WebSocketServerBootstrap.run(Application.class);
    }
}

// 2.load properties

@Configuration
public class LoadProperties extends AbstractLoadProperties {

    protected String[] classPathProperties() {
        return new String[]{"app.properties"};
    }
}

// 3. handle

@Component
public class HelloHandle extends AbstractWebSocketHandle {

    private final static Logger log = LoggerFactory.getLogger(HelloHandle.class);

    @Autowired
    private SendService sendService;

    @Override
    public UpgradeResponse beforeOpen(WebSocketChannel channel,HttpHeaders headers) {
        return new UpgradeResponse(true, HttpResponseStatus.OK);
    }

    public void open(WebSocketChannel channel) {
        log.info("Ws Open Success!! channelHashCode:{}",channel.hashCode());
    }


    public void onMessage(final WebSocketChannel channel, String text) {
        log.info("channel:{},msg:{}",channel.hashCode(),text);
        
        sendService.send(channel,text);

        if ("close".equals(text)) {
            channel.close();
        }
    }

    public void onMessage(WebSocketChannel channel, byte[] bytes) {
        log.info("msg:{}",bytes.length);
    }

    public void onClose(WebSocketChannel channel, CloseEvent event) {
        log.info("channel:{} close type:{}",channel.hashCode(),event);
    }

    public void onStarted(ApplicationContext ctx) {
        log.info("on started");
    }
}

```


**run Application.main()**

And you can use WebSocket Client tools connect to ws://localhost:8080/ws


## Next

* support wss
* performance testing
* ...

