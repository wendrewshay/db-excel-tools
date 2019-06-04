$('#tree').treeview({
    data: getTree(),    // 获取数据节点
    levels: 1,  //节点层级数
    expandIcon: 'glyphicon glyphicon-plus',
    collapseIcon: 'glyphicon glyphicon-minus',
    // nodeIcon: 'glyphicon glyphicon-send',
    emptyIcon: 'glyphicon glyphicon-leaf',
    color: "#000",  //每一级通用的 节点字体颜色
    backColor: "#ddf5dd",   //每一级通用的 节点字背景色
    onhoverColor: "orange", //选中浮动颜色
    borderColor: "#d7d5d5", //设置组件的边框颜色; 设置showBorder为false，如果你不想要一个可见的边框
    showBorder: true,
    showTags: true, //是否在每个节点的右侧显示标签。 其值必须在每个节点的数据结构中提供

    highlightSelected: true,//是否突出显示选定的节点
    selectedColor: "#337AB7",//设置选定节点的前景色
    selectedBackColor: "darkorange",//设置选定节点的背景色
    showCheckbox: !0,   // 显示复选框
    multiSelect: !0 // 支持多选
});
function getTree() {
    var tree = [
        {
            text: "Parent 1",
            nodes: [
                {
                    text: "Child 1",
                    nodes: [
                        {
                            text: "Grandchild 1"
                        },
                        {
                            text: "Grandchild 2"
                        }
                    ]
                },
                {
                    text: "Child 2"
                }
            ]
        },
        {
            text: "Parent 2"
        },
        {
            text: "Parent 3"
        },
        {
            text: "Parent 4"
        },
        {
            text: "Parent 5"
        }
    ];
    return tree;
}