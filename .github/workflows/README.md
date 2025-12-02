# CI/CD Workflows

This directory contains GitHub Actions workflows for automated building, testing, and deployment of the Oshu Store Android application.

## Workflows

### 1. Android CI/CD (`android-ci.yml`)

Main workflow that runs on every push and pull request:

**Jobs:**
- **build-and-test**: Builds debug and release APKs, runs unit tests
- **lint**: Runs Android Lint checks for code quality

**Triggers:**
- Push to `main`, `master`, or `develop` branches
- Pull requests to `main`, `master`, or `develop`
- Manual workflow dispatch

**Artifacts:**
- Debug APK (`app-debug.apk`)
- Release APK (`app-release.apk`)
- Test results
- Lint results

### 2. Android Instrumented Tests (`android-tests.yml`)

Runs instrumented (UI) tests on an Android emulator:

**Jobs:**
- **instrumented-tests**: Runs Espresso UI tests on emulator

**Triggers:**
- Push to `main` or `master` branches
- Pull requests to `main` or `master`
- Manual workflow dispatch

**Note:** Instrumented tests require an emulator and run on macOS runners, which may consume more GitHub Actions minutes.

## Firebase Configuration

The workflows automatically create a placeholder `google-services.json` file for CI builds since the actual file is gitignored. This allows the project to build successfully in CI without exposing sensitive Firebase credentials.

For local development, use your actual `google-services.json` file.

## Viewing Results

1. Go to the **Actions** tab in your GitHub repository
2. Click on a workflow run to see detailed logs
3. Download artifacts (APKs, test results) from the workflow run page

## Customization

### Changing Test Commands

Edit the workflow files to customize test commands:
```yaml
- name: Run Custom Tests
  run: ./gradlew testDebugUnitTest
```

### Adding More Jobs

You can add additional jobs for:
- Code coverage reports
- Static analysis (SonarQube, CodeQL)
- APK signing and publishing
- Deployment to Firebase App Distribution

### Branch Protection

Consider setting up branch protection rules that require:
- CI checks to pass before merging
- Code review approval
- Up-to-date branches



