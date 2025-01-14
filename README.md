
```
curl --location 'http://localhost:8080/public/register' \
--header 'Content-Type: application/json' \
--data '{
"username":"max123321@gmail.com",
"password":"Password1!",
"role":"CLIENT"
}'
```
and
```
curl --location 'http://localhost:8080/public/login' \
--header 'Content-Type: application/json' \
--data '{                                                                                           
"username":"max123321@gmail.com",                          
"password":"Password1!"
}'                                                                                                  
```

```
curl --location 'http://localhost:8080/api/user' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM2ODY3MTY3LCJleHAiOjE3MzY4Njg5Njd9.U1Rt0q59QiCox4YC7FXZVBlyxnm9ieJ3rmz4dVRcgri3-f8xB2IO8BeMOtZAL8BE5a4W70bIb238gBiR-ryfiA' \
--header 'Content-Type: application/json' \
--data '{
  "title": "My Recording",
  "comment": "This is a test recording",
  "dueDate": "2024-12-30T18:00:00",
  "completed": false
}'
```

```
curl --location 'http://localhost:8080/api/user/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxIiwiaWF0IjoxNzM2ODUxMzA0LCJleHAiOjE3MzY4NTMxMDR9.V504RWbiE-1Px7L8ua87L-Jmt-XMtsg9mkCmLwYAaRLOMqYoZZjALnyjsq6OIgoX_NQG6KwSsqRiB8XJx0GomA' \
--header 'Content-Type: application/json'
```
