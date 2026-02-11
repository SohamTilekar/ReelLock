# Changelog

All notable changes to the **ReelLock** project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-02-13

### Added
- **Real-Time Instagram Reels Detection**:
  - Implemented high-performance Accessibility Service inspecting window state changes (`TYPE_WINDOW_STATE_CHANGED`, `TYPE_WINDOW_CONTENT_CHANGED`).
  - Target heuristic engine identifying Instagram Reels container nodes, resource IDs, and view class hierarchies.
- **Three Versatile Blocking Action Modes**:
  - `Auto Back`: Programmatically executes system back navigation upon Reel detection.
  - `Go Home`: Instantly navigates to the launcher home screen.
  - `Custom Full-Screen Overlay`: Displays an unbypassable blocking layout over the Reels player (`overlay_reels_blocked.xml`).
- **Material 3 Control Dashboard**:
  - Permission status indicators for Accessibility Service and System Alert Window.
  - Interactive Mode Selector with real-time preference persistence via `SharedPreferences`.
  - Blocked counter metric tracking real-time Reel intercepts.
- **System Integration & Stability**:
  - Battery optimization notice for custom OEM Android flavors (Xiaomi HyperOS/MIUI, Samsung One UI, ColorOS/OxygenOS).
  - Clean service lifecycle hooks preventing resource leaks or performance degradation.
- **Comprehensive Documentation**:
  - MkDocs Material technical documentation site (`mkdocs.yml`).
  - Deep-dive guides for architecture, user setup, compilation, and troubleshooting.

### Security & Privacy
- **Zero Network Permissions**: ReelLock requires no Internet access permission (`android.permission.INTERNET`). All node tree analysis happens completely on-device.
