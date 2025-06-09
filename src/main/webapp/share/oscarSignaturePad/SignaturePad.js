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

/**
 * @fileoverview Signature Pad implementation for OSCAR EMR
 * This file provides functionality for capturing and managing digital signatures
 * in the OSCAR Electronic Medical Record system.
 */

/**
 * Enum for signature event types
 * @readonly
 * @enum {string}
 */
const SignatureEventType = Object.freeze({
    /** Event triggered when the signature is cleared */
    CLEAR: "CLEAR",
    /** Event triggered when the signature is saved */
    SAVE: "SAVE",
    /** Event triggered when the signature is drawn */
    DRAW: "DRAW"
});

/**
 * Interface for handling signature pad events
 * Implement this interface to receive notifications about signature pad events
 */
class SignaturePadEventListener {
    /**
     * Called when a signature event occurs
     * @param {SignatureEventType} eventType - The type of event that occurred
     * @param {Object} result - The result object containing details about the signature event
     * @param {boolean} result.isSave - Whether the signature was saved
     * @param {boolean} result.isDirty - Whether the signature pad has unsaved changes
     * @param {string} result.requestIdKey - The request ID key
     * @param {string} result.previewImageUrl - URL for the preview image
     * @param {string} result.storedImageUrl - URL for the stored image
     * @param {Window} result.target - The parent window
     */
    onSignaturePadEvent(eventType, result) { }
}

/**
 * Configuration options for the signature pad
 * @typedef {Object} SignaturePadOptions
 * @property {string} [backgroundColor='white'] - Background color of the signature pad
 * @property {number} [dotSize=1.5] - Size of the drawing dot
 * @property {number} [minWidth=0.7] - Minimum width of the line
 * @property {number} [maxWidth=2] - Maximum width of the line
 * @property {string} [penColor='black'] - Color of the drawing pen
 */

/**
 * Digital Signature Pad implementation for OSCAR EMR
 * Provides functionality for capturing and managing digital signatures
 */
class DigitalSignaturePad {
    /**
     * Creates a new DigitalSignaturePad instance
     */
    constructor() {
        /**
         * Event handler for signature events
         * @type {SignaturePadEventListener|null}
         * @private
         */
        this._eventHandler = null;

        /**
         * The signature pad instance
         * @type {SignaturePad|null}
         * @private
         */
        this._signaturePad = null;

        /**
         * The canvas element
         * @type {HTMLCanvasElement|null}
         * @private
         */
        this._canvas = null;

        /**
         * Default options for the signature pad
         * @type {SignaturePadOptions}
         * @private
         */
        this._defaultOptions = {
            backgroundColor: 'white', dotSize: 1.5, minWidth: 0.7, maxWidth: 2, penColor: 'black'
        };
    }

    /**
     * Saves the signature canvas to the server
     * @param {string} contextPath - The context path for the server request
     * @param {string} requestIdKey - The request ID key
     * @param {string} imageUrl - URL for the preview image
     * @param {string} storedImageUrl - URL for the stored image
     * @returns {Promise<void>} A promise that resolves when the signature is saved
     * @throws {Error} If the canvas element is not found
     */
    async _saveSignature(contextPath, requestIdKey, imageUrl, storedImageUrl) {
        try {
            if (!this._canvas) {
                this._notifySignatureErrorEvent(new Error("Canvas element not found"));
                return;
            }

            const signatureImage = document.getElementById("signatureImage");
            if (!signatureImage) {
                this._notifySignatureErrorEvent(new Error("Signature image element not found"));
                return;
            }

            if (typeof jQuery === 'undefined') {
                this._notifySignatureErrorEvent(new Error("jQuery is not loaded"));
                return;
            }

            signatureImage.value = this._canvas.toDataURL("image/png");

            const response = await fetch(`${contextPath}/digitalSignature.do`, {
                method: "POST",
                body: new URLSearchParams(new FormData(document.getElementById("signatureForm"))).toString(),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }); // TODO (Chitrank Dave): show a loading indicator
            
            if (!response.ok) {
                this._notifySignatureErrorEvent(new Error(`Server responded with status: ${response.status}`));
                return;
            }

            const data = await response.json();
            if (!data || !data.signatureId) {
                this._notifySignatureErrorEvent(new Error("Invalid server response format"));
                return;
            }

            this._notifySignatureEvent({
                isSave: true,
                isDirty: false,
                savedId: data.signatureId,
                requestIdKey,
                previewImageUrl: imageUrl,
                storedImageUrl
            });
        } catch (error) {
            this._notifySignatureErrorEvent(error);
        }
    }
    /**
     * Initializes the signature pad with all necessary components
     * @param {SignaturePadEventListener|Function} eventListener - The event listener to handle signature events
     */
    initializeSignaturePad(eventListener) {
        this._setupEventHandler(eventListener);
        this._setupCanvas(document.getElementById("signatureCanvas"));
        this._createSignaturePad();
        this._setupCanvasResizing();
        this._setupSignatureEvents();
        this._setupClearButton();
    }

    /**
     * Sets up the event handler for signature events
     * @param {SignaturePadEventListener|Function} eventListener - The event listener to handle signature events
     * @private
     */
    _setupEventHandler(eventListener) {
        // Support both the new interface and legacy callback function
        if (eventListener && eventListener.onSignaturePadEvent && typeof eventListener.onSignaturePadEvent === 'function') {
            this._eventHandler = eventListener;
        } else {
            this._eventHandler = null;
        }
    }

    /**
     * Sets up the canvas for the signature pad
     * @param {HTMLCanvasElement} canvas - The canvas element to use for the signature pad
     * @throws {Error} If the canvas is not a valid HTML Canvas Element
     * @private
     */
    _setupCanvas(canvas) {
        if (!(canvas instanceof HTMLCanvasElement)) {
            throw new Error("Invalid canvas element provided");
        }
        this._canvas = canvas;
    }

    /**
     * Creates the signature pad with the specified options
     * @private
     */
    _createSignaturePad() {
        this._signaturePad = new SignaturePad(this._canvas, this._defaultOptions);
    }

    /**
     * Sets up canvas resizing to handle window resize events
     * @private
     */
    _setupCanvasResizing() {
        const resizeCanvas = () => {
            if (!this._canvas || !this._signaturePad) return;

            // Get the device pixel ratio
            const ratio = Math.max(window.devicePixelRatio || 1, 1);
            this._canvas.width = this._canvas.offsetWidth * ratio;
            this._canvas.height = this._canvas.offsetHeight * ratio;
            this._canvas.getContext("2d").scale(ratio, ratio);

            // Clear the signature pad to prevent false isEmpty() results
            this._signaturePad.clear();
        };

        window.addEventListener('resize', resizeCanvas);
        // Initial resize
        resizeCanvas();
    }

    /**
     * Sets up signature events to track changes to the signature
     * @private
     */
    _setupSignatureEvents() {
        if (!this._signaturePad) return;

        this._signaturePad.addEventListener('endStroke', () => {
            const signatureImage = document.getElementById("signatureImage");
            if (signatureImage) {
                signatureImage.value = this._signaturePad.toDataURL();
            }
            this._notifySignatureEvent({isSave: false, isDirty: true});
        });
    }

    /**
     * Sets up the clear button to clear the signature pad
     * @private
     */
    _setupClearButton() {
        const clearButton = document.getElementById("clear");
        if (clearButton) {
            clearButton.addEventListener('click', () => {
                if (this._signaturePad) {
                    this._signaturePad.clear();
                    this._notifySignatureEvent({isSave: false, isDirty: false});
                }
            });
        }
    }

    /**
     * Notifies an error event related to the signature with details about the error.
     *
     * @param {Error} error - The error object containing details about the signature-related issue.
     * @return {void} Does not return a value.
     */
    _notifySignatureErrorEvent(error) {
        console.error("Error saving signature:", error);
        // Notify of error through event
        this._notifySignatureEvent({
            isSave: false, isDirty: true, error: error.message
        });
    }

    /**
     * Notifies listeners of signature events
     * @param {Object} eventData - Data about the signature event
     * @param {boolean} [eventData.isSave=false] - Whether the signature was saved
     * @param {boolean} [eventData.isDirty=false] - Whether the signature pad has unsaved changes
     * @param {string} [eventData.savedId] - The ID of the saved signature
     * @param {string} [eventData.requestIdKey] - The request ID key
     * @param {string} [eventData.previewImageUrl] - URL for the preview image
     * @param {string} [eventData.storedImageUrl] - URL for the stored image
     * @param {string} [eventData.error] - Error message if an error occurred
     * @private
     */
    _notifySignatureEvent(eventData) {
        // Update UI elements based on event state
        const saveButton = document.getElementById("save");
        const clearButton = document.getElementById("clear");

        if (saveButton) {
            saveButton.disabled = !eventData.isDirty;
        }

        if (clearButton) {
            clearButton.disabled = !(eventData.isDirty || eventData.isSave);
        }

        // Determine the event type based on the event data
        let eventType = SignatureEventType.DRAW; // Default event type
        if (eventData.isSave) {
            eventType = SignatureEventType.SAVE;
        } else if (!eventData.isDirty && !eventData.isSave) {
            eventType = SignatureEventType.CLEAR;
        }

        // Notify event handler if available
        if (this._eventHandler) {
            const result = {
                target: parent,
                isSave: eventData.isSave || false,
                isDirty: eventData.isDirty || false,
                requestIdKey: eventData.requestIdKey,
                previewImageUrl: eventData.previewImageUrl,
                storedImageUrl: eventData.storedImageUrl ? `${eventData.storedImageUrl}${eventData.savedId || ''}` : undefined,
                error: eventData.error
            };

            // Use the interface method if available, otherwise call directly for backward compatibility
            if (typeof this._eventHandler.onSignaturePadEvent === 'function') {
                this._eventHandler.onSignaturePadEvent(eventType, result);
            } else if (typeof this._eventHandler === 'function') {
                this._eventHandler(eventType, result);
            }
        }
    }

    /**
     * Checks if the signature pad is empty
     * @returns {boolean} True if the signature pad is empty, false otherwise
     */
    isSignatureEmpty() {
        return this._signaturePad ? this._signaturePad.isEmpty() : true;
    }

    /**
     * Clears the signature pad
     */
    clearSignature() {
        if (this._signaturePad) {
            this._signaturePad.clear();
            this._notifySignatureEvent({isSave: false, isDirty: false});
        }
    }

    /**
     * Gets the signature as a data URL
     * @param {string} [type='image/png'] - The image type
     * @param {number} [encoderOptions=0.92] - The image quality (0-1)
     * @returns {string|null} The signature as a data URL, or null if the signature pad is not initialized
     */
    getSignatureDataUrl(type = 'image/png', encoderOptions = 0.92) {
        return this._signaturePad ? this._signaturePad.toDataURL(type, encoderOptions) : null;
    }
}

// Create a singleton instance
const oscarSignaturePad = new DigitalSignaturePad();
/**
 * Save the signature canvas
 * @param {string} contextPath - The context path for the server request
 * @param {string} requestIdKey - The request ID key
 * @param {string} imageUrl - URL for the image
 * @param {string} storedImageUrl - URL for the stored image
 */
function saveSignature(contextPath, requestIdKey, imageUrl, storedImageUrl) {
    oscarSignaturePad._saveSignature(contextPath, requestIdKey, imageUrl, storedImageUrl);
}

/**
 * Prepare the signature pad with a canvas and event listener
 * @param {HTMLCanvasElement} canvas - The canvas element to use for the signature pad
 * @param {SignaturePadEventListener|Function} signatureEventListener - The event listener to handle signature events
 *        Can be either a SignaturePadEventListener instance or a legacy callback function
 * @param {SignaturePadOptions} [options] - Optional configuration options for the signature pad
 */
function prepareSignaturePad(canvas, signatureEventListener, options) {
    oscarSignaturePad.initializeSignaturePad(signatureEventListener, options);
}
