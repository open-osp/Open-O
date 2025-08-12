#!/bin/bash
set -e  # Exit on error

echo "Installing python packages to the virtual environment"
./scripts/aider_cli_automation_tools/setup/install_packages.sh

export PATH="/root/.local/bin:$PATH"

echo "Running automation tools"
./.venv/bin/python ./scripts/aider_cli_automation_tools/github_code_scanning/code_scanning_aider.py