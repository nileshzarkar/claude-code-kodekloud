"
Create a fully working authentication system with a basic frontend (HTML + CSS + JavaScript) and a Quarkus backend, using H2 in-memory database.

🔸 Requirements
1. Backend (Quarkus)
Use REST APIs
Use H2 in-memory DB (auto schema generation)
Create User entity:
id
email (unique)
password (hashed using bcrypt)
isVerified (boolean)
verificationToken
Implement APIs:
POST /api/auth/register
GET /api/auth/verify-email?token=
POST /api/auth/login
GET /api/auth/me (JWT protected)
2. Security (MANDATORY)
Hash passwords using bcrypt
JWT:
signed token
expiry (e.g., 1 hour)
include userId + email
Protect endpoints using JWT filter
Validate:
email format
password length
Return proper HTTP status codes (400, 401, etc.)
3. Email Verification
Generate token during registration
Print verification link in backend console (simulate email)
Verification API activates user
4. Frontend (Simple HTML/JS)

Create 3 pages:

✅ register.html
Form: email + password
Calls /register
✅ login.html
Form: email + password
Stores JWT in localStorage
✅ dashboard.html
Calls /me
Shows user info
Redirects if not logged in
5. Frontend Behavior
Use fetch API
Store JWT in localStorage

Attach JWT in headers:

Authorization: Bearer <token>
Handle errors (invalid login, not verified, etc.)
Redirect between pages properly
6. Project Structure
Backend:
src/main/java/
  entity/
  repository/
  service/
  resource/
  security/
Frontend:
/frontend
  register.html
  login.html
  dashboard.html
  style.css
  app.js
7. Configuration
Provide:
application.properties (H2 + JWT secret)
Maven config
Enable H2 console
8. Run Instructions (VERY IMPORTANT)
Step-by-step:
Start Quarkus backend
Open HTML files in browser
Mention exact URLs and ports
9. Output Requirements
Generate complete working code
No placeholders
All files included
Copy-paste runnable
Include sample curl requests
"


This is more than vibe coding
🔹 What is “vibe coding”?
“Vibe coding” =
👉 You give a vague prompt
👉 AI generates code
👉 You don’t deeply understand or validate it
Example:
“Create login system in React”
That’s shallow. No constraints, no architecture, no security thinking.
🔹 Your prompt is different — why?
Your prompt:
“Create a complete authentication system… React + Quarkus + H2 + JWT + email verification”
This forces structured engineering thinking, not just code generation.
Your prompt is not vibe coding because it requires real engineering, not just code generation.
In short:
It covers a full system (React + Quarkus + DB + JWT + email)
It includes security (password hashing, tokens, verification)
It needs proper flows (register → verify → login)
It requires architecture + integration
👉 Vibe coding = quick, shallow code
👉 Your prompt = structured, real-world system design
🔹 One-line takeaway
This is engineering, not vibe coding—because it requires design, security, integration, and reasoning, not just code generation.

We can assume as humans that Claude knows security best practices, but on the same token Claude Code can assume that we dont need them. So you have to control assumptions as much as possible when you are building these types of prompts.
You’re basically pointing out a gap in assumptions between humans and AI.
Here’s the simplified idea 👇
🔹 The problem
Humans think: 👉 “AI already knows security best practices”
AI might think: 👉 “User didn’t mention security → maybe not needed”
⚠️ Result: Important things (like hashing, token expiry, validation) can be skipped.
🔹 Why this happens
AI follows your instructions literally.
If you don’t say: “hash passwords” , “use secure JWT” , “validate input”  
…it may:  simplify the solution or ignore those parts
🔹 What “control assumptions” means
👉 Don’t rely on AI to guess correctly
👉 Tell it exactly what matters
Instead of: “Build auth system”
Say: “Use hashed passwords, JWT expiry, secure endpoints, email verification”
🔹 Simple analogy
Vague prompt = “Cook something tasty” 🍲
Controlled prompt = “Cook hygienic food with proper ingredients and steps” ✅
🔹 One-line takeaway 
👉 AI is powerful, but it follows instructions—not assumptions. So you must clearly specify critical requirements like security.

🔹 What is happening here?
The person gives one detailed prompt
AI (Claude Code) doesn’t just write code
👉 It plans everything first
🔹 What AI is doing step-by-step
Assumes capabilities
It assumes it can: create files, run commands (like Bash, npm) , set up projects, Starts thinking like a developer Instead of coding immediately
👉 it creates a to-do list / plan
Analyzes project
Sees it’s an empty folder
Decides what to build: backend (Express) , database (MongoDB) , security setup
Adds important things automatically : installs dependencies, adds security (bcrypt, JWT), creates user model
builds: register, login, email verification
🔹 Key idea
👉 AI is not just writing code
👉 It is acting like a developer planning a full project
🔹 Why this is important
If you give a good prompt with clear assumptions:
AI builds complete system
Includes things you might forget (like security)
If prompt is weak:
AI builds incomplete system
🔹 One-line takeaway
👉 Good prompt → AI plans + builds like a real developer. Bad prompt → AI just dumps code