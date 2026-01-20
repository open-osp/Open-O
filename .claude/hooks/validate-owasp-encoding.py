#!/usr/bin/env python3
"""
OWASP Encoding Validator Hook for Claude Code

This hook validates that JSP and Java files use proper OWASP encoding
to prevent XSS vulnerabilities.

Exit codes:
- 0: Safe patterns detected or non-applicable file
- 2: Unsafe patterns detected (blocks the operation with feedback)
"""

import json
import re
import sys


def get_file_content_from_input(tool_input: dict) -> tuple[str, str]:
    """Extract file path and content from tool input."""
    file_path = tool_input.get("file_path", "")

    # For Write tool, content is in 'content' field
    # For Edit tool, new content is in 'new_string' field
    content = tool_input.get("content", "") or tool_input.get("new_string", "")

    return file_path, content


def check_jsp_unsafe_patterns(content: str) -> list[str]:
    """
    Check JSP content for unsafe EL expressions without OWASP encoding.

    Unsafe patterns:
    - ${param.xxx} - raw parameter access
    - ${requestScope.xxx} - raw request scope access
    - ${sessionScope.xxx} - raw session scope access
    - ${xxx} without Encode wrapper - any unencoded EL expression

    Safe patterns:
    - ${Encode.forHtml(...)}
    - ${Encode.forJavaScript(...)}
    - ${Encode.forHtmlAttribute(...)}
    - <c:out value="${...}"/>
    - ${fn:escapeXml(...)}
    """
    issues = []

    # Pattern for raw EL expressions that are NOT inside Encode.for* or c:out
    # This regex finds ${...} expressions
    el_pattern = r'\$\{([^}]+)\}'

    # Safe wrappers that indicate proper encoding
    safe_wrappers = [
        r'Encode\.for\w+\s*\(',  # Encode.forHtml(), Encode.forJavaScript(), etc.
        r'fn:escapeXml\s*\(',    # JSTL escapeXml function
    ]

    # Check for c:out tags which are safe
    cout_pattern = r'<c:out\s+[^>]*value\s*=\s*["\']?\$\{[^}]+\}["\']?[^>]*/?\s*>'

    for match in re.finditer(el_pattern, content):
        el_expr = match.group(0)
        el_content = match.group(1)

        # Skip if this is inside a safe wrapper
        is_safe = False

        # Check if expression uses safe encoding functions
        for safe_pattern in safe_wrappers:
            if re.search(safe_pattern, el_content):
                is_safe = True
                break

        if is_safe:
            continue

        # Check if this ${} is inside a <c:out> tag
        # Look backwards from the match to find if we're in a c:out
        start_pos = max(0, match.start() - 200)
        context_before = content[start_pos:match.start()]
        context_after = content[match.end():match.end() + 50]

        # Simple check: if <c:out is found before and /> or </c:out> after
        if '<c:out' in context_before and ('/' in context_after[:10] or '</c:out>' in context_after):
            is_safe = True

        if is_safe:
            continue

        # Check for high-risk expressions (user input)
        high_risk_patterns = [
            r'param\.',           # Request parameters
            r'requestScope\.',    # Request scope
            r'sessionScope\.',    # Session scope
            r'cookie\.',          # Cookies
            r'header\.',          # Headers
            r'initParam\.',       # Init parameters
        ]

        is_high_risk = any(re.search(p, el_content) for p in high_risk_patterns)

        if is_high_risk:
            issues.append(
                f"CRITICAL: Unencoded user input in EL expression: {el_expr}\n"
                f"  This can lead to XSS vulnerabilities.\n"
                f"  Use: ${{Encode.forHtml({el_content})}} or <c:out value=\"{el_expr}\"/>"
            )
        else:
            # For other expressions, warn but be less strict
            # Check if it looks like it might contain user data
            if re.search(r'[Nn]ame|[Vv]alue|[Ii]nput|[Dd]ata|[Tt]ext|[Mm]essage|[Cc]omment', el_content):
                issues.append(
                    f"WARNING: Potentially unsafe EL expression: {el_expr}\n"
                    f"  If this contains user input, use OWASP encoding.\n"
                    f"  Suggested: ${{Encode.forHtml({el_content})}} or <c:out value=\"{el_expr}\"/>"
                )

    return issues


def check_java_unsafe_patterns(content: str) -> list[str]:
    """
    Check Java content for unsafe output patterns without OWASP encoding.

    Unsafe patterns:
    - response.getWriter().write(userInput)
    - out.println(userInput) without encoding
    - PrintWriter writing raw strings with user data

    Safe patterns:
    - Encode.forHtml(userInput)
    - Encode.forJavaScript(userInput)
    - response.getWriter().write(Encode.forHtml(...))
    """
    issues = []

    # Pattern for direct output to response without encoding
    # Look for response.getWriter().write/print patterns
    output_patterns = [
        (r'response\s*\.\s*getWriter\s*\(\s*\)\s*\.\s*(?:write|print|println)\s*\(\s*(?!Encode\.)',
         "response.getWriter().write() without OWASP encoding"),
        (r'out\s*\.\s*(?:write|print|println)\s*\(\s*(?!Encode\.)',
         "out.print() without OWASP encoding"),
        (r'PrintWriter\s+\w+\s*=.*;\s*\w+\s*\.\s*(?:write|print|println)\s*\(\s*(?!Encode\.)',
         "PrintWriter output without OWASP encoding"),
    ]

    for pattern, description in output_patterns:
        if re.search(pattern, content):
            # Check if this is outputting a variable that could be user input
            # Look for common variable patterns
            match = re.search(pattern + r'([^)]+)\)', content)
            if match:
                output_content = match.group(1) if match.lastindex else ""
                # Skip if it's a literal string only
                if output_content.strip().startswith('"') and output_content.strip().endswith('"'):
                    if '+' not in output_content:  # Pure string literal, no concatenation
                        continue

                issues.append(
                    f"WARNING: {description}\n"
                    f"  If outputting user data, wrap with Encode.forHtml() or appropriate encoder.\n"
                    f"  Example: response.getWriter().write(Encode.forHtml(userInput))"
                )

    return issues


def validate_content(file_path: str, content: str) -> tuple[bool, list[str]]:
    """
    Validate file content for OWASP encoding compliance.

    Returns:
        (is_safe, issues): Tuple of safety status and list of issues found
    """
    if not file_path or not content:
        return True, []

    issues = []

    # Check based on file type
    if file_path.endswith('.jsp'):
        issues.extend(check_jsp_unsafe_patterns(content))
    elif file_path.endswith('.java'):
        issues.extend(check_java_unsafe_patterns(content))

    # Critical issues start with "CRITICAL:"
    has_critical = any(issue.startswith("CRITICAL:") for issue in issues)

    return not has_critical, issues


def main():
    """Main entry point for the hook."""
    try:
        # Read JSON input from stdin
        input_data = json.load(sys.stdin)

        # Extract tool input
        tool_input = input_data.get("tool_input", {})
        tool_name = input_data.get("tool_name", "")

        # Only process Edit and Write tools
        if tool_name not in ("Edit", "Write"):
            sys.exit(0)

        # Get file path and content
        file_path, content = get_file_content_from_input(tool_input)

        # Only check JSP and Java files
        if not (file_path.endswith('.jsp') or file_path.endswith('.java')):
            sys.exit(0)

        # Validate content
        is_safe, issues = validate_content(file_path, content)

        if issues:
            # Output feedback to stderr
            print("\n=== OWASP Encoding Validator ===", file=sys.stderr)
            print(f"File: {file_path}\n", file=sys.stderr)
            for issue in issues:
                print(f"{issue}\n", file=sys.stderr)

            if not is_safe:
                print("BLOCKED: Critical XSS vulnerability detected.", file=sys.stderr)
                print("Please use OWASP encoding for all user input.", file=sys.stderr)
                print("\nOWASP Encoder methods:", file=sys.stderr)
                print("  - Encode.forHtml() - HTML body content", file=sys.stderr)
                print("  - Encode.forHtmlAttribute() - HTML attributes", file=sys.stderr)
                print("  - Encode.forJavaScript() - JavaScript contexts", file=sys.stderr)
                print("  - <c:out value=\"${...}\"/> - JSP safe output", file=sys.stderr)
                sys.exit(2)
            else:
                print("WARNING: Potential issues detected but not blocking.", file=sys.stderr)
                print("Please review the warnings above.", file=sys.stderr)

        sys.exit(0)

    except json.JSONDecodeError as e:
        print(f"Error parsing JSON input: {e}", file=sys.stderr)
        sys.exit(0)  # Don't block on parse errors
    except Exception as e:
        print(f"Error in OWASP validation hook: {e}", file=sys.stderr)
        sys.exit(0)  # Don't block on unexpected errors


if __name__ == "__main__":
    main()
