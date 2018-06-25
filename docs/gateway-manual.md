# 使用说明

## 部署环境

![部署关系](imgs/gateway-deploy.jpg)

* GatewayServer：提供网关服务
* GovernorAgent：维护API网关配置和策略配置，配置变更时通知网关服务
* EurekaServer：Eureka运行实例，提供服务注册和发现
* RedisServer：Redis缓存服务
* IAMServer：提供认证鉴权服务
* ServiceProvider：提供业务服务接口的节点

## 准备

* 启动数据库、GovernorAgent、EurekaServer、IAMServer、RedisServer；

使用以下信息做说明：

| 服务            | 注册服务名称       | 接口地址                           |
| --------------- | ------------------ | ---------------------------------- |
| 数据库          |                    | jdbc:mysql://localhost:3306/newgov |
| GovernorAgent   | eos-governor-agent | http://loalhost:8080/admin         |
| EurekaServer    |                    | http://localhost:8761/eureka       |
| IAMServer       |                    | http://192.168.16.210:8080/oauth   |
| RedisServer     |                    | 192.168.16.37:6379                 |
| GatewayServer   | api-gateway        |                                    |
| ServiceProvider | demo               | http://192.168.16.37:9898          |

* 初始化脚本

  ~~~sql
  INSERT INTO agw_http_contenttype_dict (type_name, default_value, type_description, is_multipart, type_pattern) VALUES ('Form', 'application/x-www-form-urlencode', 'Form表单', 0, 'application/x-www-form-urlencode(.*)');
  INSERT INTO agw_http_contenttype_dict (type_name, default_value, type_description, is_multipart, type_pattern) VALUES ('JSON', 'application/json', 'JSON格式', 0, '.+/json(.*)');
  INSERT INTO agw_http_contenttype_dict (type_name, default_value, type_description, is_multipart, type_pattern) VALUES ('XML', 'application/xml', 'XML格式', 0, '.+/xml(.*)');
  ~~~

  

* 打开网关服务引擎安装目录中的com.primeton.esb.apigateway-6.6.0.0.jar文件，根据部署情况对application.yml文件进行修改：

~~~yaml
eureka:
  client:
    enabled: true
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/ # eureka地址
  instance:
    appname: api-gateway 
    virtual-host-name: api-gateway # 网关服务注册名称

spring:
  datasource:
    druid: # 数据库配置
      username: root
      password: volme320
      url: jdbc:mysql://localhost:3306/newgov
      driver-class-name: com.mysql.jdbc.Driver
      max-active: 20
      min-idle: 10
      query-timeout: 5
      max-wait: 5000
      max-wait-thread-count: 10
      validation-query-timeout: 3
      transaction-query-timeout: 5
  jpa:
      hibernate:
        ddl-auto: update
  redis: # Redis服务配置
    timeout: 5000
    host: 192.168.16.37
    port: 6379
    pool:
      max-wait: 10000
      max-active: 500
      min-idle: 50
      max-idle: 250

server:
  port: 3453 # 网关管理接口的端口

gateway:
  credentials:
    iam:
      client: gw # 认证中心颁发给网关的凭证
      secret: 1
      access-token-uri: http://192.168.16.210:8080/iam/oauth/token # 认证中心获取访问令牌的地址
      check-token-uri: http://192.168.16.210:8080/iam/oauth/token_info # 认证中心验证令牌的地址
      grant-type: client_credentials

security:
  basic:
    enabled: false
~~~

* 执行网关启动脚本startServer启动网关服务

* 启动服务提供者

  

  > 本说明使用的服务提供者为一个演示性质的示例，注册名称为demo。
  >
  > 服务提供者demo可由[源码](attachment/api-gateway-demo.zip)通过mvn package导出可运行的jar文件。



## 分组管理

通过分组管理可以创建多个分组，对API按照分组进行管理。

![创建分组](imgs/add-group.jpg)



## 网关服务

### 配置透传服务

以服务提供者提供的查看图书详情为例，其接口为` /json/library/{library}/book/{book}`，通过API网关将该服务发布为`/api/service/{library}/{book}`。

1. 进入API管理之后，可以查看所有注册的API

   

   ![API列表](imgs/list-api.jpg)

   

2. 点击`创建API`，进入到创建API向导中，配置API的名称和编码信息

   

   ![配置基本信息](imgs/config1-api-first.jpg)

   

3. `下一步`进入API请求声明配置

   

   ![配置请求信息](imgs/config1-api-second.jpg)

   

   * 请求路径中可以使用REST风格的模式匹配表达式，该表达式基本语法

     { variable-name [ : regular-expression ] }

     

     > 详情可参考[Path and regular expression mappings](http://docs.jboss.org/resteasy/docs/3.5.1.Final/userguide/html_single/index.html#Using_Path)。

     

   * 在路径中定义了两个变量，在入参定义中对两个变量进行声明，在运行过程中会将变量值赋给入参定义中的参数名

     

   * 入参模式使用透传模式

     > 透传模式：指不对HTTP报文体部分进行任何处理；
     >
     > 转换模式：值对HTTP报文进行处理；

     

4. `下一步`进入到对服务提供者访问时的请求配置

   ![服务提供者请求配置](imgs/config1-api-third.png)

   * 设置后端服务提供者的主机名和端口信息

     > 主机信息有两种方式设置，一种是从以注册的应用中选择，另外一种通过输入具体的主机信息。

   * 设置服务提供者提供的路径信息，这个路径中包含了两个变量

   * 设置HTTP方法为GET

   * 在后端服务参数中设置两个路径参数，将之前设置的变量`libraryId`和`bookId`分别对应的路径中的两个变量`library`和`book`中。

   

5. `下一步`进入到响应配置当中

   ![响应配置](imgs/config1-api-response.jpg)

   * 错误码定义中可以定义多个HTTP错误码，同时定义返回的错误信息值

     

6. `完成`之后可以查看API为停用状态，此时API尚未发布，处于不可用状态。

   ![未部署状态](imgs/list-undeploy-api.jpg)

   

### API授权

上一步中已经配置了一个API，但是该API尚未发布，在发布前可以进行API授权。

1. 进入授权管理之后，创建一个新的客户端

   ![创建客户端](imgs/add-client.png)

   

2. 客户端创建完成之后，选择创建好的客户端，点击`API授权`

   ![授权API](imgs/grant-api.jpg)

   ![API授权](imgs/granted-api.jpg)

   * 从API列表中选择一个API，点击`授权`功能，然后点击`确定`即可完成授权。

### 发布API



1. 返回API管理，选择创建好的API，点击`发布`，API状态为`已发布`则API发布成功。

   ![发布API](imgs/deploy-api.jpg)

   

### 客户端调用



1. 进入API授权管理，点击客户端，查看客户端详情，可以流量客户端已授权的API和客户端凭证

![已授权API](imgs/view-granted-api.jpg)

![客户端凭证](imgs/view-granted-client.jpg)

2. 使用HTTP客户端工具或程序，使用客户端的AppKey和AppSecret去访问IAM获得访问令牌，AppKey和AppSecret使用Basic Authentication方式作为认证凭据，此处以curl为例：

   ~~~shell
   curl -X "POST" "http://192.168.16.210:8080/iam/oauth/token" \
        -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
        -u 'DxbxzshBOt3U:bLUqhMeaZGaVqDORLsSLGBePjr4tptryNkkUakpEODcGlIpc' \
        --data-urlencode "grant_type=client_credentials"
   ~~~

   之后获得类似以下信息

   ~~~json
   {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJkZWZhbHV0Il0sImV4cCI6MTUyOTA4MDQyNywianRpIjoiZDQ1Njk1NGUtZTI2MC00YzVjLTlhYTktNjFiZTcwZjg5MDBmIiwiY2xpZW50X2lkIjoiRHhieHpzaEJPdDNVIn0.KxQfJ4tZcKWCiSKfSPJy5R2HvU3mbQeCw05Y4zZVYxk",
     "token_type": "bearer",
     "expires_in": 119,
     "scope": "defalut",
     "jti": "d456954e-e260-4c5c-9aa9-61be70f8900f"
   }
   ~~~

   其中access_token就是访问令牌。

3. 使用HTTP客户端或者程序访问API网关发布出来的服务`/api/service/{library}/{book}`，同时加入查询参数access_token，并把访问令牌设置到access_token上

   ~~~shell
   curl "http://localhost:8081/api/service/1/1?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJkZWZhbHV0Il0sImV4cCI6MTUyOTA4MDQyNywianRpIjoiZDQ1Njk1NGUtZTI2MC00YzVjLTlhYTktNjFiZTcwZjg5MDBmIiwiY2xpZW50X2lkIjoiRHhieHpzaEJPdDNVIn0.KxQfJ4tZcKWCiSKfSPJy5R2HvU3mbQeCw05Y4zZVYxk" \
        -H 'Content-Type: application/json; charset=utf-8’
   ~~~

   响应结果如下

   ~~~json
   {
     "id": "1",
     "name": "名称1",
     "isbn": "isbn1",
     "author": "作者1",
     "price": 45.32
   }
   ~~~



### 请求报文转换

demo中提供了一个用于购置图书的接口，该接口为`POST /xml/library/{library}/book`，请求报文格式如下

~~~xml
<book author="作者1" id="1" isbn="isbn1" name="名称1" price="45.32"/>
~~~

现在需要通过网关将该接口发布为`POST /api/v2/service/{library}`的JSON格式报文，且请求报文格式如下：

~~~json
{"id":"33","name":"名称1","isbn":"isbn1","author":"作者1","price":45.32}
~~~



1. 配置API基本信息

![](imgs/config2-api-first.jpg)

2. 配置API请求信息



![](imgs/config2-api-second.jpg)

* 设置路径为/api/v2/service/{library}，路径中包含参数library

* 入参定义中对JSON报文的各个字段进行配置

  > 当参数位置为HTTP报文体时，如果是XML格式则使用XPath，如果是JSON格式则使用JSONPath表达式。

3. 配置后端服务

![](imgs/config2-api-third.png)



* 在路径参数中存在一个变量library，所以服务参数中需要从API请求配置中获得值，这个值被设置在libraryId参数中

* 报文转换通过VTL脚本语言进行编写，由于API请求信息中定义了四个参数名称，那么这四个参数名称皆可以作为VTL脚本中使用的变量，路径参数中直接写入VTL脚本即可，脚本如下所示：

  ~~~velocity
  <?xml version="1.0" encoding="UTF-8" standalone="yes"?><book name="$bookName.asText()" price="$bookPrice.asText()" author="$bookAuthor.asText()"/>
  ~~~

  > 对于JSON格式的参数，该参数类型为com.fasterxml.jackson.databind.node.BaseJsonNode；
  >
  > 对于XML格式的参数，该参数类型为org.dom4j.Document。
  >
  > VTL脚本语言的使用可参考[Velocity语法指南](http://velocity.apache.org/engine/devel/vtl-reference.html)。

* HTTP方法为POST，报文格式选择XML格式

4. 配置响应格式，响应格式为XML格式（暂不支持响应格式转换）

   ![](imgs/config2-api-response.png)

### 响应报文转换

当期页面中未提供设置响应报文的转换配置，可以通过调用配置API的接口实现响应配置。

demo中提供了一个用于购置图书的接口，该接口为`POST /xml/library/{library}/book`，请求报文格式如下

```xml
<book author="作者1" id="1" isbn="isbn1" name="名称1" price="45.32"/>
```

响应报文如下：

~~~xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><book author="作者1" name="名称1" price="45.32"/>
~~~



现在需要通过网关将该接口发布为`POST /api/v3/service/{library}`的JSON格式报文，且请求报文格式如下：

```json
{"id":"33","name":"名称1","isbn":"isbn1","author":"作者1","price":45.32}
```

响应报文如下：

~~~json
{"id":"33","name":"名称1","isbn":"isbn1","author":"作者1","price":45.32}
~~~



可使用如下配置设置请求和响应报文转换

~~~json
{
  "backendRequest": {
    "methods": [
      "post"
    ],
    "pathPattern": "/xml/library/{library}/book",
    "payload": [
      {
        "constant": false,
        "content": "libraryId",
        "engine": "default",
        "name": "library",
        "partField": null,
        "partType": null,
        "valueDestPath": "library",
        "valueType": "string",
        "writeLocation": "path"
      },
      {
        "constant": false,
        "content": "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><book name=\"$bookName.asText()\" price=\"$bookPrice.asText()\" author=\"$bookAuthor.asText()\"/>",
        "engine": "vtl",
        "name": "book",
        "partField": null,
        "partType": null,
        "valueDestPath": null,
        "valueType": "string",
        "writeLocation": "body"
      }
    ],
    "payloadCharset": "auto",
    "payloadType": "json",
    "securityId": null,
    "protocol": "http",
    "host": null,
    "serviceId": "demo"
  },
  "backendResponse": {
    "payload": [
      {
        "accessPath": "status",
        "description": "响应码",
        "name": "status",
        "partField": null,
        "partType": null,
        "sourceFrom": "status_code",
        "validateSchema": null,
        "validateType": null,
        "valueType": "string"
      },
      {
        "accessPath": "/book",
        "description": "书的信息",
        "name": "list",
        "partField": null,
        "partType": null,
        "sourceFrom": "body",
        "validateSchema": null,
        "validateType": null,
        "valueType": "string"
      }
    ],
    "payloadCharset": "auto",
    "payloadConvert": 2,
    "payloadType": "xml"
  },
  "description": "向某个指定的图书馆购置一本书",
  "frontendRequest": {
    "methods": [
      "post"
    ],
    "pathPattern": "/api/v3/service/{library}",
    "pathPatternType": "restful",
    "payload": [
      {
        "accessPath": "$.name",
        "description": "图书名称",
        "name": "bookName",
        "sourceFrom": "body"
      },
      {
        "accessPath": "$.author",
        "description": "图书作者",
        "name": "bookAuthor",
        "sourceFrom": "body"
      },
      {
        "accessPath": "$.price",
        "description": "图书价格",
        "name": "bookPrice",
        "sourceFrom": "body"
      },
      {
        "accessPath": "library",
        "description": "图书馆ID",
        "name": "libraryId",
        "sourceFrom": "path"
      }
    ],
    "payloadCharset": "auto",
    "payloadConvert": 2,
    "payloadType": "json"
  },
  "name": "购置图书",
  "protocol": "http",
  "code": "library.book.buy",
  "frontendResponses": [
    {
      "accessVarName": "status",
      "contain": true,
      "internalError": false,
      "matchPattern": "200",
      "payload": [
        {
          "constant": false,
          "content": "#if ($list.size() == 1)#set($bookInfo = $list.get(0))#set($id = $bookInfo.attributeValue(\"id\"))#set($name = $bookInfo.attributeValue(\"name\"))#set($isbn = $bookInfo.attributeValue(\"isbn\"))#set($author = $bookInfo.attributeValue(\"author\"))#set($price = $bookInfo.attributeValue(\"price\")){\"id\":#if($id)\"$id\"#{else}null#end,\"name\":#if($name)\"$name\"#{else}null#end,\"isbn\":#if($isbn)\"$isbn\"#{else}null#end,\"author\":#if($author)\"$author\"#{else}null#end,\"price\":#if($price)$price#{else}null#end}#else{}#end",
          "description": "书的信息",
          "engine": "vtl",
          "name": "retResponse",
          "partField": null,
          "partType": null,
          "valueDestPath": null,
          "valueType": "string",
          "writeLocation": "body"
        }
      ],
      "payloadCharset": "auto"
    }
  ]
}
~~~

* backendResponse中定义了后端的响应格式已经报文体，其中使用status参数来获取响应码，使用参数list代表从XML中的/book获得报文信息

* frontendResponses[0]设置了从参数status中获得值，并匹配200返回值时，启用如下响应报文转换脚本

  ~~~velocity
  #if ($list.size() == 1)
      #set($bookInfo = $list.get(0))
      #set($id = $bookInfo.attributeValue("id"))
      #set($name = $bookInfo.attributeValue("name"))
      #set($isbn = $bookInfo.attributeValue("isbn"))
      #set($author = $bookInfo.attributeValue("author"))
      #set($price = $bookInfo.attributeValue("price"))
      {"id":#if($id)"$id"#{else}null#end,"name":#if($name)"$name"#{else}null#end,"isbn":#if($isbn)"$isbn"#{else}null#end,"author":#if($author)"$author"#{else}null#end,"price":#if($price)$price#{else}null#end}
  #else
  {}
  #end
  ~~~

  按照之前的发布和授权过程，该API即可调用，同时可以对请求和响应报文进行转换操作。

## 日志监控

1. 配置API网关日志存储路径

   打开网关安装目录下的/server/EOS/_srv/config/esb-monitor-logwriter.xml文件，其中配置节点file中的属性path，用于指定网关生成的日志数据。
2. 配置SSM日志解析路径和数据源

   打开SSM安装目录下/ssm/instances/default/conf/ssm.user.properties文件，修改loader.log.file.directory设置为上一步配置的目录地址；修改dispatcher.statistics.db数据库连接；设置dispatcher.statistics.all.enable=true。

3. 启动SSM

4. 进入API管理，选择一个API，点击`API监控`可以查看近12小时的监控数据

   ![](imgs/api-monitor.jpg)



