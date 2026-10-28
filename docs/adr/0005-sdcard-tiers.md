# ADR-0005: Shared sdcard default (private virtual card, not home)

- **Status**: Accepted (2026-10-19)
- **Date**: 2026-10-19

## Context

Mounting the user's real home directory read-write at `/sdcard` would expose
`~/.ssh` and other apps' data, break the isolation contract, and litter home.

## Decision

Default to a private virtual card at `<root>/sdcard` (shared by all apps, with
standard folders). Opt-in user-mapped folders and system file pickers (with
per-app grants) cover the rest; whole-home mounting remains an explicit
advanced option, read-only by default with a warning. See ADR-0003 for the
mount rules.

## Consequences

- Apps stay isolated by default.
- `/sdcard` behavior matches scoped-storage expectations on real Android.