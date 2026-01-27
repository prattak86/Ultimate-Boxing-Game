# GitHub Actions Publish Strategy

## Overview
This document explains the publishing strategy for Maven packages and Docker images in the Ultimate Boxing Game project.

## Problem Statement
Previously, the workflow would attempt to publish Maven packages from both `main` branch and feature branches (like `copilot/**`), causing version conflicts. When a Pull Request branch published version `2.0.0`, the `main` branch would fail to publish the same version after merging.

## Solution

### Main Branch Publishing (Production)
- **Maven Packages**: Only published from `main` branch with clean semantic versions (e.g., `2.0.0`)
- **Docker Images**: Only published from `main` branch with tags like `latest`, `main`, etc.
- **Trigger**: Automatic on push to `main` branch

### Pull Request & Feature Branch Builds
- **Maven Packages**: NOT published by default to prevent version conflicts
- **Docker Images**: NOT published by default
- **JAR Artifacts**: Still uploaded to GitHub Actions artifacts for testing

## Jobs

### 1. `build`
- Runs on: All branches and Pull Requests
- Actions:
  - Builds the project with Maven
  - Runs tests
  - Uploads JAR as GitHub Actions artifact
  - Builds Docker image (but doesn't push from non-main branches)

### 2. `publish-to-github-packages`
- Runs on: Only `main` branch
- Condition: `github.ref == 'refs/heads/main' && github.event_name == 'push'`
- Actions:
  - Publishes Maven package to GitHub Packages with clean version (e.g., `2.0.0`)

### 3. `publish-snapshot-from-branch` (Optional, Disabled by Default)
- Runs on: Feature branches (disabled by default)
- Condition: `if: false && ...` (change `false` to enable)
- Actions:
  - Creates version with branch suffix (e.g., `2.0.0-copilot-feature-xyz`)
  - Publishes to GitHub Packages with unique version
  - Prevents conflicts with main branch versions

## Enabling Branch Publishing (Optional)

If you need to test publishing from feature branches, you can enable the `publish-snapshot-from-branch` job:

1. Edit `.github/workflows/build.yml`
2. Change line 102 from:
   ```yaml
   if: false && github.ref != 'refs/heads/main' && github.event_name == 'push'
   ```
   to:
   ```yaml
   if: github.ref != 'refs/heads/main' && github.event_name == 'push'
   ```

This will publish versions like:
- Branch `copilot/feature-x` → `2.0.0-copilot-feature-x`
- Branch `develop` → `2.0.0-develop`
- Pull Request #123 → `2.0.0-PR123`

## Version Suffix Examples

| Branch/Event | Version Suffix | Full Version |
|--------------|---------------|--------------|
| main | (none) | 2.0.0 |
| copilot/fix-bug | copilot-fix-bug | 2.0.0-copilot-fix-bug |
| feature/new-api | feature-new-api | 2.0.0-feature-new-api |
| PR #42 | PR42 | 2.0.0-PR42 |

## Maven Configuration

The `versions-maven-plugin` has been added to `pom.xml` to support dynamic version changes during the workflow. This plugin allows the workflow to:
1. Read the base version from `pom.xml`
2. Append the branch/PR suffix
3. Publish with the modified version

## Benefits

1. **No Version Conflicts**: Main branch always publishes clean versions
2. **Safe Testing**: Feature branches can optionally publish with unique versions
3. **Clear Version Identification**: Branch suffixes make it easy to identify source
4. **Artifact Availability**: All builds still produce JAR artifacts for testing
5. **Backward Compatible**: Existing main branch workflow unchanged

## Testing the Changes

To test that the workflow works correctly:

1. **Pull Request**: Create a PR and verify:
   - Build job runs ✓
   - Publish job does NOT run ✓
   - JAR artifact is uploaded ✓

2. **Main Branch**: Push to main and verify:
   - Build job runs ✓
   - Publish job runs ✓
   - Maven package published with version `2.0.0` ✓
   - Docker image pushed with `latest` tag ✓

3. **Feature Branch** (if enabled): Push to `copilot/**` and verify:
   - Build job runs ✓
   - Snapshot publish job runs ✓
   - Maven package published with version like `2.0.0-copilot-branch-name` ✓
