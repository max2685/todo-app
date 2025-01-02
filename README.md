Error:
io.jsonwebtoken.impl.lang.UnavailableImplementationException: Unable to find an implementation for interface io.jsonwebtoken.io.Serializer using java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, for example jjwt-jackson.jar, jjwt-gson.jar or jjwt-orgjson.jar, or your own .jar for custom implementations.

Tried:
1. Disable/enable io.jsonwebtoken libraries. Use different versions.
2. Use different TOKEN generation techniques. 
3. All stuff that I could find in the internet.

If I remove .compact in org/app/config/service/JWTService.java:45 - everything works.


Some requests:
```
curl --location 'http://localhost:8080/public/register' \
--header 'Content-Type: application/json' \
--data '{
"username":"max1",
"password":"max2",
"role":"USER"
}'
```
and
```
curl --location 'http://localhost:8080/public/login' \   
--header 'Content-Type: application/json' \
--data '{
"username":"max1",
"password":"max2"
}'
```

