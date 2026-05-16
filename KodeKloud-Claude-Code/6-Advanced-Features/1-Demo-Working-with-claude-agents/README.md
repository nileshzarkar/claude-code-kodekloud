There are 2 ways to create agents in Claude Code 
1. sub-agents that you can call from the CLI
2. Programmatically with Python using Claude Code SDK

Anthropic provides an official Java SDK for calling Claude APIs:
Anthropic Java SDK
Using this, you can build:
* AI agents
* tool calling
* workflows
* RAG systems
* multi-agent systems

❌ What Java Does NOT Have Yet
Java does not currently have an official equivalent of:
* Claude Code SDK
* Claude Code sub-agents framework
Those are officially available mainly in:
* Python
* TypeScript

Java Alternatives
Java developers usually use:
* LangChain4j
* Spring AI
together with Claude API to build agents.

Term	                        Simplified Meaning
Official Claude API SDK	        A library to simply talk to Claude from your code
Official Claude Agent SDK	    A framework to build smart autonomous agents using Claude
Build AI agents	                Create AI programs that can think, use tools, and perform tasks
Claude Code sub-agents	        Small specialized AI helpers inside Claude Code CLI

1. Official Claude API SDK
This is the basic connection library.
Example:
Your Java/Python app sends:
"Summarize this file"
Claude returns:
"Here is the summary"
So this SDK is mainly:
sending prompts
receiving responses
Think of it like:
Your App ↔ Claude API


2. Official Claude Agent SDK
This is more advanced.
Instead of just asking questions, you create an AI that can:
think step-by-step
use tools
call APIs
read files
make decisions
continue tasks automatically
Example:
User asks:
"Analyze logs and create report"
Agent:
1. Reads logs
2. Finds errors
3. Summarizes issues
4. Creates report
5. Saves file
So Agent SDK = framework for autonomous AI workflows.


3. Build AI Agents
An AI agent is simply:
AI + tools + memory + actions
Instead of only chatting, it can:
execute commands
query databases
search documents
call APIs
automate workflows
Example:
DevOps Agent
- checks Kubernetes
- reads logs
- detects failures
- suggests fixes

4. Claude Code Sub-agents
These exist inside Claude Code CLI.
Think of them like:
mini specialized coding assistants
Example:
one agent for debugging
one for writing tests
one for refactoring
one for documentation
Main agent delegates work:
Main Agent
   ├── Debug Agent
   ├── Test Agent
   └── Refactor Agent
These are mostly focused on coding workflows.


"
Create a simple quarkus based microservice which does the following 
1. A simple html and javascript based web page UI which allows the user to enter the name in the input box. 
2. The name goes to the back end and the service layer make the name UPPER CASE and appends Hello <NAME>, Good Morning, AfterNoon or Evening based on the time of the server where it is running .
Make this microservice inside the folder 
D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents
"

D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents\greeting-service> mvn clean compile

D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents\greeting-service> mvn quarkus:dev

http://localhost:8080/




In Claude Code CLI, the /agents command is used to manage and use sub-agents
Think of sub-agents as specialized AI helpers inside Claude Code.

Example:
One agent for backend code
One for Kubernetes
One for testing
One for documentation
Instead of giving long prompts every time, you create reusable agents with their own instructions.

What /agents lets you do
Typical uses:
View available agents
Create a new agent
Edit agent instructions
Select which agent to use
Example flow:
/agents

Simplified Analogy
Without agents:
Every time:
"Act as a Kubernetes expert and help debug Helm issues..."
With agents:
Use kubernetes-expert agent
The behavior is already predefined.

PS D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents> claude


 Agents  Running   Library 
  ❯ Create new agent
  No agents found. Create specialized subagents that Claude can delegate to.
  Each subagent has its own context window, custom system prompt, and specific tools.
  Try creating: Code Reviewer, Code Simplifier, Security Reviewer, Tech Lead, or UX Reviewer.

  Create new agent
  Choose location
  ❯ 1. Project (.claude/agents/)
    2. Personal (~/.claude/agents/)

1. Project Agents (.claude/agents/)
These agents belong to a specific project/repository.
Location:
    .claude/agents/
Inside your current project folder.
Characteristics
* Only available in that project
* Can be committed to Git
* Shared with team members
* Good for project-specific workflows
Example
Project:
    payment-service/
Agent:
    payment-debugger
Instructions may include:
* Use project architecture
* Follow company coding style
* Use specific APIs
* Follow repo conventions
Very useful for teams.

2. Personal Agents (~/.claude/agents/)
These are global agents for your user account.
Location:
    ~/.claude/agents/
(home directory)
Characteristics
* Available in all projects
* Not tied to a repository
* Personal to you
* Not shared automatically
Example
You create:
    kubernetes-expert
You can use it in:
* Project A
* Project B
* Random folders
* Practice environments

When to use which?
Use Project Agent when:
* Instructions depend on repo structure
* Team should share the agent
* Project has unique workflows
* Company conventions matter
Example:
    packet-writer-debugger
Use Personal Agent when:
* General reusable expertise
* Personal coding style
* Kubernetes helper
* Linux helper
* Java expert
Example:
    ckad-coach
    java-performance-expert



Create new agent
  Creation method
  ❯ 1. Generate with Claude (recommended)
    2. Manual configuration    

1. Generate with Claude (recommended)
Claude automatically creates the agent configuration for you.
You typically describe what you want:
Example:
    Create an agent for Kubernetes troubleshooting
Claude then generates:
* Agent name
* System prompt/instructions
* Behavior rules
* Tool preferences
Example generated agent:
"You are a Kubernetes troubleshooting expert.
Help diagnose pods, networking, Helm, and RBAC issues.
Prefer kubectl-based debugging steps."
Best for
* Beginners / Fast setup / Common workflows / Quick experimentation    

2. Manual configuration
You create the agent yourself.
Claude opens a config/template file that you edit manually.
You define:
Agent instructions
Behavior
Scope
Preferred workflow
Example:
---
name: java-performance-expert
description: JVM and performance tuning expert
---
You are an expert in:
- JVM tuning
- GC analysis
- Thread dumps
- Quarkus performance
Best for
* Advanced users / Precise control / Enterprise workflows / Custom agent engineering



 Create new agent
  Describe what this agent should do and when it should be used (be comprehensive for best results)
  e.g., Help me write unit tests for my code...



Create new agent
  Describe what this agent should do and when it should be used (be comprehensive for best results)

  Evaluate code for readability and namimg of the varibles, method names as per 
  Java standards 

   Create new agent
  Describe what this agent should do and when it should be used (be comprehensive for best results)
  ✢  Generating agent from description...

In Claude Code, tools define what capabilities the agent is allowed to use.
Think of them like permissions/capabilities for the AI agent.

What tools mean
An agent can:
* Read files
* Edit code
* Run terminal commands
* Use MCP servers
* Access integrations
The tool selection controls this.

| Tool                                         | Very Short Purpose                    |
| -------------------------------------------- | ------------------------------------- |
| **Bash**                                     | Run Linux shell commands              |
| **CronCreate**                               | Create scheduled jobs/tasks           |
| **CronDelete**                               | Delete scheduled jobs                 |
| **CronList**                                 | Show scheduled jobs                   |
| **Edit**                                     | Modify existing files                 |
| **EnterWorktree**                            | Enter Git worktree/project            |
| **ExitWorktree**                             | Exit current worktree                 |
| **Glob**                                     | Find files using patterns (`*.java`)  |
| **Grep**                                     | Search text/code inside files         |
| **Monitor**                                  | Watch logs/process/task status        |
| **NotebookEdit**                             | Edit Jupyter notebooks                |
| **PowerShell**                               | Run Windows PowerShell commands       |
| **PushNotification**                         | Send notifications/alerts             |
| **Read**                                     | Read/open files                       |
| **RemoteTrigger**                            | Trigger remote workflows/actions      |
| **ShareOnboardingGuide**                     | Share setup/onboarding docs           |
| **Skill**                                    | Use predefined AI skills/capabilities |
| **TaskCreate**                               | Create task/work item                 |
| **TaskGet**                                  | Get task details                      |
| **TaskList**                                 | List tasks                            |
| **TaskStop**                                 | Stop/cancel running task              |
| **TaskUpdate**                               | Update task status/details            |
| **ToolSearch**                               | Search available tools                |
| **WebFetch**                                 | Fetch webpage content                 |
| **WebSearch**                                | Search internet                       |
| **Write**                                    | Create new files                      |
| **authenticate (claude.ai Google Calendar)** | Connect Google Calendar               |
| **complete_authentication**                  | Finish login/auth flow                |
| **executeCode (ide)**                        | Run code in IDE                       |
| **getDiagnostics (ide)**                     | Get IDE/compiler errors/warnings      |


Create new agent
  Select tools
    [ Continue ]
  ────────────────────────────────────────
    ☐ All tools
    ☒ Read-only tools
    ☐ Edit tools
  ❯ ☒ Execution tools
    ☐ MCP tools
    ☐ Other tools
  ────────────────────────────────────────
    [ Show advanced options ]

     Create new agent
  Select model

  Model determines the agent's reasoning capabilities and speed.

  ❯ 1. Sonnet ✔             Balanced performance - best for most agents
    2. Opus                 Most capable for complex reasoning tasks
    3. Haiku                Fast and efficient for simple tasks
    4. Inherit from parent  Use the same model as the main conversation

Yes — the speaker is saying model choice affects cost, speed, and reasoning quality.    
This is especially relevant in:
* Multi-agent systems
* Claude Code teams
* API-based usage where token/model pricing matters.


  Create new agent
  Configure agent memory

    1. Project scope (.claude/agent-memory/) (Recommended)
  ❯ 2. None (no persistent memory)
    3. User scope (~/.claude/agent-memory/)
    4. Local scope (.claude/agent-memory-local/)

Select  None as of now 

Location: .claude\agents\java-readability-reviewer.md
  Tools: Glob, Grep, Read, TaskCreate, TaskGet, TaskList, TaskStop, TaskUpdate, WebFetch, WebSearch, and Bash
  Model: Sonnet
  Memory: None

  Description (tells Claude when to use this agent):

    Use this agent when Java code has been written or modified and needs to be evaluated for readability, naming conventions, and adherence to Java coding standards. This includes reviewing variable names, method
    names, class names, constants,…

  System prompt:

    You are an expert Java code quality reviewer with over 15 years of experience in Java development, software architecture, and code standards enforcement. You have deep expertise in the Oracle Java Code
    Conventions, Google Java Style Guide,…


  Press s or Enter to save, e to save and edit

D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents\greeting-service\.claude\agents\java-readability-reviewer.md


PS D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents\greeting-service> claude         
 ▐▛███▜▌   Claude Code v2.1.143
▝▜█████▛▘  Sonnet 4.6 with high effort · Claude Pro
  ▘▘ ▝▝    D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents\greeting-service

❯ /agents    

Agents  Running   Library 
    Create new agent
    Project agents (D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\6-Advanced-Features\1-Demo-Working-with-claude-agents\greeting-service\.claude\agents)
  ❯ java-readability-reviewer · sonnet

 java-readability-reviewer
  ❯ 1. Run agent
    2. View agent
    3. Edit agent
    4. Delete agent
    5. Back


Next how to use the agent ?
First lets make the code unreadable so agent can fix or suggest fixes.
@ApplicationScoped
public class GreetingService {

    public String greet(String name) {
        String upper_name = name.trim().toUpperCase();
        String time_of_day = gettimeofday();
        return "Hello " + upper_name + "! Good " + time_of_day + "!";
    }

    private String gettimeofday() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Afternoon";
        } else {
            return "Evening";
        }
    }
}



❯ /agents 
  ⎿  Agents dialog dismissed
❯ use the java readability reviewer agent to inspect GreetingService.java and give me a report 

In Claude Code agents, the:
Score: 6/10
is usually an agent-generated evaluation score, not an official Claude system metric.
It means the agent (here: Java Readability Reviewer) judged the code readability as:
Average / Needs Improvement

● Let me find the file first, then launch the reviewer.
  Searched for 1 pattern, read 1 file (ctrl+o to expand)
● java-readability-reviewer(Java readability review of GreetingService.java)
  ⎿  Done (0 tool uses · 13.5k tokens · 24s)
  (ctrl+o to expand)
● Here is the full report from the Java Readability Reviewer:
  ---
  GreetingService.java — Readability Review
  Score: 6/10
  Critical Issues (Java Standard Violations)
  ┌──────────────────────────┬────────────────────────────────────────────────────────────────────────────┬────────────────┐
  │        Identifier        │                                  Problem                                   │ Suggested Fix  │
  ├──────────────────────────┼────────────────────────────────────────────────────────────────────────────┼────────────────┤
  │ upper_name (line 10)     │ snake_case for a local variable — violates Java camelCase convention       │ formattedName  │
  ├──────────────────────────┼────────────────────────────────────────────────────────────────────────────┼────────────────┤
  │ time_of_day (line 11)    │ snake_case for a local variable                                            │ timeOfDay      │
  ├──────────────────────────┼────────────────────────────────────────────────────────────────────────────┼────────────────┤
  │ gettimeofday() (line 15) │ All-lowercase, no word separation — C/POSIX-style name, not idiomatic Java │ getTimeOfDay() │
  └──────────────────────────┴────────────────────────────────────────────────────────────────────────────┴────────────────┘
  Major Issue
  - greet() silently uppercases the caller's input, producing "Hello ALICE!" for "Alice". Renaming upper_name → formattedName at least signals that transformation is happening.

  Minor Suggestions
  - The string literals "Morning", "Afternoon", "Evening" are magic strings. Extract them as private static final constants for maintainability.

  What's Done Well
  - Class name GreetingService and package name follow conventions correctly.
  - The two-method structure (public entry point + private helper) reflects good single-responsibility thinking.
  - hour is a concise, appropriate local variable name.

  Recommended Refactor
  @ApplicationScoped
  public class GreetingService {

      private static final String MORNING   = "Morning";
      private static final String AFTERNOON = "Afternoon";
      private static final String EVENING   = "Evening";

      public String greet(String name) {
          String formattedName = name.trim().toUpperCase();
          String timeOfDay     = getTimeOfDay();
          return "Hello " + formattedName + "! Good " + timeOfDay + "!";
      }

      private String getTimeOfDay() {
          int hour = LocalTime.now().getHour();
          if (hour >= 5 && hour < 12) {
              return MORNING;
          } else if (hour >= 12 && hour < 17) {
              return AFTERNOON;
          } else {
              return EVENING;
          }
      }
  }

  The three naming fixes (upper_name, time_of_day, gettimeofday) are the priority — they are direct violations of Java's camelCase standard.
✻ Cooked for 48s

❯ apply the suggested fixes to GreetingService.java

Main advantage of agents in Claude Code:
Reusable specialized automation
Instead of repeating long prompts every time, the agent already remembers:
* Role
* Instructions
* Rules
* Workflow
* Tools

| Advantage             | Meaning                                |
| --------------------- | -------------------------------------- |
| Reusable prompts      | No repeated copy-paste                 |
| Consistent reviews    | Same standards every time              |
| Faster workflow       | One command does complex tasks         |
| Specialized expertise | Dedicated agents for Java/K8s/Security |
| Team sharing          | Whole team uses same agent             |
| Automation            | Can run audits/debugging automatically |
| Memory                | Remembers project conventions          |
| Tool integration      | Can read/edit/run commands             |


Real Examples
Security audit agent
Checks:
    SQL injection
    Secrets exposure
    API security
    Infra issues
Kubernetes agent
Runs:
    kubectl describe
    kubectl logs
    helm checks
Java readability reviewer
Reviews:
    Naming
    Complexity
    Maintainability
    Clean code

Biggest Practical Benefit
Agents convert:
Long repeated prompts
into:
Reusable intelligent workflows





    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.agentscope</groupId>
                <artifactId>agentscope-dependencies-bom</artifactId>
                <version>1.0.12</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>


Using Anthropic Java SDK programmatically, you can build complete AI applications and agent systems in Java.
Main Things You Can Do
1. Chat with Claude
Basic AI conversations.
client.messages().create(...)
Use cases:
    Chatbots
    Q&A systems
    Internal assistants

2. Streaming Responses
Receive tokens live while Claude generates.
Use cases:
    Real-time chat UI
    Live coding assistant
    CLI assistants

3. Tool Calling (Function Calling)
Claude can request Java functions/tools.
Example: "Get Kubernetes pod logs"
Claude decides: getPodLogs(namespace, pod)
Use cases:
    Agentic AI
    DevOps assistants
    Automation systems

4. Build AI Agents
Create autonomous/reasoning agents.
Example agents:
    Kubernetes debugger
    Java code reviewer
    Security auditor
    Pulsar monitoring agent

5. Multi-Agent Systems
Multiple AI agents collaborate.
Example:
Planner Agent
↓
Code Agent
↓
Review Agent
↓
Security Agent

6. Structured JSON Output
Ask Claude to return JSON.
Example:
{
  "severity": "HIGH",
  "issue": "SQL Injection"
}
Useful for:
    Automation
    Pipelines
    APIs
    Rule engines

7. Code Generation
Generate:
    Java
    YAML
    Helm charts
    Kubernetes manifests
    SQL
    Scripts

8. Code Review / Auditing
Automated review systems.
Example:
    Readability scoring
    Security analysis
    Performance review
    Best-practice validation

9. RAG Applications
Connect Claude with your documents.
Example:
    PDFs + Vector DB + Claude
Use cases:
    Internal company search
    Knowledge assistants
    Documentation bots

10. Kubernetes / DevOps AI
Very powerful for your domain.
Example workflow:
    Claude →
    kubectl logs →
    Analyze failure →
    Suggest fix →
    Generate YAML patch

11. AI Workflow Automation
Automate operational tasks.
Examples:
    Incident analysis
    Release notes
    Jira summaries
    Log analysis
    Root cause analysis

12. IDE / CLI Assistants
Build:
    Terminal AI assistants
    IntelliJ plugins
    VSCode integrations

13. Memory + Context Systems
Maintain long-term conversation/project memory.
Example:
    Remember architecture decisions
    Remember coding conventions

14. Image Understanding (Vision)
Claude can analyze images/screenshots.
Examples:
    Kubernetes dashboards
    Architecture diagrams
    Error screenshots
    UI review

15. Enterprise Integrations
Integrate with:
    GitHub
    Slack
    Jira
    Databases
    Internal APIs

16. AI-Powered Microservices
Perfect for Quarkus/Spring Boot.
Example:
POST /analyzeLogs
POST /reviewCode
POST /generateHelm
Architecture Example
Java App
   ↓
Anthropic SDK
   ↓
Claude API
   ↓
Tool Calling
   ↓
kubectl / DB / APIs

Set ANTHROPIC_API_KEY  as SYSTEM ENvironment Variable

All three changes are done. Here's what was created:

pom.xml — added com.anthropic:anthropic-java:2.27.0 as a runtime dependency.

JavaReadabilityReviewerService.java — the programmatic agent:

System prompt is the full text from the .claude/agents/java-readability-reviewer.md definition, marked with CacheControlEphemeral so it's cached on the Anthropic side after the first call (saves tokens on repeated reviews)
Uses AnthropicOkHttpClient.fromEnv() to pick up ANTHROPIC_API_KEY from the environment
Uses Model.CLAUDE_SONNET_4_6 to match the model: sonnet setting in the agent spec
The review(String javaCode) method returns the full markdown review text
JavaReadabilityReviewerResource.java — REST endpoint at POST /api/review:

https://platform.claude.com/settings/workspaces/default/keys

Accepts {"code": "..."} JSON body in SINGE LINE 
Returns {"review": "..."} JSON response
To test it, set ANTHROPIC_API_KEY in your environment, start Quarkus with ./mvnw quarkus:dev, then:


curl -X POST http://localhost:8080/api/review \
  -H "Content-Type: application/json" \
  -d '{"code": "public class usr { int x; void proc() {} }"}'

https://platform.claude.com/docs/en/api/sdks/java

Here’s a very simplified explanation of each topic from [the
](https://platform.claude.com/docs/en/api/sdks/java)

1. Installation
1.3 Client configuration
How to create and configure the Claude Java client in your application.
Purpose:
Used to connect your Java app with Claude APIs.
1.4 API key setup
How to add your Anthropic API key securely.
Purpose:
Used for authentication so Claude knows who is making requests.
1.5 Configuration options
Settings like timeout, retries, base URL, etc.
Purpose:
Customize SDK behavior according to your application needs.

2. Modifying Configuration
2.1 Async usage
Running API calls asynchronously without blocking the main thread.
Purpose:
Improve performance in scalable applications.
2.2 Streaming
Receive Claude responses gradually instead of waiting for the full response.
Purpose:
Useful for chatbots and real-time AI output.
2.2.1 Synchronous streaming
Stream responses in a blocking/simple way.
Purpose:
Easy implementation for sequential processing.
2.2.2 Asynchronous streaming
Stream responses using async/non-blocking methods.
Purpose:
Better for high-performance concurrent applications.
2.2.3 Streaming with message accumulator
Collect streamed chunks into one complete message.
Purpose:
Helps reconstruct the final response cleanly.

3. Structured Outputs
3.1 Tool use
Allow Claude to call external functions/tools.
Purpose:
Integrate AI with real application logic.
3.1.1 Defining tools with annotations
Create tools using Java annotations.
Purpose:
Simplifies tool registration and metadata creation.
3.1.2 Calling tools
Execute tools requested by Claude.
Purpose:
Allows Claude to perform actions like DB queries or API calls.
3.1.3 Tool name conversion
Customize tool naming rules.
Purpose:
Keeps tool names clean and compatible.
3.1.4 Local tool JSON schema validation
Validate tool input structure locally.
Purpose:
Prevents invalid tool calls before sending requests.
3.1.5 Annotating tool classes
Add annotations directly on tool classes.
Purpose:
Makes tool configuration easier and organized.

4. Message Handling
4.1 Message batches
Send multiple requests together.
Purpose:
Improves efficiency for bulk operations.
4.2 File uploads
Upload files to Claude.
Purpose:
Allows document/image analysis.
4.2.1 Binary responses
Handle binary data like files/images.
Purpose:
Useful when responses are not plain text.

5. Error Handling & Reliability
5.1 Error handling
Manage API exceptions and failures.
Purpose:
Prevents application crashes.
5.2 Status code mapping
Maps HTTP status codes to Java exceptions.
Purpose:
Makes debugging easier.
5.3 Request IDs
Unique IDs for API requests.
Purpose:
Helps trace/debug requests with support teams.
5.4 Retries
Automatically retry failed requests.
Purpose:
Improves reliability during temporary failures.
5.5 Timeouts
Limit how long requests wait.
Purpose:
Avoids hanging requests.
5.6 Long requests
Handle requests taking more time.
Purpose:
Useful for large AI processing tasks.

6. Pagination
6.1 Auto-pagination
SDK automatically loads next pages.
Purpose:
Simplifies reading large datasets.
6.2 Manual pagination
Developer manually controls page loading.
Purpose:
Gives better control over data fetching.

7. Type System
7.1 Immutability and builders
Uses immutable objects with builder patterns.
Purpose:
Safer and cleaner Java code.
7.2 Requests and responses
Java models for API input/output.
Purpose:
Provides structured communication.
7.3 Undocumented parameters
Send custom parameters not officially documented.
Purpose:
Useful for experimental/internal features.
7.4 JsonValue creation
Create raw JSON values manually.
Purpose:
Useful for dynamic/custom payloads.
7.5 Forcibly omitting required parameters
Skip required fields intentionally.
Purpose:
Advanced customization/testing scenarios.
7.6 Response properties
Access response metadata and fields.
Purpose:
Read AI response details properly.
7.7 Response validation
Validate API responses.
Purpose:
Ensures response data correctness.

8. HTTP Client Customization
8.1 Proxy configuration
Use proxies for network requests.
Purpose:
Needed in enterprise/corporate environments.
8.2 HTTPS / SSL configuration
Customize SSL certificates/security.
Purpose:
Secure API communication.
8.3 Custom HTTP client
Use your own HTTP client implementation.
Purpose:
Advanced networking customization.

9. Platform Integrations
Integration support with different platforms/frameworks.
Purpose:
Helps Claude work with existing ecosystems.

10. Advanced Usage
10.1 Raw response access
Access original HTTP/API response.
Purpose:
Useful for debugging and advanced processing.
10.2 Logging
Enable request/response logs.
Purpose:
Monitor and debug SDK activity.
10.3 Undocumented API functionality
Use internal or advanced API features.
Purpose:
Experimental/custom implementations.

11. Beta Features
Features still under testing/development.
Purpose:
Try new capabilities early.

12. Frequently Asked Questions
Common user questions and answers.
Purpose:
Quick troubleshooting/help.
12.1 Semantic versioning
Explains SDK version numbering rules.
Purpose:
Understand compatibility and upgrades.


