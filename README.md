# Movie Recommendation System

A Java 21 + Maven project that loads movies and users from text files, validates the input, generates genre-based recommendations, and writes results to `recommendations.txt`.

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [Architecture](#architecture)
- [SOLID Principles in This Project](#solid-principles-in-this-project)
- [Testing Strategy](#testing-strategy)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Input and Output Format](#input-and-output-format)
- [How to Run Tests](#how-to-run-tests)
- [Roadmap](#roadmap)

## Overview

The system follows a clear pipeline:

1. Read movie and user files.
2. Parse and validate data.
3. Build recommendations from shared genres.
4. Write recommendations to `recommendations.txt`.

Main entry point:
- `src/main/java/org/example/app/MovieRecSystem.java`

## Core Features

- Interactive file loading for movies and users.
- Strong validation for:
  - movie title format
  - movie ID format and title-ID consistency
  - user name and user ID format
  - uniqueness of entity IDs
- Recommendation engine (`Recommender`) that:
  - extracts genres from each user's liked movies
  - excludes already liked movies
  - recommends movies with overlapping genres
- File-based output writer with user-wise recommendation blocks.
- Centralized error handling through `ValidationException` and clear error messages.

## Architecture

The codebase is organized by responsibility:

- `app`: application orchestration (`MovieRecSystem`)
- `model`: domain objects (`Movie`, `User`)
- `validation`: data validation rules (`FormatValidator`)
- `io.loader`: entity loading workflow (`EntityLoader`, `MovieLoader`, `UserLoader`)
- `io.loader.component`: reusable parsing and loading components (`FileReader`, `TwoLineParser`, `IdValidator`)
- `recommendation`: recommendation logic (`Recommender`, `UserRecommendations`)
- `io.output`: output persistence (`OutputWriter`)
- `exception`: domain-specific validation exception model

### Design Pattern Highlight

- **Template Method** in `EntityLoader<T>`:
  - `load()` defines the common loading algorithm.
  - subclasses (`MovieLoader`, `UserLoader`) provide type-specific behavior.

## SOLID Principles in This Project

The project demonstrates SOLID-oriented design through class boundaries and dependency handling.

### S - Single Responsibility Principle

- `Recommender` focuses on recommendation rules only.
- `FormatValidator` focuses on input format validation only.
- `OutputWriter` focuses on writing output only.
- `MovieLoader` and `UserLoader` focus on mapping parsed data into domain objects.

### O - Open/Closed Principle

- `EntityLoader<T>` is open for extension via new loader types and closed for modification of the core load algorithm.
- New entity loaders can reuse parsing and ID validation without rewriting the pipeline.

### L - Liskov Substitution Principle

- `MovieLoader` and `UserLoader` substitute `EntityLoader<T>` contracts without breaking `load()` expectations.

### I - Interface Segregation Principle

- Responsibilities are separated into small components (`FileReader`, `TwoLineParser`, `IdValidator`) so consumers depend on focused APIs.

### D - Dependency Inversion Principle

- Constructors accept dependencies for testability (for example, loaders in `MovieRecSystem`, components in `EntityLoader` subclasses).
- Tests inject mocks/stubs (Mockito and test doubles) instead of relying on concrete runtime-only behavior.

## Testing Strategy

The test suite is layered to validate both correctness and behavior across boundaries.

### 1) Unit Testing (JUnit 5 + Mockito)

Location:
- `src/test/java/org/example/unit/**`

Coverage includes:
- domain model validation
- loader logic with mocked collaborators
- recommender behavior
- app orchestration behavior

Examples:
- `MovieLoaderTest` uses Mockito to isolate parser, file reader, and ID validator.
- `MovieRecSystemTest` verifies orchestration with mocked loaders and writer.

### 2) Integration Testing

Locations:
- Top-down: `src/test/java/org/example/integration/topdown/**`
- Bottom-up: `src/test/java/org/example/integration/bottomup/**`

#### Top-down integration
- Starts from higher-level flow and controls lower-level dependencies where needed.
- Example: `MovieRecSystem_TopChainIT` verifies that `MovieRecSystem` passes correct recommendations to `OutputWriter`.

#### Bottom-up integration
- Validates lower-level components first, then builds toward full flow.
- Examples:
  - `EntityLoaderIntegrationTesting`
  - `RecommenderIntegrationTesting`
  - `MovieRecSystemIntegrationTesting`

### 3) System Testing

Location:
- `src/test/java/org/example/system/MovieRecSystemSystemTest.java`

Focus:
- end-to-end behavior with real files
- expected output comparisons
- error scenarios (duplicate IDs, malformed files)

### 4) Black-Box and White-Box Testing

Locations:
- Black-box: `src/test/java/org/example/Blackbox/**`
- White-box: `src/test/java/org/example/WhiteBox/**`

Focus:
- black-box suites emphasize input partitions, boundaries, and output behavior.
- white-box suites target coverage paths and internal recommendation-flow behavior.

## Tech Stack

- Java 21
- Maven
- JUnit Jupiter `5.10.2`
- Mockito Core `5.10.0`

Dependency source:
- `pom.xml`

## Project Structure

```text
src/
  main/
    java/org/example/
      app/
      exception/
      io/
        loader/
        output/
      model/
      recommendation/
      validation/
    resources/
      movies.txt
      users.txt
  test/
    java/org/example/
      unit/
      integration/
        topdown/
        bottomup/
      system/
      Blackbox/
      WhiteBox/
    resources/
      integration/
      system/
      Blackbox/
```

## Getting Started

### Prerequisites

- JDK 21 installed
- Maven installed and available in `PATH`

### Build the project

```powershell
mvn clean compile
```

### Run the application

```powershell
mvn clean compile
java -cp target/classes org.example.app.MovieRecSystem
```

When prompted, provide paths for movie and user files.

Example inputs in repository:
- `src/main/resources/movies.txt`
- `src/main/resources/users.txt`

## Input and Output Format

### Movies file (2-line blocks)

```text
Movie Title, MOV123
Genre1, Genre2
```

### Users file (2-line blocks)

```text
User Name, 12345678A
MOV123, MOV456
```

### Output file

Generated file:
- `recommendations.txt`

Output pattern:

```text
User Name, UserId
Recommended Movie 1, Recommended Movie 2
```

## How to Run Tests

Run all tests:

```powershell
mvn test
```

Run specific testing layers:

```powershell
mvn -Dtest="org.example.unit.**.*Test" test
mvn -Dtest="org.example.integration.topdown.**.*IT,org.example.integration.topdown.**.*Test" test
mvn -Dtest="org.example.integration.bottomup.**.*IT,org.example.integration.bottomup.**.*Test" test
mvn -Dtest="org.example.system.*Test" test
```

## Roadmap

- Add interfaces for key services (`Recommender`, output abstraction) to make DIP even stronger.
- Add CI pipeline for automated build and test reporting.
- Add coverage thresholds and mutation testing.
- Add configuration-driven input/output paths to reduce console interaction in production runs.
