let QuillBlockEmbed = Quill.import('blots/block/embed');
class TemplateBlockPlaceholder extends QuillBlockEmbed {
    static blotName = 'TemplateBlockPlaceholder';
    static tagName = 'div';
    static className = 'quill-placeholder-block';
    
    static create(value) {
        let node = super.create(value);
        
        // Set node classes
        node.classList.add('quill-placeholder');
        node.classList.add('quill-placeholder-' + value.subject);
        node.setAttribute('data-subject', value.subject);
        node.setAttribute('data-marker', value.marker);
        node.setAttribute('data-title', value.title);
        node.setAttribute('title', value.title);
        node.setAttribute('contenteditable', false);

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
    'formats/TemplateBlockPlaceholder': TemplateBlockPlaceholder
});

