# VoteSecure — Setup & Deployment Guide

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| JDK | 11 or 17 | https://adoptium.net |
| Apache Tomcat | 10.1.x | https://tomcat.apache.org |
| MySQL | 8.0+ | https://dev.mysql.com/downloads |
| Maven | 3.8+ | https://maven.apache.org |

---

## Step 1 — Database Setup

```sql
-- Run in MySQL client (mysql -u root -p)
SOURCE voting_system_db.sql;
```

Verify:
```sql
USE voting_system;
SELECT * FROM users;
SELECT * FROM elections;
```

---

## Step 2 — Configure DB credentials

Edit `src/main/resources/db.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/voting_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

> Never commit real passwords to Git. Add `db.properties` to `.gitignore`.

---

## Step 3 — Build the WAR

```bash
cd OnlineVotingSystem
mvn clean package -DskipTests
```

Output: `target/OnlineVotingSystem.war`

---

## Step 4 — Deploy to Tomcat

### Option A — Copy WAR (recommended)
```bash
cp target/OnlineVotingSystem.war $TOMCAT_HOME/webapps/
$TOMCAT_HOME/bin/startup.sh        # Linux/Mac
$TOMCAT_HOME/bin/startup.bat       # Windows
```
Tomcat auto-deploys. Access at: http://localhost:8080/OnlineVotingSystem

### Option B — Maven embedded Tomcat
```bash
mvn tomcat10:run
```
Access at: http://localhost:8080/voting

### Option C — Eclipse/IntelliJ
1. Import as Maven project
2. Add Tomcat 10 server in IDE
3. Add project to server → Run

---

## Step 5 — Test Login

| Role  | Email              | Password   |
|-------|--------------------|------------|
| Admin | admin@vote.com     | Admin@123  |
| Voter | alice@vote.com     | Voter@123  |
| Voter | bob@vote.com       | Voter@123  |

---

## URL Reference

| URL | Access | Description |
|-----|--------|-------------|
| `/` | Public | Landing page |
| `/login` | Public | Login form |
| `/register` | Public | Voter registration |
| `/voter/dashboard` | VOTER | View elections, cast votes |
| `/vote?electionId=X` | VOTER | Ballot for election X |
| `/results?electionId=X` | VOTER (closed) / ADMIN | Election results |
| `/admin/dashboard` | ADMIN | Stats overview |
| `/admin/elections` | ADMIN | Manage elections |
| `/admin/elections/new` | ADMIN | Create election |
| `/admin/candidates?electionId=X` | ADMIN | Manage candidates |
| `/logout` | Auth | Invalidate session |

---

## Common Issues

### "No suitable driver" error
Ensure `mysql-connector-j-8.x.x.jar` is in `WEB-INF/lib/` or Maven packaged it into the WAR. Run `mvn dependency:tree` to verify.

### "Cannot find db.properties"
The file must be at `src/main/resources/db.properties` so Maven copies it to `WEB-INF/classes/` inside the WAR.

### BCrypt class not found
Ensure `org.mindrot:jbcrypt:0.4` is in `pom.xml` and was downloaded. Run `mvn dependency:resolve`.

### Tomcat 10 vs Tomcat 9
This project uses **Jakarta EE 5** (`jakarta.servlet.*`). It requires **Tomcat 10+**. Tomcat 9 uses `javax.servlet.*` — you'll get `ClassNotFoundException` at runtime if you use Tomcat 9.

### Port 8080 already in use
Edit `$TOMCAT_HOME/conf/server.xml`, change `port="8080"` to `port="8090"` (or any free port).

---

## Project Structure (final)

```
OnlineVotingSystem/
├── pom.xml
├── SETUP.md
├── voting_system_db.sql
└── src/
    └── main/
        ├── java/com/voting/
        │   ├── controller/
        │   │   ├── RegisterServlet.java
        │   │   ├── LoginServlet.java
        │   │   ├── LogoutServlet.java
        │   │   ├── VoterDashboardServlet.java
        │   │   ├── VoteServlet.java
        │   │   ├── ResultServlet.java
        │   │   └── AdminServlet.java
        │   ├── dao/
        │   │   ├── UserDAO.java
        │   │   ├── ElectionDAO.java
        │   │   ├── CandidateDAO.java
        │   │   └── VoteDAO.java
        │   ├── model/
        │   │   ├── User.java
        │   │   ├── Election.java
        │   │   ├── Candidate.java
        │   │   └── Vote.java
        │   ├── filter/
        │   │   ├── AuthFilter.java
        │   │   └── AdminFilter.java
        │   └── util/
        │       ├── DBConnection.java
        │       └── AppStartupListener.java
        ├── resources/
        │   └── db.properties
        └── webapp/
            ├── index.jsp
            ├── login.jsp
            ├── register.jsp
            ├── css/
            │   ├── style.css
            │   ├── voter.css
            │   └── admin.css
            ├── js/
            │   ├── main.js
            │   ├── validation.js
            │   ├── timer.js
            │   └── admin.js
            └── WEB-INF/
                ├── web.xml
                └── views/
                    ├── common/
                    │   ├── header.jsp
                    │   ├── footer.jsp
                    │   └── error.jsp
                    ├── voter/
                    │   ├── dashboard.jsp
                    │   ├── vote.jsp
                    │   └── results.jsp
                    └── admin/
                        ├── dashboard.jsp
                        ├── manage-elections.jsp
                        ├── manage-candidates.jsp
                        └── results.jsp
```
