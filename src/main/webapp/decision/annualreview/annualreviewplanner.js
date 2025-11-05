// Restore checkbox states from saved XML data (replaces IE-only XML Data Binding)
function restoreCheckboxStates() {
    var xmlElement = document.getElementById('xml_list');
    if (!xmlElement) return;

    var xmlContent = xmlElement.innerHTML || xmlElement.textContent || xmlElement.innerText;
    if (!xmlContent) return;

    // Parse XML content to find all tags
    var tagRegex = /<([^>]+)>checked<\/\1>/g;
    var match;
    while ((match = tagRegex.exec(xmlContent)) !== null) {
        var fieldName = match[1];

        // Try exact match first
        var checkbox = document.getElementsByName(fieldName)[0];

        // If not found, try case-insensitive search
        if (!checkbox || checkbox.type !== 'checkbox') {
            var allInputs = document.getElementsByTagName('input');
            for (var i = 0; i < allInputs.length; i++) {
                if (allInputs[i].type === 'checkbox' && 
                    allInputs[i].name.toLowerCase() === fieldName.toLowerCase()) {
                    checkbox = allInputs[i];
                    break;
                }
            }
        }

        if (checkbox && checkbox.type === 'checkbox') {
            checkbox.checked = true;
        }
    }
}

// Run after page loads
if (window.addEventListener) {
    window.addEventListener('load', restoreCheckboxStates, false);
} else if (window.attachEvent) {
    window.attachEvent('onload', restoreCheckboxStates);
}