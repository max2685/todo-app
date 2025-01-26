
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
curl --location 'http://localhost:8080/api/user/todos' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWtzaW0udG9sa2FjaG92QGdtYWlsLmNvbSIsImlhdCI6MTczNzkxNDcxMSwiZXhwIjoxNzM3OTE2NTExfQ.Oua8B74dpoCI8fFsC5t1aUsvmLuxQzEGdtIIqsJ-zPeuC5ddxfnpV0PnTp2LGXoNMhVpaStw6H4VD2T4W9Pzmg' \
--header 'Content-Type: application/json' \
--data '{
  "title": "My Recording",
  "comment": "This is a test recording",
  "dueDate": "2025-01-27",
  "completed": false
}'
```

```
curl --location 'http://localhost:8080/api/user/todos/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM3OTExNzc4LCJleHAiOjE3Mzc5MTM1Nzh9.KocfTHcdqw5YdOYwvvT5V9h_2d2HfLJ2cBzSGVUsqDfbxixlYoA3lz4_mbMThUBc0SvrljFCKmpCvdlzSz2k_w' \
--header 'Content-Type: application/json'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-01-26&dueDate=2025-12-30&completed=false' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM3OTExNzc4LCJleHAiOjE3Mzc5MTM1Nzh9.KocfTHcdqw5YdOYwvvT5V9h_2d2HfLJ2cBzSGVUsqDfbxixlYoA3lz4_mbMThUBc0SvrljFCKmpCvdlzSz2k_w'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-01-26&dueDate=2025-12-30&completed=false&query=Recording' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM3OTExNzc4LCJleHAiOjE3Mzc5MTM1Nzh9.KocfTHcdqw5YdOYwvvT5V9h_2d2HfLJ2cBzSGVUsqDfbxixlYoA3lz4_mbMThUBc0SvrljFCKmpCvdlzSz2k_w'
```
