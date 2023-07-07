let QuillParchment = Quill.import('parchment');
let Delta = Quill.import('delta');
class TemplatePlaceholderQuill {
    constructor(formId, shortCodes, demographicPlaceholderValues, options) {
        let quillElement = document.getElementById(formId);
        if (!quillElement) {
            console.error(`TemplatePlaceholderQuill: element \'${formId}\' does not exist`)
        }
        
        if (options) {
            this.setWebServiceParameters(options);
        }
        this.quillElement = quillElement;
        let noToolbar = (options != null && options['noToolBar']);
        this.quill = new Quill(this.quillElement, {theme: 'snow', modules : { toolbar: noToolbar ? !noToolbar : this.getDefaultToolbarOptions()}});
        
        this.setShortCodes(shortCodes);
        this.setResolvedPlaceHolderValues(demographicPlaceholderValues, false);
        
        let boundFillInKeyHandler = this.fillInKeyHandler.bind(this);
        this.quill.keyboard.addBinding({key: 115}, boundFillInKeyHandler); // bind F4 key
        let boundPreviousFillInKeyHandler = this.previousFillInKeyHandler.bind(this);
        this.quill.keyboard.addBinding({key: 115, shiftKey: true}, boundPreviousFillInKeyHandler); // bind shift + F4 key
        
        // // Bind # to placeholder lookup
        // let addPlaceholderKeyHandler = this.addPlaceholderKeyHandler.bind(this);
        // this.quill.keyboard.addBinding({key: '3', shiftKey: true}, addPlaceholderKeyHandler); // bind # key

        if (!noToolbar) {
            // remove tab indices from toolbar since shortcuts already exist
            this.removeTabindexForToolbar();
            
            let boundImageHandler = this.imageHandler.bind(this);
            this.quill.getModule('toolbar').addHandler('image', boundImageHandler);
        }
    }
    
    setShortCodes(shortCodes) {
        this.shortCodes = shortCodes || [];
        if (this.shortCodes.size > 0) {
            let boundShortCodeHandler = this.shortCodeHandler.bind(this);
            this.quill.keyboard.addBinding({key: 220}, boundShortCodeHandler); // bind slash character '\'
        }
    }
    setResolvedPlaceHolderValues(demographicPlaceholderValues, showMissingValuePopup = false) {
        this.demographicPlaceholderValues = demographicPlaceholderValues || [];
        this.updateDomWithResolvedPlaceHolderValues(showMissingValuePopup);
    }

    setWebServiceParameters(options) {
        this.demographicNo = options['demographicNo'];
        this.appointmentNo = options['appointmentNo'];
        this.oscarContext = options['oscarContext'];
        this.csrfToken = options['csrfToken'];
    }

    updateDomWithResolvedPlaceHolderValues(showMissingValuePopup = false) {
        let noValuePlaceHolders = [];

        // get all markers
        let placeholderMarkers = this.getTemplatePlaceholderMarkers();
        for (let i = 0; i < placeholderMarkers.length; i++) {

            // if resolved value exists for marker, add it
            let resolvedPlaceholder = null;
            let valuesForMarker = this.demographicPlaceholderValues.filter(item => item.marker === placeholderMarkers[i] && !item.noValue);
            if (valuesForMarker.length > 0) {
                resolvedPlaceholder = valuesForMarker[0];
            } else {
                noValuePlaceHolders.push(placeholderMarkers[i]);
            }
            for (let j = 0; j < this.demographicPlaceholderValues.length; j++) {
                let placeholderValue = this.demographicPlaceholderValues[j];
                if (placeholderValue['marker'] === placeholderMarkers[i]) {
                    resolvedPlaceholder = placeholderValue;
                    break;
                }
            }

            let nodes = document.querySelectorAll(`[data-marker="${placeholderMarkers[i]}"]`);
            for (let j = 0; j < nodes.length; j++) {
                let blot = QuillParchment.find(nodes[j]);
                if (resolvedPlaceholder === null || resolvedPlaceholder['noValue']) {
                    // if placeholder value does not exist, add asterisk 
                    if (blot.domNode.firstElementChild) { // if block placeholder
                        blot.domNode.firstElementChild.classList.add('quill-placeholder-no-value');
                    } else {
                        blot.domNode.classList.add('quill-placeholder-no-value');
                    }
                } else {
                    if (resolvedPlaceholder['isBlock']) {
                        // block placeholders are allowed newlines
                        blot.domNode.innerHTML = resolvedPlaceholder['value'].replace(/\\n/g, '\n')
                    } else {
                        blot.domNode.innerHTML = `<span contenteditable="false">${resolvedPlaceholder['value']}</span>`;
                    }
                }
            }
        }

        if (noValuePlaceHolders.length > 0 && showMissingValuePopup) {
            let replacePlaceholdersWithMarker = window.confirm('Not all placeholders on form have data to display. ' +
                'Would you like to replace effected placeholders with fill in marker («»)?');
            if (replacePlaceholdersWithMarker) {
                for (let i = 0; i < noValuePlaceHolders.length; i++) {
                    let noValuePlaceHolder = noValuePlaceHolders[i];
                    let nodes = document.querySelectorAll(`[data-marker="${noValuePlaceHolder}"]`);
                    for (let j = 0; j < nodes.length; j++) {
                        let blot = QuillParchment.find(nodes[j]);
                        blot.replaceWith('text', '«»');
                    }
                }
            }
        }
    }
    
    setContents(newContent) {
        this.quill.setContents(newContent);
    }
    getContents() {
        return this.quill.getContents();
    }
    getContentsWithImagePlaceholders() {
        let delta = this.quill.getContents();
        let deltaWithImagePlaceholders = { ops: [] };
        for (let i = 0; i < delta.ops.length; i++) {
            let richTextItem = delta.ops[i];
            if (richTextItem.insert['ImagePlaceholder'] != null) {
                let imagePlaceholder = richTextItem.insert.ImagePlaceholder;
                // imagePlaceholder.data = '\$\{' + richTextItem.insert.ImagePlaceholder.identifier + '\}';
                deltaWithImagePlaceholders.ops.push({'insert': { 'ImagePlaceholder': { 'identifier' : imagePlaceholder.identifier, 'data' : '\$\{' + imagePlaceholder.identifier + '\}'}}});
            } else { // no template or image, just add to formatted delta
                deltaWithImagePlaceholders.ops.push(richTextItem);
            }
        }
        return deltaWithImagePlaceholders;
    }
    getText() {
        return this.quill.getText().trim();
    }
    getTextWithTemplatePlaceholderIndexes() {
        return this.getContents().map(function (op) {
            return typeof op.insert === 'string' ? op.insert : '#'
        }).join('');
    }
    getHtml() {
        return this.quill.root.innerHTML;
    }
    
    getTemplatePlaceholders() {
        let delta = this.quill.getContents();
        return delta.ops.filter(item => (item.insert.TemplatePlaceholder != null || item.insert.TemplateBlockPlaceholder != null));
    }
    getTemplatePlaceholderMarkers() {
        let placeholders =  this.getTemplatePlaceholders();
        let placeholderMarkers = [];
        for (let i = 0; i < placeholders.length; i++) {
            if (placeholders[i].insert['TemplatePlaceholder']) {
                placeholderMarkers.push(placeholders[i].insert['TemplatePlaceholder'].marker);
            } else if (placeholders[i].insert['TemplateBlockPlaceholder']) {
                placeholderMarkers.push(placeholders[i].insert['TemplateBlockPlaceholder'].marker);
            }
        }
        return placeholderMarkers;
    }
    getDocumentImages() {
        let delta = this.quill.getContents();
        return delta.ops.filter(item => item.insert.ImagePlaceholder != null);
    }
    fetchAndApplyTemplatePlaceHoldersWithFilledValues(placeholdersToResolve, callback) {
        // Check if needed parameters are set
        if (typeof this.demographicNo === 'undefined' || typeof this.csrfToken === 'undefined' || typeof this.oscarContext === 'undefined') {
            console.log('Error: Cannot resolve placeholders, demographicNo, csrfToken, or oscarContext is not set!');
        } else {
            // Get list of needed placeholder keys
            let templatePlaceholderMarkers;
            if (typeof placeholdersToResolve === 'undefined' || placeholdersToResolve.length === 0) {
                templatePlaceholderMarkers = this.getTemplatePlaceholderMarkers();
            } else {
                templatePlaceholderMarkers = placeholdersToResolve;
            }
    
            // Ajax request to fetch data using keys
            const httpRequest = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('demographicNo', this.demographicNo);
            formData.append('appointmentNo', this.appointmentNo);
            formData.append('fieldsToPullListString', templatePlaceholderMarkers.join(';'));
            formData.append(this.csrfToken['name'], this.csrfToken['value']);
    
            // bind events
            let onSendRequestSuccessEvent = this.onSendRequestSuccess.bind(this, callback);
            httpRequest.addEventListener('load', onSendRequestSuccessEvent);
            let onSendRequestErrorEvent = this.onSendRequestError.bind(this);
            httpRequest.addEventListener('error', onSendRequestErrorEvent);
    
            httpRequest.open('POST', this.oscarContext + '/form/FormSmartEncounterAction.do?method=getTemplatePlaceHolderValues');
            httpRequest.send(formData);
        }
    }

    onSendRequestSuccess(callback, event) {
        let data = JSON.parse(event.target.responseText);
        if (data['success'] === true) {
            // Set data values into placeholders innerHTML
            this.setResolvedPlaceHolderValues(data['resolvedPlaceholders'], true);
            if (callback) {
                callback(data['resolvedPlaceholders']);
            }
        }
    }
    onSendRequestError(event) {
        console.log(event);
    }
    
    getFormattedHtmlAndText(headerHtml, footerHtml) {
        let delta = this.quill.getContents();
        let richTextDeltaFormatted = { ops: [] };
        for (let i = 0; i < delta.ops.length; i++) {
            let richTextItem = delta.ops[i];
            if (richTextItem.insert['TemplatePlaceholder'] != null) { // replace value and add as standard delta input
                let fieldName = richTextItem.insert.TemplatePlaceholder.marker;
                let resolvedDelta = {insert: `\$\{${fieldName}\}`};
                richTextDeltaFormatted.ops.push(resolvedDelta);
            } else if (richTextItem.insert['TemplateBlockPlaceholder'] != null) {
                let fieldName = richTextItem.insert.TemplateBlockPlaceholder.marker;
                let resolvedDelta = {insert: `\$\{${fieldName}\}`};
                richTextDeltaFormatted.ops.push(resolvedDelta);
            } else if (richTextItem.insert['ImagePlaceholder'] != null) {
                let imageIdentifier = richTextItem.insert.ImagePlaceholder.identifier;
                let resolvedDelta = {insert: `\$\{${imageIdentifier}\}`};
                richTextDeltaFormatted.ops.push(resolvedDelta);
            } else { // no template or image, just add to formatted delta
                richTextDeltaFormatted.ops.push(richTextItem);
            }
        }
        this.quill.setContents(richTextDeltaFormatted);

        if (headerHtml === null) { headerHtml = ''; }
        if (footerHtml === null) { footerHtml = ''; }
        let contentHtml = this.getHtml();
        let css = 'body { font-family: sans-serif; tab-size: 4; font-size: 13px; } p, ol, ul, pre, blockquote, h1, h2, h3, h4, h5, h6 { margin: 0; padding: 0; } @page { @top-center { content: element(header); } } #pageHeader{ position: running(header); span.tab { display: inline-block; } }';
        let formattedHtml = `<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><head><title>Smart Encounter Printout</title><meta http-equiv="Content-Type" content="text/html; charset=utf-8"/><style type="text/css">${css}</style></head><body><div class="header">${headerHtml}</div><div class="content">${contentHtml}</div><div class="footer">${footerHtml}</div></body></html>`;
        
        let formattedText = this.getText();
        this.quill.setContents(delta);

        return {html: formattedHtml, text: formattedText};
    }

    insertTextAtCaret(textToInsert) {
        let caretPosition = this.quill.getSelection(true);
        this.quill.insertText(caretPosition, textToInsert);
    }

    insertPlaceholderAtCaret(placeholderToInsert) {
        let caretPosition = this.quill.getSelection(true);
        
        this.quill.insertEmbed(caretPosition.index, placeholderToInsert, Quill.sources.USER);
        if ('DIV' === placeholderToInsert.nodeName) {
            this.quill.setSelection(caretPosition.index + 1, Quill.sources.USER);
        } else {
            this.quill.insertText(caretPosition.index + 1, ' ', Quill.sources.USER);
            this.quill.setSelection(caretPosition.index + 2, Quill.sources.USER);
        }
    }

    shortCodeHandler(range, context) {
        let prefix = context.prefix.trim();
        let shortCodeString = prefix;
        if (prefix.includes(' ')) {
            shortCodeString = prefix.substring(prefix.lastIndexOf(' ')).trim();
        }
        let shortCode = this.shortCodes.get(shortCodeString.toLowerCase());
        if (shortCode) {
            // create new delta that retains all data prior to the short code name and deletes entered name
            let changes = new Delta().retain(range.index - shortCodeString.length).delete(shortCodeString.length);
            
            let placeholderNamesToResolve = [];
            // iterate over shortcode delta ops and add to end of new delta
            for (let i = 0; i < shortCode.ops.length; i++) {
                let embed = shortCode.ops[i];
                changes.ops.push(embed);
                if (embed.insert['TemplatePlaceholder']) {
                    placeholderNamesToResolve.push(embed.insert['TemplatePlaceholder'].marker);
                } else if (embed.insert['TemplateBlockPlaceholder']) {
                    placeholderNamesToResolve.push(embed.insert['TemplateBlockPlaceholder'].marker);
                }
            }
            
            // all data after short code ops will be retained
            this.quill.updateContents(changes, Quill.sources.USER);
            
            // call service to fill in placeholders if demographic is used (aka not in settings screen)
            if (this.demographicNo) {
                this.fetchAndApplyTemplatePlaceHoldersWithFilledValues(placeholderNamesToResolve);
            }
        } else { // insert character
            this.quill.insertText(range.index, '\\');
        }
    };
    
    fillInKeyHandler(range, context) {
        let text = this.getTextWithTemplatePlaceholderIndexes();
        
        // get index of next fill in marker
        let indexToSelect = text.indexOf('«»', range.index);
        if (indexToSelect === range.index) {
            // selecting existing selection, add 1 to index and find next
            indexToSelect = text.indexOf('«»', range.index + 1);
        }
        // if no «» is found, look from the start
        if (indexToSelect === -1) {
            indexToSelect = text.indexOf('«»');
        }
        this.quill.setSelection(indexToSelect, 2, Quill.sources.USER);
    };

    previousFillInKeyHandler(range, context) {
        let text = this.getTextWithTemplatePlaceholderIndexes();
        let firstHalf = text.substr(0, range.index);
        // get index of next fill in marker
        let indexToSelect = firstHalf.lastIndexOf('«»');
        if (indexToSelect === range.index && range.index > 0) {
            // selecting existing selection, add 1 to index and find next
            indexToSelect = firstHalf.substr(0, range.index - 1).lastIndexOf('«»');
        }

        // if no «» is found, look from the end
        if (indexToSelect === -1) {
            let lastHalf = text.substr(range.index);
            indexToSelect = lastHalf.lastIndexOf('«»') + 2;
        }
        this.quill.setSelection(indexToSelect, 2, Quill.sources.USER);
    };

    imageHandler() {
        let thisQuill = this.quill;
        let thisCreateUuid = this.createUuid;
        let fileInput = this.quillElement.querySelector('input.ql-image[type=file]');
        if (fileInput == null) {
            fileInput = document.createElement('input');
            fileInput.setAttribute('type', 'file');
            fileInput.setAttribute('accept', 'image/png, image/gif, image/jpeg, image/bmp, image/x-icon');
            fileInput.style.display = 'none';
            fileInput.classList.add('ql-image');
            fileInput.addEventListener('change', function () {
                if (fileInput.files != null && fileInput.files[0] != null) {
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        let range = thisQuill.getSelection(true);
                        let imageBlot = ImagePlaceholder.create({ data: e.target.result, identifier: 'image_' + thisCreateUuid() });
                        thisQuill.insertEmbed(range.index, imageBlot, Quill.sources.USER);
                        thisQuill.setSelection(range.index + 1, Quill.sources.USER);
                        fileInput.value = '';
                    };
                    reader.readAsDataURL(fileInput.files[0]);
                }
            });
            this.quillElement.appendChild(fileInput);
        }
        fileInput.click();
    };
    
    getDefaultToolbarOptions() {
        return [
            ['bold', 'italic', 'underline', 'strike'], ['blockquote', 'code-block'], [{ 'header': 1 }, { 'header': 2 }],
            [{ 'list': 'ordered'}, { 'list': 'bullet' }], [{ 'script': 'sub'}, { 'script': 'super' }],
            [{ 'direction': 'rtl' }], [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
            [{ 'color': [] }, { 'background': [] }], [{ 'font': [] }], [{ 'align': [] }], ['image'], ['clean']
        ];
    }
    removeTabindexForToolbar() {
        let toolbarElements = document.querySelector('.ql-toolbar');
        for (let i = 0; i < toolbarElements.children.length; i++) {
            let toolbarSubsection = toolbarElements.children[i];
            for (let i = 0; i < toolbarSubsection.children.length; i++) {
                let toolbarControl = toolbarSubsection.children[i];
                if (toolbarControl.classList.contains("ql-picker")) {
                    // if control is a picker the control element is a <span> acting as a dropdown box
                    toolbarControl = toolbarControl.firstElementChild;
                }
                if (toolbarControl.type === 'button' || toolbarControl.type === 'select' || toolbarControl.tabIndex !== -1) {
                    toolbarControl.tabIndex = -1;
                }
            }
        }
    }

    createUuid() {
        return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
            (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
        );
    }
}

