// configure Quill to use inline styles so the email's format properly
let DirectionAttribute = Quill.import('attributors/attribute/direction');
Quill.register(DirectionAttribute,true);

let AlignClass = Quill.import('attributors/class/align');
Quill.register(AlignClass,true);

let BackgroundClass = Quill.import('attributors/class/background');
Quill.register(BackgroundClass,true);

let ColorClass = Quill.import('attributors/class/color');
Quill.register(ColorClass,true);

let DirectionClass = Quill.import('attributors/class/direction');
Quill.register(DirectionClass,true);

let FontClass = Quill.import('attributors/class/font');
Quill.register(FontClass,true);

let SizeClass = Quill.import('attributors/class/size');
Quill.register(SizeClass,true);

let AlignStyle = Quill.import('attributors/style/align');
Quill.register(AlignStyle,true);

let BackgroundStyle = Quill.import('attributors/style/background');
Quill.register(BackgroundStyle,true);

let ColorStyle = Quill.import('attributors/style/color');
Quill.register(ColorStyle,true);

let DirectionStyle = Quill.import('attributors/style/direction');
Quill.register(DirectionStyle,true);

let FontStyle = Quill.import('attributors/style/font');
Quill.register(FontStyle,true);

let SizeStyle = Quill.import('attributors/style/size');
Quill.register(SizeStyle,true);