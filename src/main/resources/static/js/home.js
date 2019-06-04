var connectionInfos = {}; // 当前连接信息
$(function(){
    $("#connBtn").on("click", function(){
        var serializeArray = $("#connForm").serializeArray();
        for (var i = 0; i < serializeArray.length; i ++) {
            connectionInfos[serializeArray[i].name] =  serializeArray[i].value;
        }
        // console.log(connectionInfos);
        /**
         * 请求数据库表
         */
        $.ajax({
            url: "/queryTables",
            method: "GET",
            data: $("#connForm").serialize(),
            dataType: "json",
            success:function(resp) {
                // console.log(JSON.stringify(resp));
                loadTreeNodes(constructTreeNodes(resp));
            },
            error:function(e) {
                console.log(e);
            }
        });
        return false;
    });
});

/**
 * 组合树节点
 * @param tableInfos
 * @returns {{nodes: Array, text: Document.dbName}[]}
 */
function constructTreeNodes(tableInfos) {
    var tree = [{text: connectionInfos.dbName, nodes: []}];
    for (var i = 0; i < tableInfos.length; i ++) {
        tree[0].nodes.push({text: tableInfos[i]["tableComment"], tags:[tableInfos[i]["tableName"]]});
    }
    return tree;
}

/**
 * 加载树节点
 * @param data
 */
function loadTreeNodes(data) {
    $('#tree').treeview({
        data: data,    // 获取数据节点
        levels: 2,  //节点层级数
        expandIcon: 'glyphicon glyphicon-plus',
        collapseIcon: 'glyphicon glyphicon-minus',
        // nodeIcon: 'glyphicon glyphicon-send',
        emptyIcon: 'glyphicon glyphicon-leaf',
        color: "#337ab7",  //每一级通用的 节点字体颜色
        backColor: "#ddf5dd",   //每一级通用的 节点字背景色
        onhoverColor: "orange", //选中浮动颜色
        borderColor: "#d7d5d5", //设置组件的边框颜色; 设置showBorder为false，如果你不想要一个可见的边框
        showBorder: true,   // 是否现实边框
        showTags: true,     //是否在每个节点的右侧显示标签。 其值必须在每个节点的数据结构中提供

        highlightSelected: true,    //是否突出显示选定的节点
        selectedColor: false,   //设置选定节点的前景色
        selectedBackColor: false,   //设置选定节点的背景色
        showCheckbox: !0,   // 显示复选框
        multiSelect: !0,     // 支持多选
        onNodeChecked: function(event, node) {
            var childNodes = getChildNodeIdArr(node);
            if (childNodes) {
                $('#tree').treeview('checkNode', [childNodes, { silent: true }]);
            }
            var parentNode = $("#tree").treeview("getNode", node.parentId);
            setParentNodeCheck(parentNode);
        },
        onNodeUnchecked: function(event, node) {
            var childNodes = getChildNodeIdArr(node);
            if (childNodes) {
                $('#tree').treeview('uncheckNode', [childNodes, { silent: true }]);
            }
        }
    });
}

/**
 * 获取子节点ID
 * @param node
 * @returns {Array}
 */
function getChildNodeIdArr(node) {
    var ts = [];
    if (node.nodes) {
        for (var x in node.nodes) {
            ts.push(node.nodes[x].nodeId);
        }
    } else {
        ts.push(node.nodeId);
    }
    return ts;
}

/**
 * 设置父节点选中
 * @param node
 */
function setParentNodeCheck(node) {
    var parentNode = $("#tree").treeview("getNode", node.parentId);
    if (parentNode.nodes) {
        var checkedCount = 0;
        for (var x in parentNode.nodes){
            if (parentNode.nodes[x].state.checked) {
                checkedCount ++;
            } else {
                break;
            }
        }
        if (checkedCount === parentNode.nodes.length) {
            $("#tree").treeview("checkNode", parentNode.nodeId);
            setParentNodeCheck(parentNode);
        }
    }
}