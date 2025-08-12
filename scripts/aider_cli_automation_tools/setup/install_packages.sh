#!/bin/bash

set -e  # Exit if any command fails

# Prevent re-installing if already done
if [ -f ".venv/.installed_marker" ]; then
    echo "Packages already installed. Skipping."
    exit 0
fi

# Create the virtual environment if it doesn't exist
if [ ! -d ".venv" ]; then
    echo "Installing Python system packages..."
    apt update
    apt install -y python3-venv python3-pip

    echo "Creating virtual environment..."
    python3 -m venv .venv
    source .venv/bin/activate
fi

# Install packages
python3 -m pip install requests
python3 -m pip install python-dotenv
python -m pip install uv
uv tool install --force --python python3.12 --with pip aider-chat@latest

echo "All packages have been installed."

# Mark installation as complete
touch .venv/.installed_marker