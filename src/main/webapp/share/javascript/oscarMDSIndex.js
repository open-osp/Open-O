/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of CommonLabResultData
 *
 *  Refactored to remove prototype.js dependencies and use native JS/fetch API
 */


/************init global data methods*****************/
let oldestLab;

/**
 * Helper function to show an element
 * @param {HTMLElement|string} el - Element or element ID
 */
function showElement(el) {
    const element = typeof el === 'string' ? document.getElementById(el) : el;
    if (element) element.style.display = '';
}

/**
 * Helper function to hide an element
 * @param {HTMLElement|string} el - Element or element ID
 */
function hideElement(el) {
    const element = typeof el === 'string' ? document.getElementById(el) : el;
    if (element) element.style.display = 'none';
}

/**
 * Helper function to serialize form data to URL-encoded string
 * @param {HTMLFormElement|string} form - Form element or form ID
 * @returns {string}
 */
function serializeForm(form) {
    const formElement = typeof form === 'string' ? document.getElementById(form) : form;
    if (!formElement) return '';
    const formData = new FormData(formElement);
    return new URLSearchParams(formData).toString();
}

/**
 * Helper function to serialize form data to object
 * @param {HTMLFormElement|string} form - Form element or form ID
 * @returns {Object}
 */
function serializeFormToObject(form) {
    const formElement = typeof form === 'string' ? document.getElementById(form) : form;
    if (!formElement) return {};
    const formData = new FormData(formElement);
    const obj = {};
    formData.forEach((value, key) => {
        if (obj.hasOwnProperty(key)) {
            if (!Array.isArray(obj[key])) {
                obj[key] = [obj[key]];
            }
            obj[key].push(value);
        } else {
            obj[key] = value;
        }
    });
    return obj;
}

function updateDocStatusInQueue(docid) {//change status of queue document link row to I=inactive
    console.log('in updateDocStatusInQueue, docid ' + docid);
    const url = ctx + "/documentManager/inboxManage.do";
    const data = "docid=" + docid + "&method=updateDocStatusInQueue";

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.text())
    .then(text => console.log(text))
    .catch(error => console.error('Error:', error));
}

function saveNext(docid) {
    updateDocumentAndNext('forms_' + docid);
}

function initPatientIds(s) {
    var r = new Array();
    var t = s.split(',');
    for (var i = 0; i < t.length; i++) {
        var e = t[i];
        e.replace(/\s/g, '');
        if (e.length > 0) {
            r.push(e);
        }
    }
    return r;
}

function initTypeDocLab(s) {
    return initHashtableWithList(s);
}

function initPatientDocs(s) {
    return initHashtableWithList(s);
}

function initDocStatus(s) {
    return initHashtableWithString(s);
}

function initDocType(s) {
    return initHashtableWithString(s);
}

function initNormals(s) {
    return initList(s);
}

function initAbnormals(s) {
    return initList(s);
}

function initPatientIdNames(s) {//;1=abc,def;2=dksi,skal;3=dks,eiw
    var ar = s.split(';');
    var r = new Object();
    for (var i = 0; i < ar.length; i++) {
        var e = ar[i];
        if (e.length > 0) {
            var ear = e.split('=');
            if (ear && ear != null && ear.length > 1) {
                var k = ear[0];
                var v = ear[1];
                r[k] = v;
            }
        }
    }
    return r;
}

function initHashtableWithList(s) {//for typeDocLab,patientDocs
    s = s.replace('{', '');
    s = s.replace('}', '');
    if (s.length > 0) {
        var sar = s.split('],');
        var r = new Object();
        for (var i = 0; i < sar.length; i++) {
            var ele = sar[i];
            ele = ele.replace(/\s/g, '');
            var elear = ele.split('=');
            var key = elear[0];
            var val = elear[1];
            val = val.replace('[', '');
            val = val.replace(']', '');
            val = val.replace(/\s/g, '');
            //console.log(key);
            //console.log(val);
            var valar = val.split(',');
            r[key] = valar;
        }
        return r;
    } else {
        return new Object();
    }

}

function initHashtableWithString(s) {//for docStatus,docType
    s = s.replace('{', '');
    s = s.replace('}', '');
    s = s.replace(/\s/g, '');
    var sar = s.split(',');
    var r = new Object();
    for (var i = 0; i < sar.length; i++) {
        var ele = sar[i];
        if (ele.length > 0) {
            var ear = ele.split('=');
            if (ear.length > 0) {
                var key = ear[0];
                var val = ear[1];
                r[key] = val;
            }
        }
    }
    return r;
}

function initList(s) {//normals,abnormals
    s = s.replace('[', '');
    s = s.replace(']', '');
    s = s.replace(/\s/g, '');
    if (s.length > 0) {
        var sar = s.split(',');
        return sar;
    } else {
        var re = new Array();
        return re;
    }
}

/********************global data util methods *****************************/
function getDocLabFromCat(cat) {
    if (cat.length > 0) {
        return typeDocLab[cat];
    }
}

function removeIdFromDocStatus(doclabid) {
    delete docStatus[doclabid];
}

function removeIdFromDocType(doclabid) {
    if (doclabid && doclabid != null) {
        delete docType[doclabid];
    }
}

function removeIdFromTypeDocLab(doclabid) {
    for (var j = 0; j < types.length; j++) {
        var cat = types[j];
        var a = typeDocLab[cat];
        if (a && a != null) {
            if (a.length > 0) {
                var i = a.indexOf(doclabid);
                if (i != -1) {
                    a.splice(i, 1);
                    typeDocLab[cat] = a;
                }
            } else {
                delete typeDocLab[cat];
            }
        }
    }
}

function removeNormal(doclabid) {
    var index = normals.indexOf(doclabid);
    if (index != -1) {
        normals.splice(index, 1);
    }
}

function removeAbnormal(doclabid) {
    var index = abnormals.indexOf(doclabid);
    if (index != -1) {
        abnormals.splice(index, 1);
    }
}

function removePatientId(pid) {
    if (pid) {
        var i = patientIds.indexOf(pid);
        //console.log('i='+i+'patientIds='+patientIds);
        if (i != -1) {
            patientIds.splice(i, 1);
        }
        //console.log(patientIds);
    }
}

function removeEmptyPairFromPatientDocs() {
    var notUsedPid = new Array();
    for (var i = 0; i < patientIds.length; i++) {
        var pid = patientIds[i];
        var e = patientDocs[pid];

        if (!e) {
            notUsedPid.push(pid);
        } else if (e == null || e.length == 0) {
            delete patientDocs[pid];
        }
    }
    //console.log(notUsedPid);
    for (var i = 0; i < notUsedPid.length; i++) {
        removePatientId(notUsedPid[i]);//remove pid if it doesn't relate to any doclab
    }
}

function removeIdFromPatientDocs(doclabid) {
//	console.log('in removeidfrompatientdocs'+doclabid);
//console.log(patientIds);
//console.log(patientDocs);
    for (var i = 0; i < patientIds.length; i++) {
        var pid = patientIds[i];
        var a = patientDocs[pid];
        //console.log('a');
        //console.log(a);
        if (a && a.length > 0) {
            var f = a.indexOf(doclabid);
            //console.log('before splice');
            //console.log(patientDocs);
            if (f != -1) {
                a.splice(f, 1);
                patientDocs[pid] = a;
            }
            //console.log('after splice');
            //console.log(patientDocs);
        } else {
            delete patientDocs[pid];
            //console.log('after delete');
            //console.log(patientDocs);
        }
    }
//console.log('after remove');
//console.log(patientDocs);
}

function addIdToPatient(did, pid) {
    var a = patientDocs[pid];
    if (a && a != null) {
        a.push(did);
        patientDocs[pid] = a;
    } else {
        var ar = [did];
        patientDocs[pid] = ar;
    }
}

function addPatientId(pid) {
    patientIds.push(pid);
}

function addPatientIdName(pid, name) {
    var n = patientIdNames[pid];
    if (n || n == null) {
        patientIdNames[pid] = name;
    }

}

function sendMRP(ele) {
    var doclabid = ele.id;
    doclabid = doclabid.split('_')[1];
    var demofindEl = document.getElementById('demofind' + doclabid);
    var demoId = demofindEl ? demofindEl.value : '-1';
    if (demoId == '-1') {
        alert('Please enter a valid demographic');
        ele.checked = false;
    } else {
        if (confirm('Send to Most Responsible Provider?')) {
            var type = checkType(doclabid);
            var url = contextpath + "/oscarMDS/SendMRP.do";
            var data = 'demoId=' + demoId + '&docLabType=' + type + '&docLabId=' + doclabid;

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: data
            })
            .then(response => {
                if (response.ok) {
                    ele.disabled = true;
                    hideElement('mrp_fail_' + doclabid);
                } else {
                    throw new Error('Request failed');
                }
            })
            .catch(error => {
                ele.checked = false;
                showElement('mrp_fail_' + doclabid);
            });
        } else {
            ele.checked = false;
        }
    }
}

function rotate180(id) {
    jQuery("#rotate180btn_" + id).attr('disabled', 'disabled');
    var displayDocumentAsEl = document.getElementById('displayDocumentAs_' + id);
    var displayDocumentAs = displayDocumentAsEl ? displayDocumentAsEl.value : '';

    fetch(contextpath + "/documentManager/SplitDocument.do", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: "method=rotate180&document=" + id
    })
    .then(response => response.text())
    .then(data => {
        jQuery("#rotate180btn_" + id).removeAttr('disabled');
        if (displayDocumentAs == "PDF") {
            showPDF(id, contextpath);
        } else {
            jQuery("#docImg_" + id).attr('src', contextpath + "/documentManager/ManageDocument.do?method=viewDocPage&doc_no=" + id + "&curPage=1&rand=" + (new Date().getTime()));
        }
    })
    .catch(error => console.error('Error:', error));
}

function rotate90(id) {
    jQuery("#rotate90btn_" + id).attr('disabled', 'disabled');
    var displayDocumentAsEl = document.getElementById('displayDocumentAs_' + id);
    var displayDocumentAs = displayDocumentAsEl ? displayDocumentAsEl.value : '';

    fetch(contextpath + "/documentManager/SplitDocument.do", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: "method=rotate90&document=" + id
    })
    .then(response => response.text())
    .then(data => {
        jQuery("#rotate90btn_" + id).removeAttr('disabled');
        if (displayDocumentAs == "PDF") {
            showPDF(id, contextpath);
        } else {
            jQuery("#docImg_" + id).attr('src', contextpath + "/documentManager/ManageDocument.do?method=viewDocPage&doc_no=" + id + "&curPage=1&rand=" + (new Date().getTime()));
        }
    })
    .catch(error => console.error('Error:', error));
}

function removeFirstPage(id) {
    jQuery("#removeFirstPagebtn_" + id).attr('disabled', 'disabled');
    if (confirm("!! This is a destructive action that can cause loss of document data !! \n Click OK to delete the first page of this document, or Cancel to abort.")) {
        ShowSpin(true);
        var displayDocumentAsEl = document.getElementById('displayDocumentAs_' + id);
        var displayDocumentAs = displayDocumentAsEl ? displayDocumentAsEl.value : '';

        fetch(contextpath + "/documentManager/SplitDocument.do", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: "method=removeFirstPage&document=" + id
        })
        .then(response => response.text())
        .then(data => {
            if (displayDocumentAs == "PDF") {
                showPDF(id, contextpath);
            } else {
                jQuery("#docImg_" + id).attr('src', contextpath + "/documentManager/ManageDocument.do?method=viewDocPage&doc_no=" + id + "&curPage=1&rand=" + (new Date().getTime()));
            }
            var numPages = parseInt(jQuery("#numPages_" + id).text()) - 1;
            jQuery("#numPages_" + id).text("" + numPages);

            if (numPages <= 1) {
                jQuery("#numPages_" + id).removeClass("multiPage");
                jQuery("#removeFirstPagebtn_" + id).remove();
            }
            HideSpin();
            jQuery("#removeFirstPagebtn_" + id).removeAttr('disabled');
        })
        .catch(error => {
            console.error('Error:', error);
            HideSpin();
            jQuery("#removeFirstPagebtn_" + id).removeAttr('disabled');
        });
    }
}

function split(id) {
    var loc = contextpath + "/oscarMDS/Split.jsp?document=" + id;
    popupStart(1400, 1400, loc, "Splitter");
}

function hideTopBtn() {
    hideElement('topFRBtn');
    var topFBtn = document.getElementById('topFBtn');
    var topFileBtn = document.getElementById('topFileBtn');
    if (topFBtn && topFileBtn) {
        hideElement('topFBtn');
        hideElement('topFileBtn');
    }
}

function showTopBtn() {
    showElement('topFRBtn');
    var topFBtn = document.getElementById('topFBtn');
    var topFileBtn = document.getElementById('topFileBtn');
    if (topFBtn && topFileBtn) {
        showElement('topFBtn');
        showElement('topFileBtn');
    }
}

function popupStart(vheight, vwidth, varpage, windowname) {
    //console.log("in popupstart 4 args");
    //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
    if (!windowname)
        windowname = "helpwindow";
    //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
    var page = varpage;
    var windowprops = "height=" + vheight + ",width=" + vwidth + ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup = window.open(varpage, windowname, windowprops);
}

function reportWindow(page, height, width) {
    //console.log(page);
    var windowprops;
    if (height && width) {
        windowprops = "height=" + height + ", width=" + width + ", location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    } else {
        windowprops = "height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    }
    var popup = window.open(encodeURI(page), "labreport", windowprops);
    popup.focus();
}

function FileSelectedRows(files, searchProviderNo, status) {

    //TODO figure out this awkward HRM part when needed.

//	var hrmQueryMethod = "method=signOff";
//	var hrmQuery = "";
//	var labs = jQuery("input[name='flaggedLabs']:checked");
//	for (var i = 0; i < labs.length; i++) {
//	    if(labs[i].next().value == "HRM"){
//	        hrmQuery += "&signedOff=1&reportId=" + labs[i].value;
//        } else {
//            query += "&flaggedLabs=" + labs[i].value;
//            query += "&" + labs[i].next().name + "=" + labs[i].next().value;
//        }
//
//	}
//	if(!hrmQuery.empty()){
//        jQuery.ajax({
//            type: "POST",
//            url: ctx + "/hospitalReportManager/Modify.do",
//            data: hrmQueryMethod + hrmQuery,
//            success: function(data) {
//                updateCategoryList();
//
//                jQuery("input[name='flaggedLabs']:checked").each(function () {
//                    jQuery(this).parent().parent().remove();
//                });
//
//                fakeScroll();
//            }
//        });
//    }
    let filelabs = {"flaggedLabs": "{\"files\" : " + JSON.stringify(files) + "}"};
    let url = ctx + "/oscarMDS/FileLabs.do";
    bulkInboxAction(url, filelabs);
}

function submitFile(searchProviderNo, status) {
    let files = [];
    jQuery("input[name='flaggedLabs']:checkbox:checked").each(function (key, value) {
        files[key] = value.value;
    })
    if (files.length === 0) {
        alert("Select documents to be filed.");
        return;
    }
    console.log("File lab from: oscarMDSIndex.js Status: " + status + " Provider: " + searchProviderNo + " Files: " + files);
    FileSelectedRows(files, searchProviderNo, status);
}

function submitForward(searchProviderNo, status) {
    var files = [];
    jQuery("input[name='flaggedLabs']:checkbox:checked").each(function (key, value) {
        files[key] = value.value;
    })
    if (files.length === 0) {
        alert("Select documents to be forwarded");
        return;
    }

    ForwardSelectedRows(files, searchProviderNo, status);
}

function bulkInboxAction(url, filelabs) {

    jQuery.ajax({
        type: "POST",
        url: url,
        data: filelabs,
        success: function (data) {
            if (data.success) {
                let files = data.files;
                let file;
                let fileId;
                for (let i = 0; i < files.length; i++) {
                    // remove the filed lab from the DOM
                    file = files[i];
                    fileId = file.split(":")[0];
                    jQuery("#labdoc_" + fileId + " input[name='flaggedLabs']").attr("checked", false)

                    if (url.includes("FileLabs.do")) {
                        jQuery("#labdoc_" + fileId).remove();
                    }
                }

                jQuery("input[name='checkA']").attr("checked", false);

                if (jQuery("input[name='isListView']").length) {
                    updateCategoryList();
             } else if (jQuery("#btnViewMode").length) {
				fetchInboxhubData();
                } else {
                    location.reload();
                }

                jQuery("#dialog").dialog("close");
            }
        }
    });
}

function isRowShown(rowid) {
    var el = document.getElementById(rowid);
    if (el && el.style.display == 'none')
        return false;
    else
        return true;
}

function checkAllLabs(formId) {
    var val = document.getElementsByName("checkA")[0].checked;
    var labs = document.getElementsByName("flaggedLabs");
    for (var i = 0; i < labs.length; i++) {
        if (labs[i].disabled === false) {
            labs[i].checked = val;
        }
    }
}

function wrapUp() {
    if (opener.callRefreshTabAlerts) {
        opener.callRefreshTabAlerts("oscar_new_lab");
        setTimeout("window.close();", 100);
    } else {
        window.close();
    }
}

function showDocLab(childId, docNo, providerNo, searchProviderNo, status, demoName, showhide) {//showhide is 0 = document currently hidden, 1=currently shown
    //create child element in docViews
    docNo = docNo.replace(' ', '');//trim
    var type = checkType(docNo);
    //oscarLog('type'+type);
    //var div=childId;

    //var div=window.frames[0].document.getElementById(childId);
    var div = document.getElementById(childId);
    //alert(div);
    var url = '';
    if (type == 'DOC')
        url = "../documentManager/showDocument.jsp";
    else if (type == 'MDS')
        url = "";
    else if (type == 'HL7')
        url = "../lab/CA/ALL/labDisplayAjax.jsp";
    else if (type == 'CML')
        url = "";
    else
        url = "";

    docNo = docNo.replace('d', '');
    //oscarLog('url='+url);
    var data = "segmentID=" + docNo + "&providerNo=" + providerNo + "&searchProviderNo=" + searchProviderNo + "&status=" + status + "&demoName=" + demoName;
    //oscarLog('url='+url+'+-+ \n data='+data);

    fetch(url + '?' + data, {
        method: 'GET'
    })
    .then(response => response.text())
    .then(html => {
        if (div) {
            // Append to bottom of div
            div.insertAdjacentHTML('beforeend', html);
            // Execute any scripts in the returned HTML
            const parser = new DOMParser();
            const tempDoc = parser.parseFromString(html, 'text/html');
            const scripts = tempDoc.querySelectorAll('script');
            scripts.forEach(script => {
                const newScript = document.createElement('script');
                if (script.src) {
                    newScript.src = script.src;
                } else {
                    newScript.textContent = script.textContent;
                }
                document.head.appendChild(newScript).parentNode.removeChild(newScript);
            });
        }
        focusFirstDocLab();
    })
    .catch(error => console.error('Error loading document:', error));
}

function createNewElement(parent, child) {
    //oscarLog('11 create new leme');
    var newdiv = document.createElement('div');
    //oscarLog('22 after create new leme');
    newdiv.setAttribute('id', child);
    var parentdiv = document.getElementById(parent);
    parentdiv.appendChild(newdiv);
    //oscarLog('55 after create new leme');
}

function clearDocView() {
    var docview = document.getElementById('docViews');
    //var docview=window.frames[0].document.getElementById('docViews');
    if (docview) docview.textContent = '';
}

function showhideSubCat(plus_minus, patientId) {
    if (plus_minus == 'plus') {
        hideElement('plus' + patientId);
        showElement('minus' + patientId);
        showElement('labdoc' + patientId + 'showSublist');
    } else {
        hideElement('minus' + patientId);
        showElement('plus' + patientId);
        hideElement('labdoc' + patientId + 'showSublist');
    }
}

function un_bold(ele) {
    //oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
    if (ele == null || currentBold == ele.id) {
        ;
    } else {
        var currentBoldEl = document.getElementById(currentBold);
        if (currentBold && currentBoldEl != null) {
            currentBoldEl.style.fontWeight = '';
        }
        ele.style.fontWeight = 'bold';
        currentBold = ele.id;
    }
    //oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
}

function re_bold(id) {
    var el = document.getElementById(id);
    if (id && el != null) {
        el.style.fontWeight = 'bold';
    }
}

function showPageNumber(page) {
    var totalNoRowEl = document.getElementById('totalNumberRow');
    var totalNoRow = totalNoRowEl ? totalNoRowEl.value : 0;
    var newStartIndex = number_of_row_per_page * (parseInt(page) - 1);
    var newEndIndex = parseInt(newStartIndex) + 19;
    var isLastPage = false;
    if (newEndIndex > totalNoRow) {
        newEndIndex = totalNoRow;
        isLastPage = true;
    }
    //oscarLog("new start="+newStartIndex+";new end="+newEndIndex);
    for (var i = 0; i < totalNoRow; i++) {
        var rowEl = document.getElementById('row' + i);
        if (rowEl && parseInt(newStartIndex) <= i && i <= parseInt(newEndIndex)) {
            //oscarLog("show row-"+i);
            showElement(rowEl);
        } else if (rowEl) {
            //oscarLog("hide row-"+i);
            hideElement(rowEl);
        }
    }
    //update current page
    var currentPageNumEl = document.getElementById('currentPageNum');
    if (currentPageNumEl) currentPageNumEl.textContent = page;
    if (page == 1) {
        var msgPreviousEl = document.getElementById('msgPrevious');
        if (msgPreviousEl) hideElement(msgPreviousEl);
    } else if (page > 1) {
        var msgPreviousEl = document.getElementById('msgPrevious');
        if (msgPreviousEl) showElement(msgPreviousEl);
    }
    if (isLastPage) {
        var msgNextEl = document.getElementById('msgNext');
        if (msgNextEl) hideElement(msgNextEl);
    } else {
        var msgNextEl = document.getElementById('msgNext');
        if (msgNextEl) showElement(msgNextEl);
    }
}

function showTypePageNumber(page, type) {
    var eles;
    var numberPerPage = 20;
    if (type == 'D') {
        eles = document.getElementsByName('scannedDoc');
        var length = eles.length;
        var startindex = (parseInt(page) - 1) * numberPerPage;
        var endindex = startindex + numberPerPage - 1;
        if (endindex > length - 1) {
            endindex = length - 1;
        }
        //only display current page
        for (var i = startindex; i < endindex + 1; i++) {
            var ele = eles[i];
            ele.style.display = 'table-row';
        }
        //hide previous page
        for (var i = 0; i < startindex; i++) {
            var ele = eles[i];
            ele.style.display = 'none';
        }
        //hide later page
        for (var i = endindex; i < length; i++) {
            var ele = eles[i];
            ele.style.display = 'none';
        }
        //hide all labs
        eles = document.getElementsByName('HL7lab');
        for (var i = 0; i < eles.length; i++) {
            var ele = eles[i];
            ele.style.display = 'none';
        }
    } else if (type == 'H') {
        eles = document.getElementsByName('HL7lab');
        var length = eles.length;
        var startindex = (parseInt(page) - 1) * numberPerPage;
        var endindex = startindex + numberPerPage - 1;
        if (endindex > length - 1) {
            endindex = length - 1;
        }
        //only display current page
        for (var i = startindex; i < endindex + 1; i++) {
            var ele = eles[i];
            ele.style.display = 'table-row';
        }
        //hide previous page
        for (var i = 0; i < startindex; i++) {
            var ele = eles[i];
            ele.style.display = 'none';
        }
        //hide later page
        for (var i = endindex; i < length; i++) {
            var ele = eles[i];
            ele.style.display = 'none';
        }
        //hide all labs
        eles = document.getElementsByName('scannedDoc');
        for (var i = 0; i < eles.length; i++) {
            var ele = eles[i];
            ele.style.display = 'none';
        }
    } else if (type == 'N') {
        var eles1 = document.getElementsByClassName('NormalRes');
        var length = eles1.length;
        var startindex = (parseInt(page) - 1) * numberPerPage;
        var endindex = startindex + numberPerPage - 1;
        if (endindex > length - 1) {
            endindex = length - 1;
        }

        for (var i = startindex; i < endindex + 1; i++) {
            var ele = eles1[i];
            ele.style.display = 'table-row';
        }
        //hide previous page
        for (var i = 0; i < startindex; i++) {
            var ele = eles1[i];
            ele.style.display = 'none';
        }
        //hide later page
        for (var i = endindex; i < length; i++) {
            var ele = eles1[i];
            ele.style.display = 'none';
        }
        //hide all abnormals
        var eles2 = document.getElementsByClassName('AbnormalRes');
        for (var i = 0; i < eles2.length; i++) {
            var ele = eles2[i];
            ele.style.display = 'none';
        }
    } else if (type == 'AB') {
        var eles1 = document.getElementsByClassName('AbnormalRes');
        var length = eles1.length;
        var startindex = (parseInt(page) - 1) * numberPerPage;
        var endindex = startindex + numberPerPage - 1;
        if (endindex > length - 1) {
            endindex = length - 1;
        }
        for (var i = startindex; i < endindex + 1; i++) {
            var ele = eles1[i];
            ele.style.display = 'table-row';
        }
        //hide previous page
        for (var i = 0; i < startindex; i++) {
            var ele = eles1[i];
            ele.style.display = 'none';
        }
        //hide later page
        for (var i = endindex; i < length; i++) {
            var ele = eles1[i];
            ele.style.display = 'none';
        }
        //hide all normals
        var eles2 = document.getElementsByClassName('NormalRes');
        for (var i = 0; i < eles2.length; i++) {
            var ele = eles2[i];
            ele.style.display = 'none';
        }
    }
}

function setTotalRows() {
    var ds = document.getElementsByName('scannedDoc');
    var ls = document.getElementsByName('HL7lab');
    for (var i = 0; i < ds.length; i++) {
        var ele = ds[i];
        total_rows.push(ele.id);
    }
    for (var i = 0; i < ls.length; i++) {
        var ele = ls[i];
        total_rows.push(ele.id);
    }
    total_rows = sortRowId(uniqueArray(total_rows));
    current_category = new Array();
    current_category[0] = document.getElementsByName('scannedDoc');
    current_category[1] = document.getElementsByName('HL7lab');
    current_category[2] = document.getElementsByClassName('NormalRes');
    current_category[3] = document.getElementsByClassName('AbnormalRes');
}

function checkBox() {
    var view = "all";
    var documentCB = document.getElementById('documentCB');
    var hl7CB = document.getElementById('hl7CB');
    var normalCB = document.getElementById('normalCB');
    var abnormalCB = document.getElementById('abnormalCB');
    var unassignedCB = document.getElementById('unassignedCB');

    if (documentCB && documentCB.checked == 1) {
        view = "documents";
    } else if (hl7CB && hl7CB.checked == 1) {
        view = "labs";
    } else if (normalCB && normalCB.checked == 1) {
        checkedArray.push('normal');
        view = "normal";
    } else if (abnormalCB && abnormalCB.checked == 1) {
        view = "abnormal";
    } else if (unassignedCB && unassignedCB.checked == 1) {
        view = "unassigned";
    }
    window.location.search = replaceQueryString(window.location.search, "view", view);

}

function displayCategoryPage(page) {
    //oscarLog('in displaycategorypage, page='+page);
    //write all row ids to an array
    var displayrowids = new Array();
    for (var p = 0; p < current_category.length; p++) {
        var elements = new Array();
        elements = current_category[p];
        //oscarLog("elements.lenght="+elements.length);
        for (var j = 0; j < elements.length; j++) {
            var e = elements[j];
            var rowid = e.id;
            displayrowids.push(rowid);
        }
    }
    //make array unique
    displayrowids = uniqueArray(displayrowids);
    displayrowids = sortRowId(displayrowids);
    //oscarLog('sort and unique displaywords='+displayrowids);

    var numOfRows = displayrowids.length;
    //oscarLog(numOfRows);
    current_numberofpages = Math.ceil(numOfRows / number_of_row_per_page);
    //oscarLog(current_numberofpages);
    var startIndex = (parseInt(page) - 1) * number_of_row_per_page;
    var endIndex = startIndex + (number_of_row_per_page - 1);
    if (endIndex > displayrowids.length - 1) {
        endIndex = displayrowids.length - 1;
    }
    //set current displaying rows
    current_rows = new Array();
    for (var i = startIndex; i < endIndex + 1; i++) {
        if (document.getElementById(displayrowids[i])) {
            current_rows.push(displayrowids[i]);
        }
    }
    //loop through every thing,if it's in displayrowids, show it , if it's not hide it.
    for (var i = 0; i < total_rows.length; i++) {
        var rowid = total_rows[i];
        if (a_contain_b(current_rows, rowid)) {
            showElement(rowid);
        } else
            hideElement(rowid);
    }
}

function initializeNavigation() {
    var currentPageNumEl = document.getElementById('currentPageNum');
    if (currentPageNumEl) currentPageNumEl.textContent = 1;
    //update the page number shown and update previous and next words
    var msgNextEl = document.getElementById('msgNext');
    var msgPreviousEl = document.getElementById('msgPrevious');
    if (current_numberofpages > 1) {
        if (msgNextEl != null) showElement(msgNextEl);
        if (msgPreviousEl != null) hideElement(msgPreviousEl);
    } else if (current_numberofpages < 1) {
        if (msgNextEl != null) hideElement(msgNextEl);
        if (msgPreviousEl != null) hideElement(msgPreviousEl);
    } else if (current_numberofpages == 1) {
        if (msgNextEl != null) hideElement(msgNextEl);
        if (msgPreviousEl != null) hideElement(msgPreviousEl);
    }
    //oscarLog("current_numberofpages "+current_numberofpages);
    var currentIndividualPagesEl = document.getElementById('current_individual_pages');
    if (currentIndividualPagesEl != null) {
        currentIndividualPagesEl.textContent = "";
        if (current_numberofpages > 1) {
            for (var i = 1; i <= current_numberofpages; i++) {
                var link = document.createElement('a');
                link.style.textDecoration = 'none';
                link.href = 'javascript:void(0);';
                link.setAttribute('data-page', i);
                link.textContent = ' [ ' + i + ' ] ';
                link.addEventListener('click', function() {
                    navigatePage(parseInt(this.getAttribute('data-page')));
                });
                currentIndividualPagesEl.appendChild(link);
            }
        }
    }
}

function sortRowId(a) {
    var numArray = new Array();
    //sort array
    for (var i = 0; i < a.length; i++) {
        var id = a[i];
        var n = id.replace('row', '');
        numArray.push(parseInt(n));
    }
    numArray.sort(function (a, b) {
        return a - b;
    });
    a = new Array();
    for (var i = 0; i < numArray.length; i++) {
        a.push('row' + numArray[i]);
    }
    return a;
}

function a_contain_b(a, b) {//a is an array, b maybe an element in a.
    for (var i = 0; i < a.length; i++) {
        if (a[i] == b) {
            return true;
        }
    }
    return false;
}

function uniqueArray(a) {
    var r = new Array();
    o:for (var i = 0, n = a.length; i < n; i++) {
        for (var x = 0, y = r.length; x < y; x++) {
            if (r[x] == a[i]) continue o;
        }
        r[r.length] = a[i];
    }
    return r;
}

function navigatePage(p) {
    var currentPageNumEl = document.getElementById('currentPageNum');
    var pagenum = parseInt(currentPageNumEl ? currentPageNumEl.textContent : 1);
    if (p == 'Previous') {
        navigatePage(pagenum - 1);
    } else if (p == 'Next') {
        navigatePage(pagenum + 1);
    } else if (parseInt(p) > 0) {
        window.location.search = replaceQueryString(window.location.search, "page", parseInt(p));
    }
}

// TODO: Remove unused function.
function changeNavigationBar() {
    var currentPageNumEl = document.getElementById('currentPageNum');
    var pagenum = parseInt(currentPageNumEl ? currentPageNumEl.textContent : 1);
    var msgNextEl = document.getElementById('msgNext');
    var msgPreviousEl = document.getElementById('msgPrevious');
    if (current_numberofpages == 1) {
        hideElement(msgNextEl);
        hideElement(msgPreviousEl);
    } else if (current_numberofpages > 1 && current_numberofpages == pagenum) {
        hideElement(msgNextEl);
        showElement(msgPreviousEl);
    } else if (current_numberofpages > 1 && pagenum == 1) {
        showElement(msgNextEl);
        hideElement(msgPreviousEl);
    } else if (pagenum < current_numberofpages && pagenum > 1) {
        showElement(msgNextEl);
        showElement(msgPreviousEl);
    }
}

function syncCB(ele) {
    var id = ele.id;
    if (id == 'documentCB') {
        var cb2 = document.getElementById('documentCB2');
        if (cb2) cb2.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'documentCB2') {
        var cb = document.getElementById('documentCB');
        if (cb) cb.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'hl7CB') {
        var cb2 = document.getElementById('hl7CB2');
        if (cb2) cb2.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'hl7CB2') {
        var cb = document.getElementById('hl7CB');
        if (cb) cb.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'normalCB') {
        var cb2 = document.getElementById('normalCB2');
        if (cb2) cb2.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'normalCB2') {
        var cb = document.getElementById('normalCB');
        if (cb) cb.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'abnormalCB') {
        var cb2 = document.getElementById('abnormalCB2');
        if (cb2) cb2.checked = ele.checked == 1 ? 1 : 0;
    } else if (id == 'abnormalCB2') {
        var cb = document.getElementById('abnormalCB');
        if (cb) cb.checked = ele.checked == 1 ? 1 : 0;
    }
}

function showAb_Normal(ab_normal) {

    var ids = new Array();
    if (ab_normal == 'normal') {
        ids = normals;
    } else if (ab_normal == 'abnormal') {
        ids = abnormals;
    }
    var childId;
    if (ab_normal == 'normal') {
        childId = 'normals';
    } else if (ab_normal == 'abnormal') {
        childId = 'abnormals';
    }
    //oscarLog(childId);
    if (childId != null && childId.length > 0) {
        clearDocView();
        createNewElement('docViews', childId);
        for (var i = 0; i < ids.length; i++) {
            var docLabId = ids[i].replace(/\s/g, '');
            var ackStatus = getAckStatusFromDocLabId(docLabId);
            var patientId = getPatientIdFromDocLabId(docLabId);
            var patientName = getPatientNameFromPatientId(patientId);
            if (current_first_doclab == 0) current_first_doclab = docLabId;
            showDocLab(childId, docLabId, providerNo, searchProviderNo, ackStatus, patientName, ab_normal + 'show');
        }
    }
}

function showSubType(patientId, subType) {
    var labdocsArr = getLabDocFromPatientId(patientId);
    if (labdocsArr && labdocsArr != null) {
        var childId = 'subType' + subType + patientId;
        if (labdocsArr.length > 0) {
            //if(toggleElement(childId));
            // else{
            clearDocView();
            createNewElement('docViews', childId);
            for (var i = 0; i < labdocsArr.length; i++) {
                var labdoc = labdocsArr[i];
                labdoc = labdoc.replace(' ', '');
                //oscarLog('check type input='+labdoc);
                var type = checkType(labdoc);
                var ackStatus = getAckStatusFromDocLabId(labdoc);
                var patientName = getPatientNameFromPatientId(patientId);
                if (current_first_doclab == 0) current_first_doclab = labdoc;
                //oscarLog("type="+type+"--subType="+subType);
                if (type == subType)
                    showDocLab(childId, labdoc, providerNo, searchProviderNo, ackStatus, patientName, 'subtype' + subType + patientId + 'show');
                else ;
            }
            //toggleMarker('subtype'+subType+patientId+'show');
            //}
        }
    }
}

function getPatientNameFromPatientId(patientId) {
    var pn = patientIdNames[patientId];
    if (pn && pn != null) {
        return pn;
    } else {
        var url = contextpath + "/documentManager/ManageDocument.do";
        var data = 'method=getDemoNameAjax&demo_no=' + patientId;

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data
        })
        .then(response => response.json())
        .then(json => {
            if (json != null) {
                var pn = json.demoName;//get name from id
                addPatientIdName(patientId, pn);
                addPatientId(patientId);
                return pn;
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function getAckStatusFromDocLabId(docLabId) {
    return docStatus[docLabId];
}

function showAllDocLabs() {

    clearDocView();
    for (var i = 0; i < patientIds.length; i++) {
        var id = patientIds[i];
        //oscarLog("ids in showalldoclabs="+id);
        if (id.length > 0) {
            showThisPatientDocs(id, true);

        }
    }

}

function showCategory(cat) {
    if (cat.length > 0) {
        var sA = getDocLabFromCat(cat);
        if (sA && sA.length > 0) {
            //oscarLog("sA="+sA);
            var childId = "category" + cat;
            //if(toggleElement(childId));
            // else{
            clearDocView();
            createNewElement('docViews', childId);
            for (var i = 0; i < sA.length; i++) {
                var docLabId = sA[i];
                docLabId = docLabId.replace(/\s/g, "");
                //oscarLog("docLabId="+docLabId);
                var patientId = getPatientIdFromDocLabId(docLabId);
                //oscarLog("patientId="+patientId);
                var patientName = getPatientNameFromPatientId(patientId);
                var ackStatus = getAckStatusFromDocLabId(docLabId);
                //oscarLog("patientName="+patientName);
                //oscarLog("ackStatus="+ackStatus);

                if (patientName != null) {
                    if (current_first_doclab == 0) current_first_doclab = docLabId;
                    showDocLab(childId, docLabId, providerNo, searchProviderNo, ackStatus, patientName, cat + 'show');
                }
            }

        }
    }
}

function getPatientIdFromDocLabId(docLabId) {
    //console.log('in getpatientidfromdoclabid='+docLabId);
    //console.log(patientIds);
    //console.log(patientDocs);
    var notUsedPid = new Array();
    for (var i = 0; i < patientIds.length; i++) {

        var pid = patientIds[i];
        var e = patientDocs[pid];
        //console.log('e'+e);
        if (!e) {
            //console.log('if');
            notUsedPid.push(pid);
        } else {
            //console.log('in else='+docLabId);
            if (e.indexOf(docLabId) > -1) {
                return pid;
            }
        }
    }
    //console.log(notUsedPid);
    for (var i = 0; i < notUsedPid.length; i++) {

        removePatientId(notUsedPid[i]);
    }
}

function getLabDocFromPatientId(patientId) {//return array of doc ids and lab ids from patient id.
    //console.log(patientId+"--");
    //console.log(patientDocs);
    return patientDocs[patientId];
}

function showThisPatientDocs(patientId, keepPrevious) {
    //oscarLog("patientId in show this patientdocs="+patientId);
    var labDocsArr = getLabDocFromPatientId(patientId);
    var patientName = getPatientNameFromPatientId(patientId);
    if (patientName != null && patientName.length > 0 && labDocsArr != null && labDocsArr.length > 0) {
        //oscarLog(patientName);
        var childId = 'patient' + patientId;
        //if(toggleElement(childId));
        //else{
        if (keepPrevious) ;
        else clearDocView();
        createNewElement('docViews', childId);
        for (var i = 0; i < labDocsArr.length; i++) {
            var docId = labDocsArr[i].replace(' ', '');
            if (current_first_doclab == 0) current_first_doclab = docId;
            var ackStatus = getAckStatusFromDocLabId(docId);
            //oscarLog('childId='+childId+',docId='+docId+',ackStatus='+ackStatus);
            showDocLab(childId, docId, providerNo, searchProviderNo, ackStatus, patientName, 'labdoc' + patientId + 'show');
        }
    }
}

function popupConsultation(segmentId) {
    var page = contextpath + '/oscarEncounter/ViewRequest.do?segmentId=' + segmentId;
    var windowprops = "height=960,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
    var popup = window.open(page, msgConsReq, windowprops);
    if (popup != null) {
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
}

function checkType(docNo) {
    return docType[docNo];
}

function ForwardSelectedRows(files, searchProviderNo, status) {
    var isListView = jQuery("input[name=isListView]").val();
    var url = ctx + "/oscarMDS/SelectProvider.jsp";

    // not sure why this is a parameter, but, just in case...
    var data = {
        "isListView": isListView,
        "forwardList": files + ""
    };
    var dialogContainer = jQuery("#dialog");
    if (!dialogContainer.length) {
        dialogContainer = drawDialogContainer();
    }
    var dialog = dialogContainer.load(url, data).dialog({
        modal: true,
        width: 685,
        height: 355,
        draggable: false,
        title: "Forward Documents",
        buttons: {
            "Forward": function () {
                // workaround for JQuery bug with multiselect items that are not "selected"
                var fwdProviders = jQuery(this).find("select[multiple]#fwdProviders option").map(function (i, e) {
                    return jQuery(e).val();
                }).toArray();

                var fwdFavorites = jQuery(this).find("select[multiple]#favorites option").map(function (i, e) {
                    return jQuery(e).val();
                }).toArray();

                if (fwdProviders.length === 0) {
                    jQuery(this).find("select[multiple]#fwdProviders").addClass("input-error");
                    return;
                }

                forwardLabs(files, fwdProviders, fwdFavorites);
            },
            Cancel: function () {
                jQuery(this).dialog("close");
            }
        },
		open: function() {
			// Applies Bootstrap 5 card styles if Bootstrap is included; otherwise, it will render as a normal jQuery dialog box.
			styleDialogAsCard();
		},
        close: function () {
            jQuery(this).find("select[multiple]#fwdProviders").val('');
            jQuery(this).find("select[multiple]#fwdFavorites").val('');
        }
    }).dialog("open");
}

function drawDialogContainer() {
    var $div = jQuery('<div />').appendTo('body');
    $div.attr('id', 'dialog');
    return $div;
}

function styleDialogAsCard() {
	// Add Bootstrap card classes
	jQuery(".ui-dialog").addClass("card shadow-lg border-0 rounded");
	jQuery(".ui-dialog-titlebar").addClass("card-header bg-transparent mx-2 mt-3 border-0");
	jQuery(".ui-dialog-title").addClass("h5 mb-0 ms-2");
	jQuery(".ui-dialog-titlebar-close").addClass("btn-close");

	// Style the content area as card body
	jQuery(".ui-dialog-content").addClass("card-body mx-3 mt-3");

	// Style buttons like card footer
	jQuery(".ui-dialog-buttonpane").addClass("card-footer bg-light border-top-0 d-flex justify-content-end rounded");
	jQuery(".ui-dialog-buttonset button").addClass("btn btn-primary btn-sm");

	// Change 'Cancel' button to a secondary button
	jQuery(".ui-dialog-buttonset button:contains('Cancel')").removeClass("btn-primary").addClass("btn-secondary btn-sm");
}

function forwardLabs(files, providers, favorites) {
    var url = ctx + "/oscarMDS/ReportReassign.do";

        var filesArray = Array.isArray(files) ? files : [files];

        var filelabs = {
            "flaggedLabs": JSON.stringify({ "files": filesArray }),
            "selectedProviders": JSON.stringify({ "providers": providers }),
            "selectedFavorites": JSON.stringify({ "favorites": favorites }),
            "searchProviderNo": jQuery("input[name='searchProviderNo']").val(),
            "ajax": "yes"
        };
        bulkInboxAction(url, filelabs);
}

function updateDocLabData(doclabid) {//remove doclabid from global variables
    var doclabidNum = doclabid
    if (checkType(doclabid + "d") == "DOC") {
        doclabid += "d";
    }
    doclabid = doclabid.replace(/\s/g, '');
    updateSideNav(doclabid);
    hideRowUsingId(doclabidNum);
    removeIdFromTypeDocLab(doclabid);
    removeIdFromDocType(doclabid);
    removeIdFromPatientDocs(doclabid);
    removeEmptyPairFromPatientDocs();
    removeIdFromDocStatus(doclabid);
    removeNormal(doclabid);
    removeAbnormal(doclabid);
}

function checkAb_normal(doclabid) {
    if (normals.indexOf(doclabid) != -1)
        return 'normal';
    else if (abnormals.indexOf(doclabid) != -1)
        return 'abnormal';
}

function updateSideNav(doclabid) {
    //oscarLog('in updatesidenav');
    var totalNumDocsEl = document.getElementById('totalNumDocs');
    var n = totalNumDocsEl ? totalNumDocsEl.textContent : '0';
    n = parseInt(n);
    if (n > 0) {
        n = n - 1;
        if (totalNumDocsEl) totalNumDocsEl.textContent = n;
    }
    var type = checkType(doclabid);
    //oscarLog('type='+type);
    if (type == 'DOC') {
        var totalDocsNumEl = document.getElementById('totalDocsNum');
        n = totalDocsNumEl ? totalDocsNumEl.textContent : '0';
        //oscarLog('n='+n);
        n = parseInt(n);
        if (n > 0) {
            n = n - 1;
            if (totalDocsNumEl) totalDocsNumEl.textContent = n;
        }
    } else if (type == 'HL7') {
        var totalHL7NumEl = document.getElementById('totalHL7Num');
        n = totalHL7NumEl ? totalHL7NumEl.textContent : '0';
        n = parseInt(n);
        if (n > 0) {
            n = n - 1;
            if (totalHL7NumEl) totalHL7NumEl.textContent = n;
        }
    }
    var ab_normal = checkAb_normal(doclabid);
    //oscarLog('normal or abnormal?'+ab_normal);
    if (ab_normal == 'normal') {
        var normalNumEl = document.getElementById('normalNum');
        n = normalNumEl ? normalNumEl.textContent : '0';
        //oscarLog('normal inner='+n);
        n = parseInt(n);
        if (n > 0) {
            n = n - 1;
            if (normalNumEl) normalNumEl.textContent = n;
        }
    } else if (ab_normal == 'abnormal') {
        var abnormalNumEl = document.getElementById('abnormalNum');
        n = abnormalNumEl ? abnormalNumEl.textContent : '0';
        n = parseInt(n);
        if (n > 0) {
            n = n - 1;
            if (abnormalNumEl) abnormalNumEl.textContent = n;
        }
    }

    //update patient and patient's subtype
    var patientId = getPatientIdFromDocLabId(doclabid);
    //oscarLog('xx '+patientId+'--'+n);
    var patientNumDocsEl = document.getElementById('patientNumDocs' + patientId);
    n = patientNumDocsEl ? patientNumDocsEl.textContent : '0';
    //oscarLog('xx xx '+patientId+'--'+n);
    n = parseInt(n);
    if (n > 0) {
        if (patientNumDocsEl) patientNumDocsEl.textContent = n - 1;
    }

    if (type == 'DOC') {
        var pDocNumEl = document.getElementById('pDocNum_' + patientId);
        n = pDocNumEl ? pDocNumEl.textContent : '0';
        n = parseInt(n);
        if (n > 0) {
            if (pDocNumEl) pDocNumEl.textContent = n - 1;
        }
    } else if (type == 'HL7') {
        var pLabNumEl = document.getElementById('pLabNum_' + patientId);
        n = pLabNumEl ? pLabNumEl.textContent : '0';
        n = parseInt(n);
        if (n > 0) {
            if (pLabNumEl) pLabNumEl.textContent = n - 1;
        }
    }
}

function getRowIdFromDocLabId(doclabid) {
    var rowid;
    for (var i = 0; i < doclabid_seq.length; i++) {
        if (doclabid == doclabid_seq[i]) {
            rowid = 'row' + i;
            break;
        }
    }
    return rowid;
}

function hideRowUsingId(doclabid) {
    if (doclabid != null) {
        var rowid;
        doclabid = doclabid.replace(' ', '');
        rowid = getRowIdFromDocLabId(doclabid);
        var rowEl = document.getElementById(rowid);
        if (rowEl) rowEl.remove();
    }
}

function resetCurrentFirstDocLab() {
    current_first_doclab = 0;
}

function focusFirstDocLab() {
    if (current_first_doclab > 0) {
        var doc_lab = checkType(current_first_doclab);
        if (doc_lab == 'DOC') {
            //oscarLog('docDesc_'+current_first_doclab);
            var docDescEl = document.getElementById('docDesc_' + current_first_doclab);
            if (docDescEl) docDescEl.focus();
        } else if (doc_lab == 'HL7') {
            //do nothing
        }
    }
}

/***methos for showDocument.jsp***/
function updateGlobalDataAndSideNav(doclabid, patientId) {
    doclabid = doclabid.replace(/\s/g, '');

    if (doclabid.length > 0 && typeof patientDocs !== 'undefined') {
        //delete doclabid from not assigned list
        var na = patientDocs['-1'];
        var index = na.indexOf(doclabid);
        if (index !== -1) {
            na.splice(index, 1);
            addIdToPatient(doclabid, patientId);//add to patient
        }
        return true;
    }
}

function updatePatientDocLabNav(num, patientId) {
    //oscarLog(num+';;'+patientId);
    if (num && patientId) {
        var changed = false;
        var type = checkType(num);
        //oscarLog($('patient'+patientId+'all'));
        if (document.getElementById('patient' + patientId + 'all')) {
            //oscarLog('if');
            //case 1,patientName exists
            //check the type of doclab,
            //check if the type is present, if yes, increase by 1; if not, create and set to 1.

            if (type == 'DOC') {
                if (document.getElementById('patient' + patientId + 'docs')) {
                    increaseCount('pDocNum_' + patientId);
                    changed = true;
                } else {
                    var newEle = createNewDocEle(patientId);
                    //oscarLog($('labdoc'+patientId+'showSublist'));
                    var sublistEl = document.getElementById('labdoc' + patientId + 'showSublist');
                    if (sublistEl) sublistEl.insertAdjacentHTML('beforeend', newEle);
                    changed = true;
                }
            } else if (type == 'HL7') {
                if (document.getElementById('patient' + patientId + 'hl7s')) {
                    increaseCount('pLabNum_' + patientId);
                    changed = true;
                } else {
                    var newEle = createNewHL7Ele(patientId);
                    var sublistEl = document.getElementById('labdoc' + patientId + 'showSublist');
                    if (sublistEl) sublistEl.insertAdjacentHTML('beforeend', newEle);
                    changed = true;
                }
            }
            if (changed) {
                increaseCount('patientNumDocs' + patientId);
            }
        } else {
            //oscarLog('else');
            //case 2, patientname doesn't exists in nav bar at all
            //create patientname, check if labdoc is a lab or a doc.
            //create lab/doc nav
            var ele = createPatientDocLabEle(patientId, num);
            changed = true;
        }
        if (changed) {//decrease Not,Assigned by 1
            decreaseCount('patientNumDocs-1');
            if (type == 'DOC') {
                decreaseCount('pDocNum_-1');
            } else if (type == 'HL7') {
                decreaseCount('pLabNum_-1');
            }
            return true;
        }
    }
}

function createPatientDocLabEle(patientId, doclabid) {
    var url = ctx + "/documentManager/ManageDocument.do";
    var data = 'method=getDemoNameAjax&demo_no=' + patientId;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.json())
    .then(json => {
        //oscarLog(json);
        if (json != null) {
            var patientName = json.demoName;//get name from id
            addPatientId(patientId);
            addPatientIdName(patientId, patientName);
            var e = '<dt><img id="plus' + patientId + '" alt="plus" src="' + ctx + '/images/plus.png" onclick="showhideSubCat(\'plus\',\'' + patientId + '\');"/><img id="minus' + patientId + '" alt="minus" style="display:none;" src="' + ctx + '/images/minus.png" onclick="showhideSubCat(\'minus\',\'' + patientId + '\');"/>' +
            '<a id="patient' + patientId + 'all" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showThisPatientDocs(\'' + patientId + '\');un_bold(this);" title="' + patientName + '">' + patientName + ' (<span id="patientNumDocs' + patientId + '">1</span>)</a>' +
            '<dl id="labdoc' + patientId + 'showSublist" style="display:none">';
            var type = checkType(doclabid);
            var s;
            //oscarLog('type='+type);
            //oscarLog('eee='+e);
            if (type == 'DOC') {
                s = createNewDocEle(patientId);
            } else if (type == 'HL7') {
                s = createNewHL7Ele(patientId);
            } else {
                return '';
            }
            e += s;
            e += '</dl></dt>';
            //oscarLog('jjjjje='+e);
            //oscarLog('before return e');
            var patientsdoclabsEl = document.getElementById('patientsdoclabs');
            if (patientsdoclabsEl) patientsdoclabsEl.insertAdjacentHTML('beforeend', e);
            return e;
        }
    })
    .catch(error => console.error('Error:', error));

}

function createNewDocEle(patientId) {
    var newEle = '<dt><a id="patient' + patientId + 'docs" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showSubType(\'' + patientId + '\',\'DOC\');un_bold(this);" title="Documents">Documents(<span id="pDocNum_' + patientId + '">1</span>)</a></dt>';
    //oscarLog('newEle='+newEle);
    return newEle;
}

function createNewHL7Ele(patientId) {
    var newEle = '<dt><a id="patient' + patientId + 'hl7s" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showSubType(\'' + patientId + '\',\'HL7\');un_bold(this);" title="HL7s">HL7s(<span id="pLabNum_' + patientId + '">1</span>)</a></dt>';
    //oscarLog('newEle='+newEle);
    return newEle;
}

function increaseCount(eleId) {
    var el = document.getElementById(eleId);
    if (el) {
        var n = el.textContent;
        if (n.length > 0) {
            n = parseInt(n);
            n++;
            el.textContent = n;
        }
    }
}

function decreaseCount(eleId) {
    var el = document.getElementById(eleId);
    if (el) {
        var n = el.textContent;
        if (n.length > 0) {
            n = parseInt(n);
            if (n > 0) {
                n--;
            } else {
                n = 0;
            }
            el.textContent = n;
        }
    }
}

function updateDocumentAndNext(eleId) {//save doc info
    const url = "../documentManager/ManageDocument.do"
    const formEl = document.getElementById(eleId);
    const data = serializeForm(formEl);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.json())
    .then(json => {
        if (json != null) {
            const patientId = json.patientId;

            const ar = eleId.split("_");
            const num = ar[1].replace(/\s/g, '');

            showElement("saveSucessMsg_" + num);
            var savedEl = document.getElementById('saved' + num);
            if (savedEl) savedEl.value = 'true';

            var msgBtnEl = document.getElementById("msgBtn_" + num);
            if (msgBtnEl) {
                msgBtnEl.onclick = function () {
                    popup(700, 960, contextpath + '/messenger/SendDemoMessage.do?demographic_no=' + patientId, 'msg');
                };
            }

            updateDocStatusInQueue(num);

            if (typeof _in_window !== 'undefined' && _in_window) {
                if (typeof self.opener.removeReport !== 'undefined') {
                    self.opener.removeReport(num);
                }
                window.close();
            } else {
                //Hide document with slide up animation
                jQuery('#labdoc_' + num).slideUp();
                var success = updateGlobalDataAndSideNav(num, patientId);
                if (success) {
                    success = updatePatientDocLabNav(num, patientId);
                    if (success) {
                        //disable demo input
                        var autocompletedemoEl = document.getElementById('autocompletedemo' + num);
                        if (autocompletedemoEl) autocompletedemoEl.disabled = true;
                    }
                }
            }
        }
    })
    .catch(error => console.error('Error:', error));

    return false;
}

function updateDocument(eleId) {
    if (!checkObservationDate(eleId)) {
        return false;
    }

    //save doc info
    var url = "../documentManager/ManageDocument.do";
    var formEl = document.getElementById(eleId);
    var data = serializeForm(formEl);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.text())
    .then(responseText => {
        var ar = eleId.split("_");
        var num = ar[1].replace(/\s/g, '');

        var msg = document.getElementById("saveSucessMsg_" + num);
        if (msg) msg.style.display = "inline";

        var savedField = document.getElementById("saved" + num);
        if (savedField) savedField.value = "true";

        var success = false;
        var patientId = null;

        try {
            var json = JSON.parse(responseText);
            if (json && json.patientId) {
                patientId = json.patientId;

                var msgBtn = document.getElementById("msgBtn_" + num);
                if (msgBtn) {
                    msgBtn.onclick = function () {
                        popup(700, 960, contextpath + '/messenger/SendDemoMessage.do?demographic_no=' + patientId, 'msg');
                    };
                }

                if (typeof _in_window !== 'undefined' && _in_window) {
                    if (typeof self.opener.removeReport !== 'undefined') {
                        self.opener.removeReport(num);
                        success = true;
                    }
                } else {
                    success = updateGlobalDataAndSideNav(num, patientId);
                }

                if (success) {
                    success = updatePatientDocLabNav(num, patientId);
                    if (success) {
                        var ac = document.getElementById("autocompletedemo" + num);
                        if (ac) ac.disabled = true;
                    }
                }
            }
        } catch (e) {
            console.warn("Not JSON");
        }
    })
    .catch(error => console.error("Save failed:", error));

    return false;
}

function checkObservationDate(formid) {
    // regular expression to match required date format
    var re = /^\d{4}\-\d{1,2}\-\d{1,2}$/;
    var re2 = /^\d{4}\/\d{1,2}\/\d{1,2}$/;

    var form = document.getElementById(formid);
    if (form.elements["observationDate"].value == "") {
        alert("Blank Date: " + form.elements["observationDate"].value);
        form.elements["observationDate"].focus();
        return false;
    }

    if (!form.elements["observationDate"].value.match(re)) {
        if (!form.elements["observationDate"].value.match(re2)) {
            alert("Invalid date format: " + form.elements["observationDate"].value);
            form.elements["observationDate"].focus();
            return false;
        } else if (form.elements["observationDate"].value.match(re2)) {
            form.elements["observationDate"].value = form.elements["observationDate"].value.replace("/", "-");
            form.elements["observationDate"].value = form.elements["observationDate"].value.replace("/", "-");
        }
    }
    var regs = form.elements["observationDate"].value.split("-");
    // day value between 1 and 31
    if (regs[2] < 1 || regs[2] > 31) {
        alert("Invalid value for day: " + regs[2]);
        form.elements["observationDate"].focus();
        return false;
    }
    // month value between 1 and 12
    if (regs[1] < 1 || regs[1] > 12) {
        alert("Invalid value for month: " + regs[1]);
        form.elements["observationDate"].focus();
        return false;
    }
    // year value between 1902 and 2015
    if (regs[0] < 1902 || regs[0] > (new Date()).getFullYear()) {
        alert("Invalid value for year: " + regs[0] + " - must be between 1902 and " + (new Date()).getFullYear());
        form.elements["observationDate"].focus();
        return false;
    }
    return true;
}

function updateStatus(formid) {//acknowledge
    var num = formid.split("_");
    var doclabid = num[1];
    if (doclabid) {

        var demoId = "0";
        var saved = true
        if (jQuery('#demofind' + doclabid).length) {
            demoId = jQuery('#demofind' + doclabid).val();
        }
        if (jQuery('#saved' + doclabid).length) {
            saved = jQuery('#saved' + doclabid).val();
        }
        console.log("Update status for demoid: " + demoId + " doclabid: " + doclabid);
        console.log("Previously saved: " + saved);

        if (demoId === '-1' || !saved) {
            alert('Document is not assigned and saved to a patient,please file it');
        } else {
            var url = contextpath + "/oscarMDS/UpdateStatus.do";
            var formEl = document.getElementById(formid);
            var data = serializeFormToObject(formEl);
            console.log("Updating status. URL: " + url);
            console.log(data);

            jQuery.post(url, data).success(function () {
                updateDocStatusInQueue(doclabid);
				if (window.frameElement) {
					// Hide the parent <div> of the iframe only for new inbox previews loaded in an iframe
					jQuery(window.frameElement).closest('.document-card.card').slideUp();
				} else if (typeof _in_window !== 'undefined' && _in_window) {
                    if (typeof self.opener.removeReport !== 'undefined') {
						/**
						 * When a user acknowledges any lab version, it automatically files away older versions
						 * as well as the acknowledged version. This function removes those versions from the
						 * inbox results by calling the jQuery `removeReport` function on IDs up to and including
						 * the doclabid.
						 */
						const multiIds = data.multiID.split(",");
						for (const id of multiIds) {
							self.opener.removeReport(id);
							if (id === doclabid) break;
						}
                    }
                    window.close();
                } else {
                    //Hide document
                    jQuery('#labdoc_' + doclabid).slideUp();
                    updateGlobalDataAndSideNav(doclabid, null);
                }
            })
        }
    }
}

function fileDoc(docId) {
    if (docId) {
        docId = docId.replace(/\s/, '');
        if (docId.length > 0) {
            var demofindEl = document.getElementById('demofind' + docId);
            var demoId = demofindEl ? demofindEl.value : '-1';
            var isFile = true;
            if (demoId == '-1') {
                isFile = confirm('Document is not assigned to any patient, do you still want to file it?');
            }
            if (isFile) {
                var type = 'DOC';
                if (type) {
                    var url = '../oscarMDS/FileLabs.do';
                    var data = 'method=fileLabAjax&flaggedLabId=' + docId + '&labType=' + type;

                    fetch(url, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: data
                    })
                    .then(response => response.text())
                    .then(responseText => {
                        updateDocStatusInQueue(docId);

                        if (typeof _in_window !== 'undefined' && _in_window) {
                            if (typeof self.opener.removeReport !== 'undefined') {
                                self.opener.removeReport(docId);
                            }

                            window.close();
                        } else {
                            // Slide up animation using jQuery
                            jQuery('#labdoc_' + docId).slideUp();
                        }
                    })
                    .catch(error => console.error('Error:', error));
                }
            }
        }
    }
}

function handleQueueListChange(queueListSelectElement, refileBtnElement, docCurrentFiledQueue) {
	refileBtnElement.disabled = queueListSelectElement.value === docCurrentFiledQueue;
}

function refileDoc(id) {
    var queueListEl = document.getElementById('queueList_' + id);
    var queueId = queueListEl.options[queueListEl.selectedIndex].value;
    var url = contextpath + "/documentManager/ManageDocument.do";
    var data = 'method=refileDocumentAjax&documentId=' + id + "&queueId=" + queueId;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.text())
    .then(responseText => {
        fileDoc(id);
    })
    .catch(error => console.error('Error:', error));
}

function addDocToList(provNo, provName, docId) {
    var bdoc = document.createElement('a');
    bdoc.setAttribute("onclick", "removeProv(this);");
    bdoc.setAttribute("style", "cursor: pointer;");
    bdoc.appendChild(document.createTextNode(" -remove- "));
    //oscarLog("--");
    var adoc = document.createElement('div');
    adoc.appendChild(document.createTextNode(provName));
    //oscarLog("--==");
    var idoc = document.createElement('input');
    idoc.setAttribute("type", "hidden");
    idoc.setAttribute("name", "flagproviders");
    idoc.setAttribute("value", provNo);

    adoc.appendChild(idoc);

    adoc.appendChild(bdoc);
    var providerList = document.getElementById('providerList' + docId);
    if (providerList) providerList.appendChild(adoc);
}

function removeLink(docType, docId, providerNo, e) {
    var url = "../documentManager/ManageDocument.do";
    var data = 'method=removeLinkFromDocument&docType=' + docType + '&docId=' + docId + '&providerNo=' + providerNo;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.text())
    .then(responseText => {
        updateDocLabData(docId);
    })
    .catch(error => console.error('Error:', error));

    e.parentNode.remove(e);
}

function replaceQueryString(url, param, value) {
    var re = new RegExp("([?|&])" + param + "=.*?(&|$)", "i");
    if (url.match(re))
        return url.replace(re, '$1' + param + "=" + value + '$2');
    else
        return url + '&' + param + "=" + value;
}

var CATEGORY_ALL = 1,
    CATEGORY_DOCUMENTS = 2,
    CATEGORY_HL7 = 3,
    CATEGORY_NORMAL = 4,
    CATEGORY_ABNORMAL = 5,
    CATEGORY_PATIENT = 6,
    CATEGORY_PATIENT_SUB = 7,
    CATEGORY_TYPE_DOC = 'DOC',
    CATEGORY_TYPE_HL7 = 'HL7';

function reloadChangeView() {
    resetCurrentFirstDocLab();

    switch (selected_category) {
        case CATEGORY_ALL:
            showAllDocLabs();
            un_bold(document.getElementById('totalAll'))
            break;
        case CATEGORY_DOCUMENTS:
            showCategory('DOC');
            un_bold(document.getElementById('totalDocs'));
            break;
        case CATEGORY_HL7:
            showCategory('HL7');
            un_bold(document.getElementById('totalHL7s'));
            break;
        case CATEGORY_NORMAL:
            showAb_Normal('normal');
            un_bold(document.getElementById('totalNormals'));
            break;
        case CATEGORY_ABNORMAL:
            showAb_Normal('abnormal');
            un_bold(document.getElementById('totalAbnormals'));
            break;
        case CATEGORY_PATIENT:
            showThisPatientDocs(selected_category_patient);
            un_bold(document.getElementById('patient' + selected_category_patient + 'all'));
            break;
        case CATEGORY_PATIENT_SUB:
            showSubType(selected_category_patient, selected_category_type);
            showhideSubCat('plus', selected_category_patient);
            switch (selected_category_type) {
                case CATEGORY_TYPE_DOC:
                    un_bold(document.getElementById('patient' + selected_category_patient + 'docs'));
                    break;
                case CATEGORY_TYPE_HL7:
                    un_bold(document.getElementById('patient' + selected_category_patient + 'hl7s'));
                    break;
            }
            break;
    }
}

function inSummaryView() {
    var summaryViewEl = document.getElementById('summaryView');
    return summaryViewEl && summaryViewEl.style.display != 'none';
}

function refreshView() {
    if (inSummaryView()) {
        location.reload();
    } else {
        var cat = selected_category;
        var patId = selected_category_patient;
        var catType = selected_category_type;
        var preview = inSummaryView() ? "0" : "1";
        var search = location.search;
        search = replaceQueryString(search, "selectedCategory", cat);
        search = replaceQueryString(search, "selectedCategoryPatient", patId);
        search = replaceQueryString(search, "selectedCategoryType", catType);
        search = replaceQueryString(search, "inPreview", preview);
        location.search = search;
    }
}

function getWidth() {
    var myWidth = 0;
    if (typeof (window.innerWidth) == 'number') {
        //Non-IE
        myWidth = window.innerWidth;
    } else if (document.documentElement && document.documentElement.clientWidth) {
        //IE 6+ in 'standards compliant mode'
        myWidth = document.documentElement.clientWidth;
    } else if (document.body && document.body.clientHeight) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
    }
    return myWidth;
}


function getHeight() {
    var myHeight = 0;
    if (typeof (window.innerHeight) == 'number') {
        //Non-IE
        myHeight = window.innerHeight;
    } else if (document.documentElement && document.documentElement.clientHeight) {
        //IE 6+ in 'standards compliant mode'
        myHeight = document.documentElement.clientHeight;
    } else if (document.body && (document.body.clientHeight)) {
        //IE 4 compatible
        myHeight = document.body.clientHeight;
    }
    return myHeight;
}

function showPDF(docid, cp) {

    // var height=700;
    // if(getHeight()>750) {
    //     height=getHeight()-50;
    // }

    // var width=700;
    // if(getWidth()>1350)
    // {
    //     width=getWidth()-650;
    // }

    var url = cp + '/documentManager/ManageDocument.do?method=display&doc_no=' + encodeURIComponent(docid) + '&rand=' + Math.random() + '#view=fitV&page=1';

    var container = document.getElementById('docDispPDF_' + docid);
    if (container) {
        container.textContent = '';
        var pdfObject = document.createElement('object');
        pdfObject.style.width = '100%';
        pdfObject.style.height = '92vh';
        pdfObject.type = 'application/pdf';
        pdfObject.data = url;
        pdfObject.id = 'docPDF_' + docid;
        container.appendChild(pdfObject);
    }
}

function showPageImg(docid, pn, cp) {
    var displayDocumentAsEl = document.getElementById('displayDocumentAs_' + docid);
    var displayDocumentAs = displayDocumentAsEl ? displayDocumentAsEl.value : '';
    if (displayDocumentAs == "PDF") {
        showPDF(docid, cp);
    } else {
        if (docid && pn && cp) {
            var e = document.getElementById('docImg_' + docid);
            var url = cp + '/documentManager/ManageDocument.do?method=viewDocPage&doc_no=' + docid + '&curPage=' + pn;
            if (e) e.setAttribute('src', url);
        }
    }
}

function nextPage(docid, cp) {
    var curPageEl = document.getElementById('curPage_' + docid);
    var totalPageEl = document.getElementById('totalPage_' + docid);
    var curPage = curPageEl ? parseInt(curPageEl.value) : 1;
    var totalPage = totalPageEl ? parseInt(totalPageEl.value) : 1;
    curPage++;
    if (curPage > totalPage) {
        curPage = totalPage;
        hideNext(docid);
        showPrev(docid);
    }
    if (curPageEl) curPageEl.value = curPage;
    var viewedPageEl = document.getElementById('viewedPage_' + docid);
    if (viewedPageEl) viewedPageEl.textContent = curPage;

    showPageImg(docid, curPage, cp);
    if (curPage + 1 > totalPage) {
        hideNext(docid);
        showPrev(docid);
    } else {
        showNext(docid);
        showPrev(docid);
    }
}

function prevPage(docid, cp) {
    var curPageEl = document.getElementById('curPage_' + docid);
    var curPage = curPageEl ? parseInt(curPageEl.value) : 1;
    curPage--;
    if (curPage < 1) {
        curPage = 1;
        hidePrev(docid);
        showNext(docid);
    }
    if (curPageEl) curPageEl.value = curPage;
    var viewedPageEl = document.getElementById('viewedPage_' + docid);
    if (viewedPageEl) viewedPageEl.textContent = curPage;

    showPageImg(docid, curPage, cp);
    if (curPage == 1) {
        hidePrev(docid);
        showNext(docid);
    } else {
        showPrev(docid);
        showNext(docid);
    }

}

function firstPage(docid, cp) {
    var curPageEl = document.getElementById('curPage_' + docid);
    if (curPageEl) curPageEl.value = 1;
    var viewedPageEl = document.getElementById('viewedPage_' + docid);
    if (viewedPageEl) viewedPageEl.textContent = 1;
    showPageImg(docid, 1, cp);
    hidePrev(docid);
    showNext(docid);
}

function lastPage(docid, cp) {
    var totalPageEl = document.getElementById('totalPage_' + docid);
    var totalPage = totalPageEl ? parseInt(totalPageEl.value) : 1;

    var curPageEl = document.getElementById('curPage_' + docid);
    if (curPageEl) curPageEl.value = totalPage;
    var viewedPageEl = document.getElementById('viewedPage_' + docid);
    if (viewedPageEl) viewedPageEl.textContent = totalPage;
    showPageImg(docid, totalPage, cp);
    hideNext(docid);
    showPrev(docid);
}

function hidePrev(docid) {
    //disable previous link
    var prevP = document.getElementById("prevP_" + docid);
    var firstP = document.getElementById("firstP_" + docid);
    var prevP2 = document.getElementById("prevP2_" + docid);
    var firstP2 = document.getElementById("firstP2_" + docid);
    if (prevP) prevP.style.display = 'none';
    if (firstP) firstP.style.display = 'none';
    if (prevP2) prevP2.style.display = 'none';
    if (firstP2) firstP2.style.display = 'none';
}

function hideNext(docid) {
    //disable next link
    var nextP = document.getElementById("nextP_" + docid);
    var lastP = document.getElementById("lastP_" + docid);
    var nextP2 = document.getElementById("nextP2_" + docid);
    var lastP2 = document.getElementById("lastP2_" + docid);
    if (nextP) nextP.style.display = 'none';
    if (lastP) lastP.style.display = 'none';
    if (nextP2) nextP2.style.display = 'none';
    if (lastP2) lastP2.style.display = 'none';
}

function showPrev(docid) {
    //disable previous link
    var prevP = document.getElementById("prevP_" + docid);
    var firstP = document.getElementById("firstP_" + docid);
    var prevP2 = document.getElementById("prevP2_" + docid);
    var firstP2 = document.getElementById("firstP2_" + docid);
    if (prevP) prevP.style.display = 'inline';
    if (firstP) firstP.style.display = 'inline';
    if (prevP2) prevP2.style.display = 'inline';
    if (firstP2) firstP2.style.display = 'inline';
}

function showNext(docid) {
    //disable next link
    var nextP = document.getElementById("nextP_" + docid);
    var lastP = document.getElementById("lastP_" + docid);
    var nextP2 = document.getElementById("nextP2_" + docid);
    var lastP2 = document.getElementById("lastP2_" + docid);
    if (nextP) nextP.style.display = 'inline';
    if (lastP) lastP.style.display = 'inline';
    if (nextP2) nextP2.style.display = 'inline';
    if (lastP2) lastP2.style.display = 'inline';
}

function handleDocSave(docid, action) {
    var url = contextpath + "/documentManager/inboxManage.do";
    var data = 'method=isDocumentLinkedToDemographic&docId=' + docid;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => response.json())
    .then(json => {
        if (json != null) {
            var success = json.isLinkedToDemographic;
            var demoid = '';

            if (success) {
                if (action == 'addTickler') {
                    demoid = json.demoId;
                    if (demoid != null && demoid.length > 0)
                        popupStart(450, 600, contextpath + '/tickler/ForwardDemographicTickler.do?docType=DOC&docId=' + docid + '&demographic_no=' + demoid, 'tickler')
                }
            } else {
                alert("Make sure demographic is linked and document changes saved!");
            }
        }
    })
    .catch(error => console.error('Error:', error));
}


function addDocComment(docId, providerNo, sync) {

    var ret = true;
    var comment = "";
    var text = jQuery("#comment_" + docId + "_" + providerNo);
    if (text.length > 0) {
        comment = jQuery("#comment_" + docId + "_" + providerNo).html();
        if (comment == null || comment == "no comment") {
            comment = "";
        }
    }
    var commentVal = prompt("Please enter a comment (max. 255 characters)", comment);

    if (commentVal == null) {
        ret = false;
    } else if (commentVal != null && commentVal.length > 0)
        jQuery("#" + "comment_" + docId).val(commentVal);
    else
        jQuery("#" + "comment_" + docId).val(comment);

    if (ret) {
        var statusEl = document.getElementById("status_" + docId);
        if (statusEl) statusEl.value = 'N';
        var url = ctx + "/oscarMDS/UpdateStatus.do";
        var formid = "acknowledgeForm_" + docId;
        var data = serializeForm(formid);
        data += "&method=addComment";

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data
        })
        .then(response => response.json())
        .then(json => {
            if (json != null) {
                var date = json.date;
                var timestampEl = document.getElementById("timestamp_" + docId + "_" + providerNo);
                if (timestampEl) timestampEl.textContent = date;
            }
            var statusEl2 = document.getElementById("status_" + docId);
            if (statusEl2) statusEl2.value = "A";
            var commentDisplayEl = document.getElementById("comment_" + docId + "_" + providerNo);
            var commentInputEl = document.getElementById("comment_" + docId);
            if (commentDisplayEl && commentInputEl) {
                commentDisplayEl.textContent = commentInputEl.value;
                commentInputEl.value = "";
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function getDocComment(docId, providerNo, inQueueB) {

    var ret = true;
    var comment = "";
    var text = jQuery("#comment_" + docId + "_" + providerNo);
    if (text.length > 0) {
        comment = jQuery("#comment_" + docId + "_" + providerNo).html();
        if (comment == null || comment == "no comment") {
            comment = "";
        }
    }
    var commentVal = prompt("Please enter a comment (max. 255 characters)", comment);

    if (commentVal == null) {
        ret = false;
    } else if (commentVal != null && commentVal.length > 0)
        jQuery("#" + "comment_" + docId).val(commentVal);
    else
        jQuery("#" + "comment_" + docId).val(comment);

    if (ret) {
        updateStatus("acknowledgeForm_" + docId, inQueueB);
    }

}
