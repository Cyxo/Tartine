# ADR-0006: Translation path becomes the primary run/gui

- **Status**: Accepted
- **Date**: 2026-10-30

## Context

The smoke path (`tartine.tx`) reached parity with the interpreter for the
fixture app: first measure/layout/draw all print successfully and the desktop
window shows a blitted frame. Keeping two entry points confused users.

## Decision

`mise run run` and `mise run gui` now drive the translation loader
(`tartine.Main run`/`gui`); `txrun`/`txgui` remain as aliases.
`firstDrawFrame` still hands a single first-draw surface to the window.

## Update (2026-10-29)

The interpreter was removed (ADR-0008), so `irun`/`igui` no longer exist. The
translation path is the only one left, which is what this ADR pointed toward.

## Consequences

- New work targets the translation path (the only VM since ADR-0008).
- Less indirection in day-to-day probing.
- Log prefixes for the primary path use `gui:` instead of `txgui:`.