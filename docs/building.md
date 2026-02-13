# Building, Signing & GitHub Release Publishing Guide

This guide covers building ReelLock release APKs from source, signing binaries, publishing releases on GitHub, and deploying MkDocs documentation to GitHub Pages.

---

## Prerequisites

- **JDK**: Java Development Kit 17 (OpenJDK 17 recommended).
- **Android SDK**: API Level 34 (Android 14) installed.
- **Gradle**: Gradle 8.2+ (managed automatically by Gradle Wrapper).
- **Android Studio**: Android Studio Hedgehog (2023.1.1) or newer.

---

## 1. Compiling the Release APK

Execute `./gradlew assembleRelease` from the terminal:

```bash
# Clone repository
git clone https://github.com/ReelLock/ReelLock.git
cd ReelLock

# Build Production Release APK
./gradlew assembleRelease
```

The output APK binary will be placed at:
`app/build/outputs/apk/release/app-release-unsigned.apk` (or `app-release.apk` if signing is configured in `build.gradle.kts`).

---

## 2. Signing Release Binaries

To produce a signed release build for publication:

1. **Generate a keystore** (if you don't have one):
   ```bash
   keytool -genkey -v -keystore reellock-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias reellock
   ```
2. **Align and sign the APK**:
   ```bash
   # Zipalign the APK
   zipalign -v -p 4 app/build/outputs/apk/release/app-release-unsigned.apk ReelLock-v1.0.0.apk

   # Sign using apksigner
   apksigner sign --ks reellock-release.jks --ks-key-alias reellock --out ReelLock-v1.0.0.apk ReelLock-v1.0.0.apk
   ```

3. **Verify the signature**:
   ```bash
   apksigner verify ReelLock-v1.0.0.apk
   ```

---

## 3. Creating SHA-256 Checksums

Generate SHA-256 checksum for security verification:

```bash
sha256sum ReelLock-v1.0.0.apk > ReelLock-v1.0.0.apk.sha256
cat ReelLock-v1.0.0.apk.sha256
```

---

## 4. Creating a GitHub Release

1. Navigate to your repository on GitHub: `https://github.com/ReelLock/ReelLock/releases`.
2. Click **Draft a new release**.
3. Set **Tag version** to `v1.0.0`.
4. Set **Release title** to `ReelLock v1.0.0 — Official Release`.
5. Paste release notes from [CHANGELOG.md](https://github.com/ReelLock/ReelLock/blob/main/CHANGELOG.md).
6. Drag and drop the signed binary `ReelLock-v1.0.0.apk` and checksum file `ReelLock-v1.0.0.apk.sha256` into the release binary attachment box.
7. Click **Publish release**.

---

## 5. Hosting MkDocs Documentation on GitHub Pages

The ReelLock technical documentation site is powered by MkDocs and hosted directly on **GitHub Pages**.

### Deployment via Command Line (`gh-deploy`)
```bash
# Install MkDocs and Material theme
pip install mkdocs mkdocs-material

# Deploy directly to the gh-pages branch
mkdocs gh-deploy --force
```

### GitHub Actions Automated Deployment Workflow
Create `.github/workflows/docs.yml`:
```yaml
name: Deploy Documentation
on:
  push:
    branches:
      - main

permissions:
  contents: write

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: 3.x
      - run: pip install mkdocs-material
      - run: mkdocs gh-deploy --force
```

Once pushed, GitHub Pages will automatically serve the documentation site at:
👉 **[https://sohamtilekar.github.io/ReelLock/](https://sohamtilekar.github.io/ReelLock/)**
