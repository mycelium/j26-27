## Spring Framework task

### Common requirements (for all variants):

#### 1. Sources and Artifacts
- **Version Control and Collaboration**
  - All code must be committed to a GitHub repository (one repository per team).
  - The repository must include a README.md file with a detailed guide on how to:
    - Set up the project locally.
    - Build and run the application.
    - Deploy the application using Docker.
    - Interact with the Telegram bot.
  - Commit history should reflect meaningful contributions from all team members.
- **Target Artifacts**
  - The application must produce a runnable fat JAR as the primary artifact.
  - The application must be deployable as a Docker container.
  - Use a build tool (Gradle, Maven or SBT) to manage dependencies, build, and package the application.
#### 2. Technology Stack
- **Core Technologies**
  - Telegram API: The application must integrate with the Telegram API to provide bot functionality.
  - LLM API: The application must integrate with an LLM API (e.g., OpenAI, Anthropic, GigaChat) to provide AI-powered functionality.
  - Programming Language (use one of the following):
    - Java 25
    - Kotlin
    - Scala 3
  - Framework:
    - Use Spring 7 for dependency injection and application structure.
    - **Spring Boot is strictly prohibited for all students.**
  - Message queue
    - Use message queue specified in google doc. **IF NEEDED**
- **Deployment and Containerization**
  - Use Docker to containerize the application.
  - Provide a Docker Compose file for local development and testing.
  - Publish the Docker image to Docker Hub for deployment.

#### 3. Code Quality and Best Practices
- **Code Style**
  - Adhere to a recognized Java code style guide (e.g., Google Java Style Guide, Oracle Code Conventions).
  - Bonus: Use static code analysis tools (e.g., Checkstyle, PMD, or SonarQube) to enforce code quality.
- **Modern Language Features**
  - Use modern language constructs and features available in the chosen language.
  - Code written in outdated styles (e.g., Java 1.5 style) will not be accepted.
- **Clean Code Principles**
  - Follow clean code principles: meaningful variable names, proper encapsulation, and modular design.
  - Avoid code duplication and ensure proper error handling.

#### 4. Submission Guidelines
- **To submit the coursework, provide the following:**
  - Requirements
  - Architecture design
  - GitHub Repository Link:
    - Include a link to the GitHub repository with the complete codebase.
    - Ensure the repository has a clear and detailed README.md file.
  - Docker Hub Link:
    - Provide a link to the Docker Hub repository where the Docker image is published.
  - Telegram Bot Link:
    - Share the link or username of the Telegram bot for testing and interaction.
- **Submission process**
  - **Step 1: Requirements**
    - Provide a detailed requirements document that outlines:
      - The purpose and functionality of the application.
      - Key features of the Telegram bot.
      - Non-functional requirements (e.g., performance, scalability, etc.).
      - Any assumptions or constraints.
    - This document should be clear, concise, and well-structured.
  - **Step 2: Architecture**
    - Submit an architecture document that includes:
      - A high-level overview of the system design.
      - Diagrams (e.g., component diagrams, sequence diagrams, or flowcharts) to illustrate the architecture.
      - Explanation of the chosen technology stack and its justification.
      - Description of how the application will be built, deployed, and run.
    - The architecture document should demonstrate a clear understanding of the system's structure and design decisions.
  - **Step 3: Full Project**
#### 5. Additional Notes
- **Teamwork:** All team members must contribute equally. The commit history should reflect individual contributions.
- **Documentation:** Proper documentation (in-code comments, README, etc.) is mandatory.
- **Testing:** While not explicitly required, writing unit tests or integration tests is highly encouraged.
- **Deadline:** Ensure the coursework is submitted before the deadline. Late submissions will not be accepted.
- **Specific requirements** (technical restrictions, etc): [google sheet](https://docs.google.com/spreadsheets/d/1ubs3omt2wO_xVBQOxGvMOkwLpKx6d-UFb9lJPf4FcTs/edit?gid=1080477820#gid=1080477820)
#### 6. Functional requirements
- **Common**
  - Authorization
    - via telegram
    - via http
  - Roles
    - `User`
    - `Admin`
  - Healthcheck endpoint (no authorization required)
    - `/healthcheck`
      - server status
      - list of authors (students)
  - Users endpoint (only for admins)
    - List of all users
- **Variant specific**
  - Must be done by yourself

### Variants


