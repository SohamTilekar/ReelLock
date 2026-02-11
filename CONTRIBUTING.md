# Contributing to ReelLock

First off, thank you for considering contributing to ReelLock! It's people like you who make ReelLock a great tool for digital wellbeing and focus.

## Table of Contents
1. [Code of Conduct](#code-of-conduct)
2. [How Can I Contribute?](#how-can-i-contribute)
   - [Reporting Bugs](#reporting-bugs)
   - [Suggesting Enhancements](#suggesting-enhancements)
   - [Pull Requests](#pull-requests)
3. [Development Environment Setup](#development-environment-setup)
4. [Coding Standards & Conventions](#coding-standards--conventions)
5. [Git Commit Guidelines](#git-commit-guidelines)

---

## Code of Conduct

This project and everyone participating in it is governed by the [ReelLock Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

---

## How Can I Contribute?

### Reporting Bugs

Bugs are tracked as GitHub Issues. Before creating a bug report, please check existing issues to ensure it hasn't been reported yet.

When creating a bug report, please include:
- **Device Information**: Android OS version, Device Model (e.g. Samsung Galaxy S23, Xiaomi HyperOS, Pixel 7).
- **Instagram Version**: Version of the Instagram app installed.
- **Action Mode Selected**: Auto-Back, Go-Home, or Overlay Mode.
- **Steps to Reproduce**: Detailed steps to reproduce the issue.
- **Expected vs Actual Behavior**: Clear description of what you expected to happen vs what occurred.

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub Issues. Please provide:
- A clear and descriptive title.
- A step-by-step explanation of the suggested feature or improvement.
- Practical use cases demonstrating why this feature would benefit ReelLock users.

### Pull Requests

1. **Fork the repo** and create your branch from `main`:
   ```bash
   git checkout -b feat/my-amazing-feature
   ```
2. **Ensure code compiles cleanly**:
   ```bash
   ./gradlew assembleDebug
   ```
3. **Write clear commit messages** following the Conventional Commits format.
4. **Submit a Pull Request** describing your changes in detail.

---

## Development Environment Setup

- **IDE**: Android Studio Hedgehog (2023.1.1) or newer recommended.
- **JDK Version**: Java 17 / OpenJDK 17.
- **Android SDK**: API Level 34 (Android 14) with minimum SDK API Level 24 (Android 7.0).
- **Gradle**: Gradle 8.2+ with Kotlin DSL (`.gradle.kts`).

---

## Coding Standards & Conventions

- Follow standard [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- Keep `ReelBlockerService.kt` lightweight. Avoid heavy allocations inside `onAccessibilityEvent()` to preserve system UI responsiveness.
- Use Android Material 3 design components for dashboard UI.
- All user-facing strings must be localized in `res/values/strings.xml`.

---

## Git Commit Guidelines

We enforce the **Conventional Commits** specification for all commit messages:

- `feat(scope)`: A new feature for the user or system
- `fix(scope)`: A bug fix
- `docs(scope)`: Documentation only changes
- `style(scope)`: Changes that do not affect the meaning of the code (white-space, formatting, etc.)
- `refactor(scope)`: A code change that neither fixes a bug nor adds a feature
- `perf(scope)`: A code change that improves performance
- `chore(scope)`: Changes to the build process or auxiliary tools/libraries

Example:
```
feat(core): implement heuristic detection for Instagram reels player node
```

Thank you for building a focus-driven community! 🚀
