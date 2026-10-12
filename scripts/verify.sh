#!/bin/sh
set -eu
mise trust
mise install
mise exec -- gradle test
