# RentRead
RentRead is a RESTful api service that manages an online book rental system.

## Application features
  * Enables a user to register. A User by default is assigned a USER role.
  * Privileged access is available to the users with ADMIN role.Admin can add, update or remove a book from the rental system.
  * All users are required to authenticate before using the book rental service.
  * Any signed in user can browse available books and rent them. A user can have at most two active rents, any further rental request requires a user to return previously rented book. 

## Tech stack and tools
  * Java, Spring boot, Spring Security, MySQL ,lombok ,Mockito , Junit.

## Getting Started

  * Download the repository on local machine.
  * Import the downloaded project in your editor, ex- Intellij, Eclipse, or VSCode.
  * The project uses MySQL as the database, so make sure to have a running instance on MySQL on your localhost or have access to cloud instance.
  * Make changes to the application.properties file to add your database credentials.
  * The project is setup using embedded tomcat container, so project can be run directly through editor without setting up external application server.
  * Additionally, project could also be executed by invoking the project jar file. Follow the below steps
    * Run command
      * `./gradlew clean build`.
    * Make sure you are in the root directory of project and execute
      * `java -jar ./build/libs/rentread-0.0.1-SNAPSHOT.jar`

## API Endpoints
  * User Registration - `POST /auth/register`. Use the below request body for post request
    * ```
          firstName:"",
          lastName:"",
          email:"",
          password:"",
          role:""
     ```
  * Add Book to the library - `POST /books` . This request requires user to send username(in this case username is the email id) and password in authorization header . Also, the role of this user must be ADMIN. User with other roles are not authorized for this operation.
    *  ```
         title:"",
         author:"",
         genre:"",
         availabilityStatus:""
       ```
  * Browse all books available for rent - `GET /books`.This request requires user to send username(in this case username is the email id) and password in authorization header. A user with both USER and ADMIN role are authorized to access this resource.
  * Rent a book - `POST /books/{bookId}/rent` . This request requires user to send username(in this case username is the email id) and password in authorization header. A user with both USER and ADMIN role are authorized to access this resource.
  * Return a book - `POST /books/{bookId}/return`. This request requires user to send username(in this case username is the email id) and password in authorization header. A user with both USER and ADMIN role are authorized to access this resource.
