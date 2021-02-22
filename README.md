**Spring Boot - User Service**
----
Feature - Loading of CSV file and save into Database\
Select, Insert, Update, Delete records

### Run Spring Boot application
Requirement: Java 11

```
mvn clean package spring-boot:run
```

---

* **URL**
  `/users`

* **Method:**

  `GET` | `POST`
  
*  **URL Params**
    When no parameters are provided, all results are fetched.

   **Optional:**
 
   `minSalary=[float]`: Lower bound of salary filtered result. Default value: **0**\
   `maxSalary=[float]`: Upper bound of salary filtered result. Default value: **Integer.MAX_VALUE**\
   `limit=[int]`: Size of subset result per request. Default value: **30**\
   `offSet=[int]`: Pagination of result based on the limit size. Default value: **0**


* **Data Params**

  ```
  {
    "id": "e9999",
    "login": "test",
    "salary": "1020.00",
    "name": "Test User"
  }
  ```

* **Success Response:**
  * **Code:** 200\
    **Content:** `{"result":[{"id":"e0001","login":"hpotter","name":"Harry Potter","salary":1234.0},{"id":"e0002","login":"rwesley","name":"Ron Weasley","salary":19234.5}]}`
 
* **Error Response:**

  * **Code:** 400 BAD REQUEST\
    **Content:** `{ error : "ERROR MESSAGE" }`

---

* **URL**
  `/users/upload`

* **Method:**
    `POST`

* **Header:**
    `"Content-Type": "text/csv"`

* **Data Params**

  ```
  {
    "file": "@File.csv
  }
  ```

  File.csv
  ```
  id,login,name,salary
  e0001,hpotter,Harry Potter,1234.00
  #comments works here too
  e0002,rwesley,Ron Weasley,19234.50
  ```


* **Success Response:**
  * **Code:** 200\
    **Content:** `{"message": "Uploaded the file successfully: data.csv"}`
 
* **Error Response:**

  * **Code:** 400 BAD REQUEST\
    **Content:** `{ "message" : "ERROR MESSAGE" }`

---

* **URL**
  `/users/{id}`

* **Method:**
    `GET` | `PATCH` | `DELETE`


* **Response:**
    `GET`
  * **Code:** 200\
    **Content:** `{"id":"e0001","login":"hpotter","name":"Harry Potter","salary":1234.0}`

  * **Code:** 400\
    **Content:** `{"message":"message"}`

  </br>

    `PATCH` | `DELETE`
  * **Code:** 200\
    **Content:** `{"message":"Employee ID %s is successfully deleted"}`
 
   * **Code:** 400\
    **Content:** `{"message":"message"}`


---

* **URL**
  `/users/download`

* **Method:**
    `GET`

* **Response:**
    `GET`
  * **Code:** 200\
    **Content:** users.csv
    ```
    id,login,name,salary
    e0001,username_a_0001,User Name is not number 0001,10001.0
    e0002,username_a_0002,User Name is not number 0002,10002.0
    ```

