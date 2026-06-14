# 📊 Firebase Analytics Guide - Colors

This document outlines all the custom analytics implemented in the **Colors** project, explains why
they are tracked, and how to interpret the metrics in the Firebase Console.

---

## 1. User Progression & Segmentation

### `current_level` (User Property)

* **What it tracks**: The highest level the user has reached.
* **Why it's useful**: This is the most powerful tool for **segmentation**. It allows you to filter
  all other reports by "Level Range".
* **How to understand**:
    * Create **Audiences** (e.g., "Beginners" levels 1-5, "Experts" levels 20+).
    * Compare retention between these groups to see if players who reach higher levels are more
      loyal.

---

## 2. Gameplay & Difficulty Analysis

### `level_start`

* **Parameters**: `level` (Int)
* **Why it's useful**: Measures the entry point for every level.

### `level_end`

* **Parameters**: `level` (Int)
* **Why it's useful**: Measures successful completion.
* **Metric to watch**: **Drop-off Rate**.
    * `Drop-off = (Starts at Level X - Ends at Level X)`.
    * If a specific level has a high drop-off, it might be too hard or boring.

### `level_retry`

* **Parameters**: `level` (Int)
* **Why it's useful**: Tracks when a user manually resets the board.
* **How to understand**: High retry counts on early levels indicate a steep learning curve that
  might frustrate new players.

### `mistake_occurred`

* **Parameters**: `level` (Int)
* **Why it's useful**: Logs every time the "Wrong Color" dialog appears.
* **How to understand**: High mistake rates relative to level completions suggest the color
  differences might be too subtle or the grid too dense for that level.

---

## 3. Monetization & Ad Performance

### `ad_reward_requested`

* **Parameters**: `ad_unit` ("undo" or "hints")
* **Why it's useful**: Measures the **intent** to watch an ad.

### `ad_reward_received`

* **Parameters**: `ad_unit` ("undo" or "hints")
* **Why it's useful**: Measures successful ad completions and reward delivery.
* **Metric to watch**: **Ad Completion Rate**.
    * `Completion % = (Received / Requested) * 100`.
    * If "undo" has a much lower completion rate than "hints", players might find the penalty of a
      mistake less frustrating than watching a 30s ad.

---

## 4. Feature Engagement

### `hint_clicked` / `hint_confirmed`

* **Why it's useful**: Measures how often users seek help.
* **How to understand**: Compares curiosity vs. actual usage. Since the confirmation dialog was
  removed, these are now logged simultaneously to maintain historical data consistency.

### `out_of_hints_dialog_shown`

* **Why it's useful**: Measures **unsatisfied demand**.
* **How to understand**: If this event triggers frequently, users want to play more but are stuck.
  This is a prime opportunity to offer more rewarded ads or a small "Hint Pack" IAP.

### `mute_toggle`

* **Parameters**: `is_muted` (Boolean)
* **Why it's useful**: Tracks user comfort with the game's audio.
* **How to understand**: If most users mute the game immediately, consider reviewing your sound
  effects (`click_005.ogg`) or adding background music.

---

## 5. Retention Tracking

### `app_open`

* **Why it's useful**: Tracks session frequency.
* **How to understand**: Used in the **Retention Report** to see how many users come back on Day 1,
  Day 7, and Day 30.

---

## 🚀 Pro-Tips for Firebase Console

1. **Register Custom Dimensions**: Go to *Custom Definitions* in Firebase and register `level` and
   `ad_unit` as custom dimensions to see them in your reports.
2. **DebugView**: Use the `DebugView` tab while developing to see events appearing in real-time.
3. **Funnel Exploration**: Create a funnel in Firebase: `level_start` -> `hint_clicked` ->
   `level_end` to see how many players need help to finish a level.
