#!/usr/bin/env python3
import os
import sys
import requests
import subprocess
from dotenv import load_dotenv

OWNER = "cc-ar-emr"
REPO = "Open-O"
GITHUB_API_URL = f"https://api.github.com/repos/{OWNER}/{REPO}/code-scanning/alerts"
PER_PAGE = 100

# Loads personal access token provided in .env file for github api usage
def load_token():
    load_dotenv(dotenv_path="./scripts/aider_cli_automation_tools/setup/.env")
    token = os.getenv("PERSONAL_ACCESS_TOKEN")
    if not token:
        raise ValueError("PERSONAL_ACCESS_TOKEN not found in environment variables")
    return token

# Fetches all code scanning alerts (goes through every page)
def fetch_code_scanning_alerts(token, state="open", rule_filter=None):
    headers = {
        "Accept": "application/vnd.github+json",
        "Authorization": f"Bearer {token}"
    }

    alerts = []
    page = 1
    
    while True:
        response = requests.get(GITHUB_API_URL, headers=headers, params={"per_page": PER_PAGE, "page": page, "state": state})
        if response.status_code != 200:
            print(f"Error {response.status_code}: {response.text}")
            break

        page_data = response.json()
        if not page_data:
            break

        alerts.extend(page_data)
        page += 1

    if rule_filter:
         alerts = [
            a for a in alerts
            if a["rule"]["id"] == rule_filter
        ]

    return alerts

# Creates prompt to be inputted into aider
def generate_prompt(alert):
    return f"""Please fix this error listed below on the file added to the chat:
            Information about the security issue:
            Rule: {alert['rule']['id']} | Description: {alert['rule']['full_description']} | Severity: {alert['rule']['security_severity_level']} | State: {alert['state']}
            Location of the security issue:
            File Path: {alert['most_recent_instance']['location']['path']} | Start Line: {alert['most_recent_instance']['location']['start_line']} | End Line: {alert['most_recent_instance']['location']['end_line']} | Start Column: {alert['most_recent_instance']['location']['start_column']} | End Column: {alert['most_recent_instance']['location']['end_column']}
            """

# Runs aider while inputting file path and prompt to be used in run_aider shell file
def run_aider(path, prompt):
    process = subprocess.Popen(
        ["./scripts/aider_cli_automation_tools/setup/run_aider.sh", path],
        stdin=subprocess.PIPE,
        stdout=None,  # Let output go to terminal
        stderr=None,  # Let errors go to terminal
        text=True,
        bufsize=1 
    )

    for line in prompt.splitlines():
        process.stdin.write(line)
        process.stdin.flush()

    process.stdin.close()
    process.wait()

# Runs on startup, sets up and calls all required methods for code scanning through aider
def main():
    issue_type = input("Enter the rule id that you want to address (or leave blank for all): ").strip()
    token = load_token()

    # Only fetch relevant alerts
    alerts = fetch_code_scanning_alerts(token, rule_filter=issue_type if issue_type else None)

    if not alerts:
        print("No alerts found.")
        return

    print(f"Found {len(alerts)} matching code scanning alerts.")

    for alert in alerts:
        rule_id = alert['rule']['id']
        path = alert['most_recent_instance']['location']['path']

        # Avoids javascript files, will be removed later on when going through those files
        if path.endswith(".js"):
            print(f"Skipping JS file: {path}")
            continue

        prompt = generate_prompt(alert)
        run_aider(path, prompt)


if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print(f"Unexpected error: {e}")
        sys.exit(1)