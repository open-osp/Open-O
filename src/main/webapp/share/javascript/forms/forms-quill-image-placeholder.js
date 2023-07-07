let QuillImage = Quill.import('formats/image');
class ImagePlaceholder extends QuillImage {
    static create(value) {
        let node = super.create(value.data);
        node.setAttribute('alt', value.identifier);
        return node;
    }
    
    static value(node) {
        return {
            identifier: node.getAttribute('alt'),
            data: node.getAttribute('src')
        };
    }
}
ImagePlaceholder.blotName = 'ImagePlaceholder';
ImagePlaceholder.tagName = 'IMG';
Quill.register({
    'formats/ImagePlaceholder': ImagePlaceholder
});
