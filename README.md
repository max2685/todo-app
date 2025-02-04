
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
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4NjcwMjQ3LCJleHAiOjE3Mzg2NzIwNDd9.HT7w3Uj-SVsE3fdsbIVcJJddJnKMjr7OwBhMslxBDc2stnlaE_bYrAh6cxqQ8YFXW6Qchi36wDc4qcHDov2ekw' \
--header 'Content-Type: application/json' \
--data '{
  "title": "My Recording1",
  "comment": "This is a test recording1",
  "dueDate": "2025-02-25",
  "completed": false
}'

curl --location 'http://localhost:8080/api/user/todos' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4NjcwMjQ3LCJleHAiOjE3Mzg2NzIwNDd9.HT7w3Uj-SVsE3fdsbIVcJJddJnKMjr7OwBhMslxBDc2stnlaE_bYrAh6cxqQ8YFXW6Qchi36wDc4qcHDov2ekw' \
--header 'Content-Type: application/json' \
--data '{
  "title": "My Recording2",
  "comment": "This is a test recording2",
  "dueDate": "2025-02-05",
  "completed": true
}'
```

```
curl --location 'http://localhost:8080/api/user/todos/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4NjY5OTkwLCJleHAiOjE3Mzg2NzE3OTB9.b4uTwY2gfoMAPlZpsO8l1pZtTLRxEtRGDl4VvBWo0dBPJZU7thfvcwCv0UaA2yWnO3KBtqGp5s6v0mUXuGY1wA' \
--header 'Content-Type: application/json'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-02-04&dueDate=2025-02-05&completed=true' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4Njc1MDIyLCJleHAiOjE3Mzg4NTUwMjJ9.OXx13dkmccSWE4tmy81NbdBOa4eM1nG84SaxcEMqooNAqMq-ihZ_EUgJe0SkVSB0cuEQGoITH2ZI_fzrgWz-Sg'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-02-04&dueDate=2025-02-05&completed=true&title=My' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4Njc1MDIyLCJleHAiOjE3Mzg4NTUwMjJ9.OXx13dkmccSWE4tmy81NbdBOa4eM1nG84SaxcEMqooNAqMq-ihZ_EUgJe0SkVSB0cuEQGoITH2ZI_fzrgWz-Sg'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-02-04&dueDate=2025-02-05&completed=true&title=Recording2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4Njc1MDIyLCJleHAiOjE3Mzg4NTUwMjJ9.OXx13dkmccSWE4tmy81NbdBOa4eM1nG84SaxcEMqooNAqMq-ihZ_EUgJe0SkVSB0cuEQGoITH2ZI_fzrgWz-Sg'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-02-04' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4Njc1MDIyLCJleHAiOjE3Mzg4NTUwMjJ9.OXx13dkmccSWE4tmy81NbdBOa4eM1nG84SaxcEMqooNAqMq-ihZ_EUgJe0SkVSB0cuEQGoITH2ZI_fzrgWz-Sg'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?completed=true' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4Njc1MDIyLCJleHAiOjE3Mzg4NTUwMjJ9.OXx13dkmccSWE4tmy81NbdBOa4eM1nG84SaxcEMqooNAqMq-ihZ_EUgJe0SkVSB0cuEQGoITH2ZI_fzrgWz-Sg'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4Njc1MDIyLCJleHAiOjE3Mzg4NTUwMjJ9.OXx13dkmccSWE4tmy81NbdBOa4eM1nG84SaxcEMqooNAqMq-ihZ_EUgJe0SkVSB0cuEQGoITH2ZI_fzrgWz-Sg'
```
