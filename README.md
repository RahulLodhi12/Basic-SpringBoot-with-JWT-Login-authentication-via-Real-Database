- Very Basic Role-Based (ADMIN or USER) Login Authentications Project via Real Database.
---------------------------------------------------------------------------------------------
Here’s a complete flow of code from the main() method for your JWT-based Spring Boot project:

✅ 1. Application Entry Point
public static void main(String[] args) {
	SpringApplication.run(SpringbootStarterWithJwtApplication.class, args);
}
- This initializes the Spring Boot application context, auto-scans all packages under com.tcs.training, and configures all beans, components, filters, and controllers.

✅ 2. Spring Boot Auto-Configuration
- Spring auto-configures the application using:
    - @SpringBootApplication (includes @ComponentScan, @Configuration, and @EnableAutoConfiguration)
- All packages (like bean, controller, config, etc.) are scanned and beans are created and wired.

✅ 3. Bean Initialization

🔹 SecurityConfig is loaded:
- Defines URL access rules using HttpSecurity.
- Configures routes:
    - "/authenticate" and "/welcome" are public
    - "/admin/users" requires ROLE_ADMIN
    - "/user-data" requires ROLE_USER

- Adds custom filter JwtFilter before Spring’s default authentication filter.
- Disables session creation (stateless).

✅ 4. Filter Chain Setup

🔹 JwtFilter is added:
- It intercepts every request.
- Checks if the request has a header like:
- Authorization: Bearer <JWT_TOKEN>

If present:
    - Extracts username from token.
    - Loads user from DB via MyUserDetailsService.
    - Sets the SecurityContext with authenticated user.

✅ 5. UserDetails Service

🔹 MyUserDetailsService:
- Implements UserDetailsService used by Spring Security.
- Fetches user by username using SignupRepo.
- Returns a MyUserDetails object with username, password, and roles.

✅ 6. JWT Utility

🔹 JwtUtil:
- Generates JWT token using username (generateToken()).
- Validates token and extracts subject (username) from it.

✅ 7. Authentication Flow

🔹 Login Endpoint: POST /authenticate
- Defined in SignUpController:
@PostMapping("/authenticate")
public String createAuthToken(@RequestBody AuthRequest authRequest)

- Flow:
1. Accepts { "username": "abc", "password": "xyz" }.
2. Authenticates using "AuthenticationManager".
3. If success:
      - Loads user from DB using "MyUserDetailsService".
      - Generates a JWT token.
      - Returns the JWT as response.

✅ 8. Accessing Protected Resources

🔹 Example: GET /admin/users or /user-data

- Steps:
1. Client sends request with:
    Authorization: Bearer <JWT_TOKEN>
2. "JwtFilter" extracts and validates the token.
3. "SecurityContext" is populated with authenticated "UserDetails".
4. Controller methods now recognize the authenticated user and can access username or role.

✅ 9. User Repository

🔹 SignupRepo:
- Interface to the real database.
- Auto-implemented by Spring Data JPA.
- Used to fetch user by username (during login or authorization checks).

✅ 10. Data Layer

🔹 Signup (Entity):
- Represents user record with id, username, password, role.


✅ End-to-End Flow Recap:

main()
 └─ Spring Boot starts
     └─ SecurityConfig sets rules and adds JwtFilter
         └─ JwtFilter checks every request
             ├─ If login (/authenticate):
             │    └─ AuthManager → DB → JWT → Return token
             └─ If secured endpoint:
                  └─ Validate JWT → set user in context → execute controller
                  
🔁 Real-life Request Examples
➤ Login
POST /authenticate
Body: { "username": "rahul", "password": "1234" }
→ Returns: JWT token

➤ Access Admin Resource
GET /admin/users
Headers: Authorization: Bearer <JWT_TOKEN>
→ Returns: All users if token is valid and user has ROLE_ADMIN
