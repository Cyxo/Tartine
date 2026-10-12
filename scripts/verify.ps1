$ErrorActionPreference = "Stop"
mise trust | Out-Null
mise install
mise exec -- gradle test
