
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
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4NDk5Mzc2LCJleHAiOjE3Mzg1MDExNzZ9.VW8dIIgw2gVLGNhHRF_1MHR0fdDkgYyGpTz_WWvCZ3e1J3iEDwBOXugh0wIJIAGk-2hmHc9tIb9491j6lkdVvw' \
--header 'Content-Type: application/json' \
--data '{
  "title": "My Recording1",
  "comment": "This is a test recording1",
  "dueDate": "2025-02-25",
  "completed": false
}'

curl --location 'http://localhost:8080/api/user/todos' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4NDk5Mzc2LCJleHAiOjE3Mzg1MDExNzZ9.VW8dIIgw2gVLGNhHRF_1MHR0fdDkgYyGpTz_WWvCZ3e1J3iEDwBOXugh0wIJIAGk-2hmHc9tIb9491j6lkdVvw' \
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
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4MzM2NjcwLCJleHAiOjE3MzgzMzg0NzB9.L1tAQ66tvz5H4L-75swXxU5zXM_6mA0PyoggJhFPrDw0JJleAwz-7Ei1Vq9GelE4iHN3-jibrvglsHCt_iWX1Q' \
--header 'Content-Type: application/json'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-01-26&dueDate=2025-12-30&completed=false' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4MzM2NjcwLCJleHAiOjE3MzgzMzg0NzB9.L1tAQ66tvz5H4L-75swXxU5zXM_6mA0PyoggJhFPrDw0JJleAwz-7Ei1Vq9GelE4iHN3-jibrvglsHCt_iWX1Q'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?createdDate=2025-01-31&dueDate=2025-02-05&completed=true&title=recording' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4MzM2NjcwLCJleHAiOjE3MzgzMzg0NzB9.L1tAQ66tvz5H4L-75swXxU5zXM_6mA0PyoggJhFPrDw0JJleAwz-7Ei1Vq9GelE4iHN3-jibrvglsHCt_iWX1Q'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter?completed=true' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4MzM2NjcwLCJleHAiOjE3MzgzMzg0NzB9.L1tAQ66tvz5H4L-75swXxU5zXM_6mA0PyoggJhFPrDw0JJleAwz-7Ei1Vq9GelE4iHN3-jibrvglsHCt_iWX1Q'
```

```
curl --location 'http://localhost:8080/api/user/todos/filter' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXgxMjMzMjFAZ21haWwuY29tIiwiaWF0IjoxNzM4NDk5Mzc2LCJleHAiOjE3Mzg1MDExNzZ9.VW8dIIgw2gVLGNhHRF_1MHR0fdDkgYyGpTz_WWvCZ3e1J3iEDwBOXugh0wIJIAGk-2hmHc9tIb9491j6lkdVvw'
```
