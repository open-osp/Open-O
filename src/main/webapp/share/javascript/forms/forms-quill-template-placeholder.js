let QuillEmbed = Quill.import('blots/embed');
class TemplatePlaceholder extends QuillEmbed {
    static blotName = 'TemplatePlaceholder';
    static tagName = 'span';
    static className = 'quill-placeholder';
    
    static create(value) {
        let node = super.create(value);

        // Set node class
        node.classList.add('quill-placeholder-' + value.subject);
        node.setAttribute('data-subject', value.subject);
        node.setAttribute('data-marker', value.marker);
        node.setAttribute('data-title', value.title);
        node.setAttribute('title', value.title);

        node.innerHTML = value.title;

        return node;
    }
    
    static value(node) {
        return {
            subject: node.getAttribute('data-subject'),
            marker: node.getAttribute('data-marker'),
            title: node.getAttribute('data-title')
        };
    }
}
Quill.register({
    'formats/TemplatePlaceholder': TemplatePlaceholder
});

