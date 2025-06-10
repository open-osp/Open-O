/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

function resetStash(ctx, myDrugRefEnabled) {
    const url = ctx + "/oscarRx/deleteRx.do?parameterValue=clearStash";
    const data = "";
    new Ajax.Request(url, {
        method: 'post', parameters: data, onSuccess: function (transport) {
            updateCurrentInteractions(myDrugRefEnabled);
        }
    });
    parent.document.getElementById('rxText').innerHTML = "";//make pending prescriptions disappear.
    parent.document.getElementById('searchString').focus();
}

function updateCurrentInteractions(myDrugRefEnabled) {
    new Ajax.Request("GetmyDrugrefInfo.do?method=findInteractingDrugList", {
        method: 'get', onSuccess: function (transport) {
            new Ajax.Request("UpdateInteractingDrugs.jsp", {
                method: 'get', onSuccess: function (transport) {
                    let str = transport.responseText;
                    str = str.replace('<script type="text/javascript">', '');
                    str = str.replace(/<\/script>/, '');
                    eval(str);
                    //oscarLog("str="+str);
                    if (myDrugRefEnabled) {
                        callReplacementWebService("GetmyDrugrefInfo.do?method=view", 'interactionsRxMyD');
                    }
                }
            });
        }
    });
}

function resetReRxDrugList(ctx) {
    const url = ctx + "/oscarRx/deleteRx.do?parameterValue=clearReRxDrugList";
    const data = "";
    new Ajax.Request(url, {
        method: 'post', parameters: data, onSuccess: function (transport) {
        }
    });
}

function setComment() {
    frames['preview'].document.getElementById('additNotes').innerHTML = '<%=Encode.forJavaScript(comment.replaceAll("\n", "<br>"))%>';
    frames['preview'].document.getElementsByName('additNotes')[0].value = frames['preview'].document.getElementById('additNotes').innerHTML;
}

function setDefaultAddr() {
    const url = "setDefaultAddr.jsp";
    const ran_number = Math.round(Math.random() * 1000000);
    const addr = encodeURIComponent(document.getElementById('addressSel').value);
    const params = "addr=" + addr + "&rand=" + ran_number;
    new Ajax.Request(url, {method: 'post', parameters: params});
}

function addNotes(scriptId) {
    const url = "AddRxComment.jsp";
    const ran_number = Math.round(Math.random() * 1000000);
    const comment = encodeURIComponent(document.getElementById('additionalNotes').value);
    const params = "scriptNo=" + scriptId + "&comment=" + comment + "&rand=" + ran_number;  //]
    new Ajax.Request(url, {method: 'post', parameters: params});
    frames['preview'].document.getElementById('additNotes').innerHTML = document.getElementById('additionalNotes').value.replace(/\n/g, "<br>");
    frames['preview'].document.getElementsByName('additNotes')[0].value = document.getElementById('additionalNotes').value.replace(/\n/g, "\r\n");
}

function printIframe() {
    const browserName = navigator.appName;
    if (browserName === "Microsoft Internet Explorer") {
        alert("Use of Microsoft Internet Explorer is not permitted")
    } else {
        if ('function' === typeof window.onbeforeunload) {
            window.onbeforeunload = null;
        }

        printDivContent('printableContent');

        self.onfocus = function () {
            self.setTimeout(function () {
                self.parent.close();
            }, 1000);
        };
        self.focus();
    }
}

function printDivContent(divId) {
    const content = document.getElementById(divId).innerHTML;

    const printFrame = document.createElement('iframe');
    printFrame.style.position = 'fixed';
    printFrame.style.right = '0';
    printFrame.style.bottom = '0';
    printFrame.style.width = '0';
    printFrame.style.height = '0';
    printFrame.style.border = '0';
    document.body.appendChild(printFrame);

    // Wait for iframe to load content, then print
    printFrame.onload = function () {
        printFrame.contentWindow.focus();
        printFrame.contentWindow.print();

        // Clean up
        setTimeout(() => {
            document.body.removeChild(printFrame);
        }, 1000);
    };

    // Write the content into the iframe
    const doc = printFrame.contentDocument || printFrame.contentWindow.document;
    doc.open();
    doc.write(`
  <html>
    <head>
      <title>Print Preview</title>
      <style>
        body { font-family: Arial, sans-serif; margin: 20px; border: 1px transparent; }

        table {
          width: 100%;
          border-collapse: collapse;
          page-break-inside: auto;
        }

        thead {
          display: table-header-group;
        }

        tbody {
          display: table-row-group;
        }

        tr {
          page-break-inside: avoid;
          page-break-after: auto;
        }

        th, td {
          padding: 8px;
          text-align: left;
        }
      </style>
    </head>
    <body>
      ${content}
    </body>
  </html>
`);
    doc.close();
}

function writeToEncounter(ctx, print, text, prefPharmacy, providerNo, demographicNo, curProviderNo, curDate, userName) {
    try {
        const url = ctx + "/oscarRx/WriteToEncounter.do";
        new Ajax.Request(url, {
            method: 'post',
            parameters: "prefPharmacy=" + encodeURIComponent(prefPharmacy) + "&additionalNotes=" + "&body=" + encodeURIComponent(text),
            onSuccess: function (ret) {
                //console.log("success")
                if (print) {
                    printIframe();
                }
                openEncounter(providerNo, demographicNo, curProviderNo, userName);
            },
            onError: function (e) {
                alert("ERROR: could not paste to EMR" + e);
                if (print) {
                    printIframe();
                }
                openEncounter(providerNo, demographicNo, curProviderNo, userName);
            }
        });
    } catch (e) {
        alert("ERROR: could not paste to EMR" + e);
    }
}

function openEncounter(providerNo, demographicNo, curProviderNo, userName) {
    const windowProps = "height=710,width=1024,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
    const currentDate = new Date().toISOString().substring(0, 10);
    const url = "../oscarEncounter/IncomingEncounter.do?providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&curProviderNo=" + curProviderNo + "&userName=" + userName + "&curDate=" + currentDate;

    if (window.parent.opener && window.parent.opener.document.forms["caseManagementEntryForm"] !== undefined) {
        // redirect if encounter window open
        window.parent.opener.location = url;
        return window.parent.opener;
    }

    return window.open(url, "encounter", windowProps);
}
function reducePreview() {
    parent.document.getElementById('lightwindow_container').style.width = "1000px";
    parent.document.getElementById('lightwindow_contents').style.width = "980px";
    document.getElementById('preview').style.width = "460px";
    frames['preview'].document.getElementById('pharmInfo').innerHTML = "";
    $("selectedPharmacy").innerHTML = "";
}

function toggleView(form) {
    var dxCode = (form.hsfo_Hypertension.checked ? 1 : 0) + (form.hsfo_Diabetes.checked ? 2 : 0) + (form.hsfo_Dyslipidemia.checked ? 4 : 0);
    // send dx code to HsfoPreview.jsp so that it will be displayed and persisted there
    document.getElementById("hsfoPop").style.display = "none";
    document.getElementById("bodyView").style.display = "block";
    document.getElementById("preview").src = "HsfoPreview.jsp?dxCode=" + dxCode;
}

function  ShowDrugInfo(drug) {
    window.open("drugInfo.do?GN=" + escape(drug), "_blank", "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
}

function refreshImage(imgURL, signatureImg) {
    if (document.getElementById("signature") != null) {
        document.getElementById("signature").src = imgURL;
    }
    document.getElementById('imgFile').value = signatureImg;
}

function unloadMess() {
    let mess = "Signature found, but fax has not been sent.";
    if (isSignatureDirty) {
        mess = "Signature changed, but not saved and fax not sent.";
    }
    return mess;
}

function setDigitalSignatureToRx(ctx, digitalSignatureId, scriptId) {
    fetch(ctx + '/oscarRx/saveDigitalSignature.do', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'method=saveDigitalSignature&digitalSignatureId=' + digitalSignatureId + '&scriptId=' + scriptId
    }).then(r => console.log(r));  // TODO (Chitrank Dave): handle response properly
}

function toggleFaxButtons(disabled) {
    document.getElementById("faxButton").disabled = disabled;
    document.getElementById("faxPasteButton").disabled = disabled;
}

function showFaxWarning() {
    if (typeof hasFaxNumber !== 'undefined' && !hasFaxNumber) {
        document.getElementById("faxWarningNote").style.display = "block";
    }
}

function signatureHandler(result, contextPath, scriptNo) {
    isSignatureDirty = result.isDirty;
    isSignatureSaved = result.isSave;
    result.target.onbeforeunload = null;

    // if (window.hasFaxNumber !== undefined) {
    //     let disabled = !hasFaxNumber || !result.isSave;
    //     toggleFaxButtons(disabled);
    // }

    if (result.isSave) {
        // if (window.hasFaxNumber && hasFaxNumber) {
        //     result.target.onbeforeunload = unloadMess;
        // }

        if (contextPath && scriptNo) {
            try {
                let signId = new URLSearchParams(result.storedImageUrl.split('?')[1]).get('digitalSignatureId');
                setDigitalSignatureToRx(contextPath, signId, scriptNo);
            } catch (e) {
                console.error(e);
            }
        }

        if (result.storedImageUrl) {
            refreshImage(result.storedImageUrl, result.previewImageUrl);
        }
    }
}

function closeRxPreviewBootstrapModal() {
    const modal = document.getElementById('rxPreviewBootstrapModal');
    let bsModal = bootstrap.Modal.getInstance(modal);
    if (bsModal) {
        bsModal.hide();
    }
}

function onPrint2(method, scriptId, useSC, scAddress) {
    const rxPageSize = $('printPageSize').value;
    console.log("rxPagesize  " + rxPageSize);

    let action = "../form/createcustomedpdf?__title=Rx&__method=" + method + "&useSC=" + useSC + "&scAddress=" + scAddress + "&rxPageSize=" + rxPageSize + "&scriptId=" + scriptId;
    document.getElementById("preview").contentWindow.document.getElementById("preview2Form").action = action;
    if (method !== "oscarRxFax") {
        document.getElementById("preview").contentWindow.document.getElementById("preview2Form").target = "_blank";
    }
    document.getElementById("preview").contentWindow.document.getElementById("preview2Form").submit();

    return true;
}

function sendFax(scriptId, signatureRequestId, useSC, scAddress) {
    if ('function' === typeof window.onbeforeunload) {
        window.onbeforeunload = null;
    }
    let faxNumber = document.getElementById('faxNumber');
    document.getElementById('finalFax').value = faxNumber.options[faxNumber.selectedIndex].value;
    document.getElementById('pdfId').value = signatureRequestId;

    onPrint2('oscarRxFax', scriptId, useSC, scAddress);
}
