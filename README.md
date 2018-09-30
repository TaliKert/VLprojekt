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
  ```
