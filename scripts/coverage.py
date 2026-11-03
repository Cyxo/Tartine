#!/usr/bin/env python3
import os, re
from pathlib import Path

ROOT = Path(os.environ.get("MISE_PROJECT_ROOT") or Path(__file__).resolve().parent.parent)
BASE = ROOT / "framework" / "src" / "main" / "java" / "android"
if not BASE.is_dir():
    raise SystemExit("Framework android sources missing: %s" % BASE)

METHOD_RE = re.compile(r'^\s*(?:public|protected|private|static)\b.*\(\s*.*\s*\)\s*\{\s*$')
SKIP_RE = re.compile(r'\b(?:class|interface|enum|@interface)\b')
STUB = "Tartine: not implemented:"

def scan(files):
    methods = stubs = 0
    for path in files:
        try:
            with open(path, encoding="utf-8", errors="replace") as fh:
                for line in fh:
                    if STUB in line:
                        stubs += 1
                    if METHOD_RE.match(line) and not SKIP_RE.search(line):
                        methods += 1
        except OSError:
            pass
    return methods, stubs

def pct(i, t):
    if t == 0:
        return "n/a"
    return "%.1f" % (i * 100.0 / t)

def main():
    print("Tartine framework coverage")
    print("Estimate: methods carrying logic vs auto-generated stubs in")
    print("  " + str(BASE))
    print()
    print("%-24s %8s %8s %8s %8s" % ("Package", "Methods", "Impl", "Stubs", "Impl %"))
    print("%-24s %8s %8s %8s %8s" % ("--------------------", "--------", "------", "------", "------"))
    rows = []
    for d in sorted(p for p in BASE.iterdir() if p.is_dir()):
        fs = [p for p in d.rglob("*.java")]
        m, s = scan(fs)
        rows.append((d.name, m, m - s, s))
    tops = sorted(BASE.glob("*.java"))
    if tops:
        m, s = scan(tops)
        rows.append(("(default pkg)", m, m - s, s))
    tm = ti = ts = 0
    for name, m, i, s in rows:
        tm += m; ti += i; ts += s
        print("%-24s %8d %8d %8d %7s%%" % (name, m, i, s, pct(i, m)))
    print()
    print("%-24s %8d %8d %8d %7s%%" % ("TOTAL", tm, ti, ts, pct(ti, tm)))
    print()
    print("Caveat: heuristic counts method/constructor declarations ending in {")
    print("A body that merely delegates to another stub still counts as logic;")
    print("refined bodies are audited by hand. Source scan only; see")
    print("`mise run refs` for unresolved-reference data.")

if __name__ == "__main__":
    main()
