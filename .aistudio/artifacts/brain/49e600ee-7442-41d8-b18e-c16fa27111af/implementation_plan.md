# Aether Cloud Ecosystem

Aether Cloud is a cohesive, modern suite of productivity, exploration, and media applications inspired by Google's connected services—delivering a streamlined, usable multi-app experience rather than a heavyweight simulated OS.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following directions have been confirmed and will guide the entire architecture and visual language:

- **Confirmed Brand & Identity**: **Aether Cloud** — A minimalist, clean utility and media ecosystem with sleek typography, refined pastel accents, and distinctive geometric iconography.
- **Confirmed Core App Suite**: Full Suite with high real-world usability:
  1. **Aether Search & Pulse**: Clean search engine with instant AI Synthesis, voice query, trending topics, and live curated discovery feed.
  2. **Aether Mail**: Fast inbox with categorized streams (Primary, Updates, Social), message reader, star/archive workflows, and rich compose sheet.
  3. **Aether Drive & Docs**: Cloud storage repository and real-time interactive rich document creator/editor with formatting controls, auto-save, and word count.
  4. **Aether Maps & Guide**: Interactive vector city canvas, spatial POIs (Cafes, Tech Hubs, Nature, Dining), place details, and simulated turn-by-turn route navigation.
  5. **Aether Photos & Canvas**: Date-organized media gallery, Memories carousel, and AI enhancement tools (Magic Object Eraser, Dynamic HDR, Warm/Cool filters).
  6. **Aether Calendar & Tasks**: Agenda schedule timeline, month picker, event creator with color badges, and integrated task checklist.
- **Confirmed Navigation Architecture**: **Hybrid Unified Navigation** ("All") featuring:
  - An interactive **Home Hub Dashboard** with live At-A-Glance widgets and quick launch tiles.
  - A persistent **Bottom Navigation Dock** for instant one-tap switching between primary apps.
  - The iconic **Aether 9-Dot App Switcher ("Waffle")** and **Aether Account Manager** accessible from the universal top bar in every app.
- **Build Fix Strategy**: Resolve the KSP/Kotlin compiler incompatibility in `app/build.gradle.kts` by removing unused KSP room/moshi compiler dependencies, ensuring lightning-fast compile times and APK stability.

---

### 1. Overview & Core Concept

- **What It Does**: Unifies six essential daily digital tools under a single, synchronized account and design language. Users can search topics, read and send emails, write documents, explore maps with turn-by-turn routing, edit photos with AI tools, and schedule meetings seamlessly.
- **Target Audience / Persona**: Users seeking an intuitive, privacy-conscious, Google-caliber ecosystem that feels cohesive, responsive, and tactile without unnecessary OS simulation bloat.
- **Key Value**: Cross-app synergy—attachments in Mail open in Docs/Drive, calendar events surface on the Search home widget, and photos can be viewed and enhanced with instant on-device tools.

---

### 2. User Experience & Visual Design

#### Key User Flows
1. **Discover & Search**: The user enters the app to find a clean search bar and smart At-a-Glance widget (weather, upcoming calendar event, flight tracker). Querying displays an AI synthesized answer, source chips, and web results.
2. **Mail & Communication**: Tap Mail to browse categorized emails, review formatted threads with sender avatars, toggle stars, and compose new emails via a floating action button.
3. **Docs Collaboration**: In Aether Drive, tap any document or hit "+" to open the full Aether Docs editor, change headings, format text, and view real-time auto-saving.
4. **Spatial Exploration**: In Aether Maps, pan and zoom an interactive custom Canvas map, tap POI markers for ratings and hours, and launch turn-by-turn navigation with step instructions.
5. **Creative Media**: In Aether Photos, browse memories by time and tag, open any photo in fullscreen, and test AI Magic Eraser and Auto-Enhance filters.
6. **Time & Tasks**: In Calendar, review daily agenda blocks and check off completed tasks.
7. **Switching & Account**: At any point, tap the 9-dot Aether launcher to jump anywhere, or tap the profile avatar to view storage usage and switch between Personal and Work profiles.

#### Visual Identity & Theme
- **Aesthetic Direction**: Minimalist Clean Tech — refined whitespace, generous 16–20dp padding, subtle tonal elevations, and smooth rounded corners (16–28dp).
- **Color Palette**:
  - `AetherBlue` (`#2979FF`): Primary brand color, Search & Drive accent.
  - `AetherTeal` (`#00BFA5`): Secondary brand color, Maps & Navigation accent.
  - `AetherAmber` (`#FFAB00`): Tertiary brand color, Photos & Keep accent.
  - `AetherCoral` (`#FF5252`): Alert & Mail accent.
  - `Background`: Crisp clean neutral (`#F9FAFB` in Light, `#121316` in Dark).
  - `Surface`: Pure white `#FFFFFF` / Deep slate `#1E2024`.
- **Iconography**: Custom-designed adaptive app icons featuring clean geometric line and glyph motifs with vibrant tinted circular backgrounds.

---

### 3. Key Product Decisions & Trade-Offs

- **Real Interactive Views over Fake OS Shell**: Instead of windowed desktop clutter or fake terminal prompts, each app is a polished, full-fidelity mobile screen matching modern Android Material 3 guidelines.
- **Cross-App Data Reactivity**: Centralized `AetherSuiteViewModel` holding reactive `StateFlow` streams so edits in Docs reflect in Drive, emails link to docs, and calendar items trigger search dashboard widgets.
- **Compilation Stability**: Cleaning up the KSP compiler Gradle configuration immediately eliminates the AWT `NullPointerException` and ensures smooth incremental builds.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Aether Suite Shell                             │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ AetherTopBar: Brand Glyph • App Title • Search • 9-Dot • Avatar  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                 │                                      │
│  ┌──────────────────────────────┴───────────────────────────────────┐  │
│  │                     Active Screen Container                      │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │  │
│  │  │   Search    │ │    Mail     │ │ Drive & Doc │ │    Maps     │ │  │
│  │  │  & Pulse    │ │   & Inbox   │ │   Editor    │ │ & Navigation│ │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘ │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                 │  │
│  │  │   Photos    │ │  Calendar   │ │    Home     │                 │  │
│  │  │  & Enhance  │ │   & Tasks   │ │  Dashboard  │                 │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘                 │  │
│  └──────────────────────────────┬───────────────────────────────────┘  │
│                                 │                                      │
│  ┌──────────────────────────────┴───────────────────────────────────┐  │
│  │ AetherBottomNav: Home • Search • Mail • Drive • Maps • All Apps  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                 │                                      │
│  ┌──────────────────────────────┴───────────────────────────────────┐  │
│  │ Overlays: Aether 9-Dot Launcher Sheet • Account & Storage Dialog │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────┬──────────────────────────────────────┘
                                  │
                 ┌────────────────┴───────────────┐
                 │     AetherSuiteViewModel       │
                 │  - App State & BackStack       │
                 │  - User Storage & Accounts     │
                 │  - Emails, Docs, POIs, Photos  │
                 │  - Calendar Events & Tasks     │
                 └────────────────────────────────┘
```

#### Step-by-Step Execution Plan
1. **Fix Build & Dependencies**: Edit `app/build.gradle.kts` to remove conflicting KSP annotation processors and sync dependencies.
2. **Update Metadata & Identity**: Configure `metadata.json` and `strings.xml` for "Aether Cloud".
3. **Build Core Domain Models**: Update data classes with Aether branding, app types, and cross-linked records.
4. **State Management**: Refine `AetherSuiteViewModel` with full state handling, back navigation, search synthesis, and account switching.
5. **Universal Navigation & Components**:
   - `AetherTopBar`: Minimalist header with 9-dot launcher trigger and profile badge.
   - `AetherWaffleSheet`: 9-dot app launcher modal with custom colored icons and badge indicators.
   - `AetherAccountDialog`: Multi-account switcher with cloud storage quota breakdown.
   - `AetherBottomNav`: Ergonomic navigation bar with insets padding.
6. **Build Full Screen Modules**:
   - `AetherSearchScreen`: Search with AI Overview, voice query, trending tags, and Discover feed.
   - `AetherMailScreen`: Categorized inbox, message thread view, and compose dialog.
   - `AetherDriveScreen`: File explorer and interactive Aether Docs rich text editor.
   - `AetherMapsScreen`: Canvas vector map, place cards, and turn-by-turn navigation.
   - `AetherPhotosScreen`: Gallery, memories, and AI Magic Eraser / Enhance filters.
   - `AetherCalendarScreen`: Timeline agenda, event scheduler, and tasks checklist.
7. **Compilation & Verification**: Run `compile_applet` to verify zero compile errors and test interactions.
