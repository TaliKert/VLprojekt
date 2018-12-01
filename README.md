# KMK
### Karl, Magnar, Kert

#### *Requirements:*
```  
  * Java 8
  * Gradle 4.10 (no wrapper included)
  * application.yml (see below)
```
  
#### *Running the project*

  `git clone https://github.com/TaliKert/VLprojekt.git`
  
  `cd VLprojekt`
  
  `gradle build`
  
  `java -jar /build/libs/VLprojekt-0.0.1-SNAPSHOT.jar`
  
#### *application.yml*

  Fill out and put this in `src/main/resources`
  
  ```
  # SSL (not required)
  # key must be in the same directory as the jar
  server:
    ssl:
      key-store-type: PKCS12
      key-store: file:keystore.p12
      key-store-password: [your key password]
      key-alias: tomcat
    
  # Google OAuth (not required)
  security:
    oauth2:
      client:
        clientId: [your client id]
        clientSecret: [your client secret]
        accessTokenUri: https://www.googleapis.com/oauth2/v3/token
        userAuthorizationUri: https://accounts.google.com/o/oauth2/auth
        tokenName: oauth_token
        authenticationScheme: query
        clientAuthenticationScheme: form
        scope: 
          - profile
          - email
          - openID
      resource:
        userInfoUri: https://www.googleapis.com/userinfo/v2/me
        preferTokenInfo: false
  
  spring:
    jpa:
      hibernate:
        ddl-auto: create
      generate-ddl: true
    datasource:
      url: jdbc:mysql://[your mysql database]
      username: [your database username]
      password: [your database password]
      
    # Automatic mail service (not required), specific to GMail
    mail:
      host: smtp.gmail.com
      port: 587
      username: [your gmail username]
      password: [your gmail app(!) password]
      properties:
        mail:
          smtp:
            auth: true
            starttls:
              enable: true
  ```

#### *Banklink*

In order to use the donation feature, you must first 
* install and run the Pangalink.net testing interface, which can be found [here](https://github.com/BitWeb/Pangalink.net).
* create a new "maksemeetod"
* look at the example provided in the pangalink configuration page and fill your corresponding values in `src/main/resources/banklink.yml`. A mock example is provided in the repo.
* Download the provided keys: `user_key.pem` and `bank_cert.pem` and put them in `src/main/resources`
* cd to `src/main/resources` and use `openssl pkcs8 -topk8 -inform PEM -outform PEM -in user_key.pem -out priv8.pem -nocrypt` in order to convert the key to pkcs8 protocol ***LINUX ONLY***
* delete `user_key.pem`
