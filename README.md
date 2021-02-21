**Spring Boot - User Service**
----
Feature - Loading of CSV file and save into Database

### Run Spring Boot application
```
mvn clean spring-boot:run
```

* **URL**

  `/users`

* **Method:**
  
  <_The request type_>

  `GET`
  
*  **URL Params**

   **Optional:**
 
   `minSalary=[float]`: Lower bound of salary filtered result. Default value: **0**
   `maxSalary=[float]`: Upper bound of salary filtered result. Default value: **Integer.MAX_VALUE**
   `limit=[int]`: Size of subset result per request. Default value: **30**
   `offSet=[int]`: Pagination of result based on the limit size. Default value: **0**


* **Data Params**

  <_If making a post request, what should the body payload look like? URL Params rules apply here too._>

* **Success Response:**
  
  <_What should the status code be on success and is there any returned data? This is useful when people need to to know what their callbacks should expect!_>

  * **Code:** 200 <br />
    **Content:** `{ id : 12 }`
 
* **Error Response:**

  <_Most endpoints will have many ways they can fail. From unauthorized access, to wrongful parameters etc. All of those should be liste d here. It might seem repetitive, but it helps prevent assumptions from being made where they should be._>

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "Log in" }`

  OR

  * **Code:** 422 UNPROCESSABLE ENTRY <br />
    **Content:** `{ error : "Email Invalid" }`

* **Sample Call:**

  <_Just a sample call to your endpoint in a runnable format ($.ajax call or a curl request) - this makes life easier and more predictable._> 

* **Notes:**

  <_This is where all uncertainties, commentary, discussion etc. can go. I recommend timestamping and identifying oneself when leaving comments here._> 
