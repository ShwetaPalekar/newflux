Absolutely. Below is the **same response converted into `README.md` format**, with the introductory section you specified removed. I’ve kept the substance and structure intact, while making the Markdown suitable for dropping directly into a Git repository.

# Spec-Driven Engineering Preparation


---

# 1. Your objective for the next 35 days

By 14 November, you should be able to take an ambiguous business requirement and independently drive this entire process:

**Business problem → Requirements → Technical specification → Task breakdown → AI-assisted implementation → Automated testing → Independent review → Production readiness**

You should be comfortable doing this using both:

- **Claude Code:** Primary tool for specification-driven development and agent orchestration
- **Cursor:** IDE-based environment for implementation, debugging, code navigation, and reviewing changes

The objective isn't to become an expert at writing prompts.

It's to learn how to make AI consistently produce code that meets explicit requirements, passes meaningful tests, and remains maintainable.

## What you should be capable of by joining day

| Capability | Target |
|---|---|
| Specification writing | Convert ambiguous requirements into testable acceptance criteria |
| Java | Implement and review production-quality Java 17+ code |
| Spring Boot | Build REST APIs with validation, transactions, persistence and error handling |
| Database | Design entities, relationships, constraints and migrations |
| Testing | Use JUnit 5, Mockito and integration tests effectively |
| AI-assisted development | Drive implementation through Claude Code and Cursor |
| Agent orchestration | Separate implementation, testing and independent review responsibilities |
| Architecture | Make and document trade-offs involving performance, reliability and maintainability |
| Feature leadership | Break a feature into deliverable tasks and identify dependencies and risks |
| Engineering quality | Verify security, observability, edge cases and operational readiness |

Notice that this combines coding and leadership. That's important for an L5 role where you are expected to write code while influencing technical direction.

---

# 2. The project you will build

Build a small but realistic **Strata Operations Platform**.

This is a deliberate choice because the new team works on a strata operational platform. You can practise relevant business workflows without needing access to actual internal code or information.

The application will be your personal engineering laboratory.

## Project: Strata Operations API

Build a Spring Boot backend that supports strata managers handling:

- Buildings
- Maintenance requests
- Contractors
- Work orders

## Core business workflow

Imagine a strata manager reports a leaking pipe in a building.

The system must:

1. Register a maintenance request.
2. Associate it with the correct building and lot.
3. Assign a priority.
4. Assign a contractor.
5. Track the work order through its lifecycle.
6. Record status changes and an audit history.
7. Prevent invalid transitions.
8. Expose APIs for a frontend to consume.

This deceptively simple workflow provides plenty of opportunities to practise production engineering.

## Suggested technology stack

| Layer | Technology |
|---|---|
| Language | Java 21, while remaining familiar with Java 17 compatibility |
| Backend | Spring Boot 3.x |
| API | REST, JSON, Bean Validation |
| Persistence | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Database migrations | Flyway |
| Unit testing | JUnit 5, Mockito, AssertJ |
| Integration testing | Spring Boot Test, Testcontainers |
| API documentation | OpenAPI |
| Build | Maven |
| CI | GitHub Actions |
| AI development | Claude Code and Cursor |
| Optional frontend | Angular with TypeScript |

Use a consistent, supported set of dependency versions rather than spending time chasing the newest releases.

## Initial domain model

Start with these entities:

- `Building`
- `Lot`
- `MaintenanceRequest`
- `Contractor`
- `WorkOrder`
- `AuditEvent`

```text
Building
   |
   +---- Lot
   |
   +---- MaintenanceRequest
               |
               +---- WorkOrder
                          |
                          +---- Contractor
```

The audit history records important business changes.

You don't need to implement every entity immediately. The model will evolve as you develop the specifications.

## Business rules

Your specifications should eventually address questions such as:

- Can a maintenance request exist without a building?
- Can a work order be assigned to an inactive contractor?
- Can a completed work order be reopened?
- What happens if two users attempt to update the same work order?
- What happens if a request is submitted twice?
- How do we prevent duplicate work orders?
- What happens if a database transaction fails halfway through an operation?
- Who can assign contractors or mark work as completed?

These are the sorts of questions that distinguish production engineering from simply generating CRUD endpoints.

> **Goal:** Don't build the biggest application. Build a small application whose correctness you can explain and demonstrate.

---

# 3. Your 35-day preparation plan

The 35 days are divided into five phases.

---

## Week 1: 11–17 October

### Master the specification-first workflow

**Primary objective:** Learn to control the development process before asking AI to write code.

Don't start by generating the entire application.

Start with a business requirement and force yourself to clarify the problem.

### Day 1 — 11 October: Establish your environment

Create the repository:

```text
strata-operations/
├── README.md
├── CLAUDE.md
├── .cursor/
│   └── rules/
├── specs/
│   ├── requirements.md
│   ├── architecture.md
│   ├── data-model.md
│   └── decisions/
├── src/
├── tests/
└── docs/
```

You can adapt the structure to the conventions of each tool. Cursor and Claude Code have different configuration mechanisms, so don't assume every file is automatically recognised by both.

Set up:

- Git repository
- Java and Maven
- PostgreSQL
- Preferred Java IDE or editor
- Claude Code
- Cursor
- A working test command

Your first milestone is simple:

> Build and run an empty Spring Boot application.

**Deliverable:** A working repository with a repeatable build.

---

### Day 2 — 12 October: Write the business requirements

Write:

```text
specs/requirements.md
```

Start with this requirement:

> As a strata manager, I want to create and track maintenance requests for a building so that maintenance activities can be prioritised, assigned and completed with an auditable history.

Now expand it.

Document:

- Actors and permissions
- Business entities
- Business rules
- Functional requirements
- Non-functional requirements
- Out-of-scope features
- Unanswered business questions

Don't let AI silently invent business decisions.

Ask it to identify ambiguities and propose alternatives, but retain ownership of the final decisions.

**Deliverable:** A requirements document that another engineer could understand without asking you to explain the entire problem verbally.

---

### Day 3 — 13 October: Write acceptance criteria

Learn to turn requirements into testable conditions.

For example:

```gherkin
Feature: Create maintenance request

Scenario: Create a request for a valid lot
  Given a building and lot exist
  When a manager submits a maintenance request
  Then the request is persisted
  And it receives a unique identifier
  And its initial status is OPEN

Scenario: Reject a request for an unknown lot
  Given the specified lot does not exist
  When a manager submits a maintenance request
  Then the request is rejected
  And no maintenance request is persisted
```

Expand this with:

- Validation failures
- Authorisation
- Concurrency
- Duplicate submissions

where relevant.

You should understand why every acceptance criterion exists.

---

### Day 4 — 14 October: Create the technical specification

Create:

```text
specs/architecture.md
specs/data-model.md
```

Define:

- Package structure
- REST endpoints
- Request and response DTOs
- Database relationships
- Transaction boundaries
- Validation rules
- Exception-handling conventions
- Logging and observability requirements

Make explicit decisions where needed.

For example:

```text
Decision: Use a modular monolith.

Reason:
The initial domain is small, and there is no demonstrated need
for independent deployment or scaling of separate services.

Consequence:
Modules have clear responsibilities, but share one deployment
and initially share a database.
```

This is architecture work, not merely documentation.

---

### Day 5 — 15 October: Break the specification into tasks

Ask Claude Code to produce an implementation plan.

A good plan might be:

1. Add database migrations.
2. Implement the domain model.
3. Implement the repository layer.
4. Implement the service layer.
5. Add request validation.
6. Implement the REST controller.
7. Add unit tests.
8. Add integration tests.
9. Add API documentation.
10. Review security and error handling.

Each task should have:

- A defined scope
- Dependencies
- Files likely to change
- Acceptance criteria
- Tests required
- Explicit exclusions

**Important:** Review the plan before authorising implementation.

---

### Day 6 — 16 October: Implement your first vertical slice

Build only the create-maintenance-request workflow.

Use Claude Code to implement the approved tasks.

Ask it to:

- Follow the specifications
- Avoid unrelated refactoring
- Explain important design decisions
- Run the tests
- Report any specification gaps instead of guessing

Review the changes yourself.

---

### Day 7 — 17 October: Reproduce the workflow in Cursor

Take the same feature or a second small feature and implement it using Cursor.

Use Cursor for:

- Codebase exploration
- Planning changes
- Generating targeted modifications
- Debugging failing tests
- Reviewing diffs

Compare the tools based on:

- Quality of the result
- Amount of correction needed
- Your ability to understand the implementation

**Week 1 exit criteria**

By the end of this week, you should have:

- A working Spring Boot application
- A written requirements specification
- An approved technical design
- A task breakdown
- One complete API workflow
- Automated tests
- A documented record of what AI got wrong and how you corrected it

---

# Week 2: 18–24 October

## Learn to solve real Java problems through specifications

**Primary objective:** Move beyond CRUD and build features that require engineering judgement.

This is where the project becomes valuable.

### Day 8 — 18 October: Implement a state machine

Define the lifecycle of a maintenance request.

For example:

```text
OPEN
  |
  v
ASSIGNED
  |
  v
IN_PROGRESS
  |
  v
COMPLETED
```

You might also allow `CANCELLED`, subject to explicit business rules.

Your job is to decide:

- Which transitions are valid
- Which transitions require permissions
- What happens when a transition is invalid
- Whether completed work can be reopened
- How state changes are audited

Have Claude implement the rules, then review the design.

Don't let status changes become arbitrary assignments scattered across controllers and services.

---

### Day 9 — 19 October: Implement business invariants

Solve these problems:

- Reject invalid contractor assignments
- Prevent work orders from being created for nonexistent requests
- Validate required fields
- Enforce unique identifiers
- Keep related database changes consistent

Ask yourself whether each rule belongs in:

- Domain model
- Service layer
- Database
- Or a combination

The point is to understand the trade-offs rather than mechanically follow an architectural pattern.

---

### Day 10 — 20 October: Solve a duplicate-submission problem

Imagine the client submits the same request twice because of a network timeout.

Design an idempotent API.

Your specification should define:

- Idempotency key
- How duplicate requests are recognised
- What happens if the same key is reused with a different payload
- How concurrent submissions are handled
- What response the client receives on a retry

Implement the solution with appropriate database constraints and tests.

This is much closer to a production engineering problem than a typical coding exercise.

---

### Day 11 — 21 October: Solve a concurrency problem

Two managers attempt to assign different contractors to the same work order simultaneously.

Determine the desired business behaviour.

Then choose an approach, such as optimistic locking with JPA's `@Version`.

Write tests for the expected behaviour.

Be prepared to explain:

- Why the race condition occurs
- How your chosen mechanism works
- What happens when a concurrent update loses
- Whether the client should retry

---

### Day 12 — 22 October: Debug a deliberately broken feature

Ask Claude to introduce a controlled defect on a separate Git branch.

Examples:

- A transaction boundary is missing
- A validation check occurs too late
- A query loads unnecessary related entities
- An exception is incorrectly translated into HTTP 500
- A status transition bypasses a business rule

Your task is to diagnose the issue.

Use Cursor or Claude Code to investigate, but don't immediately ask for a complete rewrite.

Follow this sequence:

1. Reproduce the failure.
2. Identify the observed behaviour.
3. Form competing hypotheses.
4. Collect evidence.
5. Determine the root cause.
6. Implement the smallest correct fix.
7. Add a regression test.

This is a particularly valuable skill for a senior engineer.

---

### Day 13 — 23 October: Improve test quality

Use three levels of testing.

| Test type | What you should verify |
|---|---|
| Unit tests | Business rules and edge cases |
| Integration tests | Database persistence, transactions and repository behaviour |
| API tests | HTTP status codes, validation, serialisation and error responses |

Use Testcontainers where appropriate to run integration tests against a real PostgreSQL instance.

Don't confuse high line coverage with correctness.

Ask your independent reviewer to identify missing behavioural cases, not just missing test methods.

---

### Day 14 — 24 October: Perform your first engineering review

Have Claude review the implementation against the requirements.

Then review it yourself.

Look for:

- Business-rule violations
- Unnecessary abstractions
- Transactional problems
- Race conditions
- Inadequate error handling
- Security weaknesses
- Tests that don't actually prove the expected behaviour

Record the findings in:

```text
docs/engineering-review.md
```

**Week 2 exit criteria**

You should now have a small but credible Java service with:

- State transitions
- Business invariants
- Idempotency
- Concurrency handling
- Unit and integration tests
- A documented engineering review

Most importantly, you should be able to explain every significant implementation decision without relying on the AI to explain it for you.

---

# Week 3: 25–31 October

## Build a repeatable spec-driven AI workflow

**Primary objective:** Turn your individual coding process into a repeatable engineering system.

You have already used Claude Code on the Silent KYC platform. Now formalise the workflow so it can handle increasingly complex features.

Your existing preference for:

> Requirements → Technical specification → Task specification → Implementation → Independent testing

is the right foundation.

---

### Day 15 — 25 October: Create your specification templates

Establish four reusable documents.

#### 1. `requirements.md`

Contains:

- Business problem
- Users and actors
- Functional requirements
- Acceptance criteria
- Non-functional requirements
- Constraints
- Open questions

#### 2. `technical-spec.md`

Contains:

- Proposed design
- API contracts
- Data model
- Business invariants
- Transaction boundaries
- Failure scenarios
- Security requirements
- Alternatives considered

#### 3. `task-spec.md`

Contains:

- Small implementation tasks
- Dependencies
- Files and modules affected
- Acceptance criteria
- Required tests
- Completion conditions

#### 4. `verification.md`

Contains:

- Requirements-to-test mapping
- Commands to run
- Expected outcomes
- Security checks
- Integration-test results
- Known limitations

The goal is to make implementation verifiable against the specifications.

---

### Day 16 — 26 October: Establish repository-level AI instructions

Create a useful `CLAUDE.md` for Claude Code.

Include:

- Java and Spring conventions
- Module boundaries
- Build and test commands
- Package naming
- Error-handling conventions
- Database migration rules
- Security requirements
- Testing expectations
- Instructions for resolving ambiguity

Keep instructions concise and actionable.

For example:

```text
Before implementation:
1. Read the relevant specifications.
2. Inspect the existing code and conventions.
3. Identify ambiguities and conflicting requirements.
4. Produce a plan for approval.

During implementation:
1. Follow existing architectural boundaries.
2. Do not change unrelated files.
3. Do not silently weaken validation or tests.
4. Do not invent business rules.

Before completion:
1. Run relevant tests.
2. Report the exact commands and results.
3. Map implemented behaviour to acceptance criteria.
4. Identify unresolved risks and limitations.
```

Create corresponding Cursor project rules using its supported configuration mechanism.

The goal is consistent engineering behaviour across both tools, not maintaining two different sets of architectural rules.

---

### Day 17 — 27 October: Build a planning workflow

Give Claude Code a new feature request and ask it to:

- Generate requirements
- Identify gaps
- Propose a technical design

Do not let it proceed straight to implementation.

Your acceptance gate should require:

- Requirements are understood
- Important business decisions are resolved
- Design is consistent with the existing codebase
- Risks and failure modes are documented
- Tasks are small enough to verify

You remain the technical decision-maker.

---

### Day 18 — 28 October: Introduce specialised agents

Start with three focused responsibilities.

| Agent | Responsibility | Restriction |
|---|---|---|
| Implementer | Implement approved tasks | Must follow the specifications |
| Test engineer | Identify gaps and add tests | Must not change production code unless explicitly authorised |
| Reviewer | Inspect implementation for defects | Read-only; must not silently fix its own findings |

You can use Claude Code's custom subagents to isolate review and testing tasks.

Do not create ten agents just because you can.

Three well-defined roles are enough to learn the coordination problems.

---

### Day 19 — 29 October: Prevent the agents from agreeing on incorrect assumptions

This is a critical exercise.

Suppose the implementation assumes that a completed work order can be reopened.

The testing agent reads the implementation and writes tests that confirm that behaviour.

The reviewer then checks the code against those tests.

All three may agree, even though the business rule was never approved.

Prevent this by making the **approved requirements and acceptance criteria the source of truth**, rather than the implementation.

The testing agent must challenge the implementation against the specifications.

The reviewer must independently examine:

- Requirement compliance
- Missing edge cases
- Business-rule violations
- Security
- Concurrency
- Maintainability

A review that merely confirms the generated code behaves as generated is not independent verification.

---

### Day 20 — 30 October: Measure AI effectiveness

Run a controlled comparison.

Implement two similarly sized features:

- One using a simple, direct AI-assisted workflow
- One using the full specification-driven process

Record:

- Time to working implementation
- Number of correction cycles
- Defects found during review
- Tests required to establish correctness
- Unrequested changes
- Time spent understanding generated code

This gives you evidence about where structured AI workflows help and where they add overhead.

Don't assume that the more elaborate workflow always wins.

---

### Day 21 — 31 October: Run a complete feature delivery

Give yourself a new feature.

For example:

> A strata manager must be able to reassign an existing work order to a different contractor. The system must preserve the audit history, validate the new assignment and prevent concurrent updates from silently overwriting one another.

Starting from the requirement, complete:

- Specification
- Architecture and design
- Task breakdown
- Implementation
- Automated tests
- Independent review
- Final acceptance report

**Week 3 exit criteria**

You should now have a repeatable AI-assisted engineering workflow that you can use on a fresh feature without reinventing your process.

This is also the week when you start producing evidence for your engineering leadership story: measurable quality improvements, controlled agent responsibilities and human ownership of technical decisions.

---

# Week 4: 1–7 November

## Add production engineering and architectural depth

**Primary objective:** Ensure your AI-generated code can survive realistic production conditions.

A senior engineer needs to think beyond the happy path.

---

### Day 22 — 1 November: Database performance

Add realistic sample data.

Investigate:

- Pagination
- Query plans
- Index selection
- N+1 query problems
- Fetch strategies
- Transaction duration

Use PostgreSQL's `EXPLAIN ANALYZE` where appropriate.

Ask AI to propose optimisations, but require it to explain the evidence supporting each change.

Do not accept an index simply because the agent recommends one.

---

### Day 23 — 2 November: API security

Implement appropriate security boundaries.

Consider:

- Authentication and authorisation
- Role-based permissions
- Input validation
- Object-level access checks
- SQL injection prevention
- Sensitive information in logs
- Secrets management
- Dependency vulnerabilities

Pay particular attention to object-level access.

A user should not be able to access another building's information simply by changing an identifier in a request.

---

### Day 24 — 3 November: Resilience and external integrations

Introduce a simulated contractor service.

Make it fail in controlled ways:

- Timeouts
- HTTP 5xx responses
- Slow responses
- Duplicate responses
- Malformed responses

Decide how your application should behave.

Understand when to use:

- Timeouts
- Bounded retries
- Exponential backoff and jitter
- Circuit breakers
- Bulkheads
- Fallbacks

Retries must account for whether an operation is safe to repeat.

For example, retrying a read may be straightforward; retrying a contractor booking without idempotency may create duplicate bookings.

---

### Day 25 — 4 November: Asynchronous processing and Kafka

Introduce an asynchronous event such as:

```text
WorkOrderAssigned
```

Define the event contract and consider how the system publishes it reliably.

Explore the transactional outbox pattern.

Understand:

- At-least-once delivery
- Duplicate event handling
- Consumer idempotency
- Event ordering
- Retry and dead-letter strategies
- Schema evolution
- Eventual consistency

You do not need to build an elaborate Kafka deployment.

A focused proof of concept and a clear design document are sufficient.

---

### Day 26 — 5 November: Observability

Add:

- Structured logging
- Correlation IDs
- Application metrics
- Health checks
- Request latency measurements
- Error-rate measurements

Define what you would monitor in production.

For example:

- Request failure rate
- p95 latency
- Database connection-pool utilisation
- External-service timeout rate
- Kafka consumer lag, if applicable

Ask Claude to introduce a controlled failure, then investigate it using the available evidence.

---

### Day 27 — 6 November: Architecture and trade-offs

Write a short architecture decision record comparing three approaches:

1. Modular monolith
2. Microservices
3. Event-driven services

For each, discuss:

- Deployment complexity
- Team ownership
- Data consistency
- Scaling
- Operational overhead
- Failure isolation
- Long-term maintainability

Choose the approach appropriate to the current requirements.

Your aim is not to prove that one architecture is universally superior.

It is to demonstrate that you can select an architecture based on business and engineering constraints.

---

### Day 28 — 7 November: Production readiness review

Conduct a review of the entire application.

Ask your reviewer agent to inspect:

- Security
- Performance
- Reliability
- Data consistency
- Observability
- Test coverage
- Deployment
- Maintainability

Prioritise findings by severity and likelihood.

Fix the highest-value issues.

**Week 4 exit criteria**

You should be able to explain how your application behaves when:

- Dependencies fail
- Concurrent updates occur
- Duplicate messages arrive
- Database performance deteriorates

That is the difference between generating a working demonstration and engineering a production-capable service.

---

# Week 5: 8–14 November

## Prove that you can lead an AI-assisted feature from discovery to delivery

**Primary objective:** Bring everything together in a realistic end-to-end delivery.

This week is less about learning new tools and more about proving that your workflow works.

---

### Day 29 — 8 November: Add a frontend

Build a simple Angular interface for:

- Listing maintenance requests
- Filtering by status and priority
- Viewing request details
- Assigning a contractor
- Updating the work order status

Keep the frontend scope deliberately small.

Focus on:

- Component boundaries
- API integration
- Loading and error states
- Form validation
- Accessibility
- Avoiding duplicated business logic

You don't need to become an Angular specialist in a week.

The goal is to practise full-stack feature delivery and refresh your Angular skills.

---

### Day 30 — 9 November: Write a feature delivery plan

Pretend that a product manager has asked you to deliver contractor reassignment.

Produce a short delivery plan containing:

- Business objective
- Scope
- Requirements
- Dependencies
- Technical design
- Task breakdown
- Risks
- Test strategy
- Rollout approach
- Acceptance criteria

This is the sort of artefact you may need to produce as a Feature Lead.

---

### Day 31 — 10 November: Deliver the feature using Claude Code

Use your complete specification-driven workflow.

You should not need to invent the process as you go.

The workflow should be:

```text
Requirement
    |
    v
Clarify business rules
    |
    v
Approved technical specification
    |
    v
Task breakdown
    |
    v
Implementation
    |
    v
Automated tests
    |
    v
Independent review
    |
    v
Fix identified defects
    |
    v
Final verification
```

Do not equate an agent reporting "complete" with the feature actually being complete.

The evidence is:

- Implementation
- Test results
- Successful verification against acceptance criteria

---

### Day 32 — 11 November: Repeat the workflow in Cursor

Use Cursor for another feature or a significant modification.

Focus on:

- Navigating an existing codebase
- Understanding the impact of changes
- Planning a multi-file modification
- Implementing the change
- Debugging failures
- Reviewing the resulting diff

Compare the process with Claude Code.

By now, you should understand where each tool fits into your own workflow, rather than simply knowing how to operate both.

---

### Day 33 — 12 November: Run a simulated production incident

Introduce a defect such as:

- A database query becoming unexpectedly slow
- Duplicate work orders appearing after retries
- An external contractor service timing out
- An invalid status transition being accepted

Use your observability tools and logs to investigate.

Write a short incident report:

- Impact
- Symptoms
- Root cause
- Mitigation
- Permanent fix
- Regression tests
- Preventive actions

This also gives you useful practice for discussions with experienced engineers and technical leaders.

---

### Day 34 — 13 November: Conduct a senior-engineer review

Review the project as if you were responsible for approving its production release.

Ask yourself:

- Can I explain the design?
- Can I justify the database model?
- Are the business invariants enforced?
- Are transactions used correctly?
- Are the tests meaningful?
- Can the system handle concurrent updates?
- Are errors observable?
- Is access appropriately restricted?
- Are the AI-generated changes understandable?
- Can I identify the remaining risks?

Fix important issues rather than adding new features.

---

### Day 35 — 14 November: Consolidate your engineering playbook

Prepare a concise document containing your repeatable workflow.

Include:

- Requirements template
- Technical specification template
- Task specification template
- Agent responsibilities
- Verification checklist
- Code-review checklist
- Production-readiness checklist
- Lessons learned from the project

You should also be able to demonstrate one complete feature in approximately 15 minutes, explaining how you used specifications, AI, tests and human review to deliver it.

**14 November is your consolidation day.**

Avoid starting another large project the day before joining.

**Week 5 exit criteria**

You should have:

- A complete feature delivered through specification-driven development
- A working AI-assisted engineering workflow
- A production-readiness review
- A senior-engineer review
- A reusable engineering playbook

---

# 4. The daily routine

You previously indicated that around two hours a day is a realistic study commitment.

Keep that constraint rather than creating a plan that requires five hours every evening.

Aim for approximately **10–14 focused hours a week**, with flexibility for weekends and other commitments.

| Activity | Daily allocation |
|---|---:|
| Understand the problem and review the specification | 15 minutes |
| Work with Claude Code or Cursor | 60 minutes |
| Review the code and run tests | 30 minutes |
| Document decisions and lessons learned | 15 minutes |
| **Total** | **2 hours** |

Some days will require more coding and others more design.

The important point is that you must spend time examining the generated code rather than letting an agent work for two hours and accepting its output.

## Daily engineering journal

Keep one short daily record:

```text
Today's feature:
What I asked the AI to do:
What it implemented correctly:
What it got wrong:
What I changed:
Tests that prove the behaviour:
What I learned:
```

This record will become an invaluable reference when you encounter unfamiliar patterns in your new team.

---

# 5. Reusable spec-driven coding framework

This is the part to prioritise above everything else.

The objective is to learn how to solve proper engineering problems through Cursor and Claude Code.

That means developing a reliable process that works across different features.

---

## Stage 1: Understand the problem

Do not ask:

> Build a contractor management API in Spring Boot.

That leaves too many decisions to the AI.

Instead, provide the business problem and ask the agent to identify:

- Missing requirements
- Ambiguous business rules
- Existing code that may be relevant
- Constraints
- Failure scenarios
- Security concerns

**Output:** A set of requirements and open questions.

Review and approve them before proceeding.

---

## Stage 2: Specify the solution

Ask the AI to propose:

- API contracts
- Domain model
- Database changes
- Transaction boundaries
- Validation
- Error handling
- Test cases
- Alternatives and trade-offs

For significant decisions, require a rationale.

For example, don't simply accept:

> Use optimistic locking.

Ask:

- What race condition does it prevent?
- What response does the API return on a conflict?
- Can the business operation be retried safely?

**Output:** An approved technical specification.

---

## Stage 3: Break the work into small tasks

A task should be independently understandable and verifiable.

### Weak task

> Implement the maintenance workflow.

### Better task

> Implement the service operation for assigning a contractor to an existing work order. Validate the contractor's eligibility, preserve the existing work-order state where appropriate, record the assignment change, and add unit tests for valid and invalid assignments. Follow the approved technical specification.

The second task has a defined scope and testable outcomes.

---

## Stage 4: Implement with constrained AI assistance

The agent should:

1. Inspect the relevant code.
2. Follow existing conventions.
3. Implement the approved task.
4. Add or update tests.
5. Run the relevant checks.
6. Report unresolved issues.

Avoid allowing the agent to make unrelated changes while implementing a feature.

For a mature codebase, uncontrolled refactoring can be more dangerous than an obvious bug.

---

## Stage 5: Verify independently

This is where the workflow should become substantially stronger than ordinary AI-assisted coding.

Use a separate review pass that compares the implementation against the requirements.

The reviewer should check:

| Dimension | Questions |
|---|---|
| Functional correctness | Does every acceptance criterion hold? |
| Business logic | Can an invalid state be created? |
| Data integrity | Are constraints and transaction boundaries appropriate? |
| Concurrency | Can simultaneous requests cause incorrect results? |
| Security | Can users access or modify unauthorised resources? |
| Reliability | Are timeouts, retries and failures handled correctly? |
| Testing | Are important edge cases covered? |
| Maintainability | Is the implementation unnecessarily complicated? |

Then run the actual build and tests yourself.

An AI-generated report is not proof that the code compiles or behaves correctly.

---

## Stage 6: Close the loop

When a defect is found:

1. Record the failing behaviour.
2. Identify the relevant requirement.
3. Add or update a regression test.
4. Correct the implementation.
5. Rerun the affected tests.
6. Review the final diff.

The purpose is to make good engineering repetitive, rather than relying on the AI to get everything right on the first attempt.

---

# 6. Claude Code starting prompt

Use this as the initial prompt with Claude Code once the repository has been created.

Adapt the same engineering constraints when working in Cursor.

```text
# Role

Act as a senior Java engineer working with me on a production-oriented
Strata Operations Platform.

I am the technical lead and own the architecture, business decisions
and final acceptance of the implementation.

Your role is to help me reason about the problem, produce specifications,
implement approved tasks and verify correctness.

# Technology

- Java 21
- Spring Boot 3.x
- Maven
- PostgreSQL
- Spring Data JPA
- Flyway
- JUnit 5
- Mockito
- Testcontainers where appropriate

# Engineering Principles

1. Treat approved requirements and technical specifications as the source of truth.
2. Inspect the existing codebase before proposing changes.
3. Follow existing conventions unless there is a justified reason to change them.
4. Do not invent business rules to resolve ambiguity.
5. Do not implement unapproved architectural changes.
6. Avoid unnecessary abstractions and unrelated refactoring.
7. Consider security, data integrity, concurrency and failure scenarios.
8. Write tests that demonstrate required behaviour, including negative cases.
9. Never claim a test passed unless it was actually executed successfully.
10. Explain significant technical trade-offs.

# Workflow

## Phase 1: Discovery

Read the relevant requirements and inspect the existing code.

Identify:
- Ambiguous requirements.
- Missing acceptance criteria.
- Existing implementation constraints.
- Dependencies.
- Security and data-integrity concerns.
- Potential failure scenarios.

Do not modify production code during discovery.

## Phase 2: Specification

Propose:
- Functional requirements.
- Acceptance criteria.
- API contracts.
- Data model changes.
- Transaction boundaries.
- Error-handling behaviour.
- Testing strategy.
- Implementation tasks.

Identify decisions that require my approval.

Do not begin implementation until I approve the specification.

## Phase 3: Implementation

Implement only the approved tasks.

For each task:
- Explain the intended change.
- Make the smallest appropriate modification.
- Add relevant tests.
- Run the relevant checks.
- Report any deviation from the specification.

Stop and report blockers rather than silently changing requirements.

## Phase 4: Independent Verification

Compare the implementation against the approved requirements.

Look for:
- Missing acceptance criteria.
- Invalid business states.
- Incorrect transaction handling.
- Race conditions.
- Security vulnerabilities.
- Inadequate error handling.
- Missing tests.
- Unnecessary complexity.

Do not assume that passing tests prove complete correctness.

## Phase 5: Final Report

Provide:
- Summary of implemented behaviour.
- Files changed.
- Tests executed and their actual results.
- Acceptance criteria verified.
- Outstanding defects.
- Known limitations.
- Recommended follow-up work.

# First Task

Do not implement the application yet.

Inspect the repository and help me define the requirements for the
maintenance-request and work-order workflow.

Ask clarifying questions where business rules are ambiguous.

Produce a requirements specification and identify the decisions that
I must make before we proceed to technical design.
```

---

# 7. Dividing work between Cursor and Claude Code

You don't need to master two completely separate development methodologies.

Learn one engineering methodology and become proficient at applying it in both tools.

| Activity | Claude Code | Cursor |
|---|---|---|
| Explore an existing repository | Excellent use case | Excellent use case |
| Generate a specification | Primary workflow | Can perform the same task |
| Implement a defined feature | Task-oriented implementation | Interactive implementation |
| Coordinate specialised agents | Custom subagents | Explore agent capabilities |
| Debug failing tests | Investigation and targeted fixes | Investigation and targeted fixes |
| Inspect changes | Review diffs and run checks | Review diffs and run checks |

### Recommended approach

Use **Claude Code for the main specification-driven workflow** and **Cursor for IDE-centric implementation and debugging**.

However, test both tools on comparable tasks.

The goal is to develop a portable workflow that isn't dependent on a particular vendor.

---

# 8. What to deliberately avoid

There are several tempting distractions during a month of preparation.

## Don't spend a week learning prompt engineering

You already have practical experience with Claude Code.

You will learn more by delivering several real features with explicit specifications than by collecting hundreds of prompts.

## Don't grind LeetCode

For this preparation period, prioritise:

- Realistic coding problems
- Debugging
- Database behaviour
- Concurrency
- API design
- Testing

Maintain basic coding fluency, but don't allow algorithm exercises to displace the main project.

## Don't build a complicated multi-agent system immediately

Start with:

- One implementation agent
- One independent reviewer

Add a separate testing agent once you have a working baseline.

Introduce orchestration only when you can demonstrate that it improves the workflow.

## Don't let AI make every architectural decision

For a senior engineer, the value is not simply generating more code.

You need to understand why you chose:

- A transaction boundary
- Database constraint
- API contract
- Concurrency mechanism
- Architectural pattern

If the AI makes a decision you cannot explain, investigate it before accepting the change.

## Don't spend too much time on Angular

The role is full-stack, so you should be able to:

- Navigate an Angular codebase
- Understand component boundaries
- Integrate APIs
- Debug common issues

But the immediate priority should be:

1. Java/Spring
2. System design
3. Technical leadership
4. AI-assisted engineering

---

# 9. Final readiness assessment

On **14 November**, assess yourself against these five exercises.

| Exercise | What success looks like |
|---|---|
| Build a feature from a written requirement | You can produce a specification, implementation and tests without losing control of the design |
| Debug a production-style defect | You can reproduce the issue, investigate evidence, identify the root cause and add a regression test |
| Review AI-generated code | You can identify meaningful correctness, security and maintainability problems |
| Explain an architectural decision | You can compare alternatives and justify the chosen approach |
| Demonstrate your AI workflow | You can explain how requirements, task boundaries, agent responsibilities and independent verification improve delivery |

Aim to complete all five without relying on the AI to make your engineering decisions for you.

### Additional challenge

Deliberately ask the AI to implement a feature from an incomplete specification.

Then:

1. Observe what it assumes.
2. Identify the resulting risks.
3. Improve the specification.
4. Repeat the implementation.

This will teach you something important about leading AI-assisted engineering in a real team.

---

# 10. Starting point

The first three tasks are:

1. Create the Strata Operations repository.
2. Write the initial maintenance-request requirements.
3. Use Claude Code to identify ambiguities and produce a proposed specification—without implementing anything yet.

Then:

1. Review the specification.
2. Start the first vertical slice.
3. Add tests.
4. Review the implementation.
5. Document what AI got wrong.
6. Repeat with increasingly complex problems.

By the end of October, you should have a credible Java service and a repeatable AI-assisted development workflow.

By **14 November**, you should have taken a complete feature from business requirement to verified implementation, including tests, a technical review and a production-readiness assessment.

The ultimate goal is to arrive at the new role able to demonstrate a disciplined way of working:

> **You define the problem → AI accelerates implementation → automated tests establish evidence → independent review challenges the result → you retain ownership of engineering quality.**
